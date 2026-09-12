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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.18">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.18</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable Double getHighHighLimit();

  /** Sets the existing node's local value. */
  void setHighHighLimit(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getHighHighLimitNode();

  /** Gets the existing node's local value. */
  @Nullable Double getHighLimit();

  /** Sets the existing node's local value. */
  void setHighLimit(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getHighLimitNode();

  /** Gets the existing node's local value. */
  @Nullable Double getLowLimit();

  /** Sets the existing node's local value. */
  void setLowLimit(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLowLimitNode();

  /** Gets the existing node's local value. */
  @Nullable Double getLowLowLimit();

  /** Sets the existing node's local value. */
  void setLowLowLimit(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLowLowLimitNode();

  /** Gets the existing node's local value. */
  @Nullable Double getBaseHighHighLimit();

  /** Sets the existing node's local value. */
  void setBaseHighHighLimit(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getBaseHighHighLimitNode();

  /** Gets the existing node's local value. */
  @Nullable Double getBaseHighLimit();

  /** Sets the existing node's local value. */
  void setBaseHighLimit(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getBaseHighLimitNode();

  /** Gets the existing node's local value. */
  @Nullable Double getBaseLowLimit();

  /** Sets the existing node's local value. */
  void setBaseLowLimit(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getBaseLowLimitNode();

  /** Gets the existing node's local value. */
  @Nullable Double getBaseLowLowLimit();

  /** Sets the existing node's local value. */
  void setBaseLowLowLimit(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getBaseLowLowLimitNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getSeverityHighHigh();

  /** Sets the existing node's local value. */
  void setSeverityHighHigh(@Nullable UShort value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSeverityHighHighNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getSeverityHigh();

  /** Sets the existing node's local value. */
  void setSeverityHigh(@Nullable UShort value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSeverityHighNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getSeverityLow();

  /** Sets the existing node's local value. */
  void setSeverityLow(@Nullable UShort value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSeverityLowNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getSeverityLowLow();

  /** Sets the existing node's local value. */
  void setSeverityLowLow(@Nullable UShort value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSeverityLowLowNode();

  /** Gets the existing node's local value. */
  @Nullable Double getHighHighDeadband();

  /** Sets the existing node's local value. */
  void setHighHighDeadband(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getHighHighDeadbandNode();

  /** Gets the existing node's local value. */
  @Nullable Double getHighDeadband();

  /** Sets the existing node's local value. */
  void setHighDeadband(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getHighDeadbandNode();

  /** Gets the existing node's local value. */
  @Nullable Double getLowDeadband();

  /** Sets the existing node's local value. */
  void setLowDeadband(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLowDeadbandNode();

  /** Gets the existing node's local value. */
  @Nullable Double getLowLowDeadband();

  /** Sets the existing node's local value. */
  void setLowLowDeadband(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLowLowDeadbandNode();
}
