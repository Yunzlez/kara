package be.zlz.kara.bin.services;

import be.zlz.kara.bin.domain.Bin;
import be.zlz.kara.bin.domain.BinConfigKey;
import be.zlz.kara.bin.domain.Reply;
import be.zlz.kara.bin.domain.Request;
import be.zlz.kara.bin.dto.RequestDto;
import be.zlz.kara.bin.exceptions.BadRequestException;
import be.zlz.kara.bin.exceptions.ResourceNotFoundException;
import be.zlz.kara.bin.repositories.BinRepository;
import be.zlz.kara.bin.repositories.RequestRepository;
import be.zlz.kara.bin.util.PagingUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class RequestService {

    private final BinRepository binRepository;

    private final RequestRepository requestRepository;

    private final ReplyService replyService;

    private final Logger logger;

    private static final ObjectMapper om = new ObjectMapper();

    private static final ObjectMapper smileMapper = new ObjectMapper(new SmileFactory());

    @Value("${store.request.binary}")
    private boolean storeBinaryRequests;

    @Value("${permanent.bin.max.count}")
    private int maxRequestsForPermanentBin;

    @Value("${bin.max.count}")
    private int maxRequests;

    @Autowired
    public RequestService(BinRepository binRepository, RequestRepository requestRepository, ReplyService replyService) {
        logger = LoggerFactory.getLogger(this.getClass());
        this.binRepository = binRepository;
        this.requestRepository = requestRepository;
        this.replyService = replyService;
    }

    public Page<Request> getOrderedRequests(Bin bin, int page, int limit) {
        return requestRepository.getByBinOrderByRequestTimeDesc(bin, PagingUtils.getPageable(page, limit));
    }

    @Transactional
    public synchronized Pair<Reply, Request> createRequest(HttpServletRequest servletRequest, HttpEntity<byte[]> body, String uuid, Map<String, String> headers) {
        Request request = new Request();

        Bin bin = binRepository.getByName(uuid);

        validateRequest(body, bin);

        request.setBin(bin);

        request.setHeaders(headers);
        headers.remove("cookie"); //Cookie header is useless and breaks localhost because no dev app ever clears cookies and the header is a bazillion chars

        if (bin.isEnabled(BinConfigKey.BINARY_BODY)) {
            request.setBody(Base64.getEncoder().encodeToString(body.getBody()));
        } else {
            request.setBody(new String(body.getBody() == null ? new byte[0] : body.getBody()));
        }

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
        } else {
            return Pair.of(replyService.getDefaultReply(request), request);
        }
    }

    @Transactional
    public Request createMqttRequest(Map<String, String> headers, String body, String binName) {
        Request request = new Request();
        request.setMqtt(true);
        request.setBody(body);
        request.setHeaders(headers);
        request.setRequestTime(new Date());
        request.setProtocol("MQTT");

        Bin bin = binRepository.getByName(binName);

        if (bin == null) {
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

    private void validateRequest(HttpEntity<byte[]> body, Bin bin) {
        if (bin == null) {
            throw new ResourceNotFoundException("No bin with that name exists");
        }
        if (bin.getRequestCount() >= maxRequestsForPermanentBin && bin.isPermanent()) {
            throw new BadRequestException("You reached the limit for this bin. Permanent bins have a limit of " + maxRequestsForPermanentBin + " requests.");
        } else if (bin.getRequestCount() >= maxRequests) {
            throw new BadRequestException("You reached the limit for this bin. Bins have a limit of " + maxRequests + " requests.");
        }
        if (body.getBody() != null && body.getBody().length > 100000) {
            throw new BadRequestException("Body length is capped to 100000 bytes");
        }
    }

    private void storeRequest(Request request, Bin bin) {
        try {
            requestRepository.save(request);
        } catch (ConstraintViolationException cve) {
            logger.warn("Constraint violation:", cve);
            throw new BadRequestException(cve.getMessage());
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
        if (req.isMqtt()) {
            name = "MQTT";
        } else {
            name = req.getMethod().name();
        }

        binRepository.updateMetric(bin.getRequestMetric().getId(), name);
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
        jsonReply = reply.getBody();

        if (reply.isCustom()) {
            reply.getHeaders().forEach(httpHeaders::add);
        }

        //calculate the content-length. java string is UTF-16 so convert to UTF8 and count
        byte[] stringbytes = jsonReply.getBytes(StandardCharsets.UTF_8);
        httpHeaders.setContentLength(stringbytes.length);

        return new ResponseEntity<>(jsonReply, httpHeaders, reply.getCode());
    }

    public List<RequestDto> getDtoWithFields(Bin bin, int page, int limit, String fields) {
        List<Request> requests = getOrderedRequests(bin, page, limit).getContent();
        if (fields == null || fields.isEmpty()) {
            return requests.stream().map(request -> new RequestDto(
                    request.getMethod(),
                    request.getRequestTime(),
                    request.getBody(),
                    request.getHeaders(),
                    request.getProtocol(),
                    request.getQueryParams()
            )).collect(Collectors.toList());
        } else {
            String[] fieldArr = fields.split(",");
            Set<String> fieldSet = Arrays.stream(fieldArr).map(String::toLowerCase).collect(Collectors.toSet());
            return buildCustomDtos(requests, fieldSet);
        }
    }

    private List<RequestDto> buildCustomDtos(List<Request> requests, Set<String> fields) {
        boolean method = fields.contains("method");
        boolean requestTime = fields.contains("requesttime");
        boolean body = fields.contains("body");
        boolean headers = fields.contains("headers");
        boolean protocol = fields.contains("protocol");
        boolean queryParams = fields.contains("queryparams");

        List<RequestDto> result = new ArrayList<>();
        for (Request request : requests) {
            result.add(
                    new RequestDto(
                            method ? request.getMethod() : null,
                            requestTime ? request.getRequestTime() : null,
                            body ? request.getBody() : null,
                            headers ? request.getHeaders() : null,
                            protocol ? request.getProtocol() : null,
                            queryParams ? request.getQueryParams() : null
                    )
            );
        }
        return result;
    }
}
