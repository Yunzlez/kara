package be.zlz.zlzbin.api.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    @OneToMany
    private List<Request> requests;

    @OneToOne(cascade = CascadeType.ALL)
    private Reply reply;

    private boolean permanent;

    private int requestCount;

    public Bin(String name) {
        this.name = name;
        this.creationDate = new Date();
        this.lastRequest = this.creationDate;
        this.requestCount = 0;
    }

    public Bin() {
        this.creationDate = new Date();
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

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }
}
