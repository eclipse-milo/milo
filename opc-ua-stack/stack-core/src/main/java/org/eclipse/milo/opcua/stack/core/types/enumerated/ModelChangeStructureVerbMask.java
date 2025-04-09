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

public enum ModelChangeStructureVerbMask implements UaEnumeratedType {
  NodeAdded(1),

  NodeDeleted(2),

  ReferenceAdded(4),

  ReferenceDeleted(8),

  DataTypeChanged(16);

  private final int value;

  ModelChangeStructureVerbMask(int value) {
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

  public static @Nullable ModelChangeStructureVerbMask from(int value) {
    return switch (value) {
      case 1 -> NodeAdded;
      case 2 -> NodeDeleted;
      case 4 -> ReferenceAdded;
      case 8 -> ReferenceDeleted;
      case 16 -> DataTypeChanged;
      default -> null;
    };
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NodeAdded"),
          new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NodeDeleted"),
          new EnumField(4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "ReferenceAdded"),
          new EnumField(8L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "ReferenceDeleted"),
          new EnumField(16L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "DataTypeChanged")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=11941");
  }
}
