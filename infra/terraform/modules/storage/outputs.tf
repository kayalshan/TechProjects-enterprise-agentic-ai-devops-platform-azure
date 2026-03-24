output "account_name" {
  description = "Storage account name"
  value       = azurerm_storage_account.this.name
}

output "account_key" {
  description = "Storage account key"
  value       = azurerm_storage_account.this.primary_access_key
  sensitive   = true
}

output "search_service_name" {
  description = "Azure AI Search service name"
  value       = azurerm_search_service.this.name
}

output "search_service_endpoint" {
  description = "Azure AI Search endpoint"
  value       = "https://${azurerm_search_service.this.name}.search.windows.net"
}

output "search_service_primary_key" {
  description = "Azure AI Search primary key"
  value       = azurerm_search_service.this.primary_key
  sensitive   = true
}

output "logs_container_name" {
  description = "Logs container name"
  value       = azurerm_storage_container.logs.name
}

output "models_container_name" {
  description = "Models container name"
  value       = azurerm_storage_container.models.name
}