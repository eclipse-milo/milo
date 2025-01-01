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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.SecurityTokenRequestType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.6">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.6</a>
 */
public interface AuditOpenSecureChannelEventType extends AuditChannelEventType {
  QualifiedProperty<ByteString> CLIENT_CERTIFICATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientCertificate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          -1,
          ByteString.class);

  QualifiedProperty<String> CLIENT_CERTIFICATE_THUMBPRINT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientCertificateThumbprint",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<SecurityTokenRequestType> REQUEST_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RequestType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=315"),
          -1,
          SecurityTokenRequestType.class);

  QualifiedProperty<String> SECURITY_POLICY_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityPolicyUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<MessageSecurityMode> SECURITY_MODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityMode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=302"),
          -1,
          MessageSecurityMode.class);

  QualifiedProperty<Double> REQUESTED_LIFETIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RequestedLifetime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<String> CERTIFICATE_ERROR_EVENT_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CertificateErrorEventId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  ByteString getClientCertificate();

  void setClientCertificate(ByteString value);

  PropertyType getClientCertificateNode();

  String getClientCertificateThumbprint();

  void setClientCertificateThumbprint(String value);

  PropertyType getClientCertificateThumbprintNode();

  SecurityTokenRequestType getRequestType();

  void setRequestType(SecurityTokenRequestType value);

  PropertyType getRequestTypeNode();

  String getSecurityPolicyUri();

  void setSecurityPolicyUri(String value);

  PropertyType getSecurityPolicyUriNode();

  MessageSecurityMode getSecurityMode();

  void setSecurityMode(MessageSecurityMode value);

  PropertyType getSecurityModeNode();

  Double getRequestedLifetime();

  void setRequestedLifetime(Double value);

  PropertyType getRequestedLifetimeNode();

  String getCertificateErrorEventId();

  void setCertificateErrorEventId(String value);

  PropertyType getCertificateErrorEventIdNode();
}
