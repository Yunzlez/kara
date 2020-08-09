package be.zlz.kara.bin.services.webhook;

import org.springframework.http.HttpMethod;

import java.util.Map;

public class WebhookContext {

    private String destination;

    private Map<String, String> headers;

    private WebhookAuthentication authentication;

    private byte[] data;

    private HttpMethod method;

    private boolean transparent;

    public WebhookContext(String destination, Map<String, String> headers, WebhookAuthentication authentication, byte[] data, HttpMethod method, boolean transparent) {
        this.destination = destination;
        this.headers = headers;
        this.authentication = authentication;
        this.data = data;
        this.method = method;
        this.transparent = transparent;
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

    public byte[] getData() {
        return data;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public boolean isTransparent() {
        return transparent;
    }
}
