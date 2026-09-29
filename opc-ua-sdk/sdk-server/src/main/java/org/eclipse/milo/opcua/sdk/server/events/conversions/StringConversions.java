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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

final class StringConversions {

  private static final DateFormat ISO_8601_UTC_DATE_FORMAT;

  static {
    ISO_8601_UTC_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    ISO_8601_UTC_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  private StringConversions() {}

  @Nullable
  static Boolean stringToBoolean(@NonNull String s) {
    if (s.equalsIgnoreCase("true") || s.equals("1")) {
      return true;
    } else if (s.equalsIgnoreCase("false") || s.equals("0")) {
      return false;
    } else {
      return null;
    }
  }

  @Nullable
  static UByte stringToByte(@NonNull String s) {
    try {
      return UByte.valueOf(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Nullable
  static DateTime stringToDateTime(@NonNull String s) {
    try {
      Date date = iso8601UtcStringToDate(s);

      return new DateTime(date);
    } catch (ParseException e) {
      return null;
    }
  }

  @Nullable
  static Double stringToDouble(@NonNull String s) {
    try {
      return Double.valueOf(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Nullable
  static ExpandedNodeId stringToExpandedNodeId(@NonNull String s) {
    try {
      return ExpandedNodeId.parse(s);
    } catch (Throwable t) {
      return null;
    }
  }

  @Nullable
  static Float stringToFloat(@NonNull String s) {
    try {
      return Float.valueOf(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Nullable
  static UUID stringToGuid(@NonNull String s) {
    try {
      return UUID.fromString(s);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  @Nullable
  static Short stringToInt16(@NonNull String s) {
    try {
      return Short.valueOf(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Nullable
  static Integer stringToInt32(@NonNull String s) {
    try {
      return Integer.valueOf(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Nullable
  static Long stringToInt64(@NonNull String s) {
    try {
      return Long.valueOf(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Nullable
  static NodeId stringToNodeId(@NonNull String s) {
    try {
      return NodeId.parse(s);
    } catch (Throwable t) {
      return null;
    }
  }

  @Nullable
  static Byte stringToSByte(@NonNull String s) {
    try {
      return Byte.valueOf(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @NonNull
  static LocalizedText stringToLocalizedText(@NonNull String s) {
    return new LocalizedText("", s);
  }

  @NonNull
  static QualifiedName stringToQualifiedName(@NonNull String s) {
    return new QualifiedName(0, s);
  }

  @Nullable
  static UShort stringToUInt16(@NonNull String s) {
    try {
      return UShort.valueOf(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Nullable
  static UInteger stringToUInt32(@NonNull String s) {
    try {
      return UInteger.valueOf(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Nullable
  static ULong stringToUInt64(@NonNull String s) {
    try {
      return ULong.valueOf(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private static Date iso8601UtcStringToDate(String s) throws ParseException {
    synchronized (ISO_8601_UTC_DATE_FORMAT) {
      return ISO_8601_UTC_DATE_FORMAT.parse(s);
    }
  }

  @Nullable
  static Object convert(@NonNull Object o, OpcUaDataType targetType, boolean implicit) {
    if (o instanceof String s) {
      return implicit ? implicitConversion(s, targetType) : explicitConversion(s, targetType);
    } else {
      return null;
    }
  }

  @Nullable
  static Object explicitConversion(@NonNull String s, OpcUaDataType targetType) {
    return switch (targetType) {
      case DateTime -> stringToDateTime(s);
      case ExpandedNodeId -> stringToExpandedNodeId(s);
      case NodeId -> stringToNodeId(s);
      case LocalizedText -> stringToLocalizedText(s);
      case QualifiedName -> stringToQualifiedName(s);
      default -> implicitConversion(s, targetType);
    };
  }

  @Nullable
  static Object implicitConversion(@NonNull String s, OpcUaDataType targetType) {
    return switch (targetType) {
      case Boolean -> stringToBoolean(s);
      case Byte -> stringToByte(s);
      case Double -> stringToDouble(s);
      case Float -> stringToFloat(s);
      case Guid -> stringToGuid(s);
      case Int16 -> stringToInt16(s);
      case Int32 -> stringToInt32(s);
      case Int64 -> stringToInt64(s);
      case SByte -> stringToSByte(s);
      case UInt16 -> stringToUInt16(s);
      case UInt32 -> stringToUInt32(s);
      case UInt64 -> stringToUInt64(s);
      default -> null;
    };
  }
}
