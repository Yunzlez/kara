package be.zlz.kara.bin.dto;

public class InboundDto {

    private String HttpUrl;

    private String MqttUrl;

    private String MqttTopic;

    public InboundDto(String httpUrl, String mqttUrl, String mqttTopic) {
        HttpUrl = httpUrl;
        MqttUrl = mqttUrl;
        MqttTopic = mqttTopic;
    }

    public String getHttpUrl() {
        return HttpUrl;
    }

    public String getMqttUrl() {
        return MqttUrl;
    }

    public String getMqttTopic() {
        return MqttTopic;
    }
}
