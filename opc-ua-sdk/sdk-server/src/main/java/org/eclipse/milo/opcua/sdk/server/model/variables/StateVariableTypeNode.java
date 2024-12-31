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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class StateVariableTypeNode extends BaseDataVariableTypeNode implements StateVariableType {
  public StateVariableTypeNode(
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

  public StateVariableTypeNode(
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
  public PropertyTypeNode getIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(StateVariableType.ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Object getId() {
    return getProperty(StateVariableType.ID).orElse(null);
  }

  @Override
  public void setId(Object value) {
    setProperty(StateVariableType.ID, value);
  }

  @Override
  public PropertyTypeNode getNameNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(StateVariableType.NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public QualifiedName getName() {
    return getProperty(StateVariableType.NAME).orElse(null);
  }

  @Override
  public void setName(QualifiedName value) {
    setProperty(StateVariableType.NAME, value);
  }

  @Override
  public PropertyTypeNode getNumberNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(StateVariableType.NUMBER);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getNumber() {
    return getProperty(StateVariableType.NUMBER).orElse(null);
  }

  @Override
  public void setNumber(UInteger value) {
    setProperty(StateVariableType.NUMBER, value);
  }

  @Override
  public PropertyTypeNode getEffectiveDisplayNameNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(StateVariableType.EFFECTIVE_DISPLAY_NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LocalizedText getEffectiveDisplayName() {
    return getProperty(StateVariableType.EFFECTIVE_DISPLAY_NAME).orElse(null);
  }

  @Override
  public void setEffectiveDisplayName(LocalizedText value) {
    setProperty(StateVariableType.EFFECTIVE_DISPLAY_NAME, value);
  }
}
