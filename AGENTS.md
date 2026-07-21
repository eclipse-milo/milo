# Project Context

**Tech Stack:** Java 17 managed through `mise`, Maven multi-module

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

| Command                              | Purpose                                    |
|--------------------------------------|--------------------------------------------|
| `mise install`                       | Install pinned Java and Maven tools        |
| `mise trust .mise.toml`              | Trust the local mise config when prompted  |
| `mise exec -- mvn -q clean compile`  | Compile without tests                      |
| `mise exec -- mvn -q clean verify`   | Full build with tests and formatting check |
| `mise exec -- mvn -q spotless:apply` | Fix code formatting issues                 |

Before running any tests, read `.claude/docs/running-tests.md` for module targeting flags and invocation patterns.

## Additional Resources

- Test documentation and quality: `.claude/docs/test-documentation-and-quality-guidelines.md`
- Java conventions: `.claude/docs/java-coding-conventions.md`
- Dependencies: `.claude/docs/dependencies.md`

## Verification

Use these steps to verify any completed work. Implementation plans should include these as success criteria.

1. **Format and compile** using the `maven-command-runner` agent:
    - `mise exec -- mvn -q spotless:apply` - Format code
    - `mise exec -- mvn -q clean compile` - Compile (skip tests)

No separate review-agent gate is required. Reviews are handled manually or by workflow-specific
gates when requested.

Before committing, ensure all verification steps pass.

---

> **Build Rule:** ALWAYS use the `maven-command-runner` agent for Maven commands.
> - Codex: delegate Maven commands to a worker subagent using
>   `.codex/agents/maven-command-runner.md`.
> - Claude: use `.claude/agents/maven-command-runner.md`.
