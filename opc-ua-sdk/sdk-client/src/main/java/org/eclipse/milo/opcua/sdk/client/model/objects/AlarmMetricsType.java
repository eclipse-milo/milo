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
import org.eclipse.milo.opcua.sdk.client.model.variables.AlarmRateVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/9.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AlarmMetricsType extends BaseObjectType {
  /** Gets the existing node's local value. */
  @Nullable UInteger getAlarmCount() throws UaException;

  /** Sets the existing node's local value. */
  void setAlarmCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readAlarmCount() throws UaException;

  /** Writes the value remotely. */
  void writeAlarmCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readAlarmCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAlarmCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getAlarmCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getAlarmCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getStartTime() throws UaException;

  /** Sets the existing node's local value. */
  void setStartTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readStartTime() throws UaException;

  /** Writes the value remotely. */
  void writeStartTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readStartTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStartTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getStartTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getStartTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getMaximumActiveState() throws UaException;

  /** Sets the existing node's local value. */
  void setMaximumActiveState(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readMaximumActiveState() throws UaException;

  /** Writes the value remotely. */
  void writeMaximumActiveState(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readMaximumActiveStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaximumActiveStateAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaximumActiveStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaximumActiveStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getMaximumUnAck() throws UaException;

  /** Sets the existing node's local value. */
  void setMaximumUnAck(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readMaximumUnAck() throws UaException;

  /** Writes the value remotely. */
  void writeMaximumUnAck(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readMaximumUnAckAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaximumUnAckAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaximumUnAckNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaximumUnAckNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getCurrentAlarmRate() throws UaException;

  /** Sets the existing node's local value. */
  void setCurrentAlarmRate(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readCurrentAlarmRate() throws UaException;

  /** Writes the value remotely. */
  void writeCurrentAlarmRate(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readCurrentAlarmRateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCurrentAlarmRateAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  AlarmRateVariableType getCurrentAlarmRateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends AlarmRateVariableType> getCurrentAlarmRateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getMaximumAlarmRate() throws UaException;

  /** Sets the existing node's local value. */
  void setMaximumAlarmRate(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readMaximumAlarmRate() throws UaException;

  /** Writes the value remotely. */
  void writeMaximumAlarmRate(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readMaximumAlarmRateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaximumAlarmRateAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  AlarmRateVariableType getMaximumAlarmRateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends AlarmRateVariableType> getMaximumAlarmRateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaximumReAlarmCount() throws UaException;

  /** Sets the existing node's local value. */
  void setMaximumReAlarmCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaximumReAlarmCount() throws UaException;

  /** Writes the value remotely. */
  void writeMaximumReAlarmCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaximumReAlarmCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaximumReAlarmCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaximumReAlarmCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaximumReAlarmCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getAverageAlarmRate() throws UaException;

  /** Sets the existing node's local value. */
  void setAverageAlarmRate(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readAverageAlarmRate() throws UaException;

  /** Writes the value remotely. */
  void writeAverageAlarmRate(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readAverageAlarmRateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAverageAlarmRateAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  AlarmRateVariableType getAverageAlarmRateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends AlarmRateVariableType> getAverageAlarmRateNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getResetMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getResetMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callReset() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callResetAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callResetDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callResetDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callResetDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callResetDetailedAsync(
      MethodCallOptions options);
}
