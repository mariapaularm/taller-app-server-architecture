package com.microspringboot;

import java.nio.charset.StandardCharsets;

/**
 * Represents an HTTP response with status, content type, and body.
 */
public class HttpResponse {

    private final int statusCode;
    private final String statusText;
    private final String contentType;
    private final byte[] bodyBytes;

    public HttpResponse(int statusCode, String statusText, String contentType, String body) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.contentType = contentType;
        this.bodyBytes = body != null ? body.getBytes(StandardCharsets.UTF_8) : new byte[0];
    }

    public HttpResponse(int statusCode, String statusText, String contentType, byte[] bodyBytes) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.contentType = contentType;
        this.bodyBytes = bodyBytes != null ? bodyBytes : new byte[0];
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getBodyBytes() {
        return bodyBytes;
    }

    public String getBody() {
        return new String(bodyBytes, StandardCharsets.UTF_8);
    }
}
