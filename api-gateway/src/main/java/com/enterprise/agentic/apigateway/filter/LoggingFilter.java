package com.enterprise.agentic.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();

        String requestUri = exchange.getRequest().getURI().toString();
        HttpMethod method = exchange.getRequest().getMethod();

        log.info(">>> Incoming Request: {} {} from {}", 
                method, 
                requestUri, 
                exchange.getRequest().getRemoteAddress());

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            int statusCode = exchange.getResponse().getStatusCode() != null ? 
                    exchange.getResponse().getStatusCode().value() : 0;

            log.info("<<< Response Completed: {} {} - Status: {} - Time: {}ms", 
                    method, 
                    requestUri, 
                    statusCode, 
                    duration);
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}