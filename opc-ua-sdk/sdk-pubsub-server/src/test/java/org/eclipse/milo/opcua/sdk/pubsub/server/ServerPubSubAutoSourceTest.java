/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Automatic address-space sources per pinned decision S3, end-to-end over unicast loopback UDP: a
 * {@link ServerPubSub} publisher whose datasets are backed by live server nodes, observed by a
 * standalone subscriber service.
 *
 * <p>Network safety: every connection uses unicast 127.0.0.1 with ephemeral ports and an explicit
 * loopback {@code discoveryAddress}, so the engine's discovery channels never touch the default
 * multicast group 224.0.2.14:4840.
 */
class ServerPubSubAutoSourceTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4721));
  private static final UShort GROUP_ID = ushort(1);
  private static final UShort WRITER_ID = ushort(1);

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

  /** DataValue field encoding so field StatusCodes and source timestamps travel on the wire. */
  private static final DataSetFieldContentMask FIELD_MASK =
      DataSetFieldContentMask.of(
          DataSetFieldContentMask.Field.StatusCode, DataSetFieldContentMask.Field.SourceTimestamp);

  private static TestPubSubServer testServer;

  private final List<ServerPubSub> attached = new CopyOnWriteArrayList<>();
  private final List<PubSubService> services = new CopyOnWriteArrayList<>();

  @BeforeAll
  static void startServer() {
    testServer = TestPubSubServer.create();
  }

  @AfterAll
  static void stopServer() {
    testServer.close();
  }

  @AfterEach
  void tearDown() throws InterruptedException {
    for (ServerPubSub serverPubSub : attached) {
      serverPubSub.close();
    }
    attached.clear();

    for (PubSubService service : services) {
      try {
        service.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      } catch (ExecutionException | TimeoutException e) {
        // best effort cleanup; failures are reported by the tests themselves
      }
    }
    services.clear();
  }

  @Test
  void allNodeFieldAddressDataSetPublishesLiveNodeValues() throws Exception {
    int port = freeUdpPort();

    NodeId temperatureNodeId =
        testServer.addVariable(
            "AS1_Temperature", NodeIds.Double, new DataValue(Variant.ofDouble(21.5)));
    NodeId statusNodeId =
        testServer.addVariable(
            "AS1_Status", NodeIds.String, new DataValue(Variant.ofString("running")));

    var temperatureFieldId = new UUID(0L, 0xA1L);
    var statusFieldId = new UUID(0L, 0xA2L);

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("as-ds1")
            .field(
                FieldDefinition.builder("temperature")
                    .source(nodeAddress(temperatureNodeId))
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(temperatureFieldId)
                    .build())
            .field(
                FieldDefinition.builder("status")
                    .source(nodeAddress(statusNodeId))
                    .dataType(NodeIds.String)
                    .dataSetFieldId(statusFieldId)
                    .build())
            .build();

    ServerPubSub serverPubSub =
        track(ServerPubSub.attach(testServer.getServer(), publisherConfig(dataSet, port, true)));

    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("as-ds1")
            .field("temperature", NodeIds.Double, temperatureFieldId)
            .field("status", NodeIds.String, statusFieldId)
            .build();

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    PubSubService subscriber = track(subscriberService(port, metaData, events));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // the publish cycle reads the live node values
    DataSetReceivedEvent event = awaitEvent(events, e -> true);
    assertEquals(21.5, event.fieldsByName().get("temperature").value().value());
    assertEquals(StatusCode.GOOD, event.fieldsByName().get("temperature").statusCode());
    assertEquals("running", event.fieldsByName().get("status").value().value());

    // a node value change is observed on the wire: snapshots are pulled fresh each cycle
    testServer.setValue(temperatureNodeId, new DataValue(Variant.ofDouble(23.5)));
    awaitEvent(
        events,
        e -> Double.valueOf(23.5).equals(e.fieldsByName().get("temperature").value().value()));
  }

  @Test
  void missingNodePublishesBadNodeIdUnknownForThatField() throws Exception {
    int port = freeUdpPort();

    NodeId realNodeId =
        testServer.addVariable("AS2_Real", NodeIds.Int32, new DataValue(Variant.ofInt32(7)));
    // a node id in the (resolvable) test namespace, but no node exists under it
    NodeId missingNodeId = testServer.nodeId("AS2_Missing");

    var realFieldId = new UUID(0L, 0xB1L);
    var missingFieldId = new UUID(0L, 0xB2L);

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("as-ds2")
            .field(
                FieldDefinition.builder("real")
                    .source(nodeAddress(realNodeId))
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(realFieldId)
                    .build())
            .field(
                FieldDefinition.builder("missing")
                    .source(nodeAddress(missingNodeId))
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(missingFieldId)
                    .build())
            .build();

    ServerPubSub serverPubSub =
        track(ServerPubSub.attach(testServer.getServer(), publisherConfig(dataSet, port, true)));

    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("as-ds2")
            .field("real", NodeIds.Int32, realFieldId)
            .field("missing", NodeIds.Int32, missingFieldId)
            .build();

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    PubSubService subscriber = track(subscriberService(port, metaData, events));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    DataSetReceivedEvent event = awaitEvent(events, e -> true);

    assertEquals(7, event.fieldsByName().get("real").value().value());
    assertEquals(StatusCode.GOOD, event.fieldsByName().get("real").statusCode());

    assertEquals(
        StatusCodes.Bad_NodeIdUnknown, event.fieldsByName().get("missing").statusCode().value());
  }

  @Test
  void mixedDataSetIsNotAutoBoundAndPublishesBadInternalError() throws Exception {
    int port = freeUdpPort();

    NodeId nodeBackedNodeId =
        testServer.addVariable("AS3_Node", NodeIds.Int32, new DataValue(Variant.ofInt32(1)));

    var nodeFieldId = new UUID(0L, 0xC1L);
    var keyFieldId = new UUID(0L, 0xC2L);

    // one NodeFieldAddress field and one (default) KeyFieldAddress field: NOT auto-bound
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("as-ds3")
            .field(
                FieldDefinition.builder("node")
                    .source(nodeAddress(nodeBackedNodeId))
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(nodeFieldId)
                    .build())
            .field(
                FieldDefinition.builder("key")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(keyFieldId)
                    .build())
            .build();

    // the writer starts disabled so startup does not validate its (unbound) source
    ServerPubSub serverPubSub =
        track(ServerPubSub.attach(testServer.getServer(), publisherConfig(dataSet, port, false)));

    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("as-ds3")
            .field("node", NodeIds.Int32, nodeFieldId)
            .field("key", NodeIds.Int32, keyFieldId)
            .build();

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    PubSubService subscriber = track(subscriberService(port, metaData, events));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    PubSubHandle writer =
        serverPubSub
            .runtime()
            .components()
            .dataSetWriter("pub-conn", "grp", "writer")
            .orElseThrow();
    serverPubSub.runtime().enable(writer);

    // the unbound writer publishes Bad_InternalError for every field, including the node-backed one
    DataSetReceivedEvent event = awaitEvent(events, e -> true);
    assertEquals(
        StatusCodes.Bad_InternalError, event.fieldsByName().get("node").statusCode().value());
    assertEquals(
        StatusCodes.Bad_InternalError, event.fieldsByName().get("key").statusCode().value());
  }

  @Test
  void userSuppliedSourceWinsOverAutoBinding() throws Exception {
    int port = freeUdpPort();

    // the node value is 1.5; the user-supplied source publishes 999.25
    NodeId nodeId =
        testServer.addVariable("AS4_Node", NodeIds.Double, new DataValue(Variant.ofDouble(1.5)));

    var valueFieldId = new UUID(0L, 0xD1L);

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("as-ds4")
            .field(
                FieldDefinition.builder("value")
                    .source(nodeAddress(nodeId))
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(valueFieldId)
                    .build())
            .build();

    PubSubBindings userBindings =
        PubSubBindings.builder()
            .source(
                dataSet.ref(),
                context -> {
                  DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
                  builder.field("value", new DataValue(Variant.ofDouble(999.25)));
                  return builder.build();
                })
            .build();

    ServerPubSubOptions options = ServerPubSubOptions.builder().bindings(userBindings).build();

    ServerPubSub serverPubSub =
        track(
            ServerPubSub.attach(
                testServer.getServer(), publisherConfig(dataSet, port, true), options));

    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("as-ds4")
            .field("value", NodeIds.Double, valueFieldId)
            .build();

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    PubSubService subscriber = track(subscriberService(port, metaData, events));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // the auto-binding was skipped: the user source's value is on the wire, not the node's
    DataSetReceivedEvent event = awaitEvent(events, e -> true);
    assertEquals(999.25, event.fieldsByName().get("value").value().value());
  }

  // region fixtures

  private static NodeFieldAddress nodeAddress(NodeId nodeId) {
    return NodeFieldAddress.of(
        nodeId, AttributeId.Value, testServer.getServer().getNamespaceTable());
  }

  /**
   * Publisher config: one UDP connection "pub-conn" sending to 127.0.0.1:{@code port}, with an
   * explicit loopback discoveryAddress, one writer group "grp" at 75 ms, and one writer "writer" on
   * {@code dataSet} using DataValue field encoding.
   */
  private static PubSubConfig publisherConfig(
      PublishedDataSetConfig dataSet, int port, boolean writerEnabled) throws SocketException {

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp("pub-conn")
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .writerGroup(
                    WriterGroupConfig.builder("grp")
                        .writerGroupId(GROUP_ID)
                        .publishingInterval(Duration.ofMillis(75))
                        .messageSettings(GROUP_SETTINGS)
                        .dataSetWriter(
                            DataSetWriterConfig.builder("writer")
                                .enabled(writerEnabled)
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(WRITER_ID)
                                .fieldContentMask(FIELD_MASK)
                                .settings(WRITER_SETTINGS)
                                .build())
                        .build())
                .build())
        .build();
  }

  /**
   * A standalone subscriber service bound to 127.0.0.1:{@code port} with one REQUIRE_CONFIGURED
   * reader matching the publisher's writer, delivering events to {@code events}.
   */
  private static PubSubService subscriberService(
      int port, DataSetMetaDataConfig metaData, BlockingQueue<DataSetReceivedEvent> events)
      throws SocketException {

    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp("sub-conn")
                    .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .readerGroup(
                        ReaderGroupConfig.builder("rgrp")
                            .dataSetReader(
                                DataSetReaderConfig.builder("reader")
                                    .publisherId(PUBLISHER_ID)
                                    .writerGroupId(GROUP_ID)
                                    .dataSetWriterId(WRITER_ID)
                                    .dataSetMetaData(metaData)
                                    .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubBindings bindings =
        PubSubBindings.builder()
            .listener(new DataSetReaderRef("sub-conn", "rgrp", "reader"), events::add)
            .build();

    return PubSubService.create(config, bindings);
  }

  // endregion

  // region helpers

  private ServerPubSub track(ServerPubSub serverPubSub) {
    attached.add(serverPubSub);
    return serverPubSub;
  }

  private PubSubService track(PubSubService service) {
    services.add(service);
    return service;
  }

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  /** Wait for an event matching {@code predicate}, discarding non-matching events. */
  private static DataSetReceivedEvent awaitEvent(
      BlockingQueue<DataSetReceivedEvent> events, Predicate<DataSetReceivedEvent> predicate)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (true) {
      long remaining = deadline - System.nanoTime();
      if (remaining <= 0) {
        fail("timed out waiting for a matching DataSetReceivedEvent");
      }
      DataSetReceivedEvent event =
          events.poll(Math.min(remaining, 100_000_000L), TimeUnit.NANOSECONDS);
      if (event != null && predicate.test(event)) {
        return event;
      }
    }
  }

  // endregion
}
