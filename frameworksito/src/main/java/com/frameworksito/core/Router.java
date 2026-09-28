package com.frameworksito.core;

import com.frameworksito.annotation.Controller;
import com.frameworksito.annotation.Delete;
import com.frameworksito.annotation.Get;
import com.frameworksito.annotation.Post;
import com.frameworksito.annotation.Put;
import com.frameworksito.annotation.RestController;
import com.frameworksito.controller.BaseController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router {

    private final List<RouteEntry> routes = new ArrayList<>();
    private final List<RestRouteEntry> restRoutes = new ArrayList<>();

    public void scanPackage(String packageName) {
        try {
            var classpath = ClassLoader.getSystemResource(packageName.replace('.', '/'));
            if (classpath == null) {
                return;
            }
            var dir = new java.io.File(classpath.toURI());
            if (!dir.isDirectory()) {
                return;
            }
            scanDirectory(dir, packageName);
        } catch (Exception ignored) {
        }
    }

    private void scanDirectory(java.io.File dir, String packageName) {
        var files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (var file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                var className = packageName + "." + file.getName().replace(".class", "");
                try {
                    var clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(Controller.class)) {
                        registerController(clazz);
                    } else if (clazz.isAnnotationPresent(RestController.class)) {
                        registerRestController(clazz);
                    }
                } catch (ClassNotFoundException ignored) {
                }
            }
        }
    }

    private void registerController(Class<?> clazz) {
        var base = "";
        if (clazz.isAnnotationPresent(Controller.class)) {
            var ctrl = clazz.getAnnotation(Controller.class);
            base = ctrl.value();
        }
        var instance = createInstance(clazz);
        var mds = clazz.getDeclaredMethods();
        for (var m : mds) {
            registerMethod(m, instance, base);
        }
    }

    private void registerRestController(Class<?> clazz) {
        var base = "";
        if (clazz.isAnnotationPresent(RestController.class)) {
            var ctrl = clazz.getAnnotation(RestController.class);
            base = ctrl.value();
        }
        var instance = createInstance(clazz);
        var mds = clazz.getDeclaredMethods();
        for (var m : mds) {
            registerRestMethod(m, instance, base);
        }
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate controller: " + clazz.getName(), e);
        }
    }

    private void registerMethod(Method m, Object instance, String base) {
        if (m.isAnnotationPresent(Get.class)) {
            var path = base + m.getAnnotation(Get.class).value();
            routes.add(new RouteEntry("GET", path, instance, m));
        }
        if (m.isAnnotationPresent(Post.class)) {
            var path = base + m.getAnnotation(Post.class).value();
            routes.add(new RouteEntry("POST", path, instance, m));
        }
        if (m.isAnnotationPresent(Put.class)) {
            var path = base + m.getAnnotation(Put.class).value();
            routes.add(new RouteEntry("PUT", path, instance, m));
        }
        if (m.isAnnotationPresent(Delete.class)) {
            var path = base + m.getAnnotation(Delete.class).value();
            routes.add(new RouteEntry("DELETE", path, instance, m));
        }
    }

    private void registerRestMethod(Method m, Object instance, String base) {
        var restCtrl = instance instanceof BaseController;
        if (m.isAnnotationPresent(Get.class)) {
            var path = base + m.getAnnotation(Get.class).value();
            restRoutes.add(new RestRouteEntry("GET", path, instance, m));
        }
        if (m.isAnnotationPresent(Post.class)) {
            var path = base + m.getAnnotation(Post.class).value();
            restRoutes.add(new RestRouteEntry("POST", path, instance, m));
        }
        if (m.isAnnotationPresent(Put.class)) {
            var path = base + m.getAnnotation(Put.class).value();
            restRoutes.add(new RestRouteEntry("PUT", path, instance, m));
        }
        if (m.isAnnotationPresent(Delete.class)) {
            var path = base + m.getAnnotation(Delete.class).value();
            restRoutes.add(new RestRouteEntry("DELETE", path, instance, m));
        }
    }

    public String dispatch(String httpMethod, String requestPath, Map<String, String> queryParams) {
        for (var route : routes) {
            if (!route.httpMethod.equals(httpMethod)) {
                continue;
            }
            var params = new LinkedHashMap<String, String>();
            if (matchPath(route.routePath, requestPath, params)) {
                try {
                    var result = route.routeMethod.invoke(route.instance, httpMethod, requestPath, queryParams);
                    return result != null ? result.toString() : "";
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return "{\"status\":\"error\",\"message\":\"Internal server error\"}";
                }
            }
        }

        for (var route : restRoutes) {
            if (!route.httpMethod.equals(httpMethod)) {
                continue;
            }
            var params = new LinkedHashMap<String, String>();
            if (matchPath(route.routePath, requestPath, params)) {
                try {
                    var result = route.routeMethod.invoke(route.instance, httpMethod, requestPath, queryParams);
                    return result != null ? result.toString() : "{\"status\":\"ok\"}";
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return "{\"status\":\"error\",\"message\":\"Internal server error\"}";
                }
            }
        }

        return null;
    }

    public boolean matchPath(String template, String path, Map<String, String> params) {
        var regex = template.replaceAll("\\{[^}]+\\}", "([^/]+)");
        regex = "^" + regex + "$";
        var matcher = Pattern.compile(regex).matcher(path);
        if (!matcher.matches()) {
            return false;
        }
        var names = extractParamNames(template);
        for (int i = 0; i < names.size(); i++) {
            params.put(names.get(i), matcher.group(i + 1));
        }
        return true;
    }

    private List<String> extractParamNames(String template) {
        var names = new ArrayList<String>();
        var pattern = Pattern.compile("\\{([^}]+)\\}");
        var matcher = pattern.matcher(template);
        while (matcher.find()) {
            names.add(matcher.group(1));
        }
        return names;
    }

    private static class RouteEntry {
        final String httpMethod;
        final String routePath;
        final Object instance;
        final Method routeMethod;

        RouteEntry(String httpMethod, String path, Object instance, Method routeMethod) {
            this.httpMethod = httpMethod;
            this.routePath = path;
            this.instance = instance;
            this.routeMethod = routeMethod;
        }
    }

    private static class RestRouteEntry {
        final String httpMethod;
        final String routePath;
        final Object instance;
        final Method routeMethod;

        RestRouteEntry(String httpMethod, String path, Object instance, Method routeMethod) {
            this.httpMethod = httpMethod;
            this.routePath = path;
            this.instance = instance;
            this.routeMethod = routeMethod;
        }
    }
}
