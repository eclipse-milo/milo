/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

public interface ProgramTransitionAuditEventType extends AuditUpdateStateEventType {
  /**
   * Get the local value of the Transition Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Transition Node.
   * @throws UaException if an error occurs creating or getting the Transition Node.
   */
  LocalizedText getTransition() throws UaException;

  /**
   * Set the local value of the Transition Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Transition Node.
   * @throws UaException if an error occurs creating or getting the Transition Node.
   */
  void setTransition(LocalizedText value) throws UaException;

  /**
   * Read the value of the Transition Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readTransition() throws UaException;

  /**
   * Write a new value for the Transition Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTransition(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTransition}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readTransitionAsync();

  /**
   * An asynchronous implementation of {@link #writeTransition}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTransitionAsync(LocalizedText value);

  /**
   * Get the Transition {@link FiniteTransitionVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Transition {@link FiniteTransitionVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  FiniteTransitionVariableType getTransitionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransitionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the FiniteTransitionVariableType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends FiniteTransitionVariableType> getTransitionNodeAsync();
}
