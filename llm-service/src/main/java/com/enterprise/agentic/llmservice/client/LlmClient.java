package com.enterprise.agentic.llmservice.client;

public interface LlmClient {
    Mono<String> call(String prompt);
}