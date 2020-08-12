package be.zlz.kara.bin.services;

import be.zlz.kara.bin.domain.Bin;
import be.zlz.kara.bin.domain.Reply;
import be.zlz.kara.bin.domain.Request;
import be.zlz.kara.bin.dto.RequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface RequestService {

    Page<Request> getOrderedRequests(Bin bin, int page, int limit);

    Pair<Reply, Request> createRequest(HttpServletRequest servletRequest, HttpEntity<byte[]> body, String uuid, Map<String, String> headers);

    Request createMqttRequest(Map<String, String> headers, String body, String binName);

    ResponseEntity<String> buildResponse(Reply reply, HttpServletResponse response);

    List<RequestDto> getDtoWithFields(Bin bin, int page, int limit, String fields);
}
