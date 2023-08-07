-- UNLOGGED and TRUNCATE commands are PostgreSQL specific
CREATE UNLOGGED TABLE IF NOT EXISTS init_data
(
    start_timestamp TIMESTAMP    NOT NULL,
    end_timestamp   TIMESTAMP    NOT NULL,
    course_name     VARCHAR(255) NOT NULL,
    group_name      VARCHAR(255) NOT NULL
);

TRUNCATE init_data;
