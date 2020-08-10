package db.migration;

import be.zlz.kara.bin.domain.converter.MapToSmileConverter;
import be.zlz.kara.bin.domain.enums.Source;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class V5_0_1__MigrateRequests extends BaseJavaMigration {

    private static final Logger LOG = LoggerFactory.getLogger(V5_0_1__MigrateRequests.class);

    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
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
