# Improve Java compatibility, diagnostics, coverage, and documentation

## Summary

This change strengthens the library's compatibility and quality checks, fixes
diagnostic state leaking between compilations, expands test coverage, and
provides clearer documentation for users and contributors.

## Changes

- Target Java 17 bytecode so the library remains usable on Java 17 and later.
- Test the project in CI on Java 17, 21, 25, and 26.
- Run the Maven `verify` lifecycle in CI and upload the generated JaCoCo report
  for each tested JDK.
- Create a fresh diagnostic collector for every compilation so failures do not
  retain diagnostics from earlier attempts.
- Make diagnostic error output use a stable newline across operating systems.
- Add focused tests for:
  - independent diagnostics across compilation failures
  - named-package compilation
  - wrapped class-loading failures
  - dynamic class and resource loading
  - file-manager lookup, delegation, output, and binary-name handling
  - exception formatting, defensive copies, null diagnostics, and causes
- Expand the README with requirements, installation, usage, package and error
  examples, security guidance, and contributor instructions.
- Add repository-specific guidance for coding agents in `AGENTS.md`.

## Verification

- [x] `mvn clean verify`
- [x] 26 tests pass
- [x] 97.7% line coverage
- [x] 91.7% branch coverage
- [x] 97.7% instruction coverage
- [x] `git diff --check`

## Notes

Dynamically compiled code is not sandboxed and runs with the permissions of the
host application. The updated README now calls this out explicitly.
