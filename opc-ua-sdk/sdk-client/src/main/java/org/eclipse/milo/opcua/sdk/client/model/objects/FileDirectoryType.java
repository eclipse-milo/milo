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
import org.eclipse.milo.opcua.sdk.core.model.methods.FileDirectoryTypeCreateFileOutputs;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface FileDirectoryType extends FolderType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getCreateDirectoryMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getCreateDirectoryMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3
   *
   * <p>Invokes <code>CreateDirectory</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callCreateDirectory(@Nullable String directoryName) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3
   *
   * <p>Invokes <code>CreateDirectory</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callCreateDirectoryAsync(
      @Nullable String directoryName);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3
   *
   * <p>Invokes <code>CreateDirectory</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callCreateDirectoryDetailed(
      @Nullable String directoryName) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3
   *
   * <p>Invokes <code>CreateDirectory</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callCreateDirectoryDetailed(
      MethodCallOptions options, @Nullable String directoryName) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3
   *
   * <p>Invokes <code>CreateDirectory</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callCreateDirectoryDetailedAsync(@Nullable String directoryName);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3
   *
   * <p>Invokes <code>CreateDirectory</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callCreateDirectoryDetailedAsync(MethodCallOptions options, @Nullable String directoryName);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getCreateFileMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getCreateFileMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4
   *
   * <p>Invokes <code>CreateFile</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  FileDirectoryTypeCreateFileOutputs callCreateFile(
      @Nullable String fileName, @Nullable Boolean requestFileOpen) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4
   *
   * <p>Invokes <code>CreateFile</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends FileDirectoryTypeCreateFileOutputs> callCreateFileAsync(
      @Nullable String fileName, @Nullable Boolean requestFileOpen);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4
   *
   * <p>Invokes <code>CreateFile</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends FileDirectoryTypeCreateFileOutputs> callCreateFileDetailed(
      @Nullable String fileName, @Nullable Boolean requestFileOpen) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4
   *
   * <p>Invokes <code>CreateFile</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends FileDirectoryTypeCreateFileOutputs> callCreateFileDetailed(
      MethodCallOptions options, @Nullable String fileName, @Nullable Boolean requestFileOpen)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4
   *
   * <p>Invokes <code>CreateFile</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends FileDirectoryTypeCreateFileOutputs>>
      callCreateFileDetailedAsync(@Nullable String fileName, @Nullable Boolean requestFileOpen);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4
   *
   * <p>Invokes <code>CreateFile</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends FileDirectoryTypeCreateFileOutputs>>
      callCreateFileDetailedAsync(
          MethodCallOptions options, @Nullable String fileName, @Nullable Boolean requestFileOpen);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getDeleteFileSystemObjectMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getDeleteFileSystemObjectMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5
   *
   * <p>Invokes <code>Delete</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callDeleteFileSystemObject(@Nullable NodeId objectToDelete) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5
   *
   * <p>Invokes <code>Delete</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callDeleteFileSystemObjectAsync(
      @Nullable NodeId objectToDelete);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5
   *
   * <p>Invokes <code>Delete</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callDeleteFileSystemObjectDetailed(
      @Nullable NodeId objectToDelete) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5
   *
   * <p>Invokes <code>Delete</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callDeleteFileSystemObjectDetailed(
      MethodCallOptions options, @Nullable NodeId objectToDelete) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5
   *
   * <p>Invokes <code>Delete</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callDeleteFileSystemObjectDetailedAsync(@Nullable NodeId objectToDelete);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5
   *
   * <p>Invokes <code>Delete</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callDeleteFileSystemObjectDetailedAsync(
          MethodCallOptions options, @Nullable NodeId objectToDelete);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getMoveOrCopyMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getMoveOrCopyMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6
   *
   * <p>Invokes <code>MoveOrCopy</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callMoveOrCopy(
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6
   *
   * <p>Invokes <code>MoveOrCopy</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callMoveOrCopyAsync(
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6
   *
   * <p>Invokes <code>MoveOrCopy</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callMoveOrCopyDetailed(
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6
   *
   * <p>Invokes <code>MoveOrCopy</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callMoveOrCopyDetailed(
      MethodCallOptions options,
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6
   *
   * <p>Invokes <code>MoveOrCopy</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callMoveOrCopyDetailedAsync(
          @Nullable NodeId objectToMoveOrCopy,
          @Nullable NodeId targetDirectory,
          @Nullable Boolean createCopy,
          @Nullable String newName);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6
   *
   * <p>Invokes <code>MoveOrCopy</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callMoveOrCopyDetailedAsync(
          MethodCallOptions options,
          @Nullable NodeId objectToMoveOrCopy,
          @Nullable NodeId targetDirectory,
          @Nullable Boolean createCopy,
          @Nullable String newName);
}
