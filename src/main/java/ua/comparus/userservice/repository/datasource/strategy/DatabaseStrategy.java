package ua.comparus.userservice.repository.datasource.strategy;

import com.zaxxer.hikari.HikariDataSource;
import ua.comparus.userservice.config.DataSourceProperties;

public interface DatabaseStrategy {
    HikariDataSource createDataSource(DataSourceProperties config);
}
