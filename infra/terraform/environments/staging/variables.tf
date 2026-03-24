variable "location" {
  description = "Azure region"
  type        = string
  default     = "East US"
}

variable "tenant_id" {
  description = "Azure tenant ID"
  type        = string
}

variable "terraform_object_id" {
  description = "Object ID of the Terraform service principal"
  type        = string
}

variable "openai_api_key" {
  description = "OpenAI API key"
  type        = string
  sensitive   = true
}