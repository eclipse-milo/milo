/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.enumerated;

import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.1/#5.3.1.9">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.1/#5.3.1.9</a>
 */
public enum ChassisIdSubtype implements UaEnumeratedType {
  /**
   * Represents a chassis identifier based on the value of entPhysicalAlias object (defined in IETF
   * RFC 2737) for a chassis component (i.e., an entPhysicalClass value of chassis(3))
   */
  ChassisComponent(1),

  /**
   * Represents a chassis identifier based on the value of ifAlias object (defined in IETF RFC 2863)
   * for an interface on the containing chassis.
   */
  InterfaceAlias(2),

  /**
   * Represents a chassis identifier based on the value of entPhysicalAlias object (defined in IETF
   * RFC 2737) for a port or backplane component (i.e., entPhysicalClass has a value of port(10), or
   * backplane(4)), within the containing chassis.
   */
  PortComponent(3),

  /**
   * Represents a chassis identifier based on the value of a unicast source address (encoded in
   * network byte order and IEEE 802.3 canonical bit order) of a port on the containing chassis as
   * defined in IEEE Std 802-2014.
   */
  MacAddress(4),

  /**
   * Represents a chassis identifier based on a network address associated with a particular
   * chassis. The encoded address is actually composed of two fields. The first field is a single
   * octet, representing the IANA AddressFamilyNumbers value for the specific address type, and the
   * second field is the network address value.
   */
  NetworkAddress(5),

  /**
   * Represents a chassis identifier based on the value of ifName object (defined in IETF RFC 2863)
   * for an interface on the containing chassis.
   */
  InterfaceName(6),

  /** Represents a chassis identifier based on a locally defined value. */
  Local(7);

  private final int value;

  ChassisIdSubtype(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return TypeInfo.TYPE_ID;
  }

  public static @Nullable ChassisIdSubtype from(int value) {
    return switch (value) {
      case 1 -> ChassisComponent;
      case 2 -> InterfaceAlias;
      case 3 -> PortComponent;
      case 4 -> MacAddress;
      case 5 -> NetworkAddress;
      case 6 -> InterfaceName;
      case 7 -> Local;
      default -> null;
    };
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(
              1L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Represents a chassis identifier based on the value of entPhysicalAlias object"
                      + " (defined in IETF RFC 2737) for a chassis component (i.e., an"
                      + " entPhysicalClass value of chassis(3))"),
              "ChassisComponent"),
          new EnumField(
              2L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Represents a chassis identifier based on the value of ifAlias object (defined in"
                      + " IETF RFC 2863) for an interface on the containing chassis."),
              "InterfaceAlias"),
          new EnumField(
              3L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Represents a chassis identifier based on the value of entPhysicalAlias object"
                      + " (defined in IETF RFC 2737) for a port or backplane component (i.e.,"
                      + " entPhysicalClass has a value of port(10), or backplane(4)), within the"
                      + " containing chassis."),
              "PortComponent"),
          new EnumField(
              4L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Represents a chassis identifier based on the value of a unicast source address"
                      + " (encoded in network byte order and IEEE 802.3 canonical bit order) of a"
                      + " port on the containing chassis as defined in IEEE Std 802-2014."),
              "MacAddress"),
          new EnumField(
              5L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Represents a chassis identifier based on a network address associated with a"
                      + " particular chassis. The encoded address is actually composed of two"
                      + " fields. The first field is a single octet, representing the IANA"
                      + " AddressFamilyNumbers value for the specific address type, and the second"
                      + " field is the network address value."),
              "NetworkAddress"),
          new EnumField(
              6L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Represents a chassis identifier based on the value of ifName object (defined in"
                      + " IETF RFC 2863) for an interface on the containing chassis."),
              "InterfaceName"),
          new EnumField(
              7L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "", "Represents a chassis identifier based on a locally defined value."),
              "Local")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=18947");
  }
}
