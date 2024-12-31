/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class PubSubTransportLimitsExceedEventTypeNode extends PubSubStatusEventTypeNode
    implements PubSubTransportLimitsExceedEventType {
  public PubSubTransportLimitsExceedEventTypeNode(
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

  public PubSubTransportLimitsExceedEventTypeNode(
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
  public PropertyTypeNode getActualNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PubSubTransportLimitsExceedEventType.ACTUAL);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getActual() {
    return getProperty(PubSubTransportLimitsExceedEventType.ACTUAL).orElse(null);
  }

  @Override
  public void setActual(UInteger value) {
    setProperty(PubSubTransportLimitsExceedEventType.ACTUAL, value);
  }

  @Override
  public PropertyTypeNode getMaximumNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PubSubTransportLimitsExceedEventType.MAXIMUM);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaximum() {
    return getProperty(PubSubTransportLimitsExceedEventType.MAXIMUM).orElse(null);
  }

  @Override
  public void setMaximum(UInteger value) {
    setProperty(PubSubTransportLimitsExceedEventType.MAXIMUM, value);
  }
}
