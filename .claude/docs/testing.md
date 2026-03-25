# Testing Guide

This is a multi-module project. When running specific tests, target the module using `-pl`
to avoid applying the filter to all modules.

## Running Specific Tests

**Run a specific test class:**

```bash
mvn -q -pl opc-ua-sdk/sdk-server test -Dtest=ClassName
```

**Run a specific test method:**

```bash
mvn -q -pl opc-ua-sdk/sdk-server test -Dtest=ClassName#methodName
```

**Run tests matching a pattern:**

```bash
mvn -q -pl opc-ua-stack/stack-core test -Dtest=*ServiceTest
```

Most tests live in `opc-ua-sdk/integration-tests`, `opc-ua-stack/stack-core`, and
`opc-ua-sdk/sdk-server`.

## Module Targeting Flags

- **`-pl <module>`** — build/test only the specified module(s)
- **`-am`** (also-make) — also build modules that the `-pl` target depends on
- **`-amd`** (also-make-dependents) — also build modules that depend on the `-pl` target

### When to use each

**`-pl` alone** — the change is entirely within the module being tested:

```bash
# Changed and testing sdk-server only
mvn -q -pl opc-ua-sdk/sdk-server test -Dtest=ClassName
```

**`-pl ... -am`** — the change is in a dependency of the module being tested. This
rebuilds the dependency chain so tests run against the latest code:

```bash
# Changed stack-core, running integration-tests
mvn -q -pl opc-ua-sdk/integration-tests -am test -Dtest=ClassName
```

**`-pl ... -amd`** — the change is in a low-level module and you want to test all modules
that depend on it:

```bash
# Changed stack-core, run tests in stack-core and everything that depends on it
mvn -q -pl opc-ua-stack/stack-core -amd test
```

### Rule of thumb

If the code you changed and the tests you're running are in **different modules**, add
`-am` so the changed module gets rebuilt. Omitting `-am` in this case means tests run
against stale (previously compiled) code.

## Run All Tests

```bash
mvn -q clean verify
```
