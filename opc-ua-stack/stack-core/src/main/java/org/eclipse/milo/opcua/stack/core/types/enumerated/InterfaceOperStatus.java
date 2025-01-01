/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.1/#5.3.1.3">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.1/#5.3.1.3</a>
 */
public enum InterfaceOperStatus implements UaEnumeratedType {
  /** Ready to pass packets. */
  Up(0),

  /** The interface does not pass any packets. */
  Down(1),

  /** In some test mode. No operational packets can be passed. */
  Testing(2),

  /** Status cannot be determined for some reason. */
  Unknown(3),

  /** Waiting for some external event. */
  Dormant(4),

  /** Some component (typically hardware) is missing. */
  NotPresent(5),

  /** Down due to state of lower-layer interface(s). */
  LowerLayerDown(6);

  private final int value;

  InterfaceOperStatus(int value) {
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

  public static @Nullable InterfaceOperStatus from(int value) {
    switch (value) {
      case 0:
        return Up;
      case 1:
        return Down;
      case 2:
        return Testing;
      case 3:
        return Unknown;
      case 4:
        return Dormant;
      case 5:
        return NotPresent;
      case 6:
        return LowerLayerDown;
      default:
        return null;
    }
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(
              0L, LocalizedText.NULL_VALUE, new LocalizedText("", "Ready to pass packets."), "Up"),
          new EnumField(
              1L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "The interface does not pass any packets."),
              "Down"),
          new EnumField(
              2L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "In some test mode. No operational packets can be passed."),
              "Testing"),
          new EnumField(
              3L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "Status cannot be determined for some reason."),
              "Unknown"),
          new EnumField(
              4L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "Waiting for some external event."),
              "Dormant"),
          new EnumField(
              5L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "Some component (typically hardware) is missing."),
              "NotPresent"),
          new EnumField(
              6L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "Down due to state of lower-layer interface(s)."),
              "LowerLayerDown")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=24214");
  }
}
