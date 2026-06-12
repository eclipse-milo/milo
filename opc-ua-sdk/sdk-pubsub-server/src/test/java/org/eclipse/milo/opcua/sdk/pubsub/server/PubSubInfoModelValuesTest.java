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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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
import org.eclipse.milo.opcua.sdk.pubsub.transport.udp.UdpTransportProvider;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressUrlDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataItemsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.TargetVariablesDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetWriterMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpWriterGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Information model node values per pinned decision S8: every exposed value comes from one {@code
 * PubSubConfig.toDataType} pass over the attach-time configuration, so the nodes show the
 * mapper-normalized values (transport profile URI, empty LocaleIds, "" HeaderLayoutUri, derived
 * DataSetMetaData, defaulted KeyFrameCount, ...).
 *
 * <p>The tests compare node values against an equivalent {@code toDataType} pass made over the same
 * configuration, plus explicit literals where the assignment pins them.
 *
 * <p>Network safety: connections use unicast 127.0.0.1 with ephemeral ports and an explicit
 * loopback {@code discoveryAddress}, so the engine's discovery channels never touch the default
 * multicast group 224.0.2.14:4840.
 */
class PubSubInfoModelValuesTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final String CONNECTION = "vals-conn";
  private static final String WRITER_GROUP = "wgrp";
  private static final String WRITER = "writer";
  private static final String READER_GROUP = "rgrp";
  private static final String READER = "reader";
  private static final String DATA_SET = "vals-ds";

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4741));
  private static final UShort GROUP_ID = ushort(7);
  private static final UShort WRITER_ID = ushort(11);

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
  private static ServerPubSub serverPubSub;

  private static int dataPort;
  private static NodeId sourceNodeId;
  private static NodeId targetNodeId;

  /** The same {@code toDataType} pass the fragment populates values from. */
  private static PubSubConfiguration2DataType expected;

  @BeforeAll
  static void startServer() throws Exception {
    testServer = TestPubSubServer.create();

    dataPort = freeUdpPort();

    sourceNodeId =
        testServer.addVariable("VALS_Source", NodeIds.Double, new DataValue(Variant.ofDouble(7.5)));
    targetNodeId =
        testServer.addVariable("VALS_Target", NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));

    PubSubConfig config = fullConfig();

    expected = config.toDataType(testServer.getServer().getNamespaceTable());

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
  void connectionValuesArePopulated() {
    String prefix = "PubSub/" + CONNECTION;

    // PublisherId is the Variant form of the configured PublisherId
    assertEquals(ushort(4741), nodeValue(prefix + "/PublisherId"));
    assertEquals(expectedConnection().getPublisherId().value(), nodeValue(prefix + "/PublisherId"));

    // TransportProfileUri is a SelectionListType variable valued with the UDP UADP profile,
    // with Selections listing the supported profiles
    UaNode transportProfileNode = managedNode(fragmentNodeId(prefix + "/TransportProfileUri"));
    assertTrue(
        hasReference(
            transportProfileNode.getNodeId(),
            NodeIds.HasTypeDefinition,
            NodeIds.SelectionListType));
    assertEquals(
        UdpTransportProvider.TRANSPORT_PROFILE_URI, nodeValue(prefix + "/TransportProfileUri"));
    assertArrayEquals(
        new String[] {UdpTransportProvider.TRANSPORT_PROFILE_URI},
        (String[]) nodeValue(prefix + "/TransportProfileUri/Selections"));

    // Address is a concrete NetworkAddressUrlType with Url and NetworkInterface variables
    assertEquals("opc.udp://127.0.0.1:" + dataPort, nodeValue(prefix + "/Address/Url"));
    assertEquals(
        ((NetworkAddressUrlDataType) expectedConnection().getAddress()).getUrl(),
        nodeValue(prefix + "/Address/Url"));
    assertEquals("", nodeValue(prefix + "/Address/NetworkInterface"));
  }

  @Test
  void writerGroupValuesArePopulated() {
    String prefix = "PubSub/" + CONNECTION + "/" + WRITER_GROUP;
    WriterGroupDataType expectedGroup = expectedWriterGroup();

    assertEquals(GROUP_ID, nodeValue(prefix + "/WriterGroupId"));
    assertEquals(100.0, nodeValue(prefix + "/PublishingInterval"));
    assertEquals(expectedGroup.getKeepAliveTime(), nodeValue(prefix + "/KeepAliveTime"));
    assertEquals(expectedGroup.getPriority(), nodeValue(prefix + "/Priority"));
    assertEquals(
        expectedGroup.getMaxNetworkMessageSize(), nodeValue(prefix + "/MaxNetworkMessageSize"));
    assertEquals(MessageSecurityMode.None, nodeValue(prefix + "/SecurityMode"));

    // mapper-normalized: no LocaleIds/HeaderLayoutUri config slots exist
    assertArrayEquals(new String[0], (String[]) nodeValue(prefix + "/LocaleIds"));
    assertEquals("", nodeValue(prefix + "/HeaderLayoutUri"));

    UadpWriterGroupMessageDataType expectedSettings =
        (UadpWriterGroupMessageDataType) expectedGroup.getMessageSettings();
    assertNotNull(expectedSettings);

    String settingsPrefix = prefix + "/MessageSettings";
    assertEquals(
        expectedSettings.getNetworkMessageContentMask(),
        nodeValue(settingsPrefix + "/NetworkMessageContentMask"));
    assertEquals(expectedSettings.getGroupVersion(), nodeValue(settingsPrefix + "/GroupVersion"));
    assertEquals(
        expectedSettings.getDataSetOrdering(), nodeValue(settingsPrefix + "/DataSetOrdering"));
    assertArrayEquals(new Double[0], (Double[]) nodeValue(settingsPrefix + "/PublishingOffset"));
  }

  @Test
  void dataSetWriterValuesArePopulated() {
    String prefix = "PubSub/" + CONNECTION + "/" + WRITER_GROUP + "/" + WRITER;
    DataSetWriterDataType expectedWriter = expectedDataSetWriter();

    assertEquals(WRITER_ID, nodeValue(prefix + "/DataSetWriterId"));

    // KeyFrameCount was not configured: the config default (1, every message a key frame) shows
    assertEquals(uint(1), nodeValue(prefix + "/KeyFrameCount"));
    assertEquals(expectedWriter.getKeyFrameCount(), nodeValue(prefix + "/KeyFrameCount"));

    assertEquals(FIELD_MASK, nodeValue(prefix + "/DataSetFieldContentMask"));

    UadpDataSetWriterMessageDataType expectedSettings =
        (UadpDataSetWriterMessageDataType) expectedWriter.getMessageSettings();
    assertNotNull(expectedSettings);

    assertEquals(
        expectedSettings.getDataSetMessageContentMask(),
        nodeValue(prefix + "/MessageSettings/DataSetMessageContentMask"));
  }

  @Test
  void publishedDataSetValuesArePopulated() {
    String prefix = "PubSub/PublishedDataSets/" + DATA_SET;
    PublishedDataSetDataType expectedDataSet = expectedPublishedDataSet();

    // ConfigurationVersion mirrors the dataset's configured (major, minor)
    assertEquals(
        new ConfigurationVersionDataType(uint(3), uint(9)),
        nodeValue(prefix + "/ConfigurationVersion"));

    // DataSetMetaData is the mapper-derived metadata (name, versions, field metadata)
    DataSetMetaDataType metaData =
        assertInstanceOf(DataSetMetaDataType.class, nodeValue(prefix + "/DataSetMetaData"));
    assertEquals(expectedDataSet.getDataSetMetaData(), metaData);
    assertEquals(DATA_SET, metaData.getName());
    assertNotNull(metaData.getFields());
    assertEquals(1, metaData.getFields().length);
    assertEquals("value", metaData.getFields()[0].getName());

    // PublishedData carries one PublishedVariableDataType per node-backed field
    PublishedVariableDataType[] publishedData =
        (PublishedVariableDataType[]) nodeValue(prefix + "/PublishedData");
    assertArrayEquals(
        ((PublishedDataItemsDataType) expectedDataSet.getDataSetSource()).getPublishedData(),
        publishedData);
    assertEquals(1, publishedData.length);
    assertEquals(sourceNodeId, publishedData[0].getPublishedVariable());

    // no DataSetClassId was configured (zero Guid): the Optional property is omitted
    assertTrue(
        testServer
            .getServer()
            .getAddressSpaceManager()
            .getManagedNode(fragmentNodeId(prefix + "/DataSetClassId"))
            .isEmpty(),
        "DataSetClassId property must be omitted for a zero Guid");
  }

  @Test
  void dataSetReaderValuesArePopulated() {
    String prefix = "PubSub/" + CONNECTION + "/" + READER_GROUP + "/" + READER;
    DataSetReaderDataType expectedReader = expectedDataSetReader();

    assertEquals(ushort(4741), nodeValue(prefix + "/PublisherId"));
    assertEquals(GROUP_ID, nodeValue(prefix + "/WriterGroupId"));
    assertEquals(WRITER_ID, nodeValue(prefix + "/DataSetWriterId"));
    assertEquals(5000.0, nodeValue(prefix + "/MessageReceiveTimeout"));
    assertEquals(expectedReader.getKeyFrameCount(), nodeValue(prefix + "/KeyFrameCount"));
    assertEquals("", nodeValue(prefix + "/HeaderLayoutUri"));

    // mapper-normalized: there is no reader-side field content mask config slot
    assertEquals(
        expectedReader.getDataSetFieldContentMask(),
        nodeValue(prefix + "/DataSetFieldContentMask"));

    // the configured metadata shows on the reader node
    DataSetMetaDataType metaData =
        assertInstanceOf(DataSetMetaDataType.class, nodeValue(prefix + "/DataSetMetaData"));
    assertEquals(expectedReader.getDataSetMetaData(), metaData);
    assertEquals(DATA_SET, metaData.getName());

    // security is not configured: the Optional SecurityMode property is omitted
    assertTrue(
        testServer
            .getServer()
            .getAddressSpaceManager()
            .getManagedNode(fragmentNodeId(prefix + "/SecurityMode"))
            .isEmpty(),
        "reader SecurityMode property must be omitted when security mode is None");

    // SubscribedDataSet/TargetVariables carries the FieldTargetDataType mappings
    FieldTargetDataType[] targetVariables =
        (FieldTargetDataType[]) nodeValue(prefix + "/SubscribedDataSet/TargetVariables");
    assertArrayEquals(
        ((TargetVariablesDataType) expectedReader.getSubscribedDataSet()).getTargetVariables(),
        targetVariables);
    assertEquals(1, targetVariables.length);
    assertEquals(targetNodeId, targetVariables[0].getTargetNodeId());
  }

  @Test
  void internalReadOfStructValuedPropertiesReturnsWellFormedDataValues() {
    // WP-L open issue: verify a ReadContext/AddressSpaceManager.read round-trip of a
    // struct-valued property served by the fragment
    NodeId metaDataNodeId =
        fragmentNodeId("PubSub/PublishedDataSets/" + DATA_SET + "/DataSetMetaData");
    NodeId targetVariablesNodeId =
        fragmentNodeId(
            "PubSub/"
                + CONNECTION
                + "/"
                + READER_GROUP
                + "/"
                + READER
                + "/SubscribedDataSet/TargetVariables");

    var readContext = new ReadContext(testServer.getServer(), null);

    List<DataValue> values =
        testServer
            .getServer()
            .getAddressSpaceManager()
            .read(
                readContext,
                0.0,
                TimestampsToReturn.Both,
                List.of(
                    new ReadValueId(
                        metaDataNodeId, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE),
                    new ReadValueId(
                        targetVariablesNodeId,
                        AttributeId.Value.uid(),
                        null,
                        QualifiedName.NULL_VALUE)));

    assertEquals(2, values.size());

    DataValue metaDataValue = values.get(0);
    StatusCode metaDataStatus = metaDataValue.statusCode();
    assertTrue(
        metaDataStatus == null || metaDataStatus.isGood(),
        "DataSetMetaData read status: " + metaDataStatus);
    DataSetMetaDataType metaData =
        assertInstanceOf(DataSetMetaDataType.class, metaDataValue.value().value());
    assertEquals(expectedPublishedDataSet().getDataSetMetaData(), metaData);

    DataValue targetVariablesValue = values.get(1);
    StatusCode targetVariablesStatus = targetVariablesValue.statusCode();
    assertTrue(
        targetVariablesStatus == null || targetVariablesStatus.isGood(),
        "TargetVariables read status: " + targetVariablesStatus);
    FieldTargetDataType[] targetVariables =
        assertInstanceOf(FieldTargetDataType[].class, targetVariablesValue.value().value());
    assertEquals(1, targetVariables.length);
    assertEquals(targetNodeId, targetVariables[0].getTargetNodeId());
  }

  // region fixtures

  /**
   * The pinned exposure shape: one UDP connection with one writer group / writer publishing one
   * node-backed dataset and one reader group / reader with TargetVariables, on unicast loopback
   * with an explicit loopback discoveryAddress.
   */
  private static PubSubConfig fullConfig() throws SocketException {
    var fieldId = new UUID(0L, 0x81L);

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder(DATA_SET)
            .configurationVersion(uint(3), uint(9))
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
                .address(UdpDatagramAddress.unicast("127.0.0.1", dataPort))
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
                                .fieldContentMask(FIELD_MASK)
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
                                .messageReceiveTimeout(Duration.ofSeconds(5))
                                .subscribedDataSet(targetVariables)
                                .build())
                        .build())
                .build())
        .build();
  }

  private static NodeFieldAddress nodeAddress(NodeId nodeId) {
    return NodeFieldAddress.of(
        nodeId, AttributeId.Value, testServer.getServer().getNamespaceTable());
  }

  private static PubSubConnectionDataType expectedConnection() {
    return expected.getConnections()[0];
  }

  private static WriterGroupDataType expectedWriterGroup() {
    return expectedConnection().getWriterGroups()[0];
  }

  private static DataSetWriterDataType expectedDataSetWriter() {
    return expectedWriterGroup().getDataSetWriters()[0];
  }

  private static DataSetReaderDataType expectedDataSetReader() {
    return expectedConnection().getReaderGroups()[0].getDataSetReaders()[0];
  }

  private static PublishedDataSetDataType expectedPublishedDataSet() {
    return expected.getPublishedDataSets()[0];
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

  /** The unwrapped Variant value of the fragment variable node with {@code identifier}. */
  private static Object nodeValue(String identifier) {
    UaNode node = managedNode(fragmentNodeId(identifier));
    return ((UaVariableNode) node).getValue().value().value();
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

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
