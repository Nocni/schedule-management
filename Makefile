# Schedule Management System - Development Makefile

# Variables
SERVICES = AuthService NotificationService RasporedService ApiGateway
MAVEN_OPTS = -Dmaven.test.skip=true

# Default target
.PHONY: help
help:
	@echo "Schedule Management System - Available Commands:"
	@echo ""
	@echo "Development:"
	@echo "  make dev-up         Start development environment (ActiveMQ only)"
	@echo "  make dev-down       Stop development environment"
	@echo "  make build          Build all services"
	@echo "  make clean          Clean all services"
	@echo "  make test           Run tests for all services"
	@echo ""
	@echo "Production:"
	@echo "  make up             Start all services with Docker Compose"
	@echo "  make down           Stop all services"
	@echo "  make logs           Show logs from all services"
	@echo ""
	@echo "Individual Services:"
	@echo "  make run-auth       Run AuthService locally"
	@echo "  make run-notification Run NotificationService locally"
	@echo "  make run-raspored   Run RasporedService locally"
	@echo "  make run-gateway    Run ApiGateway locally"
	@echo ""
	@echo "Utilities:"
	@echo "  make status         Check status of all services"
	@echo "  make health         Health check for all services"

# Development environment (ActiveMQ only)
.PHONY: dev-up
dev-up:
	@echo "Starting development environment..."
	docker-compose -f docker-compose.dev.yml up -d
	@echo "ActiveMQ Admin Console: http://localhost:8161 (admin/admin)"

.PHONY: dev-down
dev-down:
	@echo "Stopping development environment..."
	docker-compose -f docker-compose.dev.yml down

# Build all services
.PHONY: build
build:
	@echo "Building all services..."
	@for service in $(SERVICES); do \
		echo "Building $$service..."; \
		cd $$service && ./mvnw clean package $(MAVEN_OPTS) && cd ..; \
	done
	@echo "All services built successfully!"

# Clean all services
.PHONY: clean
clean:
	@echo "Cleaning all services..."
	@for service in $(SERVICES); do \
		echo "Cleaning $$service..."; \
		cd $$service && ./mvnw clean && cd ..; \
	done

# Test all services
.PHONY: test
test:
	@echo "Running tests for all services..."
	@for service in $(SERVICES); do \
		echo "Testing $$service..."; \
		cd $$service && ./mvnw test && cd ..; \
	done

# Production Docker Compose
.PHONY: up
up:
	@echo "Starting all services..."
	docker-compose up -d
	@echo "Services started! Gateway available at: http://localhost:8080"

.PHONY: down
down:
	@echo "Stopping all services..."
	docker-compose down

.PHONY: logs
logs:
	docker-compose logs -f

# Individual service runners
.PHONY: run-auth
run-auth:
	@echo "Starting AuthService..."
	cd AuthService && ./mvnw spring-boot:run

.PHONY: run-notification
run-notification:
	@echo "Starting NotificationService..."
	cd NotificationService && ./mvnw spring-boot:run

.PHONY: run-raspored
run-raspored:
	@echo "Starting RasporedService..."
	cd RasporedService && ./mvnw spring-boot:run

.PHONY: run-gateway
run-gateway:
	@echo "Starting ApiGateway..."
	cd ApiGateway && ./mvnw spring-boot:run

# Status and health checks
.PHONY: status
status:
	@echo "Checking service status..."
	@echo "AuthService (8081):"
	@curl -s -f http://localhost:8081/actuator/health 2>/dev/null && echo "✅ Healthy" || echo "❌ Unhealthy"
	@echo "NotificationService (8082):"
	@curl -s -f http://localhost:8082/actuator/health 2>/dev/null && echo "✅ Healthy" || echo "❌ Unhealthy"
	@echo "RasporedService (8083):"
	@curl -s -f http://localhost:8083/actuator/health 2>/dev/null && echo "✅ Healthy" || echo "❌ Unhealthy"
	@echo "ApiGateway (8080):"
	@curl -s -f http://localhost:8080/actuator/health 2>/dev/null && echo "✅ Healthy" || echo "❌ Unhealthy"

.PHONY: health
health: status