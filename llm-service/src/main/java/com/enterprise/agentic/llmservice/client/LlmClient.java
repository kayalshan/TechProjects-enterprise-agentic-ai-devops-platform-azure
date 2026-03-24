package com.enterprise.agentic.llmservice.client;

import reactor.core.publisher.Mono;

public interface LlmClient {
    Mono<String> call(String prompt);
}