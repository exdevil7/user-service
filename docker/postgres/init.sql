CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users
(
    user_id    VARCHAR(255) PRIMARY KEY DEFAULT uuid_generate_v4(),
    login   VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL
    );

CREATE INDEX idx_users_first_name ON users (first_name varchar_pattern_ops);
CREATE INDEX idx_users_last_name ON users (last_name varchar_pattern_ops);

INSERT INTO users (login, first_name, last_name) VALUES
('jdoe', 'John', 'Doe'),
('jdoe1', 'Jonathan', 'Doers'),
('asmith', 'Alice', 'Smith')
ON CONFLICT (login) DO NOTHING;
