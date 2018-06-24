package be.zlz.kara.bin.controller;

import be.zlz.kara.bin.domain.Bin;
import be.zlz.kara.bin.domain.Request;
import be.zlz.kara.bin.domain.RequestMetric;
import be.zlz.kara.bin.dto.BinDto;
import be.zlz.kara.bin.dto.SettingDTO;
import be.zlz.kara.bin.exceptions.ResourceNotFoundException;
import be.zlz.kara.bin.repositories.BinRepository;
import be.zlz.kara.bin.repositories.RequestRepository;
import be.zlz.kara.bin.services.BinService;
import be.zlz.kara.bin.services.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static be.zlz.kara.bin.services.BinService.NOT_FOUND_MESSAGE;

@Controller
public class BinController {

    private final BinRepository binRepository;

    private final RequestRepository requestRepository;

    private final BinService binService;

    private final RequestService requestService;

    private Logger logger;

    @Autowired
    public BinController(BinRepository binRepository, RequestRepository requestRepository, BinService binService, RequestService requestService) {
        logger = LoggerFactory.getLogger(this.getClass());
        this.binRepository = binRepository;
        this.requestRepository = requestRepository;
        this.binService = binService;
        this.requestService = requestService;
    }

    @GetMapping("/bin/create")
    public String createBin() {
        String name = UUID.randomUUID().toString();

        Bin bin = new Bin(name);
        binRepository.save(bin);
        logger.info("created bin with UUID {}", name);

        return "redirect:/bin/" + name + "/log";
    }

    @GetMapping(value = "/bin/{uuid}/log", produces = "application/json")
    @ResponseBody
    public BinDto getLogForUuidAsJson(@PathVariable String uuid,
                                      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                      @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                      HttpServletRequest request) {
        Bin bin = binRepository.getByName(uuid);
        if (bin == null) {
            throw new ResourceNotFoundException("Could not find bin with name " + uuid);
        }

        return binService.getPagedBinDto(bin, binService.buildRequestUrl(request, uuid), page, limit);
    }

    @GetMapping(value = "/bin/{uuid}/log", produces = "text/html")
    public String getLogForUuidAsPage(@PathVariable String uuid,
                                      Map<String, Object> model,
                                      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                      @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                      HttpServletRequest request) {
        Bin bin = binRepository.getByName(uuid);
        if (bin == null) {
            throw new ResourceNotFoundException(NOT_FOUND_MESSAGE + uuid);
        }
        model.put("pageTitle", "Bin " + uuid);
        model.put("binName", uuid);

        Page<Request> current = requestService.getOrderedRequests(bin, page, limit);
        List<Request> requests = current.getContent();

        model.put("requests", requests);
        model.put("requestCount", current.getTotalElements());
        model.put("pageCount", current.getTotalPages());
        model.put("currentPage", current);
        model.put("currentLimit", limit);
        model.put("requestUrl", binService.buildRequestUrl(request, uuid));
        model.put("binSize", binService.getSize(bin));
        setRequestCounts(bin, model);

        return "requestlog";
    }

    @GetMapping(value = "/bin/{uuid}/log/settings", produces = "application/json")
    public String getBinSetup(@PathVariable String uuid, Map<String, Object> model) {
        model.put("pageTitle", "Bin " + uuid + " settings");
        model.put("binName", uuid);
        model.put("reply", binService.getSettings(uuid));

        return "settings";
    }

    @PostMapping(value = "/bin/{uuid}/log/settings", produces = "application/json")
    public String saveBinSetup(@PathVariable String uuid, @ModelAttribute SettingDTO settings, BindingResult bindingResult) {
        String newName = binService.updateSettings(uuid, settings);
        return "redirect:/bin/" + newName + "/log";
    }

    @GetMapping(value = "/bin/{uuid}/log/charts")
    @ResponseBody
    public String createCharts(@PathVariable String uuid, Map<String, Object> model) {
        //todo implement
        return "redirect:/";
    }

    @GetMapping(value = "/bin/{uuid}/delete")
    public String deleteBin(@PathVariable String uuid) {
        binService.clearBin(uuid);
        return "redirect:/bin/" + uuid + "/log";
    }

    private void setRequestCounts(Bin bin, Map<String, Object> model) {
        RequestMetric metric = bin.getRequestMetric();
        if (metric == null) {
            doMigration(bin);
            logger.info("doing migration for {}", bin.getName());
            metric = bin.getRequestMetric();
        }

        model.put("getCount", metric.getCounts().getOrDefault(HttpMethod.GET.name(), 0));
        model.put("postCount", metric.getCounts().getOrDefault(HttpMethod.POST.name(), 0));
        model.put("patchCount", metric.getCounts().getOrDefault(HttpMethod.PATCH.name(), 0));
        model.put("deleteCount", metric.getCounts().getOrDefault(HttpMethod.DELETE.name(), 0));
        model.put("putCount", metric.getCounts().getOrDefault(HttpMethod.PUT.name(), 0));
        model.put("mqttCount", metric.getCounts().getOrDefault("MQTT", 0));
    }

    @Deprecated
    private void doMigration(Bin bin) {
        RequestMetric metric = new RequestMetric();
        metric.setBin(bin);
        bin.setRequestMetric(metric);
        Page<Request> requests = requestRepository.getByBinOrderByRequestTimeDesc(bin, new PageRequest(0, 10000));
        int get = 0;
        int put = 0;
        int patch = 0;
        int delete = 0;
        int post = 0;
        for (Request req : requests.getContent()) {
            switch (req.getMethod()) {
                case GET:
                    get++;
                    break;
                case PUT:
                    put++;
                    break;
                case PATCH:
                    patch++;
                    break;
                case POST:
                    post++;
                    break;
                case DELETE:
                    delete++;
                    break;
                default:
                    break;
            }
        }
        metric.getCounts().put(HttpMethod.GET.name(), get);
        metric.getCounts().put(HttpMethod.PUT.name(), put);
        metric.getCounts().put(HttpMethod.PATCH.name(), patch);
        metric.getCounts().put(HttpMethod.POST.name(), post);
        metric.getCounts().put(HttpMethod.DELETE.name(), delete);

        binRepository.save(bin);
    }
}
