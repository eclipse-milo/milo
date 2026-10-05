# Wiki examples

This module holds the code samples shown on the [Milo Wiki](https://github.com/eclipse-milo/milo/wiki).

- `FirstClient` and `FirstServer` are the complete programs from the first-client and first-server
  tutorials. `FirstProgramsTest` runs them together.
- The `snippets` package holds the other samples. The build compiles them, so an API change that
  breaks a sample fails here. Nothing runs them. Each sample sits between `// snippet:NAME:start`
  and `// snippet:NAME:end` comments.

## Updating the Wiki

Each Wiki code block backed by this module has a marker line directly above it:

```markdown
<!-- snippet: snippets/ClientSnippets.java#read -->
```

Edit samples here, not in the Wiki. Then copy them into a local clone of the Wiki from the
repository root:

```sh
git clone https://github.com/eclipse-milo/milo.wiki.git ../milo.wiki
python3 milo-examples/wiki-examples/tools/sync_snippets.py ../milo.wiki
```

Add `--check` to list stale blocks without changing them. The tool needs Python 3.9 or later and
no other packages.
