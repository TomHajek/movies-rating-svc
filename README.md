# 🎬 Movies Rating Service

This is a Spring Boot demo REST API project for registering users, log in, and rating movies.
User authentication and authorization are handled through Spring Security with JWT. Only authenticated
users can add, update or delete their movie ratings.

## ✨ Features

- **User Management**
    - Register with email and password.
    - Login and receive a JWT token.
    - Delete own account (requires authentication).

- **Movie Management**
    - View all movies.
    - Get paginated leaderboard sorted by average rating.
    - Get top-rated movie (latest created wins in case of a tie).

- **Rating Management**
    - Add a rating to a movie.
    - Update an existing rating.
    - Delete rating for a movie.

- **Security**
    - JWT authentication with Spring Security.
    - Passwords hashed with BCrypt.
    - Custom global exception handling.

- **Documentation**
    - API fully documented with [SpringDoc OpenAPI](https://springdoc.org/).

- **Monitoring**
    - Spring Boot Actuator endpoints enabled for health checks and information.

---

## 🛠️ Tech Stack

- Java 21,
- Spring Boot 3.5.5,
- Spring Security + JWT,
- Hibernate / JPA,
- PostgreSQL,
- Lombok,
- SpringDoc OpenAPI,
- JUnit 5 + MockMvc + Testcontainers,
- Maven,
- Docker.

---

## ⚙️ Getting Started

### Prerequisites

- Java 21,
- Maven 3.3+,
- PostgreSQL running locally or via Docker.

### Clone Repository

```bash
git clone https://github.com/TomHajek/movies-rating-svc
cd movies-rating-svc
```

### Configure Database

Edit `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://your-domain:5432/your-database
    username: your-username
    password: your-password
```

For Docker, edit also `docker-compose.yml`:

```yaml
# database container datasource
db:
  # ..
  environment:
    POSTGRES_DB: your-database
    POSTGRES_USER: your-postgres-user
    POSTGRES_PASSWORD: your-postgres-password

# application container datasource
app:
  # ..
  environment:
    SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/your-database
    SPRING_DATASOURCE_USERNAME: your-postgres-user
    SPRING_DATASOURCE_PASSWORD: your-postgres-password
```

### Run the Application

#### Locally

```bash
./mvnw spring-boot:run
```

#### In Docker

```bash
docker-compose build --no-cache && docker-compose up
```

Application will start on http://localhost:8080.

---

## 🔑 Authentication

This service uses JSON Web Token authentication.
- Users must register and login to receive a JWT token.
- The token must be included in the Authorization header for protected endpoints:

```http
Authorization: Bearer <your_token>
```

### Flow

1. Register a user → `POST /api/user/register`. 
2. Login with credentials → `POST /api/user/login`. 
3. Receive JWT token in the response. 
4. Use the token in subsequent requests.

---

## 📬 API Reference

Postman collection can be found in `src/main/resources/postman/movies-rating-api.postman_collection.json`.

---

## 📖 API Documentation

When the application is running, the interactive API docs are available via Swagger UI:
- URL: http://localhost:8080/swagger-ui.html.

---

## 📊 Monitoring

### Actuator

Spring Boot Actuator provides production-ready endpoints for monitoring and managing the application.

Available endpoints:
- Health Check → `GET /actuator/health`,
- Application Info → `GET /actuator/info`.
