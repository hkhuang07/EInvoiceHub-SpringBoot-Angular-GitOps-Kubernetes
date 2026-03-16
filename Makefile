# Định nghĩa các biến
DC = docker-compose
APP_NAME = einvoicehub-backend

# Lệnh mặc định khi gõ 'make'
all: help

help:
	@echo "Common Command:"
	@echo "  make up      : Start Docker containers"
	@echo "  make down    : Stop containers"
	@echo "  make restart : Restart System"
	@echo "  make build   : Rebuild Spring Boot Application and Docker"
	@echo "  make logs    : Views Log  of Application

up:
	$(DC) up -d

down:
	$(DC) down

restart:
	$(DC) down && $(DC) up -d

build:
	./mvnw clean package -DskipTests
	$(DC) up --build -d

logs:
	$(DC) logs -f app
