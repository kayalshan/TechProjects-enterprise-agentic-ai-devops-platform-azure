package com.enterprise.agentic.agentorchestrator.service;

import com.enterprise.agentic.agentorchestrator.dto.OrchestratorRequest;
import com.enterprise.agentic.agentorchestrator.service.AgentOrchestratorService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AgentOrchestratorServiceTest {

    private final AgentOrchestratorService service = new AgentOrchestratorService();

    @Test
    void testProcess() {
        var request = new OrchestratorRequest("High memory usage detected", "payment-service");
        var response = service.process(request);

        assertNotNull(response);
        assertNotNull(response.rootCause());
        assertNotNull(response.impact());
        assertNotNull(response.severity());
        assertNotNull(response.suggestedAction());
        assertNotNull(response.toolTriggered());
        assertNotNull(response.actionResult());
        assertFalse(response.rootCause().isEmpty());
    }

    @Test
    void testProcessReturnsEqualForDifferentServices() {
        var requestA = new OrchestratorRequest("Disk I/O spike", "database-service");
        var requestB = new OrchestratorRequest("CPU spike", "cache-service");

        var responseA = service.process(requestA);
        var responseB = service.process(requestB);

        assertNotNull(responseA);
        assertNotNull(responseB);
        assertEquals(responseA, responseB);
    }
}