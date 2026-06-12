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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldSelector;
import org.eclipse.milo.opcua.sdk.pubsub.config.KeyFieldAddress;
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
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.OverrideValueHandling;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * TargetVariables behavior per pinned decisions S5/S6, end-to-end: an in-process standalone
 * publisher service drives a {@link ServerPubSub}-attached DataSetReader over unicast loopback UDP,
 * and the Part 14 §6.2.11.1 Table 80 message-to-target mapping is asserted as address-space writes
 * on the embedded test server's nodes.
 *
 * <p>Network safety: every connection uses unicast 127.0.0.1 with ephemeral ports and an explicit
 * loopback {@code discoveryAddress}, so the engine's discovery channels never touch the default
 * multicast group 224.0.2.14:4840.
 */
class ServerPubSubTargetVariablesIntegrationTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4722));
  private static final UShort GROUP_ID = ushort(1);
  private static final UShort WRITER_ID = ushort(1);

  private static final String READER_PATH = "tv-conn/rgrp/reader";

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
  void goodFieldsWriteThroughWithStatusTimestampsAndIndexRanges() throws Exception {
    int port = freeUdpPort();

    NodeId tempNodeId =
        testServer.addVariable("TV1_Temp", NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));
    NodeId sliceNodeId =
        testServer.addArrayVariable(
            "TV1_Slice", NodeIds.Int32, new DataValue(Variant.of(new Integer[] {0, 0})));
    NodeId spliceNodeId =
        testServer.addArrayVariable(
            "TV1_Splice", NodeIds.Int32, new DataValue(Variant.of(new Integer[] {1, 2, 3})));

    DateTime sourceTime = DateTime.now();

    var values =
        new AtomicReference<Map<String, DataValue>>(
            Map.of(
                "temperature",
                    new DataValue(Variant.ofDouble(21.5), StatusCode.GOOD, sourceTime, null),
                "arr", new DataValue(Variant.of(new Integer[] {10, 20, 30, 40})),
                "one", new DataValue(Variant.of(new Integer[] {77}))));

    TargetVariablesConfig targetVariables =
        TargetVariablesConfig.builder()
            .map(
                FieldSelector.byName("temperature"),
                TargetVariableConfig.builder().target(target(tempNodeId)).build())
            .map(
                FieldSelector.byName("arr"),
                TargetVariableConfig.builder()
                    .target(target(sliceNodeId))
                    .receiverIndexRange("1:2")
                    .build())
            .map(
                FieldSelector.byName("one"),
                TargetVariableConfig.builder()
                    .target(target(spliceNodeId))
                    .writeIndexRange("1")
                    .build())
            .build();

    startPublisherAndReader(
        port,
        dataSet(
            "tv-ds1", "temperature", NodeIds.Double, "arr", NodeIds.Int32, "one", NodeIds.Int32),
        values,
        metaData(
            "tv-ds1", "temperature", NodeIds.Double, "arr", NodeIds.Int32, "one", NodeIds.Int32),
        targetVariables);

    // Operational + Good: value, status, and source timestamp pass through
    awaitValue(
        tempNodeId,
        value -> Double.valueOf(21.5).equals(value.value().value()),
        "TV1_Temp == 21.5");
    DataValue written = testServer.getValue(tempNodeId);
    assertEquals(StatusCode.GOOD, written.statusCode());
    assertEquals(sourceTime, written.sourceTime());

    // receiverIndexRange "1:2" slices {10,20,30,40} down to {20,30}
    awaitValue(
        sliceNodeId,
        value -> value.value().value() instanceof Integer[] array && array.length == 2,
        "TV1_Slice sliced");
    assertArrayEquals(
        new Integer[] {20, 30}, (Integer[]) testServer.getValue(sliceNodeId).value().value());

    // writeIndexRange "1" splices the received {77} into element 1 of {1,2,3}
    awaitValue(
        spliceNodeId,
        value ->
            value.value().value() instanceof Integer[] array
                && array.length == 3
                && Integer.valueOf(77).equals(array[1]),
        "TV1_Splice spliced");
    assertArrayEquals(
        new Integer[] {1, 77, 3}, (Integer[]) testServer.getValue(spliceNodeId).value().value());
  }

  @Test
  void operationalBadFieldsApplyOverrideValueAndDisabledHandling() throws Exception {
    int port = freeUdpPort();

    NodeId overrideNodeId =
        testServer.addVariable(
            "TV2_Override", NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));
    NodeId disabledNodeId =
        testServer.addVariable(
            "TV2_Disabled", NodeIds.Double, new DataValue(Variant.ofDouble(5.5)));

    var badValue = new DataValue(Variant.ofNull(), new StatusCode(StatusCodes.Bad_DeviceFailure));

    var values =
        new AtomicReference<Map<String, DataValue>>(Map.of("ov", badValue, "dis", badValue));

    TargetVariablesConfig targetVariables =
        TargetVariablesConfig.builder()
            .map(
                FieldSelector.byName("ov"),
                TargetVariableConfig.builder()
                    .target(target(overrideNodeId))
                    .overrideHandling(OverrideValueHandling.OverrideValue)
                    .overrideValue(Variant.ofDouble(99.5))
                    .build())
            .map(
                FieldSelector.byName("dis"),
                TargetVariableConfig.builder()
                    .target(target(disabledNodeId))
                    .overrideHandling(OverrideValueHandling.Disabled)
                    .build())
            .build();

    startPublisherAndReader(
        port,
        dataSet("tv-ds2", "ov", NodeIds.Double, "dis", NodeIds.Double),
        values,
        metaData("tv-ds2", "ov", NodeIds.Double, "dis", NodeIds.Double),
        targetVariables);

    // Bad + OverrideValue: (override value, Good_LocalOverride)
    awaitValue(
        overrideNodeId,
        value -> Double.valueOf(99.5).equals(value.value().value()),
        "TV2_Override == 99.5");
    assertEquals(
        StatusCodes.Good_LocalOverride, testServer.getValue(overrideNodeId).statusCode().value());

    // Bad + Disabled: (null, received Bad status)
    awaitValue(
        disabledNodeId,
        value -> value.statusCode().value() == StatusCodes.Bad_DeviceFailure,
        "TV2_Disabled has the received Bad status");
    assertNull(testServer.getValue(disabledNodeId).value().value());
  }

  @Test
  void operationalBadFieldLastUsableValueUsesLastGoodValueThenNull() throws Exception {
    int port = freeUdpPort();

    NodeId lastNodeId =
        testServer.addVariable("TV3_Last", NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));
    NodeId neverNodeId =
        testServer.addVariable("TV3_Never", NodeIds.Double, new DataValue(Variant.ofDouble(1.25)));

    var badValue = new DataValue(Variant.ofNull(), new StatusCode(StatusCodes.Bad_DeviceFailure));

    var values =
        new AtomicReference<Map<String, DataValue>>(
            Map.of("lu", new DataValue(Variant.ofDouble(5.5)), "never", badValue));

    TargetVariablesConfig targetVariables =
        TargetVariablesConfig.builder()
            .map(
                FieldSelector.byName("lu"),
                TargetVariableConfig.builder()
                    .target(target(lastNodeId))
                    .overrideHandling(OverrideValueHandling.LastUsableValue)
                    .build())
            .map(
                FieldSelector.byName("never"),
                TargetVariableConfig.builder()
                    .target(target(neverNodeId))
                    .overrideHandling(OverrideValueHandling.LastUsableValue)
                    .build())
            .build();

    startPublisherAndReader(
        port,
        dataSet("tv-ds3", "lu", NodeIds.Double, "never", NodeIds.Double),
        values,
        metaData("tv-ds3", "lu", NodeIds.Double, "never", NodeIds.Double),
        targetVariables);

    // a usable value flows through before any error
    awaitValue(
        lastNodeId, value -> Double.valueOf(5.5).equals(value.value().value()), "TV3_Last == 5.5");

    // no usable value was ever received for "never": (null, Uncertain_LastUsableValue) -- the
    // pinned deviation from Table 80 footnote (b): no DataType-default synthesis
    awaitValue(
        neverNodeId,
        value -> value.statusCode().value() == StatusCodes.Uncertain_LastUsableValue,
        "TV3_Never has Uncertain_LastUsableValue");
    assertNull(testServer.getValue(neverNodeId).value().value());

    // the field goes Bad: the last usable value is written with Uncertain_LastUsableValue
    values.set(Map.of("lu", badValue, "never", badValue));

    awaitValue(
        lastNodeId,
        value ->
            value.statusCode().value() == StatusCodes.Uncertain_LastUsableValue
                && Double.valueOf(5.5).equals(value.value().value()),
        "TV3_Last == (5.5, Uncertain_LastUsableValue)");
  }

  @Test
  void disablingTheReaderWritesStateChangeRowsExactlyOnce() throws Exception {
    int port = freeUdpPort();

    NodeId stateNodeId =
        testServer.addVariable("TV4_State", NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));
    NodeId overrideNodeId =
        testServer.addVariable(
            "TV4_Override", NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));

    var values =
        new AtomicReference<Map<String, DataValue>>(
            Map.of("sv", new DataValue(Variant.ofDouble(1.5))));

    TargetVariablesConfig targetVariables =
        TargetVariablesConfig.builder()
            .map(
                FieldSelector.byName("sv"),
                TargetVariableConfig.builder()
                    .target(target(stateNodeId))
                    .overrideHandling(OverrideValueHandling.Disabled)
                    .build())
            .map(
                FieldSelector.byName("sv"),
                TargetVariableConfig.builder()
                    .target(target(overrideNodeId))
                    .overrideHandling(OverrideValueHandling.OverrideValue)
                    .overrideValue(Variant.ofDouble(42.5))
                    .build())
            .build();

    Fixture fixture =
        startPublisherAndReader(
            port,
            dataSet("tv-ds4", "sv", NodeIds.Double),
            values,
            metaData("tv-ds4", "sv", NodeIds.Double),
            targetVariables);

    // message-driven writes are flowing
    awaitValue(
        stateNodeId,
        value -> Double.valueOf(1.5).equals(value.value().value()),
        "TV4_State == 1.5");

    // stop the publisher and drain in-flight deliveries before observing state-change writes
    fixture.publisher.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    services.remove(fixture.publisher);
    Thread.sleep(300);

    var stateWrites = new LinkedBlockingQueue<DataValue>();
    testServer
        .getVariableNode(stateNodeId)
        .addAttributeObserver(
            (node, attributeId, value) -> {
              if (attributeId == AttributeId.Value && value instanceof DataValue dataValue) {
                stateWrites.add(dataValue);
              }
            });

    var overrideWrites = new LinkedBlockingQueue<DataValue>();
    testServer
        .getVariableNode(overrideNodeId)
        .addAttributeObserver(
            (node, attributeId, value) -> {
              if (attributeId == AttributeId.Value && value instanceof DataValue dataValue) {
                overrideWrites.add(dataValue);
              }
            });

    PubSubHandle reader =
        fixture
            .serverPubSub
            .runtime()
            .components()
            .dataSetReader("tv-conn", "rgrp", "reader")
            .orElseThrow();
    fixture.serverPubSub.runtime().disable(reader);

    // Disabled-handling target: one write of (null, Bad_OutOfService)
    DataValue stateWrite = stateWrites.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    assertNotNull(stateWrite, "expected a state-change write to TV4_State");
    assertEquals(StatusCodes.Bad_OutOfService, stateWrite.statusCode().value());
    assertNull(stateWrite.value().value());

    // OverrideValue-handling target: one write of (override value, Good_LocalOverride)
    DataValue overrideWrite = overrideWrites.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    assertNotNull(overrideWrite, "expected a state-change write to TV4_Override");
    assertEquals(StatusCodes.Good_LocalOverride, overrideWrite.statusCode().value());
    assertEquals(42.5, overrideWrite.value().value());

    // exactly once: a quiet window produces no further writes
    Thread.sleep(500);
    assertNull(stateWrites.poll(), "expected exactly one state-change write to TV4_State");
    assertNull(overrideWrites.poll(), "expected exactly one state-change write to TV4_Override");
  }

  @Test
  void nonWritableTargetCountsWriteErrorsWhilePublishKeepsFlowing() throws Exception {
    int port = freeUdpPort();

    NodeId readOnlyNodeId =
        testServer.addReadOnlyVariable(
            "TV5_ReadOnly", NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));
    NodeId writableNodeId =
        testServer.addVariable(
            "TV5_Writable", NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));

    var values =
        new AtomicReference<Map<String, DataValue>>(
            Map.of("v", new DataValue(Variant.ofDouble(1.5))));

    TargetVariablesConfig targetVariables =
        TargetVariablesConfig.builder()
            .map(
                FieldSelector.byName("v"),
                TargetVariableConfig.builder().target(target(readOnlyNodeId)).build())
            .map(
                FieldSelector.byName("v"),
                TargetVariableConfig.builder().target(target(writableNodeId)).build())
            .build();

    Fixture fixture =
        startPublisherAndReader(
            port,
            dataSet("tv-ds5", "v", NodeIds.Double),
            values,
            metaData("tv-ds5", "v", NodeIds.Double),
            targetVariables);

    awaitValue(
        writableNodeId,
        value -> Double.valueOf(1.5).equals(value.value().value()),
        "TV5_Writable == 1.5");

    // the read-only target's failed writes are counted, keyed "<reader-path>/<targetNodeId>"
    String errorKey = READER_PATH + "/" + readOnlyNodeId.toParseableString();
    awaitTrue(
        () -> fixture.serverPubSub.targetWriteErrors().getOrDefault(errorKey, 0L) >= 1,
        "targetWriteErrors[" + errorKey + "] >= 1");

    // publish keeps flowing: a new published value still reaches the writable target
    values.set(Map.of("v", new DataValue(Variant.ofDouble(2.5))));
    awaitValue(
        writableNodeId,
        value -> Double.valueOf(2.5).equals(value.value().value()),
        "TV5_Writable == 2.5");

    // and the read-only target was never written
    assertEquals(0.0, testServer.getValue(readOnlyNodeId).value().value());
  }

  // region fixtures

  private record Fixture(PubSubService publisher, ServerPubSub serverPubSub) {}

  /**
   * Start a standalone publisher service on {@code port} publishing {@code dataSet} from {@code
   * values}, and a {@link ServerPubSub} whose single reader subscribes with {@code metaData} and
   * writes through {@code targetVariables}.
   */
  private Fixture startPublisherAndReader(
      int port,
      PublishedDataSetConfig dataSet,
      AtomicReference<Map<String, DataValue>> values,
      DataSetMetaDataConfig metaData,
      TargetVariablesConfig targetVariables)
      throws Exception {

    PubSubConfig publisherConfig =
        PubSubConfig.builder()
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
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(WRITER_ID)
                                    .fieldContentMask(FIELD_MASK)
                                    .settings(WRITER_SETTINGS)
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubService publisher =
        track(
            PubSubService.create(
                publisherConfig,
                PubSubBindings.builder().source(dataSet.ref(), mapSource(values)).build()));

    PubSubConfig readerConfig =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp("tv-conn")
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
                                    .subscribedDataSet(targetVariables)
                                    .build())
                            .build())
                    .build())
            .build();

    ServerPubSub serverPubSub = track(ServerPubSub.attach(testServer.getServer(), readerConfig));

    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    return new Fixture(publisher, serverPubSub);
  }

  private static NodeFieldAddress target(NodeId nodeId) {
    return NodeFieldAddress.of(
        nodeId, AttributeId.Value, testServer.getServer().getNamespaceTable());
  }

  /** A published dataset of key-addressed fields given as (name, dataType) pairs. */
  private static PublishedDataSetConfig dataSet(String name, Object... fields) {
    PublishedDataSetConfig.Builder builder = PublishedDataSetConfig.builder(name);
    for (int i = 0; i < fields.length; i += 2) {
      String fieldName = (String) fields[i];
      builder.field(
          FieldDefinition.builder(fieldName)
              .dataType((NodeId) fields[i + 1])
              .dataSetFieldId(fieldId(name, fieldName))
              .build());
    }
    return builder.build();
  }

  /** Subscriber metadata matching {@link #dataSet}: same names, types, and field ids. */
  private static DataSetMetaDataConfig metaData(String name, Object... fields) {
    DataSetMetaDataConfig.Builder builder = DataSetMetaDataConfig.builder(name);
    for (int i = 0; i < fields.length; i += 2) {
      String fieldName = (String) fields[i];
      builder.field(fieldName, (NodeId) fields[i + 1], fieldId(name, fieldName));
    }
    return builder.build();
  }

  /** A deterministic per-(dataset, field) UUID so dataset and metadata always agree. */
  private static UUID fieldId(String dataSetName, String fieldName) {
    return UUID.nameUUIDFromBytes((dataSetName + "/" + fieldName).getBytes());
  }

  /** A source that reads the current values of an AtomicReference-backed map by field key. */
  private static PublishedDataSetSource mapSource(AtomicReference<Map<String, DataValue>> values) {
    return context -> {
      DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
      Map<String, DataValue> currentValues = values.get();
      for (FieldDefinition field : context.fields()) {
        String key =
            field.getSource() instanceof KeyFieldAddress keyAddress
                ? keyAddress.key()
                : field.getName();
        DataValue value = currentValues.get(key);
        if (value != null) {
          builder.field(field.getName(), value);
        }
      }
      return builder.build();
    };
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

  /** Poll the value of {@code nodeId} until it matches {@code predicate}. */
  private static void awaitValue(NodeId nodeId, Predicate<DataValue> predicate, String description)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (true) {
      DataValue value = testServer.getValue(nodeId);
      if (predicate.test(value)) {
        return;
      }
      if (System.nanoTime() >= deadline) {
        fail("timed out waiting for " + description + "; last value: " + value);
      }
      Thread.sleep(25);
    }
  }

  /** Poll {@code condition} until it holds or the deadline expires. */
  private static void awaitTrue(java.util.function.BooleanSupplier condition, String description)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (!condition.getAsBoolean()) {
      if (System.nanoTime() >= deadline) {
        fail("timed out waiting for: " + description);
      }
      Thread.sleep(25);
    }
  }

  // endregion
}
