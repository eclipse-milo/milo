/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.structured.NumberRange;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.6">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.6</a>
 */
public interface AnalogNumberItemType extends AnalogItemType {
  QualifiedProperty<NumberRange> EU_NUMBER_RANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EUNumberRange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23903"),
          -1,
          NumberRange.class);

  /**
   * Get the local value of the EUNumberRange Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EUNumberRange Node.
   * @throws UaException if an error occurs creating or getting the EUNumberRange Node.
   */
  NumberRange getEuNumberRange() throws UaException;

  /**
   * Set the local value of the EUNumberRange Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EUNumberRange Node.
   * @throws UaException if an error occurs creating or getting the EUNumberRange Node.
   */
  void setEuNumberRange(NumberRange value) throws UaException;

  /**
   * Read the value of the EUNumberRange Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NumberRange} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NumberRange readEuNumberRange() throws UaException;

  /**
   * Write a new value for the EUNumberRange Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NumberRange} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEuNumberRange(NumberRange value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEuNumberRange}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NumberRange> readEuNumberRangeAsync();

  /**
   * An asynchronous implementation of {@link #writeEuNumberRange}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEuNumberRangeAsync(NumberRange value);

  /**
   * Get the EUNumberRange {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EUNumberRange {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEuNumberRangeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEuNumberRangeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEuNumberRangeNodeAsync();
}
