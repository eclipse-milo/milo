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
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConfigurationTypeCloseAndUpdateOutputs;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConfigurationTypeReserveIdsOutputs;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.1</a>
 */
public interface PubSubConfigurationType extends FileType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5
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
  UaMethodNode getReserveIdsMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5
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
  CompletableFuture<? extends UaMethodNode> getReserveIdsMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5
   *
   * <p>Invokes <code>ReserveIds</code> on this node's ObjectId using the effective Method contract.
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
   * @param transportProfileUri ; the supplied payload may be null.
   * @param numReqWriterGroupIds ; the supplied payload may be null.
   * @param numReqDataSetWriterIds ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  PubSubConfigurationTypeReserveIdsOutputs callReserveIds(
      @Nullable String transportProfileUri,
      @Nullable UShort numReqWriterGroupIds,
      @Nullable UShort numReqDataSetWriterIds)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5
   *
   * <p>Invokes <code>ReserveIds</code> on this node's ObjectId using the effective Method contract.
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
   * @param transportProfileUri ; the supplied payload may be null.
   * @param numReqWriterGroupIds ; the supplied payload may be null.
   * @param numReqDataSetWriterIds ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends PubSubConfigurationTypeReserveIdsOutputs> callReserveIdsAsync(
      @Nullable String transportProfileUri,
      @Nullable UShort numReqWriterGroupIds,
      @Nullable UShort numReqDataSetWriterIds);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5
   *
   * <p>Invokes <code>ReserveIds</code> on this node's ObjectId using the effective Method contract.
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
   * @param transportProfileUri ; the supplied payload may be null.
   * @param numReqWriterGroupIds ; the supplied payload may be null.
   * @param numReqDataSetWriterIds ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends PubSubConfigurationTypeReserveIdsOutputs> callReserveIdsDetailed(
      @Nullable String transportProfileUri,
      @Nullable UShort numReqWriterGroupIds,
      @Nullable UShort numReqDataSetWriterIds)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5
   *
   * <p>Invokes <code>ReserveIds</code> on this node's ObjectId using the effective Method contract.
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
   * @param transportProfileUri ; the supplied payload may be null.
   * @param numReqWriterGroupIds ; the supplied payload may be null.
   * @param numReqDataSetWriterIds ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends PubSubConfigurationTypeReserveIdsOutputs> callReserveIdsDetailed(
      MethodCallOptions options,
      @Nullable String transportProfileUri,
      @Nullable UShort numReqWriterGroupIds,
      @Nullable UShort numReqDataSetWriterIds)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5
   *
   * <p>Invokes <code>ReserveIds</code> on this node's ObjectId using the effective Method contract.
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
   * @param transportProfileUri ; the supplied payload may be null.
   * @param numReqWriterGroupIds ; the supplied payload may be null.
   * @param numReqDataSetWriterIds ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends PubSubConfigurationTypeReserveIdsOutputs>>
      callReserveIdsDetailedAsync(
          @Nullable String transportProfileUri,
          @Nullable UShort numReqWriterGroupIds,
          @Nullable UShort numReqDataSetWriterIds);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5
   *
   * <p>Invokes <code>ReserveIds</code> on this node's ObjectId using the effective Method contract.
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
   * @param transportProfileUri ; the supplied payload may be null.
   * @param numReqWriterGroupIds ; the supplied payload may be null.
   * @param numReqDataSetWriterIds ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends PubSubConfigurationTypeReserveIdsOutputs>>
      callReserveIdsDetailedAsync(
          MethodCallOptions options,
          @Nullable String transportProfileUri,
          @Nullable UShort numReqWriterGroupIds,
          @Nullable UShort numReqDataSetWriterIds);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6
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
  UaMethodNode getCloseAndUpdateMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6
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
  CompletableFuture<? extends UaMethodNode> getCloseAndUpdateMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
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
   * @param fileHandle ; the supplied payload may be null.
   * @param requireCompleteUpdate ; the supplied payload may be null.
   * @param configurationReferences ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  PubSubConfigurationTypeCloseAndUpdateOutputs callCloseAndUpdate(
      @Nullable UInteger fileHandle,
      @Nullable Boolean requireCompleteUpdate,
      @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
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
   * @param fileHandle ; the supplied payload may be null.
   * @param requireCompleteUpdate ; the supplied payload may be null.
   * @param configurationReferences ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends PubSubConfigurationTypeCloseAndUpdateOutputs> callCloseAndUpdateAsync(
      @Nullable UInteger fileHandle,
      @Nullable Boolean requireCompleteUpdate,
      @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
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
   * @param fileHandle ; the supplied payload may be null.
   * @param requireCompleteUpdate ; the supplied payload may be null.
   * @param configurationReferences ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends PubSubConfigurationTypeCloseAndUpdateOutputs>
      callCloseAndUpdateDetailed(
          @Nullable UInteger fileHandle,
          @Nullable Boolean requireCompleteUpdate,
          @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
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
   * @param fileHandle ; the supplied payload may be null.
   * @param requireCompleteUpdate ; the supplied payload may be null.
   * @param configurationReferences ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends PubSubConfigurationTypeCloseAndUpdateOutputs>
      callCloseAndUpdateDetailed(
          MethodCallOptions options,
          @Nullable UInteger fileHandle,
          @Nullable Boolean requireCompleteUpdate,
          @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
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
   * @param fileHandle ; the supplied payload may be null.
   * @param requireCompleteUpdate ; the supplied payload may be null.
   * @param configurationReferences ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends PubSubConfigurationTypeCloseAndUpdateOutputs>>
      callCloseAndUpdateDetailedAsync(
          @Nullable UInteger fileHandle,
          @Nullable Boolean requireCompleteUpdate,
          @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
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
   * @param fileHandle ; the supplied payload may be null.
   * @param requireCompleteUpdate ; the supplied payload may be null.
   * @param configurationReferences ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends PubSubConfigurationTypeCloseAndUpdateOutputs>>
      callCloseAndUpdateDetailedAsync(
          MethodCallOptions options,
          @Nullable UInteger fileHandle,
          @Nullable Boolean requireCompleteUpdate,
          @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences);
}
