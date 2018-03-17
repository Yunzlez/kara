package be.zlz.zlzbin.bin.tasks;

import be.zlz.zlzbin.bin.controller.RequestController;
import be.zlz.zlzbin.bin.domain.Bin;
import be.zlz.zlzbin.bin.domain.Request;
import be.zlz.zlzbin.bin.repositories.BinRepository;
import be.zlz.zlzbin.bin.repositories.RequestRepository;
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
    private RequestRepository requestRepository;

    @Autowired
    private BinRepository binRepository;

    private List<String> mqttEnabledBins = new ArrayList<>();

    private static final Logger LOG = LoggerFactory.getLogger(MqttMessageHandlerService.class);

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {


        Map<String, String> headers = new HashMap<>();
        message.getHeaders().forEach((s, o) -> headers.put(s, o.toString()));

        String topic = headers.get(MQTT_RECEIVED_TOPIC);
        LOG.debug("Received MQTT message on topic {}", topic);

        String binName = topic.substring(topic.lastIndexOf('/')+1);
        Bin bin = binRepository.getByName(binName);
        if(bin == null){
            LOG.info("Got message for unknown bin {}", binName);
            return;
        }

        LOG.debug("Building request");
        //todo proper b64 for binary messages
        Request request = buildMqttRequest(headers, message.getPayload().toString(), bin);
        requestRepository.save(request);
        LOG.debug("Pushing to WS");
        requestController.newRequest(request, binName);
    }

    private Request buildMqttRequest(Map<String, String> headers, String body, Bin bin){
        Request request = new Request();
        request.setBin(bin);
        request.setMqtt(true);
        request.setBody(body);
        request.setHeaders(headers);
        request.setRequestTime(new Date());
        request.setProtocol("MQTT");
        return request;
    }
}
