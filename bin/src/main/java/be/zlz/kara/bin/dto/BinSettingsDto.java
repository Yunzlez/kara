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

    private Map<String, Boolean> config;

    private boolean isPermanent;

    public BinSettingsDto() {
        this.headers = new HashMap<>();
        this.cookies = new HashMap<>();
        this.config = new HashMap<>();
    }

    public BinSettingsDto(Bin bin) {
        if (bin.getReply() != null) {
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
        this.config = bin.getConfiguration();
    }

    public BinSettingsDto(Integer code, String mimeType, String body, Map<String, String> headers, Map<String, String> cookies, String customName, Map<String, Boolean> config, boolean isPermanent) {
        this.code = code;
        this.mimeType = mimeType;
        this.body = body;
        this.headers = headers == null ? new HashMap<>() : headers;
        this.cookies = cookies == null ? new HashMap<>() : cookies;
        this.config = config == null ? new HashMap<>() : config;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public String getCustomName() {
        return customName;
    }

    public boolean isPermanent() {
        return isPermanent;
    }

    public Map<String, Boolean> getConfig() {
        return config;
    }
}
