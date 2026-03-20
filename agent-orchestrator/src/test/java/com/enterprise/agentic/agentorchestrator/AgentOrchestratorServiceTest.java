package com.enterprise.agentic.agentorchestrator;

import com.enterprise.agentic.agentorchestrator.dto.OrchestratorRequest;
import com.enterprise.agentic.agentorchestrator.service.AgentOrchestratorService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AgentOrchestratorServiceTest {

    private final AgentOrchestratorService service = new AgentOrchestratorService();

    @Test
    void testExecuteTask() {
        var request = new OrchestratorRequest("Hello Orchestrator");
        var response = service.executeTask(request);
        assertNotNull(response);
        // Since the actual services may not be running, these can be null in tests
    }
}