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
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.19/#5.8.19.3">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.19/#5.8.19.3</a>
 */
public interface ExclusiveLimitAlarmType extends LimitAlarmType {
  /**
   * Get the local value of the ActiveState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ActiveState Node.
   * @throws UaException if an error occurs creating or getting the ActiveState Node.
   */
  LocalizedText getActiveState() throws UaException;

  /**
   * Set the local value of the ActiveState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ActiveState Node.
   * @throws UaException if an error occurs creating or getting the ActiveState Node.
   */
  void setActiveState(LocalizedText value) throws UaException;

  /**
   * Read the value of the ActiveState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readActiveState() throws UaException;

  /**
   * Write a new value for the ActiveState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeActiveState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readActiveState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readActiveStateAsync();

  /**
   * An asynchronous implementation of {@link #writeActiveState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeActiveStateAsync(LocalizedText value);

  /**
   * Get the ActiveState {@link TwoStateVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ActiveState {@link TwoStateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TwoStateVariableType getActiveStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getActiveStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TwoStateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TwoStateVariableType> getActiveStateNodeAsync();

  /**
   * Get the LimitState {@link ExclusiveLimitStateMachineType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LimitState {@link ExclusiveLimitStateMachineType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  ExclusiveLimitStateMachineType getLimitStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLimitStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the ExclusiveLimitStateMachineType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends ExclusiveLimitStateMachineType> getLimitStateNodeAsync();
}
