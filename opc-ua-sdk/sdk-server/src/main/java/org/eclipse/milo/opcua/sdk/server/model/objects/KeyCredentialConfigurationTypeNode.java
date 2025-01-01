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
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class KeyCredentialConfigurationTypeNode extends BaseObjectTypeNode
    implements KeyCredentialConfigurationType {
  public KeyCredentialConfigurationTypeNode(
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

  public KeyCredentialConfigurationTypeNode(
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
  public PropertyTypeNode getResourceUriNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(KeyCredentialConfigurationType.RESOURCE_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getResourceUri() {
    return getProperty(KeyCredentialConfigurationType.RESOURCE_URI).orElse(null);
  }

  @Override
  public void setResourceUri(String value) {
    setProperty(KeyCredentialConfigurationType.RESOURCE_URI, value);
  }

  @Override
  public PropertyTypeNode getProfileUriNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(KeyCredentialConfigurationType.PROFILE_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getProfileUri() {
    return getProperty(KeyCredentialConfigurationType.PROFILE_URI).orElse(null);
  }

  @Override
  public void setProfileUri(String value) {
    setProperty(KeyCredentialConfigurationType.PROFILE_URI, value);
  }

  @Override
  public PropertyTypeNode getEndpointUrlsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(KeyCredentialConfigurationType.ENDPOINT_URLS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String[] getEndpointUrls() {
    return getProperty(KeyCredentialConfigurationType.ENDPOINT_URLS).orElse(null);
  }

  @Override
  public void setEndpointUrls(String[] value) {
    setProperty(KeyCredentialConfigurationType.ENDPOINT_URLS, value);
  }

  @Override
  public PropertyTypeNode getServiceStatusNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(KeyCredentialConfigurationType.SERVICE_STATUS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public StatusCode getServiceStatus() {
    return getProperty(KeyCredentialConfigurationType.SERVICE_STATUS).orElse(null);
  }

  @Override
  public void setServiceStatus(StatusCode value) {
    setProperty(KeyCredentialConfigurationType.SERVICE_STATUS, value);
  }

  @Override
  public UaMethodNode getGetEncryptingKeyMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "GetEncryptingKey",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getUpdateCredentialMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "UpdateCredential",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getDeleteCredentialMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "DeleteCredential",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
