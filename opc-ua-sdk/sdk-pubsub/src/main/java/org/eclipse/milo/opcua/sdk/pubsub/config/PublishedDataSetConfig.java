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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * Configuration of a published dataset: an ordered list of {@link FieldDefinition}s plus a
 * configuration version.
 *
 * <p>Published datasets are referenced by DataSetWriters via {@link PublishedDataSetRef} and
 * supplied with values at runtime by a {@code PublishedDataSetSource} bound to the same ref.
 * Corresponds to the Part 14 {@code PublishedDataSetDataType} with a {@code
 * PublishedDataItemsDataType} source.
 */
public final class PublishedDataSetConfig {

  private final String name;
  private final boolean enabled;
  private final List<FieldDefinition> fields;
  private final UInteger configurationVersionMajor;
  private final UInteger configurationVersionMinor;
  private final Map<QualifiedName, Variant> properties;

  private PublishedDataSetConfig(Builder builder) {
    this.name = builder.name;
    this.enabled = builder.enabled;
    this.fields = List.copyOf(builder.fields);
    this.configurationVersionMajor = builder.configurationVersionMajor;
    this.configurationVersionMinor = builder.configurationVersionMinor;
    this.properties = Collections.unmodifiableMap(new LinkedHashMap<>(builder.properties));
  }

  /**
   * Get the name of this published dataset.
   *
   * @return the name of this published dataset.
   */
  public String getName() {
    return name;
  }

  /**
   * Get whether this published dataset is enabled.
   *
   * @return {@code true} if this published dataset is enabled.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Get the field definitions, in dataset (wire) order.
   *
   * @return the field definitions.
   */
  public List<FieldDefinition> getFields() {
    return fields;
  }

  /**
   * Get the major configuration version.
   *
   * @return the major configuration version; defaults to 1.
   */
  public UInteger getConfigurationVersionMajor() {
    return configurationVersionMajor;
  }

  /**
   * Get the minor configuration version.
   *
   * @return the minor configuration version; defaults to 1.
   */
  public UInteger getConfigurationVersionMinor() {
    return configurationVersionMinor;
  }

  /**
   * Get the additional properties of this published dataset, in insertion order.
   *
   * @return an unmodifiable view of the additional properties.
   */
  public Map<QualifiedName, Variant> getProperties() {
    return properties;
  }

  /**
   * Get a {@link PublishedDataSetRef} referencing this published dataset by name.
   *
   * @return a {@link PublishedDataSetRef} referencing this published dataset.
   */
  public PublishedDataSetRef ref() {
    return new PublishedDataSetRef(name);
  }

  /**
   * Create a {@link Builder} pre-populated with the values of this config.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder(name);
    builder.enabled = enabled;
    builder.fields.addAll(fields);
    builder.configurationVersionMajor = configurationVersionMajor;
    builder.configurationVersionMinor = configurationVersionMinor;
    builder.properties.putAll(properties);
    return builder;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof PublishedDataSetConfig that)) {
      return false;
    }
    return enabled == that.enabled
        && name.equals(that.name)
        && fields.equals(that.fields)
        && configurationVersionMajor.equals(that.configurationVersionMajor)
        && configurationVersionMinor.equals(that.configurationVersionMinor)
        && properties.equals(that.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        name, enabled, fields, configurationVersionMajor, configurationVersionMinor, properties);
  }

  @Override
  public String toString() {
    return "PublishedDataSetConfig{name='%s', enabled=%s, fields=%s, configurationVersion=(%s, %s), properties=%s}"
        .formatted(
            name,
            enabled,
            fields,
            configurationVersionMajor,
            configurationVersionMinor,
            properties);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @param name the name of the published dataset.
   * @return a new {@link Builder}.
   */
  public static Builder builder(String name) {
    return new Builder(name);
  }

  /** A builder of {@link PublishedDataSetConfig} instances. */
  public static final class Builder {

    private final String name;
    private boolean enabled = true;
    private final List<FieldDefinition> fields = new ArrayList<>();
    private UInteger configurationVersionMajor = uint(1);
    private UInteger configurationVersionMinor = uint(1);
    private final Map<QualifiedName, Variant> properties = new LinkedHashMap<>();

    private Builder(String name) {
      this.name = name;
    }

    /**
     * Set whether the published dataset is enabled.
     *
     * @param enabled {@code true} if the published dataset is enabled.
     * @return this {@link Builder}.
     */
    public Builder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    /**
     * Add a field definition. Field order defines wire order.
     *
     * @param field the field definition to add.
     * @return this {@link Builder}.
     */
    public Builder field(FieldDefinition field) {
      fields.add(field);
      return this;
    }

    /**
     * Set the configuration version.
     *
     * @param major the major configuration version.
     * @param minor the minor configuration version.
     * @return this {@link Builder}.
     */
    public Builder configurationVersion(UInteger major, UInteger minor) {
      this.configurationVersionMajor = major;
      this.configurationVersionMinor = minor;
      return this;
    }

    /**
     * Add an additional property of the published dataset.
     *
     * @param name the name of the property.
     * @param value the value of the property.
     * @return this {@link Builder}.
     */
    public Builder property(QualifiedName name, Variant value) {
      properties.put(name, value);
      return this;
    }

    /**
     * Build a {@link PublishedDataSetConfig} from the configured values.
     *
     * @return a new {@link PublishedDataSetConfig}.
     * @throws PubSubConfigValidationException if the name is empty or two fields share a name or a
     *     DataSetFieldId.
     */
    public PublishedDataSetConfig build() {
      if (name.isEmpty()) {
        throw new PubSubConfigValidationException("PublishedDataSetConfig: name must not be empty");
      }

      Set<String> fieldNames = new HashSet<>();
      Set<UUID> fieldIds = new HashSet<>();
      for (FieldDefinition field : fields) {
        if (!fieldNames.add(field.getName())) {
          throw new PubSubConfigValidationException(
              "PublishedDataSetConfig '%s': duplicate field name '%s'"
                  .formatted(name, field.getName()));
        }
        if (!fieldIds.add(field.getDataSetFieldId())) {
          throw new PubSubConfigValidationException(
              "PublishedDataSetConfig '%s': duplicate dataSetFieldId %s on field '%s'"
                  .formatted(name, field.getDataSetFieldId(), field.getName()));
        }
      }

      return new PublishedDataSetConfig(this);
    }
  }
}
