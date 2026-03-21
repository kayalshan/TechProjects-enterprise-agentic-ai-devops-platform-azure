# LLM Service – AI-Powered Log Analysis Engine

## Overview

The LLM Service is a core component of the Enterprise Agentic AI DevOps Platform.
It is responsible for analyzing application logs using Large Language Models (LLMs) and generating structured Root Cause Analysis (RCA) responses.

This service acts as a pure AI layer, abstracting multiple LLM providers such as:

* OpenAI
* Azure OpenAI
* Local LLMs (Ollama / LLaMA / Mistral)

---

## Objective

To convert raw log messages into:

* Root cause insights
* Impact analysis
* Severity classification
* Suggested remediation actions
* Tool execution recommendations

---

## Responsibilities

The LLM Service is responsible for:

* Understanding unstructured log messages
* Performing intelligent analysis using LLMs
* Generating structured RCA output (JSON)
* Supporting multiple LLM providers via abstraction
* Returning results in a format usable by the orchestrator

---

## What This Service Does Not Do

To maintain clean architecture:

* Does NOT call RAG service
* Does NOT call Tools service
* Does NOT orchestrate workflows

All orchestration is handled by the agent-orchestrator.

---

## How It Works

```text
Log + Context
     ↓
Build Prompt
     ↓
Call LLM Provider (OpenAI / Azure / Local)
     ↓
Parse JSON Response
     ↓
Return Structured RCA
```

---

## API Endpoints

### Analyze Log

POST `/llm/analyze`

#### Request:

```json
{
  "logMessage": "ERROR: DB timeout in payment-service",
  "context": "Past issue: DB overload during peak hours"
}
```

#### Response:

```json
{
  "rootCause": "Database overload",
  "impact": "Service latency increased",
  "severity": "HIGH",
  "suggestedAction": "Restart DB service",
  "toolName": "restart-service"
}
```

---

### Health Check

GET `/llm/ping`

---

## Architecture Role

```text
Agent Orchestrator → LLM Service → LLM Provider
```

* Orchestrator sends log and context
* LLM Service processes and returns RCA
* Orchestrator decides next steps

---

## Supported LLM Providers

| Provider     | Description                    |
| ------------ | ------------------------------ |
| OpenAI       | GPT-based models               |
| Azure OpenAI | Enterprise-grade hosted models |
| Local LLM    | Offline models via Ollama      |

---

## Configuration

```yaml
llm:
  provider: openai
  openai-url: https://api.openai.com
```

---

## Prompt Engineering

The service uses structured prompts to enforce JSON output:

```text
You are a DevOps AI assistant.
Analyze the log and return STRICT JSON with:
- rootCause
- impact
- severity
- suggestedAction
- toolName
```

---

## Tech Stack

* Java 17
* Spring Boot
* Spring WebFlux (Reactive)
* WebClient (Non-blocking HTTP client)
* Jackson (JSON parsing)

---

## Key Features

* Multi-LLM provider support
* Structured RCA output
* Prompt engineering for accuracy
* Reactive, non-blocking design
* Easily extensible

---

## Future Enhancements

* Add retry and circuit breaker (Resilience4j)
* Add response validation (JSON schema)
* Add token usage tracking
* Add observability (logs, metrics, tracing)

---

## Summary

The LLM Service is the intelligence layer of the platform, responsible for transforming raw logs into actionable insights, enabling automated and intelligent DevOps operations.

---


## Folder Structure
llm-service/
├── controller/
│   └── LlmController.java
│
├── service/
│   └── LlmService.java
│
├── client/
│   ├── OpenAiClient.java
│   ├── AzureOpenAiClient.java
│   └── LocalLlmClient.java
│
├── dto/
│   ├── LlmRequest.java
│   ├── RcaResponse.java
│
├── config/
│   ├── WebClientConfig.java
│   └── LlmProperties.java
│
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── LlmException.java
│
├── util/
│   └── JsonUtils.java
│
└── LlmServiceApplication.java