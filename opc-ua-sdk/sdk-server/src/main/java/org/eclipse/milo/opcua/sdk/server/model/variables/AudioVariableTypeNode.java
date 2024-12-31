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

public class AudioVariableTypeNode extends BaseDataVariableTypeNode implements AudioVariableType {
  public AudioVariableTypeNode(
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

  public AudioVariableTypeNode(
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
  public PropertyTypeNode getListIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AudioVariableType.LIST_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getListId() {
    return getProperty(AudioVariableType.LIST_ID).orElse(null);
  }

  @Override
  public void setListId(String value) {
    setProperty(AudioVariableType.LIST_ID, value);
  }

  @Override
  public PropertyTypeNode getAgencyIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AudioVariableType.AGENCY_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getAgencyId() {
    return getProperty(AudioVariableType.AGENCY_ID).orElse(null);
  }

  @Override
  public void setAgencyId(String value) {
    setProperty(AudioVariableType.AGENCY_ID, value);
  }

  @Override
  public PropertyTypeNode getVersionIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AudioVariableType.VERSION_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getVersionId() {
    return getProperty(AudioVariableType.VERSION_ID).orElse(null);
  }

  @Override
  public void setVersionId(String value) {
    setProperty(AudioVariableType.VERSION_ID, value);
  }
}
