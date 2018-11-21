package be.zlz.kara.bin.config;

import be.zlz.kara.bin.services.mqtt.MqttMessageHandlerService;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@ConditionalOnProperty("mqtt.enabled")
@IntegrationComponentScan()
public class MqttConfiguration {

    @Value("${mqtt.broker.url}")
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
        if (mqttEnabled) {
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
    public MqttPahoClientFactory mqttPahoClientFactory() {
        if (mqttEnabled) {
            DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
            MqttConnectOptions options = new MqttConnectOptions();
            options.setServerURIs(new String[] { mqttUrl });
            factory.setConnectionOptions(options);
            return factory;
        }
        return null;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler outbound() {
        if (mqttEnabled) {
            MqttPahoMessageHandler messageHandler =
                    new MqttPahoMessageHandler("kara-client", mqttPahoClientFactory());
            messageHandler.setAsync(true);
            messageHandler.setDefaultTopic("/bin/main");
            return messageHandler;
        }
        return null;
    }

    @Bean("mqttOutboundChannel")
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MqttMessageHandlerService();
    }
}
