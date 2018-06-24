package be.zlz.kara.bin.dto;

public class BinListDto {

    private String binName;

    private RequestCountDto requestCounts;

    public BinListDto(String binName, RequestCountDto requestCounts) {
        this.binName = binName;
        this.requestCounts = requestCounts;
    }

    public String getBinName() {
        return binName;
    }

    public RequestCountDto getRequestCounts() {
        return requestCounts;
    }
}
