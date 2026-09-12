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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameVerboseDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AliasNameCategoryType extends FolderType {
  QualifiedProperty<UInteger> LAST_CHANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastChange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  /** Gets the existing node's local value. */
  @Nullable UInteger getLastChange() throws UaException;

  /** Sets the existing node's local value. */
  void setLastChange(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readLastChange() throws UaException;

  /** Writes the value remotely. */
  void writeLastChange(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readLastChangeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastChangeAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLastChangeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getLastChangeNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getFindAliasMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getFindAliasMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2
   *
   * <p>Invokes <code>FindAlias</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable AliasNameDataType @Nullable [] callFindAlias(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2
   *
   * <p>Invokes <code>FindAlias</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable AliasNameDataType @Nullable []> callFindAliasAsync(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2
   *
   * <p>Invokes <code>FindAlias</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable AliasNameDataType @Nullable []> callFindAliasDetailed(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2
   *
   * <p>Invokes <code>FindAlias</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable AliasNameDataType @Nullable []> callFindAliasDetailed(
      MethodCallOptions options,
      @Nullable String aliasNameSearchPattern,
      @Nullable NodeId referenceTypeFilter)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2
   *
   * <p>Invokes <code>FindAlias</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable AliasNameDataType @Nullable []>>
      callFindAliasDetailedAsync(
          @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2
   *
   * <p>Invokes <code>FindAlias</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable AliasNameDataType @Nullable []>>
      callFindAliasDetailedAsync(
          MethodCallOptions options,
          @Nullable String aliasNameSearchPattern,
          @Nullable NodeId referenceTypeFilter);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getFindAliasVerboseMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getFindAliasVerboseMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3
   *
   * <p>Invokes <code>FindAliasVerbose</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable AliasNameVerboseDataType @Nullable [] callFindAliasVerbose(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3
   *
   * <p>Invokes <code>FindAliasVerbose</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable AliasNameVerboseDataType @Nullable []>
      callFindAliasVerboseAsync(
          @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3
   *
   * <p>Invokes <code>FindAliasVerbose</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable AliasNameVerboseDataType @Nullable []>
      callFindAliasVerboseDetailed(
          @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3
   *
   * <p>Invokes <code>FindAliasVerbose</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable AliasNameVerboseDataType @Nullable []>
      callFindAliasVerboseDetailed(
          MethodCallOptions options,
          @Nullable String aliasNameSearchPattern,
          @Nullable NodeId referenceTypeFilter)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3
   *
   * <p>Invokes <code>FindAliasVerbose</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends @Nullable AliasNameVerboseDataType @Nullable []>>
      callFindAliasVerboseDetailedAsync(
          @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3
   *
   * <p>Invokes <code>FindAliasVerbose</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends @Nullable AliasNameVerboseDataType @Nullable []>>
      callFindAliasVerboseDetailedAsync(
          MethodCallOptions options,
          @Nullable String aliasNameSearchPattern,
          @Nullable NodeId referenceTypeFilter);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getAddAliasesToCategoryMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddAliasesToCategoryMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4
   *
   * <p>Invokes <code>AddAliasesToCategory</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable StatusCode @Nullable [] callAddAliasesToCategory(
      @Nullable String @Nullable [] aliasNames,
      @Nullable ExpandedNodeId @Nullable [] targetNodes,
      @Nullable String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4
   *
   * <p>Invokes <code>AddAliasesToCategory</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable StatusCode @Nullable []> callAddAliasesToCategoryAsync(
      @Nullable String @Nullable [] aliasNames,
      @Nullable ExpandedNodeId @Nullable [] targetNodes,
      @Nullable String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4
   *
   * <p>Invokes <code>AddAliasesToCategory</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable StatusCode @Nullable []> callAddAliasesToCategoryDetailed(
      @Nullable String @Nullable [] aliasNames,
      @Nullable ExpandedNodeId @Nullable [] targetNodes,
      @Nullable String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4
   *
   * <p>Invokes <code>AddAliasesToCategory</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable StatusCode @Nullable []> callAddAliasesToCategoryDetailed(
      MethodCallOptions options,
      @Nullable String @Nullable [] aliasNames,
      @Nullable ExpandedNodeId @Nullable [] targetNodes,
      @Nullable String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4
   *
   * <p>Invokes <code>AddAliasesToCategory</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callAddAliasesToCategoryDetailedAsync(
          @Nullable String @Nullable [] aliasNames,
          @Nullable ExpandedNodeId @Nullable [] targetNodes,
          @Nullable String @Nullable [] targetServers,
          @Nullable NodeId targetReferenceType);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4
   *
   * <p>Invokes <code>AddAliasesToCategory</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callAddAliasesToCategoryDetailedAsync(
          MethodCallOptions options,
          @Nullable String @Nullable [] aliasNames,
          @Nullable ExpandedNodeId @Nullable [] targetNodes,
          @Nullable String @Nullable [] targetServers,
          @Nullable NodeId targetReferenceType);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getDeleteAliasesFromCategoryMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getDeleteAliasesFromCategoryMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5
   *
   * <p>Invokes <code>DeleteAliasesFromCategory</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable StatusCode @Nullable [] callDeleteAliasesFromCategory(
      @Nullable String @Nullable [] aliasNames, @Nullable ExpandedNodeId @Nullable [] targetNodes)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5
   *
   * <p>Invokes <code>DeleteAliasesFromCategory</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable StatusCode @Nullable []> callDeleteAliasesFromCategoryAsync(
      @Nullable String @Nullable [] aliasNames, @Nullable ExpandedNodeId @Nullable [] targetNodes);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5
   *
   * <p>Invokes <code>DeleteAliasesFromCategory</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable StatusCode @Nullable []>
      callDeleteAliasesFromCategoryDetailed(
          @Nullable String @Nullable [] aliasNames,
          @Nullable ExpandedNodeId @Nullable [] targetNodes)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5
   *
   * <p>Invokes <code>DeleteAliasesFromCategory</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable StatusCode @Nullable []>
      callDeleteAliasesFromCategoryDetailed(
          MethodCallOptions options,
          @Nullable String @Nullable [] aliasNames,
          @Nullable ExpandedNodeId @Nullable [] targetNodes)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5
   *
   * <p>Invokes <code>DeleteAliasesFromCategory</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callDeleteAliasesFromCategoryDetailedAsync(
          @Nullable String @Nullable [] aliasNames,
          @Nullable ExpandedNodeId @Nullable [] targetNodes);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5
   *
   * <p>Invokes <code>DeleteAliasesFromCategory</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callDeleteAliasesFromCategoryDetailedAsync(
          MethodCallOptions options,
          @Nullable String @Nullable [] aliasNames,
          @Nullable ExpandedNodeId @Nullable [] targetNodes);
}
