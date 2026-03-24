output "key_vault_name" {
  description = "Key Vault name"
  value       = azurerm_key_vault.this.name
}

output "key_vault_uri" {
  description = "Key Vault URI"
  value       = azurerm_key_vault.this.vault_uri
}

output "openai_api_key_secret_name" {
  description = "OpenAI API key secret name"
  value       = azurerm_key_vault_secret.openai_api_key.name
}

output "azure_openai_key_secret_name" {
  description = "Azure OpenAI key secret name"
  value       = azurerm_key_vault_secret.azure_openai_key.name
}

output "search_api_key_secret_name" {
  description = "Search API key secret name"
  value       = azurerm_key_vault_secret.search_api_key.name
}