# Running Tests

This project is a Maven multi-module build. When running specific tests, target the
module with `-pl` so Maven does not apply the filter across the entire repo.

Most tests live in:

- `opc-ua-sdk/integration-tests`
- `opc-ua-stack/stack-core`
- `opc-ua-sdk/sdk-server`

## Common Commands

Run a specific test class:

```bash
mvn -q -pl opc-ua-sdk/sdk-server test -Dtest=ClassName
```

Run a specific test method:

```bash
mvn -q -pl opc-ua-sdk/sdk-server test -Dtest=ClassName#methodName
```

Run tests matching a pattern:

```bash
mvn -q -pl opc-ua-stack/stack-core test -Dtest=*ServiceTest
```

Run the full test suite:

```bash
mvn -q clean verify
```

## Module Targeting Flags

- `-pl <module>`: Build or test only the specified module(s).
- `-am`: Also build modules that the `-pl` target depends on.
- `-amd`: Also build modules that depend on the `-pl` target.

## When To Use Each Flag

Use `-pl` alone when the change is entirely within the module being tested:

```bash
# Changed and testing sdk-server only
mvn -q -pl opc-ua-sdk/sdk-server test -Dtest=ClassName
```

Use `-pl ... -am` when the code you changed is a dependency of the module whose tests
you want to run. This rebuilds the dependency chain so tests run against fresh code:

```bash
# Changed stack-core, running integration-tests
mvn -q -pl opc-ua-sdk/integration-tests -am test -Dtest=ClassName
```

Use `-pl ... -amd` when you changed a low-level module and want to run tests in that
module plus everything that depends on it:

```bash
# Changed stack-core, run tests in stack-core and everything that depends on it
mvn -q -pl opc-ua-stack/stack-core -amd test
```

## Rule Of Thumb

If the code you changed and the tests you are running are in different modules, add
`-am`. Without it, Maven can run tests against stale compiled dependencies.
