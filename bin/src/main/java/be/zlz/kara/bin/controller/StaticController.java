package be.zlz.kara.bin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/api/v1/swagger")
    public String apiSpec() {
        return "api";
    }

    @GetMapping("/api/v1.1.0/swagger")
    public String apiV11Spec() {
        return "apiv11";
    }
}
