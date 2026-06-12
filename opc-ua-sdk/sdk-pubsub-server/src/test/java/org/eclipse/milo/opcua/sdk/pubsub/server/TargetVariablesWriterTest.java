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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetFieldValue;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldSelector;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigValidationException;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariableConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariablesConfig;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.OverrideValueHandling;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit-level coverage of the Part 14 §6.2.11.1 Table 80 message-to-target and state-change-to-
 * target mapping implemented by {@link TargetVariablesWriter}, driven with synthetic {@link
 * DataSetReceivedEvent}s against the embedded test server's address space.
 *
 * <p>{@code onDataSet} and {@code onStateChange} write synchronously on the calling thread, so the
 * outcome of each call can be asserted immediately. Rows that cannot be produced end-to-end in v1
 * (the engine only emits key frames, so a field can never be absent from a received DataSet) are
 * covered here with synthetic events.
 */
class TargetVariablesWriterTest {

  private static final String READER_PATH = "conn/grp/reader";

  private static TestPubSubServer testServer;

  private static final AtomicInteger nodeCounter = new AtomicInteger();

  private final ConcurrentMap<String, AtomicLong> counters = new ConcurrentHashMap<>();

  @BeforeAll
  static void startServer() {
    testServer = TestPubSubServer.create();
  }

  @AfterAll
  static void stopServer() {
    testServer.close();
  }

  @Test
  void goodFieldWritesThroughWithValueStatusAndTimestamps() {
    NodeId nodeId = scalarNode(NodeIds.Double, Variant.ofDouble(0.0));
    TargetVariablesWriter writer = writer(mapping("f", nodeId));

    var sourceTime = new DateTime(Instant.now().minusSeconds(5));
    var serverTime = new DateTime(Instant.now().minusSeconds(4));

    writer.onDataSet(
        event(
            field(
                "f",
                0,
                new DataValue(Variant.ofDouble(2.5), StatusCode.GOOD, sourceTime, serverTime))));

    // note: the received serverTime is also written through, but UaVariableNode re-stamps the
    // serverTime on every read, so only the source timestamp is observable here
    DataValue written = testServer.getValue(nodeId);
    assertEquals(2.5, written.value().value());
    assertEquals(StatusCode.GOOD, written.statusCode());
    assertEquals(sourceTime, written.sourceTime());
  }

  @Test
  void uncertainFieldWritesThroughWithReceivedStatus() {
    NodeId nodeId = scalarNode(NodeIds.Double, Variant.ofDouble(0.0));
    TargetVariablesWriter writer = writer(mapping("f", nodeId));

    var uncertain = new StatusCode(StatusCodes.Uncertain_SubNormal);

    writer.onDataSet(
        event(field("f", 0, new DataValue(Variant.ofDouble(3.5), uncertain, null, null))));

    DataValue written = testServer.getValue(nodeId);
    assertEquals(3.5, written.value().value());
    assertEquals(uncertain, written.statusCode());
  }

  @Test
  void badFieldWithOverrideValueWritesOverrideAndGoodLocalOverride() {
    NodeId nodeId = scalarNode(NodeIds.Double, Variant.ofDouble(0.0));

    TargetVariablesWriter writer =
        writer(
            TargetVariablesConfig.builder()
                .map(
                    FieldSelector.byName("f"),
                    TargetVariableConfig.builder()
                        .target(target(nodeId))
                        .overrideHandling(OverrideValueHandling.OverrideValue)
                        .overrideValue(Variant.ofDouble(99.5))
                        .build())
                .build());

    writer.onDataSet(event(badField("f", 0, StatusCodes.Bad_DeviceFailure)));

    DataValue written = testServer.getValue(nodeId);
    assertEquals(99.5, written.value().value());
    assertEquals(StatusCodes.Good_LocalOverride, written.statusCode().value());
  }

  @Test
  void badFieldWithLastUsableValueWritesLastGoodValue() {
    NodeId nodeId = scalarNode(NodeIds.Double, Variant.ofDouble(0.0));

    TargetVariablesWriter writer =
        writer(
            TargetVariablesConfig.builder()
                .map(
                    FieldSelector.byName("f"),
                    TargetVariableConfig.builder()
                        .target(target(nodeId))
                        .overrideHandling(OverrideValueHandling.LastUsableValue)
                        .build())
                .build());

    writer.onDataSet(event(field("f", 0, new DataValue(Variant.ofDouble(1.5)))));
    writer.onDataSet(event(badField("f", 0, StatusCodes.Bad_DeviceFailure)));

    DataValue written = testServer.getValue(nodeId);
    assertEquals(1.5, written.value().value());
    assertEquals(StatusCodes.Uncertain_LastUsableValue, written.statusCode().value());
  }

  @Test
  void badFieldWithLastUsableValueAndNoPriorValueWritesNull() {
    // pinned deviation from Table 80 footnote (b): no DataType-default synthesis in v1
    NodeId nodeId = scalarNode(NodeIds.Double, Variant.ofDouble(7.5));

    TargetVariablesWriter writer =
        writer(
            TargetVariablesConfig.builder()
                .map(
                    FieldSelector.byName("f"),
                    TargetVariableConfig.builder()
                        .target(target(nodeId))
                        .overrideHandling(OverrideValueHandling.LastUsableValue)
                        .build())
                .build());

    writer.onDataSet(event(badField("f", 0, StatusCodes.Bad_DeviceFailure)));

    DataValue written = testServer.getValue(nodeId);
    assertNull(written.value().value());
    assertEquals(StatusCodes.Uncertain_LastUsableValue, written.statusCode().value());
  }

  @Test
  void badFieldWithDisabledHandlingWritesNullAndReceivedStatusAndTimestamps() {
    NodeId nodeId = scalarNode(NodeIds.Double, Variant.ofDouble(7.5));
    TargetVariablesWriter writer = writer(mapping("f", nodeId)); // default handling is Disabled

    var sourceTime = new DateTime(Instant.now().minusSeconds(3));
    var received =
        new DataValue(
            Variant.ofDouble(1.0), new StatusCode(StatusCodes.Bad_DeviceFailure), sourceTime, null);

    writer.onDataSet(event(field("f", 0, received)));

    DataValue written = testServer.getValue(nodeId);
    assertNull(written.value().value(), "Disabled handling writes a null value");
    assertEquals(StatusCodes.Bad_DeviceFailure, written.statusCode().value());
    assertEquals(sourceTime, written.sourceTime());
  }

  @Test
  void receiverIndexRangeSlicesTheReceivedValue() {
    NodeId nodeId = arrayNode(NodeIds.Int32, Variant.of(new Integer[] {0, 0}));

    TargetVariablesWriter writer =
        writer(
            TargetVariablesConfig.builder()
                .map(
                    FieldSelector.byName("f"),
                    TargetVariableConfig.builder()
                        .target(target(nodeId))
                        .receiverIndexRange("1:2")
                        .build())
                .build());

    writer.onDataSet(
        event(field("f", 0, new DataValue(Variant.of(new Integer[] {10, 20, 30, 40})))));

    assertArrayEquals(
        new Integer[] {20, 30}, (Integer[]) testServer.getValue(nodeId).value().value());
  }

  @Test
  void receiverIndexRangeFailureCountsAnErrorAndSkipsTheWrite() {
    NodeId nodeId = arrayNode(NodeIds.Int32, Variant.of(new Integer[] {0, 0}));

    TargetVariablesWriter writer =
        writer(
            TargetVariablesConfig.builder()
                .map(
                    FieldSelector.byName("f"),
                    TargetVariableConfig.builder()
                        .target(target(nodeId))
                        .receiverIndexRange("5:6")
                        .build())
                .build());

    // the received array is shorter than the range: the slice fails, the write is skipped
    writer.onDataSet(event(field("f", 0, new DataValue(Variant.of(new Integer[] {1, 2})))));

    assertArrayEquals(
        new Integer[] {0, 0},
        (Integer[]) testServer.getValue(nodeId).value().value(),
        "the target must not be written when the slice fails");
    assertEquals(1L, counters.get(errorKey(nodeId)).get());
  }

  @Test
  void writeIndexRangeIsPassedThroughToTheWrite() {
    NodeId nodeId = arrayNode(NodeIds.Int32, Variant.of(new Integer[] {1, 2, 3}));

    TargetVariablesWriter writer =
        writer(
            TargetVariablesConfig.builder()
                .map(
                    FieldSelector.byName("f"),
                    TargetVariableConfig.builder()
                        .target(target(nodeId))
                        .writeIndexRange("1")
                        .build())
                .build());

    writer.onDataSet(event(field("f", 0, new DataValue(Variant.of(new Integer[] {77})))));

    assertArrayEquals(
        new Integer[] {1, 77, 3}, (Integer[]) testServer.getValue(nodeId).value().value());
  }

  @Test
  void fieldAbsentFromTheDataSetProducesNoWrite() {
    NodeId nodeId = scalarNode(NodeIds.Double, Variant.ofDouble(7.5));
    TargetVariablesWriter writer = writer(mapping("f", nodeId));

    var writeCount = new AtomicInteger();
    testServer
        .getVariableNode(nodeId)
        .addAttributeObserver(
            (node, attributeId, value) -> {
              if (attributeId == AttributeId.Value) {
                writeCount.incrementAndGet();
              }
            });

    // the event carries only an unrelated field, e.g. a delta frame without the mapped field
    writer.onDataSet(event(field("other", 0, new DataValue(Variant.ofDouble(1.0)))));

    assertEquals(0, writeCount.get());
    assertEquals(7.5, testServer.getValue(nodeId).value().value());
  }

  @Test
  void selectorsMatchByIdAndByIndex() {
    NodeId byIdNodeId = scalarNode(NodeIds.Double, Variant.ofDouble(0.0));
    NodeId byIndexNodeId = scalarNode(NodeIds.Double, Variant.ofDouble(0.0));

    var fieldId = new UUID(0L, 0xE1L);

    TargetVariablesWriter writer =
        writer(
            TargetVariablesConfig.builder()
                .map(
                    FieldSelector.byId(fieldId),
                    TargetVariableConfig.builder().target(target(byIdNodeId)).build())
                .map(
                    FieldSelector.byIndex(1),
                    TargetVariableConfig.builder().target(target(byIndexNodeId)).build())
                .build());

    writer.onDataSet(
        event(
            new DataSetFieldValue(fieldId, "x", 0, new DataValue(Variant.ofDouble(1.5))),
            new DataSetFieldValue(
                new UUID(0L, 0xE2L), "y", 1, new DataValue(Variant.ofDouble(2.5)))));

    assertEquals(1.5, testServer.getValue(byIdNodeId).value().value());
    assertEquals(2.5, testServer.getValue(byIndexNodeId).value().value());
  }

  @Test
  void nonWritableTargetCountsAWriteErrorPerFailedWrite() {
    NodeId nodeId =
        testServer.addReadOnlyVariable(
            nextNodeName(), NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));

    TargetVariablesWriter writer = writer(mapping("f", nodeId));

    writer.onDataSet(event(field("f", 0, new DataValue(Variant.ofDouble(1.5)))));
    assertEquals(1L, counters.get(errorKey(nodeId)).get());

    writer.onDataSet(event(field("f", 0, new DataValue(Variant.ofDouble(2.5)))));
    assertEquals(2L, counters.get(errorKey(nodeId)).get());

    assertEquals(0.0, testServer.getValue(nodeId).value().value());
  }

  @Test
  void stateChangeIntoDisabledWritesTable80StateRows() {
    NodeId disabledNodeId = scalarNode(NodeIds.Double, Variant.ofDouble(1.0));
    NodeId overrideNodeId = scalarNode(NodeIds.Double, Variant.ofDouble(1.0));
    NodeId lastUsableNodeId = scalarNode(NodeIds.Double, Variant.ofDouble(1.0));

    TargetVariablesWriter writer =
        writer(
            TargetVariablesConfig.builder()
                .map(
                    FieldSelector.byName("f"),
                    TargetVariableConfig.builder()
                        .target(target(disabledNodeId))
                        .overrideHandling(OverrideValueHandling.Disabled)
                        .build())
                .map(
                    FieldSelector.byName("f"),
                    TargetVariableConfig.builder()
                        .target(target(overrideNodeId))
                        .overrideHandling(OverrideValueHandling.OverrideValue)
                        .overrideValue(Variant.ofDouble(99.5))
                        .build())
                .map(
                    FieldSelector.byName("f"),
                    TargetVariableConfig.builder()
                        .target(target(lastUsableNodeId))
                        .overrideHandling(OverrideValueHandling.LastUsableValue)
                        .build())
                .build());

    // establish a last usable value first
    writer.onDataSet(event(field("f", 0, new DataValue(Variant.ofDouble(3.5)))));

    var writeCount = new AtomicInteger();
    testServer
        .getVariableNode(disabledNodeId)
        .addAttributeObserver(
            (node, attributeId, value) -> {
              if (attributeId == AttributeId.Value) {
                writeCount.incrementAndGet();
              }
            });

    writer.onStateChange(PubSubState.Disabled);

    DataValue disabledWrite = testServer.getValue(disabledNodeId);
    assertNull(disabledWrite.value().value());
    assertEquals(StatusCodes.Bad_OutOfService, disabledWrite.statusCode().value());
    assertEquals(1, writeCount.get(), "state-change targets are written exactly once");

    DataValue overrideWrite = testServer.getValue(overrideNodeId);
    assertEquals(99.5, overrideWrite.value().value());
    assertEquals(StatusCodes.Good_LocalOverride, overrideWrite.statusCode().value());

    DataValue lastUsableWrite = testServer.getValue(lastUsableNodeId);
    assertEquals(3.5, lastUsableWrite.value().value());
    assertEquals(StatusCodes.Uncertain_LastUsableValue, lastUsableWrite.statusCode().value());
  }

  @Test
  void stateChangeIntoPausedWritesBadOutOfService() {
    NodeId nodeId = scalarNode(NodeIds.Double, Variant.ofDouble(1.0));
    TargetVariablesWriter writer = writer(mapping("f", nodeId));

    writer.onStateChange(PubSubState.Paused);

    DataValue written = testServer.getValue(nodeId);
    assertNull(written.value().value());
    assertEquals(StatusCodes.Bad_OutOfService, written.statusCode().value());
  }

  @Test
  void stateChangeIntoErrorWritesBadNoCommunication() {
    NodeId nodeId = scalarNode(NodeIds.Double, Variant.ofDouble(1.0));
    TargetVariablesWriter writer = writer(mapping("f", nodeId));

    writer.onStateChange(PubSubState.Error);

    DataValue written = testServer.getValue(nodeId);
    assertNull(written.value().value());
    assertEquals(StatusCodes.Bad_NoCommunication, written.statusCode().value());
  }

  @Test
  void stateChangeIntoOperationalOrPreOperationalWritesNothing() {
    NodeId nodeId = scalarNode(NodeIds.Double, Variant.ofDouble(1.0));
    TargetVariablesWriter writer = writer(mapping("f", nodeId));

    var writeCount = new AtomicInteger();
    testServer
        .getVariableNode(nodeId)
        .addAttributeObserver(
            (node, attributeId, value) -> {
              if (attributeId == AttributeId.Value) {
                writeCount.incrementAndGet();
              }
            });

    writer.onStateChange(PubSubState.PreOperational);
    writer.onStateChange(PubSubState.Operational);

    assertEquals(0, writeCount.get());
    assertEquals(1.0, testServer.getValue(nodeId).value().value());
  }

  @Test
  void lastUsableValueIsTheSlicedValue() {
    NodeId nodeId = arrayNode(NodeIds.Int32, Variant.of(new Integer[] {0, 0}));

    TargetVariablesWriter writer =
        writer(
            TargetVariablesConfig.builder()
                .map(
                    FieldSelector.byName("f"),
                    TargetVariableConfig.builder()
                        .target(target(nodeId))
                        .receiverIndexRange("0:1")
                        .overrideHandling(OverrideValueHandling.LastUsableValue)
                        .build())
                .build());

    writer.onDataSet(event(field("f", 0, new DataValue(Variant.of(new Integer[] {5, 6, 7})))));
    writer.onDataSet(event(badField("f", 0, StatusCodes.Bad_DeviceFailure)));

    DataValue written = testServer.getValue(nodeId);
    assertArrayEquals(new Integer[] {5, 6}, (Integer[]) written.value().value());
    assertEquals(StatusCodes.Uncertain_LastUsableValue, written.statusCode().value());
  }

  @Test
  void constructionRejectsUnresolvableTargetsAndMalformedRanges() {
    NodeId nodeId = scalarNode(NodeIds.Double, Variant.ofDouble(0.0));

    TargetVariablesConfig unresolvable =
        TargetVariablesConfig.builder()
            .map(
                FieldSelector.byName("f"),
                TargetVariableConfig.builder()
                    .target(
                        NodeFieldAddress.parse(
                            "nsu=urn:milo:test:unresolvable;s=X", AttributeId.Value))
                    .build())
            .build();

    PubSubConfigValidationException e1 =
        assertThrows(PubSubConfigValidationException.class, () -> writer(unresolvable));
    assertTrue(e1.getMessage().contains(READER_PATH), e1.getMessage());

    TargetVariablesConfig malformedRange =
        TargetVariablesConfig.builder()
            .map(
                FieldSelector.byName("f"),
                TargetVariableConfig.builder()
                    .target(target(nodeId))
                    .receiverIndexRange("not-a-range")
                    .build())
            .build();

    PubSubConfigValidationException e2 =
        assertThrows(PubSubConfigValidationException.class, () -> writer(malformedRange));
    assertTrue(e2.getMessage().contains("not-a-range"), e2.getMessage());
  }

  // region fixtures

  private TargetVariablesWriter writer(TargetVariablesConfig config) {
    return new TargetVariablesWriter(testServer.getServer(), READER_PATH, config, counters);
  }

  /** A single mapping of the field named {@code fieldName} to {@code nodeId}, default handling. */
  private static TargetVariablesConfig mapping(String fieldName, NodeId nodeId) {
    return TargetVariablesConfig.builder()
        .map(
            FieldSelector.byName(fieldName),
            TargetVariableConfig.builder().target(target(nodeId)).build())
        .build();
  }

  private static NodeFieldAddress target(NodeId nodeId) {
    return NodeFieldAddress.of(
        nodeId, AttributeId.Value, testServer.getServer().getNamespaceTable());
  }

  private static NodeId scalarNode(NodeId dataTypeId, Variant initialValue) {
    return testServer.addVariable(nextNodeName(), dataTypeId, new DataValue(initialValue));
  }

  private static NodeId arrayNode(NodeId dataTypeId, Variant initialValue) {
    return testServer.addArrayVariable(nextNodeName(), dataTypeId, new DataValue(initialValue));
  }

  private static String nextNodeName() {
    return "TVW_Node" + nodeCounter.incrementAndGet();
  }

  private static String errorKey(NodeId nodeId) {
    return READER_PATH + "/" + nodeId.toParseableString();
  }

  private static DataSetReceivedEvent event(DataSetFieldValue... fields) {
    return new DataSetReceivedEvent(
        new PubSubHandle(ComponentType.DATA_SET_READER, READER_PATH),
        PublisherId.uint16(ushort(1)),
        ushort(1),
        ushort(1),
        "ds",
        null,
        List.of(fields));
  }

  private static DataSetFieldValue field(String name, int index, DataValue value) {
    return new DataSetFieldValue(UUID.nameUUIDFromBytes(name.getBytes()), name, index, value);
  }

  private static DataSetFieldValue badField(String name, int index, long statusCode) {
    return field(name, index, new DataValue(Variant.ofNull(), new StatusCode(statusCode)));
  }

  // endregion
}
