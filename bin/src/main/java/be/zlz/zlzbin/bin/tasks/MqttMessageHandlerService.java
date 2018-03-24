package be.zlz.zlzbin.bin.tasks;

import be.zlz.zlzbin.bin.controller.RequestController;
import be.zlz.zlzbin.bin.domain.Bin;
import be.zlz.zlzbin.bin.domain.Request;
import be.zlz.zlzbin.bin.repositories.BinRepository;
import be.zlz.zlzbin.bin.repositories.RequestRepository;
import be.zlz.zlzbin.bin.services.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MqttMessageHandlerService implements MessageHandler{

    private static final String MQTT_RECEIVED_TOPIC = "mqtt_receivedTopic";
    @Autowired
    private RequestController requestController;

    @Autowired
    private RequestService requestService;

    private static final Logger LOG = LoggerFactory.getLogger(MqttMessageHandlerService.class);

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        Map<String, String> headers = new HashMap<>();
        message.getHeaders().forEach((s, o) -> headers.put(s, o.toString()));

        String topic = headers.get(MQTT_RECEIVED_TOPIC);
        LOG.debug("Received MQTT message on topic {}", topic);

        String binName = topic.substring(topic.lastIndexOf('/')+1);

        LOG.debug("Building request");
        //todo proper b64 for binary messages
        Request request = requestService.createRequest(headers, message.getPayload().toString(), binName);
        if(request == null){
            return;
        }

        LOG.debug("Pushing to WS");
        requestController.newRequest(request, binName);
    }
}
