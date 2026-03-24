terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~>3.0"
    }
  }

  backend "azurerm" {
    resource_group_name  = "tfstate"
    storage_account_name = "tfstateaccount"
    container_name       = "tfstate"
    key                  = "staging.terraform.tfstate"
  }
}

provider "azurerm" {
  features {}
}

locals {
  resource_prefix = "agentic-staging"
  tags = {
    Environment = "staging"
    Project     = "agentic-ai-platform"
    ManagedBy   = "terraform"
  }
}

module "resource_group" {
  source = "../../modules/resource_group"

  name     = "${local.resource_prefix}-rg"
  location = var.location
  tags     = local.tags
}

module "networking" {
  source = "../../modules/networking"

  resource_prefix      = local.resource_prefix
  resource_group_name  = module.resource_group.name
  location             = var.location
  vnet_address_space   = ["10.0.0.0/16"]
  subnet_address_space = ["10.0.1.0/24"]
  tags                 = local.tags
}

module "aks" {
  source = "../../modules/aks"

  resource_prefix     = local.resource_prefix
  resource_group_name = module.resource_group.name
  location            = var.location
  kubernetes_version  = "1.27.0"
  node_count          = 3
  vm_size             = "Standard_DS3_v2"
  subnet_id           = module.networking.subnet_id
  acr_id              = module.acr.id
  tags                = local.tags
}

module "acr" {
  source = "../../modules/acr"

  resource_prefix     = local.resource_prefix
  resource_group_name = module.resource_group.name
  location            = var.location
  sku                 = "Basic"
  tags                = local.tags
}

module "storage" {
  source = "../../modules/storage"

  resource_prefix     = local.resource_prefix
  resource_group_name = module.resource_group.name
  location            = var.location
  search_sku          = "standard"
  tags                = local.tags
}

module "cognitive_services" {
  source = "../../modules/cognitive_services"

  resource_prefix     = local.resource_prefix
  resource_group_name = module.resource_group.name
  location            = var.location
  openai_sku          = "S0"
  tags                = local.tags
}

module "key_vault" {
  source = "../../modules/key_vault"

  resource_prefix         = local.resource_prefix
  resource_group_name     = module.resource_group.name
  location                = var.location
  tenant_id               = var.tenant_id
  terraform_object_id     = var.terraform_object_id
  aks_identity_object_id  = module.aks.kubelet_identity_object_id
  openai_api_key          = var.openai_api_key
  azure_openai_key        = module.cognitive_services.openai_primary_key
  search_api_key          = module.storage.search_service_primary_key
  tags                    = local.tags
}