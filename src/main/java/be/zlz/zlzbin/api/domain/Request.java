package be.zlz.zlzbin.api.domain;

import javafx.util.Pair;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private HttpMethod method;

    private String body;

    private Map<String, String> headers;

    private List<Cookie> cookies;

    private String protocol;

    private List<Pair<String, String>> queryParams;

    @ManyToOne
    private Bin bin;

    public Request(HttpMethod method, String body, String protocol, Map<String, String> headers) {
        this.method = method;
        this.body = body;
        this.protocol = protocol;
        this.headers = headers;

        cookies = new ArrayList<>();
        queryParams = new ArrayList<>();
    }

    public Request(){
        cookies = new ArrayList<>();
        queryParams = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public List<Cookie> getCookies() {
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

    public void setMethod(String method) {
        switch (method){
            case "POST":
                this.method = HttpMethod.POST;
                break;
            case "PUT":
                this.method = HttpMethod.PUT;
                break;
            case "DELETE":
                this.method = HttpMethod.DELETE;
                break;
            default:
                this.method = HttpMethod.PATCH;
                break;
        }
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", method=" + method +
                ", body='" + body + '\'' +
                ", headers=" + headers +
                ", cookies=" + cookies +
                ", protocol='" + protocol + '\'' +
                ", queryParams=" + queryParams +
                ", bin=" + bin +
                '}';
    }
}
