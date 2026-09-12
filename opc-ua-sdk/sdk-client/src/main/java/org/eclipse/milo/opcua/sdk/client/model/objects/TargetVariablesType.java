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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface TargetVariablesType extends SubscribedDataSetType {
  QualifiedProperty<FieldTargetDataType[]> TARGET_VARIABLES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TargetVariables",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14744"),
          1,
          FieldTargetDataType[].class);

  /** Gets the existing node's local value. */
  @Nullable FieldTargetDataType @Nullable [] getTargetVariables() throws UaException;

  /** Sets the existing node's local value. */
  void setTargetVariables(@Nullable FieldTargetDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable FieldTargetDataType @Nullable [] readTargetVariables() throws UaException;

  /** Writes the value remotely. */
  void writeTargetVariables(@Nullable FieldTargetDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable FieldTargetDataType @Nullable []>
      readTargetVariablesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTargetVariablesAsync(
      @Nullable FieldTargetDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getTargetVariablesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getTargetVariablesNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getAddTargetVariablesMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddTargetVariablesMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2
   *
   * <p>Invokes <code>AddTargetVariables</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable StatusCode @Nullable [] callAddTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2
   *
   * <p>Invokes <code>AddTargetVariables</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable StatusCode @Nullable []> callAddTargetVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2
   *
   * <p>Invokes <code>AddTargetVariables</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable StatusCode @Nullable []> callAddTargetVariablesDetailed(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2
   *
   * <p>Invokes <code>AddTargetVariables</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable StatusCode @Nullable []> callAddTargetVariablesDetailed(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2
   *
   * <p>Invokes <code>AddTargetVariables</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callAddTargetVariablesDetailedAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2
   *
   * <p>Invokes <code>AddTargetVariables</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callAddTargetVariablesDetailedAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getRemoveTargetVariablesMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemoveTargetVariablesMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3
   *
   * <p>Invokes <code>RemoveTargetVariables</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable StatusCode @Nullable [] callRemoveTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable UInteger @Nullable [] targetsToRemove)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3
   *
   * <p>Invokes <code>RemoveTargetVariables</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable StatusCode @Nullable []> callRemoveTargetVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable UInteger @Nullable [] targetsToRemove);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3
   *
   * <p>Invokes <code>RemoveTargetVariables</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable StatusCode @Nullable []> callRemoveTargetVariablesDetailed(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable UInteger @Nullable [] targetsToRemove)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3
   *
   * <p>Invokes <code>RemoveTargetVariables</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable StatusCode @Nullable []> callRemoveTargetVariablesDetailed(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable UInteger @Nullable [] targetsToRemove)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3
   *
   * <p>Invokes <code>RemoveTargetVariables</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callRemoveTargetVariablesDetailedAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable UInteger @Nullable [] targetsToRemove);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3
   *
   * <p>Invokes <code>RemoveTargetVariables</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callRemoveTargetVariablesDetailedAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable UInteger @Nullable [] targetsToRemove);
}
