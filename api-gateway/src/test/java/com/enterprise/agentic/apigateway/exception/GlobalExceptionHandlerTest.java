package com.enterprise.agentic.apigateway.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    @Test
    void shouldCatchExceptionsAndReturnInternalServerError() {
        ErrorWebExceptionHandler handler = new GlobalExceptionHandler();

        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost/api/orchestrator/test").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        handler.handle(exchange, new RuntimeException("boom")).block();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(exchange.getResponse().getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }
}
