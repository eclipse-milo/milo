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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.24">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.24</a>
 */
public interface ThreeDCartesianCoordinatesType extends CartesianCoordinatesType {
  /**
   * Get the local value of the X Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the X Node.
   * @throws UaException if an error occurs creating or getting the X Node.
   */
  Double getX() throws UaException;

  /**
   * Set the local value of the X Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the X Node.
   * @throws UaException if an error occurs creating or getting the X Node.
   */
  void setX(Double value) throws UaException;

  /**
   * Read the value of the X Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readX() throws UaException;

  /**
   * Write a new value for the X Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeX(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readX}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readXAsync();

  /**
   * An asynchronous implementation of {@link #writeX}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeXAsync(Double value);

  /**
   * Get the X {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the X {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getXNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getXNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getXNodeAsync();

  /**
   * Get the local value of the Y Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Y Node.
   * @throws UaException if an error occurs creating or getting the Y Node.
   */
  Double getY() throws UaException;

  /**
   * Set the local value of the Y Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Y Node.
   * @throws UaException if an error occurs creating or getting the Y Node.
   */
  void setY(Double value) throws UaException;

  /**
   * Read the value of the Y Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readY() throws UaException;

  /**
   * Write a new value for the Y Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeY(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readY}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readYAsync();

  /**
   * An asynchronous implementation of {@link #writeY}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeYAsync(Double value);

  /**
   * Get the Y {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Y {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getYNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getYNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getYNodeAsync();

  /**
   * Get the local value of the Z Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Z Node.
   * @throws UaException if an error occurs creating or getting the Z Node.
   */
  Double getZ() throws UaException;

  /**
   * Set the local value of the Z Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Z Node.
   * @throws UaException if an error occurs creating or getting the Z Node.
   */
  void setZ(Double value) throws UaException;

  /**
   * Read the value of the Z Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readZ() throws UaException;

  /**
   * Write a new value for the Z Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeZ(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readZ}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readZAsync();

  /**
   * An asynchronous implementation of {@link #writeZ}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeZAsync(Double value);

  /**
   * Get the Z {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Z {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getZNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getZNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getZNodeAsync();
}
