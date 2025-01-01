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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

public class PubSubKeyPushTargetTypeNode extends BaseObjectTypeNode
    implements PubSubKeyPushTargetType {
  public PubSubKeyPushTargetTypeNode(
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

  public PubSubKeyPushTargetTypeNode(
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
  public PropertyTypeNode getApplicationUriNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PubSubKeyPushTargetType.APPLICATION_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getApplicationUri() {
    return getProperty(PubSubKeyPushTargetType.APPLICATION_URI).orElse(null);
  }

  @Override
  public void setApplicationUri(String value) {
    setProperty(PubSubKeyPushTargetType.APPLICATION_URI, value);
  }

  @Override
  public PropertyTypeNode getEndpointUrlNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PubSubKeyPushTargetType.ENDPOINT_URL);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getEndpointUrl() {
    return getProperty(PubSubKeyPushTargetType.ENDPOINT_URL).orElse(null);
  }

  @Override
  public void setEndpointUrl(String value) {
    setProperty(PubSubKeyPushTargetType.ENDPOINT_URL, value);
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUriNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PubSubKeyPushTargetType.SECURITY_POLICY_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getSecurityPolicyUri() {
    return getProperty(PubSubKeyPushTargetType.SECURITY_POLICY_URI).orElse(null);
  }

  @Override
  public void setSecurityPolicyUri(String value) {
    setProperty(PubSubKeyPushTargetType.SECURITY_POLICY_URI, value);
  }

  @Override
  public PropertyTypeNode getUserTokenTypeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PubSubKeyPushTargetType.USER_TOKEN_TYPE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UserTokenPolicy getUserTokenType() {
    return getProperty(PubSubKeyPushTargetType.USER_TOKEN_TYPE).orElse(null);
  }

  @Override
  public void setUserTokenType(UserTokenPolicy value) {
    setProperty(PubSubKeyPushTargetType.USER_TOKEN_TYPE, value);
  }

  @Override
  public PropertyTypeNode getRequestedKeyCountNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PubSubKeyPushTargetType.REQUESTED_KEY_COUNT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getRequestedKeyCount() {
    return getProperty(PubSubKeyPushTargetType.REQUESTED_KEY_COUNT).orElse(null);
  }

  @Override
  public void setRequestedKeyCount(UShort value) {
    setProperty(PubSubKeyPushTargetType.REQUESTED_KEY_COUNT, value);
  }

  @Override
  public PropertyTypeNode getRetryIntervalNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PubSubKeyPushTargetType.RETRY_INTERVAL);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getRetryInterval() {
    return getProperty(PubSubKeyPushTargetType.RETRY_INTERVAL).orElse(null);
  }

  @Override
  public void setRetryInterval(Double value) {
    setProperty(PubSubKeyPushTargetType.RETRY_INTERVAL, value);
  }

  @Override
  public PropertyTypeNode getLastPushExecutionTimeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PubSubKeyPushTargetType.LAST_PUSH_EXECUTION_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getLastPushExecutionTime() {
    return getProperty(PubSubKeyPushTargetType.LAST_PUSH_EXECUTION_TIME).orElse(null);
  }

  @Override
  public void setLastPushExecutionTime(DateTime value) {
    setProperty(PubSubKeyPushTargetType.LAST_PUSH_EXECUTION_TIME, value);
  }

  @Override
  public PropertyTypeNode getLastPushErrorTimeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PubSubKeyPushTargetType.LAST_PUSH_ERROR_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getLastPushErrorTime() {
    return getProperty(PubSubKeyPushTargetType.LAST_PUSH_ERROR_TIME).orElse(null);
  }

  @Override
  public void setLastPushErrorTime(DateTime value) {
    setProperty(PubSubKeyPushTargetType.LAST_PUSH_ERROR_TIME, value);
  }

  @Override
  public UaMethodNode getConnectSecurityGroupsMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "ConnectSecurityGroups",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getDisconnectSecurityGroupsMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "DisconnectSecurityGroups",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getTriggerKeyUpdateMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "TriggerKeyUpdate",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
