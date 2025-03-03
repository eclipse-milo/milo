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
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class UnitTypeNode extends BaseObjectTypeNode implements UnitType {
  public UnitTypeNode(
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

  public UnitTypeNode(
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
  public PropertyTypeNode getSymbolNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(UnitType.SYMBOL);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LocalizedText getSymbol() {
    return getProperty(UnitType.SYMBOL).orElse(null);
  }

  @Override
  public void setSymbol(LocalizedText value) {
    setProperty(UnitType.SYMBOL, value);
  }

  @Override
  public PropertyTypeNode getUnitSystemNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(UnitType.UNIT_SYSTEM);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getUnitSystem() {
    return getProperty(UnitType.UNIT_SYSTEM).orElse(null);
  }

  @Override
  public void setUnitSystem(String value) {
    setProperty(UnitType.UNIT_SYSTEM, value);
  }

  @Override
  public PropertyTypeNode getDisciplineNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(UnitType.DISCIPLINE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getDiscipline() {
    return getProperty(UnitType.DISCIPLINE).orElse(null);
  }

  @Override
  public void setDiscipline(String value) {
    setProperty(UnitType.DISCIPLINE, value);
  }
}
