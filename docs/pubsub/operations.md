# Operations

This page covers running PubSub over time: the component state machine, the receive-timeout
watchdog, sequence numbers and duplicate handling, live reconfiguration, diagnostics, and the
threading and shutdown rules that every long-running or self-terminating program needs. It
assumes you already have a publisher and subscriber exchanging data — if not, start with
[getting started](getting-started.md) and the [configuration model](configuration.md).

The runnable anchor for this page is `ReconfigureExample`
(`milo-examples/pubsub-examples`, package `org.eclipse.milo.examples.pubsub`): a single
self-terminating JVM that hosts a publisher and a subscriber over UDP loopback, publishes at
1000 ms for ~5 seconds, reconfigures the live publisher to 250 ms, runs ~5 more seconds, and
exits after printing per-phase counts. Run it from the repository root:

```sh
mvn -q -pl milo-examples/pubsub-examples -am install -DskipTests

mvn -pl milo-examples/pubsub-examples exec:java \
    -Dexec.mainClass=org.eclipse.milo.examples.pubsub.ReconfigureExample
```

Do not add `-q` to the `exec:java` step. `exec:java` runs the example inside the Maven JVM,
where SLF4J binds to Maven's own logger, and `-q` caps that logger at ERROR — the example runs
fine but prints nothing. Keep `-q` on the build step only.

## The component state machine

A `PubSubService` is a tree of components: connections contain writer groups and reader groups,
which contain DataSetWriters and DataSetReaders. Each component is always in one of five
`PubSubState` values:

| State | Meaning |
|---|---|
| `Disabled` | the component's enabled flag is off |
| `Paused` | enabled, but its parent is not `Operational` |
| `PreOperational` | activated, startup not yet complete |
| `Operational` | running |
| `Error` | a failure occurred; the component stays in the tree and can recover |

Only the enabled flag is externally settable; everything else is computed. A component can be
`Operational` only while its parent is, and a parent in `Error` counts as not operational, so
its children sit in `Paused` until it recovers.

Most components pass through `PreOperational` instantaneously on the way to `Operational`.
There are two exceptions. A [secured](message-security-and-sks.md) writer or reader group stays
`PreOperational` until its first key fetch succeeds (its children wait in `Paused`), and fails
into `Error` if no fetch succeeds within twice the configured key lifetime. And a DataSetReader
stays `PreOperational` until it accepts a first key
frame (including a zero-field heartbeat key frame — a key frame with no fields, which some
stacks send as a liveness signal) or an event DataSetMessage (Milo decodes event messages from
peers but never publishes them; see [events](limitations-and-interop.md#events)). A keep-alive
resets the receive watchdog but is never delivered as a data event; a delta frame — a partial
update carrying only changed fields, which Milo decodes from any peer and emits when a writer's
`keyFrameCount` exceeds 1 (see [delta frames](limitations-and-interop.md#delta-frames)) — is
delivered and counted; neither completes reader startup. A `PreOperational` reader is already
listening — the state means "no full baseline yet", not "deaf".

Look up components by the names used in your config via `PubSubService.components()`, which has
`connection`, `writerGroup`, `dataSetWriter`, `readerGroup`, and `dataSetReader` lookups, each
returning `Optional<PubSubHandle>`. Pass a handle to `enable(handle)`, `disable(handle)`, or the
read-only `state(handle)`. Handles use object identity: a handle whose component was removed or
restarted by reconfiguration is invalidated, lookups stop returning it, and passing it to
`enable`/`disable`/`state` throws `IllegalArgumentException`. Re-look handles up after every
reconfigure.

To observe transitions instead of polling, register a `PubSubStateListener` via
`addStateListener` (and remove it with `removeStateListener` when you lose interest — every
`add*Listener` method has a `remove*Listener` counterpart). Each `PubSubStateChangeEvent` carries
the component handle, the old and new `PubSubState`, a `StatusCode` giving the reason (for example
`Bad_Timeout` when the receive watchdog fires, or `Bad_ServerNotConnected` when an MQTT broker
connection drops), and a `Cause` attributing the transition: an explicit enable/disable (the owner
API or a remote Enable/Disable method), a parent cascade, an error or its recovery, service
startup/reconfiguration, or disposal. Events are delivered on the transport executor through a
serializing queue: in emission order, one at a time, and exceptions thrown by your listener are
caught and logged, not propagated into engine threads.

## The receive-timeout watchdog

`DataSetReaderConfig.Builder.messageReceiveTimeout(Duration)` arms a per-reader staleness
watchdog. The default is `Duration.ZERO`, which disables it entirely; a negative value is
rejected at build time with `PubSubConfigValidationException`.

With a positive timeout, the watchdog is armed when the reader enters `Operational`. If no
message is accepted for that reader within the timeout, the reader transitions to `Error` with
`Bad_Timeout`, and a diagnostics error (`messageReceiveTimeout expired`, `Bad_Timeout`) is
recorded against the reader's path and delivered to diagnostics listeners. Recovery is
automatic: the next accepted message — key frame, delta frame, or keep-alive — moves the reader
back to `Operational` and re-arms the watchdog.

*Accepted* is doing real work in both of those sentences. A message rejected by the
[sequence-number window](#sequence-numbers-and-duplicate-handling) — a duplicate, say — resets
nothing and recovers nothing: a publisher that only ever repeats old messages looks exactly like
a silent one to the watchdog. Keep-alives, by contrast, always reset it, whatever the window
makes of them. A positive timeout also sets the window's record-discard clock (twice the
timeout; see below), which is what lets a subscriber ride out a publisher restart.

This watchdog is the subscriber's only staleness signal, so use it whenever silence matters.
Over MQTT it is no longer the only outage signal in the system: a broker outage now fails the
publisher's *connection* into `Error` with `Bad_ServerNotConnected` and recovers it on reconnect
(see [the MQTT transport page](mqtt.md#broker-outages-and-reconnect)), and send failures carry
their real status codes instead of a blanket `Bad_CommunicationError`. But none of that is visible
on the subscriber, whose broker session may be perfectly healthy while the publisher's is down —
subscriber-side `messageReceiveTimeout` remains the mechanism that turns publisher silence into a
subscriber state transition.

## Sequence numbers and duplicate handling

Every DataSetReader tracks the Part 14 §7.2.3 sequence-number windows, on every transport: the
NetworkMessage sequence per (publisher, writer group) stream — UADP only; the JSON mapping has
no NetworkMessage sequence number — and the DataSetMessage sequence per (publisher, writer)
stream, UInt16 wide for UADP and UInt32 for JSON. The first message on a stream seeds its
window; a wildcard reader matching several publishers or writers tracks each stream
independently, bounded at 4096 streams per reader and window kind (least-recently-used
eviction, purely as rogue-traffic protection).

The window classifies every data or event DataSetMessage that carries a sequence number, and a
message classified *stale* (a duplicate of, or older than, the last processed message) or
*invalid* (outside the window in either direction — neither provably newer nor older, such as a
huge forward jump) is dropped: not delivered to listeners, not counted in
`dataSetMessagesReceived`, and — because per §6.2.9.6 a message is "new" only if its sequence
number increments — it does not reset the receive-timeout watchdog, does not complete reader
startup, and does not recover a reader from `Error`. UDP duplication and MQTT QoS 1 redelivery
are absorbed here, before your listener sees anything; a stale or invalid NetworkMessage
suppresses every DataSetMessage it carries for that reader. Drops tick the per-reader
`staleSequenceMessages` / `invalidSequenceMessages` counters and nothing else: dropping a
duplicate is normal operation ("shall be ignored"), so there is no `lastError` and no
diagnostics event. Messages that carry no sequence number bypass the windows entirely — every
such message is "new" per §6.2.9.6 and is delivered.

Keep-alives interact with the windows without passing through them. A keep-alive carries the
writer's next expected sequence number without consuming it, so it seeds an unseeded stream,
never advances a window it agrees with, and — when the carried value is off-window — reseeds the
window to match it (with a WARN naming the stream), because the publisher is authoritative about
its own counter. Whatever the window makes of one, a keep-alive always resets the receive
timeout and refreshes the stream's record-discard clock.

That reseed is one of three ways a subscriber recovers from a publisher whose numbering
restarted at zero — a process restart, or a reconfigure on the publisher that removed and
re-added the writer (a path-stable restart *preserves* the sequence — see
[live reconfiguration](#live-reconfiguration) — but removal plus re-addition, or a restart that
switches the mapping's sequence width, restarts it at 0):

- **Keep-alive reseed** — the next keep-alive that arrives reseeds the stream on the spot. This
  is the steady-state path for delta-emitting writers with a `keepAliveTime` configured, whose
  quiet cycles produce keep-alives; a writer that sends a data frame every cycle never emits
  keep-alives (see [timing behavior](#timing-behavior-worth-knowing)) and recovers by one of the
  paths below instead.
- **Record discard** — with a positive `messageReceiveTimeout`, a stream's window record is
  discarded after twice the timeout passes without an accepted message or keep-alive on it
  (§7.2.3), and the next message re-seeds. Rejected messages deliberately do not refresh that
  clock, so a restarted publisher recovers even if it never falls silent.
- **Rejection heuristic** — with the default timeout of zero there is no time-based discard; as
  a documented Milo extension, 16 consecutive rejections on one stream with no intervening
  accept or keep-alive discard the record (WARN), bounding the outage at 16 dropped messages.
  With a positive timeout this heuristic is inert and the pure spec semantics stand.

Both wire sequence numbers are surfaced on `DataSetReceivedEvent`:
`networkMessageSequenceNumber` (`UShort`; null for JSON, or when the UADP GroupHeader omits it)
and `dataSetMessageSequenceNumber` (`UInteger`; null when absent on the wire).

What the window does not do: rejects are dropped, never buffered and reordered — a message that
arrives after a newer one is gone, not delivered late. JSON streams whose DataSetMessages carry
no sequence numbers get no dedup at all (`MessageId`-based dedup is absent). And the two drop
counters are Milo vendor counters on the SDK diagnostics API only — Part 14 names no counter
for sequence-window drops, so they have no ns0 counterpart in the
[§9.1.11 exposure](#the-part-14-9111-diagnostics-exposure) either. See
[sequence numbers](limitations-and-interop.md#sequence-numbers).

## Live reconfiguration

Reconfiguration is by replacement. `PubSubConfig` objects are immutable; you build a complete
new config and hand it to the service, which computes the difference against the running config
and restarts only what changed:

- `reconfigure(newConfig, mode)` — explicit `ReconfigureMode`.
- `update(transform)` — applies a `UnaryOperator<PubSubConfig>` to the current config, always
  using `DISABLE_AFFECTED`.

`DISABLE_AFFECTED` disables, replaces, and re-enables only the minimal covering components of
each change; `STOP_AND_RESTART` stops and restarts the whole connection containing any affected
component. Subtrees that compare equal are left running untouched — traffic on them continues
through the reconfigure.

One thing the builders will not let you do: modify a config in place. `toBuilder()` pre-populates
the collections and `connection()`, `writerGroup()`, `publishedDataSet()` and friends *add*
rather than replace, so `current.toBuilder().connection(modified).build()` produces a duplicate
connection name and fails validation. The working idiom is a config factory parameterized by
whatever varies, returning a config identical except for that value. From `ReconfigureExample`:

```java
private static PubSubConfig publisherConfig(Duration publishingInterval) {
  PublishedDataSetConfig dataSet =
      PublishedDataSetConfig.builder(DATA_SET_NAME)
          .field(FieldDefinition.builder("temperature").dataType(NodeIds.Double).build())
          .field(FieldDefinition.builder("tick").dataType(NodeIds.Int32).build())
          .build();

  return PubSubConfig.builder()
      .publishedDataSet(dataSet)
      .connection(
          PubSubConnectionConfig.udp("pub-conn")
              .publisherId(PUBLISHER_ID)
              .address(UdpDatagramAddress.unicast(LOOPBACK, DATA_PORT))
              // ...
              .discoveryAddress(UdpDatagramAddress.unicast(LOOPBACK, PUBLISHER_DISCOVERY_PORT))
              .writerGroup(
                  WriterGroupConfig.builder("group")
                      .writerGroupId(ushort(1))
                      .publishingInterval(publishingInterval)
                      .messageSettings(GROUP_SETTINGS)
                      .dataSetWriter(
                          DataSetWriterConfig.builder("writer")
                              .dataSet(dataSet.ref())
                              .dataSetWriterId(ushort(1))
                              .settings(WRITER_SETTINGS)
                              .build())
                      .build())
              .build())
      .build();
}
```

The example calls this factory twice — once at startup with the phase-1 interval, once inside
`update` with the phase-2 interval — and logs the `ReconfigureResult`:

```java
ReconfigureResult result = publisher.update(current -> publisherConfig(PHASE_2_INTERVAL));

logger.info("[reconfigured] addedPaths={}", result.addedPaths());
logger.info("[reconfigured] removedPaths={}", result.removedPaths());
logger.info("[reconfigured] restartedPaths={}", result.restartedPaths());
```

A run looks like this (each line carries a `[thread] INFO logger -` prefix under
`mvn exec:java`; only the message portion is shown, and the exact counts drift by one or two
with timing):

```
[received] phase=1 dataSet=demo temperature=20.99, tick=1
...
[received] phase=1 dataSet=demo temperature=24.66, tick=6
[reconfigured] addedPaths=[]
[reconfigured] removedPaths=[]
[reconfigured] restartedPaths=[pub-conn/group]
[received] phase=2 dataSet=demo temperature=22.58, tick=7
...
[summary] phase 1: 6 DataSets received in ~5s at 1000ms
[summary] phase 2: 20 DataSets received in ~5s at 250ms
[summary] diagnostics: writer sent 26 DataSetMessages and reader received 26 across both phases
shutdown complete, exiting
```

Note `restartedPaths=[pub-conn/group]`: only the writer group restarted, because the publishing
interval is a group-level property and nothing at the connection level changed. The result lists
the minimal covering components; descendants (here `pub-conn/group/writer`) restart with their
parent but are not listed separately.

Note also where phase 2 starts: at tick=7, immediately after the reconfigure, with no gap. A
path-stable restart — a component that appears in `restartedPaths` under the same name path —
*preserves* the writer's DataSetMessage sequence numbering and the group's NetworkMessage
sequence numbering, so the reader's
[sequence-number window](#sequence-numbers-and-duplicate-handling) accepts the restarted
stream's first message: to a subscriber, the restarted writer is the same
(PublisherId, WriterGroupId, DataSetWriterId) stream, uninterrupted. (Earlier revisions of this
module restarted the sequence at 0 here and paid for it with a window of silently dropped
messages after every reconfigure.) The end of this section gives the exact preservation rules
and the cases that still restart at 0.

What escalates a change to a connection-level restart is the connection "shell": the enabled
flag, the PublisherId, the data address, connection properties, and raw transport settings (for
MQTT, the broker URI and broker security). Change any of those and every change on that
connection is applied as one connection restart. Two scoping rules to know beyond that:

- On broker (MQTT) connections, *any* reader-side change escalates to a full connection
  restart, because broker subscriber channels compute their subscription set once, at channel
  open.
- The UDP `discoveryAddress` is *not* part of the shell comparison. A reconfigure that changes
  only the discovery address restarts nothing — in either mode, since the diff sees no change
  to act on: the new value is recorded in the config, but the running discovery channel keeps
  its old address until the connection is rebuilt for some other reason. To force it through,
  pair the discovery-address change with `STOP_AND_RESTART` and any other change on that
  connection (any change then rebuilds the whole connection from the new config), or change a
  shell field.

`reconfigure` and `update` pre-validate the new config and throw `UaRuntimeException` with
`Bad_ConfigurationError` *before any change is applied* if it fails a check that `startup()`
also enforces — a mapping name with no registered provider, or an enabled DataSetReader on a
broker connection without a configured data queue name. A rejected reconfigure leaves the
running config untouched and existing handles valid.

If the new config adds a PublishedDataSet, bind its source with
`bindSource(ref, source)` — the integration pattern is reconfigure first, then bind. Until a
source is bound, the new writer's cycles still run: every field publishes as
`Bad_InternalError` and the writer's `sourceErrors` counter ticks with a
`Bad_ConfigurationError` ("no PublishedDataSetSource bound") diagnostics entry. Bind promptly.

Two `ReconfigureResult` reading caveats. First, `addedPaths` and `removedPaths` mix
vocabularies: runtime components appear as slash-joined paths (`pub-conn/grp-b/writer-b`) while
added or removed PublishedDataSets appear as bare dataset names (`ds-b`). Component names may
themselves contain `/`, so do not parse these strings — compare them against names you already
know. Second, diagnostics counters follow the same preservation rule as sequence numbers:
components on `restartedPaths` *keep* their counters, `lastError`, and TimeFirstChange
baselines across the restart, while components on `removedPaths` are zeroed — and a component
removed by one reconfigure and re-added by a later one starts fresh. In the run above, both
summary numbers span both phases: the restarted writer's `dataSetMessagesSent` counts phase 1's
6 plus phase 2's ~20, and the untouched reader's `dataSetMessagesReceived` matches it. If you
scrape counters into a monitoring system, treat paths in `removedPaths` as counter resets; paths
in `restartedPaths` stay cumulative.

The sequence-numbering preservation rules, exactly:

- **Path-stable restarts preserve.** A writer restarted under the same name path continues its
  DataSetMessage sequence where the replaced runtime left off, and a group restarted under the
  same name path continues its NetworkMessage sequence. Subscribers see one uninterrupted
  stream; there is no drop window. (The delta-frame cadence is *not* preserved: a restarted
  writer under a delta cadence re-baselines with a fresh key frame.)
- **A width change resets.** The DataSetMessage sequence is UInt16-wide on the UADP wire and
  UInt32-wide on JSON; a restart that switches the mapping (and with it the counter width)
  restarts the writer's sequence at 0 rather than masking a 32-bit value into 16 bits.
- **Removal plus re-addition resets.** A component that leaves the config in one reconfigure
  and returns in a later one restarts at 0, like a process restart.

Where a restart at 0 does occur, subscribers drop the restarted stream's messages as stale or
invalid until their window recovers — by whichever of the recovery paths described in
[sequence numbers and duplicate handling](#sequence-numbers-and-duplicate-handling) comes
first: a keep-alive reseed when the writer emits keep-alives, otherwise the record discard or
the 16-rejection heuristic.

## Diagnostics

`PubSubService.diagnostics()` returns a `PubSubDiagnostics` view with three operations:
`snapshot()` returns an immutable `Map<String, ComponentDiagnostics>` of every component,
`component(path)` returns an `Optional` for one, and `reset(path)` zeroes one component's
counters (below). Keys are the slash-joined config names you saw above
(`"pub-conn/group/writer"`, `"sub-conn/readers/reader"`). Each `ComponentDiagnostics` carries:

- the traffic counters `networkMessagesSent`, `networkMessagesReceived`, `dataSetMessagesSent`,
  `dataSetMessagesReceived`;
- the error counters `decodeErrors` and `sourceErrors`, plus the transmission-failure pair:
  `failedTransmissions` at writer-group paths (NetworkMessages never transmitted — send
  failures and oversize skips; encode failures don't count, since no NetworkMessage existed)
  and `failedDataSetMessages` at writer paths (DataSetMessages never sent due to encode *or*
  send failure, attributed per contributing writer — an oversize skip ticks both counters, an
  encode failure only this one);
- the per-reader sequence-drop counters `staleSequenceMessages` and `invalidSequenceMessages`;
- the five security counters (`encryptionErrors` at writer-group paths; `decryptionErrors`,
  `invalidSignatureMessages`, `unknownTokenMessages`, and `staleKeyMessages` at reader-group
  paths — their exact semantics are tabulated on the
  [message security page](message-security-and-sks.md#the-key-lifecycle-at-runtime));
- six state-transition counters mirroring Part 14 Table 311: `stateError` (every entry into
  `Error`), `stateOperationalByMethod` (Operational entries caused by an explicit enable — the
  owner API or a remote Enable method), `stateOperationalByParent` (Operational entries
  cascaded from a parent), `stateOperationalFromError`, `statePausedByParent`, and
  `stateDisabledByMethod` (explicit disables; dispose-caused Disabled transitions tick
  nothing). Transitions caused by service startup or reconfiguration tick neither ByMethod nor
  ByParent on the initiating component — only cascaded descendants tick ByParent;
- a `timeFirstChange` map recording when each counter first left zero (the Part 14 §9.1.11.5
  TimeFirstChange contract: no entry while a counter is 0), and a nullable `lastError` status
  code.

Counters that do not apply to a component type stay zero. `reset(path)` zeroes exactly that
path's counters and TimeFirstChange baselines — per-object, so resetting a group does not touch
its writers — and leaves `lastError` alone; increments concurrent with a reset may be lost
(documented imprecision, not a bug). Counters survive path-stable reconfigure restarts and are
zeroed by removal, as described [above](#live-reconfiguration).

A few counters reward knowing exactly where they tick. `networkMessagesReceived` on a
connection counts data-path arrivals — one tick per datagram or broker message, before decoding
and regardless of the outcome — while on a reader group it counts NetworkMessages that carried
at least one DataSetMessage matching one of the group's readers; a climbing connection counter
over flat group counters therefore means traffic that does not decode or does not match.
`decodeErrors` counts messages dropped for cause, at the path that dropped them: undecodable or
truncated input, unsupported chunked *discovery* messages, and reassembled chunk payloads that
fail to decode at the connection (inbound data-plane chunks are otherwise
[reassembled and delivered](limitations-and-interop.md#chunking-and-message-size)),
NetworkMessages larger than a reader group's non-zero `maxNetworkMessageSize` at the group,
version mismatches and invalid DataSetMessages at the reader. Security drops never tick
`decodeErrors` — they have [their own counters](message-security-and-sks.md#the-key-lifecycle-at-runtime),
of which only `decryptionErrors` records `lastError` and emits an event (`encryptionErrors` is
edge-triggered, `invalidSignatureMessages` sets `lastError` without an event, and the other two
are silent). The sequence-drop counters are the exception to
"drops are errors": a duplicate dropped by the
[sequence-number window](#sequence-numbers-and-duplicate-handling) is normal operation, so they
tick without touching `lastError` and without emitting any diagnostics event — visible in
snapshots only. At a reader's path, `dataSetMessagesReceived` plus the two sequence-drop
counters equals the total DataSetMessages matched to that reader.

For push-style monitoring, `addDiagnosticsListener` registers a `PubSubDiagnosticsListener`
(removable via `removeDiagnosticsListener`). In
this version only *error* events are delivered — decode failures, source read failures, send
failures, receive timeouts, publish-side oversize skips (a NetworkMessage exceeding the
writer group's non-zero `maxNetworkMessageSize` is skipped, not sent, and reported at the group
path with `Bad_EncodingLimitsExceeded` and the actual and maximum sizes), decryption failures,
and the *first* keyless-skipped publish cycle of a secured writer group after a successful one —
each as a
`PubSubDiagnosticsEvent` with the component path, a classifying `StatusCode`, a message, the
underlying exception when there is one, and a `Kind` discriminator separating `SEND_FAILURE`
(a NetworkMessage that was not transmitted) from `OTHER_ERROR` (everything else). Happy-path
activity is visible only through the
counters; sequence-window drops and most [security drops](message-security-and-sks.md#the-key-lifecycle-at-runtime)
emit no events at all; do not wait for informational events that will never come.

Send-failure event behaviors worth pinning:

- **The status codes are real.** A send failure carries the failure's own status code, not a
  blanket one: `Bad_ServerNotConnected` from an MQTT publish while the broker is disconnected
  (the fail-fast path), `Bad_InvalidState` from a closed channel, `Bad_ConfigurationError` from
  a broker writer with no resolvable queue, `Bad_EncodingLimitsExceeded` from an oversize skip.
  The code is extracted from the failure's cause chain; a failure that carries no OPC UA status
  code anywhere in its chain — a raw `SocketException: Message too long` from a UDP send, say —
  falls back to `Bad_InternalError`. Earlier revisions flattened every send failure to
  `Bad_CommunicationError`; keying automation on that one code is now wrong.
- **Shutdown and restarts are silent.** Send failures that surface while their component is
  being torn down — service shutdown, disable, or a reconfigure-driven restart — are recognized
  as teardown noise and suppressed: no event, no failure-counter tick (the hand-off
  `networkMessagesSent`/`dataSetMessagesSent` ticks stand, since the messages were handed to
  the channel). One benign residue remains: a failure that races the teardown in the same
  instant may still be reported once; nothing is reported after `shutdown()` resolves beyond
  events already enqueued, which are always delivered.

### The Part 14 §9.1.11 diagnostics exposure

Everything above is the SDK API. When the runtime is attached to an `OpcUaServer` via
`ServerPubSub` with `exposeInformationModel(true)` and `diagnosticsEnabled(true)`, the same
diagnostics are additionally served as the Part 14 §9.1.11 information model: the ns0 root
`Diagnostics` object (`i=17409`) is backed with live values, and every exposed connection,
group, writer, and reader gains a `Diagnostics` object whose spec-named counters and LiveValues
are computed per read from the runtime. Values there follow the §9.1.11.5 numeric rules —
counters are served as UInt32 and *saturate* at `0xFFFFFFFF` (the SourceTimestamp keeps
advancing at the cap; the SDK's `long` counters do not saturate) — and `DiagnosticsLevel` is
read-only `Basic`, with no level-switching machinery.

Each exposed Diagnostics object carries a `Reset` method. Reset is authorization-gated (the
attachment's `PubSubMethodAuthorizer.checkConfigure`; session-less calls get
`Bad_UserAccessDenied`; no channel-security minimum is imposed) and delegates to
`PubSubDiagnostics.reset(path)` with the per-object scope described above — except the
PublishSubscribe *root* `Reset` (`i=17421`), which zeroes only the root object's fragment-local
State* counters and never reaches the engine (the root has no engine path). See
[server integration](server-integration.md#exposing-diagnostics) for wiring and options.

## Threading and shutdown

`PubSubServiceConfig` separates three concerns, all defaulting to Milo's shared Stack
resources:

| Builder method | Runs | Default |
|---|---|---|
| `scheduledExecutor` | publish cycles, receive-timeout watchdogs | `Stack.sharedScheduledExecutor()` |
| `transportExecutor` | decoding received messages, listener delivery | `Stack.sharedExecutor()` |
| `eventLoopGroup` | Netty transport I/O | `Stack.sharedEventLoop()` |

Your code never runs on the publish scheduler or the Netty event loop. DataSet and metadata
events are dispatched by tasks already on the transport executor; state-change and diagnostics
events are re-dispatched onto it through a serializing queue, in emission order, one at a time.
A slow or blocking DataSet listener therefore stalls decode and delivery for the service — keep
listeners fast, and hand heavy work to your own executor. For isolating fast connections from
slow ones at the I/O level, `eventLoopPerConnection(true)` gives each connection its own
single-threaded event loop, shut down with the service.

The shared Stack executors use daemon threads. Two consequences:

- A long-running program must keep `main` alive itself, because nothing in the service does.
  Every long-running example uses the same pattern: a shutdown hook that closes the service and
  completes a `CompletableFuture` that `main` blocks on. Ctrl-C then triggers an orderly
  shutdown instead of an abrupt exit.
- A self-terminating program should release the shared resources at the end. Under
  `mvn exec:java` the JVM otherwise lingers around 15 seconds while the plugin tries to
  interrupt threads that will not die ("thread ... will linger despite being asked to die"
  warnings). `ReconfigureExample` ends with exactly this:

```java
// Both services used the default service config, which borrows the Stack's shared
// daemon executors; release them so the JVM exits promptly.
Stack.releaseSharedResources();
```

`shutdown()` is asynchronous and completes only after the internal event queue drains, so no
listener callback for the shutdown-induced `Disabled` transitions is still in flight when the
future resolves. `close()` is the synchronous variant — it waits for `shutdown()` to complete —
which makes the shutdown-hook lambda clean. Both are idempotent.

One ordering note for final tallies (`ReconfigureExample` does this): a received event can
land between reading a counter and logging a summary. Shut the publisher down before tallying
if you want deterministic last lines.

## Timing behavior worth knowing

The first publish cycle fires immediately when a writer group starts, not after one interval.
A 5-second window at 1000 ms therefore yields 6 messages, not 5 — which is exactly the verified
phase-1 count above — and a group restarted by reconfiguration also publishes immediately.

Delta frames change the cycle arithmetic. With `keyFrameCount` greater than 1, a writer emits a
key frame on its first cycle after start or activation and again every `keyFrameCount`-th
publishing interval (suppressed and keep-alive cycles count toward the cadence); cycles in
between emit a delta frame carrying only the fields whose transmitted values changed — and a
cycle where nothing changed emits nothing at all, consuming no sequence number. A restart, or a
reconfigure that recreates the writer, resets the cadence with a fresh key frame.

Keep-alives behave differently than many readers expect. `keepAliveTime` is unset by default,
meaning never emit. When set, a keep-alive is emitted for any writer that has had no
DataSetMessage actually handed to the transport within `keepAliveTime`: disabled and paused
writers, but also `Operational` writers that go quiet — a delta-emitting writer in a no-change
spell, or a writer whose every message is skipped at the size gate ("sent" means handed to the
channel, never merely drafted). Two exclusions: with the default `keyFrameCount` of 1 an
`Operational` writer publishes a key frame every cycle and never lapses, so a fully healthy
default-config group sends no keep-alives at all (and an idle group with no `keepAliveTime`
sends nothing — no empty NetworkMessages); and a writer in `Error` never emits keep-alives — a
keep-alive asserts the writer is still active, and emitting one would mask the failure from
subscribers. So for default configs, build subscriber liveness on `messageReceiveTimeout`,
which data frames reset just as well; but if you raise `keyFrameCount` above 1, set
`keepAliveTime` below your subscribers' `messageReceiveTimeout`, or a no-change quiet spell
reads as publisher death.

## Known limits

- Sequence-number rejects are dropped, never reordered: there is no reorder buffer, so a
  message that arrives after a newer one is gone, not delivered late. JSON streams whose
  DataSetMessages carry no sequence numbers get no dedup at all (`MessageId`-based dedup is
  absent). The sequence-drop counters are Milo vendor counters on the SDK diagnostics API;
  Part 14 names no counter for sequence-window drops, so the
  [§9.1.11 exposure](#the-part-14-9111-diagnostics-exposure) does not serve them either.
- The diagnostics listener delivers error events only; there are no informational or
  recovery events.
- A reconfigure that changes only a UDP connection's `discoveryAddress` is silently inert until
  that connection restarts for another reason (see above).
- A reconfigure that removes everything a connection's *discovery* channels exist for (its
  writer groups and `REQUEST_IF_MISSING` readers) closes them — but the connection's *data*
  channels stay open until the connection itself is removed or the service shuts down, even
  when no group remains to use them. An idle-socket leak, not a correctness problem.
