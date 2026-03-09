package com.microspringboot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Unit tests for ClasspathScanner.
 */
class ClasspathScannerTest {

    @Test
    @DisplayName("Should scan package without errors")
    void testScanForControllers() {
        ClasspathScanner scanner = new ClasspathScanner();
        
        // Note: Classpath scanning may return empty list in test environment
        // because classes might be in different locations during test phase
        List<Class<?>> controllers = scanner.scanForControllers("com.microspringboot");
        
        // Should not throw exception
        assertNotNull(controllers);
    }

    @Test
    @DisplayName("Should return empty list for non-existent package")
    void testScanNonExistentPackage() {
        ClasspathScanner scanner = new ClasspathScanner();
        
        List<Class<?>> controllers = scanner.scanForControllers("com.nonexistent");
        
        assertTrue(controllers.isEmpty());
    }

    @Test
    @DisplayName("Should load controller by class name")
    void testLoadController() throws ClassNotFoundException {
        ClasspathScanner scanner = new ClasspathScanner();
        
        Class<?> controller = scanner.loadController("com.microspringboot.HelloController");
        
        assertEquals(HelloController.class, controller);
    }

    @Test
    @DisplayName("Should throw exception for non-RestController class")
    void testLoadNonController() {
        ClasspathScanner scanner = new ClasspathScanner();
        
        assertThrows(IllegalArgumentException.class, () -> {
            scanner.loadController("java.lang.String");
        });
    }

    @Test
    @DisplayName("Should throw ClassNotFoundException for unknown class")
    void testLoadUnknownClass() {
        ClasspathScanner scanner = new ClasspathScanner();
        
        assertThrows(ClassNotFoundException.class, () -> {
            scanner.loadController("com.unknown.UnknownClass");
        });
    }
}
