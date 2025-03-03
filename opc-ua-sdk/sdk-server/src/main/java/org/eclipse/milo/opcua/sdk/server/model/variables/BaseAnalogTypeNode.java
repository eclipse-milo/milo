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
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class BaseAnalogTypeNode extends DataItemTypeNode implements BaseAnalogType {
  public BaseAnalogTypeNode(
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

  public BaseAnalogTypeNode(
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
  public PropertyTypeNode getInstrumentRangeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseAnalogType.INSTRUMENT_RANGE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Range getInstrumentRange() {
    return getProperty(BaseAnalogType.INSTRUMENT_RANGE).orElse(null);
  }

  @Override
  public void setInstrumentRange(Range value) {
    setProperty(BaseAnalogType.INSTRUMENT_RANGE, value);
  }

  @Override
  public PropertyTypeNode getEuRangeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseAnalogType.EU_RANGE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Range getEuRange() {
    return getProperty(BaseAnalogType.EU_RANGE).orElse(null);
  }

  @Override
  public void setEuRange(Range value) {
    setProperty(BaseAnalogType.EU_RANGE, value);
  }

  @Override
  public PropertyTypeNode getEngineeringUnitsNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseAnalogType.ENGINEERING_UNITS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public EUInformation getEngineeringUnits() {
    return getProperty(BaseAnalogType.ENGINEERING_UNITS).orElse(null);
  }

  @Override
  public void setEngineeringUnits(EUInformation value) {
    setProperty(BaseAnalogType.ENGINEERING_UNITS, value);
  }
}
