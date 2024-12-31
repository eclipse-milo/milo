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
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ProgramDiagnostic2TypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnostic2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class ProgramStateMachineTypeNode extends FiniteStateMachineTypeNode
    implements ProgramStateMachineType {
  public ProgramStateMachineTypeNode(
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

  public ProgramStateMachineTypeNode(
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
  public PropertyTypeNode getCreatableNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ProgramStateMachineType.CREATABLE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getCreatable() {
    return getProperty(ProgramStateMachineType.CREATABLE).orElse(null);
  }

  @Override
  public void setCreatable(Boolean value) {
    setProperty(ProgramStateMachineType.CREATABLE, value);
  }

  @Override
  public PropertyTypeNode getDeletableNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ProgramStateMachineType.DELETABLE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getDeletable() {
    return getProperty(ProgramStateMachineType.DELETABLE).orElse(null);
  }

  @Override
  public void setDeletable(Boolean value) {
    setProperty(ProgramStateMachineType.DELETABLE, value);
  }

  @Override
  public PropertyTypeNode getAutoDeleteNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ProgramStateMachineType.AUTO_DELETE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getAutoDelete() {
    return getProperty(ProgramStateMachineType.AUTO_DELETE).orElse(null);
  }

  @Override
  public void setAutoDelete(Boolean value) {
    setProperty(ProgramStateMachineType.AUTO_DELETE, value);
  }

  @Override
  public PropertyTypeNode getRecycleCountNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ProgramStateMachineType.RECYCLE_COUNT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Integer getRecycleCount() {
    return getProperty(ProgramStateMachineType.RECYCLE_COUNT).orElse(null);
  }

  @Override
  public void setRecycleCount(Integer value) {
    setProperty(ProgramStateMachineType.RECYCLE_COUNT, value);
  }

  @Override
  public PropertyTypeNode getInstanceCountNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ProgramStateMachineType.INSTANCE_COUNT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getInstanceCount() {
    return getProperty(ProgramStateMachineType.INSTANCE_COUNT).orElse(null);
  }

  @Override
  public void setInstanceCount(UInteger value) {
    setProperty(ProgramStateMachineType.INSTANCE_COUNT, value);
  }

  @Override
  public PropertyTypeNode getMaxInstanceCountNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ProgramStateMachineType.MAX_INSTANCE_COUNT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxInstanceCount() {
    return getProperty(ProgramStateMachineType.MAX_INSTANCE_COUNT).orElse(null);
  }

  @Override
  public void setMaxInstanceCount(UInteger value) {
    setProperty(ProgramStateMachineType.MAX_INSTANCE_COUNT, value);
  }

  @Override
  public PropertyTypeNode getMaxRecycleCountNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ProgramStateMachineType.MAX_RECYCLE_COUNT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxRecycleCount() {
    return getProperty(ProgramStateMachineType.MAX_RECYCLE_COUNT).orElse(null);
  }

  @Override
  public void setMaxRecycleCount(UInteger value) {
    setProperty(ProgramStateMachineType.MAX_RECYCLE_COUNT, value);
  }

  @Override
  public FiniteStateVariableTypeNode getCurrentStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentState");
    return (FiniteStateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getCurrentState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setCurrentState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CurrentState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public FiniteTransitionVariableTypeNode getLastTransitionNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastTransition");
    return (FiniteTransitionVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getLastTransition() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastTransition");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setLastTransition(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LastTransition")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public ProgramDiagnostic2TypeNode getProgramDiagnosticNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ProgramDiagnostic");
    return (ProgramDiagnostic2TypeNode) component.orElse(null);
  }

  @Override
  public ProgramDiagnostic2DataType getProgramDiagnostic() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ProgramDiagnostic");
    return component
        .map(node -> (ProgramDiagnostic2DataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setProgramDiagnostic(ProgramDiagnostic2DataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ProgramDiagnostic")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseObjectTypeNode getFinalResultDataNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "FinalResultData");
    return (BaseObjectTypeNode) component.orElse(null);
  }

  @Override
  public StateTypeNode getHaltedNode() {
    Optional<ObjectNode> component = getObjectComponent("http://opcfoundation.org/UA/", "Halted");
    return (StateTypeNode) component.orElse(null);
  }

  @Override
  public StateTypeNode getReadyNode() {
    Optional<ObjectNode> component = getObjectComponent("http://opcfoundation.org/UA/", "Ready");
    return (StateTypeNode) component.orElse(null);
  }

  @Override
  public StateTypeNode getRunningNode() {
    Optional<ObjectNode> component = getObjectComponent("http://opcfoundation.org/UA/", "Running");
    return (StateTypeNode) component.orElse(null);
  }

  @Override
  public StateTypeNode getSuspendedNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "Suspended");
    return (StateTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getHaltedToReadyNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "HaltedToReady");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getReadyToRunningNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "ReadyToRunning");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getRunningToHaltedNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "RunningToHalted");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getRunningToReadyNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "RunningToReady");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getRunningToSuspendedNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "RunningToSuspended");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getSuspendedToRunningNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "SuspendedToRunning");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getSuspendedToHaltedNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "SuspendedToHalted");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getSuspendedToReadyNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "SuspendedToReady");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getReadyToHaltedNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "ReadyToHalted");
    return (TransitionTypeNode) component.orElse(null);
  }
}
