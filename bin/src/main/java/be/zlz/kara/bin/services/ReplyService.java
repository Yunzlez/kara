package be.zlz.kara.bin.services;

import be.zlz.kara.bin.domain.Reply;
import be.zlz.kara.bin.domain.Request;
import be.zlz.kara.bin.util.ReplyBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {
    
    public Reply fromRequest(Request request) {
        ReplyBuilder replyBuilder = new ReplyBuilder();

        return replyBuilder
                .setBody(request.getBody())
                .setCode(HttpStatus.OK)
                .setMimeType("application/json")
                .addAllHeaders(request.getHeaders())
                .setCustom(false)
                .build();
    }
}
