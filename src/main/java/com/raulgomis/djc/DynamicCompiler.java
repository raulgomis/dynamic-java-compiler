package com.raulgomis.djc;

import java.util.List;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public final class DynamicCompiler<T> {

    private final JavaCompiler compiler;
    private final DynamicExtendedFileManager dynamicExtendedFileManager;
    private final DynamicClassLoader classLoader;
    private final DiagnosticCollector<JavaFileObject> diagnostics;

    public DynamicCompiler() throws DynamicCompilerException {
        compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new DynamicCompilerException("Compiler not found");
        }

        classLoader = new DynamicClassLoader(Thread.currentThread().getContextClassLoader());
        diagnostics = new DiagnosticCollector<>();

        final StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        dynamicExtendedFileManager = new DynamicExtendedFileManager(standardFileManager, classLoader);
    }

    @SuppressWarnings("unchecked")
    public synchronized Class<T> compile(String packageName, String className, String javaSource) throws DynamicCompilerException {
        try {
            final String qualifiedClassName = DynamicCompilerUtils.getQualifiedClassName(packageName, className);
            final DynamicStringObject sourceObj = new DynamicStringObject(className, javaSource);

            dynamicExtendedFileManager.putFileForInput(
                StandardLocation.SOURCE_PATH,
                packageName,
                className + JavaFileObject.Kind.SOURCE.extension,
                sourceObj
            );

            final CompilationTask task = compiler.getTask(
                null,
                dynamicExtendedFileManager,
                diagnostics,
                null,
                null,
                List.of(sourceObj)
            );

            if (!task.call()) {
                throw new DynamicCompilerException("Compilation failure", diagnostics.getDiagnostics());
            }

            dynamicExtendedFileManager.close();
            return (Class<T>) classLoader.loadClass(qualifiedClassName);
        } catch (DynamicCompilerException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new DynamicCompilerException(exception, diagnostics.getDiagnostics());
        }
    }
}
