output "openai_account_name" {
  description = "Azure OpenAI account name"
  value       = azurerm_cognitive_account.openai.name
}

output "openai_endpoint" {
  description = "Azure OpenAI endpoint"
  value       = azurerm_cognitive_account.openai.endpoint
}

output "openai_primary_key" {
  description = "Azure OpenAI primary key"
  value       = azurerm_cognitive_account.openai.primary_access_key
  sensitive   = true
}

output "gpt4_deployment_name" {
  description = "GPT-4 deployment name"
  value       = azurerm_cognitive_deployment.gpt4.name
}

output "embedding_deployment_name" {
  description = "Embedding deployment name"
  value       = azurerm_cognitive_deployment.embedding.name
}