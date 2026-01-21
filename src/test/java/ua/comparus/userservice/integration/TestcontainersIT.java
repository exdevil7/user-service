package ua.comparus.userservice.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Testcontainers()
public abstract class TestcontainersIT {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:17-alpine"))
            .withInitScript("postgres/init.sql");

    @Container
    static MySQLContainer mysql = new MySQLContainer(DockerImageName.parse("mysql:8.4"))
            .withInitScript("mysql/init.sql");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("POSTGRES_DB_NAME", postgres::getDatabaseName);
        registry.add("POSTGRES_DB_URL", postgres::getJdbcUrl);

        registry.add("MYSQL_DB_NAME", mysql::getDatabaseName);
        registry.add("MYSQL_DB_URL", mysql::getJdbcUrl);

        registry.add("DB_USER", postgres::getUsername);
        registry.add("DB_PASSWORD", postgres::getPassword);
    }

}