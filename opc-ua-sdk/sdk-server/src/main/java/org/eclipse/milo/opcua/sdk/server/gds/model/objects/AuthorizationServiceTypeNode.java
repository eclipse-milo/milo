/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.gds.model.objects;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

public class AuthorizationServiceTypeNode extends BaseObjectTypeNode
    implements AuthorizationServiceType {
  public AuthorizationServiceTypeNode(
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

  public AuthorizationServiceTypeNode(
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
  public PropertyTypeNode getServiceUriNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AuthorizationServiceType.SERVICE_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getServiceUri() {
    return getProperty(AuthorizationServiceType.SERVICE_URI).orElse(null);
  }

  @Override
  public void setServiceUri(String value) {
    setProperty(AuthorizationServiceType.SERVICE_URI, value);
  }

  @Override
  public PropertyTypeNode getServiceCertificateNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuthorizationServiceType.SERVICE_CERTIFICATE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public ByteString getServiceCertificate() {
    return getProperty(AuthorizationServiceType.SERVICE_CERTIFICATE).orElse(null);
  }

  @Override
  public void setServiceCertificate(ByteString value) {
    setProperty(AuthorizationServiceType.SERVICE_CERTIFICATE, value);
  }

  @Override
  public PropertyTypeNode getUserTokenPoliciesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuthorizationServiceType.USER_TOKEN_POLICIES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UserTokenPolicy[] getUserTokenPolicies() {
    return getProperty(AuthorizationServiceType.USER_TOKEN_POLICIES).orElse(null);
  }

  @Override
  public void setUserTokenPolicies(UserTokenPolicy[] value) {
    setProperty(AuthorizationServiceType.USER_TOKEN_POLICIES, value);
  }

  @Override
  public PropertyTypeNode getSupportedRolesNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(AuthorizationServiceType.SUPPORTED_ROLES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String[] getSupportedRoles() {
    return getProperty(AuthorizationServiceType.SUPPORTED_ROLES).orElse(null);
  }

  @Override
  public void setSupportedRoles(String[] value) {
    setProperty(AuthorizationServiceType.SUPPORTED_ROLES, value);
  }

  @Override
  public UaMethodNode getGetServiceDescriptionMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/GDS/",
            "GetServiceDescription",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getRequestAccessTokenMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/GDS/",
            "RequestAccessToken",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getStartRequestTokenMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/GDS/",
            "StartRequestToken",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getFinishRequestTokenMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/GDS/",
            "FinishRequestToken",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getRefreshTokenMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/GDS/",
            "RefreshToken",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
