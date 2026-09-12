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
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyServiceTypeGetSecurityKeysOutputs;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PubSubKeyServiceType extends BaseObjectType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getGetSecurityKeysMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getGetSecurityKeysMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2
   *
   * <p>Invokes <code>GetSecurityKeys</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  PubSubKeyServiceTypeGetSecurityKeysOutputs callGetSecurityKeys(
      @Nullable String securityGroupId,
      @Nullable UInteger startingTokenId,
      @Nullable UInteger requestedKeyCount)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2
   *
   * <p>Invokes <code>GetSecurityKeys</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends PubSubKeyServiceTypeGetSecurityKeysOutputs> callGetSecurityKeysAsync(
      @Nullable String securityGroupId,
      @Nullable UInteger startingTokenId,
      @Nullable UInteger requestedKeyCount);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2
   *
   * <p>Invokes <code>GetSecurityKeys</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends PubSubKeyServiceTypeGetSecurityKeysOutputs>
      callGetSecurityKeysDetailed(
          @Nullable String securityGroupId,
          @Nullable UInteger startingTokenId,
          @Nullable UInteger requestedKeyCount)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2
   *
   * <p>Invokes <code>GetSecurityKeys</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends PubSubKeyServiceTypeGetSecurityKeysOutputs>
      callGetSecurityKeysDetailed(
          MethodCallOptions options,
          @Nullable String securityGroupId,
          @Nullable UInteger startingTokenId,
          @Nullable UInteger requestedKeyCount)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2
   *
   * <p>Invokes <code>GetSecurityKeys</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends PubSubKeyServiceTypeGetSecurityKeysOutputs>>
      callGetSecurityKeysDetailedAsync(
          @Nullable String securityGroupId,
          @Nullable UInteger startingTokenId,
          @Nullable UInteger requestedKeyCount);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2
   *
   * <p>Invokes <code>GetSecurityKeys</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends PubSubKeyServiceTypeGetSecurityKeysOutputs>>
      callGetSecurityKeysDetailedAsync(
          MethodCallOptions options,
          @Nullable String securityGroupId,
          @Nullable UInteger startingTokenId,
          @Nullable UInteger requestedKeyCount);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getGetSecurityGroupMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getGetSecurityGroupMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3
   *
   * <p>Invokes <code>GetSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callGetSecurityGroup(@Nullable String securityGroupId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3
   *
   * <p>Invokes <code>GetSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callGetSecurityGroupAsync(
      @Nullable String securityGroupId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3
   *
   * <p>Invokes <code>GetSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callGetSecurityGroupDetailed(
      @Nullable String securityGroupId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3
   *
   * <p>Invokes <code>GetSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callGetSecurityGroupDetailed(
      MethodCallOptions options, @Nullable String securityGroupId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3
   *
   * <p>Invokes <code>GetSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callGetSecurityGroupDetailedAsync(@Nullable String securityGroupId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3
   *
   * <p>Invokes <code>GetSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callGetSecurityGroupDetailedAsync(
          MethodCallOptions options, @Nullable String securityGroupId);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable SecurityGroupFolderType getSecurityGroupsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable SecurityGroupFolderType> getSecurityGroupsNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubKeyPushTargetFolderType getKeyPushTargetsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PubSubKeyPushTargetFolderType> getKeyPushTargetsNodeAsync();
}
