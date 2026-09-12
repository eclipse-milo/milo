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
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteTransitionVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ProgramTransitionAuditEventType extends AuditUpdateStateEventType {
  /** Gets the existing node's local value. */
  @Nullable LocalizedText getTransition() throws UaException;

  /** Sets the existing node's local value. */
  void setTransition(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readTransition() throws UaException;

  /** Writes the value remotely. */
  void writeTransition(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readTransitionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTransitionAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FiniteTransitionVariableType getTransitionNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends FiniteTransitionVariableType> getTransitionNodeAsync();
}
