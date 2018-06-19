package be.zlz.kara.bin.controller;

import be.zlz.kara.bin.domain.Reply;
import be.zlz.kara.bin.domain.Request;
import be.zlz.kara.bin.services.RequestService;
import com.codahale.metrics.Meter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class RequestController {

    private RequestService requestService;
    private Logger logger;
    private SimpMessageSendingOperations messagingTemplate;

    private final Meter requestMeter;

    @Autowired
    public RequestController(RequestService requestService, SimpMessageSendingOperations messagingTemplate, Meter requestMeter) {
        this.requestService = requestService;
        this.requestMeter = requestMeter;
        logger = LoggerFactory.getLogger(this.getClass());
        this.messagingTemplate = messagingTemplate;
    }

    @RequestMapping(value = "/bin/{uuid}", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<String> handleRequest(HttpServletRequest servletRequest, HttpServletResponse response, HttpEntity<String> body, @PathVariable String uuid, @RequestHeader Map<String, String> headers) {
        Pair<Reply, Request> replyRequestPair = requestService.createRequest(servletRequest, body, uuid, headers);
        newRequest(replyRequestPair.getSecond(), uuid);
        requestMeter.mark();
        return requestService.buildResponse(replyRequestPair.getFirst(), response);
    }

    @SubscribeMapping("/topic/newrequests/{binName}")
    public void newRequest(Request request, @DestinationVariable String binName) {
        logger.debug("Sending message for {}", binName);
        messagingTemplate.convertAndSend("/topic/newrequests/" + binName, request);
    }


}