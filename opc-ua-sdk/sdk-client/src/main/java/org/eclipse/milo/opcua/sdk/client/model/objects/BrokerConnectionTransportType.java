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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.1</a>
 */
public interface BrokerConnectionTransportType extends ConnectionTransportType {
  QualifiedProperty<String> RESOURCE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ResourceUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> AUTHENTICATION_PROFILE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AuthenticationProfileUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /**
   * Get the local value of the ResourceUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ResourceUri Node.
   * @throws UaException if an error occurs creating or getting the ResourceUri Node.
   */
  String getResourceUri() throws UaException;

  /**
   * Set the local value of the ResourceUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ResourceUri Node.
   * @throws UaException if an error occurs creating or getting the ResourceUri Node.
   */
  void setResourceUri(String value) throws UaException;

  /**
   * Read the value of the ResourceUri Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readResourceUri() throws UaException;

  /**
   * Write a new value for the ResourceUri Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeResourceUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readResourceUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readResourceUriAsync();

  /**
   * An asynchronous implementation of {@link #writeResourceUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeResourceUriAsync(String value);

  /**
   * Get the ResourceUri {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ResourceUri {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getResourceUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getResourceUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getResourceUriNodeAsync();

  /**
   * Get the local value of the AuthenticationProfileUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AuthenticationProfileUri Node.
   * @throws UaException if an error occurs creating or getting the AuthenticationProfileUri Node.
   */
  String getAuthenticationProfileUri() throws UaException;

  /**
   * Set the local value of the AuthenticationProfileUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AuthenticationProfileUri Node.
   * @throws UaException if an error occurs creating or getting the AuthenticationProfileUri Node.
   */
  void setAuthenticationProfileUri(String value) throws UaException;

  /**
   * Read the value of the AuthenticationProfileUri Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readAuthenticationProfileUri() throws UaException;

  /**
   * Write a new value for the AuthenticationProfileUri Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAuthenticationProfileUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAuthenticationProfileUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readAuthenticationProfileUriAsync();

  /**
   * An asynchronous implementation of {@link #writeAuthenticationProfileUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAuthenticationProfileUriAsync(String value);

  /**
   * Get the AuthenticationProfileUri {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AuthenticationProfileUri {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getAuthenticationProfileUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAuthenticationProfileUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getAuthenticationProfileUriNodeAsync();
}
