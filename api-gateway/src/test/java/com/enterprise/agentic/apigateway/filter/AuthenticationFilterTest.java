package com.enterprise.agentic.apigateway.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.Date;

class AuthenticationFilterTest {

    private static final String SECRET = "test-secret-for-jwt-validation-12345";

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
        config.setJwtSecret(SECRET);

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
        config.setJwtSecret(SECRET);

        String token = createToken(SECRET, "gateway-tests");

        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost/api/orchestrator/test")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        GatewayFilterChain chain = Mockito.mock(GatewayFilterChain.class);
        Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

        filter.apply(config).filter(exchange, chain).block();

        assertThat(exchange.getResponse().getStatusCode()).isNull();
        Mockito.verify(chain).filter(exchange);
    }

    @Test
    void shouldRejectWhenTokenSignatureIsInvalid() {
        AuthenticationFilter filter = new AuthenticationFilter();
        AuthenticationFilter.Config config = new AuthenticationFilter.Config();
        config.setEnabled(true);
        config.setValidateToken(true);
        config.setJwtSecret(SECRET);

        String token = createToken("different-secret-for-signature-check-123", "gateway-tests");

        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost/api/orchestrator/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        GatewayFilterChain chain = Mockito.mock(GatewayFilterChain.class);
        Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

        filter.apply(config).filter(exchange, chain).block();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private static String createToken(String secret, String issuer) {
        return Jwts.builder()
                .subject("test-user")
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }
}
