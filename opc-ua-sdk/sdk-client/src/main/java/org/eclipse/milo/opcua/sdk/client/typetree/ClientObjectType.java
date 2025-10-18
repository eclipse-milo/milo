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

import java.util.Objects;
import java.util.StringJoiner;
import org.eclipse.milo.opcua.sdk.core.typetree.ObjectType;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;

/**
 * Data object that holds details of an ObjectType:
 *
 * <ul>
 *   <li>Browse Name of the ObjectType Node
 *   <li>NodeId of the ObjectType Node
 *   <li>IsAbstract attribute value
 * </ul>
 */
class ClientObjectType implements ObjectType {

  private final QualifiedName browseName;
  private final NodeId nodeId;
  private final Boolean isAbstract;

  public ClientObjectType(QualifiedName browseName, NodeId nodeId, Boolean isAbstract) {
    this.browseName = browseName;
    this.nodeId = nodeId;
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
  public Boolean isAbstract() {
    return isAbstract;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ClientObjectType that = (ClientObjectType) o;
    return browseName.equals(that.browseName)
        && nodeId.equals(that.nodeId)
        && Objects.equals(isAbstract, that.isAbstract);
  }

  @Override
  public int hashCode() {
    return Objects.hash(browseName, nodeId, isAbstract);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ClientObjectType.class.getSimpleName() + "{", "}")
        .add("browseName=" + browseName)
        .add("nodeId=" + nodeId)
        .add("isAbstract=" + isAbstract)
        .toString();
  }
}
