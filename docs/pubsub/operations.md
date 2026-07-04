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
`addStateListener`. Each `PubSubStateChangeEvent` carries the component handle, the old and new
`PubSubState`, and a `StatusCode` giving the reason (for example `Bad_Timeout` when the receive
watchdog fires). Events are delivered on the transport executor through a serializing queue: in
emission order, one at a time, and exceptions thrown by your listener are caught and logged, not
propagated into engine threads.

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
It is especially relevant over MQTT: a broker outage surfaces on the publisher side only as
send-failure diagnostics (flattened to `Bad_CommunicationError`) and never changes publisher
state, so subscriber-side `messageReceiveTimeout` is the one mechanism that turns an outage
into a state transition.

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
restarted at zero — a process restart, or a reconfigure on the publisher that recreated the
writer (writer recreation restarts the DataSetMessage sequence):

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
counters are Milo vendor counters on the SDK diagnostics API only — the spec-named Part 14
diagnostics counters are not exposed. See
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
`mvn exec:java`; only the message portion is shown):

```
[received] phase=1 dataSet=demo temperature=20.99, tick=1
...
[received] phase=1 dataSet=demo temperature=24.66, tick=6
[reconfigured] addedPaths=[]
[reconfigured] removedPaths=[]
[reconfigured] restartedPaths=[pub-conn/group]
[received] phase=2 dataSet=demo temperature=22.58, tick=13
...
[summary] phase 1: 6 DataSets received in ~5s at 1000ms
[summary] phase 2: 14 DataSets received in ~5s at 250ms
[summary] diagnostics: writer sent 20 DataSetMessages since the group restart, reader received 20 across both phases
shutdown complete, exiting
```

Note `restartedPaths=[pub-conn/group]`: only the writer group restarted, because the publishing
interval is a group-level property and nothing at the connection level changed. The result lists
the minimal covering components; descendants (here `pub-conn/group/writer`) restart with their
parent but are not listed separately.

Note also where phase 2 starts: at tick=13, after a quiet ~1.5 seconds, not at tick=7. The
restarted group restarted its sequence numbering, and the reader's
[sequence-number window](#sequence-numbers-and-duplicate-handling) silently dropped the first
post-restart messages as stale. The end of this section returns to that gap and to the summary
arithmetic it produces.

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
know. Second, diagnostics counters do not survive a restart, which the example demonstrates
deliberately:

```java
// Diagnostics counters are keyed by component path, but a restarted component starts
// fresh counters: the writer's count covers phase 2 only, while the reader, untouched by
// the reconfigure, counts both phases. Capture the writer's count before shutdown.
long dataSetsSent =
    publisher
        .diagnostics()
        .component("pub-conn/group/writer")
        .map(diagnostics -> diagnostics.dataSetMessagesSent())
        .orElse(0L);

// Stop the publisher before tallying so the summary is the last thing logged.
publisher.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

int phase2Received = receivedCount.get() - phase1Received;

long dataSetsReceived =
    subscriber
        .diagnostics()
        .component("sub-conn/readers/reader")
        .map(diagnostics -> diagnostics.dataSetMessagesReceived())
        .orElse(0L);
```

That is why the verified summary line reads writer sent 20 and reader received 20 — equal
numbers counting different things. The restarted writer's counter covers phase 2 only: 20
DataSetMessages sent since the restart. The untouched reader's counter spans both phases but
counts accepted messages only: the 6 from phase 1 plus the 14 from phase 2. The other 6 phase-2
messages — the quiet ~1.5 seconds and the jump from tick=6 to tick=13 in the transcript — were
dropped by the sequence-number window and ticked `staleSequenceMessages` instead. Restarted
paths get fresh counters; components the reconfigure did not touch stay cumulative. If you
scrape counters into a monitoring system, treat any path that appears in `restartedPaths` as a
counter reset.

A restarted writer also restarts its DataSetMessage sequence numbering at zero, and until the
subscriber's window catches up, the restarted writer's messages are dropped as stale or
invalid — expect a brief gap after a reconfigure that restarts a writer. The gap ends at
whichever comes first: the restarted counter overtaking the reader's window on its own — the
run above, where phase 1 was only 6 messages deep, so 6 silent drops and ~1.5 seconds covered
it — or one of the recovery paths described in
[sequence numbers and duplicate handling](#sequence-numbers-and-duplicate-handling): a
keep-alive reseed when the writer emits keep-alives, otherwise the record discard or the
16-rejection heuristic.

## Diagnostics

`PubSubService.diagnostics()` returns a `PubSubDiagnostics` view with two accessors:
`snapshot()` returns an immutable `Map<String, ComponentDiagnostics>` of every component, and
`component(path)` returns an `Optional` for one. Keys are the slash-joined config names you saw
above (`"pub-conn/group/writer"`, `"sub-conn/readers/reader"`). Each `ComponentDiagnostics`
carries `networkMessagesSent`, `networkMessagesReceived`, `dataSetMessagesSent`,
`dataSetMessagesReceived`, `decodeErrors`, `sourceErrors`, the per-reader sequence-drop
counters `staleSequenceMessages` and `invalidSequenceMessages`, the five security counters
(`encryptionErrors` at writer-group paths; `decryptionErrors`, `invalidSignatureMessages`,
`unknownTokenMessages`, and `staleKeyMessages` at reader-group paths — their exact semantics are
tabulated on the [message security page](message-security-and-sks.md#the-key-lifecycle-at-runtime)),
and a nullable `lastError` status code; counters that do not apply to a component type stay zero.

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

For push-style monitoring, `addDiagnosticsListener` registers a `PubSubDiagnosticsListener`. In
this version only *error* events are delivered — decode failures, source read failures, send
failures, receive timeouts, publish-side oversize skips (a NetworkMessage exceeding the
writer group's non-zero `maxNetworkMessageSize` is skipped, not sent, and reported at the group
path with `Bad_EncodingLimitsExceeded` and the actual and maximum sizes), decryption failures,
and the *first* keyless-skipped publish cycle of a secured writer group after a successful one —
each as a
`PubSubDiagnosticsEvent` with the component path, a classifying `StatusCode`, a message, and
the underlying exception when there is one. Happy-path activity is visible only through the
counters; sequence-window drops and most [security drops](message-security-and-sks.md#the-key-lifecycle-at-runtime)
emit no events at all; do not wait for informational events that will never come.

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

One ordering note for final tallies, also visible in the snippet above: a received event can
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

- There is no listener-removal API: `PubSubService` has `add*` methods only. Listeners live as
  long as the service; embedders that outlive their interest in events must make their
  listeners self-disabling.
- Sequence-number rejects are dropped, never reordered: there is no reorder buffer, so a
  message that arrives after a newer one is gone, not delivered late. JSON streams whose
  DataSetMessages carry no sequence numbers get no dedup at all (`MessageId`-based dedup is
  absent), and the sequence-drop counters exist only on the SDK diagnostics API — the
  spec-named Part 14 diagnostics counters are not exposed.
- The diagnostics listener delivers error events only; there are no informational or
  recovery events.
- A reconfigure that changes only a UDP connection's `discoveryAddress` is silently inert until
  that connection restarts for another reason (see above).
