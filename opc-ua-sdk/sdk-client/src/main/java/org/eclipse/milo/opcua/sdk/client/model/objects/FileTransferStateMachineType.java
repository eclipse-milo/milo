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

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.6">https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.6</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface FileTransferStateMachineType extends FiniteStateMachineType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  InitialStateType getIdleNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends InitialStateType> getIdleNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getReadPrepareNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateType> getReadPrepareNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getReadTransferNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateType> getReadTransferNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getApplyWriteNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateType> getApplyWriteNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getErrorNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateType> getErrorNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getIdleToReadPrepareNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getIdleToReadPrepareNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getReadPrepareToReadTransferNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getReadPrepareToReadTransferNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getReadTransferToIdleNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getReadTransferToIdleNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getIdleToApplyWriteNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getIdleToApplyWriteNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getApplyWriteToIdleNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getApplyWriteToIdleNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getReadPrepareToErrorNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getReadPrepareToErrorNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getReadTransferToErrorNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getReadTransferToErrorNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getApplyWriteToErrorNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getApplyWriteToErrorNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getErrorToIdleNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getErrorToIdleNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/1
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getResetMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/1
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getResetMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/1
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callReset() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/1
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callResetAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/1
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callResetDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/1
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callResetDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/1
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callResetDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/1
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callResetDetailedAsync(
      MethodCallOptions options);
}
