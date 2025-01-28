/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.types;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.jspecify.annotations.Nullable;

public final class DynamicUnionType extends DynamicType implements UaStructuredType {

  private final DataType dataType;
  private final @Nullable UnionValue value;

  public DynamicUnionType(DataType dataType) {
    this(dataType, null);
  }

  public DynamicUnionType(DataType dataType, @Nullable UnionValue value) {
    this.dataType = dataType;
    this.value = value;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return dataType.getNodeId().expanded();
  }

  @Override
  public ExpandedNodeId getBinaryEncodingId() {
    NodeId binaryEncodingId = dataType.getBinaryEncodingId();
    return binaryEncodingId != null ? binaryEncodingId.expanded() : ExpandedNodeId.NULL_VALUE;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    NodeId xmlEncodingId = dataType.getXmlEncodingId();
    return xmlEncodingId != null ? xmlEncodingId.expanded() : ExpandedNodeId.NULL_VALUE;
  }

  @Override
  public ExpandedNodeId getJsonEncodingId() {
    NodeId jsonEncodingId = dataType.getJsonEncodingId();
    return jsonEncodingId != null ? jsonEncodingId.expanded() : ExpandedNodeId.NULL_VALUE;
  }

  @Override
  public DataType getDataType() {
    return dataType;
  }

  @Override
  public StructureDefinition getDataTypeDefinition() {
    return (StructureDefinition) requireNonNull(dataType.getDataTypeDefinition());
  }

  /**
   * Get the value of this union.
   *
   * @return the value of this union, or {@link Optional#empty()} if the value is {@code null}.
   */
  public Optional<UnionValue> getValue() {
    return Optional.ofNullable(value);
  }

  /**
   * Check if this union contains a value for the given field name.
   *
   * @param fieldName the field name to check.
   * @return {@code true} if the value is not {@code null} and its field name matches the given
   *     field name, {@code false} otherwise.
   */
  public boolean is(String fieldName) {
    return value != null && value.fieldName().equals(fieldName);
  }

  /**
   * @return {@code true} if the value is {@code null}, {@code false} otherwise.
   */
  public boolean isNull() {
    return value == null;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    DynamicUnionType that = (DynamicUnionType) o;
    return Objects.equals(dataType.getNodeId(), that.dataType.getNodeId())
        && Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataType, value);
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DynamicUnionType.class.getSimpleName() + "[", "]");
    joiner.add("dataType=" + dataType.getNodeId().toParseableString());
    if (value != null) {
      joiner.add("%s=%s".formatted(value.fieldName(), value.fieldValue()));
    } else {
      joiner.add("null");
    }
    return joiner.toString();
  }

  /**
   * Pair of a union's field name and its value.
   *
   * @param fieldName the field name.
   * @param fieldValue the field's value.
   */
  public record UnionValue(String fieldName, Object fieldValue) {
    public UnionValue {
      requireNonNull(fieldName);
      requireNonNull(fieldValue);
    }
  }

  /**
   * Create a new DynamicUnionType using the given DataType and value.
   *
   * @param dataType the {@link DataType} of the union.
   * @param value the {@link UnionValue} of the union.
   * @return a new DynamicUnionType with the given DataType and value.
   */
  public static DynamicUnionType newInstance(DataType dataType, @Nullable UnionValue value) {
    return new DynamicUnionType(dataType, value);
  }

  /**
   * Create a new instance "factory" that produces new DynamicUnionType instances of the given
   * DataType.
   *
   * @param dataType the {@link DataType} of the union.
   * @return a new instance "factory".
   */
  public static Function<UnionValue, DynamicUnionType> newInstanceFactory(DataType dataType) {
    return value -> new DynamicUnionType(dataType, value);
  }
}
