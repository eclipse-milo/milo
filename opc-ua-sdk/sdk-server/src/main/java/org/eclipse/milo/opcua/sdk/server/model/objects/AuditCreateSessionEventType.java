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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.8">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.8</a>
 */
public interface AuditCreateSessionEventType extends AuditSessionEventType {
  QualifiedProperty<String> SECURE_CHANNEL_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecureChannelId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

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

  QualifiedProperty<Double> REVISED_SESSION_TIMEOUT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RevisedSessionTimeout",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  String getSecureChannelId();

  void setSecureChannelId(String value);

  PropertyType getSecureChannelIdNode();

  ByteString getClientCertificate();

  void setClientCertificate(ByteString value);

  PropertyType getClientCertificateNode();

  String getClientCertificateThumbprint();

  void setClientCertificateThumbprint(String value);

  PropertyType getClientCertificateThumbprintNode();

  Double getRevisedSessionTimeout();

  void setRevisedSessionTimeout(Double value);

  PropertyType getRevisedSessionTimeoutNode();
}
