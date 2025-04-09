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

public enum BrowseResultMask implements UaEnumeratedType {
  None(0),

  ReferenceTypeId(1),

  IsForward(2),

  NodeClass(4),

  BrowseName(8),

  DisplayName(16),

  TypeDefinition(32),

  All(63),

  ReferenceTypeInfo(3),

  TargetInfo(60);

  private final int value;

  BrowseResultMask(int value) {
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

  public static @Nullable BrowseResultMask from(int value) {
    return switch (value) {
      case 0 -> None;
      case 1 -> ReferenceTypeId;
      case 2 -> IsForward;
      case 4 -> NodeClass;
      case 8 -> BrowseName;
      case 16 -> DisplayName;
      case 32 -> TypeDefinition;
      case 63 -> All;
      case 3 -> ReferenceTypeInfo;
      case 60 -> TargetInfo;
      default -> null;
    };
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "None"),
          new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "ReferenceTypeId"),
          new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "IsForward"),
          new EnumField(4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NodeClass"),
          new EnumField(8L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "BrowseName"),
          new EnumField(16L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "DisplayName"),
          new EnumField(32L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "TypeDefinition"),
          new EnumField(63L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "All"),
          new EnumField(
              3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "ReferenceTypeInfo"),
          new EnumField(60L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "TargetInfo")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=517");
  }
}
