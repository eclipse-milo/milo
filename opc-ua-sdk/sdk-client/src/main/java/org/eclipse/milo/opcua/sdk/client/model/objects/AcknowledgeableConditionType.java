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
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AcknowledgeableConditionType extends ConditionType {
  /** Gets the existing node's local value. */
  @Nullable LocalizedText getEnabledState() throws UaException;

  /** Sets the existing node's local value. */
  void setEnabledState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readEnabledState() throws UaException;

  /** Writes the value remotely. */
  void writeEnabledState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readEnabledStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEnabledStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TwoStateVariableType getEnabledStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TwoStateVariableType> getEnabledStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getAckedState() throws UaException;

  /** Sets the existing node's local value. */
  void setAckedState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readAckedState() throws UaException;

  /** Writes the value remotely. */
  void writeAckedState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readAckedStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAckedStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TwoStateVariableType getAckedStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TwoStateVariableType> getAckedStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getConfirmedState() throws UaException;

  /** Sets the existing node's local value. */
  void setConfirmedState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readConfirmedState() throws UaException;

  /** Writes the value remotely. */
  void writeConfirmedState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readConfirmedStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConfirmedStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getConfirmedStateNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getConfirmedStateNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getAcknowledgeMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getAcknowledgeMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3
   *
   * <p>Invokes <code>Acknowledge</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callAcknowledge(@Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3
   *
   * <p>Invokes <code>Acknowledge</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callAcknowledgeAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3
   *
   * <p>Invokes <code>Acknowledge</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAcknowledgeDetailed(
      @Nullable ByteString eventId, @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3
   *
   * <p>Invokes <code>Acknowledge</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAcknowledgeDetailed(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3
   *
   * <p>Invokes <code>Acknowledge</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAcknowledgeDetailedAsync(@Nullable ByteString eventId, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3
   *
   * <p>Invokes <code>Acknowledge</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAcknowledgeDetailedAsync(
          MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getConfirmMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getConfirmMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4
   *
   * <p>Invokes <code>Confirm</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callConfirm(@Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4
   *
   * <p>Invokes <code>Confirm</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callConfirmAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4
   *
   * <p>Invokes <code>Confirm</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callConfirmDetailed(
      @Nullable ByteString eventId, @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4
   *
   * <p>Invokes <code>Confirm</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callConfirmDetailed(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4
   *
   * <p>Invokes <code>Confirm</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callConfirmDetailedAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4
   *
   * <p>Invokes <code>Confirm</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callConfirmDetailedAsync(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment);
}
