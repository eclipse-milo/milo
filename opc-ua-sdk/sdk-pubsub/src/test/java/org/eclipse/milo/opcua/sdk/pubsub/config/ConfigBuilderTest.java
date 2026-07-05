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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ConfigBuilderTest {

  private static final RolePermissionType ROLE_PERMISSION =
      new RolePermissionType(
          NodeIds.WellKnownRole_SecurityAdmin, PermissionType.of(PermissionType.Field.Call));

  private static ExtensionObject extensionObject(int id) {
    return ExtensionObject.of(ByteString.of(new byte[] {(byte) id}), new NodeId(0, id));
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

  private static DataSetWriterConfig writer(String name, int writerId, String dataSetName) {
    return DataSetWriterConfig.builder(name)
        .dataSet(new PublishedDataSetRef(dataSetName))
        .dataSetWriterId(ushort(writerId))
        .build();
  }

  private static DataSetMetaDataConfig metaData(String name) {
    return DataSetMetaDataConfig.builder(name).field("f1", NodeIds.Int32, new UUID(0L, 1L)).build();
  }

  private static TargetVariablesConfig targetVariables() {
    return TargetVariablesConfig.builder()
        .map(
            FieldSelector.byName("f1"),
            TargetVariableConfig.builder()
                .target(NodeFieldAddress.parse("nsu=urn:milo:test;s=Target", AttributeId.Value))
                .build())
        .build();
  }

  /** A {@link WriterGroupConfig.Builder} with every field set to a non-default value. */
  private static WriterGroupConfig.Builder fullWriterGroupBuilder(String name) {
    return WriterGroupConfig.builder(name)
        .enabled(false)
        .writerGroupId(ushort(10))
        .publishingInterval(Duration.ofMillis(250))
        .keepAliveTime(Duration.ofSeconds(5))
        .priority(ubyte(3))
        .maxNetworkMessageSize(uint(8192))
        .messageSecurity(
            MessageSecurityConfig.builder()
                .mode(MessageSecurityMode.Sign)
                .securityGroup(new SecurityGroupRef("sg"))
                .build())
        .messageSettings(
            UadpWriterGroupSettings.builder()
                .groupVersion(uint(7))
                .dataSetOrdering(DataSetOrderingType.AscendingWriterId)
                .networkMessageContentMask(
                    UadpNetworkMessageContentMask.of(
                        UadpNetworkMessageContentMask.Field.PublisherId))
                .build())
        .brokerTransport(BrokerTransportSettings.builder().queueName("queue").build())
        .dataSetWriter(writer("w1", 1, "ds"))
        .rawTransportSettings(extensionObject(1))
        .rawMessageSettings(extensionObject(2))
        .property(new QualifiedName(1, "p"), Variant.ofInt32(42));
  }

  @Nested
  class LocalValidation {

    @Test
    void writerGroupRequiresNonBlankName() {
      assertThrows(
          PubSubConfigValidationException.class,
          () -> WriterGroupConfig.builder("").writerGroupId(ushort(1)).build());
      assertThrows(
          PubSubConfigValidationException.class,
          () -> WriterGroupConfig.builder("  ").writerGroupId(ushort(1)).build());
    }

    @Test
    void writerGroupRequiresWriterGroupId() {
      PubSubConfigValidationException e =
          assertThrows(
              PubSubConfigValidationException.class, () -> WriterGroupConfig.builder("wg").build());

      assertTrue(e.getMessage().contains("wg"), e.getMessage());
    }

    @Test
    void writerGroupRejectsZeroWriterGroupId() {
      assertThrows(
          PubSubConfigValidationException.class,
          () -> WriterGroupConfig.builder("wg").writerGroupId(ushort(0)).build());
    }

    @Test
    void writerGroupRejectsNonPositivePublishingInterval() {
      assertThrows(
          PubSubConfigValidationException.class,
          () ->
              WriterGroupConfig.builder("wg")
                  .writerGroupId(ushort(1))
                  .publishingInterval(Duration.ZERO)
                  .build());
      assertThrows(
          PubSubConfigValidationException.class,
          () ->
              WriterGroupConfig.builder("wg")
                  .writerGroupId(ushort(1))
                  .publishingInterval(Duration.ofMillis(-1))
                  .build());
    }

    @Test
    void writerGroupRejectsNonPositiveKeepAliveTime() {
      assertThrows(
          PubSubConfigValidationException.class,
          () ->
              WriterGroupConfig.builder("wg")
                  .writerGroupId(ushort(1))
                  .keepAliveTime(Duration.ZERO)
                  .build());
      assertThrows(
          PubSubConfigValidationException.class,
          () ->
              WriterGroupConfig.builder("wg")
                  .writerGroupId(ushort(1))
                  .keepAliveTime(Duration.ofMillis(-1))
                  .build());
    }

    @Test
    void dataSetWriterRequiresNonBlankName() {
      assertThrows(
          PubSubConfigValidationException.class,
          () ->
              DataSetWriterConfig.builder(" ")
                  .dataSet(new PublishedDataSetRef("ds"))
                  .dataSetWriterId(ushort(1))
                  .build());
    }

    @Test
    void dataSetWriterRequiresDataSet() {
      PubSubConfigValidationException e =
          assertThrows(
              PubSubConfigValidationException.class,
              () -> DataSetWriterConfig.builder("w").dataSetWriterId(ushort(1)).build());

      assertTrue(e.getMessage().contains("dataSet"), e.getMessage());
    }

    @Test
    void dataSetWriterRequiresDataSetWriterId() {
      assertThrows(
          PubSubConfigValidationException.class,
          () -> DataSetWriterConfig.builder("w").dataSet(new PublishedDataSetRef("ds")).build());
    }

    @Test
    void dataSetWriterRejectsZeroDataSetWriterId() {
      assertThrows(
          PubSubConfigValidationException.class,
          () ->
              DataSetWriterConfig.builder("w")
                  .dataSet(new PublishedDataSetRef("ds"))
                  .dataSetWriterId(ushort(0))
                  .build());
    }

    @Test
    void readerGroupRequiresNonBlankName() {
      assertThrows(
          PubSubConfigValidationException.class, () -> ReaderGroupConfig.builder("").build());
    }

    @Test
    void dataSetReaderRequiresNonBlankName() {
      assertThrows(
          PubSubConfigValidationException.class, () -> DataSetReaderConfig.builder(" ").build());
    }

    @Test
    void dataSetReaderRejectsNegativeMessageReceiveTimeout() {
      assertThrows(
          PubSubConfigValidationException.class,
          () ->
              DataSetReaderConfig.builder("r")
                  .messageReceiveTimeout(Duration.ofMillis(-1))
                  .build());
    }

    @Test
    void udpConnectionRequiresNonBlankName() {
      assertThrows(
          PubSubConfigValidationException.class,
          () ->
              UdpConnectionConfig.builder("")
                  .address(UdpDatagramAddress.unicast("127.0.0.1", 4840))
                  .build());
    }

    @Test
    void udpConnectionRequiresAddress() {
      PubSubConfigValidationException e =
          assertThrows(
              PubSubConfigValidationException.class,
              () -> UdpConnectionConfig.builder("conn").build());

      assertTrue(e.getMessage().contains("address"), e.getMessage());
    }

    @Test
    void mqttConnectionRequiresNonBlankName() {
      assertThrows(
          PubSubConfigValidationException.class, () -> MqttConnectionConfig.builder(" ").build());
    }

    @Test
    void mqttConnectionRequiresBrokerUri() {
      PubSubConfigValidationException e =
          assertThrows(
              PubSubConfigValidationException.class,
              () -> MqttConnectionConfig.builder("conn").build());

      assertTrue(e.getMessage().contains("brokerUri"), e.getMessage());
    }

    @Test
    void mqttPublisherStatusModeDefaultsToAutoAndCopiesThroughBuilder() {
      MqttConnectionConfig connection =
          MqttConnectionConfig.builder("conn").brokerUri(URI.create("mqtt://broker:1883")).build();

      assertEquals(PublisherStatusMode.AUTO, connection.getPublisherStatusMode());

      MqttConnectionConfig disabled =
          connection.toBuilder().publisherStatusMode(PublisherStatusMode.DISABLED).build();

      assertEquals(PublisherStatusMode.DISABLED, disabled.getPublisherStatusMode());
      assertEquals(
          PublisherStatusMode.DISABLED, disabled.toBuilder().build().getPublisherStatusMode());
    }

    @Test
    void mqttPublisherStatusModeRejectsNull() {
      assertThrows(
          NullPointerException.class,
          () ->
              MqttConnectionConfig.builder("conn")
                  .brokerUri(URI.create("mqtt://broker:1883"))
                  .publisherStatusMode(null));
    }

    @Test
    void fieldDefinitionRejectsEmptyName() {
      assertThrows(
          PubSubConfigValidationException.class, () -> FieldDefinition.builder("").build());
    }

    @Test
    void publishedDataSetRejectsEmptyName() {
      assertThrows(
          PubSubConfigValidationException.class, () -> PublishedDataSetConfig.builder("").build());
    }

    @Test
    void publishedDataSetRejectsDuplicateFieldName() {
      PubSubConfigValidationException e =
          assertThrows(
              PubSubConfigValidationException.class,
              () ->
                  PublishedDataSetConfig.builder("ds")
                      .field(FieldDefinition.builder("f1").build())
                      .field(FieldDefinition.builder("f1").build())
                      .build());

      assertTrue(e.getMessage().contains("f1"), e.getMessage());
    }

    @Test
    void publishedDataSetRejectsDuplicateDataSetFieldId() {
      UUID fieldId = new UUID(0L, 1L);

      assertThrows(
          PubSubConfigValidationException.class,
          () ->
              PublishedDataSetConfig.builder("ds")
                  .field(FieldDefinition.builder("f1").dataSetFieldId(fieldId).build())
                  .field(FieldDefinition.builder("f2").dataSetFieldId(fieldId).build())
                  .build());
    }

    @Test
    void dataSetMetaDataRejectsDuplicateFieldName() {
      assertThrows(
          PubSubConfigValidationException.class,
          () ->
              DataSetMetaDataConfig.builder("md")
                  .field("f1", NodeIds.Int32)
                  .field("f1", NodeIds.String)
                  .build());
    }

    @Test
    void dataSetMetaDataRejectsDuplicateDataSetFieldId() {
      UUID fieldId = new UUID(0L, 1L);

      assertThrows(
          PubSubConfigValidationException.class,
          () ->
              DataSetMetaDataConfig.builder("md")
                  .field("f1", NodeIds.Int32, fieldId)
                  .field("f2", NodeIds.String, fieldId)
                  .build());
    }

    @Test
    void standaloneSubscribedDataSetRejectsEmptyName() {
      assertThrows(
          PubSubConfigValidationException.class,
          () ->
              StandaloneSubscribedDataSetConfig.builder("")
                  .metaData(metaData("md"))
                  .subscribedDataSet(targetVariables())
                  .build());
    }

    @Test
    void standaloneSubscribedDataSetRequiresMetaData() {
      assertThrows(
          PubSubConfigValidationException.class,
          () ->
              StandaloneSubscribedDataSetConfig.builder("sds")
                  .subscribedDataSet(targetVariables())
                  .build());
    }

    @Test
    void standaloneSubscribedDataSetRequiresSubscribedDataSet() {
      assertThrows(
          PubSubConfigValidationException.class,
          () -> StandaloneSubscribedDataSetConfig.builder("sds").metaData(metaData("md")).build());
    }

    @Test
    void standaloneSubscribedDataSetRejectsSelfReference() {
      PubSubConfigValidationException e =
          assertThrows(
              PubSubConfigValidationException.class,
              () ->
                  StandaloneSubscribedDataSetConfig.builder("sds")
                      .metaData(metaData("md"))
                      .subscribedDataSet(new StandaloneSubscribedDataSetRef("sds"))
                      .build());

      assertTrue(e.getMessage().contains("sds"), e.getMessage());
    }

    @Test
    void targetVariableRequiresTarget() {
      assertThrows(
          PubSubConfigValidationException.class, () -> TargetVariableConfig.builder().build());
    }

    @Test
    void fieldIndexSelectorRejectsNegativeIndex() {
      assertThrows(IllegalArgumentException.class, () -> FieldSelector.byIndex(-1));
    }
  }

  @Nested
  class Defaults {

    @Test
    void writerGroupDefaults() {
      WriterGroupConfig group = WriterGroupConfig.builder("wg").writerGroupId(ushort(1)).build();

      assertTrue(group.isEnabled());
      assertEquals(Duration.ofMillis(1000), group.getPublishingInterval());
      assertNull(group.getKeepAliveTime());
      assertEquals(ubyte(0), group.getPriority());
      // 0 = no enforced limit; the pre-HG1 default of 1400 would newly reject messages in the
      // 1400-2048 byte range that always worked end-to-end
      assertEquals(uint(0), group.getMaxNetworkMessageSize());
      assertNull(group.getMessageSecurity());
      assertNull(group.getBrokerTransport());
      assertNull(group.getRawTransportSettings());
      assertNull(group.getRawMessageSettings());
      assertTrue(group.getDataSetWriters().isEmpty());
      assertTrue(group.getProperties().isEmpty());
      assertEquals(UadpWriterGroupSettings.builder().build(), group.getMessageSettings());
    }

    @Test
    void dataSetWriterDefaults() {
      DataSetWriterConfig dataSetWriter = writer("w", 1, "ds");

      assertTrue(dataSetWriter.isEnabled());
      assertEquals(uint(1), dataSetWriter.getKeyFrameCount());
      assertEquals(DataSetFieldContentMask.of(), dataSetWriter.getFieldContentMask());
      assertEquals(UadpDataSetWriterSettings.builder().build(), dataSetWriter.getSettings());
      assertNull(dataSetWriter.getBrokerTransport());
      assertNull(dataSetWriter.getRawTransportSettings());
      assertNull(dataSetWriter.getRawMessageSettings());
    }

    @Test
    void dataSetReaderDefaults() {
      DataSetReaderConfig reader = DataSetReaderConfig.builder("r").build();

      assertTrue(reader.isEnabled());
      assertNull(reader.getPublisherId());
      assertNull(reader.getWriterGroupId());
      assertNull(reader.getDataSetWriterId());
      assertNull(reader.getDataSetMetaData());
      assertEquals(MetadataPolicy.REQUIRE_CONFIGURED, reader.getMetadataPolicy());
      assertNull(reader.getSubscribedDataSet());
      // No override: the reader inherits message security from its reader group.
      assertNull(reader.getMessageSecurity());
      assertEquals(Duration.ZERO, reader.getMessageReceiveTimeout());
      assertEquals(uint(0), reader.getKeyFrameCount());
      assertEquals(UadpDataSetReaderSettings.builder().build(), reader.getSettings());
    }

    @Test
    void readerGroupDefaults() {
      ReaderGroupConfig group = ReaderGroupConfig.builder("rg").build();

      assertTrue(group.isEnabled());
      assertEquals(uint(0), group.getMaxNetworkMessageSize());
      assertNull(group.getMessageSecurity());
    }

    @Test
    void securityGroupDefaults() {
      SecurityGroupConfig group = SecurityGroupConfig.builder("sg").build();

      assertEquals("sg", group.getName());
      // securityGroupId defaults to the name when not explicitly configured
      assertEquals("sg", group.getSecurityGroupId());
      assertTrue(group.getSecurityGroupFolder().isEmpty());
      assertNull(group.getSecurityPolicyUri());
      assertEquals(Duration.ofHours(1), group.getKeyLifeTime());
      assertEquals(uint(0), group.getMaxFutureKeyCount());
      assertEquals(uint(0), group.getMaxPastKeyCount());
      assertTrue(group.getKeyServices().isEmpty());
      assertTrue(group.getRolePermissions().isEmpty());
      assertTrue(group.getProperties().isEmpty());
    }

    @Test
    void uadpWriterGroupSettingsDefaultToUadpDynamicProfile() {
      UadpWriterGroupSettings settings = UadpWriterGroupSettings.builder().build();

      assertEquals(uint(0), settings.getGroupVersion());
      assertEquals(DataSetOrderingType.Undefined, settings.getDataSetOrdering());

      // Annex A.2.2 "UADP-Dynamic": PublisherId + PayloadHeader = 0x41
      UadpNetworkMessageContentMask mask = settings.getNetworkMessageContentMask();
      assertEquals(0x41L, mask.getValue().longValue());
      assertTrue(mask.getPublisherId());
      assertTrue(mask.getPayloadHeader());
      assertFalse(mask.getGroupHeader());
      assertFalse(mask.getTimestamp());
      assertFalse(mask.getPromotedFields());
    }

    @Test
    void uadpDataSetWriterSettingsDefaultToUadpDynamicProfile() {
      UadpDataSetWriterSettings settings = UadpDataSetWriterSettings.builder().build();

      assertEquals(ushort(0), settings.getConfiguredSize());
      assertEquals(ushort(0), settings.getNetworkMessageNumber());
      assertEquals(ushort(0), settings.getDataSetOffset());

      // Annex A.2.2 "UADP-Dynamic": Timestamp + Status + MinorVersion + SequenceNumber = 0x35
      UadpDataSetMessageContentMask mask = settings.getDataSetMessageContentMask();
      assertEquals(0x35L, mask.getValue().longValue());
      assertTrue(mask.getTimestamp());
      assertTrue(mask.getStatus());
      assertTrue(mask.getMinorVersion());
      assertTrue(mask.getSequenceNumber());
      assertFalse(mask.getMajorVersion());
      assertFalse(mask.getPicoSeconds());
    }

    @Test
    void uadpDataSetReaderSettingsDefaultsMatchWriterSideDefaults() {
      UadpDataSetReaderSettings settings = UadpDataSetReaderSettings.builder().build();

      assertEquals(uint(0), settings.getGroupVersion());
      assertEquals(ushort(0), settings.getNetworkMessageNumber());
      assertEquals(ushort(0), settings.getDataSetOffset());
      assertEquals(
          UadpWriterGroupSettings.UADP_DYNAMIC_NETWORK_MESSAGE_CONTENT_MASK,
          settings.getNetworkMessageContentMask());
      assertEquals(
          UadpDataSetWriterSettings.UADP_DYNAMIC_DATA_SET_MESSAGE_CONTENT_MASK,
          settings.getDataSetMessageContentMask());
    }

    @Test
    void jsonSettingsDefaultToEmptyContentMasks() {
      assertEquals(
          0L,
          JsonWriterGroupSettings.builder()
              .build()
              .getNetworkMessageContentMask()
              .getValue()
              .longValue());
      assertEquals(
          0L,
          JsonDataSetWriterSettings.builder()
              .build()
              .getDataSetMessageContentMask()
              .getValue()
              .longValue());
      assertEquals(
          0L,
          JsonDataSetReaderSettings.builder()
              .build()
              .getNetworkMessageContentMask()
              .getValue()
              .longValue());
      assertEquals(
          0L,
          JsonDataSetReaderSettings.builder()
              .build()
              .getDataSetMessageContentMask()
              .getValue()
              .longValue());
    }

    @Test
    void fieldDefinitionDefaults() {
      FieldDefinition field = FieldDefinition.builder("temperature").build();

      assertEquals("temperature", field.getName());
      assertEquals(new KeyFieldAddress("temperature"), field.getSource());
      assertEquals(NodeIds.BaseDataType, field.getDataType());
      assertEquals(-1, field.getValueRank());
      assertNull(field.getArrayDimensions());
      assertFalse(field.isPromoted());
      assertTrue(field.getProperties().isEmpty());
    }

    @Test
    void fieldDefinitionGeneratesRandomDataSetFieldIdPerBuild() {
      FieldDefinition first = FieldDefinition.builder("f").build();
      FieldDefinition second = FieldDefinition.builder("f").build();

      assertNotEquals(first.getDataSetFieldId(), second.getDataSetFieldId());
    }

    @Test
    void fieldDefinitionRetainsConfiguredDataSetFieldId() {
      UUID fieldId = new UUID(1L, 2L);

      FieldDefinition field = FieldDefinition.builder("f").dataSetFieldId(fieldId).build();

      assertEquals(fieldId, field.getDataSetFieldId());
    }

    @Test
    void fieldDefinitionRetainsConfiguredSource() {
      NodeFieldAddress source =
          NodeFieldAddress.parse("nsu=urn:milo:test;s=Sensor", AttributeId.Value);

      FieldDefinition field = FieldDefinition.builder("f").source(source).build();

      assertEquals(source, field.getSource());
    }

    @Test
    void publishedDataSetConfigurationVersionDefaults() {
      PublishedDataSetConfig dataSet = PublishedDataSetConfig.builder("ds").build();

      assertEquals(uint(1), dataSet.getConfigurationVersionMajor());
      assertEquals(uint(1), dataSet.getConfigurationVersionMinor());
      assertTrue(dataSet.isEnabled());
    }

    @Test
    void dataSetMetaDataConfigurationVersionDefaults() {
      DataSetMetaDataConfig metaData = DataSetMetaDataConfig.builder("md").build();

      assertEquals(uint(1), metaData.getConfigurationVersionMajor());
      assertEquals(uint(1), metaData.getConfigurationVersionMinor());
      assertTrue(metaData.getDataSetClassId().isEmpty());
    }

    @Test
    void pubSubConfigDefaults() {
      PubSubConfig config = PubSubConfig.builder().build();

      assertTrue(config.isEnabled());
      assertTrue(config.connections().isEmpty());
      assertTrue(config.publishedDataSets().isEmpty());
      assertTrue(config.standaloneSubscribedDataSets().isEmpty());
      assertTrue(config.securityGroups().isEmpty());
      assertTrue(config.defaultSecurityKeyServices().isEmpty());
      assertTrue(config.properties().isEmpty());
    }
  }

  @Nested
  class ToBuilderRoundTrips {

    @Test
    void writerGroupToBuilderRoundTrip() {
      WriterGroupConfig group = fullWriterGroupBuilder("wg").build();
      WriterGroupConfig copy = group.toBuilder().build();

      assertEquals(group, copy);
      assertEquals(group.hashCode(), copy.hashCode());
    }

    @Test
    void dataSetReaderToBuilderRoundTrip() {
      DataSetReaderConfig reader =
          DataSetReaderConfig.builder("r")
              .enabled(false)
              .publisherId(PublisherId.uint16(ushort(1)))
              .writerGroupId(ushort(2))
              .dataSetWriterId(ushort(3))
              .dataSetMetaData(metaData("md"))
              .metadataPolicy(MetadataPolicy.ACCEPT_DISCOVERED)
              .subscribedDataSet(targetVariables())
              .settings(UadpDataSetReaderSettings.builder().groupVersion(uint(9)).build())
              .brokerTransport(BrokerTransportSettings.builder().queueName("q").build())
              .messageSecurity(
                  MessageSecurityConfig.builder()
                      .mode(MessageSecurityMode.Sign)
                      .securityGroup(new SecurityGroupRef("sg"))
                      .keyServices(List.of(keyServiceEndpoint("opc.tcp://sks.example:4840")))
                      .build())
              .messageReceiveTimeout(Duration.ofSeconds(2))
              .keyFrameCount(uint(4))
              .rawTransportSettings(extensionObject(5))
              .rawMessageSettings(extensionObject(6))
              .property(new QualifiedName(1, "rp"), Variant.ofString("v"))
              .build();

      DataSetReaderConfig copy = reader.toBuilder().build();

      assertEquals(reader, copy);
      assertEquals(reader.hashCode(), copy.hashCode());
    }

    @Test
    void udpConnectionToBuilderRoundTrip() {
      UdpConnectionConfig connection =
          UdpConnectionConfig.builder("conn")
              .enabled(false)
              .publisherId(PublisherId.uint32(uint(99)))
              .address(UdpDatagramAddress.multicast("224.0.2.14", 4840).networkInterface("lo0"))
              .writerGroup(fullWriterGroupBuilder("wg").build())
              .readerGroup(
                  ReaderGroupConfig.builder("rg")
                      .dataSetReader(DataSetReaderConfig.builder("r").build())
                      .build())
              .property(new QualifiedName(1, "cp"), Variant.ofInt32(7))
              .rawTransportSettings(extensionObject(7))
              .build();

      UdpConnectionConfig copy = connection.toBuilder().build();

      assertEquals(connection, copy);
      assertEquals(connection.hashCode(), copy.hashCode());
    }

    @Test
    void pubSubConfigToBuilderRoundTrip() {
      PubSubConfig config =
          PubSubConfig.builder()
              .enabled(false)
              .connection(
                  UdpConnectionConfig.builder("conn")
                      .publisherId(PublisherId.uint16(ushort(1)))
                      .address(UdpDatagramAddress.unicast("127.0.0.1", 4840))
                      .writerGroup(fullWriterGroupBuilder("wg").build())
                      .readerGroup(
                          ReaderGroupConfig.builder("rg")
                              .dataSetReader(
                                  DataSetReaderConfig.builder("r")
                                      .subscribedDataSet(new StandaloneSubscribedDataSetRef("sds"))
                                      .build())
                              .build())
                      .build())
              .publishedDataSet(
                  PublishedDataSetConfig.builder("ds")
                      .field(FieldDefinition.builder("f1").build())
                      .build())
              .standaloneSubscribedDataSet(
                  StandaloneSubscribedDataSetConfig.builder("sds")
                      .metaData(metaData("md"))
                      .subscribedDataSet(targetVariables())
                      .build())
              .securityGroup(SecurityGroupConfig.builder("sg").build())
              .defaultSecurityKeyService(keyServiceEndpoint("opc.tcp://sks-default.example:4840"))
              .property(new QualifiedName(1, "gp"), Variant.ofString("global"))
              .build();

      PubSubConfig copy = config.toBuilder().build();

      assertEquals(config, copy);
      assertEquals(config.hashCode(), copy.hashCode());
    }

    @Test
    void securityGroupToBuilderRoundTrip() {
      SecurityGroupConfig group =
          SecurityGroupConfig.builder("sg")
              .securityGroupId("SG-1")
              .securityGroupFolder(List.of("Folder", "SubFolder"))
              .securityPolicyUri("http://opcfoundation.org/UA/SecurityPolicy#PubSub-Aes256-CTR")
              .keyLifeTime(Duration.ofMinutes(10))
              .maxFutureKeyCount(uint(2))
              .maxPastKeyCount(uint(3))
              .keyService(keyServiceEndpoint("opc.tcp://sks.example:4840"))
              .rolePermission(ROLE_PERMISSION)
              .property(new QualifiedName(1, "sp"), Variant.ofString("v"))
              .build();

      SecurityGroupConfig copy = group.toBuilder().build();

      assertEquals(group, copy);
      assertEquals(group.hashCode(), copy.hashCode());
    }

    @Test
    void fieldDefinitionToBuilderRoundTrip() {
      FieldDefinition field =
          FieldDefinition.builder("f")
              .source(NodeFieldAddress.parse("nsu=urn:milo:test;s=Sensor", AttributeId.Value))
              .dataType(NodeIds.Int32)
              .dataSetFieldId(new UUID(1L, 2L))
              .promoted(true)
              .valueRank(1)
              .arrayDimensions(new UInteger[] {uint(3)})
              .property(new QualifiedName(1, "fp"), Variant.ofInt32(1))
              .build();

      FieldDefinition copy = field.toBuilder().build();

      assertEquals(field, copy);
      assertEquals(field.hashCode(), copy.hashCode());
      assertArrayEquals(field.getArrayDimensions(), copy.getArrayDimensions());
    }
  }

  @Nested
  class EqualsContract {

    @Test
    void equalConfigsAreEqualWithEqualHashCodes() {
      WriterGroupConfig group = fullWriterGroupBuilder("wg").build();
      WriterGroupConfig same = fullWriterGroupBuilder("wg").build();

      assertEquals(group, same);
      assertEquals(group.hashCode(), same.hashCode());
      assertNotEquals(group, null);
      assertNotEquals(group, "wg");
    }

    @Test
    void inequalityOnEachVariedField() {
      WriterGroupConfig base = fullWriterGroupBuilder("wg").build();

      // name
      assertNotEquals(base, fullWriterGroupBuilder("other").build());
      // enabled
      assertNotEquals(base, fullWriterGroupBuilder("wg").enabled(true).build());
      // writerGroupId
      assertNotEquals(base, fullWriterGroupBuilder("wg").writerGroupId(ushort(11)).build());
      // publishingInterval
      assertNotEquals(
          base, fullWriterGroupBuilder("wg").publishingInterval(Duration.ofMillis(500)).build());
      // keepAliveTime
      assertNotEquals(
          base, fullWriterGroupBuilder("wg").keepAliveTime(Duration.ofSeconds(10)).build());
      // priority
      assertNotEquals(base, fullWriterGroupBuilder("wg").priority(ubyte(4)).build());
      // maxNetworkMessageSize
      assertNotEquals(base, fullWriterGroupBuilder("wg").maxNetworkMessageSize(uint(4096)).build());
      // messageSecurity
      assertNotEquals(
          base,
          fullWriterGroupBuilder("wg")
              .messageSecurity(
                  MessageSecurityConfig.builder()
                      .mode(MessageSecurityMode.SignAndEncrypt)
                      .securityGroup(new SecurityGroupRef("sg"))
                      .build())
              .build());
      // messageSettings
      assertNotEquals(
          base,
          fullWriterGroupBuilder("wg")
              .messageSettings(UadpWriterGroupSettings.builder().groupVersion(uint(8)).build())
              .build());
      // brokerTransport
      assertNotEquals(
          base,
          fullWriterGroupBuilder("wg")
              .brokerTransport(BrokerTransportSettings.builder().queueName("queue2").build())
              .build());
      // dataSetWriters (an additional writer changes the list)
      assertNotEquals(
          base, fullWriterGroupBuilder("wg").dataSetWriter(writer("w2", 2, "ds")).build());
      // rawTransportSettings
      assertNotEquals(
          base, fullWriterGroupBuilder("wg").rawTransportSettings(extensionObject(3)).build());
      // rawMessageSettings
      assertNotEquals(
          base, fullWriterGroupBuilder("wg").rawMessageSettings(extensionObject(4)).build());
      // properties (an additional property changes the map)
      assertNotEquals(
          base,
          fullWriterGroupBuilder("wg")
              .property(new QualifiedName(1, "p2"), Variant.ofInt32(43))
              .build());
    }

    @Test
    void securityGroupInequalityOnFolderAndRolePermissions() {
      SecurityGroupConfig base = SecurityGroupConfig.builder("sg").build();

      // securityGroupFolder
      assertNotEquals(
          base, SecurityGroupConfig.builder("sg").securityGroupFolder(List.of("Folder")).build());
      // rolePermissions
      assertNotEquals(
          base, SecurityGroupConfig.builder("sg").rolePermission(ROLE_PERMISSION).build());
    }

    @Test
    void dataSetReaderInequalityOnMessageSecurityOverride() {
      DataSetReaderConfig base = DataSetReaderConfig.builder("r").build();

      // Presence of the override object is significant, even for an all-default (None) override.
      DataSetReaderConfig overrideToNone =
          DataSetReaderConfig.builder("r")
              .messageSecurity(MessageSecurityConfig.builder().build())
              .build();
      assertNotEquals(base, overrideToNone);

      DataSetReaderConfig overrideToSign =
          DataSetReaderConfig.builder("r")
              .messageSecurity(
                  MessageSecurityConfig.builder()
                      .mode(MessageSecurityMode.Sign)
                      .securityGroup(new SecurityGroupRef("sg"))
                      .build())
              .build();
      assertNotEquals(overrideToNone, overrideToSign);
    }

    @Test
    void pubSubConfigInequalityOnDefaultSecurityKeyServices() {
      PubSubConfig base = PubSubConfig.builder().build();
      PubSubConfig withDefaults =
          PubSubConfig.builder()
              .defaultSecurityKeyService(keyServiceEndpoint("opc.tcp://sks.example:4840"))
              .build();

      assertNotEquals(base, withDefaults);
    }

    @Test
    void messageSettingsTypeDiscriminatesEquality() {
      // UADP settings and JSON settings are never equal, regardless of contents
      assertNotEquals(
          UadpWriterGroupSettings.builder().build(), JsonWriterGroupSettings.builder().build());
    }

    @Test
    void dataSetMetaDataFieldEqualityIsArrayAware() {
      UUID fieldId = new UUID(0L, 1L);

      DataSetMetaDataConfig.Field field =
          new DataSetMetaDataConfig.Field("f", NodeIds.Int32, fieldId, 1, new UInteger[] {uint(3)});
      DataSetMetaDataConfig.Field same =
          new DataSetMetaDataConfig.Field("f", NodeIds.Int32, fieldId, 1, new UInteger[] {uint(3)});
      DataSetMetaDataConfig.Field different =
          new DataSetMetaDataConfig.Field("f", NodeIds.Int32, fieldId, 1, new UInteger[] {uint(4)});

      assertEquals(field, same);
      assertEquals(field.hashCode(), same.hashCode());
      assertNotEquals(field, different);
    }

    @Test
    void fieldDefinitionEqualityIsArrayAware() {
      UUID fieldId = new UUID(0L, 1L);

      FieldDefinition field =
          FieldDefinition.builder("f")
              .dataSetFieldId(fieldId)
              .valueRank(1)
              .arrayDimensions(new UInteger[] {uint(3)})
              .build();
      FieldDefinition same =
          FieldDefinition.builder("f")
              .dataSetFieldId(fieldId)
              .valueRank(1)
              .arrayDimensions(new UInteger[] {uint(3)})
              .build();
      FieldDefinition different =
          FieldDefinition.builder("f")
              .dataSetFieldId(fieldId)
              .valueRank(1)
              .arrayDimensions(new UInteger[] {uint(4)})
              .build();

      assertEquals(field, same);
      assertEquals(field.hashCode(), same.hashCode());
      assertNotEquals(field, different);
    }
  }

  @Test
  void connectionFactoriesReturnMatchingBuilders() {
    PubSubConnectionConfig udp =
        PubSubConnectionConfig.udp("u")
            .address(UdpDatagramAddress.unicast("127.0.0.1", 4840))
            .build();
    assertInstanceOf(UdpConnectionConfig.class, udp);
    assertEquals("u", udp.name());

    PubSubConnectionConfig mqtt =
        PubSubConnectionConfig.mqtt("m").brokerUri(URI.create("mqtt://broker:1883")).build();
    assertInstanceOf(MqttConnectionConfig.class, mqtt);
    assertEquals("m", mqtt.name());
  }
}
