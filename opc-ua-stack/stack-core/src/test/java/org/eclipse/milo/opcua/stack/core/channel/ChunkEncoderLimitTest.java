/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.junit.jupiter.api.Test;

class ChunkEncoderLimitTest {
  private static final ChannelParameters PARAMETERS =
      new ChannelParameters(0, 8192, 8192, 0, 0, 8192, 8192, 1);

  // SecurityPolicy.None has a 12-byte message header, 4-byte security header and 8-byte sequence
  // header. The exact payload boundary must be accepted; one byte more must fail before encoding.
  @Test
  void chunkCountLimitIsCheckedBeforeConsumingTheMessageOrSequence() throws Exception {
    var channel = new ServerSecureChannel();
    channel.setSecurityPolicy(SecurityPolicy.None);
    channel.setMessageSecurityMode(MessageSecurityMode.None);
    var encoder = new ChunkEncoder(PARAMETERS);
    var decoder = new ChunkDecoder(PARAMETERS, EncodingLimits.DEFAULT);
    ByteBuf exact = Unpooled.buffer().writeZero(8192 - 24);
    ByteBuf oversized = Unpooled.buffer().writeZero(8192 - 24 + 1);
    ByteBuf following = Unpooled.buffer().writeIntLE(42);
    try {
      List<ByteBuf> chunks =
          encoder.encodeSymmetric(channel, 1, exact, MessageType.SecureMessage).getMessageChunks();
      assertEquals(1, chunks.size());
      decoder.decodeSymmetric(channel, chunks).getMessage().release();
      int originalIndex = oversized.readerIndex();
      MessageEncodeException failure =
          assertThrows(
              MessageEncodeException.class,
              () -> encoder.encodeSymmetric(channel, 2, oversized, MessageType.SecureMessage));
      assertEquals(
          StatusCodes.Bad_EncodingLimitsExceeded,
          ((UaException) failure.getCause()).getStatusCode().value());
      assertEquals(
          originalIndex, oversized.readerIndex(), "a rejected message must not be partly encoded");
      var decoded =
          decoder.decodeSymmetric(
              channel,
              encoder
                  .encodeSymmetric(channel, 3, following, MessageType.SecureMessage)
                  .getMessageChunks());
      try {
        assertEquals(42, decoded.getMessage().readIntLE());
      } finally {
        decoded.getMessage().release();
      }
    } finally {
      exact.release();
      oversized.release();
      following.release();
    }
  }
}
