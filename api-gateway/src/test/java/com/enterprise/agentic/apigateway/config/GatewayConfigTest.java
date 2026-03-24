package com.enterprise.agentic.apigateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "gateway.agent-orchestrator-url=http://agent-orchestrator:8084",
        "gateway.llm-service-url=http://llm-service:8083",
        "gateway.rag-service-url=http://rag-service:8082",
        "gateway.tools-service-url=http://tools-service:8081"
})
class GatewayConfigTest {

    @Autowired
    private RouteLocator routeLocator;

    @Test
    void shouldCreateRoutes() {
        List<Route> routes = routeLocator.getRoutes().collectList().block();

        assertThat(routes).isNotNull();
        assertThat(routes).extracting(Route::getId)
                .containsExactlyInAnyOrder("agent-orchestrator", "llm-service", "rag-service", "tools-service");
    }
}
