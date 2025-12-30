# Testing Guide

This is a multi-module project. When running specific tests, target the module that contains the test
using `-pl` to avoid applying the filter to all modules. Optionally add `-am` to build required
dependent modules.

## Module Quick Reference

| What you're testing       | Module path                    |
|---------------------------|--------------------------------|
| Core types, encoding      | `opc-ua-stack/stack-core`      |
| Client SDK                | `opc-ua-sdk/sdk-client`        |
| Server SDK                | `opc-ua-sdk/sdk-server`        |
| Client-server integration | `opc-ua-sdk/integration-tests` |
| JSON encoding             | `opc-ua-stack/encoding-json`   |
| Custom data types         | `opc-ua-sdk/dtd-core`          |

## Running Specific Tests

The examples below assume the test lives in `opc-ua-stack/stack-core` — adjust the module path as
needed.

**Run a specific test class:**
```bash
mvn -q -pl opc-ua-stack/stack-core test -Dtest=ClassName
```

**Run a specific test method:**
```bash
mvn -q -pl opc-ua-stack/stack-core test -Dtest=ClassName#methodName
```

**Run multiple test classes:**
```bash
mvn -q -pl opc-ua-stack/stack-core test -Dtest=ClassOne,ClassTwo
```

**Run tests matching a pattern:**
```bash
mvn -q -pl opc-ua-stack/stack-core test -Dtest=*ServiceTest
```

## Run All Tests

To run all tests and verify the project:

```bash
mvn -q clean verify
```

This will clean previous builds, compile the code, run all unit and integration tests, and run code
quality checks (like Spotless).
