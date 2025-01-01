/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.26">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.26</a>
 */
public interface ThreeDOrientationType extends OrientationType {
  /**
   * Get the local value of the A Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the A Node.
   * @throws UaException if an error occurs creating or getting the A Node.
   */
  Double getA() throws UaException;

  /**
   * Set the local value of the A Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the A Node.
   * @throws UaException if an error occurs creating or getting the A Node.
   */
  void setA(Double value) throws UaException;

  /**
   * Read the value of the A Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readA() throws UaException;

  /**
   * Write a new value for the A Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeA(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readA}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readAAsync();

  /**
   * An asynchronous implementation of {@link #writeA}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAAsync(Double value);

  /**
   * Get the A {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the A {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getANode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getANode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getANodeAsync();

  /**
   * Get the local value of the B Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the B Node.
   * @throws UaException if an error occurs creating or getting the B Node.
   */
  Double getB() throws UaException;

  /**
   * Set the local value of the B Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the B Node.
   * @throws UaException if an error occurs creating or getting the B Node.
   */
  void setB(Double value) throws UaException;

  /**
   * Read the value of the B Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readB() throws UaException;

  /**
   * Write a new value for the B Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeB(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readB}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readBAsync();

  /**
   * An asynchronous implementation of {@link #writeB}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeBAsync(Double value);

  /**
   * Get the B {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the B {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getBNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getBNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getBNodeAsync();

  /**
   * Get the local value of the C Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the C Node.
   * @throws UaException if an error occurs creating or getting the C Node.
   */
  Double getC() throws UaException;

  /**
   * Set the local value of the C Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the C Node.
   * @throws UaException if an error occurs creating or getting the C Node.
   */
  void setC(Double value) throws UaException;

  /**
   * Read the value of the C Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readC() throws UaException;

  /**
   * Write a new value for the C Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeC(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readC}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readCAsync();

  /**
   * An asynchronous implementation of {@link #writeC}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCAsync(Double value);

  /**
   * Get the C {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the C {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCNodeAsync();
}
