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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4</a>
 */
public interface LldpRemoteStatisticsType extends BaseObjectType {
  /**
   * Get the local value of the LastChangeTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastChangeTime Node.
   * @throws UaException if an error occurs creating or getting the LastChangeTime Node.
   */
  UInteger getLastChangeTime() throws UaException;

  /**
   * Set the local value of the LastChangeTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastChangeTime Node.
   * @throws UaException if an error occurs creating or getting the LastChangeTime Node.
   */
  void setLastChangeTime(UInteger value) throws UaException;

  /**
   * Read the value of the LastChangeTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readLastChangeTime() throws UaException;

  /**
   * Write a new value for the LastChangeTime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastChangeTime(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastChangeTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readLastChangeTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeLastChangeTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastChangeTimeAsync(UInteger value);

  /**
   * Get the LastChangeTime {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastChangeTime {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getLastChangeTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastChangeTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastChangeTimeNodeAsync();

  /**
   * Get the local value of the RemoteInserts Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RemoteInserts Node.
   * @throws UaException if an error occurs creating or getting the RemoteInserts Node.
   */
  UInteger getRemoteInserts() throws UaException;

  /**
   * Set the local value of the RemoteInserts Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RemoteInserts Node.
   * @throws UaException if an error occurs creating or getting the RemoteInserts Node.
   */
  void setRemoteInserts(UInteger value) throws UaException;

  /**
   * Read the value of the RemoteInserts Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readRemoteInserts() throws UaException;

  /**
   * Write a new value for the RemoteInserts Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRemoteInserts(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRemoteInserts}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readRemoteInsertsAsync();

  /**
   * An asynchronous implementation of {@link #writeRemoteInserts}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRemoteInsertsAsync(UInteger value);

  /**
   * Get the RemoteInserts {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RemoteInserts {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getRemoteInsertsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRemoteInsertsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRemoteInsertsNodeAsync();

  /**
   * Get the local value of the RemoteDeletes Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RemoteDeletes Node.
   * @throws UaException if an error occurs creating or getting the RemoteDeletes Node.
   */
  UInteger getRemoteDeletes() throws UaException;

  /**
   * Set the local value of the RemoteDeletes Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RemoteDeletes Node.
   * @throws UaException if an error occurs creating or getting the RemoteDeletes Node.
   */
  void setRemoteDeletes(UInteger value) throws UaException;

  /**
   * Read the value of the RemoteDeletes Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readRemoteDeletes() throws UaException;

  /**
   * Write a new value for the RemoteDeletes Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRemoteDeletes(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRemoteDeletes}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readRemoteDeletesAsync();

  /**
   * An asynchronous implementation of {@link #writeRemoteDeletes}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRemoteDeletesAsync(UInteger value);

  /**
   * Get the RemoteDeletes {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RemoteDeletes {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getRemoteDeletesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRemoteDeletesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRemoteDeletesNodeAsync();

  /**
   * Get the local value of the RemoteDrops Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RemoteDrops Node.
   * @throws UaException if an error occurs creating or getting the RemoteDrops Node.
   */
  UInteger getRemoteDrops() throws UaException;

  /**
   * Set the local value of the RemoteDrops Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RemoteDrops Node.
   * @throws UaException if an error occurs creating or getting the RemoteDrops Node.
   */
  void setRemoteDrops(UInteger value) throws UaException;

  /**
   * Read the value of the RemoteDrops Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readRemoteDrops() throws UaException;

  /**
   * Write a new value for the RemoteDrops Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRemoteDrops(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRemoteDrops}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readRemoteDropsAsync();

  /**
   * An asynchronous implementation of {@link #writeRemoteDrops}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRemoteDropsAsync(UInteger value);

  /**
   * Get the RemoteDrops {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RemoteDrops {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getRemoteDropsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRemoteDropsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRemoteDropsNodeAsync();

  /**
   * Get the local value of the RemoteAgeouts Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RemoteAgeouts Node.
   * @throws UaException if an error occurs creating or getting the RemoteAgeouts Node.
   */
  UInteger getRemoteAgeouts() throws UaException;

  /**
   * Set the local value of the RemoteAgeouts Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RemoteAgeouts Node.
   * @throws UaException if an error occurs creating or getting the RemoteAgeouts Node.
   */
  void setRemoteAgeouts(UInteger value) throws UaException;

  /**
   * Read the value of the RemoteAgeouts Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readRemoteAgeouts() throws UaException;

  /**
   * Write a new value for the RemoteAgeouts Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRemoteAgeouts(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRemoteAgeouts}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readRemoteAgeoutsAsync();

  /**
   * An asynchronous implementation of {@link #writeRemoteAgeouts}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRemoteAgeoutsAsync(UInteger value);

  /**
   * Get the RemoteAgeouts {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RemoteAgeouts {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getRemoteAgeoutsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRemoteAgeoutsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRemoteAgeoutsNodeAsync();
}
