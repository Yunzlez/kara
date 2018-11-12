package be.zlz.kara.bin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultResponseDto
{

    private HttpStatus code;

    private String mimeType;

    private Map<String, String> cookies;

    private Map<String, String> headers;

    private Map<String, String> queryParams;

    public DefaultResponseDto(HttpStatus code, String mimeType, Map<String, String> cookies, Map<String, String> headers, Map<String, String> queryParams) {
        this.code = code;
        this.mimeType = mimeType;
        this.cookies = cookies;
        this.headers = headers;
        this.queryParams = queryParams;
    }

    public HttpStatus getCode() {
        return code;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }
}
