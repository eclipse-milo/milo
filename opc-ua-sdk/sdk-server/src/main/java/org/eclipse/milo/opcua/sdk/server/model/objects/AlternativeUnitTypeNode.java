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
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LinearConversionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AlternativeUnitTypeNode extends UnitTypeNode implements AlternativeUnitType {
  public AlternativeUnitTypeNode(
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

  public AlternativeUnitTypeNode(
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
  public PropertyTypeNode getLinearConversionNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AlternativeUnitType.LINEAR_CONVERSION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LinearConversionDataType getLinearConversion() {
    return getProperty(AlternativeUnitType.LINEAR_CONVERSION).orElse(null);
  }

  @Override
  public void setLinearConversion(LinearConversionDataType value) {
    setProperty(AlternativeUnitType.LINEAR_CONVERSION, value);
  }

  @Override
  public PropertyTypeNode getMathMlConversionNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AlternativeUnitType.MATH_ML_CONVERSION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getMathMlConversion() {
    return getProperty(AlternativeUnitType.MATH_ML_CONVERSION).orElse(null);
  }

  @Override
  public void setMathMlConversion(String value) {
    setProperty(AlternativeUnitType.MATH_ML_CONVERSION, value);
  }

  @Override
  public PropertyTypeNode getMathMlInverseConversionNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AlternativeUnitType.MATH_ML_INVERSE_CONVERSION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getMathMlInverseConversion() {
    return getProperty(AlternativeUnitType.MATH_ML_INVERSE_CONVERSION).orElse(null);
  }

  @Override
  public void setMathMlInverseConversion(String value) {
    setProperty(AlternativeUnitType.MATH_ML_INVERSE_CONVERSION, value);
  }
}
