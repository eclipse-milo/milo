/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.conversions;

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ExplicitConversions {

  private ExplicitConversions() {}

  @Nullable
  public static Object convert(@Nullable Object sourceValue, @NonNull OpcUaDataType targetType) {
    if (sourceValue == null) {
      return null;
    }

    OpcUaDataType sourceType = OpcUaDataType.fromBackingClass(sourceValue.getClass());

    if (sourceType == null) {
      return null;
    }

    if (!sourceType.getBackingClass().isAssignableFrom(sourceValue.getClass())) {
      return null;
    }

    if (sourceType == targetType) {
      return sourceValue;
    }

    return convert(sourceValue, sourceType, targetType);
  }

  @Nullable
  private static Object convert(
      @NonNull Object sourceValue, OpcUaDataType sourceType, OpcUaDataType targetType) {

    switch (sourceType) {
      case Boolean:
        return BooleanConversions.convert(sourceValue, targetType, false);
      case Byte:
        return ByteConversions.convert(sourceValue, targetType, false);

      case ByteString:
        return ByteStringConversions.convert(sourceValue, targetType, false);

      case DateTime:
        return DateTimeConversions.convert(sourceValue, targetType, false);

      case Double:
        return DoubleConversions.convert(sourceValue, targetType, false);

      case ExpandedNodeId:
        return ExpandedNodeIdConversions.convert(sourceValue, targetType, false);

      case Float:
        return FloatConversions.convert(sourceValue, targetType, false);
      case Guid:
        return GuidConversions.convert(sourceValue, targetType, false);

      case Int16:
        return Int16Conversions.convert(sourceValue, targetType, false);

      case Int32:
        return Int32Conversions.convert(sourceValue, targetType, false);

      case Int64:
        return Int64Conversions.convert(sourceValue, targetType, false);

      case NodeId:
        return NodeIdConversions.convert(sourceValue, targetType, false);

      case SByte:
        return SByteConversions.convert(sourceValue, targetType, false);

      case StatusCode:
        return StatusCodeConversions.convert(sourceValue, targetType, false);

      case String:
        return StringConversions.convert(sourceValue, targetType, false);

      case LocalizedText:
        return LocalizedTextConversions.convert(sourceValue, targetType, false);

      case QualifiedName:
        return QualifiedNameConversions.convert(sourceValue, targetType, false);

      case UInt16:
        return UInt16Conversions.convert(sourceValue, targetType, false);

      case UInt32:
        return UInt32Conversions.convert(sourceValue, targetType, false);

      case UInt64:
        return UInt64Conversions.convert(sourceValue, targetType, false);

      default:
        return null;
    }
  }
}
