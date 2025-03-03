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
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogUnitTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class IetfBaseNetworkInterfaceTypeNode extends BaseObjectTypeNode
    implements IetfBaseNetworkInterfaceType {
  public IetfBaseNetworkInterfaceTypeNode(
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

  public IetfBaseNetworkInterfaceTypeNode(
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
  public BaseDataVariableTypeNode getAdminStatusNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AdminStatus");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public InterfaceAdminStatus getAdminStatus() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AdminStatus");
    return component
        .map(node -> (InterfaceAdminStatus) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setAdminStatus(InterfaceAdminStatus value) {
    getVariableComponent("http://opcfoundation.org/UA/", "AdminStatus")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getOperStatusNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "OperStatus");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public InterfaceOperStatus getOperStatus() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "OperStatus");
    return component
        .map(node -> (InterfaceOperStatus) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setOperStatus(InterfaceOperStatus value) {
    getVariableComponent("http://opcfoundation.org/UA/", "OperStatus")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getPhysAddressNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PhysAddress");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getPhysAddress() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PhysAddress");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setPhysAddress(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "PhysAddress")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public AnalogUnitTypeNode getSpeedNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Speed");
    return (AnalogUnitTypeNode) component.orElse(null);
  }

  @Override
  public ULong getSpeed() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Speed");
    return component.map(node -> (ULong) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSpeed(ULong value) {
    getVariableComponent("http://opcfoundation.org/UA/", "Speed")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
