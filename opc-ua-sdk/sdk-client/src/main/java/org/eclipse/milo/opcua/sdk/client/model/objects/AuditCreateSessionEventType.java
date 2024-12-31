/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

  /**
   * Get the local value of the SecureChannelId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SecureChannelId Node.
   * @throws UaException if an error occurs creating or getting the SecureChannelId Node.
   */
  String getSecureChannelId() throws UaException;

  /**
   * Set the local value of the SecureChannelId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SecureChannelId Node.
   * @throws UaException if an error occurs creating or getting the SecureChannelId Node.
   */
  void setSecureChannelId(String value) throws UaException;

  /**
   * Read the value of the SecureChannelId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readSecureChannelId() throws UaException;

  /**
   * Write a new value for the SecureChannelId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSecureChannelId(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSecureChannelId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readSecureChannelIdAsync();

  /**
   * An asynchronous implementation of {@link #writeSecureChannelId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSecureChannelIdAsync(String value);

  /**
   * Get the SecureChannelId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecureChannelId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSecureChannelIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecureChannelIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSecureChannelIdNodeAsync();

  /**
   * Get the local value of the ClientCertificate Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ClientCertificate Node.
   * @throws UaException if an error occurs creating or getting the ClientCertificate Node.
   */
  ByteString getClientCertificate() throws UaException;

  /**
   * Set the local value of the ClientCertificate Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ClientCertificate Node.
   * @throws UaException if an error occurs creating or getting the ClientCertificate Node.
   */
  void setClientCertificate(ByteString value) throws UaException;

  /**
   * Read the value of the ClientCertificate Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ByteString} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ByteString readClientCertificate() throws UaException;

  /**
   * Write a new value for the ClientCertificate Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link ByteString} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeClientCertificate(ByteString value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readClientCertificate}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ByteString> readClientCertificateAsync();

  /**
   * An asynchronous implementation of {@link #writeClientCertificate}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeClientCertificateAsync(ByteString value);

  /**
   * Get the ClientCertificate {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ClientCertificate {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getClientCertificateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getClientCertificateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getClientCertificateNodeAsync();

  /**
   * Get the local value of the ClientCertificateThumbprint Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ClientCertificateThumbprint Node.
   * @throws UaException if an error occurs creating or getting the ClientCertificateThumbprint
   *     Node.
   */
  String getClientCertificateThumbprint() throws UaException;

  /**
   * Set the local value of the ClientCertificateThumbprint Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ClientCertificateThumbprint Node.
   * @throws UaException if an error occurs creating or getting the ClientCertificateThumbprint
   *     Node.
   */
  void setClientCertificateThumbprint(String value) throws UaException;

  /**
   * Read the value of the ClientCertificateThumbprint Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readClientCertificateThumbprint() throws UaException;

  /**
   * Write a new value for the ClientCertificateThumbprint Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeClientCertificateThumbprint(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readClientCertificateThumbprint}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readClientCertificateThumbprintAsync();

  /**
   * An asynchronous implementation of {@link #writeClientCertificateThumbprint}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeClientCertificateThumbprintAsync(String value);

  /**
   * Get the ClientCertificateThumbprint {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ClientCertificateThumbprint {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getClientCertificateThumbprintNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getClientCertificateThumbprintNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getClientCertificateThumbprintNodeAsync();

  /**
   * Get the local value of the RevisedSessionTimeout Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RevisedSessionTimeout Node.
   * @throws UaException if an error occurs creating or getting the RevisedSessionTimeout Node.
   */
  Double getRevisedSessionTimeout() throws UaException;

  /**
   * Set the local value of the RevisedSessionTimeout Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RevisedSessionTimeout Node.
   * @throws UaException if an error occurs creating or getting the RevisedSessionTimeout Node.
   */
  void setRevisedSessionTimeout(Double value) throws UaException;

  /**
   * Read the value of the RevisedSessionTimeout Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readRevisedSessionTimeout() throws UaException;

  /**
   * Write a new value for the RevisedSessionTimeout Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRevisedSessionTimeout(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRevisedSessionTimeout}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readRevisedSessionTimeoutAsync();

  /**
   * An asynchronous implementation of {@link #writeRevisedSessionTimeout}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRevisedSessionTimeoutAsync(Double value);

  /**
   * Get the RevisedSessionTimeout {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RevisedSessionTimeout {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getRevisedSessionTimeoutNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRevisedSessionTimeoutNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getRevisedSessionTimeoutNodeAsync();
}
