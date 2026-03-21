package com.enterprise.agentic.ragservice.config;

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

    //  Connection Pool
    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("rag-connection-pool")
                .maxConnections(100)
                .pendingAcquireMaxCount(500)
                .pendingAcquireTimeout(Duration.ofSeconds(5))
                .maxIdleTime(Duration.ofSeconds(30))
                .maxLifeTime(Duration.ofMinutes(5))
                .build();
    }

    //  Http Client with timeouts
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

    //  WebClient Builder
    @Bean
    public WebClient.Builder webClientBuilder(HttpClient httpClient) {

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("Content-Type", "application/json")
                .filter(logRequest())
                .filter(logResponse())
                .filter(errorHandler());
    }

    //  Request Logging
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            log.info(" {} {}", request.method(), request.url());
            return reactor.core.publisher.Mono.just(request);
        });
    }

    //  Response Logging
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            log.info(" Status: {}", response.statusCode());
            return reactor.core.publisher.Mono.just(response);
        });
    }

    //  Error Handling
    private ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {

            if (response.statusCode().isError()) {
                return response.bodyToMono(String.class)
                        .flatMap(error -> {
                            log.error(" API Error: {}", error);
                            return reactor.core.publisher.Mono.error(
                                    new RuntimeException("External API Error: " + error)
                            );
                        });
            }

            return reactor.core.publisher.Mono.just(response);
        });
    }
}