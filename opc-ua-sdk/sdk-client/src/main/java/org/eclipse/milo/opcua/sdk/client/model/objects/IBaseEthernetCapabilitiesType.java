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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.4">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.4</a>
 */
public interface IBaseEthernetCapabilitiesType extends BaseInterfaceType {
  /**
   * Get the local value of the VlanTagCapable Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the VlanTagCapable Node.
   * @throws UaException if an error occurs creating or getting the VlanTagCapable Node.
   */
  Boolean getVlanTagCapable() throws UaException;

  /**
   * Set the local value of the VlanTagCapable Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the VlanTagCapable Node.
   * @throws UaException if an error occurs creating or getting the VlanTagCapable Node.
   */
  void setVlanTagCapable(Boolean value) throws UaException;

  /**
   * Read the value of the VlanTagCapable Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readVlanTagCapable() throws UaException;

  /**
   * Write a new value for the VlanTagCapable Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeVlanTagCapable(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readVlanTagCapable}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readVlanTagCapableAsync();

  /**
   * An asynchronous implementation of {@link #writeVlanTagCapable}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeVlanTagCapableAsync(Boolean value);

  /**
   * Get the VlanTagCapable {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the VlanTagCapable {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getVlanTagCapableNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getVlanTagCapableNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getVlanTagCapableNodeAsync();
}
