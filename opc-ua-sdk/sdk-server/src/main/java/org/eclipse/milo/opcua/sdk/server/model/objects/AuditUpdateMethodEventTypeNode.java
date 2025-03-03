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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AuditUpdateMethodEventTypeNode extends AuditEventTypeNode
    implements AuditUpdateMethodEventType {
  public AuditUpdateMethodEventTypeNode(
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

  public AuditUpdateMethodEventTypeNode(
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
  public PropertyTypeNode getMethodIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AuditUpdateMethodEventType.METHOD_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getMethodId() {
    return getProperty(AuditUpdateMethodEventType.METHOD_ID).orElse(null);
  }

  @Override
  public void setMethodId(NodeId value) {
    setProperty(AuditUpdateMethodEventType.METHOD_ID, value);
  }

  @Override
  public PropertyTypeNode getStatusCodeIdNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditUpdateMethodEventType.STATUS_CODE_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public StatusCode getStatusCodeId() {
    return getProperty(AuditUpdateMethodEventType.STATUS_CODE_ID).orElse(null);
  }

  @Override
  public void setStatusCodeId(StatusCode value) {
    setProperty(AuditUpdateMethodEventType.STATUS_CODE_ID, value);
  }

  @Override
  public PropertyTypeNode getInputArgumentsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditUpdateMethodEventType.INPUT_ARGUMENTS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Object[] getInputArguments() {
    return getProperty(AuditUpdateMethodEventType.INPUT_ARGUMENTS).orElse(null);
  }

  @Override
  public void setInputArguments(Object[] value) {
    setProperty(AuditUpdateMethodEventType.INPUT_ARGUMENTS, value);
  }

  @Override
  public PropertyTypeNode getOutputArgumentsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditUpdateMethodEventType.OUTPUT_ARGUMENTS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Object[] getOutputArguments() {
    return getProperty(AuditUpdateMethodEventType.OUTPUT_ARGUMENTS).orElse(null);
  }

  @Override
  public void setOutputArguments(Object[] value) {
    setProperty(AuditUpdateMethodEventType.OUTPUT_ARGUMENTS, value);
  }
}
