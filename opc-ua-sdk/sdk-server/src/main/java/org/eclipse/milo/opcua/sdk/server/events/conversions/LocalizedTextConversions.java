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

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

final class LocalizedTextConversions {

  private LocalizedTextConversions() {}

  @Nullable
  static String localizedTextToString(@NonNull LocalizedText text) {
    return text.text();
  }

  @Nullable
  static Object convert(@NonNull Object o, OpcUaDataType targetType, boolean implicit) {
    if (o instanceof LocalizedText text) {
      return implicit ? implicitConversion(text, targetType) : explicitConversion(text, targetType);
    } else {
      return null;
    }
  }

  @Nullable
  static Object explicitConversion(@NonNull LocalizedText text, OpcUaDataType targetType) {
    return implicitConversion(text, targetType);
  }

  @Nullable
  static Object implicitConversion(@NonNull LocalizedText text, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case String:
        return localizedTextToString(text);
      default:
        return null;
    }
    // @formatter:on
  }
}
