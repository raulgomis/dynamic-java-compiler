package com.raulgomis.djc;

import org.junit.jupiter.api.Test;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DynamicCompilerExceptionTest {

    @Test
    void messageOnlyExceptionHasNoDiagnostics() {
        DynamicCompilerException exception = new DynamicCompilerException("Compiler not found");

        assertEquals("Compiler not found", exception.getMessage());
        assertTrue(exception.getDiagnostics().isEmpty());
        assertEquals("", exception.getDiagnosticsError());
        assertEquals("", exception.toString());
        assertTrue(new DynamicCompilerException("Compilation failure", null).getDiagnostics().isEmpty());
    }

    @Test
    void snapshotsDiagnosticsAndFormatsThem() {
        List<Diagnostic<? extends JavaFileObject>> diagnostics = new ArrayList<>();
        diagnostics.add(diagnostic(7, "bad source"));

        DynamicCompilerException exception = new DynamicCompilerException("Compilation failure", diagnostics);
        diagnostics.clear();

        assertEquals("Error on line 7: bad source\n", exception.getDiagnosticsError());
        assertEquals(exception.getDiagnosticsError(), exception.toString());
        assertThrows(UnsupportedOperationException.class, () -> exception.getDiagnostics().clear());
    }

    @Test
    void throwableConstructorPreservesCauseAndHandlesNullDiagnostics() {
        IllegalStateException cause = new IllegalStateException("closed");

        DynamicCompilerException exception = new DynamicCompilerException(cause, null);

        assertSame(cause, exception.getCause());
        assertTrue(exception.getDiagnostics().isEmpty());
    }

    private static Diagnostic<JavaFileObject> diagnostic(long line, String message) {
        return new Diagnostic<>() {
            @Override public Kind getKind() { return Kind.ERROR; }
            @Override public JavaFileObject getSource() { return null; }
            @Override public long getPosition() { return NOPOS; }
            @Override public long getStartPosition() { return NOPOS; }
            @Override public long getEndPosition() { return NOPOS; }
            @Override public long getLineNumber() { return line; }
            @Override public long getColumnNumber() { return NOPOS; }
            @Override public String getCode() { return "test"; }
            @Override public String getMessage(java.util.Locale locale) { return message; }
        };
    }
}
