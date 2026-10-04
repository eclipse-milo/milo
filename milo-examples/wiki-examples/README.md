# Wiki examples

This module contains the complete first-client and first-server programs for the
1.2.0-SNAPSHOT Wiki. It also provides tools for checking those programs as a
standalone consumer. Additional Wiki fragments live in the SDK integration tests,
where they run against local servers and assert service results and cleanup.

## Prerequisites

Run commands from the repository root. Use the Java 17 and Maven versions pinned
in [`.mise.toml`](../../.mise.toml). Install them with `mise install` after trusting
the checkout's configuration. Dependency downloads need access to Maven Central;
the standalone consumer also uses the Sonatype snapshot repository in its POM.

Read the repository's [test guidance](../../.claude/docs/running-tests.md) before
running tests. The commands below are suitable for maintainers. Coding agents
must delegate **every Maven command** through the runner specified in
[AGENTS.md](../../AGENTS.md), such as the
[Codex Maven command runner](../../.codex/agents/maven-command-runner.md).

The Python tools require Python 3.9 or later. `prepare_consumer.py`,
`smoke_first_programs.py`, and `decode_capture.py` use only the standard library.
`inventory_wiki.py` also
requires `markdown-it-py` (used here with version `3.0.0`) and Git when checking
source links. The smoke tool uses a POSIX classpath separator and invokes Java
through `mise`; run it where the repository's mise configuration applies.

## Run the local fixtures

Format and compile before submitting changes, then run the tutorial and Wiki
integration tests. `-am` builds their reactor dependencies. The Surefire flag
allows upstream modules with no matching tests to participate in that build.

```sh
mise exec -- mvn -q spotless:apply
mise exec -- mvn -q clean compile
mise exec -- mvn -q -pl milo-examples/wiki-examples -am test -Dtest=FirstProgramsTest -Dsurefire.failIfNoSpecifiedTests=false
mise exec -- mvn -q -pl opc-ua-sdk/integration-tests -am test '-Dtest=Wiki*Test' -Dsurefire.failIfNoSpecifiedTests=false
```

Inspect Maven's exit status and each target module's `target/surefire-reports`.
Compilation alone does not check service behavior. The commands above select
these documentation fixtures, not the full repository test suite.

[FirstProgramsTest](src/test/java/org/eclipse/milo/examples/wiki/FirstProgramsTest.java)
exercises both tutorial programs. Its
`firstProgramsReadBrowseRejectWritesAndReleaseTheirPort` test reads `21.5`, browses
the Variable, checks `Bad_NotWritable` and `Bad_NodeIdUnknown`, disconnects, shuts
down, and rebinds the server port. Its
`occupiedPortFailsStartupAndCanBeShutDown` test checks exceptional startup and the
subsequent shutdown path.

The integration fixtures are under
[`opc-ua-sdk/integration-tests`](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk).
The class names below are also valid Surefire selectors for a focused rerun.

| Fixture | Behavior checked |
| --- | --- |
| [`WikiClientGuideTest`](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/WikiClientGuideTest.java) | Discovery and connection; typed reads and writes; unknown nodes and type errors; Browse pagination and abandoned cursor release; browse paths; attribute synchronization; Method argument results; data changes; event field order; partitioned Read result order. |
| [`WikiClientHistoryTest`](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/WikiClientHistoryTest.java) | Raw history pagination preserves bad stored values; early release invalidates a cursor; cursors belong to a Session; unsupported operations and empty requests return their respective operation or service errors. |
| [`WikiClientSecurityTest`](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/WikiClientSecurityTest.java) | Explicit server-certificate trust permits an encrypted connection; an empty trust list rejects the same server. |
| [`WikiServerGuidesTest`](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/server/WikiServerGuidesTest.java) | Namespace reads; rejected writes preserve values; revised sampling and permission changes; Method argument validation; instance publication and deletion; event delivery; alarm acknowledgment; explicit certificate trust; bound endpoint discovery. |
| [`WikiServerLifecycleTest`](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/server/WikiServerLifecycleTest.java) | Registered namespaces follow server startup and shutdown; failed startup rolls back earlier participants in reverse order. |
| [`WikiDataTypesTest`](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/WikiDataTypesTest.java) | Discovered structures preserve Matrix contents and dimensions; a registered codec produces the application class; a bad Read status stops decoding. |
| [`WikiEncodingGuidesTest`](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/WikiEncodingGuidesTest.java) | Binary, XML, and JSON structure round trips; unsigned Matrix and timestamp preservation; flat JSON DataValue fields; malformed input statuses; the decoder's acceptance of repeated top-level DataValue `UaType` fields with the last value taking effect. |
| [`WikiFeatureAliasesTest`](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/test/aliases/WikiFeatureAliasesTest.java) | Alias publication, idempotence, version changes, lookup, target reads, deletion, invalid targets, and use after shutdown. |
| [`WikiFeatureReverseTest`](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/WikiFeatureReverseTest.java) | A Session reads over a server-opened connection; target removal and listener cleanup; timeout when no matching server arrives. |
| [`WikiFeatureGdsTest`](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/gds/WikiFeatureGdsTest.java) | Registration reuse and authorization errors; trust-list replacement and malformed-file cleanup; pending certificate issuance and installation without changing the requested key. |
| [`WikiDiagnosticsTest`](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/WikiDiagnosticsTest.java) | Encrypted Read and key-log record format; no default key listener; diagnostics availability, anonymous-user restrictions, and insufficient channel security. |
| [`WikiSourceExamplesTest`](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/WikiSourceExamplesTest.java) | Executes the original Prosys history and legacy dictionary `run()` methods against local service models; checks historical values and errors, actual BSD discovery, wrapper type, and decoded structure fields. |
| [`WikiSourceGdsExampleTest`](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/gds/WikiSourceGdsExampleTest.java) | Executes the original GDS Pull `run()` body; verifies registration reuse, pending/issued requests, installed trust, closed file handles, and signing rejection. |

Most service tests use real OPC UA traffic between local Milo applications. The
history provider is a bounded, in-memory test implementation, not a persistent
historian. The GDS fixture uses `FakeGdsNamespace` to control authorization,
issuance, and malformed responses. Neither local fixtures nor mocks establish
interoperability with a vendor server, GDS, or discovery service. External product
behavior needs separate tests with that product and its configuration.

[`smoke_source_examples.py`](tools/smoke_source_examples.py) runs the sixteen local
source example entry points in separate JVMs. Build `client-examples` and generate
its dependency classpath first, then pass the source root, classpath file, and an
evidence directory to the script. It checks expected output, logged errors, exit,
and port release. The runner's zero exit code alone is insufficient. Each JVM
uses a temporary identity directory, removed afterward; key-log output is
redacted before logs are retained. The external examples above are exercised by
the integration fixtures instead of connecting to a vendor installation.

## Run the first programs as a consumer

[FirstServer](src/main/java/org/eclipse/milo/examples/wiki/FirstServer.java) binds
`opc.tcp://127.0.0.1:12686/wiki` and publishes a read-only Double Variable named
`Temperature`, initially `21.5`, under Objects. It permits anonymous access with
SecurityPolicy None for this loopback tutorial. Production applications need
their own endpoint security, identity, trust, and authorization configuration.

[FirstClient](src/main/java/org/eclipse/milo/examples/wiki/FirstClient.java)
selects that endpoint, connects anonymously, resolves the namespace URI, checks
the Read status and value type, prints `Temperature: 21.5`, and disconnects. The
server stops when it receives a newline. Both entry points release shared stack
resources when they finish. The server accepts an optional port argument; the
client accepts an optional endpoint URL.

The following workflow copies the exact source files and
[consumer POM](consumer/pom.xml) to a new directory, compiles them independently
of the reactor, and runs both entry points. The destination must not already
exist. Run this after reactor `clean` commands, which remove the generated
consumer under this module's `target` directory.

```sh
python3 milo-examples/wiki-examples/tools/prepare_consumer.py milo-examples/wiki-examples/target/consumer
mise exec -- mvn -q -f milo-examples/wiki-examples/target/consumer/pom.xml clean compile dependency:build-classpath -Dmdep.outputFile=target/classpath.txt
python3 milo-examples/wiki-examples/tools/smoke_first_programs.py milo-examples/wiki-examples/target/consumer milo-examples/wiki-examples/target/consumer-smoke
```

Keep loopback TCP port `12686` free. The smoke tool starts the server, waits for its
readiness message, runs the client, checks the output and both exit codes, sends
the server its shutdown newline, and rebinds the port. It records `server.log`,
`client.log`, and `result.json` in the supplied output directory. On failure it
terminates the child server process. It does not invoke Maven or build a missing
classpath file.

The consumer POM pins the Milo BOM to `1.2.0-SNAPSHOT`. That version resolves to
the artifacts available in the selected Maven repositories at build time. A
locally installed snapshot can therefore differ from the published snapshot.
Use a separate, initially empty Maven local repository with
`-Dmaven.repo.local=...` when checking published-consumer dependencies, and retain
the resolved dependency versions alongside the test result. To check a source
revision instead, install that revision's artifacts before building the consumer.

Pass `--wiki /path/to/milo.wiki` to `smoke_first_programs.py` to execute the exact
POSIX shell blocks from the local First-Client and First-Server drafts. Without
that option it constructs the equivalent Java commands from the compiled project.

## Validate migration examples separately

The independent [1.1.7 project](migration-1.1.7/pom.xml) compiles the old APIs
against Milo `1.1.7`. The independent
[1.2.0-SNAPSHOT project](migration-1.2.0-SNAPSHOT/pom.xml) compiles the replacements
against Milo `1.2.0-SNAPSHOT`. They have no reactor parent and are not reactor
modules. Run each in its own Maven invocation so the two API versions never share
a test classpath.

```sh
mise exec -- mvn -q -f milo-examples/wiki-examples/migration-1.1.7/pom.xml clean test
mise exec -- mvn -q -f milo-examples/wiki-examples/migration-1.2.0-SNAPSHOT/pom.xml clean test
```

Both projects require Java 17. The old release resolves from Maven Central. The
new project's snapshot artifacts must already be installed from the intended
source revision, or be available through a snapshot repository in Maven settings.
Its POM does not declare the consumer project's snapshot repository. Separate
local repositories can also isolate dependency provenance when comparing builds.

Each `MigrationExamplesTest` runs
`migrationFragmentsEstablishTrustExposeFolderAndCleanUp`. It executes the
version-specific client identity, certificate-manager, and node-instantiation
fragments; establishes a trusted Basic256Sha256/SignAndEncrypt connection; reads
and browses the published FileType instance and its mandatory Size child; deletes
them and checks `Bad_NodeIdUnknown`. The 1.2 fixture also checks duplicate root
rejection, numeric child NodeId formatting, and identity-builder errors. Generated identities, nodes,
connections, and shared stack resources belong to the fixture. These tests cover
the included migration fragments, not every behavior changed between releases.

`RevocationMigrationTest` generates a local CA, leaf certificate, and signed clear
and revoked CRLs. Four flag combinations check revoked peers, missing CRLs, and
successful validation with a clear CRL. No network CRL distribution points or
vendor services are involved. Run the old test again in a separate JVM with
`-Dtest=RevocationMigrationTest` and
`'-DargLine=--add-opens java.base/sun.security.provider.certpath=ALL-UNNAMED'`
to compare the legacy public-checker fallback with its internal JDK checker.
Preserve the first run's Surefire reports before rerunning the same class.

The paired `JsonMigrationTest` classes also exchange files across the two JVMs.
Run the projects in the order above. The 1.1.7 test generates and decodes real
legacy JSON, explicitly converts JSON ExtensionObject bodies to Binary, and writes
Binary DataValues plus a format marker to `migration-1.1.7/target/json-migration`.
The 1.2 test reads that directory, writes current JSON, decodes it again, and
checks the values, Matrix shape, unsigned range, quality, timestamps, and structure
identity. It also checks direct decoding of legacy Variant/DataValue/ExtensionObject
layouts and the changed null Matrix field result.

Both tests accept `-Dwiki.json.exchange=/absolute/path` to use a different shared
directory. Pass the same path to both invocations. Preserve this directory until
the second process has completed; the generated files contain only local fixture
data. Remove it after inspection. These cases exercise standard types, not
application-specific optional structures, unions, or vendor models.

## Capture an encrypted diagnostic exchange

`WikiDiagnosticsTest#encryptedReadProducesUsableKeyLog` runs a local
Basic256Sha256/SignAndEncrypt Read. It disconnects the client before closing the
key-log writer and checks the resulting record format without printing keys.
Without properties, it selects a test port and uses a temporary key-log file.

For an external capture, select that single method and supply `-Dwiki.port=12689`
and `-Dwiki.keylog=/absolute/path/to/new-opcua.keys` to the integration-test Maven
command. Replace the example path with a private writable location. Its parent
directory must exist and the file itself must not exist. Start a loopback packet
capture for that port before running the test, and stop it afterward. Capturing
traffic requires the appropriate OS privileges and a capture tool such as
`tcpdump`; interpreting it requires Wireshark or `tshark` with OPC UA decryption
support.

The test creates the traffic and key log; it does not capture packets or invoke
an analyzer. A passing test establishes the encrypted service result and key-log
format. Use [`decode_capture.py`](tools/decode_capture.py) for the independent
analyzer check. With `tshark` on PATH, substitute the actual private file paths:

```sh
python3 milo-examples/wiki-examples/tools/decode_capture.py --capture /private/capture/opcua.pcap --keylog /private/capture/opcua.keys --port 12689
```

The checker was exercised with tshark 4.4.19. It sets `opcua.tcp.port` to the server
port and `opcua.debug_file` to the key log. In that release the GUI labels the
latter preference **OPCUA debug file**. Decode As alone does not set the port
preference used to select server-to-client keys; requests may decode while
responses appear malformed or report ServiceId 0.

The checker requires a decrypted CurrentTime ReadRequest and matching
ReadResponse, Good service and operation statuses, a DateTime result, and no
malformed OPC UA packets. An omitted DataValue StatusCode means Good. It repeats
the decode without keys and requires the same exchange to remain encrypted.
It prints a JSON result with analyzer version and commands, without key material.
A missing key file fails this check. This targets the single diagnostic exchange,
not arbitrary captures with multiple CurrentTime reads.

To run an analyzer in a container, append
`--tshark-prefix docker exec CONTAINER_NAME tshark` and use capture/key paths as
seen inside that container. The tool neither starts containers nor captures
traffic. Key logs permit decryption of the associated traffic. Restrict access to
them and delete them with the captures after use.

## Check Wiki examples and links

[`inventory_wiki.py`](tools/inventory_wiki.py) parses fenced and indented code
blocks, records their SHA-256 hashes and surrounding headings, and checks local
Wiki page and anchor targets. Supply a separate Wiki checkout as its positional
argument. Add `--source .` from the Milo repository root to check whether pinned
GitHub source paths exist in local Git objects.

The tool writes JSON to standard output and returns a nonzero status for link
errors. Install its parser dependency in a Python environment with
`python3 -m pip install markdown-it-py==3.0.0`, then invoke
`python3 milo-examples/wiki-examples/tools/inventory_wiki.py /path/to/milo.wiki --source .`
with the actual checkout path. Keep generated output outside the Wiki checkout.

This is a local structural check. It does not execute snippets, fetch remote
links, prove that a source revision was published, or reproduce GitHub rendering.
Its heading-slug calculation is approximate. Check the rendered Wiki and external
links separately. When a displayed fragment changes, keep its fixture logic in
sync and rerun the tests that assert its behavior, errors, and resource cleanup.
