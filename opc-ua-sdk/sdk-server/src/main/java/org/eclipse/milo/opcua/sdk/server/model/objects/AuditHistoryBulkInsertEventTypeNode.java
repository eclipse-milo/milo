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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AuditHistoryBulkInsertEventTypeNode extends AuditEventTypeNode
    implements AuditHistoryBulkInsertEventType {
  public AuditHistoryBulkInsertEventTypeNode(
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

  public AuditHistoryBulkInsertEventTypeNode(
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
  public PropertyTypeNode getUpdatedNodeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditHistoryBulkInsertEventType.UPDATED_NODE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getUpdatedNode() {
    return getProperty(AuditHistoryBulkInsertEventType.UPDATED_NODE).orElse(null);
  }

  @Override
  public void setUpdatedNode(NodeId value) {
    setProperty(AuditHistoryBulkInsertEventType.UPDATED_NODE, value);
  }

  @Override
  public PropertyTypeNode getStartTimeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditHistoryBulkInsertEventType.START_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getStartTime() {
    return getProperty(AuditHistoryBulkInsertEventType.START_TIME).orElse(null);
  }

  @Override
  public void setStartTime(DateTime value) {
    setProperty(AuditHistoryBulkInsertEventType.START_TIME, value);
  }

  @Override
  public PropertyTypeNode getEndTimeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AuditHistoryBulkInsertEventType.END_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getEndTime() {
    return getProperty(AuditHistoryBulkInsertEventType.END_TIME).orElse(null);
  }

  @Override
  public void setEndTime(DateTime value) {
    setProperty(AuditHistoryBulkInsertEventType.END_TIME, value);
  }
}
