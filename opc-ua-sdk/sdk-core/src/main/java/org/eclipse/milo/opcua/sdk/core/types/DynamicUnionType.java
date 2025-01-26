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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.StringJoiner;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

public final class DynamicUnionType extends DynamicType implements UaStructuredType {

  private final DataType dataType;
  private final LinkedHashMap<String, Object> members;

  public DynamicUnionType(DataType dataType) {
    this(dataType, new LinkedHashMap<>());
  }

  public DynamicUnionType(DataType dataType, LinkedHashMap<String, Object> members) {
    this.dataType = dataType;
    this.members = members;
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

  public LinkedHashMap<String, Object> getMembers() {
    return members;
  }

  public @Nullable String getFieldName() {
    return getMembers().keySet().stream().findFirst().orElse(null);
  }

  public @Nullable Object getFieldValue() {
    return getMembers().values().stream().findFirst().orElse(null);
  }

  public boolean isNull() {
    return getMembers().isEmpty();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    DynamicUnionType that = (DynamicUnionType) o;
    return Objects.equals(dataType.getNodeId(), that.dataType.getNodeId())
        && Objects.equals(members, that.members);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataType.getNodeId(), members);
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DynamicUnionType.class.getSimpleName() + "[", "]");
    joiner.add("dataType=" + dataType.getNodeId());
    joiner.add(joinMembers(members));
    return joiner.toString();
  }

  private static String joinMembers(LinkedHashMap<String, Object> members) {
    return members.entrySet().stream()
        .findFirst()
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
        .orElse("null");
  }

  public static DynamicUnionType ofNull(DataType dataType) {
    return new DynamicUnionType(dataType);
  }

  public static DynamicUnionType of(DataType dataType, String fieldName, Object fieldValue) {
    LinkedHashMap<String, Object> members = new LinkedHashMap<>();
    members.put(fieldName, fieldValue);
    return new DynamicUnionType(dataType, members);
  }
}
