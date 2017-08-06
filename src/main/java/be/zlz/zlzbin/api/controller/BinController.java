package be.zlz.zlzbin.api.controller;

import be.zlz.zlzbin.api.repositories.BinRepository;
import be.zlz.zlzbin.api.domain.Bin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/bin")
public class BinController {

    @Autowired
    private BinRepository binRepository;

    @GetMapping("/create")
    public String createBin(){
        String name = UUID.randomUUID().toString();

        Bin bin = new Bin(name);
        binRepository.save(bin);

        return name;
    }
}
