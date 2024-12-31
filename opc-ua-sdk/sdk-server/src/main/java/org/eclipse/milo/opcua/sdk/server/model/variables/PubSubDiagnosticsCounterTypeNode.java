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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class PubSubDiagnosticsCounterTypeNode extends BaseDataVariableTypeNode
    implements PubSubDiagnosticsCounterType {
  public PubSubDiagnosticsCounterTypeNode(
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

  public PubSubDiagnosticsCounterTypeNode(
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
  public PropertyTypeNode getActiveNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PubSubDiagnosticsCounterType.ACTIVE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getActive() {
    return getProperty(PubSubDiagnosticsCounterType.ACTIVE).orElse(null);
  }

  @Override
  public void setActive(Boolean value) {
    setProperty(PubSubDiagnosticsCounterType.ACTIVE, value);
  }

  @Override
  public PropertyTypeNode getClassificationNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PubSubDiagnosticsCounterType.CLASSIFICATION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public PubSubDiagnosticsCounterClassification getClassification() {
    return getProperty(PubSubDiagnosticsCounterType.CLASSIFICATION).orElse(null);
  }

  @Override
  public void setClassification(PubSubDiagnosticsCounterClassification value) {
    setProperty(PubSubDiagnosticsCounterType.CLASSIFICATION, value);
  }

  @Override
  public PropertyTypeNode getDiagnosticsLevelNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PubSubDiagnosticsCounterType.DIAGNOSTICS_LEVEL);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DiagnosticsLevel getDiagnosticsLevel() {
    return getProperty(PubSubDiagnosticsCounterType.DIAGNOSTICS_LEVEL).orElse(null);
  }

  @Override
  public void setDiagnosticsLevel(DiagnosticsLevel value) {
    setProperty(PubSubDiagnosticsCounterType.DIAGNOSTICS_LEVEL, value);
  }

  @Override
  public PropertyTypeNode getTimeFirstChangeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PubSubDiagnosticsCounterType.TIME_FIRST_CHANGE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getTimeFirstChange() {
    return getProperty(PubSubDiagnosticsCounterType.TIME_FIRST_CHANGE).orElse(null);
  }

  @Override
  public void setTimeFirstChange(DateTime value) {
    setProperty(PubSubDiagnosticsCounterType.TIME_FIRST_CHANGE, value);
  }
}
