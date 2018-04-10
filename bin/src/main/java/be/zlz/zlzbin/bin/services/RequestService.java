package be.zlz.zlzbin.bin.services;

import be.zlz.zlzbin.bin.domain.Bin;
import be.zlz.zlzbin.bin.domain.BinaryRequest;
import be.zlz.zlzbin.bin.domain.Reply;
import be.zlz.zlzbin.bin.domain.Request;
import be.zlz.zlzbin.bin.exceptions.BadRequestException;
import be.zlz.zlzbin.bin.exceptions.ResourceNotFoundException;
import be.zlz.zlzbin.bin.repositories.BinRepository;
import be.zlz.zlzbin.bin.repositories.BinaryrequestRepository;
import be.zlz.zlzbin.bin.repositories.RequestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
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

    private Gson gson;

    private static ObjectMapper smileMapper = new ObjectMapper(new SmileFactory());

    @Value("${store.request.binary}")
    private boolean storeBinaryRequests;

    @Value("${permanent.bin.max.count}")
    private int maxRequestsForPermanentBin;

    @Value("${bin.max.count}")
    private int maxRequests;

    @Autowired
    public RequestService(BinRepository binRepository, RequestRepository requestRepository, ReplyService replyService, BinaryrequestRepository binaryrequestRepository) {
        logger = LoggerFactory.getLogger(this.getClass());
        this.binRepository = binRepository;
        this.requestRepository = requestRepository;
        this.replyService = replyService;
        this.binaryrequestRepository = binaryrequestRepository;
        gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
    }

    public Pair<Reply, Request> createRequest(HttpServletRequest servletRequest, HttpEntity<String> body, String uuid, Map<String, String> headers) {
        Request request = new Request();

        Bin bin = binRepository.getByName(uuid);

        validateRequest(body, bin);

        request.setBin(bin);

        request.setHeaders(headers);
        headers.remove("cookie"); //Cookie header is useless and breaks localhost because no dev app ever clears cookies and the header is a bazillion chars

        if (logger.isDebugEnabled()) {
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
        bin.setRequestCount(bin.getRequestCount() + 1);
        updateMetrics(bin, request);
        binRepository.save(bin);
        if (bin.getReply() != null) {
            return Pair.of(bin.getReply(), request);
        } else return Pair.of(replyService.fromRequest(request), request);
    }

    public Request createRequest(Map<String, String> headers, String body, String binName){
        Request request = new Request();
        request.setMqtt(true);
        request.setBody(body);
        request.setHeaders(headers);
        request.setRequestTime(new Date());
        request.setProtocol("MQTT");

        Bin bin = binRepository.getByName(binName);

        if(bin == null){
            logger.info("Got message for unknown bin {}", binName);
            return null;
        }

        request.setBin(bin);

        bin.setLastRequest(new Date());
        bin.setRequestCount(bin.getRequestCount() + 1);
        updateMetrics(bin, request);

        binRepository.save(bin);

        return requestRepository.save(request);
    }

    private void validateRequest(HttpEntity<String> body, Bin bin) {
        if (bin == null) {
            throw new ResourceNotFoundException("No bin with that name exists");
        }
        if (bin.getRequestCount() >= maxRequestsForPermanentBin && bin.isPermanent()) {
            throw new BadRequestException("You reached the limit for this bin. Permanent bins have a limit of " + maxRequestsForPermanentBin + " requests.");
        } else if (bin.getRequestCount() >= maxRequests) {
            throw new BadRequestException("You reached the limit for this bin. Bins have a limit of " + maxRequests + " requests.");
        }
        if (body.getBody() != null && body.getBody().length() > 100000) {
            throw new BadRequestException("Body length is capped to 100000");
        }
    }

    private void storeRequest(Request request, Bin bin) {
        if (storeBinaryRequests) {
            BinaryRequest binaryRequest = new BinaryRequest();
            binaryRequest.setBin(bin);
            try {
                binaryRequest.setBinaryRequest(smileMapper.writeValueAsBytes(request));
                binaryrequestRepository.save(binaryRequest);
            } catch (JsonProcessingException e) {
                throw new BadRequestException("could not store binary request", e);
            }
        } else {
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

    private void updateMetrics(Bin bin, Request req) {
        String name;
        if(req.isMqtt()){
            name = "MQTT";
        } else {
            name = req.getMethod().name();
        }

        if (bin.getRequestMetric().getCounts().containsKey(name)) {
            int cnt = bin.getRequestMetric().getCounts().get(name);
            bin.getRequestMetric().getCounts().put(name, cnt + 1);
        } else {
            bin.getRequestMetric().getCounts().put(name, 1);
        }
    }

    public ResponseEntity<String> buildResponse(Reply reply, HttpServletResponse response) {
        reply.getCookies().forEach((k, v) -> response.addCookie(new Cookie(k, v)));
        logger.debug("Added cookies");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(reply.getMimeType()));
        logger.debug("Set content-type header to " + httpHeaders.getContentType());

        reply.getHeaders().remove("content-type"); //set using another header
        reply.getHeaders().remove("content-length"); //calculated

        String jsonReply;

        if (reply.isCustom()) {
            jsonReply = reply.getBody();
        } else {
            jsonReply = gson.toJson(reply);
        }

        //calculate the content-length. java string is UTF-16 so convert to UTF8 and count
        byte[] stringbytes = jsonReply.getBytes(StandardCharsets.UTF_8);
        httpHeaders.setContentLength(stringbytes.length);

        reply.getHeaders().forEach(httpHeaders::add);

        return new ResponseEntity<>(jsonReply, httpHeaders, reply.getCode());
    }
}
