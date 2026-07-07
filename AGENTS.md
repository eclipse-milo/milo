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

Before running any tests, read `.claude/docs/testing.md` for module targeting flags and
test patterns, and `.claude/docs/test-guidelines.md` for test quality expectations.

## Documentation

Before adding or updating Java/Kotlin documentation, read
`.claude/docs/documentation-guidelines.md`.

When creating a new Java package, substantially changing a package's responsibilities, or touching a
package whose data flow, lifecycle, runtime boundaries, or collaboration model are non-obvious, add
or update a `package-info.java` file. Prefer high-level, onboarding-focused package documentation
that explains how the package fits together, even when the package is not being annotated with
`@NullMarked`.

Keep class and method docs caller-focused. Use package docs for the broader mental model: ownership,
data flow, validation layers, runtime boundaries, invariants, and extension guidance.

## Additional Resources

- Testing patterns: `.claude/docs/testing.md`
- Test quality guidelines: `.claude/docs/test-guidelines.md`
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
