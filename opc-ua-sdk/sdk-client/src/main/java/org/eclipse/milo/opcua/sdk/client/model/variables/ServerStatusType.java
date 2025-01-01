/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.6">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.6</a>
 */
public interface ServerStatusType extends BaseDataVariableType {
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
   * Get the local value of the CurrentTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CurrentTime Node.
   * @throws UaException if an error occurs creating or getting the CurrentTime Node.
   */
  DateTime getCurrentTime() throws UaException;

  /**
   * Set the local value of the CurrentTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CurrentTime Node.
   * @throws UaException if an error occurs creating or getting the CurrentTime Node.
   */
  void setCurrentTime(DateTime value) throws UaException;

  /**
   * Read the value of the CurrentTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readCurrentTime() throws UaException;

  /**
   * Write a new value for the CurrentTime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCurrentTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCurrentTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readCurrentTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeCurrentTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCurrentTimeAsync(DateTime value);

  /**
   * Get the CurrentTime {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CurrentTime {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getCurrentTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCurrentTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getCurrentTimeNodeAsync();

  /**
   * Get the local value of the State Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the State Node.
   * @throws UaException if an error occurs creating or getting the State Node.
   */
  ServerState getState() throws UaException;

  /**
   * Set the local value of the State Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the State Node.
   * @throws UaException if an error occurs creating or getting the State Node.
   */
  void setState(ServerState value) throws UaException;

  /**
   * Read the value of the State Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link ServerState} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServerState readState() throws UaException;

  /**
   * Write a new value for the State Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link ServerState} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeState(ServerState value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServerState> readStateAsync();

  /**
   * An asynchronous implementation of {@link #writeState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStateAsync(ServerState value);

  /**
   * Get the State {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the State {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getStateNodeAsync();

  /**
   * Get the local value of the BuildInfo Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the BuildInfo Node.
   * @throws UaException if an error occurs creating or getting the BuildInfo Node.
   */
  BuildInfo getBuildInfo() throws UaException;

  /**
   * Set the local value of the BuildInfo Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the BuildInfo Node.
   * @throws UaException if an error occurs creating or getting the BuildInfo Node.
   */
  void setBuildInfo(BuildInfo value) throws UaException;

  /**
   * Read the value of the BuildInfo Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link BuildInfo} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  BuildInfo readBuildInfo() throws UaException;

  /**
   * Write a new value for the BuildInfo Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link BuildInfo} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeBuildInfo(BuildInfo value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readBuildInfo}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends BuildInfo> readBuildInfoAsync();

  /**
   * An asynchronous implementation of {@link #writeBuildInfo}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeBuildInfoAsync(BuildInfo value);

  /**
   * Get the BuildInfo {@link BuildInfoType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the BuildInfo {@link BuildInfoType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BuildInfoType getBuildInfoNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getBuildInfoNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BuildInfoType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BuildInfoType> getBuildInfoNodeAsync();

  /**
   * Get the local value of the SecondsTillShutdown Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SecondsTillShutdown Node.
   * @throws UaException if an error occurs creating or getting the SecondsTillShutdown Node.
   */
  UInteger getSecondsTillShutdown() throws UaException;

  /**
   * Set the local value of the SecondsTillShutdown Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SecondsTillShutdown Node.
   * @throws UaException if an error occurs creating or getting the SecondsTillShutdown Node.
   */
  void setSecondsTillShutdown(UInteger value) throws UaException;

  /**
   * Read the value of the SecondsTillShutdown Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readSecondsTillShutdown() throws UaException;

  /**
   * Write a new value for the SecondsTillShutdown Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSecondsTillShutdown(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSecondsTillShutdown}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readSecondsTillShutdownAsync();

  /**
   * An asynchronous implementation of {@link #writeSecondsTillShutdown}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSecondsTillShutdownAsync(UInteger value);

  /**
   * Get the SecondsTillShutdown {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecondsTillShutdown {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSecondsTillShutdownNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecondsTillShutdownNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSecondsTillShutdownNodeAsync();

  /**
   * Get the local value of the ShutdownReason Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ShutdownReason Node.
   * @throws UaException if an error occurs creating or getting the ShutdownReason Node.
   */
  LocalizedText getShutdownReason() throws UaException;

  /**
   * Set the local value of the ShutdownReason Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ShutdownReason Node.
   * @throws UaException if an error occurs creating or getting the ShutdownReason Node.
   */
  void setShutdownReason(LocalizedText value) throws UaException;

  /**
   * Read the value of the ShutdownReason Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readShutdownReason() throws UaException;

  /**
   * Write a new value for the ShutdownReason Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeShutdownReason(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readShutdownReason}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readShutdownReasonAsync();

  /**
   * An asynchronous implementation of {@link #writeShutdownReason}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeShutdownReasonAsync(LocalizedText value);

  /**
   * Get the ShutdownReason {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ShutdownReason {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getShutdownReasonNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getShutdownReasonNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getShutdownReasonNodeAsync();
}
