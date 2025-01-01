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
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
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

public class HistoryServerCapabilitiesTypeNode extends BaseObjectTypeNode
    implements HistoryServerCapabilitiesType {
  public HistoryServerCapabilitiesTypeNode(
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

  public HistoryServerCapabilitiesTypeNode(
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
  public PropertyTypeNode getAccessHistoryDataCapabilityNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoryServerCapabilitiesType.ACCESS_HISTORY_DATA_CAPABILITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getAccessHistoryDataCapability() {
    return getProperty(HistoryServerCapabilitiesType.ACCESS_HISTORY_DATA_CAPABILITY).orElse(null);
  }

  @Override
  public void setAccessHistoryDataCapability(Boolean value) {
    setProperty(HistoryServerCapabilitiesType.ACCESS_HISTORY_DATA_CAPABILITY, value);
  }

  @Override
  public PropertyTypeNode getAccessHistoryEventsCapabilityNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoryServerCapabilitiesType.ACCESS_HISTORY_EVENTS_CAPABILITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getAccessHistoryEventsCapability() {
    return getProperty(HistoryServerCapabilitiesType.ACCESS_HISTORY_EVENTS_CAPABILITY).orElse(null);
  }

  @Override
  public void setAccessHistoryEventsCapability(Boolean value) {
    setProperty(HistoryServerCapabilitiesType.ACCESS_HISTORY_EVENTS_CAPABILITY, value);
  }

  @Override
  public PropertyTypeNode getMaxReturnDataValuesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoryServerCapabilitiesType.MAX_RETURN_DATA_VALUES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxReturnDataValues() {
    return getProperty(HistoryServerCapabilitiesType.MAX_RETURN_DATA_VALUES).orElse(null);
  }

  @Override
  public void setMaxReturnDataValues(UInteger value) {
    setProperty(HistoryServerCapabilitiesType.MAX_RETURN_DATA_VALUES, value);
  }

  @Override
  public PropertyTypeNode getMaxReturnEventValuesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoryServerCapabilitiesType.MAX_RETURN_EVENT_VALUES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxReturnEventValues() {
    return getProperty(HistoryServerCapabilitiesType.MAX_RETURN_EVENT_VALUES).orElse(null);
  }

  @Override
  public void setMaxReturnEventValues(UInteger value) {
    setProperty(HistoryServerCapabilitiesType.MAX_RETURN_EVENT_VALUES, value);
  }

  @Override
  public PropertyTypeNode getInsertDataCapabilityNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoryServerCapabilitiesType.INSERT_DATA_CAPABILITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getInsertDataCapability() {
    return getProperty(HistoryServerCapabilitiesType.INSERT_DATA_CAPABILITY).orElse(null);
  }

  @Override
  public void setInsertDataCapability(Boolean value) {
    setProperty(HistoryServerCapabilitiesType.INSERT_DATA_CAPABILITY, value);
  }

  @Override
  public PropertyTypeNode getReplaceDataCapabilityNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoryServerCapabilitiesType.REPLACE_DATA_CAPABILITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getReplaceDataCapability() {
    return getProperty(HistoryServerCapabilitiesType.REPLACE_DATA_CAPABILITY).orElse(null);
  }

  @Override
  public void setReplaceDataCapability(Boolean value) {
    setProperty(HistoryServerCapabilitiesType.REPLACE_DATA_CAPABILITY, value);
  }

  @Override
  public PropertyTypeNode getUpdateDataCapabilityNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoryServerCapabilitiesType.UPDATE_DATA_CAPABILITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getUpdateDataCapability() {
    return getProperty(HistoryServerCapabilitiesType.UPDATE_DATA_CAPABILITY).orElse(null);
  }

  @Override
  public void setUpdateDataCapability(Boolean value) {
    setProperty(HistoryServerCapabilitiesType.UPDATE_DATA_CAPABILITY, value);
  }

  @Override
  public PropertyTypeNode getDeleteRawCapabilityNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoryServerCapabilitiesType.DELETE_RAW_CAPABILITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getDeleteRawCapability() {
    return getProperty(HistoryServerCapabilitiesType.DELETE_RAW_CAPABILITY).orElse(null);
  }

  @Override
  public void setDeleteRawCapability(Boolean value) {
    setProperty(HistoryServerCapabilitiesType.DELETE_RAW_CAPABILITY, value);
  }

  @Override
  public PropertyTypeNode getDeleteAtTimeCapabilityNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoryServerCapabilitiesType.DELETE_AT_TIME_CAPABILITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getDeleteAtTimeCapability() {
    return getProperty(HistoryServerCapabilitiesType.DELETE_AT_TIME_CAPABILITY).orElse(null);
  }

  @Override
  public void setDeleteAtTimeCapability(Boolean value) {
    setProperty(HistoryServerCapabilitiesType.DELETE_AT_TIME_CAPABILITY, value);
  }

  @Override
  public PropertyTypeNode getInsertEventCapabilityNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoryServerCapabilitiesType.INSERT_EVENT_CAPABILITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getInsertEventCapability() {
    return getProperty(HistoryServerCapabilitiesType.INSERT_EVENT_CAPABILITY).orElse(null);
  }

  @Override
  public void setInsertEventCapability(Boolean value) {
    setProperty(HistoryServerCapabilitiesType.INSERT_EVENT_CAPABILITY, value);
  }

  @Override
  public PropertyTypeNode getReplaceEventCapabilityNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoryServerCapabilitiesType.REPLACE_EVENT_CAPABILITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getReplaceEventCapability() {
    return getProperty(HistoryServerCapabilitiesType.REPLACE_EVENT_CAPABILITY).orElse(null);
  }

  @Override
  public void setReplaceEventCapability(Boolean value) {
    setProperty(HistoryServerCapabilitiesType.REPLACE_EVENT_CAPABILITY, value);
  }

  @Override
  public PropertyTypeNode getUpdateEventCapabilityNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoryServerCapabilitiesType.UPDATE_EVENT_CAPABILITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getUpdateEventCapability() {
    return getProperty(HistoryServerCapabilitiesType.UPDATE_EVENT_CAPABILITY).orElse(null);
  }

  @Override
  public void setUpdateEventCapability(Boolean value) {
    setProperty(HistoryServerCapabilitiesType.UPDATE_EVENT_CAPABILITY, value);
  }

  @Override
  public PropertyTypeNode getDeleteEventCapabilityNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoryServerCapabilitiesType.DELETE_EVENT_CAPABILITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getDeleteEventCapability() {
    return getProperty(HistoryServerCapabilitiesType.DELETE_EVENT_CAPABILITY).orElse(null);
  }

  @Override
  public void setDeleteEventCapability(Boolean value) {
    setProperty(HistoryServerCapabilitiesType.DELETE_EVENT_CAPABILITY, value);
  }

  @Override
  public PropertyTypeNode getInsertAnnotationCapabilityNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoryServerCapabilitiesType.INSERT_ANNOTATION_CAPABILITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getInsertAnnotationCapability() {
    return getProperty(HistoryServerCapabilitiesType.INSERT_ANNOTATION_CAPABILITY).orElse(null);
  }

  @Override
  public void setInsertAnnotationCapability(Boolean value) {
    setProperty(HistoryServerCapabilitiesType.INSERT_ANNOTATION_CAPABILITY, value);
  }

  @Override
  public PropertyTypeNode getServerTimestampSupportedNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoryServerCapabilitiesType.SERVER_TIMESTAMP_SUPPORTED);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getServerTimestampSupported() {
    return getProperty(HistoryServerCapabilitiesType.SERVER_TIMESTAMP_SUPPORTED).orElse(null);
  }

  @Override
  public void setServerTimestampSupported(Boolean value) {
    setProperty(HistoryServerCapabilitiesType.SERVER_TIMESTAMP_SUPPORTED, value);
  }

  @Override
  public FolderTypeNode getAggregateFunctionsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "AggregateFunctions");
    return (FolderTypeNode) component.orElse(null);
  }
}
