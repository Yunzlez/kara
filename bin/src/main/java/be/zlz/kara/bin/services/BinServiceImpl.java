package be.zlz.kara.bin.services;

import be.zlz.kara.bin.domain.Bin;
import be.zlz.kara.bin.domain.Response;
import be.zlz.kara.bin.domain.enums.BinConfigKey;
import be.zlz.kara.bin.domain.Request;
import be.zlz.kara.bin.domain.enums.Interpretation;
import be.zlz.kara.bin.dto.*;
import be.zlz.kara.bin.dto.v11.ResponseOrigin;
import be.zlz.kara.bin.exceptions.ResourceNotFoundException;
import be.zlz.kara.bin.repositories.BinRepository;
import be.zlz.kara.bin.util.PagingUtils;
import be.zlz.kara.bin.util.ReplyBuilder;
import be.zlz.kara.bin.util.SizeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Deprecated
public class BinServiceImpl implements BinService {

    private final EventService eventService;

    private final BinRepository binRepository;

    private final RequestService requestService;

    public static final String NOT_FOUND_MESSAGE = "Could not find bin with name ";

    private static final Logger logger = LoggerFactory.getLogger(BinServiceImpl.class);

    @Value("${mqtt.broker.url}")
    private static String mqttUrl;

    @Autowired
    public BinServiceImpl(EventService eventService, BinRepository binRepository, RequestService requestService) {
        this.eventService = eventService;
        this.binRepository = binRepository;
        this.requestService = requestService;
    }

    @Override
    public Bin getByName(String name) {
        return binRepository.getByName(name);
    }

    @Override
    public Bin save(Bin bin) {
        return binRepository.save(bin);
    }

    @Override
    public List<BinListDto> listBins(int page, int limit) {
        Page<Bin> bins = binRepository.findAll(PagingUtils.getPageable(page, limit));
        return toBinListDto(bins);
    }

    private List<BinListDto> toBinListDto(Page<Bin> bins) {
        return bins.stream().map(bin -> new BinListDto(
                bin.getName(),
                new RequestCountDto(bin.getRequestCount(), bin.getRequestMetric().getCounts())
        )).collect(Collectors.toList());
    }

    @Override
    public BinDto getPagedBinDto(Bin bin, String requestUrl, int page, int limit) {
        Page<Request> requests = requestService.getOrderedRequests(bin, page, limit);
        return new BinDto(
                bin.getName(),
                new RequestCountDto((int) requests.getTotalElements(), bin.getRequestMetric().getCounts()),
                new InboundDto(requestUrl, mqttUrl, "/bin/" + bin.getName()), //todo make variable MQTT url
                requests.getContent(),
                page,
                limit,
                requests.getTotalPages(),
                getSize(bin)
        );
    }

    @Override
    public void deleteBin(Bin bin) {
        eventService.deleteEventsForBin(bin);
        binRepository.delete(bin);
    }

    @Override
    public String getSize(Bin bin) {
        Long val = binRepository.getBinSizeInBytes(bin.getId());
        if (val == null) {
            val = 0L;
        }
        return SizeUtil.autoScale(val);
    }

    @Override
    @Transactional
    public String updateSettings(String name, SettingViewModel settings) {
        Map<String, String> headers = new HashMap<>();
        if (settings.getHeaderNames() != null) {
            for (int i = 0; i < settings.getHeaderNames().size(); i++) {
                headers.put(settings.getHeaderNames().get(i), settings.getHeaderValues().get(i));
            }
        }
        return updateSettings(name, new BinSettingsDto(
                settings.getCode(),
                settings.getMimeType(),
                settings.getBody(),
                headers,
                new HashMap<>(), //todo remove
                settings.getCustomName(),
                null, //todo
                settings.isPermanent()
        ));
    }

    @Override
    @Transactional
    public String updateSettings(String name, BinSettingsDto settings) {
        Bin bin = binRepository.getByName(name);

        createReply(bin, settings);

        String redirect = name;
        if (settings.getCustomName() != null) {
            bin.setName(settings.getCustomName());
            redirect = settings.getCustomName();
        }
        if (!bin.isPermanent() && settings.isPermanent()) {
            clearBin(name);
        }
        bin.addConfigEntry(BinConfigKey.PERMANENT_KEY, settings.isPermanent());
        bin.addConfigEntries(settings.getConfig());

        binRepository.save(bin);
        return redirect;
    }

    @Override
    @Transactional
    public void clearBin(String uuid) {
        Bin bin = binRepository.getByName(uuid);
        if (bin != null) {
            bin.setRequestCount(0);
            bin.getRequestMetric().getCounts().clear();
            eventService.deleteEventsForBin(bin);
            binRepository.save(bin);
        }
    }

    @Override
    public SettingViewModel getSettings(String name) {
        Bin bin = binRepository.getByName(name);
        if (bin == null) {
            throw new ResourceNotFoundException(NOT_FOUND_MESSAGE + name);
        }
        SettingViewModel settings;
        if (bin.getReply() != null) {
            settings = new SettingViewModel(bin.getReply());
        } else {
            settings = new SettingViewModel();
        }
        settings.setCustomName(bin.getName());
        settings.setPermanent(bin.isPermanent());
        return settings;
    }

    @Override
    public BinSettingsDto getApiBinSettings(String name) {
        Bin bin = binRepository.getByName(name);
        if (bin == null) {
            throw new ResourceNotFoundException(NOT_FOUND_MESSAGE + name);
        }
        return new BinSettingsDto(bin);
    }

    @Override
    public String buildRequestUrl(HttpServletRequest request, String uuid) {
        UriBuilder builder = new DefaultUriBuilderFactory().builder()
                .scheme(request.getScheme())
                .host(request.getServerName());

        if (request.getServerPort() != 80 && request.getServerPort() != 443) {
            builder.port(request.getServerPort());
        }
        builder.path("/bin/" + uuid);
        return builder.build().toASCIIString();
    }

    private boolean isBlank(String val) {
        return val == null || val.isEmpty();
    }

    private void createReply(Bin bin, BinSettingsDto settings) {
        if (settings.getCode() == null && settings.getMimeType() == null && settings.getBody() == null && settings.getHeaders() == null) {
            return;
        }
        ReplyBuilder replyBuilder = new ReplyBuilder()
                .setCode(HttpStatus.valueOf(settings.getCode() == null ? 200 : settings.getCode()))
                .setBody(settings.getBody() == null ? "" : settings.getBody())
                .setMimeType(isBlank(settings.getMimeType()) ? "text/plain" : settings.getMimeType())
                .setCustom(true);

        if (settings.getHeaders() != null) {
            replyBuilder.addAllHeaders(settings.getHeaders());
        }

        bin.setResponse(new Response(
                HttpStatus.valueOf(settings.getCode() == null ? 200 : settings.getCode()),
                isBlank(settings.getMimeType()) ? "text/plain" : settings.getMimeType(),
                settings.getBody() == null ? null : settings.getBody().getBytes(),
                settings.getHeaders() != null ? settings.getHeaders() : Collections.emptyMap(),
                Interpretation.TEXT,
                ResponseOrigin.CUSTOM
        ));
    }
}
