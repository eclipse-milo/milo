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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteStateVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteTransitionVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.5">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.5</a>
 */
public interface FiniteStateMachineType extends StateMachineType {
  /**
   * Get the local value of the CurrentState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CurrentState Node.
   * @throws UaException if an error occurs creating or getting the CurrentState Node.
   */
  LocalizedText getCurrentState() throws UaException;

  /**
   * Set the local value of the CurrentState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CurrentState Node.
   * @throws UaException if an error occurs creating or getting the CurrentState Node.
   */
  void setCurrentState(LocalizedText value) throws UaException;

  /**
   * Read the value of the CurrentState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readCurrentState() throws UaException;

  /**
   * Write a new value for the CurrentState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCurrentState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCurrentState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readCurrentStateAsync();

  /**
   * An asynchronous implementation of {@link #writeCurrentState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCurrentStateAsync(LocalizedText value);

  /**
   * Get the CurrentState {@link FiniteStateVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CurrentState {@link FiniteStateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  FiniteStateVariableType getCurrentStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCurrentStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the FiniteStateVariableType Node
   *     or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends FiniteStateVariableType> getCurrentStateNodeAsync();

  /**
   * Get the local value of the LastTransition Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastTransition Node.
   * @throws UaException if an error occurs creating or getting the LastTransition Node.
   */
  LocalizedText getLastTransition() throws UaException;

  /**
   * Set the local value of the LastTransition Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastTransition Node.
   * @throws UaException if an error occurs creating or getting the LastTransition Node.
   */
  void setLastTransition(LocalizedText value) throws UaException;

  /**
   * Read the value of the LastTransition Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readLastTransition() throws UaException;

  /**
   * Write a new value for the LastTransition Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastTransition(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastTransition}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readLastTransitionAsync();

  /**
   * An asynchronous implementation of {@link #writeLastTransition}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastTransitionAsync(LocalizedText value);

  /**
   * Get the LastTransition {@link FiniteTransitionVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastTransition {@link FiniteTransitionVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  FiniteTransitionVariableType getLastTransitionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastTransitionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the FiniteTransitionVariableType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends FiniteTransitionVariableType> getLastTransitionNodeAsync();

  /**
   * Get the local value of the AvailableStates Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AvailableStates Node.
   * @throws UaException if an error occurs creating or getting the AvailableStates Node.
   */
  NodeId[] getAvailableStates() throws UaException;

  /**
   * Set the local value of the AvailableStates Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AvailableStates Node.
   * @throws UaException if an error occurs creating or getting the AvailableStates Node.
   */
  void setAvailableStates(NodeId[] value) throws UaException;

  /**
   * Read the value of the AvailableStates Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId[] readAvailableStates() throws UaException;

  /**
   * Write a new value for the AvailableStates Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAvailableStates(NodeId[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAvailableStates}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId[]> readAvailableStatesAsync();

  /**
   * An asynchronous implementation of {@link #writeAvailableStates}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAvailableStatesAsync(NodeId[] value);

  /**
   * Get the AvailableStates {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AvailableStates {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getAvailableStatesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAvailableStatesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getAvailableStatesNodeAsync();

  /**
   * Get the local value of the AvailableTransitions Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AvailableTransitions Node.
   * @throws UaException if an error occurs creating or getting the AvailableTransitions Node.
   */
  NodeId[] getAvailableTransitions() throws UaException;

  /**
   * Set the local value of the AvailableTransitions Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AvailableTransitions Node.
   * @throws UaException if an error occurs creating or getting the AvailableTransitions Node.
   */
  void setAvailableTransitions(NodeId[] value) throws UaException;

  /**
   * Read the value of the AvailableTransitions Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link NodeId[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId[] readAvailableTransitions() throws UaException;

  /**
   * Write a new value for the AvailableTransitions Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link NodeId[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAvailableTransitions(NodeId[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAvailableTransitions}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId[]> readAvailableTransitionsAsync();

  /**
   * An asynchronous implementation of {@link #writeAvailableTransitions}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAvailableTransitionsAsync(NodeId[] value);

  /**
   * Get the AvailableTransitions {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AvailableTransitions {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getAvailableTransitionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAvailableTransitionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getAvailableTransitionsNodeAsync();
}
