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
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SamplingIntervalDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerDiagnosticsSummaryType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SubscriptionDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.3">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.3</a>
 */
public interface ServerDiagnosticsType extends BaseObjectType {
  QualifiedProperty<Boolean> ENABLED_FLAG =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EnabledFlag",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /**
   * Get the local value of the EnabledFlag Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EnabledFlag Node.
   * @throws UaException if an error occurs creating or getting the EnabledFlag Node.
   */
  Boolean getEnabledFlag() throws UaException;

  /**
   * Set the local value of the EnabledFlag Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EnabledFlag Node.
   * @throws UaException if an error occurs creating or getting the EnabledFlag Node.
   */
  void setEnabledFlag(Boolean value) throws UaException;

  /**
   * Read the value of the EnabledFlag Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readEnabledFlag() throws UaException;

  /**
   * Write a new value for the EnabledFlag Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEnabledFlag(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEnabledFlag}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readEnabledFlagAsync();

  /**
   * An asynchronous implementation of {@link #writeEnabledFlag}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEnabledFlagAsync(Boolean value);

  /**
   * Get the EnabledFlag {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EnabledFlag {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEnabledFlagNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEnabledFlagNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEnabledFlagNodeAsync();

  /**
   * Get the local value of the ServerDiagnosticsSummary Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ServerDiagnosticsSummary Node.
   * @throws UaException if an error occurs creating or getting the ServerDiagnosticsSummary Node.
   */
  ServerDiagnosticsSummaryDataType getServerDiagnosticsSummary() throws UaException;

  /**
   * Set the local value of the ServerDiagnosticsSummary Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ServerDiagnosticsSummary Node.
   * @throws UaException if an error occurs creating or getting the ServerDiagnosticsSummary Node.
   */
  void setServerDiagnosticsSummary(ServerDiagnosticsSummaryDataType value) throws UaException;

  /**
   * Read the value of the ServerDiagnosticsSummary Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link ServerDiagnosticsSummaryDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ServerDiagnosticsSummaryDataType readServerDiagnosticsSummary() throws UaException;

  /**
   * Write a new value for the ServerDiagnosticsSummary Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link ServerDiagnosticsSummaryDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeServerDiagnosticsSummary(ServerDiagnosticsSummaryDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readServerDiagnosticsSummary}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ServerDiagnosticsSummaryDataType> readServerDiagnosticsSummaryAsync();

  /**
   * An asynchronous implementation of {@link #writeServerDiagnosticsSummary}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeServerDiagnosticsSummaryAsync(
      ServerDiagnosticsSummaryDataType value);

  /**
   * Get the ServerDiagnosticsSummary {@link ServerDiagnosticsSummaryType} Node, or {@code null} if
   * it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ServerDiagnosticsSummary {@link ServerDiagnosticsSummaryType} Node, or {@code null}
   *     if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  ServerDiagnosticsSummaryType getServerDiagnosticsSummaryNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getServerDiagnosticsSummaryNode()}.
   *
   * @return a CompletableFuture that completes successfully with the ServerDiagnosticsSummaryType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends ServerDiagnosticsSummaryType> getServerDiagnosticsSummaryNodeAsync();

  /**
   * Get the local value of the SamplingIntervalDiagnosticsArray Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SamplingIntervalDiagnosticsArray Node.
   * @throws UaException if an error occurs creating or getting the SamplingIntervalDiagnosticsArray
   *     Node.
   */
  SamplingIntervalDiagnosticsDataType[] getSamplingIntervalDiagnosticsArray() throws UaException;

  /**
   * Set the local value of the SamplingIntervalDiagnosticsArray Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SamplingIntervalDiagnosticsArray Node.
   * @throws UaException if an error occurs creating or getting the SamplingIntervalDiagnosticsArray
   *     Node.
   */
  void setSamplingIntervalDiagnosticsArray(SamplingIntervalDiagnosticsDataType[] value)
      throws UaException;

  /**
   * Read the value of the SamplingIntervalDiagnosticsArray Node from the server and update the
   * local value if the operation succeeds.
   *
   * @return the {@link SamplingIntervalDiagnosticsDataType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  SamplingIntervalDiagnosticsDataType[] readSamplingIntervalDiagnosticsArray() throws UaException;

  /**
   * Write a new value for the SamplingIntervalDiagnosticsArray Node to the server and update the
   * local value if the operation succeeds.
   *
   * @param value the {@link SamplingIntervalDiagnosticsDataType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSamplingIntervalDiagnosticsArray(SamplingIntervalDiagnosticsDataType[] value)
      throws UaException;

  /**
   * An asynchronous implementation of {@link #readSamplingIntervalDiagnosticsArray}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends SamplingIntervalDiagnosticsDataType[]>
      readSamplingIntervalDiagnosticsArrayAsync();

  /**
   * An asynchronous implementation of {@link #writeSamplingIntervalDiagnosticsArray}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSamplingIntervalDiagnosticsArrayAsync(
      SamplingIntervalDiagnosticsDataType[] value);

  /**
   * Get the SamplingIntervalDiagnosticsArray {@link SamplingIntervalDiagnosticsArrayType} Node, or
   * {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SamplingIntervalDiagnosticsArray {@link SamplingIntervalDiagnosticsArrayType} Node,
   *     or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SamplingIntervalDiagnosticsArrayType getSamplingIntervalDiagnosticsArrayNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSamplingIntervalDiagnosticsArrayNode()}.
   *
   * @return a CompletableFuture that completes successfully with the
   *     SamplingIntervalDiagnosticsArrayType Node or completes exceptionally if an error occurs
   *     creating or getting the Node.
   */
  CompletableFuture<? extends SamplingIntervalDiagnosticsArrayType>
      getSamplingIntervalDiagnosticsArrayNodeAsync();

  /**
   * Get the local value of the SubscriptionDiagnosticsArray Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SubscriptionDiagnosticsArray Node.
   * @throws UaException if an error occurs creating or getting the SubscriptionDiagnosticsArray
   *     Node.
   */
  SubscriptionDiagnosticsDataType[] getSubscriptionDiagnosticsArray() throws UaException;

  /**
   * Set the local value of the SubscriptionDiagnosticsArray Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SubscriptionDiagnosticsArray Node.
   * @throws UaException if an error occurs creating or getting the SubscriptionDiagnosticsArray
   *     Node.
   */
  void setSubscriptionDiagnosticsArray(SubscriptionDiagnosticsDataType[] value) throws UaException;

  /**
   * Read the value of the SubscriptionDiagnosticsArray Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link SubscriptionDiagnosticsDataType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  SubscriptionDiagnosticsDataType[] readSubscriptionDiagnosticsArray() throws UaException;

  /**
   * Write a new value for the SubscriptionDiagnosticsArray Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link SubscriptionDiagnosticsDataType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSubscriptionDiagnosticsArray(SubscriptionDiagnosticsDataType[] value)
      throws UaException;

  /**
   * An asynchronous implementation of {@link #readSubscriptionDiagnosticsArray}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends SubscriptionDiagnosticsDataType[]>
      readSubscriptionDiagnosticsArrayAsync();

  /**
   * An asynchronous implementation of {@link #writeSubscriptionDiagnosticsArray}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSubscriptionDiagnosticsArrayAsync(
      SubscriptionDiagnosticsDataType[] value);

  /**
   * Get the SubscriptionDiagnosticsArray {@link SubscriptionDiagnosticsArrayType} Node, or {@code
   * null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SubscriptionDiagnosticsArray {@link SubscriptionDiagnosticsArrayType} Node, or
   *     {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SubscriptionDiagnosticsArrayType getSubscriptionDiagnosticsArrayNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSubscriptionDiagnosticsArrayNode()}.
   *
   * @return a CompletableFuture that completes successfully with the
   *     SubscriptionDiagnosticsArrayType Node or completes exceptionally if an error occurs
   *     creating or getting the Node.
   */
  CompletableFuture<? extends SubscriptionDiagnosticsArrayType>
      getSubscriptionDiagnosticsArrayNodeAsync();

  /**
   * Get the SessionsDiagnosticsSummary {@link SessionsDiagnosticsSummaryType} Node, or {@code null}
   * if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SessionsDiagnosticsSummary {@link SessionsDiagnosticsSummaryType} Node, or {@code
   *     null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SessionsDiagnosticsSummaryType getSessionsDiagnosticsSummaryNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSessionsDiagnosticsSummaryNode()}.
   *
   * @return a CompletableFuture that completes successfully with the SessionsDiagnosticsSummaryType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends SessionsDiagnosticsSummaryType>
      getSessionsDiagnosticsSummaryNodeAsync();
}
