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

public class StateMachineTypeNode extends BaseObjectTypeNode implements StateMachineType {
  public StateMachineTypeNode(
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

  public StateMachineTypeNode(
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
  public StateVariableTypeNode getCurrentStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentState");
    return (StateVariableTypeNode) component.orElse(null);
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
  public TransitionVariableTypeNode getLastTransitionNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastTransition");
    return (TransitionVariableTypeNode) component.orElse(null);
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
}
