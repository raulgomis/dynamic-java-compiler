package com.raulgomis.djc;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import javax.tools.SimpleJavaFileObject;

public class DynamicByteObject extends SimpleJavaFileObject {

    private final ByteArrayOutputStream outputStream;

    DynamicByteObject(String name, Kind kind) {
        super(DynamicCompilerUtils.createURI(name), kind);
        outputStream = new ByteArrayOutputStream();
    }

    @Override
    public OutputStream openOutputStream() {
        return outputStream;
    }

    byte[] getBytes() {
        return outputStream.toByteArray();
    }
}
