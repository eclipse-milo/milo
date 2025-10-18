/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.typetree;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;
import org.eclipse.milo.opcua.sdk.core.typetree.VariableType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * Data object that holds details of a VariableType:
 *
 * <ul>
 *   <li>Browse Name of the VariableType Node
 *   <li>NodeId of the VariableType Node
 *   <li>Value attribute
 *   <li>DataType attribute
 *   <li>ValueRank attribute
 *   <li>ArrayDimensions attribute
 *   <li>IsAbstract attribute value
 * </ul>
 */
class ClientVariableType implements VariableType {

  private final QualifiedName browseName;
  private final NodeId nodeId;
  private final DataValue value;
  private final NodeId dataType;
  private final Integer valueRank;
  private final UInteger[] arrayDimensions;
  private final Boolean isAbstract;

  public ClientVariableType(
      QualifiedName browseName,
      NodeId nodeId,
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
      Boolean isAbstract) {

    this.browseName = browseName;
    this.nodeId = nodeId;
    this.value = value;
    this.dataType = dataType;
    this.valueRank = valueRank;
    this.arrayDimensions = arrayDimensions;
    this.isAbstract = isAbstract;
  }

  @Override
  public QualifiedName getBrowseName() {
    return browseName;
  }

  @Override
  public NodeId getNodeId() {
    return nodeId;
  }

  @Override
  public DataValue getValue() {
    return value;
  }

  @Override
  public NodeId getDataType() {
    return dataType;
  }

  @Override
  public Integer getValueRank() {
    return valueRank;
  }

  @Override
  public UInteger[] getArrayDimensions() {
    return arrayDimensions;
  }

  @Override
  public Boolean isAbstract() {
    return isAbstract;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ClientVariableType that = (ClientVariableType) o;
    return browseName.equals(that.browseName)
        && nodeId.equals(that.nodeId)
        && Objects.equals(value, that.value)
        && Objects.equals(dataType, that.dataType)
        && Objects.equals(valueRank, that.valueRank)
        && Arrays.equals(arrayDimensions, that.arrayDimensions)
        && Objects.equals(isAbstract, that.isAbstract);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(browseName, nodeId, value, dataType, valueRank, isAbstract);
    result = 31 * result + Arrays.hashCode(arrayDimensions);
    return result;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ClientVariableType.class.getSimpleName() + "{", "}")
        .add("browseName=" + browseName)
        .add("nodeId=" + nodeId)
        .add("value=" + value)
        .add("dataType=" + dataType)
        .add("valueRank=" + valueRank)
        .add("arrayDimensions=" + Arrays.toString(arrayDimensions))
        .add("isAbstract=" + isAbstract)
        .toString();
  }
}
