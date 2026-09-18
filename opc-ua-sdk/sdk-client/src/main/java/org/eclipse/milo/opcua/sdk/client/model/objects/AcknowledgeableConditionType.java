package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AcknowledgeableConditionType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.2">Model
 *     documentation</a>
 */
public interface AcknowledgeableConditionType extends ConditionType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2881L);

  /**
   * Resolves the mandatory AckedState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  TwoStateVariableType getAckedStateNode() throws UaException;

  /** Asynchronous form of {@link #getAckedStateNode()}. */
  CompletableFuture<? extends TwoStateVariableType> getAckedStateNodeAsync();

  /**
   * Reads the Value of the AckedState child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readAckedState() throws UaException;

  /**
   * Writes the Value of the AckedState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAckedState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readAckedState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readAckedStateAsync();

  /** Asynchronous form of {@link #writeAckedState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAckedStateAsync(@Nullable LocalizedText value);

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
   * Resolves the optional ConfirmedState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableType getConfirmedStateNode() throws UaException;

  /** Asynchronous form of {@link #getConfirmedStateNode()}. */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getConfirmedStateNodeAsync();

  /**
   * Reads the Value of the ConfirmedState child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readConfirmedState() throws UaException;

  /**
   * Writes the Value of the ConfirmedState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConfirmedState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readConfirmedState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readConfirmedStateAsync();

  /** Asynchronous form of {@link #writeConfirmedState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeConfirmedStateAsync(@Nullable LocalizedText value);

  /**
   * Resolves the mandatory Acknowledge Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3">Model
   *     documentation</a>
   */
  UaMethodNode getAcknowledgeMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAcknowledgeMethodNode()}. */
  CompletableFuture<UaMethodNode> getAcknowledgeMethodNodeAsync();

  /**
   * Calls the Acknowledge Method and returns its outputs; requires a Good result.
   *
   * @param eventId the identifier for the event to comment.
   * @param comment the comment to add to the condition.
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3">Model
   *     documentation</a>
   */
  void acknowledge(@Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException;

  /**
   * Calls the Acknowledge Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAcknowledge(
      @Nullable ByteString eventId, @Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the Acknowledge Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAcknowledgeWith(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException;

  /** Asynchronous form of {@link #acknowledge}. */
  CompletableFuture<Void> acknowledgeAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callAcknowledge}. */
  CompletableFuture<MethodCallResult<Void>> callAcknowledgeAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callAcknowledgeWith}. */
  CompletableFuture<MethodCallResult<Void>> callAcknowledgeWithAsync(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment);

  /**
   * Resolves the optional Confirm Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getConfirmMethodNode() throws UaException;

  /** Asynchronous form of {@link #getConfirmMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getConfirmMethodNodeAsync();

  /**
   * Calls the Confirm Method and returns its outputs; requires a Good result.
   *
   * @param eventId the identifier for the event to comment.
   * @param comment the comment to add to the condition.
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4">Model
   *     documentation</a>
   */
  void confirm(@Nullable ByteString eventId, @Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the Confirm Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callConfirm(@Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException;

  /**
   * Calls the Confirm Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callConfirmWith(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException;

  /** Asynchronous form of {@link #confirm}. */
  CompletableFuture<Void> confirmAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callConfirm}. */
  CompletableFuture<MethodCallResult<Void>> callConfirmAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callConfirmWith}. */
  CompletableFuture<MethodCallResult<Void>> callConfirmWithAsync(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment);
}
