package be.zlz.zlzbin.api.services;

import be.zlz.zlzbin.api.domain.Bin;
import be.zlz.zlzbin.api.domain.Reply;
import be.zlz.zlzbin.api.domain.Request;
import be.zlz.zlzbin.api.exceptions.BadRequestException;
import be.zlz.zlzbin.api.exceptions.ResourceNotFoundException;
import be.zlz.zlzbin.api.repositories.BinRepository;
import be.zlz.zlzbin.api.repositories.RequestRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class RequestService {

    private BinRepository binRepository;

    private RequestRepository requestRepository;

    private ReplyService replyService;

    private Logger logger;

    @Value("${permanent.bin.max.count}")
    private int maxRequestsForPermanentBin;

    @Autowired
    public RequestService(BinRepository binRepository, RequestRepository requestRepository, ReplyService replyService){
        logger = LoggerFactory.getLogger(this.getClass());
        this.binRepository = binRepository;
        this.requestRepository = requestRepository;
        this.replyService = replyService;
    }

    public Reply createRequest(HttpServletRequest servletRequest, HttpEntity<String> body, String uuid, Map<String, String> headers){
        Request request = new Request();

        Bin bin = binRepository.getByName(uuid);

        if (bin == null) {
            throw new ResourceNotFoundException("No bin with that name exists");
        }
        if(bin.getRequestCount() >= maxRequestsForPermanentBin){
            throw new BadRequestException("You reached the limit for this bin. Permanent bins have a limit of " + maxRequestsForPermanentBin + " requests.");
        }

        request.setBin(bin);

        request.setHeaders(headers);
        headers.remove("cookie"); //Cookie header is useless and breaks localhost because no dev app ever clears cookies and the header is a bazillion chars

        if(logger.isDebugEnabled()){
            logger.debug("Headers = " + headers);
        }

        if (body.getBody() != null && body.getBody().length() > 1000) {
            throw new BadRequestException("Body length is capped to 1000");
        }
        request.setBody(body.getBody());
        request.setMethod(servletRequest.getMethod());
        logger.debug("Method = " + servletRequest.getMethod());

        request.setProtocol(servletRequest.getProtocol());

        logger.debug(servletRequest.getQueryString());
        request.setQueryParams(extractQueryParams(servletRequest.getQueryString()));

        try {
            requestRepository.save(request);
        } catch (ConstraintViolationException cve) {
            logger.warn("Constraint violation:", cve);
            throw new BadRequestException(cve.getMessage());
        }

        bin.setLastRequest(new Date());
        bin.setRequestCount(bin.getRequestCount()+1);
        binRepository.save(bin);

        if(bin.getReply() !=null){
            return bin.getReply();
        }
        else return replyService.fromRequest(request);
    }

    private Map<String, String> extractQueryParams(String queryString) {
        if (queryString == null || "".equals(queryString)) {
            return null;
        }

        Map<String, String> ret = new HashMap<>();

        String[] paramsWithNames = queryString.split("&");

        for (String param : paramsWithNames) {
            String[] paramKeyValue = param.split("=");
            ret.put(paramKeyValue[0], paramKeyValue[1]);
        }

        return ret;
    }

}
