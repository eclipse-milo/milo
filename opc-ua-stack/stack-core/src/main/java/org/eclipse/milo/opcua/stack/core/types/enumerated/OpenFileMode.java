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

public enum OpenFileMode implements UaEnumeratedType {
  Read(1),

  Write(2),

  EraseExisting(4),

  Append(8);

  private final int value;

  OpenFileMode(int value) {
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

  public static @Nullable OpenFileMode from(int value) {
    return switch (value) {
      case 1 -> Read;
      case 2 -> Write;
      case 4 -> EraseExisting;
      case 8 -> Append;
      default -> null;
    };
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Read"),
          new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Write"),
          new EnumField(4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "EraseExisting"),
          new EnumField(8L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Append")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=11939");
  }
}
