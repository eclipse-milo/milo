/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.3">https://reference.opcfoundation.org/v105/Core/docs/Part9/9.3</a>
 */
public interface AlarmRateVariableType extends BaseDataVariableType {
  QualifiedProperty<UShort> RATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Rate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  /**
   * Get the local value of the Rate Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Rate Node.
   * @throws UaException if an error occurs creating or getting the Rate Node.
   */
  UShort getRate() throws UaException;

  /**
   * Set the local value of the Rate Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Rate Node.
   * @throws UaException if an error occurs creating or getting the Rate Node.
   */
  void setRate(UShort value) throws UaException;

  /**
   * Read the value of the Rate Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readRate() throws UaException;

  /**
   * Write a new value for the Rate Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRate(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRate}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readRateAsync();

  /**
   * An asynchronous implementation of {@link #writeRate}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRateAsync(UShort value);

  /**
   * Get the Rate {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Rate {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getRateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getRateNodeAsync();
}
