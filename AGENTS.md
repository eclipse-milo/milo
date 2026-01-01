# Agent Instructions

## Project Overview

**Tech Stack:** Java 17, Maven, Netty

Milo is an open-source OPC UA (IEC 62541) implementation for Java, enabling
industrial communication and IoT integration. It provides a complete client/server
SDK for building OPC UA applications.

**Architecture:**

- **opc-ua-stack**: Low-level protocol layer (encoding, transport, security, channels)
- **opc-ua-sdk**: High-level API layer — the primary API for most applications

## Key Entry Points

- Client API: `opc-ua-sdk/sdk-client/src/main/java/org/eclipse/milo/opcua/sdk/client/OpcUaClient.java`
- Server API: `opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/OpcUaServer.java`
- Examples: `milo-examples/client-examples/` and `milo-examples/server-examples/`

## Building and Testing

| Command                 | Purpose                                    |
|-------------------------|--------------------------------------------|
| `mvn -q clean compile`  | Compile without tests                      |
| `mvn -q clean verify`   | Full build with tests and formatting check |
| `mvn -q spotless:apply` | Fix code formatting issues                 |

For running specific tests and module targeting, see `.claude/docs/testing.md`.

## Additional Resources

- Testing patterns: `.claude/docs/testing.md`
- Java conventions: `.claude/docs/java-coding-conventions.md`
- Dependencies: `.claude/docs/dependencies.md`

## Verification

Use these steps to verify any completed work. Implementation plans should include these as success criteria.

1. **Format and compile** using the `maven-command-runner` agent:
    - `mvn -q spotless:apply` - Format code
    - `mvn -q clean compile` - Compile (skip tests)

2. **Request code review** from the `preflight` agent, which will:
    - Review changes for correctness, style, and adherence to project conventions
    - Report **APPROVED** or **CHANGES REQUESTED**

Before committing, ensure all verification steps pass and preflight approval is received.

---

> **Build Rule:** ALWAYS use the `maven-command-runner` agent for Maven commands.
