# AGENTS.md

## Project overview

This repository contains a small Java library for compiling and loading Java
source code at runtime. Production code is under
`src/main/java/com/raulgomis/djc`, and tests are under the matching package in
`src/test/java`.

The project targets Java 17 bytecode and uses Maven, JUnit 5, and JaCoCo.

## Build and verification

Run commands from the repository root.

- `mvn test` runs the unit tests.
- `mvn verify` runs the tests and generates the JaCoCo report.
- `mvn clean verify` performs a clean, full verification when stale build
  output may matter.
- The HTML coverage report is written to
  `target/site/jacoco/index.html`.

Before handing off a code change, run `mvn verify`. Also run
`git diff --check` to catch whitespace errors.

## Implementation guidelines

- Keep the public API small and compatible unless the task explicitly calls
  for an API change.
- Compile with Java 17-compatible language and library features.
- Follow the existing four-space indentation and brace style.
- Prefer standard-library solutions; avoid adding dependencies for behavior
  that can be implemented clearly without them.
- Keep compiler diagnostics associated with the individual compilation that
  produced them.
- Preserve the in-memory design: generated source and bytecode should not
  require temporary source or class files.
- Treat package names, binary class names, source file names, and resource
  paths carefully. A source package and the qualified name used for loading
  must agree.
- Preserve exception causes when wrapping unexpected compiler, file-manager,
  or class-loading failures.

## Testing guidelines

- Use JUnit Jupiter.
- Name tests after observable behavior, such as
  `compilesAClassInANamedPackage`.
- Add regression tests for bug fixes and focused tests for new branches.
- Test both successful compilation and relevant failure behavior, including
  diagnostics and wrapped causes.
- Tests for package-private implementation classes should remain in
  `com.raulgomis.djc` rather than widening production visibility.
- Prefer small inline source strings for a single scenario. Use
  `src/test/resources` when a source fixture is substantial or reused.
- Dynamically compiled test code must be deterministic and must not perform
  network access, modify user files, or depend on machine-specific state.
- Do not weaken assertions merely to increase the coverage percentage.

## Change hygiene

- The worktree may already contain user changes. Inspect `git status` and
  preserve unrelated modifications.
- Do not edit generated files under `target`.
- Keep documentation and CI configuration consistent with the Java version
  and Maven commands in `pom.xml`.
- Avoid broad formatting or refactoring unrelated to the requested change.
