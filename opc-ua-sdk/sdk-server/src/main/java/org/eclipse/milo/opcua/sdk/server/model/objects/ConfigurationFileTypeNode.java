/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class ConfigurationFileTypeNode extends FileTypeNode implements ConfigurationFileType {
  public ConfigurationFileTypeNode(
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

  public ConfigurationFileTypeNode(
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
  public PropertyTypeNode getLastUpdateTimeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ConfigurationFileType.LAST_UPDATE_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getLastUpdateTime() {
    return getProperty(ConfigurationFileType.LAST_UPDATE_TIME).orElse(null);
  }

  @Override
  public void setLastUpdateTime(DateTime value) {
    setProperty(ConfigurationFileType.LAST_UPDATE_TIME, value);
  }

  @Override
  public PropertyTypeNode getCurrentVersionNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ConfigurationFileType.CURRENT_VERSION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getCurrentVersion() {
    return getProperty(ConfigurationFileType.CURRENT_VERSION).orElse(null);
  }

  @Override
  public void setCurrentVersion(UInteger value) {
    setProperty(ConfigurationFileType.CURRENT_VERSION, value);
  }

  @Override
  public PropertyTypeNode getActivityTimeoutNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ConfigurationFileType.ACTIVITY_TIMEOUT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getActivityTimeout() {
    return getProperty(ConfigurationFileType.ACTIVITY_TIMEOUT).orElse(null);
  }

  @Override
  public void setActivityTimeout(Double value) {
    setProperty(ConfigurationFileType.ACTIVITY_TIMEOUT, value);
  }

  @Override
  public PropertyTypeNode getSupportedDataTypeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ConfigurationFileType.SUPPORTED_DATA_TYPE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getSupportedDataType() {
    return getProperty(ConfigurationFileType.SUPPORTED_DATA_TYPE).orElse(null);
  }

  @Override
  public void setSupportedDataType(NodeId value) {
    setProperty(ConfigurationFileType.SUPPORTED_DATA_TYPE, value);
  }

  @Override
  public UaMethodNode getConfirmUpdateMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "ConfirmUpdate",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getCloseAndUpdateMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "CloseAndUpdate",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
