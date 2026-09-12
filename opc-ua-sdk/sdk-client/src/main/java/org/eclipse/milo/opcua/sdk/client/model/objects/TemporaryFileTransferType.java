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
import org.eclipse.milo.opcua.sdk.core.model.methods.TemporaryFileTransferTypeGenerateFileForReadOutputs;
import org.eclipse.milo.opcua.sdk.core.model.methods.TemporaryFileTransferTypeGenerateFileForWriteOutputs;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface TemporaryFileTransferType extends BaseObjectType {
  QualifiedProperty<Double> CLIENT_PROCESSING_TIMEOUT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientProcessingTimeout",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /** Gets the existing node's local value. */
  @Nullable Double getClientProcessingTimeout() throws UaException;

  /** Sets the existing node's local value. */
  void setClientProcessingTimeout(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readClientProcessingTimeout() throws UaException;

  /** Writes the value remotely. */
  void writeClientProcessingTimeout(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readClientProcessingTimeoutAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeClientProcessingTimeoutAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getClientProcessingTimeoutNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getClientProcessingTimeoutNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getGenerateFileForReadMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getGenerateFileForReadMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3
   *
   * <p>Invokes <code>GenerateFileForRead</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  TemporaryFileTransferTypeGenerateFileForReadOutputs callGenerateFileForRead(
      @Nullable Object generateOptions) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3
   *
   * <p>Invokes <code>GenerateFileForRead</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends TemporaryFileTransferTypeGenerateFileForReadOutputs>
      callGenerateFileForReadAsync(@Nullable Object generateOptions);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3
   *
   * <p>Invokes <code>GenerateFileForRead</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForReadOutputs>
      callGenerateFileForReadDetailed(@Nullable Object generateOptions) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3
   *
   * <p>Invokes <code>GenerateFileForRead</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForReadOutputs>
      callGenerateFileForReadDetailed(MethodCallOptions options, @Nullable Object generateOptions)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3
   *
   * <p>Invokes <code>GenerateFileForRead</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForReadOutputs>>
      callGenerateFileForReadDetailedAsync(@Nullable Object generateOptions);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3
   *
   * <p>Invokes <code>GenerateFileForRead</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForReadOutputs>>
      callGenerateFileForReadDetailedAsync(
          MethodCallOptions options, @Nullable Object generateOptions);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getGenerateFileForWriteMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getGenerateFileForWriteMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4
   *
   * <p>Invokes <code>GenerateFileForWrite</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  TemporaryFileTransferTypeGenerateFileForWriteOutputs callGenerateFileForWrite(
      @Nullable Object generateOptions) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4
   *
   * <p>Invokes <code>GenerateFileForWrite</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends TemporaryFileTransferTypeGenerateFileForWriteOutputs>
      callGenerateFileForWriteAsync(@Nullable Object generateOptions);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4
   *
   * <p>Invokes <code>GenerateFileForWrite</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForWriteOutputs>
      callGenerateFileForWriteDetailed(@Nullable Object generateOptions) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4
   *
   * <p>Invokes <code>GenerateFileForWrite</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForWriteOutputs>
      callGenerateFileForWriteDetailed(MethodCallOptions options, @Nullable Object generateOptions)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4
   *
   * <p>Invokes <code>GenerateFileForWrite</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<
          ? extends
              MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForWriteOutputs>>
      callGenerateFileForWriteDetailedAsync(@Nullable Object generateOptions);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4
   *
   * <p>Invokes <code>GenerateFileForWrite</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<
          ? extends
              MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForWriteOutputs>>
      callGenerateFileForWriteDetailedAsync(
          MethodCallOptions options, @Nullable Object generateOptions);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getCloseAndCommitMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getCloseAndCommitMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5
   *
   * <p>Invokes <code>CloseAndCommit</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callCloseAndCommit(@Nullable UInteger fileHandle) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5
   *
   * <p>Invokes <code>CloseAndCommit</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callCloseAndCommitAsync(
      @Nullable UInteger fileHandle);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5
   *
   * <p>Invokes <code>CloseAndCommit</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callCloseAndCommitDetailed(
      @Nullable UInteger fileHandle) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5
   *
   * <p>Invokes <code>CloseAndCommit</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callCloseAndCommitDetailed(
      MethodCallOptions options, @Nullable UInteger fileHandle) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5
   *
   * <p>Invokes <code>CloseAndCommit</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callCloseAndCommitDetailedAsync(@Nullable UInteger fileHandle);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5
   *
   * <p>Invokes <code>CloseAndCommit</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callCloseAndCommitDetailedAsync(MethodCallOptions options, @Nullable UInteger fileHandle);
}
