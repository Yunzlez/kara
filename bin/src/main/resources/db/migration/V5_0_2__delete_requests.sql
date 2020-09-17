DROP TABLE request;
DROP TABLE request_headers;
DROP TABLE request_query_params;

DROP TABLE reply;
DROP TABLE reply_headers;
DROP TABLE request_query_params;

ALTER TABLE bin DROP COLUMN reply_id;

ALTER TABLE bin ADD COLUMN response_id BIGINT(20) DEFAULT NULL;