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
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
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

public class IIeeeTsnMacAddressTypeNode extends BaseInterfaceTypeNode
    implements IIeeeTsnMacAddressType {
  public IIeeeTsnMacAddressTypeNode(
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

  public IIeeeTsnMacAddressTypeNode(
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
  public BaseDataVariableTypeNode getDestinationAddressNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DestinationAddress");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UByte[] getDestinationAddress() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DestinationAddress");
    return component.map(node -> (UByte[]) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setDestinationAddress(UByte[] value) {
    getVariableComponent("http://opcfoundation.org/UA/", "DestinationAddress")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSourceAddressNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SourceAddress");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UByte[] getSourceAddress() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SourceAddress");
    return component.map(node -> (UByte[]) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSourceAddress(UByte[] value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SourceAddress")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
