package db.migration;

import be.zlz.kara.bin.domain.converter.MapToSmileConverter;
import be.zlz.kara.bin.domain.enums.Source;
import be.zlz.kara.bin.domain.enums.Interpretation;
import be.zlz.kara.bin.dto.v11.ResponseOrigin;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class V5_0_1__MigrateRequestsAndReplies extends BaseJavaMigration {

    private static final Logger LOG = LoggerFactory.getLogger(V5_0_1__MigrateRequestsAndReplies.class);

    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        try {
            requestToEvents(connection);
            replyToResposne(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replyToResposne(Connection connection) throws SQLException {
        ResultSet rs = connection.prepareStatement("select * from reply;").executeQuery();
        PreparedStatement getHeaders = connection.prepareStatement("select * from reply_headers where reply_id=?");

        PreparedStatement insertResponse = connection.prepareStatement("insert into " +
                "response(id, code, content_type, body, headers, response_type, response_origin) value (?, ?, ?, ?, ?, ?, ?);");

        MapToSmileConverter converter = new MapToSmileConverter();

        int i = 0;
        while (rs.next()) {
            byte[] body = rs.getString("body").getBytes();
            HttpStatus code = HttpStatus.resolve(rs.getInt("code"));

            getHeaders.setLong(1, rs.getLong("id"));
            ResultSet headersRs = getHeaders.executeQuery();
            Map<String, String> headers = toHeadersMap(headersRs);

            insertResponse.setInt(1, rs.getInt("id"));
            insertResponse.setString(2, code == null ? HttpStatus.OK.name() : code.name());
            insertResponse.setString(3, rs.getString("mime_type"));
            insertResponse.setBytes(4, body.length == 0 ? null : body);
            insertResponse.setBytes(5, converter.convertToDatabaseColumn(headers));
            insertResponse.setString(6, Interpretation.TEXT.name());
            insertResponse.setString(7, ResponseOrigin.CUSTOM.name());

            insertResponse.addBatch();
            if (++i % 100 == 0 || rs.isLast() || rs.isAfterLast()) {
                LOG.info("processed {} replies", i);
                insertResponse.executeBatch();
            }
        }
    }

    private void requestToEvents(Connection connection) throws SQLException {
        ResultSet rs = connection.prepareStatement("select * from request;").executeQuery();
        PreparedStatement getHeaders = connection.prepareStatement("select * from request_headers where request_id=?");
        PreparedStatement getParams = connection.prepareStatement("select * from request_query_params where request_id=?");
        PreparedStatement insertEvent = connection.prepareStatement("insert into " +
                "event(id, body, method, source, location, metadata, additional_data, content_type, event_time, origin, body_size, protocol_version, bin_id) " +
                "value (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

        MapToSmileConverter converter = new MapToSmileConverter();

        int i = 0;
        while (rs.next()) {
            byte[] body = rs.getString("body").getBytes();
            boolean isMqtt = rs.getBoolean("mqtt");

            getHeaders.setLong(1, rs.getLong("id"));
            ResultSet headersRs = getHeaders.executeQuery();
            Map<String, String> headers = toHeadersMap(headersRs);

            getParams.setLong(1, rs.getLong("id"));
            ResultSet qpRs = getParams.executeQuery();
            Map<String, String> qps = toQPMap(qpRs);

            insertEvent.setString(1, UUID.randomUUID().toString());
            insertEvent.setBytes(2, body.length == 0 ? null : body);
            insertEvent.setString(3, mapMethod(rs.getInt("method")).name());
            insertEvent.setString(4, isMqtt ? Source.MQTT.name() : Source.HTTP.name());
            insertEvent.setString(5, null);
            insertEvent.setBytes(6, converter.convertToDatabaseColumn(headers));
            insertEvent.setBytes(7, converter.convertToDatabaseColumn(qps));
            insertEvent.setString(8, "");//content-type
            insertEvent.setTimestamp(9, rs.getTimestamp("request_time"));
            insertEvent.setString(10, isMqtt ? null : "");//x-real-ip
            insertEvent.setInt(11, body.length);
            insertEvent.setString(12, rs.getString("protocol"));
            insertEvent.setLong(13, rs.getLong("bin_id"));

            insertEvent.addBatch();
            if (++i % 100 == 0 || rs.isLast() || rs.isAfterLast()) {
                LOG.info("processed {} events", i);
                insertEvent.executeBatch();
            }
        }
    }

    private Map<String, String> toHeadersMap(ResultSet headersRs) throws SQLException {
        Map<String, String> headers = new HashMap<>();
        while (headersRs.next()) {
            headers.put(headersRs.getString("headers_key"), headersRs.getString("headers"));
        }
        return headers;
    }

    private Map<String, String> toQPMap(ResultSet qpRs) throws SQLException {
        Map<String, String> qps = new HashMap<>();
        while (qpRs.next()) {
            qps.put(qpRs.getString("query_params_key"), qpRs.getString("query_params"));
        }
        return qps;
    }

    private HttpMethod mapMethod(Integer method) {
        if (method == null) {
            return null;
        }
        return HttpMethod.values()[method];
    }
}
