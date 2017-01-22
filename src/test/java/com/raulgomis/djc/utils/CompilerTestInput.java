package com.raulgomis.djc.utils;

import com.raulgomis.djc.DynamicCompilerUtils;

import java.io.Serializable;
import java.util.Objects;

class CompilerTestInput implements Serializable {

    private static final long serialVersionUID = 3305045467314963410L;

    static final CompilerTestInput EMPTY = new CompilerTestInput(DynamicCompilerUtils.EMPTY, DynamicCompilerUtils.EMPTY);

    private String className;

    private String source;

    String getClassName() {
        return className;
    }

    String getSource() {
        return source;
    }

    CompilerTestInput(String className, String source) {
        this.className = className;
        this.source = source;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompilerTestInput that = (CompilerTestInput) o;
        return Objects.equals(className, that.className) &&
                Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, source);
    }

    @Override
    public String toString() {
        return "CompilerTestInput{" +
                "className='" + className + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
