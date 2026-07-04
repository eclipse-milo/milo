# Server Integration

This page covers `milo-sdk-pubsub-server`, the module that connects a PubSub runtime to an
`OpcUaServer` address space. Read it if you want to publish live node values without writing a
data source, or have received DataSets written into variable nodes. It assumes the basics from
[getting started](getting-started.md); the [configuration model](configuration.md) page is useful
as a reference alongside it.

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>milo-sdk-pubsub-server</artifactId>
</dependency>
```

The artifact is managed by `milo-bom` and depends on `milo-sdk-pubsub` and `milo-sdk-server`.

A standalone `PubSubService` knows nothing about servers: you bind a `PublishedDataSetSource` to
feed each dataset and a `DataSetListener` to consume what arrives. `ServerPubSub` wires both ends
to the address space for you:

- a published dataset whose fields all point at nodes is fed automatically from live node values
  (the auto-source) — no source code to write;
- a reader configured with TargetVariables writes received field values into variable nodes;
- optionally, the PubSub configuration and live component states are exposed as an information
  model under the server's ns0 `PublishSubscribe` object — one that tracks reconfiguration;
- optionally, OPC UA clients can reconfigure the runtime remotely (the Part 14 configuration
  file model plus per-component Enable/Disable methods), the Part 14 §9.1.11 diagnostics are
  served in the information model, and state changes and send failures are bridged to OPC UA
  events.

One honest limit before you design around this module: `ServerPubSub` builds its runtime
configuration internally and has no hook to register transport providers, so it is UDP/UADP only
in this version. See [what the server integration does not do](#what-the-server-integration-does-not-do).

## Attach and lifecycle

`ServerPubSub.attach(OpcUaServer, PubSubConfig)` (or the three-argument overload taking
`ServerPubSubOptions`) returns a `ServerPubSub` that is not yet started. Start it with
`startup()`; stop it with `close()` (or the async `shutdown()`). From
`ServerSourcePublisherExample`:

```java
// Because every field of "demo-nodes" is a NodeFieldAddress, attach() auto-binds an
// address-space source for it: each publish cycle pulls a fresh snapshot of the live
// node values. attach() also eagerly resolves every NodeFieldAddress against the
// server's NamespaceTable, failing fast on an unresolvable namespace.
ServerPubSub serverPubSub = ServerPubSub.attach(server, config);

serverPubSub.startup().get();
```

Attach validates eagerly: every `NodeFieldAddress` in the configuration — dataset field sources
and TargetVariables targets alike — is resolved against the server's `NamespaceTable`, and
TargetVariables index ranges are parsed. A failure throws `PubSubConfigValidationException`
naming the offending element. Node *existence* is deliberately not checked at attach time: a
field whose namespace resolves but whose node does not exist publishes `Bad_NodeIdUnknown` for
that field at runtime instead.

### PubSub does not need a started server

`ServerPubSub` is decoupled from the server's client-facing lifecycle. Attach is legal any time
after the `OpcUaServer` constructor returns, and the PubSub runtime operates independently of the
server's endpoints and transports. Both server examples exploit this and never start their
server at all:

```java
// An endpoint-less OpcUaServer that is never started: the address space (including ns0)
// loads in the constructor, which is all ServerPubSub needs. Attaching to a real, started
// server works exactly the same way.
OpcUaServerConfig serverConfig =
    OpcUaServerConfig.builder()
        .setApplicationUri("urn:eclipse:milo:examples:pubsub:server-source")
        .setApplicationName(LocalizedText.english("PubSub server-source example"))
        .setProductUri("urn:eclipse:milo:examples:pubsub")
        .build();

var server =
    new OpcUaServer(
        serverConfig,
        transportProfile -> {
          throw new IllegalStateException(
              "this example server has no transports: " + transportProfile);
        });
```

On a real, started server everything behaves identically. Two lifecycle details are worth
knowing either way:

- The ns0 address space loads in the `OpcUaServer` constructor and takes a few seconds. When you
  run the examples, the gap between launching and the first log line is mostly this, not PubSub.
- Do not call `server.shutdown()` on a server that was never started — it throws. The example's
  teardown reflects this:

```java
updater.shutdownNow();
serverPubSub.close();
namespace.shutdown();
// the OpcUaServer was never started, so there is no server.shutdown() to call
```

The caller owns shutdown ordering. The runtime borrows the server's executors, so close the
`ServerPubSub` before shutting down a started server.

## Publishing node values: the auto-source

`ServerSourcePublisherExample` publishes three variable nodes (Temperature, Pressure, Counter)
over UADP/UDP while a background task updates them every 500 ms. The interesting part is what it
does *not* contain: there is no `PublishedDataSetSource` anywhere in the file.

The trigger is the shape of the dataset. At attach time, a published dataset is auto-bound to an
address-space source if and only if its field list is non-empty and **every** field's source is a
`NodeFieldAddress`:

```java
// Field order is wire order, and ALL fields use NodeFieldAddress: a dataset with mixed or
// key-only field addresses would not be auto-bound and would need an explicit source.
PublishedDataSetConfig dataSet =
    PublishedDataSetConfig.builder("demo-nodes")
        .field(
            FieldDefinition.builder("Temperature")
                .source(nodeAddress(server, temperatureNodeId))
                .dataType(NodeIds.Double)
                .build())
        .field(
            FieldDefinition.builder("Pressure")
                .source(nodeAddress(server, pressureNodeId))
                .dataType(NodeIds.Double)
                .build())
        .field(
            FieldDefinition.builder("Counter")
                .source(nodeAddress(server, counterNodeId))
                .dataType(NodeIds.Int32)
                .build())
        .build();
```

```java
private static NodeFieldAddress nodeAddress(OpcUaServer server, NodeId nodeId) {
  return NodeFieldAddress.of(nodeId, AttributeId.Value, server.getNamespaceTable());
}
```

The auto-bound source pulls a fresh snapshot of the node values once per publish cycle (one
batched internal read), so writing to the nodes — `node.setValue(...)` — is all it takes to
change what goes on the wire. The example does this from a background task; nothing about the
publish cycle needs to know.

The rule's edges, precisely:

- A dataset that mixes `NodeFieldAddress` fields with key-only fields, or has only key fields, is
  **not** auto-bound. If no source is bound for it, `startup()` fails with
  `Bad_ConfigurationError` naming the unbound dataset.
- A source you supply yourself via `ServerPubSubOptions.builder().bindings(...)` always wins over
  the automatic binding, even for an all-`NodeFieldAddress` dataset.

The subscribing side of this pair, `ServerSourceSubscriberExample`, is a plain standalone
`PubSubService` — no server involved — whose `DataSetMetaDataConfig` mirrors the publisher's
dataset. Field names, order, and types must match; nothing else needs pinning, because
`PublishedDataSetConfig` and `DataSetMetaDataConfig` both default the configuration version to
(1, 1), so the reader's version check passes without you setting versions or field UUIDs on
either side.

### Run it

Build the examples module once, then run each program from the repository root in its own
terminal:

```sh
mvn -q -pl milo-examples/pubsub-examples -am install -DskipTests
```

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
    -Dexec.mainClass=org.eclipse.milo.examples.pubsub.server.ServerSourcePublisherExample
```

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
    -Dexec.mainClass=org.eclipse.milo.examples.pubsub.server.ServerSourceSubscriberExample
```

Do not add `-q` to the `exec:java` commands. The example runs inside Maven's JVM, where SLF4J
binds to Maven's own logger, and `-q` caps that logger at errors — the example runs fine but
prints nothing, which looks exactly like a hang. Keep `-q` on the build step only.

The publisher logs a startup line and then an update every ~5 seconds:

```
publishing "demo-nodes" (publisherId=4001, writerGroupId=1, dataSetWriterId=1) to 127.0.0.1:15120 every 500 ms
update #10: Temperature=24.33, Pressure=1015.42, Counter=10
```

The subscriber logs one line per delivered DataSet, roughly every 500 ms:

```
[received] dataSet=demo-nodes publisherId=4001 writerGroupId=1 dataSetWriterId=1 Temperature=22.94, Pressure=1014.02, Counter=24
[received] dataSet=demo-nodes publisherId=4001 writerGroupId=1 dataSetWriterId=1 Temperature=22.5, Pressure=1013.9, Counter=25
```

The continuously changing values are the point: the auto-source reads the live nodes each cycle,
not a captured snapshot. Both connections pin `discoveryAddress` to distinct loopback ports
(15121 and 15122) — without an explicit discovery address the engine binds UDP 4840 and joins
multicast group 224.0.2.14, and two processes on one host cannot share a discovery port.

## Writing received values into nodes: TargetVariables

The reverse direction: `ServerTargetSubscriberExample` is an `OpcUaServer` whose Temperature and
Counter nodes are written by a PubSub reader, fed by the standalone
`ServerTargetPublisherExample`.

The mapping is Part 14 TargetVariables, configured on the reader with
`DataSetReaderConfig.subscribedDataSet(TargetVariablesConfig)`. Select received fields with a
`FieldSelector` (`byName`, `byId`, or `byIndex`) and point each at a node:

```java
// Part 14 §6.2.11.1 TargetVariables: select received fields by name and write them to the
// Value attribute of server nodes. StatusCodes and source timestamps received on the wire
// pass through to the target nodes (Table 80).
TargetVariablesConfig targetVariables =
    TargetVariablesConfig.builder()
        .map(
            FieldSelector.byName("temperature"),
            TargetVariableConfig.builder()
                .target(
                    NodeFieldAddress.of(
                        temperatureNodeId, AttributeId.Value, server.getNamespaceTable()))
                .build())
        .map(
            FieldSelector.byName("counter"),
            TargetVariableConfig.builder()
                .target(
                    NodeFieldAddress.of(
                        counterNodeId, AttributeId.Value, server.getNamespaceTable()))
                .build())
        .build();
```

```java
.readerGroup(
    ReaderGroupConfig.builder("readers")
        .dataSetReader(
            DataSetReaderConfig.builder("reader")
                .publisherId(PUBLISHER_ID)
                .writerGroupId(WRITER_GROUP_ID)
                .dataSetWriterId(DATA_SET_WRITER_ID)
                .dataSetMetaData(metaData)
                .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                .subscribedDataSet(targetVariables)
                .build())
        .build())
```

Note the connection has no `.publisherId(...)` — a PublisherId is only required on connections
that have writer groups, and this side only reads.

### Target nodes should allow nulls

The Part 14 state-change rows (reader Disabled, Error, and so on) write status-only updates whose
value is a null Variant. A Milo variable node rejects null writes with `Bad_TypeMismatch` unless
you allow them, so create target nodes accordingly (from the example's `DemoNamespace`):

```java
// The Part 14 Table 80 Disabled-handling and state-change rows write null Variants;
// without allowNulls the server's AttributeWriter rejects them with Bad_TypeMismatch.
node.setAllowNulls(true);
```

Target writes also respect access level: a target node without `CurrentWrite` fails every write
with `Bad_NotWritable` (counted, see below) and is never written.

### Status and timestamp pass through

If the publisher's writer encodes fields as full DataValues, the received StatusCode and source
timestamp are written into the target node along with the value. `ServerTargetPublisherExample`
opts in with its field content mask:

```java
private static final DataSetFieldContentMask FIELD_MASK =
    DataSetFieldContentMask.of(
        DataSetFieldContentMask.Field.StatusCode, DataSetFieldContentMask.Field.SourceTimestamp);
```

With this mask the source timestamps you read off the target nodes are the publisher's — the time
the value was sampled, not the time it was written into the subscriber's address space.

### Monitoring target writes

Failed target writes never stop the flow; they are counted per target.
`ServerPubSub.targetWriteErrors()` returns a map keyed `"<reader-path>/<targetNodeId>"` to error
counts. The example polls it once a second:

```java
Map<String, Long> targetWriteErrors = serverPubSub.targetWriteErrors();
if (!targetWriteErrors.isEmpty()) {
  logger.warn("targetWriteErrors: {}", targetWriteErrors);
}
```

### Watching both layers at once

`ServerPubSub.runtime()` exposes the full `PubSubService`, so everything from the standalone API —
listeners, diagnostics, reconfiguration — is available alongside the server integration. What it
returns is a *managed* view of the runtime: `reconfigure` and `update` additionally validate the
new configuration against the attach-time rules (every `NodeFieldAddress` must resolve, every
TargetVariables index range must parse, the config must map to its wire form) and throw
`PubSubConfigValidationException` before anything is applied; after a successful apply, the
attachment re-synchronizes itself before the call returns — the exposed information model rebuilds
the affected subtrees, TargetVariables writers are re-derived for affected readers, added or
changed fully node-addressed datasets are auto-bound (unless you bound a source yourself, which
always wins), the served SKS groups are refreshed, and the configuration is saved to a
configured store. In other words: reconfiguring through `runtime()` no longer desyncs the server
integration. (One asymmetry to know: a dataset changed *away* from fully node-addressed keeps its
earlier automatic source binding — the engine has no unbind — unlike an identical attach-time
configuration, which would never auto-bind it.)

The example uses `runtime()` to observe the same data at two layers, which is a useful pattern
when bringing up any TargetVariables configuration:

```java
// ServerPubSub.runtime() exposes the full PubSubService; a DataSetListener shows DataSets
// arriving on the wire, independent of the TargetVariables writes into the address space.
serverPubSub.runtime().addDataSetListener(this::onDataSetReceived);
```

The listener logs a `[received]` line for each DataSet arriving on the wire; a scheduled task
reads the target nodes once a second and logs a `[node]` line. If `[received]` lines appear but
`[node]` values never change, the problem is in your TargetVariables mapping (selector names,
target addresses, writability), not the network.

### Run it

Start the subscriber **first**. The subscribing side binds the UDP data port, and anything
published before that bind exists is silently lost — no error is reported anywhere. (For a
continuously publishing pair this only costs the earliest samples, but make it a habit; it
matters whenever the first messages carry meaning.)

```sh
mvn -q -pl milo-examples/pubsub-examples -am install -DskipTests
```

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
    -Dexec.mainClass=org.eclipse.milo.examples.pubsub.server.ServerTargetSubscriberExample
```

Wait for `ServerPubSub started, reader listening on opc.udp://127.0.0.1:15130` (about ten
seconds — Maven and JVM startup plus the ns0 load), then in a second terminal:

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
    -Dexec.mainClass=org.eclipse.milo.examples.pubsub.server.ServerTargetPublisherExample
```

Again, no `-q` on the `exec:java` steps. The subscriber terminal then shows both views
interleaved — about two `[received]` lines per second and one `[node]` line per second:

```
[received] publisherId=UInt16Id[value=5001] writerGroupId=1 dataSetWriterId=1 fields: temperature=24.817790927085966, counter=13
[node] Temperature=24.316046833244368 (status=StatusCode[name=Good, ...], sourceTime=...) Counter=21 (status=Good, ...)
```

The `[node]` lines show Good status and the publisher's source timestamps landing on the nodes —
the pass-through described above, observable end to end.

## The information model

Opt in with `ServerPubSubOptions.builder().exposeInformationModel(true)` (default `false`). At
`startup()`, `ServerPubSub` populates the ns0 `PublishSubscribe` object and grafts a node tree
describing the configuration onto it: connections, writer and reader groups, writers, readers,
and published datasets, with their addresses, message settings, and properties.

What you get, and the edges:

- All variables are read-only, and the tree is *live in both directions*: per-component
  `Status/State` variables track the runtime state machine, so a browsing client sees readers
  go `Operational` and components go `Disabled` as it happens — and every successful
  reconfiguration through `runtime()` (or a remote `CloseAndUpdate`) incrementally rebuilds the
  config-derived subtrees it affected, so the model always describes the running configuration.
  Node identities are deterministic string NodeIds derived from component name paths, stable
  across rebuilds and server restarts for unchanged names.
- The ns0 `ConfigurationVersion` property serves a real Part 14 VersionTime, advanced once per
  applied configuration change; the `PubSubCapablities` (the NodeSet's own typo) properties are
  populated — every `Max*` is 0, meaning no limit, and the SKS capability flags reflect the
  [SKS server face](#serving-security-keys-the-sks-server-face) option.
- Methods appear only with the corresponding opt-ins: `Enable`/`Disable` on every component
  Status object with [`allowRemoteConfiguration(true)`](#remote-configuration), and `Reset` on
  every Diagnostics object with [`diagnosticsEnabled(true)`](#exposing-diagnostics). Without
  them the tree is method-free.
- `SupportedTransportProfiles` advertises the UDP-UADP profile only, matching what the server
  integration actually runs.
- The remaining ns0 method nodes — the deprecated imperative configuration methods
  (`AddConnection`, `RemoveConnection`, ...) and the SKS — Security Key Service — management
  methods — are left unbacked; a client calling them gets `Bad_NotImplemented`. `GetSecurityKeys`
  is backed when the [SKS server face](#serving-security-keys-the-sks-server-face) is enabled,
  and the `PubSubConfiguration` file object is backed when
  [remote configuration](#remote-configuration) is enabled — both work whether or not the
  information model is exposed. The ns0 root Status object never gets an `Enable`/`Disable`
  pair (they are Optional members the Foundation NodeSet omits, and ns0 method dispatch cannot
  reach handlers for them).

## Remote configuration

Opt in with `ServerPubSubOptions.builder().allowRemoteConfiguration(true)` (default `false`).
This is the Part 14 *file model*: instead of per-element configuration methods, a client reads
and rewrites the whole configuration as a binary configuration file through the ns0
`PublishSubscribe.PubSubConfiguration` object (`i=25451`), plus per-component `Enable`/`Disable`
methods. When the option is off, ns0 stays untouched: the file object's methods keep the loader
default `Bad_NotImplemented` and its property values stay null.

### The configuration file object

Remote-configuration support backs the eight Part 14 §9.1.3.7 methods on `i=25451` — the six FileType methods
(`Open`, `Close`, `Read`, `Write`, `GetPosition`, `SetPosition`) plus `ReserveIds` and
`CloseAndUpdate` — and maintains the file properties, including three the ns0 loader does not
instantiate (`MimeType` = `application/opcua+uabinary`, `MaxByteStringLength`, and
`LastModifiedTime`, created at startup and removed at shutdown). It works whether or not the
information model is exposed.

File semantics follow the Part 20 FileType contract with the Part 14 overlay:

- `Open` accepts modes `0x01` (read), `0x03` (read+write), and `0x06` (write+erase-existing —
  the recommended full-rewrite mode) and nothing else (`Bad_InvalidArgument`). Reading serves a
  snapshot of the current configuration, encoded exactly as
  [`PubSubConfigFiles`](configuration.md#binary-configuration-files-pubsubconfigfiles) encodes
  it, materialized at `Open`. Writing is exclusive: one write handle at a time, and readers are
  locked out while it exists.
- Handles are per-session: a handle used from another session answers `Bad_InvalidArgument`,
  and a closing session's handles are evicted. Reads are clamped to `MaxByteStringLength`
  (1 MiB by default — clients chunk longer files across multiple `Read` calls).
- `Size` always serves the real encoded length of the *current* configuration (it does not
  track an open write buffer); `LastModifiedTime` starts at face startup and then tracks each
  applied change, the same instant the ns0 `ConfigurationVersion` advances.

`CloseAndUpdate` closes a write handle and applies the uploaded file as element operations: each
`PubSubConfigurationRefDataType` names an Add, Match, Add+Match, Modify, or Remove of one
configuration element, resolved against the live configuration by name. Every reference is
individually evaluated and answered in a full-length `ReferencesResults` array — the
method-level result is `Good` even when element operations fail, so **check the per-reference
results** — and `RequireCompleteUpdate` selects atomic (all-or-nothing) versus partial
(apply-the-survivors) mode. Whatever survives is applied as *one* reconfiguration through the
managed runtime, with `DISABLE_AFFECTED` restart semantics: affected components visibly bounce
through the state machine, the [post-apply re-synchronization](#watching-both-layers-at-once)
runs, the ns0 `ConfigurationVersion` advances, and the result is
[saved](#persisting-configuration). Added elements with null names or zero ids get
server-assigned values (returned in `ConfigurationValues`), and `ConfigurationObjects` returns
the information-model NodeIds of applied elements when the model is exposed. SecurityGroup
references are supported (guarded by the authorizer's `checkSksAdmin`); PushTarget references
are rejected per-reference — push targets are not modeled.

`ReserveIds` grants WriterGroupIds/DataSetWriterIds from the server-assigned range
(`0x8000`–`0xFFFF`) and answers the transport profile's `DefaultPublisherId` (§6.2.7.1 — a
MAC+port-derived UInt64, or its string form for the JSON profile). Reservations are per-session:
valid while the session lives, honored by `CloseAndUpdate` auto-assignment, consumed on use,
and cross-session exclusive — an explicit server-range id another session holds reserved is
rejected per-reference, whether an `ElementAdd` supplies it or an `ElementModify` introduces it.
`Bad_ResourceUnavailable` means genuine id-space exhaustion only — the advertised
`MaxWriterGroups`/`MaxDataSetWriters` capabilities stay 0 (no limit). All three Part 14 profile
URIs Milo's config model can express are accepted, deliberately independent of which transport
providers the attachment can actually run (reservations are configuration-tool currency; see
the UDP-only note [below](#what-the-server-integration-does-not-do)).

### Enable and Disable

With the information model exposed, every component Status object gains `Enable` and `Disable`
methods (Part 14 §9.1.10). The state rules: `Enable` is rejected with `Bad_InvalidState` unless
the component is currently `Disabled`; `Disable` is rejected with `Bad_InvalidState` only when
it already is. Enable/Disable are *not* configuration mutations — they are never saved to a
configuration store, exactly like `enable(handle)`/`disable(handle)` on the runtime, which is
what the handlers call. (The ns0 *root* Status object gets no Enable/Disable pair; see the
information-model edges above.)

### Authorization

Every remote-configuration handler — all eight file methods, including the read-side ones
(a caller who may not configure may not read the configuration file either), Enable/Disable,
and the diagnostics `Reset` methods — consults one effective `PubSubMethodAuthorizer` per
attachment: the instance you supply via `ServerPubSubOptions.methodAuthorizer(...)`, or the
default posture. The default: with a `RoleMapper` configured, the well-known roles apply
(ConfigureAdmin-or-equivalent for configuration); without one, callers are allowed — the
explicit `allowRemoteConfiguration(true)` opt-in is what justifies that. Session-less
invocations are uniformly rejected with `Bad_UserAccessDenied`, and no channel-security minimum
is imposed (the spec pins none for these methods; run encrypted endpoints if the configuration
is sensitive). SecurityGroup-touching `CloseAndUpdate` references additionally consult
`checkSksAdmin`.

Interop note: no open-source OPC UA stack implements the Part 14 file model as a client, so
remote-configuration interop testing is manual, against commercial tooling — see
[limitations and interop](limitations-and-interop.md#interop-status).

## Exposing diagnostics

Opt in with `ServerPubSubOptions.builder().diagnosticsEnabled(true)` — effective only together
with `exposeInformationModel(true)` (without it the option is a WARN-logged no-op). This serves
the Part 14 §9.1.11 diagnostics model: the loader-built ns0 root `Diagnostics` object
(`i=17409`) is backed with live values and its `Reset` method (`i=17421`) gains a handler, and
every exposed connection, group, writer, and reader gains a `Diagnostics` object computed
per read from the runtime's [diagnostics](operations.md#diagnostics) — following
reconfiguration rebuilds like every other part of the model.

The postures worth knowing: counters are served as UInt32 and saturate at `0xFFFFFFFF` (the
SourceTimestamp keeps advancing at the cap); `DiagnosticsLevel` is read-only `Basic` with no
level-switching machinery (a conformant simplification — the Advanced/Info-level rows are
served anyway); the secured-group LiveValues (`SecurityTokenID`, `TimeToNextTokenID`), the
writer `MessageSequenceNumber`, and Major/MinorVersion rows are backed; `Reset` methods are
gated by the authorizer's `checkConfigure` and delegate per-object to the runtime's
`reset(path)` — see [the operations page](operations.md#the-part-14-9111-diagnostics-exposure)
for the semantics. Engine counters remain available through `runtime().diagnostics()` either
way.

## Status events

Opt in with `ServerPubSubOptions.builder().statusEventsEnabled(true)` (default `false`),
independent of both other options. `ServerPubSub` then bridges the runtime's feeds to OPC UA
events fired with Server-object semantics — clients receive them through an event monitored
item on the Server object (`i=2253`):

- every state change of a connection, group, writer, or reader emits a `PubSubStatusEventType`
  (`i=15535`) event carrying the component's information-model NodeIds and new state;
- a NetworkMessage transmission failure emits a `PubSubCommunicationFailureEventType`
  (`i=15563`, a subtype) event additionally carrying the failure's status code — the *first*
  failure per outage episode only, re-armed when the component (or an ancestor) returns to
  `Operational`, so a writer failing every publish cycle emits one event, not a storm.

Informational transitions carry severity 100; Error entries and communication failures carry
severity 500. A client filters with an `OfType(i=15535)` where-clause to receive both (the base
type is abstract but reportable — verify your client tolerates abstract-typed notifications).
Shutdown and reconfigure-removal transitions emit nothing. Two operational notes: the event
fields carry the deterministic information-model NodeIds whether or not the model is exposed
(without it they reference non-browsable NodeIds), and firing events requires a *started*
server — call `server.startup()` before `ServerPubSub.startup()`, or events are dropped with a
WARN.

## Serving security keys: the SKS server face

`ServerPubSubOptions.builder().securityKeyServerEnabled(true)` (default `false`) turns the
attached server into a Security Key Service for the `SecurityGroupConfig`s of the attach-time
configuration: at `startup()` a `GetSecurityKeys` handler is attached to the ns0 method node, keys
are generated and rotated per each group's `keyLifeTime`, and the ns0 `PubSubCapablities` flags
advertise pull support. Calls require a SignAndEncrypt channel, and authorization defaults to a
posture that fails closed on groups carrying explicit `rolePermissions` — replaceable wholesale
via `ServerPubSubOptions.methodAuthorizer(...)`.

The served groups follow the configuration: on every successful apply through `runtime()` or a
remote `CloseAndUpdate`, the SKS helper refreshes its group set — retained groups keep serving their
keys undisturbed, while a group whose `SecurityPolicyUri` or `KeyLifetime` changed has its
existing keys invalidated and regenerated (Part 14 §6.2.12.2; the engine independently restarts
referencing groups so consumers re-fetch).

The full treatment — key generation and rotation arithmetic, the authorization posture table, and
the limits (no push distribution, no SKS management methods) — is on the
[message security and SKS page](message-security-and-sks.md#the-sks-server-face). Note that the
face serves keys regardless of whether this server's own PubSub runtime uses them: a UDP-only
`ServerPubSub` can act as the SKS for secured publishers and subscribers running elsewhere.

## Persisting configuration

`ServerPubSubOptions.builder().configurationStore(...)` accepts a `PubSubConfigurationStore`, a
two-method interface you implement — no implementation ships with the SDK:

```java
public interface PubSubConfigurationStore {

  @Nullable PubSubConfiguration2DataType load();

  void save(PubSubConfiguration2DataType value);
}
```

The semantics at attach are load-wins, save-once: if `load()` returns a non-null configuration,
it wins and the config passed to `attach` is ignored; if `load()` returns null, the attach config
is used and saved exactly once via `save()`. A `load` failure propagates out of `attach`.

After attach, **every successful configuration apply is saved**: an owner reconfigure through
`runtime()` and a remote `CloseAndUpdate` alike. The saved wire form carries the current
ConfigurationVersion, so a store-loaded configuration resumes with the version it was saved
under. Enable/Disable calls — local or remote — are not configuration mutations and are never
saved, so a stored configuration preserves the *configured* enabled flags, not the momentary
runtime states.

A `save` failure never fails the operation that triggered it: the change was already applied and
its results reported. The failure is WARN-logged, surfaced via
`ServerPubSub.lastConfigurationSaveError()` (a nullable `Exception`, cleared by the next
successful save), and effectively retried at the next successful apply. Poll that accessor if
persistence matters to you — nothing else will tell you the store is failing.

## What the server integration does not do

These are hard limits of this version, listed with the exact failure mode so you can recognize
them:

- **MQTT and custom transports.** `ServerPubSub` cannot register transport providers, so only the
  built-in UDP transport (UADP mapping) is available. An enabled MQTT connection in the attached
  config fails `startup()` with `Bad_ConfigurationError` ("no TransportProvider supports
  connection ..."); a disabled MQTT connection passes startup but is rejected with the same
  status if you later enable it. For PubSub over MQTT, use the standalone `PubSubService` with
  `MqttTransportProvider` registered — and bridge to the address space yourself if needed. One
  deliberate asymmetry: [`ReserveIds`](#remote-configuration) accepts all three Part 14 profile
  URIs even though only UDP-UADP can run here — reservations are configuration currency, and
  provider presence is enforced when a configuration is applied.
- **The deprecated imperative configuration methods.** Remote configuration is the
  [file model](#remote-configuration); the per-element ns0 methods (`AddConnection`,
  `RemoveConnection`, and friends) and the SKS management methods remain unbacked and answer
  `Bad_NotImplemented`. The dataset-level binding methods (`CreateTargetVariables`,
  `CreateDataSetMirror`, ...) are likewise not implemented.
- **Standalone SubscribedDataSet references.** A reader whose SubscribedDataSet is a
  `StandaloneSubscribedDataSetRef` carrying TargetVariables gets no automatic writes: attach
  succeeds, the targets are still validated, a WARN is logged, and nothing is written at runtime.
  Configure the `TargetVariablesConfig` directly on the reader instead, as shown above.
