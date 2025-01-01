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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.Duplex;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class IIeeeBaseEthernetPortTypeNode extends BaseInterfaceTypeNode
    implements IIeeeBaseEthernetPortType {
  public IIeeeBaseEthernetPortTypeNode(
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

  public IIeeeBaseEthernetPortTypeNode(
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

  @Override
  public BaseDataVariableTypeNode getDuplexNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Duplex");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Duplex getDuplex() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Duplex");
    return component.map(node -> (Duplex) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setDuplex(Duplex value) {
    getVariableComponent("http://opcfoundation.org/UA/", "Duplex")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getMaxFrameLengthNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxFrameLength");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UShort getMaxFrameLength() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxFrameLength");
    return component.map(node -> (UShort) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setMaxFrameLength(UShort value) {
    getVariableComponent("http://opcfoundation.org/UA/", "MaxFrameLength")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
