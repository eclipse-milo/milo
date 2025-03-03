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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.6">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.6</a>
 */
public interface ISrClassType extends BaseInterfaceType {
  /**
   * Get the local value of the Id Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Id Node.
   * @throws UaException if an error occurs creating or getting the Id Node.
   */
  UByte getId() throws UaException;

  /**
   * Set the local value of the Id Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Id Node.
   * @throws UaException if an error occurs creating or getting the Id Node.
   */
  void setId(UByte value) throws UaException;

  /**
   * Read the value of the Id Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link UByte} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UByte readId() throws UaException;

  /**
   * Write a new value for the Id Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link UByte} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeId(UByte value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UByte> readIdAsync();

  /**
   * An asynchronous implementation of {@link #writeId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeIdAsync(UByte value);

  /**
   * Get the Id {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Id {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getIdNodeAsync();

  /**
   * Get the local value of the Priority Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Priority Node.
   * @throws UaException if an error occurs creating or getting the Priority Node.
   */
  UByte getPriority() throws UaException;

  /**
   * Set the local value of the Priority Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Priority Node.
   * @throws UaException if an error occurs creating or getting the Priority Node.
   */
  void setPriority(UByte value) throws UaException;

  /**
   * Read the value of the Priority Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link UByte} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UByte readPriority() throws UaException;

  /**
   * Write a new value for the Priority Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UByte} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePriority(UByte value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPriority}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UByte> readPriorityAsync();

  /**
   * An asynchronous implementation of {@link #writePriority}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePriorityAsync(UByte value);

  /**
   * Get the Priority {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Priority {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getPriorityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPriorityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPriorityNodeAsync();

  /**
   * Get the local value of the Vid Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Vid Node.
   * @throws UaException if an error occurs creating or getting the Vid Node.
   */
  UShort getVid() throws UaException;

  /**
   * Set the local value of the Vid Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Vid Node.
   * @throws UaException if an error occurs creating or getting the Vid Node.
   */
  void setVid(UShort value) throws UaException;

  /**
   * Read the value of the Vid Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readVid() throws UaException;

  /**
   * Write a new value for the Vid Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeVid(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readVid}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readVidAsync();

  /**
   * An asynchronous implementation of {@link #writeVid}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeVidAsync(UShort value);

  /**
   * Get the Vid {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Vid {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getVidNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getVidNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getVidNodeAsync();
}
