/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.28">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.28</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ThreeDFrameType extends FrameType {
  /** Gets the existing node's local value. */
  @Nullable ThreeDCartesianCoordinates getCartesianCoordinates() throws UaException;

  /** Sets the existing node's local value. */
  void setCartesianCoordinates(@Nullable ThreeDCartesianCoordinates value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ThreeDCartesianCoordinates readCartesianCoordinates() throws UaException;

  /** Writes the value remotely. */
  void writeCartesianCoordinates(@Nullable ThreeDCartesianCoordinates value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ThreeDCartesianCoordinates> readCartesianCoordinatesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCartesianCoordinatesAsync(
      @Nullable ThreeDCartesianCoordinates value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ThreeDCartesianCoordinatesType getCartesianCoordinatesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends ThreeDCartesianCoordinatesType> getCartesianCoordinatesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ThreeDOrientation getOrientation() throws UaException;

  /** Sets the existing node's local value. */
  void setOrientation(@Nullable ThreeDOrientation value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ThreeDOrientation readOrientation() throws UaException;

  /** Writes the value remotely. */
  void writeOrientation(@Nullable ThreeDOrientation value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ThreeDOrientation> readOrientationAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOrientationAsync(@Nullable ThreeDOrientation value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ThreeDOrientationType getOrientationNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends ThreeDOrientationType> getOrientationNodeAsync();
}
