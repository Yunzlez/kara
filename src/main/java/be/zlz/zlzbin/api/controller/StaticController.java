package be.zlz.zlzbin.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class StaticController {

    @GetMapping("/")
    public String index(Map<String, Object> model){
        model.put("pageTitle", "index");
        model.put("content", "Hello, world");

        return "index";
    }
}
