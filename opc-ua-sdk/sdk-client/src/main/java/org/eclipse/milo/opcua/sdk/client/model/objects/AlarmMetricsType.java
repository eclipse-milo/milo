/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.AlarmRateVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/9.2</a>
 */
public interface AlarmMetricsType extends BaseObjectType {
  /**
   * Get the local value of the AlarmCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AlarmCount Node.
   * @throws UaException if an error occurs creating or getting the AlarmCount Node.
   */
  UInteger getAlarmCount() throws UaException;

  /**
   * Set the local value of the AlarmCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AlarmCount Node.
   * @throws UaException if an error occurs creating or getting the AlarmCount Node.
   */
  void setAlarmCount(UInteger value) throws UaException;

  /**
   * Read the value of the AlarmCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readAlarmCount() throws UaException;

  /**
   * Write a new value for the AlarmCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAlarmCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAlarmCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readAlarmCountAsync();

  /**
   * An asynchronous implementation of {@link #writeAlarmCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAlarmCountAsync(UInteger value);

  /**
   * Get the AlarmCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AlarmCount {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getAlarmCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAlarmCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getAlarmCountNodeAsync();

  /**
   * Get the local value of the StartTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the StartTime Node.
   * @throws UaException if an error occurs creating or getting the StartTime Node.
   */
  DateTime getStartTime() throws UaException;

  /**
   * Set the local value of the StartTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the StartTime Node.
   * @throws UaException if an error occurs creating or getting the StartTime Node.
   */
  void setStartTime(DateTime value) throws UaException;

  /**
   * Read the value of the StartTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readStartTime() throws UaException;

  /**
   * Write a new value for the StartTime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeStartTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readStartTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readStartTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeStartTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStartTimeAsync(DateTime value);

  /**
   * Get the StartTime {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the StartTime {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getStartTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStartTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getStartTimeNodeAsync();

  /**
   * Get the local value of the MaximumActiveState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaximumActiveState Node.
   * @throws UaException if an error occurs creating or getting the MaximumActiveState Node.
   */
  Double getMaximumActiveState() throws UaException;

  /**
   * Set the local value of the MaximumActiveState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaximumActiveState Node.
   * @throws UaException if an error occurs creating or getting the MaximumActiveState Node.
   */
  void setMaximumActiveState(Double value) throws UaException;

  /**
   * Read the value of the MaximumActiveState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readMaximumActiveState() throws UaException;

  /**
   * Write a new value for the MaximumActiveState Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaximumActiveState(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaximumActiveState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readMaximumActiveStateAsync();

  /**
   * An asynchronous implementation of {@link #writeMaximumActiveState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaximumActiveStateAsync(Double value);

  /**
   * Get the MaximumActiveState {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaximumActiveState {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getMaximumActiveStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaximumActiveStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaximumActiveStateNodeAsync();

  /**
   * Get the local value of the MaximumUnAck Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaximumUnAck Node.
   * @throws UaException if an error occurs creating or getting the MaximumUnAck Node.
   */
  Double getMaximumUnAck() throws UaException;

  /**
   * Set the local value of the MaximumUnAck Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaximumUnAck Node.
   * @throws UaException if an error occurs creating or getting the MaximumUnAck Node.
   */
  void setMaximumUnAck(Double value) throws UaException;

  /**
   * Read the value of the MaximumUnAck Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readMaximumUnAck() throws UaException;

  /**
   * Write a new value for the MaximumUnAck Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaximumUnAck(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaximumUnAck}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readMaximumUnAckAsync();

  /**
   * An asynchronous implementation of {@link #writeMaximumUnAck}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaximumUnAckAsync(Double value);

  /**
   * Get the MaximumUnAck {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaximumUnAck {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getMaximumUnAckNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaximumUnAckNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaximumUnAckNodeAsync();

  /**
   * Get the local value of the CurrentAlarmRate Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CurrentAlarmRate Node.
   * @throws UaException if an error occurs creating or getting the CurrentAlarmRate Node.
   */
  Double getCurrentAlarmRate() throws UaException;

  /**
   * Set the local value of the CurrentAlarmRate Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CurrentAlarmRate Node.
   * @throws UaException if an error occurs creating or getting the CurrentAlarmRate Node.
   */
  void setCurrentAlarmRate(Double value) throws UaException;

  /**
   * Read the value of the CurrentAlarmRate Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readCurrentAlarmRate() throws UaException;

  /**
   * Write a new value for the CurrentAlarmRate Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCurrentAlarmRate(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCurrentAlarmRate}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readCurrentAlarmRateAsync();

  /**
   * An asynchronous implementation of {@link #writeCurrentAlarmRate}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCurrentAlarmRateAsync(Double value);

  /**
   * Get the CurrentAlarmRate {@link AlarmRateVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CurrentAlarmRate {@link AlarmRateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  AlarmRateVariableType getCurrentAlarmRateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCurrentAlarmRateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the AlarmRateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends AlarmRateVariableType> getCurrentAlarmRateNodeAsync();

  /**
   * Get the local value of the MaximumAlarmRate Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaximumAlarmRate Node.
   * @throws UaException if an error occurs creating or getting the MaximumAlarmRate Node.
   */
  Double getMaximumAlarmRate() throws UaException;

  /**
   * Set the local value of the MaximumAlarmRate Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaximumAlarmRate Node.
   * @throws UaException if an error occurs creating or getting the MaximumAlarmRate Node.
   */
  void setMaximumAlarmRate(Double value) throws UaException;

  /**
   * Read the value of the MaximumAlarmRate Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readMaximumAlarmRate() throws UaException;

  /**
   * Write a new value for the MaximumAlarmRate Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaximumAlarmRate(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaximumAlarmRate}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readMaximumAlarmRateAsync();

  /**
   * An asynchronous implementation of {@link #writeMaximumAlarmRate}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaximumAlarmRateAsync(Double value);

  /**
   * Get the MaximumAlarmRate {@link AlarmRateVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaximumAlarmRate {@link AlarmRateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  AlarmRateVariableType getMaximumAlarmRateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaximumAlarmRateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the AlarmRateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends AlarmRateVariableType> getMaximumAlarmRateNodeAsync();

  /**
   * Get the local value of the MaximumReAlarmCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaximumReAlarmCount Node.
   * @throws UaException if an error occurs creating or getting the MaximumReAlarmCount Node.
   */
  UInteger getMaximumReAlarmCount() throws UaException;

  /**
   * Set the local value of the MaximumReAlarmCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaximumReAlarmCount Node.
   * @throws UaException if an error occurs creating or getting the MaximumReAlarmCount Node.
   */
  void setMaximumReAlarmCount(UInteger value) throws UaException;

  /**
   * Read the value of the MaximumReAlarmCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaximumReAlarmCount() throws UaException;

  /**
   * Write a new value for the MaximumReAlarmCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaximumReAlarmCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaximumReAlarmCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaximumReAlarmCountAsync();

  /**
   * An asynchronous implementation of {@link #writeMaximumReAlarmCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaximumReAlarmCountAsync(UInteger value);

  /**
   * Get the MaximumReAlarmCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaximumReAlarmCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getMaximumReAlarmCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaximumReAlarmCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaximumReAlarmCountNodeAsync();

  /**
   * Get the local value of the AverageAlarmRate Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AverageAlarmRate Node.
   * @throws UaException if an error occurs creating or getting the AverageAlarmRate Node.
   */
  Double getAverageAlarmRate() throws UaException;

  /**
   * Set the local value of the AverageAlarmRate Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AverageAlarmRate Node.
   * @throws UaException if an error occurs creating or getting the AverageAlarmRate Node.
   */
  void setAverageAlarmRate(Double value) throws UaException;

  /**
   * Read the value of the AverageAlarmRate Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readAverageAlarmRate() throws UaException;

  /**
   * Write a new value for the AverageAlarmRate Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAverageAlarmRate(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAverageAlarmRate}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readAverageAlarmRateAsync();

  /**
   * An asynchronous implementation of {@link #writeAverageAlarmRate}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAverageAlarmRateAsync(Double value);

  /**
   * Get the AverageAlarmRate {@link AlarmRateVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AverageAlarmRate {@link AlarmRateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  AlarmRateVariableType getAverageAlarmRateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAverageAlarmRateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the AlarmRateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends AlarmRateVariableType> getAverageAlarmRateNodeAsync();
}
