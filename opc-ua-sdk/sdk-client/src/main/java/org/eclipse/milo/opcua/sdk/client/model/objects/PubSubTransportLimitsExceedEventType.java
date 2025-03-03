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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.13/#9.1.13.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.13/#9.1.13.2</a>
 */
public interface PubSubTransportLimitsExceedEventType extends PubSubStatusEventType {
  QualifiedProperty<UInteger> ACTUAL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Actual",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAXIMUM =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Maximum",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  /**
   * Get the local value of the Actual Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Actual Node.
   * @throws UaException if an error occurs creating or getting the Actual Node.
   */
  UInteger getActual() throws UaException;

  /**
   * Set the local value of the Actual Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Actual Node.
   * @throws UaException if an error occurs creating or getting the Actual Node.
   */
  void setActual(UInteger value) throws UaException;

  /**
   * Read the value of the Actual Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readActual() throws UaException;

  /**
   * Write a new value for the Actual Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeActual(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readActual}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readActualAsync();

  /**
   * An asynchronous implementation of {@link #writeActual}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeActualAsync(UInteger value);

  /**
   * Get the Actual {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Actual {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getActualNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getActualNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getActualNodeAsync();

  /**
   * Get the local value of the Maximum Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Maximum Node.
   * @throws UaException if an error occurs creating or getting the Maximum Node.
   */
  UInteger getMaximum() throws UaException;

  /**
   * Set the local value of the Maximum Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Maximum Node.
   * @throws UaException if an error occurs creating or getting the Maximum Node.
   */
  void setMaximum(UInteger value) throws UaException;

  /**
   * Read the value of the Maximum Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaximum() throws UaException;

  /**
   * Write a new value for the Maximum Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaximum(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaximum}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaximumAsync();

  /**
   * An asynchronous implementation of {@link #writeMaximum}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaximumAsync(UInteger value);

  /**
   * Get the Maximum {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Maximum {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaximumNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaximumNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaximumNodeAsync();
}
