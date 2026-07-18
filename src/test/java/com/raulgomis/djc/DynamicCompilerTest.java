package com.raulgomis.djc;

import com.raulgomis.djc.utils.CompilerExecutor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void keepsDiagnosticsIndependentAcrossCompilationFailures() throws Exception {
        DynamicCompiler<Runnable> compiler = new DynamicCompiler<>();

        DynamicCompilerException firstFailure = assertThrows(
            DynamicCompilerException.class,
            () -> compiler.compile(
                null,
                "BrokenOne",
                "public class BrokenOne implements Runnable { public void run() { nope } }"
            )
        );
        int firstFailureDiagnosticCount = firstFailure.getDiagnostics().size();

        DynamicCompilerException secondFailure = assertThrows(
            DynamicCompilerException.class,
            () -> compiler.compile(
                null,
                "BrokenTwo",
                "public class BrokenTwo implements Runnable { public void run() { nope } }"
            )
        );

        assertTrue(firstFailureDiagnosticCount > 0);
        assertEquals(firstFailureDiagnosticCount, firstFailure.getDiagnostics().size());
        assertEquals(firstFailureDiagnosticCount, secondFailure.getDiagnostics().size());
        assertTrue(secondFailure.getDiagnostics().stream().allMatch(
            diagnostic -> diagnostic.getSource().getName().contains("BrokenTwo")
        ));
    }

    @Test
    void compilesAClassInANamedPackage() throws Exception {
        DynamicCompiler<Runnable> compiler = new DynamicCompiler<>();

        Class<Runnable> clazz = compiler.compile(
            "example.generated",
            "PackagedTask",
            "package example.generated; public class PackagedTask implements Runnable { public void run() {} }"
        );

        assertEquals("example.generated.PackagedTask", clazz.getName());
        assertNotNull(clazz.getDeclaredConstructor().newInstance());
    }

    @Test
    void wrapsClassLoadingFailuresWithTheirCause() throws Exception {
        DynamicCompiler<Runnable> compiler = new DynamicCompiler<>();

        DynamicCompilerException exception = assertThrows(
            DynamicCompilerException.class,
            () -> compiler.compile(
                "expected.package",
                "UnexpectedPackage",
                "public class UnexpectedPackage implements Runnable { public void run() {} }"
            )
        );

        assertInstanceOf(ClassNotFoundException.class, exception.getCause());
        assertTrue(exception.getDiagnostics().isEmpty());
    }
}
