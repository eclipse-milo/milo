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
import org.eclipse.milo.opcua.stack.core.types.structured.UnsignedRationalNumber;

public class IIeeeBaseTsnTrafficSpecificationTypeNode extends BaseInterfaceTypeNode
    implements IIeeeBaseTsnTrafficSpecificationType {
  public IIeeeBaseTsnTrafficSpecificationTypeNode(
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

  public IIeeeBaseTsnTrafficSpecificationTypeNode(
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
  public BaseDataVariableTypeNode getMaxIntervalFramesNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxIntervalFrames");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UShort getMaxIntervalFrames() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxIntervalFrames");
    return component.map(node -> (UShort) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setMaxIntervalFrames(UShort value) {
    getVariableComponent("http://opcfoundation.org/UA/", "MaxIntervalFrames")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getMaxFrameSizeNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxFrameSize");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getMaxFrameSize() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxFrameSize");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setMaxFrameSize(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "MaxFrameSize")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getIntervalNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Interval");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UnsignedRationalNumber getInterval() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Interval");
    return component
        .map(node -> (UnsignedRationalNumber) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setInterval(UnsignedRationalNumber value) {
    getVariableComponent("http://opcfoundation.org/UA/", "Interval")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
