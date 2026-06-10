/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.structured.TraceContextDataType;

public class BaseLogEventTypeNode extends BaseEventTypeNode implements BaseLogEventType {
  public BaseLogEventTypeNode(
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

  public BaseLogEventTypeNode(
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
  public PropertyTypeNode getConditionClassIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseLogEventType.CONDITION_CLASS_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getConditionClassId() {
    return getProperty(BaseLogEventType.CONDITION_CLASS_ID).orElse(null);
  }

  @Override
  public void setConditionClassId(NodeId value) {
    setProperty(BaseLogEventType.CONDITION_CLASS_ID, value);
  }

  @Override
  public PropertyTypeNode getConditionClassNameNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseLogEventType.CONDITION_CLASS_NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LocalizedText getConditionClassName() {
    return getProperty(BaseLogEventType.CONDITION_CLASS_NAME).orElse(null);
  }

  @Override
  public void setConditionClassName(LocalizedText value) {
    setProperty(BaseLogEventType.CONDITION_CLASS_NAME, value);
  }

  @Override
  public PropertyTypeNode getErrorCodePropertyNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseLogEventType.ERROR_CODE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public StatusCode getErrorCode() {
    return getProperty(BaseLogEventType.ERROR_CODE).orElse(null);
  }

  @Override
  public void setErrorCode(StatusCode value) {
    setProperty(BaseLogEventType.ERROR_CODE, value);
  }

  @Override
  public PropertyTypeNode getErrorCodeNodeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseLogEventType.ERROR_CODE_NODE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getErrorCodeNode() {
    return getProperty(BaseLogEventType.ERROR_CODE_NODE).orElse(null);
  }

  @Override
  public void setErrorCodeNode(NodeId value) {
    setProperty(BaseLogEventType.ERROR_CODE_NODE, value);
  }

  @Override
  public PropertyTypeNode getTraceContextNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseLogEventType.TRACE_CONTEXT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public TraceContextDataType getTraceContext() {
    return getProperty(BaseLogEventType.TRACE_CONTEXT).orElse(null);
  }

  @Override
  public void setTraceContext(TraceContextDataType value) {
    setProperty(BaseLogEventType.TRACE_CONTEXT, value);
  }
}
