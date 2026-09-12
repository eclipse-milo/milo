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
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.1">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ShelvedStateMachineType extends FiniteStateMachineType {
  QualifiedProperty<Double> UNSHELVE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UnshelveTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /** Gets the existing node's local value. */
  @Nullable Double getUnshelveTime() throws UaException;

  /** Sets the existing node's local value. */
  void setUnshelveTime(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readUnshelveTime() throws UaException;

  /** Writes the value remotely. */
  void writeUnshelveTime(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readUnshelveTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUnshelveTimeAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUnshelveTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getUnshelveTimeNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getUnshelvedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateType> getUnshelvedNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getTimedShelvedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateType> getTimedShelvedNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getOneShotShelvedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateType> getOneShotShelvedNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getUnshelvedToTimedShelvedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getUnshelvedToTimedShelvedNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getUnshelvedToOneShotShelvedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getUnshelvedToOneShotShelvedNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getTimedShelvedToUnshelvedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getTimedShelvedToUnshelvedNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getTimedShelvedToOneShotShelvedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getTimedShelvedToOneShotShelvedNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getOneShotShelvedToUnshelvedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getOneShotShelvedToUnshelvedNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getOneShotShelvedToTimedShelvedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getOneShotShelvedToTimedShelvedNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getTimedShelveMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getTimedShelveMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4
   *
   * <p>Invokes <code>TimedShelve</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callTimedShelve(@Nullable Double shelvingTime) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4
   *
   * <p>Invokes <code>TimedShelve</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callTimedShelveAsync(@Nullable Double shelvingTime);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4
   *
   * <p>Invokes <code>TimedShelve</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callTimedShelveDetailed(@Nullable Double shelvingTime)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4
   *
   * <p>Invokes <code>TimedShelve</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callTimedShelveDetailed(
      MethodCallOptions options, @Nullable Double shelvingTime) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4
   *
   * <p>Invokes <code>TimedShelve</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callTimedShelveDetailedAsync(@Nullable Double shelvingTime);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4
   *
   * <p>Invokes <code>TimedShelve</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callTimedShelveDetailedAsync(MethodCallOptions options, @Nullable Double shelvingTime);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getTimedShelve2MethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getTimedShelve2MethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5
   *
   * <p>Invokes <code>TimedShelve2</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callTimedShelve2(@Nullable Double shelvingTime, @Nullable LocalizedText comment)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5
   *
   * <p>Invokes <code>TimedShelve2</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callTimedShelve2Async(
      @Nullable Double shelvingTime, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5
   *
   * <p>Invokes <code>TimedShelve2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callTimedShelve2Detailed(
      @Nullable Double shelvingTime, @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5
   *
   * <p>Invokes <code>TimedShelve2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callTimedShelve2Detailed(
      MethodCallOptions options, @Nullable Double shelvingTime, @Nullable LocalizedText comment)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5
   *
   * <p>Invokes <code>TimedShelve2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callTimedShelve2DetailedAsync(@Nullable Double shelvingTime, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5
   *
   * <p>Invokes <code>TimedShelve2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callTimedShelve2DetailedAsync(
          MethodCallOptions options,
          @Nullable Double shelvingTime,
          @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getUnshelveMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getUnshelveMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2
   *
   * <p>Invokes <code>Unshelve</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callUnshelve() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2
   *
   * <p>Invokes <code>Unshelve</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callUnshelveAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2
   *
   * <p>Invokes <code>Unshelve</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callUnshelveDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2
   *
   * <p>Invokes <code>Unshelve</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callUnshelveDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2
   *
   * <p>Invokes <code>Unshelve</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callUnshelveDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2
   *
   * <p>Invokes <code>Unshelve</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callUnshelveDetailedAsync(
      MethodCallOptions options);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getUnshelve2MethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getUnshelve2MethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3
   *
   * <p>Invokes <code>Unshelve2</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callUnshelve2(@Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3
   *
   * <p>Invokes <code>Unshelve2</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callUnshelve2Async(@Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3
   *
   * <p>Invokes <code>Unshelve2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callUnshelve2Detailed(@Nullable LocalizedText comment)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3
   *
   * <p>Invokes <code>Unshelve2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callUnshelve2Detailed(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3
   *
   * <p>Invokes <code>Unshelve2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callUnshelve2DetailedAsync(@Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3
   *
   * <p>Invokes <code>Unshelve2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callUnshelve2DetailedAsync(MethodCallOptions options, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getOneShotShelveMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getOneShotShelveMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6
   *
   * <p>Invokes <code>OneShotShelve</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callOneShotShelve() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6
   *
   * <p>Invokes <code>OneShotShelve</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callOneShotShelveAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6
   *
   * <p>Invokes <code>OneShotShelve</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callOneShotShelveDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6
   *
   * <p>Invokes <code>OneShotShelve</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callOneShotShelveDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6
   *
   * <p>Invokes <code>OneShotShelve</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callOneShotShelveDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6
   *
   * <p>Invokes <code>OneShotShelve</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callOneShotShelveDetailedAsync(MethodCallOptions options);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getOneShotShelve2MethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getOneShotShelve2MethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7
   *
   * <p>Invokes <code>OneShotShelve2</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callOneShotShelve2(@Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7
   *
   * <p>Invokes <code>OneShotShelve2</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callOneShotShelve2Async(
      @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7
   *
   * <p>Invokes <code>OneShotShelve2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callOneShotShelve2Detailed(
      @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7
   *
   * <p>Invokes <code>OneShotShelve2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callOneShotShelve2Detailed(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7
   *
   * <p>Invokes <code>OneShotShelve2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callOneShotShelve2DetailedAsync(@Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7
   *
   * <p>Invokes <code>OneShotShelve2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callOneShotShelve2DetailedAsync(MethodCallOptions options, @Nullable LocalizedText comment);
}
