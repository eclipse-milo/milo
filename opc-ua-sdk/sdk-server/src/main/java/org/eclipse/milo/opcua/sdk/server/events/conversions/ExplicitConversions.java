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

    return switch (sourceType) {
      case Boolean -> BooleanConversions.convert(sourceValue, targetType, false);
      case Byte -> ByteConversions.convert(sourceValue, targetType, false);

      case ByteString -> ByteStringConversions.convert(sourceValue, targetType, false);

      case DateTime -> DateTimeConversions.convert(sourceValue, targetType, false);

      case Double -> DoubleConversions.convert(sourceValue, targetType, false);

      case ExpandedNodeId -> ExpandedNodeIdConversions.convert(sourceValue, targetType, false);

      case Float -> FloatConversions.convert(sourceValue, targetType, false);
      case Guid -> GuidConversions.convert(sourceValue, targetType, false);

      case Int16 -> Int16Conversions.convert(sourceValue, targetType, false);

      case Int32 -> Int32Conversions.convert(sourceValue, targetType, false);

      case Int64 -> Int64Conversions.convert(sourceValue, targetType, false);

      case NodeId -> NodeIdConversions.convert(sourceValue, targetType, false);

      case SByte -> SByteConversions.convert(sourceValue, targetType, false);

      case StatusCode -> StatusCodeConversions.convert(sourceValue, targetType, false);

      case String -> StringConversions.convert(sourceValue, targetType, false);

      case LocalizedText -> LocalizedTextConversions.convert(sourceValue, targetType, false);

      case QualifiedName -> QualifiedNameConversions.convert(sourceValue, targetType, false);

      case UInt16 -> UInt16Conversions.convert(sourceValue, targetType, false);

      case UInt32 -> UInt32Conversions.convert(sourceValue, targetType, false);

      case UInt64 -> UInt64Conversions.convert(sourceValue, targetType, false);

      default -> null;
    };
  }
}
