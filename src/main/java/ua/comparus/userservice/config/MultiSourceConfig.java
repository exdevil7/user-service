package ua.comparus.userservice.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "data-sources")
@Validated
public class MultiSourceConfig {
    @NotEmpty
    @Valid
    private List<DataSourceProperties> configs = new ArrayList<>();

    public List<DataSourceProperties> getConfigs() {
        return configs;
    }

    public void setDataSources(List<DataSourceProperties> configs) {
        this.configs = configs;
    }
}
