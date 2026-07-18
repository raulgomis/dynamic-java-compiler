package com.raulgomis.djc;

import java.net.URI;
import java.net.URISyntaxException;
import javax.tools.JavaFileObject.Kind;

public final class DynamicCompilerUtils {

    public static final String EMPTY = "";

    private DynamicCompilerUtils() {
    }

    static URI createURI(String str) {
        try {
            return new URI(str);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI: " + str, e);
        }
    }

    static String getQualifiedClassName(String packageName, String className) {
        if (isEmpty(packageName)) {
            return className;
        }
        return packageName + "." + className;
    }

    static String getClassNameWithExt(String className) {
        return className + Kind.SOURCE.extension;
    }

    static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
