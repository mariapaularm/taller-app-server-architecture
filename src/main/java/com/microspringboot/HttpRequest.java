package com.microspringboot;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents an HTTP request with method, path, and query parameters.
 */
public class HttpRequest {

    private final String method;
    private final String path;
    private final Map<String, String> queryParams;

    public HttpRequest(String method, String path, Map<String, String> queryParams) {
        this.method = method;
        this.path = path;
        this.queryParams = queryParams != null ? queryParams : new HashMap<>();
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public String getQueryParam(String name) {
        return queryParams.get(name);
    }

    public String getQueryParam(String name, String defaultValue) {
        return queryParams.getOrDefault(name, defaultValue);
    }

    @Override
    public String toString() {
        return method + " " + path + (queryParams.isEmpty() ? "" : "?" + queryParams);
    }
}
