# Tools Service – Action Execution Engine

## Overview

The Tools Service is responsible for executing real-world actions based on decisions made by the LLM service. It acts as the execution layer of the agentic AI system, enabling automated remediation of issues identified during log analysis.

This service receives tool execution requests from the Agent Orchestrator and performs infrastructure or operational actions such as restarting services, scaling applications, or sending alerts.

---

## Objective

To translate AI-driven decisions into real system actions, enabling automated incident resolution and DevOps operations.

---

## Responsibilities

The Tools Service is responsible for:

* Executing actions based on toolName received from the orchestrator
* Supporting multiple pluggable tool executors
* Interacting with infrastructure systems (e.g., Kubernetes)
* Returning execution status and messages
* Providing a scalable and extensible execution framework

---

## What This Service Does Not Do

* Does NOT analyze logs
* Does NOT call LLM service
* Does NOT retrieve context (RAG)
* Does NOT orchestrate workflows

All decision-making is handled by the Agent Orchestrator.

---

## Architecture

```text
Agent Orchestrator
        ↓
Tools Service
        ↓
Tool Executor (Factory Pattern)
        ↓
Infrastructure / External Systems
```

---

## Folder Structure

```text
tools-service/
├── controller/
│   └── ToolsController.java
│
├── service/
│   ├── ToolsService.java
│   └── ToolExecutorFactory.java
│
├── executor/
│   ├── ToolExecutor.java
│   ├── RestartServiceExecutor.java
│   ├── ScaleServiceExecutor.java
│   └── AlertExecutor.java
│
├── client/
│   └── KubernetesClient.java
│
├── dto/
│   ├── ToolRequest.java
│   └── ToolResponse.java
│
├── config/
│   └── AppConfig.java
│
├── exception/
│   ├── ToolsException.java
│   └── GlobalExceptionHandler.java
│
└── ToolsServiceApplication.java
```

---

## API Endpoints

### Execute Tool

POST `/tools/execute`

#### Request

```json
{
  "toolName": "restart-service",
  "target": "payment-service",
  "metadata": "optional"
}
```

#### Response

```json
{
  "status": "SUCCESS",
  "message": "Service restarted: payment-service"
}
```

---

### Health Check

GET `/tools/ping`

---

## Supported Tools

### Restart Service

* Restarts a Kubernetes service or pod
* Used when service is unresponsive

---

### Scale Service

* Scales service replicas
* Used during high load conditions

---

### Send Alert

* Sends alert/notification
* Used for critical incidents

---

## Execution Flow

1. Receive tool execution request
2. Identify tool using ToolExecutorFactory
3. Route request to corresponding executor
4. Execute action via infrastructure client
5. Return execution result

---

## Design Patterns Used

### Factory Pattern

* Dynamically selects appropriate tool executor
* Enables easy addition of new tools

---

### Strategy Pattern

* Each tool executor implements a common interface
* Encapsulates execution logic

---

## Tech Stack

* Java 17
* Spring Boot
* Spring WebFlux (Reactive)
* Reactor (Mono)
* Kubernetes (simulated client / extendable to real cluster)

---

## Key Features

* Pluggable tool execution framework
* Reactive, non-blocking execution
* Extensible architecture for new tools
* Clean separation of concerns
* Integration-ready with real infrastructure

---

## Future Enhancements

* Integrate real Kubernetes API (Fabric8 client)
* Add RBAC and security controls
* Add audit logging for executed actions
* Add retry and circuit breaker mechanisms
* Integrate alerting systems (Slack, Email, PagerDuty)
* Add role-based tool authorization

---

## Architecture Role

```text
LLM Service → Decision → Tools Service → Action
```

* LLM decides what action to take
* Tools Service executes the action

---

## Example Use Case

```text
Log: DB timeout error
        ↓
LLM: root cause identified + toolName = restart-service
        ↓
Tools Service: restart payment-service
        ↓
Issue resolved
```

---

## Summary

The Tools Service acts as the execution engine of the platform, enabling the system to move from analysis to real-world action. It is a critical component in achieving fully automated, agent-driven DevOps operations.

---
