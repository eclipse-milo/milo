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
import org.eclipse.milo.opcua.stack.core.types.structured.CartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.Orientation;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class FrameTypeNode extends BaseDataVariableTypeNode implements FrameType {
  public FrameTypeNode(
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

  public FrameTypeNode(
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
  public PropertyTypeNode getConstantNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(FrameType.CONSTANT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getConstant() {
    return getProperty(FrameType.CONSTANT).orElse(null);
  }

  @Override
  public void setConstant(Boolean value) {
    setProperty(FrameType.CONSTANT, value);
  }

  @Override
  public PropertyTypeNode getFixedBaseNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(FrameType.FIXED_BASE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getFixedBase() {
    return getProperty(FrameType.FIXED_BASE).orElse(null);
  }

  @Override
  public void setFixedBase(Boolean value) {
    setProperty(FrameType.FIXED_BASE, value);
  }

  @Override
  public CartesianCoordinatesTypeNode getCartesianCoordinatesNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CartesianCoordinates");
    return (CartesianCoordinatesTypeNode) component.orElse(null);
  }

  @Override
  public CartesianCoordinates getCartesianCoordinates() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CartesianCoordinates");
    return component
        .map(node -> (CartesianCoordinates) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setCartesianCoordinates(CartesianCoordinates value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CartesianCoordinates")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public OrientationTypeNode getOrientationNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Orientation");
    return (OrientationTypeNode) component.orElse(null);
  }

  @Override
  public Orientation getOrientation() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Orientation");
    return component.map(node -> (Orientation) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setOrientation(Orientation value) {
    getVariableComponent("http://opcfoundation.org/UA/", "Orientation")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getBaseFrameNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "BaseFrame");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public NodeId getBaseFrame() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "BaseFrame");
    return component.map(node -> (NodeId) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setBaseFrame(NodeId value) {
    getVariableComponent("http://opcfoundation.org/UA/", "BaseFrame")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
