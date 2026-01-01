# EInvoiceHub Terraform Outputs

# =============================================================================
# Namespace Outputs
# =============================================================================

output "namespace_name" {
  description = "Kubernetes namespace name"
  value       = kubernetes_namespace.einvoice_namespace.metadata[0].name
}

output "namespace_creation_timestamp" {
  description = "Namespace creation timestamp"
  value       = kubernetes_namespace.einvoice_namespace.metadata[0].creation_timestamp
}

# =============================================================================
# Application Configuration Outputs
# =============================================================================

output "app_name" {
  description = "Application name"
  value       = var.app_name
}

output "app_version" {
  description = "Application version"
  value       = var.app_version
}

output "environment" {
  description = "Deployment environment"
  value       = var.environment
}

output "server_port" {
  description = "Application server port"
  value       = var.server_port
}

# =============================================================================
# Resource Outputs
# =============================================================================

output "replica_count" {
  description = "Number of pod replicas configured"
  value       = var.replicas
}

output "cpu_limit" {
  description = "CPU limit per pod"
  value       = var.limit_range.cpu_limit
}

output "memory_limit" {
  description = "Memory limit per pod"
  value       = var.limit_range.memory_limit
}

# =============================================================================
# Database Configuration Outputs
# =============================================================================

output "mysql_host" {
  description = "MySQL hostname"
  value       = var.mysql_host
}

output "mysql_port" {
  description = "MySQL port"
  value       = var.mysql_port
}

output "mysql_database" {
  description = "MySQL database name"
  value       = var.mysql_database
}

output "mongodb_uri" {
  description = "MongoDB connection URI"
  value       = "mongodb://${var.mongo_host}:${var.mongo_port}/${var.mongo_database}"
  sensitive   = false
}

output "redis_host" {
  description = "Redis hostname"
  value       = var.redis_host
}

output "redis_port" {
  description = "Redis port"
  value       = var.redis_port
}

# =============================================================================
# Secret Names (not values)
# =============================================================================

output "database_secret_name" {
  description = "Kubernetes secret name for database credentials"
  value       = kubernetes_secret.database_secrets.metadata[0].name
}

output "api_secret_name" {
  description = "Kubernetes secret name for API credentials"
  value       = kubernetes_secret.api_secrets.metadata[0].name
}

output "provider_secret_name" {
  description = "Kubernetes secret name for provider credentials"
  value       = kubernetes_secret.provider_secrets.metadata[0].name
}

output "configmap_name" {
  description = "Kubernetes ConfigMap name for application configuration"
  value       = kubernetes_config_map.app_config.metadata[0].name
}

# =============================================================================
# Network Policy Outputs
# =============================================================================

output "network_policy_name" {
  description = "Kubernetes NetworkPolicy name"
  value       = kubernetes_network_policy.einvoice_network_policy.metadata[0].name
}

# =============================================================================
# Resource Quota Outputs
# =============================================================================

output "resource_quota_name" {
  description = "ResourceQuota name"
  value       = kubernetes_resource_quota.namespace_quota.metadata[0].name
}

# =============================================================================
# Connection Information
# =============================================================================

output "application_url" {
  description = "Application URL (if ingress is enabled)"
  value       = var.ingress_enabled ? "http://${var.ingress_host}" : "http://localhost:${var.server_port}"
}

output "health_endpoint" {
  description = "Application health check endpoint"
  value       = var.ingress_enabled ? "http://${var.ingress_host}/actuator/health" : "http://localhost:${var.server_port}/actuator/health"
}

output "metrics_endpoint" {
  description = "Prometheus metrics endpoint"
  value       = var.ingress_enabled ? "http://${var.ingress_host}/actuator/prometheus" : "http://localhost:${var.server_port}/actuator/prometheus"
}

# =============================================================================
# Monitoring Endpoints (if enabled)
# =============================================================================

output "grafana_url" {
  description = "Grafana dashboard URL"
  value       = var.monitoring_enabled ? "http://${var.ingress_host}:3000" : "Grafana not enabled"
}

output "prometheus_url" {
  description = "Prometheus URL"
  value       = var.monitoring_enabled ? "http://${var.ingress_host}:9090" : "Prometheus not enabled"
}

output "jaeger_url" {
  description = "Jaeger tracing UI URL"
  value       = var.tracing_enabled ? "http://${var.ingress_host}:16686" : "Jaeger not enabled"
}

# =============================================================================
# Kubernetes Context Information
# =============================================================================

output "kubernetes_context" {
  description = "Kubernetes context used"
  value       = var.kubernetes_context
}

output "kubectl_command" {
  description = "Example kubectl command to access the application"
  value       = "kubectl port-forward -n ${kubernetes_namespace.einvoice_namespace.metadata[0].name} svc/${var.app_name} ${var.server_port}:${var.server_port}"
}

# =============================================================================
# Image Information
# =============================================================================

output "image_repository" {
  description = "Container image repository"
  value       = var.image_repository
}

output "image_tag" {
  description = "Container image tag"
  value       = var.image_tag
}

output "full_image_name" {
  description = "Full container image name with tag"
  value       = "${var.image_repository}:${var.image_tag}"
}

# =============================================================================
# Autoscaling Information
# =============================================================================

output "hpa_min_replicas" {
  description = "HPA minimum replicas"
  value       = var.autoscaling.min_replicas
}

output "hpa_max_replicas" {
  description = "HPA maximum replicas"
  value       = var.autoscaling.max_replicas
}

output "hpa_cpu_target" {
  description = "HPA CPU target utilization percentage"
  value       = var.autoscaling.cpu_target
}

output "hpa_memory_target" {
  description = "HPA memory target utilization percentage"
  value       = var.autoscaling.memory_target
}

# =============================================================================
# Security Information
# =============================================================================

output "encryption_enabled" {
  description = "Whether encryption is enabled"
  value       = var.encryption_key != ""
}

output "tls_enabled" {
  description = "Whether TLS is enabled"
  value       = var.tls_enabled
}

output "ingress_enabled" {
  description = "Whether ingress is enabled"
  value       = var.ingress_enabled
}

# =============================================================================
# Provider Information
# =============================================================================

output "providers_configured" {
  description = "List of invoice providers configured"
  value       = ["viettel", "bkav", "misa", "vnpt"]
}

# =============================================================================
# Summary Message
# =============================================================================

output "deployment_summary" {
  description = "Summary of deployment configuration"
  value       = <<-EOT
╔══════════════════════════════════════════════════════════════════╗
║                  EInvoiceHub Deployment Summary                   ║
╠══════════════════════════════════════════════════════════════════╣
║ Application: ${var.app_name} v${var.app_version}
║ Environment: ${var.environment}
║ Namespace: ${kubernetes_namespace.einvoice_namespace.metadata[0].name}
║ Replicas: ${var.replicas}
║
║ Database: ${var.mysql_host}:${var.mysql_port}/${var.mysql_database}
║ Cache: ${var.redis_host}:${var.redis_port}
║ MongoDB: ${var.mongo_host}:${var.mongo_port}/${var.mongo_database}
║
║ Access: ${var.ingress_enabled ? "http://${var.ingress_host}" : "http://localhost:${var.server_port}"}
║ Health: ${var.ingress_enabled ? "http://${var.ingress_host}/actuator/health" : "http://localhost:${var.server_port}/actuator/health"}
╚══════════════════════════════════════════════════════════════════╝
EOT
}
