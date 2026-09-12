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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundantServerMode;
import org.eclipse.milo.opcua.stack.core.types.structured.RedundantServerDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.15">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.15</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface NonTransparentBackupRedundancyType extends NonTransparentRedundancyType {
  QualifiedProperty<RedundantServerDataType[]> REDUNDANT_SERVER_ARRAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RedundantServerArray",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=853"),
          1,
          RedundantServerDataType[].class);

  QualifiedProperty<RedundantServerMode> MODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Mode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32417"),
          -1,
          RedundantServerMode.class);

  /** Gets the existing node's local value. */
  @Nullable RedundantServerDataType @Nullable [] getRedundantServerArray() throws UaException;

  /** Sets the existing node's local value. */
  void setRedundantServerArray(@Nullable RedundantServerDataType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable RedundantServerDataType @Nullable [] readRedundantServerArray() throws UaException;

  /** Writes the value remotely. */
  void writeRedundantServerArray(@Nullable RedundantServerDataType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable RedundantServerDataType @Nullable []>
      readRedundantServerArrayAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRedundantServerArrayAsync(
      @Nullable RedundantServerDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRedundantServerArrayNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getRedundantServerArrayNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable RedundantServerMode getMode() throws UaException;

  /** Sets the existing node's local value. */
  void setMode(@Nullable RedundantServerMode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable RedundantServerMode readMode() throws UaException;

  /** Writes the value remotely. */
  void writeMode(@Nullable RedundantServerMode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable RedundantServerMode> readModeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeModeAsync(@Nullable RedundantServerMode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getModeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getModeNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getFailoverMethodNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getFailoverMethodNodeAsync();

  /**
   * Invokes <code>Failover</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callFailover() throws UaException;

  /**
   * Invokes <code>Failover</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callFailoverAsync();

  /**
   * Invokes <code>Failover</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callFailoverDetailed() throws UaException;

  /**
   * Invokes <code>Failover</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callFailoverDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * Invokes <code>Failover</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callFailoverDetailedAsync();

  /**
   * Invokes <code>Failover</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callFailoverDetailedAsync(
      MethodCallOptions options);
}
