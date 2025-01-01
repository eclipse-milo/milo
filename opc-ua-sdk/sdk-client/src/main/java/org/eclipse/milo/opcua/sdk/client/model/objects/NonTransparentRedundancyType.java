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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.9">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.9</a>
 */
public interface NonTransparentRedundancyType extends ServerRedundancyType {
  QualifiedProperty<String[]> SERVER_URI_ARRAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServerUriArray",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  /**
   * Get the local value of the ServerUriArray Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ServerUriArray Node.
   * @throws UaException if an error occurs creating or getting the ServerUriArray Node.
   */
  String[] getServerUriArray() throws UaException;

  /**
   * Set the local value of the ServerUriArray Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ServerUriArray Node.
   * @throws UaException if an error occurs creating or getting the ServerUriArray Node.
   */
  void setServerUriArray(String[] value) throws UaException;

  /**
   * Read the value of the ServerUriArray Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String[] readServerUriArray() throws UaException;

  /**
   * Write a new value for the ServerUriArray Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeServerUriArray(String[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readServerUriArray}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String[]> readServerUriArrayAsync();

  /**
   * An asynchronous implementation of {@link #writeServerUriArray}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeServerUriArrayAsync(String[] value);

  /**
   * Get the ServerUriArray {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ServerUriArray {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getServerUriArrayNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getServerUriArrayNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getServerUriArrayNodeAsync();
}
