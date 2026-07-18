package com.raulgomis.djc;

import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public final class DynamicCompilerException extends Exception {

    private final List<Diagnostic<? extends JavaFileObject>> diagnostics;

    public DynamicCompilerException(String message) {
        super(message);
        this.diagnostics = List.of();
    }

    public DynamicCompilerException(String message, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        super(message);
        this.diagnostics = diagnostics == null ? List.of() : List.copyOf(diagnostics);
    }

    public DynamicCompilerException(Throwable e, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        super(e);
        this.diagnostics = diagnostics == null ? List.of() : List.copyOf(diagnostics);
    }

    public String getDiagnosticsError() {
        final StringBuilder sb = new StringBuilder();
        diagnostics.forEach(diagnostic -> sb.append(
            String.format("Error on line %d: %s\n", diagnostic.getLineNumber(), diagnostic.getMessage(null))
        ));
        return sb.toString();
    }

    public List<Diagnostic<? extends JavaFileObject>> getDiagnostics() {
        return diagnostics;
    }

    @Override
    public String toString() {
        return getDiagnosticsError();
    }
}
