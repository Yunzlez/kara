package be.zlz.zlzbin.api.controller;

import be.zlz.zlzbin.api.domain.Bin;
import be.zlz.zlzbin.api.domain.Request;
import be.zlz.zlzbin.api.repositories.BinRepository;
import be.zlz.zlzbin.api.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class BinController {

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Value("${base.url}")
    private String baseUrl;

    @GetMapping("/bin/create")
    public String createBin() {
        String name = UUID.randomUUID().toString();

        Bin bin = new Bin(name);
        binRepository.save(bin);

        return "redirect:/bin/" + name + "/log";
    }

    //todo need a limit system & pagination system
    @GetMapping(value = "/bin/{uuid}/log", produces = "application/json")
    @ResponseBody
    public List<Request> getLogForUuidAsJson(@PathVariable String uuid) {
        return requestRepository.getAllByBin(binRepository.getByName(uuid));
    }

    @GetMapping(value = "/bin/{uuid}/log", produces = "text/html")
    public String getLogForUuidAsPage(@PathVariable String uuid, Map<String, Object> model) {
        model.put("pageTitle", "Bin " + uuid);
        model.put("binName", uuid);
        List<Request> requests = requestRepository.getAllByBin(binRepository.getByName(uuid));
        model.put("requests", requests);
        model.put("requestCount", requests.size());
        model.put("requestUrl", baseUrl + "/bin/" + uuid);
        setCounts(requests, model);

        return "requestlog";
    }

    @GetMapping(value = "/bin/{uuid}/delete")
    @ResponseBody
    public String deleteBin(@PathVariable String uuid) {
        //todo implement
        //binRepository.delete(binRepository.getByName(uuid));
        //requestRepository.deleteForBin(uuid);
        return "redirect:/";
    }

    private void setCounts(List<Request> requests, Map<String, Object> model) {
        int get = 0, put = 0, patch = 0, delete = 0, post = 0;
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
            }
        }

        model.put("getCount", get);
        model.put("postCount", post);
        model.put("patchCount", patch);
        model.put("deleteCount", delete);
        model.put("putCount", put);
    }
}
