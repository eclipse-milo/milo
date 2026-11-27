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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBufUtil;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.junit.jupiter.api.Test;

public class ByteStringConversionsTest {

  private static final UUID TEST_GUID = new UUID(0x0011223344556677L, 0x8899AABBCCDDEEFFL);

  private static final byte[] TEST_GUID_WIRE_BYTES = {
    0x33, 0x22, 0x11, 0x00,
    0x55, 0x44,
    0x77, 0x66,
    (byte) 0x88, (byte) 0x99, (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, (byte) 0xEE,
    (byte) 0xFF
  };

  @Test
  public void testByteStringToGuid() {
    ByteString bs = ByteString.of(TEST_GUID_WIRE_BYTES);

    assertEquals(TEST_GUID, ByteStringConversions.byteStringToGuid(bs));
  }

  /** The bytes the binary encoder writes for a GUID must decode to that same GUID. */
  @Test
  public void testByteStringToGuidReadsTheBinaryEncoding() {
    ByteBuf buffer = Unpooled.buffer();
    new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE)
        .setBuffer(buffer)
        .encodeGuid(TEST_GUID);
    byte[] encoded = ByteBufUtil.getBytes(buffer);

    assertEquals(TEST_GUID, ByteStringConversions.byteStringToGuid(ByteString.of(encoded)));
  }

  @Test
  public void testByteStringGuidRoundTrip() {
    ByteString wireBytes = ByteString.of(TEST_GUID_WIRE_BYTES);

    UUID guid = ByteStringConversions.byteStringToGuid(wireBytes);
    ByteString convertedBack = GuidConversions.guidToByteString(guid);

    assertEquals(wireBytes, convertedBack);
  }

  @Test
  public void testByteStringToGuidRejectsWrongLength() {
    assertNull(ByteStringConversions.byteStringToGuid(ByteString.of(new byte[15])));
    assertNull(ByteStringConversions.byteStringToGuid(ByteString.of(new byte[17])));
  }

  @Test
  public void testByteStringToString() {
    ByteString bs = ByteString.of(new byte[] {0x01, 0x02, 0x03});

    assertEquals(
        ByteBufUtil.hexDump(bs.bytesOrEmpty()), ByteStringConversions.byteStringToString(bs));
  }
}
