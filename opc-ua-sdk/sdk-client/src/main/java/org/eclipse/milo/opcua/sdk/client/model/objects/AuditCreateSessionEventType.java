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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.8">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.8</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable String getSecureChannelId() throws UaException;

  /** Sets the existing node's local value. */
  void setSecureChannelId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readSecureChannelId() throws UaException;

  /** Writes the value remotely. */
  void writeSecureChannelId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readSecureChannelIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecureChannelIdAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSecureChannelIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSecureChannelIdNodeAsync();

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
  @Nullable Double getRevisedSessionTimeout() throws UaException;

  /** Sets the existing node's local value. */
  void setRevisedSessionTimeout(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readRevisedSessionTimeout() throws UaException;

  /** Writes the value remotely. */
  void writeRevisedSessionTimeout(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readRevisedSessionTimeoutAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRevisedSessionTimeoutAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRevisedSessionTimeoutNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getRevisedSessionTimeoutNodeAsync();
}
