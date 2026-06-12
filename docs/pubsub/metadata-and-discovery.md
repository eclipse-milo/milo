# Metadata and Discovery

How a subscriber learns the names and types of the fields it receives: configure them locally, accept them from the wire, or actively request them via UDP discovery. Read this page when you are writing a subscriber and want to decide between `REQUIRE_CONFIGURED`, `ACCEPT_DISCOVERED`, and `REQUEST_IF_MISSING` — or when discovered field names aren't showing up and you want to know why.

## Why metadata exists

A UADP key frame on the wire is essentially a list of values in positions: value 0, value 1, value 2. Nothing in the data message says that position 0 is `temperature` and is a `Double`. That description lives in a separate structure, the DataSetMetaData: the dataset's name, its field names, types, and order, and a ConfigurationVersion that lets a subscriber detect when the publisher's dataset shape has changed.

A reader can get this description two ways:

- locally, from a `DataSetMetaDataConfig` you write into the reader's configuration, or
- from the publisher, as a metadata message — a UDP discovery announcement, or a retained `ua-metadata` document on an MQTT broker.

Which sources a reader uses, and which it trusts, is controlled by the reader's `MetadataPolicy`.

## The three metadata policies

`DataSetReaderConfig.Builder.metadataPolicy(...)` takes one of three values from `org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy`:

`REQUIRE_CONFIGURED` (the default) decodes exclusively against the locally configured `DataSetMetaDataConfig`. Metadata that arrives from the wire is reported to `MetaDataReceivedEvent` listeners but never applied to decoding. Messages whose ConfigurationVersion major version is on the wire and mismatches are dropped and counted in diagnostics.

`ACCEPT_DISCOVERED` applies metadata that happens to arrive — discovery announcements on UDP, retained `ua-metadata` documents on MQTT — but never asks for it. Discovered metadata, once received, takes precedence over any configured metadata for decoding.

`REQUEST_IF_MISSING` does everything `ACCEPT_DISCOVERED` does, and on UDP connections additionally probes the discovery address for metadata when the reader has none. A reader whose PublisherId or DataSetWriterId filter is a wildcard defers probing until a first matching data message reveals the identifiers to ask about. On MQTT connections there is nothing to probe (see [Metadata over MQTT](#metadata-over-mqtt) below).

One thing `MetaDataReceivedEvent` is not: a signal that metadata was applied. The event fires for every metadata message a reader's filters match, regardless of policy — with `REQUIRE_CONFIGURED` it is notification only.

## Metadata does not gate data delivery

This is the caveat that surprises people. A reader with `ACCEPT_DISCOVERED` or `REQUEST_IF_MISSING` and no configured metadata still delivers DataSets the moment data arrives — it does not wait for metadata. Until metadata is applied, decoded fields carry synthetic positional names and the dataset has no name. Observed live with the discovery example pair, a pre-metadata delivery looks like this:

```
[received] dataSet=null Field_0=..., Field_1=..., Field_2=OK
```

You can recognize this state programmatically: `DataSetReceivedEvent.dataSetName()` returns `null`, and the field names follow the `Field_<index>` pattern. Once the announcement arrives and is applied, subsequent events carry the real dataset and field names.

If your listener keys logic on field names, either tolerate the positional names, or hold off processing until `dataSetName()` is non-null (or until your `MetaDataReceivedEvent` listener has fired). Whether you ever see this window depends on timing: in one verified run the announcement beat the first data message and no `Field_N` line appeared; in another the first `[received]` line was positional.

## Configured metadata

With `REQUIRE_CONFIGURED`, the reader's `DataSetMetaDataConfig` must mirror the publisher's `PublishedDataSetConfig`: same field names, same order, same types. From `UadpSubscriberExample`:

```java
// REQUIRE_CONFIGURED means this reader decodes exclusively against this metadata; field
// names, order, and types must match the publisher's PublishedDataSet declaration.
DataSetMetaDataConfig telemetryMetaData =
    DataSetMetaDataConfig.builder("telemetry")
        .field("temperature", NodeIds.Double)
        .field("status", NodeIds.Boolean)
        .field("counter", NodeIds.UInt32)
        .build();
```

That is all the matching you need. `PublishedDataSetConfig.Builder` and `DataSetMetaDataConfig.Builder` both default the ConfigurationVersion to (1,1), so a defaults-only publisher and a defaults-only reader already agree and the version gate passes. You may run across code that also pins matching `dataSetFieldId` UUIDs on both sides — that is not a requirement. UADP key frames decode by field position, and the version check needs no help when both builders use their defaults.

Two related notes on the version gate:

- The major-version check only applies when the publisher's DataSetMessage content mask actually puts the major version on the wire. The default UADP masks carry only the minor version, so defaults-only configs deliver to a `REQUIRE_CONFIGURED` reader without any version configuration at all.
- If you do set `configurationVersion(...)` explicitly, set it consistently on both sides (the publisher's `PublishedDataSetConfig` and the reader's `DataSetMetaDataConfig`), or mismatched messages will be silently dropped into diagnostics counters when the major version is on the wire.

## UDP discovery

On UDP connections, discovery is built in and largely automatic.

Every UDP connection that has writer groups runs a metadata responder — zero configuration. The responder listens on the connection's discovery address, answers DataSetMetaData probes addressed to its PublisherId with one announcement per requested DataSetWriterId, answers requests for unknown ids with a `Bad_NotFound` denial, and pushes one unsolicited announcement when a live writer's dataset metadata changes through reconfiguration. A denial stops the requesting reader's probe loop and is recorded in that reader's diagnostics.

On the subscriber side, a `REQUEST_IF_MISSING` reader without metadata probes that same discovery address. Per Part 14, the first probe is delayed by a random 100–500 ms, and retries back off with doubling intervals from 500 ms up to a 60 s cap. Combined with process startup, this means the first fully-named DataSet can take a few seconds to appear — that is normal, not a misconfiguration.

One behavior to design for: the responder sends every announcement to both the discovery address and the connection's data address. This is deliberate, for compatibility with third-party stacks (UA-.NETStandard subscribers listen for announcements on the data address only). A subscriber joined to both addresses — as in the example below — therefore hears the same announcement twice, and `MetaDataReceivedEvent` fires twice. There is no deduplication; write metadata listeners to be idempotent.

A few configuration facts worth knowing:

- If you don't set `discoveryAddress(...)`, the engine uses the Part 14 default `opc.udp://224.0.2.14:4840` — binding well-known port 4840 and joining a multicast group. Always pin an explicit discovery address in development so demos and tests don't touch the default.
- The discovery channel only opens when it's needed: the connection has writer groups (responder), or some reader uses `REQUEST_IF_MISSING` (probing). A subscriber-only connection whose readers are all `REQUIRE_CONFIGURED` or `ACCEPT_DISCOVERED` never binds its discovery address — but pin it anyway, so a later policy change can't silently bind 4840.
- Discovery messages are capped at 4096 bytes (not configurable). A metadata document too large to encode within that is not announced; the failure lands in diagnostics.

### Walkthrough: the multicast discovery pair

`DiscoveryPublisherExample` and `DiscoverySubscriberExample` (in `milo-examples/pubsub-examples`, package `org.eclipse.milo.examples.pubsub.udp`) demonstrate the whole loop: a publisher multicasting a three-field dataset and answering probes, and a subscriber that configures no metadata at all and learns the field names by probing.

The publisher declares the dataset — field order defines wire order — and gives the connection both a data and a discovery address. Both are multicast groups, joined on the loopback interface by default so no traffic leaves the machine:

```java
PublishedDataSetConfig dataSet =
    PublishedDataSetConfig.builder("telemetry")
        .field(FieldDefinition.builder("temperature").dataType(NodeIds.Double).build())
        .field(FieldDefinition.builder("humidity").dataType(NodeIds.Double).build())
        .field(FieldDefinition.builder("status").dataType(NodeIds.String).build())
        .configurationVersion(uint(1), uint(0))
        .build();

UdpDatagramAddress dataAddress =
    UdpDatagramAddress.multicast(DATA_GROUP, DATA_PORT).networkInterface(networkInterface);

// The discovery address is shared with the subscriber. A multicast group lets both
// processes bind the same port (SO_REUSEADDR) and join the same group, even on one host;
// configuring it explicitly also keeps the engine off the Part 14 default 224.0.2.14:4840.
UdpDatagramAddress discoveryAddress =
    UdpDatagramAddress.multicast(DISCOVERY_GROUP, DISCOVERY_PORT)
        .networkInterface(networkInterface);
```

The shared multicast discovery group is the detail that lets two co-located processes coexist. With unicast discovery addresses, each process on a host needs its own port (two processes can't bind the same unicast port); with a multicast group, both join `239.255.20.2:15111` and both hear the same datagrams. The publisher's connection wires it together — note there is no discovery-specific writer configuration anywhere; having a writer group is what activates the responder:

```java
PubSubConfig config =
    PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp("udp-multicast")
                .publisherId(PUBLISHER_ID)
                .address(dataAddress)
                .discoveryAddress(discoveryAddress)
                .writerGroup(
                    WriterGroupConfig.builder("demo")
                        .writerGroupId(ushort(1))
                        .publishingInterval(Duration.ofMillis(1000))
                        .dataSetWriter(
                            DataSetWriterConfig.builder("demo-writer")
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(ushort(1))
                                .build())
                        .build())
                .build())
        .build();
```

The subscriber's reader is the interesting half — no `dataSetMetaData(...)` at all:

```java
// No dataSetMetaData is configured: REQUEST_IF_MISSING makes the reader probe the
// discovery address for it. The first probe is randomly delayed 100-500 ms and retried
// with doubling intervals, so metadata (and with it the first fully-named DataSet) can
// take a few seconds to arrive.
//
// No writerGroupId filter is set: the publisher uses the default UADP-Dynamic masks,
// which omit the GroupHeader (and with it the WriterGroupId) from the wire, so filtering
// on it would discard every message.
DataSetReaderConfig reader =
    DataSetReaderConfig.builder("demo-reader")
        .publisherId(PUBLISHER_ID)
        .dataSetWriterId(ushort(1))
        .metadataPolicy(MetadataPolicy.REQUEST_IF_MISSING)
        .build();
```

It registers two listeners before startup, so no event is missed — one for metadata announcements, one for decoded DataSets:

```java
service.addMetaDataListener(this::onMetaDataReceived);
service.addDataSetListener(
    new DataSetReaderRef("udp-multicast", "demo", "demo-reader"), this::onDataSetReceived);
```

The metadata listener reads the announced fields from the event. `getFields()` is nullable, so guard it:

```java
private void onMetaDataReceived(MetaDataReceivedEvent event) {
  FieldMetaData[] fields = event.metaData().getFields();

  String fieldNames =
      fields == null
          ? "(none)"
          : Arrays.stream(fields).map(FieldMetaData::getName).collect(Collectors.joining(", "));

  logger.info(
      "discovery delivered metadata: dataSet={}, version={}.{}, fields=[{}]",
      event.dataSetName(),
      event.configurationVersion().getMajorVersion(),
      event.configurationVersion().getMinorVersion(),
      fieldNames);
}
```

### Running the pair

Build once from the repository root, then run each example in its own terminal:

```
mvn -q -pl milo-examples/pubsub-examples -am install -DskipTests
```

Terminal 1 (start first; order isn't critical, the subscriber retries its probes):

```
mvn -pl milo-examples/pubsub-examples exec:java \
    -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.DiscoveryPublisherExample
```

Terminal 2:

```
mvn -pl milo-examples/pubsub-examples exec:java \
    -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.DiscoverySubscriberExample
```

Do not add `-q` to the `exec:java` commands. `exec:java` runs the example inside the Maven JVM, where SLF4J binds to Maven's own logger, and `-q` caps that logger at ERROR — the example runs perfectly but prints nothing, which looks exactly like a hang. Keep `-q` on the build step only. For the same reason, the example's log lines appear in Maven's format (`[thread] INFO logger - message`); only the message portion below will match your output.

Publisher output (verified live):

```
publishing 'telemetry' as publisher UInt16Id[value=1002] to opc.udp://239.255.20.1:15110 (discovery responder on opc.udp://239.255.20.2:15111)
published cycle 1: temperature=20.50, humidity=49.99, status=OK
```

Subscriber output (verified live; note the metadata line appearing twice — both addresses, as described above):

```
subscribed to opc.udp://239.255.20.1:15110 as reader 'demo-reader', probing opc.udp://239.255.20.2:15111 for metadata
discovery delivered metadata: dataSet=telemetry, version=1.0, fields=[temperature, humidity, status]
discovery delivered metadata: dataSet=telemetry, version=1.0, fields=[temperature, humidity, status]
[received] dataSet=telemetry temperature=23.728526060883603, humidity=44.08487440884157, status=OK
[received] dataSet=telemetry temperature=23.377315902755754, humidity=43.62357754476674, status=OK
```

The named fields are the proof: this reader configured no metadata, so `temperature`, `humidity`, and `status` could only have come from the discovery announcement. If a data message beats the announcement, the first line or two may instead read `[received] dataSet=null Field_0=..., Field_1=..., Field_2=OK` until the metadata is applied.

Both examples take an optional network interface address argument (e.g. `-Dexec.args="192.0.2.10"` on both) to run across machines instead of loopback.

## Metadata over MQTT

There is no probing on broker connections — and none is needed. On MQTT, the publisher side handles metadata automatically: at writer activation the engine publishes a retained, QoS 1 `ua-metadata` document to the writer's metadata topic, with no configuration required. Because the broker retains it, any subscriber that connects later gets the document replayed immediately on subscribe.

The subscriber-side recipe is therefore `ACCEPT_DISCOVERED` plus a `metaDataQueueName`, as in `MqttJsonSubscriberExample`:

```java
DataSetReaderConfig reader =
    DataSetReaderConfig.builder("reader")
        // UInt16 filter matched against the JSON wire string "3001" by canonical form
        .publisherId(PublisherId.uint16(ushort(3001)))
        // no writerGroupId filter: the default JSON NetworkMessage content mask does not
        // put the WriterGroupId on the wire
        .dataSetWriterId(ushort(1))
        .settings(JsonDataSetReaderSettings.builder().build())
        // no dataSetMetaData configured: field definitions come from the retained
        // ua-metadata document replayed from the metadata queue below
        .metadataPolicy(MetadataPolicy.ACCEPT_DISCOVERED)
        .brokerTransport(
            BrokerTransportSettings.builder()
                .queueName(DATA_TOPIC)
                .metaDataQueueName(META_TOPIC)
                .build())
        .build();
```

where the topics match the publisher's derived Part 14 topic tree:

```java
private static final String DATA_TOPIC = "opcua/json/data/3001/demo";
private static final String META_TOPIC = "opcua/json/metadata/3001/demo/writer";
```

Mind the asymmetry: the data topic stops at the writer group name, but the metadata topic additionally embeds the DataSetWriter's name (`writer` here). Renaming a writer on the publisher silently changes its metadata topic and breaks every subscriber's configured `metaDataQueueName`.

One strictness caveat when the publisher is a third-party stack: Milo decodes `ua-metadata` documents strictly, and an unknown member inside the `MetaData` object — a vendor extension, for example — rejects the whole document with `Bad_DecodingError`. The rejection is visible in diagnostics (it increments the connection's `decodeErrors` counter and sets `lastError`, via `PubSubService.diagnostics()`), but behaviorally it looks like the metadata never arrived: no `MetaDataReceivedEvent` fires, the metadata is never applied, and decoded fields keep the positional `Field_<index>` names indefinitely. If a peer's metadata trips this, switch the reader to `REQUIRE_CONFIGURED` with a locally written `DataSetMetaDataConfig` that mirrors the peer's dataset.

Setting `REQUEST_IF_MISSING` on an MQTT reader is not rejected, but the request half is silently inert — there is no discovery runtime on broker connections, so it behaves exactly like `ACCEPT_DISCOVERED`. Use `ACCEPT_DISCOVERED` on MQTT to say what you mean.

Run the `MqttJsonPublisherExample` / `MqttJsonSubscriberExample` pair (plus `EmbeddedBrokerExample` or any external broker) to see this live; the subscriber and publisher can start in either order because the broker retains the metadata. Verified subscriber output:

```
metadata received: dataSet=demo-dataset version=1.1 fields=[temperature, status]
[received] publisherId=3001 dataSet=demo-dataset temperature=20.522642316338267, status=running
```

The `version=1.1` is the auto-populated `PublishedDataSetConfig` default mentioned earlier — the publisher example never sets one.

## What discovery does not do

Milo's discovery implementation covers DataSetMetaData request/response and on-change announcements. Be aware of what is not there:

- No periodic announcements. The DiscoveryAnnounceRate mechanism is not implemented; the responder answers probes and pushes once on reconfiguration changes, nothing more. Subscribers that need metadata must ask (or use the retained-message pattern on MQTT).
- Only the DataSetMetaData discovery type. The other Part 14 discovery request and information types — PublisherEndpoints, DataSetWriterConfiguration, FindApplications — are not implemented. Incoming requests or announcements of those types are not errors; they are debug-logged and ignored (silently inert, not rejected).
- Discovery is UDP/UADP only. Discovery channels always go through the built-in UDP transport and the built-in UADP mapping, even when custom transport providers or message mappings are registered for the data plane. Broker connections have no discovery runtime at all; retained metadata is the broker-world equivalent.
- The 4096-byte discovery message limit is fixed; there is no DiscoveryMaxMessageSize configuration.

For the configuration model these policies hang off of — readers, groups, connections — see the [configuration model](configuration.md) page.
