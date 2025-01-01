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
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointType;
import org.eclipse.milo.opcua.stack.core.types.structured.IdentityMappingRuleType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class RoleTypeNode extends BaseObjectTypeNode implements RoleType {
  public RoleTypeNode(
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

  public RoleTypeNode(
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
  public PropertyTypeNode getIdentitiesNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(RoleType.IDENTITIES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public IdentityMappingRuleType[] getIdentities() {
    return getProperty(RoleType.IDENTITIES).orElse(null);
  }

  @Override
  public void setIdentities(IdentityMappingRuleType[] value) {
    setProperty(RoleType.IDENTITIES, value);
  }

  @Override
  public PropertyTypeNode getApplicationsExcludeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(RoleType.APPLICATIONS_EXCLUDE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getApplicationsExclude() {
    return getProperty(RoleType.APPLICATIONS_EXCLUDE).orElse(null);
  }

  @Override
  public void setApplicationsExclude(Boolean value) {
    setProperty(RoleType.APPLICATIONS_EXCLUDE, value);
  }

  @Override
  public PropertyTypeNode getApplicationsNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(RoleType.APPLICATIONS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String[] getApplications() {
    return getProperty(RoleType.APPLICATIONS).orElse(null);
  }

  @Override
  public void setApplications(String[] value) {
    setProperty(RoleType.APPLICATIONS, value);
  }

  @Override
  public PropertyTypeNode getEndpointsExcludeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(RoleType.ENDPOINTS_EXCLUDE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getEndpointsExclude() {
    return getProperty(RoleType.ENDPOINTS_EXCLUDE).orElse(null);
  }

  @Override
  public void setEndpointsExclude(Boolean value) {
    setProperty(RoleType.ENDPOINTS_EXCLUDE, value);
  }

  @Override
  public PropertyTypeNode getEndpointsNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(RoleType.ENDPOINTS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public EndpointType[] getEndpoints() {
    return getProperty(RoleType.ENDPOINTS).orElse(null);
  }

  @Override
  public void setEndpoints(EndpointType[] value) {
    setProperty(RoleType.ENDPOINTS, value);
  }

  @Override
  public PropertyTypeNode getCustomConfigurationNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(RoleType.CUSTOM_CONFIGURATION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getCustomConfiguration() {
    return getProperty(RoleType.CUSTOM_CONFIGURATION).orElse(null);
  }

  @Override
  public void setCustomConfiguration(Boolean value) {
    setProperty(RoleType.CUSTOM_CONFIGURATION, value);
  }

  @Override
  public UaMethodNode getAddIdentityMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "AddIdentity",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getRemoveIdentityMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "RemoveIdentity",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getAddApplicationMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "AddApplication",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getRemoveApplicationMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "RemoveApplication",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getAddEndpointMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "AddEndpoint",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getRemoveEndpointMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "RemoveEndpoint",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
