/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.dtd;

import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.opcua.binaryschema.StructuredType;

public class BsdDataType implements DataType {

  private final String name;
  private final NodeId nodeId;
  private final NodeId encodingId;
  private final @Nullable StructuredType structuredType;

  public BsdDataType(String name, NodeId nodeId, NodeId encodingId) {
    this(name, nodeId, encodingId, null);
  }

  public BsdDataType(
      String name, NodeId nodeId, NodeId encodingId, @Nullable StructuredType structuredType) {

    this.name = name;
    this.nodeId = nodeId;
    this.encodingId = encodingId;
    this.structuredType = structuredType;
  }

  @Override
  public QualifiedName getBrowseName() {
    return new QualifiedName(nodeId.getNamespaceIndex(), name);
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
    return false;
  }
}
