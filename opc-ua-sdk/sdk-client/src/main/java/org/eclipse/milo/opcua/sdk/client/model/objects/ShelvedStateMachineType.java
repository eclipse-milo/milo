/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.1">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.1</a>
 */
public interface ShelvedStateMachineType extends FiniteStateMachineType {
  QualifiedProperty<Double> UNSHELVE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UnshelveTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /**
   * Get the local value of the UnshelveTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the UnshelveTime Node.
   * @throws UaException if an error occurs creating or getting the UnshelveTime Node.
   */
  Double getUnshelveTime() throws UaException;

  /**
   * Set the local value of the UnshelveTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the UnshelveTime Node.
   * @throws UaException if an error occurs creating or getting the UnshelveTime Node.
   */
  void setUnshelveTime(Double value) throws UaException;

  /**
   * Read the value of the UnshelveTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readUnshelveTime() throws UaException;

  /**
   * Write a new value for the UnshelveTime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeUnshelveTime(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readUnshelveTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readUnshelveTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeUnshelveTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeUnshelveTimeAsync(Double value);

  /**
   * Get the UnshelveTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UnshelveTime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getUnshelveTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUnshelveTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getUnshelveTimeNodeAsync();

  /**
   * Get the Unshelved {@link StateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Unshelved {@link StateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateType getUnshelvedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUnshelvedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateType> getUnshelvedNodeAsync();

  /**
   * Get the TimedShelved {@link StateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TimedShelved {@link StateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateType getTimedShelvedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTimedShelvedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateType> getTimedShelvedNodeAsync();

  /**
   * Get the OneShotShelved {@link StateType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the OneShotShelved {@link StateType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  StateType getOneShotShelvedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOneShotShelvedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the StateType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends StateType> getOneShotShelvedNodeAsync();

  /**
   * Get the UnshelvedToTimedShelved {@link TransitionType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UnshelvedToTimedShelved {@link TransitionType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getUnshelvedToTimedShelvedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUnshelvedToTimedShelvedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getUnshelvedToTimedShelvedNodeAsync();

  /**
   * Get the UnshelvedToOneShotShelved {@link TransitionType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UnshelvedToOneShotShelved {@link TransitionType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getUnshelvedToOneShotShelvedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUnshelvedToOneShotShelvedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getUnshelvedToOneShotShelvedNodeAsync();

  /**
   * Get the TimedShelvedToUnshelved {@link TransitionType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TimedShelvedToUnshelved {@link TransitionType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getTimedShelvedToUnshelvedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTimedShelvedToUnshelvedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getTimedShelvedToUnshelvedNodeAsync();

  /**
   * Get the TimedShelvedToOneShotShelved {@link TransitionType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TimedShelvedToOneShotShelved {@link TransitionType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getTimedShelvedToOneShotShelvedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTimedShelvedToOneShotShelvedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getTimedShelvedToOneShotShelvedNodeAsync();

  /**
   * Get the OneShotShelvedToUnshelved {@link TransitionType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the OneShotShelvedToUnshelved {@link TransitionType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getOneShotShelvedToUnshelvedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOneShotShelvedToUnshelvedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getOneShotShelvedToUnshelvedNodeAsync();

  /**
   * Get the OneShotShelvedToTimedShelved {@link TransitionType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the OneShotShelvedToTimedShelved {@link TransitionType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransitionType getOneShotShelvedToTimedShelvedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOneShotShelvedToTimedShelvedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransitionType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransitionType> getOneShotShelvedToTimedShelvedNodeAsync();
}
