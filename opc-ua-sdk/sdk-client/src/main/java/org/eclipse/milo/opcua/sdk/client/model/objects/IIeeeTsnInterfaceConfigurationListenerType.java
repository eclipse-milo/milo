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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.12">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.12</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIeeeTsnInterfaceConfigurationListenerType
    extends IIeeeTsnInterfaceConfigurationType {
  /** Gets the existing node's local value. */
  @Nullable UInteger getReceiveOffset() throws UaException;

  /** Sets the existing node's local value. */
  void setReceiveOffset(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readReceiveOffset() throws UaException;

  /** Writes the value remotely. */
  void writeReceiveOffset(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readReceiveOffsetAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeReceiveOffsetAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getReceiveOffsetNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getReceiveOffsetNodeAsync();
}
