# IronVault Auth Service

Microservice responsible for authentication and authorization in the IronVault ecosystem.
Issues JWT tokens consumed by other services such as `ironvault-payments`.

---

## Why a Dedicated Auth Service?

In a microservices architecture, centralizing identity management brings clear advantages:

- **Single Responsibility**: `ironvault-payments` handles payments. `ironvault-auth` handles identity. Each service evolves independently.
- **Reusability**: Any future service in the IronVault ecosystem can validate tokens issued here without duplicating auth logic.
- **Independent Deployability**: Auth can be scaled, updated, or replaced without touching payment flows.
- **Security Boundary**: Credentials, password hashing, and token secrets are isolated in one place.

---

## Architecture

This service follows **Hexagonal Architecture (Ports & Adapters)**, keeping the domain completely isolated from frameworks and infrastructure.

```
Dependency direction always flows inward:

[REST Controllers] → [Use Cases / Ports] → [Domain] ← [Adapters Out]
```

### Layer Breakdown

```
src/main/java/com/ironvault/auth/
├── adapter/
│   ├── in/
│   │   ├── web/          ← REST controllers and DTOs
│   │   ├── security/     ← JWT filter and Spring Security config
│   │   └── common/       ← Global exception handler
│   └── out/
│       └── persistence/  ← JPA entities, repositories and adapters
├── application/          ← Business logic (use cases implementation)
├── config/               ← Application configuration beans
├── domain/
│   ├── model/            ← User, Role (pure Java, no frameworks)
│   ├── port/
│   │   ├── in/           ← Inbound ports (use case interfaces)
│   │   └── out/          ← Outbound ports (repository interfaces)
│   └── exception/        ← Domain exceptions
└── utils/                ← JWT token provider
```

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Language |
| Spring Boot | 3.3.5 | Application framework |
| Spring Security | 6 | Authentication & authorization |
| Spring Data JPA | 3.3.5 | Database access |
| PostgreSQL | 15 | Production database |
| JJWT | 0.12.3 | JWT generation and validation |
| MapStruct | 1.5.5 | Object mapping |
| Lombok | Latest | Boilerplate reduction |
| H2 | Latest | In-memory database for tests |
| springdoc-openapi | 2.3.0 | API documentation |
| Docker | - | Container infrastructure |

---

## API Endpoints

### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@ironvault.com",
  "password": "SecurePass@123",
  "role": "MERCHANT"
}
```

**Response:** `201 Created`

---

### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@ironvault.com",
  "password": "SecurePass@123"
}
```

**Response:** `200 OK`
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "email": "user@ironvault.com",
  "role": "MERCHANT"
}
```

---

### Available Roles

| Role | Description |
|---|---|
| `ADMIN` | Full access to all services |
| `MERCHANT` | Can create and view own payments |
| `READONLY` | Read-only access |

---

## Security

- Passwords hashed with **BCrypt**
- Tokens signed with **HMAC-SHA256**
- Stateless session — no server-side session storage
- Public routes: `/api/auth/register`, `/api/auth/login`, `/swagger-ui/**`
- All other routes require a valid `Authorization: Bearer <token>` header

### JWT Token Structure

```
Header:  { "alg": "HS256" }
Payload: { "sub": "email", "role": "MERCHANT", "userId": "uuid", "iat": ..., "exp": ... }
```

---

## How It Integrates with ironvault-payments

`ironvault-auth` issues tokens. `ironvault-payments` validates them.

```
[Client] ──POST /api/auth/login──► [ironvault-auth] ──► issues JWT
[Client] ──POST /api/payments──► [ironvault-payments] ──► validates JWT signature
```

`ironvault-payments` is configured as a **Resource Server** — it only verifies the
token signature using the shared secret. It never manages users or credentials.

---

## Running Locally

### Prerequisites
- Java 21
- Maven 3.9+
- Docker

### 1. Start the database

```bash
cd infra/docker
docker-compose up -d
```

### 2. Configure environment variables

In IntelliJ → Run Configurations → Environment Variables:

```
JWT_SECRET=your-secret-key-at-least-32-chars
DB_URL=jdbc:postgresql://localhost:5491/ironvault_auth
DB_USERNAME=ironvault_auth
DB_PASSWORD=ironvault_auth
```

### 3. Start the application

```bash
cd backend
mvn spring-boot:run
```

The application starts on **port 8081**.
Database tables are created automatically on first run.

### 4. Access API documentation

```
http://localhost:8081/swagger-ui/index.html
```

---

## Environment Variables

| Variable | Required | Default | Description |
|---|---|---|---|
| `DB_URL` | Yes | - | PostgreSQL connection URL |
| `DB_USERNAME` | Yes | - | Database username |
| `DB_PASSWORD` | Yes | - | Database password |
| `JWT_SECRET` | Yes | - | HMAC signing secret (min 32 chars) |
| `JWT_EXPIRATION_MS` | No | `86400000` | Access token TTL (24h) |
| `JWT_REFRESH_EXPIRATION_MS` | No | `604800000` | Refresh token TTL (7d) |

---

## Database

| Field | Type | Description |
|---|---|---|
| `id` | UUID | Primary key |
| `email` | VARCHAR | Unique user identifier |
| `password` | VARCHAR | BCrypt hashed |
| `role` | VARCHAR | ADMIN, MERCHANT, READONLY |
| `active` | BOOLEAN | Account status |
| `created_at` | TIMESTAMP | Registration date |

---

## Roadmap

- [x] User registration with BCrypt password hashing
- [x] JWT login with enriched response
- [x] Role-based access control (ADMIN, MERCHANT, READONLY)
- [x] Global exception handling
- [x] Hexagonal architecture
- [ ] Refresh token endpoint
- [ ] Flyway database migrations
- [ ] Integration tests
- [ ] Rate limiting on auth endpoints
- [ ] Account lockout after failed attempts
- [ ] Token revocation (blacklist)
- [ ] Integration with ironvault-payments as Resource Server

---

## Project Structure Reference

```
ironvault-auth/
├── backend/          ← Spring Boot application
├── infra/
│   └── docker/       ← Docker Compose for local PostgreSQL
└── docs/             ← Architecture documentation
```

---

## Related Services

| Service | Repository | Description |
|---|---|---|
| ironvault-payments | github.com/viniciuspascucci1993/ironvault-payments | Instant payment processing |
| ironvault-auth | github.com/viniciuspascucci1993/ironvault-auth | Authentication & authorization |
