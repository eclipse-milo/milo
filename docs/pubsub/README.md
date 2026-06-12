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

The support ships as three Maven artifacts, all managed by `milo-bom`:

| Artifact | Contents |
|---|---|
| `org.eclipse.milo:milo-sdk-pubsub` | Configuration model, runtime engine, UADP and JSON codecs, UDP transport |
| `org.eclipse.milo:milo-sdk-pubsub-mqtt` | MQTT transport, built on the HiveMQ MQTT client |
| `org.eclipse.milo:milo-sdk-pubsub-server` | `ServerPubSub`, the `OpcUaServer` integration |

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
| Message security (Sign/SignAndEncrypt) and SKS (Security Key Service) key distribution | Not yet — enabled secure groups fail startup with `Bad_NotSupported` ([details](limitations-and-interop.md#message-security-and-sks)) |
| [Delta-frame](limitations-and-interop.md#delta-frames) (changed-fields-only) and [event](limitations-and-interop.md#events) publishing | Not yet — both are decode-only; every published frame is a full key frame, and `keyFrameCount` is silently inert |
| [NetworkMessage chunking](limitations-and-interop.md#chunking-and-message-size) (splitting oversized messages) | Not yet — `maxNetworkMessageSize` is silently inert |
| Ethernet, AMQP, and MQTT-over-WebSocket [transports](limitations-and-interop.md#transports) | Not yet — rejected at config build or connection open |
| Remote configuration over OPC UA | Not yet — the standard PubSub method nodes (ns0) return `Bad_NotImplemented` ([details](limitations-and-interop.md#server-integration-limits)) |
| `ServerPubSub` over MQTT | Not yet — UDP/UADP only; rejected with `Bad_ConfigurationError` |

The distinction in the right column matters: some unsupported configuration is rejected with a
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
  `ServerPubSub.attach`, publishing node values with no source code, and writing received
  datasets into nodes with TargetVariables.

Three pages serve as reference once you're past the tutorial:

- [Configuration model](configuration.md) — the builder graph, validation moments, defaults,
  content masks, and which knobs do nothing yet.
- [Metadata and discovery](metadata-and-discovery.md) — how subscribers learn field names and
  types: the three `MetadataPolicy` values, UDP discovery, and retained MQTT metadata.
- [Operations](operations.md) — the component state machine, the receive-timeout watchdog, live
  reconfiguration, diagnostics, and threading and shutdown rules.

Finally, [Examples](examples.md) is the lab bench: a catalog of every runnable example in
`milo-examples/pubsub-examples`, with a port map, verified run commands, expected output, and a
troubleshooting section. All examples run with zero arguments on one machine, and the MQTT
scenarios include an embedded broker so no external infrastructure is needed.

One operational rule worth stating even on the index, because it makes a working example look
broken: when running examples with `mvn exec:java`, never pass `-q` to the `exec:java` step — it
silences the example's own log output completely. `-q` belongs on the build step only. The full
explanation of why is in the examples page's [troubleshooting section](examples.md#troubleshooting).
