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
import org.eclipse.milo.opcua.sdk.core.model.methods.LogObjectTypeGetRecordsOutputs;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.LogRecordMask;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.2">https://reference.opcfoundation.org/v105/Core/docs/Part26/5.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface LogObjectType extends BaseObjectType {
  QualifiedProperty<UInteger> MAX_RECORDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxRecords",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<Double> MAX_STORAGE_DURATION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxStorageDuration",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<UShort> MINIMUM_SEVERITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MinimumSeverity",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxRecords() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxRecords(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxRecords() throws UaException;

  /** Writes the value remotely. */
  void writeMaxRecords(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxRecordsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxRecordsAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxRecordsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxRecordsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getMaxStorageDuration() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxStorageDuration(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readMaxStorageDuration() throws UaException;

  /** Writes the value remotely. */
  void writeMaxStorageDuration(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readMaxStorageDurationAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxStorageDurationAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxStorageDurationNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxStorageDurationNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getMinimumSeverity() throws UaException;

  /** Sets the existing node's local value. */
  void setMinimumSeverity(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readMinimumSeverity() throws UaException;

  /** Writes the value remotely. */
  void writeMinimumSeverity(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readMinimumSeverityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMinimumSeverityAsync(@Nullable UShort value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMinimumSeverityNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMinimumSeverityNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getGetRecordsMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getGetRecordsMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3
   *
   * <p>Invokes <code>GetRecords</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  LogObjectTypeGetRecordsOutputs callGetRecords(
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3
   *
   * <p>Invokes <code>GetRecords</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends LogObjectTypeGetRecordsOutputs> callGetRecordsAsync(
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3
   *
   * <p>Invokes <code>GetRecords</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends LogObjectTypeGetRecordsOutputs> callGetRecordsDetailed(
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3
   *
   * <p>Invokes <code>GetRecords</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends LogObjectTypeGetRecordsOutputs> callGetRecordsDetailed(
      MethodCallOptions options,
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3
   *
   * <p>Invokes <code>GetRecords</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends LogObjectTypeGetRecordsOutputs>>
      callGetRecordsDetailedAsync(
          @Nullable DateTime startTime,
          @Nullable DateTime endTime,
          @Nullable UInteger maxReturnRecords,
          @Nullable UShort minimumSeverity,
          @Nullable LogRecordMask requestMask,
          @Nullable ByteString continuationPointIn);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3
   *
   * <p>Invokes <code>GetRecords</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends LogObjectTypeGetRecordsOutputs>>
      callGetRecordsDetailedAsync(
          MethodCallOptions options,
          @Nullable DateTime startTime,
          @Nullable DateTime endTime,
          @Nullable UInteger maxReturnRecords,
          @Nullable UShort minimumSeverity,
          @Nullable LogRecordMask requestMask,
          @Nullable ByteString continuationPointIn);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getReleaseContinuationPointMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getReleaseContinuationPointMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4
   *
   * <p>Invokes <code>ReleaseContinuationPoint</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callReleaseContinuationPoint(@Nullable ByteString continuationPointIn) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4
   *
   * <p>Invokes <code>ReleaseContinuationPoint</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callReleaseContinuationPointAsync(
      @Nullable ByteString continuationPointIn);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4
   *
   * <p>Invokes <code>ReleaseContinuationPoint</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callReleaseContinuationPointDetailed(
      @Nullable ByteString continuationPointIn) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4
   *
   * <p>Invokes <code>ReleaseContinuationPoint</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callReleaseContinuationPointDetailed(
      MethodCallOptions options, @Nullable ByteString continuationPointIn) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4
   *
   * <p>Invokes <code>ReleaseContinuationPoint</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callReleaseContinuationPointDetailedAsync(@Nullable ByteString continuationPointIn);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4
   *
   * <p>Invokes <code>ReleaseContinuationPoint</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callReleaseContinuationPointDetailedAsync(
          MethodCallOptions options, @Nullable ByteString continuationPointIn);
}
