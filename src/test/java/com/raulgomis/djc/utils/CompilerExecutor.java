package com.raulgomis.djc.utils;

import com.raulgomis.djc.DynamicCompiler;
import com.raulgomis.djc.DynamicCompilerException;

public class CompilerExecutor {

    public Class<Runnable> compile(String className) throws DynamicCompilerException {
        DynamicCompiler<Runnable> compiler = new DynamicCompiler<>();
        CompilerTestInput compilerTestInput = CompilerTestUtils.getSource(className);
        return compiler.compile(null, compilerTestInput.getClassName(), compilerTestInput.getSource());
    }
}
