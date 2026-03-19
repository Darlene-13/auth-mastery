# Auth Mastery

A hands-on authentication engineering project built across three transport layers and three technology stacks. The goal is to understand authentication deeply by implementing every major mechanism from scratch, not just using it.

## What This Project Covers

Authentication is one of the most critical and most misunderstood parts of software engineering. Most developers use authentication libraries without understanding what is happening underneath. This project fixes that.

Each authentication type is implemented as a standalone, runnable application. The order is intentional. Each module builds on the concepts introduced by the previous one, so the learning is cumulative.

| Module | Type | Status |
|---|---|---|
| 01 | Basic Auth | Complete |
| 02 | Session Auth | In Progress |
| 03 | JWT | Pending |
| 04 | OAuth 2.0 / OpenID Connect | Pending |
| 05 | API Key Auth | Pending |
| 06 | MFA / TOTP | Pending |

## Technology Stacks

Each module is implemented in all three stacks so you can see how the same concept is expressed differently across ecosystems.

- Java Spring Boot (Spring Security)
- Python FastAPI
- Python Django with Django REST Framework

## Transport Layers

Each auth type is further implemented across three transport layers.

- REST (implemented first, foundational)
- GraphQL (second, same concepts, different transport)
- gRPC (third, metadata-based auth)

## Project Structure

```
auth-mastery/
├── 01-basic-auth/
│   ├── spring-boot/
│   ├── fastapi/
│   └── django/
├── 02-session-auth/
├── 03-jwt-auth/
├── 04-oauth2/
├── 05-api-key-auth/
└── 06-mfa/
```

---

## Module 01: Basic Auth

### What is Basic Auth

Basic Auth is the simplest form of HTTP authentication. The client combines a username and password with a colon separator, Base64 encodes the result, and attaches it to every request inside the Authorization header.

```
Authorization: Basic ZGFybGVuZTpwYXNzd29yZDEyMw==
```

The server decodes the header on every request, looks up the user, and compares the incoming password against the stored hash. There is no session, no token, and no concept of logging in or logging out. Credentials travel with every single request.

### Why Base64 is Not Encryption

Base64 is encoding, not encryption. Anyone who intercepts the request can decode it instantly with no key and no cracking. This is why Basic Auth must always be used over HTTPS. HTTPS provides the transport layer protection that makes Basic Auth viable in practice.

### Implementation: Spring Boot

**Stack:** Java 17, Spring Boot 3.4.3, Spring Security, JPA, H2, Lombok

**Endpoints:**

| Method | Path | Access | Description |
|---|---|---|---|
| POST | /api/auth/register | Public | Register a new user |
| GET | /api/public | Public | Public endpoint, no auth required |
| GET | /api/hello | Protected | Returns greeting for authenticated user |

**Key files:**

```
src/main/java/io/github/darlene/basicauth/
├── config/
│   └── SecurityConfig.java          -- Spring Security configuration
├── controller/
│   └── HelloController.java         -- REST endpoints
├── dto/
│   └── RegisterRequest.java         -- Registration request body
├── model/
│   └── User.java                    -- JPA entity
├── repository/
│   └── UserRepository.java          -- Database access
└── service/
    ├── CustomUserDetailsService.java -- Spring Security user loading
    └── UserService.java             -- Registration business logic
```

**How it works:**

1. A new user registers via POST /api/auth/register with a JSON body containing username and password.
2. The password is BCrypt hashed before being saved to the database. The plain text password is never stored.
3. On subsequent requests to protected endpoints, the client attaches the Authorization header with Base64 encoded credentials.
4. Spring Security intercepts the request, decodes the header, calls CustomUserDetailsService to load the user, and uses BCryptPasswordEncoder to compare passwords.
5. If credentials match, the request proceeds to the controller. If not, a 401 Unauthorized response is returned.

**Running the application:**

```bash
cd 01-basic-auth/spring-boot
./mvnw spring-boot:run
```

The app starts on port 8080. The H2 in-memory database is created fresh on every startup and dropped on shutdown.

**Testing with Postman:**

Register a user:
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "darlene",
  "password": "password123"
}
```

Access protected endpoint:
```
GET http://localhost:8080/api/hello
Authorization: Basic Auth (enter username and password in Postman Authorization tab)
```

**Strengths:**

- Simple to implement and understand
- Stateless, no server-side memory required per user
- Works with any HTTP client out of the box

**Weaknesses:**

- Credentials are sent with every request
- No logout mechanism, the server cannot invalidate credentials
- No token expiry, credentials are valid until the password changes
- Vulnerable if used without HTTPS

These weaknesses are what motivated the invention of Session Auth, which is covered in module 02.

---

## Prerequisites

- Java 17 or higher
- Maven (or use the included mvnw wrapper)
- Python 3.11 or higher (for FastAPI and Django modules)
- Postman or any HTTP client for testing

## Author

Darlene Wendie