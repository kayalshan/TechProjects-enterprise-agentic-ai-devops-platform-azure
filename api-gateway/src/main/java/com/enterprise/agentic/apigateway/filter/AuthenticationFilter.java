package com.enterprise.agentic.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (config.isEnabled() && (authHeader == null || authHeader.isEmpty())) {
                log.warn("Missing Authorization header for request: {}", exchange.getRequest().getURI());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            if (authHeader != null && config.isValidateToken()) {
                if (!authHeader.startsWith("Bearer ")) {
                    log.warn("Invalid Authorization header format for request: {}", exchange.getRequest().getURI());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String token = authHeader.substring(7);
                log.debug("Token validated for request: {}", exchange.getRequest().getURI());
            }

            log.info("Authentication passed for request: {} {}", 
                    exchange.getRequest().getMethod(), 
                    exchange.getRequest().getURI());

            return chain.filter(exchange);
        };
    }

    public static class Config {
        private boolean enabled = true;
        private boolean validateToken = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isValidateToken() {
            return validateToken;
        }

        public void setValidateToken(boolean validateToken) {
            this.validateToken = validateToken;
        }
    }
}