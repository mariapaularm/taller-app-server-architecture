package com.microspringboot;

import java.util.List;

/**
 * MicroSpringBoot - A minimal IoC framework similar to Spring Boot.
 * 
 * Features:
 * - Classpath scanning for @RestController classes
 * - Route mapping via @GetMapping annotations
 * - Query parameter support via @RequestParam
 * - Static file serving (HTML, PNG)
 * - Simple HTTP server (sequential request handling)
 * 
 * Usage:
 *   java -cp target/classes com.microspringboot.MicroSpringBoot [ControllerClass]
 * 
 * If no controller class is specified, scans com.microspringboot package automatically.
 */
public class MicroSpringBoot {

    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_PACKAGE = "com.microspringboot";

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   MicroSpringBoot - Mini IoC Framework");
        System.out.println("===========================================");
        
        try {
            IoCContainer container = new IoCContainer();
            ClasspathScanner scanner = new ClasspathScanner();

            if (args.length > 0) {
                // Manual mode: Load specific controller from command line
                System.out.println("\nMode: Manual controller loading");
                String className = args[0];
                System.out.println("Loading controller: " + className);
                
                Class<?> controllerClass = scanner.loadController(className);
                container.registerController(controllerClass);
            } else {
                // Automatic mode: Scan classpath for controllers
                System.out.println("\nMode: Automatic classpath scanning");
                System.out.println("Scanning package: " + DEFAULT_PACKAGE);
                
                List<Class<?>> controllers = scanner.scanForControllers(DEFAULT_PACKAGE);
                
                if (controllers.isEmpty()) {
                    System.out.println("No controllers found!");
                } else {
                    for (Class<?> controller : controllers) {
                        container.registerController(controller);
                    }
                }
            }

            System.out.println("\nRegistered routes:");
            container.getRouteMappings().forEach((path, method) -> 
                System.out.println("  " + path + " -> " + method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()")
            );

            // Start HTTP server
            System.out.println("\nStarting HTTP server...");
            HttpServer server = new HttpServer(DEFAULT_PORT, container);
            server.start();

        } catch (ClassNotFoundException e) {
            System.err.println("Error: Controller class not found: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}