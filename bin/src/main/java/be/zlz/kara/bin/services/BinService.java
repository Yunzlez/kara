package be.zlz.kara.bin.services;

import be.zlz.kara.bin.domain.Bin;
import be.zlz.kara.bin.domain.Request;
import be.zlz.kara.bin.dto.SettingDTO;
import be.zlz.kara.bin.exceptions.ResourceNotFoundException;
import be.zlz.kara.bin.repositories.BinRepository;
import be.zlz.kara.bin.repositories.RequestRepository;
import be.zlz.kara.bin.util.ReplyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BinService {

    private final RequestRepository requestRepository;

    private final BinRepository binRepository;

    public static final String NOT_FOUND_MESSAGE = "Could not find bin with name ";

    @Value("${max.page.size}")
    private int maxPageSize;

    @Autowired
    public BinService(RequestRepository requestRepository, BinRepository binRepository) {
        this.requestRepository = requestRepository;
        this.binRepository = binRepository;
    }

    public Page<Request> getOrderedRequests(String name, int page, int limit) {
        return requestRepository.getByBinOrderByRequestTimeDesc(binRepository.getByName(name), getPageable(page, limit));
    }

    public void deleteBin(Bin bin) {
        long id = bin.getId();
        requestRepository.deleteAllByBinEfficient(bin.getId());
        binRepository.delete(bin);
    }

    public String updateSettings(String name, SettingDTO settings) {
        Bin bin = binRepository.getByName(name);
        ReplyBuilder replyBuilder = new ReplyBuilder();
        if (!(settings.getCode() == null || settings.getMimeType() == null || settings.getBody() == null)) {
            bin.setReply(
                    replyBuilder.setCode(HttpStatus.valueOf(settings.getCode()))
                            .setMimeType(settings.getMimeType())
                            .setBody(settings.getBody())
                            .setCustom(true)
                            .build()
            );
        }
        String redirect = name;
        if (settings.getCustomName() != null) {
            bin.setName(settings.getCustomName());
            redirect = settings.getCustomName();
        }
        if (!bin.isPermanent() && settings.isPermanent()) {
            clearBin(name);
        }
        bin.setPermanent(settings.isPermanent());

        binRepository.save(bin);
        return redirect;
    }

    @Transactional
    public void clearBin(String uuid) {
        Bin bin = binRepository.getByName(uuid);
        if (bin != null) {
            bin.setRequestCount(0);
            bin.getRequestMetric().getCounts().clear();
            deleteBinRequestsEfficient(bin.getId());
            binRepository.save(bin);
        }
    }

    private void deleteBinRequestsEfficient(long binId){
        requestRepository.deleteHeadersForBin(binId);
        requestRepository.deleteQueryParamsForBin(binId);
        requestRepository.deleteAllByBinEfficient(binId);
    }

    private Pageable getPageable(Integer page, Integer size) {
        if (size == null || size > maxPageSize || size == 0) {
            size = maxPageSize;
        }
        if (page == null) {
            page = 0;
        }
        return PageRequest.of(page, size);
    }

    public SettingDTO getSettings(String name) {
        Bin bin = binRepository.getByName(name);
        if (bin == null) {
            throw new ResourceNotFoundException(NOT_FOUND_MESSAGE + name);
        }
        SettingDTO settings;
        if (bin.getReply() != null) {
            settings = new SettingDTO(bin.getReply());
        } else {
            settings = new SettingDTO();
        }
        settings.setCustomName(bin.getName());
        settings.setPermanent(bin.isPermanent());
        return settings;
    }
}