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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.1/#5.3.1.10">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.1/#5.3.1.10</a>
 */
public enum PortIdSubtype implements UaEnumeratedType {
  /** Represents a port identifier based on the ifAlias MIB object defined in IETF RFC 2863. */
  InterfaceAlias(1),

  /**
   * Represents a port identifier based on the value of entPhysicalAlias (defined in IETF RFC 2737)
   * for a port component (i.e., entPhysicalClass value of port(10) or backplane(4)), within the
   * containing chassis.
   */
  PortComponent(2),

  /**
   * Represents a port identifier based on a unicast source address (encoded in network byte order
   * and IEEE 802.3 canonical bit order) which has been detected by the agent and associated with a
   * particular port (IEEE Std 802-2014).
   */
  MacAddress(3),

  /**
   * Represents a port identifier based on a network address, detected by the agent and associated
   * with a particular port.
   */
  NetworkAddress(4),

  /** Represents a port identifier based on the ifName MIB object, defined in IETF RFC 2863. */
  InterfaceName(5),

  /**
   * Represents a port identifier based on the agent-local identifier of the circuit (defined in
   * IETF RFC 3046), detected by the agent and associated with a particular port.
   */
  AgentCircuitId(6),

  /** Represents a port identifier based on a value locally assigned. */
  Local(7);

  private final int value;

  PortIdSubtype(int value) {
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

  public static @Nullable PortIdSubtype from(int value) {
    return switch (value) {
      case 1 -> InterfaceAlias;
      case 2 -> PortComponent;
      case 3 -> MacAddress;
      case 4 -> NetworkAddress;
      case 5 -> InterfaceName;
      case 6 -> AgentCircuitId;
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
                  "Represents a port identifier based on the ifAlias MIB object defined in IETF RFC"
                      + " 2863."),
              "InterfaceAlias"),
          new EnumField(
              2L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Represents a port identifier based on the value of entPhysicalAlias (defined in"
                      + " IETF RFC 2737) for a port component (i.e., entPhysicalClass value of"
                      + " port(10) or backplane(4)), within the containing chassis."),
              "PortComponent"),
          new EnumField(
              3L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Represents a port identifier based on a unicast source address (encoded in"
                      + " network byte order and IEEE 802.3 canonical bit order) which has been"
                      + " detected by the agent and associated with a particular port (IEEE Std"
                      + " 802-2014)."),
              "MacAddress"),
          new EnumField(
              4L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Represents a port identifier based on a network address, detected by the agent"
                      + " and associated with a particular port."),
              "NetworkAddress"),
          new EnumField(
              5L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Represents a port identifier based on the ifName MIB object, defined in IETF RFC"
                      + " 2863."),
              "InterfaceName"),
          new EnumField(
              6L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Represents a port identifier based on the agent-local identifier of the circuit"
                      + " (defined in IETF RFC 3046), detected by the agent and associated with a"
                      + " particular port."),
              "AgentCircuitId"),
          new EnumField(
              7L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "", "Represents a port identifier based on a value locally assigned."),
              "Local")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=18949");
  }
}
