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
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.13">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.13</a>
 */
public interface SessionDiagnosticsArrayType extends BaseDataVariableType {
  /**
   * Get the local value of the SessionDiagnostics Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SessionDiagnostics Node.
   * @throws UaException if an error occurs creating or getting the SessionDiagnostics Node.
   */
  SessionDiagnosticsDataType getSessionDiagnostics() throws UaException;

  /**
   * Set the local value of the SessionDiagnostics Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SessionDiagnostics Node.
   * @throws UaException if an error occurs creating or getting the SessionDiagnostics Node.
   */
  void setSessionDiagnostics(SessionDiagnosticsDataType value) throws UaException;

  /**
   * Read the value of the SessionDiagnostics Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link SessionDiagnosticsDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  SessionDiagnosticsDataType readSessionDiagnostics() throws UaException;

  /**
   * Write a new value for the SessionDiagnostics Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link SessionDiagnosticsDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSessionDiagnostics(SessionDiagnosticsDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSessionDiagnostics}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends SessionDiagnosticsDataType> readSessionDiagnosticsAsync();

  /**
   * An asynchronous implementation of {@link #writeSessionDiagnostics}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSessionDiagnosticsAsync(SessionDiagnosticsDataType value);

  /**
   * Get the SessionDiagnostics {@link SessionDiagnosticsVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SessionDiagnostics {@link SessionDiagnosticsVariableType} Node, or {@code null} if
   *     it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SessionDiagnosticsVariableType getSessionDiagnosticsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSessionDiagnosticsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the SessionDiagnosticsVariableType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends SessionDiagnosticsVariableType> getSessionDiagnosticsNodeAsync();
}
