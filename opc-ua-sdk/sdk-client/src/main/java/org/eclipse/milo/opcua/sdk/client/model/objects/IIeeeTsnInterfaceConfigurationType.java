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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.10">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.10</a>
 */
public interface IIeeeTsnInterfaceConfigurationType extends BaseInterfaceType {
  /**
   * Get the local value of the MacAddress Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MacAddress Node.
   * @throws UaException if an error occurs creating or getting the MacAddress Node.
   */
  String getMacAddress() throws UaException;

  /**
   * Set the local value of the MacAddress Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MacAddress Node.
   * @throws UaException if an error occurs creating or getting the MacAddress Node.
   */
  void setMacAddress(String value) throws UaException;

  /**
   * Read the value of the MacAddress Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readMacAddress() throws UaException;

  /**
   * Write a new value for the MacAddress Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMacAddress(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMacAddress}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readMacAddressAsync();

  /**
   * An asynchronous implementation of {@link #writeMacAddress}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMacAddressAsync(String value);

  /**
   * Get the MacAddress {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MacAddress {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getMacAddressNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMacAddressNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMacAddressNodeAsync();

  /**
   * Get the local value of the InterfaceName Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the InterfaceName Node.
   * @throws UaException if an error occurs creating or getting the InterfaceName Node.
   */
  String getInterfaceName() throws UaException;

  /**
   * Set the local value of the InterfaceName Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the InterfaceName Node.
   * @throws UaException if an error occurs creating or getting the InterfaceName Node.
   */
  void setInterfaceName(String value) throws UaException;

  /**
   * Read the value of the InterfaceName Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readInterfaceName() throws UaException;

  /**
   * Write a new value for the InterfaceName Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeInterfaceName(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readInterfaceName}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readInterfaceNameAsync();

  /**
   * An asynchronous implementation of {@link #writeInterfaceName}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeInterfaceNameAsync(String value);

  /**
   * Get the InterfaceName {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the InterfaceName {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getInterfaceNameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getInterfaceNameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getInterfaceNameNodeAsync();
}
