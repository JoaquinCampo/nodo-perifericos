# Nodos Periféricos - Spring Boot

Peripheral node component for HCEN (Historia Clínica Electrónica Nacional) system.

## Technology Stack

- Spring Boot 3.3.5
- Java 17
- PostgreSQL
- Spring Security
- Spring Data JPA
- Flyway
- JWT
- Spring Mail
- Thymeleaf

## Setup

1. Ensure PostgreSQL is running
2. Set environment variables:
   - `DB_USER`
   - `DB_PASSWORD`
   - `EMAIL_HOST`
   - `EMAIL_PORT`
   - `EMAIL_USER`
   - `EMAIL_PASSWORD`
   - `EMAIL_FROM`
   - `HCEN_BASE_URL`
   - `HCEN_APP_USERNAME`
   - `HCEN_APP_PASSWORD`
   - `JWT_SECRET`

3. Run the application:
```bash
mvn spring-boot:run
```

## API Documentation

Once running, access Swagger UI at: http://localhost:8080/swagger-ui.html

