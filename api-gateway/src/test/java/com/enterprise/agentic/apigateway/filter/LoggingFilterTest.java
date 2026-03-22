package com.enterprise.agentic.apigateway.filter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

class LoggingFilterTest {

    @Test
    void shouldCallChainAndLogViaFilter() {
        LoggingFilter loggingFilter = new LoggingFilter();

        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.GET, "http://localhost/api/orchestrator/status").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        GatewayFilterChain chain = Mockito.mock(GatewayFilterChain.class);
        Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

        loggingFilter.filter(exchange, chain).block();

        assertThat(exchange.getResponse().getStatusCode()).isNull();
        Mockito.verify(chain).filter(exchange);
        assertThat(loggingFilter.getOrder()).isEqualTo(-1);
    }
}
