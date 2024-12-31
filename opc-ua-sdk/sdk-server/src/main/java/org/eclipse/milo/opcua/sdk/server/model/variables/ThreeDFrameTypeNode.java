/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDCartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDOrientation;

public class ThreeDFrameTypeNode extends FrameTypeNode implements ThreeDFrameType {
  public ThreeDFrameTypeNode(
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

  public ThreeDFrameTypeNode(
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
  public ThreeDCartesianCoordinatesTypeNode getCartesianCoordinatesNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CartesianCoordinates");
    return (ThreeDCartesianCoordinatesTypeNode) component.orElse(null);
  }

  @Override
  public ThreeDCartesianCoordinates getCartesianCoordinates() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CartesianCoordinates");
    return component
        .map(node -> (ThreeDCartesianCoordinates) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setCartesianCoordinates(ThreeDCartesianCoordinates value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CartesianCoordinates")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public ThreeDOrientationTypeNode getOrientationNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Orientation");
    return (ThreeDOrientationTypeNode) component.orElse(null);
  }

  @Override
  public ThreeDOrientation getOrientation() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Orientation");
    return component
        .map(node -> (ThreeDOrientation) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setOrientation(ThreeDOrientation value) {
    getVariableComponent("http://opcfoundation.org/UA/", "Orientation")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
