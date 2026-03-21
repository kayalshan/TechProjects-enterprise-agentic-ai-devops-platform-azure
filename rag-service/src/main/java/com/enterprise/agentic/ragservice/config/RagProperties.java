package com.enterprise.agentic.ragservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rag")
public class RagProperties {

    private String embeddingUrl;
    private String embeddingDeployment;
    private String searchUrl;
    private String indexName;
    private String apiKey;

    // Getters and setters
    public String getEmbeddingUrl() {
        return embeddingUrl;
    }

    public void setEmbeddingUrl(String embeddingUrl) {
        this.embeddingUrl = embeddingUrl;
    }

    public String getEmbeddingDeployment() {
        return embeddingDeployment;
    }

    public void setEmbeddingDeployment(String embeddingDeployment) {
        this.embeddingDeployment = embeddingDeployment;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}