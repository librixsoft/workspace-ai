package com.frameworksito.server;

import com.frameworksito.core.Router;
import com.frameworksito.json.JsonBuilder;
import com.frameworksito.json.JsonResponse;
import com.frameworksito.util.ASCIIArt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class FrameworksitoServer {

    private static final int DEFAULT_PORT = 8080;
    private com.sun.net.httpserver.HttpServer server;

    public static void main(String[] args) {
        ASCIIArt.printLogo();

        int port = DEFAULT_PORT;
        String packageName = "com.controllers";

        for (int i = 0; i < args.length; i++) {
            if ("--port".equals(args[i]) && i + 1 < args.length) {
                try {
                    port = Integer.parseInt(args[++i]);
                } catch (NumberFormatException ignored) {
                }
            }
            if ("--package".equals(args[i]) && i + 1 < args.length) {
                packageName = args[++i];
            }
        }

        var app = new FrameworksitoServer(port, packageName);
        app.start();

        System.out.println("Frameworksito running on port " + port);
        System.out.println("Scanned package: " + packageName);
        System.out.println("Press Ctrl+C to stop.");
    }

    public FrameworksitoServer(int port, String packageName) {
        try {
            this.server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
            this.packageName = packageName;
            this.port = port;
        } catch (IOException e) {
            throw new RuntimeException("Cannot start server on port " + port, e);
        }
    }

    private final int port;
    private final String packageName;

    public void start() {
        var router = buildRouter();
        server.createContext("/", exchange -> handleRequest(exchange, router));
        server.start();
    }

    private Router buildRouter() {
        var router = new Router();
        router.scanPackage(packageName);
        return router;
    }

    private void handleRequest(com.sun.net.httpserver.HttpExchange exchange, Router router) {
        var requestPath = exchange.getRequestURI().getPath();
        var method = exchange.getRequestMethod();
        var queryString = exchange.getRequestURI().getQuery();
        var params = parseQueryParams(queryString);

        var body = router.dispatch(method, requestPath, params);

        if (body == null) {
            body = JsonResponse.notFound();
            sendResponse(exchange, 404, body, "application/json");
            return;
        }

        sendResponse(exchange, 200, body, "application/json");
    }

    private Map<String, String> parseQueryParams(String queryString) {
        var params = new LinkedHashMap<String, String>();
        if (queryString == null || queryString.isEmpty()) {
            return params;
        }
        for (var pair : queryString.split("&")) {
            var idx = pair.indexOf('=');
            if (idx > 0) {
                var key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                var value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                params.put(key, value);
            }
        }
        return params;
    }

    private void sendResponse(com.sun.net.httpserver.HttpExchange exchange, int status, String body, String contentType) {
        try (var os = exchange.getResponseBody()) {
            var response = body.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(status, response.length);
            os.write(response);
        } catch (IOException ignored) {
        }
    }
}
