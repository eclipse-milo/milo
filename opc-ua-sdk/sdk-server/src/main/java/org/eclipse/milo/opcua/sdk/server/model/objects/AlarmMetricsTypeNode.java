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
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AlarmRateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AlarmMetricsTypeNode extends BaseObjectTypeNode implements AlarmMetricsType {
  public AlarmMetricsTypeNode(
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

  public AlarmMetricsTypeNode(
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
  public BaseDataVariableTypeNode getAlarmCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AlarmCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getAlarmCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AlarmCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setAlarmCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "AlarmCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getStartTimeNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "StartTime");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public DateTime getStartTime() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "StartTime");
    return component.map(node -> (DateTime) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setStartTime(DateTime value) {
    getVariableComponent("http://opcfoundation.org/UA/", "StartTime")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getMaximumActiveStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaximumActiveState");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Double getMaximumActiveState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaximumActiveState");
    return component.map(node -> (Double) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setMaximumActiveState(Double value) {
    getVariableComponent("http://opcfoundation.org/UA/", "MaximumActiveState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getMaximumUnAckNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaximumUnAck");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Double getMaximumUnAck() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaximumUnAck");
    return component.map(node -> (Double) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setMaximumUnAck(Double value) {
    getVariableComponent("http://opcfoundation.org/UA/", "MaximumUnAck")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public AlarmRateVariableTypeNode getCurrentAlarmRateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentAlarmRate");
    return (AlarmRateVariableTypeNode) component.orElse(null);
  }

  @Override
  public Double getCurrentAlarmRate() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentAlarmRate");
    return component.map(node -> (Double) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setCurrentAlarmRate(Double value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CurrentAlarmRate")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public AlarmRateVariableTypeNode getMaximumAlarmRateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaximumAlarmRate");
    return (AlarmRateVariableTypeNode) component.orElse(null);
  }

  @Override
  public Double getMaximumAlarmRate() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaximumAlarmRate");
    return component.map(node -> (Double) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setMaximumAlarmRate(Double value) {
    getVariableComponent("http://opcfoundation.org/UA/", "MaximumAlarmRate")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getMaximumReAlarmCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaximumReAlarmCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getMaximumReAlarmCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaximumReAlarmCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setMaximumReAlarmCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "MaximumReAlarmCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public AlarmRateVariableTypeNode getAverageAlarmRateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AverageAlarmRate");
    return (AlarmRateVariableTypeNode) component.orElse(null);
  }

  @Override
  public Double getAverageAlarmRate() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AverageAlarmRate");
    return component.map(node -> (Double) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setAverageAlarmRate(Double value) {
    getVariableComponent("http://opcfoundation.org/UA/", "AverageAlarmRate")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public UaMethodNode getResetMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Reset",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
