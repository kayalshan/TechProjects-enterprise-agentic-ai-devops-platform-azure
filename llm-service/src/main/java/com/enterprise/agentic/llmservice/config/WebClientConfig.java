package com.enterprise.agentic.llmservice.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.*;

import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    private static final Logger log = LoggerFactory.getLogger(WebClientConfig.class);

    // ✅ Connection Pool Configuration
    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("custom-connection-pool")
                .maxConnections(100)                      // max concurrent connections
                .pendingAcquireMaxCount(500)              // max queued requests
                .pendingAcquireTimeout(Duration.ofSeconds(5))
                .maxIdleTime(Duration.ofSeconds(30))
                .maxLifeTime(Duration.ofMinutes(5))
                .build();
    }

    // ✅ HttpClient with Timeouts
    @Bean
    public HttpClient httpClient(ConnectionProvider provider) {
        return HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofSeconds(10))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                            .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS))
                );
    }

    // ✅ WebClient Builder (Reusable)
    @Bean
    public WebClient.Builder webClientBuilder(HttpClient httpClient) {

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))

                // Default headers
                .defaultHeader("Content-Type", "application/json")

                // Filters
                .filter(logRequest())
                .filter(logResponse())
                .filter(errorHandler());
    }

    // ✅ Logging Request
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            log.info("➡️ Request: {} {}", request.method(), request.url());
            request.headers()
                    .forEach((name, values) ->
                            values.forEach(value -> log.debug("{}={}", name, value)));
            return reactor.core.publisher.Mono.just(request);
        });
    }

    // ✅ Logging Response
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            log.info("⬅️ Response Status: {}", response.statusCode());
            return reactor.core.publisher.Mono.just(response);
        });
    }

    // ✅ Error Handling
    private ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {

            if (response.statusCode().isError()) {

                return response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("❌ Error Response: {}", errorBody);
                            return reactor.core.publisher.Mono.error(
                                    new RuntimeException("External API Error: " + errorBody)
                            );
                        });
            }

            return reactor.core.publisher.Mono.just(response);
        });
    }
}