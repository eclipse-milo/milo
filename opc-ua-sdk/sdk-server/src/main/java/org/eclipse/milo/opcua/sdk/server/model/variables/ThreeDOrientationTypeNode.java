/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class ThreeDOrientationTypeNode extends OrientationTypeNode
    implements ThreeDOrientationType {
  public ThreeDOrientationTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      boolean historizing,
      AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  public ThreeDOrientationTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions);
  }

  @Override
  public BaseDataVariableTypeNode getANode() {
    Optional<VariableNode> component = getVariableComponent("http://opcfoundation.org/UA/", "A");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Double getA() {
    Optional<VariableNode> component = getVariableComponent("http://opcfoundation.org/UA/", "A");
    return component.map(node -> (Double) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setA(Double value) {
    getVariableComponent("http://opcfoundation.org/UA/", "A")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getBNode() {
    Optional<VariableNode> component = getVariableComponent("http://opcfoundation.org/UA/", "B");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Double getB() {
    Optional<VariableNode> component = getVariableComponent("http://opcfoundation.org/UA/", "B");
    return component.map(node -> (Double) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setB(Double value) {
    getVariableComponent("http://opcfoundation.org/UA/", "B")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getCNode() {
    Optional<VariableNode> component = getVariableComponent("http://opcfoundation.org/UA/", "C");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Double getC() {
    Optional<VariableNode> component = getVariableComponent("http://opcfoundation.org/UA/", "C");
    return component.map(node -> (Double) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setC(Double value) {
    getVariableComponent("http://opcfoundation.org/UA/", "C")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
