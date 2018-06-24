package be.zlz.kara.bin.dto;

import be.zlz.kara.bin.domain.Request;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestDto {

    private HttpMethod method;

    private Date requestTime;

    private String body;

    private Map<String, String> headers;

    private String protocol;

    private Map<String, String> queryParams;

    public RequestDto(HttpMethod method, Date requestTime, String body, Map<String, String> headers, String protocol, Map<String, String> queryParams) {
        this.method = method;
        this.requestTime = requestTime;
        this.body = body;
        this.headers = headers;
        this.protocol = protocol;
        this.queryParams = queryParams;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        if(headers == null) {
            return null;
        }
        return Collections.unmodifiableMap(headers);
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getQueryParams() {
        if(queryParams == null) {
            return null;
        }
        return Collections.unmodifiableMap(queryParams);
    }
}
