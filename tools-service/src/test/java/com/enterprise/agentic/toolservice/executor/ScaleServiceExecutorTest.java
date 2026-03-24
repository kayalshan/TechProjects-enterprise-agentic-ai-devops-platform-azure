package com.enterprise.agentic.toolservice.executor;

import com.enterprise.agentic.toolservice.client.KubernetesClient;
import com.enterprise.agentic.toolservice.dto.ToolsRequest;
import com.enterprise.agentic.toolservice.dto.ToolsResponse;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScaleServiceExecutorTest {

    private final KubernetesClient kubernetesClient = mock(KubernetesClient.class);
    private final ScaleServiceExecutor executor = new ScaleServiceExecutor(kubernetesClient);

    @Test
    void shouldReturnCorrectName() {
        assert(executor.getName().equals("scale-service"));
    }

    @Test
    void shouldExecuteScaleSuccessfully() {
        // Given
        ToolsRequest request = new ToolsRequest("scale-service", "payment-service", "3");
        when(kubernetesClient.scale(anyString(), anyInt())).thenReturn(Mono.empty());

        // When
        Mono<ToolsResponse> result = executor.execute(request);

        // Then
        StepVerifier.create(result)
                .expectNext(new ToolsResponse("SUCCESS", "Scaled service: payment-service to 3 replicas"))
                .verifyComplete();
    }

    @Test
    void shouldHandleInvalidReplicaCount() {
        // Given
        ToolsRequest request = new ToolsRequest("scale-service", "payment-service", "invalid");

        // When
        Mono<ToolsResponse> result = executor.execute(request);

        // Then
        StepVerifier.create(result)
                .expectNext(new ToolsResponse("FAILED", "Invalid replica count: invalid"))
                .verifyComplete();
    }

    @Test
    void shouldHandleScaleFailure() {
        // Given
        ToolsRequest request = new ToolsRequest("scale-service", "payment-service", "5");
        RuntimeException exception = new RuntimeException("Scaling failed");
        when(kubernetesClient.scale(anyString(), anyInt())).thenReturn(Mono.error(exception));

        // When
        Mono<ToolsResponse> result = executor.execute(request);

        // Then
        StepVerifier.create(result)
                .expectNext(new ToolsResponse("FAILED", "Failed to scale service: Scaling failed"))
                .verifyComplete();
    }
}