package com.raulgomis.djc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DynamicClassLoaderTest {

    @Test
    void exposesCompiledClassesAsClassResources() throws Exception {
        DynamicClassLoader classLoader = new DynamicClassLoader(getClass().getClassLoader());
        DynamicByteObject byteObject = new DynamicByteObject("example.Generated", javax.tools.JavaFileObject.Kind.CLASS);
        byte[] bytecode = {1, 2, 3, 4};
        byteObject.openOutputStream().write(bytecode);
        classLoader.addClass(byteObject);

        try (InputStream resource = classLoader.getResourceAsStream("example/Generated.class")) {
            assertNotNull(resource);
            assertArrayEquals(bytecode, resource.readAllBytes());
        }
    }

    @Test
    void delegatesUnknownResourcesToItsParent() throws Exception {
        DynamicClassLoader classLoader = new DynamicClassLoader(getClass().getClassLoader());

        try (InputStream resource = classLoader.getResourceAsStream("Test01.java")) {
            assertNotNull(resource);
        }
        assertNull(classLoader.getResourceAsStream("missing/Type.class"));
    }

    @Test
    void reportsUnknownClasses() {
        DynamicClassLoader classLoader = new DynamicClassLoader(null);

        assertThrows(ClassNotFoundException.class, () -> classLoader.findClass("missing.Type"));
    }
}
