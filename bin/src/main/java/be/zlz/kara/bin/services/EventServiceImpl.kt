package be.zlz.kara.bin.services

import be.zlz.kara.bin.config.logger
import be.zlz.kara.bin.domain.Bin
import be.zlz.kara.bin.domain.Event
import be.zlz.kara.bin.domain.enums.Interpretation
import be.zlz.kara.bin.domain.enums.Source
import be.zlz.kara.bin.dto.v11.ResponseOrigin
import be.zlz.kara.bin.exceptions.BadRequestException
import be.zlz.kara.bin.exceptions.ResourceNotFoundException
import be.zlz.kara.bin.repositories.BinRepository
import be.zlz.kara.bin.repositories.EventRepository
import be.zlz.kara.bin.util.PagingUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.transaction.Transactional

@Service
open class EventServiceImpl(
        private val binRepository: BinRepository,
        private val eventRepository: EventRepository,
        private val responseService: ResponseService,
        private val moduleService: ModuleService
) : EventService {

    private val log by logger()

    @Value("\${permanent.bin.max.count}")
    private val maxRequestsForPermanentBin = 0

    @Value("\${bin.max.count}")
    private val maxRequests = 0

    @Transactional
    override fun logHttpEvent(id: String, headers: Map<String, String>, body: HttpEntity<ByteArray>, servletRequest: HttpServletRequest, response: HttpServletResponse): ResponseEntity<String> {
        val binOpt = binRepository.findBinByName(id)
        if (binOpt.isEmpty) {
            throw ResourceNotFoundException("No bin $id exists")
        }
        val bin = binOpt.get()
        enforceLimits(bin, body.body)

       val event = logHttpEvent(bin, headers, body, servletRequest)

        val reply = moduleService.handleModulesForBin(bin, event)?:
        if (bin.response == null) {
            responseService.getDefaultResponse(event)
        } else {
            bin.response
        }

        val responseHeaders = HttpHeaders()

        if (reply.responseOrigin != ResponseOrigin.DEFAULT) {
            reply.headers?.forEach {
                responseHeaders.add(it.key, it.value)
            }
        }

        responseHeaders.contentType = MediaType.parseMediaType(reply.contentType!!)
        responseHeaders.contentLength = reply.body?.size?.toLong() ?: 0

        val bodyStr = if (reply.responseType == Interpretation.BINARY) {
            Base64.getEncoder().encodeToString(reply.body)
        } else {
            String(reply.body!!)
        }
        return ResponseEntity.status(reply.code).headers(responseHeaders).body(bodyStr)
    }

    @Transactional
    override fun logHttpEvent(bin: Bin, headers: Map<String, String>, body: HttpEntity<ByteArray>, servletRequest: HttpServletRequest): Event {
        val event = Event(
                UUID.randomUUID().toString(),
                body.body,
                HttpMethod.valueOf(servletRequest.method),
                Source.HTTP,
                servletRequest.servletPath,
                headers,
                parseParams(servletRequest.queryString),
                headers["content-type"],
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

        return event
    }

    override fun logMqttEvent(headers: Map<String, String>, body: ByteArray, binName: String, topic: String?, origin: String?, proto: String?): Event? {
        val binOpt = binRepository.findBinByName(binName)
        if (binOpt.isEmpty) {
            log.error("Event for unknown bin")
            return null
        }
        val bin = binOpt.get()
        val event = Event(
                UUID.randomUUID().toString(),
                body,
                null,
                Source.MQTT,
                topic,
                headers,
                null,
                null,
                LocalDateTime.now(),
                origin,
                body.size.toLong(),
                proto,
                bin
        )

        eventRepository.save(event)

        moduleService.handleModulesForBin(bin, event)

        bin.lastRequest = Date()
        bin.requestCount++
        updateMetrics(bin, event)
        binRepository.save(bin)

        return event
    }

    private fun updateMetrics(bin: Bin, req: Event) {
        val name: String? = if (req.source == Source.MQTT) {
            "MQTT"
        } else {
            req.method?.name
        }
        binRepository.updateMetric(bin.requestMetric.id, name)
    }

    override fun getOrderedRequests(bin: Bin, page: Int, limit: Int): Page<Event> {
        return eventRepository.getByBinOrderByEventTimeDesc(bin, PagingUtils.getPageable(page, limit))
    }

    override fun deleteEventsForBin(bin: Bin) {
        eventRepository.deleteAllByBinNative(bin.id)
    }

    override fun compactEventsInBin(bin: Bin, compactTo: Int) {
        TODO("Not yet implemented")
    }

    private fun parseParams(queryString: String?): Map<String, String>? {
        if (queryString == null || StringUtils.isEmpty(queryString)) {
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

    private fun enforceLimits(bin: Bin, body: ByteArray?) {
        if (bin.requestCount >= maxRequestsForPermanentBin && bin.isPermanent) {
            throw BadRequestException("You reached the limit for this bin. Permanent bins have a limit of $maxRequestsForPermanentBin requests.")
        } else if (bin.requestCount >= maxRequests) {
            throw BadRequestException("You reached the limit for this bin. Bins have a limit of $maxRequests requests.")
        }
        if (body != null && body.size > 100000) { //todo setting
            throw BadRequestException("Body length is capped to 100000 bytes")
        }
    }

}