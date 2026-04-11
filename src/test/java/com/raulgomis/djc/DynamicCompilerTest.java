package com.raulgomis.djc;

import com.raulgomis.djc.utils.CompilerExecutor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DynamicCompilerTest {

    @Test
    void compilesAndInstantiatesSimpleRunnable() throws Exception {
        CompilerExecutor compilerExecutor = new CompilerExecutor();

        Class<Runnable> clazz = compilerExecutor.compile("Test01.java");
        Runnable runnable = clazz.getDeclaredConstructor().newInstance();

        runnable.run();
        assertNotNull(clazz);
    }

    @Test
    void returnsReadableDiagnosticsForCompilationFailure() {
        CompilerExecutor compilerExecutor = new CompilerExecutor();

        DynamicCompilerException exception = assertThrows(
            DynamicCompilerException.class,
            () -> compilerExecutor.compile("Test02.java")
        );

        assertEquals("Error on line 3: ';' expected\n", exception.getDiagnosticsError());
    }

    @Test
    void compilesRunnableWithImports() throws Exception {
        CompilerExecutor compilerExecutor = new CompilerExecutor();

        Class<Runnable> clazz = compilerExecutor.compile("Test03.java");
        Runnable runnable = clazz.getDeclaredConstructor().newInstance();

        runnable.run();
        assertNotNull(clazz);
    }
}
