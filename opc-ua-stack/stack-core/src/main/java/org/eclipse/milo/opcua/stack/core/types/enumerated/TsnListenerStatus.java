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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.1/#5.3.1.8">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.1/#5.3.1.8</a>
 */
public enum TsnListenerStatus implements UaEnumeratedType {
  /** No Listener detected. */
  None(0),

  /** Listener ready (configured). */
  Ready(1),

  /** One or more Listeners ready, and one or more Listeners failed. */
  PartialFailed(2),

  /** Listener failed. */
  Failed(3);

  private final int value;

  TsnListenerStatus(int value) {
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

  public static @Nullable TsnListenerStatus from(int value) {
    return switch (value) {
      case 0 -> None;
      case 1 -> Ready;
      case 2 -> PartialFailed;
      case 3 -> Failed;
      default -> null;
    };
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(
              0L, LocalizedText.NULL_VALUE, new LocalizedText("", "No Listener detected."), "None"),
          new EnumField(
              1L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "Listener ready (configured)."),
              "Ready"),
          new EnumField(
              2L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "", "One or more Listeners ready, and one or more Listeners failed."),
              "PartialFailed"),
          new EnumField(
              3L, LocalizedText.NULL_VALUE, new LocalizedText("", "Listener failed."), "Failed")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=24224");
  }
}
