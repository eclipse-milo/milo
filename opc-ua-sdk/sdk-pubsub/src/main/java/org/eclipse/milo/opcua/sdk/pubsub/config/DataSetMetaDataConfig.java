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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * Authored DataSetMetaData used by a DataSetReader (or standalone subscribed dataset) to decode
 * received DataSetMessages without depending on wire-discovered metadata.
 *
 * <p>Corresponds to the Part 14 {@code DataSetMetaDataType}.
 */
public final class DataSetMetaDataConfig {

  private final String name;
  private final List<Field> fields;
  private final @Nullable UUID dataSetClassId;
  private final UInteger configurationVersionMajor;
  private final UInteger configurationVersionMinor;

  private DataSetMetaDataConfig(Builder builder) {
    this.name = builder.name;
    this.fields = List.copyOf(builder.fields);
    this.dataSetClassId = builder.dataSetClassId;
    this.configurationVersionMajor = builder.configurationVersionMajor;
    this.configurationVersionMinor = builder.configurationVersionMinor;
  }

  /**
   * Get the name of this metadata.
   *
   * @return the name of this metadata.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the field entries, in dataset (wire) order.
   *
   * @return the field entries.
   */
  public List<Field> fields() {
    return fields;
  }

  /**
   * Get the DataSetClassId identifying the dataset class this metadata describes.
   *
   * @return the DataSetClassId, if configured.
   */
  public Optional<UUID> getDataSetClassId() {
    return Optional.ofNullable(dataSetClassId);
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
   * Create a {@link Builder} pre-populated with the values of this config.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder(name);
    builder.fields.addAll(fields);
    builder.dataSetClassId = dataSetClassId;
    builder.configurationVersionMajor = configurationVersionMajor;
    builder.configurationVersionMinor = configurationVersionMinor;
    return builder;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof DataSetMetaDataConfig that)) {
      return false;
    }
    return name.equals(that.name)
        && fields.equals(that.fields)
        && Objects.equals(dataSetClassId, that.dataSetClassId)
        && configurationVersionMajor.equals(that.configurationVersionMajor)
        && configurationVersionMinor.equals(that.configurationVersionMinor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        name, fields, dataSetClassId, configurationVersionMajor, configurationVersionMinor);
  }

  @Override
  public String toString() {
    return "DataSetMetaDataConfig{name='%s', fields=%s, dataSetClassId=%s, configurationVersion=(%s, %s)}"
        .formatted(
            name, fields, dataSetClassId, configurationVersionMajor, configurationVersionMinor);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @param name the name of the metadata.
   * @return a new {@link Builder}.
   */
  public static Builder builder(String name) {
    return new Builder(name);
  }

  /**
   * A single field entry in a {@link DataSetMetaDataConfig}.
   *
   * @param name the name of the field.
   * @param dataTypeId the NodeId of the field's DataType.
   * @param dataSetFieldId the stable wire identity of the field.
   * @param valueRank the value rank of the field; {@code -1} (scalar) by default.
   * @param arrayDimensions the array dimensions of the field, or {@code null} if not applicable.
   */
  public record Field(
      String name,
      NodeId dataTypeId,
      UUID dataSetFieldId,
      int valueRank,
      UInteger @Nullable [] arrayDimensions) {

    /**
     * Create a {@link Field}, defensively copying {@code arrayDimensions}.
     *
     * @param name the name of the field.
     * @param dataTypeId the NodeId of the field's DataType.
     * @param dataSetFieldId the stable wire identity of the field.
     * @param valueRank the value rank of the field.
     * @param arrayDimensions the array dimensions of the field, or {@code null}.
     */
    public Field {
      arrayDimensions = arrayDimensions == null ? null : arrayDimensions.clone();
    }

    @Override
    public UInteger @Nullable [] arrayDimensions() {
      return arrayDimensions == null ? null : arrayDimensions.clone();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Field that)) {
        return false;
      }
      return valueRank == that.valueRank
          && name.equals(that.name)
          && dataTypeId.equals(that.dataTypeId)
          && dataSetFieldId.equals(that.dataSetFieldId)
          && Arrays.equals(arrayDimensions, that.arrayDimensions);
    }

    @Override
    public int hashCode() {
      int result = Objects.hash(name, dataTypeId, dataSetFieldId, valueRank);
      result = 31 * result + Arrays.hashCode(arrayDimensions);
      return result;
    }

    @Override
    public String toString() {
      return "Field{name='%s', dataTypeId=%s, dataSetFieldId=%s, valueRank=%s, arrayDimensions=%s}"
          .formatted(name, dataTypeId, dataSetFieldId, valueRank, Arrays.toString(arrayDimensions));
    }
  }

  /** A builder of {@link DataSetMetaDataConfig} instances. */
  public static final class Builder {

    private final String name;
    private final List<Field> fields = new ArrayList<>();
    private @Nullable UUID dataSetClassId;
    private UInteger configurationVersionMajor = uint(1);
    private UInteger configurationVersionMinor = uint(1);

    private Builder(String name) {
      this.name = name;
    }

    /**
     * Add a field with a generated random DataSetFieldId, scalar value rank, and no array
     * dimensions.
     *
     * @param name the name of the field.
     * @param dataTypeId the NodeId of the field's DataType.
     * @return this {@link Builder}.
     */
    public Builder field(String name, NodeId dataTypeId) {
      return field(name, dataTypeId, UUID.randomUUID());
    }

    /**
     * Add a field with scalar value rank and no array dimensions.
     *
     * @param name the name of the field.
     * @param dataTypeId the NodeId of the field's DataType.
     * @param dataSetFieldId the stable wire identity of the field.
     * @return this {@link Builder}.
     */
    public Builder field(String name, NodeId dataTypeId, UUID dataSetFieldId) {
      return field(new Field(name, dataTypeId, dataSetFieldId, -1, null));
    }

    /**
     * Add a field with an explicit value rank and array dimensions.
     *
     * @param name the name of the field.
     * @param dataTypeId the NodeId of the field's DataType.
     * @param dataSetFieldId the stable wire identity of the field.
     * @param valueRank the value rank of the field.
     * @param arrayDimensions the array dimensions of the field, or {@code null}.
     * @return this {@link Builder}.
     */
    public Builder field(
        String name,
        NodeId dataTypeId,
        UUID dataSetFieldId,
        int valueRank,
        UInteger @Nullable [] arrayDimensions) {

      return field(new Field(name, dataTypeId, dataSetFieldId, valueRank, arrayDimensions));
    }

    /**
     * Add a field entry.
     *
     * @param field the field entry to add.
     * @return this {@link Builder}.
     */
    public Builder field(Field field) {
      fields.add(field);
      return this;
    }

    /**
     * Set the DataSetClassId identifying the dataset class this metadata describes.
     *
     * @param value the DataSetClassId.
     * @return this {@link Builder}.
     */
    public Builder dataSetClassId(UUID value) {
      this.dataSetClassId = value;
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
     * Build a {@link DataSetMetaDataConfig} from the configured values.
     *
     * @return a new {@link DataSetMetaDataConfig}.
     * @throws PubSubConfigValidationException if two fields share a name or a DataSetFieldId.
     */
    public DataSetMetaDataConfig build() {
      Set<String> fieldNames = new HashSet<>();
      Set<UUID> fieldIds = new HashSet<>();
      for (Field field : fields) {
        if (!fieldNames.add(field.name())) {
          throw new PubSubConfigValidationException(
              "DataSetMetaDataConfig '%s': duplicate field name '%s'"
                  .formatted(name, field.name()));
        }
        if (!fieldIds.add(field.dataSetFieldId())) {
          throw new PubSubConfigValidationException(
              "DataSetMetaDataConfig '%s': duplicate dataSetFieldId %s on field '%s'"
                  .formatted(name, field.dataSetFieldId(), field.name()));
        }
      }
      return new DataSetMetaDataConfig(this);
    }
  }
}
