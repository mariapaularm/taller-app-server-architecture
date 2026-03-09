package com.microspringboot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HttpResponse.
 */
class HttpResponseTest {

    @Test
    @DisplayName("Should create response with string body")
    void testCreateResponseWithStringBody() {
        HttpResponse response = new HttpResponse(200, "OK", "text/html", "Hello");
        
        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getStatusText());
        assertEquals("text/html", response.getContentType());
        assertEquals("Hello", response.getBody());
    }

    @Test
    @DisplayName("Should create response with byte array body")
    void testCreateResponseWithByteBody() {
        byte[] body = {72, 101, 108, 108, 111}; // "Hello"
        HttpResponse response = new HttpResponse(200, "OK", "text/plain", body);
        
        assertEquals("Hello", response.getBody());
        assertArrayEquals(body, response.getBodyBytes());
    }

    @Test
    @DisplayName("Should handle null string body")
    void testNullStringBody() {
        HttpResponse response = new HttpResponse(404, "Not Found", "text/html", (String) null);
        
        assertEquals(0, response.getBodyBytes().length);
    }

    @Test
    @DisplayName("Should handle null byte array body")
    void testNullByteBody() {
        HttpResponse response = new HttpResponse(500, "Error", "text/plain", (byte[]) null);
        
        assertEquals(0, response.getBodyBytes().length);
    }

    @Test
    @DisplayName("Should calculate correct content length")
    void testContentLength() {
        HttpResponse response = new HttpResponse(200, "OK", "text/html", "Test123");
        
        assertEquals(7, response.getBodyBytes().length);
    }
}
