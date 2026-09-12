/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.4">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.4</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SessionsDiagnosticsSummaryType extends BaseObjectType {
  /** Gets the existing node's local value. */
  @Nullable SessionDiagnosticsDataType @Nullable [] getSessionDiagnosticsArray() throws UaException;

  /** Sets the existing node's local value. */
  void setSessionDiagnosticsArray(@Nullable SessionDiagnosticsDataType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable SessionDiagnosticsDataType @Nullable [] readSessionDiagnosticsArray()
      throws UaException;

  /** Writes the value remotely. */
  void writeSessionDiagnosticsArray(@Nullable SessionDiagnosticsDataType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable SessionDiagnosticsDataType @Nullable []>
      readSessionDiagnosticsArrayAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSessionDiagnosticsArrayAsync(
      @Nullable SessionDiagnosticsDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SessionDiagnosticsArrayType getSessionDiagnosticsArrayNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends SessionDiagnosticsArrayType> getSessionDiagnosticsArrayNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable SessionSecurityDiagnosticsDataType @Nullable [] getSessionSecurityDiagnosticsArray()
      throws UaException;

  /** Sets the existing node's local value. */
  void setSessionSecurityDiagnosticsArray(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable SessionSecurityDiagnosticsDataType @Nullable [] readSessionSecurityDiagnosticsArray()
      throws UaException;

  /** Writes the value remotely. */
  void writeSessionSecurityDiagnosticsArray(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable SessionSecurityDiagnosticsDataType @Nullable []>
      readSessionSecurityDiagnosticsArrayAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsArrayAsync(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SessionSecurityDiagnosticsArrayType getSessionSecurityDiagnosticsArrayNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends SessionSecurityDiagnosticsArrayType>
      getSessionSecurityDiagnosticsArrayNodeAsync();
}
