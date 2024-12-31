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
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.11">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.11</a>
 */
public interface SubscriptionDiagnosticsArrayType extends BaseDataVariableType {
  /**
   * Get the local value of the SubscriptionDiagnostics Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SubscriptionDiagnostics Node.
   * @throws UaException if an error occurs creating or getting the SubscriptionDiagnostics Node.
   */
  SubscriptionDiagnosticsDataType getSubscriptionDiagnostics() throws UaException;

  /**
   * Set the local value of the SubscriptionDiagnostics Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SubscriptionDiagnostics Node.
   * @throws UaException if an error occurs creating or getting the SubscriptionDiagnostics Node.
   */
  void setSubscriptionDiagnostics(SubscriptionDiagnosticsDataType value) throws UaException;

  /**
   * Read the value of the SubscriptionDiagnostics Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link SubscriptionDiagnosticsDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  SubscriptionDiagnosticsDataType readSubscriptionDiagnostics() throws UaException;

  /**
   * Write a new value for the SubscriptionDiagnostics Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link SubscriptionDiagnosticsDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSubscriptionDiagnostics(SubscriptionDiagnosticsDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSubscriptionDiagnostics}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends SubscriptionDiagnosticsDataType> readSubscriptionDiagnosticsAsync();

  /**
   * An asynchronous implementation of {@link #writeSubscriptionDiagnostics}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSubscriptionDiagnosticsAsync(
      SubscriptionDiagnosticsDataType value);

  /**
   * Get the SubscriptionDiagnostics {@link SubscriptionDiagnosticsType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SubscriptionDiagnostics {@link SubscriptionDiagnosticsType} Node, or {@code null}
   *     if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SubscriptionDiagnosticsType getSubscriptionDiagnosticsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSubscriptionDiagnosticsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the SubscriptionDiagnosticsType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends SubscriptionDiagnosticsType> getSubscriptionDiagnosticsNodeAsync();
}
