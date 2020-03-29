package com.raulgomis.djc;

import javax.tools.JavaFileObject.Kind;
import java.net.URI;
import java.net.URISyntaxException;

public final class DynamicCompilerUtils {

    private DynamicCompilerUtils() {

    }

    public static final String EMPTY = "";

    static URI createURI(String str) {
        try {
            return new URI(str);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    static String getQualifiedClassName(String packageName, String className) {
        if (isEmpty(packageName)) {
            return className;
        } else {
            return packageName + "." + className;
        }
    }

    static String getClassNameWithExt(String className) {
        return className + Kind.SOURCE.extension;
    }

    static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

}