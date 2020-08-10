package be.zlz.kara.bin.controller.core

import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CrossOrigin
@RestController
@RequestMapping("/in")
class EventController {


    @RequestMapping(value = ["/{id}/**"], method = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH])
    fun handleRequest(servletRequest: HttpServletRequest,
                      response: HttpServletResponse,
                      body: HttpEntity<ByteArray>,
                      @PathVariable id: String,
                      @RequestHeader headers: Map<String, String>
    ): ResponseEntity<ByteArray> {
        //Pair<Reply, Request> replyRequestPair = requestService.createRequest(servletRequest, body, uuid, headers);
        //newRequest(replyRequestPair.getSecond(), uuid);
        //return requestService.buildResponse(replyRequestPair.getFirst(), response);
        return ResponseEntity.noContent().build()
    }
}