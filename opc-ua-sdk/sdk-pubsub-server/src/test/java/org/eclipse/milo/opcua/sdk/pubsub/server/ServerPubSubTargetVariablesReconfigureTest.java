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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldSelector;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariableConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariablesConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
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
 * Bindings re-derivation on reconfigure: the automatic TargetVariables writers follow readers
 * added, restarted, and removed through {@code ServerPubSub.runtime()} — closing the old "runtime()
 * reconfigures don't rebind" limitation. An in-process standalone publisher drives the reader over
 * unicast loopback UDP, and the Table 80 writes are asserted as address-space writes on the
 * embedded test server's nodes.
 *
 * <p>Network safety: connections use unicast 127.0.0.1 with ephemeral ports and an explicit
 * loopback {@code discoveryAddress}, so the engine's discovery channels never touch the default
 * multicast group 224.0.2.14:4840.
 */
class ServerPubSubTargetVariablesReconfigureTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final String CONNECTION = "tvr-conn";
  private static final String READER_GROUP = "rgrp";
  private static final String READER = "reader";
  private static final String DATA_SET = "tvr-ds";

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4781));
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
                  UadpDataSetMessageContentMask.Field.MajorVersion,
                  UadpDataSetMessageContentMask.Field.MinorVersion,
                  UadpDataSetMessageContentMask.Field.SequenceNumber))
          .build();

  private static final DataSetFieldContentMask FIELD_MASK =
      DataSetFieldContentMask.of(DataSetFieldContentMask.Field.StatusCode);

  private static TestPubSubServer testServer;
  private static NodeId target1NodeId;
  private static NodeId target2NodeId;

  private final List<ServerPubSub> attached = new CopyOnWriteArrayList<>();
  private final List<PubSubService> services = new CopyOnWriteArrayList<>();

  @BeforeAll
  static void startServer() {
    testServer = TestPubSubServer.create();

    target1NodeId =
        testServer.addVariable("TVR_Target1", NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));
    target2NodeId =
        testServer.addVariable("TVR_Target2", NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));
  }

  @AfterAll
  static void stopServer() {
    testServer.close();
  }

  @AfterEach
  void tearDown() {
    attached.forEach(ServerPubSub::close);
    attached.clear();

    for (PubSubService service : services) {
      try {
        service.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      } catch (InterruptedException | ExecutionException | TimeoutException e) {
        // best effort cleanup; failures are reported by the tests themselves
      }
    }
    services.clear();
  }

  @Test
  void writersFollowReadersAddedRemappedAndRemovedThroughRuntime() throws Exception {
    int dataPort = freeUdpPort();
    int publisherDiscoveryPort = freeUdpPort();
    int readerDiscoveryPort = freeUdpPort();

    var values = new AtomicReference<>(Map.of("v", new DataValue(Variant.ofDouble(3.5))));

    PubSubService publisher = track(publisher(dataPort, publisherDiscoveryPort, values));
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // attach with NO reader at all; nothing writes yet
    ServerPubSub serverPubSub =
        track(
            ServerPubSub.attach(
                testServer.getServer(),
                serverConfig(dataPort, readerDiscoveryPort, ReaderShape.NONE)));
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // 1. ADD: a reader with TargetVariables added via runtime() gets a writer derived for it
    serverPubSub
        .runtime()
        .update(current -> serverConfig(dataPort, readerDiscoveryPort, ReaderShape.TARGET1));

    awaitValue(
        target1NodeId,
        value -> Double.valueOf(3.5).equals(value.value().value()),
        "TVR_Target1 == 3.5");

    // 2. RESTART with remapped targets: the old writer is detached, the new one writes the
    //    new target, and the old target goes quiescent
    serverPubSub
        .runtime()
        .update(current -> serverConfig(dataPort, readerDiscoveryPort, ReaderShape.TARGET2));

    values.set(Map.of("v", new DataValue(Variant.ofDouble(4.5))));

    awaitValue(
        target2NodeId,
        value -> Double.valueOf(4.5).equals(value.value().value()),
        "TVR_Target2 == 4.5");

    // the detached writer no longer writes the old target: plant a sentinel, publish more,
    // and verify the sentinel survives a quiet window
    testServer.setValue(target1NodeId, new DataValue(Variant.ofDouble(-1.0)));
    values.set(Map.of("v", new DataValue(Variant.ofDouble(5.5))));
    awaitValue(
        target2NodeId,
        value -> Double.valueOf(5.5).equals(value.value().value()),
        "TVR_Target2 == 5.5");
    Thread.sleep(400);
    assertEquals(-1.0, testServer.getValue(target1NodeId).value().value());

    // 3. REMOVE: dropping the reader detaches and deactivates its writer; writes stop
    serverPubSub
        .runtime()
        .update(current -> serverConfig(dataPort, readerDiscoveryPort, ReaderShape.NONE));

    testServer.setValue(target2NodeId, new DataValue(Variant.ofDouble(-2.0)));
    values.set(Map.of("v", new DataValue(Variant.ofDouble(6.5))));
    Thread.sleep(500);
    assertEquals(-2.0, testServer.getValue(target2NodeId).value().value());
  }

  // region fixtures

  private enum ReaderShape {
    NONE,
    TARGET1,
    TARGET2
  }

  /**
   * The server-side config: one connection, reader group present, reader shaped by {@code shape}.
   */
  private static PubSubConfig serverConfig(int dataPort, int discoveryPort, ReaderShape shape) {
    ReaderGroupConfig.Builder readerGroup = ReaderGroupConfig.builder(READER_GROUP);

    if (shape != ReaderShape.NONE) {
      NodeId targetNodeId = shape == ReaderShape.TARGET1 ? target1NodeId : target2NodeId;

      TargetVariablesConfig targetVariables =
          TargetVariablesConfig.builder()
              .map(
                  FieldSelector.byName("v"),
                  TargetVariableConfig.builder().target(nodeAddress(targetNodeId)).build())
              .build();

      readerGroup.dataSetReader(
          DataSetReaderConfig.builder(READER)
              .publisherId(PUBLISHER_ID)
              .writerGroupId(GROUP_ID)
              .dataSetWriterId(WRITER_ID)
              .dataSetMetaData(metaData())
              .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
              .subscribedDataSet(targetVariables)
              .build());
    }

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .address(UdpDatagramAddress.unicast("127.0.0.1", dataPort))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", discoveryPort))
                .readerGroup(readerGroup.build())
                .build())
        .build();
  }

  /** A standalone publisher service whose writer matches the reader's filters. */
  private static PubSubService publisher(
      int dataPort, int discoveryPort, AtomicReference<Map<String, DataValue>> values) {

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder(DATA_SET)
            .field(
                FieldDefinition.builder("v")
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(fieldId())
                    .build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.udp("tvr-pub")
                    .publisherId(PUBLISHER_ID)
                    .address(UdpDatagramAddress.unicast("127.0.0.1", dataPort))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", discoveryPort))
                    .writerGroup(
                        WriterGroupConfig.builder("grp")
                            .writerGroupId(GROUP_ID)
                            .publishingInterval(Duration.ofMillis(75))
                            .messageSettings(GROUP_SETTINGS)
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(WRITER_ID)
                                    .fieldContentMask(FIELD_MASK)
                                    .settings(WRITER_SETTINGS)
                                    .build())
                            .build())
                    .build())
            .build();

    PublishedDataSetSource source =
        context -> {
          DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
          DataValue value = values.get().get("v");
          if (value != null) {
            builder.field("v", value);
          }
          return builder.build();
        };

    return PubSubService.create(
        config, PubSubBindings.builder().source(dataSet.ref(), source).build());
  }

  private static DataSetMetaDataConfig metaData() {
    return DataSetMetaDataConfig.builder(DATA_SET).field("v", NodeIds.Double, fieldId()).build();
  }

  private static UUID fieldId() {
    return new UUID(0L, 0xD1L);
  }

  private static NodeFieldAddress nodeAddress(NodeId nodeId) {
    return NodeFieldAddress.of(
        nodeId, AttributeId.Value, testServer.getServer().getNamespaceTable());
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

  /** Poll the value of {@code nodeId} until it matches {@code predicate}. */
  private static void awaitValue(NodeId nodeId, Predicate<DataValue> predicate, String description)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    DataValue lastValue = null;
    while (true) {
      lastValue = testServer.getValue(nodeId);
      if (predicate.test(lastValue)) {
        return;
      }
      if (System.nanoTime() >= deadline) {
        fail("timed out waiting for " + description + "; last value: " + lastValue);
      }
      Thread.sleep(25);
    }
  }

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
