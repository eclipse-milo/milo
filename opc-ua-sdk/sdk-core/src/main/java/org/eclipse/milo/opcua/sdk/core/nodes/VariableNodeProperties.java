/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.nodes;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

public final class VariableNodeProperties {

  private VariableNodeProperties() {}

  /**
   * The NodeVersion Property is used to indicate the version of a Node. It does not apply to
   * Properties.
   *
   * <p>The NodeVersion Property is updated each time a Reference is added or removed from the Node
   * the Property belongs to.
   *
   * <p>Although the relationship of a Variable to its DataType is not modelled using References,
   * changes to the DataType Attribute of a Variable lead to an update of the NodeVersion Property.
   *
   * <p>The usage of this Property is optional.
   */
  public static final QualifiedProperty<String> NodeVersion =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NodeVersion",
          NodeIds.String.expanded(),
          ValueRanks.Scalar,
          String.class);

  /**
   * The LocalTime Property is only used for DataVariables. It does not apply to Properties.
   *
   * <p>This Property is a structure containing the Offset and the DaylightSavingInOffset flag. The
   * Offset specifies the time difference (in minutes) between the SourceTimestamp (UTC) associated
   * with the value and the time at the location in which the value was obtained. If
   * DaylightSavingInOffset is TRUE, then Standard/Daylight savings time (DST) at the originating
   * location is in effect and Offset includes the DST correction. If FALSE then the Offset does not
   * include DST correction and DST may or may not have been in effect.
   *
   * <p>The usage of this Property is optional.
   */
  public static final QualifiedProperty<TimeZoneDataType> LocalTime =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LocalTime",
          NodeIds.TimeZoneDataType.expanded(),
          ValueRanks.Scalar,
          TimeZoneDataType.class);

  /**
   * Only used for Variables of the VariableType DataTypeDictionaryType and DataTypeDescriptionType
   * as described in OPC UA Part 5, section 5.18.
   *
   * <p>The usage of this Property is optional.
   */
  public static final QualifiedProperty<String> DataTypeVersion =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataTypeVersion",
          NodeIds.String.expanded(),
          ValueRanks.Scalar,
          String.class);

  /**
   * Only used for Variables of the VariableType DataTypeDescriptionType as described in OPC UA Part
   * 5, section 5.18.
   *
   * <p>The usage of this Property is optional.
   */
  public static final QualifiedProperty<ByteString> DictionaryFragment =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DictionaryFragment",
          NodeIds.ByteString.expanded(),
          ValueRanks.Scalar,
          ByteString.class);

  /**
   * The AllowNulls Property is only used for DataVariables. It does not apply to Properties.
   *
   * <p>This Property specifies if a null value is allowed for the Value Attribute of the
   * DataVariable. If it is set to true, the Server may return null values and accept writing of
   * null values. If it is set to false, the Server shall never return a null value and shall reject
   * any request writing a null value.
   *
   * <p>If this Property is not provided, it is Server-specific if null values are allowed or not.
   *
   * <p>The usage of this Property is optional.
   */
  public static final QualifiedProperty<Boolean> AllowNulls =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "AllowNulls",
          NodeIds.Boolean.expanded(),
          ValueRanks.Scalar,
          Boolean.class);

  /**
   * Only used for DataVariables having an Enumeration DataType.
   *
   * <p>This optional Property provides the localized text representation of the enumeration value.
   * It can be used by Clients only interested in displaying the text to subscribe to the Property
   * instead of the value attribute.
   *
   * <p>The usage of this Property is optional.
   */
  public static final QualifiedProperty<LocalizedText> ValueAsText =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ValueAsText",
          NodeIds.LocalizedText.expanded(),
          ValueRanks.Scalar,
          LocalizedText.class);

  /**
   * Only used for DataVariables having a String DataType.
   *
   * <p>This optional Property indicates the maximum number of characters supported by the
   * DataVariable.
   *
   * <p>The usage of this Property is optional.
   */
  public static final QualifiedProperty<UInteger> MaxStringLength =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxStringLength",
          NodeIds.UInt32.expanded(),
          ValueRanks.Scalar,
          UInteger.class);

  /**
   * Only used for DataVariables having its ValueRank Attribute not set to scalar.
   *
   * <p>This optional Property indicates the maximum length of an array supported by the
   * DataVariable. In a multidimensional array it indicates the overall length.
   *
   * <p>NOTE: In order to expose the length of an array of bytes do not use the DataType ByteString
   * but an array of the DataType Byte. In that case the MaxArrayLength applies.
   *
   * <p>The usage of this Property is optional.
   */
  public static final QualifiedProperty<UInteger> MaxArrayLength =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxArrayLength",
          NodeIds.UInt32.expanded(),
          ValueRanks.Scalar,
          UInteger.class);

  /**
   * Only used for DataVariables having a Number DataType.
   *
   * <p>This optional Property indicates the engineering units for the value of the DataVariable
   * (e.g. hertz or seconds). Details about the Property and what engineering units should be used
   * are defined in Part 8. The DataType EUInformation is also defined in Part 8.
   *
   * <p>The usage of this Property is optional.
   */
  public static final QualifiedProperty<EUInformation> EngineeringUnits =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EngineeringUnits",
          NodeIds.EUInformation.expanded(),
          ValueRanks.Scalar,
          EUInformation.class);
}
