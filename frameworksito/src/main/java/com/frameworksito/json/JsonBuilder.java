package com.frameworksito.json;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public final class JsonBuilder {

    private JsonBuilder() {}

    public static String toJson(Object obj) {
        if (obj == null) {
            return "null";
        }
        Class<?> clazz = obj.getClass();
        if (clazz == String.class) {
            return "\"" + escapeString((String) obj) + "\"";
        }
        if (clazz == int.class || clazz == long.class || clazz == double.class || clazz == float.class || clazz == short.class || clazz == byte.class) {
            return obj.toString();
        }
        if (clazz == boolean.class) {
            return obj.toString();
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            return toJsonCollection((Collection<?>) obj);
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return toJsonMap((Map<?, ?>) obj);
        }
        if (clazz.isArray()) {
            return toJsonArray(obj);
        }
        return toJsonObject(obj);
    }

    private static String toJsonObject(Object obj) {
        var sb = new StringBuilder();
        sb.append("{");
        Class<?> clazz = obj.getClass();
        var fields = getAllFields(clazz);
        var entries = new java.util.ArrayList<String>();
        for (var f : fields) {
            f.setAccessible(true);
            try {
                var val = f.get(obj);
                var key = f.getName();
                var jsonKey = "\"" + escapeString(key) + "\"";
                entries.add(jsonKey + ":" + toJson(val));
            } catch (IllegalAccessException ignored) {
            }
        }
        for (int i = 0; i < entries.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(entries.get(i));
        }
        sb.append("}");
        return sb.toString();
    }

    private static java.util.List<Field> getAllFields(Class<?> clazz) {
        var fields = new java.util.ArrayList<Field>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            for (var f : current.getDeclaredFields()) {
                fields.add(f);
            }
            current = current.getSuperclass();
        }
        return fields;
    }

    private static String toJsonCollection(Collection<?> collection) {
        var sb = new StringBuilder("[");
        var arr = collection.toArray();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(arr[i]));
        }
        sb.append("]");
        return sb.toString();
    }

    private static String toJsonMap(Map<?, ?> map) {
        var sb = new StringBuilder("{");
        var entries = new java.util.ArrayList<String>();
        for (var entry : map.entrySet()) {
            var key = entry.getKey() != null ? entry.getKey().toString() : "null";
            var jsonKey = "\"" + escapeString(key) + "\"";
            entries.add(jsonKey + ":" + toJson(entry.getValue()));
        }
        for (int i = 0; i < entries.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(entries.get(i));
        }
        sb.append("}");
        return sb.toString();
    }

    private static String toJsonArray(Object arr) {
        var sb = new StringBuilder("[");
        var len = java.lang.reflect.Array.getLength(arr);
        for (int i = 0; i < len; i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(java.lang.reflect.Array.get(arr, i)));
        }
        sb.append("]");
        return sb.toString();
    }

    static String escapeString(String s) {
        var sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            var c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }
}
