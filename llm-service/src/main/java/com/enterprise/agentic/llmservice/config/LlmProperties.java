package com.enterprise.agentic.llmservice.config;

@Configuration
@ConfigurationProperties(prefix = "llm")
public class LlmProperties {

    private String provider;
    private String openaiUrl;
    private String azureUrl;
    private String azureDeployment;
    private String azureApiKey;
    private String openaiApiKey;
    private String localUrl;import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "llm")
public class LlmProperties {

    private final String provider;
    private final String openaiUrl;
    private final String azureUrl;
    private final String azureDeployment;
    private final String azureApiKey;
    private final String openaiApiKey;
    private final String localUrl;

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
    public String getOpenaiUrl() { return openaiUrl; }
    public String getAzureUrl() { return azureUrl; }
    public String getAzureDeployment() { return azureDeployment; }
    public String getAzureApiKey() { return azureApiKey; }
    public String getOpenaiApiKey() { return openaiApiKey; }
    public String getLocalUrl() { return localUrl; }
}