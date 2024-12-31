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
import org.eclipse.milo.opcua.sdk.client.model.variables.SelectionListType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.6">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.6</a>
 */
public interface NetworkAddressType extends BaseObjectType {
  /**
   * Get the local value of the NetworkInterface Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NetworkInterface Node.
   * @throws UaException if an error occurs creating or getting the NetworkInterface Node.
   */
  String getNetworkInterface() throws UaException;

  /**
   * Set the local value of the NetworkInterface Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NetworkInterface Node.
   * @throws UaException if an error occurs creating or getting the NetworkInterface Node.
   */
  void setNetworkInterface(String value) throws UaException;

  /**
   * Read the value of the NetworkInterface Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readNetworkInterface() throws UaException;

  /**
   * Write a new value for the NetworkInterface Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNetworkInterface(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNetworkInterface}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readNetworkInterfaceAsync();

  /**
   * An asynchronous implementation of {@link #writeNetworkInterface}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNetworkInterfaceAsync(String value);

  /**
   * Get the NetworkInterface {@link SelectionListType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NetworkInterface {@link SelectionListType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SelectionListType getNetworkInterfaceNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNetworkInterfaceNode()}.
   *
   * @return a CompletableFuture that completes successfully with the SelectionListType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends SelectionListType> getNetworkInterfaceNodeAsync();
}
