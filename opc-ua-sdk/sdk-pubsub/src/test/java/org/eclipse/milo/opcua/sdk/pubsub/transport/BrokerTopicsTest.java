/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.transport;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link BrokerTopics}: the Part 14 §7.3.4.7 topic-tree derivation, the {@code
 * 0:MqttTopicPrefix} connection property, and the configured-queue-name-always-wins resolution
 * rules (writer over group; empty string counts as absent; group-level {@code metaDataQueueName} is
 * a Milo-local fallback).
 */
class BrokerTopicsTest {

  private static final PublisherId PUBLISHER_ID = PublisherId.string("line-7");

  private static MqttConnectionConfig connection() {
    return connectionBuilder().build();
  }

  private static MqttConnectionConfig.Builder connectionBuilder() {
    return MqttConnectionConfig.builder("conn")
        .brokerUri(URI.create("mqtt://localhost:1883"))
        .publisherId(PUBLISHER_ID);
  }

  private static WriterGroupConfig group(@Nullable BrokerTransportSettings brokerTransport) {
    WriterGroupConfig.Builder builder = WriterGroupConfig.builder("WG").writerGroupId(ushort(1));
    if (brokerTransport != null) {
      builder.brokerTransport(brokerTransport);
    }
    return builder.build();
  }

  private static DataSetWriterConfig writer(@Nullable BrokerTransportSettings brokerTransport) {
    DataSetWriterConfig.Builder builder =
        DataSetWriterConfig.builder("W1")
            .dataSet(new PublishedDataSetRef("PDS"))
            .dataSetWriterId(ushort(1));
    if (brokerTransport != null) {
      builder.brokerTransport(brokerTransport);
    }
    return builder.build();
  }

  // region topicPrefix

  @Test
  void topicPrefixDefaultsToOpcua() {
    assertEquals("opcua", BrokerTopics.DEFAULT_TOPIC_PREFIX);
    assertEquals("opcua", BrokerTopics.topicPrefix(connection()));
  }

  @Test
  void topicPrefixOverriddenByConnectionProperty() {
    MqttConnectionConfig connection =
        connectionBuilder()
            .property(BrokerTopics.MQTT_TOPIC_PREFIX_PROPERTY, Variant.ofString("factory"))
            .build();

    assertEquals("factory", BrokerTopics.topicPrefix(connection));
  }

  @Test
  void topicPrefixMayContainMultipleLevels() {
    // §7.3.4.7.1: the prefix "may itself contain multiple levels"
    MqttConnectionConfig connection =
        connectionBuilder()
            .property(BrokerTopics.MQTT_TOPIC_PREFIX_PROPERTY, Variant.ofString("factory/cell-1"))
            .build();

    assertEquals("factory/cell-1", BrokerTopics.topicPrefix(connection));
    assertEquals(
        "factory/cell-1/uadp/data/line-7/WG",
        BrokerTopics.resolveDataQueueName(connection, "uadp", PUBLISHER_ID, group(null)));
  }

  @Test
  void topicPrefixIgnoresEmptyStringProperty() {
    MqttConnectionConfig connection =
        connectionBuilder()
            .property(BrokerTopics.MQTT_TOPIC_PREFIX_PROPERTY, Variant.ofString(""))
            .build();

    assertEquals("opcua", BrokerTopics.topicPrefix(connection));
  }

  @Test
  void topicPrefixIgnoresNonStringProperty() {
    MqttConnectionConfig connection =
        connectionBuilder()
            .property(BrokerTopics.MQTT_TOPIC_PREFIX_PROPERTY, Variant.ofInt32(5))
            .build();

    assertEquals("opcua", BrokerTopics.topicPrefix(connection));
  }

  @Test
  void mqttTopicPrefixPropertyIsNamespaceZero() {
    assertEquals(new QualifiedName(0, "MqttTopicPrefix"), BrokerTopics.MQTT_TOPIC_PREFIX_PROPERTY);
  }

  // endregion

  // region topic derivation

  @Test
  void dataTopicAtGroupGranularity() {
    assertEquals(
        "opcua/uadp/data/line-7/WG", BrokerTopics.dataTopic("opcua", "uadp", PUBLISHER_ID, "WG"));
  }

  @Test
  void dataTopicAtWriterGranularityAppendsWriterName() {
    assertEquals(
        "opcua/json/data/line-7/WG/W1",
        BrokerTopics.dataTopic("opcua", "json", PUBLISHER_ID, "WG", "W1"));
  }

  @Test
  void metaDataTopicAlwaysIncludesWriterLevel() {
    // §7.3.4.7.4: the DataSetWriter level is always present in the metadata tree
    assertEquals(
        "opcua/json/metadata/line-7/WG/W1",
        BrokerTopics.metaDataTopic("opcua", "json", PUBLISHER_ID, "WG", "W1"));
  }

  @Test
  void statusTopicStopsAtPublisherId() {
    assertEquals(
        "opcua/json/status/line-7", BrokerTopics.statusTopic("opcua", "json", PUBLISHER_ID));
  }

  @Test
  void resolveStatusQueueNameUsesConnectionPrefixAndPublisherId() {
    MqttConnectionConfig connection =
        connectionBuilder()
            .property(BrokerTopics.MQTT_TOPIC_PREFIX_PROPERTY, Variant.ofString("factory"))
            .build();

    assertEquals(
        "factory/json/status/line-7", BrokerTopics.resolveStatusQueueName(connection, "json"));
  }

  @Test
  void numericPublisherIdsAreStringifiedCanonically() {
    // numeric ids are stringified as decimal without leading zeros (§6.2.7.1 / Table 184)
    assertEquals(
        "opcua/uadp/data/5/WG",
        BrokerTopics.dataTopic("opcua", "uadp", PublisherId.ubyte(ubyte(5)), "WG"));
    assertEquals(
        "opcua/uadp/data/7/WG",
        BrokerTopics.dataTopic("opcua", "uadp", PublisherId.uint16(ushort(7)), "WG"));
    assertEquals(
        "opcua/uadp/data/70000/WG",
        BrokerTopics.dataTopic("opcua", "uadp", PublisherId.uint32(uint(70_000)), "WG"));
    assertEquals(
        "opcua/uadp/data/5000000000/WG",
        BrokerTopics.dataTopic("opcua", "uadp", PublisherId.uint64(ulong(5_000_000_000L)), "WG"));
  }

  @Test
  void mappingNameIsTheEncodingLevel() {
    // the <Encoding> level is the mapping name verbatim (Table 206 for the built-ins)
    assertEquals(
        "opcua/custom/data/line-7/WG",
        BrokerTopics.dataTopic("opcua", "custom", PUBLISHER_ID, "WG"));
  }

  // endregion

  // region data queue resolution

  @Test
  void resolveDataQueueNameConfiguredGroupQueueWins() {
    WriterGroupConfig group =
        group(BrokerTransportSettings.builder().queueName("custom/group/topic").build());

    assertEquals(
        "custom/group/topic",
        BrokerTopics.resolveDataQueueName(connection(), "uadp", PUBLISHER_ID, group));
  }

  @Test
  void resolveDataQueueNameDerivedWhenGroupQueueUnset() {
    assertEquals(
        "opcua/uadp/data/line-7/WG",
        BrokerTopics.resolveDataQueueName(connection(), "uadp", PUBLISHER_ID, group(null)));
  }

  @Test
  void resolveDataQueueNameEmptyStringGroupQueueCountsAsAbsent() {
    WriterGroupConfig group = group(BrokerTransportSettings.builder().queueName("").build());

    assertEquals(
        "opcua/uadp/data/line-7/WG",
        BrokerTopics.resolveDataQueueName(connection(), "uadp", PUBLISHER_ID, group));
  }

  @Test
  void resolveDataQueueNameWriterOverrideWinsOverGroup() {
    WriterGroupConfig group =
        group(BrokerTransportSettings.builder().queueName("group/topic").build());
    DataSetWriterConfig writer =
        writer(BrokerTransportSettings.builder().queueName("writer/topic").build());

    assertEquals(
        "writer/topic", BrokerTopics.resolveDataQueueName(connection(), "uadp", group, writer));
  }

  @Test
  void resolveDataQueueNameWriterWithoutOverrideUsesGroupResolution() {
    // §7.3.4.7.3: without a writer-level QueueName the group's queue is used and the
    // DataSetWriter name is NOT part of the topic
    WriterGroupConfig group =
        group(BrokerTransportSettings.builder().queueName("group/topic").build());

    assertEquals(
        "group/topic",
        BrokerTopics.resolveDataQueueName(connection(), "uadp", group, writer(null)));

    assertEquals(
        "opcua/uadp/data/line-7/WG",
        BrokerTopics.resolveDataQueueName(connection(), "uadp", group(null), writer(null)));
  }

  @Test
  void resolveDataQueueNameEmptyStringWriterOverrideCountsAsAbsent() {
    WriterGroupConfig group =
        group(BrokerTransportSettings.builder().queueName("group/topic").build());
    DataSetWriterConfig writer = writer(BrokerTransportSettings.builder().queueName("").build());

    assertEquals(
        "group/topic", BrokerTopics.resolveDataQueueName(connection(), "uadp", group, writer));
  }

  // endregion

  // region metadata queue resolution

  @Test
  void resolveMetaDataQueueNameWriterLevelWins() {
    WriterGroupConfig group =
        group(BrokerTransportSettings.builder().metaDataQueueName("group-md").build());
    DataSetWriterConfig writer =
        writer(BrokerTransportSettings.builder().metaDataQueueName("writer-md").build());

    assertEquals(
        "writer-md", BrokerTopics.resolveMetaDataQueueName(connection(), "uadp", group, writer));
  }

  @Test
  void resolveMetaDataQueueNameGroupLevelIsMiloLocalFallback() {
    // the group-level metaDataQueueName has no Part 14 slot (it does not round-trip); it is
    // honored as a Milo-local fallback between the writer level and the derived default
    WriterGroupConfig group =
        group(BrokerTransportSettings.builder().metaDataQueueName("group-md").build());

    assertEquals(
        "group-md",
        BrokerTopics.resolveMetaDataQueueName(connection(), "uadp", group, writer(null)));
  }

  @Test
  void resolveMetaDataQueueNameDerivedDefault() {
    assertEquals(
        "opcua/uadp/metadata/line-7/WG/W1",
        BrokerTopics.resolveMetaDataQueueName(connection(), "uadp", group(null), writer(null)));
  }

  @Test
  void resolveMetaDataQueueNameEmptyStringsCountAsAbsent() {
    WriterGroupConfig group =
        group(BrokerTransportSettings.builder().metaDataQueueName("").build());
    DataSetWriterConfig writer =
        writer(BrokerTransportSettings.builder().metaDataQueueName("").build());

    assertEquals(
        "opcua/uadp/metadata/line-7/WG/W1",
        BrokerTopics.resolveMetaDataQueueName(connection(), "uadp", group, writer));
  }

  @Test
  void resolveMetaDataQueueNameIgnoresDataQueueNames() {
    // a configured DATA queue name never leaks into the metadata resolution (no $Metadata-style
    // suffix derivation exists in v1.05)
    WriterGroupConfig group =
        group(BrokerTransportSettings.builder().queueName("group/topic").build());
    DataSetWriterConfig writer =
        writer(BrokerTransportSettings.builder().queueName("writer/topic").build());

    assertEquals(
        "opcua/uadp/metadata/line-7/WG/W1",
        BrokerTopics.resolveMetaDataQueueName(connection(), "uadp", group, writer));
  }

  // endregion

  // region publisher id requirement

  @Test
  void derivationWithoutPublisherIdThrows() {
    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn").brokerUri(URI.create("mqtt://localhost:1883")).build();

    IllegalArgumentException dataException =
        assertThrows(
            IllegalArgumentException.class,
            () -> BrokerTopics.resolveDataQueueName(connection, "uadp", group(null), writer(null)));
    assertTrue(dataException.getMessage().contains("conn"), dataException.getMessage());

    assertThrows(
        IllegalArgumentException.class,
        () -> BrokerTopics.resolveMetaDataQueueName(connection, "uadp", group(null), writer(null)));
  }

  @Test
  void configuredQueueNamesAvoidPublisherIdRequirement() {
    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn").brokerUri(URI.create("mqtt://localhost:1883")).build();

    WriterGroupConfig group = group(null);
    DataSetWriterConfig writer =
        writer(
            BrokerTransportSettings.builder()
                .queueName("writer/topic")
                .metaDataQueueName("writer-md")
                .build());

    assertEquals(
        "writer/topic", BrokerTopics.resolveDataQueueName(connection, "uadp", group, writer));
    assertEquals(
        "writer-md", BrokerTopics.resolveMetaDataQueueName(connection, "uadp", group, writer));
  }

  /**
   * A configured queue name ALWAYS wins — a group-level data queue resolves through the
   * writer-arity overload without requiring a PublisherId, like the metadata sibling.
   */
  @Test
  void groupConfiguredDataQueueAvoidsPublisherIdRequirement() {
    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn").brokerUri(URI.create("mqtt://localhost:1883")).build();

    WriterGroupConfig group =
        group(
            BrokerTransportSettings.builder()
                .queueName("group/topic")
                .metaDataQueueName("group-md")
                .build());

    assertEquals(
        "group/topic", BrokerTopics.resolveDataQueueName(connection, "uadp", group, writer(null)));
    assertEquals(
        "group-md", BrokerTopics.resolveMetaDataQueueName(connection, "uadp", group, writer(null)));
  }

  // endregion
}
