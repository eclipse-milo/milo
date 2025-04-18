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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
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

final class BooleanConversions {

  private BooleanConversions() {}

  @NonNull
  static UByte booleanToByte(@NonNull Boolean b) {
    return ubyte(b ? 1 : 0);
  }

  @NonNull
  static Double booleanToDouble(@NonNull Boolean b) {
    return b ? 1.0 : 0.0;
  }

  @NonNull
  static Float booleanToFloat(@NonNull Boolean b) {
    return b ? 1.0f : 0.0f;
  }

  @NonNull
  static Short booleanToInt16(@NonNull Boolean b) {
    return b ? (short) 1 : (short) 0;
  }

  @NonNull
  static Integer booleanToInt32(@NonNull Boolean b) {
    return b ? 1 : 0;
  }

  @NonNull
  static Long booleanToInt64(@NonNull Boolean b) {
    return b ? 1L : 0L;
  }

  @NonNull
  static Byte booleanToSByte(@NonNull Boolean b) {
    return b ? (byte) 1 : (byte) 0;
  }

  @NonNull
  static String booleanToString(@NonNull Boolean b) {
    return b ? "1" : "0";
  }

  @NonNull
  static UShort booleanToUInt16(@NonNull Boolean b) {
    return b ? ushort(1) : ushort(0);
  }

  @NonNull
  static UInteger booleanToUInt32(@NonNull Boolean b) {
    return b ? uint(1) : uint(0);
  }

  @NonNull
  static ULong booleanToUInt64(@NonNull Boolean b) {
    return b ? ulong(1) : ulong(0);
  }

  @Nullable
  static Object convert(@NonNull Object o, OpcUaDataType targetType, boolean implicit) {
    if (o instanceof Boolean b) {
      return implicit ? implicitConversion(b, targetType) : explicitConversion(b, targetType);
    } else {
      return null;
    }
  }

  @Nullable
  static Object explicitConversion(@NonNull Boolean b, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case String:
        return booleanToString(b);
      default:
        return implicitConversion(b, targetType);
    }
    // @formatter:on
  }

  @Nullable
  static Object implicitConversion(@NonNull Boolean b, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case Byte:
        return booleanToByte(b);
      case Double:
        return booleanToDouble(b);
      case Float:
        return booleanToFloat(b);
      case Int16:
        return booleanToInt16(b);
      case Int32:
        return booleanToInt32(b);
      case Int64:
        return booleanToInt64(b);
      case SByte:
        return booleanToSByte(b);
      case UInt16:
        return booleanToUInt16(b);
      case UInt32:
        return booleanToUInt32(b);
      case UInt64:
        return booleanToUInt64(b);
      default:
        return null;
    }
    // @formatter:on
  }
}
