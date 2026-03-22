package com.enterprise.agentic.agentorchestrator.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrchestratorPropertiesTest {

    @Test
    void shouldSetAndGetValues() {
        OrchestratorProperties properties = new OrchestratorProperties();

        properties.setRagServiceUrl("http://localhost:8082");
        properties.setLlmServiceUrl("http://localhost:8083");
        properties.setToolsServiceUrl("http://localhost:8084");

        assertEquals("http://localhost:8082", properties.getRagServiceUrl());
        assertEquals("http://localhost:8083", properties.getLlmServiceUrl());
        assertEquals("http://localhost:8084", properties.getToolsServiceUrl());
    }
}
