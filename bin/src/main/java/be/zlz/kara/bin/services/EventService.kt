package be.zlz.kara.bin.services

import be.zlz.kara.bin.domain.Bin
import be.zlz.kara.bin.domain.Event
import be.zlz.kara.bin.domain.Reply
import be.zlz.kara.bin.domain.Request
import be.zlz.kara.bin.domain.enums.Source
import be.zlz.kara.bin.exceptions.ResourceNotFoundException
import be.zlz.kara.bin.repositories.BinRepository
import be.zlz.kara.bin.repositories.EventRepository
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.transaction.Transactional

@Service
open class EventService(
        private val binRepository: BinRepository,
        private val eventRepository: EventRepository,
        private val replyService: ReplyService
) {

    @Transactional
    open fun logHttpEvent(id: String, headers: Map<String, String>, body: HttpEntity<ByteArray>, servletRequest: HttpServletRequest, response: HttpServletResponse): ResponseEntity<String> {
        val binOpt = binRepository.findBinByName(id)
        if (binOpt.isEmpty) {
            throw ResourceNotFoundException("No bin $id exists")
        }
        val bin = binOpt.get()
        val event = Event(
                UUID.randomUUID().toString(),
                body.body,
                HttpMethod.valueOf(servletRequest.method),
                Source.HTTP,
                servletRequest.servletPath,
                headers,
                parseParams(servletRequest.queryString),
                headers["Content-Type"],
                LocalDateTime.now(),
                headers["x-real-ip"],
                if (body.body == null) 0 else body.body.size.toLong(),
                servletRequest.protocol,
                bin
        )

        eventRepository.save(event)

        bin.lastRequest = Date()
        bin.requestCount++
        updateMetrics(bin, event)
        binRepository.save(bin)

        val reply = if (bin.reply == null) {
            replyService.getDefaultReply(event)
        } else {
            bin.reply
        }

        reply.headers.remove("Content-Type")
        reply.headers.remove("Content-Length")

        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType(reply.mimeType)

        if (reply.isCustom) {
            reply.headers.forEach(headers::add)
        }

        headers.contentLength = reply.body.toByteArray(Charsets.UTF_8).size.toLong()

        return ResponseEntity.status(reply.code).headers(headers).body(reply.body)
    }

    open fun logMqttEvent() {

    }

    private fun updateMetrics(bin: Bin, req: Event) {
        val name: String?
        name = if (req.source == Source.MQTT) {
            "MQTT"
        } else {
            req.method?.name
        }
        binRepository.updateMetric(bin.requestMetric.id, name)
    }

    private fun parseParams(queryString: String): Map<String, String>? {
        if (StringUtils.isEmpty(queryString)) {
            return null
        }

        val ret: MutableMap<String, String> = HashMap()
        val paramsWithNames = queryString.split("&").toTypedArray()
        for (param in paramsWithNames) {
            val paramKeyValue = param.split("=").toTypedArray()
            ret[paramKeyValue[0]] = paramKeyValue[1]
        }
        return ret
    }

}