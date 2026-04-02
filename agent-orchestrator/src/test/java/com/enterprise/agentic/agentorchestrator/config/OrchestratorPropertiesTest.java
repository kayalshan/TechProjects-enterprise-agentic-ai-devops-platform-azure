package com.enterprise.agentic.agentorchestrator.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrchestratorPropertiesTest {

    @Test
    void shouldSetAndGetValues() {
        OrchestratorProperties properties = new OrchestratorProperties();

        properties.setRagServiceUrl("http://localhost:8130");
        properties.setLlmServiceUrl("http://localhost:8120");
        properties.setToolsServiceUrl("http://localhost:8140");

        assertEquals("http://localhost:8130", properties.getRagServiceUrl());
        assertEquals("http://localhost:8120", properties.getLlmServiceUrl());
        assertEquals("http://localhost:8140", properties.getToolsServiceUrl());
    }
}
