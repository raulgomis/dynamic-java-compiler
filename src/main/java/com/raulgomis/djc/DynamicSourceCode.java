package com.raulgomis.djc;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.tools.SimpleJavaFileObject;

public final class DynamicSourceCode extends SimpleJavaFileObject {

    private String source;

    private ByteArrayOutputStream byteCode = new ByteArrayOutputStream();

    public DynamicSourceCode(String baseName, String source) {
        super(DynamicCompilerUtils.createURI(DynamicCompilerUtils.getClassNameWithExt(baseName)),
                Kind.SOURCE);
        this.source = source;
    }

    public DynamicSourceCode(String name) {
        super(DynamicCompilerUtils.createURI(name), Kind.CLASS);
    }

    @Override
    public String getCharContent(boolean ignoreEncodingErrors) {
        return source;
    }

    @Override
    public OutputStream openOutputStream() {
        return byteCode;
    }

    public byte[] getBytes() {
        return byteCode.toByteArray();
    }
}