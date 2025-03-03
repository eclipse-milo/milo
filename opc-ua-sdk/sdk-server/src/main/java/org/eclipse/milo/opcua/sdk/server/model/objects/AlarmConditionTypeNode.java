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
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AudioVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AlarmConditionTypeNode extends AcknowledgeableConditionTypeNode
    implements AlarmConditionType {
  public AlarmConditionTypeNode(
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

  public AlarmConditionTypeNode(
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
  public PropertyTypeNode getInputNodeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AlarmConditionType.INPUT_NODE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getInputNode() {
    return getProperty(AlarmConditionType.INPUT_NODE).orElse(null);
  }

  @Override
  public void setInputNode(NodeId value) {
    setProperty(AlarmConditionType.INPUT_NODE, value);
  }

  @Override
  public PropertyTypeNode getSuppressedOrShelvedNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AlarmConditionType.SUPPRESSED_OR_SHELVED);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getSuppressedOrShelved() {
    return getProperty(AlarmConditionType.SUPPRESSED_OR_SHELVED).orElse(null);
  }

  @Override
  public void setSuppressedOrShelved(Boolean value) {
    setProperty(AlarmConditionType.SUPPRESSED_OR_SHELVED, value);
  }

  @Override
  public PropertyTypeNode getMaxTimeShelvedNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AlarmConditionType.MAX_TIME_SHELVED);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getMaxTimeShelved() {
    return getProperty(AlarmConditionType.MAX_TIME_SHELVED).orElse(null);
  }

  @Override
  public void setMaxTimeShelved(Double value) {
    setProperty(AlarmConditionType.MAX_TIME_SHELVED, value);
  }

  @Override
  public PropertyTypeNode getAudibleEnabledNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AlarmConditionType.AUDIBLE_ENABLED);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getAudibleEnabled() {
    return getProperty(AlarmConditionType.AUDIBLE_ENABLED).orElse(null);
  }

  @Override
  public void setAudibleEnabled(Boolean value) {
    setProperty(AlarmConditionType.AUDIBLE_ENABLED, value);
  }

  @Override
  public PropertyTypeNode getOnDelayNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AlarmConditionType.ON_DELAY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getOnDelay() {
    return getProperty(AlarmConditionType.ON_DELAY).orElse(null);
  }

  @Override
  public void setOnDelay(Double value) {
    setProperty(AlarmConditionType.ON_DELAY, value);
  }

  @Override
  public PropertyTypeNode getOffDelayNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AlarmConditionType.OFF_DELAY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getOffDelay() {
    return getProperty(AlarmConditionType.OFF_DELAY).orElse(null);
  }

  @Override
  public void setOffDelay(Double value) {
    setProperty(AlarmConditionType.OFF_DELAY, value);
  }

  @Override
  public PropertyTypeNode getReAlarmTimeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AlarmConditionType.RE_ALARM_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getReAlarmTime() {
    return getProperty(AlarmConditionType.RE_ALARM_TIME).orElse(null);
  }

  @Override
  public void setReAlarmTime(Double value) {
    setProperty(AlarmConditionType.RE_ALARM_TIME, value);
  }

  @Override
  public TwoStateVariableTypeNode getEnabledStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "EnabledState");
    return (TwoStateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getEnabledState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "EnabledState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setEnabledState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "EnabledState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public TwoStateVariableTypeNode getActiveStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ActiveState");
    return (TwoStateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getActiveState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ActiveState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setActiveState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ActiveState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public TwoStateVariableTypeNode getSuppressedStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SuppressedState");
    return (TwoStateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getSuppressedState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SuppressedState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setSuppressedState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SuppressedState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public TwoStateVariableTypeNode getOutOfServiceStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "OutOfServiceState");
    return (TwoStateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getOutOfServiceState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "OutOfServiceState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setOutOfServiceState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "OutOfServiceState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public ShelvedStateMachineTypeNode getShelvingStateNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "ShelvingState");
    return (ShelvedStateMachineTypeNode) component.orElse(null);
  }

  @Override
  public AudioVariableTypeNode getAudibleSoundNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AudibleSound");
    return (AudioVariableTypeNode) component.orElse(null);
  }

  @Override
  public ByteString getAudibleSound() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AudibleSound");
    return component.map(node -> (ByteString) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setAudibleSound(ByteString value) {
    getVariableComponent("http://opcfoundation.org/UA/", "AudibleSound")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public TwoStateVariableTypeNode getSilenceStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SilenceState");
    return (TwoStateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getSilenceState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SilenceState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setSilenceState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SilenceState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getFirstInGroupFlagNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "FirstInGroupFlag");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Boolean getFirstInGroupFlag() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "FirstInGroupFlag");
    return component.map(node -> (Boolean) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setFirstInGroupFlag(Boolean value) {
    getVariableComponent("http://opcfoundation.org/UA/", "FirstInGroupFlag")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public AlarmGroupTypeNode getFirstInGroupNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "FirstInGroup");
    return (AlarmGroupTypeNode) component.orElse(null);
  }

  @Override
  public TwoStateVariableTypeNode getLatchedStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LatchedState");
    return (TwoStateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getLatchedState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LatchedState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setLatchedState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LatchedState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getReAlarmRepeatCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ReAlarmRepeatCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Short getReAlarmRepeatCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ReAlarmRepeatCount");
    return component.map(node -> (Short) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setReAlarmRepeatCount(Short value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ReAlarmRepeatCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public UaMethodNode getSilenceMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Silence",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getSuppressMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Suppress",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getSuppress2MethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Suppress2",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getUnsuppressMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Unsuppress",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getUnsuppress2MethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Unsuppress2",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getRemoveFromServiceMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "RemoveFromService",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getRemoveFromService2MethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "RemoveFromService2",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getPlaceInServiceMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "PlaceInService",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getPlaceInService2MethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "PlaceInService2",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
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

  @Override
  public UaMethodNode getReset2MethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Reset2",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getGetGroupMembershipsMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "GetGroupMemberships",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
