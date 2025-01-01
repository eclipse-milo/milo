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

public class RationalNumberTypeNode extends BaseDataVariableTypeNode implements RationalNumberType {
  public RationalNumberTypeNode(
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

  public RationalNumberTypeNode(
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
  public BaseDataVariableTypeNode getNumeratorNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Numerator");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Integer getNumerator() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Numerator");
    return component.map(node -> (Integer) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setNumerator(Integer value) {
    getVariableComponent("http://opcfoundation.org/UA/", "Numerator")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getDenominatorNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Denominator");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getDenominator() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Denominator");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setDenominator(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "Denominator")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
