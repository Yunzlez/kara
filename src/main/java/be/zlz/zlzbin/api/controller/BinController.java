package be.zlz.zlzbin.api.controller;

import be.zlz.zlzbin.api.domain.Bin;
import be.zlz.zlzbin.api.domain.Request;
import be.zlz.zlzbin.api.repositories.BinRepository;
import be.zlz.zlzbin.api.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class BinController {

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private RequestRepository requestRepository;

    @GetMapping("/bin/create")
    public String createBin(){
        String name = UUID.randomUUID().toString();

        Bin bin = new Bin(name);
        binRepository.save(bin);

        return name;
    }

    @GetMapping(value = "/bin/{uuid}/log", produces = "application/json")
    public List<Request> getLogForUuidAsJson(@PathVariable String uuid){
        return requestRepository.getAllByBin(binRepository.getByName(uuid));
    }

    @GetMapping(value = "/bin/{uuid}/log", produces = "text/html")
    public String getLogForUuidAsPage(@PathVariable String uuid, Map<String, String> model){
        return "requestLog.html";
    }
}
