package be.zlz.kara.bin.dto;

import be.zlz.kara.bin.domain.Bin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BinSettingsDto {
    private Integer code;

    private String mimeType;

    private String body;

    private Map<String, String> headers;

    private Map<String, String> cookies;

    private String customName;

    //todo add map (keep permanent for backwards compat)

    private boolean isPermanent;

    public BinSettingsDto() {
        this.headers = new HashMap<>();
        this.cookies = new HashMap<>();
    }

    public BinSettingsDto(Bin bin) {
        if(bin.getReply() != null){
            this.code = bin.getReply().getCode().value();
            this.mimeType = bin.getReply().getMimeType();
            this.body = bin.getReply().getBody();
            this.headers = bin.getReply().getHeaders();
            this.cookies = bin.getReply().getCookies();
        } else {
            this.headers = new HashMap<>();
            this.cookies = new HashMap<>();
        }
        this.customName = bin.getName();
        this.isPermanent = bin.isPermanent();
    }

    public BinSettingsDto(Integer code, String mimeType, String body, Map<String, String> headers, Map<String, String> cookies, String customName, boolean isPermanent) {
        this.code = code;
        this.mimeType = mimeType;
        this.body = body;
        this.headers = headers == null ? new HashMap<>() : headers;
        this.cookies = cookies == null ? new HashMap<>() : cookies;
        this.customName = customName;
        this.isPermanent = isPermanent;
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

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public void addHeader(String k, String v) {
        this.headers.put(k, v);
    }

    public Map<String, String> getCookies() {
        return Collections.unmodifiableMap(cookies);
    }

    public void addCookie(String k, String v) {
        this.cookies.put(k, v);
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
