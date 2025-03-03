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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class LimitAlarmTypeNode extends AlarmConditionTypeNode implements LimitAlarmType {
  public LimitAlarmTypeNode(
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

  public LimitAlarmTypeNode(
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
  public PropertyTypeNode getHighHighLimitNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.HIGH_HIGH_LIMIT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getHighHighLimit() {
    return getProperty(LimitAlarmType.HIGH_HIGH_LIMIT).orElse(null);
  }

  @Override
  public void setHighHighLimit(Double value) {
    setProperty(LimitAlarmType.HIGH_HIGH_LIMIT, value);
  }

  @Override
  public PropertyTypeNode getHighLimitNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.HIGH_LIMIT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getHighLimit() {
    return getProperty(LimitAlarmType.HIGH_LIMIT).orElse(null);
  }

  @Override
  public void setHighLimit(Double value) {
    setProperty(LimitAlarmType.HIGH_LIMIT, value);
  }

  @Override
  public PropertyTypeNode getLowLimitNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.LOW_LIMIT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getLowLimit() {
    return getProperty(LimitAlarmType.LOW_LIMIT).orElse(null);
  }

  @Override
  public void setLowLimit(Double value) {
    setProperty(LimitAlarmType.LOW_LIMIT, value);
  }

  @Override
  public PropertyTypeNode getLowLowLimitNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.LOW_LOW_LIMIT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getLowLowLimit() {
    return getProperty(LimitAlarmType.LOW_LOW_LIMIT).orElse(null);
  }

  @Override
  public void setLowLowLimit(Double value) {
    setProperty(LimitAlarmType.LOW_LOW_LIMIT, value);
  }

  @Override
  public PropertyTypeNode getBaseHighHighLimitNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.BASE_HIGH_HIGH_LIMIT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getBaseHighHighLimit() {
    return getProperty(LimitAlarmType.BASE_HIGH_HIGH_LIMIT).orElse(null);
  }

  @Override
  public void setBaseHighHighLimit(Double value) {
    setProperty(LimitAlarmType.BASE_HIGH_HIGH_LIMIT, value);
  }

  @Override
  public PropertyTypeNode getBaseHighLimitNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.BASE_HIGH_LIMIT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getBaseHighLimit() {
    return getProperty(LimitAlarmType.BASE_HIGH_LIMIT).orElse(null);
  }

  @Override
  public void setBaseHighLimit(Double value) {
    setProperty(LimitAlarmType.BASE_HIGH_LIMIT, value);
  }

  @Override
  public PropertyTypeNode getBaseLowLimitNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.BASE_LOW_LIMIT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getBaseLowLimit() {
    return getProperty(LimitAlarmType.BASE_LOW_LIMIT).orElse(null);
  }

  @Override
  public void setBaseLowLimit(Double value) {
    setProperty(LimitAlarmType.BASE_LOW_LIMIT, value);
  }

  @Override
  public PropertyTypeNode getBaseLowLowLimitNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.BASE_LOW_LOW_LIMIT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getBaseLowLowLimit() {
    return getProperty(LimitAlarmType.BASE_LOW_LOW_LIMIT).orElse(null);
  }

  @Override
  public void setBaseLowLowLimit(Double value) {
    setProperty(LimitAlarmType.BASE_LOW_LOW_LIMIT, value);
  }

  @Override
  public PropertyTypeNode getSeverityHighHighNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.SEVERITY_HIGH_HIGH);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getSeverityHighHigh() {
    return getProperty(LimitAlarmType.SEVERITY_HIGH_HIGH).orElse(null);
  }

  @Override
  public void setSeverityHighHigh(UShort value) {
    setProperty(LimitAlarmType.SEVERITY_HIGH_HIGH, value);
  }

  @Override
  public PropertyTypeNode getSeverityHighNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.SEVERITY_HIGH);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getSeverityHigh() {
    return getProperty(LimitAlarmType.SEVERITY_HIGH).orElse(null);
  }

  @Override
  public void setSeverityHigh(UShort value) {
    setProperty(LimitAlarmType.SEVERITY_HIGH, value);
  }

  @Override
  public PropertyTypeNode getSeverityLowNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.SEVERITY_LOW);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getSeverityLow() {
    return getProperty(LimitAlarmType.SEVERITY_LOW).orElse(null);
  }

  @Override
  public void setSeverityLow(UShort value) {
    setProperty(LimitAlarmType.SEVERITY_LOW, value);
  }

  @Override
  public PropertyTypeNode getSeverityLowLowNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.SEVERITY_LOW_LOW);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getSeverityLowLow() {
    return getProperty(LimitAlarmType.SEVERITY_LOW_LOW).orElse(null);
  }

  @Override
  public void setSeverityLowLow(UShort value) {
    setProperty(LimitAlarmType.SEVERITY_LOW_LOW, value);
  }

  @Override
  public PropertyTypeNode getHighHighDeadbandNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.HIGH_HIGH_DEADBAND);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getHighHighDeadband() {
    return getProperty(LimitAlarmType.HIGH_HIGH_DEADBAND).orElse(null);
  }

  @Override
  public void setHighHighDeadband(Double value) {
    setProperty(LimitAlarmType.HIGH_HIGH_DEADBAND, value);
  }

  @Override
  public PropertyTypeNode getHighDeadbandNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.HIGH_DEADBAND);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getHighDeadband() {
    return getProperty(LimitAlarmType.HIGH_DEADBAND).orElse(null);
  }

  @Override
  public void setHighDeadband(Double value) {
    setProperty(LimitAlarmType.HIGH_DEADBAND, value);
  }

  @Override
  public PropertyTypeNode getLowDeadbandNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.LOW_DEADBAND);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getLowDeadband() {
    return getProperty(LimitAlarmType.LOW_DEADBAND).orElse(null);
  }

  @Override
  public void setLowDeadband(Double value) {
    setProperty(LimitAlarmType.LOW_DEADBAND, value);
  }

  @Override
  public PropertyTypeNode getLowLowDeadbandNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LimitAlarmType.LOW_LOW_DEADBAND);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getLowLowDeadband() {
    return getProperty(LimitAlarmType.LOW_LOW_DEADBAND).orElse(null);
  }

  @Override
  public void setLowLowDeadband(Double value) {
    setProperty(LimitAlarmType.LOW_LOW_DEADBAND, value);
  }
}
