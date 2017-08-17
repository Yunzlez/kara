package be.zlz.zlzbin.api.controller;

import be.zlz.zlzbin.api.domain.Reply;
import be.zlz.zlzbin.api.exceptions.BadRequestException;
import be.zlz.zlzbin.api.exceptions.ResourceNotFoundException;
import be.zlz.zlzbin.api.domain.Request;
import be.zlz.zlzbin.api.repositories.BinRepository;
import be.zlz.zlzbin.api.repositories.RequestRepository;
import be.zlz.zlzbin.api.services.RequestService;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RequestController {

    private RequestService requestService;
    private Logger logger;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping(value = "/bin/{uuid}", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<String> handleRequest(HttpServletRequest servletRequest, HttpServletResponse response, HttpEntity<String> body, @PathVariable String uuid, @RequestHeader Map<String, String> headers) {
        Reply reply = requestService.createRequest(servletRequest, body, uuid, headers);

        //response.addCookie();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(reply.getMimeType()));
        return new ResponseEntity<String>(reply.getBody(), httpHeaders, reply.getCode());
    }


}
