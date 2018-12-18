CREATE INDEX header_req_id_idx ON request_headers (request_id);
CREATE INDEX param_req_id_idx ON request_query_params (request_id);