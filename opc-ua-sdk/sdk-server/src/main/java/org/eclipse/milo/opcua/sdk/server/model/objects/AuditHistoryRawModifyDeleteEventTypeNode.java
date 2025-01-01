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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AuditHistoryRawModifyDeleteEventTypeNode extends AuditHistoryDeleteEventTypeNode
    implements AuditHistoryRawModifyDeleteEventType {
  public AuditHistoryRawModifyDeleteEventTypeNode(
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

  public AuditHistoryRawModifyDeleteEventTypeNode(
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
  public PropertyTypeNode getIsDeleteModifiedNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditHistoryRawModifyDeleteEventType.IS_DELETE_MODIFIED);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getIsDeleteModified() {
    return getProperty(AuditHistoryRawModifyDeleteEventType.IS_DELETE_MODIFIED).orElse(null);
  }

  @Override
  public void setIsDeleteModified(Boolean value) {
    setProperty(AuditHistoryRawModifyDeleteEventType.IS_DELETE_MODIFIED, value);
  }

  @Override
  public PropertyTypeNode getStartTimeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditHistoryRawModifyDeleteEventType.START_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getStartTime() {
    return getProperty(AuditHistoryRawModifyDeleteEventType.START_TIME).orElse(null);
  }

  @Override
  public void setStartTime(DateTime value) {
    setProperty(AuditHistoryRawModifyDeleteEventType.START_TIME, value);
  }

  @Override
  public PropertyTypeNode getEndTimeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditHistoryRawModifyDeleteEventType.END_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getEndTime() {
    return getProperty(AuditHistoryRawModifyDeleteEventType.END_TIME).orElse(null);
  }

  @Override
  public void setEndTime(DateTime value) {
    setProperty(AuditHistoryRawModifyDeleteEventType.END_TIME, value);
  }

  @Override
  public PropertyTypeNode getOldValuesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditHistoryRawModifyDeleteEventType.OLD_VALUES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DataValue[] getOldValues() {
    return getProperty(AuditHistoryRawModifyDeleteEventType.OLD_VALUES).orElse(null);
  }

  @Override
  public void setOldValues(DataValue[] value) {
    setProperty(AuditHistoryRawModifyDeleteEventType.OLD_VALUES, value);
  }
}
