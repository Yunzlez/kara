package be.zlz.kara.bin.services

import be.zlz.kara.bin.domain.Bin
import be.zlz.kara.bin.domain.Event
import org.springframework.data.domain.Page
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.transaction.Transactional

interface EventService {

    fun logHttpEvent(id: String, headers: Map<String, String>, body: HttpEntity<ByteArray>, servletRequest: HttpServletRequest, response: HttpServletResponse): ResponseEntity<String>

    fun logHttpEvent(bin: Bin, headers: Map<String, String>, body: HttpEntity<ByteArray>, servletRequest: HttpServletRequest): Event

    fun logMqttEvent(headers: Map<String, String>, body: ByteArray, binName: String, topic: String?, origin: String?, proto: String?): Event?

    fun getOrderedRequests(bin: Bin, page: Int, limit: Int): Page<Event>

    fun deleteEventsForBin(bin: Bin)

    fun compactEventsInBin(bin: Bin, compactTo: Int)
}