# Agent Orchestrator – AI Workflow Engine (Brain)

## Overview

The Agent Orchestrator is the central brain of the platform. It coordinates multiple services (RAG, LLM, Tools) to perform end-to-end automated log analysis and remediation.

It implements the **agentic workflow pattern**, where:

* Context is retrieved
* Intelligence is applied
* Actions are executed

This service is responsible for chaining all components together into a single intelligent pipeline.

---

## Objective

To orchestrate the complete AI-driven DevOps workflow:

* Analyze logs
* Identify root cause
* Decide action
* Execute remediation

---

## Responsibilities

The Agent Orchestrator is responsible for:

* Receiving log analysis requests
* Calling RAG service for context enrichment
* Calling LLM service for root cause analysis
* Calling Tools service for action execution
* Aggregating results and returning final response
* Managing workflow execution and error handling

---

## What This Service Does Not Do

* Does NOT directly analyze logs (handled by LLM)
* Does NOT retrieve context itself (handled by RAG)
* Does NOT execute actions directly (handled by Tools)

It only **coordinates and orchestrates**.

---

## Architecture

```text
User / API Gateway
        ↓
Agent Orchestrator
        ↓
RAG Service → Context
        ↓
LLM Service → Root Cause + Action
        ↓
Tools Service → Execute Action
        ↓
Final Response
```

---

## Folder Structure

```text
agent-orchestrator/
├── controller/
│   └── OrchestratorController.java
│
├── service/
│   └── AgentOrchestratorService.java
│
├── client/
│   ├── RagClient.java
│   ├── LlmClient.java
│   └── ToolsClient.java
│
├── dto/
│   ├── OrchestratorRequest.java
│   ├── OrchestratorResponse.java
│   └── LlmResponse.java
│
├── config/
│   ├── WebClientConfig.java
│   └── OrchestratorProperties.java
│
├── exception/
│   ├── OrchestratorException.java
│   └── GlobalExceptionHandler.java
│
└── AgentOrchestratorApplication.java
```

---

## API Endpoints

### Execute AI Workflow

POST `/orchestrator/analyze-log`

#### Request

```json
{
  "logMessage": "ERROR: DB timeout in payment-service"
}
```

#### Response

```json
{
  "rootCause": "Database overload",
  "actionTaken": "Service restarted: payment-service",
  "status": "SUCCESS"
}
```

---

### Health Check

GET `/orchestrator/ping`

---

## Execution Flow

1. Receive log message
2. Call RAG service → get context
3. Call LLM service → analyze log + context
4. Extract toolName and root cause
5. Call Tools service → execute action
6. Aggregate response
7. Return final result

---

## Service Communication

* RAG Service → `/rag/enrich`
* LLM Service → `/llm/analyze`
* Tools Service → `/tools/execute`

All communication is done using **WebClient (Reactive HTTP client)**.

---

## Design Patterns Used

### Orchestrator Pattern

* Central coordination of multiple microservices
* Enables agentic workflow execution

---

### Client Abstraction

* Each external service is wrapped in a dedicated client
* Promotes loose coupling

---

### Reactive Programming

* Non-blocking execution using Mono
* Scalable under high load

---

## Tech Stack

* Java 17
* Spring Boot
* Spring WebFlux (Reactive)
* WebClient
* Reactor (Mono)

---

## Key Features

* End-to-end AI workflow orchestration
* Reactive, non-blocking execution
* Clean service separation
* Scalable microservice communication
* Fault-tolerant with fallback handling

---

## Error Handling Strategy

* Handles failures in any downstream service
* Returns safe fallback response
* Prevents system crash due to partial failure

---

## Future Enhancements

* Add circuit breaker (Resilience4j)
* Add retry mechanism for service calls
* Add distributed tracing (OpenTelemetry)
* Add workflow state tracking
* Add async event-driven orchestration (Kafka)

---

## Architecture Role

```text
RAG → Context
LLM → Intelligence
TOOLS → Action
ORCHESTRATOR → Brain (Coordinates everything)
```

---

## Example Use Case

```text
Log: DB timeout error
        ↓
RAG: retrieves historical DB overload context
        ↓
LLM: identifies root cause + suggests restart-service
        ↓
Tools: restarts payment-service
        ↓
Orchestrator: returns final response
```

---

## Summary

The Agent Orchestrator is the core intelligence coordinator of the platform. It connects all services into a unified AI-driven pipeline, enabling automated log analysis and self-healing DevOps operations.

---
