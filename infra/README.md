# Enterprise Agentic AI DevOps Platform - Infrastructure

This directory contains the complete infrastructure-as-code setup for deploying the Enterprise Agentic AI DevOps Platform on Azure using Terraform and Kubernetes.

## Architecture Overview

The platform consists of the following services:
- **API Gateway**: Spring Cloud Gateway for routing requests
- **Agent Orchestrator**: Central coordination service
- **LLM Service**: Large Language Model processing
- **RAG Service**: Retrieval-Augmented Generation
- **Tools Service**: DevOps automation tools

## Infrastructure Components

### Azure Resources
- **Resource Groups**: Environment-specific resource isolation
- **AKS Clusters**: Kubernetes orchestration
- **Azure Container Registry**: Private container registry
- **Azure Cognitive Services**: OpenAI and AI Search
- **Azure Storage**: Blob storage for logs and models
- **Azure Key Vault**: Secrets management
- **Virtual Networks**: Network isolation and security

### Environments
- **dev**: Development environment with minimal resources
- **staging**: Pre-production environment with moderate scaling
- **prod**: Production environment with high availability

## Prerequisites

1. **Azure CLI** installed and authenticated
2. **Terraform** >= 1.0.0
3. **kubectl** for Kubernetes management
4. **Helm** 3.x for chart deployment

## Quick Start

### 1. Initialize Terraform Backend

First, create a storage account for Terraform state:

```bash
# Create resource group for Terraform state
az group create --name tfstate --location eastus

# Create storage account
az storage account create --name tfstateaccount --resource-group tfstate --location eastus --sku Standard_LRS

# Create container
az storage container create --name tfstate --account-name tfstateaccount
```

### 2. Deploy Infrastructure

```bash
# Navigate to environment
cd infra/terraform/environments/dev

# Initialize Terraform
terraform init

# Plan deployment
terraform plan -var-file=terraform.tfvars

# Apply changes
terraform apply -var-file=terraform.tfvars
```

### 3. Configure Kubernetes Access

```bash
# Get AKS credentials
az aks get-credentials --resource-group $(terraform output -raw resource_group_name) --name $(terraform output -raw aks_cluster_name)
```

### 4. Deploy Applications

```bash
# Build and push Docker images
docker build -t $(terraform output -raw acr_login_server)/agent-orchestrator:latest ../agent-orchestrator/
docker push $(terraform output -raw acr_login_server)/agent-orchestrator:latest

# Deploy using Helm
helm install agent-orchestrator ../helm/agent-orchestrator/
```

## Environment Configuration

### Development (dev)
- AKS: 2 nodes, Standard_DS2_v2
- ACR: Basic SKU
- AI Search: Basic tier
- OpenAI: S0 SKU

### Staging (staging)
- AKS: 3 nodes, Standard_DS3_v2
- ACR: Standard SKU
- AI Search: Standard tier
- OpenAI: S0 SKU

### Production (prod)
- AKS: 5 nodes, Standard_DS4_v2
- ACR: Premium SKU
- AI Search: Standard tier
- OpenAI: S0 SKU

## Application Configuration

Each service uses Spring profiles for environment-specific configuration:

### Environment Variables Required

```bash
# LLM Service
OPENAI_API_KEY=your_openai_key
AZURE_OPENAI_URL=https://your-resource.openai.azure.com
AZURE_API_KEY=your_azure_key
AZURE_DEPLOYMENT=gpt-4

# API Gateway
JWT_SECRET=replace_with_minimum_32_char_secret
JWT_ISSUER=your-token-issuer
GATEWAY_ALLOWED_ORIGINS=https://app.company.com

# RAG Service
AZURE_EMBEDDING_URL=https://your-resource.openai.azure.com
AZURE_EMBEDDING_DEPLOYMENT=text-embedding-ada-002
AZURE_SEARCH_URL=https://your-search.search.windows.net
AZURE_SEARCH_API_KEY=your_search_key
AZURE_SEARCH_INDEX=log-index

# Tools Service
AZURE_SUBSCRIPTION_ID=your_subscription_id
AZURE_RESOURCE_GROUP=your_resource_group
AZURE_WORKSPACE_NAME=your_ml_workspace
```

## Security Considerations

1. **Key Vault Integration**: All secrets are stored in Azure Key Vault
2. **Managed Identities**: AKS uses managed identities for Azure resource access
3. **Network Security**: NSGs restrict inbound traffic
4. **RBAC**: Role-based access control for Azure resources

## Monitoring and Observability

- **Spring Boot Actuator**: Health checks and metrics for all services
- **Azure Monitor**: Infrastructure monitoring
- **Application Insights**: Application performance monitoring

## Scaling

### Horizontal Pod Autoscaling
```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: agent-orchestrator-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: agent-orchestrator
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
```

### AKS Node Autoscaling
Enabled by default with min 1, max 10 nodes per node pool.

## Backup and Disaster Recovery

- **Terraform State**: Stored in Azure Storage with versioning
- **Database Backups**: Configure Azure Backup for persistent volumes
- **Geo-redundancy**: Use Azure regions with geo-redundant storage

## Troubleshooting

### Common Issues

1. **Terraform State Lock**: If state is locked, check for running deployments
2. **AKS Authentication**: Ensure Azure CLI is logged in with correct subscription
3. **Key Vault Access**: Verify managed identity has correct permissions

### Logs and Debugging

```bash
# View pod logs
kubectl logs -f deployment/agent-orchestrator

# Check service health
kubectl exec -it deployment/agent-orchestrator -- curl http://localhost:8110/actuator/health

# Debug network issues
kubectl get services
kubectl describe ingress
```

## Cost Optimization

- Use Azure Cost Management for monitoring
- Implement resource limits in Kubernetes
- Use spot instances for non-production workloads
- Schedule workloads during off-peak hours

## Contributing

1. Follow infrastructure as code best practices
2. Test changes in dev environment first
3. Update documentation for any architectural changes
4. Use semantic versioning for infrastructure changes