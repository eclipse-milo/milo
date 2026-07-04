# The MQTT Transport

This page covers `milo-sdk-pubsub-mqtt`, the module that lets a `PubSubService` publish and subscribe through an MQTT broker instead of UDP. It assumes you know the core configuration and lifecycle API from [getting started](getting-started.md); everything broker-specific — registration, topics, QoS, client identity, outages, TLS — is here.

## Dependency and registration

```xml
<dependency>
  <groupId>org.eclipse.milo</groupId>
  <artifactId>milo-sdk-pubsub-mqtt</artifactId>
</dependency>
```

The artifact is managed by `milo-bom` alongside `milo-sdk-pubsub` and `milo-sdk-pubsub-server`. It pulls in the core `milo-sdk-pubsub` module plus the HiveMQ MQTT client, which it uses for all broker communication.

Transports are an explicit registry — there is no ServiceLoader auto-discovery, so having the jar on the classpath does nothing by itself. Registering `MqttTransportProvider` on the service config is the one line that wires MQTT in. From `MqttUadpPublisherExample`:

```java
// MQTT is NOT auto-discovered: registering MqttTransportProvider on the service config is
// the one line that wires the MQTT transport in. Without it, startup fails with
// Bad_ConfigurationError ("no TransportProvider").
PubSubServiceConfig serviceConfig =
    PubSubServiceConfig.builder().transportProvider(MqttTransportProvider.create()).build();
```

If you forget this and your config contains an enabled MQTT connection, `startup()` fails with `Bad_ConfigurationError` reporting that no TransportProvider supports the connection.

## Broker URIs

An MQTT connection is built with `PubSubConnectionConfig.mqtt(name)` and requires a `brokerUri`:

- `mqtt://host[:port]` — plain TCP, default port 1883.
- `mqtts://host[:port]` — TLS, default port 8883 (see [TLS and authentication](#tls-and-authentication)).

MQTT-over-WebSocket is not supported: `ws://` and `wss://` URIs fail with `Bad_NotSupported`. Any other scheme fails with `Bad_ConfigurationError`. Both surface when the connection's channel opens, i.e. as a `startup()` failure.

## Choosing the message mapping: UADP or JSON

Both built-in mappings work over MQTT. Which one a writer group uses is selected purely by the *type* of its message settings: `Uadp*` settings select the binary `"uadp"` mapping, `Json*` settings select `"json"`. From `MqttJsonPublisherExample`:

```java
WriterGroupConfig.builder("demo")
    .writerGroupId(ushort(1))
    .publishingInterval(Duration.ofSeconds(1))
    // Json* settings select the "json" mapping; the empty content
    // masks select the recommended Part 14 defaults
    .messageSettings(JsonWriterGroupSettings.builder().build())
    .dataSetWriter(
        DataSetWriterConfig.builder("writer")
            .dataSet(dataSet.ref())
            .dataSetWriterId(ushort(1))
            .settings(JsonDataSetWriterSettings.builder().build())
            .build())
    .build()
```

On the subscriber side the same rule applies to `DataSetReaderConfig.settings(...)`, with one trap: the reader's default settings are already UADP, so nothing in a UADP subscriber config visibly says "uadp". `MqttUadpSubscriberExample` carries this comment at the reader builder:

```java
// the reader's default message settings are UADP, matching the
// publisher's explicit Uadp* settings and selecting the "uadp"
// mapping for decoding
DataSetReaderConfig.builder("telemetry-reader")
```

A JSON subscriber must opt in explicitly with `.settings(JsonDataSetReaderSettings.builder().build())`, as `MqttJsonSubscriberExample` does. The reader's settings type selects the codec its messages are decoded with, so when a broker subscriber receives nothing, check that its settings type matches the publisher's mapping before suspecting the topic.

JSON is broker-only: putting `Json*` settings anywhere on a UDP connection is rejected at config build time with a `PubSubConfigValidationException`. See the [configuration model](configuration.md).

## The topic tree

Topics follow the Part 14 topic tree: `<prefix>/<mapping>/data/<publisherId>/<writerGroupName>` for data and `<prefix>/<mapping>/metadata/<publisherId>/<writerGroupName>/<dataSetWriterName>` for metadata. The prefix defaults to `opcua` (overridable per connection, see [connection properties](#client-identity-and-connection-properties)); the mapping level is `uadp` or `json`; numeric publisher ids are stringified.

Publishers derive their topics automatically — there is nothing to configure. There is also no runtime API to ask a running writer group which topic it resolved to; the `BrokerTopics` helper re-derives it, which is how `MqttUadpPublisherExample` logs it:

```java
// The engine derives the data topic per Part 14 §7.3.4.7:
// <prefix>/<mapping>/data/<publisherId>/<writerGroupName> = opcua/uadp/data/2001/demo
String dataTopic =
    BrokerTopics.dataTopic(BrokerTopics.DEFAULT_TOPIC_PREFIX, "uadp", PUBLISHER_ID, "demo");
```

Readers cannot derive the data topic: derivation needs the publisher's WriterGroup name, and a subscriber config carries no writer groups. Every enabled broker reader must therefore set `BrokerTransportSettings.queueName` explicitly, or startup (and reconfiguration) fails with `Bad_ConfigurationError`. From `MqttUadpSubscriberExample`:

```java
// broker readers MUST configure the data topic; startup
// fails with Bad_ConfigurationError without a queueName
.brokerTransport(
    BrokerTransportSettings.builder()
        .queueName(DATA_TOPIC)
        .build())
```

Note the asymmetry between the two derived topics: the data topic stops at the writer group, but the metadata topic additionally embeds the DataSetWriter *name* (`opcua/json/metadata/3001/demo/writer`). Renaming a writer on the publisher silently moves its metadata topic — subscribers with a configured `metaDataQueueName` stop receiving metadata even though data keeps flowing. Treat writer names on broker connections as part of your published contract.

A configured `queueName` always wins over derivation, on the publish side too: a `BrokerTransportSettings.queueName` on the writer group replaces the group's derived data topic, and one on an individual DataSetWriter splits that writer's messages out of the group's shared NetworkMessage onto its own topic. (How writers otherwise pack into the group's message, and when the per-writer split is the right structure, is covered in [Structuring and sizing](structuring-and-sizing.md#multiple-writers-on-the-wire).) Also be aware that topic levels are not sanitized — component names containing MQTT-special characters (`/`, `+`, `#`, `$`) produce broken derived topics, so use explicit queue names in that case.

## Retained metadata

On broker connections the engine automatically publishes each writer's DataSetMetaData to the derived metadata topic as a *retained*, QoS 1 message: once at writer activation (which includes reactivation after a [broker reconnect](#broker-outages-and-reconnect)), again whenever reconfiguration changes the metadata, and periodically if `metaDataUpdateTime` is configured. The publisher configures nothing for this — `MqttJsonPublisherExample` contains no metadata-related settings at all.

Retained metadata is the broker-side substitute for UDP discovery probing: there is nothing to probe for, because the broker replays the retained document to any subscriber the moment it subscribes. The UDP `REQUEST_IF_MISSING` policy does not probe on MQTT connections (it silently degrades to accepting whatever metadata arrives), so the idiomatic broker subscriber uses `ACCEPT_DISCOVERED` with a `metaDataQueueName` and no locally configured field list. From `MqttJsonSubscriberExample`:

```java
DataSetReaderConfig.builder("reader")
    // UInt16 filter matched against the JSON wire string "3001" by canonical form
    .publisherId(PublisherId.uint16(ushort(3001)))
    // ...
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

The metadata's ConfigurationVersion is auto-populated by the config builders (it appears as `1.1` in the example output below) — you don't have to set one for the version check to pass. The full metadata story, including the three `MetadataPolicy` values and UDP discovery, is in [metadata and discovery](metadata-and-discovery.md).

## Delivery guarantees and MQTT QoS

`BrokerTransportSettings.requestedDeliveryGuarantee` maps directly onto MQTT QoS:

| Requested guarantee | MQTT QoS |
|---|---|
| `BestEffort`, `AtMostOnce` | 0 |
| `AtLeastOnce` | 1 |
| `ExactlyOnce` | 2 |
| `NotSpecified` (default) | 0 for data, 1 for metadata |

Settings on a DataSetWriter or DataSetReader override settings on the WriterGroup. The defaults match what the examples produce: data published QoS 0 not retained, metadata QoS 1 retained.

One caveat: a writer-level `requestedDeliveryGuarantee` requires a writer-level `queueName` override. Writers without a queue override share the group's NetworkMessage partition, and that shared partition publishes at the group-resolved QoS — there is nothing a per-writer QoS could apply to — so an enabled broker writer that overrides the delivery guarantee without overriding the queue name is rejected at `startup()` and reconfigure with `Bad_ConfigurationError` (Part 14 §6.4.2.5.4). Earlier revisions accepted the combination and silently ignored the QoS. Give the writer its own `queueName` if you need per-writer QoS.

## Client identity and connection properties

Per Part 14, the MQTT ClientId defaults to the canonical string form of the connection's PublisherId — a publisher with `PublisherId.uint16(2001)` connects as ClientId `2001`. Subscriber-only connections (no `publisherId` configured) get a random `milo-`-prefixed id.

This default has a sharp edge: MQTT brokers allow only one session per ClientId, so two engines sharing a PublisherId against the same broker take over each other's sessions in an endless mutual-kick loop. The symptom is confusing — both processes log repeated `Server sent DISCONNECT.` warnings and reconnect, while data keeps flowing in bursts. Run one engine per PublisherId per broker, or set the `0:MqttClientId` property to make the ids distinct.

Connection-level knobs are set as properties on the MQTT connection builder, e.g. `.property(new QualifiedName(0, "MqttClientId"), Variant.ofString("my-client"))`:

| Property | Values | Effect |
|---|---|---|
| `0:MqttClientId` | non-empty string | Overrides the ClientId (default: canonical PublisherId) |
| `0:MqttVersion` | `"5.0"`, `"3.1.1"`, `"BestAvailable"` | Protocol version; default `BestAvailable` |
| `0:MqttTopicPrefix` | non-empty string | Replaces `opcua` as the derived-topic prefix |

`BestAvailable` connects with MQTT 5.0 and falls back to 3.1.1 if the broker rejects the connect with Unsupported Protocol Version. (The fallback leg is implemented but not exercised by Milo's in-repo tests — the embedded test broker speaks MQTT 5.) An unrecognized `0:MqttVersion` or empty `0:MqttClientId` value fails with `Bad_ConfigurationError`.

## Broker outages and reconnect

The transport reconnects automatically and re-issues every subscription on reconnect; recovery across a full broker restart (data resumes, readers stay usable) is verified by test. The backoff schedule is the HiveMQ client default — 1 s initial, doubling per attempt, capped at 120 s, with jitter — and is not configurable.

An outage is visible in PubSub component state. When a previously connected broker session is lost, the MQTT transport reports the down edge through the engine's transport-state listener and the *connection* fails into `Error` with `Bad_ServerNotConnected`; its groups, writers, and readers cascade to `Paused` per the ordinary state rules, state-change events fire, and the `stateError` diagnostics counter ticks. On reconnect the transport re-issues its subscriptions first, then reports the up edge: the connection returns to `Operational`, the children reactivate, and — because metadata publishing rides writer activation — every writer's retained metadata is re-published to the broker. Two edges of the edge-tracking are deliberate: the *initial* connect is not a recovery (a session that never connected is not an outage — first-connect retries and version-fallback churn stay silent, and sends simply fail fast until the session is up, the Phase 3 behavior), and a transport-up never "recovers" an `Error` the transport did not cause.

The per-cycle failure story while disconnected:

- Publisher side: each failed publish surfaces as a `SEND_FAILURE` diagnostics event carrying `Bad_ServerNotConnected` (publishes fail fast while disconnected — no blanket `Bad_CommunicationError` anymore) and ticks the group's `failedTransmissions` and the writers' `failedDataSetMessages` counters. QoS 1/2 messages are not buffered and replayed after reconnect — data published during an outage is lost.
- Subscriber side: a subscriber's own broker session failing produces the same connection-level `Error`/recovery on the subscriber. But the *publisher's* outage is invisible to a subscriber whose session is healthy, so still opt in to the `messageReceiveTimeout` watchdog on `DataSetReaderConfig` (default off) for end-to-end staleness: the reader goes to Error/`Bad_Timeout` after the configured silence and returns to Operational on the next message it accepts. Sequence tracking applies here: a message dropped as a stale duplicate (a QoS 1 redelivery, for example) neither resets the watchdog nor recovers the reader; see [operations](operations.md).

## TLS and authentication

The configuration surface for secured brokers lives on the connection: a `mqtts://` URI forces TLS (and `BrokerSecurityConfig.tls(true)` enables it for `mqtt://` URIs), and `.brokerSecurity(BrokerSecurityConfig.builder()...)` carries:

- `username(...)` / `password(...)` — MQTT simple auth, for both MQTT 5 and 3.1.1.
- `caCertificate(Path)` — a PEM trust anchor for the broker's certificate.
- `clientCertificate(Path)` + `clientKey(Path)` — mutual TLS; the key must be unencrypted PKCS#8 (encrypted PKCS#8 and PKCS#1/SEC1 keys are rejected with `Bad_NotSupported`).
- `allowUntrustedCertificates(true)` — disables certificate and hostname verification, for development only.

Misconfiguration is rejected at startup: a client certificate without a key (or vice versa) fails with `Bad_ConfigurationError`, unreadable or unparseable key material with `Bad_SecurityChecksFailed`, and a password without a username fails with `Bad_ConfigurationError` when the version is pinned to 3.1.1. Under the default `BestAvailable` version the same password-without-username configuration is accepted at startup; it only fails — with a logged error, not an exception — if the broker ever forces the 3.1.1 fallback. Pin `3.1.1` explicitly if you want this misconfiguration caught at startup.

To be clear about the test status: TLS and authentication are implemented and wired into the same MQTT client the plaintext tests prove, but no in-repo test performs an actual TLS handshake or authenticated connect — that requires an external TLS- or auth-enabled broker. Validate against your broker before relying on it.

Note also that `BrokerSecurityConfig` deliberately does not round-trip through the Part 14 wire configuration (`PubSubConfiguration2DataType`): credentials and key paths are dropped on export and come back as defaults, so secured deployments must re-apply security config in code after importing a wire config.

TLS is transport security — it protects the hop to the broker, not the messages themselves. UADP-mapped groups on MQTT connections can additionally use Part 14 [message security](message-security-and-sks.md) (Sign or SignAndEncrypt): the secured bytes travel inside the broker message, end to end from publisher to subscriber, and the broker never holds a key. The security path is transport-independent, but the in-repo secured test matrix runs over UDP, so treat secured-over-MQTT the way this page treats TLS: applied but unverified against a real broker. For JSON-mapped groups there is no message security in OPC UA 1.05 — a secured JSON group is rejected with `Bad_ConfigurationError` — so TLS is the JSON mapping's only protection.

## Running the examples

The MQTT examples live in `milo-examples/pubsub-examples` under `org.eclipse.milo.examples.pubsub.mqtt`. Build the module once from the repository root:

```
mvn -q -pl milo-examples/pubsub-examples -am install -DskipTests
```

Then run each example with `exec:java` — and do not put `-q` on these commands. `exec:java` runs the example inside the Maven JVM, where its SLF4J output is routed through Maven's logger; `-q` silences everything below ERROR, so the example runs perfectly but appears dead. (`-q` is fine on the build step only.) Maven also prefixes each line with its own `[thread] INFO logger -` boilerplate; the output excerpts below show the message portion.

### 1. A broker: `EmbeddedBrokerExample`

```
mvn -pl milo-examples/pubsub-examples exec:java \
    -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.EmbeddedBrokerExample
```

This runs an embedded HiveMQ Community Edition broker on `127.0.0.1:1883` (optional argument overrides the port, e.g. `-Dexec.args="1884"`). Bootstrap takes roughly 1.5-8 seconds; wait for the single line:

```
broker ready on port 1883
```

This example requires JDK 17 or 21 — HiveMQ CE does not bootstrap on JDK 25, and the example fails fast there with a clear error instead of a stack trace. The publisher and subscriber examples themselves run on any supported JDK; only the embedded broker is constrained. Any external MQTT broker (Mosquitto, EMQX, HiveMQ, ...) works instead — skip this step and pass its URI to the other examples with `-Dexec.args="mqtt://host:1883"`.

### 2. The UADP pair

With the broker running, start the publisher, then the subscriber, one terminal each:

```
mvn -pl milo-examples/pubsub-examples exec:java \
    -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.MqttUadpPublisherExample

mvn -pl milo-examples/pubsub-examples exec:java \
    -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.MqttUadpSubscriberExample
```

The publisher logs its derived topic, then a sparse progress line every 10th cycle:

```
publishing dataset "telemetry" every 500ms on topic opcua/uadp/data/2001/demo
publish cycle 10: temperature=24.21, counter=10, status=running
```

The subscriber logs `subscribed to topic opcua/uadp/data/2001/demo, waiting for DataSets...` and then about two lines per second:

```
[received] dataSet=telemetry publisherId=2001 writerGroupId=1 dataSetWriterId=1 fields: temperature=20.49916708323414, counter=1, status=running
```

A quiet gap between the "subscribed" line and the first `[received]` is normal for a few seconds while the publisher's first messages arrive. Both run until Ctrl-C.

This pair demonstrates explicit `Uadp*` settings selecting the binary mapping, a `REQUIRE_CONFIGURED` reader with locally configured metadata and the configuration-version check, and the reader-side `queueName` requirement.

### 3. The JSON pair

```
mvn -pl milo-examples/pubsub-examples exec:java \
    -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.MqttJsonPublisherExample

mvn -pl milo-examples/pubsub-examples exec:java \
    -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.MqttJsonSubscriberExample
```

Publisher and subscriber may be started in either order — the broker retains the metadata document, so a late subscriber discovers the field definitions immediately. The subscriber configures no fields at all; its expected output proves discovery worked:

```
subscriber started: broker=mqtt://127.0.0.1:1883, data topic=opcua/json/data/3001/demo, metadata topic=opcua/json/metadata/3001/demo/writer
metadata received: dataSet=demo-dataset version=1.1 fields=[temperature, status]
[received] publisherId=3001 dataSet=demo-dataset temperature=20.522642316338267, status=running
```

The publisher logs `publish cycle N: temperature=..., status=...` on the first cycle and every 10th after that. This pair demonstrates the `"json"` mapping, automatic retained metadata, the `ACCEPT_DISCOVERED` + `metaDataQueueName` subscriber pattern, and cross-type PublisherId matching (the reader's UInt16 3001 filter matches the JSON wire string `"3001"` by canonical form).
