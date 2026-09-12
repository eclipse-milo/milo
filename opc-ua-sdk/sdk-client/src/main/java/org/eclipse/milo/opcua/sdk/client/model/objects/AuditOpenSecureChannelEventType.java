/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
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
  @Nullable ByteString getClientCertificate() throws UaException;

  /** Sets the existing node's local value. */
  void setClientCertificate(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ByteString readClientCertificate() throws UaException;

  /** Writes the value remotely. */
  void writeClientCertificate(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ByteString> readClientCertificateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeClientCertificateAsync(@Nullable ByteString value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getClientCertificateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getClientCertificateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getClientCertificateThumbprint() throws UaException;

  /** Sets the existing node's local value. */
  void setClientCertificateThumbprint(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readClientCertificateThumbprint() throws UaException;

  /** Writes the value remotely. */
  void writeClientCertificateThumbprint(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readClientCertificateThumbprintAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeClientCertificateThumbprintAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getClientCertificateThumbprintNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getClientCertificateThumbprintNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable SecurityTokenRequestType getRequestType() throws UaException;

  /** Sets the existing node's local value. */
  void setRequestType(@Nullable SecurityTokenRequestType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable SecurityTokenRequestType readRequestType() throws UaException;

  /** Writes the value remotely. */
  void writeRequestType(@Nullable SecurityTokenRequestType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable SecurityTokenRequestType> readRequestTypeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRequestTypeAsync(@Nullable SecurityTokenRequestType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRequestTypeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getRequestTypeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getSecurityPolicyUri() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityPolicyUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readSecurityPolicyUri() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityPolicyUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readSecurityPolicyUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityPolicyUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSecurityPolicyUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSecurityPolicyUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable MessageSecurityMode getSecurityMode() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityMode(@Nullable MessageSecurityMode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable MessageSecurityMode readSecurityMode() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityMode(@Nullable MessageSecurityMode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable MessageSecurityMode> readSecurityModeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityModeAsync(@Nullable MessageSecurityMode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSecurityModeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSecurityModeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getRequestedLifetime() throws UaException;

  /** Sets the existing node's local value. */
  void setRequestedLifetime(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readRequestedLifetime() throws UaException;

  /** Writes the value remotely. */
  void writeRequestedLifetime(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readRequestedLifetimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRequestedLifetimeAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRequestedLifetimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getRequestedLifetimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ByteString getCertificateErrorEventId() throws UaException;

  /** Sets the existing node's local value. */
  void setCertificateErrorEventId(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ByteString readCertificateErrorEventId() throws UaException;

  /** Writes the value remotely. */
  void writeCertificateErrorEventId(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ByteString> readCertificateErrorEventIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCertificateErrorEventIdAsync(@Nullable ByteString value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getCertificateErrorEventIdNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getCertificateErrorEventIdNodeAsync();
}
