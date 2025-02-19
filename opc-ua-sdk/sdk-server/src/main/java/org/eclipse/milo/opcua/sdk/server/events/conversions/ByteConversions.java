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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

final class ByteConversions {

  private ByteConversions() {}

  @NonNull
  static Boolean byteToBoolean(@NonNull UByte ub) {
    return ub.intValue() != 0;
  }

  @NonNull
  static Double byteToDouble(@NonNull UByte ub) {
    return ub.doubleValue();
  }

  @NonNull
  static Float byteToFloat(@NonNull UByte ub) {
    return ub.floatValue();
  }

  @NonNull
  static Short byteToInt16(@NonNull UByte ub) {
    return ub.shortValue();
  }

  @NonNull
  static Integer byteToInt32(@NonNull UByte ub) {
    return ub.intValue();
  }

  @NonNull
  static Long byteToInt64(@NonNull UByte ub) {
    return ub.longValue();
  }

  @Nullable
  static Byte byteToSByte(@NonNull UByte ub) {
    return ub.intValue() > Byte.MAX_VALUE ? null : ub.byteValue();
  }

  @NonNull
  static String byteToString(@NonNull UByte ub) {
    return ub.toString();
  }

  @NonNull
  static UShort byteToUInt16(@NonNull UByte ub) {
    return ushort(ub.intValue());
  }

  @NonNull
  static UInteger byteToUInt32(@NonNull UByte ub) {
    return uint(ub.intValue());
  }

  @NonNull
  static ULong byteToUInt64(@NonNull UByte ub) {
    return ulong(ub.longValue());
  }

  @Nullable
  static Object convert(@NonNull Object o, OpcUaDataType targetType, boolean implicit) {
    if (o instanceof UByte) {
      UByte b = (UByte) o;

      return implicit ? implicitConversion(b, targetType) : explicitConversion(b, targetType);
    } else {
      return null;
    }
  }

  @Nullable
  static Object explicitConversion(@NonNull UByte b, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case Boolean:
        return byteToBoolean(b);
      case String:
        return byteToString(b);
      default:
        return implicitConversion(b, targetType);
    }
    // @formatter:on
  }

  @Nullable
  static Object implicitConversion(@NonNull UByte b, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case Double:
        return byteToDouble(b);
      case Float:
        return byteToFloat(b);
      case Int16:
        return byteToInt16(b);
      case Int32:
        return byteToInt32(b);
      case Int64:
        return byteToInt64(b);
      case SByte:
        return byteToSByte(b);
      case UInt16:
        return byteToUInt16(b);
      case UInt32:
        return byteToUInt32(b);
      case UInt64:
        return byteToUInt64(b);
      default:
        return null;
    }
    // @formatter:on
  }
}
