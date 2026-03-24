package com.enterprise.agentic.agentorchestrator.controller;

import com.enterprise.agentic.agentorchestrator.dto.OrchestratorRequest;
import com.enterprise.agentic.agentorchestrator.dto.OrchestratorResponse;
import com.enterprise.agentic.agentorchestrator.service.AgentOrchestratorService;
import com.enterprise.agentic.common.dto.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class OrchestratorControllerTest {

    private AgentOrchestratorService service;
    private OrchestratorController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(AgentOrchestratorService.class);
        controller = new OrchestratorController(service);
    }

    @Test
    void shouldReturnAnalyzedOrchestratorResponse() {
        OrchestratorRequest request = new OrchestratorRequest("CPU spike detected", "inventory-service");
        OrchestratorResponse sampleResponse = new OrchestratorResponse(
                "CPU overload",
                "Performance degradation",
                "HIGH",
                "Restart service",
                "scale-out",
                "success"
        );

        Mockito.when(service.process(request)).thenReturn(sampleResponse);

        ResponseEntity<ApiResponse<OrchestratorResponse>> result = controller.analyze(request);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().data()).isEqualTo(sampleResponse);
        assertThat(result.getBody().message()).isEqualTo("SUCCESS");

        Mockito.verify(service).process(request);
    }

    @Test
    void shouldReturnPingStatus() {
        var response = controller.ping();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().data()).isEqualTo("Orchestrator is UP");
        assertThat(response.getBody().message()).isEqualTo("SUCCESS");
    }
}