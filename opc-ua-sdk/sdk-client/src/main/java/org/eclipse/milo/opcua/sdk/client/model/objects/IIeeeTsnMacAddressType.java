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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.13">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.13</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIeeeTsnMacAddressType extends BaseInterfaceType {
  /** Gets the existing node's local value. */
  @Nullable UByte @Nullable [] getDestinationAddress() throws UaException;

  /** Sets the existing node's local value. */
  void setDestinationAddress(@Nullable UByte @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UByte @Nullable [] readDestinationAddress() throws UaException;

  /** Writes the value remotely. */
  void writeDestinationAddress(@Nullable UByte @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UByte @Nullable []> readDestinationAddressAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDestinationAddressAsync(@Nullable UByte @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDestinationAddressNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDestinationAddressNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UByte @Nullable [] getSourceAddress() throws UaException;

  /** Sets the existing node's local value. */
  void setSourceAddress(@Nullable UByte @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UByte @Nullable [] readSourceAddress() throws UaException;

  /** Writes the value remotely. */
  void writeSourceAddress(@Nullable UByte @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UByte @Nullable []> readSourceAddressAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSourceAddressAsync(@Nullable UByte @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getSourceAddressNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getSourceAddressNodeAsync();
}
