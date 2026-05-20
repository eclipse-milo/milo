/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class MessageTypeTest {

  @ParameterizedTest
  @EnumSource(MessageType.class)
  public void testMessageTypeRoundTrip(MessageType messageType) throws UaException {
    assertEquals(messageType, MessageType.fromMediumInt(MessageType.toMediumInt(messageType)));
  }

  @Test
  public void testReverseHelloMapsToRhe() throws UaException {
    ByteBuf buffer = Unpooled.buffer(3);

    try {
      buffer.writeMediumLE(MessageType.toMediumInt(MessageType.ReverseHello));

      assertEquals((byte) 'R', buffer.getByte(0));
      assertEquals((byte) 'H', buffer.getByte(1));
      assertEquals((byte) 'E', buffer.getByte(2));
      assertEquals(MessageType.ReverseHello, MessageType.fromMediumInt(buffer.getMediumLE(0)));
    } finally {
      buffer.release();
    }
  }
}
