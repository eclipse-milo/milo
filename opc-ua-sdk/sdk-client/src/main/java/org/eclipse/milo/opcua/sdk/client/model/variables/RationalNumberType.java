/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.20">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.20</a>
 */
public interface RationalNumberType extends BaseDataVariableType {
  /**
   * Get the local value of the Numerator Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Numerator Node.
   * @throws UaException if an error occurs creating or getting the Numerator Node.
   */
  Integer getNumerator() throws UaException;

  /**
   * Set the local value of the Numerator Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Numerator Node.
   * @throws UaException if an error occurs creating or getting the Numerator Node.
   */
  void setNumerator(Integer value) throws UaException;

  /**
   * Read the value of the Numerator Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Integer} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Integer readNumerator() throws UaException;

  /**
   * Write a new value for the Numerator Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Integer} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNumerator(Integer value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNumerator}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Integer> readNumeratorAsync();

  /**
   * An asynchronous implementation of {@link #writeNumerator}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNumeratorAsync(Integer value);

  /**
   * Get the Numerator {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Numerator {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getNumeratorNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNumeratorNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getNumeratorNodeAsync();

  /**
   * Get the local value of the Denominator Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Denominator Node.
   * @throws UaException if an error occurs creating or getting the Denominator Node.
   */
  UInteger getDenominator() throws UaException;

  /**
   * Set the local value of the Denominator Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Denominator Node.
   * @throws UaException if an error occurs creating or getting the Denominator Node.
   */
  void setDenominator(UInteger value) throws UaException;

  /**
   * Read the value of the Denominator Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readDenominator() throws UaException;

  /**
   * Write a new value for the Denominator Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDenominator(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDenominator}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readDenominatorAsync();

  /**
   * An asynchronous implementation of {@link #writeDenominator}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDenominatorAsync(UInteger value);

  /**
   * Get the Denominator {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Denominator {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getDenominatorNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDenominatorNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDenominatorNodeAsync();
}
