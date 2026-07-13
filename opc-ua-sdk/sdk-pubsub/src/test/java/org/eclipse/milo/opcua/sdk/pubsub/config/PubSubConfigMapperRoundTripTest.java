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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.OverrideValueHandling;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerConnectionTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramConnectionTransport2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramWriterGroupTransport2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressUrlDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataItemsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedEventsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SecurityGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleTypeDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.types.structured.TargetVariablesDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.junit.jupiter.api.Test;

/**
 * Round-trip tests for the {@code PubSubConfig} / {@code PubSubConfiguration2DataType} mapper:
 * authored configs that are fixed points of toDataType/fromDataType, spot checks of the emitted
 * Part 14 wire shapes, namespace table conversions, and raw escape hatch behavior.
 */
class PubSubConfigMapperRoundTripTest {

  private static final String URI_1 = "urn:milo:test:ns1";
  private static final String URI_2 = "urn:milo:test:ns2";

  private static final String PROFILE_UDP_UADP =
      "http://opcfoundation.org/UA-Profile/Transport/pubsub-udp-uadp";
  private static final String PROFILE_MQTT_UADP =
      "http://opcfoundation.org/UA-Profile/Transport/pubsub-mqtt-uadp";
  private static final String PROFILE_MQTT_JSON =
      "http://opcfoundation.org/UA-Profile/Transport/pubsub-mqtt-json";

  private static final QualifiedName MILO_SOURCE_KEY = new QualifiedName(0, "MiloSourceKey");

  private static final QualifiedName MILO_EVENT_QUEUE_CAPACITY =
      new QualifiedName(0, "MiloEventQueueCapacity");

  private static final RolePermissionType ROLE_PERMISSION =
      new RolePermissionType(
          NodeIds.WellKnownRole_SecurityAdmin, PermissionType.of(PermissionType.Field.Call));

  private static final UUID FIELD_TEMP_ID = new UUID(0xA1L, 1L);
  private static final UUID FIELD_COUNT_ID = new UUID(0xA1L, 2L);
  private static final UUID FIELD_LABEL_ID = new UUID(0xA1L, 3L);
  private static final UUID FIELD_ENVELOPE_ID = new UUID(0xA1L, 4L);
  private static final UUID FIELD_VALUE_ID = new UUID(0xA2L, 1L);
  private static final UUID FIELD_EVT_SEVERITY_ID = new UUID(0xA3L, 1L);
  private static final UUID FIELD_EVT_MESSAGE_ID = new UUID(0xA3L, 2L);
  private static final UUID FIELD_POSITION_ID = new UUID(0xA4L, 1L);
  private static final UUID MD_F1_ID = new UUID(0xB1L, 1L);
  private static final UUID MD_F2_ID = new UUID(0xB1L, 2L);
  private static final UUID MD_CLASS_ID = new UUID(0xB1L, 99L);
  private static final UUID MD_J1_ID = new UUID(0xB2L, 1L);
  private static final UUID SDS_G1_ID = new UUID(0xC1L, 1L);

  private static NamespaceTable namespaceTable() {
    return new NamespaceTable(URI_1, URI_2);
  }

  private static ExtensionObject encode(UaStructuredType struct) {
    return ExtensionObject.encode(new DefaultEncodingContext(), struct);
  }

  private static UaStructuredType decode(ExtensionObject xo) {
    return xo.decode(new DefaultEncodingContext());
  }

  // region Authored config fixture

  private static DatagramConnectionTransport2DataType datagram2ConnectionTransport() {
    return new DatagramConnectionTransport2DataType(
        new NetworkAddressUrlDataType(null, null), uint(5), uint(8192), "qos-a", null);
  }

  private static DatagramWriterGroupTransport2DataType datagram2WriterGroupTransport() {
    return new DatagramWriterGroupTransport2DataType(
        ubyte(2),
        10.0,
        new NetworkAddressUrlDataType(null, null),
        "qos-b",
        null,
        uint(0),
        "topic-x");
  }

  private static BrokerConnectionTransportDataType vendorishBrokerConnectionTransport() {
    return new BrokerConnectionTransportDataType("res-x", "auth-x");
  }

  private static EndpointDescription keyServiceEndpoint() {
    return keyServiceEndpoint("opc.tcp://sks.example:4840");
  }

  /** A distinct endpoint for the config-level defaultSecurityKeyServices slot. */
  private static EndpointDescription defaultKeyServiceEndpoint() {
    return keyServiceEndpoint("opc.tcp://sks-default.example:4840");
  }

  private static EndpointDescription keyServiceEndpoint(String url) {
    return new EndpointDescription(
        url,
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

  private static SecurityGroupConfig securityGroup() {
    return SecurityGroupConfig.builder("sg-1")
        .securityGroupId("SG-001")
        .securityGroupFolder(List.of("Folder", "SubFolder"))
        .securityPolicyUri("http://opcfoundation.org/UA/SecurityPolicy#Aes128_Sha256_RsaOaep")
        .keyLifeTime(Duration.ofMinutes(30))
        .maxFutureKeyCount(uint(2))
        .maxPastKeyCount(uint(3))
        .rolePermission(ROLE_PERMISSION)
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
            FieldDefinition.builder("count")
                .source(
                    new NodeFieldAddress(ExpandedNodeId.of(URI_1, uint(5001)), AttributeId.Value))
                .dataType(NodeIds.Int32)
                .dataSetFieldId(FIELD_COUNT_ID)
                .build())
        .field(
            FieldDefinition.builder("label")
                .source(KeyFieldAddress.of("label.key"))
                .dataType(NodeIds.String)
                .dataSetFieldId(FIELD_LABEL_ID)
                .build())
        .field(
            // No source: defaults to KeyFieldAddress("envelope"). Range is not a builtin type.
            FieldDefinition.builder("envelope")
                .dataType(NodeIds.Range)
                .dataSetFieldId(FIELD_ENVELOPE_ID)
                .valueRank(1)
                .arrayDimensions(new UInteger[] {uint(3)})
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

  /**
   * A dataset with a custom-structure field and authored DataTypeSchemaHeader type descriptions.
   * The DataType NodeIds live in URI_2 (publisher-local index 2 in {@link #namespaceTable()}).
   */
  private static PublishedDataSetConfig customTypesDataSet() {
    return PublishedDataSetConfig.builder("pds-custom")
        .field(
            FieldDefinition.builder("position")
                .dataType(new NodeId(2, 3001))
                .dataSetFieldId(FIELD_POSITION_ID)
                .build())
        .structureDataType(
            new NodeId(2, 3001),
            new QualifiedName(2, "Position"),
            new StructureDefinition(
                new NodeId(2, 3003),
                NodeIds.Structure,
                StructureType.Structure,
                new StructureField[] {
                  new StructureField(
                      "X", LocalizedText.NULL_VALUE, NodeIds.Double, -1, null, uint(0), false),
                  new StructureField(
                      "Y", LocalizedText.NULL_VALUE, NodeIds.Double, -1, null, uint(0), false)
                }))
        .enumDataType(
            new EnumDescription(
                new NodeId(2, 4001),
                new QualifiedName(2, "Mode"),
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Off"),
                      new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "On")
                    }),
                ubyte(6)))
        .simpleDataType(
            new SimpleTypeDescription(
                new NodeId(2, 5001), new QualifiedName(2, "Meters"), NodeIds.Double, ubyte(11)))
        .build();
  }

  private static SimpleAttributeOperand baseEventOperand(String browseName) {
    return new SimpleAttributeOperand(
        NodeIds.BaseEventType,
        new QualifiedName[] {new QualifiedName(0, browseName)},
        AttributeId.Value.uid(),
        null);
  }

  private static ContentFilter eventFilter() {
    return new ContentFilter(
        new ContentFilterElement[] {
          new ContentFilterElement(
              FilterOperator.OfType,
              new ExtensionObject[] {
                encode(new LiteralOperand(Variant.ofNodeId(NodeIds.BaseEventType)))
              })
        });
  }

  private static PublishedDataSetConfig publishedEventsDataSet() {
    return PublishedDataSetConfig.builder("pds-events")
        .source(
            // The eventNotifier is authored in the canonical namespace-URI ExpandedNodeId form
            // that the Part 14 import mapping produces, so the round trip is a fixed point.
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
                .filter(eventFilter())
                .build())
        .configurationVersion(uint(4), uint(13))
        .property(new QualifiedName(1, "evtDsProp"), Variant.ofInt32(9))
        .build();
  }

  private static DataSetMetaDataConfig readerMetaData() {
    return DataSetMetaDataConfig.builder("md-1")
        .field("f1", NodeIds.Double, MD_F1_ID)
        .field(
            new DataSetMetaDataConfig.Field(
                "f2", NodeIds.Int32, MD_F2_ID, 1, new UInteger[] {uint(4)}))
        .dataSetClassId(MD_CLASS_ID)
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
                    .mode(MessageSecurityMode.None)
                    .securityGroup(sg.ref())
                    .keyServices(List.of(keyServiceEndpoint()))
                    .build())
            .messageSettings(
                UadpWriterGroupSettings.builder()
                    .groupVersion(uint(123))
                    .dataSetOrdering(DataSetOrderingType.AscendingWriterId)
                    .networkMessageContentMask(
                        UadpNetworkMessageContentMask.of(
                            UadpNetworkMessageContentMask.Field.PublisherId,
                            UadpNetworkMessageContentMask.Field.GroupHeader,
                            UadpNetworkMessageContentMask.Field.WriterGroupId,
                            UadpNetworkMessageContentMask.Field.GroupVersion,
                            UadpNetworkMessageContentMask.Field.PayloadHeader,
                            UadpNetworkMessageContentMask.Field.Timestamp))
                    .build())
            .rawTransportSettings(encode(datagram2WriterGroupTransport()))
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
                                    UadpDataSetMessageContentMask.Field.Status,
                                    UadpDataSetMessageContentMask.Field.MajorVersion,
                                    UadpDataSetMessageContentMask.Field.MinorVersion,
                                    UadpDataSetMessageContentMask.Field.SequenceNumber))
                            .configuredSize(ushort(64))
                            .networkMessageNumber(ushort(1))
                            .dataSetOffset(ushort(8))
                            .build())
                    .property(new QualifiedName(1, "dswProp"), Variant.ofDouble(1.25))
                    .build())
            .dataSetWriter(
                DataSetWriterConfig.builder("dsw-2")
                    .enabled(false)
                    .dataSet(new PublishedDataSetRef("pds-2"))
                    .dataSetWriterId(ushort(12))
                    .build())
            .dataSetWriter(
                DataSetWriterConfig.builder("dsw-events")
                    .dataSet(new PublishedDataSetRef("pds-events"))
                    .dataSetWriterId(ushort(13))
                    // Part 14 §6.2.4.3: event datasets are non-cyclic, KeyFrameCount 0.
                    .keyFrameCount(uint(0))
                    .eventQueueCapacity(500)
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
                            .keyServices(List.of(keyServiceEndpoint()))
                            .build())
                    .messageReceiveTimeout(Duration.ofSeconds(5))
                    .keyFrameCount(uint(10))
                    .settings(
                        UadpDataSetReaderSettings.builder()
                            .groupVersion(uint(7))
                            .networkMessageNumber(ushort(1))
                            .dataSetOffset(ushort(2))
                            .networkMessageContentMask(
                                UadpNetworkMessageContentMask.of(
                                    UadpNetworkMessageContentMask.Field.PublisherId,
                                    UadpNetworkMessageContentMask.Field.GroupHeader,
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
                                            ExpandedNodeId.of(URI_1, "Target.Node1"),
                                            AttributeId.Value))
                                    .receiverIndexRange("0:3")
                                    .writeIndexRange("1:2")
                                    .overrideHandling(OverrideValueHandling.OverrideValue)
                                    .overrideValue(Variant.ofDouble(99.5))
                                    .build())
                            .map(
                                FieldSelector.byId(MD_F2_ID),
                                TargetVariableConfig.builder()
                                    .target(
                                        new NodeFieldAddress(
                                            ExpandedNodeId.of(URI_2, "Target.Node2"),
                                            AttributeId.Value))
                                    .overrideHandling(OverrideValueHandling.LastUsableValue)
                                    .build())
                            .build())
                    .property(new QualifiedName(1, "readerProp"), Variant.ofInt32(3))
                    .build())
            .dataSetReader(
                DataSetReaderConfig.builder("dsr-sds")
                    .publisherId(PublisherId.uint32(uint(777)))
                    .subscribedDataSet(new StandaloneSubscribedDataSetRef("sds-1"))
                    // Explicit override to None: an active override to unsecured operation, kept
                    // distinct from "no override" by its security group reference.
                    .messageSecurity(
                        MessageSecurityConfig.builder()
                            .mode(MessageSecurityMode.None)
                            .securityGroup(sg.ref())
                            .build())
                    .build())
            .build();

    return PubSubConnectionConfig.udp("udp-conn")
        .publisherId(PublisherId.uint16(ushort(42)))
        .address(UdpDatagramAddress.multicast("239.0.0.5", 4841).networkInterface("en0"))
        .property(new QualifiedName(1, "connProp"), Variant.ofString("connVal"))
        .rawTransportSettings(encode(datagram2ConnectionTransport()))
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
                                    JsonDataSetMessageContentMask.Field.SequenceNumber,
                                    JsonDataSetMessageContentMask.Field.Timestamp))
                            .build())
                    .brokerTransport(
                        BrokerTransportSettings.builder()
                            .queueName("w-queue")
                            .metaDataQueueName("w-meta")
                            .metaDataUpdateTime(Duration.ofSeconds(2))
                            .resourceUri("w-res")
                            .authenticationProfileUri("w-auth")
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
                                    JsonDataSetMessageContentMask.Field.DataSetWriterId,
                                    JsonDataSetMessageContentMask.Field.Status))
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
        .rawTransportSettings(encode(vendorishBrokerConnectionTransport()))
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

  private static StandaloneSubscribedDataSetConfig standalone2() {
    return StandaloneSubscribedDataSetConfig.builder("sds-2")
        .metaData(DataSetMetaDataConfig.builder("md-empty").build())
        .subscribedDataSet(TargetVariablesConfig.builder().build())
        .build();
  }

  /**
   * A maximal authored config that is a fixed point of the round trip: UDP and MQTT connections,
   * UADP and JSON settings, all five PublisherId types, message security with a security group
   * reference, reader-level security overrides (including an explicit override to None), a security
   * group with a folder path and role permissions, config-level default Security Key Services,
   * broker transports at all three levels, node and key field addresses, promoted fields, an event
   * published dataset with a content filter referenced by an event writer with a non-default event
   * queue capacity, standalone subscribed datasets, target variables, properties at every level,
   * raw escape hatches built from namespace 0 types, and a published dataset with authored
   * DataTypeSchemaHeader type descriptions whose DataType NodeIds live in a non-zero namespace.
   */
  private static PubSubConfig maximalConfig() {
    SecurityGroupConfig sg = securityGroup();

    return PubSubConfig.builder()
        .connection(udpConnection(sg))
        .connection(mqttConnection())
        .publishedDataSet(publishedDataSet1())
        .publishedDataSet(publishedDataSet2())
        .publishedDataSet(publishedEventsDataSet())
        .publishedDataSet(customTypesDataSet())
        .standaloneSubscribedDataSet(standalone1())
        .standaloneSubscribedDataSet(standalone2())
        .securityGroup(sg)
        .defaultSecurityKeyService(defaultKeyServiceEndpoint())
        .property(new QualifiedName(1, "cfgProp"), Variant.ofString("cfgVal"))
        .build();
  }

  // endregion

  // region Round trip equality

  @Test
  void maximalConfigRoundTripsToFixedPoint() {
    NamespaceTable table = namespaceTable();
    PubSubConfig config = maximalConfig();

    PubSubConfiguration2DataType dataType = config.toDataType(table);
    PubSubConfig roundTripped = PubSubConfig.fromDataType(dataType, table);

    assertEquals(config.securityGroups(), roundTripped.securityGroups());
    assertEquals(config.defaultSecurityKeyServices(), roundTripped.defaultSecurityKeyServices());
    assertEquals(config.publishedDataSets(), roundTripped.publishedDataSets());
    assertEquals(
        config.standaloneSubscribedDataSets(), roundTripped.standaloneSubscribedDataSets());
    assertEquals(config.properties(), roundTripped.properties());
    assertEquals(config.connections().get(0), roundTripped.connections().get(0));
    assertEquals(config.connections().get(1), roundTripped.connections().get(1));
    assertEquals(config, roundTripped);
  }

  @Test
  void roundTripIsIdempotent() {
    NamespaceTable table = namespaceTable();
    PubSubConfig config = maximalConfig();

    PubSubConfig once = PubSubConfig.fromDataType(config.toDataType(table), table);
    PubSubConfig twice = PubSubConfig.fromDataType(once.toDataType(table), table);

    assertEquals(once, twice);
  }

  @Test
  void rawEscapeHatchesSurviveRoundTrip() {
    NamespaceTable table = namespaceTable();
    PubSubConfig config = maximalConfig();

    PubSubConfig roundTripped = PubSubConfig.fromDataType(config.toDataType(table), table);

    UdpConnectionConfig udp =
        assertInstanceOf(
            UdpConnectionConfig.class, roundTripped.connection("udp-conn").orElseThrow());
    assertNotNull(udp.rawTransportSettings());
    assertEquals(datagram2ConnectionTransport(), decode(udp.rawTransportSettings()));

    WriterGroupConfig wgUdp = udp.writerGroups().get(0);
    assertNotNull(wgUdp.getRawTransportSettings());
    assertEquals(datagram2WriterGroupTransport(), decode(wgUdp.getRawTransportSettings()));

    MqttConnectionConfig mqtt =
        assertInstanceOf(
            MqttConnectionConfig.class, roundTripped.connection("mqtt-conn").orElseThrow());
    assertNotNull(mqtt.rawTransportSettings());
    assertEquals(vendorishBrokerConnectionTransport(), decode(mqtt.rawTransportSettings()));
  }

  @Test
  void fractionalMillisecondDurationsRoundTrip() {
    NamespaceTable table = namespaceTable();

    PublishedDataSetConfig dataSet = publishedDataSet2();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.udp("c")
                    .publisherId(PublisherId.uint16(ushort(1)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", 4840))
                    .writerGroup(
                        WriterGroupConfig.builder("wg")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofNanos(333_500_000))
                            .keepAliveTime(Duration.ofMillis(2500))
                            .dataSetWriter(
                                DataSetWriterConfig.builder("w")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .build())
                            .build())
                    .readerGroup(
                        ReaderGroupConfig.builder("rg")
                            .dataSetReader(
                                DataSetReaderConfig.builder("r")
                                    .messageReceiveTimeout(Duration.ofNanos(1_500_000))
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubConfiguration2DataType dataType = config.toDataType(table);

    WriterGroupDataType wg = dataType.getConnections()[0].getWriterGroups()[0];
    assertEquals(333.5, wg.getPublishingInterval());
    assertEquals(2500.0, wg.getKeepAliveTime());

    DataSetReaderDataType reader =
        dataType.getConnections()[0].getReaderGroups()[0].getDataSetReaders()[0];
    assertEquals(1.5, reader.getMessageReceiveTimeout());

    assertEquals(config, PubSubConfig.fromDataType(dataType, table));
  }

  // endregion

  // region Wire shape spot checks

  @Test
  void writerGroupWireFields() {
    PubSubConfiguration2DataType dataType = maximalConfig().toDataType(namespaceTable());

    WriterGroupDataType wg = dataType.getConnections()[0].getWriterGroups()[0];

    assertEquals("wg-udp", wg.getName());
    assertEquals(Boolean.TRUE, wg.getEnabled());
    assertEquals(ushort(1), wg.getWriterGroupId());
    assertEquals(250.0, wg.getPublishingInterval());
    assertEquals(5000.0, wg.getKeepAliveTime());
    assertEquals(ubyte(3), wg.getPriority());
    assertEquals(uint(4096), wg.getMaxNetworkMessageSize());
    assertEquals(MessageSecurityMode.None, wg.getSecurityMode());
    assertEquals("SG-001", wg.getSecurityGroupId());
    assertNotNull(wg.getSecurityKeyServices());
    assertEquals(1, wg.getSecurityKeyServices().length);
    assertEquals(keyServiceEndpoint(), wg.getSecurityKeyServices()[0]);

    DataSetWriterDataType writer = wg.getDataSetWriters()[0];
    assertEquals("dsw-1", writer.getName());
    assertEquals(ushort(11), writer.getDataSetWriterId());
    assertEquals(uint(5), writer.getKeyFrameCount());
    assertEquals("pds-1", writer.getDataSetName());
    assertEquals(
        DataSetFieldContentMask.of(
            DataSetFieldContentMask.Field.StatusCode,
            DataSetFieldContentMask.Field.SourceTimestamp),
        writer.getDataSetFieldContentMask());
    // Part 14 defines no datagram DataSetWriter transport type; UDP writers emit null.
    assertNull(writer.getTransportSettings());

    DataSetWriterDataType disabled = wg.getDataSetWriters()[1];
    assertEquals(Boolean.FALSE, disabled.getEnabled());
  }

  @Test
  void transportProfileUriDerivation() {
    PubSubConfiguration2DataType dataType = maximalConfig().toDataType(namespaceTable());

    assertEquals(PROFILE_UDP_UADP, dataType.getConnections()[0].getTransportProfileUri());
    // The MQTT connection uses JSON settings on its groups, writers, and readers.
    assertEquals(PROFILE_MQTT_JSON, dataType.getConnections()[1].getTransportProfileUri());
  }

  @Test
  void mqttWithUadpSettingsDerivesMqttUadpProfile() {
    PublishedDataSetConfig dataSet = publishedDataSet2();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.mqtt("m")
                    .publisherId(PublisherId.string("pub"))
                    .brokerUri(URI.create("mqtt://broker.example:1883"))
                    .writerGroup(
                        WriterGroupConfig.builder("wg")
                            .writerGroupId(ushort(1))
                            .dataSetWriter(
                                DataSetWriterConfig.builder("w")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubConfiguration2DataType dataType = config.toDataType(namespaceTable());

    assertEquals(PROFILE_MQTT_UADP, dataType.getConnections()[0].getTransportProfileUri());
  }

  @Test
  void jsonReaderAloneSelectsMqttJsonProfile() {
    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.mqtt("m")
                    .brokerUri(URI.create("mqtt://broker.example:1883"))
                    .readerGroup(
                        ReaderGroupConfig.builder("rg")
                            .dataSetReader(
                                DataSetReaderConfig.builder("r")
                                    .settings(JsonDataSetReaderSettings.builder().build())
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubConfiguration2DataType dataType = config.toDataType(namespaceTable());

    assertEquals(PROFILE_MQTT_JSON, dataType.getConnections()[0].getTransportProfileUri());
  }

  @Test
  void udpAddressWireForm() {
    PubSubConfiguration2DataType dataType = maximalConfig().toDataType(namespaceTable());

    PubSubConnectionDataType udp = dataType.getConnections()[0];
    NetworkAddressUrlDataType address =
        assertInstanceOf(NetworkAddressUrlDataType.class, udp.getAddress());
    assertEquals("opc.udp://239.0.0.5:4841", address.getUrl());
    assertEquals("en0", address.getNetworkInterface());

    PubSubConnectionDataType mqtt = dataType.getConnections()[1];
    NetworkAddressUrlDataType brokerAddress =
        assertInstanceOf(NetworkAddressUrlDataType.class, mqtt.getAddress());
    assertEquals("mqtt://broker.example:1883", brokerAddress.getUrl());
    assertNull(brokerAddress.getNetworkInterface());
  }

  @Test
  void publisherIdVariantTyping() {
    PubSubConfiguration2DataType dataType = maximalConfig().toDataType(namespaceTable());

    Object udpConnectionId = dataType.getConnections()[0].getPublisherId().value();
    assertInstanceOf(UShort.class, udpConnectionId);
    assertEquals(ushort(42), udpConnectionId);

    Object mqttConnectionId = dataType.getConnections()[1].getPublisherId().value();
    assertInstanceOf(ULong.class, mqttConnectionId);
    assertEquals(ulong(123456789L), mqttConnectionId);

    DataSetReaderDataType[] udpReaders =
        dataType.getConnections()[0].getReaderGroups()[0].getDataSetReaders();
    Object readerByteId = udpReaders[0].getPublisherId().value();
    assertInstanceOf(UByte.class, readerByteId);
    assertEquals(ubyte(7), readerByteId);

    Object readerUInt32Id = udpReaders[1].getPublisherId().value();
    assertInstanceOf(UInteger.class, readerUInt32Id);
    assertEquals(uint(777), readerUInt32Id);

    Object readerStringId =
        dataType
            .getConnections()[1]
            .getReaderGroups()[0]
            .getDataSetReaders()[0]
            .getPublisherId()
            .value();
    assertEquals("publisher-x", readerStringId);
  }

  @Test
  void targetVariablesWireForm() {
    PubSubConfiguration2DataType dataType = maximalConfig().toDataType(namespaceTable());

    DataSetReaderDataType reader =
        dataType.getConnections()[0].getReaderGroups()[0].getDataSetReaders()[0];

    TargetVariablesDataType targetVariables =
        assertInstanceOf(TargetVariablesDataType.class, reader.getSubscribedDataSet());
    FieldTargetDataType[] targets = targetVariables.getTargetVariables();
    assertNotNull(targets);
    assertEquals(2, targets.length);

    FieldTargetDataType first = targets[0];
    assertEquals(MD_F1_ID, first.getDataSetFieldId());
    assertEquals("0:3", first.getReceiverIndexRange());
    assertEquals(new NodeId(1, "Target.Node1"), first.getTargetNodeId());
    assertEquals(uint(13), first.getAttributeId());
    assertEquals("1:2", first.getWriteIndexRange());
    assertEquals(OverrideValueHandling.OverrideValue, first.getOverrideValueHandling());
    assertEquals(Variant.ofDouble(99.5), first.getOverrideValue());

    FieldTargetDataType second = targets[1];
    assertEquals(MD_F2_ID, second.getDataSetFieldId());
    assertNull(second.getReceiverIndexRange());
    assertEquals(new NodeId(2, "Target.Node2"), second.getTargetNodeId());
    assertEquals(OverrideValueHandling.LastUsableValue, second.getOverrideValueHandling());
    assertEquals(Variant.ofNull(), second.getOverrideValue());

    // Reader scalar fields.
    assertEquals(5000.0, reader.getMessageReceiveTimeout());
    assertEquals(uint(10), reader.getKeyFrameCount());
    assertEquals(ushort(1), reader.getWriterGroupId());
    assertEquals(ushort(11), reader.getDataSetWriterId());
    assertEquals(MD_CLASS_ID, reader.getDataSetMetaData().getDataSetClassId());
    assertEquals(uint(3), reader.getDataSetMetaData().getConfigurationVersion().getMajorVersion());
    assertEquals(uint(9), reader.getDataSetMetaData().getConfigurationVersion().getMinorVersion());
  }

  @Test
  void fieldMetaDataBuiltInTypeDerivation() {
    PubSubConfiguration2DataType dataType = maximalConfig().toDataType(namespaceTable());

    PublishedDataSetDataType pds1 = dataType.getPublishedDataSets()[0];
    FieldMetaData[] fields = pds1.getDataSetMetaData().getFields();
    assertNotNull(fields);
    assertEquals(4, fields.length);

    assertEquals(ubyte(11), fields[0].getBuiltInType()); // Double
    assertEquals(NodeIds.Double, fields[0].getDataType());
    assertTrue(fields[0].getFieldFlags().getPromotedField());

    assertEquals(ubyte(6), fields[1].getBuiltInType()); // Int32
    assertEquals(NodeIds.Int32, fields[1].getDataType());

    assertEquals(ubyte(12), fields[2].getBuiltInType()); // String
    assertEquals(NodeIds.String, fields[2].getDataType());

    // Range is not a builtin type: builtInType falls back to ExtensionObject (22).
    assertEquals(ubyte(22), fields[3].getBuiltInType());
    assertEquals(NodeIds.Range, fields[3].getDataType());
    assertEquals(1, fields[3].getValueRank());
    assertEquals(uint(3), fields[3].getArrayDimensions()[0]);

    assertEquals(uint(2), pds1.getDataSetMetaData().getConfigurationVersion().getMajorVersion());
    assertEquals(uint(7), pds1.getDataSetMetaData().getConfigurationVersion().getMinorVersion());
  }

  @Test
  void keyFieldAddressUsesMiloSourceKeyProperty() {
    PubSubConfiguration2DataType dataType = maximalConfig().toDataType(namespaceTable());

    PublishedDataSetDataType pds1 = dataType.getPublishedDataSets()[0];
    FieldMetaData[] fields = pds1.getDataSetMetaData().getFields();
    PublishedDataItemsDataType source =
        assertInstanceOf(PublishedDataItemsDataType.class, pds1.getDataSetSource());
    PublishedVariableDataType[] publishedData = source.getPublishedData();
    assertNotNull(publishedData);

    // "temperature" is a node field: real NodeId, no MiloSourceKey property.
    assertEquals(new NodeId(1, "Sensor.Temperature"), publishedData[0].getPublishedVariable());
    assertEquals(uint(13), publishedData[0].getAttributeId());
    assertNull(propertyValue(fields[0].getProperties(), MILO_SOURCE_KEY));
    assertEquals(
        Variant.ofString("celsius"),
        propertyValue(fields[0].getProperties(), new QualifiedName(1, "unit")));

    // "label" has an explicit key: null published variable + reserved property.
    assertTrue(publishedData[2].getPublishedVariable().isNull());
    assertEquals(uint(13), publishedData[2].getAttributeId());
    assertEquals(
        Variant.ofString("label.key"), propertyValue(fields[2].getProperties(), MILO_SOURCE_KEY));

    // "envelope" defaulted to a key address keyed by the field name.
    assertTrue(publishedData[3].getPublishedVariable().isNull());
    assertEquals(
        Variant.ofString("envelope"), propertyValue(fields[3].getProperties(), MILO_SOURCE_KEY));
  }

  @Test
  void standaloneSubscribedDataSetRefWireForm() {
    PubSubConfiguration2DataType dataType = maximalConfig().toDataType(namespaceTable());

    DataSetReaderDataType reader =
        dataType.getConnections()[0].getReaderGroups()[0].getDataSetReaders()[1];

    StandaloneSubscribedDataSetRefDataType ref =
        assertInstanceOf(
            StandaloneSubscribedDataSetRefDataType.class, reader.getSubscribedDataSet());
    assertEquals("sds-1", ref.getDataSetName());

    assertEquals("sds-1", dataType.getSubscribedDataSets()[0].getName());
    TargetVariablesDataType sds1Targets =
        assertInstanceOf(
            TargetVariablesDataType.class,
            dataType.getSubscribedDataSets()[0].getSubscribedDataSet());
    assertEquals(1, sds1Targets.getTargetVariables().length);
    assertEquals(SDS_G1_ID, sds1Targets.getTargetVariables()[0].getDataSetFieldId());

    // An empty TargetVariables spec is preserved on a standalone dataset (the slot is mandatory).
    assertEquals("sds-2", dataType.getSubscribedDataSets()[1].getName());
    TargetVariablesDataType sds2Targets =
        assertInstanceOf(
            TargetVariablesDataType.class,
            dataType.getSubscribedDataSets()[1].getSubscribedDataSet());
    assertEquals(0, sds2Targets.getTargetVariables().length);
  }

  @Test
  void securityGroupIdResolution() {
    PubSubConfiguration2DataType dataType = maximalConfig().toDataType(namespaceTable());

    // The wire SecurityGroupId is the group's id, not its config name.
    assertEquals("SG-001", dataType.getConnections()[0].getWriterGroups()[0].getSecurityGroupId());
    assertEquals("SG-001", dataType.getConnections()[0].getReaderGroups()[0].getSecurityGroupId());

    SecurityGroupDataType sg = dataType.getSecurityGroups()[0];
    assertEquals("sg-1", sg.getName());
    assertEquals("SG-001", sg.getSecurityGroupId());
    assertEquals(30 * 60 * 1000.0, sg.getKeyLifetime());
    assertEquals(uint(2), sg.getMaxFutureKeyCount());
    assertEquals(uint(3), sg.getMaxPastKeyCount());
    assertEquals(
        "http://opcfoundation.org/UA/SecurityPolicy#Aes128_Sha256_RsaOaep",
        sg.getSecurityPolicyUri());
    assertArrayEquals(new String[] {"Folder", "SubFolder"}, sg.getSecurityGroupFolder());
    assertArrayEquals(new RolePermissionType[] {ROLE_PERMISSION}, sg.getRolePermissions());

    assertArrayEquals(
        new EndpointDescription[] {defaultKeyServiceEndpoint()},
        dataType.getDefaultSecurityKeyServices());
  }

  @Test
  void readerSecurityOverrideWireForm() {
    PubSubConfiguration2DataType dataType = maximalConfig().toDataType(namespaceTable());

    DataSetReaderDataType[] udpReaders =
        dataType.getConnections()[0].getReaderGroups()[0].getDataSetReaders();

    // dsr-1 has an active override; the ref name resolves to the wire SecurityGroupId.
    assertEquals(MessageSecurityMode.Sign, udpReaders[0].getSecurityMode());
    assertEquals("SG-001", udpReaders[0].getSecurityGroupId());
    assertArrayEquals(
        new EndpointDescription[] {keyServiceEndpoint()}, udpReaders[0].getSecurityKeyServices());

    // dsr-sds is an explicit override to None; the group reference keeps it an active override.
    assertEquals(MessageSecurityMode.None, udpReaders[1].getSecurityMode());
    assertEquals("SG-001", udpReaders[1].getSecurityGroupId());
    assertNull(udpReaders[1].getSecurityKeyServices());

    // dsr-json has no override: the Part 14 §6.2.9.9 sentinel is Invalid/null/null.
    DataSetReaderDataType jsonReader =
        dataType.getConnections()[1].getReaderGroups()[0].getDataSetReaders()[0];
    assertEquals(MessageSecurityMode.Invalid, jsonReader.getSecurityMode());
    assertNull(jsonReader.getSecurityGroupId());
    assertNull(jsonReader.getSecurityKeyServices());
  }

  @Test
  void readerOverrideToNoneSurvivesRoundTrip() {
    NamespaceTable table = namespaceTable();
    PubSubConfig config = maximalConfig();

    PubSubConfig roundTripped = PubSubConfig.fromDataType(config.toDataType(table), table);

    DataSetReaderConfig reader =
        roundTripped
            .connection("udp-conn")
            .orElseThrow()
            .readerGroups()
            .get(0)
            .getDataSetReaders()
            .get(1);

    MessageSecurityConfig security = reader.getMessageSecurity();
    assertNotNull(security);
    assertEquals(MessageSecurityMode.None, security.getMode());
    assertEquals(new SecurityGroupRef("sg-1"), security.getSecurityGroup());
    assertTrue(security.getKeyServices().isEmpty());
  }

  @Test
  void emptySecurityCollectionsEmitNull() {
    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp("c")
                    .address(UdpDatagramAddress.unicast("127.0.0.1", 4840))
                    .readerGroup(
                        ReaderGroupConfig.builder("rg")
                            .dataSetReader(DataSetReaderConfig.builder("r").build())
                            .build())
                    .build())
            .securityGroup(SecurityGroupConfig.builder("sg").build())
            .build();

    PubSubConfiguration2DataType dataType = config.toDataType(namespaceTable());

    // Empty collections are emitted as null arrays, not empty arrays.
    assertNull(dataType.getDefaultSecurityKeyServices());

    SecurityGroupDataType sg = dataType.getSecurityGroups()[0];
    assertNull(sg.getSecurityGroupFolder());
    assertNull(sg.getRolePermissions());

    DataSetReaderDataType reader =
        dataType.getConnections()[0].getReaderGroups()[0].getDataSetReaders()[0];
    assertNull(reader.getSecurityGroupId());
    assertNull(reader.getSecurityKeyServices());
  }

  // endregion

  // region Event datasets

  @Test
  void publishedEventsWireForm() {
    PubSubConfiguration2DataType dataType = maximalConfig().toDataType(namespaceTable());

    PublishedDataSetDataType pdsEvents = dataType.getPublishedDataSets()[2];
    assertEquals("pds-events", pdsEvents.getName());

    PublishedEventsDataType source =
        assertInstanceOf(PublishedEventsDataType.class, pdsEvents.getDataSetSource());
    // The canonical URI-form eventNotifier resolves to a local NodeId on export.
    assertEquals(new NodeId(1, "Events.Notifier"), source.getEventNotifier());
    assertArrayEquals(
        new SimpleAttributeOperand[] {baseEventOperand("Severity"), baseEventOperand("Message")},
        source.getSelectedFields());
    assertEquals(eventFilter(), source.getFilter());

    DataSetWriterDataType writer =
        dataType.getConnections()[0].getWriterGroups()[0].getDataSetWriters()[2];
    assertEquals("dsw-events", writer.getName());
    assertEquals(uint(0), writer.getKeyFrameCount());
    assertEquals(
        Variant.ofInt32(500),
        propertyValue(writer.getDataSetWriterProperties(), MILO_EVENT_QUEUE_CAPACITY));
  }

  @Test
  void eventWriterKeyFrameCountNormalizesToZeroOnExport() {
    PublishedDataSetConfig eventDataSet = publishedEventsDataSet();
    PublishedDataSetConfig dataDataSet = publishedDataSet2();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(eventDataSet)
            .publishedDataSet(dataDataSet)
            .connection(
                PubSubConnectionConfig.udp("c")
                    .publisherId(PublisherId.uint16(ushort(1)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", 4840))
                    .writerGroup(
                        WriterGroupConfig.builder("wg")
                            .writerGroupId(ushort(1))
                            .dataSetWriter(
                                DataSetWriterConfig.builder("w-event")
                                    .dataSet(eventDataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .keyFrameCount(uint(3))
                                    .build())
                            .dataSetWriter(
                                DataSetWriterConfig.builder("w-data")
                                    .dataSet(dataDataSet.ref())
                                    .dataSetWriterId(ushort(2))
                                    .keyFrameCount(uint(3))
                                    .build())
                            .build())
                    .build())
            .build();

    DataSetWriterDataType[] writers =
        config
            .toDataType(namespaceTable())
            .getConnections()[0]
            .getWriterGroups()[0]
            .getDataSetWriters();

    // Part 14 §6.2.4.3: event datasets are non-cyclic; export normalizes KeyFrameCount to 0
    // regardless of the authored value.
    assertEquals(uint(0), writers[0].getKeyFrameCount());
    // Writers referencing a data-items dataset keep their authored value.
    assertEquals(uint(3), writers[1].getKeyFrameCount());
  }

  @Test
  void eventQueueCapacityEmittedOnlyWhenNonDefault() {
    PublishedDataSetConfig eventDataSet = publishedEventsDataSet();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(eventDataSet)
            .connection(
                PubSubConnectionConfig.udp("c")
                    .publisherId(PublisherId.uint16(ushort(1)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", 4840))
                    .writerGroup(
                        WriterGroupConfig.builder("wg")
                            .writerGroupId(ushort(1))
                            .dataSetWriter(
                                DataSetWriterConfig.builder("w-default")
                                    .dataSet(eventDataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .keyFrameCount(uint(0))
                                    .build())
                            .dataSetWriter(
                                DataSetWriterConfig.builder("w-bounded")
                                    .dataSet(eventDataSet.ref())
                                    .dataSetWriterId(ushort(2))
                                    .keyFrameCount(uint(0))
                                    .eventQueueCapacity(7)
                                    .build())
                            .build())
                    .build())
            .build();

    DataSetWriterDataType[] writers =
        config
            .toDataType(namespaceTable())
            .getConnections()[0]
            .getWriterGroups()[0]
            .getDataSetWriters();

    // The default capacity emits no reserved MiloEventQueueCapacity pair at all.
    assertEquals(
        0, propertyCount(writers[0].getDataSetWriterProperties(), MILO_EVENT_QUEUE_CAPACITY));

    // A non-default capacity emits exactly one pair carrying an Int32 Variant.
    assertEquals(
        1, propertyCount(writers[1].getDataSetWriterProperties(), MILO_EVENT_QUEUE_CAPACITY));
    Variant capacity =
        propertyValue(writers[1].getDataSetWriterProperties(), MILO_EVENT_QUEUE_CAPACITY);
    assertInstanceOf(Integer.class, capacity.value());
    assertEquals(Variant.ofInt32(7), capacity);
  }

  @Test
  void explicitDefaultEventQueueCapacityDroppedOnReExport() {
    NamespaceTable table = namespaceTable();

    var wireDataSet =
        new PublishedDataSetDataType(
            "pds-events",
            null,
            null,
            null,
            new PublishedEventsDataType(new NodeId(1, "Events.Notifier"), null, null));
    var wireWriter =
        new DataSetWriterDataType(
            "w",
            true,
            ushort(1),
            null,
            uint(0),
            "pds-events",
            new KeyValuePair[] {new KeyValuePair(MILO_EVENT_QUEUE_CAPACITY, Variant.ofInt32(100))},
            null,
            null);
    var wireGroup =
        new WriterGroupDataType(
            "wg",
            true,
            MessageSecurityMode.None,
            null,
            null,
            uint(0),
            null,
            ushort(1),
            250.0,
            0.0,
            ubyte(0),
            null,
            null,
            null,
            null,
            new DataSetWriterDataType[] {wireWriter});
    var wireConnection =
        new PubSubConnectionDataType(
            "c",
            true,
            Variant.ofUInt16(ushort(9)),
            PROFILE_UDP_UADP,
            new NetworkAddressUrlDataType(null, "opc.udp://239.0.0.5:4841"),
            null,
            null,
            new WriterGroupDataType[] {wireGroup},
            null);
    var wireConfig =
        new PubSubConfiguration2DataType(
            new PublishedDataSetDataType[] {wireDataSet},
            new PubSubConnectionDataType[] {wireConnection},
            true,
            null,
            null,
            null,
            null,
            null,
            uint(0),
            null);

    PubSubConfig imported = PubSubConfig.fromDataType(wireConfig, table);

    // The reserved pair is consumed into eventQueueCapacity, not kept as a user property.
    DataSetWriterConfig writer =
        imported.connection("c").orElseThrow().writerGroups().get(0).getDataSetWriters().get(0);
    assertEquals(100, writer.getEventQueueCapacity());
    assertTrue(writer.getProperties().isEmpty());

    // An explicit wire value equal to the default is dropped on re-export.
    DataSetWriterDataType reExported =
        imported.toDataType(table).getConnections()[0].getWriterGroups()[0].getDataSetWriters()[0];
    assertNull(propertyValue(reExported.getDataSetWriterProperties(), MILO_EVENT_QUEUE_CAPACITY));
  }

  @Test
  void zeroFieldEventDataSetExportsEmptySelectedFields() {
    NamespaceTable table = namespaceTable();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(
                PublishedDataSetConfig.builder("pds-empty-events")
                    .source(
                        PublishedEventsConfig.builder(ExpandedNodeId.of(URI_1, "Events.Notifier"))
                            .build())
                    .build())
            .build();

    PublishedEventsDataType exported =
        assertInstanceOf(
            PublishedEventsDataType.class,
            config.toDataType(table).getPublishedDataSets()[0].getDataSetSource());

    // Canonical shape: an empty (non-null) array, matching the publishedData sibling convention.
    assertNotNull(exported.getSelectedFields());
    assertEquals(0, exported.getSelectedFields().length);
  }

  @Test
  void nullSelectedFieldsImportAsZeroFieldsAndReExportAsEmpty() {
    NamespaceTable table = namespaceTable();

    var wireConfig =
        new PubSubConfiguration2DataType(
            new PublishedDataSetDataType[] {
              new PublishedDataSetDataType(
                  "pds-null-events",
                  null,
                  null,
                  null,
                  new PublishedEventsDataType(new NodeId(1, "Events.Notifier"), null, null))
            },
            null,
            true,
            null,
            null,
            null,
            null,
            null,
            uint(0),
            null);

    PubSubConfig imported = PubSubConfig.fromDataType(wireConfig, table);

    PublishedEventsConfig source =
        assertInstanceOf(
            PublishedEventsConfig.class, imported.publishedDataSets().get(0).getSource());
    assertTrue(source.getFields().isEmpty());
    // A null wire filter imports as the canonical empty filter.
    assertEquals(new ContentFilter(null), source.getFilter());

    PublishedEventsDataType reExported =
        assertInstanceOf(
            PublishedEventsDataType.class,
            imported.toDataType(table).getPublishedDataSets()[0].getDataSetSource());
    assertNotNull(reExported.getSelectedFields());
    assertEquals(0, reExported.getSelectedFields().length);
  }

  @Test
  void eventDataSetMetaDataMatchesDataSetMetaDataMapperOutput() {
    PubSubConfiguration2DataType dataType = maximalConfig().toDataType(namespaceTable());

    PublishedDataSetDataType pdsEvents = dataType.getPublishedDataSets()[2];

    // The emitted metadata is exactly the DataSetMetaDataMapper derivation.
    assertEquals(
        DataSetMetaDataMapper.toDataSetMetaDataType(
            publishedEventsDataSet(), false, namespaceTable()),
        pdsEvents.getDataSetMetaData());

    FieldMetaData[] fields = pdsEvents.getDataSetMetaData().getFields();
    assertNotNull(fields);
    assertEquals(2, fields.length);

    assertEquals("severity", fields[0].getName());
    assertTrue(fields[0].getFieldFlags().getPromotedField());
    assertEquals(ubyte(5), fields[0].getBuiltInType()); // UInt16
    assertEquals(NodeIds.UInt16, fields[0].getDataType());
    assertEquals(FIELD_EVT_SEVERITY_ID, fields[0].getDataSetFieldId());

    assertEquals("message", fields[1].getName());
    assertFalse(fields[1].getFieldFlags().getPromotedField());
    assertEquals(ubyte(21), fields[1].getBuiltInType()); // LocalizedText
    assertEquals(NodeIds.LocalizedText, fields[1].getDataType());
    assertEquals(FIELD_EVT_MESSAGE_ID, fields[1].getDataSetFieldId());
    assertEquals(
        Variant.ofString("evtVal"),
        propertyValue(fields[1].getProperties(), new QualifiedName(1, "evtFieldProp")));

    // Event fields have no field address: no MiloSourceKey property is emitted.
    assertNull(propertyValue(fields[0].getProperties(), MILO_SOURCE_KEY));
    assertNull(propertyValue(fields[1].getProperties(), MILO_SOURCE_KEY));

    ConfigurationVersionDataType version = pdsEvents.getDataSetMetaData().getConfigurationVersion();
    assertEquals(uint(4), version.getMajorVersion());
    assertEquals(uint(13), version.getMinorVersion());
  }

  // endregion

  // region Namespace handling

  @Test
  void unresolvableNamespaceUriThrowsOnToDataType() {
    PubSubConfig config = maximalConfig();

    // A table without URI_1/URI_2 cannot resolve the authored field addresses.
    assertThrows(
        PubSubConfigValidationException.class, () -> config.toDataType(new NamespaceTable()));
  }

  @Test
  void namespaceUriResolvedToIndex() {
    PubSubConfiguration2DataType dataType = maximalConfig().toDataType(namespaceTable());

    PublishedDataItemsDataType source =
        assertInstanceOf(
            PublishedDataItemsDataType.class,
            dataType.getPublishedDataSets()[0].getDataSetSource());

    // URI_1 is at index 1, URI_2 at index 2 in the test namespace table.
    assertEquals(
        new NodeId(1, "Sensor.Temperature"), source.getPublishedData()[0].getPublishedVariable());
    assertEquals(new NodeId(1, uint(5001)), source.getPublishedData()[1].getPublishedVariable());
  }

  @Test
  void dataTypeSchemaHeaderWireFormUsesMetaDataLocalIndexes() {
    NamespaceTable table = namespaceTable();
    PubSubConfiguration2DataType dataType = maximalConfig().toDataType(table);

    DataSetMetaDataType metaData = dataType.getPublishedDataSets()[3].getDataSetMetaData();

    // Only URI_2 is referenced by the metadata, so the namespaces array holds just that URI and
    // publisher-local index 2 remaps to metadata-local index 1 (OPC 10000-5 §12.31: entry k maps
    // to NamespaceIndex k + 1; namespace 0 is never included).
    assertArrayEquals(new String[] {URI_2}, metaData.getNamespaces());
    assertEquals(new NodeId(1, 3001), metaData.getFields()[0].getDataType());

    StructureDescription structure = metaData.getStructureDataTypes()[0];
    assertEquals(new NodeId(1, 3001), structure.getDataTypeId());
    assertEquals(new QualifiedName(1, "Position"), structure.getName());
    assertEquals(new NodeId(1, 3003), structure.getStructureDefinition().getDefaultEncodingId());
    assertEquals(NodeIds.Structure, structure.getStructureDefinition().getBaseDataType());
    assertEquals(NodeIds.Double, structure.getStructureDefinition().getFields()[0].getDataType());

    assertEquals(new NodeId(1, 4001), metaData.getEnumDataTypes()[0].getDataTypeId());
    assertEquals(new QualifiedName(1, "Mode"), metaData.getEnumDataTypes()[0].getName());
    assertEquals(new NodeId(1, 5001), metaData.getSimpleDataTypes()[0].getDataTypeId());
    assertEquals(NodeIds.Double, metaData.getSimpleDataTypes()[0].getBaseDataType());

    // The import direction restores publisher-local indexes and the authored descriptions.
    PubSubConfig imported = PubSubConfig.fromDataType(dataType, table);
    assertEquals(customTypesDataSet(), imported.publishedDataSet("pds-custom").orElseThrow());
  }

  @Test
  void unresolvableMetaDataNamespaceThrowsOnFromDataType() {
    PubSubConfiguration2DataType dataType = maximalConfig().toDataType(namespaceTable());

    // A table without URI_2 cannot resolve the metadata namespaces array back to local indexes.
    assertThrows(
        PubSubConfigValidationException.class,
        () -> PubSubConfig.fromDataType(dataType, new NamespaceTable(URI_1)));
  }

  // endregion

  // region Selector normalization

  @Test
  void nameAndIndexSelectorsNormalizeToIdSelectors() {
    NamespaceTable table = namespaceTable();

    DataSetMetaDataConfig metaData = readerMetaData();

    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp("c")
                    .address(UdpDatagramAddress.unicast("127.0.0.1", 4840))
                    .readerGroup(
                        ReaderGroupConfig.builder("rg")
                            .dataSetReader(
                                DataSetReaderConfig.builder("r")
                                    .dataSetMetaData(metaData)
                                    .subscribedDataSet(
                                        TargetVariablesConfig.builder()
                                            .map(
                                                FieldSelector.byName("f1"),
                                                TargetVariableConfig.builder()
                                                    .target(
                                                        new NodeFieldAddress(
                                                            ExpandedNodeId.of(URI_1, "t1"),
                                                            AttributeId.Value))
                                                    .build())
                                            .map(
                                                FieldSelector.byIndex(1),
                                                TargetVariableConfig.builder()
                                                    .target(
                                                        new NodeFieldAddress(
                                                            ExpandedNodeId.of(URI_1, "t2"),
                                                            AttributeId.Value))
                                                    .build())
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubConfig roundTripped = PubSubConfig.fromDataType(config.toDataType(table), table);

    DataSetReaderConfig reader =
        roundTripped.connection("c").orElseThrow().readerGroups().get(0).getDataSetReaders().get(0);
    TargetVariablesConfig targetVariables =
        assertInstanceOf(TargetVariablesConfig.class, reader.getSubscribedDataSet());

    FieldIdSelector first =
        assertInstanceOf(FieldIdSelector.class, targetVariables.getMappings().get(0).selector());
    assertEquals(MD_F1_ID, first.fieldId());

    FieldIdSelector second =
        assertInstanceOf(FieldIdSelector.class, targetVariables.getMappings().get(1).selector());
    assertEquals(MD_F2_ID, second.fieldId());
  }

  @Test
  void nameSelectorWithoutMetadataThrowsOnToDataType() {
    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp("c")
                    .address(UdpDatagramAddress.unicast("127.0.0.1", 4840))
                    .readerGroup(
                        ReaderGroupConfig.builder("rg")
                            .dataSetReader(
                                DataSetReaderConfig.builder("r")
                                    .subscribedDataSet(
                                        TargetVariablesConfig.builder()
                                            .map(
                                                FieldSelector.byName("f1"),
                                                TargetVariableConfig.builder()
                                                    .target(
                                                        new NodeFieldAddress(
                                                            ExpandedNodeId.of(URI_1, "t1"),
                                                            AttributeId.Value))
                                                    .build())
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    assertThrows(PubSubConfigValidationException.class, () -> config.toDataType(namespaceTable()));
  }

  // endregion

  // region Raw escape hatch error paths

  @Test
  void rawSettingsWithoutCodecThrowsOnToDataType() {
    ExtensionObject vendorSettings =
        ExtensionObject.of(ByteString.of(new byte[] {1, 2, 3}), new NodeId(1, "VendorEncoding"));

    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp("c")
                    .address(UdpDatagramAddress.unicast("127.0.0.1", 4840))
                    .rawTransportSettings(vendorSettings)
                    .build())
            .build();

    assertThrows(PubSubConfigValidationException.class, () -> config.toDataType(namespaceTable()));
  }

  @Test
  void rawSettingsOfWrongTypeThrowsOnToDataType() {
    PublishedDataSetConfig dataSet = publishedDataSet2();

    // A ConnectionTransportDataType is not a valid WriterGroup transport settings type.
    ExtensionObject wrongType = encode(vendorishBrokerConnectionTransport());

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.udp("c")
                    .publisherId(PublisherId.uint16(ushort(1)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", 4840))
                    .writerGroup(
                        WriterGroupConfig.builder("wg")
                            .writerGroupId(ushort(1))
                            .rawTransportSettings(wrongType)
                            .dataSetWriter(
                                DataSetWriterConfig.builder("w")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .build())
                            .build())
                    .build())
            .build();

    assertThrows(PubSubConfigValidationException.class, () -> config.toDataType(namespaceTable()));
  }

  // endregion

  private static Variant propertyValue(KeyValuePair[] pairs, QualifiedName key) {
    if (pairs == null) {
      return null;
    }
    for (KeyValuePair pair : pairs) {
      if (pair != null && key.equals(pair.getKey())) {
        return pair.getValue();
      }
    }
    return null;
  }

  private static int propertyCount(KeyValuePair[] pairs, QualifiedName key) {
    if (pairs == null) {
      return 0;
    }
    int count = 0;
    for (KeyValuePair pair : pairs) {
      if (pair != null && key.equals(pair.getKey())) {
        count++;
      }
    }
    return count;
  }
}
