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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.SecurityTokenRequestType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.6">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.6</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  QualifiedProperty<ByteString> CERTIFICATE_ERROR_EVENT_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CertificateErrorEventId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          -1,
          ByteString.class);

  /** Gets the existing node's local value. */
  @Nullable ByteString getClientCertificate();

  /** Sets the existing node's local value. */
  void setClientCertificate(@Nullable ByteString value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getClientCertificateNode();

  /** Gets the existing node's local value. */
  @Nullable String getClientCertificateThumbprint();

  /** Sets the existing node's local value. */
  void setClientCertificateThumbprint(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getClientCertificateThumbprintNode();

  /** Gets the existing node's local value. */
  @Nullable SecurityTokenRequestType getRequestType();

  /** Sets the existing node's local value. */
  void setRequestType(@Nullable SecurityTokenRequestType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRequestTypeNode();

  /** Gets the existing node's local value. */
  @Nullable String getSecurityPolicyUri();

  /** Sets the existing node's local value. */
  void setSecurityPolicyUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSecurityPolicyUriNode();

  /** Gets the existing node's local value. */
  @Nullable MessageSecurityMode getSecurityMode();

  /** Sets the existing node's local value. */
  void setSecurityMode(@Nullable MessageSecurityMode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSecurityModeNode();

  /** Gets the existing node's local value. */
  @Nullable Double getRequestedLifetime();

  /** Sets the existing node's local value. */
  void setRequestedLifetime(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRequestedLifetimeNode();

  /** Gets the existing node's local value. */
  @Nullable ByteString getCertificateErrorEventId();

  /** Sets the existing node's local value. */
  void setCertificateErrorEventId(@Nullable ByteString value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getCertificateErrorEventIdNode();
}
