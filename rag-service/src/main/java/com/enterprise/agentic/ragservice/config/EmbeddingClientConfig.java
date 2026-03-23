package com.enterprise.agentic.ragservice.config;

import com.enterprise.agentic.ragservice.client.EmbeddingClient;
import com.enterprise.agentic.ragservice.client.VectorSearchClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class EmbeddingClientConfig {

    @Bean
    public EmbeddingClient embeddingClient(WebClient.Builder builder, RagProperties props) {
        return new EmbeddingClient(builder, props);
    }

    @Bean
    public VectorSearchClient vectorSearchClient(WebClient.Builder builder, RagProperties props) {
        return new VectorSearchClient(builder, props);
    }
}
