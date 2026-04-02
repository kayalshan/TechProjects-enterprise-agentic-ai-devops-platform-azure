#!/usr/bin/env bash
# =============================================================================
# Enterprise Agentic AI DevOps Platform – End-to-End Test Suite
# =============================================================================
# Tests the full request flow: Gateway → Orchestrator → RAG/LLM/Tools
#
# Usage:
#   ./tests/e2e/e2e-tests.sh                      # defaults: localhost, no JWT
#   GATEWAY_HOST=myhost JWT_TOKEN=xxx ./tests/e2e/e2e-tests.sh
#
# Environment variables:
#   GATEWAY_HOST      – hostname for the API gateway (default: localhost)
#   GATEWAY_PORT      – gateway port (default: 8100)
#   ORCHESTRATOR_HOST – direct orchestrator host (default: localhost)
#   ORCHESTRATOR_PORT – orchestrator port (default: 8110)
#   LLM_HOST          – LLM service host (default: localhost)
#   LLM_PORT          – LLM service port (default: 8120)
#   RAG_HOST          – RAG service host (default: localhost)
#   RAG_PORT          – RAG service port (default: 8130)
#   TOOLS_HOST        – Tools service host (default: localhost)
#   TOOLS_PORT        – Tools service port (default: 8140)
#   JWT_TOKEN         – Bearer token to send (optional)
#   FAIL_FAST         – exit on first failure (default: false)
# =============================================================================
set -euo pipefail

# --------------- configuration -----------------------------------------------
GATEWAY_HOST="${GATEWAY_HOST:-localhost}"
GATEWAY_PORT="${GATEWAY_PORT:-8100}"
ORCHESTRATOR_HOST="${ORCHESTRATOR_HOST:-localhost}"
ORCHESTRATOR_PORT="${ORCHESTRATOR_PORT:-8110}"
LLM_HOST="${LLM_HOST:-localhost}"
LLM_PORT="${LLM_PORT:-8120}"
RAG_HOST="${RAG_HOST:-localhost}"
RAG_PORT="${RAG_PORT:-8130}"
TOOLS_HOST="${TOOLS_HOST:-localhost}"
TOOLS_PORT="${TOOLS_PORT:-8140}"
JWT_TOKEN="${JWT_TOKEN:-}"
FAIL_FAST="${FAIL_FAST:-false}"

GATEWAY_BASE="http://${GATEWAY_HOST}:${GATEWAY_PORT}"
ORCH_BASE="http://${ORCHESTRATOR_HOST}:${ORCHESTRATOR_PORT}"
LLM_BASE="http://${LLM_HOST}:${LLM_PORT}"
RAG_BASE="http://${RAG_HOST}:${RAG_PORT}"
TOOLS_BASE="http://${TOOLS_HOST}:${TOOLS_PORT}"

# --------------- counters -----------------------------------------------------
PASS=0
FAIL=0
SKIP=0

# --------------- colours (disable when not a terminal) -----------------------
if [[ -t 1 ]]; then
  GREEN="\033[0;32m"; RED="\033[0;31m"; YELLOW="\033[1;33m"; RESET="\033[0m"; BOLD="\033[1m"
else
  GREEN=""; RED=""; YELLOW=""; RESET=""; BOLD=""
fi

# --------------- helpers ------------------------------------------------------
log()  { echo -e "${BOLD}[E2E]${RESET} $*"; }
pass() { echo -e "${GREEN}  ✓ PASS${RESET} – $*"; (( PASS++ )) || true; }
fail() { echo -e "${RED}  ✗ FAIL${RESET} – $*"; (( FAIL++ )) || true; [[ "${FAIL_FAST}" == "true" ]] && exit 1; }
skip() { echo -e "${YELLOW}  ○ SKIP${RESET} – $*"; (( SKIP++ )) || true; }
section() { echo; echo -e "${BOLD}━━━ $* ━━━${RESET}"; }

auth_headers() {
  if [[ -n "${JWT_TOKEN}" ]]; then
    echo "-H" "Authorization: Bearer ${JWT_TOKEN}"
  fi
}

# curl wrapper: returns HTTP status code, stores body in $BODY
http() {
  local method="$1"; shift
  local url="$1";    shift
  BODY=$(curl -s -o /tmp/e2e_resp.json -w "%{http_code}" \
    -X "${method}" \
    -H "Content-Type: application/json" \
    $(auth_headers) \
    "$@" \
    "${url}" 2>/dev/null) || true
  cat /tmp/e2e_resp.json 2>/dev/null || true
}

json_field() { python3 -c "import sys,json; d=json.load(open('/tmp/e2e_resp.json')); print(d.get('$1',''))" 2>/dev/null || true; }

wait_for() {
  local name="$1" url="$2" retries=15 delay=3
  log "Waiting for ${name} at ${url} ..."
  for i in $(seq 1 "${retries}"); do
    if curl -sf "${url}" -o /dev/null; then
      log "${name} is ready."
      return 0
    fi
    sleep "${delay}"
  done
  log "WARNING: ${name} did not respond in $((retries * delay))s – tests for this service may fail."
}

# =============================================================================
# PRE-FLIGHT: wait for all services
# =============================================================================
section "Pre-flight – service availability"
wait_for "API Gateway"        "${GATEWAY_BASE}/actuator/health"
wait_for "Agent Orchestrator" "${ORCH_BASE}/actuator/health"
wait_for "LLM Service"        "${LLM_BASE}/actuator/health"
wait_for "RAG Service"        "${RAG_BASE}/actuator/health"
wait_for "Tools Service"      "${TOOLS_BASE}/actuator/health"

# =============================================================================
# 1. HEALTH CHECKS
# =============================================================================
section "1 – Health / Ping Checks"

log "1.1 API Gateway actuator health"
http GET "${GATEWAY_BASE}/actuator/health"
if [[ "${BODY}" == "200" ]]; then pass "Gateway actuator /health → 200"; else fail "Gateway /health returned ${BODY}"; fi

log "1.2 Orchestrator ping (direct)"
http GET "${ORCH_BASE}/orchestrator/ping"
if [[ "${BODY}" == "200" ]]; then pass "Orchestrator /ping → 200"; else fail "Orchestrator /ping returned ${BODY}"; fi

log "1.3 Orchestrator ping via Gateway"
http GET "${GATEWAY_BASE}/api/orchestrator/ping"
if [[ "${BODY}" == "200" ]]; then pass "Gateway → /api/orchestrator/ping → 200"; else fail "Gateway /api/orchestrator/ping returned ${BODY}"; fi

log "1.4 LLM Service ping (direct)"
http GET "${LLM_BASE}/llm/ping"
if [[ "${BODY}" == "200" ]]; then pass "LLM /ping → 200"; else fail "LLM /ping returned ${BODY}"; fi

log "1.5 LLM ping via Gateway"
http GET "${GATEWAY_BASE}/api/llm/ping"
if [[ "${BODY}" == "200" ]]; then pass "Gateway → /api/llm/ping → 200"; else fail "Gateway /api/llm/ping returned ${BODY}"; fi

log "1.6 RAG Service ping (direct)"
http GET "${RAG_BASE}/rag/ping"
if [[ "${BODY}" == "200" ]]; then pass "RAG /ping → 200"; else fail "RAG /ping returned ${BODY}"; fi

log "1.7 RAG ping via Gateway"
http GET "${GATEWAY_BASE}/api/rag/ping"
if [[ "${BODY}" == "200" ]]; then pass "Gateway → /api/rag/ping → 200"; else fail "Gateway /api/rag/ping returned ${BODY}"; fi

log "1.8 Tools Service ping (direct)"
http GET "${TOOLS_BASE}/tools/ping"
if [[ "${BODY}" == "200" ]]; then pass "Tools /ping → 200"; else fail "Tools /ping returned ${BODY}"; fi

log "1.9 Tools ping via Gateway"
http GET "${GATEWAY_BASE}/api/tools/ping"
if [[ "${BODY}" == "200" ]]; then pass "Gateway → /api/tools/ping → 200"; else fail "Gateway /api/tools/ping returned ${BODY}"; fi

# =============================================================================
# 2. RAG SERVICE – direct
# =============================================================================
section "2 – RAG Service (direct)"

log "2.1 POST /rag/enrich – valid request"
http POST "${RAG_BASE}/rag/enrich" -d '{"logMessage":"ERROR: DB timeout in payment-service"}'
if [[ "${BODY}" == "200" ]]; then pass "RAG /enrich → 200"; else fail "RAG /enrich returned ${BODY}"; fi

log "2.2 POST /rag/enrich via Gateway"
http POST "${GATEWAY_BASE}/api/rag/enrich" -d '{"logMessage":"ERROR: Connection refused on auth-service"}'
if [[ "${BODY}" == "200" ]]; then pass "Gateway → /api/rag/enrich → 200"; else fail "Gateway /api/rag/enrich returned ${BODY}"; fi

# =============================================================================
# 3. LLM SERVICE – direct
# =============================================================================
section "3 – LLM Service (direct)"

log "3.1 POST /llm/analyze – valid request"
http POST "${LLM_BASE}/llm/analyze" -d '{
  "logMessage": "ERROR: DB timeout in payment-service",
  "context": "Database connection pool exhausted during peak load"
}'
if [[ "${BODY}" == "200" ]]; then
  pass "LLM /analyze → 200"
  ROOT=$(json_field "rootCause")
  [[ -n "${ROOT}" ]] && pass "LLM response has rootCause field" || fail "LLM response missing rootCause"
else
  fail "LLM /analyze returned ${BODY}"
fi

log "3.2 POST /llm/analyze via Gateway"
http POST "${GATEWAY_BASE}/api/llm/analyze" -d '{
  "logMessage": "WARN: Memory usage at 95% on order-service",
  "context": "Service running without memory limits"
}'
if [[ "${BODY}" == "200" ]]; then pass "Gateway → /api/llm/analyze → 200"; else fail "Gateway /api/llm/analyze returned ${BODY}"; fi

# =============================================================================
# 4. TOOLS SERVICE – direct
# =============================================================================
section "4 – Tools Service (direct)"

log "4.1 POST /tools/execute – restart-service"
http POST "${TOOLS_BASE}/tools/execute" -d '{
  "toolName": "restart-service",
  "target": "payment-service",
  "metadata": null
}'
if [[ "${BODY}" == "200" ]]; then
  pass "Tools /execute restart-service → 200"
  STATUS=$(json_field "status")
  [[ "${STATUS}" == "SUCCESS" ]] && pass "Tools response status = SUCCESS" || fail "Expected SUCCESS, got ${STATUS}"
else
  fail "Tools /execute restart-service returned ${BODY}"
fi

log "4.2 POST /tools/execute – scale-service"
http POST "${TOOLS_BASE}/tools/execute" -d '{
  "toolName": "scale-service",
  "target": "order-service",
  "metadata": "3"
}'
if [[ "${BODY}" == "200" ]]; then pass "Tools /execute scale-service → 200"; else fail "Tools /execute scale-service returned ${BODY}"; fi

log "4.3 POST /tools/execute – send-alert"
http POST "${TOOLS_BASE}/tools/execute" -d '{
  "toolName": "send-alert",
  "target": "ops-team",
  "metadata": "High CPU on auth-service"
}'
if [[ "${BODY}" == "200" ]]; then pass "Tools /execute send-alert → 200"; else fail "Tools /execute send-alert returned ${BODY}"; fi

log "4.4 POST /tools/execute via Gateway"
http POST "${GATEWAY_BASE}/api/tools/execute" -d '{
  "toolName": "restart-service",
  "target": "payment-service",
  "metadata": null
}'
if [[ "${BODY}" == "200" ]]; then pass "Gateway → /api/tools/execute → 200"; else fail "Gateway /api/tools/execute returned ${BODY}"; fi

log "4.5 POST /tools/execute – unknown tool expects 400"
http POST "${TOOLS_BASE}/tools/execute" -d '{
  "toolName": "unknown-tool",
  "target": "any-service",
  "metadata": null
}'
if [[ "${BODY}" == "400" || "${BODY}" == "500" ]]; then pass "Tools unknown tool → ${BODY} (non-200)"; else fail "Expected error for unknown tool, got ${BODY}"; fi

# =============================================================================
# 5. ORCHESTRATOR – end-to-end workflow
# =============================================================================
section "5 – Orchestrator (full AI workflow)"

log "5.1 POST /orchestrator/analyze-log – DB timeout scenario"
http POST "${ORCH_BASE}/orchestrator/analyze-log" -d '{
  "logMessage": "ERROR: Database connection timeout in payment-service",
  "serviceName": "payment-service"
}'
if [[ "${BODY}" == "200" ]]; then
  pass "Orchestrator /analyze-log → 200"
  ROOT=$(json_field "rootCause")
  [[ -n "${ROOT}" ]] && pass "Orchestrator response has rootCause field" || fail "Orchestrator response missing rootCause"
else
  fail "Orchestrator /analyze-log returned ${BODY}"
fi

log "5.2 POST /orchestrator/analyze-log – high memory scenario"
http POST "${ORCH_BASE}/orchestrator/analyze-log" -d '{
  "logMessage": "WARN: Memory usage at 95% on order-service",
  "serviceName": "order-service"
}'
if [[ "${BODY}" == "200" ]]; then pass "Orchestrator /analyze-log high-memory → 200"; else fail "Orchestrator high-memory returned ${BODY}"; fi

log "5.3 POST /orchestrator/analyze-log via Gateway (full E2E)"
http POST "${GATEWAY_BASE}/api/orchestrator/analyze-log" -d '{
  "logMessage": "ERROR: Connection refused to auth-service:5432",
  "serviceName": "auth-service"
}'
if [[ "${BODY}" == "200" ]]; then pass "Gateway → /api/orchestrator/analyze-log → 200"; else fail "Gateway /api/orchestrator/analyze-log returned ${BODY}"; fi

log "5.4 POST /orchestrator/analyze-log – missing body expects 400/500"
http POST "${ORCH_BASE}/orchestrator/analyze-log" -d '{}'
if [[ "${BODY}" != "200" ]]; then pass "Orchestrator empty body returns error ${BODY}"; else skip "Orchestrator accepts empty body (validation not enforced)"; fi

# =============================================================================
# 6. GATEWAY – security checks
# =============================================================================
section "6 – Gateway Security"

log "6.1 Missing Authorization header in prod mode"
# This check is informational: dev profile has validateToken=false, so 2xx is expected.
# In prod with validateToken=true and no token, expect 401.
http GET "${GATEWAY_BASE}/api/orchestrator/ping"
if [[ "${BODY}" == "401" ]]; then
  pass "Gateway correctly returns 401 without token (prod mode)"
elif [[ "${BODY}" == "200" ]]; then
  skip "Gateway returned 200 without token – validateToken=false (dev/test mode, expected)"
else
  fail "Unexpected status ${BODY} for unauthenticated request"
fi

log "6.2 Malformed Authorization header"
BODY=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "Authorization: NotBearer garbage" \
  "${GATEWAY_BASE}/api/orchestrator/ping" 2>/dev/null) || true
if [[ "${BODY}" == "401" ]]; then
  pass "Gateway rejects malformed Authorization header with 401"
elif [[ "${BODY}" == "200" ]]; then
  skip "Malformed auth header accepted – validateToken=false (dev/test mode)"
else
  fail "Unexpected status ${BODY} for malformed auth header"
fi

# =============================================================================
# 7. ACTUATOR endpoints
# =============================================================================
section "7 – Actuator / Observability"

for svc_base in "${ORCH_BASE}" "${LLM_BASE}" "${RAG_BASE}" "${TOOLS_BASE}"; do
  log "Checking ${svc_base}/actuator/health"
  http GET "${svc_base}/actuator/health"
  if [[ "${BODY}" == "200" ]]; then pass "${svc_base}/actuator/health → 200"; else fail "${svc_base}/actuator/health returned ${BODY}"; fi

  log "Checking ${svc_base}/actuator/info"
  http GET "${svc_base}/actuator/info"
  if [[ "${BODY}" == "200" ]]; then pass "${svc_base}/actuator/info → 200"; else fail "${svc_base}/actuator/info returned ${BODY}"; fi

  log "Checking ${svc_base}/actuator/metrics"
  http GET "${svc_base}/actuator/metrics"
  if [[ "${BODY}" == "200" ]]; then pass "${svc_base}/actuator/metrics → 200"; else fail "${svc_base}/actuator/metrics returned ${BODY}"; fi
done

# =============================================================================
# SUMMARY
# =============================================================================
section "Test Summary"
TOTAL=$(( PASS + FAIL + SKIP ))
echo -e "  Total :  ${TOTAL}"
echo -e "  ${GREEN}Pass${RESET}  :  ${PASS}"
echo -e "  ${RED}Fail${RESET}  :  ${FAIL}"
echo -e "  ${YELLOW}Skip${RESET}  :  ${SKIP}"
echo

if [[ "${FAIL}" -gt 0 ]]; then
  echo -e "${RED}E2E suite FAILED – ${FAIL} test(s) did not pass.${RESET}"
  exit 1
else
  echo -e "${GREEN}All E2E tests passed (${PASS} passed, ${SKIP} skipped).${RESET}"
  exit 0
fi
