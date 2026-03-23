package com.enterprise.agentic.llmservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "llm")
public class LlmProperties {

    private String provider;
    private String openaiUrl;
    private String azureUrl;
    private String azureDeployment;
    private String azureApiKey;
    private String openaiApiKey;
    private String localUrl;

    public LlmProperties() {
        // no-arg constructor for tests and non-constructor binding usages
    }

    @ConstructorBinding
    public LlmProperties(String provider, String openaiUrl, String azureUrl,
                         String azureDeployment, String azureApiKey,
                         String openaiApiKey, String localUrl) {
        this.provider = provider;
        this.openaiUrl = openaiUrl;
        this.azureUrl = azureUrl;
        this.azureDeployment = azureDeployment;
        this.azureApiKey = azureApiKey;
        this.openaiApiKey = openaiApiKey;
        this.localUrl = localUrl;
    }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getOpenaiUrl() { return openaiUrl; }
    public void setOpenaiUrl(String openaiUrl) { this.openaiUrl = openaiUrl; }

    public String getAzureUrl() { return azureUrl; }
    public void setAzureUrl(String azureUrl) { this.azureUrl = azureUrl; }

    public String getAzureDeployment() { return azureDeployment; }
    public void setAzureDeployment(String azureDeployment) { this.azureDeployment = azureDeployment; }

    public String getAzureApiKey() { return azureApiKey; }
    public void setAzureApiKey(String azureApiKey) { this.azureApiKey = azureApiKey; }

    public String getOpenaiApiKey() { return openaiApiKey; }
    public void setOpenaiApiKey(String openaiApiKey) { this.openaiApiKey = openaiApiKey; }

    public String getLocalUrl() { return localUrl; }
    public void setLocalUrl(String localUrl) { this.localUrl = localUrl; }
}