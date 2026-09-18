package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ShelvedStateMachineType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.1">Model
 *     documentation</a>
 */
public interface ShelvedStateMachineType extends FiniteStateMachineType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2929L);

  QualifiedProperty<Double> UnshelveTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UnshelveTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  /**
   * Resolves the mandatory UnshelveTime child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getUnshelveTimeNode() throws UaException;

  /** Asynchronous form of {@link #getUnshelveTimeNode()}. */
  CompletableFuture<? extends PropertyType> getUnshelveTimeNodeAsync();

  /**
   * Reads the Value of the UnshelveTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readUnshelveTime() throws UaException;

  /**
   * Writes the Value of the UnshelveTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUnshelveTime(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readUnshelveTime()}. */
  CompletableFuture<? extends @Nullable Double> readUnshelveTimeAsync();

  /** Asynchronous form of {@link #writeUnshelveTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeUnshelveTimeAsync(@Nullable Double value);

  /**
   * Resolves the mandatory OneShotShelve Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6">Model
   *     documentation</a>
   */
  UaMethodNode getOneShotShelveMethodNode() throws UaException;

  /** Asynchronous form of {@link #getOneShotShelveMethodNode()}. */
  CompletableFuture<UaMethodNode> getOneShotShelveMethodNodeAsync();

  /**
   * Calls the OneShotShelve Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6">Model
   *     documentation</a>
   */
  void oneShotShelve() throws UaException;

  /**
   * Calls the OneShotShelve Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callOneShotShelve() throws UaException;

  /**
   * Calls the OneShotShelve Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callOneShotShelveWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #oneShotShelve}. */
  CompletableFuture<Void> oneShotShelveAsync();

  /** Asynchronous form of {@link #callOneShotShelve}. */
  CompletableFuture<MethodCallResult<Void>> callOneShotShelveAsync();

  /** Asynchronous form of {@link #callOneShotShelveWith}. */
  CompletableFuture<MethodCallResult<Void>> callOneShotShelveWithAsync(MethodCallOptions options);

  /**
   * Resolves the optional OneShotShelve2 Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getOneShotShelve2MethodNode() throws UaException;

  /** Asynchronous form of {@link #getOneShotShelve2MethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getOneShotShelve2MethodNodeAsync();

  /**
   * Calls the OneShotShelve2 Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7">Model
   *     documentation</a>
   */
  void oneShotShelve2(@Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the OneShotShelve2 Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callOneShotShelve2(@Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the OneShotShelve2 Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callOneShotShelve2With(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException;

  /** Asynchronous form of {@link #oneShotShelve2}. */
  CompletableFuture<Void> oneShotShelve2Async(@Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callOneShotShelve2}. */
  CompletableFuture<MethodCallResult<Void>> callOneShotShelve2Async(
      @Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callOneShotShelve2With}. */
  CompletableFuture<MethodCallResult<Void>> callOneShotShelve2WithAsync(
      MethodCallOptions options, @Nullable LocalizedText comment);

  /**
   * Resolves the mandatory TimedShelve Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4">Model
   *     documentation</a>
   */
  UaMethodNode getTimedShelveMethodNode() throws UaException;

  /** Asynchronous form of {@link #getTimedShelveMethodNode()}. */
  CompletableFuture<UaMethodNode> getTimedShelveMethodNodeAsync();

  /**
   * Calls the TimedShelve Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4">Model
   *     documentation</a>
   */
  void timedShelve(@Nullable Double shelvingTime) throws UaException;

  /**
   * Calls the TimedShelve Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callTimedShelve(@Nullable Double shelvingTime) throws UaException;

  /**
   * Calls the TimedShelve Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callTimedShelveWith(
      MethodCallOptions options, @Nullable Double shelvingTime) throws UaException;

  /** Asynchronous form of {@link #timedShelve}. */
  CompletableFuture<Void> timedShelveAsync(@Nullable Double shelvingTime);

  /** Asynchronous form of {@link #callTimedShelve}. */
  CompletableFuture<MethodCallResult<Void>> callTimedShelveAsync(@Nullable Double shelvingTime);

  /** Asynchronous form of {@link #callTimedShelveWith}. */
  CompletableFuture<MethodCallResult<Void>> callTimedShelveWithAsync(
      MethodCallOptions options, @Nullable Double shelvingTime);

  /**
   * Resolves the optional TimedShelve2 Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getTimedShelve2MethodNode() throws UaException;

  /** Asynchronous form of {@link #getTimedShelve2MethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getTimedShelve2MethodNodeAsync();

  /**
   * Calls the TimedShelve2 Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5">Model
   *     documentation</a>
   */
  void timedShelve2(@Nullable Double shelvingTime, @Nullable LocalizedText comment)
      throws UaException;

  /**
   * Calls the TimedShelve2 Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callTimedShelve2(
      @Nullable Double shelvingTime, @Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the TimedShelve2 Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callTimedShelve2With(
      MethodCallOptions options, @Nullable Double shelvingTime, @Nullable LocalizedText comment)
      throws UaException;

  /** Asynchronous form of {@link #timedShelve2}. */
  CompletableFuture<Void> timedShelve2Async(
      @Nullable Double shelvingTime, @Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callTimedShelve2}. */
  CompletableFuture<MethodCallResult<Void>> callTimedShelve2Async(
      @Nullable Double shelvingTime, @Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callTimedShelve2With}. */
  CompletableFuture<MethodCallResult<Void>> callTimedShelve2WithAsync(
      MethodCallOptions options, @Nullable Double shelvingTime, @Nullable LocalizedText comment);

  /**
   * Resolves the mandatory Unshelve Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2">Model
   *     documentation</a>
   */
  UaMethodNode getUnshelveMethodNode() throws UaException;

  /** Asynchronous form of {@link #getUnshelveMethodNode()}. */
  CompletableFuture<UaMethodNode> getUnshelveMethodNodeAsync();

  /**
   * Calls the Unshelve Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2">Model
   *     documentation</a>
   */
  void unshelve() throws UaException;

  /**
   * Calls the Unshelve Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callUnshelve() throws UaException;

  /**
   * Calls the Unshelve Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callUnshelveWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #unshelve}. */
  CompletableFuture<Void> unshelveAsync();

  /** Asynchronous form of {@link #callUnshelve}. */
  CompletableFuture<MethodCallResult<Void>> callUnshelveAsync();

  /** Asynchronous form of {@link #callUnshelveWith}. */
  CompletableFuture<MethodCallResult<Void>> callUnshelveWithAsync(MethodCallOptions options);

  /**
   * Resolves the optional Unshelve2 Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getUnshelve2MethodNode() throws UaException;

  /** Asynchronous form of {@link #getUnshelve2MethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getUnshelve2MethodNodeAsync();

  /**
   * Calls the Unshelve2 Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3">Model
   *     documentation</a>
   */
  void unshelve2(@Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the Unshelve2 Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callUnshelve2(@Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the Unshelve2 Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callUnshelve2With(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException;

  /** Asynchronous form of {@link #unshelve2}. */
  CompletableFuture<Void> unshelve2Async(@Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callUnshelve2}. */
  CompletableFuture<MethodCallResult<Void>> callUnshelve2Async(@Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callUnshelve2With}. */
  CompletableFuture<MethodCallResult<Void>> callUnshelve2WithAsync(
      MethodCallOptions options, @Nullable LocalizedText comment);
}
