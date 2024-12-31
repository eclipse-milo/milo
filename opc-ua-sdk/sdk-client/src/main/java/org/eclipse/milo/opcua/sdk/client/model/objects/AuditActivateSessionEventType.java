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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.10">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.10</a>
 */
public interface AuditActivateSessionEventType extends AuditSessionEventType {
  QualifiedProperty<SignedSoftwareCertificate[]> CLIENT_SOFTWARE_CERTIFICATES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientSoftwareCertificates",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=344"),
          1,
          SignedSoftwareCertificate[].class);

  QualifiedProperty<UserIdentityToken> USER_IDENTITY_TOKEN =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UserIdentityToken",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=316"),
          -1,
          UserIdentityToken.class);

  QualifiedProperty<String> SECURE_CHANNEL_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecureChannelId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /**
   * Get the local value of the ClientSoftwareCertificates Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ClientSoftwareCertificates Node.
   * @throws UaException if an error occurs creating or getting the ClientSoftwareCertificates Node.
   */
  SignedSoftwareCertificate[] getClientSoftwareCertificates() throws UaException;

  /**
   * Set the local value of the ClientSoftwareCertificates Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ClientSoftwareCertificates Node.
   * @throws UaException if an error occurs creating or getting the ClientSoftwareCertificates Node.
   */
  void setClientSoftwareCertificates(SignedSoftwareCertificate[] value) throws UaException;

  /**
   * Read the value of the ClientSoftwareCertificates Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link SignedSoftwareCertificate[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  SignedSoftwareCertificate[] readClientSoftwareCertificates() throws UaException;

  /**
   * Write a new value for the ClientSoftwareCertificates Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link SignedSoftwareCertificate[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeClientSoftwareCertificates(SignedSoftwareCertificate[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readClientSoftwareCertificates}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends SignedSoftwareCertificate[]> readClientSoftwareCertificatesAsync();

  /**
   * An asynchronous implementation of {@link #writeClientSoftwareCertificates}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeClientSoftwareCertificatesAsync(
      SignedSoftwareCertificate[] value);

  /**
   * Get the ClientSoftwareCertificates {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ClientSoftwareCertificates {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getClientSoftwareCertificatesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getClientSoftwareCertificatesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getClientSoftwareCertificatesNodeAsync();

  /**
   * Get the local value of the UserIdentityToken Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the UserIdentityToken Node.
   * @throws UaException if an error occurs creating or getting the UserIdentityToken Node.
   */
  UserIdentityToken getUserIdentityToken() throws UaException;

  /**
   * Set the local value of the UserIdentityToken Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the UserIdentityToken Node.
   * @throws UaException if an error occurs creating or getting the UserIdentityToken Node.
   */
  void setUserIdentityToken(UserIdentityToken value) throws UaException;

  /**
   * Read the value of the UserIdentityToken Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UserIdentityToken} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UserIdentityToken readUserIdentityToken() throws UaException;

  /**
   * Write a new value for the UserIdentityToken Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UserIdentityToken} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeUserIdentityToken(UserIdentityToken value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readUserIdentityToken}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UserIdentityToken> readUserIdentityTokenAsync();

  /**
   * An asynchronous implementation of {@link #writeUserIdentityToken}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeUserIdentityTokenAsync(UserIdentityToken value);

  /**
   * Get the UserIdentityToken {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UserIdentityToken {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getUserIdentityTokenNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUserIdentityTokenNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getUserIdentityTokenNodeAsync();

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
}
