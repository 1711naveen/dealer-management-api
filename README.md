# Dealer Management API

A comprehensive REST API for managing dealers, vehicles, and payment processing with JWT authentication.

## 🚀 Features

### Task 1: Dealer & Vehicle Management API
- **Dealer Management**: Complete CRUD operations for dealers with subscription types (BASIC/PREMIUM)
- **Vehicle Management**: Full CRUD operations for vehicles with dealer associations
- **Premium Dealer Vehicles**: Special endpoint to fetch vehicles from PREMIUM dealers only
- **Auto-generated SQL schema** using JPA annotations
- **Swagger UI** for API testing and documentation

### Task 2: Payment Gateway Simulation
- **Payment Initiation**: Endpoint `/api/payment/initiate` for processing dealer subscriptions
- **Automatic Status Updates**: Payments auto-update from PENDING to SUCCESS after 5 seconds
- **JWT Authentication**: Secure payment endpoints using Bearer tokens
- **Multiple Payment Methods**: Support for UPI, Card, and NetBanking
- **Transaction Tracking**: Unique transaction IDs for each payment

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Database**: PostgreSQL with JPA/Hibernate
- **Security**: Spring Security with JWT
- **Documentation**: Swagger/OpenAPI 3
- **Build Tool**: Maven
- **Java Version**: 17

## 📋 Prerequisites

1. **Java 17** or higher
2. **PostgreSQL** database
3. **Maven** 3.6 or higher

## 🚀 Quick Start

### 1. Database Setup

Create a PostgreSQL database:
```sql
CREATE DATABASE dealer_management;
```

### 2. Configure Database

Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/dealer_management
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Run the Application

```bash
# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Swagger UI
Access the interactive API documentation at:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

### Authentication

#### Get JWT Token (for Payment APIs)
```bash
POST /api/auth/generate-token
# Returns a demo JWT token for testing
```

#### Using JWT Token
Add to request headers:
```
Authorization: Bearer <your-jwt-token>
```

## 🔗 API Endpoints

### 🏢 Dealer Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/dealers` | Get all dealers |
| GET | `/api/dealers/{id}` | Get dealer by ID |
| GET | `/api/dealers/email/{email}` | Get dealer by email |
| GET | `/api/dealers/subscription/{type}` | Get dealers by subscription type |
| POST | `/api/dealers` | Create new dealer |
| PUT | `/api/dealers/{id}` | Update dealer |
| DELETE | `/api/dealers/{id}` | Delete dealer |

### 🚗 Vehicle Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/vehicles` | Get all vehicles |
| GET | `/api/vehicles/{id}` | Get vehicle by ID |
| GET | `/api/vehicles/dealer/{dealerId}` | Get vehicles by dealer |
| GET | `/api/vehicles/status/{status}` | Get vehicles by status |
| GET | `/api/vehicles/premium-dealers` | **Get vehicles from PREMIUM dealers only** |
| GET | `/api/vehicles/search?model={model}` | Search vehicles by model |
| POST | `/api/vehicles` | Create new vehicle |
| PUT | `/api/vehicles/{id}` | Update vehicle |
| DELETE | `/api/vehicles/{id}` | Delete vehicle |

### 💳 Payment Gateway

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/payment/initiate` | Initiate payment | ✅ JWT |
| GET | `/api/payment` | Get all payments | ✅ JWT |
| GET | `/api/payment/{id}` | Get payment by ID | ✅ JWT |
| GET | `/api/payment/dealer/{dealerId}` | Get payments by dealer | ✅ JWT |
| GET | `/api/payment/status/{status}` | Get payments by status | ✅ JWT |
| GET | `/api/payment/transaction/{transactionId}` | Get payment by transaction ID | ✅ JWT |

## 📝 Sample API Calls

### Create a Dealer
```bash
POST /api/dealers
Content-Type: application/json

{
  "name": "Premium Motors Ltd",
  "email": "contact@premiummotors.com",
  "subscriptionType": "PREMIUM"
}
```

### Create a Vehicle
```bash
POST /api/vehicles
Content-Type: application/json

{
  "dealerId": 1,
  "model": "BMW X5",
  "price": 55000.00,
  "status": "AVAILABLE"
}
```

### Get Vehicles from Premium Dealers Only
```bash
GET /api/vehicles/premium-dealers
```

### Initiate Payment
```bash
POST /api/payment/initiate
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "dealerId": 1,
  "amount": 999.99,
  "method": "UPI"
}
```

## 🗄️ Database Schema

The application automatically creates the following tables:

### dealers
- `id` (BIGINT, Primary Key)
- `name` (VARCHAR, NOT NULL)
- `email` (VARCHAR, NOT NULL, UNIQUE)
- `subscription_type` (VARCHAR, NOT NULL) - BASIC/PREMIUM
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### vehicles
- `id` (BIGINT, Primary Key)
- `dealer_id` (BIGINT, Foreign Key)
- `model` (VARCHAR, NOT NULL)
- `price` (DECIMAL, NOT NULL)
- `status` (VARCHAR, NOT NULL) - AVAILABLE/SOLD
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### payments
- `id` (BIGINT, Primary Key)
- `dealer_id` (BIGINT, Foreign Key)
- `amount` (DECIMAL, NOT NULL)
- `payment_method` (VARCHAR, NOT NULL) - UPI/CARD/NETBANKING
- `status` (VARCHAR, NOT NULL) - PENDING/SUCCESS/FAILED
- `transaction_id` (VARCHAR, UNIQUE)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Sample Data
The application includes sample data initialization:
- 4 dealers (2 PREMIUM, 2 BASIC)
- 9 vehicles across different dealers
- Automatic data loading on first startup

### Test with Postman
Import the API endpoints from Swagger and test:
1. Get JWT token from `/api/auth/generate-token`
2. Use token for payment API calls
3. Test all CRUD operations for dealers and vehicles
4. Verify premium dealer vehicle filtering

## 🔧 Configuration

### JWT Configuration
```properties
app.jwt.secret=your-secret-key
app.jwt.expiration=86400000  # 24 hours
```

### Database Configuration
```properties
spring.jpa.hibernate.ddl-auto=create-drop  # Change to 'update' for production
spring.jpa.show-sql=true  # Set to false for production
```

## 🚀 Deployment

### Building for Production
```bash
mvn clean package
java -jar target/dealer-management-api-0.0.1-SNAPSHOT.jar
```

### Docker Support
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 🤝 API Response Format

### Success Response
```json
{
  "id": 1,
  "name": "Premium Motors",
  "email": "premium@example.com",
  "subscriptionType": "PREMIUM"
}
```

### Error Response
```json
{
  "timestamp": "2024-08-16T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Dealer with email already exists",
  "fieldErrors": {
    "email": "Email should be valid"
  }
}
```

## 📋 Key Features Implemented

✅ **Task 1 Requirements:**
- [x] Dealer entity with id, name, email, subscriptionType
- [x] Vehicle entity with id, dealerId, model, price, status
- [x] Complete CRUD REST APIs for both entities
- [x] Special API for fetching vehicles from PREMIUM dealers only
- [x] Spring Boot + JPA + PostgreSQL implementation
- [x] Auto-generated SQL schema via JPA
- [x] Swagger documentation for API testing

✅ **Task 2 Requirements:**
- [x] `/api/payment/initiate` endpoint
- [x] Accepts dealerId, amount, method (UPI/Card/NetBanking)
- [x] Saves payment with PENDING status
- [x] Auto-updates to SUCCESS after 5 seconds
- [x] JWT authentication for secure API access
- [x] PostgreSQL table for transaction storage
- [x] Complete payment tracking system

## 🔍 Additional Features

- **Global Exception Handling**: Consistent error responses
- **Input Validation**: Comprehensive validation with custom messages
- **Async Payment Processing**: Non-blocking payment status updates
- **Sample Data Initialization**: Ready-to-test data on startup
- **Comprehensive Logging**: Debug and error logging
- **Security Configuration**: JWT-based authentication with configurable endpoints

## 📞 Support

For any issues or questions, please contact:
- **Email**: yn.naveen00@gmail.com
- **API Documentation**: http://localhost:8080/swagger-ui.html

---

**Note**: This is a complete implementation of the Dealer & Vehicle Management API with Payment Gateway simulation as requested. The application is ready for testing and deployment.
