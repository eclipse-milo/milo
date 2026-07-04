/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.OverrideValueHandling;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerConnectionTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerDataSetWriterTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerWriterGroupTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldFlags;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramConnectionTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramWriterGroupTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressUrlDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubKeyPushTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataItemsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedEventsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SecurityGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscribedDataSetMirrorDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TargetVariablesDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetReaderMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetWriterMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpWriterGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupMessageDataType;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@code PubSubConfig.fromDataType} against hand-built {@code
 * PubSubConfiguration2DataType} instances: null tolerance, rejection of unsupported shapes,
 * namespace index to URI conversion, raw hatch preservation of non-representable settings, and
 * datatype-to-authored-to-datatype structural fidelity for canonical configurations.
 */
class PubSubConfigMapperFromDataTypeTest {

  private static final String URI_1 = "urn:milo:test:ns1";
  private static final String URI_2 = "urn:milo:test:ns2";

  private static final String PROFILE_UDP_UADP =
      "http://opcfoundation.org/UA-Profile/Transport/pubsub-udp-uadp";
  private static final String PROFILE_MQTT_UADP =
      "http://opcfoundation.org/UA-Profile/Transport/pubsub-mqtt-uadp";

  private static final UUID NULL_UUID = new UUID(0L, 0L);
  private static final UUID PUB_FIELD_ID = new UUID(0xD1L, 1L);
  private static final UUID READER_FIELD_ID = new UUID(0xD2L, 1L);

  /** The Part 14 Annex A.2.2 "UADP-Dynamic" network message content mask. */
  private static final UadpNetworkMessageContentMask NM_DYNAMIC =
      UadpNetworkMessageContentMask.of(
          UadpNetworkMessageContentMask.Field.PublisherId,
          UadpNetworkMessageContentMask.Field.PayloadHeader);

  /** The Part 14 Annex A.2.2 "UADP-Dynamic" DataSetMessage content mask. */
  private static final UadpDataSetMessageContentMask DSM_DYNAMIC =
      UadpDataSetMessageContentMask.of(
          UadpDataSetMessageContentMask.Field.Timestamp,
          UadpDataSetMessageContentMask.Field.Status,
          UadpDataSetMessageContentMask.Field.MinorVersion,
          UadpDataSetMessageContentMask.Field.SequenceNumber);

  private static NamespaceTable table() {
    return new NamespaceTable(URI_1, URI_2);
  }

  private static UaStructuredType decode(ExtensionObject xo) {
    return xo.decode(new DefaultEncodingContext());
  }

  // region Wire fixture helpers (canonical shapes, matching what toDataType emits)

  private static PubSubConfiguration2DataType wireConfig(
      PublishedDataSetDataType[] publishedDataSets, PubSubConnectionDataType[] connections) {

    return wireConfig(publishedDataSets, connections, null);
  }

  private static PubSubConfiguration2DataType wireConfig(
      PublishedDataSetDataType[] publishedDataSets,
      PubSubConnectionDataType[] connections,
      SecurityGroupDataType[] securityGroups) {

    return new PubSubConfiguration2DataType(
        publishedDataSets,
        connections,
        true,
        null,
        null,
        null,
        securityGroups,
        null,
        uint(0),
        null);
  }

  private static PublishedDataSetDataType wirePublishedDataSet(String name, UUID fieldId) {
    return wirePublishedDataSet(name, fieldId, new NodeId(1, "n1"));
  }

  private static PublishedDataSetDataType wirePublishedDataSet(
      String name, UUID fieldId, NodeId publishedVariable) {

    DataSetMetaDataType metaData =
        new DataSetMetaDataType(
            null,
            null,
            null,
            null,
            name,
            LocalizedText.NULL_VALUE,
            new FieldMetaData[] {
              new FieldMetaData(
                  "f1",
                  LocalizedText.NULL_VALUE,
                  DataSetFieldFlags.of(),
                  ubyte(11),
                  NodeIds.Double,
                  -1,
                  null,
                  uint(0),
                  fieldId,
                  null)
            },
            NULL_UUID,
            new ConfigurationVersionDataType(uint(1), uint(1)));

    PublishedVariableDataType publishedData =
        new PublishedVariableDataType(
            publishedVariable, uint(13), 0.0, uint(0), 0.0, null, Variant.ofNull(), null);

    return new PublishedDataSetDataType(
        name,
        null,
        metaData,
        null,
        new PublishedDataItemsDataType(new PublishedVariableDataType[] {publishedData}));
  }

  private static PubSubConnectionDataType wireUdpConnection(
      String name,
      Variant publisherId,
      WriterGroupDataType[] writerGroups,
      ReaderGroupDataType[] readerGroups) {

    return new PubSubConnectionDataType(
        name,
        true,
        publisherId,
        PROFILE_UDP_UADP,
        new NetworkAddressUrlDataType(null, "opc.udp://127.0.0.1:4840"),
        null,
        new DatagramConnectionTransportDataType(new NetworkAddressUrlDataType(null, null)),
        writerGroups,
        readerGroups);
  }

  private static WriterGroupDataType wireWriterGroup(
      String name,
      Double publishingInterval,
      MessageSecurityMode securityMode,
      String securityGroupId,
      WriterGroupMessageDataType messageSettings,
      DataSetWriterDataType[] dataSetWriters) {

    return new WriterGroupDataType(
        name,
        true,
        securityMode,
        securityGroupId,
        null,
        uint(1400),
        null,
        ushort(1),
        publishingInterval,
        0.0,
        ubyte(0),
        null,
        null,
        new DatagramWriterGroupTransportDataType(ubyte(0), 0.0),
        messageSettings,
        dataSetWriters);
  }

  private static DataSetWriterDataType wireWriter(String name, String dataSetName) {
    return new DataSetWriterDataType(
        name,
        true,
        ushort(11),
        DataSetFieldContentMask.of(),
        uint(1),
        dataSetName,
        null,
        null,
        canonicalUadpWriterMessage());
  }

  private static ReaderGroupDataType wireReaderGroup(
      String name, DataSetReaderDataType... readers) {

    return new ReaderGroupDataType(
        name,
        true,
        MessageSecurityMode.None,
        null,
        null,
        uint(0),
        null,
        null,
        null,
        readers.length == 0 ? null : readers);
  }

  private static DataSetReaderDataType wireReader(
      String name,
      DataSetMetaDataType metaData,
      DataSetReaderMessageDataType messageSettings,
      SubscribedDataSetDataType subscribedDataSet) {

    // Invalid is the §6.2.9.9 "no reader-level override" sentinel and the shape toDataType
    // emits for override-less readers.
    return wireReader(
        name,
        MessageSecurityMode.Invalid,
        null,
        null,
        metaData,
        messageSettings,
        subscribedDataSet);
  }

  private static DataSetReaderDataType wireReader(
      String name,
      MessageSecurityMode securityMode,
      String securityGroupId,
      EndpointDescription[] securityKeyServices,
      DataSetMetaDataType metaData,
      DataSetReaderMessageDataType messageSettings,
      SubscribedDataSetDataType subscribedDataSet) {

    return new DataSetReaderDataType(
        name,
        true,
        Variant.ofNull(),
        ushort(0),
        ushort(0),
        metaData,
        DataSetFieldContentMask.of(),
        0.0,
        uint(0),
        null,
        securityMode,
        securityGroupId,
        securityKeyServices,
        null,
        null,
        messageSettings,
        subscribedDataSet);
  }

  private static EndpointDescription keyServiceEndpoint() {
    return new EndpointDescription(
        "opc.tcp://sks.example:4840",
        new ApplicationDescription(
            "urn:sks",
            "urn:sks:product",
            LocalizedText.english("SKS"),
            ApplicationType.Server,
            null,
            null,
            null),
        ByteString.NULL_VALUE,
        MessageSecurityMode.SignAndEncrypt,
        "http://opcfoundation.org/UA/SecurityPolicy#Basic256Sha256",
        null,
        null,
        ubyte(0));
  }

  /** The shape emitted for "reader has no configured metadata". */
  private static DataSetMetaDataType emptyWireMetaData() {
    return new DataSetMetaDataType(
        null,
        null,
        null,
        null,
        null,
        LocalizedText.NULL_VALUE,
        null,
        NULL_UUID,
        new ConfigurationVersionDataType(uint(0), uint(0)));
  }

  private static UadpWriterGroupMessageDataType canonicalUadpGroupMessage() {
    return new UadpWriterGroupMessageDataType(
        uint(0), DataSetOrderingType.Undefined, NM_DYNAMIC, 0.0, null);
  }

  private static UadpDataSetWriterMessageDataType canonicalUadpWriterMessage() {
    return new UadpDataSetWriterMessageDataType(DSM_DYNAMIC, ushort(0), ushort(0), ushort(0));
  }

  private static UadpDataSetReaderMessageDataType canonicalUadpReaderMessage() {
    return new UadpDataSetReaderMessageDataType(
        uint(0), ushort(0), ushort(0), NULL_UUID, NM_DYNAMIC, DSM_DYNAMIC, 0.0, 0.0, 0.0);
  }

  /** A single-reader, reader-only UDP configuration wrapping {@code reader}. */
  private static PubSubConfiguration2DataType readerOnlyConfig(
      DataSetReaderDataType reader, SecurityGroupDataType... securityGroups) {

    return wireConfig(
        null,
        new PubSubConnectionDataType[] {
          wireUdpConnection(
              "c",
              Variant.ofNull(),
              null,
              new ReaderGroupDataType[] {wireReaderGroup("rg", reader)})
        },
        securityGroups.length == 0 ? null : securityGroups);
  }

  private static DataSetReaderConfig onlyReader(PubSubConfig config) {
    return config.connection("c").orElseThrow().readerGroups().get(0).getDataSetReaders().get(0);
  }

  // endregion

  // region Null tolerance

  @Test
  void minimalAllNullConfigurationIsTolerated() {
    PubSubConfiguration2DataType dataType =
        new PubSubConfiguration2DataType(
            null, null, null, null, null, null, null, null, uint(0), null);

    PubSubConfig config = PubSubConfig.fromDataType(dataType, table());

    assertTrue(config.isEnabled());
    assertTrue(config.connections().isEmpty());
    assertTrue(config.publishedDataSets().isEmpty());
    assertTrue(config.standaloneSubscribedDataSets().isEmpty());
    assertTrue(config.securityGroups().isEmpty());
    assertTrue(config.properties().isEmpty());
  }

  /**
   * Documented mapper loss: {@code pubSubKeyPushTargets} is a datatype field with no config slot —
   * a configuration carrying push targets maps WITHOUT throwing, the targets are dropped, and the
   * reverse mapping emits none (push-model distribution is CUT; see the PubSubConfigMapper
   * "Documented losses" contract).
   */
  @Test
  void pubSubKeyPushTargetsAreDroppedNotRejected() {
    var pushTarget =
        new PubSubKeyPushTargetDataType(
            "urn:milo:test:push-target",
            new String[] {"folder"},
            "opc.tcp://push-target:4840",
            null,
            new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null),
            ushort(3),
            1000.0,
            null,
            new String[] {"SG1"});

    PubSubConfiguration2DataType dataType =
        new PubSubConfiguration2DataType(
            null,
            null,
            true,
            null,
            null,
            null,
            null,
            new PubSubKeyPushTargetDataType[] {pushTarget},
            uint(0),
            null);

    PubSubConfig config = PubSubConfig.fromDataType(dataType, table());

    // the rest of the (empty) configuration mapped normally
    assertTrue(config.isEnabled());
    assertTrue(config.connections().isEmpty());
    assertTrue(config.securityGroups().isEmpty());

    // and the drop is total: the reverse mapping emits no push targets
    PubSubKeyPushTargetDataType[] emitted = config.toDataType(table()).getPubSubKeyPushTargets();
    assertTrue(emitted == null || emitted.length == 0);
  }

  @Test
  void nullArraysAndNullScalarsAreTolerated() {
    DataSetWriterDataType writer =
        new DataSetWriterDataType("w", null, ushort(1), null, null, "pds", null, null, null);

    WriterGroupDataType writerGroup =
        new WriterGroupDataType(
            "wg",
            null,
            null,
            null,
            null,
            null,
            null,
            ushort(1),
            1000.0,
            null,
            null,
            null,
            null,
            null,
            null,
            new DataSetWriterDataType[] {writer, null});

    DataSetReaderDataType reader =
        new DataSetReaderDataType(
            "r",
            null,
            Variant.ofNull(),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

    ReaderGroupDataType readerGroup =
        new ReaderGroupDataType(
            "rg",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            new DataSetReaderDataType[] {reader, null});

    // No transportProfileUri: the connection type is sniffed from the address scheme.
    PubSubConnectionDataType connection =
        new PubSubConnectionDataType(
            "c",
            null,
            Variant.ofByte(ubyte(1)),
            null,
            new NetworkAddressUrlDataType(null, "opc.udp://127.0.0.1:4840"),
            null,
            null,
            new WriterGroupDataType[] {writerGroup},
            new ReaderGroupDataType[] {readerGroup});

    PublishedDataSetDataType publishedDataSet =
        new PublishedDataSetDataType("pds", null, null, null, null);

    StandaloneSubscribedDataSetDataType standalone =
        new StandaloneSubscribedDataSetDataType(
            "sds",
            null,
            new DataSetMetaDataType(
                null, null, null, null, null, LocalizedText.NULL_VALUE, null, null, null),
            new TargetVariablesDataType(null));

    SecurityGroupDataType securityGroup =
        new SecurityGroupDataType("sg", null, null, null, null, null, null, null, null);

    PubSubConfiguration2DataType dataType =
        new PubSubConfiguration2DataType(
            new PublishedDataSetDataType[] {publishedDataSet, null},
            new PubSubConnectionDataType[] {connection, null},
            null,
            new StandaloneSubscribedDataSetDataType[] {standalone},
            null,
            null,
            new SecurityGroupDataType[] {securityGroup},
            null,
            uint(0),
            null);

    PubSubConfig config = PubSubConfig.fromDataType(dataType, table());

    assertTrue(config.isEnabled());
    assertEquals(1, config.connections().size());

    UdpConnectionConfig udp =
        assertInstanceOf(UdpConnectionConfig.class, config.connection("c").orElseThrow());
    assertTrue(udp.enabled());
    assertEquals(PublisherId.ubyte(ubyte(1)), udp.publisherId());
    assertEquals(UdpDatagramAddress.unicast("127.0.0.1", 4840), udp.getAddress());
    assertNull(udp.rawTransportSettings());

    WriterGroupConfig wg = udp.writerGroups().get(0);
    assertEquals("wg", wg.getName());
    assertTrue(wg.isEnabled());
    assertEquals(ushort(1), wg.getWriterGroupId());
    assertEquals(Duration.ofMillis(1000), wg.getPublishingInterval());
    assertNull(wg.getKeepAliveTime());
    assertEquals(ubyte(0), wg.getPriority());
    // a null wire MaxNetworkMessageSize keeps the builder default: 0 = no enforced limit
    assertEquals(uint(0), wg.getMaxNetworkMessageSize());
    assertNull(wg.getMessageSecurity());
    assertEquals(UadpWriterGroupSettings.builder().build(), wg.getMessageSettings());
    assertEquals(1, wg.getDataSetWriters().size());

    DataSetWriterConfig w = wg.getDataSetWriters().get(0);
    assertEquals("w", w.getName());
    assertEquals(new PublishedDataSetRef("pds"), w.getDataSet());
    assertEquals(ushort(1), w.getDataSetWriterId());
    assertEquals(uint(1), w.getKeyFrameCount());
    assertEquals(DataSetFieldContentMask.of(), w.getFieldContentMask());
    assertEquals(UadpDataSetWriterSettings.builder().build(), w.getSettings());

    ReaderGroupConfig rg = udp.readerGroups().get(0);
    assertEquals("rg", rg.getName());
    assertEquals(1, rg.getDataSetReaders().size());

    DataSetReaderConfig r = rg.getDataSetReaders().get(0);
    assertEquals("r", r.getName());
    assertNull(r.getPublisherId());
    assertNull(r.getWriterGroupId());
    assertNull(r.getDataSetWriterId());
    assertNull(r.getDataSetMetaData());
    assertNull(r.getSubscribedDataSet());
    // a null reader securityMode maps to "no override"
    assertNull(r.getMessageSecurity());
    assertEquals(Duration.ZERO, r.getMessageReceiveTimeout());
    assertEquals(uint(0), r.getKeyFrameCount());
    assertEquals(UadpDataSetReaderSettings.builder().build(), r.getSettings());

    PublishedDataSetConfig pds = config.publishedDataSet("pds").orElseThrow();
    assertTrue(pds.getFields().isEmpty());
    assertEquals(uint(1), pds.getConfigurationVersionMajor());
    assertEquals(uint(1), pds.getConfigurationVersionMinor());

    StandaloneSubscribedDataSetConfig sds = config.standaloneSubscribedDataSets().get(0);
    assertEquals("sds", sds.getName());
    // The wire metadata had no name; the standalone dataset name is the fallback.
    assertEquals("sds", sds.getMetaData().getName());
    assertTrue(sds.getMetaData().fields().isEmpty());
    assertEquals(TargetVariablesConfig.builder().build(), sds.getSubscribedDataSet());

    SecurityGroupConfig sg = config.securityGroups().get(0);
    assertEquals("sg", sg.getName());
    assertEquals("sg", sg.getSecurityGroupId());
    assertEquals(Duration.ofHours(1), sg.getKeyLifeTime());
  }

  @Test
  void nullElementsInSecurityArraysAreSkipped() {
    RolePermissionType rolePermission =
        new RolePermissionType(
            NodeIds.WellKnownRole_SecurityAdmin, PermissionType.of(PermissionType.Field.Call));

    SecurityGroupDataType securityGroup =
        new SecurityGroupDataType(
            "sg",
            new String[] {"folder-a", null, "folder-b"},
            null,
            null,
            null,
            null,
            null,
            new RolePermissionType[] {rolePermission, null},
            null);

    PubSubConfiguration2DataType dataType =
        new PubSubConfiguration2DataType(
            null,
            null,
            null,
            null,
            null,
            new EndpointDescription[] {keyServiceEndpoint(), null},
            new SecurityGroupDataType[] {securityGroup},
            null,
            uint(0),
            null);

    PubSubConfig config = PubSubConfig.fromDataType(dataType, table());

    SecurityGroupConfig sg = config.securityGroups().get(0);
    assertEquals(List.of("folder-a", "folder-b"), sg.getSecurityGroupFolder());
    assertEquals(List.of(rolePermission), sg.getRolePermissions());
    assertEquals(List.of(keyServiceEndpoint()), config.defaultSecurityKeyServices());
  }

  @Test
  void nullElementsInGroupKeyServicesAreSkipped() {
    SecurityGroupDataType securityGroup =
        new SecurityGroupDataType("sg-1", null, 60000.0, null, null, null, "SG-001", null, null);

    // wireWriterGroup has no key services slot; build the group inline to plant the null element.
    WriterGroupDataType writerGroup =
        new WriterGroupDataType(
            "wg",
            true,
            MessageSecurityMode.Sign,
            "SG-001",
            new EndpointDescription[] {keyServiceEndpoint(), null},
            uint(1400),
            null,
            ushort(1),
            1000.0,
            0.0,
            ubyte(0),
            null,
            null,
            new DatagramWriterGroupTransportDataType(ubyte(0), 0.0),
            canonicalUadpGroupMessage(),
            new DataSetWriterDataType[] {wireWriter("w", "ds1")});

    PubSubConfiguration2DataType dataType =
        wireConfig(
            new PublishedDataSetDataType[] {wirePublishedDataSet("ds1", PUB_FIELD_ID)},
            new PubSubConnectionDataType[] {
              wireUdpConnection(
                  "c", Variant.ofUInt16(ushort(9)), new WriterGroupDataType[] {writerGroup}, null)
            },
            new SecurityGroupDataType[] {securityGroup});

    PubSubConfig config = PubSubConfig.fromDataType(dataType, table());

    MessageSecurityConfig security =
        config.connection("c").orElseThrow().writerGroups().get(0).getMessageSecurity();
    assertNotNull(security);
    assertEquals(List.of(keyServiceEndpoint()), security.getKeyServices());
  }

  // endregion

  // region Rejected shapes

  @Test
  void unknownTransportProfileThrows() {
    PubSubConnectionDataType connection =
        new PubSubConnectionDataType(
            "c",
            true,
            Variant.ofNull(),
            "http://opcfoundation.org/UA-Profile/Transport/pubsub-amqp-uadp",
            new NetworkAddressUrlDataType(null, "amqp://broker.example:5672"),
            null,
            null,
            null,
            null);

    PubSubConfiguration2DataType dataType =
        wireConfig(null, new PubSubConnectionDataType[] {connection});

    assertThrows(
        PubSubConfigValidationException.class, () -> PubSubConfig.fromDataType(dataType, table()));
  }

  @Test
  void missingAddressUrlThrows() {
    PubSubConnectionDataType connection =
        new PubSubConnectionDataType(
            "c",
            true,
            Variant.ofNull(),
            PROFILE_UDP_UADP,
            new NetworkAddressUrlDataType(null, null),
            null,
            null,
            null,
            null);

    PubSubConfiguration2DataType dataType =
        wireConfig(null, new PubSubConnectionDataType[] {connection});

    assertThrows(
        PubSubConfigValidationException.class, () -> PubSubConfig.fromDataType(dataType, table()));
  }

  @Test
  void nonPublishedDataItemsSourceThrows() {
    PublishedDataSetDataType publishedDataSet =
        new PublishedDataSetDataType(
            "events",
            null,
            null,
            null,
            new PublishedEventsDataType(new NodeId(0, 0), null, new ContentFilter(null)));

    PubSubConfiguration2DataType dataType =
        wireConfig(new PublishedDataSetDataType[] {publishedDataSet}, null);

    assertThrows(
        PubSubConfigValidationException.class, () -> PubSubConfig.fromDataType(dataType, table()));
  }

  @Test
  void subscribedDataSetMirrorThrows() {
    DataSetReaderDataType reader =
        wireReader(
            "r",
            emptyWireMetaData(),
            canonicalUadpReaderMessage(),
            new SubscribedDataSetMirrorDataType("parent", null));

    PubSubConfiguration2DataType dataType =
        wireConfig(
            null,
            new PubSubConnectionDataType[] {
              wireUdpConnection(
                  "c",
                  Variant.ofNull(),
                  null,
                  new ReaderGroupDataType[] {wireReaderGroup("rg", reader)})
            });

    assertThrows(
        PubSubConfigValidationException.class, () -> PubSubConfig.fromDataType(dataType, table()));
  }

  @Test
  void inlineStandaloneSubscribedDataSetOnReaderThrows() {
    DataSetReaderDataType reader =
        wireReader(
            "r",
            emptyWireMetaData(),
            canonicalUadpReaderMessage(),
            new StandaloneSubscribedDataSetDataType(
                "inline", null, emptyWireMetaData(), new TargetVariablesDataType(null)));

    PubSubConfiguration2DataType dataType =
        wireConfig(
            null,
            new PubSubConnectionDataType[] {
              wireUdpConnection(
                  "c",
                  Variant.ofNull(),
                  null,
                  new ReaderGroupDataType[] {wireReaderGroup("rg", reader)})
            });

    assertThrows(
        PubSubConfigValidationException.class, () -> PubSubConfig.fromDataType(dataType, table()));
  }

  @Test
  void invalidAttributeIdThrows() {
    FieldTargetDataType target =
        new FieldTargetDataType(
            READER_FIELD_ID,
            null,
            new NodeId(1, "t1"),
            uint(999),
            null,
            OverrideValueHandling.Disabled,
            Variant.ofNull());

    DataSetReaderDataType reader =
        wireReader(
            "r",
            emptyWireMetaData(),
            canonicalUadpReaderMessage(),
            new TargetVariablesDataType(new FieldTargetDataType[] {target}));

    PubSubConfiguration2DataType dataType =
        wireConfig(
            null,
            new PubSubConnectionDataType[] {
              wireUdpConnection(
                  "c",
                  Variant.ofNull(),
                  null,
                  new ReaderGroupDataType[] {wireReaderGroup("rg", reader)})
            });

    assertThrows(
        PubSubConfigValidationException.class, () -> PubSubConfig.fromDataType(dataType, table()));
  }

  @Test
  void nonPositivePublishingIntervalThrows() {
    WriterGroupDataType writerGroup =
        wireWriterGroup(
            "wg",
            0.0,
            MessageSecurityMode.None,
            null,
            canonicalUadpGroupMessage(),
            new DataSetWriterDataType[] {wireWriter("w", "ds1")});

    PubSubConfiguration2DataType dataType =
        wireConfig(
            new PublishedDataSetDataType[] {wirePublishedDataSet("ds1", PUB_FIELD_ID)},
            new PubSubConnectionDataType[] {
              wireUdpConnection(
                  "c", Variant.ofUInt16(ushort(9)), new WriterGroupDataType[] {writerGroup}, null)
            });

    assertThrows(
        PubSubConfigValidationException.class, () -> PubSubConfig.fromDataType(dataType, table()));
  }

  @Test
  void missingDataSetNameThrows() {
    DataSetWriterDataType writer =
        new DataSetWriterDataType(
            "w",
            true,
            ushort(11),
            DataSetFieldContentMask.of(),
            uint(1),
            null,
            null,
            null,
            canonicalUadpWriterMessage());

    WriterGroupDataType writerGroup =
        wireWriterGroup(
            "wg",
            1000.0,
            MessageSecurityMode.None,
            null,
            canonicalUadpGroupMessage(),
            new DataSetWriterDataType[] {writer});

    PubSubConfiguration2DataType dataType =
        wireConfig(
            null,
            new PubSubConnectionDataType[] {
              wireUdpConnection(
                  "c", Variant.ofUInt16(ushort(9)), new WriterGroupDataType[] {writerGroup}, null)
            });

    assertThrows(
        PubSubConfigValidationException.class, () -> PubSubConfig.fromDataType(dataType, table()));
  }

  @Test
  void unsupportedPublisherIdVariantThrows() {
    PubSubConnectionDataType connection =
        new PubSubConnectionDataType(
            "c",
            true,
            Variant.ofInt32(5),
            PROFILE_UDP_UADP,
            new NetworkAddressUrlDataType(null, "opc.udp://127.0.0.1:4840"),
            null,
            null,
            null,
            null);

    PubSubConfiguration2DataType dataType =
        wireConfig(null, new PubSubConnectionDataType[] {connection});

    assertThrows(
        PubSubConfigValidationException.class, () -> PubSubConfig.fromDataType(dataType, table()));
  }

  // endregion

  // region Namespace conversion

  @Test
  void nodeIdNamespaceIndexMapsBackToUriForm() {
    PubSubConfiguration2DataType dataType =
        wireConfig(
            new PublishedDataSetDataType[] {
              wirePublishedDataSet("ds1", PUB_FIELD_ID, new NodeId(2, "X"))
            },
            null);

    PubSubConfig config = PubSubConfig.fromDataType(dataType, table());

    FieldDefinition field = config.publishedDataSet("ds1").orElseThrow().getFields().get(0);
    NodeFieldAddress address = assertInstanceOf(NodeFieldAddress.class, field.getSource());
    assertEquals(ExpandedNodeId.of(URI_2, "X"), address.nodeId());
    assertEquals(AttributeId.Value, address.attributeId());
  }

  @Test
  void nodeIdNamespaceIndexNotInTableThrows() {
    PubSubConfiguration2DataType dataType =
        wireConfig(
            new PublishedDataSetDataType[] {
              wirePublishedDataSet("ds1", PUB_FIELD_ID, new NodeId(7, "X"))
            },
            null);

    assertThrows(
        PubSubConfigValidationException.class, () -> PubSubConfig.fromDataType(dataType, table()));
  }

  // endregion

  // region Non-representable settings preserved in raw hatches

  @Test
  void writerGroupSamplingOffsetPreservedInRawHatch() {
    UadpWriterGroupMessageDataType original =
        new UadpWriterGroupMessageDataType(
            uint(3), DataSetOrderingType.Undefined, NM_DYNAMIC, 12.5, null);

    WriterGroupDataType writerGroup =
        wireWriterGroup(
            "wg",
            1000.0,
            MessageSecurityMode.None,
            null,
            original,
            new DataSetWriterDataType[] {wireWriter("w", "ds1")});

    PubSubConfiguration2DataType dataType =
        wireConfig(
            new PublishedDataSetDataType[] {wirePublishedDataSet("ds1", PUB_FIELD_ID)},
            new PubSubConnectionDataType[] {
              wireUdpConnection(
                  "c", Variant.ofUInt16(ushort(9)), new WriterGroupDataType[] {writerGroup}, null)
            });

    NamespaceTable table = table();
    PubSubConfig config = PubSubConfig.fromDataType(dataType, table);

    WriterGroupConfig wg = config.connection("c").orElseThrow().writerGroups().get(0);
    assertNotNull(wg.getRawMessageSettings());
    assertEquals(original, decode(wg.getRawMessageSettings()));

    // The raw hatch wins on the way back out: full datatype -> authored -> datatype fidelity.
    PubSubConfiguration2DataType reEmitted = config.toDataType(table);
    assertEquals(original, reEmitted.getConnections()[0].getWriterGroups()[0].getMessageSettings());
  }

  @Test
  void readerNonRepresentableMessageSettingsPreservedInRawHatch() {
    UadpDataSetReaderMessageDataType original =
        new UadpDataSetReaderMessageDataType(
            uint(0),
            ushort(0),
            ushort(0),
            new UUID(0xDL, 0xEL),
            NM_DYNAMIC,
            DSM_DYNAMIC,
            0.0,
            250.0,
            0.0);

    DataSetReaderDataType reader =
        wireReader("r", emptyWireMetaData(), original, new TargetVariablesDataType(null));

    PubSubConfiguration2DataType dataType =
        wireConfig(
            null,
            new PubSubConnectionDataType[] {
              wireUdpConnection(
                  "c",
                  Variant.ofNull(),
                  null,
                  new ReaderGroupDataType[] {wireReaderGroup("rg", reader)})
            });

    NamespaceTable table = table();
    PubSubConfig config = PubSubConfig.fromDataType(dataType, table);

    DataSetReaderConfig r =
        config.connection("c").orElseThrow().readerGroups().get(0).getDataSetReaders().get(0);
    assertNotNull(r.getRawMessageSettings());
    assertEquals(original, decode(r.getRawMessageSettings()));
    // The typed settings slot keeps its default; the raw hatch carries the wire shape.
    assertEquals(UadpDataSetReaderSettings.builder().build(), r.getSettings());

    PubSubConfiguration2DataType reEmitted = config.toDataType(table);
    assertEquals(
        original,
        reEmitted.getConnections()[0].getReaderGroups()[0].getDataSetReaders()[0]
            .getMessageSettings());
  }

  // endregion

  // region Security group resolution

  @Test
  void securityGroupIdResolvedBackToGroupName() {
    SecurityGroupDataType securityGroup =
        new SecurityGroupDataType("sg-1", null, 60000.0, null, null, null, "SG-001", null, null);

    WriterGroupDataType writerGroup =
        wireWriterGroup(
            "wg",
            1000.0,
            MessageSecurityMode.Sign,
            "SG-001",
            canonicalUadpGroupMessage(),
            new DataSetWriterDataType[] {wireWriter("w", "ds1")});

    PubSubConfiguration2DataType dataType =
        wireConfig(
            new PublishedDataSetDataType[] {wirePublishedDataSet("ds1", PUB_FIELD_ID)},
            new PubSubConnectionDataType[] {
              wireUdpConnection(
                  "c", Variant.ofUInt16(ushort(9)), new WriterGroupDataType[] {writerGroup}, null)
            },
            new SecurityGroupDataType[] {securityGroup});

    PubSubConfig config = PubSubConfig.fromDataType(dataType, table());

    SecurityGroupConfig sg = config.securityGroups().get(0);
    assertEquals("sg-1", sg.getName());
    assertEquals("SG-001", sg.getSecurityGroupId());
    assertEquals(Duration.ofMinutes(1), sg.getKeyLifeTime());

    MessageSecurityConfig security =
        config.connection("c").orElseThrow().writerGroups().get(0).getMessageSecurity();
    assertNotNull(security);
    assertEquals(MessageSecurityMode.Sign, security.getMode());
    // The wire id "SG-001" resolves back to a ref naming the group "sg-1".
    assertEquals(new SecurityGroupRef("sg-1"), security.getSecurityGroup());
  }

  @Test
  void danglingSecurityGroupIdFailsValidation() {
    WriterGroupDataType writerGroup =
        wireWriterGroup(
            "wg",
            1000.0,
            MessageSecurityMode.Sign,
            "unknown-group",
            canonicalUadpGroupMessage(),
            new DataSetWriterDataType[] {wireWriter("w", "ds1")});

    PubSubConfiguration2DataType dataType =
        wireConfig(
            new PublishedDataSetDataType[] {wirePublishedDataSet("ds1", PUB_FIELD_ID)},
            new PubSubConnectionDataType[] {
              wireUdpConnection(
                  "c", Variant.ofUInt16(ushort(9)), new WriterGroupDataType[] {writerGroup}, null)
            });

    assertThrows(
        PubSubConfigValidationException.class, () -> PubSubConfig.fromDataType(dataType, table()));
  }

  // endregion

  // region Reader-level security override

  @Test
  void invalidReaderSecurityModeMapsToAbsentOverride() {
    DataSetReaderDataType reader =
        wireReader(
            "r",
            MessageSecurityMode.Invalid,
            null,
            null,
            emptyWireMetaData(),
            canonicalUadpReaderMessage(),
            new TargetVariablesDataType(null));

    PubSubConfig config = PubSubConfig.fromDataType(readerOnlyConfig(reader), table());

    assertNull(onlyReader(config).getMessageSecurity());
  }

  @Test
  void legacyNoneReaderSecurityShapeMapsToAbsentOverride() {
    // Earlier Milo versions emitted None/null/null for readers without an override; it must keep
    // mapping to "no override" alongside the Invalid sentinel.
    DataSetReaderDataType reader =
        wireReader(
            "r",
            MessageSecurityMode.None,
            null,
            null,
            emptyWireMetaData(),
            canonicalUadpReaderMessage(),
            new TargetVariablesDataType(null));

    PubSubConfig config = PubSubConfig.fromDataType(readerOnlyConfig(reader), table());

    assertNull(onlyReader(config).getMessageSecurity());
  }

  @Test
  void allNullReaderKeyServicesNormalizeToAbsentOverride() {
    // A securityKeyServices array whose elements are all null normalizes like an empty/null
    // array, so None/null/{null,null} is still the legacy absent shape, not an active override.
    DataSetReaderDataType reader =
        wireReader(
            "r",
            MessageSecurityMode.None,
            null,
            new EndpointDescription[] {null, null},
            emptyWireMetaData(),
            canonicalUadpReaderMessage(),
            new TargetVariablesDataType(null));

    PubSubConfig config = PubSubConfig.fromDataType(readerOnlyConfig(reader), table());

    assertNull(onlyReader(config).getMessageSecurity());
  }

  @Test
  void readerOverrideToNoneWithGroupIdIsPreserved() {
    SecurityGroupDataType securityGroup =
        new SecurityGroupDataType("sg-1", null, 60000.0, null, null, null, "SG-001", null, null);

    // None WITH a group id is a legal active override to unsecured operation, not the legacy
    // all-default absent shape.
    DataSetReaderDataType reader =
        wireReader(
            "r",
            MessageSecurityMode.None,
            "SG-001",
            null,
            emptyWireMetaData(),
            canonicalUadpReaderMessage(),
            new TargetVariablesDataType(null));

    PubSubConfig config =
        PubSubConfig.fromDataType(readerOnlyConfig(reader, securityGroup), table());

    MessageSecurityConfig security = onlyReader(config).getMessageSecurity();
    assertNotNull(security);
    assertEquals(MessageSecurityMode.None, security.getMode());
    assertEquals(new SecurityGroupRef("sg-1"), security.getSecurityGroup());
    assertTrue(security.getKeyServices().isEmpty());
  }

  @Test
  void readerSecurityGroupIdResolvedBackToGroupName() {
    SecurityGroupDataType securityGroup =
        new SecurityGroupDataType("sg-1", null, 60000.0, null, null, null, "SG-001", null, null);

    DataSetReaderDataType reader =
        wireReader(
            "r",
            MessageSecurityMode.Sign,
            "SG-001",
            new EndpointDescription[] {keyServiceEndpoint()},
            emptyWireMetaData(),
            canonicalUadpReaderMessage(),
            new TargetVariablesDataType(null));

    PubSubConfig config =
        PubSubConfig.fromDataType(readerOnlyConfig(reader, securityGroup), table());

    MessageSecurityConfig security = onlyReader(config).getMessageSecurity();
    assertNotNull(security);
    assertEquals(MessageSecurityMode.Sign, security.getMode());
    // The wire id "SG-001" resolves back to a ref naming the group "sg-1".
    assertEquals(new SecurityGroupRef("sg-1"), security.getSecurityGroup());
    assertEquals(List.of(keyServiceEndpoint()), security.getKeyServices());
  }

  @Test
  void danglingReaderSecurityGroupIdFailsValidation() {
    DataSetReaderDataType reader =
        wireReader(
            "r",
            MessageSecurityMode.Sign,
            "unknown-group",
            null,
            emptyWireMetaData(),
            canonicalUadpReaderMessage(),
            new TargetVariablesDataType(null));

    PubSubConfiguration2DataType dataType = readerOnlyConfig(reader);

    assertThrows(
        PubSubConfigValidationException.class, () -> PubSubConfig.fromDataType(dataType, table()));
  }

  // endregion

  // region Structural fidelity for hand-built canonical configurations

  @Test
  void canonicalUdpConfigurationHasStructuralFidelity() {
    PublishedDataSetDataType ds1 = wirePublishedDataSet("ds1", PUB_FIELD_ID);

    WriterGroupDataType wg1 =
        wireWriterGroup(
            "wg1",
            1000.0,
            MessageSecurityMode.None,
            null,
            canonicalUadpGroupMessage(),
            new DataSetWriterDataType[] {wireWriter("w1", "ds1")});

    DataSetMetaDataType readerMetaData =
        new DataSetMetaDataType(
            null,
            null,
            null,
            null,
            "md1",
            LocalizedText.NULL_VALUE,
            new FieldMetaData[] {
              new FieldMetaData(
                  "g1",
                  LocalizedText.NULL_VALUE,
                  DataSetFieldFlags.of(),
                  ubyte(11),
                  NodeIds.Double,
                  -1,
                  null,
                  uint(0),
                  READER_FIELD_ID,
                  null)
            },
            NULL_UUID,
            new ConfigurationVersionDataType(uint(1), uint(1)));

    FieldTargetDataType target =
        new FieldTargetDataType(
            READER_FIELD_ID,
            null,
            new NodeId(1, "t1"),
            uint(13),
            null,
            OverrideValueHandling.Disabled,
            Variant.ofNull());

    DataSetReaderDataType r1 =
        wireReader(
            "r1",
            readerMetaData,
            canonicalUadpReaderMessage(),
            new TargetVariablesDataType(new FieldTargetDataType[] {target}));

    PubSubConfiguration2DataType dataType1 =
        wireConfig(
            new PublishedDataSetDataType[] {ds1},
            new PubSubConnectionDataType[] {
              wireUdpConnection(
                  "c1",
                  Variant.ofUInt16(ushort(9)),
                  new WriterGroupDataType[] {wg1},
                  new ReaderGroupDataType[] {wireReaderGroup("rg1", r1)})
            });

    NamespaceTable table = table();
    PubSubConfig config = PubSubConfig.fromDataType(dataType1, table);
    PubSubConfiguration2DataType dataType2 = config.toDataType(table);

    assertEquals(dataType1.getPublishedDataSets()[0], dataType2.getPublishedDataSets()[0]);
    assertEquals(
        dataType1.getConnections()[0].getWriterGroups()[0],
        dataType2.getConnections()[0].getWriterGroups()[0]);
    assertEquals(
        dataType1.getConnections()[0].getReaderGroups()[0],
        dataType2.getConnections()[0].getReaderGroups()[0]);
    assertEquals(dataType1.getConnections()[0], dataType2.getConnections()[0]);
    assertEquals(dataType1, dataType2);
  }

  @Test
  void canonicalMqttConfigurationHasStructuralFidelity() {
    PublishedDataSetDataType ds1 = wirePublishedDataSet("ds1", PUB_FIELD_ID);

    DataSetWriterDataType writer =
        new DataSetWriterDataType(
            "w1",
            true,
            ushort(1),
            DataSetFieldContentMask.of(),
            uint(1),
            "ds1",
            null,
            new BrokerDataSetWriterTransportDataType(
                null, null, null, BrokerTransportQualityOfService.NotSpecified, null, 0.0),
            canonicalUadpWriterMessage());

    WriterGroupDataType writerGroup =
        new WriterGroupDataType(
            "wg1",
            true,
            MessageSecurityMode.None,
            null,
            null,
            uint(1400),
            null,
            ushort(1),
            1000.0,
            0.0,
            ubyte(0),
            null,
            null,
            new BrokerWriterGroupTransportDataType(
                null, null, null, BrokerTransportQualityOfService.NotSpecified),
            canonicalUadpGroupMessage(),
            new DataSetWriterDataType[] {writer});

    PubSubConnectionDataType connection =
        new PubSubConnectionDataType(
            "m1",
            true,
            Variant.ofString("pub"),
            PROFILE_MQTT_UADP,
            new NetworkAddressUrlDataType(null, "mqtt://broker.example:1883"),
            null,
            new BrokerConnectionTransportDataType(null, null),
            new WriterGroupDataType[] {writerGroup},
            null);

    PubSubConfiguration2DataType dataType1 =
        wireConfig(
            new PublishedDataSetDataType[] {ds1}, new PubSubConnectionDataType[] {connection});

    NamespaceTable table = table();
    PubSubConfig config = PubSubConfig.fromDataType(dataType1, table);

    // All-default broker transport shapes collapse to "absent" in the authored model...
    MqttConnectionConfig mqtt =
        assertInstanceOf(MqttConnectionConfig.class, config.connection("m1").orElseThrow());
    assertNull(mqtt.rawTransportSettings());
    WriterGroupConfig wg = mqtt.writerGroups().get(0);
    assertNull(wg.getBrokerTransport());
    assertNull(wg.getRawTransportSettings());
    assertNull(wg.getDataSetWriters().get(0).getBrokerTransport());

    // ...and are re-emitted as the same canonical empty structures.
    PubSubConfiguration2DataType dataType2 = config.toDataType(table);
    assertEquals(PROFILE_MQTT_UADP, dataType2.getConnections()[0].getTransportProfileUri());
    assertEquals(dataType1.getConnections()[0], dataType2.getConnections()[0]);
    assertEquals(dataType1, dataType2);
  }

  // endregion
}
