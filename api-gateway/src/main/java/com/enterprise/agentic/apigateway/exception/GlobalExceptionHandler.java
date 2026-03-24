package com.enterprise.agentic.apigateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        log.error("Exception occurred in API Gateway: {}", throwable.getMessage(), throwable);

        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        String error = String.format(
                "{\"error\": \"%s\", \"status\": 500, \"message\": \"An error occurred in the gateway\"}",
                throwable.getMessage()
        );

        byte[] bytes = error.getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(
                Mono.fromCallable(() -> bufferFactory.wrap(bytes))
        );
    }
}