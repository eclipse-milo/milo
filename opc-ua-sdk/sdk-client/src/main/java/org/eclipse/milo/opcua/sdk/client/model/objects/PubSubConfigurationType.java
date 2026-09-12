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
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PubSubConfigurationType extends FileType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getReserveIdsMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getReserveIdsMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5
   *
   * <p>Invokes <code>ReserveIds</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
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
   * Requires Good operation status.
   *
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
   * Retains the operation status, diagnostics and outputs.
   *
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
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
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
   * Retains the operation status, diagnostics and outputs.
   *
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
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
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
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getCloseAndUpdateMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getCloseAndUpdateMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
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
   * contract. Requires Good operation status.
   *
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
   * contract. Retains the operation status, diagnostics and outputs.
   *
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
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
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
   * contract. Retains the operation status, diagnostics and outputs.
   *
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
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
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
