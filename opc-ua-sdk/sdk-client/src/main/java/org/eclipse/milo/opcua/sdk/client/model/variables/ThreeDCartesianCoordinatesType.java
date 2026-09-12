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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.24">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.24</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ThreeDCartesianCoordinatesType extends CartesianCoordinatesType {
  /** Gets the existing node's local value. */
  @Nullable Double getX() throws UaException;

  /** Sets the existing node's local value. */
  void setX(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readX() throws UaException;

  /** Writes the value remotely. */
  void writeX(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readXAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeXAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getXNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getXNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getY() throws UaException;

  /** Sets the existing node's local value. */
  void setY(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readY() throws UaException;

  /** Writes the value remotely. */
  void writeY(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readYAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeYAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getYNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getYNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getZ() throws UaException;

  /** Sets the existing node's local value. */
  void setZ(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readZ() throws UaException;

  /** Writes the value remotely. */
  void writeZ(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readZAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeZAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getZNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getZNodeAsync();
}
