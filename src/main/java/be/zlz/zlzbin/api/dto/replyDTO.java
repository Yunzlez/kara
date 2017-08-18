package be.zlz.zlzbin.api.dto;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class replyDTO {

    private HttpStatus code;

    private String mimeType;

    private String body;

    private Map<String, String> cookies;

    private Map<String, String> headers;

    public replyDTO() {
    }

    public replyDTO(HttpStatus code, String mimeType, String body) {
        this.code = code;
        this.mimeType = mimeType;
        this.body = body;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
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
