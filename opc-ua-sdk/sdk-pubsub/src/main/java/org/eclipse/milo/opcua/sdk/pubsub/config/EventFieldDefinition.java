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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.Nullable;

/**
 * Definition of a single event field in a {@link PublishedEventsConfig}.
 *
 * <p>Field order in the dataset defines wire order. Each field carries a stable {@code
 * DataSetFieldId} ({@link UUID}); if none is configured one is generated when {@code build()} is
 * called. The {@code selectedField} operand selects the event field to publish; NodeIds it contains
 * are carried verbatim and are not remapped across namespace tables. The {@code promoted} flag maps
 * to the {@code FieldMetaData} field flags. The {@code dataType}, {@code valueRank}, and {@code
 * arrayDimensions} values exist for metadata fidelity and default to {@code BaseDataType}, scalar
 * ({@code -1}), and no dimensions.
 */
public final class EventFieldDefinition {

  private final String name;
  private final SimpleAttributeOperand selectedField;
  private final NodeId dataType;
  private final UUID dataSetFieldId;
  private final boolean promoted;
  private final int valueRank;
  private final UInteger @Nullable [] arrayDimensions;
  private final Map<QualifiedName, Variant> properties;

  private EventFieldDefinition(
      Builder builder, SimpleAttributeOperand selectedField, UUID dataSetFieldId) {

    this.name = builder.name;
    this.selectedField = selectedField;
    this.dataType = builder.dataType;
    this.dataSetFieldId = dataSetFieldId;
    this.promoted = builder.promoted;
    this.valueRank = builder.valueRank;
    this.arrayDimensions = builder.arrayDimensions == null ? null : builder.arrayDimensions.clone();
    this.properties = Collections.unmodifiableMap(new LinkedHashMap<>(builder.properties));
  }

  /**
   * Get the name of this field.
   *
   * @return the name of this field.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the operand selecting the event field to publish.
   *
   * @return the {@link SimpleAttributeOperand} selecting the event field; NodeIds it contains are
   *     carried verbatim and are not remapped across namespace tables.
   */
  public SimpleAttributeOperand getSelectedField() {
    return selectedField;
  }

  /**
   * Get the NodeId of this field's DataType.
   *
   * @return the DataType NodeId; defaults to {@code BaseDataType}.
   */
  public NodeId getDataType() {
    return dataType;
  }

  /**
   * Get the stable wire identity of this field.
   *
   * @return the DataSetFieldId.
   */
  public UUID getDataSetFieldId() {
    return dataSetFieldId;
  }

  /**
   * Get whether this field is promoted into the NetworkMessage header.
   *
   * @return {@code true} if this field is promoted.
   */
  public boolean isPromoted() {
    return promoted;
  }

  /**
   * Get the value rank of this field.
   *
   * @return the value rank; defaults to {@code -1} (scalar).
   */
  public int getValueRank() {
    return valueRank;
  }

  /**
   * Get the array dimensions of this field.
   *
   * @return the array dimensions, or {@code null} if not applicable.
   */
  public UInteger @Nullable [] getArrayDimensions() {
    return arrayDimensions == null ? null : arrayDimensions.clone();
  }

  /**
   * Get the additional properties of this field, in insertion order.
   *
   * @return an unmodifiable view of the additional properties.
   */
  public Map<QualifiedName, Variant> getProperties() {
    return properties;
  }

  /**
   * Create a {@link Builder} pre-populated with the values of this definition.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder(name);
    builder.selectedField = selectedField;
    builder.dataType = dataType;
    builder.dataSetFieldId = dataSetFieldId;
    builder.promoted = promoted;
    builder.valueRank = valueRank;
    builder.arrayDimensions = arrayDimensions == null ? null : arrayDimensions.clone();
    builder.properties.putAll(properties);
    return builder;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof EventFieldDefinition that)) {
      return false;
    }
    return promoted == that.promoted
        && valueRank == that.valueRank
        && name.equals(that.name)
        && selectedField.equals(that.selectedField)
        && dataType.equals(that.dataType)
        && dataSetFieldId.equals(that.dataSetFieldId)
        && Arrays.equals(arrayDimensions, that.arrayDimensions)
        && properties.equals(that.properties);
  }

  @Override
  public int hashCode() {
    int result =
        Objects.hash(
            name, selectedField, dataType, dataSetFieldId, promoted, valueRank, properties);
    result = 31 * result + Arrays.hashCode(arrayDimensions);
    return result;
  }

  @Override
  public String toString() {
    return "EventFieldDefinition{name='%s', selectedField=%s, dataType=%s, dataSetFieldId=%s, promoted=%s, valueRank=%s, arrayDimensions=%s, properties=%s}"
        .formatted(
            name,
            selectedField,
            dataType,
            dataSetFieldId,
            promoted,
            valueRank,
            Arrays.toString(arrayDimensions),
            properties);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @param name the name of the field.
   * @return a new {@link Builder}.
   */
  public static Builder builder(String name) {
    return new Builder(name);
  }

  /** A builder of {@link EventFieldDefinition} instances. */
  public static final class Builder {

    private final String name;
    private @Nullable SimpleAttributeOperand selectedField;
    private NodeId dataType = NodeIds.BaseDataType;
    private @Nullable UUID dataSetFieldId;
    private boolean promoted = false;
    private int valueRank = -1;
    private UInteger @Nullable [] arrayDimensions;
    private final Map<QualifiedName, Variant> properties = new LinkedHashMap<>();

    private Builder(String name) {
      this.name = name;
    }

    /**
     * Set the operand selecting the event field to publish.
     *
     * @param selectedField the {@link SimpleAttributeOperand} selecting the event field; NodeIds it
     *     contains are carried verbatim and are not remapped across namespace tables.
     * @return this {@link Builder}.
     */
    public Builder selectedField(SimpleAttributeOperand selectedField) {
      this.selectedField = selectedField;
      return this;
    }

    /**
     * Set the NodeId of the field's DataType.
     *
     * @param dataTypeId the DataType NodeId.
     * @return this {@link Builder}.
     */
    public Builder dataType(NodeId dataTypeId) {
      this.dataType = dataTypeId;
      return this;
    }

    /**
     * Set the stable wire identity of the field.
     *
     * <p>If not set, a random {@link UUID} is generated when {@link #build()} is called.
     *
     * @param value the DataSetFieldId.
     * @return this {@link Builder}.
     */
    public Builder dataSetFieldId(UUID value) {
      this.dataSetFieldId = value;
      return this;
    }

    /**
     * Set whether the field is promoted into the NetworkMessage header.
     *
     * @param promoted {@code true} if the field is promoted.
     * @return this {@link Builder}.
     */
    public Builder promoted(boolean promoted) {
      this.promoted = promoted;
      return this;
    }

    /**
     * Set the value rank of the field.
     *
     * @param valueRank the value rank; {@code -1} (scalar) by default.
     * @return this {@link Builder}.
     */
    public Builder valueRank(int valueRank) {
      this.valueRank = valueRank;
      return this;
    }

    /**
     * Set the array dimensions of the field.
     *
     * @param arrayDimensions the array dimensions, or {@code null} if not applicable.
     * @return this {@link Builder}.
     */
    public Builder arrayDimensions(UInteger @Nullable [] arrayDimensions) {
      this.arrayDimensions = arrayDimensions == null ? null : arrayDimensions.clone();
      return this;
    }

    /**
     * Add an additional property of the field.
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
     * Build an {@link EventFieldDefinition} from the configured values.
     *
     * @return a new {@link EventFieldDefinition}.
     * @throws PubSubConfigValidationException if the field name is empty or no selected field is
     *     configured.
     */
    public EventFieldDefinition build() {
      if (name.isEmpty()) {
        throw new PubSubConfigValidationException("EventFieldDefinition: name must not be empty");
      }
      if (selectedField == null) {
        throw new PubSubConfigValidationException(
            "EventFieldDefinition '%s': selectedField is required".formatted(name));
      }

      UUID dataSetFieldId = this.dataSetFieldId != null ? this.dataSetFieldId : UUID.randomUUID();

      return new EventFieldDefinition(this, selectedField, dataSetFieldId);
    }
  }
}
