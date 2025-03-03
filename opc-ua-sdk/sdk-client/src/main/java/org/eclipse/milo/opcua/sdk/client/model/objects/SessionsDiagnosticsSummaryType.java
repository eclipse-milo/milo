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
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionSecurityDiagnosticsArrayType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.4">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.4</a>
 */
public interface SessionsDiagnosticsSummaryType extends BaseObjectType {
  /**
   * Get the local value of the SessionDiagnosticsArray Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SessionDiagnosticsArray Node.
   * @throws UaException if an error occurs creating or getting the SessionDiagnosticsArray Node.
   */
  SessionDiagnosticsDataType[] getSessionDiagnosticsArray() throws UaException;

  /**
   * Set the local value of the SessionDiagnosticsArray Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SessionDiagnosticsArray Node.
   * @throws UaException if an error occurs creating or getting the SessionDiagnosticsArray Node.
   */
  void setSessionDiagnosticsArray(SessionDiagnosticsDataType[] value) throws UaException;

  /**
   * Read the value of the SessionDiagnosticsArray Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link SessionDiagnosticsDataType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  SessionDiagnosticsDataType[] readSessionDiagnosticsArray() throws UaException;

  /**
   * Write a new value for the SessionDiagnosticsArray Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link SessionDiagnosticsDataType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSessionDiagnosticsArray(SessionDiagnosticsDataType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSessionDiagnosticsArray}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends SessionDiagnosticsDataType[]> readSessionDiagnosticsArrayAsync();

  /**
   * An asynchronous implementation of {@link #writeSessionDiagnosticsArray}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSessionDiagnosticsArrayAsync(
      SessionDiagnosticsDataType[] value);

  /**
   * Get the SessionDiagnosticsArray {@link SessionDiagnosticsArrayType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SessionDiagnosticsArray {@link SessionDiagnosticsArrayType} Node, or {@code null}
   *     if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SessionDiagnosticsArrayType getSessionDiagnosticsArrayNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSessionDiagnosticsArrayNode()}.
   *
   * @return a CompletableFuture that completes successfully with the SessionDiagnosticsArrayType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends SessionDiagnosticsArrayType> getSessionDiagnosticsArrayNodeAsync();

  /**
   * Get the local value of the SessionSecurityDiagnosticsArray Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SessionSecurityDiagnosticsArray Node.
   * @throws UaException if an error occurs creating or getting the SessionSecurityDiagnosticsArray
   *     Node.
   */
  SessionSecurityDiagnosticsDataType[] getSessionSecurityDiagnosticsArray() throws UaException;

  /**
   * Set the local value of the SessionSecurityDiagnosticsArray Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SessionSecurityDiagnosticsArray Node.
   * @throws UaException if an error occurs creating or getting the SessionSecurityDiagnosticsArray
   *     Node.
   */
  void setSessionSecurityDiagnosticsArray(SessionSecurityDiagnosticsDataType[] value)
      throws UaException;

  /**
   * Read the value of the SessionSecurityDiagnosticsArray Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link SessionSecurityDiagnosticsDataType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  SessionSecurityDiagnosticsDataType[] readSessionSecurityDiagnosticsArray() throws UaException;

  /**
   * Write a new value for the SessionSecurityDiagnosticsArray Node to the server and update the
   * local value if the operation succeeds.
   *
   * @param value the {@link SessionSecurityDiagnosticsDataType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSessionSecurityDiagnosticsArray(SessionSecurityDiagnosticsDataType[] value)
      throws UaException;

  /**
   * An asynchronous implementation of {@link #readSessionSecurityDiagnosticsArray}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends SessionSecurityDiagnosticsDataType[]>
      readSessionSecurityDiagnosticsArrayAsync();

  /**
   * An asynchronous implementation of {@link #writeSessionSecurityDiagnosticsArray}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsArrayAsync(
      SessionSecurityDiagnosticsDataType[] value);

  /**
   * Get the SessionSecurityDiagnosticsArray {@link SessionSecurityDiagnosticsArrayType} Node, or
   * {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SessionSecurityDiagnosticsArray {@link SessionSecurityDiagnosticsArrayType} Node,
   *     or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SessionSecurityDiagnosticsArrayType getSessionSecurityDiagnosticsArrayNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSessionSecurityDiagnosticsArrayNode()}.
   *
   * @return a CompletableFuture that completes successfully with the
   *     SessionSecurityDiagnosticsArrayType Node or completes exceptionally if an error occurs
   *     creating or getting the Node.
   */
  CompletableFuture<? extends SessionSecurityDiagnosticsArrayType>
      getSessionSecurityDiagnosticsArrayNodeAsync();
}
