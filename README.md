# Schedule Management Microservices System

A complete microservices-based schedule management system built with Spring Boot, featuring JWT authentication, asynchronous messaging, and centralized API gateway routing.

## 🏗️ Architecture Overview

The system consists of 4 microservices communicating through an API Gateway:

```
Client → API Gateway (8080) → {
  AuthService (8081)         - User Authentication & Authorization
  RasporedService (8082)     - Schedule Management & CSV Processing
  NotificationService (8083) - Email Notifications
}

RasporedService ──JMS──→ NotificationService (Async Messaging)
```

## 🚀 Microservices

### 1. API Gateway (Port 8080)
**Technology**: Spring Cloud Gateway  
**Purpose**: Centralized routing, authentication, and load balancing

**Features**:
- Routes all client requests to appropriate microservices
- JWT token validation and user context forwarding
- CORS configuration for cross-origin requests
- Request/response filtering and transformation

**Routes**:
- `/auth/**` → AuthService (8081)
- `/api/raspored/**` → RasporedService (8082)  
- `/api/notification/**` → NotificationService (8083)

### 2. AuthService (Port 8081)
**Technology**: Spring Boot + JWT + H2 Database  
**Purpose**: User authentication and role-based authorization

**Features**:
- JWT token generation with configurable expiration (24 hours)
- Role-based access control (ADMIN, PROFESSOR)
- User registration and role assignment
- Password encryption with BCrypt

**Key Endpoints**:
- `POST /auth/prijava` - User login
- `GET /auth/uloge` - Get user roles  
- `POST /auth/dodela-uloge` - Assign roles (Admin only)

**Database Entities**:
- `User` - User credentials and basic info
- `Role` - User roles (ADMIN, PROFESSOR)
- User-Role many-to-many relationship

### 3. RasporedService (Port 8082)
**Technology**: Spring Boot + JPA/Hibernate + H2 Database + JMS  
**Purpose**: Core schedule management with CSV data import

**Features**:
- Complete CRUD operations for schedules, professors, subjects, classrooms
- CSV file parsing and automatic database population
- Advanced filtering and sorting capabilities
- Asynchronous notifications via JMS messaging
- Unique constraints preventing schedule conflicts

**Key Endpoints**:
- `POST /termin` - Create new schedule entry
- `DELETE /termin` - Delete schedule entry  
- `GET /termin/sortiraniPoUcioniciRastuce` - Get schedules sorted by classroom
- `POST /new/{nastavnik|predmet|grupa|ucionica}` - Create entities
- `GET /get/**` - Various filtering and search operations

**Database Entities**:
- `Termin` (Schedule) - Main scheduling entity
- `Nastavnik` (Professor) - Professor information
- `Predmet` (Subject) - Subject details
- `Ucionica` (Classroom) - Classroom information  
- `Grupa` (Group) - Student groups
- Complex many-to-many relationships between entities

**CSV Integration**:
- Automatic parsing of `raspored.csv` on startup
- Populates database with schedule data
- Handles duplicate data gracefully

### 4. NotificationService (Port 8083)
**Technology**: Spring Boot + JMS + H2 Database  
**Purpose**: Email notifications for schedule changes

**Features**:
- Asynchronous message processing via JMS
- Email notification storage and retrieval
- Schedule change notifications (ADD, UPDATE, DELETE)
- RESTful API for notification management

**Key Endpoints**:
- `POST /notification` - Send notification manually
- `GET /notification` - Get all notifications
- `GET /notification/{id}` - Get specific notification

**Database Entities**:
- `Notification` - Notification details and status
- Timestamps and user tracking

**JMS Integration**:
- Listens to `send_emails_queue` for schedule changes
- Processes messages from RasporedService
- Stores notification history

## 🔧 Technology Stack

- **Framework**: Spring Boot 3.2.1
- **Security**: JWT tokens, BCrypt password encryption
- **Database**: H2 in-memory databases
- **ORM**: JPA/Hibernate
- **Messaging**: JMS with ActiveMQ support
- **Gateway**: Spring Cloud Gateway (Reactive)
- **Build Tool**: Maven
- **Java Version**: 17

## 🚦 Getting Started

### Prerequisites
- **Java 17+** - Required for all Spring Boot services
- **Maven 3.6+** - For building and managing dependencies
- **Docker & Docker Compose** (recommended) - For easy setup and deployment
- **ActiveMQ** - Message broker for inter-service communication
  - Can be run via Docker (recommended) or installed locally
  - Default configuration uses `tcp://localhost:61616`
  - Admin console available at `http://localhost:8161` (admin/admin)

### Project Files
- `docker-compose.yml` - Full production environment setup
- `docker-compose.dev.yml` - Development environment (ActiveMQ only)
- `Makefile` - Common development commands and shortcuts
- `.env.example` - Environment variables template (copy to `.env` and customize)
- `.gitignore` - Git ignore patterns for the entire project

### Running the System

#### Option 1: Docker Compose (Recommended)

1. **Development Setup** (ActiveMQ only):
   ```bash
   # Copy environment variables template
   cp .env.example .env
   
   # Start development environment
   make dev-up
   # or
   docker-compose -f docker-compose.dev.yml up -d
   ```

2. **Full Production Setup**:
   ```bash
   # Build and start all services
   make up
   # or  
   docker-compose up -d
   ```

3. **Using Makefile shortcuts**:
   ```bash
   make help          # Show all available commands
   make dev-up        # Start development environment
   make build         # Build all services
   make status        # Check service health
   make logs          # View logs
   make down          # Stop all services
   ```

#### Option 2: Manual Setup

1. **Start API Gateway**:
   ```bash
   cd ApiGateway
   ./mvnw spring-boot:run
   ```

2. **Start AuthService**:
   ```bash
   cd AuthService  
   ./mvnw spring-boot:run
   ```

3. **Start RasporedService**:
   ```bash
   cd RasporedService
   ./mvnw spring-boot:run  
   ```

4. **Start NotificationService**:
   ```bash
   cd NotificationService
   ./mvnw spring-boot:run
   ```

### Service Health Check
- API Gateway: http://localhost:8080
- AuthService: http://localhost:8081
- RasporedService: http://localhost:8082  
- NotificationService: http://localhost:8083

## 📊 Database Schema

Each microservice has its own H2 database:

### AuthService Database
- **users** - User credentials
- **roles** - User roles
- **user_roles** - User-role assignments

### RasporedService Database  
- **termini** - Schedule entries
- **nastavnik** - Professors
- **predmet** - Subjects
- **ucionica** - Classrooms
- **grupa** - Student groups
- Various junction tables for relationships

### NotificationService Database
- **notifications** - Email notifications and status

## 🔐 Authentication Flow

1. **User Login**: POST to `/auth/prijava` with credentials
2. **JWT Token**: Receive JWT token with user info and roles
3. **API Calls**: Include `Authorization: Bearer <token>` header
4. **Gateway Validation**: API Gateway validates JWT on each request
5. **Service Access**: Validated requests forwarded to microservices

## 📨 Messaging Flow

1. **Schedule Change**: RasporedService detects schedule modification
2. **JMS Message**: Sends message to `send_emails_queue`
3. **Notification Processing**: NotificationService receives and processes message
4. **Email Storage**: Notification stored in database with timestamp
5. **History Tracking**: All notifications available via REST API

## 🏃‍♂️ Development

### Adding New Features
1. **Entities**: Add JPA entities to appropriate service
2. **Controllers**: Create REST endpoints  
3. **Services**: Implement business logic
4. **Gateway**: Add routing rules if needed
5. **Security**: Add authorization checks
6. **Messaging**: Integrate with JMS if notifications needed

### Testing
Each service includes:
- Unit tests for services and controllers
- Integration tests for database operations
- H2 console available at `/h2-console` for development

## 📝 Configuration

### JWT Configuration
- **Secret**: Configured in each service's `application.properties`
- **Expiration**: 24 hours (configurable)
- **Roles**: ADMIN, PROFESSOR

### Database Configuration
- **Type**: H2 in-memory
- **Console**: Enabled in development
- **Auto-creation**: Enabled with `update` strategy

### JMS Configuration
- **Queue**: `send_emails_queue`
- **Broker**: ActiveMQ (configurable)
- **Connection Factory**: Auto-configured

## 🔍 Monitoring & Troubleshooting

### Common Issues
- **Port Conflicts**: Ensure ports 8080-8083 are available
- **JWT Secrets**: Must match across all services
- **Database**: H2 data is lost on restart (by design)
- **Messaging**: ActiveMQ broker needed for full functionality

### Health Checks
- Check service logs for startup errors
- Verify database connectivity via H2 console
- Test JWT token generation/validation
- Monitor JMS queue status

## 📋 API Documentation

### Authentication Required
Most endpoints require JWT authentication except:
- `POST /auth/prijava` - Login endpoint
- Health check endpoints

### Response Formats
All APIs return JSON with consistent error handling:
```json
{
  "status": "success|error",
  "data": {...},
  "message": "Success message or error details"
}
```

## 🎯 Future Enhancements

- **Service Discovery**: Eureka integration
- **Configuration Management**: Spring Cloud Config
- **Monitoring**: Actuator endpoints + Prometheus
- **API Documentation**: Swagger/OpenAPI integration
- **Database**: PostgreSQL for production
- **Caching**: Redis integration
- **Load Balancing**: Multiple service instances
- **Container Deployment**: Docker & Kubernetes support

## 👥 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)  
5. Open Pull Request

## 📄 License

This project is part of an academic assignment for microservices architecture demonstration.