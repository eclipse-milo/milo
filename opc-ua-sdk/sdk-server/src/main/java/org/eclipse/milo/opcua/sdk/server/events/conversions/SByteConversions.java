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

final class SByteConversions {

  private SByteConversions() {}

  @NonNull
  static Boolean sByteToBoolean(@NonNull Byte b) {
    return b != 0;
  }

  @Nullable
  static UByte sByteToByte(@NonNull Byte b) {
    if (b >= 0) {
      return ubyte(b);
    } else {
      return null;
    }
  }

  @NonNull
  static Double sByteToDouble(@NonNull Byte b) {
    return b.doubleValue();
  }

  @NonNull
  static Float sByteToFloat(@NonNull Byte b) {
    return b.floatValue();
  }

  @NonNull
  static Short sByteToInt16(@NonNull Byte b) {
    return b.shortValue();
  }

  @NonNull
  static Integer sByteToInt32(@NonNull Byte b) {
    return b.intValue();
  }

  @NonNull
  static Long sByteToInt64(@NonNull Byte b) {
    return b.longValue();
  }

  @NonNull
  static String sByteToString(@NonNull Byte b) {
    return b.toString();
  }

  @Nullable
  static UShort sByteToUInt16(@NonNull Byte b) {
    if (b >= 0) {
      return ushort(b);
    } else {
      return null;
    }
  }

  @Nullable
  static UInteger sByteToUInt32(@NonNull Byte b) {
    if (b >= 0) {
      return uint(b);
    } else {
      return null;
    }
  }

  @Nullable
  static ULong sByteToUInt64(@NonNull Byte b) {
    if (b >= 0) {
      return ulong(b);
    } else {
      return null;
    }
  }

  @Nullable
  static Object convert(@NonNull Object o, OpcUaDataType targetType, boolean implicit) {
    if (o instanceof Byte b) {
      return implicit ? implicitConversion(b, targetType) : explicitConversion(b, targetType);
    } else {
      return null;
    }
  }

  @Nullable
  static Object explicitConversion(@NonNull Byte b, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case Boolean:
        return sByteToBoolean(b);
      case Byte:
        return sByteToByte(b);
      case String:
        return sByteToString(b);
      default:
        return implicitConversion(b, targetType);
    }
    // @formatter:on
  }

  @Nullable
  static Object implicitConversion(@NonNull Byte b, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case Double:
        return sByteToDouble(b);
      case Float:
        return sByteToFloat(b);
      case Int16:
        return sByteToInt16(b);
      case Int32:
        return sByteToInt32(b);
      case Int64:
        return sByteToInt64(b);
      case UInt16:
        return sByteToUInt16(b);
      case UInt32:
        return sByteToUInt32(b);
      case UInt64:
        return sByteToUInt64(b);
      default:
        return null;
    }
    // @formatter:on
  }
}
