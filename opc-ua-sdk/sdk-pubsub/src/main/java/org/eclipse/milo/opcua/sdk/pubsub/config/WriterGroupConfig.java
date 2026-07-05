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

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * A writer group: the publishing side unit that periodically encodes the DataSetMessages of its
 * {@link DataSetWriterConfig}s into NetworkMessages and sends them on its connection.
 */
public final class WriterGroupConfig {

  private final String name;
  private final boolean enabled;
  private final UShort writerGroupId;
  private final Duration publishingInterval;
  private final @Nullable Duration keepAliveTime;
  private final UByte priority;
  private final UInteger maxNetworkMessageSize;
  private final @Nullable MessageSecurityConfig messageSecurity;
  private final WriterGroupMessageSettings messageSettings;
  private final @Nullable BrokerTransportSettings brokerTransport;
  private final List<DataSetWriterConfig> dataSetWriters;
  private final @Nullable ExtensionObject rawTransportSettings;
  private final @Nullable ExtensionObject rawMessageSettings;
  private final Map<QualifiedName, Variant> properties;

  private WriterGroupConfig(Builder builder, UShort writerGroupId) {
    this.name = builder.name;
    this.enabled = builder.enabled;
    this.writerGroupId = writerGroupId;
    this.publishingInterval = builder.publishingInterval;
    this.keepAliveTime = builder.keepAliveTime;
    this.priority = builder.priority;
    this.maxNetworkMessageSize = builder.maxNetworkMessageSize;
    this.messageSecurity = builder.messageSecurity;
    this.messageSettings = builder.messageSettings;
    this.brokerTransport = builder.brokerTransport;
    this.dataSetWriters = List.copyOf(builder.dataSetWriters);
    this.rawTransportSettings = builder.rawTransportSettings;
    this.rawMessageSettings = builder.rawMessageSettings;
    this.properties = Collections.unmodifiableMap(new LinkedHashMap<>(builder.properties));
  }

  /**
   * Get the name of this writer group, unique among the groups of its connection.
   *
   * @return the writer group name.
   */
  public String getName() {
    return name;
  }

  /**
   * Check if this writer group is enabled.
   *
   * @return {@code true} if this writer group is enabled.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Get the WriterGroupId: the wire identifier of this group, unique within the publisher.
   *
   * @return the WriterGroupId.
   */
  public UShort getWriterGroupId() {
    return writerGroupId;
  }

  /**
   * Get the interval at which this group publishes NetworkMessages.
   *
   * @return the publishing interval. {@link Duration#ZERO} selects the event-triggered publishing
   *     mode: NetworkMessages are published as events arrive instead of cyclically, valid only when
   *     every writer in the group references an event-source dataset (cross-checked when the full
   *     {@code PubSubConfig} is assembled).
   */
  public Duration getPublishingInterval() {
    return publishingInterval;
  }

  /**
   * Get the keep-alive time: the maximum quiet period before a keep-alive message is sent.
   *
   * @return the keep-alive time, or {@code null} if keep-alive messages are not sent.
   */
  public @Nullable Duration getKeepAliveTime() {
    return keepAliveTime;
  }

  /**
   * Get the relative priority of this group when multiple groups contend for publishing.
   *
   * @return the priority.
   */
  public UByte getPriority() {
    return priority;
  }

  /**
   * Get the maximum size of NetworkMessages produced by this group: the size of the complete
   * encoded NetworkMessage, before transport protocol headers (Part 14 §6.2.5.5).
   *
   * <p>When non-zero, an encoded NetworkMessage exceeding this size is skipped instead of sent and
   * recorded as a group error with {@code Bad_EncodingLimitsExceeded} (the Part 14 Annex B.3.1
   * behavior for mappings without chunking support). On OPC UA UDP connections values above 65535
   * are rejected at startup and reconfiguration (§7.3.2.1).
   *
   * <p>Enabling UADP message security ({@link #getMessageSecurity()} with mode Sign or
   * SignAndEncrypt) adds 46-47 bytes of overhead to every NetworkMessage (14-byte SecurityHeader +
   * 32-byte signature, +1 if ExtendedFlags1 was previously absent); the limit is checked against
   * the final secured size, so account for this overhead when sizing.
   *
   * @return the maximum NetworkMessage size in bytes; 0 means no enforced limit.
   */
  public UInteger getMaxNetworkMessageSize() {
    return maxNetworkMessageSize;
  }

  /**
   * Get the message security settings of this group.
   *
   * @return the {@link MessageSecurityConfig}, or {@code null} for no message security.
   */
  public @Nullable MessageSecurityConfig getMessageSecurity() {
    return messageSecurity;
  }

  /**
   * Get the message mapping settings of this group.
   *
   * @return the {@link WriterGroupMessageSettings}; UADP with Part 14 Annex A.2.2 "UADP-Dynamic"
   *     defaults if not configured.
   */
  public WriterGroupMessageSettings getMessageSettings() {
    return messageSettings;
  }

  /**
   * Get the broker transport settings of this group, used on broker-based connections.
   *
   * @return the {@link BrokerTransportSettings}, or {@code null} if not configured.
   */
  public @Nullable BrokerTransportSettings getBrokerTransport() {
    return brokerTransport;
  }

  /**
   * Get the dataset writers of this group.
   *
   * @return the {@link DataSetWriterConfig}s of this group.
   */
  public List<DataSetWriterConfig> getDataSetWriters() {
    return dataSetWriters;
  }

  /**
   * Get the raw transport settings escape hatch.
   *
   * @return an {@link ExtensionObject} that overrides the typed transport settings when mapping to
   *     the Part 14 configuration model, or {@code null} if not set.
   */
  public @Nullable ExtensionObject getRawTransportSettings() {
    return rawTransportSettings;
  }

  /**
   * Get the raw message settings escape hatch.
   *
   * @return an {@link ExtensionObject} that overrides the typed message settings when mapping to
   *     the Part 14 configuration model, or {@code null} if not set.
   */
  public @Nullable ExtensionObject getRawMessageSettings() {
    return rawMessageSettings;
  }

  /**
   * Get the group properties, mapped to and from the Part 14 KeyValuePair array.
   *
   * @return the group properties, in insertion order.
   */
  public Map<QualifiedName, Variant> getProperties() {
    return properties;
  }

  /**
   * Create a new {@link Builder} initialized with the values of this instance.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder(name);
    builder.enabled = enabled;
    builder.writerGroupId = writerGroupId;
    builder.publishingInterval = publishingInterval;
    builder.keepAliveTime = keepAliveTime;
    builder.priority = priority;
    builder.maxNetworkMessageSize = maxNetworkMessageSize;
    builder.messageSecurity = messageSecurity;
    builder.messageSettings = messageSettings;
    builder.brokerTransport = brokerTransport;
    builder.dataSetWriters.addAll(dataSetWriters);
    builder.rawTransportSettings = rawTransportSettings;
    builder.rawMessageSettings = rawMessageSettings;
    builder.properties.putAll(properties);
    return builder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof WriterGroupConfig that)) {
      return false;
    }
    return name.equals(that.name)
        && enabled == that.enabled
        && writerGroupId.equals(that.writerGroupId)
        && publishingInterval.equals(that.publishingInterval)
        && Objects.equals(keepAliveTime, that.keepAliveTime)
        && priority.equals(that.priority)
        && maxNetworkMessageSize.equals(that.maxNetworkMessageSize)
        && Objects.equals(messageSecurity, that.messageSecurity)
        && messageSettings.equals(that.messageSettings)
        && Objects.equals(brokerTransport, that.brokerTransport)
        && dataSetWriters.equals(that.dataSetWriters)
        && Objects.equals(rawTransportSettings, that.rawTransportSettings)
        && Objects.equals(rawMessageSettings, that.rawMessageSettings)
        && properties.equals(that.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        name,
        enabled,
        writerGroupId,
        publishingInterval,
        keepAliveTime,
        priority,
        maxNetworkMessageSize,
        messageSecurity,
        messageSettings,
        brokerTransport,
        dataSetWriters,
        rawTransportSettings,
        rawMessageSettings,
        properties);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @param name the writer group name.
   * @return a new {@link Builder}.
   */
  public static Builder builder(String name) {
    return new Builder(name);
  }

  /** A builder of {@link WriterGroupConfig} instances. */
  public static final class Builder {

    private final String name;
    private boolean enabled = true;
    private @Nullable UShort writerGroupId;
    private Duration publishingInterval = Duration.ofMillis(1000);
    private @Nullable Duration keepAliveTime;
    private UByte priority = ubyte(0);
    private UInteger maxNetworkMessageSize = uint(0);
    private @Nullable MessageSecurityConfig messageSecurity;
    private WriterGroupMessageSettings messageSettings = UadpWriterGroupSettings.builder().build();
    private @Nullable BrokerTransportSettings brokerTransport;
    private final List<DataSetWriterConfig> dataSetWriters = new ArrayList<>();
    private @Nullable ExtensionObject rawTransportSettings;
    private @Nullable ExtensionObject rawMessageSettings;
    private final Map<QualifiedName, Variant> properties = new LinkedHashMap<>();

    private Builder(String name) {
      this.name = name;
    }

    /**
     * Set whether this writer group is enabled.
     *
     * @param enabled {@code true} to enable this writer group; defaults to {@code true}.
     * @return this {@link Builder}.
     */
    public Builder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    /**
     * Set the WriterGroupId: the wire identifier of this group.
     *
     * @param writerGroupId the WriterGroupId; must be non-zero and unique within the publisher.
     * @return this {@link Builder}.
     */
    public Builder writerGroupId(UShort writerGroupId) {
      this.writerGroupId = writerGroupId;
      return this;
    }

    /**
     * Set the interval at which this group publishes NetworkMessages.
     *
     * @param interval the publishing interval; defaults to 1000 ms. {@link Duration#ZERO} selects
     *     the event-triggered publishing mode and is valid only when every writer in the group
     *     references an event-source dataset (cross-checked when the full {@code PubSubConfig} is
     *     assembled).
     * @return this {@link Builder}.
     */
    public Builder publishingInterval(Duration interval) {
      this.publishingInterval = interval;
      return this;
    }

    /**
     * Set the keep-alive time: the maximum quiet period before a keep-alive message is sent.
     *
     * @param keepAlive the keep-alive time; keep-alive messages are not sent unless configured.
     * @return this {@link Builder}.
     */
    public Builder keepAliveTime(Duration keepAlive) {
      this.keepAliveTime = keepAlive;
      return this;
    }

    /**
     * Set the relative priority of this group when multiple groups contend for publishing.
     *
     * @param priority the priority; defaults to 0.
     * @return this {@link Builder}.
     */
    public Builder priority(UByte priority) {
      this.priority = priority;
      return this;
    }

    /**
     * Set the maximum size of NetworkMessages produced by this group. When non-zero, encoded
     * NetworkMessages exceeding this size are skipped instead of sent and recorded as group errors
     * with {@code Bad_EncodingLimitsExceeded}; see {@link
     * WriterGroupConfig#getMaxNetworkMessageSize()}. UADP message security (mode Sign or
     * SignAndEncrypt) adds 46-47 bytes of overhead to every NetworkMessage, and the limit is
     * checked against the final secured size.
     *
     * @param maxNetworkMessageSize the maximum NetworkMessage size in bytes; defaults to 0 (no
     *     enforced limit).
     * @return this {@link Builder}.
     */
    public Builder maxNetworkMessageSize(UInteger maxNetworkMessageSize) {
      this.maxNetworkMessageSize = maxNetworkMessageSize;
      return this;
    }

    /**
     * Set the message security settings of this group.
     *
     * @param security the {@link MessageSecurityConfig}.
     * @return this {@link Builder}.
     */
    public Builder messageSecurity(MessageSecurityConfig security) {
      this.messageSecurity = security;
      return this;
    }

    /**
     * Set the message mapping settings of this group.
     *
     * @param settings the {@link WriterGroupMessageSettings}, either {@link
     *     UadpWriterGroupSettings} or {@link JsonWriterGroupSettings}; defaults to UADP with Part
     *     14 Annex A.2.2 "UADP-Dynamic" values.
     * @return this {@link Builder}.
     */
    public Builder messageSettings(WriterGroupMessageSettings settings) {
      this.messageSettings = settings;
      return this;
    }

    /**
     * Set the broker transport settings of this group, used on broker-based connections.
     *
     * @param settings the {@link BrokerTransportSettings}.
     * @return this {@link Builder}.
     */
    public Builder brokerTransport(BrokerTransportSettings settings) {
      this.brokerTransport = settings;
      return this;
    }

    /**
     * Add a dataset writer to this group.
     *
     * @param writer the {@link DataSetWriterConfig} to add.
     * @return this {@link Builder}.
     */
    public Builder dataSetWriter(DataSetWriterConfig writer) {
      dataSetWriters.add(writer);
      return this;
    }

    /**
     * Set the raw transport settings escape hatch, overriding the typed transport settings when
     * mapping to the Part 14 configuration model.
     *
     * @param value the raw transport settings {@link ExtensionObject}.
     * @return this {@link Builder}.
     */
    public Builder rawTransportSettings(ExtensionObject value) {
      this.rawTransportSettings = value;
      return this;
    }

    /**
     * Set the raw message settings escape hatch, overriding the typed message settings when mapping
     * to the Part 14 configuration model.
     *
     * @param value the raw message settings {@link ExtensionObject}.
     * @return this {@link Builder}.
     */
    public Builder rawMessageSettings(ExtensionObject value) {
      this.rawMessageSettings = value;
      return this;
    }

    /**
     * Add a group property, mapped to and from the Part 14 KeyValuePair array.
     *
     * @param name the property name.
     * @param value the property value.
     * @return this {@link Builder}.
     */
    public Builder property(QualifiedName name, Variant value) {
      properties.put(name, value);
      return this;
    }

    /**
     * Build a new {@link WriterGroupConfig} from the values configured on this builder.
     *
     * @return a new {@link WriterGroupConfig}.
     * @throws PubSubConfigValidationException if the name is blank, the WriterGroupId is missing or
     *     zero, the publishing interval is negative, or the keep-alive time is configured but not
     *     positive.
     */
    public WriterGroupConfig build() {
      if (name.isBlank()) {
        throw new PubSubConfigValidationException("writer group: name must not be blank");
      }
      if (writerGroupId == null) {
        throw new PubSubConfigValidationException(
            "writer group '" + name + "': writerGroupId is required");
      }
      if (writerGroupId.intValue() == 0) {
        throw new PubSubConfigValidationException(
            "writer group '" + name + "': writerGroupId must be non-zero");
      }
      if (publishingInterval.isNegative()) {
        throw new PubSubConfigValidationException(
            "writer group '" + name + "': publishingInterval must not be negative");
      }
      if (keepAliveTime != null && (keepAliveTime.isZero() || keepAliveTime.isNegative())) {
        throw new PubSubConfigValidationException(
            "writer group '" + name + "': keepAliveTime must be positive when configured");
      }

      return new WriterGroupConfig(this, writerGroupId);
    }
  }
}
