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

public class SelectionListTypeNode extends BaseDataVariableTypeNode implements SelectionListType {
  public SelectionListTypeNode(
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

  public SelectionListTypeNode(
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
  public PropertyTypeNode getSelectionsNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(SelectionListType.SELECTIONS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Object[] getSelections() {
    return getProperty(SelectionListType.SELECTIONS).orElse(null);
  }

  @Override
  public void setSelections(Object[] value) {
    setProperty(SelectionListType.SELECTIONS, value);
  }

  @Override
  public PropertyTypeNode getSelectionDescriptionsNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(SelectionListType.SELECTION_DESCRIPTIONS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LocalizedText[] getSelectionDescriptions() {
    return getProperty(SelectionListType.SELECTION_DESCRIPTIONS).orElse(null);
  }

  @Override
  public void setSelectionDescriptions(LocalizedText[] value) {
    setProperty(SelectionListType.SELECTION_DESCRIPTIONS, value);
  }

  @Override
  public PropertyTypeNode getRestrictToListNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(SelectionListType.RESTRICT_TO_LIST);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getRestrictToList() {
    return getProperty(SelectionListType.RESTRICT_TO_LIST).orElse(null);
  }

  @Override
  public void setRestrictToList(Boolean value) {
    setProperty(SelectionListType.RESTRICT_TO_LIST, value);
  }
}
