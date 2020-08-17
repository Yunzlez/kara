package be.zlz.kara.bin.services;

import be.zlz.kara.bin.domain.Bin;
import be.zlz.kara.bin.domain.Event;
import be.zlz.kara.bin.domain.Reply;
import be.zlz.kara.bin.domain.Request;
import be.zlz.kara.bin.domain.enums.Source;
import be.zlz.kara.bin.dto.RequestDto;
import be.zlz.kara.bin.exceptions.BadRequestException;
import be.zlz.kara.bin.exceptions.ResourceNotFoundException;
import be.zlz.kara.bin.repositories.BinRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class RequestServiceImpl implements RequestService {

    private final BinRepository binRepository;

    private final EventService eventService;

    private final ReplyService replyService;

    private final Logger logger;

    @Value("${permanent.bin.max.count}")
    private int maxRequestsForPermanentBin;

    @Value("${bin.max.count}")
    private int maxRequests;

    @Autowired
    public RequestServiceImpl(BinRepository binRepository, EventService eventService, ReplyService replyService) {
        this.eventService = eventService;
        logger = LoggerFactory.getLogger(this.getClass());
        this.binRepository = binRepository;
        this.replyService = replyService;
    }

    @Override
    public Page<Request> getOrderedRequests(Bin bin, int page, int limit) {
        return fromEventPage(eventService.getOrderedRequests(bin, page, limit));
    }

    @Override
    public Pair<Reply, Request> createRequest(HttpServletRequest servletRequest, HttpEntity<byte[]> body, String uuid, Map<String, String> headers) {
        Bin bin = binRepository.getByName(uuid);
        validateRequest(body, bin);

        Event event = eventService.logHttpEvent(bin, headers, body, servletRequest);

        Request req = toRequest(event);

        if (bin.getReply() != null) {
            return Pair.of(bin.getReply(), req);
        } else {
            return Pair.of(replyService.getDefaultReply(req), req);
        }
    }

    @Override
    @Transactional
    public Request createMqttRequest(Map<String, String> headers, String body, String binName) {
        Event event = eventService.logMqttEvent(headers, body.getBytes(StandardCharsets.UTF_8), binName, null, null, null);
        return event == null ? null : toRequest(event);
    }

    private Page<Request> fromEventPage(Page<Event> events) {
        List<Request> requests = events.getContent().stream()
                .map(this::toRequest)
                .collect(Collectors.toList());

        return new PageImpl<>(requests, events.getPageable(), events.getTotalElements());
    }

    private Request toRequest(Event event) {
        return new Request(
                0L,
                event.getMethod(),
                Date.from(event.getEventTime().toInstant(ZoneOffset.UTC)),
                event.getBody() == null ? null : new String(event.getBody(), StandardCharsets.UTF_8),
                event.getMetadata(),
                event.getProtocolVersion(),
                event.getAdditionalData(),
                event.getBin(),
                event.getSource() == Source.MQTT
        );
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

    @Override
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

    @Override
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
