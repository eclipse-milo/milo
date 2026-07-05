/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.pubsub.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.EventFieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedEventsConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.server.ServerPubSub;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Publishes {@link OpcUaServer} events over OPC UA PubSub (UADP over UDP unicast) using {@link
 * ServerPubSub}'s automatic event-notifier source.
 *
 * <p>What it demonstrates: the PublishedDataSet is a {@link PublishedEventsConfig} whose {@code
 * eventNotifier} is the {@code Server} object ({@code i=2253}). {@link ServerPubSub#attach}
 * registers a listener on the server's event bus; every event fired under that notifier is matched
 * against the dataset's where-clause, its selected fields are extracted, and it is pushed into the
 * PubSub runtime as its own {@code EVENT} DataSetMessage. A background task fires one {@code
 * BaseEventType} event every 2 s through {@code server.getEventNotifier().fire(...)}; no {@code
 * publishEvent} call is made by this example — the notifier adapter turns fired server events into
 * published PubSub events.
 *
 * <p>The {@link OpcUaServer} built here is intentionally minimal and has <b>no endpoints or
 * transports</b>, keeping the example focused on PubSub. It is never started as a full server (that
 * requires a bound endpoint); instead only its {@code EventFactory} is started, which registers the
 * transient event nodes so their fields can be selected synchronously when an event fires. {@link
 * ServerPubSub} works exactly the same attached to a real, started server.
 *
 * <p>Build the examples module once, then run from the repository root:
 *
 * <pre>{@code
 * mvn -q -pl milo-examples/pubsub-examples -am install -DskipTests
 *
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *     -Dexec.mainClass=org.eclipse.milo.examples.pubsub.server.ServerEventPublisherExample
 * }</pre>
 *
 * <p>Optional arguments override the data port (default {@code 15130}) and this side's discovery
 * port (default {@code 15131}), e.g. {@code -Dexec.args="15130 15131"}.
 *
 * <p>Expected output: one startup line announcing the publisher is running, then a {@code fired
 * event #N: sourceName=..., severity=..., message=...} line every 2 s, until Ctrl-C. Each fired
 * event is published as a UADP {@code EVENT} NetworkMessage carrying the selected
 * SourceName/Severity/Message field values.
 */
public class ServerEventPublisherExample {

  private static final String DATA_HOST = "127.0.0.1";
  private static final int DEFAULT_DATA_PORT = 15130;
  private static final int DEFAULT_DISCOVERY_PORT = 15131;

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4010));
  private static final UShort WRITER_GROUP_ID = ushort(1);
  private static final UShort DATA_SET_WRITER_ID = ushort(1);

  private static final Duration PUBLISHING_INTERVAL = Duration.ofMillis(500);
  private static final Duration EVENT_INTERVAL = Duration.ofSeconds(2);

  public static void main(String[] args) throws Exception {
    int dataPort = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_DATA_PORT;
    int discoveryPort = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_DISCOVERY_PORT;

    new ServerEventPublisherExample(dataPort, discoveryPort).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AtomicLong eventCount = new AtomicLong(0);

  private final int dataPort;
  private final int discoveryPort;

  public ServerEventPublisherExample(int dataPort, int discoveryPort) {
    this.dataPort = dataPort;
    this.discoveryPort = discoveryPort;
  }

  private void run() throws Exception {
    // An endpoint-less OpcUaServer: the address space (including ns0 event types) loads in the
    // constructor. Attaching to a real, started server works exactly the same way.
    OpcUaServerConfig serverConfig =
        OpcUaServerConfig.builder()
            .setApplicationUri("urn:eclipse:milo:examples:pubsub:server-event")
            .setApplicationName(LocalizedText.english("PubSub server-event example"))
            .setProductUri("urn:eclipse:milo:examples:pubsub")
            .build();

    var server =
        new OpcUaServer(
            serverConfig,
            transportProfile -> {
              throw new IllegalStateException(
                  "this example server has no transports: " + transportProfile);
            });

    // Start only the EventFactory (not the full server, which would require a bound endpoint): this
    // registers the transient event nodes minted by createEvent() so the notifier binding can
    // select
    // their fields synchronously when an event fires.
    server.getEventFactory().startup();

    ServerPubSub serverPubSub = ServerPubSub.attach(server, buildPubSubConfig());

    serverPubSub.startup().get();

    logger.info(
        "publishing \"demo-events\" (publisherId={}, writerGroupId={}, dataSetWriterId={})"
            + " to {}:{}, firing one event every {} ms",
        PUBLISHER_ID.toCanonicalString(),
        WRITER_GROUP_ID,
        DATA_SET_WRITER_ID,
        DATA_HOST,
        dataPort,
        EVENT_INTERVAL.toMillis());

    ScheduledExecutorService firer =
        Executors.newSingleThreadScheduledExecutor(
            r -> {
              Thread thread = new Thread(r, "example-event-firer");
              thread.setDaemon(true);
              return thread;
            });

    firer.scheduleAtFixedRate(
        () -> fireServerEvent(server),
        EVENT_INTERVAL.toMillis(),
        EVENT_INTERVAL.toMillis(),
        TimeUnit.MILLISECONDS);

    final CompletableFuture<Void> future = new CompletableFuture<>();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> future.complete(null)));

    future.get();

    firer.shutdownNow();
    serverPubSub.close();
    server.getEventFactory().shutdown();
    // the OpcUaServer was never started, so there is no server.shutdown() to call
  }

  /**
   * Build the PubSub configuration: one event-source PublishedDataSet whose notifier is the {@code
   * Server} object, published by one writer over UDP unicast as UADP {@code EVENT} NetworkMessages.
   */
  private PubSubConfig buildPubSubConfig() {
    // Field order is wire order; each field selects a standard BaseEventType field by browse name.
    // The Server notifier (i=2253) is unscoped, so every fired event is a candidate.
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("demo-events")
            .source(
                PublishedEventsConfig.builder(NodeIds.Server.expanded())
                    .field(eventField("sourceName", "SourceName", NodeIds.String))
                    .field(eventField("severity", "Severity", NodeIds.UInt16))
                    .field(eventField("message", "Message", NodeIds.LocalizedText))
                    .build())
            .build();

    // GroupHeader + WriterGroupId let a subscriber filter on writerGroupId; PayloadHeader carries
    // the dataSetWriterId so events can be routed to the matching event reader.
    UadpWriterGroupSettings groupSettings =
        UadpWriterGroupSettings.builder()
            .networkMessageContentMask(
                UadpNetworkMessageContentMask.of(
                    UadpNetworkMessageContentMask.Field.PublisherId,
                    UadpNetworkMessageContentMask.Field.GroupHeader,
                    UadpNetworkMessageContentMask.Field.WriterGroupId,
                    UadpNetworkMessageContentMask.Field.SequenceNumber,
                    UadpNetworkMessageContentMask.Field.PayloadHeader))
            .build();

    // SequenceNumber lets the reader detect loss; event fields are always UADP Variant-encoded, so
    // no DataSetFieldContentMask is configured (its field-encoding bits are ignored for events).
    UadpDataSetWriterSettings writerSettings =
        UadpDataSetWriterSettings.builder()
            .dataSetMessageContentMask(
                UadpDataSetMessageContentMask.of(
                    UadpDataSetMessageContentMask.Field.SequenceNumber))
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp("server-conn")
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast(DATA_HOST, dataPort))
                // Pin the discovery plane to a loopback unicast port; otherwise the engine binds
                // UDP
                // 4840 and joins multicast group 224.0.2.14.
                .discoveryAddress(UdpDatagramAddress.unicast(DATA_HOST, discoveryPort))
                .writerGroup(
                    WriterGroupConfig.builder("demo")
                        .writerGroupId(WRITER_GROUP_ID)
                        .publishingInterval(PUBLISHING_INTERVAL)
                        .messageSettings(groupSettings)
                        .dataSetWriter(
                            DataSetWriterConfig.builder("writer")
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(DATA_SET_WRITER_ID)
                                .settings(writerSettings)
                                .build())
                        .build())
                .build())
        .build();
  }

  /**
   * Fire one synthetic {@code BaseEventType} event under the {@code Server} notifier. The attached
   * {@link ServerPubSub} notifier binding selects the dataset's fields from it and publishes it as
   * an {@code EVENT} DataSetMessage; the transient event node is deleted once fired.
   */
  private void fireServerEvent(OpcUaServer server) {
    long n = eventCount.incrementAndGet();

    String sourceName = "demo-source";
    UShort severity = ushort(100 + (int) (n % 9) * 100);
    String message = "synthetic event #" + n;

    BaseEventTypeNode eventNode;
    try {
      eventNode =
          server
              .getEventFactory()
              .createEvent(new NodeId(1, UUID.randomUUID()), NodeIds.BaseEventType);
    } catch (UaException e) {
      logger.warn("failed to create event node", e);
      return;
    }

    try {
      eventNode.setBrowseName(new QualifiedName(1, "demo-event"));
      eventNode.setDisplayName(LocalizedText.english("demo-event"));
      eventNode.setEventId(
          ByteString.of(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
      eventNode.setEventType(NodeIds.BaseEventType);
      eventNode.setSourceNode(NodeIds.Server);
      eventNode.setSourceName(sourceName);
      eventNode.setTime(DateTime.now());
      eventNode.setReceiveTime(DateTime.NULL_VALUE);
      eventNode.setMessage(LocalizedText.english(message));
      eventNode.setSeverity(severity);

      server.getEventNotifier().fire(eventNode);
    } finally {
      eventNode.delete();
    }

    logger.info(
        "fired event #{}: sourceName={}, severity={}, message={}",
        n,
        sourceName,
        severity,
        message);
  }

  /**
   * An event field selecting the BaseEventType field {@code browseName} under the name {@code
   * name}.
   */
  private static EventFieldDefinition eventField(String name, String browseName, NodeId dataType) {
    return EventFieldDefinition.builder(name)
        .selectedField(
            new SimpleAttributeOperand(
                NodeIds.BaseEventType,
                new QualifiedName[] {new QualifiedName(0, browseName)},
                AttributeId.Value.uid(),
                null))
        .dataType(dataType)
        .build();
  }
}
