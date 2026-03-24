output "vnet_name" {
  description = "Virtual network name"
  value       = azurerm_virtual_network.this.name
}

output "subnet_id" {
  description = "Subnet ID"
  value       = azurerm_subnet.this.id
}

output "subnet_name" {
  description = "Subnet name"
  value       = azurerm_subnet.this.name
}

output "nsg_name" {
  description = "Network security group name"
  value       = azurerm_network_security_group.this.name
}