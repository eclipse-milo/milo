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
import org.eclipse.milo.opcua.sdk.core.model.methods.SecurityGroupFolderTypeAddSecurityGroupOutputs;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SecurityGroupFolderType extends FolderType {
  QualifiedProperty<String[]> SUPPORTED_SECURITY_POLICY_URIS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportedSecurityPolicyUris",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getSupportedSecurityPolicyUris() throws UaException;

  /** Sets the existing node's local value. */
  void setSupportedSecurityPolicyUris(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readSupportedSecurityPolicyUris() throws UaException;

  /** Writes the value remotely. */
  void writeSupportedSecurityPolicyUris(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readSupportedSecurityPolicyUrisAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSupportedSecurityPolicyUrisAsync(
      @Nullable String @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSupportedSecurityPolicyUrisNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSupportedSecurityPolicyUrisNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getAddSecurityGroupMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getAddSecurityGroupMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2
   *
   * <p>Invokes <code>AddSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  SecurityGroupFolderTypeAddSecurityGroupOutputs callAddSecurityGroup(
      @Nullable String securityGroupName,
      @Nullable Double keyLifetime,
      @Nullable String securityPolicyUri,
      @Nullable UInteger maxFutureKeyCount,
      @Nullable UInteger maxPastKeyCount)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2
   *
   * <p>Invokes <code>AddSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends SecurityGroupFolderTypeAddSecurityGroupOutputs>
      callAddSecurityGroupAsync(
          @Nullable String securityGroupName,
          @Nullable Double keyLifetime,
          @Nullable String securityPolicyUri,
          @Nullable UInteger maxFutureKeyCount,
          @Nullable UInteger maxPastKeyCount);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2
   *
   * <p>Invokes <code>AddSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends SecurityGroupFolderTypeAddSecurityGroupOutputs>
      callAddSecurityGroupDetailed(
          @Nullable String securityGroupName,
          @Nullable Double keyLifetime,
          @Nullable String securityPolicyUri,
          @Nullable UInteger maxFutureKeyCount,
          @Nullable UInteger maxPastKeyCount)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2
   *
   * <p>Invokes <code>AddSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends SecurityGroupFolderTypeAddSecurityGroupOutputs>
      callAddSecurityGroupDetailed(
          MethodCallOptions options,
          @Nullable String securityGroupName,
          @Nullable Double keyLifetime,
          @Nullable String securityPolicyUri,
          @Nullable UInteger maxFutureKeyCount,
          @Nullable UInteger maxPastKeyCount)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2
   *
   * <p>Invokes <code>AddSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends SecurityGroupFolderTypeAddSecurityGroupOutputs>>
      callAddSecurityGroupDetailedAsync(
          @Nullable String securityGroupName,
          @Nullable Double keyLifetime,
          @Nullable String securityPolicyUri,
          @Nullable UInteger maxFutureKeyCount,
          @Nullable UInteger maxPastKeyCount);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2
   *
   * <p>Invokes <code>AddSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends SecurityGroupFolderTypeAddSecurityGroupOutputs>>
      callAddSecurityGroupDetailedAsync(
          MethodCallOptions options,
          @Nullable String securityGroupName,
          @Nullable Double keyLifetime,
          @Nullable String securityPolicyUri,
          @Nullable UInteger maxFutureKeyCount,
          @Nullable UInteger maxPastKeyCount);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getRemoveSecurityGroupMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getRemoveSecurityGroupMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3
   *
   * <p>Invokes <code>RemoveSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemoveSecurityGroup(@Nullable NodeId securityGroupNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3
   *
   * <p>Invokes <code>RemoveSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemoveSecurityGroupAsync(
      @Nullable NodeId securityGroupNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3
   *
   * <p>Invokes <code>RemoveSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveSecurityGroupDetailed(
      @Nullable NodeId securityGroupNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3
   *
   * <p>Invokes <code>RemoveSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveSecurityGroupDetailed(
      MethodCallOptions options, @Nullable NodeId securityGroupNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3
   *
   * <p>Invokes <code>RemoveSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveSecurityGroupDetailedAsync(@Nullable NodeId securityGroupNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3
   *
   * <p>Invokes <code>RemoveSecurityGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveSecurityGroupDetailedAsync(
          MethodCallOptions options, @Nullable NodeId securityGroupNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getAddSecurityGroupFolderMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddSecurityGroupFolderMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4
   *
   * <p>Invokes <code>AddSecurityGroupFolder</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callAddSecurityGroupFolder(@Nullable String name) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4
   *
   * <p>Invokes <code>AddSecurityGroupFolder</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callAddSecurityGroupFolderAsync(
      @Nullable String name);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4
   *
   * <p>Invokes <code>AddSecurityGroupFolder</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddSecurityGroupFolderDetailed(
      @Nullable String name) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4
   *
   * <p>Invokes <code>AddSecurityGroupFolder</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddSecurityGroupFolderDetailed(
      MethodCallOptions options, @Nullable String name) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4
   *
   * <p>Invokes <code>AddSecurityGroupFolder</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddSecurityGroupFolderDetailedAsync(@Nullable String name);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4
   *
   * <p>Invokes <code>AddSecurityGroupFolder</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddSecurityGroupFolderDetailedAsync(MethodCallOptions options, @Nullable String name);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getRemoveSecurityGroupFolderMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemoveSecurityGroupFolderMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5
   *
   * <p>Invokes <code>RemoveSecurityGroupFolder</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemoveSecurityGroupFolder(@Nullable NodeId securityGroupFolderNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5
   *
   * <p>Invokes <code>RemoveSecurityGroupFolder</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemoveSecurityGroupFolderAsync(
      @Nullable NodeId securityGroupFolderNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5
   *
   * <p>Invokes <code>RemoveSecurityGroupFolder</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveSecurityGroupFolderDetailed(
      @Nullable NodeId securityGroupFolderNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5
   *
   * <p>Invokes <code>RemoveSecurityGroupFolder</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveSecurityGroupFolderDetailed(
      MethodCallOptions options, @Nullable NodeId securityGroupFolderNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5
   *
   * <p>Invokes <code>RemoveSecurityGroupFolder</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveSecurityGroupFolderDetailedAsync(@Nullable NodeId securityGroupFolderNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5
   *
   * <p>Invokes <code>RemoveSecurityGroupFolder</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveSecurityGroupFolderDetailedAsync(
          MethodCallOptions options, @Nullable NodeId securityGroupFolderNodeId);
}
