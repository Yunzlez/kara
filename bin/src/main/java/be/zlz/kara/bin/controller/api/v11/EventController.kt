package be.zlz.kara.bin.controller.api.v11

import be.zlz.kara.bin.config.logger
import be.zlz.kara.bin.domain.Event
import be.zlz.kara.bin.dto.PagedList
import be.zlz.kara.bin.services.EventService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1.1.0/bins")
class EventController (
        eventService: EventService
) {

    val logger by logger()

    @GetMapping("/{name}/events")
    open fun getEventsForBin(
            @PathVariable("name") name: String,
            @RequestParam("page") page: Int,
            @RequestParam("limit") limit: Int,
            @RequestParam("fields") fields: String
    ): PagedList<Event>?{
        return null
    }
}