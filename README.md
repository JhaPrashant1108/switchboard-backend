# SwitchBoard

A robust feature flag and rollout management system built with Spring Boot, designed for controlling feature toggles across multiple applications and environments.

## Overview

SwitchBoard provides a centralized platform for managing feature flags (switches) with support for:
- Multi-environment configuration (dev, staging, production)
- Multi-application support
- Real-time updates via RabbitMQ
- High-performance caching with Redis
- Metered rollouts for gradual feature releases
- RESTful API for easy integration

## Tech Stack

- **Java 21**
- **Spring Boot 3.5.6**
- **PostgreSQL** - Primary data store
- **Redis** - Caching layer
- **RabbitMQ** - Message queue for real-time updates
- **Maven** - Build tool
- **Lombok** - Boilerplate reduction
- **MapStruct** - Object mapping

## Features

### Core Functionality
- Create, read, update, and delete feature flags
- Environment-based switch management
- Application-level switch isolation
- Metered status tracking for gradual rollouts
- Automatic audit trails (created/updated timestamps and users)

### Performance
- Redis caching for low-latency switch lookups
- RabbitMQ message publishing for real-time updates
- Connection pooling for optimal resource usage

### Security
- Environment variable-based configuration
- Profile-based deployment (dev/prod)
- Configurable CORS policies
- No hardcoded credentials

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+
- PostgreSQL 12+
- Redis 6+
- RabbitMQ 3.8+

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd switchboard-backend
   ```

2. **Set up environment variables**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   nano .env
   ```

3. **Configure database**
   ```sql
   CREATE DATABASE switchboard;
   ```

4. **Build the project**
   ```bash
   ./mvnw clean install
   ```

5. **Run the application**

   **Development mode:**
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

   **With environment variables:**
   ```bash
   export $(cat .env | xargs)
   ./mvnw spring-boot:run
   ```

   **Production mode:**
   ```bash
   java -jar target/SwitchBoard-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
   ```

## Configuration

### Environment Variables

Create a `.env` file based on `.env.example`:

```bash
# Database
DB_URL=jdbc:postgresql://localhost:5432/switchboard
DB_USERNAME=postgres
DB_PASSWORD=your_password

# Redis
REDIS_HOSTNAME=localhost
REDIS_PORT=6379
REDIS_PASSWORD=your_redis_password

# RabbitMQ
RABBITMQ_HOSTNAME=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:3000
```

See [CONFIGURATION.md](CONFIGURATION.md) for complete configuration reference.

### Profiles

- **default** - Uses environment variables with sensible defaults
- **dev** - Development configuration with debug logging
- **prod** - Production configuration with strict security

## API Documentation

### Base URL
```
http://localhost:8080/switch
```

### Endpoints

#### Create Switch
```http
POST /switch/createswitch
Content-Type: application/json

{
  "switchId": "feature-new-ui",
  "switchName": "new_ui",
  "switchDescription": "Enable new UI design",
  "applicationName": "web-app",
  "environmentName": "production",
  "status": true,
  "meteredStatus": {
    "user_group_1": {
      "percentage": 50
    }
  },
  "createdBy": "admin",
  "createdAt": "2025-10-30T10:00:00Z"
}
```

#### Read Switch
```http
GET /switch/readswitch
switchId: feature-new-ui
```

#### Fetch All Switches
```http
GET /switch/fetchallswitch
environmentName: production
applicationName: web-app
```

#### Fetch Specific Switches
```http
POST /switch/fetchswitch
Content-Type: application/json
applicationName: web-app
environmentName: production

{
  "switchNames": ["new_ui", "dark_mode", "beta_features"]
}
```

#### Update Switch
```http
PUT /switch/updateswitch
Content-Type: application/json

{
  "switchId": "feature-new-ui",
  "switchName": "new_ui",
  "status": false,
  "updatedBy": "admin",
  "updatedAt": "2025-10-30T11:00:00Z"
}
```

#### Delete Switch
```http
DELETE /switch/deleteswitch
switchId: feature-new-ui
```

## Architecture

### Data Flow

```
Client Request → Controller → Service → Repository (PostgreSQL + Redis)
                                    ↓
                              Message Publisher → RabbitMQ
```

### Components

- **Controllers** - REST API endpoints
- **Services** - Business logic layer
- **Repositories** - Data access layer (PostgreSQL + Redis)
- **Entities** - JPA entity models
- **Models** - Domain models for business logic
- **DTOs** - Data transfer objects
- **Mappers** - MapStruct entity-model converters
- **Components** - Message publishers for RabbitMQ
- **Config** - Configuration classes

### Database Schema

**Table: `switch`**

| Column | Type | Constraints |
|--------|------|-------------|
| switch_id | VARCHAR(50) | PRIMARY KEY |
| switch_name | VARCHAR | NOT NULL |
| switch_description | VARCHAR | |
| application_name | VARCHAR | NOT NULL |
| environment_name | VARCHAR | NOT NULL |
| status | BOOLEAN | |
| metered_status | TEXT | JSON format |
| created_by | VARCHAR(50) | |
| created_at | VARCHAR(50) | |
| updated_by | VARCHAR(50) | |
| updated_at | VARCHAR(50) | |

**Unique Constraint:** `(environment_name, switch_name)`

## Development

### Project Structure

```
switchboard-backend/
├── src/
│   ├── main/
│   │   ├── java/com/jhaprashant1108/SwitchBoard/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controllers/     # REST controllers
│   │   │   ├── services/        # Business logic
│   │   │   ├── repositories/    # Data access
│   │   │   ├── entities/        # JPA entities
│   │   │   ├── models/          # Domain models
│   │   │   ├── dtos/            # Data transfer objects
│   │   │   ├── mappers/         # MapStruct mappers
│   │   │   ├── components/      # Message publishers
│   │   │   └── utils/           # Utility classes
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
├── .env.example
├── .gitignore
├── pom.xml
├── README.md
├── CONFIGURATION.md
└── DEPLOYMENT.md
```

### Building

```bash
# Clean build
./mvnw clean install

# Skip tests
./mvnw clean install -DskipTests

# Run tests only
./mvnw test

# Create JAR
./mvnw package
```

### Code Style

- Follow Java naming conventions
- Use Lombok annotations to reduce boilerplate
- Keep methods focused and concise
- Document public APIs
- Use constants for magic numbers and strings

## Deployment

For detailed deployment instructions, see [DEPLOYMENT.md](DEPLOYMENT.md).

### Quick Production Deployment

1. Set environment variables
2. Build the application: `./mvnw clean package`
3. Run with production profile: `java -jar target/SwitchBoard-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod`

### Docker Deployment

```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/SwitchBoard-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
docker build -t switchboard:latest .
docker run -p 8080:8080 --env-file .env switchboard:latest
```

## Monitoring

### Health Check

```bash
curl http://localhost:8080/actuator/health
```

### Metrics

Access application metrics at:
```
http://localhost:8080/actuator/metrics
```

## Security

- All credentials use environment variables
- CORS configurable per environment
- SSL/TLS support for external connections
- No hardcoded secrets in codebase
- Comprehensive .gitignore for sensitive files

## Performance Optimization

- Redis caching for frequently accessed switches
- Database connection pooling (HikariCP)
- Efficient JSON serialization/deserialization
- Indexed database queries

## Troubleshooting

### Common Issues

**Database connection failed**
- Verify PostgreSQL is running
- Check DB_URL, DB_USERNAME, DB_PASSWORD
- Ensure database exists

**Redis connection failed**
- Verify Redis is running: `redis-cli ping`
- Check REDIS_HOSTNAME and REDIS_PORT
- Verify credentials if authentication is enabled

**RabbitMQ connection failed**
- Verify RabbitMQ is running: `rabbitmqctl status`
- Check RABBITMQ_HOSTNAME and RABBITMQ_PORT
- Verify virtual host exists

**CORS errors**
- Update CORS_ALLOWED_ORIGINS to include your frontend URL
- Ensure no trailing slashes in origin URLs

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -am 'Add new feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Submit a pull request

## License

[Specify your license here]

## Contact

Project maintained by Prashant Jha

## Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL, Redis, and RabbitMQ communities

## Changelog

### Version 1.0.0 (2025-10-30)
- Initial release
- Feature flag management system
- Multi-environment and multi-application support
- Redis caching integration
- RabbitMQ message publishing
- RESTful API
- Environment-based configuration
- Production-ready deployment