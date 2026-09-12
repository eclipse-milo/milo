/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UnsignedRationalNumber;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.8">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.8</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIeeeBaseTsnTrafficSpecificationType extends BaseInterfaceType {
  /** Gets the existing node's local value. */
  @Nullable UShort getMaxIntervalFrames() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxIntervalFrames(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readMaxIntervalFrames() throws UaException;

  /** Writes the value remotely. */
  void writeMaxIntervalFrames(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readMaxIntervalFramesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxIntervalFramesAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxIntervalFramesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaxIntervalFramesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxFrameSize() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxFrameSize(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxFrameSize() throws UaException;

  /** Writes the value remotely. */
  void writeMaxFrameSize(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxFrameSizeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxFrameSizeAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxFrameSizeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaxFrameSizeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UnsignedRationalNumber getInterval() throws UaException;

  /** Sets the existing node's local value. */
  void setInterval(@Nullable UnsignedRationalNumber value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UnsignedRationalNumber readInterval() throws UaException;

  /** Writes the value remotely. */
  void writeInterval(@Nullable UnsignedRationalNumber value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UnsignedRationalNumber> readIntervalAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIntervalAsync(@Nullable UnsignedRationalNumber value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getIntervalNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getIntervalNodeAsync();
}
