/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.binary;

import static org.testng.Assert.assertThrows;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.testng.annotations.Test;

public class OpcUaBinaryDecoderTest {

  @Test
  public void testReadDiagnosticInfoStackOverflow() {
    ByteBuf buffer = Unpooled.buffer();

    for (int i = 0; i < 10000; i++) {
      buffer.writeByte(0x40);
    }
    buffer.writeByte(0x00);

    assertThrows(
        UaSerializationException.class,
        () ->
            new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
                .setBuffer(buffer)
                .decodeDiagnosticInfo());
  }

  @Test
  public void testReadVariantStackOverflow() {
    ByteBuf buffer = Unpooled.buffer();

    for (int i = 0; i < 10000; i++) {
      buffer.writerIndex(5 * i);

      buffer.writeByte(24 | 0x80);
      buffer.writeByte(1);
      buffer.writeByte(0);
      buffer.writeByte(0);
      buffer.writeByte(0);
    }
    buffer.writeByte(0);

    assertThrows(
        UaSerializationException.class,
        () ->
            new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
                .setBuffer(buffer)
                .decodeVariant());
  }

  @Test
  public void testReadVariantStackOverflow2() {
    ByteBuf buffer = Unpooled.buffer();

    for (int i = 0; i < 10000; i++) {
      buffer.writerIndex(2 * i);

      buffer.writeByte(23);
      buffer.writeByte(1);
    }
    buffer.writeByte(0);

    assertThrows(
        UaSerializationException.class,
        () ->
            new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE)
                .setBuffer(buffer)
                .decodeVariant());
  }
}
