package ua.comparus.userservice.config;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Map;

public record DataSourceProperties(
        @NotEmpty String name,
        @DefaultValue("postgres") String strategy,
        @NotEmpty String url,
        @NotEmpty String table,
        @NotEmpty String user,
        @NotEmpty String password,
        @NotNull Map<String, String> mapping
) {
}
