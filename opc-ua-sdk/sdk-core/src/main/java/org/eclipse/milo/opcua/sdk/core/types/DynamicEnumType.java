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

import static java.util.Objects.requireNonNullElse;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;

public final class DynamicEnumType extends DynamicType implements UaEnumeratedType {

  private final String name;
  private final LocalizedText displayName;
  private final LocalizedText description;

  private final DataType dataType;
  private final int value;

  public DynamicEnumType(DataType dataType, int value) {
    this.dataType = dataType;

    EnumDefinition definition = (EnumDefinition) dataType.getDataTypeDefinition();
    assert definition != null;

    EnumField[] fields = requireNonNullElse(definition.getFields(), new EnumField[0]);

    for (EnumField field : fields) {
      if (field.getValue() == value) {
        this.value = field.getValue().intValue();

        this.name = field.getName() != null ? field.getName() : "";
        this.displayName =
            field.getDisplayName() != null ? field.getDisplayName() : LocalizedText.NULL_VALUE;
        this.description =
            field.getDescription() != null ? field.getDescription() : LocalizedText.NULL_VALUE;
        return;
      }
    }

    // if we reach this point the value doesn't match any of the fields
    throw new IllegalArgumentException("value: " + value);
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return dataType.getNodeId().expanded();
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public DataType getDataType() {
    return dataType;
  }

  public String getName() {
    return name;
  }

  public LocalizedText getDisplayName() {
    return displayName;
  }

  public LocalizedText getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    DynamicEnumType that = (DynamicEnumType) o;
    return value == that.value
        && Objects.equals(dataType.getNodeId(), that.dataType.getNodeId())
        && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataType.getNodeId(), name, value);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", DynamicEnumType.class.getSimpleName() + "[", "]")
        .add("name='" + name + "'")
        .add("value=" + value)
        .toString();
  }

  public static DynamicEnumType newInstance(DataType dataType, int value) {
    return new DynamicEnumType(dataType, value);
  }

  public static Function<Integer, DynamicEnumType> newInstanceFactory(DataType dataType) {
    return value -> new DynamicEnumType(dataType, value);
  }
}
