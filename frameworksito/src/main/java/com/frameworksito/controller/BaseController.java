package com.frameworksito.controller;

import com.frameworksito.json.JsonBuilder;
import com.frameworksito.json.JsonResponse;

import java.util.Map;

public abstract class BaseController {

    protected String success(Object data) {
        return JsonResponse.ok(data);
    }

    protected String created(Object data) {
        return JsonResponse.created(data);
    }

    protected String deleted() {
        return JsonResponse.deleted();
    }

    protected String error(String message) {
        return JsonResponse.error(message);
    }

    protected String successList(Object[] items) {
        return JsonBuilder.toJson(items);
    }

    protected String errorList(Object[] items) {
        var sb = new StringBuilder("[");
        for (int i = 0; i < items.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(JsonBuilder.toJson(items[i]));
        }
        return sb.append("]").toString();
    }
}
