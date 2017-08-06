package be.zlz.zlzbin.api.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Bin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @OneToMany
    private List<Request> requests;

    public Bin(String name) {
        this.name = name;
    }

    public Bin() {
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
}
