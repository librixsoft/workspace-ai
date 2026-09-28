package com.frameworksito.controller;

import java.util.Map;

public interface Controller {
    String handle(String method, String path, Map<String, String> params);
    int statusCode();
}
