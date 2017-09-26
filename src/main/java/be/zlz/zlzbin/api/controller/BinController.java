package be.zlz.zlzbin.api.controller;

import be.zlz.zlzbin.api.domain.Bin;
import be.zlz.zlzbin.api.domain.Request;
import be.zlz.zlzbin.api.dto.ReplyDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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
    public List<Request> getLogForUuidAsJson(@PathVariable String uuid) {
        if(binRepository.getByName(uuid) == null){
            throw new ResourceNotFoundException("Could not find bin with name " + uuid);
        }
        return requestRepository.getAllByBinOrderByRequestTimeAsc(binRepository.getByName(uuid));
    }

    @GetMapping(value = "/bin/{uuid}/log", produces = "text/html")
    public String getLogForUuidAsPage(@PathVariable String uuid, Map<String, Object> model) {
        if(binRepository.getByName(uuid) == null){
            throw new ResourceNotFoundException("Could not find bin with name " + uuid);
        }
        model.put("pageTitle", "Bin " + uuid);
        model.put("binName", uuid);
        List<Request> requests = requestRepository.getAllByBinOrderByRequestTimeAsc(binRepository.getByName(uuid));
        model.put("requests", requests);
        model.put("requestCount", requests.size());
        logger.debug("Creating request URL: " + baseUrl + "/bin/" + uuid);
        logger.debug("baseUrl = " + baseUrl);
        logger.debug("uuid = " + uuid);
        model.put("requestUrl", baseUrl + "/bin/" + uuid);
        setCounts(requests, model);

        return "requestlog";
    }

    @GetMapping(value = "/bin/{uuid}/log/settings", produces = "application/json")
    public String getBinSetup(@PathVariable String uuid, Map<String, Object> model) {
        Bin bin = binRepository.getByName(uuid);

        model.put("pageTitle", "Bin " + uuid + " settings");
        model.put("binName", uuid);

        logger.debug("bin " + bin + "with uuid " + uuid);
        if(bin.getReply() != null){
            model.put("reply",bin.getReply());
        }
        else {
            model.put("reply", new ReplyDTO());
        }

        return "settings";
    }

    @PostMapping(value = "/bin/{uuid}/log/settings", produces = "application/json")
    public String saveBinSetup(@PathVariable String uuid, @ModelAttribute ReplyDTO reply) {
        Bin bin = binRepository.getByName(uuid);
        ReplyBuilder replyBuilder = new ReplyBuilder();
        //todo: VALIDATE
        bin.setReply(
                replyBuilder.setCode(HttpStatus.valueOf(reply.getCode()))
                        .setMimeType(reply.getMimeType())
                        .setBody(reply.getBody())
                        .setCustom(true)
                        .build()
        );

        binRepository.save(bin);

        return "redirect:/bin/" +  uuid  + "/log";
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

    private void setCounts(List<Request> requests, Map<String, Object> model) {
        int get = 0;
        int put = 0;
        int patch = 0;
        int delete = 0;
        int post = 0;
        for (Request req : requests) {
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
        model.put("getCount", get);
        model.put("postCount", post);
        model.put("patchCount", patch);
        model.put("deleteCount", delete);
        model.put("putCount", put);
    }
}
