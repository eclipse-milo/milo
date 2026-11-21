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

import io.netty.buffer.ByteBufUtil;
import java.nio.ByteBuffer;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

final class ByteStringConversions {

  private ByteStringConversions() {}

  @Nullable
  static UUID byteStringToGuid(@NonNull ByteString bs) {
    if (bs.length() != 16) {
      return null;
    } else {
      // mirror the OPC UA Part 6 §5.1.3 binary encoding: Data1, Data2 and Data3
      // are little-endian, Data4 is a raw byte array. This matches what
      // OpcUaBinaryDecoder.decodeGuid reads for the same bytes.
      ByteBuffer byteBuffer = ByteBuffer.wrap(bs.bytesOrEmpty());
      int data1 = Integer.reverseBytes(byteBuffer.getInt());
      short data2 = Short.reverseBytes(byteBuffer.getShort());
      short data3 = Short.reverseBytes(byteBuffer.getShort());
      long data4 = byteBuffer.getLong(); // intentionally Big Endian

      long msb = ((long) data1 & 0xFFFFFFFFL) << 32 | (data2 & 0xFFFFL) << 16 | (data3 & 0xFFFFL);
      return new UUID(msb, data4);
    }
  }

  @NonNull
  static String byteStringToString(@NonNull ByteString bs) {
    return ByteBufUtil.hexDump(bs.bytesOrEmpty());
  }

  @Nullable
  static Object convert(@NonNull Object o, OpcUaDataType targetType, boolean implicit) {
    if (o instanceof ByteString bs) {
      return implicit ? implicitConversion(bs, targetType) : explicitConversion(bs, targetType);
    } else {
      return null;
    }
  }

  @Nullable
  static Object explicitConversion(@NonNull ByteString bs, OpcUaDataType targetType) {
    return switch (targetType) {
      case Guid -> byteStringToGuid(bs);
      case String -> byteStringToString(bs);
      default -> implicitConversion(bs, targetType);
    };
  }

  @Nullable
  static Object implicitConversion(@NonNull ByteString bs, OpcUaDataType targetType) {
    // no implicit conversions exist
    return null;
  }
}
