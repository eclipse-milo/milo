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

final class FloatConversions {

  private FloatConversions() {}

  @NonNull
  static Boolean floatToBoolean(@NonNull Float f) {
    return f != 0.0f;
  }

  @Nullable
  static UByte floatToByte(@NonNull Float f) {
    long rounded = Math.round(f);

    if (rounded >= 0 && rounded <= UByte.MAX_VALUE) {
      return ubyte(rounded);
    } else {
      return null;
    }
  }

  @NonNull
  static Double floatToDouble(@NonNull Float f) {
    return f.doubleValue();
  }

  @Nullable
  static Short floatToInt16(@NonNull Float f) {
    long rounded = Math.round(f);

    if (rounded >= Short.MIN_VALUE && rounded <= Short.MAX_VALUE) {
      return (short) rounded;
    } else {
      return null;
    }
  }

  @NonNull
  static Integer floatToInt32(@NonNull Float f) {
    return Math.round(f);
  }

  @NonNull
  static Long floatToInt64(@NonNull Float f) {
    return (long) Math.round(f);
  }

  @Nullable
  static Byte floatToSByte(@NonNull Float f) {
    long rounded = Math.round(f);

    if (rounded >= Byte.MIN_VALUE && rounded <= Byte.MAX_VALUE) {
      return (byte) rounded;
    } else {
      return null;
    }
  }

  @NonNull
  static String floatToString(@NonNull Float f) {
    return f.toString();
  }

  @Nullable
  static UShort floatToUInt16(@NonNull Float f) {
    long rounded = Math.round(f);

    if (rounded >= UShort.MIN_VALUE && rounded <= UShort.MAX_VALUE) {
      return ushort((int) rounded);
    } else {
      return null;
    }
  }

  @Nullable
  static UInteger floatToUInt32(@NonNull Float f) {
    int rounded = Math.round(f);

    if (rounded >= 0) {
      return uint(rounded);
    } else {
      return null;
    }
  }

  @Nullable
  static ULong floatToUInt64(@NonNull Float f) {
    long rounded = Math.round(f);

    if (rounded >= 0) {
      return ulong(rounded);
    } else {
      return null;
    }
  }

  @Nullable
  static Object convert(@Nullable Object o, OpcUaDataType targetType, boolean implicit) {
    if (o instanceof Float) {
      Float f = (Float) o;

      return implicit ? implicitConversion(f, targetType) : explicitConversion(f, targetType);
    } else {
      return null;
    }
  }

  @Nullable
  static Object explicitConversion(@NonNull Float f, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case Boolean:
        return floatToBoolean(f);
      case Byte:
        return floatToByte(f);
      case Int16:
        return floatToInt16(f);
      case Int32:
        return floatToInt32(f);
      case Int64:
        return floatToInt64(f);
      case SByte:
        return floatToSByte(f);
      case String:
        return floatToString(f);
      case UInt16:
        return floatToUInt16(f);
      case UInt32:
        return floatToUInt32(f);
      case UInt64:
        return floatToUInt64(f);
      default:
        return implicitConversion(f, targetType);
    }
    // @formatter:on
  }

  @Nullable
  static Object implicitConversion(@NonNull Float f, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case Double:
        return floatToDouble(f);
      default:
        return null;
    }
    // @formatter:on
  }
}
