package ua.comparus.userservice.repository.datasource.strategy.impl;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;
import ua.comparus.userservice.config.DataSourceProperties;
import ua.comparus.userservice.repository.datasource.strategy.DatabaseStrategy;

@Component("postgres")
public class PostgresStrategy implements DatabaseStrategy {

    @Override
    public HikariDataSource createDataSource(DataSourceProperties config) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(config.url());
        ds.setUsername(config.user());
        ds.setPassword(config.password());
        ds.setPoolName("HikariPool-" + config.name());
        ds.setMaximumPoolSize(5); // adjust based on needs
        return ds;
    }
}