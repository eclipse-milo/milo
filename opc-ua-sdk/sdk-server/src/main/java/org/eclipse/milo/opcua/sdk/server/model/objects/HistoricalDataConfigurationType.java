/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ExceptionDeviationFormat;
import org.jspecify.annotations.Nullable;

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
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable Boolean getStepped();

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  void setStepped(@Nullable Boolean value);

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  PropertyType getSteppedNode();

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable String getDefinition();

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  void setDefinition(@Nullable String value);

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable PropertyType getDefinitionNode();

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable Double getMaxTimeInterval();

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  void setMaxTimeInterval(@Nullable Double value);

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable PropertyType getMaxTimeIntervalNode();

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable Double getMinTimeInterval();

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  void setMinTimeInterval(@Nullable Double value);

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable PropertyType getMinTimeIntervalNode();

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable Double getExceptionDeviation();

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  void setExceptionDeviation(@Nullable Double value);

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable PropertyType getExceptionDeviationNode();

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable ExceptionDeviationFormat getExceptionDeviationFormat();

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  void setExceptionDeviationFormat(@Nullable ExceptionDeviationFormat value);

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable PropertyType getExceptionDeviationFormatNode();

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable DateTime getStartOfArchive();

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  void setStartOfArchive(@Nullable DateTime value);

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable PropertyType getStartOfArchiveNode();

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable DateTime getStartOfOnlineArchive();

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  void setStartOfOnlineArchive(@Nullable DateTime value);

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable PropertyType getStartOfOnlineArchiveNode();

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable Boolean getServerTimestampSupported();

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  void setServerTimestampSupported(@Nullable Boolean value);

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable PropertyType getServerTimestampSupportedNode();

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable Double getMaxTimeStoredValues();

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  void setMaxTimeStoredValues(@Nullable Double value);

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable PropertyType getMaxTimeStoredValuesNode();

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable UInteger getMaxCountStoredValues();

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  void setMaxCountStoredValues(@Nullable UInteger value);

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable PropertyType getMaxCountStoredValuesNode();

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  AggregateConfigurationType getAggregateConfigurationNode();

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable FolderType getAggregateFunctionsNode();
}
