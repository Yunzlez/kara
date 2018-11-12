package be.zlz.kara.bin.controller;

import be.zlz.kara.bin.domain.Reply;
import be.zlz.kara.bin.domain.Request;
import be.zlz.kara.bin.services.DelayService;
import be.zlz.kara.bin.services.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
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

    private SimpMessageSendingOperations messagingTemplate;

    private DelayService delayService;

    private Logger logger;

    @Autowired
    public RequestController(RequestService requestService, SimpMessageSendingOperations messagingTemplate, DelayService delayService) {
        this.requestService = requestService;
        logger = LoggerFactory.getLogger(this.getClass());
        this.messagingTemplate = messagingTemplate;
        this.delayService = delayService;

    }

    @CrossOrigin
    @RequestMapping(value = {"/bin/{uuid}", "/api/v1/bins/{uuid}"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<String> handleRequest(HttpServletRequest servletRequest,
                                                HttpServletResponse response,
                                                HttpEntity<String> body,
                                                @PathVariable String uuid,
                                                @RequestHeader Map<String, String> headers
    ) {
        Pair<Reply, Request> replyRequestPair = requestService.createRequest(servletRequest, body, uuid, headers);
        newRequest(replyRequestPair.getSecond(), uuid);
        return requestService.buildResponse(replyRequestPair.getFirst(), response);
    }

    @CrossOrigin
    @RequestMapping(value = {"/bin/{uuid}/delay/{ms}", "/api/v1/bins/{uuid}/delay/{ms}"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<String> handleRequestDelayed(HttpServletRequest servletRequest,
                                                       HttpServletResponse response,
                                                       HttpEntity<String> body,
                                                       @PathVariable String uuid,
                                                       @RequestHeader Map<String, String> headers,
                                                       @PathVariable int ms
    ) {
        try{
            if(!delayService.delay(ms)){
                return new ResponseEntity<String>(
                        "Cannot delay response at this time, please try again later",
                        HttpStatus.SERVICE_UNAVAILABLE
                );
            }
            return handleRequest(servletRequest, response, body, uuid, headers);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @SubscribeMapping(value = {"/topic/newrequests/{binName}", "/api/v1/bins/{binName}/requests/ws"})
    public void newRequest(Request request, @DestinationVariable String binName) {
        logger.debug("Sending message for {}", binName);
        messagingTemplate.convertAndSend("/topic/newrequests/" + binName, request);
    }
}
