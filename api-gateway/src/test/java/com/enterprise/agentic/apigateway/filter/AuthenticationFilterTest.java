package com.enterprise.agentic.apigateway.filter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationFilterTest {

    @Test
    void shouldRejectWhenAuthorizationMissingAndEnabled() {
        AuthenticationFilter filter = new AuthenticationFilter();
        AuthenticationFilter.Config config = new AuthenticationFilter.Config();
        config.setEnabled(true);
        config.setValidateToken(false);

        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost/api/orchestrator/test").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        GatewayFilterChain chain = Mockito.mock(GatewayFilterChain.class);
        Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

        filter.apply(config).filter(exchange, chain).block();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldRejectWhenAuthorizationInvalidAndValidateTokenEnabled() {
        AuthenticationFilter filter = new AuthenticationFilter();
        AuthenticationFilter.Config config = new AuthenticationFilter.Config();
        config.setEnabled(true);
        config.setValidateToken(true);

        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost/api/orchestrator/test")
                .header(HttpHeaders.AUTHORIZATION, "BadToken val")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        GatewayFilterChain chain = Mockito.mock(GatewayFilterChain.class);
        Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

        filter.apply(config).filter(exchange, chain).block();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldAllowWhenAuthorizationValid() {
        AuthenticationFilter filter = new AuthenticationFilter();
        AuthenticationFilter.Config config = new AuthenticationFilter.Config();
        config.setEnabled(true);
        config.setValidateToken(true);

        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost/api/orchestrator/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token-value")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        GatewayFilterChain chain = Mockito.mock(GatewayFilterChain.class);
        Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

        filter.apply(config).filter(exchange, chain).block();

        assertThat(exchange.getResponse().getStatusCode()).isNull();
        Mockito.verify(chain).filter(exchange);
    }
}
