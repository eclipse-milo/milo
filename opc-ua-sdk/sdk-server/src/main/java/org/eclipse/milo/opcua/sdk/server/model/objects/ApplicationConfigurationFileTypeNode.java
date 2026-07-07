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
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

public class ApplicationConfigurationFileTypeNode extends ConfigurationFileTypeNode
    implements ApplicationConfigurationFileType {
  public ApplicationConfigurationFileTypeNode(
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

  public ApplicationConfigurationFileTypeNode(
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
  public PropertyTypeNode getAvailableNetworksNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ApplicationConfigurationFileType.AVAILABLE_NETWORKS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String[] getAvailableNetworks() {
    return getProperty(ApplicationConfigurationFileType.AVAILABLE_NETWORKS).orElse(null);
  }

  @Override
  public void setAvailableNetworks(String[] value) {
    setProperty(ApplicationConfigurationFileType.AVAILABLE_NETWORKS, value);
  }

  @Override
  public PropertyTypeNode getAvailablePortsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ApplicationConfigurationFileType.AVAILABLE_PORTS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getAvailablePorts() {
    return getProperty(ApplicationConfigurationFileType.AVAILABLE_PORTS).orElse(null);
  }

  @Override
  public void setAvailablePorts(String value) {
    setProperty(ApplicationConfigurationFileType.AVAILABLE_PORTS, value);
  }

  @Override
  public PropertyTypeNode getMaxEndpointsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ApplicationConfigurationFileType.MAX_ENDPOINTS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getMaxEndpoints() {
    return getProperty(ApplicationConfigurationFileType.MAX_ENDPOINTS).orElse(null);
  }

  @Override
  public void setMaxEndpoints(UShort value) {
    setProperty(ApplicationConfigurationFileType.MAX_ENDPOINTS, value);
  }

  @Override
  public PropertyTypeNode getMaxCertificateGroupsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ApplicationConfigurationFileType.MAX_CERTIFICATE_GROUPS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getMaxCertificateGroups() {
    return getProperty(ApplicationConfigurationFileType.MAX_CERTIFICATE_GROUPS).orElse(null);
  }

  @Override
  public void setMaxCertificateGroups(UShort value) {
    setProperty(ApplicationConfigurationFileType.MAX_CERTIFICATE_GROUPS, value);
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUrisNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ApplicationConfigurationFileType.SECURITY_POLICY_URIS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String[] getSecurityPolicyUris() {
    return getProperty(ApplicationConfigurationFileType.SECURITY_POLICY_URIS).orElse(null);
  }

  @Override
  public void setSecurityPolicyUris(String[] value) {
    setProperty(ApplicationConfigurationFileType.SECURITY_POLICY_URIS, value);
  }

  @Override
  public PropertyTypeNode getUserTokenTypesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ApplicationConfigurationFileType.USER_TOKEN_TYPES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UserTokenPolicy[] getUserTokenTypes() {
    return getProperty(ApplicationConfigurationFileType.USER_TOKEN_TYPES).orElse(null);
  }

  @Override
  public void setUserTokenTypes(UserTokenPolicy[] value) {
    setProperty(ApplicationConfigurationFileType.USER_TOKEN_TYPES, value);
  }

  @Override
  public PropertyTypeNode getCertificateTypesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ApplicationConfigurationFileType.CERTIFICATE_TYPES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId[] getCertificateTypes() {
    return getProperty(ApplicationConfigurationFileType.CERTIFICATE_TYPES).orElse(null);
  }

  @Override
  public void setCertificateTypes(NodeId[] value) {
    setProperty(ApplicationConfigurationFileType.CERTIFICATE_TYPES, value);
  }

  @Override
  public PropertyTypeNode getCertificateGroupPurposesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ApplicationConfigurationFileType.CERTIFICATE_GROUP_PURPOSES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId[] getCertificateGroupPurposes() {
    return getProperty(ApplicationConfigurationFileType.CERTIFICATE_GROUP_PURPOSES).orElse(null);
  }

  @Override
  public void setCertificateGroupPurposes(NodeId[] value) {
    setProperty(ApplicationConfigurationFileType.CERTIFICATE_GROUP_PURPOSES, value);
  }
}
