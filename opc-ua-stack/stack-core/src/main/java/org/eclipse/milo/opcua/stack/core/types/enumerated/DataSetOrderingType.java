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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.1/#6.3.1.1.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.1/#6.3.1.1.3</a>
 */
public enum DataSetOrderingType implements UaEnumeratedType {
  Undefined(0),

  AscendingWriterId(1),

  AscendingWriterIdSingle(2);

  private final int value;

  DataSetOrderingType(int value) {
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

  public static @Nullable DataSetOrderingType from(int value) {
    switch (value) {
      case 0:
        return Undefined;
      case 1:
        return AscendingWriterId;
      case 2:
        return AscendingWriterIdSingle;
      default:
        return null;
    }
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Undefined"),
          new EnumField(
              1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "AscendingWriterId"),
          new EnumField(
              2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "AscendingWriterIdSingle")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=20408");
  }
}
