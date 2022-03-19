CREATE TABLE IF NOT EXISTS temperature (
    id CHAR(36) NOT NULL,
    sensor_id CHAR(36) NOT NULL,
    celsius_registered DECIMAL(5,2) NOT NULL,
    timestamp BIGINT NOT NULL,
    PRIMARY KEY (id)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;