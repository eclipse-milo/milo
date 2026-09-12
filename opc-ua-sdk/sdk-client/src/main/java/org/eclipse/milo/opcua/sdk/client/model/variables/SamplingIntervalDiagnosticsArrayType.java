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
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.9">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.9</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SamplingIntervalDiagnosticsArrayType extends BaseDataVariableType {
  /** Gets the existing node's local value. */
  @Nullable SamplingIntervalDiagnosticsDataType getSamplingIntervalDiagnostics() throws UaException;

  /** Sets the existing node's local value. */
  void setSamplingIntervalDiagnostics(@Nullable SamplingIntervalDiagnosticsDataType value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable SamplingIntervalDiagnosticsDataType readSamplingIntervalDiagnostics()
      throws UaException;

  /** Writes the value remotely. */
  void writeSamplingIntervalDiagnostics(@Nullable SamplingIntervalDiagnosticsDataType value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable SamplingIntervalDiagnosticsDataType>
      readSamplingIntervalDiagnosticsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSamplingIntervalDiagnosticsAsync(
      @Nullable SamplingIntervalDiagnosticsDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SamplingIntervalDiagnosticsType getSamplingIntervalDiagnosticsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends SamplingIntervalDiagnosticsType>
      getSamplingIntervalDiagnosticsNodeAsync();
}
