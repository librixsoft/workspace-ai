package com.frameworksito.json;

public final class JsonResponse {

    private JsonResponse() {}

    public static String ok(Object data) {
        return "{\"status\":\"ok\"," + JsonBuilder.toJson(data).substring(1) ;
    }

    public static String ok() {
        return "{\"status\":\"ok\"}";
    }

    public static String error(String message) {
        return "{\"status\":\"error\",\"message\":\"" + JsonBuilder.escapeString(message) + "\"}";
    }

    public static String notFound() {
        return "{\"status\":\"error\",\"message\":\"Not found\"}";
    }

    public static String created(Object data) {
        return "{\"status\":\"created\",\"data\":" + JsonBuilder.toJson(data) + "}";
    }

    public static String deleted() {
        return "{\"status\":\"deleted\"}";
    }
}
