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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.1</a>
 */
public enum BrokerTransportQualityOfService implements UaEnumeratedType {
  NotSpecified(0),

  BestEffort(1),

  AtLeastOnce(2),

  AtMostOnce(3),

  ExactlyOnce(4);

  private final int value;

  BrokerTransportQualityOfService(int value) {
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

  public static @Nullable BrokerTransportQualityOfService from(int value) {
    return switch (value) {
      case 0 -> NotSpecified;
      case 1 -> BestEffort;
      case 2 -> AtLeastOnce;
      case 3 -> AtMostOnce;
      case 4 -> ExactlyOnce;
      default -> null;
    };
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NotSpecified"),
          new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "BestEffort"),
          new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "AtLeastOnce"),
          new EnumField(3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "AtMostOnce"),
          new EnumField(4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "ExactlyOnce")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15008");
  }
}
