package be.zlz.kara.bin.dto;

import be.zlz.kara.bin.domain.Reply;

import java.util.*;

public class SettingViewModel {

    private Integer code;

    private String mimeType;

    private String body;

    //lists to make thymeleaf binding easier
    private List<String> headerNames;

    private List<String> headerValues;

    private List<String> cookieNames;

    private List<String> cookieValues;

    private String customName;

    private boolean isPermanent;

    public SettingViewModel() {
    }

    public SettingViewModel(Reply reply){
        this.code = reply.getCode().value();
        this.body = reply.getBody();
        this.mimeType = reply.getMimeType();
        if (reply.getHeaders() != null && !reply.getHeaders().isEmpty()) {
            this.headerNames = new ArrayList<>();
            this.headerValues = new ArrayList<>();
            for (Map.Entry<String, String> header : reply.getHeaders().entrySet()) {
                headerNames.add(header.getKey());
                headerValues.add(header.getValue());
            }
        }
        isPermanent = false;
    }

    public SettingViewModel(Integer code, String mimeType, String body, String customName, boolean isPermanent) {
        this.code = code;
        this.mimeType = mimeType;
        this.body = body;
        this.customName = customName;
        this.isPermanent = isPermanent;
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

    public Map<String, String> getHeaders() {
        if (this.headerValues == null || this.headerNames == null) {
            return null;
        }
        Map<String, String> headers = new HashMap<>();
        for (int i = 0; i < this.headerNames.size(); i++) {
            headers.put(headerNames.get(i), headerValues.get(i));
        }
        return Collections.unmodifiableMap(headers);
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public boolean isPermanent() {
        return isPermanent;
    }

    public void setPermanent(boolean permanent) {
        isPermanent = permanent;
    }
}
