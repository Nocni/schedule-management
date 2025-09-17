# API Gateway Documentation

## Overview
The API Gateway serves as the single entry point for all client requests to the Schedule Management microservices architecture. It provides routing, authentication, and cross-cutting concerns handling.

## Architecture
```
Client -> API Gateway (Port 8080) -> Microservices
                                   ├─ AuthService (Port 8081)
                                   ├─ RasporedService (Port 8082)
                                   └─ NotificationService (Port 8083)
```

## Features

### 🔀 **Routing**
- **AuthService**: `/api/auth/**` → `http://localhost:8081`
- **RasporedService**: `/api/raspored/**` → `http://localhost:8082`
- **NotificationService**: `/api/notification/**` → `http://localhost:8083`

### 🔐 **Authentication**
- JWT-based authentication for protected routes
- Public endpoints: `/api/auth/auth/prijava`, `/api/auth/user/register`
- Protected endpoints: All other routes require valid JWT token

### 🌐 **CORS Support**
- Configured for cross-origin requests
- Allows all origins, methods, and headers
- Supports credentials

## Endpoints

### Gateway Information
```http
GET /
# Returns gateway information and available routes

GET /health  
# Returns health status
```

### Routed Services
```http
# Authentication (Public)
POST /api/auth/auth/prijava       # Login
POST /api/auth/user/register      # User registration

# Authentication (Protected)
POST /api/auth/auth/uloge         # Create roles
POST /api/auth/auth/dodela-uloge  # Assign roles
GET  /api/auth/user              # List users
# ... other auth endpoints

# Schedule Management (Protected)
GET  /api/raspored/**            # All schedule endpoints
POST /api/raspored/**
PUT  /api/raspored/**
DELETE /api/raspored/**

# Notifications (Protected)
GET  /api/notification/**        # All notification endpoints  
POST /api/notification/**
```

## Authentication Flow

1. **Login**: Client calls `POST /api/auth/auth/prijava` with credentials
2. **Token**: AuthService returns JWT token
3. **Requests**: Client includes token in `Authorization: Bearer <token>` header
4. **Gateway**: Validates token and forwards request with user context
5. **Services**: Receive request with additional headers:
   - `username`: Username from token
   - `role`: User role from token  
   - `userId`: User ID from token

## Configuration

### JWT Configuration
```yaml
jwt:
  secret: secret_key  # Must match AuthService secret
```

### Service URLs
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: http://localhost:8081
        - id: raspored-service  
          uri: http://localhost:8082
        - id: notification-service
          uri: http://localhost:8083
```

## Error Handling
- **401 Unauthorized**: Missing or invalid JWT token
- **404 Not Found**: Route not configured
- **500 Internal Server Error**: Downstream service error

## Security Features
- JWT token validation
- Token expiration checking
- Header population with user context
- CORS configuration
- Request/response logging (DEBUG mode)

## Usage Examples

### Login and Get Token
```bash
curl -X POST http://localhost:8080/api/auth/auth/prijava \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
```

### Access Protected Resource
```bash
curl -X GET http://localhost:8080/api/raspored/termini \
  -H "Authorization: Bearer <your-jwt-token>"
```

### Health Check
```bash
curl http://localhost:8080/health
```

## Deployment Notes
- Ensure all microservices are running on configured ports
- JWT secret must be consistent across AuthService and Gateway
- Consider service discovery (Eureka) for production deployment
- Monitor logs for routing and authentication issues