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
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.15">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.15</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SessionSecurityDiagnosticsArrayType extends BaseDataVariableType {
  /** Gets the existing node's local value. */
  @Nullable SessionSecurityDiagnosticsDataType getSessionSecurityDiagnostics() throws UaException;

  /** Sets the existing node's local value. */
  void setSessionSecurityDiagnostics(@Nullable SessionSecurityDiagnosticsDataType value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable SessionSecurityDiagnosticsDataType readSessionSecurityDiagnostics() throws UaException;

  /** Writes the value remotely. */
  void writeSessionSecurityDiagnostics(@Nullable SessionSecurityDiagnosticsDataType value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable SessionSecurityDiagnosticsDataType>
      readSessionSecurityDiagnosticsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsAsync(
      @Nullable SessionSecurityDiagnosticsDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SessionSecurityDiagnosticsType getSessionSecurityDiagnosticsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends SessionSecurityDiagnosticsType>
      getSessionSecurityDiagnosticsNodeAsync();
}
