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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.6">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.6</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ISrClassType extends BaseInterfaceType {
  /** Gets the existing node's local value. */
  @Nullable UByte getId() throws UaException;

  /** Sets the existing node's local value. */
  void setId(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UByte readId() throws UaException;

  /** Writes the value remotely. */
  void writeId(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UByte> readIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIdAsync(@Nullable UByte value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UByte getPriority() throws UaException;

  /** Sets the existing node's local value. */
  void setPriority(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UByte readPriority() throws UaException;

  /** Writes the value remotely. */
  void writePriority(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UByte> readPriorityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePriorityAsync(@Nullable UByte value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPriorityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPriorityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getVid() throws UaException;

  /** Sets the existing node's local value. */
  void setVid(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readVid() throws UaException;

  /** Writes the value remotely. */
  void writeVid(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readVidAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeVidAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getVidNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getVidNodeAsync();
}
