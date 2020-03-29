package com.raulgomis.djc;

import javax.tools.SimpleJavaFileObject;

public class DynamicStringObject extends SimpleJavaFileObject {
    private final String source;

    DynamicStringObject(String name, String source) {
        super(DynamicCompilerUtils.createURI(DynamicCompilerUtils.getClassNameWithExt(name)), Kind.SOURCE);
        this.source = source;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return source;
    }
}
