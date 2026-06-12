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

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * A dataset reader: selects DataSetMessages from received NetworkMessages by PublisherId,
 * WriterGroupId, and DataSetWriterId filters, decodes their fields, and delivers them to listeners
 * and the optional subscribed dataset mapping.
 *
 * <p>Filters left unset act as wildcards: the reader matches any value for that filter.
 */
public final class DataSetReaderConfig {

  private final String name;
  private final boolean enabled;
  private final @Nullable PublisherId publisherId;
  private final @Nullable UShort writerGroupId;
  private final @Nullable UShort dataSetWriterId;
  private final @Nullable DataSetMetaDataConfig dataSetMetaData;
  private final MetadataPolicy metadataPolicy;
  private final @Nullable SubscribedDataSetSpec subscribedDataSet;
  private final DataSetReaderMessageSettings settings;
  private final @Nullable BrokerTransportSettings brokerTransport;
  private final Duration messageReceiveTimeout;
  private final UInteger keyFrameCount;
  private final @Nullable ExtensionObject rawTransportSettings;
  private final @Nullable ExtensionObject rawMessageSettings;
  private final Map<QualifiedName, Variant> properties;

  private DataSetReaderConfig(Builder builder) {
    this.name = builder.name;
    this.enabled = builder.enabled;
    this.publisherId = builder.publisherId;
    this.writerGroupId = builder.writerGroupId;
    this.dataSetWriterId = builder.dataSetWriterId;
    this.dataSetMetaData = builder.dataSetMetaData;
    this.metadataPolicy = builder.metadataPolicy;
    this.subscribedDataSet = builder.subscribedDataSet;
    this.settings = builder.settings;
    this.brokerTransport = builder.brokerTransport;
    this.messageReceiveTimeout = builder.messageReceiveTimeout;
    this.keyFrameCount = builder.keyFrameCount;
    this.rawTransportSettings = builder.rawTransportSettings;
    this.rawMessageSettings = builder.rawMessageSettings;
    this.properties = Collections.unmodifiableMap(new LinkedHashMap<>(builder.properties));
  }

  /**
   * Get the name of this dataset reader, unique among the readers of its group.
   *
   * @return the dataset reader name.
   */
  public String getName() {
    return name;
  }

  /**
   * Check if this dataset reader is enabled.
   *
   * @return {@code true} if this dataset reader is enabled.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Get the PublisherId filter.
   *
   * @return the {@link PublisherId} this reader matches, or {@code null} to match any publisher.
   */
  public @Nullable PublisherId getPublisherId() {
    return publisherId;
  }

  /**
   * Get the WriterGroupId filter.
   *
   * @return the WriterGroupId this reader matches, or {@code null} to match any writer group.
   */
  public @Nullable UShort getWriterGroupId() {
    return writerGroupId;
  }

  /**
   * Get the DataSetWriterId filter.
   *
   * @return the DataSetWriterId this reader matches, or {@code null} to match any dataset writer.
   */
  public @Nullable UShort getDataSetWriterId() {
    return dataSetWriterId;
  }

  /**
   * Get the locally configured metadata used to decode received DataSetMessages.
   *
   * @return the {@link DataSetMetaDataConfig}, or {@code null} if no metadata is configured
   *     locally.
   */
  public @Nullable DataSetMetaDataConfig getDataSetMetaData() {
    return dataSetMetaData;
  }

  /**
   * Get the policy governing how this reader obtains and validates metadata.
   *
   * @return the {@link MetadataPolicy}.
   */
  public MetadataPolicy getMetadataPolicy() {
    return metadataPolicy;
  }

  /**
   * Get the subscribed dataset mapping that received field values are dispatched to.
   *
   * @return the {@link SubscribedDataSetSpec}, or {@code null} if values are only delivered to
   *     listeners.
   */
  public @Nullable SubscribedDataSetSpec getSubscribedDataSet() {
    return subscribedDataSet;
  }

  /**
   * Get the message mapping settings of this reader.
   *
   * @return the {@link DataSetReaderMessageSettings}; UADP with Part 14 Annex A.2.2 "UADP-Dynamic"
   *     defaults if not configured.
   */
  public DataSetReaderMessageSettings getSettings() {
    return settings;
  }

  /**
   * Get the broker transport settings of this reader, used on broker-based connections.
   *
   * @return the {@link BrokerTransportSettings}, or {@code null} if not configured.
   */
  public @Nullable BrokerTransportSettings getBrokerTransport() {
    return brokerTransport;
  }

  /**
   * Get the maximum acceptable gap between received DataSetMessages before the reader transitions
   * to the Error state; it returns to Operational on the next received message.
   *
   * @return the message receive timeout; {@link Duration#ZERO} means the timeout is disabled.
   */
  public Duration getMessageReceiveTimeout() {
    return messageReceiveTimeout;
  }

  /**
   * Get the key frame count expected from the matched dataset writer.
   *
   * @return the key frame count; 0 means not configured.
   */
  public UInteger getKeyFrameCount() {
    return keyFrameCount;
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
   * Get the reader properties, mapped to and from the Part 14 KeyValuePair array.
   *
   * @return the reader properties, in insertion order.
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
    builder.publisherId = publisherId;
    builder.writerGroupId = writerGroupId;
    builder.dataSetWriterId = dataSetWriterId;
    builder.dataSetMetaData = dataSetMetaData;
    builder.metadataPolicy = metadataPolicy;
    builder.subscribedDataSet = subscribedDataSet;
    builder.settings = settings;
    builder.brokerTransport = brokerTransport;
    builder.messageReceiveTimeout = messageReceiveTimeout;
    builder.keyFrameCount = keyFrameCount;
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
    if (!(o instanceof DataSetReaderConfig that)) {
      return false;
    }
    return name.equals(that.name)
        && enabled == that.enabled
        && Objects.equals(publisherId, that.publisherId)
        && Objects.equals(writerGroupId, that.writerGroupId)
        && Objects.equals(dataSetWriterId, that.dataSetWriterId)
        && Objects.equals(dataSetMetaData, that.dataSetMetaData)
        && metadataPolicy == that.metadataPolicy
        && Objects.equals(subscribedDataSet, that.subscribedDataSet)
        && settings.equals(that.settings)
        && Objects.equals(brokerTransport, that.brokerTransport)
        && messageReceiveTimeout.equals(that.messageReceiveTimeout)
        && keyFrameCount.equals(that.keyFrameCount)
        && Objects.equals(rawTransportSettings, that.rawTransportSettings)
        && Objects.equals(rawMessageSettings, that.rawMessageSettings)
        && properties.equals(that.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        name,
        enabled,
        publisherId,
        writerGroupId,
        dataSetWriterId,
        dataSetMetaData,
        metadataPolicy,
        subscribedDataSet,
        settings,
        brokerTransport,
        messageReceiveTimeout,
        keyFrameCount,
        rawTransportSettings,
        rawMessageSettings,
        properties);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @param name the dataset reader name.
   * @return a new {@link Builder}.
   */
  public static Builder builder(String name) {
    return new Builder(name);
  }

  /** A builder of {@link DataSetReaderConfig} instances. */
  public static final class Builder {

    private final String name;
    private boolean enabled = true;
    private @Nullable PublisherId publisherId;
    private @Nullable UShort writerGroupId;
    private @Nullable UShort dataSetWriterId;
    private @Nullable DataSetMetaDataConfig dataSetMetaData;
    private MetadataPolicy metadataPolicy = MetadataPolicy.REQUIRE_CONFIGURED;
    private @Nullable SubscribedDataSetSpec subscribedDataSet;
    private DataSetReaderMessageSettings settings = UadpDataSetReaderSettings.builder().build();
    private @Nullable BrokerTransportSettings brokerTransport;
    private Duration messageReceiveTimeout = Duration.ZERO;
    private UInteger keyFrameCount = uint(0);
    private @Nullable ExtensionObject rawTransportSettings;
    private @Nullable ExtensionObject rawMessageSettings;
    private final Map<QualifiedName, Variant> properties = new LinkedHashMap<>();

    private Builder(String name) {
      this.name = name;
    }

    /**
     * Set whether this dataset reader is enabled.
     *
     * @param enabled {@code true} to enable this dataset reader; defaults to {@code true}.
     * @return this {@link Builder}.
     */
    public Builder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    /**
     * Set the PublisherId filter.
     *
     * @param publisherId the {@link PublisherId} to match; unset matches any publisher.
     * @return this {@link Builder}.
     */
    public Builder publisherId(PublisherId publisherId) {
      this.publisherId = publisherId;
      return this;
    }

    /**
     * Set the WriterGroupId filter.
     *
     * @param writerGroupId the WriterGroupId to match; unset matches any writer group.
     * @return this {@link Builder}.
     */
    public Builder writerGroupId(UShort writerGroupId) {
      this.writerGroupId = writerGroupId;
      return this;
    }

    /**
     * Set the DataSetWriterId filter.
     *
     * @param dataSetWriterId the DataSetWriterId to match; unset matches any dataset writer.
     * @return this {@link Builder}.
     */
    public Builder dataSetWriterId(UShort dataSetWriterId) {
      this.dataSetWriterId = dataSetWriterId;
      return this;
    }

    /**
     * Set the locally configured metadata used to decode received DataSetMessages.
     *
     * @param metaData the {@link DataSetMetaDataConfig}.
     * @return this {@link Builder}.
     */
    public Builder dataSetMetaData(DataSetMetaDataConfig metaData) {
      this.dataSetMetaData = metaData;
      return this;
    }

    /**
     * Set the policy governing how this reader obtains and validates metadata.
     *
     * @param policy the {@link MetadataPolicy}; defaults to {@link
     *     MetadataPolicy#REQUIRE_CONFIGURED}.
     * @return this {@link Builder}.
     */
    public Builder metadataPolicy(MetadataPolicy policy) {
      this.metadataPolicy = policy;
      return this;
    }

    /**
     * Set the subscribed dataset mapping that received field values are dispatched to.
     *
     * @param spec the {@link SubscribedDataSetSpec}.
     * @return this {@link Builder}.
     */
    public Builder subscribedDataSet(SubscribedDataSetSpec spec) {
      this.subscribedDataSet = spec;
      return this;
    }

    /**
     * Set the message mapping settings of this reader.
     *
     * @param settings the {@link DataSetReaderMessageSettings}, either {@link
     *     UadpDataSetReaderSettings} or {@link JsonDataSetReaderSettings}; defaults to UADP with
     *     Part 14 Annex A.2.2 "UADP-Dynamic" values.
     * @return this {@link Builder}.
     */
    public Builder settings(DataSetReaderMessageSettings settings) {
      this.settings = settings;
      return this;
    }

    /**
     * Set the broker transport settings of this reader, used on broker-based connections.
     *
     * @param settings the {@link BrokerTransportSettings}.
     * @return this {@link Builder}.
     */
    public Builder brokerTransport(BrokerTransportSettings settings) {
      this.brokerTransport = settings;
      return this;
    }

    /**
     * Set the maximum acceptable gap between received DataSetMessages before the reader transitions
     * to the Error state.
     *
     * @param timeout the message receive timeout; {@link Duration#ZERO} (the default) disables the
     *     timeout.
     * @return this {@link Builder}.
     */
    public Builder messageReceiveTimeout(Duration timeout) {
      this.messageReceiveTimeout = timeout;
      return this;
    }

    /**
     * Set the key frame count expected from the matched dataset writer.
     *
     * @param keyFrameCount the key frame count; defaults to 0 (not configured).
     * @return this {@link Builder}.
     */
    public Builder keyFrameCount(UInteger keyFrameCount) {
      this.keyFrameCount = keyFrameCount;
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
     * Add a reader property, mapped to and from the Part 14 KeyValuePair array.
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
     * Build a new {@link DataSetReaderConfig} from the values configured on this builder.
     *
     * @return a new {@link DataSetReaderConfig}.
     * @throws PubSubConfigValidationException if the name is blank or the message receive timeout
     *     is negative.
     */
    public DataSetReaderConfig build() {
      if (name.isBlank()) {
        throw new PubSubConfigValidationException("dataset reader: name must not be blank");
      }
      if (messageReceiveTimeout.isNegative()) {
        throw new PubSubConfigValidationException(
            "dataset reader '" + name + "': messageReceiveTimeout must not be negative");
      }

      return new DataSetReaderConfig(this);
    }
  }
}
