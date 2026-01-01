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

## Run All Tests

```bash
mvn -q clean verify
```
