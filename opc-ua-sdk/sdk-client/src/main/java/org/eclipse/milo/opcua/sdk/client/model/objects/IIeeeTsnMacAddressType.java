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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.13">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.13</a>
 */
public interface IIeeeTsnMacAddressType extends BaseInterfaceType {
  /**
   * Get the local value of the DestinationAddress Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DestinationAddress Node.
   * @throws UaException if an error occurs creating or getting the DestinationAddress Node.
   */
  UByte[] getDestinationAddress() throws UaException;

  /**
   * Set the local value of the DestinationAddress Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DestinationAddress Node.
   * @throws UaException if an error occurs creating or getting the DestinationAddress Node.
   */
  void setDestinationAddress(UByte[] value) throws UaException;

  /**
   * Read the value of the DestinationAddress Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UByte[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UByte[] readDestinationAddress() throws UaException;

  /**
   * Write a new value for the DestinationAddress Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UByte[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDestinationAddress(UByte[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDestinationAddress}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UByte[]> readDestinationAddressAsync();

  /**
   * An asynchronous implementation of {@link #writeDestinationAddress}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDestinationAddressAsync(UByte[] value);

  /**
   * Get the DestinationAddress {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DestinationAddress {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getDestinationAddressNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDestinationAddressNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDestinationAddressNodeAsync();

  /**
   * Get the local value of the SourceAddress Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SourceAddress Node.
   * @throws UaException if an error occurs creating or getting the SourceAddress Node.
   */
  UByte[] getSourceAddress() throws UaException;

  /**
   * Set the local value of the SourceAddress Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SourceAddress Node.
   * @throws UaException if an error occurs creating or getting the SourceAddress Node.
   */
  void setSourceAddress(UByte[] value) throws UaException;

  /**
   * Read the value of the SourceAddress Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UByte[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UByte[] readSourceAddress() throws UaException;

  /**
   * Write a new value for the SourceAddress Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UByte[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSourceAddress(UByte[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSourceAddress}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UByte[]> readSourceAddressAsync();

  /**
   * An asynchronous implementation of {@link #writeSourceAddress}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSourceAddressAsync(UByte[] value);

  /**
   * Get the SourceAddress {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SourceAddress {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSourceAddressNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSourceAddressNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSourceAddressNodeAsync();
}
