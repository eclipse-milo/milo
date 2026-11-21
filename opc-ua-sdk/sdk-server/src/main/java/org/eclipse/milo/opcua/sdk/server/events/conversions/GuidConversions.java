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
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

final class GuidConversions {

  private GuidConversions() {}

  @NonNull
  static ByteString guidToByteString(@NonNull UUID uuid) {
    // the ByteString is the 16-byte OPC UA Part 6 §5.1.3 binary encoding of the
    // GUID: Data1, Data2 and Data3 are little-endian, Data4 is a raw byte array.
    // This matches what OpcUaBinaryEncoder.encodeGuid writes for the same value.
    ByteBuffer bb = ByteBuffer.allocate(16);
    long msb = uuid.getMostSignificantBits();
    long lsb = uuid.getLeastSignificantBits();

    bb.putInt(Integer.reverseBytes((int) (msb >>> 32)));
    bb.putShort(Short.reverseBytes((short) ((msb >>> 16) & 0xFFFF)));
    bb.putShort(Short.reverseBytes((short) (msb & 0xFFFF)));
    bb.putLong(lsb); // intentionally Big Endian

    return ByteString.of(bb.array());
  }

  @NonNull
  static String guidToString(@NonNull UUID uuid) {
    return uuid.toString();
  }

  @Nullable
  static Object convert(@NonNull Object o, OpcUaDataType targetType, boolean implicit) {
    if (o instanceof UUID uuid) {
      return implicit ? implicitConversion(uuid, targetType) : explicitConversion(uuid, targetType);
    } else {
      return null;
    }
  }

  @Nullable
  static Object explicitConversion(@NonNull UUID uuid, OpcUaDataType targetType) {
    return switch (targetType) {
      case ByteString -> guidToByteString(uuid);
      case String -> guidToString(uuid);
      default -> implicitConversion(uuid, targetType);
    };
  }

  @Nullable
  static Object implicitConversion(@NonNull UUID uuid, OpcUaDataType targetType) {
    // no implicit conversions exist
    return null;
  }
}
