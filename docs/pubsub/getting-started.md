# Getting Started with Milo PubSub

This page takes you from zero to a working OPC UA Part 14 publisher and subscriber exchanging live data over UDP on your own machine. It is written for Java developers who know Milo's client/server basics but are new to PubSub.

## What PubSub is, in Milo terms

Everything else in Milo is client/server: a client opens a session to one server, reads, writes, and subscribes, and the server tracks state for that client. PubSub inverts this. A publisher pushes messages onto the network on a fixed cycle, addressed to no one in particular; any number of subscribers listen for them and decode the ones they care about. There are no sessions and no per-subscriber state — the publisher never knows who, or how many, are listening. That makes it the right shape for one-to-many telemetry distribution at a steady rate.

Milo gives you two ways onto the network and two ways to host the engine. Transport: brokerless UDP, where UADP binary messages travel as datagrams (unicast or multicast), or broker-based [MQTT](mqtt.md), where messages flow through an MQTT broker as UADP binary or JSON (JSON is broker-only; JSON settings on a UDP connection are rejected at config build time). Hosting: a standalone `PubSubService`, which needs no OPC UA server at all, or [`ServerPubSub`](server-integration.md), which attaches the same engine to an `OpcUaServer` so datasets can publish address-space node values or write received values into nodes. This tutorial uses the simplest combination — standalone service, UDP unicast, UADP — because it teaches every concept the other combinations build on.

## Modules and dependencies

| Artifact | Contents | When you need it |
|---|---|---|
| `org.eclipse.milo:milo-sdk-pubsub` | Config model, runtime engine, UADP and JSON codecs, UDP transport | Always — and it is sufficient by itself for everything on this page |
| `org.eclipse.milo:milo-sdk-pubsub-mqtt` | MQTT transport, built on the HiveMQ MQTT client | Publishing or subscribing through an MQTT broker |
| `org.eclipse.milo:milo-sdk-pubsub-server` | `ServerPubSub` | Attaching PubSub to an `OpcUaServer` |

The core module depends only on the Milo stack — not on `milo-sdk-client` or `milo-sdk-server` — so a standalone publisher or subscriber pulls in no client/server machinery. All three artifacts are managed by `milo-bom`:

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.eclipse.milo</groupId>
      <artifactId>milo-bom</artifactId>
      <version>${milo.version}</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<dependencies>
  <dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>milo-sdk-pubsub</artifactId>
  </dependency>
</dependencies>
```

## Build the publisher

The complete programs for this walk-through are `UadpPublisherExample` and `UadpSubscriberExample` in `milo-examples/pubsub-examples` (package `org.eclipse.milo.examples.pubsub.udp`). Every snippet below is lifted from them. (`ushort(...)` and `uint(...)` are static imports from `org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned`.)

Start with what you want to publish. A `PublishedDataSetConfig` is the schema: a named, ordered list of fields. Order matters: on the wire, a UADP key frame carries a complete snapshot of every field as raw values, positionally, with no names — so the field order you declare here is the wire order. (A key frame's counterpart is the delta frame, which carries only the fields that changed; Milo publishes and decodes both, but at the default `keyFrameCount` of 1 — which this page never changes — every published frame is a key frame. See [Limitations and interop](limitations-and-interop.md).)

```java
private static PublishedDataSetConfig telemetryDataSet() {
  return PublishedDataSetConfig.builder("telemetry")
      .field(FieldDefinition.builder("temperature").dataType(NodeIds.Double).build())
      .field(FieldDefinition.builder("status").dataType(NodeIds.Boolean).build())
      .field(FieldDefinition.builder("counter").dataType(NodeIds.UInt32).build())
      .build();
}
```

Next, where and how it goes out. Three pieces nest inside a `PubSubConfig`:

- a connection — the transport endpoint, carrying a `PublisherId`, the identity that tells subscribers who this is (required on any connection that has writer groups);
- a writer group — the publishing schedule; everything in the group goes out on one shared cycle at `publishingInterval`;
- a dataset writer — binds one published dataset into the group; its `dataSetWriterId` names that stream for subscribers.

```java
PublishedDataSetConfig telemetry = telemetryDataSet();

WriterGroupConfig writerGroup =
    WriterGroupConfig.builder("demo")
        .writerGroupId(ushort(1))
        .publishingInterval(Duration.ofMillis(500))
        .dataSetWriter(
            DataSetWriterConfig.builder("telemetry-writer")
                .dataSet(telemetry.ref())
                .dataSetWriterId(ushort(1))
                .build())
        .build();

return PubSubConfig.builder()
    .publishedDataSet(telemetry)
    .connection(
        PubSubConnectionConfig.udp("publisher-connection")
            .publisherId(PublisherId.uint16(ushort(1001)))
            .address(UdpDatagramAddress.unicast("127.0.0.1", 15100))
            .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", 15101))
            .writerGroup(writerGroup)
            .build())
    .build();
```

The one non-obvious line is `discoveryAddress`, and it matters on every UDP connection you ever configure. A UDP connection with writer groups also opens a discovery channel that answers metadata probes from subscribers (metadata — the field names and types a subscriber needs to decode what it receives — is covered in the subscriber section below). Left unconfigured, that channel binds the Part 14 default — the well-known port 4840 plus a join of multicast group 224.0.2.14 — and your program quietly attaches itself to the real network. Pin it to a loopback port instead. And because the discovery address is a socket the engine binds, two processes on one host need distinct discovery ports: this pair uses 15101 for the publisher and 15102 for the subscriber.

The config so far declares that a dataset named "telemetry" exists; it says nothing about where values come from. Milo's publishing model is pull: you bind a `PublishedDataSetSource` — a function from `PublishedDataSetReadContext` to `DataSetSnapshot` — and the engine calls it once per publish cycle. You write no threads and no timers; producing changing values is just returning different snapshots when asked.

```java
PubSubBindings bindings =
    PubSubBindings.builder().source(telemetryDataSet().ref(), this::readTelemetry).build();

PubSubService service = PubSubService.create(createConfig(), bindings);

service.startup().get();
```

```java
private DataSetSnapshot readTelemetry(PublishedDataSetReadContext context) {
  long n = cycle.getAndIncrement();

  // one full sine period every 120 cycles (60 seconds at 500ms)
  double temperature = 20.0 + 5.0 * Math.sin(2.0 * Math.PI * n / 120.0);
  boolean status = temperature >= 20.0;

  // ... (log every 20th cycle) ...

  return DataSetSnapshot.builder(context)
      .field("temperature", new DataValue(Variant.ofDouble(temperature)))
      .field("status", new DataValue(Variant.ofBoolean(status)))
      .field("counter", new DataValue(Variant.ofUInt32(uint(n))))
      .build();
}
```

The snapshot builder accepts only the field names declared in the dataset — an unknown name throws. A declared field you don't supply publishes as `Bad_NoData`. If you forget to bind a source at all, `startup()` fails with `Bad_ConfigurationError` naming the writer; if your source throws at runtime, the cycle still publishes, with every field carrying `Bad_InternalError`.

Once `startup()` completes, the connection is open and the group is publishing. Now a detail every standalone PubSub `main` needs: Milo's default executors are daemon threads. A running `PubSubService` does not keep the JVM alive — if `main` returns after startup, the process exits immediately. The examples block `main` on a future that a shutdown hook completes:

```java
// Run until Ctrl-C: the shutdown hook completes the future and main exits.
final CompletableFuture<Void> future = new CompletableFuture<>();

Runtime.getRuntime().addShutdownHook(new Thread(() -> future.complete(null)));

future.get();
```

When you embed PubSub in an application that already has a lifecycle, use `service.shutdown()` (asynchronous; completes after in-flight listener events drain) or `close()` — `PubSubService` is `AutoCloseable`, and both are idempotent.

## Build the subscriber

The subscriber's config mirrors the connection — same data address, but with the roles reversed: the publisher sends to `127.0.0.1:15100`, the subscriber binds it. Instead of writer groups it holds a reader group with one dataset reader, and that reader introduces the two subscriber-side concepts: filters and metadata.

```java
DataSetMetaDataConfig telemetryMetaData =
    DataSetMetaDataConfig.builder("telemetry")
        .field("temperature", NodeIds.Double)
        .field("status", NodeIds.Boolean)
        .field("counter", NodeIds.UInt32)
        .build();

ReaderGroupConfig readerGroup =
    ReaderGroupConfig.builder("demo")
        .dataSetReader(
            DataSetReaderConfig.builder("telemetry-reader")
                .publisherId(PublisherId.uint16(ushort(1001)))
                .dataSetWriterId(ushort(1))
                .dataSetMetaData(telemetryMetaData)
                .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                .build())
        .build();

return PubSubConfig.builder()
    .connection(
        PubSubConnectionConfig.udp("subscriber-connection")
            .address(UdpDatagramAddress.unicast("127.0.0.1", 15100))
            .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", 15102))
            .readerGroup(readerGroup)
            .build())
    .build();
```

Metadata first. UADP messages carry values, not names or types, so the reader needs `DataSetMetaData` to turn positional wire values back into named fields. `MetadataPolicy.REQUIRE_CONFIGURED` means this reader decodes exclusively against the metadata configured here, and that metadata must match the publisher's `PublishedDataSet` declaration in field names, order, and types. You do not need to pin configuration versions or field UUIDs — both builders default the `ConfigurationVersion` to the same value, so versions match by default. (Other policies, `ACCEPT_DISCOVERED` and `REQUEST_IF_MISSING`, let the reader obtain metadata from the publisher at runtime instead; see [Metadata and discovery](metadata-and-discovery.md).)

Now the filters. The reader matches incoming messages on `publisherId` and `dataSetWriterId`; unset filters are wildcards. Notice what is deliberately missing: there is no `.writerGroupId(...)` filter, even though the publisher configured `writerGroupId(ushort(1))`. With the default UADP message settings the GroupHeader is omitted from the wire, so the WriterGroupId is never transmitted — and per the Part 14 matching rules (6.2.9), an identifier filter is checked only when that identifier is present in the message. A configured `writerGroupId` filter is therefore silently ignored: no error, but no filtering either — the reader behaves exactly as if the filter were unset. It takes effect only when the publisher's writer group message mask explicitly includes the GroupHeader and WriterGroupId bits, so leave it unset until then. The `publisherId` and `dataSetWriterId` filters work under the defaults because the default mask does include the PublisherId and the payload header.

Two smaller things to notice about the connection: it sets no `publisherId`, because that is required only on connections that have writer groups; and its `discoveryAddress` pin is purely defensive — a connection with only `REQUIRE_CONFIGURED` readers never opens a discovery channel in this release, so nothing actually listens on 15102. The pin just guarantees nothing ever falls back to 4840/224.0.2.14.

Receiving is push, not poll: decoded DataSets are delivered to listeners as they arrive. (A naming note: in these pages, lowercase "dataset" means the configured schema, like the `PublishedDataSetConfig` above, while "DataSet" means a decoded instance delivered to a listener — following API names like `DataSetReceivedEvent`.)

```java
PubSubService service = PubSubService.create(createConfig());

// DataSets are pushed to listeners as they are received and decoded; there is no polling.
// This registers globally; listeners can also be registered per reader, or pre-wired with
// PubSubBindings before startup.
service.addDataSetListener(this::onDataSet);

service.startup().get();
```

```java
private void onDataSet(DataSetReceivedEvent event) {
  String fields =
      event.fieldsByName().entrySet().stream()
          .map(e -> e.getKey() + "=" + e.getValue().value().value())
          .collect(Collectors.joining(", "));

  logger.info(
      "[received] dataSet={} publisherId={} dataSetWriterId={} fields: {}",
      event.dataSetName(),
      event.publisherId().toCanonicalString(),
      event.dataSetWriterId(),
      fields);
}
```

Getting from a received field to a plain Java value is an unwrap chain worth seeing once, explicitly. `event.fields()` returns the fields in wire order as `DataSetFieldValue` objects, and the full chain is three hops: `field.value()` is a `DataValue`, `.value()` on that is a `Variant`, and `.value()` on the `Variant` is the raw `Object` — `field.value().value().value()`. The snippet above uses `event.fieldsByName()` instead, which flattens the first hop away by returning a `Map<String, DataValue>`, so it unwraps with two: `e.getValue().value().value()`.

One logging tip the snippet also demonstrates: `PublisherId` is a sealed type over records, and the default record `toString()` prints `UInt16Id[value=1001]`. Use `toCanonicalString()` anywhere a human will read the output.

## Run it

You need a checkout of Milo and JDK 17 or newer. From the repository root, build once:

```sh
mvn -q -pl milo-examples/pubsub-examples -am install -DskipTests
```

Start the subscriber in one terminal (subscriber first, so no messages are missed — UDP buffers nothing, and datagrams published before the subscriber binds the port are simply lost, with no error on either side):

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.UadpSubscriberExample
```

Then the publisher in a second terminal:

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.UadpPublisherExample
```

> Do not put `-q` on the `exec:java` commands. `exec:java` runs the example inside the Maven JVM, so the example's SLF4J logging binds to Maven's own logger, and `-q` caps that logger at ERROR. The example runs perfectly — socket bound, data flowing — but prints nothing, which looks exactly like a hang. `-q` belongs on the build step only.

Maven and JVM startup dominate: expect roughly ten seconds before each program's startup line appears. Each log line is prefixed by Maven's logger format (`[thread] INFO logger - ...`); trimming that prefix, the publisher prints one startup line and then a `[publishing]` line every 20 cycles (10 seconds):

```
UADP publisher started: publishing dataset "telemetry" to opc.udp://127.0.0.1:15100 every 500ms, Ctrl-C to exit
[publishing] cycle=0 temperature=20.00 status=true counter=0
[publishing] cycle=20 temperature=24.33 status=true counter=20
```

The subscriber prints its startup line, then — once the publisher is up — two `[received]` lines per second, with the counter incrementing and the temperature tracing a sine wave:

```
UADP subscriber started: listening on opc.udp://127.0.0.1:15100 for dataset "telemetry" from publisher 1001, Ctrl-C to exit
[received] dataSet=telemetry publisherId=1001 dataSetWriterId=1 fields: temperature=20.0, status=true, counter=0
[received] dataSet=telemetry publisherId=1001 dataSetWriterId=1 fields: temperature=20.26167978121472, status=true, counter=1
[received] dataSet=telemetry publisherId=1001 dataSetWriterId=1 fields: temperature=20.522642316338267, status=true, counter=2
```

Ctrl-C stops each side cleanly via the shutdown hook.

## Where to go next

- [Structuring and sizing](structuring-and-sizing.md) — growing past one writer: what connections, groups, and writers each own, how multiple writers pack into one NetworkMessage, and what bounds dataset size; see `MultiGroupPublisherExample` and `MultiGroupSubscriberExample`.
- [Configuration model](configuration.md) — the full builder graph: message settings and content masks, metadata policies, keep-alives, receive timeouts, and Part 14 wire-config round-trip.
- [Metadata and discovery](metadata-and-discovery.md) — how subscribers learn field names and types: the three `MetadataPolicy` values, multicast data planes with zero-config metadata via `REQUEST_IF_MISSING` probing (see `DiscoveryPublisherExample` and `DiscoverySubscriberExample`), and retained MQTT metadata.
- [MQTT transport](mqtt.md) — broker connections with UADP or JSON payloads, topic derivation, and retained metadata; the `mqtt/` examples include an embeddable broker so you need no external infrastructure.
- [Server integration](server-integration.md) — `ServerPubSub`: publish address-space node values automatically and write received DataSets into target variables; see the `server/` examples.
- [Operations](operations.md) — the component state machine, the receive-timeout watchdog, diagnostics, and replacing config on a live service with `update(...)`; see `ReconfigureExample`.
- [Examples](examples.md) — the full catalog of runnable example programs, their verified run commands, and the ports and identifiers they use.
- [Limitations and interop](limitations-and-interop.md) — what each module supports, what is rejected versus silently inert, and interop status.

All example programs referenced here live in `milo-examples/pubsub-examples` under `org.eclipse.milo.examples.pubsub`.
