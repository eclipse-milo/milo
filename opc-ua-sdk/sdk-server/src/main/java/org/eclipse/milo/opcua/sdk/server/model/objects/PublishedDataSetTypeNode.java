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
import java.util.UUID;
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
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class PublishedDataSetTypeNode extends BaseObjectTypeNode implements PublishedDataSetType {
  public PublishedDataSetTypeNode(
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

  public PublishedDataSetTypeNode(
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
  public PropertyTypeNode getConfigurationVersionNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PublishedDataSetType.CONFIGURATION_VERSION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public ConfigurationVersionDataType getConfigurationVersion() {
    return getProperty(PublishedDataSetType.CONFIGURATION_VERSION).orElse(null);
  }

  @Override
  public void setConfigurationVersion(ConfigurationVersionDataType value) {
    setProperty(PublishedDataSetType.CONFIGURATION_VERSION, value);
  }

  @Override
  public PropertyTypeNode getDataSetMetaDataNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PublishedDataSetType.DATA_SET_META_DATA);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DataSetMetaDataType getDataSetMetaData() {
    return getProperty(PublishedDataSetType.DATA_SET_META_DATA).orElse(null);
  }

  @Override
  public void setDataSetMetaData(DataSetMetaDataType value) {
    setProperty(PublishedDataSetType.DATA_SET_META_DATA, value);
  }

  @Override
  public PropertyTypeNode getDataSetClassIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PublishedDataSetType.DATA_SET_CLASS_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UUID getDataSetClassId() {
    return getProperty(PublishedDataSetType.DATA_SET_CLASS_ID).orElse(null);
  }

  @Override
  public void setDataSetClassId(UUID value) {
    setProperty(PublishedDataSetType.DATA_SET_CLASS_ID, value);
  }

  @Override
  public PropertyTypeNode getCyclicDataSetNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PublishedDataSetType.CYCLIC_DATA_SET);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getCyclicDataSet() {
    return getProperty(PublishedDataSetType.CYCLIC_DATA_SET).orElse(null);
  }

  @Override
  public void setCyclicDataSet(Boolean value) {
    setProperty(PublishedDataSetType.CYCLIC_DATA_SET, value);
  }

  @Override
  public ExtensionFieldsTypeNode getExtensionFieldsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "ExtensionFields");
    return (ExtensionFieldsTypeNode) component.orElse(null);
  }
}
