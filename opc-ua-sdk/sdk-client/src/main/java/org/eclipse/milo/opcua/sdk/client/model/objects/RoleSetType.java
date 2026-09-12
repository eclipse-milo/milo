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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface RoleSetType extends BaseObjectType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getAddRoleMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getAddRoleMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2
   *
   * <p>Invokes <code>AddRole</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callAddRole(@Nullable String roleName, @Nullable String namespaceUri)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2
   *
   * <p>Invokes <code>AddRole</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callAddRoleAsync(
      @Nullable String roleName, @Nullable String namespaceUri);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2
   *
   * <p>Invokes <code>AddRole</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddRoleDetailed(
      @Nullable String roleName, @Nullable String namespaceUri) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2
   *
   * <p>Invokes <code>AddRole</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddRoleDetailed(
      MethodCallOptions options, @Nullable String roleName, @Nullable String namespaceUri)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2
   *
   * <p>Invokes <code>AddRole</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddRoleDetailedAsync(@Nullable String roleName, @Nullable String namespaceUri);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2
   *
   * <p>Invokes <code>AddRole</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddRoleDetailedAsync(
          MethodCallOptions options, @Nullable String roleName, @Nullable String namespaceUri);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getRemoveRoleMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getRemoveRoleMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3
   *
   * <p>Invokes <code>RemoveRole</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemoveRole(@Nullable NodeId roleNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3
   *
   * <p>Invokes <code>RemoveRole</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemoveRoleAsync(@Nullable NodeId roleNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3
   *
   * <p>Invokes <code>RemoveRole</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveRoleDetailed(@Nullable NodeId roleNodeId)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3
   *
   * <p>Invokes <code>RemoveRole</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveRoleDetailed(
      MethodCallOptions options, @Nullable NodeId roleNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3
   *
   * <p>Invokes <code>RemoveRole</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveRoleDetailedAsync(@Nullable NodeId roleNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3
   *
   * <p>Invokes <code>RemoveRole</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveRoleDetailedAsync(MethodCallOptions options, @Nullable NodeId roleNodeId);
}
