package be.zlz.zlzbin.api.controller;

import be.zlz.zlzbin.api.domain.Reply;
import be.zlz.zlzbin.api.services.RequestService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    private Gson gson;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
        logger = LoggerFactory.getLogger(this.getClass());
        gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
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

        reply.getHeaders().remove("content-type"); //set using another header
        reply.getHeaders().remove("content-length"); //calculated

        //calculate the content-length. java string is UTF-16 so convert to UTF8 and count
        String jsonReply = gson.toJson(reply);
        byte[] stringbytes = jsonReply.getBytes(StandardCharsets.UTF_8);
        httpHeaders.setContentLength(stringbytes.length);

        reply.getHeaders().forEach(httpHeaders::add);

        return new ResponseEntity<>(gson.toJson(reply), httpHeaders, reply.getCode());
    }


}
