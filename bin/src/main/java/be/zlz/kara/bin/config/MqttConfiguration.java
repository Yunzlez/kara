package be.zlz.kara.bin.config;

import be.zlz.kara.bin.tasks.MqttMessageHandlerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttConfiguration {

    @Value("${mqtt.url}")
    private String mqttUrl;

    @Value("${mqtt.clientid}")
    private String clientId;

    @Value("${mqtt.enabled}")
    private boolean mqttEnabled;

    private String topicName = "/bin/+";

    @Bean
    @Qualifier("MQTT")
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        if (mqttEnabled){
            MqttPahoMessageDrivenChannelAdapter adapter =
                    new MqttPahoMessageDrivenChannelAdapter(mqttUrl, clientId, topicName);
            adapter.setCompletionTimeout(5000);
            adapter.setConverter(new DefaultPahoMessageConverter());
            adapter.setQos(1);
            adapter.setOutputChannel(mqttInputChannel());
            return adapter;
        }
        return null;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MqttMessageHandlerService();
    }
}
