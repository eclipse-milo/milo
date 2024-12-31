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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AuditClientUpdateMethodResultEventTypeNode extends AuditClientEventTypeNode
    implements AuditClientUpdateMethodResultEventType {
  public AuditClientUpdateMethodResultEventTypeNode(
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

  public AuditClientUpdateMethodResultEventTypeNode(
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
  public PropertyTypeNode getObjectIdNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditClientUpdateMethodResultEventType.OBJECT_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getObjectId() {
    return getProperty(AuditClientUpdateMethodResultEventType.OBJECT_ID).orElse(null);
  }

  @Override
  public void setObjectId(NodeId value) {
    setProperty(AuditClientUpdateMethodResultEventType.OBJECT_ID, value);
  }

  @Override
  public PropertyTypeNode getMethodIdNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditClientUpdateMethodResultEventType.METHOD_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getMethodId() {
    return getProperty(AuditClientUpdateMethodResultEventType.METHOD_ID).orElse(null);
  }

  @Override
  public void setMethodId(NodeId value) {
    setProperty(AuditClientUpdateMethodResultEventType.METHOD_ID, value);
  }

  @Override
  public PropertyTypeNode getStatusCodeIdNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditClientUpdateMethodResultEventType.STATUS_CODE_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public StatusCode getStatusCodeId() {
    return getProperty(AuditClientUpdateMethodResultEventType.STATUS_CODE_ID).orElse(null);
  }

  @Override
  public void setStatusCodeId(StatusCode value) {
    setProperty(AuditClientUpdateMethodResultEventType.STATUS_CODE_ID, value);
  }

  @Override
  public PropertyTypeNode getInputArgumentsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditClientUpdateMethodResultEventType.INPUT_ARGUMENTS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Argument[] getInputArguments() {
    return getProperty(AuditClientUpdateMethodResultEventType.INPUT_ARGUMENTS).orElse(null);
  }

  @Override
  public void setInputArguments(Argument[] value) {
    setProperty(AuditClientUpdateMethodResultEventType.INPUT_ARGUMENTS, value);
  }

  @Override
  public PropertyTypeNode getOutputArgumentsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditClientUpdateMethodResultEventType.OUTPUT_ARGUMENTS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Argument[] getOutputArguments() {
    return getProperty(AuditClientUpdateMethodResultEventType.OUTPUT_ARGUMENTS).orElse(null);
  }

  @Override
  public void setOutputArguments(Argument[] value) {
    setProperty(AuditClientUpdateMethodResultEventType.OUTPUT_ARGUMENTS, value);
  }
}
