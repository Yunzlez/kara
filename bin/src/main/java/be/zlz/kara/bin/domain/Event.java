package be.zlz.kara.bin.domain;

import org.springframework.http.HttpMethod;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
public class Event {

    @Id
    private String id;

    private byte[] body;

    private HttpMethod method;

    private Source source;

    private String location;

    private Map<String, String> metadata;

    private Map<String, String> additionalData;

    private String contentType;

    private LocalDateTime eventTime;

    private String originIp;

    private long bodySize;

    @ManyToOne
    private Bin bin;
}
