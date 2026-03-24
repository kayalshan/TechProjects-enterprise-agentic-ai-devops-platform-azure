# Enterprise Agentic AI DevOps Platform (Azure)

## Overview

This project is a production-grade Agentic AI platform designed to automate log analysis, root cause detection, and incident remediation using a microservices-based architecture.

The system leverages LLMs, Retrieval-Augmented Generation (RAG), and intelligent orchestration to create a self-healing DevOps platform.

It follows an **agentic architecture pattern**, where multiple specialized services collaborate to analyze, decide, and act autonomously.

---

## Key Objective

To build an intelligent system that:

* Accepts raw application logs
* Identifies root causes using AI
* Suggests or executes corrective actions
* Reduces manual DevOps effort

---

## High-Level Architecture

```text
Client
   ↓
API Gateway (Entry Point)
   ↓
Agent Orchestrator (Brain)
   ↓
RAG Service → Context Enrichment (Vector DB)
LLM Service → Root Cause + Decision
Tools Service → Action Execution
```

---

## End-to-End Flow

```text
1. Client sends log message
2. API Gateway routes request
3. Orchestrator coordinates workflow
4. RAG fetches relevant context
5. LLM analyzes and decides action
6. Tools service executes action
7. Final response returned to client
```

---

## Repository Structure

```text
enterprise-agentic-ai-devops-platform-azure/
│
├── api-gateway/
├── agent-orchestrator/
├── llm-service/
├── rag-service/
├── tools-service/
├── common-lib/
│
├── infra/
│   ├── k8s/
│   ├── terraform/
│   └── helm/
│
├── docs/
│   ├── architecture.png
│   └── architecture.md
│
├── .github/workflows/
│   └── deploy.yml
│
├── docker-compose.yml
└── README.md
```

---

## Service-Level Breakdown

### API Gateway

* Entry point for all client requests
* Handles routing, authentication, logging

---

### Agent Orchestrator (Brain)

* Coordinates all services
* Executes full AI workflow
* Implements orchestrator pattern

---

### RAG Service

* Enriches logs with historical context
* Uses vector embeddings
* Integrates with Azure AI Search

---

### LLM Service

* Performs root cause analysis
* Generates fix recommendations
* Supports Azure OpenAI + Local LLM

---

### Tools Service

* Executes real-world actions
* Restart service, scale system, send alerts
* Uses pluggable executor pattern

---

## Detailed Service Flow

```text
Orchestrator
   ↓
RAG → fetch context from vector DB
   ↓
LLM → analyze log + context
   ↓
Tools → execute action
```

---

## Sample Input & Output

### Input

```json
{
  "logMessage": "ERROR: Database timeout in payment-service"
}
```

---

### Output

```json
{
  "rootCause": "Database connection pool exhausted due to high traffic",
  "actionTaken": "Service restarted: payment-service",
  "status": "SUCCESS"
}
```

---

## Features

* AI-powered log analysis
* Retrieval-Augmented Generation (RAG)
* Automated root cause detection
* Intelligent action execution
* Reactive microservices architecture
* Pluggable tool execution framework
* Vector database integration
* Kubernetes-ready deployment

---

## Tech Stack

### Core Technologies

* Java 17
* Spring Boot 3.x
* Spring WebFlux (Reactive)
* Maven (Multi-module)

---

### AI & Data

* Azure OpenAI (LLM + Embeddings)
* Azure AI Search (Vector Database)

---

### Infrastructure

* Docker
* Kubernetes (AKS-ready)
* Helm
* Terraform

---

### DevOps

* GitHub Actions (CI/CD)
* Docker Compose (Local setup)

---

## Deployment

### Local

```bash
docker-compose up
```

---

### Kubernetes

```bash
kubectl apply -f infra/k8s/
```

---

### CI/CD

* Automated builds via GitHub Actions
* Container deployment to Kubernetes

---

## Design Patterns Used

* Orchestrator Pattern
* Factory Pattern (Tools Service)
* Strategy Pattern (Tool Executors)
* Client Abstraction Pattern
* Reactive Programming Model

---

## Production-Grade Features

* Non-blocking reactive architecture
* Fault-tolerant service communication
* Config-driven environment setup
* Scalable microservices design
* Clean separation of concerns

---

## Future Enhancements

* Event-driven architecture using Kafka
* Circuit breaker (Resilience4j)
* Distributed tracing (OpenTelemetry)
* Role-based access control
* Multi-agent collaboration
* AI guardrails and validation

---

## Use Case

```text
Log: NullPointerException in PaymentService
        ↓
RAG: Retrieves similar past incidents
        ↓
LLM: Identifies root cause
        ↓
Tools: Executes restart-service
        ↓
System auto-recovers
```

---

## Summary

This project demonstrates how modern AI techniques can be integrated with microservices architecture to build a self-healing DevOps platform capable of intelligent decision-making and automated remediation.

---
