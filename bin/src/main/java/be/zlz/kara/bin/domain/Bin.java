package be.zlz.kara.bin.domain;

import be.zlz.kara.bin.domain.enums.BinConfigKey;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
public class Bin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String name;

    @Temporal(TemporalType.DATE)
    private final Date creationDate;

    @Temporal(TemporalType.DATE)
    private Date lastRequest;

    @OneToMany(mappedBy = "bin")
    private List<Request> requests;

    @OneToOne(cascade = CascadeType.ALL)
    private Response response;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private RequestMetric requestMetric;

    @ElementCollection(targetClass = Boolean.class)
    @MapKeyClass(String.class)
    private final Map<String, Boolean> config;

    private int requestCount;

    public Bin(String name) {
        this.name = name;
        this.creationDate = new Date();
        this.lastRequest = this.creationDate;
        this.requestMetric = new RequestMetric(this);
        this.requestCount = 0;
        this.config = new HashMap<>();
        this.config.put(BinConfigKey.PERMANENT_KEY.getValue(), false);
    }

    public Bin() {
        this.creationDate = new Date();
        this.requestMetric = new RequestMetric(this);
        this.config = new HashMap<>();
        this.config.put(BinConfigKey.PERMANENT_KEY.getValue(), false);
    }

    public RequestMetric getRequestMetric() {
        return requestMetric;
    }

    public void setRequestMetric(RequestMetric requestMetric) {
        this.requestMetric = requestMetric;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Response getResponse() {
        return response;
    }

    public Reply getReply() {
        return new Reply(response);
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Date getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(Date lastRequest) {
        this.lastRequest = lastRequest;
    }

    public boolean isPermanent() {
        Boolean value = config.get(BinConfigKey.PERMANENT_KEY.getValue());
        return value != null && value;
    }

    public void addConfigEntry(BinConfigKey key, boolean value) {
        this.config.put(key.getValue(), value);
    }

    public void addConfigEntries(Map<String, Boolean> entries) {
        this.config.putAll(entries);
    }

    public boolean isEnabled(BinConfigKey setting){
        Boolean val = config.get(setting.getValue());
        return val != null && val;
    }

    public Map<String, Boolean> getConfiguration(){
        return Collections.unmodifiableMap(this.config);
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }
}
