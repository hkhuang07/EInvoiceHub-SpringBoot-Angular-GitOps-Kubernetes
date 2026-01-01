# EInvoiceHub Terraform Variables

# Environment Configuration

variable "environment" {
  description = "Deployment environment (dev, staging, prod)"
  type        = string
  default     = "dev"
}

variable "app_name" {
  description = "Application name"
  type        = string
  default     = "einvoicehub"
}

variable "app_version" {
  description = "Application version"
  type        = string
  default     = "1.0.0"
}

# Kubernetes Configuration

variable "kubernetes_context" {
  description = "Kubernetes context to use"
  type        = string
  default     = "k3d-einvoice"
}

variable "namespace" {
  description = "Kubernetes namespace"
  type        = string
  default     = "einvoice-dev"
}

variable "replicas" {
  description = "Number of pod replicas"
  type        = number
  default     = 2
}

variable "server_port" {
  description = "Application server port"
  type        = number
  default     = 8080
}

# Resource Configuration

variable "resource_quota" {
  description = "Resource quota configuration"
  type = object({
    cpu_limit    = string
    memory_limit = string
  })
  default = {
    cpu_limit    = "8"
    memory_limit = "8Gi"
  }
}

variable "limit_range" {
  description = "Container limit range configuration"
  type = object({
    cpu_request  = string
    memory_request = string
    cpu_limit    = string
    memory_limit = string
    cpu_min      = string
    memory_min   = string
    cpu_max      = string
    memory_max   = string
  })
  default = {
    cpu_request    = "250m"
    memory_request = "256Mi"
    cpu_limit      = "1000m"
    memory_limit   = "1Gi"
    cpu_min        = "100m"
    memory_min     = "128Mi"
    cpu_max        = "2000m"
    memory_max     = "2Gi"
  }
}

variable "autoscaling" {
  description = "Horizontal Pod Autoscaler configuration"
  type = object({
    min_replicas = number
    max_replicas = number
    cpu_target   = number
    memory_target = number
  })
  default = {
    min_replicas = 2
    max_replicas = 10
    cpu_target   = 70
    memory_target = 80
  }
}

# Logging Configuration

variable "log_level" {
  description = "Application log level"
  type        = string
  default     = "INFO"
}

# Database Configuration (for ConfigMap - use secrets for actual values)

variable "mysql_host" {
  description = "MySQL hostname"
  type        = string
  default     = "mysql"
}

variable "mysql_port" {
  description = "MySQL port"
  type        = number
  default     = 3306
}

variable "mysql_database" {
  description = "MySQL database name"
  type        = string
  default     = "einvoicehub"
}

variable "mysql_username" {
  description = "MySQL username"
  type        = string
  default     = "einvoice_user"
}

# MongoDB Configuration

variable "mongo_host" {
  description = "MongoDB hostname"
  type        = string
  default     = "mongodb"
}

variable "mongo_port" {
  description = "MongoDB port"
  type        = number
  default     = 27017
}

variable "mongo_database" {
  description = "MongoDB database name"
  type        = string
  default     = "einvoicehub"
}

variable "mongo_username" {
  description = "MongoDB username"
  type        = string
  default     = "admin"
}

# Redis Configuration

variable "redis_host" {
  description = "Redis hostname"
  type        = string
  default     = "redis"
}

variable "redis_port" {
  description = "Redis port"
  type        = number
  default     = 6379
}

# Security Variables (use environment variables or secret management)

variable "encryption_key" {
  description = "AES-256 encryption key (32 bytes)"
  type        = string
  default     = ""
  sensitive   = true
}

variable "encryption_iv" {
  description = "AES encryption initialization vector (16 bytes)"
  type        = string
  default     = ""
  sensitive   = true
}

variable "jwt_secret" {
  description = "JWT secret key"
  type        = string
  default     = ""
  sensitive   = true
}

variable "jwt_expiration_ms" {
  description = "JWT expiration time in milliseconds"
  type        = string
  default     = "86400000"
}

# Provider API Credentials (use secrets for actual values)

variable "viettel_api_key" {
  description = "Viettel API Key"
  type        = string
  default     = ""
  sensitive   = true
}

variable "viettel_client_id" {
  description = "Viettel Client ID"
  type        = string
  default     = ""
  sensitive   = true
}

variable "bkav_api_key" {
  description = "BKAV API Key"
  type        = string
  default     = ""
  sensitive   = true
}

variable "bkav_client_id" {
  description = "BKAV Client ID"
  type        = string
  default     = ""
  sensitive   = true
}

variable "misa_api_key" {
  description = "MISA API Key"
  type        = string
  default     = ""
  sensitive   = true
}

variable "misa_client_id" {
  description = "MISA Client ID"
  type        = string
  default     = ""
  sensitive   = true
}

variable "misa_client_secret" {
  description = "MISA Client Secret"
  type        = string
  default     = ""
  sensitive   = true
}

# Image Configuration

variable "image_repository" {
  description = "Container image repository"
  type        = string
  default     = "einvoicehub/backend"
}

variable "image_tag" {
  description = "Container image tag"
  type        = string
  default     = "latest"
}

variable "image_pull_policy" {
  description = "Image pull policy (Always, Never, IfNotPresent)"
  type        = string
  default     = "IfNotPresent"
}

# Persistence Configuration

variable "persistence" {
  description = "Persistence configuration"
  type = object({
    enabled  = bool
    size     = string
    storage  = string
  })
  default = {
    enabled  = true
    size     = "10Gi"
    storage  = "standard"
  }
}

# Monitoring Configuration

variable "monitoring_enabled" {
  description = "Enable monitoring stack"
  type        = bool
  default     = true
}

variable "metrics_port" {
  description = "Port for Prometheus metrics"
  type        = number
  default     = 9090
}

# Tracing Configuration

variable "tracing_enabled" {
  description = "Enable distributed tracing"
  type        = bool
  default     = true
}

variable "jaeger_endpoint" {
  description = "Jaeger collector endpoint"
  type        = string
  default     = "http://jaeger:4317"
}

# Ingress Configuration

variable "ingress_enabled" {
  description = "Enable ingress controller"
  type        = bool
  default     = true
}

variable "ingress_host" {
  description = "Ingress hostname"
  type        = string
  default     = "einvoice.local"
}

variable "ingress_class" {
  description = "Ingress class name"
  type        = string
  default     = "nginx"
}

# Certificate Configuration

variable "tls_enabled" {
  description = "Enable TLS for ingress"
  type        = bool
  default     = false
}

variable "tls_secret_name" {
  description = "TLS secret name"
  type        = string
  default     = "einvoice-tls"
}
