/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

  Boolean getStepped();

  void setStepped(Boolean value);

  PropertyType getSteppedNode();

  String getDefinition();

  void setDefinition(String value);

  PropertyType getDefinitionNode();

  Double getMaxTimeInterval();

  void setMaxTimeInterval(Double value);

  PropertyType getMaxTimeIntervalNode();

  Double getMinTimeInterval();

  void setMinTimeInterval(Double value);

  PropertyType getMinTimeIntervalNode();

  Double getExceptionDeviation();

  void setExceptionDeviation(Double value);

  PropertyType getExceptionDeviationNode();

  ExceptionDeviationFormat getExceptionDeviationFormat();

  void setExceptionDeviationFormat(ExceptionDeviationFormat value);

  PropertyType getExceptionDeviationFormatNode();

  DateTime getStartOfArchive();

  void setStartOfArchive(DateTime value);

  PropertyType getStartOfArchiveNode();

  DateTime getStartOfOnlineArchive();

  void setStartOfOnlineArchive(DateTime value);

  PropertyType getStartOfOnlineArchiveNode();

  Boolean getServerTimestampSupported();

  void setServerTimestampSupported(Boolean value);

  PropertyType getServerTimestampSupportedNode();

  Double getMaxTimeStoredValues();

  void setMaxTimeStoredValues(Double value);

  PropertyType getMaxTimeStoredValuesNode();

  UInteger getMaxCountStoredValues();

  void setMaxCountStoredValues(UInteger value);

  PropertyType getMaxCountStoredValuesNode();

  AggregateConfigurationType getAggregateConfigurationNode();

  FolderType getAggregateFunctionsNode();
}
