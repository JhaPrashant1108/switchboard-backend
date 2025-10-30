# Configuration Guide

This guide covers how configuration works in SwitchBoard and how to set it up for different environments.

## Quick Start

**For local development:**
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

**For production:**
```bash
# Set your environment variables first
export DB_PASSWORD=your_password
export REDIS_PASSWORD=your_redis_password
export RABBITMQ_PASSWORD=your_rabbitmq_password

# Then run with prod profile
java -jar target/SwitchBoard-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

---

## Application Constants

I've consolidated all the magic strings and hardcoded values into two files:

**AppConstants.java** - Houses all the application-level constants like queue names, endpoints, and DB schema details. It's organized into nested classes by category (RabbitMQ, Redis, API, Database, etc.) to keep things clean.

**ErrorMessages.java** - Contains standardized error messages for consistency across the application.

You can reference these anywhere in the code like `AppConstants.RabbitMQ.EXCHANGE` or `ErrorMessages.Connection.REDIS_CONNECTION_FAILED`.

---

## Environment Variables

The application reads sensitive config from environment variables instead of hardcoding them. Check `.env.example` for the full list.

**Required variables:**
- `DB_PASSWORD` - Your PostgreSQL password
- `REDIS_PASSWORD` - Your Redis password (if using auth)
- `RABBITMQ_PASSWORD` - Your RabbitMQ password

**Optional (have defaults):**
- `DB_URL` - Defaults to `jdbc:postgresql://localhost:5432/switchboard`
- `DB_USERNAME` - Defaults to `postgres`
- `REDIS_HOSTNAME` - Defaults to `localhost`
- `REDIS_PORT` - Defaults to `6379`
- `RABBITMQ_HOSTNAME` - Defaults to `localhost`
- `RABBITMQ_PORT` - Defaults to `5672`
- `CORS_ALLOWED_ORIGINS` - Defaults to `http://localhost:3000`

**Setting them up:**
```bash
# Copy the example file
cp .env.example .env

# Edit with your values
nano .env

# Load into your shell
export $(cat .env | xargs)
```

---

## Profiles

The app supports three configuration profiles:

**Default (`application.properties`)**
Basic config with environment variable placeholders. If you don't specify a profile, this is what you get.

**Development (`application-dev.properties`)**
Runs everything on localhost with debug logging turned on. Good for local development.
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

**Production (`application-prod.properties`)**
Strict security settings, INFO-level logging, and requires all credentials via environment variables.
```bash
java -jar target/SwitchBoard-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

---

## Common Issues

**"DB_PASSWORD not set" error**
Export the required environment variable:
```bash
export DB_PASSWORD=your_password
```

**CORS errors in the browser**
Make sure your frontend URL is in the allowed origins:
```bash
export CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080
```

**Can't connect to Redis**
Check if Redis is running and your credentials are correct:
```bash
redis-cli -h $REDIS_HOSTNAME -p $REDIS_PORT -a $REDIS_PASSWORD ping
```

---

## Security Notes

- Never commit `.env` files to version control
- Use different credentials for each environment
- Consider using a secrets manager (AWS Secrets Manager, Vault, etc.) in production
- Don't use `*` for CORS origins in production
- Always enable SSL/TLS for production deployments

---

## Additional Reading

- [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [Spring Profiles Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)
- See `DEPLOYMENT.md` for production deployment steps
