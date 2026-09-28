# Frameworksito

A minimal annotation-based REST framework for Java 21. Build APIs with zero external dependencies — uses only the Java standard library.

## Architecture

Frameworksito is a **multi-module Maven project**:

```
frameworksito/
├── pom.xml                          ← Parent POM (aggregator)
├── frameworksito-api/               ← The framework library (publish to Maven)
├── frameworksito-server/            ← HTTP server (wraps the API)
└── sample-app/                      ← Sample app demonstrating usage
```

| Module | Purpose |
|---|---|
| `frameworksito-api` | The framework itself — annotations, interfaces, router, JSON builder. Publish to your local Maven repo and import in your projects. |
| `frameworksito-server` | HTTP server implementation that boots the framework. Depends on `frameworksito-api`. |
| `sample-app` | A working sample app that bundles everything into a runnable JAR (shade). |

## Features

- **Annotation-based routing** — `@Get`, `@Post`, `@Put`, `@Delete`
- **`@RestController`** — returns JSON automatically with helper methods
- **`@Controller`** — legacy simple controller (raw string return)
- **Path parameters** — `{id}`, `{name}` style params extracted automatically
- **JSON builder** — zero-dependency reflection-based JSON serialization
- **`BaseController`** helpers — `success()`, `created()`, `deleted()`, `error()`
- **Auto-discovery** — scans packages for annotated classes
- **Zero dependencies** — only Java 21 standard library (`com.sun.net.httpserver`)

## Requirements

- **Java 21** (LTS) — `java --version`
- **Maven 3.9+** — `mvn --version`

## Building from Source

```bash
cd frameworksito
mvn clean install
```

This builds all three modules and installs them into your local Maven repository (`~/.m2/repository`).

### Artifacts produced

| Artifact | GAV |
|---|---|
| `frameworksito-api` | `com.frameworksito:frameworksito-api:1.0.0` |
| `frameworksito-server` | `com.frameworksito:frameworksito-server:1.0.0` |
| `sample-app` | `com.frameworksito:sample-app:1.0.0` |

## Creating Your Own Project

### Step 1 — Add dependencies

```xml
<dependencies>
    <dependency>
        <groupId>com.frameworksito</groupId>
        <artifactId>frameworksito-api</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>com.frameworksito</groupId>
        <artifactId>frameworksito-server</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Step 2 — Create a REST Controller

```java
package com.myapp.controllers;

import com.frameworksito.annotation.*;
import com.frameworksito.controller.BaseController;
import com.myapp.models.Product;

import java.util.Map;

@RestController("/api")
public class ProductController extends BaseController {

    @Get("/products")
    public String listProducts(String method, String path, Map<String, String> params) {
        return success(new Product("1", "Laptop", 999.99));
    }

    @Get("/products/{id}")
    public String getProduct(String method, String path, Map<String, String> params) {
        String id = params.get("id");
        return success(new Product(id, "Product-" + id, 49.99));
    }

    @Post("/products")
    public String createProduct(String method, String path, Map<String, String> params) {
        String name = params.get("name");
        double price = Double.parseDouble(params.getOrDefault("price", "0"));
        return created(new Product(String.valueOf(System.currentTimeMillis()), name, price));
    }

    @Put("/products/{id}")
    public String updateProduct(String method, String path, Map<String, String> params) {
        String id = params.get("id");
        String name = params.get("name");
        return success(new Product(id, name, 0));
    }

    @Delete("/products/{id}")
    public String deleteProduct(String method, String path, Map<String, String> params) {
        return deleted();
    }
}
```

### Step 3 — Create Your Models (DTOs)

```java
package com.myapp.models;

public class Product {
    private String id;
    private String name;
    private double price;

    public Product() {}

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
```

Any public fields or getter/setter pairs will be serialized to JSON automatically.

### Step 4 — Add Configuration

Create `src/main/resources/frameworksito.properties`:

```properties
scan.package=com.myapp.controllers
```

This tells the router which package to scan for `@Controller` and `@RestController` classes.

### Step 5 — Build and Package

```bash
mvn clean package
```

### Step 6 — Run

#### Option A — Using the sample-app (uber-jar)

```bash
java -jar sample-app/target/sample-app-1.0.0.jar
```

#### Option B — Using frameworksito-server with your JAR

```bash
java -cp "your-app.jar:$(mvn dependency:build-classpath -q -DincludeScope=runtime -Dmdep.outputFile=/dev/stdout)" com.frameworksito.server.FrameworksitoServer --port 8080 --package com.myapp.controllers
```

#### Option C — Maven exec plugin

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>exec-maven-plugin</artifactId>
    <version>3.1.1</version>
    <configuration>
        <mainClass>com.frameworksito.server.FrameworksitoServer</mainClass>
        <arguments>
            <argument>--port</argument>
            <argument>8080</argument>
            <argument>--package</argument>
            <argument>com.myapp.controllers</argument>
        </arguments>
    </configuration>
</plugin>
```

Then run: `mvn exec:java`

## REST Controller API

All `@RestController` classes extend `BaseController` which provides helper methods:

| Method | Returns | Example |
|---|---|---|
| `success(Object)` | `{"status":"ok",...}` | `return success(myObject);` |
| `created(Object)` | `{"status":"created","data":...}` | `return created(newUser);` |
| `deleted()` | `{"status":"deleted"}` | `return deleted();` |
| `error(String)` | `{"status":"error","message":"..."}` | `return error("not found");` |
| `successList(Object[])` | JSON array | `return successList(items);` |
| `errorList(Object[])` | JSON array | `return errorList(errors);` |

## Annotations Reference

### Route Annotations

| Annotation | HTTP Method | Example |
|---|---|---|
| `@Get("/path")` | GET | `@Get("/users/{id}")` |
| `@Post("/path")` | POST | `@Post("/users")` |
| `@Put("/path")` | PUT | `@Put("/users/{id}")` |
| `@Delete("/path")` | DELETE | `@Delete("/users/{id}")` |

### Class Annotations

| Annotation | Purpose | Example |
|---|---|---|
| `@Controller("/prefix")` | Regular controller | `@Controller("/api")` |
| `@RestController("/prefix")` | REST controller (JSON) | `@RestController("/api")` |

### Controller Method Signature

All handler methods must accept these three parameters:

```java
public String methodName(String method, String path, Map<String, String> params)
```

| Parameter | Description |
|---|---|
| `method` | HTTP method (GET, POST, PUT, DELETE) |
| `path` | Request path (e.g., `/api/users/42`) |
| `params` | Map of path parameters + query string parameters |

Path parameters like `{id}` are automatically extracted and placed in `params` with key `id`.

## Command Line Options

| Option | Description | Example |
|---|---|---|
| `--port <num>` | Server port (default: 8080) | `--port 9090` |
| `--package <pkg>` | Package to scan for controllers | `--package com.myapp.controllers` |

## Running the Sample App

```bash
cd frameworksito
mvn clean install

# Run sample app
java -jar sample-app/target/sample-app-1.0.0.jar --port 8080

# Test endpoints
curl http://localhost:8080/api/hello
curl http://localhost:8080/api/users
curl http://localhost:8080/api/users/1
curl -X POST "http://localhost:8080/api/users?name=Aníbal&email=ani@example.com"
curl -X PUT "http://localhost:8080/api/users/1?name=Updated"
curl -X DELETE "http://localhost:8080/api/users/1"
curl http://localhost:8080/api/health
```

## JSON Builder

Frameworksito includes a built-in JSON builder (`com.frameworksito.json.JsonBuilder`) that serializes Java objects to JSON using reflection:

- POJOs → JSON objects
- Arrays/lists → JSON arrays
- Strings → JSON strings (with escaping)
- Primitives → JSON primitives
- Null → `null`

No external dependency needed.

## Response Format

All endpoints return `application/json`. Examples:

```json
{"status":"ok","id":"1","name":"Aníbal","email":"ani@example.com"}
{"status":"created","data":{"id":"1","name":"New User"}}
{"status":"deleted"}
{"status":"error","message":"Not found"}
```

## License

MIT
