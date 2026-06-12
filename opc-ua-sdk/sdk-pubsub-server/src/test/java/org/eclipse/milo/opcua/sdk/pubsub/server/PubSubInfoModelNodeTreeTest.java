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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
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
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Information model node tree per pinned decision S8: with {@code exposeInformationModel(true)},
 * {@link ServerPubSub#startup()} grafts deterministic string-id subtrees for connections, groups,
 * writers, readers, and published datasets onto the existing ns0 PublishSubscribe nodes.
 *
 * <p>Node construction happens at {@code ServerPubSub.startup()} (the fragment lifecycle builds the
 * nodes and registers its node manager); {@code attach} alone builds nothing, and {@code shutdown}
 * unregisters the fragment again.
 *
 * <p>Network safety: connections use unicast 127.0.0.1 with ephemeral ports and an explicit
 * loopback {@code discoveryAddress}, so the engine's discovery channels never touch the default
 * multicast group 224.0.2.14:4840.
 */
class PubSubInfoModelNodeTreeTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final String CONNECTION = "nt-conn";
  private static final String WRITER_GROUP = "wgrp";
  private static final String WRITER = "writer";
  private static final String READER_GROUP = "rgrp";
  private static final String READER = "reader";
  private static final String DATA_SET = "nt-ds";

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4731));
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

  private static TestPubSubServer testServer;
  private static ServerPubSub serverPubSub;

  @BeforeAll
  static void startServer() throws Exception {
    testServer = TestPubSubServer.create();

    NodeId sourceNodeId =
        testServer.addVariable("NT_Source", NodeIds.Double, new DataValue(Variant.ofDouble(1.5)));
    NodeId targetNodeId =
        testServer.addVariable("NT_Target", NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));

    PubSubConfig config = fullConfig(sourceNodeId, targetNodeId);

    serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            config,
            ServerPubSubOptions.builder().exposeInformationModel(true).build());

    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
  }

  @AfterAll
  static void stopServer() {
    serverPubSub.close();
    testServer.close();
  }

  @Test
  void connectionNodeIsGraftedUnderPublishSubscribe() {
    NodeId connectionNodeId = fragmentNodeId("PubSub/" + CONNECTION);

    UaNode connectionNode = managedNode(connectionNodeId);
    assertEquals(
        new QualifiedName(appNamespaceIndex(), CONNECTION), connectionNode.getBrowseName());
    assertTrue(
        hasReference(connectionNodeId, NodeIds.HasTypeDefinition, NodeIds.PubSubConnectionType));

    // a browse of ns0 i=14443 through the server AddressSpaceManager picks up the connection:
    // the fragment's node manager contributes the forward HasPubSubConnection reference
    assertTrue(
        hasReference(NodeIds.PublishSubscribe, NodeIds.HasPubSubConnection, connectionNodeId),
        "expected forward HasPubSubConnection from i=14443 to " + connectionNodeId);

    // and the connection node carries the inverse reference back to i=14443
    assertTrue(
        hasInverseReference(
            connectionNodeId, NodeIds.HasPubSubConnection, NodeIds.PublishSubscribe),
        "expected inverse HasPubSubConnection from " + connectionNodeId + " to i=14443");
  }

  @Test
  void groupWriterAndReaderNodesAreChildrenOfTheConnection() {
    NodeId connectionNodeId = fragmentNodeId("PubSub/" + CONNECTION);
    NodeId writerGroupNodeId = fragmentNodeId("PubSub/" + CONNECTION + "/" + WRITER_GROUP);
    NodeId writerNodeId =
        fragmentNodeId("PubSub/" + CONNECTION + "/" + WRITER_GROUP + "/" + WRITER);
    NodeId readerGroupNodeId = fragmentNodeId("PubSub/" + CONNECTION + "/" + READER_GROUP);
    NodeId readerNodeId =
        fragmentNodeId("PubSub/" + CONNECTION + "/" + READER_GROUP + "/" + READER);

    managedNode(writerGroupNodeId);
    assertTrue(hasReference(writerGroupNodeId, NodeIds.HasTypeDefinition, NodeIds.WriterGroupType));
    assertTrue(hasReference(connectionNodeId, NodeIds.HasWriterGroup, writerGroupNodeId));

    managedNode(writerNodeId);
    assertTrue(hasReference(writerNodeId, NodeIds.HasTypeDefinition, NodeIds.DataSetWriterType));
    assertTrue(hasReference(writerGroupNodeId, NodeIds.HasDataSetWriter, writerNodeId));

    managedNode(readerGroupNodeId);
    assertTrue(hasReference(readerGroupNodeId, NodeIds.HasTypeDefinition, NodeIds.ReaderGroupType));
    assertTrue(hasReference(connectionNodeId, NodeIds.HasReaderGroup, readerGroupNodeId));

    managedNode(readerNodeId);
    assertTrue(hasReference(readerNodeId, NodeIds.HasTypeDefinition, NodeIds.DataSetReaderType));
    assertTrue(hasReference(readerGroupNodeId, NodeIds.HasDataSetReader, readerNodeId));
  }

  @Test
  void publishedDataSetNodeIsLinkedUnderTheFolderAndToItsWriter() {
    NodeId dataSetNodeId = fragmentNodeId("PubSub/PublishedDataSets/" + DATA_SET);
    NodeId writerNodeId =
        fragmentNodeId("PubSub/" + CONNECTION + "/" + WRITER_GROUP + "/" + WRITER);

    UaNode dataSetNode = managedNode(dataSetNodeId);
    assertEquals(new QualifiedName(appNamespaceIndex(), DATA_SET), dataSetNode.getBrowseName());
    assertTrue(
        hasReference(dataSetNodeId, NodeIds.HasTypeDefinition, NodeIds.PublishedDataItemsType));

    // grafted under the existing ns0 PublishedDataSets folder i=17371
    assertTrue(
        hasReference(
            NodeIds.PublishSubscribe_PublishedDataSets, NodeIds.HasComponent, dataSetNodeId),
        "expected forward HasComponent from i=17371 to " + dataSetNodeId);

    // DataSetToWriter from the dataset node to the writer fed by it
    assertTrue(
        hasReference(dataSetNodeId, NodeIds.DataSetToWriter, writerNodeId),
        "expected forward DataSetToWriter from " + dataSetNodeId + " to " + writerNodeId);
  }

  @Test
  void statusStateNodesExistAtEveryLevelButNotOnTheDataSet() {
    List<String> componentPaths =
        List.of(
            CONNECTION,
            CONNECTION + "/" + WRITER_GROUP,
            CONNECTION + "/" + WRITER_GROUP + "/" + WRITER,
            CONNECTION + "/" + READER_GROUP,
            CONNECTION + "/" + READER_GROUP + "/" + READER);

    for (String path : componentPaths) {
      NodeId componentNodeId = fragmentNodeId("PubSub/" + path);
      NodeId statusNodeId = fragmentNodeId("PubSub/" + path + "/Status");
      NodeId stateNodeId = fragmentNodeId("PubSub/" + path + "/Status/State");

      managedNode(statusNodeId);
      assertTrue(
          hasReference(statusNodeId, NodeIds.HasTypeDefinition, NodeIds.PubSubStatusType),
          path + ": Status type definition");
      assertTrue(
          hasReference(componentNodeId, NodeIds.HasComponent, statusNodeId),
          path + ": HasComponent to Status");

      UaVariableNode stateNode = (UaVariableNode) managedNode(stateNodeId);
      assertEquals(NodeIds.PubSubState, stateNode.getDataType(), path + ": State data type");
      assertInstanceOf(
          PubSubState.class, stateNode.getValue().value().value(), path + ": State value");
    }

    // S9: PublishedDataSet nodes carry no Status child (Part 14 defines none)
    Optional<UaNode> dataSetStatus =
        testServer
            .getServer()
            .getAddressSpaceManager()
            .getManagedNode(fragmentNodeId("PubSub/PublishedDataSets/" + DATA_SET + "/Status"));
    assertTrue(dataSetStatus.isEmpty(), "PublishedDataSet must not have a Status child");
  }

  @Test
  void subscribedDataSetChildIsTargetVariablesType() {
    NodeId readerNodeId =
        fragmentNodeId("PubSub/" + CONNECTION + "/" + READER_GROUP + "/" + READER);
    NodeId subscribedDataSetNodeId =
        fragmentNodeId(
            "PubSub/" + CONNECTION + "/" + READER_GROUP + "/" + READER + "/SubscribedDataSet");
    NodeId targetVariablesNodeId =
        fragmentNodeId(
            "PubSub/"
                + CONNECTION
                + "/"
                + READER_GROUP
                + "/"
                + READER
                + "/SubscribedDataSet/TargetVariables");

    managedNode(subscribedDataSetNodeId);
    assertTrue(
        hasReference(
            subscribedDataSetNodeId, NodeIds.HasTypeDefinition, NodeIds.TargetVariablesType));
    assertTrue(hasReference(readerNodeId, NodeIds.HasComponent, subscribedDataSetNodeId));

    managedNode(targetVariablesNodeId);
    assertTrue(hasReference(subscribedDataSetNodeId, NodeIds.HasProperty, targetVariablesNodeId));
  }

  @Test
  void noMethodNodesAndOnlyReadOnlyVariablesInTheFragmentSubtree() {
    Set<NodeId> visited = new HashSet<>();
    var queue = new ArrayDeque<NodeId>();
    queue.add(fragmentNodeId("PubSub/" + CONNECTION));
    queue.add(fragmentNodeId("PubSub/PublishedDataSets/" + DATA_SET));

    var visitedNodes = new ArrayList<UaNode>();

    while (!queue.isEmpty()) {
      NodeId nodeId = queue.poll();
      if (!visited.add(nodeId)) {
        continue;
      }

      UaNode node = managedNode(nodeId);
      visitedNodes.add(node);

      testServer.getServer().getAddressSpaceManager().getManagedReferences(nodeId).stream()
          .filter(r -> r.isForward() && !r.getReferenceTypeId().equals(NodeIds.HasTypeDefinition))
          .forEach(
              r ->
                  r.getTargetNodeId()
                      .toNodeId(testServer.getServer().getNamespaceTable())
                      .filter(target -> appNamespaceIndex().equals(target.getNamespaceIndex()))
                      .ifPresent(queue::add));
    }

    // sanity: the walk actually traversed the connection and dataset subtrees
    assertTrue(visitedNodes.size() > 30, "expected to visit the full subtree: " + visited);

    for (UaNode node : visitedNodes) {
      assertFalse(
          node instanceof UaMethodNode, "method node in fragment subtree: " + node.getNodeId());

      if (node instanceof UaVariableNode variableNode) {
        Set<AccessLevel> accessLevels = AccessLevel.fromValue(variableNode.getAccessLevel());
        assertFalse(
            accessLevels.contains(AccessLevel.CurrentWrite),
            "writable variable in fragment subtree: " + node.getNodeId());
      }
    }
  }

  @Test
  void attachAloneBuildsNoNodesAndShutdownRemovesThem() throws Exception {
    String connectionName = "nt-conn2";
    NodeId connectionNodeId = fragmentNodeId("PubSub/" + connectionName);

    PubSubConfig config = readerOnlyConfig(connectionName);

    ServerPubSub second =
        ServerPubSub.attach(
            testServer.getServer(),
            config,
            ServerPubSubOptions.builder().exposeInformationModel(true).build());
    try {
      // attach alone builds nothing: the fragment starts with ServerPubSub.startup()
      assertTrue(
          testServer
              .getServer()
              .getAddressSpaceManager()
              .getManagedNode(connectionNodeId)
              .isEmpty(),
          "no fragment nodes before startup()");

      second.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      managedNode(connectionNodeId);

      second.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      assertTrue(
          testServer
              .getServer()
              .getAddressSpaceManager()
              .getManagedNode(connectionNodeId)
              .isEmpty(),
          "fragment nodes are unregistered after shutdown()");
    } finally {
      second.close();
    }
  }

  // region fixtures

  /**
   * The pinned exposure shape: one UDP connection with one writer group / writer publishing one
   * node-backed dataset and one reader group / reader with TargetVariables, on unicast loopback
   * with an explicit loopback discoveryAddress.
   */
  private static PubSubConfig fullConfig(NodeId sourceNodeId, NodeId targetNodeId)
      throws SocketException {

    var fieldId = new UUID(0L, 0x71L);

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder(DATA_SET)
            .field(
                FieldDefinition.builder("value")
                    .source(nodeAddress(sourceNodeId))
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(fieldId)
                    .build())
            .build();

    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder(DATA_SET).field("value", NodeIds.Double, fieldId).build();

    TargetVariablesConfig targetVariables =
        TargetVariablesConfig.builder()
            .map(
                FieldSelector.byName("value"),
                TargetVariableConfig.builder().target(nodeAddress(targetNodeId)).build())
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .writerGroup(
                    WriterGroupConfig.builder(WRITER_GROUP)
                        .writerGroupId(GROUP_ID)
                        .publishingInterval(Duration.ofMillis(100))
                        .messageSettings(GROUP_SETTINGS)
                        .dataSetWriter(
                            DataSetWriterConfig.builder(WRITER)
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(WRITER_ID)
                                .fieldContentMask(
                                    DataSetFieldContentMask.of(
                                        DataSetFieldContentMask.Field.StatusCode))
                                .settings(WRITER_SETTINGS)
                                .build())
                        .build())
                .readerGroup(
                    ReaderGroupConfig.builder(READER_GROUP)
                        .dataSetReader(
                            DataSetReaderConfig.builder(READER)
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
  }

  /** A reader-only config used to observe the attach/startup/shutdown node lifecycle. */
  private static PubSubConfig readerOnlyConfig(String connectionName) throws SocketException {
    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("nt-ds2")
            .field("value", NodeIds.Double, new UUID(0L, 0x72L))
            .build();

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp(connectionName)
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .readerGroup(
                    ReaderGroupConfig.builder(READER_GROUP)
                        .dataSetReader(
                            DataSetReaderConfig.builder(READER)
                                .publisherId(PUBLISHER_ID)
                                .writerGroupId(GROUP_ID)
                                .dataSetWriterId(WRITER_ID)
                                .dataSetMetaData(metaData)
                                .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                .build())
                        .build())
                .build())
        .build();
  }

  private static NodeFieldAddress nodeAddress(NodeId nodeId) {
    return NodeFieldAddress.of(
        nodeId, AttributeId.Value, testServer.getServer().getNamespaceTable());
  }

  // endregion

  // region helpers

  private static UShort appNamespaceIndex() {
    return testServer.getServer().getServerNamespace().getNamespaceIndex();
  }

  /** A {@link NodeId} with the fragment's deterministic string identifier scheme. */
  private static NodeId fragmentNodeId(String identifier) {
    return new NodeId(appNamespaceIndex(), identifier);
  }

  /** Get a node through the server's AddressSpaceManager, failing if it is absent. */
  private static UaNode managedNode(NodeId nodeId) {
    return testServer
        .getServer()
        .getAddressSpaceManager()
        .getManagedNode(nodeId)
        .orElseThrow(() -> new AssertionError("expected node: " + nodeId));
  }

  /** True if a forward reference of {@code referenceTypeId} exists from source to target. */
  private static boolean hasReference(NodeId sourceNodeId, NodeId referenceTypeId, NodeId target) {
    return testServer
        .getServer()
        .getAddressSpaceManager()
        .getManagedReferences(sourceNodeId)
        .stream()
        .anyMatch(
            r ->
                r.isForward()
                    && r.getReferenceTypeId().equals(referenceTypeId)
                    && r.getTargetNodeId().equals(target.expanded()));
  }

  /** True if an inverse reference of {@code referenceTypeId} exists from source to target. */
  private static boolean hasInverseReference(
      NodeId sourceNodeId, NodeId referenceTypeId, NodeId target) {
    return testServer
        .getServer()
        .getAddressSpaceManager()
        .getManagedReferences(sourceNodeId)
        .stream()
        .anyMatch(
            r ->
                r.isInverse()
                    && r.getReferenceTypeId().equals(referenceTypeId)
                    && r.getTargetNodeId().equals(target.expanded()));
  }

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
