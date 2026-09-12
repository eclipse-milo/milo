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
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.PriorityMappingEntryType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PriorityMappingTableType extends BaseObjectType {
  QualifiedProperty<PriorityMappingEntryType[]> PRIORITY_MAPPPING_ENTRIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PriorityMapppingEntries",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=25220"),
          1,
          PriorityMappingEntryType[].class);

  /** Gets the existing node's local value. */
  @Nullable PriorityMappingEntryType @Nullable [] getPriorityMapppingEntries() throws UaException;

  /** Sets the existing node's local value. */
  void setPriorityMapppingEntries(@Nullable PriorityMappingEntryType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable PriorityMappingEntryType @Nullable [] readPriorityMapppingEntries() throws UaException;

  /** Writes the value remotely. */
  void writePriorityMapppingEntries(@Nullable PriorityMappingEntryType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable PriorityMappingEntryType @Nullable []>
      readPriorityMapppingEntriesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePriorityMapppingEntriesAsync(
      @Nullable PriorityMappingEntryType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPriorityMapppingEntriesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPriorityMapppingEntriesNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getAddPriorityMappingEntryMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddPriorityMappingEntryMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
   *
   * <p>Invokes <code>AddPriorityMappingEntry</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callAddPriorityMappingEntry(
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValuePcp,
      @Nullable UInteger priorityValueDscp)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
   *
   * <p>Invokes <code>AddPriorityMappingEntry</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callAddPriorityMappingEntryAsync(
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValuePcp,
      @Nullable UInteger priorityValueDscp);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
   *
   * <p>Invokes <code>AddPriorityMappingEntry</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAddPriorityMappingEntryDetailed(
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValuePcp,
      @Nullable UInteger priorityValueDscp)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
   *
   * <p>Invokes <code>AddPriorityMappingEntry</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAddPriorityMappingEntryDetailed(
      MethodCallOptions options,
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValuePcp,
      @Nullable UInteger priorityValueDscp)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
   *
   * <p>Invokes <code>AddPriorityMappingEntry</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAddPriorityMappingEntryDetailedAsync(
          @Nullable String mappingUri,
          @Nullable String priorityLabel,
          @Nullable UByte priorityValuePcp,
          @Nullable UInteger priorityValueDscp);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
   *
   * <p>Invokes <code>AddPriorityMappingEntry</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAddPriorityMappingEntryDetailedAsync(
          MethodCallOptions options,
          @Nullable String mappingUri,
          @Nullable String priorityLabel,
          @Nullable UByte priorityValuePcp,
          @Nullable UInteger priorityValueDscp);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getDeletePriorityMappingEntryMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode>
      getDeletePriorityMappingEntryMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
   *
   * <p>Invokes <code>DeletePriorityMappingEntry</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callDeletePriorityMappingEntry(@Nullable String mappingUri, @Nullable String priorityLabel)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
   *
   * <p>Invokes <code>DeletePriorityMappingEntry</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callDeletePriorityMappingEntryAsync(
      @Nullable String mappingUri, @Nullable String priorityLabel);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
   *
   * <p>Invokes <code>DeletePriorityMappingEntry</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callDeletePriorityMappingEntryDetailed(
      @Nullable String mappingUri, @Nullable String priorityLabel) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
   *
   * <p>Invokes <code>DeletePriorityMappingEntry</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callDeletePriorityMappingEntryDetailed(
      MethodCallOptions options, @Nullable String mappingUri, @Nullable String priorityLabel)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
   *
   * <p>Invokes <code>DeletePriorityMappingEntry</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callDeletePriorityMappingEntryDetailedAsync(
          @Nullable String mappingUri, @Nullable String priorityLabel);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
   *
   * <p>Invokes <code>DeletePriorityMappingEntry</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callDeletePriorityMappingEntryDetailedAsync(
          MethodCallOptions options, @Nullable String mappingUri, @Nullable String priorityLabel);
}
