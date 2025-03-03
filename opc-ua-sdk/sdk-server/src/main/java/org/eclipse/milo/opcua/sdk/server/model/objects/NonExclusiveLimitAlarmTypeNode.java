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
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class NonExclusiveLimitAlarmTypeNode extends LimitAlarmTypeNode
    implements NonExclusiveLimitAlarmType {
  public NonExclusiveLimitAlarmTypeNode(
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

  public NonExclusiveLimitAlarmTypeNode(
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
  public TwoStateVariableTypeNode getHighHighStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "HighHighState");
    return (TwoStateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getHighHighState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "HighHighState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setHighHighState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "HighHighState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public TwoStateVariableTypeNode getHighStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "HighState");
    return (TwoStateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getHighState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "HighState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setHighState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "HighState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public TwoStateVariableTypeNode getLowStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LowState");
    return (TwoStateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getLowState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LowState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setLowState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LowState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public TwoStateVariableTypeNode getLowLowStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LowLowState");
    return (TwoStateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getLowLowState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LowLowState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setLowLowState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LowLowState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
