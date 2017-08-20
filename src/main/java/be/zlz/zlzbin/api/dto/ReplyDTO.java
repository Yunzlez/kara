package be.zlz.zlzbin.api.dto;

import java.util.List;

public class ReplyDTO {

    private int code;

    private String mimeType;

    private String body;

    //lists to make thymeleaf binding easier
    private List<String> headerNames;

    private List<String> headerValues;

    private List<String> cookieNames;

    private List<String> cookieValues;

    public ReplyDTO() {
    }

    public ReplyDTO(int code, String mimeType, String body) {
        this.code = code;
        this.mimeType = mimeType;
        this.body = body;
    }

    public List<String> getHeaderNames() {
        return headerNames;
    }

    public void setHeaderNames(List<String> headerNames) {
        this.headerNames = headerNames;
    }

    public List<String> getHeaderValues() {
        return headerValues;
    }

    public void setHeaderValues(List<String> headerValues) {
        this.headerValues = headerValues;
    }

    public List<String> getCookieNames() {
        return cookieNames;
    }

    public void setCookieNames(List<String> cookieNames) {
        this.cookieNames = cookieNames;
    }

    public List<String> getCookieValues() {
        return cookieValues;
    }

    public void setCookieValues(List<String> cookieValues) {
        this.cookieValues = cookieValues;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
