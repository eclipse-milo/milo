/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

public class AggregateConfigurationTypeNode extends BaseObjectTypeNode
    implements AggregateConfigurationType {
  public AggregateConfigurationTypeNode(
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

  public AggregateConfigurationTypeNode(
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
  public PropertyTypeNode getTreatUncertainAsBadNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AggregateConfigurationType.TREAT_UNCERTAIN_AS_BAD);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getTreatUncertainAsBad() {
    return getProperty(AggregateConfigurationType.TREAT_UNCERTAIN_AS_BAD).orElse(null);
  }

  @Override
  public void setTreatUncertainAsBad(Boolean value) {
    setProperty(AggregateConfigurationType.TREAT_UNCERTAIN_AS_BAD, value);
  }

  @Override
  public PropertyTypeNode getPercentDataBadNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AggregateConfigurationType.PERCENT_DATA_BAD);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UByte getPercentDataBad() {
    return getProperty(AggregateConfigurationType.PERCENT_DATA_BAD).orElse(null);
  }

  @Override
  public void setPercentDataBad(UByte value) {
    setProperty(AggregateConfigurationType.PERCENT_DATA_BAD, value);
  }

  @Override
  public PropertyTypeNode getPercentDataGoodNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AggregateConfigurationType.PERCENT_DATA_GOOD);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UByte getPercentDataGood() {
    return getProperty(AggregateConfigurationType.PERCENT_DATA_GOOD).orElse(null);
  }

  @Override
  public void setPercentDataGood(UByte value) {
    setProperty(AggregateConfigurationType.PERCENT_DATA_GOOD, value);
  }

  @Override
  public PropertyTypeNode getUseSlopedExtrapolationNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AggregateConfigurationType.USE_SLOPED_EXTRAPOLATION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getUseSlopedExtrapolation() {
    return getProperty(AggregateConfigurationType.USE_SLOPED_EXTRAPOLATION).orElse(null);
  }

  @Override
  public void setUseSlopedExtrapolation(Boolean value) {
    setProperty(AggregateConfigurationType.USE_SLOPED_EXTRAPOLATION, value);
  }
}
