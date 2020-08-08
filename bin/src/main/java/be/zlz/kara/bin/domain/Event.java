package be.zlz.kara.bin.domain;

import be.zlz.kara.bin.domain.converter.MapToSmileConverter;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
public class Event {

    @Id
    private String id;

    private byte[] body;

    @Enumerated(EnumType.STRING)
    private HttpMethod method;

    @Enumerated(EnumType.STRING)
    private Source source;

    private String location;

    @Convert(converter = MapToSmileConverter.class)
    private Map<String, String> metadata;

    @Convert(converter = MapToSmileConverter.class)
    private Map<String, String> additionalData;

    private String contentType;

    private LocalDateTime eventTime;

    private String originIp;

    private long bodySize;

    @ManyToOne
    private Bin bin;

    protected Event() {

    }

    public Event(String id, byte[] body, HttpMethod method, Source source, String location, Map<String, String> metadata, Map<String, String> additionalData, String contentType, LocalDateTime eventTime, String originIp, long bodySize, Bin bin) {
        this.id = id;
        this.body = body;
        this.method = method;
        this.source = source;
        this.location = location;
        this.metadata = metadata;
        this.additionalData = additionalData;
        this.contentType = contentType;
        this.eventTime = eventTime;
        this.originIp = originIp;
        this.bodySize = bodySize;
        this.bin = bin;
    }

    public String getId() {
        return id;
    }

    public byte[] getBody() {
        return body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Source getSource() {
        return source;
    }

    public String getLocation() {
        return location;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public Map<String, String> getAdditionalData() {
        return additionalData;
    }

    public String getContentType() {
        return contentType;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public String getOriginIp() {
        return originIp;
    }

    public long getBodySize() {
        return bodySize;
    }

    public Bin getBin() {
        return bin;
    }
}
