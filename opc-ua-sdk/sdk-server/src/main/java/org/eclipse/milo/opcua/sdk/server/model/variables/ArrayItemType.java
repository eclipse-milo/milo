/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
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
  @Nullable Range getInstrumentRange();

  /** Sets the existing node's local value. */
  void setInstrumentRange(@Nullable Range value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getInstrumentRangeNode();

  /** Gets the existing node's local value. */
  @Nullable Range getEuRange();

  /** Sets the existing node's local value. */
  void setEuRange(@Nullable Range value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEuRangeNode();

  /** Gets the existing node's local value. */
  @Nullable EUInformation getEngineeringUnitsProperty();

  /** Sets the existing node's local value. */
  void setEngineeringUnitsProperty(@Nullable EUInformation value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEngineeringUnitsNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getTitle();

  /** Sets the existing node's local value. */
  void setTitle(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getTitleNode();

  /** Gets the existing node's local value. */
  @Nullable AxisScaleEnumeration getAxisScaleType();

  /** Sets the existing node's local value. */
  void setAxisScaleType(@Nullable AxisScaleEnumeration value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAxisScaleTypeNode();
}
