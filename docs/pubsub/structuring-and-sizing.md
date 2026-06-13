# Structuring and Sizing

This page answers the two questions that come up as soon as the [getting started](getting-started.md) pair stops being a toy: how do you structure a configuration with more than one dataset — more writers, more groups, more connections — and what bounds how large a dataset can be? It assumes you have the one-writer pair running. The full builder reference is in the [configuration model](configuration.md); the runnable anchor for this page is the `MultiGroupPublisherExample` / `MultiGroupSubscriberExample` pair, walked through below.

## What each level of the configuration is for

A `PubSubConfig` is a tree — connections contain writer groups and reader groups, which contain DataSetWriters and DataSetReaders — and each level owns a distinct set of concerns. Structuring a configuration well is mostly a matter of putting each decision at the level that owns it.

A connection is an identity and an endpoint. It owns the `PublisherId`, the transport and its address (one UDP address, or one broker URI), and the actual sockets: every writer group on a connection sends through that connection's single publisher channel, and every reader group receives through its single subscriber channel — groups own no sockets of their own. The connection also owns the machinery around those channels: an internal dispatch queue that decodes and delivers received messages in arrival order, and either the UDP discovery responder or the broker metadata publisher (see [metadata and discovery](metadata-and-discovery.md)). On UDP one consequence is worth pinning: every data datagram the connection publishes goes to the connection's one configured address. Writer groups on a UDP connection cannot diverge in destination — publishing to two multicast groups, or two ports, means two connections. So you add a connection when you need a second transport, a second address or broker, or a second publisher identity, and not before: each one costs sockets (publisher and subscriber channels as its groups need them, plus discovery sockets on UDP) and, on MQTT, one broker client session. For isolating fast connections from slow ones at the I/O level there is also `eventLoopPerConnection(true)` — see [operations](operations.md).

A writer group is a schedule and a message format. Each group runs its own fixed-rate publish task at its own `publishingInterval` — groups on the same connection never share a cycle — and carries the group-level message settings: the UADP or JSON mapping choice and content masks, `keepAliveTime`, GroupVersion. Sequence numbering is scoped to match: one NetworkMessage sequence number per group, one DataSetMessage sequence number per writer. The key-frame cadence is per writer too: `keyFrameCount` counts the group's publishing intervals, and a writer's no-change cycle under a delta cadence sends nothing and consumes no sequence number (see [what bounds dataset size](#what-bounds-dataset-size)). The group is also a unit of operations: it can be enabled and disabled by its own handle, a group-level config change restarts only that group (see [live reconfiguration](operations.md#live-reconfiguration)), and diagnostics counters roll up at its path. Split writers into separate groups when their natural rates diverge, or when you want to operate on them independently.

A DataSetWriter plus its PublishedDataSet is a schema and a stream identity. The dataset declares the named, ordered fields; the writer binds it into a group and gives it the `dataSetWriterId` subscribers match on. Metadata is scoped here, on both transports: the UDP discovery responder answers probes with one announcement per requested DataSetWriterId, and a broker connection publishes one retained metadata message per writer. Changing a dataset's fields therefore produces new metadata for every writer that references it, and a reconfigure that changes a PublishedDataSet restarts exactly those writers. The `ConfigurationVersion` does not follow along on its own: the builders default it to 1.1 and never auto-increment, so bumping the version on a schema change is your job. Group fields into a dataset by cohesion — fields that describe the same logical thing and change schema together — rather than packing everything into one wide dataset where any tweak means new metadata, a manual version bump, and a restart for every writer that carries it.

On the consuming side the symmetry is deliberately looser. A reader group is organizational: it owns no socket and no thread — activating one validates its security mode and ensures the connection's subscriber channel is open, nothing more — and exists for lifecycle scoping, reconfigure restart scope, and a diagnostics rollup level. The unit that matters is the DataSetReader: each reader filters on `publisherId` and `dataSetWriterId` (unset filters are wildcards) and yields one decoded stream. Readers do not need to mirror the publisher's group structure — and under the default UADP masks they cannot even see it, as the next section explains. One reader per writer, as in the example below, keeps the streams separated in application code; a single wildcard reader that receives every matching DataSetMessage can be the right shape for a generic monitor.

One more reason to structure deliberately: diagnostics counters and error events are keyed by component path at every level — `connection`, `connection/group`, `connection/group/writer-or-reader` — so the structure you choose is also the monitoring granularity you get. Sent counters tick at the connection and group per NetworkMessage and at each contributing writer per DataSetMessage; receive counters tick at the connection per arrival — before decode, whatever the outcome — and at each matched group and matched reader. See [diagnostics](operations.md#diagnostics).

## Multiple writers on the wire

Within one writer group, every publish cycle collects one DataSetMessage per writer with something to send — sources are read back-to-back, in the order the writers were declared; at the default `keyFrameCount` that is every writer, every cycle, while under a delta cadence a no-change writer contributes nothing — and packs them all into a single UADP NetworkMessage. What makes that demultiplexable is the PayloadHeader, on by default: it carries a count byte plus each DataSetMessage's `dataSetWriterId`, in writer order, and (when there is more than one DataSetMessage) a sizes array. The fixed wire limits that come with this packing are generous: at most 255 DataSetMessages per NetworkMessage, and at most 65535 bytes per DataSetMessage body when the sizes array is present; exceeding either fails the encode with `Bad_EncodingLimitsExceeded`.

Packing means writers in a group share fate per cycle in one direction only. A writer whose source fails — throws, returns the wrong field count, or has no source because a reconfigure added the writer unbound (plain startup rejects unbound sources with `Bad_ConfigurationError`) — does not abort the cycle or affect its siblings: its DataSetMessage is still sent, with every field carrying `Bad_InternalError`, and a `sourceErrors` diagnostic lands at that writer's path. Subscribers see Bad-status fields, never a gap. Keep-alives, when configured, merge into the same cycle and the same packing — see [operations](operations.md#timing-behavior-worth-knowing).

What subscribers cannot see is the group structure itself. Under the default UADP masks the GroupHeader is omitted, so the WriterGroupId never appears on the wire; readers distinguish streams by PublisherId plus the `dataSetWriterId` from the PayloadHeader, and identifier filters apply only when the identifier is actually on the wire. The practical rule: assign `dataSetWriterId` values unique across *all* groups under one PublisherId, and leave reader-side `writerGroupId` filters unset rather than trusting them. A filter on an identifier that never reaches the wire is silently ignored — no error, but no isolation either; the reader receives every otherwise-matching message as if the filter were unset. The trap is believing the filter is filtering — the same one [getting started](getting-started.md) warns about. If you want the WriterGroupId transmitted, the masks are configurable — see [UADP message settings](configuration.md#uadp-defaults-and-the-masks-you-will-actually-want).

Broker transports add one structural option UDP does not have. A writer-level `BrokerTransportSettings` with a `queueName` splits that writer out of the group's shared NetworkMessage: its DataSetMessages are published separately to its own queue, with its own QoS. This is broker-only — on a UDP connection a writer-level queue name is silently ignored and everything shares one NetworkMessage; the configuration page's [inert knobs](configuration.md#knobs-that-exist-but-are-inert-or-rejected-today) section covers the related broker-settings caveats. The MQTT topic layout follows the same split: data topics are per *group* (`<prefix>/<mapping>/data/<publisherId>/<writerGroupName>`, shared by every writer without an override) while metadata topics are per *writer* (`<prefix>/<mapping>/metadata/<publisherId>/<writerGroupName>/<dataSetWriterName>`). So on MQTT, a broker-side consumer can only subscribe per-writer if you give writers queue overrides; otherwise the group is the unit of subscription. See [MQTT transport](mqtt.md) for topic derivation.

The JSON mapping packs the same way by default but can also split: the `SingleDataSetMessage` network message mask emits one NetworkMessage per DataSetMessage. That matters for sizing, below.

## The multi-group example pair

`MultiGroupPublisherExample` and `MultiGroupSubscriberExample` (`milo-examples/pubsub-examples`, package `org.eclipse.milo.examples.pubsub.udp`) put the structural rules above into one runnable pair: one UDP connection (PublisherId 7001, data on `127.0.0.1:15150`) carrying two writer groups at different rates — "fast" (250 ms) with two writers, "motion" and "vibration", and "slow" (2000 ms) with one, "status". Every snippet below is lifted from them.

The publisher's groups, from `MultiGroupPublisherExample.createConfig()`. The two fast writers will share one NetworkMessage per 250 ms cycle; the slow group runs its own independent task. Note the `dataSetWriterId` values 1, 2, 3 — unique across both groups, because the group structure is invisible on the wire:

```java
WriterGroupConfig fastGroup =
    WriterGroupConfig.builder("fast")
        .writerGroupId(ushort(1))
        .publishingInterval(Duration.ofMillis(250))
        .dataSetWriter(
            DataSetWriterConfig.builder("motion-writer")
                .dataSet(motion.ref())
                .dataSetWriterId(ushort(1))
                .build())
        .dataSetWriter(
            DataSetWriterConfig.builder("vibration-writer")
                .dataSet(vibration.ref())
                .dataSetWriterId(ushort(2))
                .build())
        .build();

WriterGroupConfig slowGroup =
    WriterGroupConfig.builder("slow")
        .writerGroupId(ushort(2))
        .publishingInterval(Duration.ofMillis(2000))
        .dataSetWriter(
            DataSetWriterConfig.builder("status-writer")
                .dataSet(status.ref())
                .dataSetWriterId(ushort(3))
                .build())
        .build();
```

Both groups hang off one connection, and all three datasets are declared at the top of the config:

```java
return PubSubConfig.builder()
    .publishedDataSet(motion)
    .publishedDataSet(vibration)
    .publishedDataSet(status)
    .connection(
        PubSubConnectionConfig.udp("publisher-connection")
            .publisherId(PublisherId.uint16(ushort(7001)))
            .address(UdpDatagramAddress.unicast("127.0.0.1", 15150))
            .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", 15151))
            .writerGroup(fastGroup)
            .writerGroup(slowGroup)
            .build())
    .build();
```

One source binds per PublishedDataSet, exactly as in the single-writer case — there is nothing group-aware about sources. The engine pulls `readMotion` and `readVibration` back-to-back every fast cycle and `readStatus` every slow cycle:

```java
PubSubBindings bindings =
    PubSubBindings.builder()
        .source(motionDataSet().ref(), this::readMotion)
        .source(vibrationDataSet().ref(), this::readVibration)
        .source(statusDataSet().ref(), this::readStatus)
        .build();
```

The subscriber, `MultiGroupSubscriberExample`, holds three readers in a single reader group — remember, reader groups are organizational, so one is enough — each matching one writer by PublisherId plus DataSetWriterId, built by a small helper:

```java
ReaderGroupConfig readerGroup =
    ReaderGroupConfig.builder("readers")
        .dataSetReader(reader("motion-reader", 1, motionMetaData))
        .dataSetReader(reader("vibration-reader", 2, vibrationMetaData))
        .dataSetReader(reader("status-reader", 3, statusMetaData))
        .build();
```

```java
private static DataSetReaderConfig reader(
    String name, int dataSetWriterId, DataSetMetaDataConfig metaData) {
  return DataSetReaderConfig.builder(name)
      .publisherId(PUBLISHER_ID)
      .dataSetWriterId(ushort(dataSetWriterId))
      .dataSetMetaData(metaData)
      .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
      .build();
}
```

No reader sets a `writerGroupId` filter — the publisher's "fast"/"slow" structure never reaches the wire. The "motion" and "vibration" DataSetMessages arrive packed in one NetworkMessage per fast cycle, and the writer ids in the PayloadHeader are what route each one to its reader. Instead of one global listener that switches on writer id, each reader gets its own listener via `DataSetReaderRef`, so the three streams arrive in application code already separated:

```java
service.addDataSetListener(
    new DataSetReaderRef("subscriber-connection", "readers", "motion-reader"),
    event -> logDataSet("[motion]", event));
service.addDataSetListener(
    new DataSetReaderRef("subscriber-connection", "readers", "vibration-reader"),
    event -> logDataSet("[vibration]", event));
service.addDataSetListener(
    new DataSetReaderRef("subscriber-connection", "readers", "status-reader"),
    event -> logDataSet("[status]", event));
```

### Run it

Build once from the repository root, then start the subscriber, then the publisher, in separate terminals. As everywhere in these pages: keep `-q` off the `exec:java` commands, or the examples run silently.

```sh
mvn -q -pl milo-examples/pubsub-examples -am install -DskipTests
```

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.MultiGroupSubscriberExample
```

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.MultiGroupPublisherExample
```

The publisher prints a startup line and then a `[summary]` line every 10 fast cycles (2.5 seconds), showing what all three sources most recently published (trimming Maven's `[thread] INFO logger -` prefix):

```
multi-group publisher started: groups "fast" (motion + vibration, 250ms) and "slow" (status, 2000ms) publishing to opc.udp://127.0.0.1:15150 as publisher 7001, Ctrl-C to exit
[summary] fastCycle=0 motion(position=0.00, velocity=100.00) vibration(rms=0.000, peak=0.000) status(uptimeSeconds=0, state=RUNNING)
[summary] fastCycle=10 motion(position=50.00, velocity=86.60) vibration(rms=1.200, peak=3.360) status(uptimeSeconds=2, state=RUNNING)
[summary] fastCycle=20 motion(position=86.60, velocity=50.00) vibration(rms=0.800, peak=2.240) status(uptimeSeconds=4, state=RUNNING)
```

The subscriber's three listeners make the structure audible: four `[motion]` and four `[vibration]` lines per second — always adjacent, because they travel in the same NetworkMessage — and one `[status]` line every two seconds:

```
multi-group subscriber started: listening on opc.udp://127.0.0.1:15150 for datasets "motion", "vibration", and "status" from publisher 7001, Ctrl-C to exit
[motion] position=0.0, velocity=100.0
[vibration] rms=0.8625737860160924, peak=2.4152066008450586
[status] uptimeSeconds=0, state=RUNNING
[motion] position=5.233595624294383, velocity=99.86295347545739
[vibration] rms=0.923606797749979, peak=2.586099033699941
[motion] position=10.452846326765346, velocity=99.45218953682733
[vibration] rms=0.9815961998958188, peak=2.7484693597082925
```

Both run until Ctrl-C. Like every UDP pair on one host, the publisher and subscriber pin distinct discovery ports (15151 and 15152) so neither falls back to the Part 14 default of port 4840 plus a multicast join.

## What bounds dataset size

Start with the bandwidth arithmetic. At the default `keyFrameCount` of 1, Milo publishes full key frames: every Operational writer snapshots and sends *every* field, *every* cycle, with no on-change suppression. Set a writer's `keyFrameCount` above 1 and it runs a delta cadence instead: a full key frame on the first cycle after the writer starts or its group (re)activates and on every `keyFrameCount`-th publishing interval, delta frames carrying only the changed fields in between — where "changed" compares the value as actually transmitted under the field mask, so a timestamp-only change counts only when timestamps are on the wire, and there is still no deadband. A delta cycle where nothing changed sends nothing at all — no message, no sequence number — so pair a delta cadence with a `keepAliveTime` unless silence is acceptable; and a delta that would carry every field is sent as a key frame instead, resetting the cadence. Configurations that cannot express delta frames — JSON without the DataSetMessage header and `MessageType` on the wire, a UADP writer with a fixed `ConfiguredSize` — are rejected at startup and reconfigure with `Bad_ConfigurationError` (enabled writers only; disabled ones round-trip); the wire formats are in [delta frames](limitations-and-interop.md#delta-frames). Steady-state bandwidth per writer group is therefore the encoded message sizes times the publish rate, and the levers are the publishing interval, the dataset contents, each writer's key-frame cadence, and how you split writers across groups. A slow-changing field placed in a fast group still rides every key frame at the fast rate — a delta cadence thins the cycles in between but does not change that — so splitting rate-divergent fields into separate groups remains the structural fix.

Next, the ceiling. There is still no NetworkMessage chunking, in either direction (see [chunking and message size](limitations-and-interop.md#chunking-and-message-size)), but `maxNetworkMessageSize` is no longer inert. On a writer group it is enforced at publish time, on every transport, whenever it is non-zero: an encoded NetworkMessage that exceeds it is skipped — never sent — with a `Bad_EncodingLimitsExceeded` diagnostics error at the group's path carrying the actual and maximum sizes, and the DataSetMessages it carried are not counted as sent. Two boundaries frame the knob: a UDP writer group configured above 65535 fails startup and reconfigure validation with `Bad_ConfigurationError` (the Part 14 cap for UDP), and discovery announcements and broker metadata messages are bounded separately, not by the group budget. The default changed to match the new semantics: 0, meaning *no enforced limit* — the old default of 1400 never enforced anything — so at defaults the assembled message size is still compared against nothing, and an arbitrarily large single-dataset message encodes successfully and dies at the socket. On the receive side a reader group's `maxNetworkMessageSize` (default 0 = unrestricted) is now honored too, as a documented Milo extension — Part 14 gives the reader side no size semantics: a NetworkMessage larger than it is never seen by that group's readers and ticks `decodeErrors` at the group's path. The encode-time limits that apply regardless of configuration are the fixed UADP wire-format ones from earlier (255 DataSetMessages, 65535 bytes per sized DataSetMessage body, 65535 fields) plus a per-value bound: an individual string, byte string, or array larger than the encoding context's `maxMessageSize` (2 MiB by default) fails with `Bad_EncodingLimitsExceeded`.

Here is exactly where the failure line sits on UDP between two Milo endpoints at defaults:

- Up to the sending OS's datagram limit, the message goes through end-to-end — and that limit is not one number. IPv4 itself allows 65,507 bytes of UDP payload, and Linux will send up to that at defaults; macOS and the BSDs default to a 9,216-byte maximum datagram (`sysctl net.inet.udp.maxdgram`) and refuse anything larger until that sysctl is raised. The receive side is not the variable: the subscriber channel reads into a fixed 65,535-byte buffer — the Part 14 cap for UDP — so any datagram the sender's OS can put on the wire is read whole; there is no smaller receive buffer to truncate against. If something else on the path truncates or corrupts a datagram, the failure is not silent: the tolerant UADP decoder still delivers whatever it decoded before the failure point, and the failure itself ticks `decodeErrors` and sets `lastError` at the subscriber connection's path.
- Above the sending OS's limit — whether that is 65,507 or macOS's 9,216 — the failure is the same: the OS rejects the send with `java.net.SocketException: Message too long`. The *only* trace is on the publisher's diagnostics: a `PubSubDiagnosticsEvent` at the writer group's path with `Bad_CommunicationError` and the message `failed to send NetworkMessage: Message too long`, plus the group's `lastError`. No log line is emitted, no error counter ticks — and because `networkMessagesSent` / `dataSetMessagesSent` are incremented when the message is handed to the channel, before the asynchronous send resolves, the failed sends still count as sent. If you have no diagnostics listener registered, `lastError` in the diagnostics snapshot is the only persistent evidence.
- With a writer-group `maxNetworkMessageSize` configured, the failure moves earlier and gets a better name: the oversize message is skipped at publish, before it ever reaches the socket, with `Bad_EncodingLimitsExceeded` at the group's path naming the actual and maximum sizes — and unlike the socket case, the skipped DataSetMessages are *not* counted sent.

So the practical end-to-end ceiling for a UADP-over-UDP NetworkMessage between Milo endpoints at defaults is the sending OS's datagram limit — 65,507 bytes (the IPv4 theoretical maximum, and what Linux allows by default) but only 9,216 bytes on macOS/BSD until `net.inet.udp.maxdgram` is raised, either way at or under the 65,535-byte Part 14 cap the receive buffer is sized to — including all headers, every DataSetMessage in the cycle's packing, and every field value. What fills the band between one MTU and that ceiling is IP fragmentation: a datagram larger than the path MTU travels as fragments, and losing any one fragment costs the whole message — fine on loopback, increasingly fragile on real networks. The MTU advice therefore stands as advice rather than as a cliff: keep the encoded key frame within a single standard-MTU datagram (about 1400 bytes of payload) and you never rely on fragmentation — or brush any OS's send limit — against Milo peers or anyone else; set the group's `maxNetworkMessageSize` to that budget if you want it enforced rather than advised. The diagnostic signatures moved with the ceiling: the old silent-drop case — connection receive counts climbing while every reader-group counter stays flat and no error ticks — is gone. Its replacements are visible: a message that arrives but fails to decode ticks `decodeErrors` and `lastError` at the subscriber connection's path, and a message larger than a reader group's configured `maxNetworkMessageSize` ticks `decodeErrors` at that group's path.

Three transport-specific notes round this out:

- JSON messages are materially larger than UADP for the same dataset: field names travel as object keys in every message, the default DataSetMessage mask selects verbose encoding, and a `ua-metadata` message embeds the dataset's complete field list in one document. Two built-in levers shrink it: the `SingleDataSetMessage` mask bit — one NetworkMessage per DataSetMessage — shrinks the individual message, and a delta cadence shrinks the steady state, since a `ua-deltaframe` carries only the changed fields. (JSON is broker-only, so the UDP ceiling above does not apply to it.)
- The MQTT transport configures no client-side packet-size limit, so absent a writer-group `maxNetworkMessageSize` — which is enforced on MQTT exactly as on UDP, skipping the oversize message before it reaches the broker — the effective limit is whatever your broker enforces. A publish the broker or client rejects fails through the send future: `MqttPublisherChannel` logs it at DEBUG, and the engine records the same `Bad_CommunicationError` diagnostics as the UDP case — again, diagnostics, not logs, are where to look.
- UADP discovery metadata has its own hard cap: announcements are bounded by a fixed, non-configurable 4096-byte `DiscoveryMaxMessageSize`. An announcement that exceeds it is dropped with a WARN log and a `Bad_EncodingLimitsExceeded` diagnostics error — never chunked. Because the announcement carries the dataset's full field list, this effectively caps how many fields a dataset can have and still be discoverable over UDP: data keeps publishing, but `REQUEST_IF_MISSING` subscribers never learn the field names. Wide datasets need configured metadata on the reader, or a broker transport with retained metadata.

## Rules of thumb

Each of these falls directly out of a mechanism above.

- Keep a dataset's encoded key frame within one MTU-sized datagram (~1400 bytes) where you can; the hard Milo-to-Milo UDP ceiling is the sending OS's datagram limit (~65,507 bytes on Linux, 9,216 on macOS/BSD at defaults), and everything between the two travels only by IP fragmentation. Nothing chunks, and nothing warns at config time that a dataset will be too big — set the group's `maxNetworkMessageSize` if you want oversize cycles skipped with a `Bad_EncodingLimitsExceeded` diagnostics error instead of discovered at the socket.
- Split fields with divergent rates into separate writer groups. At the default `keyFrameCount` every field publishes every cycle, and even under a delta cadence every key frame carries everything, so group membership is still a bandwidth commitment at that group's rate; a second group on the same connection costs only a scheduled task.
- Split datasets by schema cohesion and change cadence. Metadata is per writer and versioned per dataset: one wide dataset means every schema tweak republishes metadata for all of its fields, restarts every writer referencing it on reconfigure, and calls for a manual `ConfigurationVersion` bump — the builders default it to 1.1 and never auto-increment.
- Assign `dataSetWriterId` values unique across all groups under a PublisherId, and leave reader `writerGroupId` filters unset. With the default masks the group structure never reaches the wire; writer ids are the only demultiplexing key.
- Add connections only for a new transport, address or broker, or publisher identity. Groups share the connection's channels for free; connections cost sockets, discovery machinery, and (on MQTT) a broker session.
- Monitor diagnostics, not logs. Send failures of every kind surface as `PubSubDiagnosticsEvent`s and `lastError` — the sent counters keep ticking through socket-level failures, though oversize-skipped messages are never counted sent — and receive-side failures tick `decodeErrors`: at the connection path for decode failures, at the reader-group path for size refusals.

For what the message settings can and cannot express, see the [configuration model](configuration.md); for handles, restart scoping, and the diagnostics API used above, see [operations](operations.md); for the full honest list of what this release does not do, see [limitations and interop](limitations-and-interop.md).
