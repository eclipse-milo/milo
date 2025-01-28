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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;

public final class DynamicStructType extends DynamicType implements UaStructuredType {

  private final DataType dataType;
  private final LinkedHashMap<String, Object> members;

  public DynamicStructType(DataType dataType, LinkedHashMap<String, Object> members) {
    this.dataType = dataType;
    this.members = members;
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
   * Get the members of this struct.
   *
   * <p>The members are a map of member names to their values.
   *
   * @return the members of this struct.
   */
  public LinkedHashMap<String, Object> getMembers() {
    return members;
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
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    DynamicStructType that = (DynamicStructType) o;
    return Objects.equals(dataType.getNodeId(), that.dataType.getNodeId())
        && Objects.equals(members, that.members);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataType.getNodeId(), members);
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DynamicStructType.class.getSimpleName() + "[", "]");
    joiner.add("dataType=" + dataType.getNodeId().toParseableString());
    joiner.add("members=" + joinMembers(members));
    return joiner.toString();
  }

  private static String joinMembers(LinkedHashMap<String, Object> members) {
    return members.entrySet().stream()
        .map(
            e -> {
              String k = e.getKey();
              Object v = e.getValue();
              if (v instanceof Object[]) {
                return String.format("%s=%s", k, Arrays.toString((Object[]) v));
              } else {
                return String.format("%s=%s", k, v);
              }
            })
        .collect(Collectors.joining(", "));
  }

  /**
   * Create a new DynamicStructType using the given DataType.
   *
   * <p>The members of the struct are empty.
   *
   * @param dataType the {@link DataType} of the struct.
   * @return a new DynamicStructType with the given DataType.
   */
  public static DynamicStructType newInstance(DataType dataType) {
    return new DynamicStructType(dataType, new LinkedHashMap<>());
  }

  /**
   * Create a new instance "factory" that produces new DynamicStructType instances of the given
   * DataType.
   *
   * @param dataType the {@link DataType} of the struct.
   * @return a new instance "factory".
   */
  public static Supplier<DynamicStructType> newInstanceFactory(DataType dataType) {
    return () -> newInstance(dataType);
  }
}
