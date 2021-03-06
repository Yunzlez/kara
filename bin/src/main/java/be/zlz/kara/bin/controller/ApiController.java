package be.zlz.kara.bin.controller;

import be.zlz.kara.bin.domain.Bin;
import be.zlz.kara.bin.dto.BinDto;
import be.zlz.kara.bin.dto.BinListDto;
import be.zlz.kara.bin.dto.BinSettingsDto;
import be.zlz.kara.bin.dto.RequestDto;
import be.zlz.kara.bin.exceptions.ResourceNotFoundException;
import be.zlz.kara.bin.services.BinService;
import be.zlz.kara.bin.services.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private final BinService binService;

    private final RequestService requestService;

    private final Logger logger;

    @Autowired
    public ApiController(BinService binService, RequestService requestService) {
        logger = LoggerFactory.getLogger(ApiController.class);
        this.binService = binService;
        this.requestService = requestService;
    }

    @GetMapping("/bins")
    public List<BinListDto> getBins(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                    @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        return binService.listBins(page, limit);
    }

    @PostMapping("/bins")
    public ResponseEntity<?> createBin() {
        String name = UUID.randomUUID().toString();

        Bin bin = new Bin(name);
        binService.save(bin);
        logger.info("created bin with UUID {}", name);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/v1/bins/" + name + "/logs");
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    @GetMapping(value = "/bins/{uuid}/logs", produces = "application/json")
    @ResponseBody
    public BinDto getLogForUuidAsJson(@PathVariable String uuid,
                                      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                      @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                      HttpServletRequest request) {
        Bin bin = binService.getByName(uuid);
        if (bin == null) {
            throw new ResourceNotFoundException("Could not find bin with name " + uuid);
        }

        return binService.getPagedBinDto(bin, binService.buildRequestUrl(request, uuid), page, limit);
    }

    @DeleteMapping("/bins/{uuid}/logs")
    public ResponseEntity<?> clearBin(@PathVariable String uuid) {
        binService.clearBin(uuid);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/v1/bins/" + uuid + "/logs");
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    @GetMapping("/bins/{uuid}/settings")
    public ResponseEntity<BinSettingsDto> getSettings(@PathVariable String uuid) {
        return new ResponseEntity<>(binService.getApiBinSettings(uuid), HttpStatus.OK);
    }

    //todo test
    @PutMapping("/bins/{uuid}/settings")
    public ResponseEntity<?> updateSettings(@PathVariable String uuid, @RequestBody BinSettingsDto settings) {
        String newName = binService.updateSettings(uuid, settings);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/v1/bins/" + newName + "/logs");
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    @GetMapping(value = "/bins/{name}/requests")
    public List<RequestDto> getRequestsFor(@PathVariable String name,
                                           @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                           @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                           @RequestParam(name = "fields", required = false) String fields) {
        Bin bin = binService.getByName(name);
        if (bin == null) {
            throw new ResourceNotFoundException("Could not find bin with name " + name);
        }
        return requestService.getDtoWithFields(bin, page, limit, fields);
    }
}
