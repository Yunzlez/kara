package be.zlz.kara.bin.controller.api.v11

import be.zlz.kara.bin.domain.Bin
import be.zlz.kara.bin.dto.BinListDto
import be.zlz.kara.bin.dto.PagedList
import be.zlz.kara.bin.dto.v11.BinSettingsDto
import be.zlz.kara.bin.services.BinService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin
@RestController("Bin_v11_Controller")
@RequestMapping("/api/v1.1.0/bins")
open class BinController(
        private val binService: BinService
) {

    val logger by logger()

    @GetMapping
    open fun getBins(@RequestParam("page") page: Int, @RequestParam("limit") limit: Int): PagedList<BinListDto> {
        return binService.pagedBinList(page, limit)
    }

    @PostMapping
    open fun createBin(): ResponseEntity<Any> {
        val name = UUID.randomUUID().toString()

        val bin = Bin(name)
        binService.save(bin)
        logger.info("created bin with UUID {}", name)

        val headers = HttpHeaders()
        headers.add(HttpHeaders.LOCATION, "/api/v1.1.0/bins/$name")
        return ResponseEntity<Any>(headers, HttpStatus.SEE_OTHER)
    }

    @GetMapping("/{name}")
    open fun getBin(@PathVariable("name") name: String): BinSettingsDto? {
        return null
    }
}