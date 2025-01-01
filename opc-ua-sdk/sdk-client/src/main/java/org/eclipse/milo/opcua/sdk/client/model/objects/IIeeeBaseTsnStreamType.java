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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnStreamState;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.7">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.7</a>
 */
public interface IIeeeBaseTsnStreamType extends BaseInterfaceType {
  /**
   * Get the local value of the StreamId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the StreamId Node.
   * @throws UaException if an error occurs creating or getting the StreamId Node.
   */
  UByte[] getStreamId() throws UaException;

  /**
   * Set the local value of the StreamId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the StreamId Node.
   * @throws UaException if an error occurs creating or getting the StreamId Node.
   */
  void setStreamId(UByte[] value) throws UaException;

  /**
   * Read the value of the StreamId Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link UByte[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UByte[] readStreamId() throws UaException;

  /**
   * Write a new value for the StreamId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UByte[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeStreamId(UByte[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readStreamId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UByte[]> readStreamIdAsync();

  /**
   * An asynchronous implementation of {@link #writeStreamId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStreamIdAsync(UByte[] value);

  /**
   * Get the StreamId {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the StreamId {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getStreamIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStreamIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getStreamIdNodeAsync();

  /**
   * Get the local value of the StreamName Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the StreamName Node.
   * @throws UaException if an error occurs creating or getting the StreamName Node.
   */
  String getStreamName() throws UaException;

  /**
   * Set the local value of the StreamName Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the StreamName Node.
   * @throws UaException if an error occurs creating or getting the StreamName Node.
   */
  void setStreamName(String value) throws UaException;

  /**
   * Read the value of the StreamName Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readStreamName() throws UaException;

  /**
   * Write a new value for the StreamName Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeStreamName(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readStreamName}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readStreamNameAsync();

  /**
   * An asynchronous implementation of {@link #writeStreamName}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStreamNameAsync(String value);

  /**
   * Get the StreamName {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the StreamName {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getStreamNameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStreamNameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getStreamNameNodeAsync();

  /**
   * Get the local value of the State Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the State Node.
   * @throws UaException if an error occurs creating or getting the State Node.
   */
  TsnStreamState getState() throws UaException;

  /**
   * Set the local value of the State Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the State Node.
   * @throws UaException if an error occurs creating or getting the State Node.
   */
  void setState(TsnStreamState value) throws UaException;

  /**
   * Read the value of the State Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link TsnStreamState} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  TsnStreamState readState() throws UaException;

  /**
   * Write a new value for the State Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link TsnStreamState} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeState(TsnStreamState value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends TsnStreamState> readStateAsync();

  /**
   * An asynchronous implementation of {@link #writeState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStateAsync(TsnStreamState value);

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
   * Get the local value of the AccumulatedLatency Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AccumulatedLatency Node.
   * @throws UaException if an error occurs creating or getting the AccumulatedLatency Node.
   */
  UInteger getAccumulatedLatency() throws UaException;

  /**
   * Set the local value of the AccumulatedLatency Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AccumulatedLatency Node.
   * @throws UaException if an error occurs creating or getting the AccumulatedLatency Node.
   */
  void setAccumulatedLatency(UInteger value) throws UaException;

  /**
   * Read the value of the AccumulatedLatency Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readAccumulatedLatency() throws UaException;

  /**
   * Write a new value for the AccumulatedLatency Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAccumulatedLatency(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAccumulatedLatency}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readAccumulatedLatencyAsync();

  /**
   * An asynchronous implementation of {@link #writeAccumulatedLatency}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAccumulatedLatencyAsync(UInteger value);

  /**
   * Get the AccumulatedLatency {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AccumulatedLatency {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getAccumulatedLatencyNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAccumulatedLatencyNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getAccumulatedLatencyNodeAsync();

  /**
   * Get the local value of the SrClassId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SrClassId Node.
   * @throws UaException if an error occurs creating or getting the SrClassId Node.
   */
  UByte getSrClassId() throws UaException;

  /**
   * Set the local value of the SrClassId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SrClassId Node.
   * @throws UaException if an error occurs creating or getting the SrClassId Node.
   */
  void setSrClassId(UByte value) throws UaException;

  /**
   * Read the value of the SrClassId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UByte} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UByte readSrClassId() throws UaException;

  /**
   * Write a new value for the SrClassId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UByte} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSrClassId(UByte value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSrClassId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UByte> readSrClassIdAsync();

  /**
   * An asynchronous implementation of {@link #writeSrClassId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSrClassIdAsync(UByte value);

  /**
   * Get the SrClassId {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SrClassId {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSrClassIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSrClassIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSrClassIdNodeAsync();
}
