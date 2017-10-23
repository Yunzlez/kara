package be.zlz.zlzbin.api.services;

import be.zlz.zlzbin.api.domain.Bin;
import be.zlz.zlzbin.api.domain.BinaryRequest;
import be.zlz.zlzbin.api.domain.Reply;
import be.zlz.zlzbin.api.domain.Request;
import be.zlz.zlzbin.api.exceptions.BadRequestException;
import be.zlz.zlzbin.api.exceptions.ResourceNotFoundException;
import be.zlz.zlzbin.api.repositories.BinRepository;
import be.zlz.zlzbin.api.repositories.BinaryrequestRepository;
import be.zlz.zlzbin.api.repositories.RequestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
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

    private final BinaryrequestRepository binaryrequestRepository;

    private Logger logger;

    private static ObjectMapper smileMapper = new ObjectMapper(new SmileFactory());

    @Value("${store.request.binary}")
    private boolean storeBinaryRequests;

    @Value("${permanent.bin.max.count}")
    private int maxRequestsForPermanentBin;

    @Value("${bin.max.count}")
    private int maxRequests;

    @Autowired
    public RequestService(BinRepository binRepository, RequestRepository requestRepository, ReplyService replyService, BinaryrequestRepository binaryrequestRepository){
        logger = LoggerFactory.getLogger(this.getClass());
        this.binRepository = binRepository;
        this.requestRepository = requestRepository;
        this.replyService = replyService;
        this.binaryrequestRepository = binaryrequestRepository;
    }

    public Reply createRequest(HttpServletRequest servletRequest, HttpEntity<String> body, String uuid, Map<String, String> headers){
        Request request = new Request();

        Bin bin = binRepository.getByName(uuid);

        validateRequest(body, bin);

        request.setBin(bin);

        request.setHeaders(headers);
        headers.remove("cookie"); //Cookie header is useless and breaks localhost because no dev app ever clears cookies and the header is a bazillion chars

        if(logger.isDebugEnabled()){
            logger.debug("Headers = " + headers);
        }

        request.setBody(body.getBody());
        request.setMethod(servletRequest.getMethod());
        logger.debug("Method = " + servletRequest.getMethod());

        request.setProtocol(servletRequest.getProtocol());

        logger.debug(servletRequest.getQueryString());
        request.setQueryParams(extractQueryParams(servletRequest.getQueryString()));

        storeRequest(request, bin);

        bin.setLastRequest(new Date());
        bin.setRequestCount(bin.getRequestCount()+1);
        updateMetrics(bin, request);
        binRepository.save(bin);
        if(bin.getReply() !=null){
            return bin.getReply();
        }
        else return replyService.fromRequest(request);
    }

    private void validateRequest(HttpEntity<String> body, Bin bin) {
        if (bin == null) {
            throw new ResourceNotFoundException("No bin with that name exists");
        }
        if(bin.getRequestCount() >= maxRequestsForPermanentBin && bin.isPermanent()){
            throw new BadRequestException("You reached the limit for this bin. Permanent bins have a limit of " + maxRequestsForPermanentBin + " requests.");
        }
        else if(bin.getRequestCount() >= maxRequests){
            throw new BadRequestException("You reached the limit for this bin. Bins have a limit of " + maxRequests + " requests.");
        }
        if (body.getBody() != null && body.getBody().length() > 1000) {
            throw new BadRequestException("Body length is capped to 1000");
        }
    }

    private void storeRequest(Request request, Bin bin) {
        if(storeBinaryRequests){
            BinaryRequest binaryRequest = new BinaryRequest();
            binaryRequest.setBin(bin);
            try {
                binaryRequest.setBinaryRequest(smileMapper.writeValueAsBytes(request));
                binaryrequestRepository.save(binaryRequest);
            } catch (JsonProcessingException e) {
                throw new BadRequestException("could not store binary request", e);
            }
        }
        else {
            try {
                requestRepository.save(request);
            } catch (ConstraintViolationException cve) {
                logger.warn("Constraint violation:", cve);
                throw new BadRequestException(cve.getMessage());
            }
        }
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

    private void updateMetrics(Bin bin, Request req){
        if(bin.getRequestMetric().getCounts().containsKey(req.getMethod().name())){
            int cnt = bin.getRequestMetric().getCounts().get(req.getMethod().name());
            bin.getRequestMetric().getCounts().put(req.getMethod().name(), cnt+1);
        }
        else{
            bin.getRequestMetric().getCounts().put(req.getMethod().name(), 1);
        }
    }

}
