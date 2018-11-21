package be.zlz.kara.bin.services.mqtt;

import be.zlz.kara.bin.controller.RequestController;
import be.zlz.kara.bin.domain.Request;
import be.zlz.kara.bin.services.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.*;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

public class MqttMessageHandlerService implements MessageHandler {

    private static final String MQTT_RECEIVED_TOPIC = "mqtt_receivedTopic";

    @Autowired
    private RequestController requestController;

    @Autowired
    private RequestService requestService;

    @Autowired
    @Qualifier("mqttOutboundChannel")
    private MessageChannel mqttMessageChannel;

    @Value("${mqtt.enabled}")
    private boolean mqttEnabled;

    private static final Logger LOG = LoggerFactory.getLogger(MqttMessageHandlerService.class);

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        Map<String, String> headers = new HashMap<>();
        message.getHeaders().forEach((s, o) -> headers.put(s, o.toString()));

        String topic = headers.get(MQTT_RECEIVED_TOPIC);
        LOG.debug("Received MQTT message on topic {}", topic);

        String binName = topic.substring(topic.lastIndexOf('/') + 1);

        LOG.debug("Building request");
        //todo proper b64 for binary messages
        Request request = requestService.createRequest(headers, message.getPayload().toString(), binName);
        if (request == null) {
            return;
        }

        LOG.debug("Pushing to WS");
        requestController.newRequest(request, binName);
    }

    public void sendMessage(String message, String binName) {
        if (mqttEnabled) {
            mqttMessageChannel.send(
                    MessageBuilder.withPayload(message)
                            .setHeader(MqttHeaders.TOPIC, "/bin/" + binName + "/log")
                            .build()
            );
        }
    }
}
