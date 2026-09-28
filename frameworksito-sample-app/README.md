# Sample App — Frameworksito

A standalone sample application demonstrating how to use the Frameworksito framework.

## Structure

```
sample-app/
├── pom.xml                          ← Depends on frameworksito library
└── src/main/java/com/
    ├── controllers/                 ← Your REST controllers
    │   └── RestSampleController.java
    └── models/                      ← Your DTOs
        ├── User.java
        ├── Message.java
        └── HealthStatus.java
```

## Build & Run

```bash
# Make sure frameworksito is installed first:
# cd ../frameworksito && mvn clean install

mvn clean package
java -jar target/sample-app-1.0.0.jar --port 8080
```

## Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/hello` | Health check |
| GET | `/api/users` | List all users |
| GET | `/api/users/{id}` | Get user by ID |
| POST | `/api/users` | Create user |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |
| GET | `/api/health` | Framework health |

## Testing

```bash
curl http://localhost:8080/api/hello
curl http://localhost:8080/api/users
curl http://localhost:8080/api/users/1
curl -X POST "http://localhost:8080/api/users?name=Aníbal&email=ani@example.com"
curl -X PUT "http://localhost:8080/api/users/1?name=Updated"
curl -X DELETE "http://localhost:8080/api/users/1"
curl http://localhost:8080/api/health
```

## Using Your Own Controllers

1. Create your controllers in `com.controllers` (or change `scan.package` in `frameworksito.properties`)
2. Extend `BaseController`
3. Use `@RestController("/api")` annotation
4. Use `success()`, `created()`, `deleted()`, `error()` helpers
5. Build and run as shown above

## Requirements

- Java 21
- Maven 3.9+
