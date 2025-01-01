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
import org.eclipse.milo.opcua.sdk.server.model.variables.StateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TransitionVariableTypeNode;
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

public class TransitionEventTypeNode extends BaseEventTypeNode implements TransitionEventType {
  public TransitionEventTypeNode(
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

  public TransitionEventTypeNode(
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
  public TransitionVariableTypeNode getTransitionNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Transition");
    return (TransitionVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getTransition() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Transition");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setTransition(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "Transition")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public StateVariableTypeNode getFromStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "FromState");
    return (StateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getFromState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "FromState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setFromState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "FromState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public StateVariableTypeNode getToStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ToState");
    return (StateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getToState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ToState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setToState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ToState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
