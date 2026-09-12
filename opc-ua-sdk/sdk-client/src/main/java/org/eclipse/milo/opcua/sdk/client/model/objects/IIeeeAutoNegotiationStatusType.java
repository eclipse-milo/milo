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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NegotiationStatus;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIeeeAutoNegotiationStatusType extends BaseInterfaceType {
  /** Gets the existing node's local value. */
  @Nullable NegotiationStatus getNegotiationStatus() throws UaException;

  /** Sets the existing node's local value. */
  void setNegotiationStatus(@Nullable NegotiationStatus value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NegotiationStatus readNegotiationStatus() throws UaException;

  /** Writes the value remotely. */
  void writeNegotiationStatus(@Nullable NegotiationStatus value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NegotiationStatus> readNegotiationStatusAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNegotiationStatusAsync(@Nullable NegotiationStatus value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getNegotiationStatusNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getNegotiationStatusNodeAsync();
}
