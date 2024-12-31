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
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class StandaloneSubscribedDataSetTypeNode extends BaseObjectTypeNode
    implements StandaloneSubscribedDataSetType {
  public StandaloneSubscribedDataSetTypeNode(
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

  public StandaloneSubscribedDataSetTypeNode(
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
  public PropertyTypeNode getDataSetMetaDataNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(StandaloneSubscribedDataSetType.DATA_SET_META_DATA);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DataSetMetaDataType getDataSetMetaData() {
    return getProperty(StandaloneSubscribedDataSetType.DATA_SET_META_DATA).orElse(null);
  }

  @Override
  public void setDataSetMetaData(DataSetMetaDataType value) {
    setProperty(StandaloneSubscribedDataSetType.DATA_SET_META_DATA, value);
  }

  @Override
  public PropertyTypeNode getIsConnectedNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(StandaloneSubscribedDataSetType.IS_CONNECTED);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getIsConnected() {
    return getProperty(StandaloneSubscribedDataSetType.IS_CONNECTED).orElse(null);
  }

  @Override
  public void setIsConnected(Boolean value) {
    setProperty(StandaloneSubscribedDataSetType.IS_CONNECTED, value);
  }

  @Override
  public SubscribedDataSetTypeNode getSubscribedDataSetNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "SubscribedDataSet");
    return (SubscribedDataSetTypeNode) component.orElse(null);
  }
}
