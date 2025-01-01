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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.1</a>
 */
public interface TemporaryFileTransferType extends BaseObjectType {
  QualifiedProperty<Double> CLIENT_PROCESSING_TIMEOUT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientProcessingTimeout",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /**
   * Get the local value of the ClientProcessingTimeout Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ClientProcessingTimeout Node.
   * @throws UaException if an error occurs creating or getting the ClientProcessingTimeout Node.
   */
  Double getClientProcessingTimeout() throws UaException;

  /**
   * Set the local value of the ClientProcessingTimeout Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ClientProcessingTimeout Node.
   * @throws UaException if an error occurs creating or getting the ClientProcessingTimeout Node.
   */
  void setClientProcessingTimeout(Double value) throws UaException;

  /**
   * Read the value of the ClientProcessingTimeout Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readClientProcessingTimeout() throws UaException;

  /**
   * Write a new value for the ClientProcessingTimeout Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeClientProcessingTimeout(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readClientProcessingTimeout}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readClientProcessingTimeoutAsync();

  /**
   * An asynchronous implementation of {@link #writeClientProcessingTimeout}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeClientProcessingTimeoutAsync(Double value);

  /**
   * Get the ClientProcessingTimeout {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ClientProcessingTimeout {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getClientProcessingTimeoutNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getClientProcessingTimeoutNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getClientProcessingTimeoutNodeAsync();
}
