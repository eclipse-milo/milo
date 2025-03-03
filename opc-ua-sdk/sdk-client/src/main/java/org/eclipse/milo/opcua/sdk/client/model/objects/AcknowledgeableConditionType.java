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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.2</a>
 */
public interface AcknowledgeableConditionType extends ConditionType {
  /**
   * Get the local value of the EnabledState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EnabledState Node.
   * @throws UaException if an error occurs creating or getting the EnabledState Node.
   */
  LocalizedText getEnabledState() throws UaException;

  /**
   * Set the local value of the EnabledState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EnabledState Node.
   * @throws UaException if an error occurs creating or getting the EnabledState Node.
   */
  void setEnabledState(LocalizedText value) throws UaException;

  /**
   * Read the value of the EnabledState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readEnabledState() throws UaException;

  /**
   * Write a new value for the EnabledState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEnabledState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEnabledState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readEnabledStateAsync();

  /**
   * An asynchronous implementation of {@link #writeEnabledState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEnabledStateAsync(LocalizedText value);

  /**
   * Get the EnabledState {@link TwoStateVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EnabledState {@link TwoStateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TwoStateVariableType getEnabledStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEnabledStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TwoStateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TwoStateVariableType> getEnabledStateNodeAsync();

  /**
   * Get the local value of the AckedState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AckedState Node.
   * @throws UaException if an error occurs creating or getting the AckedState Node.
   */
  LocalizedText getAckedState() throws UaException;

  /**
   * Set the local value of the AckedState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AckedState Node.
   * @throws UaException if an error occurs creating or getting the AckedState Node.
   */
  void setAckedState(LocalizedText value) throws UaException;

  /**
   * Read the value of the AckedState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readAckedState() throws UaException;

  /**
   * Write a new value for the AckedState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAckedState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAckedState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readAckedStateAsync();

  /**
   * An asynchronous implementation of {@link #writeAckedState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAckedStateAsync(LocalizedText value);

  /**
   * Get the AckedState {@link TwoStateVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AckedState {@link TwoStateVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TwoStateVariableType getAckedStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAckedStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TwoStateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TwoStateVariableType> getAckedStateNodeAsync();

  /**
   * Get the local value of the ConfirmedState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConfirmedState Node.
   * @throws UaException if an error occurs creating or getting the ConfirmedState Node.
   */
  LocalizedText getConfirmedState() throws UaException;

  /**
   * Set the local value of the ConfirmedState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConfirmedState Node.
   * @throws UaException if an error occurs creating or getting the ConfirmedState Node.
   */
  void setConfirmedState(LocalizedText value) throws UaException;

  /**
   * Read the value of the ConfirmedState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readConfirmedState() throws UaException;

  /**
   * Write a new value for the ConfirmedState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConfirmedState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConfirmedState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readConfirmedStateAsync();

  /**
   * An asynchronous implementation of {@link #writeConfirmedState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConfirmedStateAsync(LocalizedText value);

  /**
   * Get the ConfirmedState {@link TwoStateVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConfirmedState {@link TwoStateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TwoStateVariableType getConfirmedStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConfirmedStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TwoStateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TwoStateVariableType> getConfirmedStateNodeAsync();
}
