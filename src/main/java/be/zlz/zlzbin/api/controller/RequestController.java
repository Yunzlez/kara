package be.zlz.zlzbin.api.controller;

import be.zlz.zlzbin.api.domain.Reply;
import be.zlz.zlzbin.api.services.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
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


        reply.getCookies().forEach((k, v) -> {
            response.addCookie(new Cookie(k, v));
        });
        logger.debug("Added cookies");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(reply.getMimeType()));
        logger.debug("Set content-type header to " + httpHeaders.getContentType());

        if(reply.getBody() != null){
            //calculate the content-length. java string is UTF-16 so convert to UTF8 and count
            byte[] stringbytes = reply.getBody().getBytes(StandardCharsets.UTF_8);
            httpHeaders.setContentLength(stringbytes.length);
        }

        reply.getHeaders().remove("content-type"); //set using another header
        reply.getHeaders().remove("content-length"); //calculated

        reply.getHeaders().forEach(httpHeaders::add);

        return new ResponseEntity<>(reply.getBody(), httpHeaders, reply.getCode());
    }


}
