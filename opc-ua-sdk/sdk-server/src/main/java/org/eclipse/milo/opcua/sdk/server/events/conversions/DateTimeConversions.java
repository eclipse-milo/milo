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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

final class DateTimeConversions {

  private DateTimeConversions() {}

  @NonNull
  static String dateTimeToString(@NonNull DateTime dt) {
    return dateToIso8601UtcString(dt.getJavaDate());
  }

  private static final DateFormat ISO_8601_UTC_DATE_FORMAT;

  static {
    ISO_8601_UTC_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    ISO_8601_UTC_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  private static String dateToIso8601UtcString(Date date) {
    synchronized (ISO_8601_UTC_DATE_FORMAT) {
      return ISO_8601_UTC_DATE_FORMAT.format(date);
    }
  }

  @Nullable
  static Object convert(@NonNull Object o, OpcUaDataType targetType, boolean implicit) {
    if (o instanceof DateTime d) {
      return implicit ? implicitConversion(d, targetType) : explicitConversion(d, targetType);
    } else {
      return null;
    }
  }

  @Nullable
  static Object explicitConversion(@NonNull DateTime d, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case String:
        return dateTimeToString(d);
      default:
        return implicitConversion(d, targetType);
    }
    // @formatter:on
  }

  @Nullable
  static Object implicitConversion(@NonNull DateTime d, OpcUaDataType targetType) {
    // no implicit conversions exist
    return null;
  }
}
