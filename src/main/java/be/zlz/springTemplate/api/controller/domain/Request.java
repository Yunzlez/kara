package be.zlz.springTemplate.api.controller.domain;

import javafx.util.Pair;
import org.springframework.http.HttpMethod;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private HttpMethod method;

    private String body;

    private List<Pair<String, String>> headers;

    private List<Pair<String,String>> cookies;

    private String protocol;

    private List<Pair<String, String>> queryParams;

    public Request(HttpMethod method, String body, String protocol) {
        this.method = method;
        this.body = body;
        this.protocol = protocol;

        headers = new ArrayList<>();
        cookies = new ArrayList<>();
        queryParams = new ArrayList<>();
    }

    public Request(){
        headers = new ArrayList<>();
        cookies = new ArrayList<>();
        queryParams = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public List<Pair<String, String>> getHeaders() {
        return headers;
    }

    public List<Pair<String, String>> getCookies() {
        return cookies;
    }

    public List<Pair<String, String>> getQueryParams() {
        return queryParams;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
