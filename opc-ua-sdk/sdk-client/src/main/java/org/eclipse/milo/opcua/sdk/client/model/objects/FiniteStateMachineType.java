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
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteStateVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteTransitionVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.5">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.5</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface FiniteStateMachineType extends StateMachineType {
  /** Gets the existing node's local value. */
  @Nullable LocalizedText getCurrentState() throws UaException;

  /** Sets the existing node's local value. */
  void setCurrentState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readCurrentState() throws UaException;

  /** Writes the value remotely. */
  void writeCurrentState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readCurrentStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCurrentStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FiniteStateVariableType getCurrentStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends FiniteStateVariableType> getCurrentStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getLastTransition() throws UaException;

  /** Sets the existing node's local value. */
  void setLastTransition(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readLastTransition() throws UaException;

  /** Writes the value remotely. */
  void writeLastTransition(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readLastTransitionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastTransitionAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable FiniteTransitionVariableType getLastTransitionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable FiniteTransitionVariableType> getLastTransitionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getAvailableStates() throws UaException;

  /** Sets the existing node's local value. */
  void setAvailableStates(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId @Nullable [] readAvailableStates() throws UaException;

  /** Writes the value remotely. */
  void writeAvailableStates(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId @Nullable []> readAvailableStatesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAvailableStatesAsync(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getAvailableStatesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getAvailableStatesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getAvailableTransitions() throws UaException;

  /** Sets the existing node's local value. */
  void setAvailableTransitions(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId @Nullable [] readAvailableTransitions() throws UaException;

  /** Writes the value remotely. */
  void writeAvailableTransitions(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId @Nullable []> readAvailableTransitionsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAvailableTransitionsAsync(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getAvailableTransitionsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getAvailableTransitionsNodeAsync();
}
