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
| JSON message mapping (`json`) | Works | n/a | Broker (MQTT) connections only. Encodes `ua-data` key and delta frames (`ua-deltaframe`), keep-alives, and `ua-metadata`; decodes all four DataSetMessage kinds and all eight structural layouts. The decoded `PublisherId` is always String-typed (JSON wire form); readers match numeric ids by canonical string comparison. |
| JSON mapping on UDP connections | Absent | Rejected: `PubSubConfigValidationException` at config build | Any `Json*Settings` at group, writer, or reader level on a UDP connection fails `UdpConnectionConfig.Builder.build()`. The Part 14 wire-config import path runs through the same validation, so there is no bypass. |
| Transports beyond UDP and MQTT-over-TCP (Ethernet, AMQP, MQTT-over-WebSocket, UDP broadcast) | Absent | Rejected (one inert edge) | See [Transports](#transports) below. `ws://`/`wss://` broker URIs fail `Bad_NotSupported`; unknown schemes `Bad_ConfigurationError`; wire configs with other transport profile URIs throw `PubSubConfigValidationException`; an enabled connection no provider supports fails startup with `Bad_ConfigurationError`. One inert edge: a broadcast IP passed to `UdpDatagramAddress.unicast()` passes validation and fails only at OS send time. |
| Message security: Sign / SignAndEncrypt | Config-only | Rejected: startup fails `Bad_NotSupported` | Any enabled group with `MessageSecurityMode` other than `None` fails `startup()`; disabled groups are tolerated for config round-trip. Received messages with security flags set are skipped without error. See [Message security and SKS](#message-security-and-sks). |
| SKS (Security Key Service) / `SecurityKeyProvider` | Config-only | Silently inert | `SecurityKeyProvider.getKeys` is never invoked; `SecurityGroupConfig` round-trips through the Part 14 mappers but its contents are never read. One non-silent edge: binding a provider to an unknown security group name throws `IllegalArgumentException`. |
| Delta frames / `keyFrameCount` | Works | Inexpressible combinations rejected: `Bad_ConfigurationError` at startup/reconfigure | `keyFrameCount` is honored on both mappings: a key frame on the first cycle and every keyFrameCount-th publishing interval, delta frames carrying only changed fields between; a no-change cycle sends nothing at all. 0 and 1 both mean every frame is a key frame. Received delta frames (UADP and JSON `ua-deltaframe`) are decoded and delivered, but still do not complete reader startup. See [Delta frames](#delta-frames). |
| Event datasets (PublishedEvents) | Partial (decode only) | Rejected on wire-config import: `PubSubConfigValidationException` | No publish API exists. Received event DataSetMessages are decoded, delivered, and complete reader startup like key frames. See [Events](#events). |
| RawData field encoding and PromotedFields (UADP) | Absent | Rejected: `Bad_NotSupported` at startup, reconfigure, and activation | Enabled UADP groups/writers with these mask bits now fail up front; a component enabled later fails into Error at activation. The old trap — startup succeeds, every publish cycle silently skipped — is gone. Disabled components are tolerated; JSON `RawData` is implemented, not rejected. See [RawData and promoted fields](#rawdata-and-promoted-fields). |
| Chunked NetworkMessages | Absent | Inbound chunks: detected and skipped, `decodeErrors` + `Bad_NotSupported` `lastError` | No chunk emission or reassembly in either direction. Oversized messages are not split; hard wire-format limits throw `Bad_EncodingLimitsExceeded` at encode. See [Chunking and message size](#chunking-and-message-size). |
| `maxNetworkMessageSize` (writer and reader groups) | Works | UDP writer groups above 65535: rejected, `Bad_ConfigurationError` at startup/reconfigure | No longer inert. Writer side: when > 0, oversize NetworkMessages are skipped — never sent — with `Bad_EncodingLimitsExceeded` in group diagnostics; the default changed to 0 (no enforced limit). Reader side: when > 0, larger received messages are not seen by that group's readers (a documented Milo extension). See [Chunking and message size](#chunking-and-message-size). |
| Live reconfiguration (`reconfigure` / `update`, by replacement) | Works | n/a | Minimal-diff restarts (`DISABLE_AFFECTED`) or whole-connection restart (`STOP_AND_RESTART`); the new config is validated before anything changes, so a rejected reconfigure leaves the old config running. On broker connections any reader-side change escalates to a full connection restart. `ServerPubSub`'s info model and auto-bindings do not track `runtime()` reconfiguration. Restarted components get fresh diagnostics counters. |
| PubSub state machine, enable/disable, receive-timeout watchdog | Works | n/a | Part 14 states Disabled/Paused/PreOperational/Operational/Error over the whole runtime tree. Readers stay PreOperational until the first key frame (including zero-field heartbeats) or event. `messageReceiveTimeout > 0` arms a watchdog (Error + `Bad_Timeout` on silence, recovery on the next accepted message — sequence-window rejects don't count); the default (zero) disables it. |
| Diagnostics (per-path counters + listener) | Works | n/a | `snapshot()` exposes messages sent/received, decode errors, source errors, and `lastError` per component path. The diagnostics listener delivers error events only, and there is no listener removal API in this version. |
| Part 14 configuration round-trip (`PubSubConfiguration2DataType`) | Works | n/a | Both directions, with documented one-way losses: `BrokerSecurityConfig` is deliberately never serialized (credentials and key paths must not leave the process), `MetadataPolicy` is restored as `REQUIRE_CONFIGURED`, plus a few smaller fields listed in the `PubSubConfigMapper` Javadoc. Import throws `PubSubConfigValidationException` for mirrors, unknown transport profiles, non-PublishedDataItems dataset sources, and dangling security group ids. |
| Subscriber sequence-number tracking | Works | n/a (always on; no config knob exists) | Part 14 7.2.3 recency windows per publisher/writer stream, on both transports: duplicates and reordered-older messages are dropped as stale, out-of-window ones as invalid, counted per reader. Messages without sequence numbers bypass the window. Rejects are dropped, never reordered into place. See [Sequence numbers](#sequence-numbers). |
| Standalone SubscribedDataSets / SubscribedDataSetMirror | Partial / mirror Absent | Mirror rejected: `PubSubConfigValidationException` on import; standalone-carried TargetVariables silently inert in the server module (WARN at attach) | Standalone refs resolve a reader's configured decode metadata (wired into the decode path, but not data-flow tested); the mirror variant cannot be expressed in the typed API at all. See [Standalone SubscribedDataSets](#standalone-subscribeddatasets-and-mirror). |
| Keep-alive NetworkMessages (`keepAliveTime`) | Works | n/a | Emitted for any writer quiet past `keepAliveTime` — disabled/paused writers, and Operational writers whose cycles were suppressed (no changes) or whose messages were never transmitted — except writers in Error, which never emit keep-alives. Default is null (never emit); zero or negative values are rejected at build with `PubSubConfigValidationException`. |
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

`keyFrameCount` is honored, on both mappings. A writer emits a key frame on its first cycle after start or group activation and on every keyFrameCount-th publishing interval after that; the cycles between carry delta frames holding only the fields that changed since the last transmitted message (changed = the mask-projected wire value differs — a timestamp-only change counts only when timestamps are actually transmitted). A cycle in which nothing changed sends nothing at all: no empty delta, no sequence number consumed. Configure `keepAliveTime` if subscribers need a liveness signal across those quiet spells — quiet Operational writers now emit keep-alives (see the table above). Values 0 and 1 both mean every frame is a key frame (0 is the spec's non-cyclic value, which Milo does not model; the runtime clamps it). A delta that would carry every field is emitted as a key frame instead and resets the cadence. On the wire, UADP delta frames are DataSetFlags2 message type 0001 with per-field FieldIndex values (Part 14 Table 164); JSON delta frames are MessageType `ua-deltaframe` with a changed-fields-only payload (Table 185).

Two configurations cannot express delta frames and are rejected with `Bad_ConfigurationError` at startup and reconfigure (disabled components tolerated): a JSON writer with `keyFrameCount > 1` requires the DataSetMessageHeader in the group's effective network-message mask and the MessageType member in its DataSetMessage mask (Annex A.3.3.4); a UADP writer with `keyFrameCount > 1` cannot combine with a non-zero `ConfiguredSize` (fixed-size layouts are key-frame-only, Annex A.2.1.7). A writer that slips past validation — enabled after startup — degrades safely to every-cycle key frames. Custom mapping providers shadowing `uadp` or `json` own their wire format and are never second-guessed by these checks.

Receiving is unchanged: delta frames are decoded, for both UADP and JSON (`ua-deltaframe`), with per-field wire indices resolved against metadata. They are delivered to listeners, counted in diagnostics, reset the receive timeout, and recover a reader from Error — but they do not complete reader startup. A reader stays PreOperational until its first key frame or event message (a Part 14 SHALL): listeners see honest partial state from pre-baseline deltas while the reader's state says "no baseline yet", and the publisher's bounded key-frame cadence keeps the wait finite.

## Events

Event publishing has no API surface: there is no `PublishedEventsConfig`, and the encoders reject the event message kind with `Bad_NotSupported` (nothing in the publish path can produce one). This is a deliberate non-goal of this version, not an oversight. Importing a Part 14 wire config whose PublishedDataSet uses a `PublishedEventsDataType` source is rejected with `PubSubConfigValidationException` ("only PublishedDataItems dataset sources are supported").

Subscribing works: event DataSetMessages (UADP type `0x02`, JSON `ua-event`) are decoded and delivered like key frames, and an event message completes reader startup the same way a key frame does.

## RawData and promoted fields

UADP `RawData` field encoding and PromotedFields emission are absent, and they now fail fast: an enabled UADP-mapped writer group with the PromotedFields network-message mask bit, or an enabled writer with the RawData field-content mask bit, is rejected with `Bad_NotSupported` at startup and at reconfigure validation — before anything changes. The same checks run again at group and writer activation, so a component enabled after startup fails into PubSubState Error with `Bad_NotSupported` instead of running. The old trap — startup succeeds, the encoder rejects every publish cycle, and the writer looks healthy while sending nothing — is gone; the per-cycle encoder rejection remains only as a backstop for code driving the mapping API directly. Disabled components are tolerated so configs round-trip, and custom providers registered under the `uadp` mapping name are exempt (they own their wire format and may support these features).

On decode the behavior is tolerant: a RawData DataSetMessage is surfaced with `valid=false` and no fields (header fields intact), and a promoted-fields block is skipped over so the rest of the payload still decodes (the promoted values themselves are dropped).

JSON is unaffected: there the `RawData` mask bit is a Variant-representation modifier per Part 14, implemented and tested, not a rejection.

## Chunking and message size

There is no NetworkMessage chunking — no emission, no reassembly. Oversized messages are never split. But `maxNetworkMessageSize` is no longer inert: it is enforced as a skip-or-deliver budget on both sides.

On a writer group, a non-zero `maxNetworkMessageSize` is enforced at publish time on every transport: a NetworkMessage whose encoded size exceeds it is skipped — never sent — with a `Bad_EncodingLimitsExceeded` diagnostics error at the group path carrying the actual and maximum sizes. Skipped DataSetMessages are not counted as sent, a perpetually skipped writer lapses into keep-alives (if configured), and a delta writer whose message was skipped re-baselines with a key frame on its next cycle. The default changed from 1400 to 0, meaning no enforced limit; UDP writer groups with a value above 65535 fail startup and reconfigure validation with `Bad_ConfigurationError` (the Part 14 UDP cap). Discovery messages (fixed 4096-byte budget) and broker metadata publishing are exempt from the group budget.

On a reader group, a non-zero `maxNetworkMessageSize` (default 0 = unrestricted) is honored on receive — a documented Milo extension; Part 14 gives the reader side no size semantics: a larger NetworkMessage is not seen by that group's readers and ticks `decodeErrors` at the group path with `Bad_EncodingLimitsExceeded`. The subscriber's UDP receive buffer is 65535 bytes (the Part 14 cap), so any datagram the OS can deliver arrives without truncation.

The fixed wire-format limits still stand, enforced at encode time with `Bad_EncodingLimitsExceeded`: more than 255 DataSetMessages in one NetworkMessage with a payload header, or any DataSetMessage body over 65535 bytes when the sizes array is present. Inbound chunked NetworkMessages from other stacks are detected and skipped, no longer silently: they tick `decodeErrors` with a `Bad_NotSupported` `lastError` at the connection path — still no reassembly. Other inbound decode failures (truncation included) surface the same way, while UADP still delivers whatever decoded before the failure point. Discovery metadata announcements that exceed the fixed, non-configurable 4096-byte discovery limit are dropped with a WARN and a `Bad_EncodingLimitsExceeded` diagnostics entry.

Practical consequence: keep datasets small enough that one NetworkMessage fits your transport MTU or broker limit — the stack will not fragment for you — and set the writer group's `maxNetworkMessageSize` to your transport budget so oversize messages fail loudly into diagnostics instead of at the OS or broker.

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

The publisher emits sequence numbers per spec: a per-WriterGroup NetworkMessage sequence and a per-DataSetWriter DataSetMessage sequence, with keep-alives carrying the next expected number without incrementing it. The NetworkMessage sequence and the UADP DataSetMessage sequence wrap at UInt16; the JSON DataSetMessage sequence is genuinely UInt32 and rolls over at 2^32.

The subscriber now tracks them — always on, no config knob, both transports. Each reader keeps Part 14 7.2.3 recency windows per stream: per publisher and writer group for the NetworkMessage sequence (UADP only — JSON has none), per publisher and writer for the DataSetMessage sequence, with wildcard readers tracking each stream independently. Duplicates and reordered-older messages are dropped as stale; messages outside the window are dropped as invalid. Dropped means dropped: not delivered, no receive-timeout reset, no startup completion, no Error recovery — and never reordered into place, so a reordered message is lost, not delayed. This is a delivery-semantics change from earlier versions, which delivered duplicates; in particular, MQTT QoS 1 redelivery of sequence-numbered messages is now deduplicated. The per-reader counters `staleSequenceMessages` and `invalidSequenceMessages` tick in the diagnostics snapshot only (drops are normal operation: no `lastError`, no error event), and `DataSetReceivedEvent` now carries the wire's `networkMessageSequenceNumber` (UADP only) and `dataSetMessageSequenceNumber`. These are vendor counters on the SDK API; the spec-named ns0 counters are not implemented.

The edges: messages without sequence numbers bypass the window entirely — every one counts as new, per spec. Keep-alives seed unseeded streams and reseed the window when their carried value is off-window (the publisher is authoritative about its own counter; this is how a restarted or reconfigured publisher, whose sequence restarts at 0, recovers). Stream records are discarded after twice the `messageReceiveTimeout` of accepted-message or keep-alive silence; with the default timeout of 0 there is no time-based discard, and instead 16 consecutive rejections on a stream discard its record so the next message reseeds (WARN logged). Tracked streams are bounded per reader at 4096 per window kind — the NetworkMessage and DataSetMessage windows each keep their own map — with the least recently used stream evicted beyond that (WARN logged), a guard against rogue traffic fabricating stream identities. JSON MessageId-based deduplication is not implemented.

## The decoder accepts more than the encoder produces

A recurring pattern in the tables above: the decode path is a strict superset of the encode path. Receiving any of these from a third-party publisher is tolerated (chunked messages are the one case that records an error); producing them from Milo is impossible:

| Received thing | What happens on receive | Can Milo send it? |
|---|---|---|
| Event DataSetMessages | Decoded and delivered (complete reader startup) | No — no publish API |
| Chunked NetworkMessages | Detected, payload skipped; `decodeErrors` + `Bad_NotSupported` `lastError` | No — no chunking |
| RawData DataSetMessages (UADP) | Surfaced header-only with `valid=false` | No — config rejected up front (startup/reconfigure/activation) |
| PromotedFields blocks (UADP) | Skipped; rest of payload decodes | No — config rejected up front (startup/reconfigure/activation) |
| Security-flagged messages | Payload skipped without error | No — security encode rejected `Bad_NotSupported` |

Delta frames used to lead this table; Milo now sends them too — see [Delta frames](#delta-frames).

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
