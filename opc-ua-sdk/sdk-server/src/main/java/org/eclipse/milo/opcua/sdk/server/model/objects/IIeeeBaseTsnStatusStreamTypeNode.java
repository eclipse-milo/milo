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
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class IIeeeBaseTsnStatusStreamTypeNode extends BaseInterfaceTypeNode
    implements IIeeeBaseTsnStatusStreamType {
  public IIeeeBaseTsnStatusStreamTypeNode(
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

  public IIeeeBaseTsnStatusStreamTypeNode(
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
  public BaseDataVariableTypeNode getTalkerStatusNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TalkerStatus");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public TsnTalkerStatus getTalkerStatus() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TalkerStatus");
    return component
        .map(node -> (TsnTalkerStatus) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setTalkerStatus(TsnTalkerStatus value) {
    getVariableComponent("http://opcfoundation.org/UA/", "TalkerStatus")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getListenerStatusNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ListenerStatus");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public TsnListenerStatus getListenerStatus() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ListenerStatus");
    return component
        .map(node -> (TsnListenerStatus) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setListenerStatus(TsnListenerStatus value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ListenerStatus")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getFailureCodeNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "FailureCode");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public TsnFailureCode getFailureCode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "FailureCode");
    return component
        .map(node -> (TsnFailureCode) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setFailureCode(TsnFailureCode value) {
    getVariableComponent("http://opcfoundation.org/UA/", "FailureCode")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getFailureSystemIdentifierNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "FailureSystemIdentifier");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Object getFailureSystemIdentifier() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "FailureSystemIdentifier");
    return component.map(node -> (Object) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setFailureSystemIdentifier(Object value) {
    getVariableComponent("http://opcfoundation.org/UA/", "FailureSystemIdentifier")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
