package com.raulgomis.djc;

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

public final class DynamicExtendedFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private DynamicClassLoader classLoader;

    private DynamicSourceCode sourceCode;
    private DynamicSourceCode compiledCode;

    public DynamicExtendedFileManager(JavaFileManager fileManager, DynamicClassLoader classLoader) {
        super(fileManager);
        this.classLoader = classLoader;
    }

    public void setSources(DynamicSourceCode sourceObject, DynamicSourceCode compiledObject) {
        this.sourceCode = sourceObject;
        this.compiledCode = compiledObject;
        this.classLoader.addClass(compiledObject);
    }

    @Override
    public FileObject getFileForInput(Location location, String packageName,
                                      String relativeName) throws IOException {
        return sourceCode;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location,
                                               String qualifiedName, Kind kind, FileObject outputFile)
            throws IOException {
        return compiledCode;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return classLoader;
    }
}