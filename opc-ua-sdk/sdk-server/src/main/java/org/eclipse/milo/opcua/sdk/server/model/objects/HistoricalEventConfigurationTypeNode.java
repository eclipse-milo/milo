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
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;

public class HistoricalEventConfigurationTypeNode extends BaseObjectTypeNode
    implements HistoricalEventConfigurationType {
  public HistoricalEventConfigurationTypeNode(
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

  public HistoricalEventConfigurationTypeNode(
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
  public PropertyTypeNode getStartOfArchiveNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalEventConfigurationType.START_OF_ARCHIVE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getStartOfArchive() {
    return getProperty(HistoricalEventConfigurationType.START_OF_ARCHIVE).orElse(null);
  }

  @Override
  public void setStartOfArchive(DateTime value) {
    setProperty(HistoricalEventConfigurationType.START_OF_ARCHIVE, value);
  }

  @Override
  public PropertyTypeNode getStartOfOnlineArchiveNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalEventConfigurationType.START_OF_ONLINE_ARCHIVE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getStartOfOnlineArchive() {
    return getProperty(HistoricalEventConfigurationType.START_OF_ONLINE_ARCHIVE).orElse(null);
  }

  @Override
  public void setStartOfOnlineArchive(DateTime value) {
    setProperty(HistoricalEventConfigurationType.START_OF_ONLINE_ARCHIVE, value);
  }

  @Override
  public PropertyTypeNode getSortByEventFieldsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalEventConfigurationType.SORT_BY_EVENT_FIELDS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public SimpleAttributeOperand[] getSortByEventFields() {
    return getProperty(HistoricalEventConfigurationType.SORT_BY_EVENT_FIELDS).orElse(null);
  }

  @Override
  public void setSortByEventFields(SimpleAttributeOperand[] value) {
    setProperty(HistoricalEventConfigurationType.SORT_BY_EVENT_FIELDS, value);
  }

  @Override
  public FolderTypeNode getEventTypesNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "EventTypes");
    return (FolderTypeNode) component.orElse(null);
  }
}
