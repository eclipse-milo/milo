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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.14">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.14</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIeeeTsnVlanTagType extends BaseInterfaceType {
  /** Gets the existing node's local value. */
  @Nullable UShort getVlanId() throws UaException;

  /** Sets the existing node's local value. */
  void setVlanId(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readVlanId() throws UaException;

  /** Writes the value remotely. */
  void writeVlanId(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readVlanIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeVlanIdAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getVlanIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getVlanIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UByte getPriorityCodePoint() throws UaException;

  /** Sets the existing node's local value. */
  void setPriorityCodePoint(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UByte readPriorityCodePoint() throws UaException;

  /** Writes the value remotely. */
  void writePriorityCodePoint(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UByte> readPriorityCodePointAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePriorityCodePointAsync(@Nullable UByte value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPriorityCodePointNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPriorityCodePointNodeAsync();
}
