# What is dynamic-java-compiler?
_Dynamic-java-compiler_ is a library that allows users to dynamically compile and execute any java source code. Writing dynamically executed Java applications require some boilerplate code: working with classloaders, compilation error handling, etc. The idea behind this library is to free you from this development and let you focus on your business logic.

# How does it work?

The dynamic ompilation task is very simple with this library. Imagine we want to compile this source code introduced dynamically as a text string by the user:

```java
public class Test01 implements Runnable {
	public void run() {
		System.out.println("Hello World!");
	}
}
```

So simple, we just need to instantiate the compiler and compile the code:

```java
DynamicCompiler<Runnable> compiler = new DynamicCompiler<>();
// Read source code as String
Class<Runnable> clazz = compiler.compile(null, "Test01", source);
final Runnable r;
try {
    r = clazz.newInstance();
    r.run();
} catch (InstantiationException | IllegalAccessException e) {
    e.printStackTrace();
}
```

The final result will be:
```
Hello World!
```

        
## Contribution

You are welcome to contribute to the project using pull requests on GitHub.

If you find a bug or want to request a feature, please use the [issue tracker](https://github.com/raulgomis/dynamic-java-compiler/issues) of Github.
