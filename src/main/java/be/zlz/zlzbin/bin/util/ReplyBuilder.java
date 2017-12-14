package be.zlz.zlzbin.bin.util;

import be.zlz.zlzbin.bin.domain.Reply;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ReplyBuilder {

    private HttpStatus code;

    private String mimeType;

    private String body;

    private Map<String, String> cookies;

    private Map<String, String> headers;

    private boolean custom = false;

    public ReplyBuilder(){
        cookies = new HashMap<>();
        headers = new HashMap<>();
    }

    public ReplyBuilder setCode(HttpStatus code){
        this.code = code;
        return this;
    }

    public ReplyBuilder setMimeType(String mimeType){
        this.mimeType = mimeType;
        return this;
    }

    public ReplyBuilder setBody(String body){
        this.body = body;
        return this;
    }

    public ReplyBuilder setCustom(boolean custom){
        this.custom = custom;
        return this;
    }

    public ReplyBuilder addCookie(String name, String value){
        cookies.put(name, value);
        return this;
    }

    public ReplyBuilder addHeader(String name, String value){
        headers.put(name, value);
        return this;
    }

    public ReplyBuilder addAllHeaders(Map<String, String> headers){
        this.headers.putAll(headers);
        return this;
    }

    public Reply build(){
        return new Reply(code, mimeType, body, cookies, headers, custom);
    }
}
