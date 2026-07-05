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

import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.jspecify.annotations.Nullable;

/**
 * Broker queue (topic) name derivation per the OPC UA Part 14 §7.3.4.7 standardized topic tree:
 *
 * <pre>{@code <Prefix>/<Encoding>/<MessageType>/<PublisherId>[/<WriterGroup>[/<DataSetWriter>]]}
 * </pre>
 *
 * <p>A configured queue name always wins: the engine only derives a topic from this tree when the
 * applicable {@link BrokerTransportSettings} carry no queue name. The {@code <Prefix>} level comes
 * from the connection property {@code 0:MqttTopicPrefix} (§7.3.4.4 Table 201), default {@value
 * #DEFAULT_TOPIC_PREFIX}; the {@code <Encoding>} level is the message mapping name ({@code "uadp"}
 * or {@code "json"} for the built-in mappings, matching Table 206).
 *
 * <p>Component names are used verbatim as topic levels; Part 14 §7.3.4.7.1 forbids levels that
 * start with {@code $} or contain {@code /}, {@code +}, {@code #}, control characters, or
 * whitespace other than space. Names violating those rules are not sanitized here — configure
 * explicit queue names for such components.
 *
 * <p>This helper is shared by the runtime engine (which resolves every publisher-side {@link
 * MessageAddress}) and broker transport modules such as {@code sdk-pubsub-mqtt}.
 */
public final class BrokerTopics {

  /** The default {@code <Prefix>} topic level (Part 14 §7.3.4.7.1). */
  public static final String DEFAULT_TOPIC_PREFIX = "opcua";

  /** The connection property that overrides the {@code <Prefix>} topic level (§7.3.4.4). */
  public static final QualifiedName MQTT_TOPIC_PREFIX_PROPERTY =
      new QualifiedName(0, "MqttTopicPrefix");

  private static final String DATA_LEVEL = "data";
  private static final String METADATA_LEVEL = "metadata";
  private static final String STATUS_LEVEL = "status";

  private BrokerTopics() {}

  /**
   * The {@code <Prefix>} topic level of a connection: the {@code 0:MqttTopicPrefix} connection
   * property when present and a non-empty String, otherwise {@value #DEFAULT_TOPIC_PREFIX}.
   *
   * @param connection the connection config.
   * @return the topic prefix; possibly multiple levels, never empty.
   */
  public static String topicPrefix(PubSubConnectionConfig connection) {
    Variant value = connection.properties().get(MQTT_TOPIC_PREFIX_PROPERTY);
    if (value != null && value.value() instanceof String prefix && !prefix.isEmpty()) {
      return prefix;
    }
    return DEFAULT_TOPIC_PREFIX;
  }

  /**
   * The §7.3.4.7.3 data topic at WriterGroup granularity: {@code
   * <prefix>/<encoding>/data/<publisherId>/<writerGroupName>}.
   *
   * @param prefix the {@code <Prefix>} level, see {@link #topicPrefix(PubSubConnectionConfig)}.
   * @param mappingName the message mapping name, used as the {@code <Encoding>} level.
   * @param publisherId the publisher id; numeric ids are stringified without leading zeros.
   * @param writerGroupName the WriterGroup name.
   * @return the derived data topic.
   */
  public static String dataTopic(
      String prefix, String mappingName, PublisherId publisherId, String writerGroupName) {

    return prefix
        + "/"
        + mappingName
        + "/"
        + DATA_LEVEL
        + "/"
        + publisherId.toCanonicalString()
        + "/"
        + writerGroupName;
  }

  /**
   * The §7.3.4.7.3 data topic at DataSetWriter granularity: {@code
   * <prefix>/<encoding>/data/<publisherId>/<writerGroupName>/<dataSetWriterName>}. Only meaningful
   * for NetworkMessages that carry a single writer's DataSetMessages.
   *
   * @param prefix the {@code <Prefix>} level, see {@link #topicPrefix(PubSubConnectionConfig)}.
   * @param mappingName the message mapping name, used as the {@code <Encoding>} level.
   * @param publisherId the publisher id; numeric ids are stringified without leading zeros.
   * @param writerGroupName the WriterGroup name.
   * @param dataSetWriterName the DataSetWriter name.
   * @return the derived data topic.
   */
  public static String dataTopic(
      String prefix,
      String mappingName,
      PublisherId publisherId,
      String writerGroupName,
      String dataSetWriterName) {

    return dataTopic(prefix, mappingName, publisherId, writerGroupName) + "/" + dataSetWriterName;
  }

  /**
   * The §7.3.4.7.4 metadata topic: {@code
   * <prefix>/<encoding>/metadata/<publisherId>/<writerGroupName>/<dataSetWriterName>}. The
   * DataSetWriter level is always present.
   *
   * @param prefix the {@code <Prefix>} level, see {@link #topicPrefix(PubSubConnectionConfig)}.
   * @param mappingName the message mapping name, used as the {@code <Encoding>} level.
   * @param publisherId the publisher id; numeric ids are stringified without leading zeros.
   * @param writerGroupName the WriterGroup name.
   * @param dataSetWriterName the DataSetWriter name.
   * @return the derived metadata topic.
   */
  public static String metaDataTopic(
      String prefix,
      String mappingName,
      PublisherId publisherId,
      String writerGroupName,
      String dataSetWriterName) {

    return prefix
        + "/"
        + mappingName
        + "/"
        + METADATA_LEVEL
        + "/"
        + publisherId.toCanonicalString()
        + "/"
        + writerGroupName
        + "/"
        + dataSetWriterName;
  }

  /**
   * The §7.3.4.7.7 status topic: {@code <prefix>/<encoding>/status/<publisherId>}.
   *
   * @param prefix the {@code <Prefix>} level, see {@link #topicPrefix(PubSubConnectionConfig)}.
   * @param mappingName the message mapping name, used as the {@code <Encoding>} level.
   * @param publisherId the publisher id; numeric ids are stringified without leading zeros.
   * @return the derived status topic.
   */
  public static String statusTopic(String prefix, String mappingName, PublisherId publisherId) {
    return prefix + "/" + mappingName + "/" + STATUS_LEVEL + "/" + publisherId.toCanonicalString();
  }

  /**
   * Resolve the status queue name for one connection and mapping.
   *
   * @param connection the connection config (topic prefix and PublisherId source).
   * @param mappingName the message mapping name.
   * @return the derived status queue name.
   * @throws IllegalArgumentException if the connection has no publisher id to derive one from.
   */
  public static String resolveStatusQueueName(
      PubSubConnectionConfig connection, String mappingName) {

    return statusTopic(topicPrefix(connection), mappingName, requirePublisherId(connection));
  }

  /**
   * Resolve the data queue name for a WriterGroup's NetworkMessages: the group's configured queue
   * name when present, otherwise the derived {@link #dataTopic(String, String, PublisherId,
   * String)}.
   *
   * <p>This is the queue for NetworkMessages carrying the DataSetMessages of writers <i>without</i>
   * a writer-level queue override; use {@link #resolveDataQueueName(PubSubConnectionConfig, String,
   * WriterGroupConfig, DataSetWriterConfig)} for writers with one.
   *
   * @param connection the connection config (topic prefix source).
   * @param mappingName the group's message mapping name.
   * @param publisherId the connection's publisher id.
   * @param group the WriterGroup config.
   * @return the resolved data queue name.
   */
  public static String resolveDataQueueName(
      PubSubConnectionConfig connection,
      String mappingName,
      PublisherId publisherId,
      WriterGroupConfig group) {

    String configured = queueNameOf(group.getBrokerTransport());
    if (configured != null) {
      return configured;
    }
    return dataTopic(topicPrefix(connection), mappingName, publisherId, group.getName());
  }

  /**
   * Resolve the data queue name for one DataSetWriter's NetworkMessages: the writer's configured
   * queue name when present (the §6.4.2.5.1 override), otherwise the group resolution of {@link
   * #resolveDataQueueName(PubSubConnectionConfig, String, PublisherId, WriterGroupConfig)}.
   *
   * @param connection the connection config (topic prefix source).
   * @param mappingName the group's message mapping name.
   * @param group the WriterGroup config.
   * @param writer the DataSetWriter config.
   * @return the resolved data queue name.
   * @throws IllegalArgumentException if no queue name is configured and the connection has no
   *     publisher id to derive one from.
   */
  public static String resolveDataQueueName(
      PubSubConnectionConfig connection,
      String mappingName,
      WriterGroupConfig group,
      DataSetWriterConfig writer) {

    String configured = queueNameOf(writer.getBrokerTransport());
    if (configured == null) {
      configured = queueNameOf(group.getBrokerTransport());
    }
    if (configured != null) {
      return configured;
    }
    return dataTopic(
        topicPrefix(connection), mappingName, requirePublisherId(connection), group.getName());
  }

  /**
   * Resolve the metadata queue name for one DataSetWriter: the writer's configured {@code
   * metaDataQueueName} when present, the group's (Milo-local; it does not round-trip through the
   * Part 14 configuration model) when present, otherwise the derived {@link #metaDataTopic(String,
   * String, PublisherId, String, String)}.
   *
   * @param connection the connection config (topic prefix source).
   * @param mappingName the group's message mapping name.
   * @param group the WriterGroup config.
   * @param writer the DataSetWriter config.
   * @return the resolved metadata queue name.
   * @throws IllegalArgumentException if no queue name is configured and the connection has no
   *     publisher id to derive one from.
   */
  public static String resolveMetaDataQueueName(
      PubSubConnectionConfig connection,
      String mappingName,
      WriterGroupConfig group,
      DataSetWriterConfig writer) {

    String configured = metaDataQueueNameOf(writer.getBrokerTransport());
    if (configured == null) {
      configured = metaDataQueueNameOf(group.getBrokerTransport());
    }
    if (configured != null) {
      return configured;
    }

    return metaDataTopic(
        topicPrefix(connection),
        mappingName,
        requirePublisherId(connection),
        group.getName(),
        writer.getName());
  }

  private static @Nullable String queueNameOf(@Nullable BrokerTransportSettings settings) {
    if (settings != null && settings.getQueueName() != null && !settings.getQueueName().isEmpty()) {
      return settings.getQueueName();
    }
    return null;
  }

  private static @Nullable String metaDataQueueNameOf(@Nullable BrokerTransportSettings settings) {
    if (settings != null
        && settings.getMetaDataQueueName() != null
        && !settings.getMetaDataQueueName().isEmpty()) {
      return settings.getMetaDataQueueName();
    }
    return null;
  }

  private static PublisherId requirePublisherId(PubSubConnectionConfig connection) {
    PublisherId publisherId = connection.publisherId();
    if (publisherId == null) {
      throw new IllegalArgumentException(
          "connection '%s' has no PublisherId; a topic cannot be derived"
              .formatted(connection.name()));
    }
    return publisherId;
  }
}
