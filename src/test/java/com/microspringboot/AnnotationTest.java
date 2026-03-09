package com.microspringboot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Unit tests for custom annotations.
 */
class AnnotationTest {

    @Test
    @DisplayName("@RestController should have RUNTIME retention")
    void testRestControllerRetention() {
        Retention retention = RestController.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    @DisplayName("@RestController should target TYPE")
    void testRestControllerTarget() {
        Target target = RestController.class.getAnnotation(Target.class);
        assertNotNull(target);
        assertEquals(1, target.value().length);
        assertEquals(ElementType.TYPE, target.value()[0]);
    }

    @Test
    @DisplayName("@GetMapping should have RUNTIME retention")
    void testGetMappingRetention() {
        Retention retention = GetMapping.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    @DisplayName("@GetMapping should target METHOD")
    void testGetMappingTarget() {
        Target target = GetMapping.class.getAnnotation(Target.class);
        assertNotNull(target);
        assertEquals(1, target.value().length);
        assertEquals(ElementType.METHOD, target.value()[0]);
    }

    @Test
    @DisplayName("@RequestParam should have RUNTIME retention")
    void testRequestParamRetention() {
        Retention retention = RequestParam.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    @DisplayName("@RequestParam should target PARAMETER")
    void testRequestParamTarget() {
        Target target = RequestParam.class.getAnnotation(Target.class);
        assertNotNull(target);
        assertEquals(1, target.value().length);
        assertEquals(ElementType.PARAMETER, target.value()[0]);
    }

    @Test
    @DisplayName("HelloController should be annotated with @RestController")
    void testHelloControllerAnnotation() {
        assertTrue(HelloController.class.isAnnotationPresent(RestController.class));
    }

    @Test
    @DisplayName("GreetingController should be annotated with @RestController")
    void testGreetingControllerAnnotation() {
        assertTrue(GreetingController.class.isAnnotationPresent(RestController.class));
    }
}
