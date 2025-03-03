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
import org.eclipse.milo.opcua.stack.core.types.structured.Range;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.3</a>
 */
public interface AnalogItemType extends BaseAnalogType {
  QualifiedProperty<Range> EU_RANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EURange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=884"),
          -1,
          Range.class);

  /**
   * Get the local value of the EURange Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EURange Node.
   * @throws UaException if an error occurs creating or getting the EURange Node.
   */
  Range getEuRange() throws UaException;

  /**
   * Set the local value of the EURange Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EURange Node.
   * @throws UaException if an error occurs creating or getting the EURange Node.
   */
  void setEuRange(Range value) throws UaException;

  /**
   * Read the value of the EURange Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Range} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Range readEuRange() throws UaException;

  /**
   * Write a new value for the EURange Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Range} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEuRange(Range value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEuRange}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Range> readEuRangeAsync();

  /**
   * An asynchronous implementation of {@link #writeEuRange}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEuRangeAsync(Range value);

  /**
   * Get the EURange {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EURange {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEuRangeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEuRangeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEuRangeNodeAsync();
}
