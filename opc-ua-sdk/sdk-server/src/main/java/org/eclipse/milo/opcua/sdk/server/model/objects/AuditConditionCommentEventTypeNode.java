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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AuditConditionCommentEventTypeNode extends AuditConditionEventTypeNode
    implements AuditConditionCommentEventType {
  public AuditConditionCommentEventTypeNode(
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

  public AuditConditionCommentEventTypeNode(
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
  public PropertyTypeNode getConditionEventIdNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditConditionCommentEventType.CONDITION_EVENT_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public ByteString getConditionEventId() {
    return getProperty(AuditConditionCommentEventType.CONDITION_EVENT_ID).orElse(null);
  }

  @Override
  public void setConditionEventId(ByteString value) {
    setProperty(AuditConditionCommentEventType.CONDITION_EVENT_ID, value);
  }

  @Override
  public PropertyTypeNode getCommentNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AuditConditionCommentEventType.COMMENT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LocalizedText getComment() {
    return getProperty(AuditConditionCommentEventType.COMMENT).orElse(null);
  }

  @Override
  public void setComment(LocalizedText value) {
    setProperty(AuditConditionCommentEventType.COMMENT, value);
  }
}
