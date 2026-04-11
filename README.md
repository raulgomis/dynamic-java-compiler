# What is dynamic-java-compiler?
![Build](https://github.com/raulgomis/dynamic-java-compiler/workflows/Build/badge.svg)

_Dynamic-java-compiler_ is a library that allows users to dynamically compile and execute Java source code. Writing dynamically executed Java applications usually requires boilerplate code (working with classloaders, compilation error handling, etc.). This library removes that overhead so you can focus on business logic.

## Java compatibility

This project is configured to compile using Java **25** (`maven.compiler.release=25`) and has tests updated for modern JDK APIs.

## How does it work?

The dynamic compilation task is very simple with this library. Imagine we want to compile this source code introduced dynamically as a text string by the user:

```java
public class Test01 implements Runnable {
    public void run() {
        System.out.println("Hello World!");
    }
}
```

Now instantiate the compiler and compile the code:

```java
DynamicCompiler<Runnable> compiler = new DynamicCompiler<>();
// Read source code as String
Class<Runnable> clazz = compiler.compile(null, "Test01", source);
Runnable runnable = clazz.getDeclaredConstructor().newInstance();
runnable.run();
```

The final result will be:

```
Hello World!
```

## Contribution

You are welcome to contribute to the project using pull requests on GitHub.

If you find a bug or want to request a feature, please use the [issue tracker](https://github.com/raulgomis/dynamic-java-compiler/issues) on GitHub.
