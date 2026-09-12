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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.5/#5.2.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.5/#5.2.5.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ProgramTransitionEventType extends TransitionEventType {
  /** Gets the existing node's local value. */
  @Nullable Object getIntermediateResult() throws UaException;

  /** Sets the existing node's local value. */
  void setIntermediateResult(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object readIntermediateResult() throws UaException;

  /** Writes the value remotely. */
  void writeIntermediateResult(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object> readIntermediateResultAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIntermediateResultAsync(@Nullable Object value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getIntermediateResultNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getIntermediateResultNodeAsync();
}
