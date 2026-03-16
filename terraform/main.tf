# EInvoiceHub Infrastructure as Code (Terraform)

terraform {
  required_version = ">= 1.5.0"

  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.24"
    }

    helm = {
      source  = "hashicorp/helm"
      version = "~> 2.12"
    }

    random = {
      source  = "hashicorp/random"
      version = "~> 3.6"
    }

    kubectl = {
      source  = "gavinbunney/kubectl"
      version = "~> 1.14"
    }
  }

  # Configure backend (modify for production)
  backend "local" {
    path = "terraform.tfstate"
  }
}

# Kubernetes Provider Configuration

provider "kubernetes" {
  config_path    = "~/.kube/config"
  config_context = var.kubernetes_context
}

provider "helm" {
  kubernetes {
    config_path    = "~/.kube/config"
    config_context = var.kubernetes_context
  }
}

provider "kubectl" {
  config_path    = "~/.kube/config"
  config_context = var.kubernetes_context
}

# Random Resources for Password Generation

resource "random_string" "mysql_password" {
  length  = 32
  special = false
}

resource "random_string" "mongodb_password" {
  length  = 32
  special = false
}

resource "random_string" "redis_password" {
  length  = 32
  special = false
}

resource "random_string" "grafana_password" {
  length  = 32
  special = false
}

resource "random_id" "session_id" {
  byte_length = 4
}

# Namespace Creation

resource "kubernetes_namespace" "einvoice_namespace" {
  metadata {
    name        = var.namespace
    labels = {
      environment = var.environment
      project     = "einvoicehub"
      managed_by  = "terraform"
    }
  }
}

# ConfigMap for Application Configuration

resource "kubernetes_config_map" "app_config" {
  metadata {
    name      = "${var.app_name}-config"
    namespace = kubernetes_namespace.einvoice_namespace.metadata[0].name
    labels = {
      app = var.app_name
    }
  }

  data = {
    # Application Settings
    APP_NAME                = var.app_name
    APP_VERSION             = var.app_version
    SERVER_PORT             = var.server_port
    SPRING_PROFILES_ACTIVE  = var.environment

    # Logging Configuration
    LOG_LEVEL               = var.log_level
    LOG_FORMAT              = "json"

    # Database Configuration (placeholders - use secrets for actual values)
    SPRING_DATASOURCE_URL   = "jdbc:mysql://${var.mysql_host}:${var.mysql_port}/${var.mysql_database}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
    SPRING_JPA_HIBERNATE_DDL_AUTO = "update"

    # MongoDB Configuration
    SPRING_DATA_MONGODB_URI = "mongodb://${var.mongo_host}:${var.mongo_port}/${var.mongo_database}?authSource=admin"

    # Redis Configuration
    SPRING_REDIS_HOST       = var.redis_host
    SPRING_REDIS_PORT       = var.redis_port

    # Actuator Endpoints
    MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE = "health,info,metrics,prometheus"
    MANAGEMENT_ENDPOINT_HEALTH_SHOW_DETAILS    = "when_authorized"

    # Virtual Threads (Java 21)
    JAVA_THREADS_VIRTUAL_ENABLED = "true"
  }
}

# Secrets for Sensitive Data
resource "kubernetes_secret" "database_secrets" {
  metadata {
    name      = "${var.app_name}-db-secrets"
    namespace = kubernetes_namespace.einvoice_namespace.metadata[0].name
    labels = {
      app = var.app_name
    }
  }

  type = "Opaque"

  data = {
    # Base64 encoded values
    SPRING_DATASOURCE_USERNAME = base64encode(var.mysql_username)
    SPRING_DATASOURCE_PASSWORD = base64encode(random_string.mysql_password.result)
    SPRING_DATA_MONGODB_USERNAME = base64encode(var.mongo_username)
    SPRING_DATA_MONGODB_PASSWORD = base64encode(random_string.mongodb_password.result)
    SPRING_REDIS_PASSWORD = base64encode(random_string.redis_password.result)
  }
}

resource "kubernetes_secret" "api_secrets" {
  metadata {
    name      = "${var.app_name}-api-secrets"
    namespace = kubernetes_namespace.einvoice_namespace.metadata[0].name
    labels = {
      app = var.app_name
    }
  }

  type = "Opaque"

  data = {
    # API Key encryption key (should be rotated regularly)
    ENCRYPTION_KEY_AES256 = base64encode(var.encryption_key)
    ENCRYPTION_KEY_IV     = base64encode(var.encryption_iv)

    # JWT Configuration
    JWT_SECRET_KEY        = base64encode(var.jwt_secret)
    JWT_EXPIRATION_MS     = base64encode(var.jwt_expiration_ms)
  }
}

resource "kubernetes_secret" "provider_secrets" {
  metadata {
    name      = "${var.app_name}-provider-secrets"
    namespace = kubernetes_namespace.einvoice_namespace.metadata[0].name
    labels = {
      app = var.app_name
    }
  }

  type = "Opaque"

  data = {
    # Provider-specific API keys (encrypted)
    VIETTEL_API_KEY      = base64encode(var.viettel_api_key)
    VIETTEL_CLIENT_ID    = base64encode(var.viettel_client_id)

    BKAV_API_KEY         = base64encode(var.bkav_api_key)
    BKAV_CLIENT_ID       = base64encode(var.bkav_client_id)

    MISA_API_KEY         = base64encode(var.misa_api_key)
    MISA_CLIENT_ID       = base64encode(var.misa_client_id)
    MISA_CLIENT_SECRET   = base64encode(var.misa_client_secret)
  }
}

# =============================================================================
# Resource Quotas
# =============================================================================

resource "kubernetes_resource_quota" "namespace_quota" {
  metadata {
    name      = "${var.app_name}-quota"
    namespace = kubernetes_namespace.einvoice_namespace.metadata[0].name
  }

  spec {
    hard = {
      "limits.cpu"    = var.resource_quota.cpu_limit
      "limits.memory" = var.resource_quota.memory_limit
      "pods"          = "20"
      "services"      = "10"
      "secrets"       = "20"
      "configmaps"    = "20"
      "persistentvolumeclaims" = "10"
    }
  }
}

# Limit Ranges

resource "kubernetes_limit_range" "namespace_limits" {
  metadata {
    name      = "${var.app_name}-limits"
    namespace = kubernetes_namespace.einvoice_namespace.metadata[0].name
  }

  spec {
    limit {
      type = "Container"

      default_request = {
        cpu    = var.limit_range.cpu_request
        memory = var.limit_range.memory_request
      }

      default = {
        cpu    = var.limit_range.cpu_limit
        memory = var.limit_range.memory_limit
      }

      min = {
        cpu    = var.limit_range.cpu_min
        memory = var.limit_range.memory_min
      }

      max = {
        cpu    = var.limit_range.cpu_max
        memory = var.limit_range.memory_max
      }
    }
  }
}

# Network Policy

resource "kubernetes_network_policy" "einvoice_network_policy" {
  metadata {
    name      = "${var.app_name}-network-policy"
    namespace = kubernetes_namespace.einvoice_namespace.metadata[0].name
  }

  spec {
    pod_selector {
      match_labels = {
        app = var.app_name
      }
    }

    policy_types = ["Ingress", "Egress"]

    ingress {
      from {
        namespace_selector {
          match_labels = {
            name = "ingress-nginx"
          }
        }
      }

      from {
        pod_selector {
          match_labels = {
            app = var.app_name
          }
        }
      }

      ports {
        protocol = "TCP"
        port     = var.server_port
      }

      ports {
        protocol = "TCP"
        port     = 9090
      }
    }

    egress {
      # Allow DNS resolution
      to {
        namespace_selector {
          match_labels = {
            name = "kube-system"
          }
        }
      }

      # Allow traffic to MySQL
      to {
        pod_selector {
          match_labels = {
            tier = "database"
          }
        }
        ports {
          protocol = "TCP"
          port     = 3306
        }
      }

      # Allow traffic to MongoDB
      to {
        pod_selector {
          match_labels = {
            tier = "database"
          }
        }
        ports {
          protocol = "TCP"
          port     = 27017
        }
      }

      # Allow traffic to Redis
      to {
        pod_selector {
          match_labels = {
            tier = "cache"
          }
        }
        ports {
          protocol = "TCP"
          port     = 6379
        }
      }

      # Allow external API calls
      to {
        ip_block {
          cidr = "0.0.0.0/0"
          except = [
            "10.0.0.0/8",
            "172.16.0.0/12",
            "192.168.0.0/16"
          ]
        }
      }
    }
  }
}
