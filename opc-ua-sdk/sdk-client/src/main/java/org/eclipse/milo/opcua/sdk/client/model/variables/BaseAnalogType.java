/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.NumberRange;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface BaseAnalogType extends DataItemType {
  QualifiedProperty<Range> INSTRUMENT_RANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InstrumentRange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=884"),
          -1,
          Range.class);

  QualifiedProperty<NumberRange> INSTRUMENT_NUMBER_RANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InstrumentNumberRange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23903"),
          -1,
          NumberRange.class);

  QualifiedProperty<Range> EU_RANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EURange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=884"),
          -1,
          Range.class);

  QualifiedProperty<NumberRange> EU_NUMBER_RANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EUNumberRange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23903"),
          -1,
          NumberRange.class);

  QualifiedProperty<EUInformation> ENGINEERING_UNITS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EngineeringUnits",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=887"),
          -1,
          EUInformation.class);

  /** Gets the existing node's local value. */
  @Nullable Range getInstrumentRange() throws UaException;

  /** Sets the existing node's local value. */
  void setInstrumentRange(@Nullable Range value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Range readInstrumentRange() throws UaException;

  /** Writes the value remotely. */
  void writeInstrumentRange(@Nullable Range value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Range> readInstrumentRangeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeInstrumentRangeAsync(@Nullable Range value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getInstrumentRangeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getInstrumentRangeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NumberRange getInstrumentNumberRange() throws UaException;

  /** Sets the existing node's local value. */
  void setInstrumentNumberRange(@Nullable NumberRange value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NumberRange readInstrumentNumberRange() throws UaException;

  /** Writes the value remotely. */
  void writeInstrumentNumberRange(@Nullable NumberRange value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NumberRange> readInstrumentNumberRangeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeInstrumentNumberRangeAsync(@Nullable NumberRange value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getInstrumentNumberRangeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getInstrumentNumberRangeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Range getEuRange() throws UaException;

  /** Sets the existing node's local value. */
  void setEuRange(@Nullable Range value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Range readEuRange() throws UaException;

  /** Writes the value remotely. */
  void writeEuRange(@Nullable Range value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Range> readEuRangeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEuRangeAsync(@Nullable Range value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEuRangeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getEuRangeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NumberRange getEuNumberRange() throws UaException;

  /** Sets the existing node's local value. */
  void setEuNumberRange(@Nullable NumberRange value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NumberRange readEuNumberRange() throws UaException;

  /** Writes the value remotely. */
  void writeEuNumberRange(@Nullable NumberRange value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NumberRange> readEuNumberRangeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEuNumberRangeAsync(@Nullable NumberRange value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEuNumberRangeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getEuNumberRangeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable EUInformation getEngineeringUnitsProperty() throws UaException;

  /** Sets the existing node's local value. */
  void setEngineeringUnitsProperty(@Nullable EUInformation value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable EUInformation readEngineeringUnitsProperty() throws UaException;

  /** Writes the value remotely. */
  void writeEngineeringUnitsProperty(@Nullable EUInformation value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable EUInformation> readEngineeringUnitsPropertyAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEngineeringUnitsPropertyAsync(@Nullable EUInformation value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEngineeringUnitsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getEngineeringUnitsNodeAsync();
}
