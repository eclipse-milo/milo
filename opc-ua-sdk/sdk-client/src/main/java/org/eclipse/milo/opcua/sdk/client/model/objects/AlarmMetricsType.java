package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.AlarmRateVariableType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AlarmMetricsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.2">Model
 *     documentation</a>
 */
public interface AlarmMetricsType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17279L);

  /**
   * Resolves the mandatory AlarmCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getAlarmCountNode() throws UaException;

  /** Asynchronous form of {@link #getAlarmCountNode()}. */
  CompletableFuture<? extends VariableNode> getAlarmCountNodeAsync();

  /**
   * Reads the Value of the AlarmCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readAlarmCount() throws UaException;

  /**
   * Writes the Value of the AlarmCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAlarmCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readAlarmCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readAlarmCountAsync();

  /** Asynchronous form of {@link #writeAlarmCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAlarmCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory MaximumUnAck child, a BaseDataVariableType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getMaximumUnAckNode() throws UaException;

  /** Asynchronous form of {@link #getMaximumUnAckNode()}. */
  CompletableFuture<? extends VariableNode> getMaximumUnAckNodeAsync();

  /**
   * Reads the Value of the MaximumUnAck child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readMaximumUnAck() throws UaException;

  /**
   * Writes the Value of the MaximumUnAck child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaximumUnAck(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readMaximumUnAck()}. */
  CompletableFuture<? extends @Nullable Double> readMaximumUnAckAsync();

  /** Asynchronous form of {@link #writeMaximumUnAck}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaximumUnAckAsync(@Nullable Double value);

  /**
   * Resolves the mandatory AverageAlarmRate child, a AlarmRateVariableType with DataType Double.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.3">AlarmRateVariableType
   *     documentation</a>
   */
  AlarmRateVariableType getAverageAlarmRateNode() throws UaException;

  /** Asynchronous form of {@link #getAverageAlarmRateNode()}. */
  CompletableFuture<? extends AlarmRateVariableType> getAverageAlarmRateNodeAsync();

  /**
   * Reads the Value of the AverageAlarmRate child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readAverageAlarmRate() throws UaException;

  /**
   * Writes the Value of the AverageAlarmRate child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAverageAlarmRate(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readAverageAlarmRate()}. */
  CompletableFuture<? extends @Nullable Double> readAverageAlarmRateAsync();

  /** Asynchronous form of {@link #writeAverageAlarmRate}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAverageAlarmRateAsync(@Nullable Double value);

  /**
   * Resolves the mandatory CurrentAlarmRate child, a AlarmRateVariableType with DataType Double.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.3">AlarmRateVariableType
   *     documentation</a>
   */
  AlarmRateVariableType getCurrentAlarmRateNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentAlarmRateNode()}. */
  CompletableFuture<? extends AlarmRateVariableType> getCurrentAlarmRateNodeAsync();

  /**
   * Reads the Value of the CurrentAlarmRate child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readCurrentAlarmRate() throws UaException;

  /**
   * Writes the Value of the CurrentAlarmRate child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCurrentAlarmRate(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readCurrentAlarmRate()}. */
  CompletableFuture<? extends @Nullable Double> readCurrentAlarmRateAsync();

  /** Asynchronous form of {@link #writeCurrentAlarmRate}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCurrentAlarmRateAsync(@Nullable Double value);

  /**
   * Resolves the mandatory MaximumAlarmRate child, a AlarmRateVariableType with DataType Double.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.3">AlarmRateVariableType
   *     documentation</a>
   */
  AlarmRateVariableType getMaximumAlarmRateNode() throws UaException;

  /** Asynchronous form of {@link #getMaximumAlarmRateNode()}. */
  CompletableFuture<? extends AlarmRateVariableType> getMaximumAlarmRateNodeAsync();

  /**
   * Reads the Value of the MaximumAlarmRate child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readMaximumAlarmRate() throws UaException;

  /**
   * Writes the Value of the MaximumAlarmRate child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaximumAlarmRate(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readMaximumAlarmRate()}. */
  CompletableFuture<? extends @Nullable Double> readMaximumAlarmRateAsync();

  /** Asynchronous form of {@link #writeMaximumAlarmRate}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaximumAlarmRateAsync(@Nullable Double value);

  /**
   * Resolves the mandatory MaximumActiveState child, a BaseDataVariableType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getMaximumActiveStateNode() throws UaException;

  /** Asynchronous form of {@link #getMaximumActiveStateNode()}. */
  CompletableFuture<? extends VariableNode> getMaximumActiveStateNodeAsync();

  /**
   * Reads the Value of the MaximumActiveState child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readMaximumActiveState() throws UaException;

  /**
   * Writes the Value of the MaximumActiveState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaximumActiveState(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readMaximumActiveState()}. */
  CompletableFuture<? extends @Nullable Double> readMaximumActiveStateAsync();

  /** Asynchronous form of {@link #writeMaximumActiveState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaximumActiveStateAsync(@Nullable Double value);

  /**
   * Resolves the mandatory MaximumReAlarmCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getMaximumReAlarmCountNode() throws UaException;

  /** Asynchronous form of {@link #getMaximumReAlarmCountNode()}. */
  CompletableFuture<? extends VariableNode> getMaximumReAlarmCountNodeAsync();

  /**
   * Reads the Value of the MaximumReAlarmCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaximumReAlarmCount() throws UaException;

  /**
   * Writes the Value of the MaximumReAlarmCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaximumReAlarmCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaximumReAlarmCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaximumReAlarmCountAsync();

  /**
   * Asynchronous form of {@link #writeMaximumReAlarmCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMaximumReAlarmCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory StartTime child, a BaseDataVariableType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getStartTimeNode() throws UaException;

  /** Asynchronous form of {@link #getStartTimeNode()}. */
  CompletableFuture<? extends VariableNode> getStartTimeNodeAsync();

  /**
   * Reads the Value of the StartTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readStartTime() throws UaException;

  /**
   * Writes the Value of the StartTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStartTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readStartTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readStartTimeAsync();

  /** Asynchronous form of {@link #writeStartTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStartTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory Reset Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4">Model
   *     documentation</a>
   */
  UaMethodNode getResetMethodNode() throws UaException;

  /** Asynchronous form of {@link #getResetMethodNode()}. */
  CompletableFuture<UaMethodNode> getResetMethodNodeAsync();

  /**
   * Calls the Reset Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4">Model
   *     documentation</a>
   */
  void reset() throws UaException;

  /**
   * Calls the Reset Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callReset() throws UaException;

  /**
   * Calls the Reset Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callResetWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #reset}. */
  CompletableFuture<Void> resetAsync();

  /** Asynchronous form of {@link #callReset}. */
  CompletableFuture<MethodCallResult<Void>> callResetAsync();

  /** Asynchronous form of {@link #callResetWith}. */
  CompletableFuture<MethodCallResult<Void>> callResetWithAsync(MethodCallOptions options);
}
