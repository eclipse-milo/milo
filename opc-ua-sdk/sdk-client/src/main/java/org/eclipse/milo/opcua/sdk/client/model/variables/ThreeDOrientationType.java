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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.26">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.26</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ThreeDOrientationType extends OrientationType {
  /** Gets the existing node's local value. */
  @Nullable Double getA() throws UaException;

  /** Sets the existing node's local value. */
  void setA(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readA() throws UaException;

  /** Writes the value remotely. */
  void writeA(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readAAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getANode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getANodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getB() throws UaException;

  /** Sets the existing node's local value. */
  void setB(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readB() throws UaException;

  /** Writes the value remotely. */
  void writeB(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readBAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeBAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getBNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getBNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getC() throws UaException;

  /** Sets the existing node's local value. */
  void setC(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readC() throws UaException;

  /** Writes the value remotely. */
  void writeC(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readCAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCNodeAsync();
}
