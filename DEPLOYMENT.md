# Production Deployment Guide

This guide walks through deploying SwitchBoard to production.

## Before You Deploy

**Security checklist:**
- [ ] Set up environment variables (don't use hardcoded values)
- [ ] Configure CORS with your actual domain (remove wildcards)
- [ ] Enable SSL/TLS for database and message queue connections
- [ ] Set up a secrets manager if possible (AWS Secrets Manager, Vault, etc.)
- [ ] Make sure `.env` files aren't committed to git

**Required environment variables:**
- `DB_PASSWORD` - Your PostgreSQL password
- `REDIS_PASSWORD` - Redis auth password
- `RABBITMQ_PASSWORD` - RabbitMQ password
- `CORS_ALLOWED_ORIGINS` - Your frontend domain(s)

Check `.env.example` for the full list with defaults.

---

## Deployment Process

### 1. Configure Environment Variables

You have a few options here:

**For local/staging (using .env file):**
```bash
cp .env.example .env
nano .env  # edit with your values
export $(cat .env | xargs)
```

**For production (direct export):**
```bash
export DB_PASSWORD=your_secure_password
export REDIS_PASSWORD=your_redis_password
export RABBITMQ_PASSWORD=your_rabbitmq_password
export RABBITMQ_SSL_ENABLED=true
export CORS_ALLOWED_ORIGINS=https://yourdomain.com
```

**Using cloud secrets (recommended):**

AWS Secrets Manager:
```bash
aws secretsmanager create-secret --name switchboard/db-password --secret-string "your_password"
```

Azure Key Vault:
```bash
az keyvault secret set --vault-name "switchboard-vault" --name "db-password" --value "your_password"
```

### 2. Build the Application

```bash
cd switchboard-backend
./mvnw clean package -DskipTests
```

Or run tests first:
```bash
./mvnw clean package
```

### 3. Run the Application

**Standalone JAR:**
```bash
java -jar target/SwitchBoard-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

**Using Docker:**

Create a `Dockerfile`:
```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/SwitchBoard-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
```

Build and run:
```bash
docker build -t switchboard:latest .
docker run -p 8080:8080 \
  -e DB_PASSWORD=secure_password \
  -e REDIS_PASSWORD=redis_password \
  -e RABBITMQ_PASSWORD=rabbitmq_password \
  -e CORS_ALLOWED_ORIGINS=https://yourdomain.com \
  switchboard:latest
```

### 4. Verify Everything Works

Check health:
```bash
curl http://localhost:8080/actuator/health
```

Test an endpoint:
```bash
curl -X GET http://localhost:8080/switch/readswitch -H "switchId: test-id"
```

Check the logs to confirm:
- Database connection is successful
- Redis connection is successful
- RabbitMQ connection is successful

---

## Production Settings

A few things to tweak in `application-prod.properties` for production:

**Database:**
Set `spring.jpa.hibernate.ddl-auto=validate` instead of `update`. You don't want Hibernate auto-modifying your production schema. Use Flyway or Liquibase for migrations.

**Logging:**
```properties
logging.level.com.jhaprashant1108.SwitchBoard=INFO
logging.file.name=/var/log/switchboard/application.log
```

**CORS:**
Lock it down to your actual domain:
```properties
cors.allowed-origins=https://yourdomain.com
cors.allowed-methods=GET,POST,PUT,DELETE
```

**Connection Pooling:**
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
```

---

## Monitoring

Enable health and metrics endpoints:

```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=when-authorized
```

Keep an eye on:
- Database connection pool usage
- Redis connection health
- RabbitMQ queue depth
- API response times
- Error rates

For centralized logging, integrate with ELK, Splunk, CloudWatch, or whatever you use.

---

## Troubleshooting

**Database won't connect:**
- Check `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- Verify the database server is reachable
- Make sure the database exists

**Redis issues:**
- Confirm Redis is running: `redis-cli ping`
- Check `REDIS_HOSTNAME` and `REDIS_PORT`
- Verify auth credentials if enabled

**RabbitMQ problems:**
- Verify RabbitMQ is up: `rabbitmqctl status`
- Check hostname, port, and credentials
- Make sure the virtual host exists

**CORS errors:**
- Update `CORS_ALLOWED_ORIGINS` with your frontend URL
- Don't include trailing slashes
- Check browser console for details

---

## Rollback

If something goes wrong:

1. Redeploy the previous JAR:
   ```bash
   java -jar target/SwitchBoard-0.0.1-SNAPSHOT-backup.jar --spring.profiles.active=prod
   ```

2. Rollback database migrations if needed (Flyway/Liquibase)

3. Revert environment variables to previous values

---

## Security Reminders

- Rotate credentials every 90 days
- Use strong passwords (16+ characters)
- Enable SSL/TLS for all connections in production
- Never use `*` for CORS origins
- Keep your dependencies up to date
- Use a secrets manager for sensitive config
