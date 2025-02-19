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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

final class UInt32Conversions {

  private UInt32Conversions() {}

  @NonNull
  static Boolean uInt32ToBoolean(@NonNull UInteger ui) {
    return ui.intValue() != 0;
  }

  @Nullable
  static UByte uInt32ToByte(@NonNull UInteger ui) {
    long l = ui.longValue();

    if (l <= UByte.MAX_VALUE) {
      return ubyte(l);
    } else {
      return null;
    }
  }

  @NonNull
  static Double uInt32ToDouble(@NonNull UInteger ui) {
    return ui.doubleValue();
  }

  @NonNull
  static Float uInt32ToFloat(@NonNull UInteger ui) {
    return ui.floatValue();
  }

  @Nullable
  static Short uInt32ToInt16(@NonNull UInteger ui) {
    int i = ui.intValue();

    if (i <= Short.MAX_VALUE) {
      return (short) i;
    } else {
      return null;
    }
  }

  @Nullable
  static Integer uInt32ToInt32(@NonNull UInteger ui) {
    long l = ui.longValue();

    if (l <= Integer.MAX_VALUE) {
      return (int) l;
    } else {
      return null;
    }
  }

  @NonNull
  static Long uInt32ToInt64(@NonNull UInteger ui) {
    return ui.longValue();
  }

  @Nullable
  static Byte uInt32ToSByte(@NonNull UInteger ui) {
    int i = ui.intValue();

    if (i <= Byte.MAX_VALUE) {
      return (byte) i;
    } else {
      return null;
    }
  }

  @NonNull
  static StatusCode uInt32ToStatusCode(@NonNull UInteger ui) {
    return new StatusCode(ui);
  }

  @NonNull
  static String uInt32ToString(@NonNull UInteger ui) {
    return ui.toString();
  }

  @Nullable
  static UShort uInt32ToUInt16(@NonNull UInteger ui) {
    int i = ui.intValue();

    if (i <= UShort.MAX_VALUE) {
      return ushort(i);
    } else {
      return null;
    }
  }

  @NonNull
  static ULong uInt32ToUInt64(@NonNull UInteger ui) {
    return ulong(ui.longValue());
  }

  @Nullable
  static Object convert(@Nullable Object o, OpcUaDataType targetType, boolean implicit) {
    if (o instanceof UInteger) {
      UInteger ui = (UInteger) o;

      return implicit ? implicitConversion(ui, targetType) : explicitConversion(ui, targetType);
    } else {
      return null;
    }
  }

  @Nullable
  static Object explicitConversion(@NonNull UInteger ui, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case Boolean:
        return uInt32ToBoolean(ui);
      case Byte:
        return uInt32ToByte(ui);
      case Int16:
        return uInt32ToInt16(ui);
      case SByte:
        return uInt32ToSByte(ui);
      case StatusCode:
        return uInt32ToStatusCode(ui);
      case String:
        return uInt32ToString(ui);
      case UInt16:
        return uInt32ToUInt16(ui);
      default:
        return implicitConversion(ui, targetType);
    }
    // @formatter:on
  }

  @Nullable
  static Object implicitConversion(@NonNull UInteger ui, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case Double:
        return uInt32ToDouble(ui);
      case Float:
        return uInt32ToFloat(ui);
      case Int32:
        return uInt32ToInt32(ui);
      case Int64:
        return uInt32ToInt64(ui);
      case UInt64:
        return uInt32ToUInt64(ui);
      default:
        return null;
    }
    // @formatter:on
  }
}
