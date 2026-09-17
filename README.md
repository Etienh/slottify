# Slottify

Slottify is a backend API for scheduling and managing appointments within projects. It lets a project define shared resources **items** (e.g. rooms, equipment) and **priorities**  and books **appointments** for users and/or items while automatically detecting scheduling conflicts (overlapping time slots), depending on whether the project's calendar is shared per user, per item, or both.

## Tech Stack

- **Language:** Java 24
- **Framework:** Spring Boot 3 (Spring Web, Spring Data JPA, Spring Security)
- **Auth:** Keycloak (OAuth2 / OpenID Connect resource server)
- **Database:** PostgreSQL
- **Migrations:** Flyway
- **Object Mapping:** MapStruct
- **Docs:** springdoc-openapi (Swagger UI)
- **Build Tool:** Maven
- **Misc:** Lombok

## Prerequisites

- JDK 24
- Maven
- Docker (for PostgreSQL and Keycloak)

## Getting Started

1. **Start the infrastructure** (PostgreSQL and Keycloak) from the `appointment-scheduler` directory:

   ```bash
   cd appointment-scheduler
   docker compose -f docker-compose.postgres.yaml up -d
   docker compose -f docker-compose.keycloak.yaml up -d
   ```

2. **Configure Keycloak**: open the admin console at `http://localhost:8282` (user: `admin` / password: `admin`) and create a realm named `slottify` with a client named `slottify-client`, matching `application.yaml`.

3. **Run the application**:

   ```bash
   mvn spring-boot:run
   ```

   The app starts on `http://localhost:8080`. Database tables are created automatically via Flyway migrations on startup.

4. **Explore the API**: Swagger UI is available at `http://localhost:8080/swagger-ui.html`.

## Running Tests

```bash
cd appointment-scheduler
mvn test
```
