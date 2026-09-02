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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class KeyCredentialServiceTypeNode extends BaseObjectTypeNode
    implements KeyCredentialServiceType {
  public KeyCredentialServiceTypeNode(
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

  public KeyCredentialServiceTypeNode(
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
    Optional<VariableNode> propertyNode = getPropertyNode(KeyCredentialServiceType.RESOURCE_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getResourceUri() {
    return getProperty(KeyCredentialServiceType.RESOURCE_URI).orElse(null);
  }

  @Override
  public void setResourceUri(String value) {
    setProperty(KeyCredentialServiceType.RESOURCE_URI, value);
  }

  @Override
  public PropertyTypeNode getProfileUrisNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(KeyCredentialServiceType.PROFILE_URIS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String[] getProfileUris() {
    return getProperty(KeyCredentialServiceType.PROFILE_URIS).orElse(null);
  }

  @Override
  public void setProfileUris(String[] value) {
    setProperty(KeyCredentialServiceType.PROFILE_URIS, value);
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUrisNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(KeyCredentialServiceType.SECURITY_POLICY_URIS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String[] getSecurityPolicyUris() {
    return getProperty(KeyCredentialServiceType.SECURITY_POLICY_URIS).orElse(null);
  }

  @Override
  public void setSecurityPolicyUris(String[] value) {
    setProperty(KeyCredentialServiceType.SECURITY_POLICY_URIS, value);
  }

  @Override
  public UaMethodNode getStartRequestMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/GDS/",
            "StartRequest",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getFinishRequestMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/GDS/",
            "FinishRequest",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getRevokeMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/GDS/",
            "Revoke",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
