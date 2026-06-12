# Configuration Model

This page is a reference for the PubSub configuration model in
`org.eclipse.milo.opcua.sdk.pubsub.config`, shipped in `org.eclipse.milo:milo-sdk-pubsub`. Read it
when you are authoring a `PubSubConfig` in Java and want to know what the builders validate, what
the defaults actually put on the wire, and which knobs do nothing yet.

The model is a plain object graph: a `PubSubConfig` holds `PublishedDataSetConfig`s and
`PubSubConnectionConfig`s; each connection holds `WriterGroupConfig`s (with `DataSetWriterConfig`s)
and `ReaderGroupConfig`s (with `DataSetReaderConfig`s). `PubSubConnectionConfig` is sealed: UDP
(`PubSubConnectionConfig.udp(name)`) and MQTT (`PubSubConnectionConfig.mqtt(name)`) are the only
connection types in this release — Ethernet and AMQP connections are inexpressible (see
[limitations](limitations-and-interop.md)). This page covers what each builder accepts; which
concerns belong at each level of the graph, and what bounds how large a dataset can be, is the
subject of [Structuring and sizing](structuring-and-sizing.md).

## Immutability and builders

Every config type is immutable. Each has a static nested `Builder`, created with `builder()` (or
`builder(name)` where a name is required), and a `toBuilder()` that copies the current values into
a fresh builder. `build()` validates and throws on bad input; a config object that exists is
structurally valid.

There is one trap: builders are append-only. `toBuilder()` pre-populates the collections, and the
collection methods — `connection(...)`, `writerGroup(...)`, `publishedDataSet(...)`,
`dataSetWriter(...)`, and so on — always add, never replace. So the natural-looking transform

```java
service.update(cfg -> cfg.toBuilder().connection(modifiedConnection).build());
```

produces a config with two connections of the same name and fails validation. The working idiom for
"the same config with one thing changed" is a config-factory method parameterized by the value you
want to change, which rebuilds the whole graph each time. `ReconfigureExample` uses exactly this to
swap the publishing interval of a live service:

```java
ReconfigureResult result = publisher.update(current -> publisherConfig(PHASE_2_INTERVAL));
```

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
              // Pinned to loopback so the engine never binds the well-known port 4840 or
              // joins the default 224.0.2.14 multicast group.
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

Because configs compare by value, the reconfigure differ leaves every subtree that came out
identical alone — only the parts that actually changed are restarted. What "restarted" means
operationally — the two reconfigure modes, restart scoping, and what happens to diagnostics
counters — is covered in [Operations](operations.md#live-reconfiguration).

## When validation happens

Configuration problems surface at three distinct moments. Knowing which is which saves debugging
time.

At build time, `build()` — and the `UdpDatagramAddress` factories — throw
`PubSubConfigValidationException` (unchecked) with a message naming the offending element. This
catches structural problems: a blank name (`"writer group: name must not be blank"`), a missing UDP
address, a missing or zero `writerGroupId` or `dataSetWriterId`, a non-positive
`publishingInterval`, a zero or negative `keepAliveTime`, an out-of-range port or blank host on a
`UdpDatagramAddress`, and `Json*` message settings anywhere on a UDP connection (`"... uses JSON
message settings, which are not valid on a UDP connection"`).

At startup time, `PubSubService.startup()` fails (the returned future completes exceptionally) with
a `UaException` for problems that span the config and its environment. `Bad_ConfigurationError`
covers:

- an enabled dataset writer whose published dataset has no bound `PublishedDataSetSource`
- message settings that resolve to a mapping name with no registered `MessageMappingProvider`
- an enabled connection that no registered `TransportProvider` supports — the classic case is an
  MQTT connection without `MqttTransportProvider` registered on the `PubSubServiceConfig`
- an enabled reader on a broker (MQTT) connection without a data `queueName` in its
  `BrokerTransportSettings`
- a connection that has writer groups but no `publisherId`

`Bad_NotSupported` is reserved for features that are recognized but not implemented in this
release: any enabled group whose `MessageSecurityConfig` mode is anything other than `None`, and
MQTT-over-WebSocket broker URIs. Disabled groups with non-`None` security modes are tolerated so
that configs can round-trip.

At reconfigure time, `reconfigure(...)` and `update(...)` run the same startup checks before
applying anything. Failures throw an unchecked `UaRuntimeException` with `Bad_ConfigurationError`,
no part of the change is applied, and existing component handles stay valid.

## PublisherId

Part 14 allows five PublisherId wire types, and `PublisherId` is a sealed interface with one
factory per type: `PublisherId.ubyte(...)`, `PublisherId.uint16(...)`, `PublisherId.uint32(...)`,
`PublisherId.uint64(...)`, and `PublisherId.string(...)`.

Ids of different types match by canonical string form: the value itself for a string id, the
decimal representation for the unsigned integer ids. This is the form Part 14 mandates wherever a
PublisherId travels as a string — the JSON message mapping and the broker topic tree — and it is
what makes cross-type matching work in practice: JSON always decodes the PublisherId as a string
(the wire carries `"3001"` even for a numeric id), and a reader configured with
`PublisherId.uint16(ushort(3001))` still matches it.

Use `toCanonicalString()` whenever you log a PublisherId. The records inherit the default record
`toString`, so a naive log statement prints `UInt16Id[value=1001]` instead of `1001`. The
subscriber examples do this consistently, e.g. `event.publisherId().toCanonicalString()` in
`UadpSubscriberExample`.

A connection needs a `publisherId` only if it has writer groups; a reader-only connection validates
and runs fine without one. A connection that has writer groups but no `publisherId` fails
`startup()` with `Bad_ConfigurationError`.

## UDP addresses

`UdpDatagramAddress` has two factories — `unicast(host, port)` and `multicast(group, port)` — plus
`networkInterface(name)`, which returns a copy restricted to a network interface (by name or
address). The connection's `address(...)` is the data plane: the publisher sends to it; the
subscriber binds it (unicast) or binds the port and joins the group (multicast). A unicast data
address is shared between both sides — one sends to the same host:port the other binds.

From `DiscoveryPublisherExample`, a multicast data plane with an explicit interface:

```java
UdpDatagramAddress dataAddress =
    UdpDatagramAddress.multicast(DATA_GROUP, DATA_PORT).networkInterface(networkInterface);

// The discovery address is shared with the subscriber. A multicast group lets both
// processes bind the same port (SO_REUSEADDR) and join the same group, even on one host;
// configuring it explicitly also keeps the engine off the Part 14 default 224.0.2.14:4840.
UdpDatagramAddress discoveryAddress =
    UdpDatagramAddress.multicast(DISCOVERY_GROUP, DISCOVERY_PORT)
        .networkInterface(networkInterface);
```

`discoveryAddress(...)` is where the connection's UADP discovery traffic (metadata probes and
announcements) is sent and received. When it is unset, the runtime applies the Part 14 default,
`opc.udp://224.0.2.14:4840` — meaning your process binds the well-known port 4840 and joins that
multicast group. A discovery channel actually opens only when the connection has writer groups
(every publisher is automatically a discovery responder) or has a reader with
`MetadataPolicy.REQUEST_IF_MISSING`; a subscriber whose readers are all `REQUIRE_CONFIGURED` or
`ACCEPT_DISCOVERED` never binds its discovery address today, so a pin there is defensive. Discovery
channels always use the built-in UDP transport, independent of any custom transport provider on the
data plane.

Pin the discovery address explicitly in anything you run on a shared machine or network. Note that
two processes on one host cannot bind the same unicast discovery port — give each its own, as in
`UadpSubscriberExample`:

```java
PubSubConnectionConfig.udp("subscriber-connection")
    .address(UdpDatagramAddress.unicast("127.0.0.1", 15100))
    // Pin the discovery address to a loopback port so nothing in this demo ever
    // binds the Part 14 default opc.udp://224.0.2.14:4840 (well-known port 4840
    // plus a multicast group join). Two processes on one host cannot bind the
    // same discovery port, so the publisher pins 15101 and the subscriber 15102.
    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", 15102))
    .readerGroup(readerGroup)
    .build()
```

A multicast discovery group, by contrast, can be shared by co-located processes, as the
`DiscoveryPublisherExample` snippet above shows.

## MQTT broker addresses

An MQTT connection takes a broker URI: `PubSubConnectionConfig.mqtt(name).brokerUri(uri)`. The
scheme decides TLS and the default port, and it is resolved at startup when the channel opens (the
parsing lives in the `milo-sdk-pubsub-mqtt` module):

| Scheme              | Default port | TLS                                                |
| ------------------- | ------------ | -------------------------------------------------- |
| `mqtt://`           | 1883         | plaintext, unless `BrokerSecurityConfig.isTls()`   |
| `mqtts://`          | 8883         | always                                             |
| `ws://` / `wss://`  | —            | rejected: startup fails with `Bad_NotSupported`    |
| anything else       | —            | rejected: startup fails with `Bad_ConfigurationError` |

A URI with no host also fails startup with `Bad_ConfigurationError`.

On broker connections, writers derive their topic from the Part 14 topic tree
(`<prefix>/<mapping>/data/<publisherId>/<writerGroupName>`) automatically, but readers cannot — the
derivation needs the publisher's writer group name, which a reader does not know. Every enabled
broker reader must therefore set an explicit data `queueName` in its `BrokerTransportSettings`, or
startup (and reconfigure) is rejected with `Bad_ConfigurationError`. `metaDataQueueName` points at
the retained metadata topic, which additionally embeds the publisher's writer name. For a worked
reader configuration that sets both queue names (`MqttJsonSubscriberExample`), see
[metadata over MQTT](metadata-and-discovery.md#metadata-over-mqtt).

## Message settings select the mapping

The runtime decides between the UADP and JSON message mappings by the Java type of the settings
objects, not by a string anywhere in the config. `UadpWriterGroupSettings` /
`UadpDataSetWriterSettings` / `UadpDataSetReaderSettings` select the binary "uadp" mapping;
`JsonWriterGroupSettings` / `JsonDataSetWriterSettings` / `JsonDataSetReaderSettings` select
"json". When you configure nothing, the defaults are UADP at every level — which means a
subscriber-side config contains no visible hint that it is using UADP; the reader's default
settings already are UADP settings.

JSON settings are only valid on MQTT connections. Putting `Json*` settings on any group, writer, or
reader of a UDP connection fails at `build()` time with a `PubSubConfigValidationException` naming
the element.

### UADP defaults and the masks you will actually want

The default UADP masks follow the Part 14 Annex A.2.2 "UADP-Dynamic" header layout, chosen because
it is self-describing and easy for third-party subscribers to consume:

- NetworkMessage content mask: `PublisherId | PayloadHeader`
- DataSetMessage content mask: `Timestamp | Status | MinorVersion | SequenceNumber`

Two consequences of those defaults matter in practice.

First, there is no `GroupHeader` in the default network mask, so the WriterGroupId is never on the
wire. A reader configured with a `writerGroupId(...)` filter is then silently inert: following the
Part 14 matching rules, an identifier filter applies only when the identifier is present in the
received message, so the reader accepts messages from any writer group of the matched publisher.
Either leave the reader's `writerGroupId` filter unset (the PublisherId and DataSetWriterId filters
work under the defaults), or add `GroupHeader` and `WriterGroupId` to the publisher's network mask
so the filter has something to check against.

Second, there is no `MajorVersion` in the default DataSetMessage mask, so a `REQUIRE_CONFIGURED`
reader cannot verify the metadata version: an absent major version decodes as 0 and the version
gate is skipped. Defaults-only configs do deliver to `REQUIRE_CONFIGURED` readers — but if you want
the version check, add `MajorVersion` and `MinorVersion` to the writer's mask.

`ReconfigureExample` defines both non-default masks with the reasons attached:

```java
/**
 * The reader filters on writerGroupId, so the publisher must include GroupHeader + WriterGroupId
 * in the NetworkMessage; the UADP default mask omits the GroupHeader.
 */
private static final UadpWriterGroupSettings GROUP_SETTINGS =
    UadpWriterGroupSettings.builder()
        .networkMessageContentMask(
            UadpNetworkMessageContentMask.of(
                UadpNetworkMessageContentMask.Field.PublisherId,
                UadpNetworkMessageContentMask.Field.GroupHeader,
                UadpNetworkMessageContentMask.Field.WriterGroupId,
                UadpNetworkMessageContentMask.Field.SequenceNumber,
                UadpNetworkMessageContentMask.Field.PayloadHeader))
        .build();

/**
 * MajorVersion + MinorVersion let the {@code REQUIRE_CONFIGURED} reader verify the configuration
 * version of the metadata it decodes against.
 */
private static final UadpDataSetWriterSettings WRITER_SETTINGS =
    UadpDataSetWriterSettings.builder()
        .dataSetMessageContentMask(
            UadpDataSetMessageContentMask.of(
                UadpDataSetMessageContentMask.Field.Timestamp,
                UadpDataSetMessageContentMask.Field.Status,
                UadpDataSetMessageContentMask.Field.MajorVersion,
                UadpDataSetMessageContentMask.Field.MinorVersion,
                UadpDataSetMessageContentMask.Field.SequenceNumber))
        .build();
```

### Field encoding: DataSetFieldContentMask

`DataSetWriterConfig.fieldContentMask(...)` selects how field values are represented inside a
DataSetMessage. The default is an empty mask, which encodes each field as a bare `Variant` — value
only, no status, no timestamps. Setting `StatusCode | SourceTimestamp` switches fields to full
`DataValue` encoding, so each value's status code and source timestamp travel on the wire and
reappear on the subscriber. `ServerTargetPublisherExample` uses this to push status and timestamps
all the way into the subscribing server's nodes:

```java
/**
 * Encode fields as DataValues carrying StatusCode and SourceTimestamp: the subscriber's
 * TargetVariables mapping passes both through into its target nodes, so the status codes and
 * source timestamps visible on the server nodes are the ones produced by this publisher's source.
 */
private static final DataSetFieldContentMask FIELD_MASK =
    DataSetFieldContentMask.of(
        DataSetFieldContentMask.Field.StatusCode, DataSetFieldContentMask.Field.SourceTimestamp);
```

The `RawData` bit is not supported for UADP publishing: the encoder rejects it with
`Bad_NotSupported`, which surfaces per publish cycle as a diagnostics error and a skipped cycle —
startup does not fail. Received RawData messages decode their headers but carry no usable fields.

### JSON defaults

For the JSON mapping, an empty content mask means "use the recommended Part 14 defaults":

- NetworkMessage mask: `NetworkMessageHeader | DataSetMessageHeader | PublisherId`
- DataSetMessage mask: `DataSetWriterId | SequenceNumber | MetaDataVersion | Timestamp | Status |
  FieldEncoding2` (Verbose field encoding, the recommended default)

So the common case is just empty builders, as in `MqttJsonPublisherExample`:

```java
// Json* settings select the "json" mapping; the empty content
// masks select the recommended Part 14 defaults
.messageSettings(JsonWriterGroupSettings.builder().build())
```

The JSON masks have three quirks:

- An all-clear mask is indistinguishable from "unset" and selects the defaults above, so you cannot
  request a fully headerless layout with an empty mask. If you want a structural bit cleared, set a
  non-empty mask without it (a mask with only the `PublisherId` bit, for example, produces the
  headerless payload layout).
- The JSON mapping never puts the numeric WriterGroupId on the wire — there is no mask bit for it —
  so a reader `writerGroupId` filter is silently inert under JSON, the same way it is under the
  default UADP masks: the filter only applies when the identifier is present in the received
  message, and for JSON it never is. Leave the filter unset, as `MqttJsonSubscriberExample` does.
- The default DataSetMessage mask omits `MessageType`, so default-config JSON keep-alive messages
  carry no `"ua-keepalive"` marker on the wire. Decoders, including Milo's, still classify them as
  keep-alives by the absence of a payload.

## Keep-alive and the receive timeout

`WriterGroupConfig.keepAliveTime(Duration)` is nullable and defaults to null: keep-alive messages
are never sent unless you configure it. `build()` rejects zero and negative values, and a Part 14
`KeepAliveTime` of 0 imports as "disabled". Mind the semantics: a keep-alive is emitted only for a
writer that is not Operational (disabled or paused) and has been quiet longer than the keep-alive
time. Operational writers publish a key frame every publishing interval anyway, so a healthy group
never sends keep-alives, and an all-idle group with no `keepAliveTime` sends nothing at all.

On the subscribing side, `DataSetReaderConfig.messageReceiveTimeout(Duration)` is the watchdog that
keep-alives exist to feed. The default is `Duration.ZERO`, which disables it. A positive timeout
arms once the reader reaches Operational; if no message (data, keep-alive, or delta frame) arrives
within the window, the reader transitions to Error with a `Bad_Timeout` diagnostics entry, and the
next accepted message brings it back to Operational.

## Part 14 round-trip and escape hatches

`PubSubConfig` converts to and from the Part 14 wire representation:
`config.toDataType(namespaceTable)` produces a `PubSubConfiguration2DataType`, and
`PubSubConfig.fromDataType(dataType, namespaceTable)` builds a config from one. The
`NamespaceTable` is needed to translate between local NodeIds and the namespace-URI-based field
addresses the config model uses. `fromDataType` throws `PubSubConfigValidationException` for shapes
the model cannot represent — most notably unknown transport profile URIs (an AMQP profile, for
example), since only the UDP/UADP, MQTT/UADP, and MQTT/JSON profiles are supported.

Two escape hatches keep round-trips lossless when the wire config contains settings the typed model
does not cover:

- `rawMessageSettings(ExtensionObject)` exists on groups, writers, and readers, and
  `rawTransportSettings(ExtensionObject)` additionally on connections. When importing,
  non-representable settings structures
  (the UADP timing offsets, for instance) are diverted there whole; when exporting, a non-null raw
  hatch wins over the typed settings. The runtime itself never reads them — they are fidelity-only.
- `property(QualifiedName, Variant)` adds entries that map to and from the Part 14 KeyValuePair
  arrays at each level. Most are carried verbatim, but the MQTT transport does read its connection
  properties: `0:MqttClientId`, `0:MqttVersion` (`"5.0"`, `"3.1.1"`, or `"BestAvailable"`), and
  `0:MqttTopicPrefix`.

One deliberate one-way drop: `BrokerSecurityConfig` (TLS material and broker credentials) is never
serialized by `toDataType` and comes back as defaults from `fromDataType`. Credentials and key
paths do not belong in a Part 14 configuration document.

## Knobs that exist but are inert or rejected today

The config model carries the full Part 14 vocabulary so configs can round-trip, which means it can
express more than this release implements. Where the runtime does not honor a setting, it falls
into one of two camps: rejected (you get an error) or inert (silently ignored). The honest list:

| Knob | Behavior today |
| --- | --- |
| `DataSetWriterConfig.keyFrameCount` > 1 | Inert. Every published DataSetMessage is a key frame; delta frames are decoded when received but never emitted. |
| `WriterGroupConfig.maxNetworkMessageSize` | Inert. No chunking or size limiting exists; a NetworkMessage exceeding the fixed UADP wire limits fails at encode with `Bad_EncodingLimitsExceeded`. |
| `MessageSecurityConfig` with mode `Sign` or `SignAndEncrypt` | Rejected. `startup()` fails with `Bad_NotSupported` for any enabled group; disabled groups are tolerated for round-trip. |
| `SecurityKeyProvider` / `SecurityGroupConfig` (SKS) | Inert. Providers are accepted but never invoked; the one non-silent edge is that binding a provider to an unknown security group name throws `IllegalArgumentException` at create time. |
| UADP timing offsets (`SamplingOffset`, `PublishingOffset`, `ReceiveOffset`, `ProcessingOffset`) | Inert. No typed config fields exist; imported values are preserved in `rawMessageSettings` and never consulted by the publish or receive paths. |
| `BrokerTransportSettings.resourceUri` / `authenticationProfileUri` | Inert. Round-trip only; never read by the runtime. |
| Writer-level `BrokerTransportSettings` QoS without a `queueName` override | Inert for data messages — the writer stays in the group's shared partition. Writer QoS is honored when combined with a queue override. |

See [limitations](limitations-and-interop.md) for the full picture of what is and is not
implemented, including the receive-side behavior for features that are emit-rejected here.
