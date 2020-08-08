CREATE TABLE event(
    id TEXT PRIMARY KEY,
    body LONGBLOB NULL DEFAULT NULL,
    method TEXT NULL DEFAULT NULL,
    source TEXT NULL DEFAULT NULL,
    location TEXT NULL DEFAULT NULL,  -- path in HTTP
    metadata BLOB NULL DEFAULT NULL, -- Headers in HTTP/MQTT
    additional_data BLOB NULL DEFAULT NULL, -- Query params in HTTP
    content_type TEXT NULL DEFAULT NULL,
    event_time TEXT NULL DEFAULT NULL,
    origin_ip TEXT NULL DEFAULT NULL,
    body_size INT NULL DEFAULT NULL,
    bin_id TEXT
);

DROP TABLE binary_request;