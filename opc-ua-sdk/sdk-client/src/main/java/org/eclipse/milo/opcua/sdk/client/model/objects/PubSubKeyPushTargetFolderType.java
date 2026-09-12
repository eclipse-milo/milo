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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PubSubKeyPushTargetFolderType extends FolderType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getAddPushTargetMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getAddPushTargetMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2
   *
   * <p>Invokes <code>AddPushTarget</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callAddPushTarget(
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2
   *
   * <p>Invokes <code>AddPushTarget</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callAddPushTargetAsync(
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2
   *
   * <p>Invokes <code>AddPushTarget</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddPushTargetDetailed(
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2
   *
   * <p>Invokes <code>AddPushTarget</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddPushTargetDetailed(
      MethodCallOptions options,
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2
   *
   * <p>Invokes <code>AddPushTarget</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddPushTargetDetailedAsync(
          @Nullable String applicationUri,
          @Nullable String endpointUrl,
          @Nullable String securityPolicyUri,
          @Nullable UserTokenPolicy userTokenType,
          @Nullable UShort requestedKeyCount,
          @Nullable Double retryInterval);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2
   *
   * <p>Invokes <code>AddPushTarget</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddPushTargetDetailedAsync(
          MethodCallOptions options,
          @Nullable String applicationUri,
          @Nullable String endpointUrl,
          @Nullable String securityPolicyUri,
          @Nullable UserTokenPolicy userTokenType,
          @Nullable UShort requestedKeyCount,
          @Nullable Double retryInterval);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getRemovePushTargetMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getRemovePushTargetMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
   *
   * <p>Invokes <code>RemovePushTarget</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemovePushTarget(@Nullable NodeId pushTargetId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
   *
   * <p>Invokes <code>RemovePushTarget</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemovePushTargetAsync(
      @Nullable NodeId pushTargetId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
   *
   * <p>Invokes <code>RemovePushTarget</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemovePushTargetDetailed(
      @Nullable NodeId pushTargetId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
   *
   * <p>Invokes <code>RemovePushTarget</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemovePushTargetDetailed(
      MethodCallOptions options, @Nullable NodeId pushTargetId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
   *
   * <p>Invokes <code>RemovePushTarget</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemovePushTargetDetailedAsync(@Nullable NodeId pushTargetId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
   *
   * <p>Invokes <code>RemovePushTarget</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemovePushTargetDetailedAsync(MethodCallOptions options, @Nullable NodeId pushTargetId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getAddPushTargetFolderMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddPushTargetFolderMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
   *
   * <p>Invokes <code>AddPushTargetFolder</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callAddPushTargetFolder(@Nullable String name) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
   *
   * <p>Invokes <code>AddPushTargetFolder</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callAddPushTargetFolderAsync(@Nullable String name);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
   *
   * <p>Invokes <code>AddPushTargetFolder</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddPushTargetFolderDetailed(
      @Nullable String name) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
   *
   * <p>Invokes <code>AddPushTargetFolder</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddPushTargetFolderDetailed(
      MethodCallOptions options, @Nullable String name) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
   *
   * <p>Invokes <code>AddPushTargetFolder</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddPushTargetFolderDetailedAsync(@Nullable String name);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
   *
   * <p>Invokes <code>AddPushTargetFolder</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddPushTargetFolderDetailedAsync(MethodCallOptions options, @Nullable String name);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getRemovePushTargetFolderMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemovePushTargetFolderMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
   *
   * <p>Invokes <code>RemovePushTargetFolder</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemovePushTargetFolder(@Nullable NodeId pushTargetFolderNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
   *
   * <p>Invokes <code>RemovePushTargetFolder</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemovePushTargetFolderAsync(
      @Nullable NodeId pushTargetFolderNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
   *
   * <p>Invokes <code>RemovePushTargetFolder</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemovePushTargetFolderDetailed(
      @Nullable NodeId pushTargetFolderNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
   *
   * <p>Invokes <code>RemovePushTargetFolder</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemovePushTargetFolderDetailed(
      MethodCallOptions options, @Nullable NodeId pushTargetFolderNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
   *
   * <p>Invokes <code>RemovePushTargetFolder</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemovePushTargetFolderDetailedAsync(@Nullable NodeId pushTargetFolderNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
   *
   * <p>Invokes <code>RemovePushTargetFolder</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemovePushTargetFolderDetailedAsync(
          MethodCallOptions options, @Nullable NodeId pushTargetFolderNodeId);
}
