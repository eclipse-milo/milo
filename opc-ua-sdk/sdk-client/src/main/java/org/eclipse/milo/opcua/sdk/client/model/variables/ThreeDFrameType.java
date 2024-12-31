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
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDCartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDOrientation;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.28">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.28</a>
 */
public interface ThreeDFrameType extends FrameType {
  /**
   * Get the local value of the CartesianCoordinates Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CartesianCoordinates Node.
   * @throws UaException if an error occurs creating or getting the CartesianCoordinates Node.
   */
  ThreeDCartesianCoordinates getCartesianCoordinates() throws UaException;

  /**
   * Set the local value of the CartesianCoordinates Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CartesianCoordinates Node.
   * @throws UaException if an error occurs creating or getting the CartesianCoordinates Node.
   */
  void setCartesianCoordinates(ThreeDCartesianCoordinates value) throws UaException;

  /**
   * Read the value of the CartesianCoordinates Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link ThreeDCartesianCoordinates} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ThreeDCartesianCoordinates readCartesianCoordinates() throws UaException;

  /**
   * Write a new value for the CartesianCoordinates Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link ThreeDCartesianCoordinates} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCartesianCoordinates(ThreeDCartesianCoordinates value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCartesianCoordinates}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ThreeDCartesianCoordinates> readCartesianCoordinatesAsync();

  /**
   * An asynchronous implementation of {@link #writeCartesianCoordinates}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCartesianCoordinatesAsync(ThreeDCartesianCoordinates value);

  /**
   * Get the CartesianCoordinates {@link ThreeDCartesianCoordinatesType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CartesianCoordinates {@link ThreeDCartesianCoordinatesType} Node, or {@code null}
   *     if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  ThreeDCartesianCoordinatesType getCartesianCoordinatesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCartesianCoordinatesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the ThreeDCartesianCoordinatesType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends ThreeDCartesianCoordinatesType> getCartesianCoordinatesNodeAsync();

  /**
   * Get the local value of the Orientation Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Orientation Node.
   * @throws UaException if an error occurs creating or getting the Orientation Node.
   */
  ThreeDOrientation getOrientation() throws UaException;

  /**
   * Set the local value of the Orientation Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Orientation Node.
   * @throws UaException if an error occurs creating or getting the Orientation Node.
   */
  void setOrientation(ThreeDOrientation value) throws UaException;

  /**
   * Read the value of the Orientation Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ThreeDOrientation} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ThreeDOrientation readOrientation() throws UaException;

  /**
   * Write a new value for the Orientation Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ThreeDOrientation} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeOrientation(ThreeDOrientation value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readOrientation}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ThreeDOrientation> readOrientationAsync();

  /**
   * An asynchronous implementation of {@link #writeOrientation}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeOrientationAsync(ThreeDOrientation value);

  /**
   * Get the Orientation {@link ThreeDOrientationType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Orientation {@link ThreeDOrientationType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  ThreeDOrientationType getOrientationNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOrientationNode()}.
   *
   * @return a CompletableFuture that completes successfully with the ThreeDOrientationType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends ThreeDOrientationType> getOrientationNodeAsync();
}
