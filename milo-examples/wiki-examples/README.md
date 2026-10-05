# Wiki examples

This module holds the code samples shown on the [Milo Wiki](https://github.com/eclipse-milo/milo/wiki).

- `FirstServer` and `FirstClient` are the complete programs from the first-server and first-client
  tutorials. `FirstServer` models a thermostat as one Thermostat Object with a read-only
  Temperature Variable, a writable Setpoint Variable, and an AdjustSetpoint Method. `FirstClient`
  reads Temperature. `FirstProgramsTest` runs them together and checks the thermostat results the
  client pages rely on. The server pages also show regions of `FirstServer`.
- The `snippets` package holds the other samples. The build compiles them, so an API change that
  breaks a sample fails here. Nothing runs them.
- Each named sample sits between `// snippet:NAME:start` and `// snippet:NAME:end` comments.

## Updating the Wiki

Each Wiki code block backed by this module has a marker line directly above it:

```markdown
<!-- snippet: snippets/ClientSnippets.java#read -->
```

A marker with `#NAME` selects one region. A marker without it selects the whole file after its
license header, without the `// snippet:` marker lines.

Edit samples here, not in the Wiki. Then copy them into a local clone of the Wiki from the
repository root:

```sh
git clone https://github.com/eclipse-milo/milo.wiki.git ../milo.wiki
python3 milo-examples/wiki-examples/tools/sync_snippets.py ../milo.wiki
```

Add `--check` to list stale blocks without changing them. The tool needs Python 3.9 or later and
no other packages.
