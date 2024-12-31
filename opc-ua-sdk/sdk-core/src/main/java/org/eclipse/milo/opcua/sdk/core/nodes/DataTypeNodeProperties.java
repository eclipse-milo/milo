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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumValueType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

public final class DataTypeNodeProperties {

  private DataTypeNodeProperties() {}

  /**
   * The NodeVersion Property is used to indicate the version of a Node.
   *
   * <p>The NodeVersion Property is updated each time a Reference is added or removed from the Node
   * the Property belongs to.
   */
  public static final QualifiedProperty<String> NodeVersion =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NodeVersion",
          NodeIds.String.expanded(),
          ValueRanks.Scalar,
          String.class);

  /**
   * The EnumStrings Property only applies for Enumeration DataTypes. It shall not be applied for
   * other DataTypes.
   *
   * <p>If the EnumValues Property is provided, the EnumStrings Property shall not be provided.
   *
   * <p>Each entry of the array of LocalizedText in this Property represents the human-readable
   * representation of an enumerated value. The Integer representation of the enumeration value
   * points to a position of the array.
   */
  public static final QualifiedProperty<LocalizedText[]> EnumStrings =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EnumStrings",
          NodeIds.LocalizedText.expanded(),
          ValueRanks.OneDimension,
          LocalizedText[].class);

  /**
   * The EnumValues Property only applies for Enumeration DataTypes. It shall not be applied for
   * other DataTypes.
   *
   * <p>If the EnumStrings Property is provided, the EnumValues Property shall not be provided.
   *
   * <p>Using the EnumValues Property it is possible to represent Enumerations with integers that
   * are not zero-based or have gaps (e.g. 1, 2, 4, 8, 16). Each entry of the array of EnumValueType
   * in this Property represents one enumeration value with its integer notation, human-readable
   * representation, and help information.
   */
  public static final QualifiedProperty<EnumValueType[]> EnumValues =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EnumValues",
          NodeIds.EnumValueType.expanded(),
          ValueRanks.OneDimension,
          EnumValueType[].class);

  /**
   * The OptionSetValues Property only applies for OptionSet DataTypes and UInteger DataTypes.
   *
   * <p>An OptionSet DataType is used to represent a bit mask and the OptionSetValues Property
   * contains the human-readable representation for each bit of the bit mask. The OptionSetValues
   * Property provides an array of LocalizedText containing the human-readable representation for
   * each bit.
   */
  public static final QualifiedProperty<LocalizedText[]> OptionSetValues =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "OptionSetValues",
          NodeIds.LocalizedText.expanded(),
          ValueRanks.OneDimension,
          LocalizedText[].class);
}
