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
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.19/#5.8.19.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.19/#5.8.19.2</a>
 */
public interface ExclusiveLimitStateMachineType extends FiniteStateMachineType {
  /**
   * Get the HighHigh {@link StateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HighHigh {@link StateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateType getHighHighNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHighHighNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateType> getHighHighNodeAsync();

  /**
   * Get the High {@link StateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the High {@link StateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateType getHighNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHighNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateType> getHighNodeAsync();

  /**
   * Get the Low {@link StateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Low {@link StateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateType getLowNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLowNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateType> getLowNodeAsync();

  /**
   * Get the LowLow {@link StateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LowLow {@link StateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateType getLowLowNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLowLowNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateType> getLowLowNodeAsync();

  /**
   * Get the LowLowToLow {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LowLowToLow {@link TransitionType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getLowLowToLowNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLowLowToLowNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getLowLowToLowNodeAsync();

  /**
   * Get the LowToLowLow {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LowToLowLow {@link TransitionType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getLowToLowLowNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLowToLowLowNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getLowToLowLowNodeAsync();

  /**
   * Get the HighHighToHigh {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HighHighToHigh {@link TransitionType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getHighHighToHighNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHighHighToHighNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getHighHighToHighNodeAsync();

  /**
   * Get the HighToHighHigh {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HighToHighHigh {@link TransitionType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getHighToHighHighNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHighToHighHighNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getHighToHighHighNodeAsync();
}
