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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NegotiationStatus;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.3</a>
 */
public interface IIeeeAutoNegotiationStatusType extends BaseInterfaceType {
  /**
   * Get the local value of the NegotiationStatus Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NegotiationStatus Node.
   * @throws UaException if an error occurs creating or getting the NegotiationStatus Node.
   */
  NegotiationStatus getNegotiationStatus() throws UaException;

  /**
   * Set the local value of the NegotiationStatus Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NegotiationStatus Node.
   * @throws UaException if an error occurs creating or getting the NegotiationStatus Node.
   */
  void setNegotiationStatus(NegotiationStatus value) throws UaException;

  /**
   * Read the value of the NegotiationStatus Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NegotiationStatus} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NegotiationStatus readNegotiationStatus() throws UaException;

  /**
   * Write a new value for the NegotiationStatus Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link NegotiationStatus} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNegotiationStatus(NegotiationStatus value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNegotiationStatus}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NegotiationStatus> readNegotiationStatusAsync();

  /**
   * An asynchronous implementation of {@link #writeNegotiationStatus}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNegotiationStatusAsync(NegotiationStatus value);

  /**
   * Get the NegotiationStatus {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NegotiationStatus {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getNegotiationStatusNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNegotiationStatusNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getNegotiationStatusNodeAsync();
}
