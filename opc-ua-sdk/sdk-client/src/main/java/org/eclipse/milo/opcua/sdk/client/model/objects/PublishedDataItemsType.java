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
import org.eclipse.milo.opcua.sdk.core.model.methods.PublishedDataItemsTypeAddVariablesOutputs;
import org.eclipse.milo.opcua.sdk.core.model.methods.PublishedDataItemsTypeRemoveVariablesOutputs;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PublishedDataItemsType extends PublishedDataSetType {
  QualifiedProperty<PublishedVariableDataType[]> PUBLISHED_DATA =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublishedData",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14273"),
          1,
          PublishedVariableDataType[].class);

  /** Gets the existing node's local value. */
  @Nullable PublishedVariableDataType @Nullable [] getPublishedData() throws UaException;

  /** Sets the existing node's local value. */
  void setPublishedData(@Nullable PublishedVariableDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable PublishedVariableDataType @Nullable [] readPublishedData() throws UaException;

  /** Writes the value remotely. */
  void writePublishedData(@Nullable PublishedVariableDataType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable PublishedVariableDataType @Nullable []>
      readPublishedDataAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePublishedDataAsync(
      @Nullable PublishedVariableDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPublishedDataNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPublishedDataNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getAddVariablesMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddVariablesMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2
   *
   * <p>Invokes <code>AddVariables</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  PublishedDataItemsTypeAddVariablesOutputs callAddVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      @Nullable Boolean @Nullable [] promotedFields,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2
   *
   * <p>Invokes <code>AddVariables</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends PublishedDataItemsTypeAddVariablesOutputs> callAddVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      @Nullable Boolean @Nullable [] promotedFields,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2
   *
   * <p>Invokes <code>AddVariables</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends PublishedDataItemsTypeAddVariablesOutputs> callAddVariablesDetailed(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      @Nullable Boolean @Nullable [] promotedFields,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2
   *
   * <p>Invokes <code>AddVariables</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends PublishedDataItemsTypeAddVariablesOutputs> callAddVariablesDetailed(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      @Nullable Boolean @Nullable [] promotedFields,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2
   *
   * <p>Invokes <code>AddVariables</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends PublishedDataItemsTypeAddVariablesOutputs>>
      callAddVariablesDetailedAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable Boolean @Nullable [] promotedFields,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2
   *
   * <p>Invokes <code>AddVariables</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends PublishedDataItemsTypeAddVariablesOutputs>>
      callAddVariablesDetailedAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable Boolean @Nullable [] promotedFields,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getRemoveVariablesMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemoveVariablesMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3
   *
   * <p>Invokes <code>RemoveVariables</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  PublishedDataItemsTypeRemoveVariablesOutputs callRemoveVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable UInteger @Nullable [] variablesToRemove)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3
   *
   * <p>Invokes <code>RemoveVariables</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends PublishedDataItemsTypeRemoveVariablesOutputs>
      callRemoveVariablesAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable UInteger @Nullable [] variablesToRemove);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3
   *
   * <p>Invokes <code>RemoveVariables</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends PublishedDataItemsTypeRemoveVariablesOutputs>
      callRemoveVariablesDetailed(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable UInteger @Nullable [] variablesToRemove)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3
   *
   * <p>Invokes <code>RemoveVariables</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends PublishedDataItemsTypeRemoveVariablesOutputs>
      callRemoveVariablesDetailed(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable UInteger @Nullable [] variablesToRemove)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3
   *
   * <p>Invokes <code>RemoveVariables</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends PublishedDataItemsTypeRemoveVariablesOutputs>>
      callRemoveVariablesDetailedAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable UInteger @Nullable [] variablesToRemove);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3
   *
   * <p>Invokes <code>RemoveVariables</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends PublishedDataItemsTypeRemoveVariablesOutputs>>
      callRemoveVariablesDetailedAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable UInteger @Nullable [] variablesToRemove);
}
