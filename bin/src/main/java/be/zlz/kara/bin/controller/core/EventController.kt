package be.zlz.kara.bin.controller.core

import be.zlz.kara.bin.services.EventService
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CrossOrigin
@RestController
@RequestMapping("/in")
class EventController(
        private val eventService: EventService
) {


    @RequestMapping(value = ["/{id}/**"], method = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.HEAD])
    fun handleRequest(servletRequest: HttpServletRequest,
                      response: HttpServletResponse,
                      body: HttpEntity<ByteArray>,
                      @PathVariable id: String,
                      @RequestHeader headers: Map<String, String>
    ): ResponseEntity<String> {
        return eventService.logHttpEvent(id, headers, body, servletRequest, response)
    }
}