package be.zlz.kara.bin.controller;

import be.zlz.kara.bin.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CodeController {

    @RequestMapping(path = "/code/{code}", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<String> returnCode(@PathVariable("code") int code, @RequestBody(required = false) String response){
        ResponseEntity<String> resp;
        try{
            if(response == null){
                resp = new ResponseEntity<>(HttpStatus.valueOf(code));
            } else {
                resp = new ResponseEntity<>(response, HttpStatus.valueOf(code));
            }
        } catch (IllegalArgumentException e){
            throw new ResourceNotFoundException("Could not find this status code, Here's a 404 instead");
        }
        return resp;
    }
}
