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
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ExceptionDeviationFormat;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.2.2</a>
 */
public interface HistoricalDataConfigurationType extends BaseObjectType {
  QualifiedProperty<Boolean> STEPPED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Stepped",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<String> DEFINITION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Definition",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<Double> MAX_TIME_INTERVAL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxTimeInterval",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> MIN_TIME_INTERVAL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MinTimeInterval",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> EXCEPTION_DEVIATION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ExceptionDeviation",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<ExceptionDeviationFormat> EXCEPTION_DEVIATION_FORMAT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ExceptionDeviationFormat",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=890"),
          -1,
          ExceptionDeviationFormat.class);

  QualifiedProperty<DateTime> START_OF_ARCHIVE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StartOfArchive",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> START_OF_ONLINE_ARCHIVE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StartOfOnlineArchive",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<Boolean> SERVER_TIMESTAMP_SUPPORTED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServerTimestampSupported",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Double> MAX_TIME_STORED_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxTimeStoredValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<UInteger> MAX_COUNT_STORED_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxCountStoredValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  /**
   * Get the local value of the Stepped Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Stepped Node.
   * @throws UaException if an error occurs creating or getting the Stepped Node.
   */
  Boolean getStepped() throws UaException;

  /**
   * Set the local value of the Stepped Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Stepped Node.
   * @throws UaException if an error occurs creating or getting the Stepped Node.
   */
  void setStepped(Boolean value) throws UaException;

  /**
   * Read the value of the Stepped Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readStepped() throws UaException;

  /**
   * Write a new value for the Stepped Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeStepped(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readStepped}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readSteppedAsync();

  /**
   * An asynchronous implementation of {@link #writeStepped}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSteppedAsync(Boolean value);

  /**
   * Get the Stepped {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Stepped {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSteppedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSteppedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSteppedNodeAsync();

  /**
   * Get the local value of the Definition Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Definition Node.
   * @throws UaException if an error occurs creating or getting the Definition Node.
   */
  String getDefinition() throws UaException;

  /**
   * Set the local value of the Definition Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Definition Node.
   * @throws UaException if an error occurs creating or getting the Definition Node.
   */
  void setDefinition(String value) throws UaException;

  /**
   * Read the value of the Definition Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readDefinition() throws UaException;

  /**
   * Write a new value for the Definition Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDefinition(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDefinition}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readDefinitionAsync();

  /**
   * An asynchronous implementation of {@link #writeDefinition}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDefinitionAsync(String value);

  /**
   * Get the Definition {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Definition {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDefinitionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDefinitionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDefinitionNodeAsync();

  /**
   * Get the local value of the MaxTimeInterval Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxTimeInterval Node.
   * @throws UaException if an error occurs creating or getting the MaxTimeInterval Node.
   */
  Double getMaxTimeInterval() throws UaException;

  /**
   * Set the local value of the MaxTimeInterval Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxTimeInterval Node.
   * @throws UaException if an error occurs creating or getting the MaxTimeInterval Node.
   */
  void setMaxTimeInterval(Double value) throws UaException;

  /**
   * Read the value of the MaxTimeInterval Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readMaxTimeInterval() throws UaException;

  /**
   * Write a new value for the MaxTimeInterval Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxTimeInterval(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxTimeInterval}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readMaxTimeIntervalAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxTimeInterval}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxTimeIntervalAsync(Double value);

  /**
   * Get the MaxTimeInterval {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxTimeInterval {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxTimeIntervalNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxTimeIntervalNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxTimeIntervalNodeAsync();

  /**
   * Get the local value of the MinTimeInterval Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MinTimeInterval Node.
   * @throws UaException if an error occurs creating or getting the MinTimeInterval Node.
   */
  Double getMinTimeInterval() throws UaException;

  /**
   * Set the local value of the MinTimeInterval Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MinTimeInterval Node.
   * @throws UaException if an error occurs creating or getting the MinTimeInterval Node.
   */
  void setMinTimeInterval(Double value) throws UaException;

  /**
   * Read the value of the MinTimeInterval Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readMinTimeInterval() throws UaException;

  /**
   * Write a new value for the MinTimeInterval Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMinTimeInterval(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMinTimeInterval}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readMinTimeIntervalAsync();

  /**
   * An asynchronous implementation of {@link #writeMinTimeInterval}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMinTimeIntervalAsync(Double value);

  /**
   * Get the MinTimeInterval {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MinTimeInterval {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMinTimeIntervalNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMinTimeIntervalNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMinTimeIntervalNodeAsync();

  /**
   * Get the local value of the ExceptionDeviation Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ExceptionDeviation Node.
   * @throws UaException if an error occurs creating or getting the ExceptionDeviation Node.
   */
  Double getExceptionDeviation() throws UaException;

  /**
   * Set the local value of the ExceptionDeviation Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ExceptionDeviation Node.
   * @throws UaException if an error occurs creating or getting the ExceptionDeviation Node.
   */
  void setExceptionDeviation(Double value) throws UaException;

  /**
   * Read the value of the ExceptionDeviation Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readExceptionDeviation() throws UaException;

  /**
   * Write a new value for the ExceptionDeviation Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeExceptionDeviation(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readExceptionDeviation}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readExceptionDeviationAsync();

  /**
   * An asynchronous implementation of {@link #writeExceptionDeviation}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeExceptionDeviationAsync(Double value);

  /**
   * Get the ExceptionDeviation {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ExceptionDeviation {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getExceptionDeviationNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getExceptionDeviationNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getExceptionDeviationNodeAsync();

  /**
   * Get the local value of the ExceptionDeviationFormat Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ExceptionDeviationFormat Node.
   * @throws UaException if an error occurs creating or getting the ExceptionDeviationFormat Node.
   */
  ExceptionDeviationFormat getExceptionDeviationFormat() throws UaException;

  /**
   * Set the local value of the ExceptionDeviationFormat Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ExceptionDeviationFormat Node.
   * @throws UaException if an error occurs creating or getting the ExceptionDeviationFormat Node.
   */
  void setExceptionDeviationFormat(ExceptionDeviationFormat value) throws UaException;

  /**
   * Read the value of the ExceptionDeviationFormat Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link ExceptionDeviationFormat} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ExceptionDeviationFormat readExceptionDeviationFormat() throws UaException;

  /**
   * Write a new value for the ExceptionDeviationFormat Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link ExceptionDeviationFormat} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeExceptionDeviationFormat(ExceptionDeviationFormat value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readExceptionDeviationFormat}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ExceptionDeviationFormat> readExceptionDeviationFormatAsync();

  /**
   * An asynchronous implementation of {@link #writeExceptionDeviationFormat}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeExceptionDeviationFormatAsync(ExceptionDeviationFormat value);

  /**
   * Get the ExceptionDeviationFormat {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ExceptionDeviationFormat {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getExceptionDeviationFormatNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getExceptionDeviationFormatNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getExceptionDeviationFormatNodeAsync();

  /**
   * Get the local value of the StartOfArchive Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the StartOfArchive Node.
   * @throws UaException if an error occurs creating or getting the StartOfArchive Node.
   */
  DateTime getStartOfArchive() throws UaException;

  /**
   * Set the local value of the StartOfArchive Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the StartOfArchive Node.
   * @throws UaException if an error occurs creating or getting the StartOfArchive Node.
   */
  void setStartOfArchive(DateTime value) throws UaException;

  /**
   * Read the value of the StartOfArchive Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readStartOfArchive() throws UaException;

  /**
   * Write a new value for the StartOfArchive Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeStartOfArchive(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readStartOfArchive}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readStartOfArchiveAsync();

  /**
   * An asynchronous implementation of {@link #writeStartOfArchive}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStartOfArchiveAsync(DateTime value);

  /**
   * Get the StartOfArchive {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the StartOfArchive {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getStartOfArchiveNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStartOfArchiveNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getStartOfArchiveNodeAsync();

  /**
   * Get the local value of the StartOfOnlineArchive Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the StartOfOnlineArchive Node.
   * @throws UaException if an error occurs creating or getting the StartOfOnlineArchive Node.
   */
  DateTime getStartOfOnlineArchive() throws UaException;

  /**
   * Set the local value of the StartOfOnlineArchive Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the StartOfOnlineArchive Node.
   * @throws UaException if an error occurs creating or getting the StartOfOnlineArchive Node.
   */
  void setStartOfOnlineArchive(DateTime value) throws UaException;

  /**
   * Read the value of the StartOfOnlineArchive Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readStartOfOnlineArchive() throws UaException;

  /**
   * Write a new value for the StartOfOnlineArchive Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeStartOfOnlineArchive(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readStartOfOnlineArchive}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readStartOfOnlineArchiveAsync();

  /**
   * An asynchronous implementation of {@link #writeStartOfOnlineArchive}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStartOfOnlineArchiveAsync(DateTime value);

  /**
   * Get the StartOfOnlineArchive {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the StartOfOnlineArchive {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getStartOfOnlineArchiveNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStartOfOnlineArchiveNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getStartOfOnlineArchiveNodeAsync();

  /**
   * Get the local value of the ServerTimestampSupported Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ServerTimestampSupported Node.
   * @throws UaException if an error occurs creating or getting the ServerTimestampSupported Node.
   */
  Boolean getServerTimestampSupported() throws UaException;

  /**
   * Set the local value of the ServerTimestampSupported Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ServerTimestampSupported Node.
   * @throws UaException if an error occurs creating or getting the ServerTimestampSupported Node.
   */
  void setServerTimestampSupported(Boolean value) throws UaException;

  /**
   * Read the value of the ServerTimestampSupported Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readServerTimestampSupported() throws UaException;

  /**
   * Write a new value for the ServerTimestampSupported Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeServerTimestampSupported(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readServerTimestampSupported}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readServerTimestampSupportedAsync();

  /**
   * An asynchronous implementation of {@link #writeServerTimestampSupported}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeServerTimestampSupportedAsync(Boolean value);

  /**
   * Get the ServerTimestampSupported {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ServerTimestampSupported {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getServerTimestampSupportedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getServerTimestampSupportedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getServerTimestampSupportedNodeAsync();

  /**
   * Get the local value of the MaxTimeStoredValues Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxTimeStoredValues Node.
   * @throws UaException if an error occurs creating or getting the MaxTimeStoredValues Node.
   */
  Double getMaxTimeStoredValues() throws UaException;

  /**
   * Set the local value of the MaxTimeStoredValues Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxTimeStoredValues Node.
   * @throws UaException if an error occurs creating or getting the MaxTimeStoredValues Node.
   */
  void setMaxTimeStoredValues(Double value) throws UaException;

  /**
   * Read the value of the MaxTimeStoredValues Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readMaxTimeStoredValues() throws UaException;

  /**
   * Write a new value for the MaxTimeStoredValues Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxTimeStoredValues(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxTimeStoredValues}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readMaxTimeStoredValuesAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxTimeStoredValues}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxTimeStoredValuesAsync(Double value);

  /**
   * Get the MaxTimeStoredValues {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxTimeStoredValues {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxTimeStoredValuesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxTimeStoredValuesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxTimeStoredValuesNodeAsync();

  /**
   * Get the local value of the MaxCountStoredValues Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxCountStoredValues Node.
   * @throws UaException if an error occurs creating or getting the MaxCountStoredValues Node.
   */
  UInteger getMaxCountStoredValues() throws UaException;

  /**
   * Set the local value of the MaxCountStoredValues Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxCountStoredValues Node.
   * @throws UaException if an error occurs creating or getting the MaxCountStoredValues Node.
   */
  void setMaxCountStoredValues(UInteger value) throws UaException;

  /**
   * Read the value of the MaxCountStoredValues Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxCountStoredValues() throws UaException;

  /**
   * Write a new value for the MaxCountStoredValues Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxCountStoredValues(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxCountStoredValues}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxCountStoredValuesAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxCountStoredValues}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxCountStoredValuesAsync(UInteger value);

  /**
   * Get the MaxCountStoredValues {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxCountStoredValues {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxCountStoredValuesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxCountStoredValuesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxCountStoredValuesNodeAsync();

  /**
   * Get the AggregateConfiguration {@link AggregateConfigurationType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AggregateConfiguration {@link AggregateConfigurationType} Node, or {@code null} if
   *     it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  AggregateConfigurationType getAggregateConfigurationNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAggregateConfigurationNode()}.
   *
   * @return a CompletableFuture that completes successfully with the AggregateConfigurationType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends AggregateConfigurationType> getAggregateConfigurationNodeAsync();

  /**
   * Get the AggregateFunctions {@link FolderType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AggregateFunctions {@link FolderType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  FolderType getAggregateFunctionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAggregateFunctionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the FolderType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends FolderType> getAggregateFunctionsNodeAsync();
}
