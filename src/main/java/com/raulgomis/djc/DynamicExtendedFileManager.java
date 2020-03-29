package com.raulgomis.djc;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public final class DynamicExtendedFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private DynamicClassLoader classLoader;
    private final Map<URI, JavaFileObject> fileObjects = new HashMap<>();

    DynamicExtendedFileManager(JavaFileManager fileManager, DynamicClassLoader classLoader) {
        super(fileManager);
        this.classLoader = classLoader;
    }

    @Override
    public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
        FileObject o = fileObjects.get(uri(location, packageName, relativeName));
        if (o != null) {
            return o;
        }
        return super.getFileForInput(location, packageName, relativeName);
    }

    void putFileForInput(StandardLocation location, String packageName, String relativeName, JavaFileObject file) {
        fileObjects.put(uri(location, packageName, relativeName), file);
    }

    private URI uri(Location location, String packageName, String relativeName) {
        return DynamicCompilerUtils.createURI(location.getName() + '/' + packageName + '/' + relativeName);
    }


    @Override
    public JavaFileObject getJavaFileForOutput(Location location,
                                               String qualifiedName, Kind kind, FileObject outputFile) {
        DynamicByteObject dynamicByteObject = new DynamicByteObject(qualifiedName, kind);
        classLoader.addClass(dynamicByteObject);
        return dynamicByteObject;
    }

    @Override
    public ClassLoader getClassLoader(JavaFileManager.Location location) {
        return classLoader;
    }

    @Override
    public String inferBinaryName(Location loc, JavaFileObject file) {
        if (file instanceof DynamicByteObject) {
            return file.getName();
        }
        return super.inferBinaryName(loc, file);
    }
}
