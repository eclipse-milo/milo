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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.AxisInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class CubeItemTypeNode extends ArrayItemTypeNode implements CubeItemType {
  public CubeItemTypeNode(
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

  public CubeItemTypeNode(
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
  public PropertyTypeNode getXAxisDefinitionNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(CubeItemType.X_AXIS_DEFINITION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public AxisInformation getXAxisDefinition() {
    return getProperty(CubeItemType.X_AXIS_DEFINITION).orElse(null);
  }

  @Override
  public void setXAxisDefinition(AxisInformation value) {
    setProperty(CubeItemType.X_AXIS_DEFINITION, value);
  }

  @Override
  public PropertyTypeNode getYAxisDefinitionNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(CubeItemType.Y_AXIS_DEFINITION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public AxisInformation getYAxisDefinition() {
    return getProperty(CubeItemType.Y_AXIS_DEFINITION).orElse(null);
  }

  @Override
  public void setYAxisDefinition(AxisInformation value) {
    setProperty(CubeItemType.Y_AXIS_DEFINITION, value);
  }

  @Override
  public PropertyTypeNode getZAxisDefinitionNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(CubeItemType.Z_AXIS_DEFINITION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public AxisInformation getZAxisDefinition() {
    return getProperty(CubeItemType.Z_AXIS_DEFINITION).orElse(null);
  }

  @Override
  public void setZAxisDefinition(AxisInformation value) {
    setProperty(CubeItemType.Z_AXIS_DEFINITION, value);
  }
}
