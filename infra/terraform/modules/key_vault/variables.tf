variable "resource_prefix" {
  description = "Prefix for resource names"
  type        = string
}

variable "location" {
  description = "Azure region"
  type        = string
}

variable "resource_group_name" {
  description = "Resource group name"
  type        = string
}

variable "tenant_id" {
  description = "Azure tenant ID"
  type        = string
}

variable "terraform_object_id" {
  description = "Object ID of the Terraform service principal"
  type        = string
}

variable "aks_identity_object_id" {
  description = "Object ID of the AKS managed identity"
  type        = string
}

variable "openai_api_key" {
  description = "OpenAI API key"
  type        = string
  sensitive   = true
}

variable "azure_openai_key" {
  description = "Azure OpenAI key"
  type        = string
  sensitive   = true
}

variable "search_api_key" {
  description = "Azure Search API key"
  type        = string
  sensitive   = true
}

variable "tags" {
  description = "Tags to apply to resources"
  type        = map(string)
  default     = {}
}