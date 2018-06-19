package be.zlz.kara.bin.controller;

import be.zlz.kara.bin.dto.ErrorDTO;
import be.zlz.kara.bin.services.DelayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DelayController {

    private final DelayService delayService;

    @Autowired
    public DelayController(DelayService delayService) {
        this.delayService = delayService;
    }

    @RequestMapping(path = "/delay/{ms}", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    private ResponseEntity delay(@PathVariable("ms") int ms){
        try{
            if(!delayService.delay(ms)){
                return new ResponseEntity<>(
                        new ErrorDTO("503", "Cannot delay response at this time, please try again later"),
                        HttpStatus.SERVICE_UNAVAILABLE
                );
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorDTO("400", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
