package ua.comparus.userservice.repository.datasource.strategy;

import com.zaxxer.hikari.HikariDataSource;
import ua.comparus.userservice.config.DataSourceProperties;
import ua.comparus.userservice.config.MultiSourceConfig;

public interface DatabaseStrategy {
    HikariDataSource createDataSource(DataSourceProperties config);
}
