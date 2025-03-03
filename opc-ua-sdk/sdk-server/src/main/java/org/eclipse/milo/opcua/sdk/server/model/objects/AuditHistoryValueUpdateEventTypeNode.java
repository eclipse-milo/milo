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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AuditHistoryValueUpdateEventTypeNode extends AuditHistoryUpdateEventTypeNode
    implements AuditHistoryValueUpdateEventType {
  public AuditHistoryValueUpdateEventTypeNode(
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

  public AuditHistoryValueUpdateEventTypeNode(
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
        getPropertyNode(AuditHistoryValueUpdateEventType.UPDATED_NODE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getUpdatedNode() {
    return getProperty(AuditHistoryValueUpdateEventType.UPDATED_NODE).orElse(null);
  }

  @Override
  public void setUpdatedNode(NodeId value) {
    setProperty(AuditHistoryValueUpdateEventType.UPDATED_NODE, value);
  }

  @Override
  public PropertyTypeNode getPerformInsertReplaceNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditHistoryValueUpdateEventType.PERFORM_INSERT_REPLACE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public PerformUpdateType getPerformInsertReplace() {
    return getProperty(AuditHistoryValueUpdateEventType.PERFORM_INSERT_REPLACE).orElse(null);
  }

  @Override
  public void setPerformInsertReplace(PerformUpdateType value) {
    setProperty(AuditHistoryValueUpdateEventType.PERFORM_INSERT_REPLACE, value);
  }

  @Override
  public PropertyTypeNode getNewValuesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditHistoryValueUpdateEventType.NEW_VALUES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DataValue[] getNewValues() {
    return getProperty(AuditHistoryValueUpdateEventType.NEW_VALUES).orElse(null);
  }

  @Override
  public void setNewValues(DataValue[] value) {
    setProperty(AuditHistoryValueUpdateEventType.NEW_VALUES, value);
  }

  @Override
  public PropertyTypeNode getOldValuesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditHistoryValueUpdateEventType.OLD_VALUES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DataValue[] getOldValues() {
    return getProperty(AuditHistoryValueUpdateEventType.OLD_VALUES).orElse(null);
  }

  @Override
  public void setOldValues(DataValue[] value) {
    setProperty(AuditHistoryValueUpdateEventType.OLD_VALUES, value);
  }
}
