package com.enterprise.agentic.llmservice.service;

import com.enterprise.agentic.llmservice.client.AzureOpenAiClient;
import com.enterprise.agentic.llmservice.client.LlmClient;
import com.enterprise.agentic.llmservice.client.LocalLlmClient;
import com.enterprise.agentic.llmservice.client.OpenAiClient;
import com.enterprise.agentic.llmservice.config.LlmProperties;
import com.enterprise.agentic.llmservice.dto.LlmRequest;
import com.enterprise.agentic.llmservice.dto.RcaResponse;
import com.enterprise.agentic.llmservice.enums.LlmProvider;
import com.enterprise.agentic.llmservice.util.JsonUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Service
public class LlmService {

    private final OpenAiClient openAiClient;
    private final AzureOpenAiClient azureClient;
    private final LocalLlmClient localClient;
    private final LlmProperties props;

    public LlmService(OpenAiClient openAiClient,
                      AzureOpenAiClient azureClient,
                      LocalLlmClient localClient,
                      LlmProperties props) {
        this.openAiClient = openAiClient;
        this.azureClient = azureClient;
        this.localClient = localClient;
        this.props = props;
    }

    public Mono<RcaResponse> analyze(LlmRequest request) {

        String prompt = buildPrompt(request.logMessage(), request.context());

        return getClient()
                .call(prompt)
                .map(JsonUtils::extractJson)
                .map(json -> JsonUtils.parse(json, RcaResponse.class));
    }

    // private LlmClient getClient() {
    //     return switch (props.getProvider()) {
    //         case "azure" -> azureClient;
    //         case "local" -> localClient;
    //         default -> openAiClient;
    //     };
    // }
    private LlmClient getClient() {

        LlmProvider provider = LlmProvider.from(props.getProvider());

        return switch (provider) {
            case AZURE -> azureClient;
            case LOCAL -> localClient;
            default -> openAiClient;
        };
    }
    private String buildPrompt(String log, String context) {

        return """
        You are a DevOps AI assistant.

        Analyze the log and return STRICT JSON only.

        Log:
        %s

        Context:
        %s

        Output:
        {
          "rootCause": "",
          "impact": "",
          "severity": "LOW|MEDIUM|HIGH",
          "suggestedAction": "",
          "toolName": "restart-service | fetch-logs | check-health"
        }
        """.formatted(log, context);
    }
}