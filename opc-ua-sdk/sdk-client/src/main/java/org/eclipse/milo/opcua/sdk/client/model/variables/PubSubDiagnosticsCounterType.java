/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.5">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.5</a>
 */
public interface PubSubDiagnosticsCounterType extends BaseDataVariableType {
  QualifiedProperty<Boolean> ACTIVE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Active",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<PubSubDiagnosticsCounterClassification> CLASSIFICATION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Classification",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19730"),
          -1,
          PubSubDiagnosticsCounterClassification.class);

  QualifiedProperty<DiagnosticsLevel> DIAGNOSTICS_LEVEL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DiagnosticsLevel",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19723"),
          -1,
          DiagnosticsLevel.class);

  QualifiedProperty<DateTime> TIME_FIRST_CHANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TimeFirstChange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  /**
   * Get the local value of the Active Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Active Node.
   * @throws UaException if an error occurs creating or getting the Active Node.
   */
  Boolean getActive() throws UaException;

  /**
   * Set the local value of the Active Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Active Node.
   * @throws UaException if an error occurs creating or getting the Active Node.
   */
  void setActive(Boolean value) throws UaException;

  /**
   * Read the value of the Active Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readActive() throws UaException;

  /**
   * Write a new value for the Active Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeActive(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readActive}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readActiveAsync();

  /**
   * An asynchronous implementation of {@link #writeActive}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeActiveAsync(Boolean value);

  /**
   * Get the Active {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Active {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getActiveNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getActiveNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getActiveNodeAsync();

  /**
   * Get the local value of the Classification Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Classification Node.
   * @throws UaException if an error occurs creating or getting the Classification Node.
   */
  PubSubDiagnosticsCounterClassification getClassification() throws UaException;

  /**
   * Set the local value of the Classification Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Classification Node.
   * @throws UaException if an error occurs creating or getting the Classification Node.
   */
  void setClassification(PubSubDiagnosticsCounterClassification value) throws UaException;

  /**
   * Read the value of the Classification Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link PubSubDiagnosticsCounterClassification} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  PubSubDiagnosticsCounterClassification readClassification() throws UaException;

  /**
   * Write a new value for the Classification Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link PubSubDiagnosticsCounterClassification} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeClassification(PubSubDiagnosticsCounterClassification value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readClassification}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends PubSubDiagnosticsCounterClassification> readClassificationAsync();

  /**
   * An asynchronous implementation of {@link #writeClassification}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeClassificationAsync(
      PubSubDiagnosticsCounterClassification value);

  /**
   * Get the Classification {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Classification {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getClassificationNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getClassificationNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getClassificationNodeAsync();

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
   * Get the DiagnosticsLevel {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DiagnosticsLevel {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDiagnosticsLevelNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDiagnosticsLevelNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDiagnosticsLevelNodeAsync();

  /**
   * Get the local value of the TimeFirstChange Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TimeFirstChange Node.
   * @throws UaException if an error occurs creating or getting the TimeFirstChange Node.
   */
  DateTime getTimeFirstChange() throws UaException;

  /**
   * Set the local value of the TimeFirstChange Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TimeFirstChange Node.
   * @throws UaException if an error occurs creating or getting the TimeFirstChange Node.
   */
  void setTimeFirstChange(DateTime value) throws UaException;

  /**
   * Read the value of the TimeFirstChange Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readTimeFirstChange() throws UaException;

  /**
   * Write a new value for the TimeFirstChange Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTimeFirstChange(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTimeFirstChange}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readTimeFirstChangeAsync();

  /**
   * An asynchronous implementation of {@link #writeTimeFirstChange}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTimeFirstChangeAsync(DateTime value);

  /**
   * Get the TimeFirstChange {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TimeFirstChange {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getTimeFirstChangeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTimeFirstChangeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getTimeFirstChangeNodeAsync();
}
