/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class PubSubStatusEventTypeNode extends SystemEventTypeNode
    implements PubSubStatusEventType {
  public PubSubStatusEventTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions,
      UByte eventNotifier) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions,
        eventNotifier);
  }

  public PubSubStatusEventTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions);
  }

  @Override
  public PropertyTypeNode getConnectionIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PubSubStatusEventType.CONNECTION_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getConnectionId() {
    return getProperty(PubSubStatusEventType.CONNECTION_ID).orElse(null);
  }

  @Override
  public void setConnectionId(NodeId value) {
    setProperty(PubSubStatusEventType.CONNECTION_ID, value);
  }

  @Override
  public PropertyTypeNode getGroupIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PubSubStatusEventType.GROUP_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getGroupId() {
    return getProperty(PubSubStatusEventType.GROUP_ID).orElse(null);
  }

  @Override
  public void setGroupId(NodeId value) {
    setProperty(PubSubStatusEventType.GROUP_ID, value);
  }

  @Override
  public PropertyTypeNode getStateNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PubSubStatusEventType.STATE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public PubSubState getState() {
    return getProperty(PubSubStatusEventType.STATE).orElse(null);
  }

  @Override
  public void setState(PubSubState value) {
    setProperty(PubSubStatusEventType.STATE, value);
  }
}
