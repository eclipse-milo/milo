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
import org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class ArrayItemTypeNode extends DataItemTypeNode implements ArrayItemType {
  public ArrayItemTypeNode(
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

  public ArrayItemTypeNode(
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
    Optional<VariableNode> propertyNode = getPropertyNode(ArrayItemType.INSTRUMENT_RANGE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Range getInstrumentRange() {
    return getProperty(ArrayItemType.INSTRUMENT_RANGE).orElse(null);
  }

  @Override
  public void setInstrumentRange(Range value) {
    setProperty(ArrayItemType.INSTRUMENT_RANGE, value);
  }

  @Override
  public PropertyTypeNode getEuRangeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ArrayItemType.EU_RANGE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Range getEuRange() {
    return getProperty(ArrayItemType.EU_RANGE).orElse(null);
  }

  @Override
  public void setEuRange(Range value) {
    setProperty(ArrayItemType.EU_RANGE, value);
  }

  @Override
  public PropertyTypeNode getEngineeringUnitsNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ArrayItemType.ENGINEERING_UNITS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public EUInformation getEngineeringUnits() {
    return getProperty(ArrayItemType.ENGINEERING_UNITS).orElse(null);
  }

  @Override
  public void setEngineeringUnits(EUInformation value) {
    setProperty(ArrayItemType.ENGINEERING_UNITS, value);
  }

  @Override
  public PropertyTypeNode getTitleNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ArrayItemType.TITLE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LocalizedText getTitle() {
    return getProperty(ArrayItemType.TITLE).orElse(null);
  }

  @Override
  public void setTitle(LocalizedText value) {
    setProperty(ArrayItemType.TITLE, value);
  }

  @Override
  public PropertyTypeNode getAxisScaleTypeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ArrayItemType.AXIS_SCALE_TYPE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public AxisScaleEnumeration getAxisScaleType() {
    return getProperty(ArrayItemType.AXIS_SCALE_TYPE).orElse(null);
  }

  @Override
  public void setAxisScaleType(AxisScaleEnumeration value) {
    setProperty(ArrayItemType.AXIS_SCALE_TYPE, value);
  }
}
