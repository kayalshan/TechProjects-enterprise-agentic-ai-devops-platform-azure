package com.enterprise.agentic.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CorsConfig {

    @Value("#{'${gateway.cors.allowed-origins:http://localhost:3000,http://localhost:8100,https://localhost}'.split(',')}")
    private List<String> allowedOrigins = Arrays.asList(
            "http://localhost:3000",
            "http://localhost:8100",
            "https://localhost"
    );

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        List<String> sanitizedOrigins = allowedOrigins.stream()
                .map(String::trim)
                .filter(origin -> !origin.isBlank())
                .filter(origin -> !"*".equals(origin))
                .collect(Collectors.toList());

        if (sanitizedOrigins.isEmpty()) {
            throw new IllegalStateException("gateway.cors.allowed-origins must include at least one non-wildcard origin");
        }

        corsConfig.setAllowedOrigins(sanitizedOrigins);
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        corsConfig.setAllowedHeaders(Arrays.asList(
                "Content-Type",
                "Authorization",
                "X-Requested-With",
                "Accept",
                "Accept-Language"
        ));
        corsConfig.setExposedHeaders(Arrays.asList(
                "Content-Type",
                "Authorization",
                "X-Total-Count"
        ));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}