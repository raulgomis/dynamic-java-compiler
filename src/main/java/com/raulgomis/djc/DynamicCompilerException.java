package com.raulgomis.djc;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.List;

public final class DynamicCompilerException extends Exception {

    private List<Diagnostic<? extends JavaFileObject>> diagnostics;

    public DynamicCompilerException(String message) {
        super(message);
    }

    public DynamicCompilerException(String message, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        super(message);
        this.diagnostics = diagnostics;
    }

    public DynamicCompilerException(Throwable e, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        super(e);
        this.diagnostics = diagnostics;
    }

    public String getDiagnosticsError() {
        StringBuilder sb = new StringBuilder();
        if(diagnostics != null) {
            diagnostics.forEach(diagnostic -> sb.append(String.format("Error on line %d: %s\n",
                    diagnostic.getLineNumber(),
                    diagnostic.getMessage(null))));
        }
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
