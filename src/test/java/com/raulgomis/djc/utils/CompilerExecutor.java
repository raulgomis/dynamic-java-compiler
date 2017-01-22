package com.raulgomis.djc.utils;

import com.raulgomis.djc.DynamicCompiler;
import com.raulgomis.djc.DynamicCompilerException;

/**
 * @author rgomis <raulgomis@gmail.com>
 */
public class CompilerExecutor {

    public Class<Runnable> compile(final String className) throws DynamicCompilerException {
        DynamicCompiler<Runnable> compiler = new DynamicCompiler<>();
        CompilerTestInput compilerTestInput = CompilerTestUtils.getSource(className);
        Class<Runnable> clazz = compiler.compile(null, compilerTestInput.getClassName(), compilerTestInput.getSource());
        return clazz;
    }
}
