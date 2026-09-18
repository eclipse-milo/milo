package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the DialogConditionType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.2">Model
 *     documentation</a>
 */
public interface DialogConditionType extends ConditionType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2830L);

  QualifiedProperty<Integer> OkResponse_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "OkResponse",
          ExpandedNodeId.of(Namespaces.OPC_UA, 6L),
          -1,
          Integer.class);

  QualifiedProperty<Integer> LastResponse_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastResponse",
          ExpandedNodeId.of(Namespaces.OPC_UA, 6L),
          -1,
          Integer.class);

  QualifiedProperty<Integer> CancelResponse_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CancelResponse",
          ExpandedNodeId.of(Namespaces.OPC_UA, 6L),
          -1,
          Integer.class);

  QualifiedProperty<Integer> DefaultResponse_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DefaultResponse",
          ExpandedNodeId.of(Namespaces.OPC_UA, 6L),
          -1,
          Integer.class);

  QualifiedProperty<LocalizedText[]> ResponseOptionSet_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ResponseOptionSet",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          1,
          LocalizedText[].class);

  QualifiedProperty<LocalizedText> Prompt_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Prompt",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          -1,
          LocalizedText.class);

  /**
   * Resolves the mandatory OkResponse child, a PropertyType with DataType Int32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getOkResponseNode() throws UaException;

  /** Asynchronous form of {@link #getOkResponseNode()}. */
  CompletableFuture<? extends PropertyType> getOkResponseNodeAsync();

  /**
   * Reads the Value of the OkResponse child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Integer readOkResponse() throws UaException;

  /**
   * Writes the Value of the OkResponse child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeOkResponse(@Nullable Integer value) throws UaException;

  /** Asynchronous form of {@link #readOkResponse()}. */
  CompletableFuture<? extends @Nullable Integer> readOkResponseAsync();

  /** Asynchronous form of {@link #writeOkResponse}; completes with the operation status. */
  CompletableFuture<StatusCode> writeOkResponseAsync(@Nullable Integer value);

  /**
   * Resolves the mandatory DialogState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  TwoStateVariableType getDialogStateNode() throws UaException;

  /** Asynchronous form of {@link #getDialogStateNode()}. */
  CompletableFuture<? extends TwoStateVariableType> getDialogStateNodeAsync();

  /**
   * Reads the Value of the DialogState child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readDialogState() throws UaException;

  /**
   * Writes the Value of the DialogState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDialogState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readDialogState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readDialogStateAsync();

  /** Asynchronous form of {@link #writeDialogState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDialogStateAsync(@Nullable LocalizedText value);

  /**
   * Resolves the mandatory EnabledState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  TwoStateVariableType getEnabledStateNode() throws UaException;

  /** Asynchronous form of {@link #getEnabledStateNode()}. */
  CompletableFuture<? extends TwoStateVariableType> getEnabledStateNodeAsync();

  /**
   * Resolves the mandatory LastResponse child, a PropertyType with DataType Int32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getLastResponseNode() throws UaException;

  /** Asynchronous form of {@link #getLastResponseNode()}. */
  CompletableFuture<? extends PropertyType> getLastResponseNodeAsync();

  /**
   * Reads the Value of the LastResponse child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Integer readLastResponse() throws UaException;

  /**
   * Writes the Value of the LastResponse child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastResponse(@Nullable Integer value) throws UaException;

  /** Asynchronous form of {@link #readLastResponse()}. */
  CompletableFuture<? extends @Nullable Integer> readLastResponseAsync();

  /** Asynchronous form of {@link #writeLastResponse}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLastResponseAsync(@Nullable Integer value);

  /**
   * Resolves the mandatory CancelResponse child, a PropertyType with DataType Int32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getCancelResponseNode() throws UaException;

  /** Asynchronous form of {@link #getCancelResponseNode()}. */
  CompletableFuture<? extends PropertyType> getCancelResponseNodeAsync();

  /**
   * Reads the Value of the CancelResponse child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Integer readCancelResponse() throws UaException;

  /**
   * Writes the Value of the CancelResponse child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCancelResponse(@Nullable Integer value) throws UaException;

  /** Asynchronous form of {@link #readCancelResponse()}. */
  CompletableFuture<? extends @Nullable Integer> readCancelResponseAsync();

  /** Asynchronous form of {@link #writeCancelResponse}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCancelResponseAsync(@Nullable Integer value);

  /**
   * Resolves the mandatory DefaultResponse child, a PropertyType with DataType Int32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDefaultResponseNode() throws UaException;

  /** Asynchronous form of {@link #getDefaultResponseNode()}. */
  CompletableFuture<? extends PropertyType> getDefaultResponseNodeAsync();

  /**
   * Reads the Value of the DefaultResponse child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Integer readDefaultResponse() throws UaException;

  /**
   * Writes the Value of the DefaultResponse child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDefaultResponse(@Nullable Integer value) throws UaException;

  /** Asynchronous form of {@link #readDefaultResponse()}. */
  CompletableFuture<? extends @Nullable Integer> readDefaultResponseAsync();

  /** Asynchronous form of {@link #writeDefaultResponse}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDefaultResponseAsync(@Nullable Integer value);

  /**
   * Resolves the mandatory ResponseOptionSet child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getResponseOptionSetNode() throws UaException;

  /** Asynchronous form of {@link #getResponseOptionSetNode()}. */
  CompletableFuture<? extends PropertyType> getResponseOptionSetNodeAsync();

  /**
   * Reads the Value of the ResponseOptionSet child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  LocalizedText @Nullable [] readResponseOptionSet() throws UaException;

  /**
   * Writes the Value of the ResponseOptionSet child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeResponseOptionSet(LocalizedText @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readResponseOptionSet()}. */
  CompletableFuture<? extends LocalizedText @Nullable []> readResponseOptionSetAsync();

  /** Asynchronous form of {@link #writeResponseOptionSet}; completes with the operation status. */
  CompletableFuture<StatusCode> writeResponseOptionSetAsync(LocalizedText @Nullable [] value);

  /**
   * Resolves the mandatory Prompt child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPromptNode() throws UaException;

  /** Asynchronous form of {@link #getPromptNode()}. */
  CompletableFuture<? extends PropertyType> getPromptNodeAsync();

  /**
   * Reads the Value of the Prompt child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readPrompt() throws UaException;

  /**
   * Writes the Value of the Prompt child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePrompt(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readPrompt()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readPromptAsync();

  /** Asynchronous form of {@link #writePrompt}; completes with the operation status. */
  CompletableFuture<StatusCode> writePromptAsync(@Nullable LocalizedText value);

  /**
   * Resolves the mandatory Respond Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3">Model
   *     documentation</a>
   */
  UaMethodNode getRespondMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRespondMethodNode()}. */
  CompletableFuture<UaMethodNode> getRespondMethodNodeAsync();

  /**
   * Calls the Respond Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3">Model
   *     documentation</a>
   */
  void respond(@Nullable Integer selectedResponse) throws UaException;

  /**
   * Calls the Respond Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRespond(@Nullable Integer selectedResponse) throws UaException;

  /**
   * Calls the Respond Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRespondWith(
      MethodCallOptions options, @Nullable Integer selectedResponse) throws UaException;

  /** Asynchronous form of {@link #respond}. */
  CompletableFuture<Void> respondAsync(@Nullable Integer selectedResponse);

  /** Asynchronous form of {@link #callRespond}. */
  CompletableFuture<MethodCallResult<Void>> callRespondAsync(@Nullable Integer selectedResponse);

  /** Asynchronous form of {@link #callRespondWith}. */
  CompletableFuture<MethodCallResult<Void>> callRespondWithAsync(
      MethodCallOptions options, @Nullable Integer selectedResponse);

  /**
   * Resolves the optional Respond2 Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRespond2MethodNode() throws UaException;

  /** Asynchronous form of {@link #getRespond2MethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRespond2MethodNodeAsync();

  /**
   * Calls the Respond2 Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4">Model
   *     documentation</a>
   */
  void respond2(@Nullable Integer selectedResponse, @Nullable LocalizedText comment)
      throws UaException;

  /**
   * Calls the Respond2 Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRespond2(
      @Nullable Integer selectedResponse, @Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the Respond2 Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRespond2With(
      MethodCallOptions options,
      @Nullable Integer selectedResponse,
      @Nullable LocalizedText comment)
      throws UaException;

  /** Asynchronous form of {@link #respond2}. */
  CompletableFuture<Void> respond2Async(
      @Nullable Integer selectedResponse, @Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callRespond2}. */
  CompletableFuture<MethodCallResult<Void>> callRespond2Async(
      @Nullable Integer selectedResponse, @Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callRespond2With}. */
  CompletableFuture<MethodCallResult<Void>> callRespond2WithAsync(
      MethodCallOptions options,
      @Nullable Integer selectedResponse,
      @Nullable LocalizedText comment);
}
