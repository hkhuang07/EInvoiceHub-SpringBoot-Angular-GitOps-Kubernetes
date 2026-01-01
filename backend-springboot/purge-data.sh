#!/bin/bash

# =============================================================================
# EInvoiceHub Data Purge Script
# =============================================================================
# Purpose: Clean up and reset data for local K8s development environment
# Usage: ./purge-data.sh [environment]
# Environments: dev, staging, prod
# =============================================================================

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Default environment
ENVIRONMENT=${1:-dev}
NAMESPACE="einvoice-${ENVIRONMENT}"

# Confirmation prompt
confirm_purge() {
    echo -e "${YELLOW}⚠️  WARNING: This will delete all data for environment '${ENVIRONMENT}'${NC}"
    echo -e "${RED}This action cannot be undone!${NC}"
    read -p "Are you sure you want to continue? (yes/no): " CONFIRM

    if [ "$CONFIRM" != "yes" ]; then
        echo -e "${YELLOW}Purge operation cancelled.${NC}"
        exit 0
    fi
}

# Print banner
print_banner() {
    echo -e "${BLUE}"
    echo "╔════════════════════════════════════════════════════════════════╗"
    echo "║           EInvoiceHub Data Purge Script                        ║"
    echo "║                                                                ║"
    echo "║  Environment: ${ENVIRONMENT}${NC}                                  "
    echo -e "${BLUE}║  Namespace:  ${NAMESPACE}${NC}                                      "
    echo -e "${BLUE}╚════════════════════════════════════════════════════════════════╝${NC}"
}

# Check prerequisites
check_prerequisites() {
    echo -e "\n${GREEN}✓ Checking prerequisites...${NC}"

    # Check for required tools
    local MISSING_TOOLS=()

    if ! command -v kubectl &> /dev/null; then
        MISSING_TOOLS+=("kubectl")
    fi

    if ! command -v docker &> /dev/null; then
        MISSING_TOOLS+=("docker")
    fi

    if ! command -v mongosh &> /dev/null; then
        MISSING_TOOLS+=("mongosh")
    fi

    if ! command -v mysql &> /dev/null; then
        MISSING_TOOLS+=("mysql")
    fi

    if [ ${#MISSING_TOOLS[@]} -ne 0 ]; then
        echo -e "${RED}✗ Missing required tools: ${MISSING_TOOLS[*]}${NC}"
        echo "Please install the missing tools before running this script."
        exit 1
    fi

    echo -e "${GREEN}✓ All prerequisites met${NC}"
}

# Stop and remove containers
stop_containers() {
    echo -e "\n${GREEN}✓ Stopping Docker containers...${NC}"

    if command -v docker &> /dev/null; then
        docker compose down -v --remove-orphans 2>/dev/null || true
        echo -e "${GREEN}✓ Docker containers stopped and volumes removed${NC}"
    fi
}

# Purge Kubernetes resources
purge_kubernetes() {
    echo -e "\n${GREEN}✓ Purging Kubernetes resources...${NC}"

    # Check if kubectl is configured
    if ! kubectl cluster-info &> /dev/null; then
        echo -e "${YELLOW}⚠️  Kubernetes not configured or not accessible. Skipping K8s cleanup.${NC}"
        return
    fi

    # Check if namespace exists
    if kubectl get namespace "$NAMESPACE" &> /dev/null; then
        echo -e "${YELLOW}  Deleting all resources in namespace: $NAMESPACE${NC}"

        # Delete deployments
        kubectl delete deployment --all -n "$NAMESPACE" --ignore-not-found=true

        # Delete services
        kubectl delete service --all -n "$NAMESPACE" --ignore-not-found=true

        # Delete configmaps
        kubectl delete configmap --all -n "$NAMESPACE" --ignore-not-found=true

        # Delete secrets
        kubectl delete secret --all -n "$NAMESPACE" --ignore-not-found=true

        # Delete persistentvolumeclaims
        kubectl delete pvc --all -n "$NAMESPACE" --ignore-not-found=true

        # Delete jobs
        kubectl delete job --all -n "$NAMESPACE" --ignore-not-found=true

        # Delete cronjobs
        kubectl delete cronjob --all -n "$NAMESPACE" --ignore-not-found=true

        echo -e "${GREEN}✓ Kubernetes resources purged${NC}"
    else
        echo -e "${YELLOW}  Namespace $NAMESPACE does not exist. Skipping.${NC}"
    fi
}

# Purge MySQL data
purge_mysql() {
    echo -e "\n${GREEN}✓ Purging MySQL data...${NC}"

    local MYSQL_HOST=${MYSQL_HOST:-localhost}
    local MYSQL_PORT=${MYSQL_PORT:-3306}
    local MYSQL_USER=${MYSQL_USER:-root}
    local MYSQL_PASSWORD=${MYSQL_PASSWORD:-einvoice_root_pass}
    local DATABASE=${DATABASE:-einvoicehub}

    # Check if MySQL is running
    if ! docker ps | grep -q "einvoice-mysql"; then
        echo -e "${YELLOW}⚠️  MySQL container not running. Attempting direct connection...${NC}"
    fi

    # Drop and recreate database
    mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" <<EOF
DROP DATABASE IF EXISTS $DATABASE;
CREATE DATABASE $DATABASE CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON $DATABASE.* TO '$MYSQL_USER'@'%';
FLUSH PRIVILEGES;
EOF

    echo -e "${GREEN}✓ MySQL data purged and database recreated${NC}"
}

# Purge MongoDB data
purge_mongodb() {
    echo -e "\n${GREEN}✓ Purging MongoDB data...${NC}"

    local MONGODB_HOST=${MONGODB_HOST:-localhost}
    local MONGODB_PORT=${MONGODB_PORT:-27017}
    local MONGODB_USER=${MONGODB_USER:-admin}
    local MONGODB_PASSWORD=${MONGODB_PASSWORD:-einvoice_mongo_pass}
    local DATABASE=${MONGODB_DATABASE:-einvoicehub}

    # Drop all collections
    mongosh --host "$MONGODB_HOST" --port "$MONGODB_PORT" -u "$MONGODB_USER" -p "$MONGODB_PASSWORD" --authenticationDatabase admin <<EOF
use $DATABASE
db.getCollectionNames().forEach(function(collection) {
    db[collection].drop();
});
print("All collections dropped from database: $DATABASE");
EOF

    echo -e "${GREEN}✓ MongoDB data purged${NC}"
}

# Purge Redis cache
purge_redis() {
    echo -e "\n${GREEN}✓ Purging Redis cache...${NC}"

    local REDIS_HOST=${REDIS_HOST:-localhost}
    local REDIS_PORT=${REDIS_PORT:-6379}

    # Flush all Redis databases
    redis-cli -h "$REDIS_HOST" -p "$REDIS_PORT" FLUSHALL

    echo -e "${GREEN}✓ Redis cache purged${NC}"
}

# Purge Elasticsearch indices
purge_elasticsearch() {
    echo -e "\n${GREEN}✓ Purging Elasticsearch data...${NC}"

    local ES_HOST=${ES_HOST:-localhost}
    local ES_PORT=${ES_PORT:-9200}

    # Delete all indices matching einvoice pattern
    curl -X DELETE "http://$ES_HOST:$ES_PORT/einvoice*" 2>/dev/null || true

    echo -e "${GREEN}✓ Elasticsearch data purged${NC}"
}

# Clean up local files
cleanup_files() {
    echo -e "\n${GREEN}✓ Cleaning up local files...${NC}"

    # Remove log files
    if [ -d "logs" ]; then
        rm -rf logs/*
        echo "  - Logs cleared"
    fi

    # Remove temporary files
    if [ -d "tmp" ]; then
        rm -rf tmp/*
        echo "  - Temporary files cleared"
    fi

    # Remove generated reports
    if [ -d "reports" ]; then
        rm -rf reports/*
        echo "  - Reports cleared"
    fi

    echo -e "${GREEN}✓ Local files cleaned${NC}"
}

# Print summary
print_summary() {
    echo -e "\n${BLUE}"
    echo "╔════════════════════════════════════════════════════════════════╗"
    echo "║                    PURGE COMPLETED                              ║"
    echo "╚════════════════════════════════════════════════════════════════╝${NC}"
    echo -e "\n${GREEN}Summary:${NC}"
    echo "  - Environment: $ENVIRONMENT"
    echo "  - Namespace: $NAMESPACE"
    echo "  - Docker containers: Stopped"
    echo "  - MySQL database: Recreated"
    echo "  - MongoDB collections: Dropped"
    echo "  - Redis cache: Flushed"
    echo "  - Elasticsearch indices: Deleted"
    echo "  - Local files: Cleaned"
    echo -e "\n${YELLOW}Next steps:${NC}"
    echo "  1. Run 'docker compose up -d' to restart services"
    echo "  2. Run migrations if needed"
    echo "  3. Restart the application"
    echo -e "\n"
}

# Main execution
main() {
    print_banner
    confirm_purge
    check_prerequisites

    # Perform purging based on environment
    case "$ENVIRONMENT" in
        dev)
            stop_containers
            purge_mysql
            purge_mongodb
            purge_redis
            cleanup_files
            ;;
        staging|prod)
            purge_kubernetes
            purge_elasticsearch
            cleanup_files
            ;;
        *)
            echo -e "${RED}Invalid environment: $ENVIRONMENT${NC}"
            echo "Valid environments: dev, staging, prod"
            exit 1
            ;;
    esac

    print_summary
}

# Run main function
main "$@"
