CREATE TABLE event
(
    id               VARCHAR(36) PRIMARY KEY,
    body             LONGBLOB   NULL DEFAULT NULL,
    method           TEXT       NULL DEFAULT NULL,
    source           TEXT       NULL DEFAULT NULL,
    location         TEXT       NULL DEFAULT NULL, -- path in HTTP
    metadata         BLOB       NULL DEFAULT NULL, -- Headers in HTTP/MQTT
    additional_data  BLOB       NULL DEFAULT NULL, -- Query params in HTTP
    content_type     TEXT       NULL DEFAULT NULL,
    event_time       TEXT       NULL DEFAULT NULL,
    origin           TEXT       NULL DEFAULT NULL,
    body_size        INT        NULL DEFAULT NULL,
    protocol_version TEXT       NULL DEFAULT NULL,
    bin_id           BIGINT(20) NOT NULL
);

CREATE INDEX idx_bin_id on event (bin_id);

CREATE TABLE response
(
    id              BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
    code            TEXT     NOT NULL,
    content_type    TEXT     NULL DEFAULT NULL,
    body            LONGBLOB NULL DEFAULT NULL,
    headers         BLOB     NULL DEFAULT NULL,
    response_type   TEXT     NOT NULL,
    response_origin TEXT     NOT NULL
);

DROP TABLE binary_request;

CREATE TABLE module_config
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    bin_id     INT     NOT NULL,
    module_key TEXT    NOT NULL,
    config     TEXT,
    sync       TINYINT NOT NULL
);

CREATE INDEX idx_bin_id_module_config ON module_config (bin_id);

CREATE TABLE module_event
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    bin_id    INT       NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    message   TEXT      NOT NULL,
    is_error  TINYINT   NOT NULL
);

CREATE INDEX idx_bin_id_module_event on module_event (bin_id);
