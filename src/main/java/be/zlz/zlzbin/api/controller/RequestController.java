package be.zlz.zlzbin.api.controller;

import be.zlz.zlzbin.api.Exceptions.ResourceNotFoundException;
import be.zlz.zlzbin.api.domain.Request;
import be.zlz.zlzbin.api.repositories.BinRepository;
import be.zlz.zlzbin.api.repositories.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RequestController {

    private RequestRepository requestRepository;

    private BinRepository binRepository;

    Logger logger;

    @Autowired
    public RequestController(RequestRepository requestRepository, BinRepository binRepository) {
        this.requestRepository = requestRepository;
        this.binRepository = binRepository;

        logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping(value = "/bin/{uuid}", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public Request handleRequest(HttpServletRequest servletRequest, HttpEntity<String> body,  @PathVariable String uuid, @RequestHeader Map<String, String> headers){
        Request request = new Request();

        request.setBin(binRepository.getByName(uuid));
        if(request.getBin() == null){
            throw new ResourceNotFoundException("No bin with that name exists");
        }

        request.setHeaders(headers);
        headers.remove("cookie"); //Cookie header is useless and breaks localhost because no dev app ever clears cookies and the header is a bazillion chars
        logger.debug("Headers = " + headers.toString());

        request.setBody(body.getBody());
        request.setMethod(servletRequest.getMethod());
        logger.debug("Method = " + servletRequest.getMethod());

        request.setProtocol(servletRequest.getProtocol());

        logger.debug(servletRequest.getQueryString());
        request.setQueryParams(extractQueryParams(servletRequest.getQueryString()));

        requestRepository.save(request);
        return request;
    }

    private Map<String, String> extractQueryParams(String queryString){
        if(queryString == null || "".equals(queryString)){
            return null;
        }

        Map<String, String> ret = new HashMap<>();

        String [] paramsWithNames = queryString.split("&");

        for (String param : paramsWithNames) {
            String[] paramKeyValue = param.split("=");
            ret.put(paramKeyValue[0], paramKeyValue[1]);
        }

        return ret;
    }
}
