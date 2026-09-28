package com.controllers;

import com.frameworksito.annotation.*;
import com.frameworksito.controller.BaseController;
import com.models.HealthStatus;
import com.models.Message;
import com.models.User;

import java.util.Map;

@RestController("/api")
public class RestSampleController extends BaseController {

    @Get("/hello")
    public String hello(String method, String path, Map<String, String> params) {
        return success(new Message("Hello from Frameworksito!"));
    }

    @Get("/users")
    public String listUsers(String method, String path, Map<String, String> params) {
        var users = new Object[]{
            new User("1", "Aníbal", "anibal@example.com"),
            new User("2", "Ana", "ana@example.com"),
            new User("3", "Luis", "luis@example.com")
        };
        return success(users);
    }

    @Get("/users/{id}")
    public String getUser(String method, String path, Map<String, String> params) {
        var id = params.get("id");
        if (id == null) {
            return error("id parameter required");
        }
        return success(new User(id, "Test", "test@example.com"));
    }

    @Post("/users")
    public String createUser(String method, String path, Map<String, String> params) {
        var name = params.getOrDefault("name", "Anonymous");
        var email = params.getOrDefault("email", "");
        var newUser = new User(String.valueOf(System.currentTimeMillis()), name, email);
        return created(newUser);
    }

    @Put("/users/{id}")
    public String updateUser(String method, String path, Map<String, String> params) {
        var id = params.get("id");
        var name = params.get("name");
        if (id == null) {
            return error("id parameter required");
        }
        var updated = new User(id, name != null ? name : "Updated", "updated@example.com");
        return success(updated);
    }

    @Delete("/users/{id}")
    public String deleteUser(String method, String path, Map<String, String> params) {
        var id = params.get("id");
        if (id == null) {
            return error("id parameter required");
        }
        return deleted();
    }

    @Get("/health")
    public String health(String method, String path, Map<String, String> params) {
        return success(new HealthStatus("Frameworksito", "1.0.0", "healthy"));
    }
}
