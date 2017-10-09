package be.zlz.zlzbin.api.controller;

import be.zlz.zlzbin.api.domain.Bin;
import be.zlz.zlzbin.api.domain.Request;
import be.zlz.zlzbin.api.domain.RequestMetric;
import be.zlz.zlzbin.api.dto.SettingDTO;
import be.zlz.zlzbin.api.exceptions.ResourceNotFoundException;
import be.zlz.zlzbin.api.repositories.BinRepository;
import be.zlz.zlzbin.api.repositories.RequestRepository;
import be.zlz.zlzbin.api.services.BinService;
import be.zlz.zlzbin.api.services.ReplyService;
import be.zlz.zlzbin.api.util.ReplyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class BinController {

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private BinService binService;

    @Value("${base.url}")
    private String baseUrl;

    @Value("${max.page.size}")
    private int maxPageSize;

    private Logger logger;

    public BinController(){
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @GetMapping("/bin/create")
    public String createBin() {
        String name = UUID.randomUUID().toString();

        Bin bin = new Bin(name);
        binRepository.save(bin);
        logger.info("created bin with UUID {}", name);
        logger.debug("BaseURL = " + baseUrl);

        return "redirect:"+ baseUrl + "/bin/" + name + "/log";
    }

    //todo need a limit system & pagination system
    @GetMapping(value = "/bin/{uuid}/log", produces = "application/json")
    @ResponseBody
    public List<Request> getLogForUuidAsJson(@PathVariable String uuid, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "limit", required = false) Integer limit) {
        if(binRepository.getByName(uuid) == null){
            throw new ResourceNotFoundException("Could not find bin with name " + uuid);
        }
        Page<Request> current = requestRepository.getByBinOrderByRequestTimeDesc(binRepository.getByName(uuid), getPageable(page, limit));
        return current.getContent();
    }

    @GetMapping(value = "/bin/{uuid}/log", produces = "text/html")
    public String getLogForUuidAsPage(@PathVariable String uuid, Map<String, Object> model, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "limit", required = false) Integer limit) {
        Bin bin = binRepository.getByName(uuid);
        if(bin == null){
            throw new ResourceNotFoundException("Could not find bin with name " + uuid);
        }
        model.put("pageTitle", "Bin " + uuid);
        model.put("binName", uuid);

        Page<Request> current = requestRepository.getByBinOrderByRequestTimeDesc(binRepository.getByName(uuid), getPageable(page, limit));
        List<Request> requests = current.getContent();

        model.put("requests", requests);
        model.put("requestCount", current.getTotalElements());
        model.put("pageCount", current.getTotalPages());
        model.put("currentPage", current);
        model.put("currentLimit", limit);
        logger.debug("Creating request URL: " + baseUrl + "/bin/" + uuid);
        logger.debug("baseUrl = " + baseUrl);
        logger.debug("uuid = " + uuid);
        model.put("requestUrl", baseUrl + "/bin/" + uuid);
        setRequestCounts(bin, model);

        return "requestlog";
    }

    @GetMapping(value = "/bin/{uuid}/log/settings", produces = "application/json")
    public String getBinSetup(@PathVariable String uuid, Map<String, Object> model) {
        Bin bin = binRepository.getByName(uuid);

        model.put("pageTitle", "Bin " + uuid + " settings");
        model.put("binName", uuid);

        logger.debug("bin " + bin + "with uuid " + uuid);

        SettingDTO settings;
        if(bin.getReply() != null){
            settings = new SettingDTO(bin.getReply());
        }
        else {
            settings = new SettingDTO();
        }
        settings.setCustomName(bin.getName());
        settings.setPermanent(bin.isPermanent());
        model.put("reply", settings);

        return "settings";
    }

    @PostMapping(value = "/bin/{uuid}/log/settings", produces = "application/json")
    public String saveBinSetup(@PathVariable String uuid, @ModelAttribute SettingDTO settings, BindingResult bindingResult) {
        Bin bin = binRepository.getByName(uuid);
        ReplyBuilder replyBuilder = new ReplyBuilder();
        if(!(settings.getCode() == null || settings.getMimeType() == null || settings.getBody() == null)){
            bin.setReply(
                    replyBuilder.setCode(HttpStatus.valueOf(settings.getCode()))
                            .setMimeType(settings.getMimeType())
                            .setBody(settings.getBody())
                            .setCustom(true)
                            .build()
            );
        }
        String redirect = uuid;
        if(settings.getCustomName() != null){
            bin.setName(settings.getCustomName());
            redirect = settings.getCustomName();
        }
        if(!bin.isPermanent() && settings.isPermanent()){
            binService.clearBin(uuid);
        }
        bin.setPermanent(settings.isPermanent());

        binRepository.save(bin);

        return "redirect:/bin/" +  redirect  + "/log";
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

    private Pageable getPageable(Integer page, Integer size){
        if(size == null || size > maxPageSize || size == 0){
            size = maxPageSize;
        }
        if(page == null){
            page = 0;
        }
        return new PageRequest(page, size);
    }

    private void setRequestCounts(Bin bin, Map<String, Object> model){
        RequestMetric metric = bin.getRequestMetric();

        model.put("getCount", metric.getCounts().getOrDefault(HttpMethod.GET.name(), 0));
        model.put("postCount", metric.getCounts().getOrDefault(HttpMethod.POST.name(), 0));
        model.put("patchCount", metric.getCounts().getOrDefault(HttpMethod.PATCH.name(), 0));
        model.put("deleteCount", metric.getCounts().getOrDefault(HttpMethod.DELETE.name(), 0));
        model.put("putCount", metric.getCounts().getOrDefault(HttpMethod.PUT.name(), 0));
    }
}
