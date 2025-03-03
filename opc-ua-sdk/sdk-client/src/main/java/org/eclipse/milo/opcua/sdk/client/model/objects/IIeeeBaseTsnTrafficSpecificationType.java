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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UnsignedRationalNumber;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.8">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.8</a>
 */
public interface IIeeeBaseTsnTrafficSpecificationType extends BaseInterfaceType {
  /**
   * Get the local value of the MaxIntervalFrames Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxIntervalFrames Node.
   * @throws UaException if an error occurs creating or getting the MaxIntervalFrames Node.
   */
  UShort getMaxIntervalFrames() throws UaException;

  /**
   * Set the local value of the MaxIntervalFrames Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxIntervalFrames Node.
   * @throws UaException if an error occurs creating or getting the MaxIntervalFrames Node.
   */
  void setMaxIntervalFrames(UShort value) throws UaException;

  /**
   * Read the value of the MaxIntervalFrames Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readMaxIntervalFrames() throws UaException;

  /**
   * Write a new value for the MaxIntervalFrames Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxIntervalFrames(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxIntervalFrames}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readMaxIntervalFramesAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxIntervalFrames}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxIntervalFramesAsync(UShort value);

  /**
   * Get the MaxIntervalFrames {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxIntervalFrames {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getMaxIntervalFramesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxIntervalFramesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaxIntervalFramesNodeAsync();

  /**
   * Get the local value of the MaxFrameSize Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxFrameSize Node.
   * @throws UaException if an error occurs creating or getting the MaxFrameSize Node.
   */
  UInteger getMaxFrameSize() throws UaException;

  /**
   * Set the local value of the MaxFrameSize Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxFrameSize Node.
   * @throws UaException if an error occurs creating or getting the MaxFrameSize Node.
   */
  void setMaxFrameSize(UInteger value) throws UaException;

  /**
   * Read the value of the MaxFrameSize Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxFrameSize() throws UaException;

  /**
   * Write a new value for the MaxFrameSize Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxFrameSize(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxFrameSize}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxFrameSizeAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxFrameSize}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxFrameSizeAsync(UInteger value);

  /**
   * Get the MaxFrameSize {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxFrameSize {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getMaxFrameSizeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxFrameSizeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaxFrameSizeNodeAsync();

  /**
   * Get the local value of the Interval Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Interval Node.
   * @throws UaException if an error occurs creating or getting the Interval Node.
   */
  UnsignedRationalNumber getInterval() throws UaException;

  /**
   * Set the local value of the Interval Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Interval Node.
   * @throws UaException if an error occurs creating or getting the Interval Node.
   */
  void setInterval(UnsignedRationalNumber value) throws UaException;

  /**
   * Read the value of the Interval Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link UnsignedRationalNumber} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UnsignedRationalNumber readInterval() throws UaException;

  /**
   * Write a new value for the Interval Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UnsignedRationalNumber} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeInterval(UnsignedRationalNumber value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readInterval}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UnsignedRationalNumber> readIntervalAsync();

  /**
   * An asynchronous implementation of {@link #writeInterval}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeIntervalAsync(UnsignedRationalNumber value);

  /**
   * Get the Interval {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Interval {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getIntervalNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getIntervalNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getIntervalNodeAsync();
}
