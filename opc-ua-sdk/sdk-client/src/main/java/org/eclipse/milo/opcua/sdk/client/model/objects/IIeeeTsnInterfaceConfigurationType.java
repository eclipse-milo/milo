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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.10">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.10</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIeeeTsnInterfaceConfigurationType extends BaseInterfaceType {
  /** Gets the existing node's local value. */
  @Nullable String getMacAddress() throws UaException;

  /** Sets the existing node's local value. */
  void setMacAddress(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readMacAddress() throws UaException;

  /** Writes the value remotely. */
  void writeMacAddress(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readMacAddressAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMacAddressAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMacAddressNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMacAddressNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getInterfaceName() throws UaException;

  /** Sets the existing node's local value. */
  void setInterfaceName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readInterfaceName() throws UaException;

  /** Writes the value remotely. */
  void writeInterfaceName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readInterfaceNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeInterfaceNameAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getInterfaceNameNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getInterfaceNameNodeAsync();
}
