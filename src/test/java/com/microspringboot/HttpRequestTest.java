package com.microspringboot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit tests for HttpRequest.
 */
class HttpRequestTest {

    @Test
    @DisplayName("Should create request with method, path, and params")
    void testCreateRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "test");
        
        HttpRequest request = new HttpRequest("GET", "/greeting", params);
        
        assertEquals("GET", request.getMethod());
        assertEquals("/greeting", request.getPath());
        assertEquals("test", request.getQueryParam("name"));
    }

    @Test
    @DisplayName("Should return default value for missing param")
    void testGetQueryParamWithDefault() {
        HttpRequest request = new HttpRequest("GET", "/test", new HashMap<>());
        
        String result = request.getQueryParam("missing", "default");
        
        assertEquals("default", result);
    }

    @Test
    @DisplayName("Should return null for missing param without default")
    void testGetQueryParamNull() {
        HttpRequest request = new HttpRequest("GET", "/test", new HashMap<>());
        
        String result = request.getQueryParam("missing");
        
        assertNull(result);
    }

    @Test
    @DisplayName("Should handle null params map")
    void testNullParams() {
        HttpRequest request = new HttpRequest("GET", "/test", null);
        
        assertTrue(request.getQueryParams().isEmpty());
    }
}
