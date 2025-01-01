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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class SecurityGroupTypeNode extends BaseObjectTypeNode implements SecurityGroupType {
  public SecurityGroupTypeNode(
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

  public SecurityGroupTypeNode(
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
  public PropertyTypeNode getSecurityGroupIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(SecurityGroupType.SECURITY_GROUP_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getSecurityGroupId() {
    return getProperty(SecurityGroupType.SECURITY_GROUP_ID).orElse(null);
  }

  @Override
  public void setSecurityGroupId(String value) {
    setProperty(SecurityGroupType.SECURITY_GROUP_ID, value);
  }

  @Override
  public PropertyTypeNode getKeyLifetimeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(SecurityGroupType.KEY_LIFETIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getKeyLifetime() {
    return getProperty(SecurityGroupType.KEY_LIFETIME).orElse(null);
  }

  @Override
  public void setKeyLifetime(Double value) {
    setProperty(SecurityGroupType.KEY_LIFETIME, value);
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUriNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(SecurityGroupType.SECURITY_POLICY_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getSecurityPolicyUri() {
    return getProperty(SecurityGroupType.SECURITY_POLICY_URI).orElse(null);
  }

  @Override
  public void setSecurityPolicyUri(String value) {
    setProperty(SecurityGroupType.SECURITY_POLICY_URI, value);
  }

  @Override
  public PropertyTypeNode getMaxFutureKeyCountNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(SecurityGroupType.MAX_FUTURE_KEY_COUNT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxFutureKeyCount() {
    return getProperty(SecurityGroupType.MAX_FUTURE_KEY_COUNT).orElse(null);
  }

  @Override
  public void setMaxFutureKeyCount(UInteger value) {
    setProperty(SecurityGroupType.MAX_FUTURE_KEY_COUNT, value);
  }

  @Override
  public PropertyTypeNode getMaxPastKeyCountNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(SecurityGroupType.MAX_PAST_KEY_COUNT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxPastKeyCount() {
    return getProperty(SecurityGroupType.MAX_PAST_KEY_COUNT).orElse(null);
  }

  @Override
  public void setMaxPastKeyCount(UInteger value) {
    setProperty(SecurityGroupType.MAX_PAST_KEY_COUNT, value);
  }

  @Override
  public UaMethodNode getInvalidateKeysMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "InvalidateKeys",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getForceKeyRotationMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "ForceKeyRotation",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
