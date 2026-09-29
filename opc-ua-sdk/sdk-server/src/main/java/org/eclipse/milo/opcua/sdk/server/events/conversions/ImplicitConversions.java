/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

public class ImplicitConversions {

  @Nullable
  public static Object convert(@NonNull Object sourceValue, @NonNull OpcUaDataType targetType) {
    OpcUaDataType sourceType = OpcUaDataType.fromBackingClass(sourceValue.getClass());

    if (sourceType == null) {
      return null;
    }

    if (!sourceType.getBackingClass().isAssignableFrom(sourceValue.getClass())) {
      return null;
    }

    return convert(sourceValue, sourceType, targetType);
  }

  private static Object convert(
      @NonNull Object sourceValue, OpcUaDataType sourceType, OpcUaDataType targetType) {

    return switch (sourceType) {
      case Boolean -> BooleanConversions.convert(sourceValue, targetType, true);
      case Byte -> ByteConversions.convert(sourceValue, targetType, true);

      case ByteString -> ByteStringConversions.convert(sourceValue, targetType, true);

      case DateTime -> DateTimeConversions.convert(sourceValue, targetType, true);

      case Double -> DoubleConversions.convert(sourceValue, targetType, true);

      case ExpandedNodeId -> ExpandedNodeIdConversions.convert(sourceValue, targetType, true);

      case Float -> FloatConversions.convert(sourceValue, targetType, true);
      case Guid -> GuidConversions.convert(sourceValue, targetType, true);

      case Int16 -> Int16Conversions.convert(sourceValue, targetType, true);

      case Int32 -> Int32Conversions.convert(sourceValue, targetType, true);

      case Int64 -> Int64Conversions.convert(sourceValue, targetType, true);

      case NodeId -> NodeIdConversions.convert(sourceValue, targetType, true);

      case SByte -> SByteConversions.convert(sourceValue, targetType, true);

      case StatusCode -> StatusCodeConversions.convert(sourceValue, targetType, true);

      case String -> StringConversions.convert(sourceValue, targetType, true);

      case LocalizedText -> LocalizedTextConversions.convert(sourceValue, targetType, true);

      case QualifiedName -> QualifiedNameConversions.convert(sourceValue, targetType, true);

      case UInt16 -> UInt16Conversions.convert(sourceValue, targetType, true);

      case UInt32 -> UInt32Conversions.convert(sourceValue, targetType, true);

      case UInt64 -> UInt64Conversions.convert(sourceValue, targetType, true);

      default -> null;
    };
  }

  public static int getPrecedence(@NonNull OpcUaDataType dataType) {
    return switch (dataType) {
      case Double -> 18;
      case Float -> 17;
      case Int64 -> 16;
      case UInt64 -> 15;
      case Int32 -> 14;
      case UInt32 -> 13;
      case StatusCode -> 12;
      case Int16 -> 11;
      case UInt16 -> 10;
      case SByte -> 9;
      case Byte -> 8;
      case Boolean -> 7;
      case Guid -> 6;
      case String -> 5;
      case ExpandedNodeId -> 4;
      case NodeId -> 3;
      case LocalizedText -> 2;
      case QualifiedName -> 1;
      default -> 0;
    };
  }
}
