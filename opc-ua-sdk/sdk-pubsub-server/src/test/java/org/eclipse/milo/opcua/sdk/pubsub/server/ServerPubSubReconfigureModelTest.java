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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
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
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Incremental info model rebuild: reconfiguring through {@code ServerPubSub.runtime()} deletes and
 * rebuilds exactly the affected config-derived subtrees, keyed by name path, with path-stable
 * NodeIds, live Status/State wiring on rebuilt nodes, the maintained ns0 values
 * (ConfigurationVersion from the mediator-owned single source), the {@link ConfigurationObjectIds}
 * answers, and the {@link ComponentNodeListener} integration point.
 *
 * <p>Network safety: connections use unicast 127.0.0.1 with ephemeral ports and an explicit
 * loopback {@code discoveryAddress}, so the engine's discovery channels never touch the default
 * multicast group 224.0.2.14:4840.
 */
class ServerPubSubReconfigureModelTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final String CONN1 = "rm-conn1";
  private static final String CONN2 = "rm-conn2";
  private static final String WRITER_GROUP = "wgrp";
  private static final String WRITER = "writer";
  private static final String READER_GROUP = "rgrp";
  private static final String READER = "reader";
  private static final String READER2 = "reader2";
  private static final String READER_GROUP2 = "rgrp2";
  private static final String DATA_SET = "rm-ds";

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4761));
  private static final PublisherId EXTERNAL_PUBLISHER_ID = PublisherId.uint16(ushort(4762));
  private static final UShort EXTERNAL_GROUP_ID = ushort(1);
  private static final UShort EXTERNAL_WRITER_ID = ushort(1);

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
  private static NodeId sourceNodeId;
  private static NodeId source2NodeId;
  private static NodeId targetNodeId;

  private final List<ServerPubSub> attached = new CopyOnWriteArrayList<>();

  @BeforeAll
  static void startServer() {
    testServer = TestPubSubServer.create();

    sourceNodeId =
        testServer.addVariable("RM_Source", NodeIds.Double, new DataValue(Variant.ofDouble(1.5)));
    source2NodeId =
        testServer.addVariable("RM_Source2", NodeIds.Double, new DataValue(Variant.ofDouble(2.5)));
    targetNodeId =
        testServer.addVariable("RM_Target", NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));
  }

  @AfterAll
  static void stopServer() {
    testServer.close();
  }

  @AfterEach
  void tearDown() {
    attached.forEach(ServerPubSub::close);
    attached.clear();
  }

  @Test
  void addedConnectionSubtreeIsBrowsableAndLiveAndRemovalDeletesIt() throws Exception {
    Ports ports = Ports.pick();

    ServerPubSub serverPubSub = attach(config(ports, Shape.base()));
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // add a disabled connection with a reader group + reader
    serverPubSub.runtime().update(current -> config(ports, Shape.base().withSecondConnection()));

    // the added subtree is browsable with deterministic ids
    assertTrue(managedNode(fragmentNodeId("PubSub/" + CONN2)).isPresent());
    assertTrue(managedNode(fragmentNodeId("PubSub/" + CONN2 + "/" + READER_GROUP2)).isPresent());
    String reader2Path = "PubSub/" + CONN2 + "/" + READER_GROUP2 + "/" + READER2;
    assertTrue(managedNode(fragmentNodeId(reader2Path)).isPresent());
    assertTrue(managedNode(fragmentNodeId(reader2Path + "/Status/State")).isPresent());

    // the added connection is disabled, and its State variable was seeded accordingly
    awaitState("PubSub/" + CONN2, PubSubState.Disabled);

    // rebuilt Status/State nodes are live: enabling through the runtime moves the value
    PubSubHandle connection = serverPubSub.runtime().components().connection(CONN2).orElseThrow();
    serverPubSub.runtime().enable(connection);
    awaitState("PubSub/" + CONN2, PubSubState.Operational);

    // removing the connection deletes the whole subtree, every child id included
    serverPubSub.runtime().update(current -> config(ports, Shape.base()));

    for (String identifier :
        List.of(
            "PubSub/" + CONN2,
            "PubSub/" + CONN2 + "/" + READER_GROUP2,
            reader2Path,
            reader2Path + "/Status",
            reader2Path + "/Status/State",
            reader2Path + "/SubscribedDataSet")) {
      assertTrue(managedNode(fragmentNodeId(identifier)).isEmpty(), identifier);
    }

    // the untouched connection keeps serving
    assertTrue(managedNode(fragmentNodeId("PubSub/" + CONN1)).isPresent());
  }

  @Test
  void restartedWriterGroupKeepsNodeIdsAndUpdatesValuesAndReseedsState() throws Exception {
    Ports ports = Ports.pick();

    ServerPubSub serverPubSub = attach(config(ports, Shape.base()));
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    String groupPath = "PubSub/" + CONN1 + "/" + WRITER_GROUP;
    awaitState(groupPath, PubSubState.Operational);
    assertEquals(100.0, propertyValue(groupPath + "/PublishingInterval"));

    serverPubSub
        .runtime()
        .update(current -> config(ports, Shape.base().withPublishingIntervalMillis(200)));

    // same NodeIds, new property values, State re-seeded from the restarted runtime
    assertEquals(200.0, propertyValue(groupPath + "/PublishingInterval"));
    assertTrue(managedNode(fragmentNodeId(groupPath + "/" + WRITER)).isPresent());
    awaitState(groupPath, PubSubState.Operational);
  }

  @Test
  void publishedDataSetChangeRebuildsReferencingWriterWithDataSetToWriterRefIntact()
      throws Exception {
    Ports ports = Ports.pick();

    ServerPubSub serverPubSub = attach(config(ports, Shape.base()));
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    NodeId dataSetNodeId = fragmentNodeId("PubSub/PublishedDataSets/" + DATA_SET);
    NodeId writerNodeId = fragmentNodeId("PubSub/" + CONN1 + "/" + WRITER_GROUP + "/" + WRITER);

    DataSetMetaDataType before =
        (DataSetMetaDataType)
            propertyValue("PubSub/PublishedDataSets/" + DATA_SET + "/DataSetMetaData");
    assertNotNull(before);
    assertEquals(1, before.getFields().length);

    serverPubSub.runtime().update(current -> config(ports, Shape.base().withExtraDataSetField()));

    // the dataset nodes reflect the new metadata (field added => minor version bump)
    DataSetMetaDataType after =
        (DataSetMetaDataType)
            propertyValue("PubSub/PublishedDataSets/" + DATA_SET + "/DataSetMetaData");
    assertNotNull(after);
    assertEquals(2, after.getFields().length);

    // the referencing writer was rebuilt in the same apply (induced CHANGED entry) and the
    // DataSetToWriter reference is intact: the dataset node browses it forward to the writer
    UaNode dataSetNode = (UaNode) managedNode(dataSetNodeId).orElseThrow();
    boolean hasDataSetToWriter =
        dataSetNode.getReferences().stream()
            .anyMatch(
                reference ->
                    reference.isForward()
                        && reference.getReferenceTypeId().equals(NodeIds.DataSetToWriter)
                        && reference.getTargetNodeId().equalTo(writerNodeId));
    assertTrue(hasDataSetToWriter, "expected a forward DataSetToWriter reference to the writer");
  }

  @Test
  void addedReaderWithTargetVariablesBuildsSubscribedDataSetNodes() throws Exception {
    Ports ports = Ports.pick();

    ServerPubSub serverPubSub = attach(config(ports, Shape.base()));
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    serverPubSub.runtime().update(current -> config(ports, Shape.base().withSecondReader()));

    String readerPath = "PubSub/" + CONN1 + "/" + READER_GROUP + "/" + READER2;
    assertTrue(managedNode(fragmentNodeId(readerPath)).isPresent());
    assertTrue(managedNode(fragmentNodeId(readerPath + "/SubscribedDataSet")).isPresent());
    assertNotNull(propertyValue(readerPath + "/SubscribedDataSet/TargetVariables"));
  }

  @Test
  void ns0ConfigurationVersionAdvancesAndRootStateFollowsEnabledFlips() throws Exception {
    Ports ports = Ports.pick();

    ServerPubSub serverPubSub = attach(config(ports, Shape.base()));
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    UInteger initialVersion = ns0ConfigurationVersion();
    assertTrue(initialVersion.longValue() > 0L);
    assertEquals(PubSubState.Operational, ns0RootState());

    serverPubSub.runtime().update(current -> config(ports, Shape.base().withEnabled(false)));

    UInteger afterFirst = ns0ConfigurationVersion();
    assertTrue(
        afterFirst.longValue() > initialVersion.longValue(),
        "ConfigurationVersion must strictly increase per apply");
    assertEquals(PubSubState.Disabled, ns0RootState());

    serverPubSub.runtime().update(current -> config(ports, Shape.base()));

    UInteger afterSecond = ns0ConfigurationVersion();
    assertTrue(afterSecond.longValue() > afterFirst.longValue());
    assertEquals(PubSubState.Operational, ns0RootState());
  }

  @Test
  void configurationObjectIdsAnswerForMaterializedKindsAndNullOtherwise() throws Exception {
    Ports ports = Ports.pick();

    ServerPubSub serverPubSub = attach(config(ports, Shape.base().withSecondConnection()));
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    ConfigurationObjectIds objectIds = serverPubSub.configurationObjectIds();
    assertNotNull(objectIds);

    assertEquals(fragmentNodeId("PubSub/" + CONN1), objectIds.connectionObjectId(CONN1));
    assertEquals(
        fragmentNodeId("PubSub/" + CONN1 + "/" + WRITER_GROUP),
        objectIds.writerGroupObjectId(CONN1, WRITER_GROUP));
    assertEquals(
        fragmentNodeId("PubSub/" + CONN1 + "/" + WRITER_GROUP + "/" + WRITER),
        objectIds.dataSetWriterObjectId(CONN1, WRITER_GROUP, WRITER));
    assertEquals(
        fragmentNodeId("PubSub/" + CONN1 + "/" + READER_GROUP),
        objectIds.readerGroupObjectId(CONN1, READER_GROUP));
    assertEquals(
        fragmentNodeId("PubSub/" + CONN1 + "/" + READER_GROUP + "/" + READER),
        objectIds.dataSetReaderObjectId(CONN1, READER_GROUP, READER));
    assertEquals(
        fragmentNodeId("PubSub/PublishedDataSets/" + DATA_SET),
        objectIds.publishedDataSetObjectId(DATA_SET));

    // removed elements answer null (existence-checked, truthful after rebuilds)
    serverPubSub.runtime().update(current -> config(ports, Shape.base()));
    assertNull(objectIds.connectionObjectId(CONN2));
    assertNull(objectIds.readerGroupObjectId(CONN2, READER_GROUP2));

    // never-materialized names answer null
    assertNull(objectIds.connectionObjectId("no-such-connection"));
    assertNull(objectIds.publishedDataSetObjectId("no-such-dataset"));

    // no fragment => no supplier (empty output)
    ServerPubSub unexposed =
        attach(
            config(Ports.pick(), Shape.base()),
            ServerPubSubOptions.builder().exposeInformationModel(false).build());
    assertNull(unexposed.configurationObjectIds());
  }

  @Test
  void componentNodeListenerObservesBuildsAndRemovalsButNotShutdown() throws Exception {
    Ports ports = Ports.pick();

    ServerPubSub serverPubSub = attach(config(ports, Shape.base()));

    var events = new CopyOnWriteArrayList<String>();
    var listener =
        new ComponentNodeListener() {
          @Override
          public void onComponentBuilt(
              ComponentType type, String namePath, UaObjectNode componentNode) {
            events.add("built:" + type + ":" + namePath);
          }

          @Override
          public void onComponentRemoving(ComponentType type, String namePath) {
            events.add("removing:" + type + ":" + namePath);
          }
        };

    PubSubInfoModelFragment fragment = serverPubSub.infoModelFragment();
    assertNotNull(fragment);
    fragment.addComponentNodeListener(listener);

    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // initial build: all five component kinds fire, nested components before their container,
    // and published datasets never fire
    assertTrue(events.contains("built:CONNECTION:" + CONN1));
    assertTrue(events.contains("built:WRITER_GROUP:" + CONN1 + "/" + WRITER_GROUP));
    assertTrue(
        events.contains("built:DATA_SET_WRITER:" + CONN1 + "/" + WRITER_GROUP + "/" + WRITER));
    assertTrue(events.contains("built:READER_GROUP:" + CONN1 + "/" + READER_GROUP));
    assertTrue(
        events.contains("built:DATA_SET_READER:" + CONN1 + "/" + READER_GROUP + "/" + READER));
    assertTrue(
        events.indexOf("built:DATA_SET_WRITER:" + CONN1 + "/" + WRITER_GROUP + "/" + WRITER)
            < events.indexOf("built:WRITER_GROUP:" + CONN1 + "/" + WRITER_GROUP));
    assertTrue(
        events.indexOf("built:WRITER_GROUP:" + CONN1 + "/" + WRITER_GROUP)
            < events.indexOf("built:CONNECTION:" + CONN1));
    assertFalse(events.stream().anyMatch(event -> event.contains(DATA_SET)));

    // an added connection fires built events only
    events.clear();
    serverPubSub.runtime().update(current -> config(ports, Shape.base().withSecondConnection()));
    assertTrue(events.contains("built:CONNECTION:" + CONN2));
    assertTrue(
        events.contains("built:DATA_SET_READER:" + CONN2 + "/" + READER_GROUP2 + "/" + READER2));
    assertFalse(events.stream().anyMatch(event -> event.startsWith("removing:")));

    // a restart fires removing (children before parents) then built
    events.clear();
    serverPubSub
        .runtime()
        .update(
            current ->
                config(
                    ports, Shape.base().withSecondConnection().withPublishingIntervalMillis(150)));

    String removingWriter = "removing:DATA_SET_WRITER:" + CONN1 + "/" + WRITER_GROUP + "/" + WRITER;
    String removingGroup = "removing:WRITER_GROUP:" + CONN1 + "/" + WRITER_GROUP;
    assertTrue(events.contains(removingWriter));
    assertTrue(events.contains(removingGroup));
    assertTrue(events.indexOf(removingWriter) < events.indexOf(removingGroup));
    assertTrue(
        events.indexOf(removingGroup)
            < events.indexOf("built:WRITER_GROUP:" + CONN1 + "/" + WRITER_GROUP));

    // a removed connection fires removing for the whole subtree, children before parents
    events.clear();
    serverPubSub
        .runtime()
        .update(current -> config(ports, Shape.base().withPublishingIntervalMillis(150)));

    String removingReader2 =
        "removing:DATA_SET_READER:" + CONN2 + "/" + READER_GROUP2 + "/" + READER2;
    String removingGroup2 = "removing:READER_GROUP:" + CONN2 + "/" + READER_GROUP2;
    String removingConn2 = "removing:CONNECTION:" + CONN2;
    assertTrue(events.contains(removingReader2));
    assertTrue(events.contains(removingGroup2));
    assertTrue(events.contains(removingConn2));
    assertTrue(events.indexOf(removingReader2) < events.indexOf(removingGroup2));
    assertTrue(events.indexOf(removingGroup2) < events.indexOf(removingConn2));

    // fragment shutdown fires NOTHING (the documented asymmetry)
    events.clear();
    serverPubSub.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    assertTrue(events.isEmpty(), "no ComponentNodeListener events at shutdown: " + events);
  }

  @Test
  void coexistingEntriesRebuildAndNotifyEachComponentExactlyOnce() throws Exception {
    // under STOP_AND_RESTART, nested ADDED/REMOVED entries are reported ALONGSIDE the containing
    // connection's CHANGED entry (the ReconfigureResult coexistence contract); the rebuild must
    // not double-build the descendant subtree (duplicate references) or double-notify listeners
    Ports ports = Ports.pick();

    ServerPubSub serverPubSub = attach(config(ports, Shape.base()));

    var events = new CopyOnWriteArrayList<String>();
    var listener =
        new ComponentNodeListener() {
          @Override
          public void onComponentBuilt(
              ComponentType type, String namePath, UaObjectNode componentNode) {
            events.add("built:" + type + ":" + namePath);
          }

          @Override
          public void onComponentRemoving(ComponentType type, String namePath) {
            events.add("removing:" + type + ":" + namePath);
          }
        };

    PubSubInfoModelFragment fragment = serverPubSub.infoModelFragment();
    assertNotNull(fragment);
    fragment.addComponentNodeListener(listener);

    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // nested reader ADDED entry + connection CHANGED entry in one apply
    events.clear();
    serverPubSub
        .runtime()
        .reconfigure(
            config(ports, Shape.base().withSecondReader()),
            PubSubService.ReconfigureMode.STOP_AND_RESTART);

    String reader2Path = CONN1 + "/" + READER_GROUP + "/" + READER2;
    String readerPath = CONN1 + "/" + READER_GROUP + "/" + READER;
    assertEquals(1, frequency(events, "built:DATA_SET_READER:" + reader2Path));
    assertEquals(1, frequency(events, "built:CONNECTION:" + CONN1));
    assertEquals(1, frequency(events, "removing:DATA_SET_READER:" + readerPath));
    assertEquals(1, frequency(events, "removing:CONNECTION:" + CONN1));

    // no duplicate references: the group grafts the added reader exactly once
    UaNode groupNode =
        managedNode(fragmentNodeId("PubSub/" + CONN1 + "/" + READER_GROUP)).orElseThrow();
    assertEquals(1, readerReferenceCount(groupNode, reader2Path));

    // nested reader REMOVED entry + connection CHANGED entry in one apply: the reader's
    // removing notification fires exactly once (not again from the connection's old-config walk)
    events.clear();
    serverPubSub
        .runtime()
        .reconfigure(config(ports, Shape.base()), PubSubService.ReconfigureMode.STOP_AND_RESTART);

    assertEquals(1, frequency(events, "removing:DATA_SET_READER:" + reader2Path));
    assertEquals(1, frequency(events, "built:CONNECTION:" + CONN1));
    assertTrue(managedNode(fragmentNodeId("PubSub/" + reader2Path)).isEmpty());
  }

  private static int frequency(List<String> events, String event) {
    return Collections.frequency(events, event);
  }

  /** Count the group node's forward HasDataSetReader references to the named reader. */
  private static long readerReferenceCount(UaNode groupNode, String readerPath) {
    NodeId readerNodeId = fragmentNodeId("PubSub/" + readerPath);
    return groupNode.getReferences().stream()
        .filter(
            reference ->
                reference.isForward()
                    && reference.getReferenceTypeId().equals(NodeIds.HasDataSetReader)
                    && reference.getTargetNodeId().equalTo(readerNodeId))
        .count();
  }

  // region fixtures

  /** The ports of one attachment, picked once so unrelated config rebuilds compare equal. */
  private record Ports(int dataPort1, int discoveryPort1, int dataPort2, int discoveryPort2) {
    static Ports pick() throws SocketException {
      return new Ports(freeUdpPort(), freeUdpPort(), freeUdpPort(), freeUdpPort());
    }
  }

  /** The variable parts of the test configuration; equal shapes build equal configs. */
  private record Shape(
      long publishingIntervalMillis,
      boolean extraDataSetField,
      boolean secondReader,
      boolean secondConnection,
      boolean enabled) {

    static Shape base() {
      return new Shape(100, false, false, false, true);
    }

    Shape withPublishingIntervalMillis(long value) {
      return new Shape(value, extraDataSetField, secondReader, secondConnection, enabled);
    }

    Shape withExtraDataSetField() {
      return new Shape(publishingIntervalMillis, true, secondReader, secondConnection, enabled);
    }

    Shape withSecondReader() {
      return new Shape(
          publishingIntervalMillis, extraDataSetField, true, secondConnection, enabled);
    }

    Shape withSecondConnection() {
      return new Shape(publishingIntervalMillis, extraDataSetField, secondReader, true, enabled);
    }

    Shape withEnabled(boolean value) {
      return new Shape(
          publishingIntervalMillis, extraDataSetField, secondReader, secondConnection, value);
    }
  }

  private static PubSubConfig config(Ports ports, Shape shape) {
    PublishedDataSetConfig.Builder dataSet = PublishedDataSetConfig.builder(DATA_SET);
    dataSet.field(
        FieldDefinition.builder("value")
            .source(nodeAddress(sourceNodeId))
            .dataType(NodeIds.Double)
            .dataSetFieldId(new UUID(0L, 0xF1L))
            .build());
    if (shape.extraDataSetField()) {
      dataSet.field(
          FieldDefinition.builder("extra")
              .source(nodeAddress(source2NodeId))
              .dataType(NodeIds.Double)
              .dataSetFieldId(new UUID(0L, 0xF2L))
              .build());
    }
    PublishedDataSetConfig builtDataSet = dataSet.build();

    ReaderGroupConfig.Builder readerGroup =
        ReaderGroupConfig.builder(READER_GROUP).dataSetReader(externalReader(READER));
    if (shape.secondReader()) {
      readerGroup.dataSetReader(externalReader(READER2));
    }

    PubSubConfig.Builder config =
        PubSubConfig.builder()
            .enabled(shape.enabled())
            .publishedDataSet(builtDataSet)
            .connection(
                PubSubConnectionConfig.udp(CONN1)
                    .publisherId(PUBLISHER_ID)
                    .address(UdpDatagramAddress.unicast("127.0.0.1", ports.dataPort1()))
                    .discoveryAddress(
                        UdpDatagramAddress.unicast("127.0.0.1", ports.discoveryPort1()))
                    .writerGroup(
                        WriterGroupConfig.builder(WRITER_GROUP)
                            .writerGroupId(ushort(21))
                            .publishingInterval(Duration.ofMillis(shape.publishingIntervalMillis()))
                            .messageSettings(GROUP_SETTINGS)
                            .dataSetWriter(
                                DataSetWriterConfig.builder(WRITER)
                                    .dataSet(builtDataSet.ref())
                                    .dataSetWriterId(ushort(31))
                                    .fieldContentMask(FIELD_MASK)
                                    .settings(WRITER_SETTINGS)
                                    .build())
                            .build())
                    .readerGroup(readerGroup.build())
                    .build());

    if (shape.secondConnection()) {
      config.connection(
          PubSubConnectionConfig.udp(CONN2)
              .enabled(false)
              .address(UdpDatagramAddress.unicast("127.0.0.1", ports.dataPort2()))
              .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", ports.discoveryPort2()))
              .readerGroup(
                  ReaderGroupConfig.builder(READER_GROUP2)
                      .dataSetReader(externalReader(READER2))
                      .build())
              .build());
    }

    return config.build();
  }

  /** A reader filtering on an external publisher that never publishes in these tests. */
  private static DataSetReaderConfig externalReader(String name) {
    TargetVariablesConfig targetVariables =
        TargetVariablesConfig.builder()
            .map(
                FieldSelector.byName("v"),
                TargetVariableConfig.builder().target(nodeAddress(targetNodeId)).build())
            .build();

    return DataSetReaderConfig.builder(name)
        .publisherId(EXTERNAL_PUBLISHER_ID)
        .writerGroupId(EXTERNAL_GROUP_ID)
        .dataSetWriterId(EXTERNAL_WRITER_ID)
        .dataSetMetaData(
            DataSetMetaDataConfig.builder("rm-ext-ds")
                .field("v", NodeIds.Double, new UUID(0L, 0xF3L))
                .build())
        .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
        .subscribedDataSet(targetVariables)
        .build();
  }

  private static NodeFieldAddress nodeAddress(NodeId nodeId) {
    return NodeFieldAddress.of(
        nodeId, AttributeId.Value, testServer.getServer().getNamespaceTable());
  }

  private ServerPubSub attach(PubSubConfig config) {
    return attach(config, ServerPubSubOptions.builder().exposeInformationModel(true).build());
  }

  private ServerPubSub attach(PubSubConfig config, ServerPubSubOptions options) {
    ServerPubSub serverPubSub = ServerPubSub.attach(testServer.getServer(), config, options);
    attached.add(serverPubSub);
    return serverPubSub;
  }

  // endregion

  // region helpers

  private static NodeId fragmentNodeId(String identifier) {
    return new NodeId(testServer.getServer().getServerNamespace().getNamespaceIndex(), identifier);
  }

  private static java.util.Optional<UaNode> managedNode(NodeId nodeId) {
    return testServer.getServer().getAddressSpaceManager().getManagedNode(nodeId);
  }

  private static Object propertyValue(String identifier) {
    UaNode node =
        managedNode(fragmentNodeId(identifier))
            .orElseThrow(() -> new AssertionError("missing node: " + identifier));
    return ((UaVariableNode) node).getValue().value().value();
  }

  private static UInteger ns0ConfigurationVersion() {
    UaVariableNode node =
        (UaVariableNode) managedNode(NodeIds.PublishSubscribe_ConfigurationVersion).orElseThrow();
    return (UInteger) node.getValue().value().value();
  }

  private static PubSubState ns0RootState() {
    UaVariableNode node =
        (UaVariableNode) managedNode(NodeIds.PublishSubscribe_Status_State).orElseThrow();
    return (PubSubState) node.getValue().value().value();
  }

  /** Poll the State variable under {@code componentIdentifier}/Status until it equals state. */
  private static void awaitState(String componentIdentifier, PubSubState state)
      throws InterruptedException {

    NodeId nodeId = fragmentNodeId(componentIdentifier + "/Status/State");

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    Object lastValue = null;
    while (true) {
      UaNode node = managedNode(nodeId).orElse(null);
      if (node instanceof UaVariableNode variableNode) {
        lastValue = variableNode.getValue().value().value();
        if (state.equals(lastValue)) {
          return;
        }
      }
      if (System.nanoTime() >= deadline) {
        fail(
            "timed out waiting for State == "
                + state
                + " on "
                + componentIdentifier
                + "; last value: "
                + lastValue);
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
