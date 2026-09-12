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
 */
public interface PubSubKeyPushTargetFolderType extends FolderType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2
   *
   * <p>Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  UaMethodNode getAddPushTargetMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2
   *
   * <p>Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getAddPushTargetMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2
   *
   * <p>Invokes <code>AddPushTarget</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param applicationUri ; the supplied payload may be null.
   * @param endpointUrl ; the supplied payload may be null.
   * @param securityPolicyUri ; the supplied payload may be null.
   * @param userTokenType ; the supplied payload may be null.
   * @param requestedKeyCount ; the supplied payload may be null.
   * @param retryInterval ; the supplied payload may be null.
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
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param applicationUri ; the supplied payload may be null.
   * @param endpointUrl ; the supplied payload may be null.
   * @param securityPolicyUri ; the supplied payload may be null.
   * @param userTokenType ; the supplied payload may be null.
   * @param requestedKeyCount ; the supplied payload may be null.
   * @param retryInterval ; the supplied payload may be null.
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
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param applicationUri ; the supplied payload may be null.
   * @param endpointUrl ; the supplied payload may be null.
   * @param securityPolicyUri ; the supplied payload may be null.
   * @param userTokenType ; the supplied payload may be null.
   * @param requestedKeyCount ; the supplied payload may be null.
   * @param retryInterval ; the supplied payload may be null.
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
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param applicationUri ; the supplied payload may be null.
   * @param endpointUrl ; the supplied payload may be null.
   * @param securityPolicyUri ; the supplied payload may be null.
   * @param userTokenType ; the supplied payload may be null.
   * @param requestedKeyCount ; the supplied payload may be null.
   * @param retryInterval ; the supplied payload may be null.
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
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param applicationUri ; the supplied payload may be null.
   * @param endpointUrl ; the supplied payload may be null.
   * @param securityPolicyUri ; the supplied payload may be null.
   * @param userTokenType ; the supplied payload may be null.
   * @param requestedKeyCount ; the supplied payload may be null.
   * @param retryInterval ; the supplied payload may be null.
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
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param applicationUri ; the supplied payload may be null.
   * @param endpointUrl ; the supplied payload may be null.
   * @param securityPolicyUri ; the supplied payload may be null.
   * @param userTokenType ; the supplied payload may be null.
   * @param requestedKeyCount ; the supplied payload may be null.
   * @param retryInterval ; the supplied payload may be null.
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
   * <p>Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  UaMethodNode getRemovePushTargetMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
   *
   * <p>Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getRemovePushTargetMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
   *
   * <p>Invokes <code>RemovePushTarget</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param pushTargetId ; the supplied payload may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemovePushTarget(@Nullable NodeId pushTargetId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
   *
   * <p>Invokes <code>RemovePushTarget</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param pushTargetId ; the supplied payload may be null.
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemovePushTargetAsync(
      @Nullable NodeId pushTargetId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
   *
   * <p>Invokes <code>RemovePushTarget</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param pushTargetId ; the supplied payload may be null.
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
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param pushTargetId ; the supplied payload may be null.
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
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param pushTargetId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemovePushTargetDetailedAsync(@Nullable NodeId pushTargetId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
   *
   * <p>Invokes <code>RemovePushTarget</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param pushTargetId ; the supplied payload may be null.
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
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  @Nullable UaMethodNode getAddPushTargetFolderMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddPushTargetFolderMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
   *
   * <p>Invokes <code>AddPushTargetFolder</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param name ; the supplied payload may be null.
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
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param name ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callAddPushTargetFolderAsync(@Nullable String name);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
   *
   * <p>Invokes <code>AddPushTargetFolder</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param name ; the supplied payload may be null.
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
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param name ; the supplied payload may be null.
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
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param name ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddPushTargetFolderDetailedAsync(@Nullable String name);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
   *
   * <p>Invokes <code>AddPushTargetFolder</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param name ; the supplied payload may be null.
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
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  @Nullable UaMethodNode getRemovePushTargetFolderMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemovePushTargetFolderMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
   *
   * <p>Invokes <code>RemovePushTargetFolder</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param pushTargetFolderNodeId ; the supplied payload may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemovePushTargetFolder(@Nullable NodeId pushTargetFolderNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
   *
   * <p>Invokes <code>RemovePushTargetFolder</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param pushTargetFolderNodeId ; the supplied payload may be null.
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemovePushTargetFolderAsync(
      @Nullable NodeId pushTargetFolderNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
   *
   * <p>Invokes <code>RemovePushTargetFolder</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param pushTargetFolderNodeId ; the supplied payload may be null.
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
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param pushTargetFolderNodeId ; the supplied payload may be null.
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
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param pushTargetFolderNodeId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemovePushTargetFolderDetailedAsync(@Nullable NodeId pushTargetFolderNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
   *
   * <p>Invokes <code>RemovePushTargetFolder</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param pushTargetFolderNodeId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemovePushTargetFolderDetailedAsync(
          MethodCallOptions options, @Nullable NodeId pushTargetFolderNodeId);
}
