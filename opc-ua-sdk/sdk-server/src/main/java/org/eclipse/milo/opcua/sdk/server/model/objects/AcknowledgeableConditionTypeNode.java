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
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
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

public class AcknowledgeableConditionTypeNode extends ConditionTypeNode
    implements AcknowledgeableConditionType {
  public AcknowledgeableConditionTypeNode(
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

  public AcknowledgeableConditionTypeNode(
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
  public TwoStateVariableTypeNode getAckedStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AckedState");
    return (TwoStateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getAckedState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AckedState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setAckedState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "AckedState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public TwoStateVariableTypeNode getConfirmedStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ConfirmedState");
    return (TwoStateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getConfirmedState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ConfirmedState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setConfirmedState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ConfirmedState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public UaMethodNode getAcknowledgeMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Acknowledge",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getConfirmMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Confirm",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
