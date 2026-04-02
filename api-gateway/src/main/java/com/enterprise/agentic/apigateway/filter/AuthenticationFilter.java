package com.enterprise.agentic.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

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
                if (!isValidJwt(token, config)) {
                    log.warn("JWT validation failed for request: {}", exchange.getRequest().getURI());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                log.debug("JWT validated for request: {}", exchange.getRequest().getURI());
            }

            log.info("Authentication passed for request: {} {}", 
                    exchange.getRequest().getMethod(), 
                    exchange.getRequest().getURI());

            return chain.filter(exchange);
        };
    }

    private boolean isValidJwt(String token, Config config) {
        if (token == null || token.isBlank()) {
            return false;
        }

        if (config.getJwtSecret() == null || config.getJwtSecret().length() < 32) {
            log.error("JWT validation enabled, but secret is missing or shorter than 32 characters");
            return false;
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(config.getJwtSecret().getBytes(StandardCharsets.UTF_8)))
                    .clockSkewSeconds(config.getClockSkewSeconds())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            if (claims.getSubject() == null || claims.getSubject().isBlank()) {
                return false;
            }

            if (config.getRequiredIssuer() != null && !config.getRequiredIssuer().isBlank()) {
                return config.getRequiredIssuer().equals(claims.getIssuer());
            }

            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("JWT parsing error: {}", ex.getMessage());
            return false;
        }
    }

    public static class Config {
        private boolean enabled = true;
        private boolean validateToken = false;
        private String jwtSecret = "";
        private String requiredIssuer = "";
        private long clockSkewSeconds = 30;

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

        public String getJwtSecret() {
            return jwtSecret;
        }

        public void setJwtSecret(String jwtSecret) {
            this.jwtSecret = jwtSecret;
        }

        public String getRequiredIssuer() {
            return requiredIssuer;
        }

        public void setRequiredIssuer(String requiredIssuer) {
            this.requiredIssuer = requiredIssuer;
        }

        public long getClockSkewSeconds() {
            return clockSkewSeconds;
        }

        public void setClockSkewSeconds(long clockSkewSeconds) {
            this.clockSkewSeconds = clockSkewSeconds;
        }
    }
}