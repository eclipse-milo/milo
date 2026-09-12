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
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface DialogConditionType extends ConditionType {
  QualifiedProperty<LocalizedText> PROMPT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Prompt",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<LocalizedText[]> RESPONSE_OPTION_SET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ResponseOptionSet",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          1,
          LocalizedText[].class);

  QualifiedProperty<Integer> DEFAULT_RESPONSE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DefaultResponse",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6"),
          -1,
          Integer.class);

  QualifiedProperty<Integer> OK_RESPONSE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OkResponse",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6"),
          -1,
          Integer.class);

  QualifiedProperty<Integer> CANCEL_RESPONSE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CancelResponse",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6"),
          -1,
          Integer.class);

  QualifiedProperty<Integer> LAST_RESPONSE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastResponse",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6"),
          -1,
          Integer.class);

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getPrompt() throws UaException;

  /** Sets the existing node's local value. */
  void setPrompt(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readPrompt() throws UaException;

  /** Writes the value remotely. */
  void writePrompt(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readPromptAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePromptAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPromptNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPromptNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText @Nullable [] getResponseOptionSet() throws UaException;

  /** Sets the existing node's local value. */
  void setResponseOptionSet(@Nullable LocalizedText @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText @Nullable [] readResponseOptionSet() throws UaException;

  /** Writes the value remotely. */
  void writeResponseOptionSet(@Nullable LocalizedText @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText @Nullable []> readResponseOptionSetAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeResponseOptionSetAsync(
      @Nullable LocalizedText @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getResponseOptionSetNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getResponseOptionSetNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Integer getDefaultResponse() throws UaException;

  /** Sets the existing node's local value. */
  void setDefaultResponse(@Nullable Integer value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Integer readDefaultResponse() throws UaException;

  /** Writes the value remotely. */
  void writeDefaultResponse(@Nullable Integer value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Integer> readDefaultResponseAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDefaultResponseAsync(@Nullable Integer value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDefaultResponseNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDefaultResponseNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Integer getOkResponse() throws UaException;

  /** Sets the existing node's local value. */
  void setOkResponse(@Nullable Integer value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Integer readOkResponse() throws UaException;

  /** Writes the value remotely. */
  void writeOkResponse(@Nullable Integer value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Integer> readOkResponseAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOkResponseAsync(@Nullable Integer value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getOkResponseNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getOkResponseNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Integer getCancelResponse() throws UaException;

  /** Sets the existing node's local value. */
  void setCancelResponse(@Nullable Integer value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Integer readCancelResponse() throws UaException;

  /** Writes the value remotely. */
  void writeCancelResponse(@Nullable Integer value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Integer> readCancelResponseAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCancelResponseAsync(@Nullable Integer value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCancelResponseNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getCancelResponseNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Integer getLastResponse() throws UaException;

  /** Sets the existing node's local value. */
  void setLastResponse(@Nullable Integer value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Integer readLastResponse() throws UaException;

  /** Writes the value remotely. */
  void writeLastResponse(@Nullable Integer value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Integer> readLastResponseAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastResponseAsync(@Nullable Integer value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastResponseNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getLastResponseNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getEnabledState() throws UaException;

  /** Sets the existing node's local value. */
  void setEnabledState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readEnabledState() throws UaException;

  /** Writes the value remotely. */
  void writeEnabledState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readEnabledStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEnabledStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TwoStateVariableType getEnabledStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TwoStateVariableType> getEnabledStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getDialogState() throws UaException;

  /** Sets the existing node's local value. */
  void setDialogState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readDialogState() throws UaException;

  /** Writes the value remotely. */
  void writeDialogState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readDialogStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDialogStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TwoStateVariableType getDialogStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TwoStateVariableType> getDialogStateNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getRespondMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getRespondMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Invokes <code>Respond</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRespond(@Nullable Integer selectedResponse) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Invokes <code>Respond</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRespondAsync(@Nullable Integer selectedResponse);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Invokes <code>Respond</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRespondDetailed(@Nullable Integer selectedResponse)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Invokes <code>Respond</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRespondDetailed(
      MethodCallOptions options, @Nullable Integer selectedResponse) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Invokes <code>Respond</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callRespondDetailedAsync(
      @Nullable Integer selectedResponse);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Invokes <code>Respond</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callRespondDetailedAsync(
      MethodCallOptions options, @Nullable Integer selectedResponse);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getRespond2MethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRespond2MethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
   *
   * <p>Invokes <code>Respond2</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRespond2(@Nullable Integer selectedResponse, @Nullable LocalizedText comment)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
   *
   * <p>Invokes <code>Respond2</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRespond2Async(
      @Nullable Integer selectedResponse, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
   *
   * <p>Invokes <code>Respond2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRespond2Detailed(
      @Nullable Integer selectedResponse, @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
   *
   * <p>Invokes <code>Respond2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRespond2Detailed(
      MethodCallOptions options,
      @Nullable Integer selectedResponse,
      @Nullable LocalizedText comment)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
   *
   * <p>Invokes <code>Respond2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callRespond2DetailedAsync(
      @Nullable Integer selectedResponse, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
   *
   * <p>Invokes <code>Respond2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callRespond2DetailedAsync(
      MethodCallOptions options,
      @Nullable Integer selectedResponse,
      @Nullable LocalizedText comment);
}
