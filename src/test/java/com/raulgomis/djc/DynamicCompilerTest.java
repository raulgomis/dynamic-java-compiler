package com.raulgomis.djc;

import com.raulgomis.djc.utils.CompilerExecutor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class DynamicCompilerTest {

    @Test
    void test01() {
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
            assertNotNull(clazz);
        } catch (DynamicCompilerException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void test02() {
        CompilerExecutor compilerExecutor = new CompilerExecutor();
        try {
            compilerExecutor.compile("Test02.java");
            fail();
        } catch (DynamicCompilerException e) {
            assertEquals("Error on line 3: ';' expected\n",
                    e.getDiagnosticsError());
        }
    }

    @Test
    void test03() {
      CompilerExecutor compilerExecutor = new CompilerExecutor();
      try {
          Class<Runnable> clazz = compilerExecutor.compile("Test03.java");
          final Runnable r;
          try {
              r = clazz.newInstance();
              r.run();
          } catch (InstantiationException | IllegalAccessException e) {
              e.printStackTrace();
          }
          assertNotNull(clazz);
      } catch (DynamicCompilerException e) {
          e.printStackTrace();
          fail();
      }
    }
}
