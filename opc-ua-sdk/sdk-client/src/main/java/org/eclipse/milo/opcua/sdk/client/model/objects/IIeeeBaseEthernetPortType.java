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
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogUnitType;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.Duplex;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.2</a>
 */
public interface IIeeeBaseEthernetPortType extends BaseInterfaceType {
  /**
   * Get the local value of the Speed Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Speed Node.
   * @throws UaException if an error occurs creating or getting the Speed Node.
   */
  ULong getSpeed() throws UaException;

  /**
   * Set the local value of the Speed Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Speed Node.
   * @throws UaException if an error occurs creating or getting the Speed Node.
   */
  void setSpeed(ULong value) throws UaException;

  /**
   * Read the value of the Speed Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link ULong} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ULong readSpeed() throws UaException;

  /**
   * Write a new value for the Speed Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link ULong} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSpeed(ULong value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSpeed}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ULong> readSpeedAsync();

  /**
   * An asynchronous implementation of {@link #writeSpeed}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSpeedAsync(ULong value);

  /**
   * Get the Speed {@link AnalogUnitType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Speed {@link AnalogUnitType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  AnalogUnitType getSpeedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSpeedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the AnalogUnitType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends AnalogUnitType> getSpeedNodeAsync();

  /**
   * Get the local value of the Duplex Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Duplex Node.
   * @throws UaException if an error occurs creating or getting the Duplex Node.
   */
  Duplex getDuplex() throws UaException;

  /**
   * Set the local value of the Duplex Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Duplex Node.
   * @throws UaException if an error occurs creating or getting the Duplex Node.
   */
  void setDuplex(Duplex value) throws UaException;

  /**
   * Read the value of the Duplex Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Duplex} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Duplex readDuplex() throws UaException;

  /**
   * Write a new value for the Duplex Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link Duplex} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDuplex(Duplex value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDuplex}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Duplex> readDuplexAsync();

  /**
   * An asynchronous implementation of {@link #writeDuplex}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDuplexAsync(Duplex value);

  /**
   * Get the Duplex {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Duplex {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getDuplexNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDuplexNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDuplexNodeAsync();

  /**
   * Get the local value of the MaxFrameLength Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxFrameLength Node.
   * @throws UaException if an error occurs creating or getting the MaxFrameLength Node.
   */
  UShort getMaxFrameLength() throws UaException;

  /**
   * Set the local value of the MaxFrameLength Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxFrameLength Node.
   * @throws UaException if an error occurs creating or getting the MaxFrameLength Node.
   */
  void setMaxFrameLength(UShort value) throws UaException;

  /**
   * Read the value of the MaxFrameLength Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readMaxFrameLength() throws UaException;

  /**
   * Write a new value for the MaxFrameLength Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxFrameLength(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxFrameLength}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readMaxFrameLengthAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxFrameLength}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxFrameLengthAsync(UShort value);

  /**
   * Get the MaxFrameLength {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxFrameLength {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getMaxFrameLengthNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxFrameLengthNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaxFrameLengthNodeAsync();
}
