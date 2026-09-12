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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
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
  @Nullable Double getHighHighLimit() throws UaException;

  /** Sets the existing node's local value. */
  void setHighHighLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readHighHighLimit() throws UaException;

  /** Writes the value remotely. */
  void writeHighHighLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readHighHighLimitAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeHighHighLimitAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getHighHighLimitNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getHighHighLimitNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getHighLimit() throws UaException;

  /** Sets the existing node's local value. */
  void setHighLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readHighLimit() throws UaException;

  /** Writes the value remotely. */
  void writeHighLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readHighLimitAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeHighLimitAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getHighLimitNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getHighLimitNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getLowLimit() throws UaException;

  /** Sets the existing node's local value. */
  void setLowLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readLowLimit() throws UaException;

  /** Writes the value remotely. */
  void writeLowLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readLowLimitAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLowLimitAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLowLimitNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getLowLimitNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getLowLowLimit() throws UaException;

  /** Sets the existing node's local value. */
  void setLowLowLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readLowLowLimit() throws UaException;

  /** Writes the value remotely. */
  void writeLowLowLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readLowLowLimitAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLowLowLimitAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLowLowLimitNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getLowLowLimitNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getBaseHighHighLimit() throws UaException;

  /** Sets the existing node's local value. */
  void setBaseHighHighLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readBaseHighHighLimit() throws UaException;

  /** Writes the value remotely. */
  void writeBaseHighHighLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readBaseHighHighLimitAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeBaseHighHighLimitAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getBaseHighHighLimitNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getBaseHighHighLimitNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getBaseHighLimit() throws UaException;

  /** Sets the existing node's local value. */
  void setBaseHighLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readBaseHighLimit() throws UaException;

  /** Writes the value remotely. */
  void writeBaseHighLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readBaseHighLimitAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeBaseHighLimitAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getBaseHighLimitNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getBaseHighLimitNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getBaseLowLimit() throws UaException;

  /** Sets the existing node's local value. */
  void setBaseLowLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readBaseLowLimit() throws UaException;

  /** Writes the value remotely. */
  void writeBaseLowLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readBaseLowLimitAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeBaseLowLimitAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getBaseLowLimitNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getBaseLowLimitNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getBaseLowLowLimit() throws UaException;

  /** Sets the existing node's local value. */
  void setBaseLowLowLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readBaseLowLowLimit() throws UaException;

  /** Writes the value remotely. */
  void writeBaseLowLowLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readBaseLowLowLimitAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeBaseLowLowLimitAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getBaseLowLowLimitNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getBaseLowLowLimitNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getSeverityHighHigh() throws UaException;

  /** Sets the existing node's local value. */
  void setSeverityHighHigh(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readSeverityHighHigh() throws UaException;

  /** Writes the value remotely. */
  void writeSeverityHighHigh(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readSeverityHighHighAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSeverityHighHighAsync(@Nullable UShort value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSeverityHighHighNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSeverityHighHighNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getSeverityHigh() throws UaException;

  /** Sets the existing node's local value. */
  void setSeverityHigh(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readSeverityHigh() throws UaException;

  /** Writes the value remotely. */
  void writeSeverityHigh(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readSeverityHighAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSeverityHighAsync(@Nullable UShort value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSeverityHighNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSeverityHighNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getSeverityLow() throws UaException;

  /** Sets the existing node's local value. */
  void setSeverityLow(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readSeverityLow() throws UaException;

  /** Writes the value remotely. */
  void writeSeverityLow(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readSeverityLowAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSeverityLowAsync(@Nullable UShort value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSeverityLowNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSeverityLowNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getSeverityLowLow() throws UaException;

  /** Sets the existing node's local value. */
  void setSeverityLowLow(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readSeverityLowLow() throws UaException;

  /** Writes the value remotely. */
  void writeSeverityLowLow(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readSeverityLowLowAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSeverityLowLowAsync(@Nullable UShort value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSeverityLowLowNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSeverityLowLowNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getHighHighDeadband() throws UaException;

  /** Sets the existing node's local value. */
  void setHighHighDeadband(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readHighHighDeadband() throws UaException;

  /** Writes the value remotely. */
  void writeHighHighDeadband(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readHighHighDeadbandAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeHighHighDeadbandAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getHighHighDeadbandNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getHighHighDeadbandNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getHighDeadband() throws UaException;

  /** Sets the existing node's local value. */
  void setHighDeadband(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readHighDeadband() throws UaException;

  /** Writes the value remotely. */
  void writeHighDeadband(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readHighDeadbandAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeHighDeadbandAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getHighDeadbandNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getHighDeadbandNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getLowDeadband() throws UaException;

  /** Sets the existing node's local value. */
  void setLowDeadband(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readLowDeadband() throws UaException;

  /** Writes the value remotely. */
  void writeLowDeadband(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readLowDeadbandAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLowDeadbandAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLowDeadbandNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getLowDeadbandNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getLowLowDeadband() throws UaException;

  /** Sets the existing node's local value. */
  void setLowLowDeadband(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readLowLowDeadband() throws UaException;

  /** Writes the value remotely. */
  void writeLowLowDeadband(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readLowLowDeadbandAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLowLowDeadbandAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLowLowDeadbandNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getLowLowDeadbandNodeAsync();
}
