CREATE TABLE module_config(
    id INT PRIMARY KEY AUTO_INCREMENT,
    bin_id INT NOT NULL,
    module_key TEXT NOT NULL,
    config TEXT,
    sync TINYINT NOT NULL
);

CREATE INDEX idx_bin_id_module_config ON module_config(bin_id);

CREATE TABLE module_event(
    id INT PRIMARY KEY AUTO_INCREMENT,
    bin_id INT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    message TEXT NOT NULL,
    is_error TINYINT NOT NULL
);

CREATE INDEX idx_bin_id_module_event on module_event(bin_id);