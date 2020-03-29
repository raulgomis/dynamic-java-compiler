package com.raulgomis.djc;

import javax.tools.JavaFileObject.Kind;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class DynamicClassLoader extends ClassLoader {

    private Map<String, DynamicByteObject> classes = new HashMap<>();

    DynamicClassLoader(ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    void addClass(DynamicByteObject compiledObj) {
        System.out.println("Compiled " + compiledObj.getName());
        classes.put(compiledObj.getName(), compiledObj);
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        DynamicByteObject byteObject = classes.get(className);
        if (byteObject != null) {
            byte[] bytes = byteObject.getBytes();
            return defineClass(className, bytes, 0, bytes.length);
        }

        return super.findClass(className);
    }

    @Override
    protected synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    @Override
    public InputStream getResourceAsStream(final String name) {
        if (name.endsWith(Kind.CLASS.extension)) {
            String qualifiedClassName = name.substring(0, name.length() - Kind.CLASS.extension.length()).replace('/', '.');
            DynamicByteObject file = classes.get(qualifiedClassName);
            if (file != null) {
                return new ByteArrayInputStream(file.getBytes());
            }
        }
        return super.getResourceAsStream(name);
    }
}
