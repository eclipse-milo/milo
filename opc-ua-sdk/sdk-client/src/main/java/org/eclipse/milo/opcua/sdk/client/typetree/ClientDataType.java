/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.typetree;

import java.util.Objects;
import java.util.StringJoiner;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.jspecify.annotations.Nullable;

/**
 * Data object that holds details of a DataType:
 *
 * <ul>
 *   <li>Browse Name of the DataType Node
 *   <li>NodeId of the DataType Node
 *   <li>NodeId of the Binary Encoding Node
 *   <li>NodeId of the XML Encoding Node
 *   <li>NodeId of the JSON Encoding Node
 *   <li>{@link DataTypeDefinition} attribute value>
 * </ul>
 */
class ClientDataType implements DataType {

  private final QualifiedName browseName;
  private final NodeId nodeId;
  private final NodeId binaryEncodingId;
  private final NodeId xmlEncodingId;
  private final NodeId jsonEncodingId;
  private final DataTypeDefinition dataTypeDefinition;
  private final Boolean isAbstract;

  public ClientDataType(
      QualifiedName browseName,
      NodeId nodeId,
      NodeId binaryEncodingId,
      NodeId xmlEncodingId,
      NodeId jsonEncodingId,
      DataTypeDefinition dataTypeDefinition,
      Boolean isAbstract) {

    this.browseName = browseName;
    this.nodeId = nodeId;
    this.binaryEncodingId = binaryEncodingId;
    this.xmlEncodingId = xmlEncodingId;
    this.jsonEncodingId = jsonEncodingId;
    this.dataTypeDefinition = dataTypeDefinition;
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
  public @Nullable NodeId getBinaryEncodingId() {
    return binaryEncodingId;
  }

  @Override
  public @Nullable NodeId getXmlEncodingId() {
    return xmlEncodingId;
  }

  @Override
  public @Nullable NodeId getJsonEncodingId() {
    return jsonEncodingId;
  }

  @Override
  public @Nullable DataTypeDefinition getDataTypeDefinition() {
    return dataTypeDefinition;
  }

  @Override
  public Boolean isAbstract() {
    return isAbstract;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ClientDataType dataType = (ClientDataType) o;
    return browseName.equals(dataType.browseName)
        && nodeId.equals(dataType.nodeId)
        && Objects.equals(binaryEncodingId, dataType.binaryEncodingId)
        && Objects.equals(xmlEncodingId, dataType.xmlEncodingId)
        && Objects.equals(jsonEncodingId, dataType.jsonEncodingId)
        && Objects.equals(dataTypeDefinition, dataType.dataTypeDefinition)
        && Objects.equals(isAbstract, dataType.isAbstract);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        browseName,
        nodeId,
        binaryEncodingId,
        xmlEncodingId,
        jsonEncodingId,
        dataTypeDefinition,
        isAbstract);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ClientDataType.class.getSimpleName() + "{", "}")
        .add("browseName=" + browseName)
        .add("nodeId=" + nodeId)
        .add("binaryEncodingId=" + binaryEncodingId)
        .add("xmlEncodingId=" + xmlEncodingId)
        .add("jsonEncodingId=" + jsonEncodingId)
        .add("dataTypeDefinition=" + dataTypeDefinition)
        .add("isAbstract=" + isAbstract)
        .toString();
  }
}
