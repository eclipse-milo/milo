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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.junit.jupiter.api.Test;

/**
 * Tests for the cross-element validation performed by {@link PubSubConfig.Builder#build()}: name
 * uniqueness scopes, wire-id uniqueness within a PublisherId scope, reference resolution,
 * publisherId presence, and JSON-settings-on-UDP rejection.
 */
class PubSubConfigValidationTest {

  private static final PublisherId PID_1 = PublisherId.uint16(ushort(1));
  private static final PublisherId PID_2 = PublisherId.uint16(ushort(2));

  private static PublishedDataSetConfig dataSet(String name) {
    return PublishedDataSetConfig.builder(name)
        .field(FieldDefinition.builder("f1").build())
        .build();
  }

  private static DataSetWriterConfig writer(String name, int writerId, String dataSetName) {
    return DataSetWriterConfig.builder(name)
        .dataSet(new PublishedDataSetRef(dataSetName))
        .dataSetWriterId(ushort(writerId))
        .build();
  }

  private static WriterGroupConfig writerGroup(
      String name, int groupId, DataSetWriterConfig... writers) {

    WriterGroupConfig.Builder builder =
        WriterGroupConfig.builder(name).writerGroupId(ushort(groupId));
    for (DataSetWriterConfig writer : writers) {
      builder.dataSetWriter(writer);
    }
    return builder.build();
  }

  private static ReaderGroupConfig readerGroup(String name, DataSetReaderConfig... readers) {
    ReaderGroupConfig.Builder builder = ReaderGroupConfig.builder(name);
    for (DataSetReaderConfig reader : readers) {
      builder.dataSetReader(reader);
    }
    return builder.build();
  }

  private static UdpConnectionConfig.Builder udp(String name) {
    return UdpConnectionConfig.builder(name).address(UdpDatagramAddress.unicast("127.0.0.1", 4840));
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

  private static StandaloneSubscribedDataSetConfig standalone(
      String name, SubscribedDataSetSpec spec) {

    return StandaloneSubscribedDataSetConfig.builder(name)
        .metaData(metaData(name + "-md"))
        .subscribedDataSet(spec)
        .build();
  }

  private static MessageSecurityConfig securityRef(String securityGroupName) {
    return MessageSecurityConfig.builder()
        .securityGroup(new SecurityGroupRef(securityGroupName))
        .build();
  }

  @Test
  void fullyResolvedConfigBuilds() {
    WriterGroupConfig writerGroup =
        WriterGroupConfig.builder("wg")
            .writerGroupId(ushort(1))
            .messageSecurity(securityRef("sg"))
            .dataSetWriter(writer("w1", 1, "ds"))
            .build();

    ReaderGroupConfig readerGroup =
        ReaderGroupConfig.builder("rg")
            .messageSecurity(securityRef("sg"))
            .dataSetReader(
                DataSetReaderConfig.builder("r1")
                    .subscribedDataSet(new StandaloneSubscribedDataSetRef("sds"))
                    .build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                udp("conn")
                    .publisherId(PID_1)
                    .writerGroup(writerGroup)
                    .readerGroup(readerGroup)
                    .build())
            .publishedDataSet(dataSet("ds"))
            .standaloneSubscribedDataSet(standalone("sds", targetVariables()))
            .securityGroup(SecurityGroupConfig.builder("sg").build())
            .build();

    assertEquals(1, config.connections().size());
    assertTrue(config.connection("conn").isPresent());
    assertTrue(config.connection("missing").isEmpty());
    assertTrue(config.publishedDataSet("ds").isPresent());
    assertTrue(config.publishedDataSet("missing").isEmpty());
  }

  @Test
  void duplicateConnectionNamesRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder().connection(udp("conn").build()).connection(udp("conn").build());

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("connection"), e.getMessage());
    assertTrue(e.getMessage().contains("conn"), e.getMessage());
  }

  @Test
  void duplicatePublishedDataSetNamesRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder().publishedDataSet(dataSet("ds")).publishedDataSet(dataSet("ds"));

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("publishedDataSet"), e.getMessage());
  }

  @Test
  void duplicateStandaloneSubscribedDataSetNamesRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .standaloneSubscribedDataSet(standalone("sds", targetVariables()))
            .standaloneSubscribedDataSet(standalone("sds", targetVariables()));

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("standaloneSubscribedDataSet"), e.getMessage());
  }

  @Test
  void duplicateSecurityGroupNamesRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .securityGroup(SecurityGroupConfig.builder("sg").build())
            .securityGroup(SecurityGroupConfig.builder("sg").build());

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("securityGroup"), e.getMessage());
  }

  @Test
  void writerAndReaderGroupNamesShareOneScopePerConnection() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .connection(
                udp("conn")
                    .publisherId(PID_1)
                    .writerGroup(writerGroup("G", 1))
                    .readerGroup(readerGroup("G"))
                    .build());

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("duplicate group name"), e.getMessage());
  }

  @Test
  void duplicateWriterGroupNamesWithinConnectionRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .connection(
                udp("conn")
                    .publisherId(PID_1)
                    .writerGroup(writerGroup("G", 1))
                    .writerGroup(writerGroup("G", 2))
                    .build());

    assertThrows(PubSubConfigValidationException.class, builder::build);
  }

  @Test
  void duplicateReaderGroupNamesWithinConnectionRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .connection(
                udp("conn").readerGroup(readerGroup("G")).readerGroup(readerGroup("G")).build());

    assertThrows(PubSubConfigValidationException.class, builder::build);
  }

  @Test
  void duplicateWriterGroupNameAcrossConnectionsSharingPublisherIdRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .connection(udp("connA").publisherId(PID_1).writerGroup(writerGroup("G", 1)).build())
            .connection(udp("connB").publisherId(PID_1).writerGroup(writerGroup("G", 2)).build());

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("duplicate writer group name"), e.getMessage());
  }

  @Test
  void sameWriterGroupNameAllowedAcrossConnectionsWithDifferentPublisherIds() {
    PubSubConfig config =
        PubSubConfig.builder()
            .connection(udp("connA").publisherId(PID_1).writerGroup(writerGroup("G", 1)).build())
            .connection(udp("connB").publisherId(PID_2).writerGroup(writerGroup("G", 1)).build())
            .build();

    assertEquals(2, config.connections().size());
  }

  @Test
  void sameReaderGroupNameAllowedAcrossConnectionsSharingPublisherId() {
    // only writer group names are scoped by PublisherId; reader group names are per-connection
    PubSubConfig config =
        PubSubConfig.builder()
            .connection(udp("connA").publisherId(PID_1).readerGroup(readerGroup("RG")).build())
            .connection(udp("connB").publisherId(PID_1).readerGroup(readerGroup("RG")).build())
            .build();

    assertEquals(2, config.connections().size());
  }

  @Test
  void duplicateWriterGroupIdWithinConnectionRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .connection(
                udp("conn")
                    .publisherId(PID_1)
                    .writerGroup(writerGroup("G1", 7))
                    .writerGroup(writerGroup("G2", 7))
                    .build());

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("duplicate writerGroupId"), e.getMessage());
  }

  @Test
  void duplicateWriterGroupIdAcrossConnectionsSharingPublisherIdRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .connection(udp("connA").publisherId(PID_1).writerGroup(writerGroup("G1", 7)).build())
            .connection(udp("connB").publisherId(PID_1).writerGroup(writerGroup("G2", 7)).build());

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("duplicate writerGroupId"), e.getMessage());
  }

  @Test
  void sameWriterGroupIdAllowedAcrossDifferentPublisherIds() {
    PubSubConfig config =
        PubSubConfig.builder()
            .connection(udp("connA").publisherId(PID_1).writerGroup(writerGroup("G1", 7)).build())
            .connection(udp("connB").publisherId(PID_2).writerGroup(writerGroup("G2", 7)).build())
            .build();

    assertEquals(2, config.connections().size());
  }

  @Test
  void differentPublisherIdTypesAreDistinctScopes() {
    // the same numeric value in different PublisherId wire types is a different scope
    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                udp("connA")
                    .publisherId(PublisherId.uint16(ushort(1)))
                    .writerGroup(writerGroup("G", 7))
                    .build())
            .connection(
                udp("connB")
                    .publisherId(PublisherId.uint32(uint(1)))
                    .writerGroup(writerGroup("G", 7))
                    .build())
            .build();

    assertEquals(2, config.connections().size());
  }

  @Test
  void duplicateDataSetWriterIdWithinGroupRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .connection(
                udp("conn")
                    .publisherId(PID_1)
                    .writerGroup(writerGroup("G", 1, writer("w1", 5, "ds"), writer("w2", 5, "ds")))
                    .build())
            .publishedDataSet(dataSet("ds"));

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("duplicate dataSetWriterId"), e.getMessage());
  }

  @Test
  void duplicateDataSetWriterIdAcrossGroupsOfConnectionRejected() {
    // dataSetWriterIds are unique across ALL groups in the publisher scope, not per group
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .connection(
                udp("conn")
                    .publisherId(PID_1)
                    .writerGroup(writerGroup("G1", 1, writer("w1", 5, "ds")))
                    .writerGroup(writerGroup("G2", 2, writer("w2", 5, "ds")))
                    .build())
            .publishedDataSet(dataSet("ds"));

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("duplicate dataSetWriterId"), e.getMessage());
  }

  @Test
  void duplicateDataSetWriterIdAcrossConnectionsSharingPublisherIdRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .connection(
                udp("connA")
                    .publisherId(PID_1)
                    .writerGroup(writerGroup("G1", 1, writer("w1", 5, "ds")))
                    .build())
            .connection(
                udp("connB")
                    .publisherId(PID_1)
                    .writerGroup(writerGroup("G2", 2, writer("w2", 5, "ds")))
                    .build())
            .publishedDataSet(dataSet("ds"));

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("duplicate dataSetWriterId"), e.getMessage());
  }

  @Test
  void sameDataSetWriterIdAllowedAcrossDifferentPublisherIds() {
    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                udp("connA")
                    .publisherId(PID_1)
                    .writerGroup(writerGroup("G1", 1, writer("w1", 5, "ds")))
                    .build())
            .connection(
                udp("connB")
                    .publisherId(PID_2)
                    .writerGroup(writerGroup("G2", 2, writer("w2", 5, "ds")))
                    .build())
            .publishedDataSet(dataSet("ds"))
            .build();

    assertEquals(2, config.connections().size());
  }

  @Test
  void duplicateDataSetWriterNamesWithinGroupRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .connection(
                udp("conn")
                    .publisherId(PID_1)
                    .writerGroup(writerGroup("G", 1, writer("w", 1, "ds"), writer("w", 2, "ds")))
                    .build())
            .publishedDataSet(dataSet("ds"));

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("duplicate dataSetWriter name"), e.getMessage());
  }

  @Test
  void duplicateDataSetReaderNamesWithinGroupRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .connection(
                udp("conn")
                    .readerGroup(
                        readerGroup(
                            "RG",
                            DataSetReaderConfig.builder("r").build(),
                            DataSetReaderConfig.builder("r").build()))
                    .build());

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("duplicate dataSetReader name"), e.getMessage());
  }

  @Test
  void unresolvedPublishedDataSetRefRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .connection(
                udp("conn")
                    .publisherId(PID_1)
                    .writerGroup(writerGroup("G", 1, writer("w", 1, "missing-ds")))
                    .build());

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("PublishedDataSetRef"), e.getMessage());
    assertTrue(e.getMessage().contains("missing-ds"), e.getMessage());
  }

  @Test
  void unresolvedSecurityGroupRefOnWriterGroupRejected() {
    WriterGroupConfig writerGroup =
        WriterGroupConfig.builder("G")
            .writerGroupId(ushort(1))
            .messageSecurity(securityRef("missing-sg"))
            .build();

    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .connection(udp("conn").publisherId(PID_1).writerGroup(writerGroup).build());

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("SecurityGroupRef"), e.getMessage());
    assertTrue(e.getMessage().contains("missing-sg"), e.getMessage());
  }

  @Test
  void unresolvedSecurityGroupRefOnReaderGroupRejected() {
    ReaderGroupConfig readerGroup =
        ReaderGroupConfig.builder("RG").messageSecurity(securityRef("missing-sg")).build();

    PubSubConfig.Builder builder =
        PubSubConfig.builder().connection(udp("conn").readerGroup(readerGroup).build());

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("SecurityGroupRef"), e.getMessage());
  }

  @Test
  void unresolvedSecurityGroupRefOnDataSetReaderOverrideRejected() {
    DataSetReaderConfig reader =
        DataSetReaderConfig.builder("r").messageSecurity(securityRef("missing-sg")).build();

    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .connection(udp("conn").readerGroup(readerGroup("RG", reader)).build());

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("dataSetReader 'r'"), e.getMessage());
    assertTrue(e.getMessage().contains("SecurityGroupRef"), e.getMessage());
    assertTrue(e.getMessage().contains("missing-sg"), e.getMessage());
  }

  @Test
  void resolvableSecurityGroupRefOnDataSetReaderOverrideAllowed() {
    DataSetReaderConfig reader =
        DataSetReaderConfig.builder("r").messageSecurity(securityRef("sg")).build();

    PubSubConfig config =
        PubSubConfig.builder()
            .connection(udp("conn").readerGroup(readerGroup("RG", reader)).build())
            .securityGroup(SecurityGroupConfig.builder("sg").build())
            .build();

    assertEquals(1, config.connections().size());
  }

  @Test
  void messageSecurityWithoutSecurityGroupRefAllowed() {
    WriterGroupConfig writerGroup =
        WriterGroupConfig.builder("G")
            .writerGroupId(ushort(1))
            .messageSecurity(MessageSecurityConfig.builder().build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .connection(udp("conn").publisherId(PID_1).writerGroup(writerGroup).build())
            .build();

    assertEquals(1, config.connections().size());
  }

  @Test
  void unresolvedStandaloneSubscribedDataSetRefOnReaderRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .connection(
                udp("conn")
                    .readerGroup(
                        readerGroup(
                            "RG",
                            DataSetReaderConfig.builder("r")
                                .subscribedDataSet(new StandaloneSubscribedDataSetRef("missing"))
                                .build()))
                    .build());

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("StandaloneSubscribedDataSetRef"), e.getMessage());
    assertTrue(e.getMessage().contains("missing"), e.getMessage());
  }

  @Test
  void unresolvedStandaloneSubscribedDataSetRefOnStandaloneRejected() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder()
            .standaloneSubscribedDataSet(
                standalone("a", new StandaloneSubscribedDataSetRef("missing")));

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("StandaloneSubscribedDataSetRef"), e.getMessage());
  }

  @Test
  void chainedStandaloneSubscribedDataSetRefResolves() {
    PubSubConfig config =
        PubSubConfig.builder()
            .standaloneSubscribedDataSet(standalone("a", new StandaloneSubscribedDataSetRef("b")))
            .standaloneSubscribedDataSet(standalone("b", targetVariables()))
            .build();

    assertEquals(2, config.standaloneSubscribedDataSets().size());
  }

  @Test
  void publisherIdRequiredWhenWriterGroupsPresent() {
    PubSubConfig.Builder builder =
        PubSubConfig.builder().connection(udp("conn").writerGroup(writerGroup("G", 1)).build());

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("publisherId"), e.getMessage());
  }

  @Test
  void readerOnlyConnectionDoesNotRequirePublisherId() {
    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                udp("conn")
                    .readerGroup(readerGroup("RG", DataSetReaderConfig.builder("r").build()))
                    .build())
            .build();

    assertEquals(1, config.connections().size());
  }

  @Test
  void jsonWriterGroupSettingsRejectedOnUdpConnection() {
    WriterGroupConfig jsonGroup =
        WriterGroupConfig.builder("G")
            .writerGroupId(ushort(1))
            .messageSettings(JsonWriterGroupSettings.builder().build())
            .build();

    UdpConnectionConfig.Builder builder = udp("conn").publisherId(PID_1).writerGroup(jsonGroup);

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("JSON"), e.getMessage());
  }

  @Test
  void jsonDataSetWriterSettingsRejectedOnUdpConnection() {
    DataSetWriterConfig jsonWriter =
        DataSetWriterConfig.builder("w")
            .dataSet(new PublishedDataSetRef("ds"))
            .dataSetWriterId(ushort(1))
            .settings(JsonDataSetWriterSettings.builder().build())
            .build();

    UdpConnectionConfig.Builder builder =
        udp("conn").publisherId(PID_1).writerGroup(writerGroup("G", 1, jsonWriter));

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("JSON"), e.getMessage());
  }

  @Test
  void jsonDataSetReaderSettingsRejectedOnUdpConnection() {
    DataSetReaderConfig jsonReader =
        DataSetReaderConfig.builder("r")
            .settings(JsonDataSetReaderSettings.builder().build())
            .build();

    UdpConnectionConfig.Builder builder = udp("conn").readerGroup(readerGroup("RG", jsonReader));

    PubSubConfigValidationException e =
        assertThrows(PubSubConfigValidationException.class, builder::build);

    assertTrue(e.getMessage().contains("JSON"), e.getMessage());
  }

  @Test
  void jsonSettingsAllowedOnMqttConnection() {
    WriterGroupConfig jsonGroup =
        WriterGroupConfig.builder("G")
            .writerGroupId(ushort(1))
            .messageSettings(JsonWriterGroupSettings.builder().build())
            .dataSetWriter(
                DataSetWriterConfig.builder("w")
                    .dataSet(new PublishedDataSetRef("ds"))
                    .dataSetWriterId(ushort(1))
                    .settings(JsonDataSetWriterSettings.builder().build())
                    .build())
            .build();

    ReaderGroupConfig jsonReaderGroup =
        readerGroup(
            "RG",
            DataSetReaderConfig.builder("r")
                .settings(JsonDataSetReaderSettings.builder().build())
                .build());

    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                MqttConnectionConfig.builder("conn")
                    .publisherId(PID_1)
                    .brokerUri(URI.create("mqtt://broker.example:1883"))
                    .writerGroup(jsonGroup)
                    .readerGroup(jsonReaderGroup)
                    .build())
            .publishedDataSet(dataSet("ds"))
            .build();

    assertEquals(1, config.connections().size());
  }
}
