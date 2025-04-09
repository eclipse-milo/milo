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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/6.8">https://reference.opcfoundation.org/v105/Core/docs/Part11/6.8</a>
 */
public enum PerformUpdateType implements UaEnumeratedType {
  /** Data was inserted. */
  Insert(1),

  /** Data was replaced. */
  Replace(2),

  /** Data was inserted or replaced. */
  Update(3),

  /** Data was deleted. */
  Remove(4);

  private final int value;

  PerformUpdateType(int value) {
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

  public static @Nullable PerformUpdateType from(int value) {
    return switch (value) {
      case 1 -> Insert;
      case 2 -> Replace;
      case 3 -> Update;
      case 4 -> Remove;
      default -> null;
    };
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(
              1L, LocalizedText.NULL_VALUE, new LocalizedText("", "Data was inserted."), "Insert"),
          new EnumField(
              2L, LocalizedText.NULL_VALUE, new LocalizedText("", "Data was replaced."), "Replace"),
          new EnumField(
              3L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "Data was inserted or replaced."),
              "Update"),
          new EnumField(
              4L, LocalizedText.NULL_VALUE, new LocalizedText("", "Data was deleted."), "Remove")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=11293");
  }
}
