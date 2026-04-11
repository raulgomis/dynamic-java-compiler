package com.raulgomis.djc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.tools.JavaFileObject.Kind;

public final class DynamicClassLoader extends ClassLoader {

    private final Map<String, DynamicByteObject> classes = new ConcurrentHashMap<>();

    DynamicClassLoader(ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    void addClass(DynamicByteObject compiledObj) {
        classes.put(compiledObj.getName(), compiledObj);
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        final DynamicByteObject byteObject = classes.get(className);
        if (byteObject != null) {
            final byte[] bytes = byteObject.getBytes();
            return defineClass(className, bytes, 0, bytes.length);
        }

        return super.findClass(className);
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        if (name.endsWith(Kind.CLASS.extension)) {
            final String qualifiedClassName = name.substring(0, name.length() - Kind.CLASS.extension.length()).replace('/', '.');
            final DynamicByteObject file = classes.get(qualifiedClassName);
            if (file != null) {
                return new ByteArrayInputStream(file.getBytes());
            }
        }
        return super.getResourceAsStream(name);
    }
}
