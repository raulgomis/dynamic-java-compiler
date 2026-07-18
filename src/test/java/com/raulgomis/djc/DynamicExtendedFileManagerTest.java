package com.raulgomis.djc;

import org.junit.jupiter.api.Test;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class DynamicExtendedFileManagerTest {

    @Test
    void returnsRegisteredInputsAndDelegatesPlatformLookups() throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager standardManager = compiler.getStandardFileManager(null, null, null);
             DynamicExtendedFileManager manager = new DynamicExtendedFileManager(
                 standardManager,
                 new DynamicClassLoader(getClass().getClassLoader())
             )) {
            DynamicStringObject source = new DynamicStringObject("Example", "class Example {}");
            manager.putFileForInput(StandardLocation.SOURCE_PATH, "example", "Example.java", source);

            assertSame(source, manager.getFileForInput(StandardLocation.SOURCE_PATH, "example", "Example.java"));
            assertNotNull(manager.getFileForInput(
                StandardLocation.PLATFORM_CLASS_PATH,
                "java.lang",
                "String.class"
            ));
        }
    }

    @Test
    void createsOutputsAndInfersBinaryNamesForBothFileKinds() throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DynamicClassLoader classLoader = new DynamicClassLoader(getClass().getClassLoader());
        try (StandardJavaFileManager standardManager = compiler.getStandardFileManager(null, null, null);
             DynamicExtendedFileManager manager = new DynamicExtendedFileManager(standardManager, classLoader)) {
            JavaFileObject output = manager.getJavaFileForOutput(
                StandardLocation.CLASS_OUTPUT,
                "example.Generated",
                JavaFileObject.Kind.CLASS,
                null
            );
            JavaFileObject stringClass = standardManager.getJavaFileForInput(
                StandardLocation.PLATFORM_CLASS_PATH,
                "java.lang.String",
                JavaFileObject.Kind.CLASS
            );

            assertEquals("example.Generated", manager.inferBinaryName(StandardLocation.CLASS_OUTPUT, output));
            assertEquals("java.lang.String", manager.inferBinaryName(StandardLocation.PLATFORM_CLASS_PATH, stringClass));
            assertSame(classLoader, manager.getClassLoader(StandardLocation.CLASS_PATH));
        }
    }
}
