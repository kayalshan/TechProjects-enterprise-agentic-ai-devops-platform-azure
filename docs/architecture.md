# Architecture

## System Topology

The platform is a Spring Boot multi-service system fronted by a reactive API gateway.

```text
Client
  -> API Gateway (8100)
	  -> Agent Orchestrator (8110)
		  -> RAG Service (8130)
		  -> LLM Service (8120)
		  -> Tools Service (8140)
```

## Service Responsibilities

- API Gateway: edge routing, authentication filter, CORS policy, request logging.
- Agent Orchestrator: coordinates the analysis/execution workflow.
- RAG Service: enriches logs with retrieval context.
- LLM Service: produces root-cause analysis and suggested actions.
- Tools Service: executes remediation actions.

## Request Flow

1. Client sends request to the gateway endpoint.
2. Gateway validates authorization (JWT in prod profile), applies CORS rules, and routes traffic.
3. Orchestrator calls RAG and LLM services, then invokes Tools service when an action is required.
4. Orchestrator returns the final response through the gateway.

## External API Paths (Through Gateway)

- POST /api/orchestrator/analyze-log
- GET /api/orchestrator/ping
- POST /api/llm/analyze
- GET /api/llm/ping
- POST /api/rag/enrich
- GET /api/rag/ping
- POST /api/tools/execute
- GET /api/tools/ping

The gateway is configured with StripPrefix=1 for these routes so downstream service base paths are preserved.

## Production Security Notes

- JWT validation is enabled in the gateway prod profile.
- JWT secret is provided by JWT_SECRET environment variable.
- Optional issuer validation is controlled by JWT_ISSUER.
- Allowed CORS origins are configured via gateway.cors.allowed-origins or GATEWAY_ALLOWED_ORIGINS in prod.
- Generic internal exception responses avoid leaking stack-trace details to clients.
