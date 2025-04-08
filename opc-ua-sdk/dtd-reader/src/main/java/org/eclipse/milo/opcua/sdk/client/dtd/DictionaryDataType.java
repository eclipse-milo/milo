/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.dtd;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.opcua.binaryschema.StructuredType;

public class DictionaryDataType implements DataType {

  private final String namespaceUri;
  private final QualifiedName browseName;
  private final NodeId nodeId;
  private final NodeId encodingId;
  private final boolean isAbstract;
  private final StructuredType structuredType;

  private DictionaryDataType(
      String namespaceUri,
      QualifiedName browseName,
      NodeId nodeId,
      NodeId encodingId,
      boolean isAbstract,
      StructuredType structuredType) {
    this.namespaceUri = namespaceUri;
    this.browseName = browseName;
    this.nodeId = nodeId;
    this.encodingId = encodingId;
    this.isAbstract = isAbstract;
    this.structuredType = structuredType;
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
    return encodingId;
  }

  @Override
  public @Nullable NodeId getXmlEncodingId() {
    return null;
  }

  @Override
  public @Nullable NodeId getJsonEncodingId() {
    return null;
  }

  @Override
  public @Nullable DataTypeDefinition getDataTypeDefinition() {
    return null;
  }

  @Override
  public Boolean isAbstract() {
    return isAbstract;
  }

  public StructuredType getStructuredType() {
    return structuredType;
  }

  static DictionaryDataType create(
      OpcUaClient client,
      String namespaceUri,
      BinaryDataTypeDictionaryReader.StructEncodingInfo structEncodingInfo,
      StructuredType structuredType) {

    UShort index = client.getNamespaceTable().getIndex(namespaceUri);
    QualifiedName browseName = new QualifiedName(index, structuredType.getName());

    return new DictionaryDataType(
        namespaceUri,
        browseName,
        structEncodingInfo.dataTypeId,
        structEncodingInfo.encodingId,
        false,
        structuredType);
  }
}
