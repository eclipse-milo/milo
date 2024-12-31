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

import java.nio.ByteBuffer;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

final class GuidConversions {

  private GuidConversions() {}

  @NonNull
  static ByteString guidToByteString(@NonNull UUID uuid) {
    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());

    return ByteString.of(bb.array());
  }

  @NonNull
  static String guidToString(@NonNull UUID uuid) {
    return uuid.toString();
  }

  @Nullable
  static Object convert(@NonNull Object o, BuiltinDataType targetType, boolean implicit) {
    if (o instanceof UUID) {
      UUID uuid = (UUID) o;

      return implicit ? implicitConversion(uuid, targetType) : explicitConversion(uuid, targetType);
    } else {
      return null;
    }
  }

  @Nullable
  static Object explicitConversion(@NonNull UUID uuid, BuiltinDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case ByteString:
        return guidToByteString(uuid);
      case String:
        return guidToString(uuid);
      default:
        return implicitConversion(uuid, targetType);
    }
    // @formatter:on
  }

  @Nullable
  static Object implicitConversion(@NonNull UUID uuid, BuiltinDataType targetType) {
    // no implicit conversions exist
    return null;
  }
}
