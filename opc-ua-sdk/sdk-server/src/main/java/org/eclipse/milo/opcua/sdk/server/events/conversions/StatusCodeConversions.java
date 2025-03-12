/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

final class StatusCodeConversions {

  private StatusCodeConversions() {}

  @NonNull
  static Short statusCodeToInt16(@NonNull StatusCode s) {
    return (short) ((s.value() >>> 16) & 0xFFFF);
  }

  @NonNull
  static Integer statusCodeToInt32(@NonNull StatusCode s) {
    return (int) s.value();
  }

  @NonNull
  static Long statusCodeToInt64(@NonNull StatusCode s) {
    return s.value();
  }

  @NonNull
  static UShort statusCodeToUInt16(@NonNull StatusCode s) {
    return ushort(statusCodeToInt16(s));
  }

  @NonNull
  static UInteger statusCodeToUInt32(@NonNull StatusCode s) {
    return uint(statusCodeToInt32(s));
  }

  @NonNull
  static ULong statusCodeToUInt64(@NonNull StatusCode s) {
    return ulong(s.value());
  }

  @Nullable
  static Object convert(@NonNull Object o, OpcUaDataType targetType, boolean implicit) {
    if (o instanceof StatusCode) {
      StatusCode s = (StatusCode) o;

      return implicit ? implicitConversion(s, targetType) : explicitConversion(s, targetType);
    } else {
      return null;
    }
  }

  @Nullable
  static Object explicitConversion(@NonNull StatusCode s, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case Int16:
        return statusCodeToInt16(s);
      case UInt16:
        return statusCodeToUInt16(s);
      default:
        return implicitConversion(s, targetType);
    }
    // @formatter:on
  }

  @Nullable
  static Object implicitConversion(@NonNull StatusCode s, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case Int32:
        return statusCodeToInt32(s);
      case Int64:
        return statusCodeToInt64(s);
      case UInt32:
        return statusCodeToUInt32(s);
      case UInt64:
        return statusCodeToUInt64(s);
      default:
        return null;
    }
    // @formatter:on
  }
}
