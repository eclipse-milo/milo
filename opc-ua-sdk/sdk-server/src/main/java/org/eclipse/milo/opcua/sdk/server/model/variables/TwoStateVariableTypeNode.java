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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class TwoStateVariableTypeNode extends StateVariableTypeNode
    implements TwoStateVariableType {
  public TwoStateVariableTypeNode(
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

  public TwoStateVariableTypeNode(
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
    Optional<VariableNode> propertyNode = getPropertyNode(TwoStateVariableType.ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getId() {
    return getProperty(TwoStateVariableType.ID).orElse(null);
  }

  @Override
  public void setId(Boolean value) {
    setProperty(TwoStateVariableType.ID, value);
  }

  @Override
  public PropertyTypeNode getTransitionTimeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(TwoStateVariableType.TRANSITION_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getTransitionTime() {
    return getProperty(TwoStateVariableType.TRANSITION_TIME).orElse(null);
  }

  @Override
  public void setTransitionTime(DateTime value) {
    setProperty(TwoStateVariableType.TRANSITION_TIME, value);
  }

  @Override
  public PropertyTypeNode getEffectiveTransitionTimeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(TwoStateVariableType.EFFECTIVE_TRANSITION_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getEffectiveTransitionTime() {
    return getProperty(TwoStateVariableType.EFFECTIVE_TRANSITION_TIME).orElse(null);
  }

  @Override
  public void setEffectiveTransitionTime(DateTime value) {
    setProperty(TwoStateVariableType.EFFECTIVE_TRANSITION_TIME, value);
  }

  @Override
  public PropertyTypeNode getTrueStateNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(TwoStateVariableType.TRUE_STATE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LocalizedText getTrueState() {
    return getProperty(TwoStateVariableType.TRUE_STATE).orElse(null);
  }

  @Override
  public void setTrueState(LocalizedText value) {
    setProperty(TwoStateVariableType.TRUE_STATE, value);
  }

  @Override
  public PropertyTypeNode getFalseStateNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(TwoStateVariableType.FALSE_STATE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LocalizedText getFalseState() {
    return getProperty(TwoStateVariableType.FALSE_STATE).orElse(null);
  }

  @Override
  public void setFalseState(LocalizedText value) {
    setProperty(TwoStateVariableType.FALSE_STATE, value);
  }
}
