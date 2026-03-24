package com.enterprise.agentic.llmservice.client;

import com.enterprise.agentic.llmservice.config.LlmProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AzureOpenAiClientTest {

    @Test
    void shouldConstructAzureOpenAiClient() {
        WebClient.Builder builder = Mockito.mock(WebClient.Builder.class);
        WebClient webClient = Mockito.mock(WebClient.class);

        Mockito.when(builder.baseUrl(Mockito.anyString())).thenReturn(builder);
        Mockito.when(builder.build()).thenReturn(webClient);

        LlmProperties props = new LlmProperties();
        props.setAzureUrl("http://localhost");
        props.setAzureDeployment("deployment");
        props.setAzureApiKey("key");

        AzureOpenAiClient client = new AzureOpenAiClient(builder, props);

        assertNotNull(client);
    }
}