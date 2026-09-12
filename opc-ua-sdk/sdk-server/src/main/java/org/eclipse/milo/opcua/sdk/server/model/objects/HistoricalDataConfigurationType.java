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
  @Nullable Boolean getStepped();

  /** Sets the existing node's local value. */
  void setStepped(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSteppedNode();

  /** Gets the existing node's local value. */
  @Nullable String getDefinition();

  /** Sets the existing node's local value. */
  void setDefinition(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDefinitionNode();

  /** Gets the existing node's local value. */
  @Nullable Double getMaxTimeInterval();

  /** Sets the existing node's local value. */
  void setMaxTimeInterval(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxTimeIntervalNode();

  /** Gets the existing node's local value. */
  @Nullable Double getMinTimeInterval();

  /** Sets the existing node's local value. */
  void setMinTimeInterval(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMinTimeIntervalNode();

  /** Gets the existing node's local value. */
  @Nullable Double getExceptionDeviation();

  /** Sets the existing node's local value. */
  void setExceptionDeviation(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getExceptionDeviationNode();

  /** Gets the existing node's local value. */
  @Nullable ExceptionDeviationFormat getExceptionDeviationFormat();

  /** Sets the existing node's local value. */
  void setExceptionDeviationFormat(@Nullable ExceptionDeviationFormat value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getExceptionDeviationFormatNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getStartOfArchive();

  /** Sets the existing node's local value. */
  void setStartOfArchive(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getStartOfArchiveNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getStartOfOnlineArchive();

  /** Sets the existing node's local value. */
  void setStartOfOnlineArchive(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getStartOfOnlineArchiveNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getServerTimestampSupported();

  /** Sets the existing node's local value. */
  void setServerTimestampSupported(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getServerTimestampSupportedNode();

  /** Gets the existing node's local value. */
  @Nullable Double getMaxTimeStoredValues();

  /** Sets the existing node's local value. */
  void setMaxTimeStoredValues(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxTimeStoredValuesNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxCountStoredValues();

  /** Sets the existing node's local value. */
  void setMaxCountStoredValues(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxCountStoredValuesNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  AggregateConfigurationType getAggregateConfigurationNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable FolderType getAggregateFunctionsNode();
}
