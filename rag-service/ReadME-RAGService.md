# RAG Service – Context Enrichment Engine

## Overview

The RAG (Retrieval-Augmented Generation) Service is responsible for enriching incoming log messages with relevant contextual information before they are analyzed by the LLM service.

It uses a hybrid approach:

* Vector-based semantic retrieval (primary)
* In-memory keyword fallback (secondary)

This ensures both production-grade intelligence and local development flexibility.

---

## Objective

To enhance raw log messages with meaningful historical and contextual data, enabling more accurate root cause analysis by the LLM service.

---

## Responsibilities

The RAG Service is responsible for:

* Converting log messages into embeddings
* Performing semantic search using a vector database
* Retrieving top relevant historical contexts
* Providing fallback context when vector search is unavailable
* Returning enriched context to the orchestrator

---

## What This Service Does Not Do

* Does NOT call LLM service
* Does NOT execute tools
* Does NOT orchestrate workflows

All orchestration is handled by the agent-orchestrator.

---

## Architecture

```text
Log Message
    ↓
Embedding Client (Azure OpenAI)
    ↓
Vector Search Client (Azure AI Search)
    ↓
Top-K Context Results
    ↓
Fallback (In-Memory Repository if needed)
    ↓
Return Context
```

---

## Folder Structure

```text
rag-service/
├── controller/
│   └── RagController.java
│
├── service/
│   └── RagService.java
│
├── client/
│   ├── EmbeddingClient.java
│   └── VectorSearchClient.java
│
├── repository/
│   └── InMemoryRagRepository.java
│
├── dto/
│   ├── RagRequest.java
│   └── RagResponse.java
│
├── config/
│   ├── WebClientConfig.java
│   └── RagProperties.java
│
├── exception/
│   ├── RagException.java
│   └── GlobalExceptionHandler.java
│
├── util/
│   └── TextUtils.java
│
└── RagServiceApplication.java
```

---

## API Endpoints

### Enrich Log with Context

POST `/rag/enrich`

#### Request

```json
{
  "logMessage": "ERROR: DB timeout in payment-service"
}
```

#### Response

```json
{
  "context": "Database overload observed during peak hours. Previous fix involved scaling DB and restarting connections."
}
```

---

### Health Check

GET `/rag/ping`

---

## Core Flow

1. Receive log message
2. Generate embedding using Azure OpenAI
3. Perform vector search using Azure AI Search
4. Retrieve top-K similar contexts
5. If vector search fails, fallback to in-memory lookup
6. Return enriched context

---

## Vector-Based Retrieval (Primary)

* Uses embeddings to understand semantic meaning
* Retrieves similar past incidents even if wording differs
* Enables intelligent and scalable context retrieval

---

## Fallback Strategy (Secondary)

If vector DB is unavailable:

* Uses keyword-based matching
* Ensures system reliability
* Supports local/offline environments

---

## Configuration

```yaml
rag:
  embedding-url: https://your-azure-openai-endpoint
  search-url: https://your-search-service.search.windows.net
  api-key: YOUR_KEY
```

---

## Tech Stack

* Java 17
* Spring Boot
* Spring WebFlux (Reactive)
* WebClient (Non-blocking HTTP client)
* Azure OpenAI (Embeddings)
* Azure AI Search (Vector Database)

---

## Key Features

* Semantic search using vector embeddings
* Hybrid architecture with fallback mechanism
* Reactive and scalable design
* Pluggable vector database support
* Production-ready separation of concerns

---

## Future Enhancements

* Replace fallback with Redis cache
* Add support for multiple vector DBs (Pinecone, Weaviate)
* Add similarity scoring threshold
* Add observability (metrics, tracing)
* Add caching for frequent queries

---

## Architecture Role

```text
Agent Orchestrator → RAG Service → Context → LLM Service
```

* Orchestrator sends log
* RAG enriches with context
* LLM performs analysis

---

## Summary

The RAG Service acts as the context engine of the platform, enabling intelligent log analysis by providing relevant historical and semantic information before LLM processing.

---
