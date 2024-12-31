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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.9">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.9</a>
 */
public interface IIeeeBaseTsnStatusStreamType extends BaseInterfaceType {
  /**
   * Get the local value of the TalkerStatus Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TalkerStatus Node.
   * @throws UaException if an error occurs creating or getting the TalkerStatus Node.
   */
  TsnTalkerStatus getTalkerStatus() throws UaException;

  /**
   * Set the local value of the TalkerStatus Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TalkerStatus Node.
   * @throws UaException if an error occurs creating or getting the TalkerStatus Node.
   */
  void setTalkerStatus(TsnTalkerStatus value) throws UaException;

  /**
   * Read the value of the TalkerStatus Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link TsnTalkerStatus} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  TsnTalkerStatus readTalkerStatus() throws UaException;

  /**
   * Write a new value for the TalkerStatus Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link TsnTalkerStatus} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTalkerStatus(TsnTalkerStatus value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTalkerStatus}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends TsnTalkerStatus> readTalkerStatusAsync();

  /**
   * An asynchronous implementation of {@link #writeTalkerStatus}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTalkerStatusAsync(TsnTalkerStatus value);

  /**
   * Get the TalkerStatus {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TalkerStatus {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getTalkerStatusNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTalkerStatusNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getTalkerStatusNodeAsync();

  /**
   * Get the local value of the ListenerStatus Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ListenerStatus Node.
   * @throws UaException if an error occurs creating or getting the ListenerStatus Node.
   */
  TsnListenerStatus getListenerStatus() throws UaException;

  /**
   * Set the local value of the ListenerStatus Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ListenerStatus Node.
   * @throws UaException if an error occurs creating or getting the ListenerStatus Node.
   */
  void setListenerStatus(TsnListenerStatus value) throws UaException;

  /**
   * Read the value of the ListenerStatus Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link TsnListenerStatus} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  TsnListenerStatus readListenerStatus() throws UaException;

  /**
   * Write a new value for the ListenerStatus Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link TsnListenerStatus} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeListenerStatus(TsnListenerStatus value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readListenerStatus}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends TsnListenerStatus> readListenerStatusAsync();

  /**
   * An asynchronous implementation of {@link #writeListenerStatus}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeListenerStatusAsync(TsnListenerStatus value);

  /**
   * Get the ListenerStatus {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ListenerStatus {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getListenerStatusNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getListenerStatusNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getListenerStatusNodeAsync();

  /**
   * Get the local value of the FailureCode Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the FailureCode Node.
   * @throws UaException if an error occurs creating or getting the FailureCode Node.
   */
  TsnFailureCode getFailureCode() throws UaException;

  /**
   * Set the local value of the FailureCode Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the FailureCode Node.
   * @throws UaException if an error occurs creating or getting the FailureCode Node.
   */
  void setFailureCode(TsnFailureCode value) throws UaException;

  /**
   * Read the value of the FailureCode Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link TsnFailureCode} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  TsnFailureCode readFailureCode() throws UaException;

  /**
   * Write a new value for the FailureCode Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link TsnFailureCode} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeFailureCode(TsnFailureCode value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readFailureCode}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends TsnFailureCode> readFailureCodeAsync();

  /**
   * An asynchronous implementation of {@link #writeFailureCode}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeFailureCodeAsync(TsnFailureCode value);

  /**
   * Get the FailureCode {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the FailureCode {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getFailureCodeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getFailureCodeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getFailureCodeNodeAsync();

  /**
   * Get the local value of the FailureSystemIdentifier Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the FailureSystemIdentifier Node.
   * @throws UaException if an error occurs creating or getting the FailureSystemIdentifier Node.
   */
  Object getFailureSystemIdentifier() throws UaException;

  /**
   * Set the local value of the FailureSystemIdentifier Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the FailureSystemIdentifier Node.
   * @throws UaException if an error occurs creating or getting the FailureSystemIdentifier Node.
   */
  void setFailureSystemIdentifier(Object value) throws UaException;

  /**
   * Read the value of the FailureSystemIdentifier Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link Object} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Object readFailureSystemIdentifier() throws UaException;

  /**
   * Write a new value for the FailureSystemIdentifier Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Object} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeFailureSystemIdentifier(Object value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readFailureSystemIdentifier}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<?> readFailureSystemIdentifierAsync();

  /**
   * An asynchronous implementation of {@link #writeFailureSystemIdentifier}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeFailureSystemIdentifierAsync(Object value);

  /**
   * Get the FailureSystemIdentifier {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the FailureSystemIdentifier {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getFailureSystemIdentifierNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getFailureSystemIdentifierNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getFailureSystemIdentifierNodeAsync();
}
