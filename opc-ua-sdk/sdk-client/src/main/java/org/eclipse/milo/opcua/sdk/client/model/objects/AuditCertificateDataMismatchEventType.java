/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.13">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.13</a>
 */
public interface AuditCertificateDataMismatchEventType extends AuditCertificateEventType {
  QualifiedProperty<String> INVALID_HOSTNAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InvalidHostname",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> INVALID_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InvalidUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /**
   * Get the local value of the InvalidHostname Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the InvalidHostname Node.
   * @throws UaException if an error occurs creating or getting the InvalidHostname Node.
   */
  String getInvalidHostname() throws UaException;

  /**
   * Set the local value of the InvalidHostname Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the InvalidHostname Node.
   * @throws UaException if an error occurs creating or getting the InvalidHostname Node.
   */
  void setInvalidHostname(String value) throws UaException;

  /**
   * Read the value of the InvalidHostname Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readInvalidHostname() throws UaException;

  /**
   * Write a new value for the InvalidHostname Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeInvalidHostname(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readInvalidHostname}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readInvalidHostnameAsync();

  /**
   * An asynchronous implementation of {@link #writeInvalidHostname}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeInvalidHostnameAsync(String value);

  /**
   * Get the InvalidHostname {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the InvalidHostname {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getInvalidHostnameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getInvalidHostnameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getInvalidHostnameNodeAsync();

  /**
   * Get the local value of the InvalidUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the InvalidUri Node.
   * @throws UaException if an error occurs creating or getting the InvalidUri Node.
   */
  String getInvalidUri() throws UaException;

  /**
   * Set the local value of the InvalidUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the InvalidUri Node.
   * @throws UaException if an error occurs creating or getting the InvalidUri Node.
   */
  void setInvalidUri(String value) throws UaException;

  /**
   * Read the value of the InvalidUri Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readInvalidUri() throws UaException;

  /**
   * Write a new value for the InvalidUri Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeInvalidUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readInvalidUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readInvalidUriAsync();

  /**
   * An asynchronous implementation of {@link #writeInvalidUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeInvalidUriAsync(String value);

  /**
   * Get the InvalidUri {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the InvalidUri {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getInvalidUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getInvalidUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getInvalidUriNodeAsync();
}
