# TechProjects
# Enterprise Agentic AI DevOps Platform (Azure)

## Overview
This project demonstrates a production-grade Agentic AI platform built using Java and Azure.

It integrates:
- LLM (Azure OpenAI)
- RAG (Azure AI Search)
- Multi-agent orchestration
- Enterprise tools

## Architecture
Client → Gateway → Agent → LLM → RAG → Vector DB → Tools → Data

## Features
- AI-powered log analysis
- Automated root cause detection
- Fix recommendation engine
- Scalable microservices architecture
- Kubernetes deployment ready

## Tech Stack
Java | Spring Boot | Azure OpenAI | Azure AI Search | Docker | Kubernetes

## Use Case
Input:
"NullPointerException in PaymentService"

Output:
Root Cause + Fix Suggestion

## Deployment
- Dockerized services
- AKS deployment
- CI/CD via GitHub Actions

## Future Enhancements
- Multi-agent workflows
- Event-driven architecture
- AI guardrails