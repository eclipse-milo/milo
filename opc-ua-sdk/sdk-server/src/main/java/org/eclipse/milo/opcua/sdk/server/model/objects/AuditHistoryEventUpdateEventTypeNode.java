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
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AuditHistoryEventUpdateEventTypeNode extends AuditHistoryUpdateEventTypeNode
    implements AuditHistoryEventUpdateEventType {
  public AuditHistoryEventUpdateEventTypeNode(
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

  public AuditHistoryEventUpdateEventTypeNode(
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
        getPropertyNode(AuditHistoryEventUpdateEventType.UPDATED_NODE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getUpdatedNode() {
    return getProperty(AuditHistoryEventUpdateEventType.UPDATED_NODE).orElse(null);
  }

  @Override
  public void setUpdatedNode(NodeId value) {
    setProperty(AuditHistoryEventUpdateEventType.UPDATED_NODE, value);
  }

  @Override
  public PropertyTypeNode getPerformInsertReplaceNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditHistoryEventUpdateEventType.PERFORM_INSERT_REPLACE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public PerformUpdateType getPerformInsertReplace() {
    return getProperty(AuditHistoryEventUpdateEventType.PERFORM_INSERT_REPLACE).orElse(null);
  }

  @Override
  public void setPerformInsertReplace(PerformUpdateType value) {
    setProperty(AuditHistoryEventUpdateEventType.PERFORM_INSERT_REPLACE, value);
  }

  @Override
  public PropertyTypeNode getFilterNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AuditHistoryEventUpdateEventType.FILTER);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public EventFilter getFilter() {
    return getProperty(AuditHistoryEventUpdateEventType.FILTER).orElse(null);
  }

  @Override
  public void setFilter(EventFilter value) {
    setProperty(AuditHistoryEventUpdateEventType.FILTER, value);
  }

  @Override
  public PropertyTypeNode getNewValuesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditHistoryEventUpdateEventType.NEW_VALUES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public HistoryEventFieldList[] getNewValues() {
    return getProperty(AuditHistoryEventUpdateEventType.NEW_VALUES).orElse(null);
  }

  @Override
  public void setNewValues(HistoryEventFieldList[] value) {
    setProperty(AuditHistoryEventUpdateEventType.NEW_VALUES, value);
  }

  @Override
  public PropertyTypeNode getOldValuesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditHistoryEventUpdateEventType.OLD_VALUES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public HistoryEventFieldList[] getOldValues() {
    return getProperty(AuditHistoryEventUpdateEventType.OLD_VALUES).orElse(null);
  }

  @Override
  public void setOldValues(HistoryEventFieldList[] value) {
    setProperty(AuditHistoryEventUpdateEventType.OLD_VALUES, value);
  }
}
