# Milo PubSub (OPC UA Part 14)

Documentation for Milo's OPC UA Part 14 PubSub support: what it is, what works today, and where
to start reading. Written for Java developers who know Milo's client/server basics but are new to
PubSub.

Everything else in Milo is client/server: a session between two parties, with the server tracking
state per client. PubSub is the other shape — a publisher pushes messages onto the network on a
fixed cycle, addressed to no one in particular, and any number of subscribers decode the ones they
care about. Milo implements this in two transport styles: brokerless UDP, carrying UADP binary
messages as unicast or multicast datagrams, and broker-based MQTT, carrying UADP or JSON. The
runtime can be hosted standalone as a `PubSubService` — no OPC UA server required — or attached to
an `OpcUaServer` via `ServerPubSub`, where datasets publish live node values and received values
are written back into nodes.

The support ships as four Maven artifacts, all managed by `milo-bom`:

| Artifact | Contents |
|---|---|
| `org.eclipse.milo:milo-sdk-pubsub` | Configuration model, runtime engine, UADP and JSON codecs, message security, UDP transport |
| `org.eclipse.milo:milo-sdk-pubsub-mqtt` | MQTT transport, built on the HiveMQ MQTT client |
| `org.eclipse.milo:milo-sdk-pubsub-server` | `ServerPubSub`, the `OpcUaServer` integration, the opt-in SKS server face |
| `org.eclipse.milo:milo-sdk-pubsub-sks` | `SksSecurityKeyProvider`, the SKS pull client (depends on `milo-sdk-client`) |

The core module depends only on the Milo stack — not on the client or server SDK — so a standalone
publisher or subscriber pulls in no client/server machinery at all.

## What works, what doesn't

The headline capabilities, at a glance:

| Capability | Status |
|---|---|
| [UADP over UDP unicast](getting-started.md) | Works |
| UADP over UDP multicast | Partial — fully implemented and interop-verified; in-repo automated coverage is indirect ([details](limitations-and-interop.md#milo-sdk-pubsub-core)) |
| [UADP over MQTT](mqtt.md) | Works |
| JSON over MQTT, including [retained metadata](mqtt.md#retained-metadata) | Works |
| [UDP metadata discovery](metadata-and-discovery.md#udp-discovery): publishers answer metadata probes, `REQUEST_IF_MISSING` readers send them | Works |
| [Server integration](server-integration.md): publishing live node values, and writing received values into nodes (TargetVariables) | Works |
| [Live reconfiguration](operations.md#live-reconfiguration) of a running service | Works |
| [Delta-frame](limitations-and-interop.md#delta-frames) (changed-fields-only) publishing | Works — `keyFrameCount` is honored; cycles between key frames send only the changed fields, or nothing at all when nothing changed |
| Subscriber [sequence-number tracking](limitations-and-interop.md#sequence-numbers): duplicate, reordered-older, and out-of-window messages are dropped and counted | Works |
| [UADP message security](message-security-and-sks.md) (Sign/SignAndEncrypt, PubSub-Aes128-CTR and PubSub-Aes256-CTR, both roles) | Works — JSON has no message security in OPC UA 1.05: secured JSON groups are rejected with `Bad_ConfigurationError` |
| [SKS](message-security-and-sks.md) (Security Key Service) key distribution | Works — pull only: an `SksSecurityKeyProvider` client, a `StaticSecurityKeyProvider` for pre-shared keys, and an opt-in `GetSecurityKeys` server face; push (`SetSecurityKeys`) and the SKS management methods are not implemented |
| Remote configuration over OPC UA: the Part 14 [file model](server-integration.md#remote-configuration) + Enable/Disable methods | Works — opt-in (`allowRemoteConfiguration`); the deprecated per-element methods (`AddConnection` et al.) remain `Bad_NotImplemented` |
| [PubSub diagnostics](server-integration.md#exposing-diagnostics) in the server information model (Part 14 §9.1.11) | Works — opt-in (`diagnosticsEnabled`, with the exposed information model) |
| [PubSub status events](server-integration.md#status-events) (Part 14 §9.1.13) | Works — opt-in (`statusEventsEnabled`); state changes and send failures fired through the Server object |
| [Event](limitations-and-interop.md#events) publishing | Not yet — received event messages are decoded and delivered, but none are published |
| [NetworkMessage chunking](limitations-and-interop.md#chunking-and-message-size) | Partial — inbound chunked messages are reassembled (with hard DoS caps); chunk *emission* is absent: a configured `maxNetworkMessageSize` is enforced, not chunked, and an oversized message is skipped with a `Bad_EncodingLimitsExceeded` diagnostics error |
| UADP [`RawData` field encoding and PromotedFields](limitations-and-interop.md#rawdata-and-promoted-fields) | Not yet — rejected with `Bad_NotSupported` at startup, reconfigure, and activation |
| Ethernet, AMQP, and MQTT-over-WebSocket [transports](limitations-and-interop.md#transports) | Not yet — rejected at config build or connection open |
| `ServerPubSub` over MQTT | Not yet — UDP/UADP only; rejected with `Bad_ConfigurationError` |

A distinction that matters beyond this table: some unsupported configuration is rejected with a
named error, and some is accepted and silently ignored. The full per-module matrix — every
feature, its status, and exactly what happens if you configure it anyway — is in
[Limitations and interop](limitations-and-interop.md). Read it before depending on a Part 14
feature, and especially before debugging a config option that seems to do nothing.

## Where to start

Start with [Getting started](getting-started.md). It explains the PubSub model in Milo terms,
gives the dependency snippets, and walks a complete UDP/UADP publisher and subscriber pair —
config graph, data source, listener, lifecycle — ending with verified commands and real output.
Everything else builds on it.

When the one-writer pair stops being enough, [Structuring and sizing](structuring-and-sizing.md)
is the natural second read: what each level of the configuration is for — connections, groups,
writers, readers — how multiple writers pack onto the wire, and what bounds how large a dataset
can be, anchored by a runnable two-group example pair.

Then branch by what you're building:

- Publishing through a broker? [The MQTT transport](mqtt.md) — transport registration, broker
  URIs, the Part 14 topic tree, QoS, client identity, outage behavior, and TLS.
- Attaching PubSub to an `OpcUaServer`? [Server integration](server-integration.md) —
  `ServerPubSub.attach`, publishing node values with no source code, writing received
  datasets into nodes with TargetVariables, and the opt-in server surfaces: the exposed
  information model, remote configuration, diagnostics, and status events.
- Signing or encrypting messages? [Message security and SKS](message-security-and-sks.md) —
  the two security policies and modes, configuring secured groups, static and SKS-pulled keys,
  the key lifecycle, and serving keys from an `OpcUaServer`.

Three pages serve as reference once you're past the tutorial:

- [Configuration model](configuration.md) — the builder graph, validation moments, defaults,
  content masks, and which knobs do nothing yet.
- [Metadata and discovery](metadata-and-discovery.md) — how subscribers learn field names and
  types: the three `MetadataPolicy` values, UDP discovery, and retained MQTT metadata.
- [Operations](operations.md) — the component state machine, the receive-timeout watchdog,
  sequence-number tracking and what it drops, live reconfiguration, diagnostics, and threading
  and shutdown rules.

Finally, [Examples](examples.md) is the lab bench: a catalog of every runnable example in
`milo-examples/pubsub-examples`, with a port map, verified run commands, expected output, and a
troubleshooting section. All examples run with zero arguments on one machine, and the MQTT
scenarios include an embedded broker so no external infrastructure is needed.

One operational rule worth stating even on the index, because it makes a working example look
broken: when running examples with `mvn exec:java`, never pass `-q` to the `exec:java` step — it
silences the example's own log output completely. `-q` belongs on the build step only. The full
explanation of why is in the examples page's [troubleshooting section](examples.md#troubleshooting).
