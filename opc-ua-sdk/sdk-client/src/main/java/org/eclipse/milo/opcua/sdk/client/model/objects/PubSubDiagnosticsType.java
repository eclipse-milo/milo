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
import org.eclipse.milo.opcua.sdk.client.model.variables.PubSubDiagnosticsCounterType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.2</a>
 */
public interface PubSubDiagnosticsType extends BaseObjectType {
  /**
   * Get the local value of the DiagnosticsLevel Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DiagnosticsLevel Node.
   * @throws UaException if an error occurs creating or getting the DiagnosticsLevel Node.
   */
  DiagnosticsLevel getDiagnosticsLevel() throws UaException;

  /**
   * Set the local value of the DiagnosticsLevel Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DiagnosticsLevel Node.
   * @throws UaException if an error occurs creating or getting the DiagnosticsLevel Node.
   */
  void setDiagnosticsLevel(DiagnosticsLevel value) throws UaException;

  /**
   * Read the value of the DiagnosticsLevel Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DiagnosticsLevel} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DiagnosticsLevel readDiagnosticsLevel() throws UaException;

  /**
   * Write a new value for the DiagnosticsLevel Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DiagnosticsLevel} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDiagnosticsLevel(DiagnosticsLevel value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDiagnosticsLevel}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DiagnosticsLevel> readDiagnosticsLevelAsync();

  /**
   * An asynchronous implementation of {@link #writeDiagnosticsLevel}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDiagnosticsLevelAsync(DiagnosticsLevel value);

  /**
   * Get the DiagnosticsLevel {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DiagnosticsLevel {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getDiagnosticsLevelNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDiagnosticsLevelNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDiagnosticsLevelNodeAsync();

  /**
   * Get the local value of the TotalInformation Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TotalInformation Node.
   * @throws UaException if an error occurs creating or getting the TotalInformation Node.
   */
  UInteger getTotalInformation() throws UaException;

  /**
   * Set the local value of the TotalInformation Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TotalInformation Node.
   * @throws UaException if an error occurs creating or getting the TotalInformation Node.
   */
  void setTotalInformation(UInteger value) throws UaException;

  /**
   * Read the value of the TotalInformation Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readTotalInformation() throws UaException;

  /**
   * Write a new value for the TotalInformation Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTotalInformation(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTotalInformation}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readTotalInformationAsync();

  /**
   * An asynchronous implementation of {@link #writeTotalInformation}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTotalInformationAsync(UInteger value);

  /**
   * Get the TotalInformation {@link PubSubDiagnosticsCounterType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TotalInformation {@link PubSubDiagnosticsCounterType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PubSubDiagnosticsCounterType getTotalInformationNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTotalInformationNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PubSubDiagnosticsCounterType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PubSubDiagnosticsCounterType> getTotalInformationNodeAsync();

  /**
   * Get the local value of the TotalError Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TotalError Node.
   * @throws UaException if an error occurs creating or getting the TotalError Node.
   */
  UInteger getTotalError() throws UaException;

  /**
   * Set the local value of the TotalError Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TotalError Node.
   * @throws UaException if an error occurs creating or getting the TotalError Node.
   */
  void setTotalError(UInteger value) throws UaException;

  /**
   * Read the value of the TotalError Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readTotalError() throws UaException;

  /**
   * Write a new value for the TotalError Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTotalError(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTotalError}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readTotalErrorAsync();

  /**
   * An asynchronous implementation of {@link #writeTotalError}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTotalErrorAsync(UInteger value);

  /**
   * Get the TotalError {@link PubSubDiagnosticsCounterType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TotalError {@link PubSubDiagnosticsCounterType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PubSubDiagnosticsCounterType getTotalErrorNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTotalErrorNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PubSubDiagnosticsCounterType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PubSubDiagnosticsCounterType> getTotalErrorNodeAsync();

  /**
   * Get the local value of the SubError Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SubError Node.
   * @throws UaException if an error occurs creating or getting the SubError Node.
   */
  Boolean getSubError() throws UaException;

  /**
   * Set the local value of the SubError Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SubError Node.
   * @throws UaException if an error occurs creating or getting the SubError Node.
   */
  void setSubError(Boolean value) throws UaException;

  /**
   * Read the value of the SubError Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readSubError() throws UaException;

  /**
   * Write a new value for the SubError Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSubError(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSubError}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readSubErrorAsync();

  /**
   * An asynchronous implementation of {@link #writeSubError}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSubErrorAsync(Boolean value);

  /**
   * Get the SubError {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SubError {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSubErrorNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSubErrorNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSubErrorNodeAsync();

  /**
   * Get the Counters {@link BaseObjectType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Counters {@link BaseObjectType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseObjectType getCountersNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCountersNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseObjectType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseObjectType> getCountersNodeAsync();

  /**
   * Get the LiveValues {@link BaseObjectType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LiveValues {@link BaseObjectType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseObjectType getLiveValuesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLiveValuesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseObjectType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseObjectType> getLiveValuesNodeAsync();
}
