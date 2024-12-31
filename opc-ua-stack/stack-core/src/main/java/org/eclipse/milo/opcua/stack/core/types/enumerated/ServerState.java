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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.6">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.6</a>
 */
public enum ServerState implements UaEnumeratedType {
  Running(0),

  Failed(1),

  NoConfiguration(2),

  Suspended(3),

  Shutdown(4),

  Test(5),

  CommunicationFault(6),

  Unknown(7);

  private final int value;

  ServerState(int value) {
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

  public static @Nullable ServerState from(int value) {
    switch (value) {
      case 0:
        return Running;
      case 1:
        return Failed;
      case 2:
        return NoConfiguration;
      case 3:
        return Suspended;
      case 4:
        return Shutdown;
      case 5:
        return Test;
      case 6:
        return CommunicationFault;
      case 7:
        return Unknown;
      default:
        return null;
    }
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Running"),
          new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Failed"),
          new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NoConfiguration"),
          new EnumField(3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Suspended"),
          new EnumField(4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Shutdown"),
          new EnumField(5L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Test"),
          new EnumField(
              6L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "CommunicationFault"),
          new EnumField(7L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Unknown")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=852");
  }
}
