package com.microspringboot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit tests for IoCContainer.
 */
class IoCContainerTest {

    private IoCContainer container;

    @BeforeEach
    void setUp() {
        container = new IoCContainer();
    }

    @Test
    @DisplayName("Should register HelloController successfully")
    void testRegisterHelloController() throws Exception {
        container.registerController(HelloController.class);
        
        assertTrue(container.hasRoute("/"));
        assertTrue(container.hasRoute("/hello"));
        assertTrue(container.hasRoute("/pi"));
    }

    @Test
    @DisplayName("Should register GreetingController successfully")
    void testRegisterGreetingController() throws Exception {
        container.registerController(GreetingController.class);
        
        assertTrue(container.hasRoute("/greeting"));
    }

    @Test
    @DisplayName("Should invoke HelloController index method")
    void testInvokeIndexMethod() throws Exception {
        container.registerController(HelloController.class);
        
        String result = container.invokeRoute("/", new HashMap<>());
        
        assertEquals("Greetings from Spring Boot!", result);
    }

    @Test
    @DisplayName("Should invoke HelloController PI method")
    void testInvokePIMethod() throws Exception {
        container.registerController(HelloController.class);
        
        String result = container.invokeRoute("/pi", new HashMap<>());
        
        assertTrue(result.startsWith("PI = "));
        assertTrue(result.contains("3.14"));
    }

    @Test
    @DisplayName("Should invoke GreetingController with default parameter")
    void testInvokeGreetingDefault() throws Exception {
        container.registerController(GreetingController.class);
        
        String result = container.invokeRoute("/greeting", new HashMap<>());
        
        assertEquals("Hello World", result);
    }

    @Test
    @DisplayName("Should invoke GreetingController with custom parameter")
    void testInvokeGreetingWithParam() throws Exception {
        container.registerController(GreetingController.class);
        
        Map<String, String> params = new HashMap<>();
        params.put("name", "Maria");
        
        String result = container.invokeRoute("/greeting", params);
        
        assertEquals("Hello Maria", result);
    }

    @Test
    @DisplayName("Should return null for non-existent route")
    void testNonExistentRoute() throws Exception {
        container.registerController(HelloController.class);
        
        String result = container.invokeRoute("/unknown", new HashMap<>());
        
        assertNull(result);
    }

    @Test
    @DisplayName("Should throw exception for non-RestController class")
    void testNonRestControllerClass() {
        assertThrows(IllegalArgumentException.class, () -> {
            container.registerController(String.class);
        });
    }

    @Test
    @DisplayName("Should return registered route mappings")
    void testGetRouteMappings() throws Exception {
        container.registerController(HelloController.class);
        container.registerController(GreetingController.class);
        
        Map<String, ?> mappings = container.getRouteMappings();
        
        assertEquals(4, mappings.size());
        assertTrue(mappings.containsKey("/"));
        assertTrue(mappings.containsKey("/hello"));
        assertTrue(mappings.containsKey("/pi"));
        assertTrue(mappings.containsKey("/greeting"));
    }
}
