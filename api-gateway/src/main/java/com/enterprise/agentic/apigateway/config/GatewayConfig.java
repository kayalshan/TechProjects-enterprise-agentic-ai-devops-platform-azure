package com.enterprise.agentic.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("orchestrator-service", r -> r
                        .path("/api/orchestrator/**")
                        .uri("${gateway.agent-orchestrator-url}"))
                .route("llm-service", r -> r
                        .path("/api/llm/**")
                        .uri("${gateway.llm-service-url}"))
                .route("rag-service", r -> r
                        .path("/api/rag/**")
                        .uri("${gateway.rag-service-url}"))
                .route("tools-service", r -> r
                        .path("/api/tools/**")
                        .uri("${gateway.tools-service-url}"))
                .build();
    }
}