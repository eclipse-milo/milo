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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ArrayItemType extends DataItemType {
  QualifiedProperty<Range> INSTRUMENT_RANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InstrumentRange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=884"),
          -1,
          Range.class);

  QualifiedProperty<Range> EU_RANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EURange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=884"),
          -1,
          Range.class);

  QualifiedProperty<EUInformation> ENGINEERING_UNITS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EngineeringUnits",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=887"),
          -1,
          EUInformation.class);

  QualifiedProperty<LocalizedText> TITLE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Title",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<AxisScaleEnumeration> AXIS_SCALE_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AxisScaleType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12077"),
          -1,
          AxisScaleEnumeration.class);

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
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEuRangeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getEuRangeNodeAsync();

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
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEngineeringUnitsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getEngineeringUnitsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getTitle() throws UaException;

  /** Sets the existing node's local value. */
  void setTitle(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readTitle() throws UaException;

  /** Writes the value remotely. */
  void writeTitle(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readTitleAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTitleAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getTitleNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getTitleNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable AxisScaleEnumeration getAxisScaleType() throws UaException;

  /** Sets the existing node's local value. */
  void setAxisScaleType(@Nullable AxisScaleEnumeration value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable AxisScaleEnumeration readAxisScaleType() throws UaException;

  /** Writes the value remotely. */
  void writeAxisScaleType(@Nullable AxisScaleEnumeration value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable AxisScaleEnumeration> readAxisScaleTypeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAxisScaleTypeAsync(@Nullable AxisScaleEnumeration value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAxisScaleTypeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getAxisScaleTypeNodeAsync();
}
