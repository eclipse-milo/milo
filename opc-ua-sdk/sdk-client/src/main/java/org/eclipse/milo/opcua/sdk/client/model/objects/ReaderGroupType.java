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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.9">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.9</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ReaderGroupType extends PubSubGroupType {
  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubDiagnosticsReaderGroupType getDiagnosticsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PubSubDiagnosticsReaderGroupType> getDiagnosticsNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable ReaderGroupTransportType getTransportSettingsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable ReaderGroupTransportType> getTransportSettingsNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable ReaderGroupMessageType getMessageSettingsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable ReaderGroupMessageType> getMessageSettingsNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getAddDataSetReaderMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddDataSetReaderMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10
   *
   * <p>Invokes <code>AddDataSetReader</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callAddDataSetReader(@Nullable DataSetReaderDataType configuration)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10
   *
   * <p>Invokes <code>AddDataSetReader</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callAddDataSetReaderAsync(
      @Nullable DataSetReaderDataType configuration);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10
   *
   * <p>Invokes <code>AddDataSetReader</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddDataSetReaderDetailed(
      @Nullable DataSetReaderDataType configuration) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10
   *
   * <p>Invokes <code>AddDataSetReader</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddDataSetReaderDetailed(
      MethodCallOptions options, @Nullable DataSetReaderDataType configuration) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10
   *
   * <p>Invokes <code>AddDataSetReader</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddDataSetReaderDetailedAsync(@Nullable DataSetReaderDataType configuration);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10
   *
   * <p>Invokes <code>AddDataSetReader</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddDataSetReaderDetailedAsync(
          MethodCallOptions options, @Nullable DataSetReaderDataType configuration);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getRemoveDataSetReaderMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemoveDataSetReaderMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11
   *
   * <p>Invokes <code>RemoveDataSetReader</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemoveDataSetReader(@Nullable NodeId dataSetReaderNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11
   *
   * <p>Invokes <code>RemoveDataSetReader</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemoveDataSetReaderAsync(
      @Nullable NodeId dataSetReaderNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11
   *
   * <p>Invokes <code>RemoveDataSetReader</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveDataSetReaderDetailed(
      @Nullable NodeId dataSetReaderNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11
   *
   * <p>Invokes <code>RemoveDataSetReader</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveDataSetReaderDetailed(
      MethodCallOptions options, @Nullable NodeId dataSetReaderNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11
   *
   * <p>Invokes <code>RemoveDataSetReader</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveDataSetReaderDetailedAsync(@Nullable NodeId dataSetReaderNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11
   *
   * <p>Invokes <code>RemoveDataSetReader</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveDataSetReaderDetailedAsync(
          MethodCallOptions options, @Nullable NodeId dataSetReaderNodeId);
}
