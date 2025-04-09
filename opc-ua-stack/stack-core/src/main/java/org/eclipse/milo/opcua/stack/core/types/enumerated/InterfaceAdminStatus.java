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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.1/#5.3.1.2">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.1/#5.3.1.2</a>
 */
public enum InterfaceAdminStatus implements UaEnumeratedType {
  /** Ready to pass packets. */
  Up(0),

  /** Not ready to pass packets and not in some test mode. */
  Down(1),

  /** In some test mode. */
  Testing(2);

  private final int value;

  InterfaceAdminStatus(int value) {
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

  public static @Nullable InterfaceAdminStatus from(int value) {
    return switch (value) {
      case 0 -> Up;
      case 1 -> Down;
      case 2 -> Testing;
      default -> null;
    };
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(
              0L, LocalizedText.NULL_VALUE, new LocalizedText("", "Ready to pass packets."), "Up"),
          new EnumField(
              1L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "Not ready to pass packets and not in some test mode."),
              "Down"),
          new EnumField(
              2L, LocalizedText.NULL_VALUE, new LocalizedText("", "In some test mode."), "Testing")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=24212");
  }
}
