package com.microspringboot;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Scans the classpath to find classes annotated with @RestController.
 * Uses reflection to dynamically discover and load controllers.
 */
public class ClasspathScanner {

    /**
     * Scans the specified package for classes annotated with @RestController.
     */
    public List<Class<?>> scanForControllers(String basePackage) {
        List<Class<?>> controllers = new ArrayList<>();
        
        try {
            String path = basePackage.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL resource = classLoader.getResource(path);
            
            if (resource == null) {
                System.err.println("Package not found: " + basePackage);
                return controllers;
            }

            File directory = new File(resource.toURI());
            if (directory.exists()) {
                scanDirectory(directory, basePackage, controllers);
            }
        } catch (Exception e) {
            System.err.println("Error scanning classpath: " + e.getMessage());
            e.printStackTrace();
        }

        return controllers;
    }

    /**
     * Recursively scans a directory for .class files.
     */
    private void scanDirectory(File directory, String packageName, List<Class<?>> controllers) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // Recurse into subdirectory
                String subPackage = packageName + "." + file.getName();
                scanDirectory(file, subPackage, controllers);
            } else if (file.getName().endsWith(".class")) {
                // Load class and check for @RestController
                String className = packageName + "." + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(RestController.class)) {
                        controllers.add(clazz);
                        System.out.println("Found controller: " + className);
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("Could not load class: " + className);
                } catch (NoClassDefFoundError e) {
                    // Ignore classes with missing dependencies
                }
            }
        }
    }

    /**
     * Loads a single controller class by name.
     * Used for manual controller loading from command line.
     */
    public Class<?> loadController(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        if (!clazz.isAnnotationPresent(RestController.class)) {
            throw new IllegalArgumentException("Class " + className + " is not annotated with @RestController");
        }
        return clazz;
    }
}
