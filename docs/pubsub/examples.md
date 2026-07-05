# PubSub Examples

This page catalogs the runnable examples in `milo-examples/pubsub-examples` (artifactId `milo-pubsub-examples`) and gives the exact commands to run each one. Use it when you want to see PubSub exchanging real data before — or while — reading the concept pages.

## Building and running

Build the examples module once from the repository root. This also installs the SNAPSHOT PubSub SDK modules the examples depend on; `exec:java` does not compile, so rerun this after pulling changes:

```sh
mvn -q -pl milo-examples/pubsub-examples -am install -DskipTests
```

Then run any example with:

```sh
mvn -pl milo-examples/pubsub-examples exec:java -Dexec.mainClass=<fully.qualified.ClassName>
```

Do not put `-q` on the `exec:java` command. `exec:java` runs the example inside the Maven JVM, so the example's SLF4J logging binds to Maven's own logger, and `-q` caps that logger at errors — the example runs fine but prints nothing at all. Keep `-q` on the build step only.

Two things to know about the output you will see:

- Maven prints a dozen or so lines of build output (and, on newer JDKs, harmless Netty `sun.misc.Unsafe` warnings) before the first example line, and prefixes each example line with `[thread] INFO logger.name - `. The excerpts below show only the message portion after the dash.
- Maven plus JVM startup typically takes around 10 seconds before an example's startup line appears, and a subscriber prints nothing between its startup line and the first `[received]` line. A quiet log for a few seconds is normal, not a hang.

All examples run on Java 17 or newer, with one exception: `EmbeddedBrokerExample` embeds HiveMQ CE, which does not bootstrap on JDK 25 or later — run that one example on JDK 17 or 21. The MQTT publisher and subscriber examples themselves are fine on newer JDKs when pointed at an external broker.

Long-running examples run until Ctrl-C; a shutdown hook closes the PubSub service cleanly. `ReconfigureExample` is the exception — it runs for about 15 seconds and exits on its own.

## Catalog

All classes are under `org.eclipse.milo.examples.pubsub`.

| Scenario | Classes | Demonstrates | Concepts |
|---|---|---|---|
| UDP unicast, UADP | `udp.UadpPublisherExample`, `udp.UadpSubscriberExample` | The minimal pub/sub pair: config builders, pull-model data source, push-model listener | [Getting started](getting-started.md) |
| UDP multicast + discovery | `udp.DiscoveryPublisherExample`, `udp.DiscoverySubscriberExample` | Multicast data plane; the subscriber configures no metadata and learns field names via discovery | [Metadata and discovery](metadata-and-discovery.md) |
| UDP multi-group | `udp.MultiGroupPublisherExample`, `udp.MultiGroupSubscriberExample` | Two writer groups at different rates on one connection; three streams separated by per-reader listeners | [Structuring and sizing](structuring-and-sizing.md) |
| MQTT, UADP | `mqtt.MqttUadpPublisherExample`, `mqtt.MqttUadpSubscriberExample` | UADP messages over an MQTT broker: topic derivation, explicit reader `queueName` | [MQTT transport](mqtt.md) |
| MQTT, JSON + retained metadata | `mqtt.MqttJsonPublisherExample`, `mqtt.MqttJsonSubscriberExample` | JSON mapping; the broker's retained `ua-metadata` document supplies the reader's field definitions | [MQTT transport](mqtt.md) |
| Embedded MQTT broker | `mqtt.EmbeddedBrokerExample` | An in-process HiveMQ CE broker so the MQTT examples need no external infrastructure | [MQTT transport](mqtt.md) |
| Server publish (auto-source) | `server.ServerSourcePublisherExample`, `server.ServerSourceSubscriberExample` | `ServerPubSub` publishing live address-space node values via `NodeFieldAddress` auto-binding | [Server integration](server-integration.md) |
| Server subscribe (TargetVariables) | `server.ServerTargetPublisherExample`, `server.ServerTargetSubscriberExample` | `ServerPubSub` writing received fields into address-space nodes, with status and timestamp pass-through | [Server integration](server-integration.md) |
| Event publishing | `mqtt.MqttJsonEventPublisherExample`, `server.ServerEventPublisherExample` | Publishing events instead of cyclic data: a `PublishedEventsConfig` source in an interval-0 event-triggered group, over MQTT/JSON and via the `ServerPubSub` event-notifier binding | [Events](limitations-and-interop.md#events) |
| Live reconfigure | `ReconfigureExample` | `service.update(...)` changing the publishing interval at runtime; restart scoping and diagnostics | [Operations](operations.md) |

## Ports and identifiers

Every example pins fixed loopback addresses and PublisherIds so the pairs find each other with zero arguments, and so all pairs can run on one machine at the same time. Each process needs its own discovery port, which is why the publisher and subscriber defaults differ.

| Scenario | Data address | Discovery address (publisher / subscriber) | PublisherId |
|---|---|---|---|
| UDP unicast | `127.0.0.1:15100` | `127.0.0.1:15101` / `127.0.0.1:15102` | 1001 |
| UDP multicast + discovery | `239.255.20.1:15110` | `239.255.20.2:15111` (shared multicast group) | 1002 |
| UDP multi-group | `127.0.0.1:15150` | `127.0.0.1:15151` / `127.0.0.1:15152` | 7001 |
| MQTT, UADP | broker `mqtt://127.0.0.1:1883`, topic `opcua/uadp/data/2001/demo` | — | 2001 |
| MQTT, JSON | broker `mqtt://127.0.0.1:1883`, topic `opcua/json/data/3001/demo` | metadata topic `opcua/json/metadata/3001/demo/writer` | 3001 |
| Server publish | `127.0.0.1:15120` | `127.0.0.1:15121` / `127.0.0.1:15122` | 4001 |
| Server subscribe | `127.0.0.1:15130` | `127.0.0.1:15132` / `127.0.0.1:15131` | 5001 |
| Reconfigure | `127.0.0.1:15140` | `127.0.0.1:15141` / `127.0.0.1:15142` (one process) | 6001 |

## UDP unicast, UADP

The getting-started pair. Start the subscriber first — UDP is connectionless, so nothing breaks if you don't, but messages published before the subscriber binds the port are simply lost.

Terminal 1:

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.UadpSubscriberExample
```

Terminal 2:

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.UadpPublisherExample
```

Subscriber output, two lines per second once the publisher is up:

```
UADP subscriber started: listening on opc.udp://127.0.0.1:15100 for dataset "telemetry" from publisher 1001, Ctrl-C to exit
[received] dataSet=telemetry publisherId=1001 dataSetWriterId=1 fields: temperature=20.0, status=true, counter=0
[received] dataSet=telemetry publisherId=1001 dataSetWriterId=1 fields: temperature=20.26167978121472, status=true, counter=1
[received] dataSet=telemetry publisherId=1001 dataSetWriterId=1 fields: temperature=20.522642316338267, status=true, counter=2
```

The publisher logs `UADP publisher started: publishing dataset "telemetry" to opc.udp://127.0.0.1:15100 every 500ms, Ctrl-C to exit`, then a `[publishing] cycle=N ...` line every 20 cycles. No arguments are needed.

## UDP multicast + discovery

The publisher sends UADP data to multicast group `239.255.20.1:15110` and answers metadata probes on `239.255.20.2:15111` — the discovery responder comes for free on any connection with writer groups. The subscriber configures no field definitions at all (`MetadataPolicy.REQUEST_IF_MISSING`); the named fields in its output are the proof that discovery worked.

Terminal 1:

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.DiscoveryPublisherExample
```

Terminal 2:

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.DiscoverySubscriberExample
```

Subscriber output:

```
subscribed to opc.udp://239.255.20.1:15110 as reader 'demo-reader', probing opc.udp://239.255.20.2:15111 for metadata
discovery delivered metadata: dataSet=telemetry, version=1.0, fields=[temperature, humidity, status]
[received] dataSet=telemetry temperature=23.728526060883603, humidity=44.08487440884157, status=OK
[received] dataSet=telemetry temperature=23.377315902755754, humidity=43.62357754476674, status=OK
```

The metadata line may appear twice — the responder announces on both the discovery and data addresses, and the event is not deduplicated. If a data frame beats the announcement, the first `[received]` line shows positional `Field_0`/`Field_1`/`Field_2` names with `dataSet=null`; named lines follow once metadata arrives. Both sides accept an optional argument to run across machines: `-Dexec.args="192.0.2.10"` sets the network interface address (default `127.0.0.1`).

## UDP multi-group

The runnable anchor for [Structuring and sizing](structuring-and-sizing.md): one connection (publisher 7001) carrying two writer groups at different rates — "fast" (250 ms) with two writers, "motion" and "vibration", and "slow" (2000 ms) with one, "status". The subscriber holds three readers in a single reader group, each with its own listener, so the three streams arrive in application code already separated. Start the subscriber first, as with any UDP pair.

Terminal 1:

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.MultiGroupSubscriberExample
```

Terminal 2:

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.MultiGroupPublisherExample
```

Subscriber output — four `[motion]` and four `[vibration]` lines per second, always adjacent because they travel packed in the same NetworkMessage, and one `[status]` line every two seconds:

```
multi-group subscriber started: listening on opc.udp://127.0.0.1:15150 for datasets "motion", "vibration", and "status" from publisher 7001, Ctrl-C to exit
[motion] position=0.0, velocity=100.0
[vibration] rms=0.8625737860160924, peak=2.4152066008450586
[status] uptimeSeconds=0, state=RUNNING
[motion] position=5.233595624294383, velocity=99.86295347545739
[vibration] rms=0.923606797749979, peak=2.586099033699941
```

The rate split is easy to verify in the log: over a ~32-second run, the subscriber printed 136 `[motion]` and 136 `[vibration]` lines against 17 `[status]` lines — exactly the 8:1 ratio of the 250 ms and 2000 ms intervals, with `uptimeSeconds` stepping from 0 to 32 in twos. The publisher logs `multi-group publisher started: groups "fast" (motion + vibration, 250ms) and "slow" (status, 2000ms) publishing to opc.udp://127.0.0.1:15150 as publisher 7001, Ctrl-C to exit`, then a `[summary]` line every 10 fast cycles (2.5 seconds) showing what all three sources most recently published. No arguments are needed.

## Embedded MQTT broker

`EmbeddedBrokerExample` starts an in-process HiveMQ CE broker on `127.0.0.1:1883` so the MQTT examples need no infrastructure. It requires JDK 17 or 21; on JDK 25+ it exits with `HiveMQ CE does not bootstrap on JDK <N>; run this on JDK 17 or 21.` (where `<N>` is the running JDK version). Any external broker (Mosquitto, HiveMQ, EMQX) substitutes — just pass its URI to the publisher and subscriber examples.

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.EmbeddedBrokerExample
```

On macOS with a newer default JDK, prefix the command with `env JAVA_HOME=$(/usr/libexec/java_home -v 21)`. Append `-Dexec.args="1884"` to change the port. Expected output, after a few seconds of bootstrap chatter:

```
starting embedded HiveMQ CE broker on 127.0.0.1:1883 (config dir: /var/folders/.../milo-pubsub-example-broker...)...
broker ready on port 1883
```

The `broker ready on port <port>` line prints exactly once and is the signal to start the other examples. If the port is taken it exits with one clear `cannot bind 127.0.0.1:1883 (port already in use?)` error, no stack trace.

## MQTT, UADP

UADP NetworkMessages carried over an MQTT broker. The publisher derives its topic (`opcua/uadp/data/2001/demo`) from the Part 14 rules; the subscriber cannot derive it and configures the topic explicitly as its reader's `queueName`.

Start the broker first (previous section, or any broker on `mqtt://127.0.0.1:1883`). Then, in either order — the publisher publishes continuously, so the subscriber picks up wherever it joins:

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.MqttUadpSubscriberExample
```

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.MqttUadpPublisherExample
```

Subscriber output, about two lines per second:

```
subscribed to topic opcua/uadp/data/2001/demo, waiting for DataSets...
[received] dataSet=telemetry publisherId=2001 writerGroupId=1 dataSetWriterId=1 fields: temperature=20.49916708323414, counter=1, status=running
[received] dataSet=telemetry publisherId=2001 writerGroupId=1 dataSetWriterId=1 fields: temperature=20.993346653975305, counter=2, status=running
```

The publisher logs `publishing dataset "telemetry" every 500ms on topic opcua/uadp/data/2001/demo`, then a `publish cycle N: ...` line every 10th cycle. Both accept an optional broker URI: `-Dexec.args="mqtt://10.0.0.5:1883"`.

## MQTT, JSON + retained metadata

JSON-mapped messages plus the broker metadata path: the publisher automatically publishes a retained `ua-metadata` document, and the subscriber — configured with no field definitions and `MetadataPolicy.ACCEPT_DISCOVERED` — gets its dataset name and field definitions from that retained message. This is the MQTT substitute for UDP discovery probing.

Start the broker first. Publisher and subscriber can then start in either order, because the broker retains the metadata:

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.MqttJsonPublisherExample
```

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.MqttJsonSubscriberExample
```

Subscriber output — the `metadata received` line is the retained-metadata replay, and the named fields prove it was applied:

```
subscriber started: broker=mqtt://127.0.0.1:1883, data topic=opcua/json/data/3001/demo, metadata topic=opcua/json/metadata/3001/demo/writer
metadata received: dataSet=demo-dataset version=1.1 fields=[temperature, status]
[received] publisherId=3001 dataSet=demo-dataset temperature=20.522642316338267, status=running
[received] publisherId=3001 dataSet=demo-dataset temperature=21.039558454088798, status=running
```

The publisher logs `publisher started: broker=mqtt://127.0.0.1:1883, publishing 'ua-data' to topic opcua/json/data/3001/demo every 1s` and a `publish cycle N` line every 10th cycle. Both accept the optional broker URI argument.

## Server publish (auto-source)

`ServerSourcePublisherExample` attaches `ServerPubSub` to an `OpcUaServer` whose namespace updates Temperature, Pressure, and Counter nodes twice a second. Because every field of its PublishedDataSet is a `NodeFieldAddress`, the dataset is auto-bound at attach time — fresh node values are read each publish cycle with no application code. The subscriber is a plain standalone `PubSubService`. Either start order works.

Terminal 1:

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.server.ServerSourcePublisherExample
```

Terminal 2:

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.server.ServerSourceSubscriberExample
```

Subscriber output:

```
listening on 127.0.0.1:15120 for publisherId=4001 writerGroupId=1 dataSetWriterId=1
[received] dataSet=demo-nodes publisherId=4001 writerGroupId=1 dataSetWriterId=1 Temperature=22.94, Pressure=1014.02, Counter=24
[received] dataSet=demo-nodes publisherId=4001 writerGroupId=1 dataSetWriterId=1 Temperature=22.5, Pressure=1013.9, Counter=25
```

The publisher logs `publishing "demo-nodes" (publisherId=4001, writerGroupId=1, dataSetWriterId=1) to 127.0.0.1:15120 every 500 ms` and an `update #N` line every 10th update. An occasional repeated or skipped Counter value is expected — the publish cycle and the node-update cycle are not phase-locked. Optional args on both sides: `-Dexec.args="<dataPort> <discoveryPort>"`.

## Server subscribe (TargetVariables)

The mirror image: `ServerTargetSubscriberExample` runs an `OpcUaServer` with a reader whose TargetVariables mapping writes each received field into an address-space node, including the publisher's StatusCode and source timestamp. It logs both views — `[received]` for the wire, `[node]` for the values landed in the nodes.

Start the subscriber first: it binds the data port, and messages published before the bind are silently lost.

Terminal 1:

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.server.ServerTargetSubscriberExample
```

Terminal 2:

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.server.ServerTargetPublisherExample
```

Subscriber output — about two `[received]` lines and one `[node]` line per second:

```
ServerPubSub started, reader listening on opc.udp://127.0.0.1:15130
[received] publisherId=UInt16Id[value=5001] writerGroupId=1 dataSetWriterId=1 fields: temperature=24.817790927085966, counter=13
[node] Temperature=20.49916708323414 (status=StatusCode[name=Good, ...], sourceTime=...) Counter=1 (...)
```

The `sourceTime` on the `[node]` lines is the publisher's publish-time timestamp, not the write time — that is the pass-through working. The publisher logs `Publishing "demo-ds" to opc.udp://127.0.0.1:15130 every 500 ms as publisherId=UInt16Id[value=5001]`. Optional args on both sides: `-Dexec.args="<dataPort> <discoveryPort>"`.

## Event publishing

Two examples publish *events* rather than a cyclic dataset (see
[Events](limitations-and-interop.md#events)): a standalone MQTT/JSON publisher and a `ServerPubSub`
publisher driven by the server's event bus. Both build a `PublishedDataSetConfig` with a
`PublishedEventsConfig` source in an interval-0 event-triggered writer group, and each event goes out
as its own event DataSetMessage.

### MQTT, JSON events

`MqttJsonEventPublisherExample` publishes JSON `ua-event` messages to an MQTT broker: it builds an
event dataset, then calls `PubSubService.publishEvent(...)` on a timer to emit events. Start a broker
first (`EmbeddedBrokerExample`, or any external broker on `mqtt://127.0.0.1:1883`), then run the
publisher. To watch the events arrive, subscribe with any JSON subscriber, or use the
`PubSubInteropTool` `mqtt-subscribe` mode, which labels each delivered DataSetMessage `EVENT` or
`DATA`.

```sh
mvn -q -pl milo-examples/pubsub-examples -am install -DskipTests
```

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.MqttJsonEventPublisherExample
```

The publisher logs a startup line naming its broker and event topic, then one line per event it
emits. As with the other MQTT examples, this one needs a broker and runs on any JDK when pointed at
an external one (the embedded broker needs JDK 17 or 21).

### Server event publishing

`ServerEventPublisherExample` attaches `ServerPubSub` to an `OpcUaServer` and lets the
[event-notifier binding](server-integration.md#publishing-events-the-event-notifier-binding) do the
publishing: it fires events into the server's address space, and the binding evaluates the event
dataset's filter and pushes the selected fields to the runtime automatically — there is no
`publishEvent` call in the example. The events are published over UDP/UADP; observe them with a UDP
subscriber or the `PubSubInteropTool` subscribe/raw modes, which mark each DataSetMessage `EVENT`.

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.server.ServerEventPublisherExample
```

Both examples run until Ctrl-C with a shutdown hook, and — like the rest of the page — must be run
without `-q` on the `exec:java` step (keep `-q` on the build). The `ServerPubSub` variant does not
need a started server; attach and the event bus work on an endpoint-less server exactly as the
[auto-source examples](#server-publish-auto-source) do.

## Live reconfigure

`ReconfigureExample` is a single self-terminating process: it hosts a publisher and subscriber service in one JVM, runs at a 1000 ms publishing interval for ~5 seconds, calls `service.update(...)` to drop the interval to 250 ms, runs another ~5 seconds, prints a summary, and exits 0.

```sh
mvn -pl milo-examples/pubsub-examples exec:java \
  -Dexec.mainClass=org.eclipse.milo.examples.pubsub.ReconfigureExample
```

Expected output over roughly 15 seconds — note the visibly higher rate of phase 2, that only the writer group restarted, and that phase 2 begins immediately at the next tick (exact counts drift by one or two with timing):

```
[received] phase=1 dataSet=demo temperature=20.99, tick=1
[received] phase=1 dataSet=demo temperature=24.66, tick=6
[reconfigured] restartedPaths=[pub-conn/group]
[received] phase=2 dataSet=demo temperature=22.58, tick=7
[summary] phase 1: 6 DataSets received in ~5s at 1000ms
[summary] phase 2: 20 DataSets received in ~5s at 250ms
```

There is no gap between the phases: a path-stable group restart preserves the writers' sequence numbering, so the reader — untouched by the reconfigure and still holding phase 1's sequence window — accepts the restarted stream's first message as the next in sequence, and the reader's `staleSequenceMessages` counter stays at zero. (Earlier revisions restarted the sequence at 0 here, and this example demonstrated the resulting drop window; that window is gone.)

The summary also reports diagnostics: both the writer's sent counter and the reader's received counter span both phases — restarted components keep their counters across a path-stable restart, another consequence of the same preservation rule. See [Operations](operations.md) for what restarts when, the preservation rules and the cases that still reset, and how subscribers recover when a reset does happen.

## Troubleshooting

No output at all, for minutes — you put `-q` on the `exec:java` command. The example is almost certainly running fine; `-q` silences its logging entirely. Rerun without `-q`.

`ClassNotFoundException` or stale behavior — `exec:java` does not compile. Rerun the one-time build command, and don't rebuild the module from another terminal while an example is running.

Subscriber starts but never prints `[received]` — another process is holding the example's UDP port (check with `lsof -nP -iUDP:15100` etc.), or a host firewall is blocking loopback multicast for the discovery pair. Remember each process also needs its own free discovery port; the defaults in the port table are already distinct.

Endless `Server sent DISCONNECT` warnings on MQTT, with data flowing in bursts — two running engines share a PublisherId on the same broker. The MQTT client ID defaults to the canonical PublisherId string, so the duplicates keep taking over each other's session. Run one engine per PublisherId per broker, or set an explicit client ID; see [MQTT transport](mqtt.md).

JVM lingers after Ctrl-C, or Maven prints "thread ... will linger despite being asked to die" — a known, harmless interaction between `exec:java` and Milo's shared daemon executors. Pass `-Dexec.cleanupDaemonThreads=false` to suppress it; see [Operations](operations.md) for shutdown ordering.

Bare `exec:java` fails to resolve — the plugin is not declared in the module POM, so in rare Maven setups the prefix may not resolve; use the fully qualified goal `org.codehaus.mojo:exec-maven-plugin:3.5.0:java` instead.
