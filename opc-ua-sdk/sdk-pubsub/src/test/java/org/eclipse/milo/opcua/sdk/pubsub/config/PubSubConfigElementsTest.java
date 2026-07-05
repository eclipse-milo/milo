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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramConnectionTransport2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressUrlDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SecurityGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link PubSubConfigElements} per-element mapping entry points: each {@code map*}
 * method maps its element exactly like whole-config {@link PubSubConfig#fromDataType} maps the same
 * element within a representative configuration, wire SecurityGroupId references resolve against
 * the supplied security-group context (with the wire-id fallback when they do not), and mapping
 * failures surface as {@link PubSubConfigValidationException}.
 */
class PubSubConfigElementsTest {

  private static final String URI_1 = "urn:milo:test:ns1";
  private static final String URI_2 = "urn:milo:test:ns2";

  private static final UUID FIELD_TEMP_ID = new UUID(0xA1L, 1L);
  private static final UUID FIELD_LABEL_ID = new UUID(0xA1L, 2L);
  private static final UUID FIELD_VALUE_ID = new UUID(0xA2L, 1L);
  private static final UUID FIELD_EVT_SEVERITY_ID = new UUID(0xA3L, 1L);
  private static final UUID FIELD_EVT_MESSAGE_ID = new UUID(0xA3L, 2L);
  private static final UUID MD_F1_ID = new UUID(0xB1L, 1L);
  private static final UUID MD_F2_ID = new UUID(0xB1L, 2L);
  private static final UUID MD_J1_ID = new UUID(0xB2L, 1L);
  private static final UUID SDS_G1_ID = new UUID(0xC1L, 1L);

  private static NamespaceTable namespaceTable() {
    return new NamespaceTable(URI_1, URI_2);
  }

  private static ExtensionObject encode(UaStructuredType struct) {
    return ExtensionObject.encode(new DefaultEncodingContext(), struct);
  }

  // region Representative authored config fixture

  private static SecurityGroupConfig securityGroup() {
    return SecurityGroupConfig.builder("sg-1")
        .securityGroupId("SG-001")
        .securityGroupFolder(List.of("Folder", "SubFolder"))
        .securityPolicyUri("http://opcfoundation.org/UA/SecurityPolicy#Aes128_Sha256_RsaOaep")
        .keyLifeTime(Duration.ofMinutes(30))
        .maxFutureKeyCount(uint(2))
        .maxPastKeyCount(uint(3))
        .property(new QualifiedName(1, "sgProp"), Variant.ofString("sgVal"))
        .build();
  }

  private static PublishedDataSetConfig publishedDataSet1() {
    return PublishedDataSetConfig.builder("pds-1")
        .field(
            FieldDefinition.builder("temperature")
                .source(
                    new NodeFieldAddress(
                        ExpandedNodeId.of(URI_1, "Sensor.Temperature"), AttributeId.Value))
                .dataType(NodeIds.Double)
                .dataSetFieldId(FIELD_TEMP_ID)
                .promoted(true)
                .property(new QualifiedName(1, "unit"), Variant.ofString("celsius"))
                .build())
        .field(
            FieldDefinition.builder("label")
                .source(KeyFieldAddress.of("label.key"))
                .dataType(NodeIds.String)
                .dataSetFieldId(FIELD_LABEL_ID)
                .build())
        .configurationVersion(uint(2), uint(7))
        .property(new QualifiedName(1, "dsProp"), Variant.ofInt32(5))
        .build();
  }

  private static PublishedDataSetConfig publishedDataSet2() {
    return PublishedDataSetConfig.builder("pds-2")
        .field(
            FieldDefinition.builder("value")
                .dataType(NodeIds.Double)
                .dataSetFieldId(FIELD_VALUE_ID)
                .build())
        .build();
  }

  private static SimpleAttributeOperand baseEventOperand(String browseName) {
    return new SimpleAttributeOperand(
        NodeIds.BaseEventType,
        new QualifiedName[] {new QualifiedName(0, browseName)},
        AttributeId.Value.uid(),
        null);
  }

  private static PublishedDataSetConfig publishedEventsDataSet() {
    return PublishedDataSetConfig.builder("pds-events")
        .source(
            PublishedEventsConfig.builder(ExpandedNodeId.of(URI_1, "Events.Notifier"))
                .field(
                    EventFieldDefinition.builder("severity")
                        .selectedField(baseEventOperand("Severity"))
                        .dataType(NodeIds.UInt16)
                        .dataSetFieldId(FIELD_EVT_SEVERITY_ID)
                        .promoted(true)
                        .build())
                .field(
                    EventFieldDefinition.builder("message")
                        .selectedField(baseEventOperand("Message"))
                        .dataType(NodeIds.LocalizedText)
                        .dataSetFieldId(FIELD_EVT_MESSAGE_ID)
                        .property(new QualifiedName(1, "evtFieldProp"), Variant.ofString("evtVal"))
                        .build())
                .filter(
                    new ContentFilter(
                        new ContentFilterElement[] {
                          new ContentFilterElement(
                              FilterOperator.OfType,
                              new ExtensionObject[] {
                                encode(new LiteralOperand(Variant.ofNodeId(NodeIds.BaseEventType)))
                              })
                        }))
                .build())
        .configurationVersion(uint(4), uint(13))
        .property(new QualifiedName(1, "evtDsProp"), Variant.ofInt32(9))
        .build();
  }

  private static DataSetMetaDataConfig readerMetaData() {
    return DataSetMetaDataConfig.builder("md-1")
        .field("f1", NodeIds.Double, MD_F1_ID)
        .field("f2", NodeIds.Int32, MD_F2_ID)
        .configurationVersion(uint(3), uint(9))
        .build();
  }

  private static UdpConnectionConfig udpConnection(SecurityGroupConfig sg) {
    WriterGroupConfig wgUdp =
        WriterGroupConfig.builder("wg-udp")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ofMillis(250))
            .keepAliveTime(Duration.ofSeconds(5))
            .priority(ubyte(3))
            .maxNetworkMessageSize(uint(4096))
            .messageSecurity(
                MessageSecurityConfig.builder()
                    .mode(MessageSecurityMode.Sign)
                    .securityGroup(sg.ref())
                    .build())
            .messageSettings(
                UadpWriterGroupSettings.builder()
                    .groupVersion(uint(123))
                    .dataSetOrdering(DataSetOrderingType.AscendingWriterId)
                    .networkMessageContentMask(
                        UadpNetworkMessageContentMask.of(
                            UadpNetworkMessageContentMask.Field.PublisherId,
                            UadpNetworkMessageContentMask.Field.WriterGroupId,
                            UadpNetworkMessageContentMask.Field.PayloadHeader))
                    .build())
            .property(new QualifiedName(0, "wgProp"), Variant.ofBoolean(true))
            .dataSetWriter(
                DataSetWriterConfig.builder("dsw-1")
                    .dataSet(new PublishedDataSetRef("pds-1"))
                    .dataSetWriterId(ushort(11))
                    .keyFrameCount(uint(5))
                    .fieldContentMask(
                        DataSetFieldContentMask.of(
                            DataSetFieldContentMask.Field.StatusCode,
                            DataSetFieldContentMask.Field.SourceTimestamp))
                    .settings(
                        UadpDataSetWriterSettings.builder()
                            .dataSetMessageContentMask(
                                UadpDataSetMessageContentMask.of(
                                    UadpDataSetMessageContentMask.Field.Timestamp,
                                    UadpDataSetMessageContentMask.Field.SequenceNumber))
                            .build())
                    .property(new QualifiedName(1, "dswProp"), Variant.ofDouble(1.25))
                    .build())
            .dataSetWriter(
                DataSetWriterConfig.builder("dsw-2")
                    .enabled(false)
                    .dataSet(new PublishedDataSetRef("pds-2"))
                    .dataSetWriterId(ushort(12))
                    .build())
            .build();

    ReaderGroupConfig rgUdp =
        ReaderGroupConfig.builder("rg-udp")
            .maxNetworkMessageSize(uint(2048))
            .messageSecurity(
                MessageSecurityConfig.builder()
                    .mode(MessageSecurityMode.Sign)
                    .securityGroup(sg.ref())
                    .build())
            .property(new QualifiedName(1, "rgProp"), Variant.ofString("rgVal"))
            .dataSetReader(
                DataSetReaderConfig.builder("dsr-1")
                    .publisherId(PublisherId.ubyte(ubyte(7)))
                    .writerGroupId(ushort(1))
                    .dataSetWriterId(ushort(11))
                    .dataSetMetaData(readerMetaData())
                    // Reader-level override: replaces the reader group's security for this reader.
                    .messageSecurity(
                        MessageSecurityConfig.builder()
                            .mode(MessageSecurityMode.Sign)
                            .securityGroup(sg.ref())
                            .build())
                    .messageReceiveTimeout(Duration.ofSeconds(5))
                    .keyFrameCount(uint(10))
                    .settings(
                        UadpDataSetReaderSettings.builder()
                            .groupVersion(uint(7))
                            .networkMessageNumber(ushort(1))
                            .networkMessageContentMask(
                                UadpNetworkMessageContentMask.of(
                                    UadpNetworkMessageContentMask.Field.PublisherId,
                                    UadpNetworkMessageContentMask.Field.PayloadHeader))
                            .dataSetMessageContentMask(
                                UadpDataSetMessageContentMask.of(
                                    UadpDataSetMessageContentMask.Field.Timestamp,
                                    UadpDataSetMessageContentMask.Field.SequenceNumber))
                            .build())
                    .subscribedDataSet(
                        TargetVariablesConfig.builder()
                            .map(
                                FieldSelector.byId(MD_F1_ID),
                                TargetVariableConfig.builder()
                                    .target(
                                        new NodeFieldAddress(
                                            ExpandedNodeId.of(URI_2, "Target.Node1"),
                                            AttributeId.Value))
                                    .build())
                            .build())
                    .property(new QualifiedName(1, "readerProp"), Variant.ofInt32(3))
                    .build())
            .dataSetReader(
                DataSetReaderConfig.builder("dsr-sds")
                    .publisherId(PublisherId.uint32(uint(777)))
                    .subscribedDataSet(new StandaloneSubscribedDataSetRef("sds-1"))
                    .build())
            .build();

    return PubSubConnectionConfig.udp("udp-conn")
        .publisherId(PublisherId.uint16(ushort(42)))
        .address(UdpDatagramAddress.multicast("239.0.0.5", 4841).networkInterface("en0"))
        .property(new QualifiedName(1, "connProp"), Variant.ofString("connVal"))
        .rawTransportSettings(
            encode(
                new DatagramConnectionTransport2DataType(
                    new NetworkAddressUrlDataType(null, null), uint(5), uint(8192), "qos-a", null)))
        .writerGroup(wgUdp)
        .readerGroup(rgUdp)
        .build();
  }

  private static MqttConnectionConfig mqttConnection() {
    WriterGroupConfig wgJson =
        WriterGroupConfig.builder("wg-json")
            .writerGroupId(ushort(2))
            .publishingInterval(Duration.ofMillis(500))
            .messageSettings(
                JsonWriterGroupSettings.builder()
                    .networkMessageContentMask(
                        JsonNetworkMessageContentMask.of(
                            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
                            JsonNetworkMessageContentMask.Field.DataSetMessageHeader,
                            JsonNetworkMessageContentMask.Field.PublisherId))
                    .build())
            .brokerTransport(
                BrokerTransportSettings.builder()
                    .queueName("wg-queue")
                    .resourceUri("res-uri")
                    .authenticationProfileUri("auth-uri")
                    .requestedDeliveryGuarantee(BrokerTransportQualityOfService.AtLeastOnce)
                    .build())
            .dataSetWriter(
                DataSetWriterConfig.builder("dsw-json")
                    .dataSet(new PublishedDataSetRef("pds-2"))
                    .dataSetWriterId(ushort(21))
                    .settings(
                        JsonDataSetWriterSettings.builder()
                            .dataSetMessageContentMask(
                                JsonDataSetMessageContentMask.of(
                                    JsonDataSetMessageContentMask.Field.DataSetWriterId,
                                    JsonDataSetMessageContentMask.Field.SequenceNumber))
                            .build())
                    .brokerTransport(
                        BrokerTransportSettings.builder()
                            .queueName("w-queue")
                            .metaDataQueueName("w-meta")
                            .metaDataUpdateTime(Duration.ofSeconds(2))
                            .requestedDeliveryGuarantee(BrokerTransportQualityOfService.ExactlyOnce)
                            .build())
                    .build())
            .build();

    ReaderGroupConfig rgJson =
        ReaderGroupConfig.builder("rg-json")
            .dataSetReader(
                DataSetReaderConfig.builder("dsr-json")
                    .publisherId(PublisherId.string("publisher-x"))
                    .dataSetMetaData(
                        DataSetMetaDataConfig.builder("md-json")
                            .field("j1", NodeIds.String, MD_J1_ID)
                            .build())
                    .settings(
                        JsonDataSetReaderSettings.builder()
                            .networkMessageContentMask(
                                JsonNetworkMessageContentMask.of(
                                    JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
                                    JsonNetworkMessageContentMask.Field.PublisherId))
                            .dataSetMessageContentMask(
                                JsonDataSetMessageContentMask.of(
                                    JsonDataSetMessageContentMask.Field.DataSetWriterId))
                            .build())
                    .brokerTransport(
                        BrokerTransportSettings.builder()
                            .queueName("r-queue")
                            .metaDataQueueName("r-meta")
                            .requestedDeliveryGuarantee(BrokerTransportQualityOfService.AtMostOnce)
                            .build())
                    .build())
            .build();

    return PubSubConnectionConfig.mqtt("mqtt-conn")
        .publisherId(PublisherId.uint64(ulong(123456789L)))
        .brokerUri(URI.create("mqtt://broker.example:1883"))
        .property(new QualifiedName(1, "mqttProp"), Variant.ofString("mqttVal"))
        .writerGroup(wgJson)
        .readerGroup(rgJson)
        .build();
  }

  private static StandaloneSubscribedDataSetConfig standalone1() {
    return StandaloneSubscribedDataSetConfig.builder("sds-1")
        .metaData(
            DataSetMetaDataConfig.builder("md-sds").field("g1", NodeIds.Double, SDS_G1_ID).build())
        .subscribedDataSet(
            TargetVariablesConfig.builder()
                .map(
                    FieldSelector.byId(SDS_G1_ID),
                    TargetVariableConfig.builder()
                        .target(
                            new NodeFieldAddress(
                                ExpandedNodeId.of(URI_2, "Sds.Target"), AttributeId.Value))
                        .build())
                .build())
        .build();
  }

  /**
   * A representative authored config covering every element kind: UDP and MQTT connections, UADP
   * and JSON groups, secured groups with SecurityGroup references, a reader-level security
   * override, broker transports at all three levels, a data-items and an event published dataset, a
   * standalone subscribed dataset, a raw escape hatch, and properties at every level.
   */
  private static PubSubConfig representativeConfig() {
    SecurityGroupConfig sg = securityGroup();

    return PubSubConfig.builder()
        .connection(udpConnection(sg))
        .connection(mqttConnection())
        .publishedDataSet(publishedDataSet1())
        .publishedDataSet(publishedDataSet2())
        .publishedDataSet(publishedEventsDataSet())
        .standaloneSubscribedDataSet(standalone1())
        .securityGroup(sg)
        .build();
  }

  // endregion

  // region Per-element equivalence with whole-config mapping

  @Test
  void mapConnectionMatchesWholeConfigMapping() {
    NamespaceTable namespaceTable = namespaceTable();
    PubSubConfiguration2DataType dataType = representativeConfig().toDataType(namespaceTable);
    PubSubConfig whole = PubSubConfig.fromDataType(dataType, namespaceTable);
    List<SecurityGroupConfig> securityGroups = whole.securityGroups();

    PubSubConnectionDataType[] connections = requireNonNull(dataType.getConnections());
    assertEquals(2, connections.length);
    for (int i = 0; i < connections.length; i++) {
      assertEquals(
          whole.connections().get(i),
          PubSubConfigElements.mapConnection(connections[i], namespaceTable, securityGroups));
    }
  }

  @Test
  void mapWriterGroupMatchesWholeConfigMapping() {
    NamespaceTable namespaceTable = namespaceTable();
    PubSubConfiguration2DataType dataType = representativeConfig().toDataType(namespaceTable);
    PubSubConfig whole = PubSubConfig.fromDataType(dataType, namespaceTable);
    List<SecurityGroupConfig> securityGroups = whole.securityGroups();

    PubSubConnectionDataType[] connections = requireNonNull(dataType.getConnections());
    for (int i = 0; i < connections.length; i++) {
      WriterGroupDataType[] writerGroups = requireNonNull(connections[i].getWriterGroups());
      assertEquals(1, writerGroups.length);
      assertEquals(
          whole.connections().get(i).writerGroups().get(0),
          PubSubConfigElements.mapWriterGroup(writerGroups[0], namespaceTable, securityGroups));
    }
  }

  @Test
  void mapDataSetWriterMatchesWholeConfigMapping() {
    NamespaceTable namespaceTable = namespaceTable();
    PubSubConfiguration2DataType dataType = representativeConfig().toDataType(namespaceTable);
    PubSubConfig whole = PubSubConfig.fromDataType(dataType, namespaceTable);
    List<SecurityGroupConfig> securityGroups = whole.securityGroups();

    PubSubConnectionDataType[] connections = requireNonNull(dataType.getConnections());
    for (int i = 0; i < connections.length; i++) {
      WriterGroupDataType[] writerGroups = requireNonNull(connections[i].getWriterGroups());
      DataSetWriterDataType[] writers = requireNonNull(writerGroups[0].getDataSetWriters());
      List<DataSetWriterConfig> expected =
          whole.connections().get(i).writerGroups().get(0).getDataSetWriters();
      assertEquals(expected.size(), writers.length);
      for (int j = 0; j < writers.length; j++) {
        assertEquals(
            expected.get(j),
            PubSubConfigElements.mapDataSetWriter(writers[j], namespaceTable, securityGroups));
      }
    }
  }

  @Test
  void mapReaderGroupMatchesWholeConfigMapping() {
    NamespaceTable namespaceTable = namespaceTable();
    PubSubConfiguration2DataType dataType = representativeConfig().toDataType(namespaceTable);
    PubSubConfig whole = PubSubConfig.fromDataType(dataType, namespaceTable);
    List<SecurityGroupConfig> securityGroups = whole.securityGroups();

    PubSubConnectionDataType[] connections = requireNonNull(dataType.getConnections());
    for (int i = 0; i < connections.length; i++) {
      ReaderGroupDataType[] readerGroups = requireNonNull(connections[i].getReaderGroups());
      assertEquals(1, readerGroups.length);
      assertEquals(
          whole.connections().get(i).readerGroups().get(0),
          PubSubConfigElements.mapReaderGroup(readerGroups[0], namespaceTable, securityGroups));
    }
  }

  @Test
  void mapDataSetReaderMatchesWholeConfigMapping() {
    NamespaceTable namespaceTable = namespaceTable();
    PubSubConfiguration2DataType dataType = representativeConfig().toDataType(namespaceTable);
    PubSubConfig whole = PubSubConfig.fromDataType(dataType, namespaceTable);
    List<SecurityGroupConfig> securityGroups = whole.securityGroups();

    PubSubConnectionDataType[] connections = requireNonNull(dataType.getConnections());
    for (int i = 0; i < connections.length; i++) {
      ReaderGroupDataType[] readerGroups = requireNonNull(connections[i].getReaderGroups());
      DataSetReaderDataType[] readers = requireNonNull(readerGroups[0].getDataSetReaders());
      List<DataSetReaderConfig> expected =
          whole.connections().get(i).readerGroups().get(0).getDataSetReaders();
      assertEquals(expected.size(), readers.length);
      for (int j = 0; j < readers.length; j++) {
        assertEquals(
            expected.get(j),
            PubSubConfigElements.mapDataSetReader(readers[j], namespaceTable, securityGroups));
      }
    }
  }

  @Test
  void mapPublishedDataSetMatchesWholeConfigMapping() {
    NamespaceTable namespaceTable = namespaceTable();
    PubSubConfiguration2DataType dataType = representativeConfig().toDataType(namespaceTable);
    PubSubConfig whole = PubSubConfig.fromDataType(dataType, namespaceTable);
    List<SecurityGroupConfig> securityGroups = whole.securityGroups();

    var publishedDataSets = requireNonNull(dataType.getPublishedDataSets());
    // Two data-items datasets plus the event dataset (pds-events).
    assertEquals(3, publishedDataSets.length);
    for (int i = 0; i < publishedDataSets.length; i++) {
      assertEquals(
          whole.publishedDataSets().get(i),
          PubSubConfigElements.mapPublishedDataSet(
              publishedDataSets[i], namespaceTable, securityGroups));
    }

    // The event dataset is present and maps identically alone and within the whole config.
    PublishedDataSetConfig event =
        PubSubConfigElements.mapPublishedDataSet(
            publishedDataSets[2], namespaceTable, securityGroups);
    assertInstanceOf(PublishedEventsConfig.class, event.getSource());
    assertEquals(whole.publishedDataSets().get(2), event);
  }

  @Test
  void mapStandaloneSubscribedDataSetMatchesWholeConfigMapping() {
    NamespaceTable namespaceTable = namespaceTable();
    PubSubConfiguration2DataType dataType = representativeConfig().toDataType(namespaceTable);
    PubSubConfig whole = PubSubConfig.fromDataType(dataType, namespaceTable);
    List<SecurityGroupConfig> securityGroups = whole.securityGroups();

    var subscribedDataSets = requireNonNull(dataType.getSubscribedDataSets());
    assertEquals(1, subscribedDataSets.length);
    assertEquals(
        whole.standaloneSubscribedDataSets().get(0),
        PubSubConfigElements.mapStandaloneSubscribedDataSet(
            subscribedDataSets[0], namespaceTable, securityGroups));
  }

  @Test
  void mapSecurityGroupMatchesWholeConfigMapping() {
    NamespaceTable namespaceTable = namespaceTable();
    PubSubConfiguration2DataType dataType = representativeConfig().toDataType(namespaceTable);
    PubSubConfig whole = PubSubConfig.fromDataType(dataType, namespaceTable);

    var securityGroups = requireNonNull(dataType.getSecurityGroups());
    assertEquals(1, securityGroups.length);
    assertEquals(
        whole.securityGroups().get(0), PubSubConfigElements.mapSecurityGroup(securityGroups[0]));
  }

  // endregion

  // region Security-group resolution context

  @Test
  void writerGroupSecurityGroupIdResolvesAgainstSuppliedGroups() {
    NamespaceTable namespaceTable = namespaceTable();
    PubSubConfiguration2DataType dataType = representativeConfig().toDataType(namespaceTable);

    WriterGroupDataType wire =
        requireNonNull(requireNonNull(dataType.getConnections())[0].getWriterGroups())[0];
    assertEquals("SG-001", wire.getSecurityGroupId());

    // With the referenced group in context the ref resolves to the group's config name.
    WriterGroupConfig resolved =
        PubSubConfigElements.mapWriterGroup(wire, namespaceTable, List.of(securityGroup()));
    assertEquals("sg-1", requireNonNull(resolved.getMessageSecurity()).getSecurityGroup().name());

    // Without it the ref falls back to the wire id itself (fails later builder validation
    // unless a configured group has that name).
    WriterGroupConfig unresolved =
        PubSubConfigElements.mapWriterGroup(wire, namespaceTable, List.of());
    assertEquals(
        "SG-001", requireNonNull(unresolved.getMessageSecurity()).getSecurityGroup().name());
  }

  @Test
  void readerOverrideSecurityGroupIdResolvesAgainstSuppliedGroups() {
    NamespaceTable namespaceTable = namespaceTable();
    PubSubConfiguration2DataType dataType = representativeConfig().toDataType(namespaceTable);

    DataSetReaderDataType wire =
        requireNonNull(
            requireNonNull(requireNonNull(dataType.getConnections())[0].getReaderGroups())[0]
                .getDataSetReaders())[0];
    assertEquals("SG-001", wire.getSecurityGroupId());

    DataSetReaderConfig resolved =
        PubSubConfigElements.mapDataSetReader(wire, namespaceTable, List.of(securityGroup()));
    assertEquals("sg-1", requireNonNull(resolved.getMessageSecurity()).getSecurityGroup().name());

    DataSetReaderConfig unresolved =
        PubSubConfigElements.mapDataSetReader(wire, namespaceTable, List.of());
    assertEquals(
        "SG-001", requireNonNull(unresolved.getMessageSecurity()).getSecurityGroup().name());
  }

  // endregion

  // region Failure rows

  @Test
  void connectionWithoutAddressUrlThrows() {
    var value =
        new PubSubConnectionDataType(
            "c1", true, Variant.NULL_VALUE, null, null, null, null, null, null);

    var e =
        assertThrows(
            PubSubConfigValidationException.class,
            () -> PubSubConfigElements.mapConnection(value, namespaceTable(), List.of()));
    assertTrue(
        e.getMessage().contains("address url is required"),
        "unexpected message: " + e.getMessage());
  }

  @Test
  void dataSetWriterWithoutDataSetNameThrows() {
    var value =
        new DataSetWriterDataType("w1", true, ushort(1), null, null, null, null, null, null);

    var e =
        assertThrows(
            PubSubConfigValidationException.class,
            () -> PubSubConfigElements.mapDataSetWriter(value, namespaceTable(), List.of()));
    assertTrue(
        e.getMessage().contains("dataSetName is required"),
        "unexpected message: " + e.getMessage());
  }

  @Test
  void standaloneSubscribedDataSetWithoutMetaDataThrows() {
    var value = new StandaloneSubscribedDataSetDataType("sds", null, null, null);

    var e =
        assertThrows(
            PubSubConfigValidationException.class,
            () ->
                PubSubConfigElements.mapStandaloneSubscribedDataSet(
                    value, namespaceTable(), List.of()));
    assertTrue(
        e.getMessage().contains("dataSetMetaData is required"),
        "unexpected message: " + e.getMessage());
  }

  @Test
  void securityGroupWithoutNameThrows() {
    var value = new SecurityGroupDataType(null, null, 0.0, null, null, null, null, null, null);

    var e =
        assertThrows(
            PubSubConfigValidationException.class,
            () -> PubSubConfigElements.mapSecurityGroup(value));
    assertTrue(
        e.getMessage().contains("name is required"), "unexpected message: " + e.getMessage());
  }

  @Test
  void unresolvableNamespaceIndexThrows() {
    // The reader's target variable NodeId lives in URI_2; a decode-side table without URI_2
    // cannot resolve its namespace index.
    PubSubConfiguration2DataType dataType = representativeConfig().toDataType(namespaceTable());
    DataSetReaderDataType wire =
        requireNonNull(
            requireNonNull(requireNonNull(dataType.getConnections())[0].getReaderGroups())[0]
                .getDataSetReaders())[0];

    NamespaceTable partial = new NamespaceTable(URI_1);

    var e =
        assertThrows(
            PubSubConfigValidationException.class,
            () -> PubSubConfigElements.mapDataSetReader(wire, partial, List.of()));
    assertTrue(
        e.getMessage().contains("not present in the namespace table"),
        "unexpected message: " + e.getMessage());
  }

  // endregion
}
