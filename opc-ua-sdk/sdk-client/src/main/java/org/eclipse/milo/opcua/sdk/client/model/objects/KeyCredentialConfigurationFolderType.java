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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface KeyCredentialConfigurationFolderType extends FolderType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getCreateCredentialMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getCreateCredentialMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3
   *
   * <p>Invokes <code>CreateCredential</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callCreateCredential(
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3
   *
   * <p>Invokes <code>CreateCredential</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callCreateCredentialAsync(
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3
   *
   * <p>Invokes <code>CreateCredential</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callCreateCredentialDetailed(
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3
   *
   * <p>Invokes <code>CreateCredential</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callCreateCredentialDetailed(
      MethodCallOptions options,
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3
   *
   * <p>Invokes <code>CreateCredential</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callCreateCredentialDetailedAsync(
          @Nullable String name,
          @Nullable String resourceUri,
          @Nullable String profileUri,
          @Nullable String @Nullable [] endpointUrls);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3
   *
   * <p>Invokes <code>CreateCredential</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callCreateCredentialDetailedAsync(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable String resourceUri,
          @Nullable String profileUri,
          @Nullable String @Nullable [] endpointUrls);
}
