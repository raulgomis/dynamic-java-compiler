# Dynamic Java Compiler

[![Build](https://github.com/raulgomis/dynamic-java-compiler/actions/workflows/master.yml/badge.svg)](https://github.com/raulgomis/dynamic-java-compiler/actions/workflows/master.yml)

Compile Java source held in a string and load the resulting class directly into
the running JVM.

Dynamic Java Compiler wraps the standard Java compiler, an in-memory file
manager, and a dedicated class loader behind a small API. Source and generated
bytecode stay in memory, so callers do not need to manage temporary `.java` or
`.class` files.

## Features

- Compiles Java source at runtime using the JDK compiler
- Loads generated classes without writing bytecode to disk
- Supports packages, imports, and nested classes
- Returns structured compiler diagnostics on failure
- Has no runtime dependencies outside the JDK

## Requirements

- Java 17 or later
- A full JDK that includes the system Java compiler

The project is compiled with `maven.compiler.release=17`, so its bytecode
remains compatible with Java 17 while builds can run on newer JDKs. A minimal
runtime image without the `jdk.compiler` module cannot perform dynamic
compilation.

## Quick start

Create the source code to compile:

```java
String source = """
    public class GreetingTask implements Runnable {
        @Override
        public void run() {
            System.out.println("Hello from dynamically compiled code!");
        }
    }
    """;
```

Compile, instantiate, and execute it:

```java
import com.raulgomis.djc.DynamicCompiler;

DynamicCompiler<Runnable> compiler = new DynamicCompiler<>();
Class<Runnable> taskClass = compiler.compile(
    null,
    "GreetingTask",
    source
);

Runnable task = taskClass.getDeclaredConstructor().newInstance();
task.run();
```

The first argument to `compile` is the package name. Pass `null` or an empty
string for the default package. The second argument is the simple class name
and must agree with the class declared by the source.

## Compiling a packaged class

The package declared in the source must match the package passed to
`compile`:

```java
String source = """
    package example.tasks;

    public class GreetingTask implements Runnable {
        @Override
        public void run() {
            System.out.println("Hello!");
        }
    }
    """;

DynamicCompiler<Runnable> compiler = new DynamicCompiler<>();
Class<Runnable> taskClass = compiler.compile(
    "example.tasks",
    "GreetingTask",
    source
);
```

The returned class has the binary name `example.tasks.GreetingTask`.

## Handling compilation errors

Compilation failures throw `DynamicCompilerException`. Use
`getDiagnostics()` for structured JDK diagnostics or
`getDiagnosticsError()` for a readable summary:

```java
import com.raulgomis.djc.DynamicCompilerException;

try {
    DynamicCompiler<Runnable> compiler = new DynamicCompiler<>();
    compiler.compile(null, "BrokenTask", brokenSource);
} catch (DynamicCompilerException exception) {
    System.err.print(exception.getDiagnosticsError());

    exception.getDiagnostics().forEach(diagnostic -> {
        System.err.printf(
            "line %d, column %d: %s%n",
            diagnostic.getLineNumber(),
            diagnostic.getColumnNumber(),
            diagnostic.getMessage(null)
        );
    });
}
```

Diagnostic wording is produced by the active JDK and may differ between JDK
versions.

## Security

This library compiles code; it does not sandbox it. Once loaded and invoked,
dynamically compiled code runs with the same permissions as the host
application. Do not compile or execute untrusted source without a separate,
appropriately isolated execution environment.

## Building and testing

Run the full verification build:

```shell
mvn verify
```

This runs the JUnit 5 test suite and generates a JaCoCo coverage report at
`target/site/jacoco/index.html`.

For a clean build:

```shell
mvn clean verify
```

## Contributing

Pull requests are welcome. Please include focused tests for behavioral changes
and run `mvn verify` before submitting.

Use the [issue tracker](https://github.com/raulgomis/dynamic-java-compiler/issues)
to report bugs or propose features.

## License

This project is available under the [MIT License](LICENSE).
