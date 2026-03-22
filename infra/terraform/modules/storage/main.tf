resource "azurerm_storage_account" "this" {
  name                     = "${var.resource_prefix}storage"
  resource_group_name      = var.resource_group_name
  location                 = var.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
  account_kind             = "StorageV2"

  tags = var.tags
}

resource "azurerm_storage_container" "logs" {
  name                 = "logs"
  storage_account_name = azurerm_storage_account.this.name
  container_access_type = "private"
}

resource "azurerm_storage_container" "models" {
  name                 = "models"
  storage_account_name = azurerm_storage_account.this.name
  container_access_type = "private"
}

resource "azurerm_search_service" "this" {
  name                = "${var.resource_prefix}-search"
  resource_group_name = var.resource_group_name
  location            = var.location
  sku                 = var.search_sku

  tags = var.tags
}