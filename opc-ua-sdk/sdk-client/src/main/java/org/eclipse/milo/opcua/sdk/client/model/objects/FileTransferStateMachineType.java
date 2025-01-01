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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.6">https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.6</a>
 */
public interface FileTransferStateMachineType extends FiniteStateMachineType {
  /**
   * Get the Idle {@link InitialStateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Idle {@link InitialStateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  InitialStateType getIdleNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getIdleNode()}.
   *
   * @return a CompletableFuture that completes successfully with the InitialStateType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends InitialStateType> getIdleNodeAsync();

  /**
   * Get the ReadPrepare {@link StateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReadPrepare {@link StateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateType getReadPrepareNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReadPrepareNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateType> getReadPrepareNodeAsync();

  /**
   * Get the ReadTransfer {@link StateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReadTransfer {@link StateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateType getReadTransferNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReadTransferNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateType> getReadTransferNodeAsync();

  /**
   * Get the ApplyWrite {@link StateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ApplyWrite {@link StateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateType getApplyWriteNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getApplyWriteNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateType> getApplyWriteNodeAsync();

  /**
   * Get the Error {@link StateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Error {@link StateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateType getErrorNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getErrorNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateType> getErrorNodeAsync();

  /**
   * Get the IdleToReadPrepare {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the IdleToReadPrepare {@link TransitionType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getIdleToReadPrepareNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getIdleToReadPrepareNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getIdleToReadPrepareNodeAsync();

  /**
   * Get the ReadPrepareToReadTransfer {@link TransitionType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReadPrepareToReadTransfer {@link TransitionType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getReadPrepareToReadTransferNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReadPrepareToReadTransferNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getReadPrepareToReadTransferNodeAsync();

  /**
   * Get the ReadTransferToIdle {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReadTransferToIdle {@link TransitionType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getReadTransferToIdleNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReadTransferToIdleNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getReadTransferToIdleNodeAsync();

  /**
   * Get the IdleToApplyWrite {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the IdleToApplyWrite {@link TransitionType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getIdleToApplyWriteNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getIdleToApplyWriteNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getIdleToApplyWriteNodeAsync();

  /**
   * Get the ApplyWriteToIdle {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ApplyWriteToIdle {@link TransitionType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getApplyWriteToIdleNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getApplyWriteToIdleNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getApplyWriteToIdleNodeAsync();

  /**
   * Get the ReadPrepareToError {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReadPrepareToError {@link TransitionType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getReadPrepareToErrorNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReadPrepareToErrorNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getReadPrepareToErrorNodeAsync();

  /**
   * Get the ReadTransferToError {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReadTransferToError {@link TransitionType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getReadTransferToErrorNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReadTransferToErrorNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getReadTransferToErrorNodeAsync();

  /**
   * Get the ApplyWriteToError {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ApplyWriteToError {@link TransitionType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getApplyWriteToErrorNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getApplyWriteToErrorNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getApplyWriteToErrorNodeAsync();

  /**
   * Get the ErrorToIdle {@link TransitionType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ErrorToIdle {@link TransitionType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getErrorToIdleNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getErrorToIdleNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getErrorToIdleNodeAsync();
}
