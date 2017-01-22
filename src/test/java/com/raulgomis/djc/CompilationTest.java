package com.raulgomis.djc;

import com.raulgomis.djc.utils.CompilerExecutor;
import org.junit.Assert;
import org.junit.Test;

public class CompilationTest {

    @Test
    public void test01() {
        CompilerExecutor compilerExecutor = new CompilerExecutor();
        try {
            Class<Runnable> clazz = compilerExecutor.compile("Test01.java");
            final Runnable r;
            try {
                r = clazz.newInstance();
                r.run();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            Assert.assertNotNull(clazz);
        } catch (DynamicCompilerException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void test02() {
        CompilerExecutor compilerExecutor = new CompilerExecutor();
        try {
            compilerExecutor.compile("Test02.java");
            Assert.fail();
        } catch (DynamicCompilerException e) {
            Assert.assertEquals("Error on line 3: ';' expected\n",
                    e.getDiagnosticsError());
        }
    }
}
