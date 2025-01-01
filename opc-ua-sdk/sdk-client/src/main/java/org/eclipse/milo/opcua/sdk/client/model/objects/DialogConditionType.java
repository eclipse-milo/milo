/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.2</a>
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

  /**
   * Get the local value of the Prompt Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Prompt Node.
   * @throws UaException if an error occurs creating or getting the Prompt Node.
   */
  LocalizedText getPrompt() throws UaException;

  /**
   * Set the local value of the Prompt Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Prompt Node.
   * @throws UaException if an error occurs creating or getting the Prompt Node.
   */
  void setPrompt(LocalizedText value) throws UaException;

  /**
   * Read the value of the Prompt Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readPrompt() throws UaException;

  /**
   * Write a new value for the Prompt Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePrompt(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPrompt}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readPromptAsync();

  /**
   * An asynchronous implementation of {@link #writePrompt}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePromptAsync(LocalizedText value);

  /**
   * Get the Prompt {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Prompt {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getPromptNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPromptNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getPromptNodeAsync();

  /**
   * Get the local value of the ResponseOptionSet Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ResponseOptionSet Node.
   * @throws UaException if an error occurs creating or getting the ResponseOptionSet Node.
   */
  LocalizedText[] getResponseOptionSet() throws UaException;

  /**
   * Set the local value of the ResponseOptionSet Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ResponseOptionSet Node.
   * @throws UaException if an error occurs creating or getting the ResponseOptionSet Node.
   */
  void setResponseOptionSet(LocalizedText[] value) throws UaException;

  /**
   * Read the value of the ResponseOptionSet Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText[] readResponseOptionSet() throws UaException;

  /**
   * Write a new value for the ResponseOptionSet Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link LocalizedText[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeResponseOptionSet(LocalizedText[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readResponseOptionSet}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText[]> readResponseOptionSetAsync();

  /**
   * An asynchronous implementation of {@link #writeResponseOptionSet}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeResponseOptionSetAsync(LocalizedText[] value);

  /**
   * Get the ResponseOptionSet {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ResponseOptionSet {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getResponseOptionSetNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getResponseOptionSetNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getResponseOptionSetNodeAsync();

  /**
   * Get the local value of the DefaultResponse Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DefaultResponse Node.
   * @throws UaException if an error occurs creating or getting the DefaultResponse Node.
   */
  Integer getDefaultResponse() throws UaException;

  /**
   * Set the local value of the DefaultResponse Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DefaultResponse Node.
   * @throws UaException if an error occurs creating or getting the DefaultResponse Node.
   */
  void setDefaultResponse(Integer value) throws UaException;

  /**
   * Read the value of the DefaultResponse Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Integer} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Integer readDefaultResponse() throws UaException;

  /**
   * Write a new value for the DefaultResponse Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Integer} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDefaultResponse(Integer value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDefaultResponse}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Integer> readDefaultResponseAsync();

  /**
   * An asynchronous implementation of {@link #writeDefaultResponse}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDefaultResponseAsync(Integer value);

  /**
   * Get the DefaultResponse {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DefaultResponse {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDefaultResponseNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDefaultResponseNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDefaultResponseNodeAsync();

  /**
   * Get the local value of the OkResponse Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the OkResponse Node.
   * @throws UaException if an error occurs creating or getting the OkResponse Node.
   */
  Integer getOkResponse() throws UaException;

  /**
   * Set the local value of the OkResponse Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the OkResponse Node.
   * @throws UaException if an error occurs creating or getting the OkResponse Node.
   */
  void setOkResponse(Integer value) throws UaException;

  /**
   * Read the value of the OkResponse Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Integer} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Integer readOkResponse() throws UaException;

  /**
   * Write a new value for the OkResponse Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Integer} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeOkResponse(Integer value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readOkResponse}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Integer> readOkResponseAsync();

  /**
   * An asynchronous implementation of {@link #writeOkResponse}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeOkResponseAsync(Integer value);

  /**
   * Get the OkResponse {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the OkResponse {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getOkResponseNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOkResponseNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getOkResponseNodeAsync();

  /**
   * Get the local value of the CancelResponse Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CancelResponse Node.
   * @throws UaException if an error occurs creating or getting the CancelResponse Node.
   */
  Integer getCancelResponse() throws UaException;

  /**
   * Set the local value of the CancelResponse Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CancelResponse Node.
   * @throws UaException if an error occurs creating or getting the CancelResponse Node.
   */
  void setCancelResponse(Integer value) throws UaException;

  /**
   * Read the value of the CancelResponse Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Integer} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Integer readCancelResponse() throws UaException;

  /**
   * Write a new value for the CancelResponse Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Integer} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCancelResponse(Integer value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCancelResponse}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Integer> readCancelResponseAsync();

  /**
   * An asynchronous implementation of {@link #writeCancelResponse}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCancelResponseAsync(Integer value);

  /**
   * Get the CancelResponse {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CancelResponse {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getCancelResponseNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCancelResponseNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getCancelResponseNodeAsync();

  /**
   * Get the local value of the LastResponse Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastResponse Node.
   * @throws UaException if an error occurs creating or getting the LastResponse Node.
   */
  Integer getLastResponse() throws UaException;

  /**
   * Set the local value of the LastResponse Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastResponse Node.
   * @throws UaException if an error occurs creating or getting the LastResponse Node.
   */
  void setLastResponse(Integer value) throws UaException;

  /**
   * Read the value of the LastResponse Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Integer} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Integer readLastResponse() throws UaException;

  /**
   * Write a new value for the LastResponse Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Integer} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastResponse(Integer value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastResponse}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Integer> readLastResponseAsync();

  /**
   * An asynchronous implementation of {@link #writeLastResponse}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastResponseAsync(Integer value);

  /**
   * Get the LastResponse {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastResponse {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLastResponseNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastResponseNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLastResponseNodeAsync();

  /**
   * Get the local value of the EnabledState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EnabledState Node.
   * @throws UaException if an error occurs creating or getting the EnabledState Node.
   */
  LocalizedText getEnabledState() throws UaException;

  /**
   * Set the local value of the EnabledState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EnabledState Node.
   * @throws UaException if an error occurs creating or getting the EnabledState Node.
   */
  void setEnabledState(LocalizedText value) throws UaException;

  /**
   * Read the value of the EnabledState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readEnabledState() throws UaException;

  /**
   * Write a new value for the EnabledState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEnabledState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEnabledState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readEnabledStateAsync();

  /**
   * An asynchronous implementation of {@link #writeEnabledState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEnabledStateAsync(LocalizedText value);

  /**
   * Get the EnabledState {@link TwoStateVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EnabledState {@link TwoStateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TwoStateVariableType getEnabledStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEnabledStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TwoStateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TwoStateVariableType> getEnabledStateNodeAsync();

  /**
   * Get the local value of the DialogState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DialogState Node.
   * @throws UaException if an error occurs creating or getting the DialogState Node.
   */
  LocalizedText getDialogState() throws UaException;

  /**
   * Set the local value of the DialogState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DialogState Node.
   * @throws UaException if an error occurs creating or getting the DialogState Node.
   */
  void setDialogState(LocalizedText value) throws UaException;

  /**
   * Read the value of the DialogState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readDialogState() throws UaException;

  /**
   * Write a new value for the DialogState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDialogState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDialogState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readDialogStateAsync();

  /**
   * An asynchronous implementation of {@link #writeDialogState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDialogStateAsync(LocalizedText value);

  /**
   * Get the DialogState {@link TwoStateVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DialogState {@link TwoStateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TwoStateVariableType getDialogStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDialogStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TwoStateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TwoStateVariableType> getDialogStateNodeAsync();
}
