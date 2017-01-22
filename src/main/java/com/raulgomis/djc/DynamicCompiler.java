package com.raulgomis.djc;

import java.util.Arrays;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public final class DynamicCompiler<T> {

    private JavaCompiler compiler;
    private DynamicExtendedFileManager dynamicExtendedFileManager;
    private DynamicClassLoader classLoader;

    private DiagnosticCollector<JavaFileObject> diagnostics;

    public DynamicCompiler() throws DynamicCompilerException {
        compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new DynamicCompilerException("Compiler not found");
        }

        classLoader = new DynamicClassLoader(getClass().getClassLoader());
        diagnostics = new DiagnosticCollector<>();

        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        dynamicExtendedFileManager = new DynamicExtendedFileManager(standardFileManager, classLoader);
    }

    @SuppressWarnings("unchecked")
    public synchronized Class<T> compile(String packageName, String className, String javaSource) throws DynamicCompilerException {
        try {
            String qualifiedClassName = DynamicCompilerUtils.getQualifiedClassName(packageName, className);
            DynamicSourceCode sourceObj = new DynamicSourceCode(className, javaSource);
            DynamicSourceCode compiledObj = new DynamicSourceCode(qualifiedClassName);
            dynamicExtendedFileManager.setSources(sourceObj, compiledObj);

            CompilationTask task = compiler.getTask(null, dynamicExtendedFileManager, diagnostics, null, null, Arrays.asList(sourceObj));
            boolean result = task.call();

            if (!result) {
                throw new DynamicCompilerException("Compilation failure", diagnostics.getDiagnostics());
            }

            Class<T> newClass = (Class<T>) classLoader.loadClass(qualifiedClassName);
            return newClass;

        } catch (Exception exception) {
            throw new DynamicCompilerException(exception, diagnostics.getDiagnostics());
        }
    }
}
