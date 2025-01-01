/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.StateVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.TransitionVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.16">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.16</a>
 */
public interface TransitionEventType extends BaseEventType {
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
   * Get the Transition {@link TransitionVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Transition {@link TransitionVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionVariableType getTransitionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransitionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionVariableType> getTransitionNodeAsync();

  /**
   * Get the local value of the FromState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the FromState Node.
   * @throws UaException if an error occurs creating or getting the FromState Node.
   */
  LocalizedText getFromState() throws UaException;

  /**
   * Set the local value of the FromState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the FromState Node.
   * @throws UaException if an error occurs creating or getting the FromState Node.
   */
  void setFromState(LocalizedText value) throws UaException;

  /**
   * Read the value of the FromState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readFromState() throws UaException;

  /**
   * Write a new value for the FromState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeFromState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readFromState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readFromStateAsync();

  /**
   * An asynchronous implementation of {@link #writeFromState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeFromStateAsync(LocalizedText value);

  /**
   * Get the FromState {@link StateVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the FromState {@link StateVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateVariableType getFromStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getFromStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateVariableType> getFromStateNodeAsync();

  /**
   * Get the local value of the ToState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ToState Node.
   * @throws UaException if an error occurs creating or getting the ToState Node.
   */
  LocalizedText getToState() throws UaException;

  /**
   * Set the local value of the ToState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ToState Node.
   * @throws UaException if an error occurs creating or getting the ToState Node.
   */
  void setToState(LocalizedText value) throws UaException;

  /**
   * Read the value of the ToState Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readToState() throws UaException;

  /**
   * Write a new value for the ToState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeToState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readToState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readToStateAsync();

  /**
   * An asynchronous implementation of {@link #writeToState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeToStateAsync(LocalizedText value);

  /**
   * Get the ToState {@link StateVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ToState {@link StateVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateVariableType getToStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getToStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateVariableType> getToStateNodeAsync();
}
