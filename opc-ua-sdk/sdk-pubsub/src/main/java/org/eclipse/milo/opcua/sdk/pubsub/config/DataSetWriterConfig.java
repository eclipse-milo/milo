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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.jspecify.annotations.Nullable;

/**
 * A dataset writer: encodes snapshots of one published dataset into DataSetMessages, published as
 * part of its containing {@link WriterGroupConfig}.
 */
public final class DataSetWriterConfig {

  private final String name;
  private final boolean enabled;
  private final PublishedDataSetRef dataSet;
  private final UShort dataSetWriterId;
  private final UInteger keyFrameCount;
  private final DataSetFieldContentMask fieldContentMask;
  private final DataSetWriterMessageSettings settings;
  private final @Nullable BrokerTransportSettings brokerTransport;
  private final @Nullable ExtensionObject rawTransportSettings;
  private final @Nullable ExtensionObject rawMessageSettings;
  private final Map<QualifiedName, Variant> properties;

  private DataSetWriterConfig(
      Builder builder, PublishedDataSetRef dataSet, UShort dataSetWriterId) {

    this.name = builder.name;
    this.enabled = builder.enabled;
    this.dataSet = dataSet;
    this.dataSetWriterId = dataSetWriterId;
    this.keyFrameCount = builder.keyFrameCount;
    this.fieldContentMask = builder.fieldContentMask;
    this.settings = builder.settings;
    this.brokerTransport = builder.brokerTransport;
    this.rawTransportSettings = builder.rawTransportSettings;
    this.rawMessageSettings = builder.rawMessageSettings;
    this.properties = Collections.unmodifiableMap(new LinkedHashMap<>(builder.properties));
  }

  /**
   * Get the name of this dataset writer, unique among the writers of its group.
   *
   * @return the dataset writer name.
   */
  public String getName() {
    return name;
  }

  /**
   * Check if this dataset writer is enabled.
   *
   * @return {@code true} if this dataset writer is enabled.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Get the reference to the published dataset this writer publishes.
   *
   * @return the {@link PublishedDataSetRef}.
   */
  public PublishedDataSetRef getDataSet() {
    return dataSet;
  }

  /**
   * Get the DataSetWriterId: the wire identifier of this writer, unique within the publisher.
   *
   * @return the DataSetWriterId.
   */
  public UShort getDataSetWriterId() {
    return dataSetWriterId;
  }

  /**
   * Get the key frame count: the maximum number of times the publishing interval expires before a
   * key frame with values for all fields is sent (Part 14 §6.2.4.3); the cycles in between send
   * delta frames of the changed fields, and a no-change cycle sends nothing.
   *
   * @return the key frame count; 1 means every message is a key frame. 0 (the spec's
   *     non-cyclic/event value, which Milo does not model) is clamped to 1 at runtime, so configs
   *     round-trip unmodified. For JSON writers a value greater than 1 requires the
   *     DataSetMessageHeader and MessageType content-mask members (Part 14 Annex A.3.3.4); for UADP
   *     writers it cannot be combined with a non-zero ConfiguredSize (fixed-size layouts are
   *     key-frame-only, Annex A.2.1.7); both enforced at startup and reconfigure.
   */
  public UInteger getKeyFrameCount() {
    return keyFrameCount;
  }

  /**
   * Get the mask selecting how dataset field values are represented on the wire.
   *
   * @return the {@link DataSetFieldContentMask}; an empty mask means fields are encoded as
   *     Variants.
   */
  public DataSetFieldContentMask getFieldContentMask() {
    return fieldContentMask;
  }

  /**
   * Get the message mapping settings of this writer.
   *
   * @return the {@link DataSetWriterMessageSettings}; UADP with Part 14 Annex A.2.2 "UADP-Dynamic"
   *     defaults if not configured.
   */
  public DataSetWriterMessageSettings getSettings() {
    return settings;
  }

  /**
   * Get the broker transport settings of this writer, used on broker-based connections; overrides
   * the settings of the containing group.
   *
   * @return the {@link BrokerTransportSettings}, or {@code null} if not configured.
   */
  public @Nullable BrokerTransportSettings getBrokerTransport() {
    return brokerTransport;
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
   * Get the writer properties, mapped to and from the Part 14 KeyValuePair array.
   *
   * @return the writer properties, in insertion order.
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
    builder.dataSet = dataSet;
    builder.dataSetWriterId = dataSetWriterId;
    builder.keyFrameCount = keyFrameCount;
    builder.fieldContentMask = fieldContentMask;
    builder.settings = settings;
    builder.brokerTransport = brokerTransport;
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
    if (!(o instanceof DataSetWriterConfig that)) {
      return false;
    }
    return name.equals(that.name)
        && enabled == that.enabled
        && dataSet.equals(that.dataSet)
        && dataSetWriterId.equals(that.dataSetWriterId)
        && keyFrameCount.equals(that.keyFrameCount)
        && fieldContentMask.equals(that.fieldContentMask)
        && settings.equals(that.settings)
        && Objects.equals(brokerTransport, that.brokerTransport)
        && Objects.equals(rawTransportSettings, that.rawTransportSettings)
        && Objects.equals(rawMessageSettings, that.rawMessageSettings)
        && properties.equals(that.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        name,
        enabled,
        dataSet,
        dataSetWriterId,
        keyFrameCount,
        fieldContentMask,
        settings,
        brokerTransport,
        rawTransportSettings,
        rawMessageSettings,
        properties);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @param name the dataset writer name.
   * @return a new {@link Builder}.
   */
  public static Builder builder(String name) {
    return new Builder(name);
  }

  /** A builder of {@link DataSetWriterConfig} instances. */
  public static final class Builder {

    private final String name;
    private boolean enabled = true;
    private @Nullable PublishedDataSetRef dataSet;
    private @Nullable UShort dataSetWriterId;
    private UInteger keyFrameCount = uint(1);
    private DataSetFieldContentMask fieldContentMask = DataSetFieldContentMask.of();
    private DataSetWriterMessageSettings settings = UadpDataSetWriterSettings.builder().build();
    private @Nullable BrokerTransportSettings brokerTransport;
    private @Nullable ExtensionObject rawTransportSettings;
    private @Nullable ExtensionObject rawMessageSettings;
    private final Map<QualifiedName, Variant> properties = new LinkedHashMap<>();

    private Builder(String name) {
      this.name = name;
    }

    /**
     * Set whether this dataset writer is enabled.
     *
     * @param enabled {@code true} to enable this dataset writer; defaults to {@code true}.
     * @return this {@link Builder}.
     */
    public Builder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    /**
     * Set the reference to the published dataset this writer publishes.
     *
     * @param dataSet the {@link PublishedDataSetRef}; must resolve to a published dataset of the
     *     same {@code PubSubConfig}, validated when the full configuration is assembled.
     * @return this {@link Builder}.
     */
    public Builder dataSet(PublishedDataSetRef dataSet) {
      this.dataSet = dataSet;
      return this;
    }

    /**
     * Set the DataSetWriterId: the wire identifier of this writer.
     *
     * @param dataSetWriterId the DataSetWriterId; must be non-zero and unique within the publisher.
     * @return this {@link Builder}.
     */
    public Builder dataSetWriterId(UShort dataSetWriterId) {
      this.dataSetWriterId = dataSetWriterId;
      return this;
    }

    /**
     * Set the key frame count: the maximum number of times the publishing interval expires before a
     * key frame with values for all fields is sent (Part 14 §6.2.4.3); the cycles in between send
     * delta frames of the changed fields, and a no-change cycle sends nothing.
     *
     * @param keyFrameCount the key frame count; defaults to 1 (every message is a key frame). 0 is
     *     clamped to 1 at runtime. For JSON writers a value greater than 1 requires the
     *     DataSetMessageHeader and MessageType content-mask members (Part 14 Annex A.3.3.4); for
     *     UADP writers it cannot be combined with a non-zero ConfiguredSize (fixed-size layouts are
     *     key-frame-only, Annex A.2.1.7); both enforced at startup and reconfigure.
     * @return this {@link Builder}.
     */
    public Builder keyFrameCount(UInteger keyFrameCount) {
      this.keyFrameCount = keyFrameCount;
      return this;
    }

    /**
     * Set the mask selecting how dataset field values are represented on the wire.
     *
     * @param mask the {@link DataSetFieldContentMask}; defaults to an empty mask (fields encoded as
     *     Variants).
     * @return this {@link Builder}.
     */
    public Builder fieldContentMask(DataSetFieldContentMask mask) {
      this.fieldContentMask = mask;
      return this;
    }

    /**
     * Set the message mapping settings of this writer.
     *
     * @param settings the {@link DataSetWriterMessageSettings}, either {@link
     *     UadpDataSetWriterSettings} or {@link JsonDataSetWriterSettings}; defaults to UADP with
     *     Part 14 Annex A.2.2 "UADP-Dynamic" values.
     * @return this {@link Builder}.
     */
    public Builder settings(DataSetWriterMessageSettings settings) {
      this.settings = settings;
      return this;
    }

    /**
     * Set the broker transport settings of this writer, used on broker-based connections; overrides
     * the settings of the containing group.
     *
     * @param settings the {@link BrokerTransportSettings}.
     * @return this {@link Builder}.
     */
    public Builder brokerTransport(BrokerTransportSettings settings) {
      this.brokerTransport = settings;
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
     * Add a writer property, mapped to and from the Part 14 KeyValuePair array.
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
     * Build a new {@link DataSetWriterConfig} from the values configured on this builder.
     *
     * @return a new {@link DataSetWriterConfig}.
     * @throws PubSubConfigValidationException if the name is blank, no dataset reference is
     *     configured, or the DataSetWriterId is missing or zero.
     */
    public DataSetWriterConfig build() {
      if (name.isBlank()) {
        throw new PubSubConfigValidationException("dataset writer: name must not be blank");
      }
      if (dataSet == null) {
        throw new PubSubConfigValidationException(
            "dataset writer '" + name + "': dataSet is required");
      }
      if (dataSetWriterId == null) {
        throw new PubSubConfigValidationException(
            "dataset writer '" + name + "': dataSetWriterId is required");
      }
      if (dataSetWriterId.intValue() == 0) {
        throw new PubSubConfigValidationException(
            "dataset writer '" + name + "': dataSetWriterId must be non-zero");
      }

      return new DataSetWriterConfig(this, dataSet, dataSetWriterId);
    }
  }
}
