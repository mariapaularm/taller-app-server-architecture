package com.microspringboot;

import java.io.IOException;
import java.io.InputStream;

/**
 * Handles serving static files (HTML, PNG, CSS, JS) from the resources folder.
 */
public class StaticFileHandler {

    private static final String STATIC_PATH = "/static";

    /**
     * Serves a static file from the resources/static folder.
     */
    public HttpResponse serve(String path) {
        // Default to index.html for root path
        if (path.equals("/")) {
            path = "/index.html";
        }

        // Build resource path
        String resourcePath = STATIC_PATH + path;
        
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                return notFound(path);
            }

            byte[] content = is.readAllBytes();
            String contentType = getContentType(path);
            return new HttpResponse(200, "OK", contentType, content);

        } catch (IOException e) {
            return serverError(e.getMessage());
        }
    }

    /**
     * Determines the content type based on file extension.
     */
    private String getContentType(String path) {
        String lowerPath = path.toLowerCase();
        
        if (lowerPath.endsWith(".html") || lowerPath.endsWith(".htm")) {
            return "text/html; charset=UTF-8";
        } else if (lowerPath.endsWith(".css")) {
            return "text/css; charset=UTF-8";
        } else if (lowerPath.endsWith(".js")) {
            return "application/javascript; charset=UTF-8";
        } else if (lowerPath.endsWith(".json")) {
            return "application/json; charset=UTF-8";
        } else if (lowerPath.endsWith(".png")) {
            return "image/png";
        } else if (lowerPath.endsWith(".jpg") || lowerPath.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerPath.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerPath.endsWith(".ico")) {
            return "image/x-icon";
        } else if (lowerPath.endsWith(".svg")) {
            return "image/svg+xml";
        } else {
            return "application/octet-stream";
        }
    }

    /**
     * Returns a 404 Not Found response.
     */
    private HttpResponse notFound(String path) {
        String body = """
            <!DOCTYPE html>
            <html>
            <head><title>404 Not Found</title></head>
            <body>
                <h1>404 Not Found</h1>
                <p>The requested resource was not found: %s</p>
            </body>
            </html>
            """.formatted(path);
        return new HttpResponse(404, "Not Found", "text/html; charset=UTF-8", body);
    }

    /**
     * Returns a 500 Internal Server Error response.
     */
    private HttpResponse serverError(String message) {
        String body = """
            <!DOCTYPE html>
            <html>
            <head><title>500 Internal Server Error</title></head>
            <body>
                <h1>500 Internal Server Error</h1>
                <p>%s</p>
            </body>
            </html>
            """.formatted(message);
        return new HttpResponse(500, "Internal Server Error", "text/html; charset=UTF-8", body);
    }
}
