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
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.11">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.11</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SubscriptionDiagnosticsArrayType extends BaseDataVariableType {
  /** Gets the existing node's local value. */
  @Nullable SubscriptionDiagnosticsDataType getSubscriptionDiagnostics() throws UaException;

  /** Sets the existing node's local value. */
  void setSubscriptionDiagnostics(@Nullable SubscriptionDiagnosticsDataType value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable SubscriptionDiagnosticsDataType readSubscriptionDiagnostics() throws UaException;

  /** Writes the value remotely. */
  void writeSubscriptionDiagnostics(@Nullable SubscriptionDiagnosticsDataType value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable SubscriptionDiagnosticsDataType>
      readSubscriptionDiagnosticsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSubscriptionDiagnosticsAsync(
      @Nullable SubscriptionDiagnosticsDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SubscriptionDiagnosticsType getSubscriptionDiagnosticsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends SubscriptionDiagnosticsType> getSubscriptionDiagnosticsNodeAsync();
}
