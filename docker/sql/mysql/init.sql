CREATE TABLE IF NOT EXISTS user_table
(
    ldap_login VARCHAR(255) PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    surname    VARCHAR(255) NOT NULL
);

CREATE INDEX idx_user_table_name ON user_table (name);
CREATE INDEX idx_user_table_surname ON user_table (surname);

INSERT IGNORE INTO user_table (ldap_login, name, surname) VALUES
('jdoe2', 'John', 'Doe'),
('jdoe3', 'Jonathan', 'Doers'),
('asmith1', 'Alice', 'Smith');
