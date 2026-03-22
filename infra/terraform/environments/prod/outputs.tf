output "resource_group_name" {
  description = "Resource group name"
  value       = module.resource_group.name
}

output "aks_cluster_name" {
  description = "AKS cluster name"
  value       = module.aks.cluster_name
}

output "aks_kube_config" {
  description = "AKS kube config"
  value       = module.aks.kube_config
  sensitive   = true
}

output "acr_login_server" {
  description = "ACR login server"
  value       = module.acr.login_server
}

output "storage_account_name" {
  description = "Storage account name"
  value       = module.storage.account_name
}

output "search_service_endpoint" {
  description = "Azure AI Search endpoint"
  value       = module.storage.search_service_endpoint
}

output "openai_endpoint" {
  description = "Azure OpenAI endpoint"
  value       = module.cognitive_services.openai_endpoint
}

output "key_vault_name" {
  description = "Key Vault name"
  value       = module.key_vault.key_vault_name
}

output "key_vault_uri" {
  description = "Key Vault URI"
  value       = module.key_vault.key_vault_uri
}