package be.zlz.kara.bin.services.webhook;

import org.springframework.http.HttpMethod;

import java.util.Map;

public class WebhookContext {

    private String destination;

    private Map<String, String> headers;

    private WebhookAuthentication authentication;

    private WebhookMode mode;

    private HttpMethod method;

    public WebhookContext(String destination, Map<String, String> headers, WebhookAuthentication authentication, WebhookMode mode, HttpMethod method) {
        this.destination = destination;
        this.headers = headers;
        this.authentication = authentication;
        this.mode = mode;
        this.method = method;
    }

    public String getDestination() {
        return destination;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public WebhookAuthentication getAuthentication() {
        return authentication;
    }

    public WebhookMode getMode() {
        return mode;
    }

    public HttpMethod getMethod() {
        return method;
    }
}
