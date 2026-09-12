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
import org.eclipse.milo.opcua.sdk.client.model.variables.StateVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.TransitionVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.16">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.16</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface TransitionEventType extends BaseEventType {
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
  TransitionVariableType getTransitionNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionVariableType> getTransitionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getFromState() throws UaException;

  /** Sets the existing node's local value. */
  void setFromState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readFromState() throws UaException;

  /** Writes the value remotely. */
  void writeFromState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readFromStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeFromStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateVariableType getFromStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateVariableType> getFromStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getToState() throws UaException;

  /** Sets the existing node's local value. */
  void setToState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readToState() throws UaException;

  /** Writes the value remotely. */
  void writeToState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readToStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeToStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateVariableType getToStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateVariableType> getToStateNodeAsync();
}
