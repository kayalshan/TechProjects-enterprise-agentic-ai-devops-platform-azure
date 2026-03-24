package com.enterprise.agentic.llmservice.service;

import com.enterprise.agentic.llmservice.client.*;
import com.enterprise.agentic.llmservice.config.LlmProperties;
import com.enterprise.agentic.llmservice.dto.LlmRequest;
import com.enterprise.agentic.llmservice.dto.RcaResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void shouldUseAzureClientForAzureProvider() {
        OpenAiClient openAiClient = Mockito.mock(OpenAiClient.class);
        AzureOpenAiClient azureClient = Mockito.mock(AzureOpenAiClient.class);
        LocalLlmClient localClient = Mockito.mock(LocalLlmClient.class);

        LlmProperties props = new LlmProperties();
        props.setProvider("azure");

        Mockito.when(azureClient.call(Mockito.any()))
                .thenReturn(Mono.just("{\"rootCause\":\"ok\",\"impact\":\"\",\"severity\":\"LOW\",\"suggestedAction\":\"\",\"toolName\":\"fetch-logs\"}"));

        LlmService service = new LlmService(openAiClient, azureClient, localClient, props);

        StepVerifier.create(service.analyze(new LlmRequest("msg", "ctx")))
                .expectNextMatches(r -> r.toolName().equals("fetch-logs"))
                .verifyComplete();

        Mockito.verify(azureClient, Mockito.times(1)).call(Mockito.any());
        Mockito.verify(openAiClient, Mockito.never()).call(Mockito.any());
        Mockito.verify(localClient, Mockito.never()).call(Mockito.any());
    }

    @Test
    void shouldUseLocalClientForLocalProvider() {
        OpenAiClient openAiClient = Mockito.mock(OpenAiClient.class);
        AzureOpenAiClient azureClient = Mockito.mock(AzureOpenAiClient.class);
        LocalLlmClient localClient = Mockito.mock(LocalLlmClient.class);

        LlmProperties props = new LlmProperties();
        props.setProvider("local");

        Mockito.when(localClient.call(Mockito.any()))
                .thenReturn(Mono.just("{\"rootCause\":\"ok\",\"impact\":\"\",\"severity\":\"LOW\",\"suggestedAction\":\"\",\"toolName\":\"check-health\"}"));

        LlmService service = new LlmService(openAiClient, azureClient, localClient, props);

        StepVerifier.create(service.analyze(new LlmRequest("msg", "ctx")))
                .expectNextMatches(r -> r.toolName().equals("check-health"))
                .verifyComplete();

        Mockito.verify(localClient, Mockito.times(1)).call(Mockito.any());
        Mockito.verify(openAiClient, Mockito.never()).call(Mockito.any());
        Mockito.verify(azureClient, Mockito.never()).call(Mockito.any());
    }

    @Test
    void shouldDefaultToOpenAiWhenProviderIsUnknown() {
        OpenAiClient openAiClient = Mockito.mock(OpenAiClient.class);
        AzureOpenAiClient azureClient = Mockito.mock(AzureOpenAiClient.class);
        LocalLlmClient localClient = Mockito.mock(LocalLlmClient.class);

        LlmProperties props = new LlmProperties();
        props.setProvider("random-provider");

        Mockito.when(openAiClient.call(Mockito.any()))
                .thenReturn(Mono.just("{\"rootCause\":\"ok\",\"impact\":\"\",\"severity\":\"LOW\",\"suggestedAction\":\"\",\"toolName\":\"restart-service\"}"));

        LlmService service = new LlmService(openAiClient, azureClient, localClient, props);

        StepVerifier.create(service.analyze(new LlmRequest("msg", "ctx")))
                .expectNextMatches(r -> r.toolName().equals("restart-service"))
                .verifyComplete();

        Mockito.verify(openAiClient, Mockito.times(1)).call(Mockito.any());
    }

    @Test
    void shouldBuildPromptCorrectly() {
        OpenAiClient openAiClient = Mockito.mock(OpenAiClient.class);
        AzureOpenAiClient azureClient = Mockito.mock(AzureOpenAiClient.class);
        LocalLlmClient localClient = Mockito.mock(LocalLlmClient.class);

        LlmProperties props = new LlmProperties();
        props.setProvider("openai");

        ArgumentCaptor<String> promptCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.when(openAiClient.call(promptCaptor.capture()))
                .thenReturn(Mono.just("{\"rootCause\":\"ok\",\"impact\":\"\",\"severity\":\"LOW\",\"suggestedAction\":\"\",\"toolName\":\"fetch-logs\"}"));

        LlmService service = new LlmService(openAiClient, azureClient, localClient, props);

        StepVerifier.create(service.analyze(new LlmRequest("DB outage", "production")))
                .expectNextCount(1)
                .verifyComplete();

        String prompt = promptCaptor.getValue();
        assertTrue(prompt.contains("DB outage"));
        assertTrue(prompt.contains("production"));
    }
}