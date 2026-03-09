package com.microspringboot;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Minimal IoC Container that manages controller instances and route mappings.
 * Uses reflection to load and invoke controller methods.
 */
public class IoCContainer {

    // Maps URI path -> Method
    private final Map<String, Method> routeMappings = new HashMap<>();
    
    // Maps URI path -> Controller instance
    private final Map<String, Object> controllerInstances = new HashMap<>();

    /**
     * Registers a controller class.
     * Scans for @GetMapping annotations and registers routes.
     */
    public void registerController(Class<?> controllerClass) throws Exception {
        if (!controllerClass.isAnnotationPresent(RestController.class)) {
            throw new IllegalArgumentException("Class " + controllerClass.getName() + " is not annotated with @RestController");
        }

        // Create instance using default constructor (IoC)
        Object instance = controllerClass.getDeclaredConstructor().newInstance();

        // Scan methods for @GetMapping
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping mapping = method.getAnnotation(GetMapping.class);
                String path = mapping.value();
                routeMappings.put(path, method);
                controllerInstances.put(path, instance);
                System.out.println("Registered route: " + path + " -> " + controllerClass.getSimpleName() + "." + method.getName() + "()");
            }
        }
    }

    /**
     * Checks if a route exists for the given path.
     */
    public boolean hasRoute(String path) {
        return routeMappings.containsKey(path);
    }

    /**
     * Invokes the controller method for the given path with query parameters.
     */
    public String invokeRoute(String path, Map<String, String> queryParams) throws Exception {
        Method method = routeMappings.get(path);
        Object controller = controllerInstances.get(path);

        if (method == null || controller == null) {
            return null;
        }

        // Build arguments array based on method parameters
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            if (param.isAnnotationPresent(RequestParam.class)) {
                RequestParam annotation = param.getAnnotation(RequestParam.class);
                String paramName = annotation.value();
                String defaultValue = annotation.defaultValue();
                
                // Get value from query params or use default
                String value = queryParams.getOrDefault(paramName, defaultValue);
                args[i] = value;
            } else {
                args[i] = null;
            }
        }

        // Invoke method with arguments
        Object result = method.invoke(controller, args);
        return result != null ? result.toString() : "";
    }

    /**
     * Gets all registered routes.
     */
    public Map<String, Method> getRouteMappings() {
        return new HashMap<>(routeMappings);
    }
}
