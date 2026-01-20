package ua.comparus.userservice.repository.datasource;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import ua.comparus.userservice.config.DataSourceProperties;
import ua.comparus.userservice.config.MultiSourceConfig;
import ua.comparus.userservice.repository.datasource.strategy.DatabaseStrategy;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataSourceRegistry {

    private final List<DataSourceContext> contexts = new ArrayList<>();
    private final Map<String, DatabaseStrategy> strategies;
    private final MultiSourceConfig config;

    public DataSourceRegistry(Map<String, DatabaseStrategy> strategies, MultiSourceConfig config) {
        this.strategies = strategies;
        this.config = config;
    }

    @PostConstruct
    public void init() {
        List<DataSourceProperties> dataSources = config.getConfigs();
        if (dataSources == null || dataSources.isEmpty()) {
            throw new RuntimeException("No data sources found! Check your data-sources.yaml");
        }
        dataSources.stream()
                .map(this::createContext)
                .forEach(contexts::add);
    }

    private DataSourceContext createContext(DataSourceProperties dsConfig) {
        DatabaseStrategy strategy = strategies.get(dsConfig.strategy());
        if (strategy == null) {
            throw new RuntimeException("Strategy not found: " + dsConfig.strategy());
        }

        HikariDataSource dataSource = strategy.createDataSource(dsConfig);
        try (var _ = dataSource.getConnection()) {
            // successfully connected to verify
        } catch (SQLException e) {
            dataSource.close();
            throw new RuntimeException("Failed to connect to data source: " + dsConfig.name(), e);
        }

        JdbcClient jdbcClient = JdbcClient.create(dataSource);
        return new DataSourceContext(dsConfig, jdbcClient, dataSource);
    }

    public List<DataSourceContext> getAllContexts() {
        return contexts;
    }

    @PreDestroy
    public void shutdown() {
        contexts.forEach(ctx -> ctx.dataSource().close());
    }

    public record DataSourceContext(
            DataSourceProperties config,
            JdbcClient jdbcClient,
            HikariDataSource dataSource
    ) {
    }
}
