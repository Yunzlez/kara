package be.zlz.zlzbin.api.domain;

import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private HttpMethod method;

    private String body;

    @ElementCollection(targetClass = String.class)
    @MapKeyClass(String.class)
    private Map<String, String> headers;

    private String protocol;

    @ElementCollection(targetClass = String.class)
    @MapKeyClass(String.class)
    private Map<String, String> queryParams;

    @ManyToOne
    private Bin bin;

    public Request(HttpMethod method, String body, String protocol, Map<String, String> headers) {
        this.method = method;
        this.body = body;
        this.protocol = protocol;
        this.headers = headers;

        queryParams = new HashMap<>();
    }

    public Request(){
        queryParams = new HashMap<>();
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

    public Map<String, String> getQueryParams() {
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
            case "GET":
                this.method = HttpMethod.GET;
                break;
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

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }



    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", method=" + method +
                ", body='" + body + '\'' +
                ", headers=" + headers +
                ", protocol='" + protocol + '\'' +
                ", queryParams=" + queryParams +
                ", bin=" + bin +
                '}';
    }
}
