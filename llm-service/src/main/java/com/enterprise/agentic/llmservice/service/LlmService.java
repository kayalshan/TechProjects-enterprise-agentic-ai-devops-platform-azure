package com.enterprise.agentic.llmservice.service;

import com.enterprise.agentic.llmservice.dto.LlmRequest;
import com.enterprise.agentic.llmservice.dto.LlmResponse;
import com.enterprise.agentic.llmservice.enums.LlmProvider;
import com.enterprise.agentic.llmservice.exception.LlmServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.enterprise.agentic.llmservice.client.OpenAiClient;
import com.enterprise.agentic.llmservice.client.AzureOpenAiClient;
import com.enterprise.agentic.llmservice.client.LocalLlmClient;
import java.time.LocalDateTime;

@Service
public class LlmService {

    private static final Logger log = LoggerFactory.getLogger(LlmService.class);

    private final OpenAiClient openAiClient;
    private final AzureOpenAiClient azureClient;
    private final LocalLlmClient localClient;

    public LlmService(OpenAiClient openAiClient,
                      AzureOpenAiClient azureClient,
                      LocalLlmClient localClient) {
        this.openAiClient = openAiClient;
        this.azureClient = azureClient;
        this.localClient = localClient;
    }

    public LlmResponse processRequest(LlmRequest request) {

        long start = System.currentTimeMillis();

        try {
            LlmProvider provider = resolveProvider(request);

            String prompt = buildPrompt(request);

            String response;

            switch (provider) {
                case AZURE -> response = azureClient.call(prompt, request);
                case LOCAL -> response = localClient.call(prompt, request);
                case OPENAI -> response = openAiClient.call(prompt, request);
                default -> throw new LlmServiceException("Unsupported provider");
            }

            long latency = System.currentTimeMillis() - start;

            return new LlmResponse(
                    response,
                    provider.name(),
                    request.model(),
                    estimateTokens(prompt, response),
                    latency,
                    LocalDateTime.now()
            );

        } catch (Exception ex) {
            log.error("LLM processing failed", ex);
            throw new LlmServiceException("LLM processing failed", ex);
        }
    }

    private LlmProvider resolveProvider(LlmRequest request) {
        if (request.provider() != null) {
            return LlmProvider.valueOf(request.provider().toUpperCase());
        }
        return LlmProvider.OPENAI;
    }

    private String buildPrompt(LlmRequest request) {
        StringBuilder prompt = new StringBuilder();

        if (request.context() != null) {
            prompt.append("Context:\n").append(request.context()).append("\n\n");
        }

        prompt.append("User Query:\n").append(request.query()).append("\n\nAnswer:");

        return prompt.toString();
    }

    private long estimateTokens(String input, String output) {
        return (input.length() + output.length()) / 4;
    }
}