package be.zlz.zlzbin.bin.domain;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class RequestMetric {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @OneToOne
    private Bin bin;

    @ElementCollection(targetClass = Integer.class)
    @MapKeyClass(String.class)
    private Map<String, Integer> counts;

    public RequestMetric(){
        this.counts = new HashMap<>();
    }

    public RequestMetric(Bin bin) {
        this.bin = bin;
        this.counts = new HashMap<>();
    }

    public long getId() {
        return id;
    }

    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }

    public Map<String, Integer> getCounts() {
        return counts;
    }
}
