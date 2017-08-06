package be.zlz.zlzbin.api.controller;

import be.zlz.zlzbin.api.Exceptions.ResourceNotFoundException;
import be.zlz.zlzbin.api.domain.Request;
import be.zlz.zlzbin.api.repositories.BinRepository;
import be.zlz.zlzbin.api.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class RequestController {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    BinRepository binRepository;

    @RequestMapping(value = "{uuid}", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public String handleRequest(HttpServletRequest servletRequest, HttpEntity<String> body, @RequestParam String uuid, @RequestHeader Map<String, String> headers){
        Request request = new Request();

        request.setBin(binRepository.getByName(uuid));
        if(request.getBin() == null){
            throw new ResourceNotFoundException("No bin with that name exists");
        }

        for (Cookie cookie : servletRequest.getCookies()) {
            request.getCookies().add(cookie);
        }
        request.setHeaders(headers);

        request.setBody(body.getBody());
        request.setMethod(servletRequest.getMethod());
        request.setProtocol(servletRequest.getProtocol());

        return request.toString();
        //requestRepository.save(new Request());
    }
}
