package be.zlz.kara.bin.domain;

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
    private Date creationDate;

    @Temporal(TemporalType.DATE)
    private Date lastRequest;

    @OneToMany(mappedBy = "bin")
    private List<Request> requests;

    @OneToMany(mappedBy = "bin")
    private List<BinaryRequest> binaryRequests;

    @OneToOne(cascade = CascadeType.ALL)
    private Reply reply;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private RequestMetric requestMetric;

    @ElementCollection(targetClass = String.class)
    @MapKeyClass(String.class)
    private Map<String, Boolean> config;

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

    public List<BinaryRequest> getBinaryRequests() {
        return binaryRequests;
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

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }

    public Date getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(Date lastRequest) {
        this.lastRequest = lastRequest;
    }

    public boolean isPermanent() {
        return config.get(BinConfigKey.PERMANENT_KEY.getValue());
    }

    public void addConfigEntry(BinConfigKey key, boolean value) {
        this.config.put(key.getValue(), value);
    }

    public boolean isEnabled(BinConfigKey setting){
        return config.get(setting.getValue());
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
