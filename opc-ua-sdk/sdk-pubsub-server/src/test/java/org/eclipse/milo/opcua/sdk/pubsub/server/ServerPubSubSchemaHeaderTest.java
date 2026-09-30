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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * {@link ServerPubSub} completes the DataTypeSchemaHeader of published datasets from the server's
 * DataType hierarchy, at attach and again on every configuration apply, so a node-backed dataset
 * announces UtcTime as DateTime (13) and an enumeration as Int32 (6) without the application
 * authoring the descriptions (OPC UA 10000-14 §6.2.3.2.2 and Table 7). The persisted configuration
 * snapshot is the observable: it is mapped from the effective configuration the runtime uses.
 */
class ServerPubSubSchemaHeaderTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final UUID TIME_FIELD_ID = new UUID(0L, 0xF1L);
  private static final UUID STATE_FIELD_ID = new UUID(0L, 0xF2L);
  private static final UUID DURATION_FIELD_ID = new UUID(0L, 0xF3L);

  private static TestPubSubServer testServer;
  private static NodeId timeNodeId;
  private static NodeId stateNodeId;
  private static NodeId durationNodeId;

  @BeforeAll
  static void startServer() {
    testServer = TestPubSubServer.create();
    timeNodeId =
        testServer.addVariable(
            "SH_Time", NodeIds.UtcTime, new DataValue(Variant.of(DateTime.now())));
    stateNodeId =
        testServer.addVariable(
            "SH_State", NodeIds.ServerState, new DataValue(Variant.of(ServerState.Running)));
    durationNodeId =
        testServer.addVariable(
            "SH_Duration", NodeIds.Duration, new DataValue(Variant.ofDouble(1500.0)));
  }

  @AfterAll
  static void stopServer() {
    testServer.close();
  }

  @Test
  void attachCompletesSchemaHeaderOfNodeBackedDataSet() {
    var store = new RecordingStore();
    ServerPubSubOptions options = ServerPubSubOptions.builder().configurationStore(store).build();

    PubSubConfig config =
        PubSubConfig.builder().publishedDataSet(timeAndStateDataSet("sh-ds1")).build();

    try (ServerPubSub ignored = ServerPubSub.attach(testServer.getServer(), config, options)) {
      assertEquals(1, store.saved.size(), "attach saves the effective configuration once");

      DataSetMetaDataType metaData = metaData(store.saved.get(0), "sh-ds1");

      assertEquals(ubyte(13), metaData.getFields()[0].getBuiltInType(), "UtcTime as DateTime");
      assertEquals(ubyte(6), metaData.getFields()[1].getBuiltInType(), "ServerState as Int32");

      assertNotNull(metaData.getSimpleDataTypes());
      assertEquals(NodeIds.UtcTime, metaData.getSimpleDataTypes()[0].getDataTypeId());
      assertEquals(NodeIds.DateTime, metaData.getSimpleDataTypes()[0].getBaseDataType());

      assertNotNull(metaData.getEnumDataTypes());
      assertEquals(NodeIds.ServerState, metaData.getEnumDataTypes()[0].getDataTypeId());
      assertEquals(ServerState.definition(), metaData.getEnumDataTypes()[0].getEnumDefinition());

      // the persisted snapshot is exactly the completed configuration
      assertEquals(
          DataTypeSchemaHeaders.populate(config, testServer.getServer().getDataTypeTree())
              .toDataType(testServer.getServer().getNamespaceTable()),
          PubSubConfigurationFace.withConfigurationVersion(store.saved.get(0), uint(0)));
    }
  }

  /**
   * A dataset added by a later apply through the managed runtime is completed the same way, so the
   * runtime, the hooks, and the persisted snapshot agree on one effective configuration.
   */
  @Test
  void configurationApplyCompletesSchemaHeaderOfAddedDataSet() throws Exception {
    var store = new RecordingStore();
    ServerPubSubOptions options = ServerPubSubOptions.builder().configurationStore(store).build();

    PubSubConfig initial =
        PubSubConfig.builder().publishedDataSet(timeAndStateDataSet("sh-ds2")).build();

    try (ServerPubSub serverPubSub =
        ServerPubSub.attach(testServer.getServer(), initial, options)) {
      serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

      PublishedDataSetConfig added =
          PublishedDataSetConfig.builder("sh-ds3")
              .field(
                  FieldDefinition.builder("duration")
                      .source(nodeAddress(durationNodeId))
                      .dataType(NodeIds.Duration)
                      .dataSetFieldId(DURATION_FIELD_ID)
                      .build())
              .build();

      serverPubSub.runtime().update(current -> current.toBuilder().publishedDataSet(added).build());

      assertEquals(2, store.saved.size(), "the apply saved the effective configuration again");

      DataSetMetaDataType metaData = metaData(store.saved.get(1), "sh-ds3");
      assertEquals(ubyte(11), metaData.getFields()[0].getBuiltInType(), "Duration as Double");
      assertNotNull(metaData.getSimpleDataTypes());
      assertEquals(NodeIds.Duration, metaData.getSimpleDataTypes()[0].getDataTypeId());

      // the attach-time dataset, carried through the apply, is still complete
      assertEquals(
          ubyte(13), metaData(store.saved.get(1), "sh-ds2").getFields()[0].getBuiltInType());
    }
  }

  // region fixtures

  private static PublishedDataSetConfig timeAndStateDataSet(String name) {
    return PublishedDataSetConfig.builder(name)
        .field(
            FieldDefinition.builder("time")
                .source(nodeAddress(timeNodeId))
                .dataType(NodeIds.UtcTime)
                .dataSetFieldId(TIME_FIELD_ID)
                .build())
        .field(
            FieldDefinition.builder("state")
                .source(nodeAddress(stateNodeId))
                .dataType(NodeIds.ServerState)
                .dataSetFieldId(STATE_FIELD_ID)
                .build())
        .build();
  }

  private static NodeFieldAddress nodeAddress(NodeId nodeId) {
    return NodeFieldAddress.of(
        nodeId, AttributeId.Value, testServer.getServer().getNamespaceTable());
  }

  private static DataSetMetaDataType metaData(PubSubConfiguration2DataType wire, String name) {
    PublishedDataSetDataType[] dataSets = wire.getPublishedDataSets();
    assertNotNull(dataSets);
    for (PublishedDataSetDataType dataSet : dataSets) {
      if (name.equals(dataSet.getName())) {
        return dataSet.getDataSetMetaData();
      }
    }
    throw new AssertionError("no published dataset named " + name);
  }

  private static final class RecordingStore implements PubSubConfigurationStore {

    final List<PubSubConfiguration2DataType> saved = new CopyOnWriteArrayList<>();

    @Override
    public @Nullable PubSubConfiguration2DataType load() {
      return null;
    }

    @Override
    public void save(PubSubConfiguration2DataType value) {
      saved.add(value);
    }
  }

  // endregion
}
