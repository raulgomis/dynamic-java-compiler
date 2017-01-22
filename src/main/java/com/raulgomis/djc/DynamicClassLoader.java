package com.raulgomis.djc;

import java.util.HashMap;
import java.util.Map;

public final class DynamicClassLoader extends ClassLoader {

    private Map<String, DynamicSourceCode> classes = new HashMap<>();

    public DynamicClassLoader(ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    public void addClass(DynamicSourceCode compiledObj) {
        classes.put(compiledObj.getName(), compiledObj);
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        DynamicSourceCode bean = classes.get(className);
        if (bean == null) {
            return super.findClass(className);
        }
        byte[] bytes = bean.getBytes();
        return defineClass(className, bytes, 0, bytes.length);
    }
}