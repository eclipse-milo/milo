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
    this.value = value;

    EnumDefinition definition = (EnumDefinition) dataType.getDataTypeDefinition();
    assert definition != null;

    EnumField[] fields = requireNonNullElse(definition.getFields(), new EnumField[0]);

    for (EnumField field : fields) {
      if (field.getValue() == value) {
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

  @Override
  public EnumDefinition getDataTypeDefinition() {
    return (EnumDefinition) requireNonNull(dataType.getDataTypeDefinition());
  }

  /**
   * Get the name associated with this enum value.
   *
   * @return the name associated with this enum value.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the display name associated with this enum value.
   *
   * @return the display name associated with this enum value.
   */
  public LocalizedText getDisplayName() {
    return displayName;
  }

  /**
   * Get the description associated with this enum value.
   *
   * @return the description associated with this enum value.
   */
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
        .add("dataType=" + dataType.getNodeId().toParseableString())
        .add("name=" + name)
        .add("value=" + value)
        .toString();
  }

  /**
   * Create a new DynamicEnumType using the given DataType and value.
   *
   * @param dataType the {@link DataType} of the enum.
   * @param value the value of the enum.
   * @return a new DynamicEnumType with the given DataType and value.
   */
  public static DynamicEnumType newInstance(DataType dataType, int value) {
    return new DynamicEnumType(dataType, value);
  }

  /**
   * Create a new instance "factory" that produces new DynamicEnumType instances of the given
   * DataType.
   *
   * @param dataType the {@link DataType} of the enum.
   * @return a new instance "factory".
   */
  public static Function<Integer, DynamicEnumType> newInstanceFactory(DataType dataType) {
    return value -> new DynamicEnumType(dataType, value);
  }
}
