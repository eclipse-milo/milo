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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.18">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.18</a>
 */
public interface LimitAlarmType extends AlarmConditionType {
  QualifiedProperty<Double> HIGH_HIGH_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HighHighLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> HIGH_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HighLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> LOW_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LowLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> LOW_LOW_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LowLowLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> BASE_HIGH_HIGH_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "BaseHighHighLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> BASE_HIGH_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "BaseHighLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> BASE_LOW_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "BaseLowLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> BASE_LOW_LOW_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "BaseLowLowLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<UShort> SEVERITY_HIGH_HIGH =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SeverityHighHigh",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> SEVERITY_HIGH =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SeverityHigh",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> SEVERITY_LOW =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SeverityLow",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> SEVERITY_LOW_LOW =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SeverityLowLow",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<Double> HIGH_HIGH_DEADBAND =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HighHighDeadband",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> HIGH_DEADBAND =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HighDeadband",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> LOW_DEADBAND =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LowDeadband",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> LOW_LOW_DEADBAND =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LowLowDeadband",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  Double getHighHighLimit();

  void setHighHighLimit(Double value);

  PropertyType getHighHighLimitNode();

  Double getHighLimit();

  void setHighLimit(Double value);

  PropertyType getHighLimitNode();

  Double getLowLimit();

  void setLowLimit(Double value);

  PropertyType getLowLimitNode();

  Double getLowLowLimit();

  void setLowLowLimit(Double value);

  PropertyType getLowLowLimitNode();

  Double getBaseHighHighLimit();

  void setBaseHighHighLimit(Double value);

  PropertyType getBaseHighHighLimitNode();

  Double getBaseHighLimit();

  void setBaseHighLimit(Double value);

  PropertyType getBaseHighLimitNode();

  Double getBaseLowLimit();

  void setBaseLowLimit(Double value);

  PropertyType getBaseLowLimitNode();

  Double getBaseLowLowLimit();

  void setBaseLowLowLimit(Double value);

  PropertyType getBaseLowLowLimitNode();

  UShort getSeverityHighHigh();

  void setSeverityHighHigh(UShort value);

  PropertyType getSeverityHighHighNode();

  UShort getSeverityHigh();

  void setSeverityHigh(UShort value);

  PropertyType getSeverityHighNode();

  UShort getSeverityLow();

  void setSeverityLow(UShort value);

  PropertyType getSeverityLowNode();

  UShort getSeverityLowLow();

  void setSeverityLowLow(UShort value);

  PropertyType getSeverityLowLowNode();

  Double getHighHighDeadband();

  void setHighHighDeadband(Double value);

  PropertyType getHighHighDeadbandNode();

  Double getHighDeadband();

  void setHighDeadband(Double value);

  PropertyType getHighDeadbandNode();

  Double getLowDeadband();

  void setLowDeadband(Double value);

  PropertyType getLowDeadbandNode();

  Double getLowLowDeadband();

  void setLowLowDeadband(Double value);

  PropertyType getLowLowDeadbandNode();
}
