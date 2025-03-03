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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.14">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.14</a>
 */
public interface IIeeeTsnVlanTagType extends BaseInterfaceType {
  /**
   * Get the local value of the VlanId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the VlanId Node.
   * @throws UaException if an error occurs creating or getting the VlanId Node.
   */
  UShort getVlanId() throws UaException;

  /**
   * Set the local value of the VlanId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the VlanId Node.
   * @throws UaException if an error occurs creating or getting the VlanId Node.
   */
  void setVlanId(UShort value) throws UaException;

  /**
   * Read the value of the VlanId Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readVlanId() throws UaException;

  /**
   * Write a new value for the VlanId Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeVlanId(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readVlanId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readVlanIdAsync();

  /**
   * An asynchronous implementation of {@link #writeVlanId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeVlanIdAsync(UShort value);

  /**
   * Get the VlanId {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the VlanId {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getVlanIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getVlanIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getVlanIdNodeAsync();

  /**
   * Get the local value of the PriorityCodePoint Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PriorityCodePoint Node.
   * @throws UaException if an error occurs creating or getting the PriorityCodePoint Node.
   */
  UByte getPriorityCodePoint() throws UaException;

  /**
   * Set the local value of the PriorityCodePoint Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PriorityCodePoint Node.
   * @throws UaException if an error occurs creating or getting the PriorityCodePoint Node.
   */
  void setPriorityCodePoint(UByte value) throws UaException;

  /**
   * Read the value of the PriorityCodePoint Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UByte} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UByte readPriorityCodePoint() throws UaException;

  /**
   * Write a new value for the PriorityCodePoint Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UByte} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePriorityCodePoint(UByte value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPriorityCodePoint}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UByte> readPriorityCodePointAsync();

  /**
   * An asynchronous implementation of {@link #writePriorityCodePoint}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePriorityCodePointAsync(UByte value);

  /**
   * Get the PriorityCodePoint {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PriorityCodePoint {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getPriorityCodePointNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPriorityCodePointNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPriorityCodePointNodeAsync();
}
