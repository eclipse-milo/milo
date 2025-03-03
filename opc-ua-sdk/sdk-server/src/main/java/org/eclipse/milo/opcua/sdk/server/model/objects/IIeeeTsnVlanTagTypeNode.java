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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class IIeeeTsnVlanTagTypeNode extends BaseInterfaceTypeNode implements IIeeeTsnVlanTagType {
  public IIeeeTsnVlanTagTypeNode(
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

  public IIeeeTsnVlanTagTypeNode(
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
  public BaseDataVariableTypeNode getVlanIdNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "VlanId");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UShort getVlanId() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "VlanId");
    return component.map(node -> (UShort) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setVlanId(UShort value) {
    getVariableComponent("http://opcfoundation.org/UA/", "VlanId")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getPriorityCodePointNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PriorityCodePoint");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UByte getPriorityCodePoint() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PriorityCodePoint");
    return component.map(node -> (UByte) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setPriorityCodePoint(UByte value) {
    getVariableComponent("http://opcfoundation.org/UA/", "PriorityCodePoint")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
