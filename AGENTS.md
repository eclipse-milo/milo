# Agent Instructions

## Building and Testing

Delegate to a subagent when running Maven commands.

**Note:** All Maven commands below use the `-q` (quiet) flag to reduce verbose output. If you need
to debug build issues or see detailed output, remove the `-q` flag and re-run the command.

### Build/Compile the Project

To compile the project without running tests:

```bash
mvn -q clean compile
```

### Run All Tests

To run all tests and verify the project:

```bash
mvn -q clean verify
```

This command will:

- Clean previous builds
- Compile the code
- Run all unit tests
- Run integration tests (if configured)
- Run code quality checks (like Spotless)

### Run Specific Tests

This is a multi-module project. When you want to run a specific test or pattern, you should target
the module that contains the test; otherwise Maven will try to apply the filter to all modules. Use
`-pl` to specify the module (optionally add `-am` to also build required dependent modules).

The examples below assume the test lives in the `opc-ua-stack/stack-core` module — adjust the module
path as needed (e.g., `opc-ua-sdk/sdk-core`, `opc-ua-sdk/sdk-client`, etc.).

To run a specific test class:

```bash
mvn -q -pl opc-ua-stack/stack-core test -Dtest=ClassName
```

To run a specific test method:

```bash
mvn -q -pl opc-ua-stack/stack-core test -Dtest=ClassName#methodName
```

To run multiple test classes:

```bash
mvn -q -pl opc-ua-stack/stack-core test -Dtest=ClassOne,ClassTwo
```

To run tests matching a pattern:

```bash
mvn -q -pl opc-ua-stack/stack-core test -Dtest=*ServiceTest
```

## Code Formatting

This project uses Spotless with Google Java Format for code formatting.

The `spotless:check` goal is bound to the `verify` phase and will fail the build if code is not
properly formatted.

If the build fails due to formatting issues, run the `spotless:apply` goal to automatically format
the code:

```bash
mvn -q spotless:apply
```

## Dependency Source Code

To examine dependency source code, check the `external/src` directory at the project root. This
directory contains unpacked source files from all dependencies, organized by package structure for
easy browsing and searching.

**If the directory doesn't exist or content is missing:**

Run this command from the project root to download and unpack all dependency sources:

```bash
mvn -q generate-resources -Pdownload-external-src
```

This will create the `external/src` directory with sources from all dependencies in a single
top-level location.
