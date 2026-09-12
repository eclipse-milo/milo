/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.2.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable Boolean getStepped() throws UaException;

  /** Sets the existing node's local value. */
  void setStepped(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readStepped() throws UaException;

  /** Writes the value remotely. */
  void writeStepped(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readSteppedAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSteppedAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSteppedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSteppedNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getDefinition() throws UaException;

  /** Sets the existing node's local value. */
  void setDefinition(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readDefinition() throws UaException;

  /** Writes the value remotely. */
  void writeDefinition(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readDefinitionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDefinitionAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDefinitionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getDefinitionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getMaxTimeInterval() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxTimeInterval(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readMaxTimeInterval() throws UaException;

  /** Writes the value remotely. */
  void writeMaxTimeInterval(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readMaxTimeIntervalAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxTimeIntervalAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxTimeIntervalNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxTimeIntervalNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getMinTimeInterval() throws UaException;

  /** Sets the existing node's local value. */
  void setMinTimeInterval(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readMinTimeInterval() throws UaException;

  /** Writes the value remotely. */
  void writeMinTimeInterval(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readMinTimeIntervalAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMinTimeIntervalAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMinTimeIntervalNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMinTimeIntervalNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getExceptionDeviation() throws UaException;

  /** Sets the existing node's local value. */
  void setExceptionDeviation(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readExceptionDeviation() throws UaException;

  /** Writes the value remotely. */
  void writeExceptionDeviation(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readExceptionDeviationAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeExceptionDeviationAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getExceptionDeviationNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getExceptionDeviationNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ExceptionDeviationFormat getExceptionDeviationFormat() throws UaException;

  /** Sets the existing node's local value. */
  void setExceptionDeviationFormat(@Nullable ExceptionDeviationFormat value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ExceptionDeviationFormat readExceptionDeviationFormat() throws UaException;

  /** Writes the value remotely. */
  void writeExceptionDeviationFormat(@Nullable ExceptionDeviationFormat value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ExceptionDeviationFormat>
      readExceptionDeviationFormatAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeExceptionDeviationFormatAsync(
      @Nullable ExceptionDeviationFormat value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getExceptionDeviationFormatNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getExceptionDeviationFormatNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getStartOfArchive() throws UaException;

  /** Sets the existing node's local value. */
  void setStartOfArchive(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readStartOfArchive() throws UaException;

  /** Writes the value remotely. */
  void writeStartOfArchive(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readStartOfArchiveAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStartOfArchiveAsync(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getStartOfArchiveNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getStartOfArchiveNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getStartOfOnlineArchive() throws UaException;

  /** Sets the existing node's local value. */
  void setStartOfOnlineArchive(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readStartOfOnlineArchive() throws UaException;

  /** Writes the value remotely. */
  void writeStartOfOnlineArchive(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readStartOfOnlineArchiveAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStartOfOnlineArchiveAsync(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getStartOfOnlineArchiveNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getStartOfOnlineArchiveNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getServerTimestampSupported() throws UaException;

  /** Sets the existing node's local value. */
  void setServerTimestampSupported(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readServerTimestampSupported() throws UaException;

  /** Writes the value remotely. */
  void writeServerTimestampSupported(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readServerTimestampSupportedAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServerTimestampSupportedAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getServerTimestampSupportedNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getServerTimestampSupportedNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getMaxTimeStoredValues() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxTimeStoredValues(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readMaxTimeStoredValues() throws UaException;

  /** Writes the value remotely. */
  void writeMaxTimeStoredValues(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readMaxTimeStoredValuesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxTimeStoredValuesAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxTimeStoredValuesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxTimeStoredValuesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxCountStoredValues() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxCountStoredValues(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxCountStoredValues() throws UaException;

  /** Writes the value remotely. */
  void writeMaxCountStoredValues(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxCountStoredValuesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxCountStoredValuesAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxCountStoredValuesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxCountStoredValuesNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  AggregateConfigurationType getAggregateConfigurationNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends AggregateConfigurationType> getAggregateConfigurationNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable FolderType getAggregateFunctionsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable FolderType> getAggregateFunctionsNodeAsync();
}
