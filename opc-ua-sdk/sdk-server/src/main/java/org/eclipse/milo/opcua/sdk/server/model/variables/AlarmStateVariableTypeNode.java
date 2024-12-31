/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AlarmStateVariableTypeNode extends BaseDataVariableTypeNode
    implements AlarmStateVariableType {
  public AlarmStateVariableTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      boolean historizing,
      AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  public AlarmStateVariableTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions);
  }

  @Override
  public PropertyTypeNode getHighestActiveSeverityNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AlarmStateVariableType.HIGHEST_ACTIVE_SEVERITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getHighestActiveSeverity() {
    return getProperty(AlarmStateVariableType.HIGHEST_ACTIVE_SEVERITY).orElse(null);
  }

  @Override
  public void setHighestActiveSeverity(UShort value) {
    setProperty(AlarmStateVariableType.HIGHEST_ACTIVE_SEVERITY, value);
  }

  @Override
  public PropertyTypeNode getHighestUnackSeverityNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AlarmStateVariableType.HIGHEST_UNACK_SEVERITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getHighestUnackSeverity() {
    return getProperty(AlarmStateVariableType.HIGHEST_UNACK_SEVERITY).orElse(null);
  }

  @Override
  public void setHighestUnackSeverity(UShort value) {
    setProperty(AlarmStateVariableType.HIGHEST_UNACK_SEVERITY, value);
  }

  @Override
  public PropertyTypeNode getActiveCountNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AlarmStateVariableType.ACTIVE_COUNT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getActiveCount() {
    return getProperty(AlarmStateVariableType.ACTIVE_COUNT).orElse(null);
  }

  @Override
  public void setActiveCount(UInteger value) {
    setProperty(AlarmStateVariableType.ACTIVE_COUNT, value);
  }

  @Override
  public PropertyTypeNode getUnacknowledgedCountNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AlarmStateVariableType.UNACKNOWLEDGED_COUNT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getUnacknowledgedCount() {
    return getProperty(AlarmStateVariableType.UNACKNOWLEDGED_COUNT).orElse(null);
  }

  @Override
  public void setUnacknowledgedCount(UInteger value) {
    setProperty(AlarmStateVariableType.UNACKNOWLEDGED_COUNT, value);
  }

  @Override
  public PropertyTypeNode getUnconfirmedCountNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AlarmStateVariableType.UNCONFIRMED_COUNT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getUnconfirmedCount() {
    return getProperty(AlarmStateVariableType.UNCONFIRMED_COUNT).orElse(null);
  }

  @Override
  public void setUnconfirmedCount(UInteger value) {
    setProperty(AlarmStateVariableType.UNCONFIRMED_COUNT, value);
  }

  @Override
  public PropertyTypeNode getFilterNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AlarmStateVariableType.FILTER);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public ContentFilter getFilter() {
    return getProperty(AlarmStateVariableType.FILTER).orElse(null);
  }

  @Override
  public void setFilter(ContentFilter value) {
    setProperty(AlarmStateVariableType.FILTER, value);
  }
}
