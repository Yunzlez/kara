package be.zlz.kara.bin.services;

import be.zlz.kara.bin.domain.Reply;
import be.zlz.kara.bin.domain.Request;
import be.zlz.kara.bin.dto.DefaultResponseDto;
import be.zlz.kara.bin.util.ReplyBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {

    private static final ObjectMapper om = new ObjectMapper();

    private Logger logger = LoggerFactory.getLogger(ReplyService.class);

    public Reply getDefaultReply(Request request) {
        ReplyBuilder replyBuilder = new ReplyBuilder();
        DefaultResponseDto defaultResponseDto =
                new DefaultResponseDto(HttpStatus.OK, MediaType.APPLICATION_JSON_VALUE, null, request.getHeaders(), request.getQueryParams());

        String body = "";

        try {
            body = om.writeValueAsString(defaultResponseDto);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize response, returning empty body", e);
        }

        return replyBuilder
                .setCode(HttpStatus.OK)
                .setMimeType(MediaType.APPLICATION_JSON_VALUE)
                .setCustom(false)
                .setBody(body)
                .build();
    }
}
