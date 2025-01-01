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
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.SecurityTokenRequestType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AuditOpenSecureChannelEventTypeNode extends AuditChannelEventTypeNode
    implements AuditOpenSecureChannelEventType {
  public AuditOpenSecureChannelEventTypeNode(
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

  public AuditOpenSecureChannelEventTypeNode(
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
  public PropertyTypeNode getClientCertificateNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditOpenSecureChannelEventType.CLIENT_CERTIFICATE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public ByteString getClientCertificate() {
    return getProperty(AuditOpenSecureChannelEventType.CLIENT_CERTIFICATE).orElse(null);
  }

  @Override
  public void setClientCertificate(ByteString value) {
    setProperty(AuditOpenSecureChannelEventType.CLIENT_CERTIFICATE, value);
  }

  @Override
  public PropertyTypeNode getClientCertificateThumbprintNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditOpenSecureChannelEventType.CLIENT_CERTIFICATE_THUMBPRINT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getClientCertificateThumbprint() {
    return getProperty(AuditOpenSecureChannelEventType.CLIENT_CERTIFICATE_THUMBPRINT).orElse(null);
  }

  @Override
  public void setClientCertificateThumbprint(String value) {
    setProperty(AuditOpenSecureChannelEventType.CLIENT_CERTIFICATE_THUMBPRINT, value);
  }

  @Override
  public PropertyTypeNode getRequestTypeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditOpenSecureChannelEventType.REQUEST_TYPE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public SecurityTokenRequestType getRequestType() {
    return getProperty(AuditOpenSecureChannelEventType.REQUEST_TYPE).orElse(null);
  }

  @Override
  public void setRequestType(SecurityTokenRequestType value) {
    setProperty(AuditOpenSecureChannelEventType.REQUEST_TYPE, value);
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUriNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditOpenSecureChannelEventType.SECURITY_POLICY_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getSecurityPolicyUri() {
    return getProperty(AuditOpenSecureChannelEventType.SECURITY_POLICY_URI).orElse(null);
  }

  @Override
  public void setSecurityPolicyUri(String value) {
    setProperty(AuditOpenSecureChannelEventType.SECURITY_POLICY_URI, value);
  }

  @Override
  public PropertyTypeNode getSecurityModeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditOpenSecureChannelEventType.SECURITY_MODE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public MessageSecurityMode getSecurityMode() {
    return getProperty(AuditOpenSecureChannelEventType.SECURITY_MODE).orElse(null);
  }

  @Override
  public void setSecurityMode(MessageSecurityMode value) {
    setProperty(AuditOpenSecureChannelEventType.SECURITY_MODE, value);
  }

  @Override
  public PropertyTypeNode getRequestedLifetimeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditOpenSecureChannelEventType.REQUESTED_LIFETIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getRequestedLifetime() {
    return getProperty(AuditOpenSecureChannelEventType.REQUESTED_LIFETIME).orElse(null);
  }

  @Override
  public void setRequestedLifetime(Double value) {
    setProperty(AuditOpenSecureChannelEventType.REQUESTED_LIFETIME, value);
  }

  @Override
  public PropertyTypeNode getCertificateErrorEventIdNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditOpenSecureChannelEventType.CERTIFICATE_ERROR_EVENT_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getCertificateErrorEventId() {
    return getProperty(AuditOpenSecureChannelEventType.CERTIFICATE_ERROR_EVENT_ID).orElse(null);
  }

  @Override
  public void setCertificateErrorEventId(String value) {
    setProperty(AuditOpenSecureChannelEventType.CERTIFICATE_ERROR_EVENT_ID, value);
  }
}
