package be.zlz.kara.bin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.util.Map;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    private HttpStatus code;

    private String mimeType;

    private String body;

    @ElementCollection(targetClass = String.class)
    @MapKeyClass(String.class)
    private Map<String, String> cookies;

    @ElementCollection(targetClass = String.class)
    @MapKeyClass(String.class)
    private Map<String, String> headers;

    @JsonIgnore
    private boolean custom = false;

    public Reply() {
    }

    public Reply(HttpStatus code, String mimeType, String body, Map<String, String> cookies, Map<String, String> headers, boolean custom) {
        this.code = code;
        this.mimeType = mimeType;
        this.body = body;
        this.cookies = cookies;
        this.headers = headers;
        this.custom = custom;
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

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public long getId() {
        return id;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }
}
