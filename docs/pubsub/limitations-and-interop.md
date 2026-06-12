# Limitations and Interop

This page is the honest map of what Milo's PubSub modules implement, what they reject, and what they silently ignore. Read it before you depend on a Part 14 feature — and especially before you debug a config option that seems to do nothing.

Three modules are covered: `org.eclipse.milo:milo-sdk-pubsub` (the core config model, runtime, UADP and JSON codecs, and built-in UDP transport), `org.eclipse.milo:milo-sdk-pubsub-mqtt` (the MQTT transport), and `org.eclipse.milo:milo-sdk-pubsub-server` (the `OpcUaServer` integration).

## How to read the tables

Status means:

- **Works** — implemented and verified end-to-end.
- **Partial** — a usable subset works; the notes say exactly which part.
- **Config-only** — the configuration model expresses and round-trips it, but the runtime never executes it.
- **Absent** — no implementation exists.

"Behavior if configured" tells you what happens when you configure something the runtime does not do. *Rejected* means you get an error (the name is given — look for it in the exception or in diagnostics `lastError`). *Silently inert* means the config is accepted and ignored; nothing warns you. Inert options are the ones worth memorizing, because they fail without a trace.

## milo-sdk-pubsub (core)

| Feature | Status | Behavior if configured | Notes |
|---|---|---|---|
| UADP over UDP unicast publish/subscribe | Works | n/a | The baseline path. Multiple writers in a group share one NetworkMessage per cycle; per-reader and global listeners; diagnostics counters on both sides. All-default UADP content masks deliver to a `REQUIRE_CONFIGURED` reader. |
| UADP over UDP multicast (data plane) | Partial | n/a | Fully implemented: subscriber binds the wildcard port and joins the group on each multicast-capable interface (hard `UaException` if zero joins); publisher sets TTL 64 and an optional egress interface. "Partial" only because automated in-repo coverage is indirect (the discovery plane exercises the same channel code); the runnable discovery examples and manual interop runs exercise the multicast data plane directly. |
| UDP discovery: metadata responder + `REQUEST_IF_MISSING` probing | Works | n/a | UDP connections only. Publisher connections answer metadata probes automatically; a reader with `MetadataPolicy.REQUEST_IF_MISSING` probes for metadata it lacks. No periodic `DiscoveryAnnounceRate` announcements (responder answers probes and pushes once on reconfigure); discovery message size is fixed at 4096 bytes. If no `discoveryAddress` is configured, the engine binds port 4840 and joins 224.0.2.14 — pin it explicitly. On MQTT connections `REQUEST_IF_MISSING` silently degrades to `ACCEPT_DISCOVERED` (probing is inert; retained broker metadata still applies). |
| JSON message mapping (`json`) | Works | n/a | Broker (MQTT) connections only. Encodes `ua-data` key frames, keep-alives, and `ua-metadata`; decodes all four DataSetMessage kinds and all eight structural layouts. The decoded `PublisherId` is always String-typed (JSON wire form); readers match numeric ids by canonical string comparison. |
| JSON mapping on UDP connections | Absent | Rejected: `PubSubConfigValidationException` at config build | Any `Json*Settings` at group, writer, or reader level on a UDP connection fails `UdpConnectionConfig.Builder.build()`. The Part 14 wire-config import path runs through the same validation, so there is no bypass. |
| Transports beyond UDP and MQTT-over-TCP (Ethernet, AMQP, MQTT-over-WebSocket, UDP broadcast) | Absent | Rejected (one inert edge) | See [Transports](#transports) below. `ws://`/`wss://` broker URIs fail `Bad_NotSupported`; unknown schemes `Bad_ConfigurationError`; wire configs with other transport profile URIs throw `PubSubConfigValidationException`; an enabled connection no provider supports fails startup with `Bad_ConfigurationError`. One inert edge: a broadcast IP passed to `UdpDatagramAddress.unicast()` passes validation and fails only at OS send time. |
| Message security: Sign / SignAndEncrypt | Config-only | Rejected: startup fails `Bad_NotSupported` | Any enabled group with `MessageSecurityMode` other than `None` fails `startup()`; disabled groups are tolerated for config round-trip. Received messages with security flags set are skipped without error. See [Message security and SKS](#message-security-and-sks). |
| SKS (Security Key Service) / `SecurityKeyProvider` | Config-only | Silently inert | `SecurityKeyProvider.getKeys` is never invoked; `SecurityGroupConfig` round-trips through the Part 14 mappers but its contents are never read. One non-silent edge: binding a provider to an unknown security group name throws `IllegalArgumentException`. |
| Delta frames / `keyFrameCount` | Partial (decode only) | `keyFrameCount` silently inert | Every emitted frame is a key frame; `keyFrameCount > 1` is accepted and never read. Received delta frames (UADP and JSON `ua-deltaframe`) are decoded and delivered, but do not complete reader startup. See [Delta frames](#delta-frames). |
| Event datasets (PublishedEvents) | Partial (decode only) | Rejected on wire-config import: `PubSubConfigValidationException` | No publish API exists. Received event DataSetMessages are decoded, delivered, and complete reader startup like key frames. See [Events](#events). |
| RawData field encoding and PromotedFields | Absent | Rejected: `Bad_NotSupported` at encode — each publish cycle is skipped, startup succeeds | The dangerous shape: the writer appears to run but sends nothing; the error lands only in diagnostics. See [RawData and promoted fields](#rawdata-and-promoted-fields). |
| Chunked NetworkMessages / `maxNetworkMessageSize` | Absent | `maxNetworkMessageSize` silently inert | No chunk emission or reassembly. Oversized messages are not split; hard wire-format limits throw `Bad_EncodingLimitsExceeded` at encode. Inbound chunked messages are skipped silently. See [Chunking and message size](#chunking-and-message-size). |
| Live reconfiguration (`reconfigure` / `update`, by replacement) | Works | n/a | Minimal-diff restarts (`DISABLE_AFFECTED`) or whole-connection restart (`STOP_AND_RESTART`); the new config is validated before anything changes, so a rejected reconfigure leaves the old config running. On broker connections any reader-side change escalates to a full connection restart. `ServerPubSub`'s info model and auto-bindings do not track `runtime()` reconfiguration. Restarted components get fresh diagnostics counters. |
| PubSub state machine, enable/disable, receive-timeout watchdog | Works | n/a | Part 14 states Disabled/Paused/PreOperational/Operational/Error over the whole runtime tree. Readers stay PreOperational until the first key frame (including zero-field heartbeats) or event. `messageReceiveTimeout > 0` arms a watchdog (Error + `Bad_Timeout` on silence, recovery on the next message); the default (zero) disables it. |
| Diagnostics (per-path counters + listener) | Works | n/a | `snapshot()` exposes messages sent/received, decode errors, source errors, and `lastError` per component path. The diagnostics listener delivers error events only, and there is no listener removal API in this version. |
| Part 14 configuration round-trip (`PubSubConfiguration2DataType`) | Works | n/a | Both directions, with documented one-way losses: `BrokerSecurityConfig` is deliberately never serialized (credentials and key paths must not leave the process), `MetadataPolicy` is restored as `REQUIRE_CONFIGURED`, plus a few smaller fields listed in the `PubSubConfigMapper` Javadoc. Import throws `PubSubConfigValidationException` for mirrors, unknown transport profiles, non-PublishedDataItems dataset sources, and dangling security group ids. |
| Subscriber sequence-number tracking | Absent | n/a (no config knob exists) | Duplicates and reordered messages are not detected. See [Sequence numbers](#sequence-numbers). |
| Standalone SubscribedDataSets / SubscribedDataSetMirror | Partial / mirror Absent | Mirror rejected: `PubSubConfigValidationException` on import; standalone-carried TargetVariables silently inert in the server module (WARN at attach) | Standalone refs resolve a reader's configured decode metadata (wired into the decode path, but not data-flow tested); the mirror variant cannot be expressed in the typed API at all. See [Standalone SubscribedDataSets](#standalone-subscribeddatasets-and-mirror). |
| Keep-alive NetworkMessages (`keepAliveTime`) | Works | n/a | Emitted only for writers that are not Operational (disabled/paused) and quiet past `keepAliveTime`; Operational writers publish a key frame every cycle and so never lapse. Default is null (never emit); zero or negative values are rejected at build with `PubSubConfigValidationException`. |
| Pluggable transport / message-mapping SPI | Works | Missing provider rejected: startup fails `Bad_ConfigurationError` | Registration is explicit via `PubSubServiceConfig.Builder` — there is no `ServiceLoader` discovery. The UADP discovery plane always uses the built-in UDP transport and UADP mapping, even when custom providers are registered. See [Extending with the SPI](#extending-with-the-spi). |
| Deterministic timing offsets (SamplingOffset, PublishingOffset, ReceiveOffset, ProcessingOffset) | Config-only | Silently inert | No typed API exists; values arriving in a wire config are preserved in the raw `ExtensionObject` escape hatches and re-exported faithfully, but the runtime never applies them. See [Timing offsets](#timing-offsets). |

## milo-sdk-pubsub-mqtt

| Feature | Status | Behavior if configured | Notes |
|---|---|---|---|
| UADP over MQTT publish/subscribe | Works | n/a | End-to-end against a real broker over MQTT 5; a pinned MQTT 3.1.1 version is verified at the transport-channel level. Requires `MqttTransportProvider` to be registered explicitly — forgetting it fails startup with `Bad_ConfigurationError`. |
| JSON over MQTT publish/subscribe | Works | n/a | `ua-data` on the derived topic tree plus retained `ua-metadata`. A reader with `ACCEPT_DISCOVERED` and a `metaDataQueueName` gets field names from retained metadata — the MQTT substitute for UDP's `REQUEST_IF_MISSING` probing. |
| Topic derivation, queue names, QoS | Works | Enabled broker readers without a data `queueName`: rejected, `Bad_ConfigurationError`. `resourceUri`/`authenticationProfileUri`, and writer-level QoS without a queue override: silently inert | Writers derive Part 14 topics automatically; readers cannot (they don't know the publisher's writer-group name) and must configure `queueName`. A configured queue name always wins over derivation. Topic levels are not sanitized — use explicit queue names if component names contain `/`, `+`, `#`, or `$`. Writer-level QoS is honored only together with a writer-level queue-name override. |
| Retained metadata publishing | Works | n/a | Published retained (QoS 1 by default) at writer activation, on reconfigure-change, and periodically when `metaDataUpdateTime > 0`. Send failures are retried up to 5 times with backoff and surface as diagnostics only — they never fail the writer. Metadata sequence numbers are strictly increasing per publisher but may have gaps; never assert continuity. |
| Broker outage recovery / reconnect | Partial | n/a | Automatic reconnect plus explicit resubscribe is proven across a full broker restart. Partial because an outage surfaces only as `Bad_CommunicationError` send diagnostics — publisher-side PubSub state never reflects it (a reader's `messageReceiveTimeout` watchdog can reflect it indirectly). Publishes fail fast with `Bad_ServerNotConnected` while disconnected; messages are never buffered across outages, regardless of QoS. Reconnect backoff is not configurable. |
| MQTT TLS (`mqtts://`) and username/password authentication | Partial | Malformed or unsupported material rejected: `Bad_ConfigurationError` (cert without key, password without username on pinned 3.1.1), `Bad_NotSupported` (encrypted PKCS#8 or PKCS#1/SEC1 keys), `Bad_SecurityChecksFailed` (unreadable/unparseable material) | Implemented and wired — `mqtts://` forces TLS (default port 8883), `BrokerSecurityConfig` supplies CA trust, mutual-TLS PEM material, and simple auth — but no automated test exercises a TLS handshake or authenticated connect (the in-repo broker is plaintext). Treat it as applied-but-unverified and test against your broker. `BrokerSecurityConfig` never round-trips through Part 14 config export, by design. |

## milo-sdk-pubsub-server

| Feature | Status | Behavior if configured | Notes |
|---|---|---|---|
| `ServerPubSub.attach` + auto-source publishing of node values | Works | n/a | Datasets whose fields are non-empty and all `NodeFieldAddress` are auto-bound to batched address-space reads, one per publish cycle. Unresolvable addresses fail attach with `PubSubConfigValidationException` naming the element. Mixed or key-only datasets are not auto-bound — an unbound writer publishes `Bad_InternalError` for every field until you supply a source via `ServerPubSubOptions.bindings()`. Works on a never-started server. |
| TargetVariables: writing received values into server nodes | Works | TargetVariables carried by a standalone SubscribedDataSet ref: silently inert (attach succeeds with a WARN; no writes happen) | Full Part 14 Table 80 semantics for reader-level `TargetVariablesConfig`: value/status/sourceTimestamp pass-through, receiver and write index ranges, override/disabled handling, write errors counted via `targetWriteErrors()` while flow continues. Writes respect `AccessLevel.CurrentWrite` (`Bad_NotWritable` is counted, never written). |
| PubSub information model exposure | Partial | `ServerPubSubOptions.diagnosticsEnabled` silently inert | Opt-in via `exposeInformationModel(true)` (default false). Read-only, method-free node tree built at startup from an attach-time snapshot; live per-component Status/State values track the runtime, but `runtime()` reconfiguration does not rebuild config-derived nodes. Only UDP-UADP is advertised in `SupportedTransportProfiles`; MQTT/JSON message settings produce no MessageSettings nodes; ns0 method nodes and Diagnostics are left unbacked. |
| Remote configuration over OPC UA (AddConnection / Remove* / SKS methods) | Absent | Rejected: `allowRemoteConfiguration(true)` throws `UnsupportedOperationException` at attach; a client calling the ns0 methods gets `Bad_NotImplemented` | No method handlers exist anywhere in the PubSub modules. See [Server integration limits](#server-integration-limits). |
| Configuration persistence (`PubSubConfigurationStore`) | Partial | n/a | A two-method interface; no implementation ships. A non-null `load()` wins over the attach config; a null `load()` causes the attach config to be saved exactly once. Save failures are non-fatal (WARN); load failures fail attach. Changes made through `runtime()` are never auto-saved — there is no warning when they happen. |
| `ServerPubSub` over MQTT / custom transports | Absent | Rejected: enabled broker connections fail startup with `Bad_ConfigurationError` ("no TransportProvider supports connection"); a disabled one fails with the same code when later enabled | `ServerPubSub` builds its `PubSubServiceConfig` internally and `ServerPubSubOptions` exposes no transport-provider hook, so `MqttTransportProvider` cannot be registered. Server integration is UDP/UADP only in this version; use a standalone `PubSubService` for MQTT. |

## Message security and SKS

SKS is the Security Key Service — the Part 14 mechanism for distributing message-security keys to publishers and subscribers; it has no counterpart in client/server OPC UA. The configuration model carries `MessageSecurityConfig`, `SecurityGroupConfig`, and a `SecurityKeyProvider` SPI (the hook where an SKS would plug in), and all of it round-trips through the Part 14 mappers — but none of it executes. There is no message signing, no encryption, and no key management runtime in this version.

The guard is loud where it matters: any *enabled* writer or reader group with `MessageSecurityMode` other than `None` fails `startup()` with `Bad_NotSupported`, and the group runtimes re-check at activation (a reconfigure that introduces security is rejected at activation rather than by the reconfigure call itself). Disabled groups with non-`None` modes are tolerated so configs can round-trip.

The SKS surface is quiet where it doesn't: `SecurityKeyProvider` implementations are accepted at service creation and never invoked, and `SecurityGroupConfig` contents are never read. The one exception is create-time validation — binding a key provider to a security group name that doesn't exist in the config throws `IllegalArgumentException`.

On the receive side, a message with any security flags set is tolerated: the payload is skipped at debug level and no error is raised. Transport-level TLS for MQTT (see the mqtt table above) is the only encryption available today.

## Delta frames

`keyFrameCount` is accepted by `DataSetWriterConfig.Builder`, round-trips through the Part 14 mappers, and appears in the server info model — and is read by nothing else. The publish cycle creates a key-frame draft for every Operational writer every cycle, so every emitted frame is a key frame regardless of the configured value. No validation rejects `keyFrameCount > 1`; it is silently inert.

Receiving is a different story: delta frames *are* decoded, for both UADP and JSON (`ua-deltaframe`), with per-field wire indices resolved against metadata. They are delivered to listeners, counted in diagnostics, reset the receive timeout, and recover a reader from Error — but they do not complete reader startup. A reader stays PreOperational until its first key frame or event message, so a peer that sends mostly delta frames will still bring your reader Operational as soon as its periodic key frame arrives.

## Events

Event publishing has no API surface: there is no `PublishedEventsConfig`, and the encoders have no event message kind. This is a deliberate non-goal of this version, not an oversight. Importing a Part 14 wire config whose PublishedDataSet uses a `PublishedEventsDataType` source is rejected with `PubSubConfigValidationException` ("only PublishedDataItems dataset sources are supported").

Subscribing works: event DataSetMessages (UADP type `0x02`, JSON `ua-event`) are decoded and delivered like key frames, and an event message completes reader startup the same way a key frame does.

## RawData and promoted fields

UADP `RawData` field encoding and PromotedFields emission are absent, and they fail in the way most likely to confuse you: startup succeeds. The encoder rejects them with `Bad_NotSupported` *per publish cycle* — the writer group catches the encode failure, records a diagnostics error, and skips that cycle's message. The symptom is a writer that looks healthy but never sends anything. If a UADP publisher is silent, check `PubSubDiagnostics` `lastError` for `Bad_NotSupported` before suspecting the network.

On decode the behavior is tolerant: a RawData DataSetMessage is surfaced with `valid=false` and no fields (header fields intact), and a promoted-fields block is skipped over so the rest of the payload still decodes (the promoted values themselves are dropped).

JSON is unaffected: there the `RawData` mask bit is a Variant-representation modifier per Part 14, implemented and tested, not a rejection.

## Chunking and message size

There is no NetworkMessage chunking — no emission, no reassembly. `maxNetworkMessageSize` on writer and reader groups is silently inert: nothing in the encode or publish path reads it, so oversized messages are not split or limited by it.

What actually bounds message size are fixed wire-format limits, enforced at encode time with `Bad_EncodingLimitsExceeded`: more than 255 DataSetMessages in one NetworkMessage with a payload header, or any DataSetMessage body over 65535 bytes when the sizes array is present. Inbound chunked NetworkMessages from other stacks are detected and skipped silently (debug log only) — no error, no reassembly. Discovery metadata announcements that exceed the fixed, non-configurable 4096-byte discovery limit are dropped with a WARN and a `Bad_EncodingLimitsExceeded` diagnostics entry.

Practical consequence: keep datasets small enough that one NetworkMessage fits your transport MTU or broker limit. The stack will not fragment for you.

## Transports

Two transports exist: UDP (built into `milo-sdk-pubsub`, always available) and MQTT over TCP (`milo-sdk-pubsub-mqtt`, registered explicitly). Everything else is absent:

- **Ethernet (`opc.eth`) and AMQP** cannot even be expressed: `PubSubConnectionConfig` is sealed and permits only `UdpConnectionConfig` and `MqttConnectionConfig`. A Part 14 wire config with any transport profile URI other than udp-uadp, mqtt-uadp, or mqtt-json throws `PubSubConfigValidationException` on import.
- **MQTT over WebSocket**: `ws://` and `wss://` broker URIs are rejected with `Bad_NotSupported` when the connection opens; other unknown schemes get `Bad_ConfigurationError`.
- **UDP broadcast**: `UdpDatagramAddress` offers only `multicast` and `unicast` factories and the sockets never set `SO_BROADCAST`. One trap: a broadcast IP passed to `UdpDatagramAddress.unicast()` passes config validation and fails only when the OS rejects the send — this single case is silently inert rather than rejected.

Any enabled connection that no registered `TransportProvider` supports fails `startup()` with `Bad_ConfigurationError`. If you need another transport, the SPI is the supported route — see [Extending with the SPI](#extending-with-the-spi).

## Server integration limits

`ServerPubSub` (in `milo-sdk-pubsub-server`) is UDP-only. It builds its `PubSubServiceConfig` internally and exposes no way to register a transport provider, so MQTT connections in an attached config fail startup with `Bad_ConfigurationError` ("no TransportProvider supports connection") — or, if disabled at attach, fail with the same code the moment they are enabled. To publish or subscribe over MQTT, run a standalone `PubSubService` alongside the server instead.

Remote configuration over OPC UA is absent. `ServerPubSubOptions.allowRemoteConfiguration(true)` throws `UnsupportedOperationException` at attach, so you cannot accidentally believe it is on. The ns0 PubSub method nodes (AddConnection, RemoveConnection, the SKS methods) exist in the address space but are unbacked; a client invoking them receives `Bad_NotImplemented`. The exposed information model (when opted in) is read-only and snapshot-based: live state values update, configuration nodes do not follow `runtime()` reconfiguration.

## Standalone SubscribedDataSets and mirror

Standalone SubscribedDataSet support is partial: a `StandaloneSubscribedDataSetRef` on a reader resolves that reader's configured decode metadata, refs are validated at config build (unresolved refs are rejected), and chained refs resolve. The resolution is wired into the live decode path but, unlike reader-level metadata, is not covered by an end-to-end data-flow test in this release — treat it as applied-but-unverified, like MQTT TLS. What does not work: in `milo-sdk-pubsub-server`, TargetVariables carried *inside* a standalone SubscribedDataSet get no writer — attach succeeds, the targets are still validated, a WARN is logged, and then nothing is ever written. Only a reader-level `TargetVariablesConfig` drives writes.

`SubscribedDataSetMirror` is absent entirely. The typed API cannot express it (the sealed spec type permits only TargetVariables and standalone refs), and a `SubscribedDataSetMirrorDataType` arriving via Part 14 import is rejected with `PubSubConfigValidationException`.

## Timing offsets

The deterministic-timing offsets — `SamplingOffset`, `PublishingOffset`, `ReceiveOffset`, `ProcessingOffset` — have no typed fields in the [configuration model](configuration.md) and no runtime behavior. Publishing is plain fixed-rate scheduling with zero initial delay, and dataset snapshots are pulled inline at publish time; readers process messages on arrival.

Offsets arriving in an imported wire config are not rejected: the whole UADP message-settings structure is diverted into the raw `ExtensionObject` escape hatch (`rawMessageSettings`), preserved with round-trip fidelity, and even surfaced as info-model properties by the server module — but the runtime never decodes or consults those values. The raw hatch exists so configs survive a round trip through Milo, not so offsets take effect. If you author config in Java and need the offsets present for a peer's benefit, you must hand-encode the `UadpWriterGroupMessageDataType` / `UadpDataSetReaderMessageDataType` yourself via `rawMessageSettings`.

## Sequence numbers

The publisher emits sequence numbers correctly: a per-WriterGroup NetworkMessage sequence and a per-DataSetWriter DataSetMessage sequence, both wrapping at UInt16, with keep-alives carrying the next expected number without incrementing, per spec.

The subscriber ignores them. Sequence numbers are decoded at the codec layer and then discarded — `DataSetReceivedEvent` has no sequence-number field, and the dispatch path never reads them. There is no duplicate detection, no reorder detection, and no config knob to turn any of that on. Over UDP (which can duplicate and reorder) your listener must tolerate stale or repeated DataSets, e.g. by keying on timestamps or treating each event as a full state replacement.

## The decoder accepts more than the encoder produces

A recurring pattern in the tables above: the decode path is a strict superset of the encode path. Receiving any of these from a third-party publisher does not error; producing them from Milo is impossible:

| Received thing | What happens on receive | Can Milo send it? |
|---|---|---|
| Delta frames | Decoded and delivered (don't complete reader startup) | No — every emitted frame is a key frame |
| Event DataSetMessages | Decoded and delivered (complete reader startup) | No — no publish API |
| Chunked NetworkMessages | Detected, payload skipped silently | No — no chunking |
| RawData DataSetMessages (UADP) | Surfaced header-only with `valid=false` | No — encode rejected `Bad_NotSupported` |
| PromotedFields blocks (UADP) | Skipped; rest of payload decodes | No — emission rejected `Bad_NotSupported` |
| Security-flagged messages | Payload skipped without error | No — security encode rejected `Bad_NotSupported` |

The practical reading: Milo as a *subscriber* is tolerant of fuller-featured peers, but don't design a system that needs Milo to *publish* any of the above.

## Interop status

The stack has been manually interop-tested against a third-party Part 14 implementation, in both directions (Milo publishing / Milo subscribing), over UDP/UADP multicast and over MQTT with the JSON mapping. UDP unicast and UADP-over-MQTT have not been part of those third-party runs; they are verified by automated in-repo tests instead.

The harness used for those runs is `PubSubInteropTool`, a command-line tool in the test tree of `milo-sdk-pubsub-mqtt` (`org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubInteropTool`). It drives the real `PubSubService` engine — or, in its "raw" modes, a bare socket or MQTT client, useful against a misconfigured or unknown peer — and prints everything sent and received. It offers publish, subscribe, and raw-capture modes for both UDP and MQTT, all emitting or consuming a fixed "MiloInterop" dataset. Its class Javadoc contains the exact build and run commands plus worked examples; running it with no arguments prints full usage. If you need to validate Milo against your own Part 14 stack, that tool is the intended starting point.

## Extending with the SPI

If you need a transport this version doesn't ship — AMQP, Ethernet, anything — the `TransportProvider` SPI is the proven path: the entire MQTT module is implemented through it, with no special access to engine internals. Custom message mappings plug in the same way via `MessageMappingProvider`. Registration is explicit and programmatic; there is no `ServiceLoader` discovery:

```java
// MQTT is NOT auto-discovered: registering MqttTransportProvider on the service config is
// the one line that wires the MQTT transport in. Without it, startup fails with
// Bad_ConfigurationError ("no TransportProvider").
PubSubServiceConfig serviceConfig =
    PubSubServiceConfig.builder().transportProvider(MqttTransportProvider.create()).build();
```

(from `MqttUadpPublisherExample` in `milo-examples/pubsub-examples`)

Three things to know before implementing a provider:

- Selection is by `supports(connection)` only — the first registered provider that returns true wins, then the built-in UDP transport is the fallback. `transportProfileUri()` is declarative and never consulted for selection. A config referencing a connection no provider supports fails startup (and reconfigure validation) with `Bad_ConfigurationError`.
- A custom message mapping needs more than a provider: the engine derives the mapping name from the message-settings *class* (built-in `Uadp*Settings` map to `uadp`, `Json*Settings` to `json`, anything else to the settings class's simple name), so a custom mapping also requires custom `WriterGroupMessageSettings` / `DataSetWriterMessageSettings` / `DataSetReaderMessageSettings` types whose simple class name equals your provider's `mappingName()`. Registering a provider under the name `uadp` or `json` shadows the built-in on the data plane — supported and tested.
- The UADP discovery plane is exempt from both SPIs: discovery probes and announcements always go through the built-in UDP transport and built-in UADP mapping. Custom providers affect the data plane only.
- `MessageMappingProvider.encodeMetaData` is optional, and its default implementation throws `Bad_NotSupported`. Override it if your mapping will be used on broker connections — otherwise metadata publishing for those writers fails into diagnostics (`lastError` on the writer path) while data messages keep flowing, which is easy to miss.
