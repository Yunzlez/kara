package be.zlz.kara.bin.services

import be.zlz.kara.bin.domain.Event
import be.zlz.kara.bin.domain.Reply
import be.zlz.kara.bin.domain.Request
import be.zlz.kara.bin.dto.DefaultResponseDto
import be.zlz.kara.bin.util.ReplyBuilder
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service
class ReplyService {

    private val om = ObjectMapper()

    private val logger by logger()

    fun getDefaultReply(request: Request): Reply {
        val replyBuilder = ReplyBuilder()
        val defaultResponseDto = DefaultResponseDto(HttpStatus.OK, MediaType.APPLICATION_JSON_VALUE, null, request.headers, request.queryParams)
        var body: String? = ""
        try {
            body = om.writeValueAsString(defaultResponseDto)
        } catch (e: JsonProcessingException) {
            logger.error("Failed to serialize response, returning empty body", e)
        }
        return replyBuilder
                .setCode(HttpStatus.OK)
                .setMimeType(MediaType.APPLICATION_JSON_VALUE)
                .setCustom(false)
                .setBody(body)
                .build()
    }

    fun getDefaultReply(event: Event): Reply {
        val replyBuilder = ReplyBuilder()
        val defaultResponseDto = DefaultResponseDto(HttpStatus.OK, MediaType.APPLICATION_JSON_VALUE, null, event.metadata, event.additionalData)
        var body: String? = ""
        try {
            body = om.writeValueAsString(defaultResponseDto)
        } catch (e: JsonProcessingException) {
            logger.error("Failed to serialize response, returning empty body", e)
        }
        return replyBuilder
                .setCode(HttpStatus.OK)
                .setMimeType(MediaType.APPLICATION_JSON_VALUE)
                .setCustom(false)
                .setBody(body)
                .build()
    }
}