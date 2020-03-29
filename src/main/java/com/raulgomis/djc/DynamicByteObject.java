package com.raulgomis.djc;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class DynamicByteObject extends SimpleJavaFileObject {
    private ByteArrayOutputStream outputStream;

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
