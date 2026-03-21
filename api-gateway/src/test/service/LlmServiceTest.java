package com.enterprise.agentic.llmservice.service;

import com.enterprise.agentic.llmservice.client.*;
import com.enterprise.agentic.llmservice.config.LlmProperties;
import com.enterprise.agentic.llmservice.dto.LlmRequest;
import com.enterprise.agentic.llmservice.dto.RcaResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class LlmServiceTest {

    @Test
    void shouldAnalyzeLogSuccessfully() {

        OpenAiClient openAiClient = Mockito.mock(OpenAiClient.class);
        AzureOpenAiClient azureClient = Mockito.mock(AzureOpenAiClient.class);
        LocalLlmClient localClient = Mockito.mock(LocalLlmClient.class);

        LlmProperties props = new LlmProperties();
        props.setProvider("openai");

        String mockResponse = """
        {
          "rootCause": "DB overload",
          "impact": "Latency",
          "severity": "HIGH",
          "suggestedAction": "Restart DB",
          "toolName": "restart-service"
        }
        """;

        Mockito.when(openAiClient.call(Mockito.any()))
                .thenReturn(Mono.just(mockResponse));

        LlmService service = new LlmService(openAiClient, azureClient, localClient, props);

        Mono<RcaResponse> result = service.analyze(
                new LlmRequest("DB timeout", "context")
        );

        StepVerifier.create(result)
                .expectNextMatches(r ->
                        r.rootCause().equals("DB overload") &&
                        r.toolName().equals("restart-service")
                )
                .verifyComplete();
    }
}