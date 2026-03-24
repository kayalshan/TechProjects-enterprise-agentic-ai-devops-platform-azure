package com.enterprise.agentic.agentorchestrator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "orchestrator")
public class OrchestratorProperties {
    private String ragServiceUrl;
    private String llmServiceUrl;
    private String toolsServiceUrl;

    // Getters and setters
    public String getRagServiceUrl() {
        return ragServiceUrl;
    }

    public void setRagServiceUrl(String ragServiceUrl) {
        this.ragServiceUrl = ragServiceUrl;
    }

    public String getLlmServiceUrl() {
        return llmServiceUrl;
    }

    public void setLlmServiceUrl(String llmServiceUrl) {
        this.llmServiceUrl = llmServiceUrl;
    }

    public String getToolsServiceUrl() {
        return toolsServiceUrl;
    }

    public void setToolsServiceUrl(String toolsServiceUrl) {
        this.toolsServiceUrl = toolsServiceUrl;
    }
}