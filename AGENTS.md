# Agent Instructions

## Project Overview

Milo is an open-source OPC UA (IEC 62541) implementation for Java, enabling industrial communication
and IoT integration. It provides a complete client/server SDK for building OPC UA applications.

**Architecture:**

- **opc-ua-stack**: Low-level protocol layer (encoding, transport, security, channels)
- **opc-ua-sdk**: High-level API layer (client and server SDKs built on the stack)

## Key Entry Points

- Client API: `opc-ua-sdk/sdk-client/src/main/java/org/eclipse/milo/opcua/sdk/client/OpcUaClient.java`
- Server API: `opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/OpcUaServer.java`
- Examples: `milo-examples/client-examples/` and `milo-examples/server-examples/`

## Building and Testing

ALWAYS use the `maven-command-runner` agent when running Maven commands. It captures output and
provides better error analysis when builds fail.

| Command                 | Purpose                          |
|-------------------------|----------------------------------|
| `mvn -q clean compile`  | Compile without tests            |
| `mvn -q clean verify`   | Full build with tests and checks |
| `mvn -q spotless:apply` | Fix code formatting issues       |

For running specific tests and module targeting, see `.claude/docs/testing.md`.

## Code Formatting

This project uses Spotless with Google Java Format. The `spotless:check` goal runs during `verify`
and will fail the build if code is not properly formatted. Run `mvn -q spotless:apply` to fix.

## Nullability

Packages should be annotated `@NullMarked` (JSpecify). Assume non-null by default; use `@Nullable`
only for parameters, fields, or return types that genuinely accept or return null.

## Additional Resources

- Testing patterns and module reference: `.claude/docs/testing.md`
- Java coding conventions: `.claude/docs/java-coding-conventions.md`
- Dependency source code: `.claude/docs/dependencies.md`
