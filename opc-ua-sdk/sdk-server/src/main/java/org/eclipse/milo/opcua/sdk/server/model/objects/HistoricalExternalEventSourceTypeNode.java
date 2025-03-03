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
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

public class HistoricalExternalEventSourceTypeNode extends BaseObjectTypeNode
    implements HistoricalExternalEventSourceType {
  public HistoricalExternalEventSourceTypeNode(
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

  public HistoricalExternalEventSourceTypeNode(
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
  public PropertyTypeNode getServerNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(HistoricalExternalEventSourceType.SERVER);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getServer() {
    return getProperty(HistoricalExternalEventSourceType.SERVER).orElse(null);
  }

  @Override
  public void setServer(String value) {
    setProperty(HistoricalExternalEventSourceType.SERVER, value);
  }

  @Override
  public PropertyTypeNode getEndpointUrlNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalExternalEventSourceType.ENDPOINT_URL);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getEndpointUrl() {
    return getProperty(HistoricalExternalEventSourceType.ENDPOINT_URL).orElse(null);
  }

  @Override
  public void setEndpointUrl(String value) {
    setProperty(HistoricalExternalEventSourceType.ENDPOINT_URL, value);
  }

  @Override
  public PropertyTypeNode getSecurityModeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalExternalEventSourceType.SECURITY_MODE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public MessageSecurityMode getSecurityMode() {
    return getProperty(HistoricalExternalEventSourceType.SECURITY_MODE).orElse(null);
  }

  @Override
  public void setSecurityMode(MessageSecurityMode value) {
    setProperty(HistoricalExternalEventSourceType.SECURITY_MODE, value);
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUriNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalExternalEventSourceType.SECURITY_POLICY_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getSecurityPolicyUri() {
    return getProperty(HistoricalExternalEventSourceType.SECURITY_POLICY_URI).orElse(null);
  }

  @Override
  public void setSecurityPolicyUri(String value) {
    setProperty(HistoricalExternalEventSourceType.SECURITY_POLICY_URI, value);
  }

  @Override
  public PropertyTypeNode getIdentityTokenPolicyNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalExternalEventSourceType.IDENTITY_TOKEN_POLICY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UserTokenPolicy getIdentityTokenPolicy() {
    return getProperty(HistoricalExternalEventSourceType.IDENTITY_TOKEN_POLICY).orElse(null);
  }

  @Override
  public void setIdentityTokenPolicy(UserTokenPolicy value) {
    setProperty(HistoricalExternalEventSourceType.IDENTITY_TOKEN_POLICY, value);
  }

  @Override
  public PropertyTypeNode getTransportProfileUriNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalExternalEventSourceType.TRANSPORT_PROFILE_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getTransportProfileUri() {
    return getProperty(HistoricalExternalEventSourceType.TRANSPORT_PROFILE_URI).orElse(null);
  }

  @Override
  public void setTransportProfileUri(String value) {
    setProperty(HistoricalExternalEventSourceType.TRANSPORT_PROFILE_URI, value);
  }

  @Override
  public PropertyTypeNode getHistoricalEventFilterNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalExternalEventSourceType.HISTORICAL_EVENT_FILTER);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public EventFilter getHistoricalEventFilter() {
    return getProperty(HistoricalExternalEventSourceType.HISTORICAL_EVENT_FILTER).orElse(null);
  }

  @Override
  public void setHistoricalEventFilter(EventFilter value) {
    setProperty(HistoricalExternalEventSourceType.HISTORICAL_EVENT_FILTER, value);
  }
}
