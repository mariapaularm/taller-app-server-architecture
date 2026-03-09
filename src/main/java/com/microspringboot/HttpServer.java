package com.microspringboot;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple HTTP Server that handles requests sequentially.
 * Supports serving static files and invoking controller methods.
 */
public class HttpServer {

    private final int port;
    private final IoCContainer iocContainer;
    private final StaticFileHandler staticFileHandler;
    private boolean running = false;

    public HttpServer(int port, IoCContainer iocContainer) {
        this.port = port;
        this.iocContainer = iocContainer;
        this.staticFileHandler = new StaticFileHandler();
    }

    /**
     * Starts the HTTP server and handles requests sequentially.
     */
    public void start() throws IOException {
        running = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            System.out.println("Access: http://localhost:" + port);
            
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    handleRequest(clientSocket);
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Error handling request: " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Handles a single HTTP request.
     */
    private void handleRequest(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream()
        ) {
            // Read request line
            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                return;
            }

            System.out.println("Request: " + requestLine);

            // Parse request
            HttpRequest request = parseRequest(requestLine);
            
            // Generate response
            HttpResponse response = processRequest(request);
            
            // Send response
            sendResponse(out, response);

        } catch (Exception e) {
            System.err.println("Error processing request: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    /**
     * Parses the HTTP request line.
     */
    private HttpRequest parseRequest(String requestLine) {
        String[] parts = requestLine.split(" ");
        String method = parts.length > 0 ? parts[0] : "GET";
        String fullPath = parts.length > 1 ? parts[1] : "/";
        
        // Separate path and query string
        String path;
        Map<String, String> queryParams = new HashMap<>();
        
        int queryIndex = fullPath.indexOf('?');
        if (queryIndex != -1) {
            path = fullPath.substring(0, queryIndex);
            String queryString = fullPath.substring(queryIndex + 1);
            queryParams = parseQueryString(queryString);
        } else {
            path = fullPath;
        }

        return new HttpRequest(method, path, queryParams);
    }

    /**
     * Parses query string into a map.
     */
    private Map<String, String> parseQueryString(String queryString) {
        Map<String, String> params = new HashMap<>();
        if (queryString == null || queryString.isEmpty()) {
            return params;
        }

        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int eqIndex = pair.indexOf('=');
            if (eqIndex != -1) {
                String key = pair.substring(0, eqIndex);
                String value = pair.substring(eqIndex + 1);
                // URL decode value
                value = decodeUrl(value);
                params.put(key, value);
            }
        }
        return params;
    }

    /**
     * Simple URL decoder for query parameters.
     */
    private String decodeUrl(String value) {
        try {
            return java.net.URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    /**
     * Processes the request and generates a response.
     */
    private HttpResponse processRequest(HttpRequest request) {
        String path = request.getPath();
        
        // Check if it's a controller route
        if (iocContainer.hasRoute(path)) {
            try {
                String body = iocContainer.invokeRoute(path, request.getQueryParams());
                return new HttpResponse(200, "OK", "text/html", body);
            } catch (Exception e) {
                return new HttpResponse(500, "Internal Server Error", "text/plain", 
                    "Error: " + e.getMessage());
            }
        }
        
        // Try to serve static file
        return staticFileHandler.serve(path);
    }

    /**
     * Sends the HTTP response to the client.
     */
    private void sendResponse(OutputStream out, HttpResponse response) throws IOException {
        PrintWriter writer = new PrintWriter(out, false);
        
        // Write status line
        writer.print("HTTP/1.1 " + response.getStatusCode() + " " + response.getStatusText() + "\r\n");
        
        // Write headers
        writer.print("Content-Type: " + response.getContentType() + "\r\n");
        
        byte[] bodyBytes = response.getBodyBytes();
        writer.print("Content-Length: " + bodyBytes.length + "\r\n");
        writer.print("Connection: close\r\n");
        writer.print("\r\n");
        writer.flush();
        
        // Write body
        out.write(bodyBytes);
        out.flush();
    }

    public void stop() {
        running = false;
    }
}
