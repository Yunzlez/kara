package be.zlz.kara.bin.dto;

import java.util.Collections;
import java.util.Map;

public class RequestCountDto {

    private int total;

    private Map<String, Integer> counts;

    public RequestCountDto(int total, Map<String, Integer> counts) {
        this.total = total;
        this.counts = counts;
    }

    public int getTotal() {
        return total;
    }

    public Map<String, Integer> getCounts() {
        return Collections.unmodifiableMap(counts);
    }
}
