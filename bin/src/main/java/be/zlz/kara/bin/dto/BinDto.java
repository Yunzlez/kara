package be.zlz.kara.bin.dto;

import be.zlz.kara.bin.domain.Request;

import java.util.Collections;
import java.util.List;

public class BinDto {

    private String binName;

    private RequestCountDto requestCounts;

    private InboundDto inbound;

    private int page;

    private int limit;

    private int totalPages;

    private List<Request> requests;

    public BinDto(String binName, RequestCountDto requestCounts, InboundDto inbound, List<Request> requests, int page, int limit, int totalPages) {
        this.binName = binName;
        this.requestCounts = requestCounts;
        this.inbound = inbound;
        this.requests = requests;
        this.page = page;
        this.limit = limit;
        this.totalPages = totalPages;
    }

    public String getBinName() {
        return binName;
    }

    public RequestCountDto getRequestCounts() {
        return requestCounts;
    }

    public InboundDto getInbound() {
        return inbound;
    }

    public List<Request> getRequests() {
        return Collections.unmodifiableList(requests);
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
