/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.7">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.7</a>
 */
public enum ConfigurationUpdateType implements UaEnumeratedType {
  /** The target is added. An error occurs if a name conflict occurs. */
  Insert(1),

  /**
   * Existing records are updated. An error occurs if a name cannot be matched to an existing
   * record.
   */
  Replace(2),

  /** Existing records are updated. New records are created if no match to an exising record. */
  InsertOrReplace(3),

  /**
   * Existing records are deleted. An error occurs if a name cannot be matched to an existing
   * record.
   */
  Delete(4);

  private final int value;

  ConfigurationUpdateType(int value) {
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

  public static @Nullable ConfigurationUpdateType from(int value) {
    return switch (value) {
      case 1 -> Insert;
      case 2 -> Replace;
      case 3 -> InsertOrReplace;
      case 4 -> Delete;
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
                  "", "The target is added. An error occurs if a name conflict occurs."),
              "Insert"),
          new EnumField(
              2L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Existing records are updated. An error occurs if a name cannot be matched to an"
                      + " existing record."),
              "Replace"),
          new EnumField(
              3L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Existing records are updated. New records are created if no match to an exising"
                      + " record."),
              "InsertOrReplace"),
          new EnumField(
              4L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Existing records are deleted. An error occurs if a name cannot be matched to an"
                      + " existing record."),
              "Delete")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15539");
  }
}
