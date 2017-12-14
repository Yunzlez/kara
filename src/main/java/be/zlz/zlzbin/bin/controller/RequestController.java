package be.zlz.zlzbin.bin.controller;

import be.zlz.zlzbin.bin.domain.Reply;
import be.zlz.zlzbin.bin.domain.Request;
import be.zlz.zlzbin.bin.services.RequestService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
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
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public RequestController(RequestService requestService, SimpMessageSendingOperations messagingTemplate) {
        this.requestService = requestService;
        logger = LoggerFactory.getLogger(this.getClass());
        this.messagingTemplate = messagingTemplate;
        gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
    }

    @RequestMapping(value = "/bin/{uuid}", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<String> handleRequest(HttpServletRequest servletRequest, HttpServletResponse response, HttpEntity<String> body, @PathVariable String uuid, @RequestHeader Map<String, String> headers) {
        Pair<Reply, Request> replyRequestPair = requestService.createRequest(servletRequest, body, uuid, headers);
        Reply reply = replyRequestPair.getFirst();

        reply.getCookies().forEach((k, v) -> {
            response.addCookie(new Cookie(k, v));
        });
        logger.debug("Added cookies");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(reply.getMimeType()));
        logger.debug("Set content-type header to " + httpHeaders.getContentType());

        reply.getHeaders().remove("content-type"); //set using another header
        reply.getHeaders().remove("content-length"); //calculated

        String jsonReply;

        if (reply.isCustom()) {
            jsonReply = reply.getBody();
        } else {
            jsonReply = gson.toJson(reply);
        }

        //calculate the content-length. java string is UTF-16 so convert to UTF8 and count
        byte[] stringbytes = jsonReply.getBytes(StandardCharsets.UTF_8);
        httpHeaders.setContentLength(stringbytes.length);

        reply.getHeaders().forEach(httpHeaders::add);

        newRequest(replyRequestPair.getSecond(), uuid);

        return new ResponseEntity<>(jsonReply, httpHeaders, reply.getCode());
    }

    @SubscribeMapping("/topic/newrequests/{binName}")
    public void newRequest(Request request, @DestinationVariable String binName) {
        logger.debug("Sending message for {}", binName);
        messagingTemplate.convertAndSend("/topic/newrequests/" + binName, request);
    }


}
