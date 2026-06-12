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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * A reader group: the subscribing side unit that groups {@link DataSetReaderConfig}s receiving
 * NetworkMessages on its connection.
 */
public final class ReaderGroupConfig {

  private final String name;
  private final boolean enabled;
  private final UInteger maxNetworkMessageSize;
  private final @Nullable MessageSecurityConfig messageSecurity;
  private final List<DataSetReaderConfig> dataSetReaders;
  private final @Nullable ExtensionObject rawTransportSettings;
  private final @Nullable ExtensionObject rawMessageSettings;
  private final Map<QualifiedName, Variant> properties;

  private ReaderGroupConfig(Builder builder) {
    this.name = builder.name;
    this.enabled = builder.enabled;
    this.maxNetworkMessageSize = builder.maxNetworkMessageSize;
    this.messageSecurity = builder.messageSecurity;
    this.dataSetReaders = List.copyOf(builder.dataSetReaders);
    this.rawTransportSettings = builder.rawTransportSettings;
    this.rawMessageSettings = builder.rawMessageSettings;
    this.properties = Collections.unmodifiableMap(new LinkedHashMap<>(builder.properties));
  }

  /**
   * Get the name of this reader group, unique among the groups of its connection.
   *
   * @return the reader group name.
   */
  public String getName() {
    return name;
  }

  /**
   * Check if this reader group is enabled.
   *
   * @return {@code true} if this reader group is enabled.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Get the maximum size of NetworkMessages accepted by this group.
   *
   * @return the maximum NetworkMessage size in bytes; 0 means unrestricted.
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
   * Get the dataset readers of this group.
   *
   * @return the {@link DataSetReaderConfig}s of this group.
   */
  public List<DataSetReaderConfig> getDataSetReaders() {
    return dataSetReaders;
  }

  /**
   * Get the raw transport settings escape hatch.
   *
   * @return an {@link ExtensionObject} carried into the Part 14 configuration model as the group's
   *     transport settings, or {@code null} if not set.
   */
  public @Nullable ExtensionObject getRawTransportSettings() {
    return rawTransportSettings;
  }

  /**
   * Get the raw message settings escape hatch.
   *
   * @return an {@link ExtensionObject} carried into the Part 14 configuration model as the group's
   *     message settings, or {@code null} if not set.
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
    builder.maxNetworkMessageSize = maxNetworkMessageSize;
    builder.messageSecurity = messageSecurity;
    builder.dataSetReaders.addAll(dataSetReaders);
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
    if (!(o instanceof ReaderGroupConfig that)) {
      return false;
    }
    return name.equals(that.name)
        && enabled == that.enabled
        && maxNetworkMessageSize.equals(that.maxNetworkMessageSize)
        && Objects.equals(messageSecurity, that.messageSecurity)
        && dataSetReaders.equals(that.dataSetReaders)
        && Objects.equals(rawTransportSettings, that.rawTransportSettings)
        && Objects.equals(rawMessageSettings, that.rawMessageSettings)
        && properties.equals(that.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        name,
        enabled,
        maxNetworkMessageSize,
        messageSecurity,
        dataSetReaders,
        rawTransportSettings,
        rawMessageSettings,
        properties);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @param name the reader group name.
   * @return a new {@link Builder}.
   */
  public static Builder builder(String name) {
    return new Builder(name);
  }

  /** A builder of {@link ReaderGroupConfig} instances. */
  public static final class Builder {

    private final String name;
    private boolean enabled = true;
    private UInteger maxNetworkMessageSize = uint(0);
    private @Nullable MessageSecurityConfig messageSecurity;
    private final List<DataSetReaderConfig> dataSetReaders = new ArrayList<>();
    private @Nullable ExtensionObject rawTransportSettings;
    private @Nullable ExtensionObject rawMessageSettings;
    private final Map<QualifiedName, Variant> properties = new LinkedHashMap<>();

    private Builder(String name) {
      this.name = name;
    }

    /**
     * Set whether this reader group is enabled.
     *
     * @param enabled {@code true} to enable this reader group; defaults to {@code true}.
     * @return this {@link Builder}.
     */
    public Builder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    /**
     * Set the maximum size of NetworkMessages accepted by this group.
     *
     * @param maxNetworkMessageSize the maximum NetworkMessage size in bytes; defaults to 0
     *     (unrestricted).
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
     * Add a dataset reader to this group.
     *
     * @param reader the {@link DataSetReaderConfig} to add.
     * @return this {@link Builder}.
     */
    public Builder dataSetReader(DataSetReaderConfig reader) {
      dataSetReaders.add(reader);
      return this;
    }

    /**
     * Set the raw transport settings escape hatch, carried into the Part 14 configuration model as
     * the group's transport settings.
     *
     * @param value the raw transport settings {@link ExtensionObject}.
     * @return this {@link Builder}.
     */
    public Builder rawTransportSettings(ExtensionObject value) {
      this.rawTransportSettings = value;
      return this;
    }

    /**
     * Set the raw message settings escape hatch, carried into the Part 14 configuration model as
     * the group's message settings.
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
     * Build a new {@link ReaderGroupConfig} from the values configured on this builder.
     *
     * @return a new {@link ReaderGroupConfig}.
     * @throws PubSubConfigValidationException if the name is blank.
     */
    public ReaderGroupConfig build() {
      if (name.isBlank()) {
        throw new PubSubConfigValidationException("reader group: name must not be blank");
      }

      return new ReaderGroupConfig(this);
    }
  }
}
