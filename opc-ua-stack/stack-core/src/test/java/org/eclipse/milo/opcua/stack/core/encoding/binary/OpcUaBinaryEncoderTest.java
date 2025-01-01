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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.testng.Assert.assertEquals;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class OpcUaBinaryEncoderTest {

  ByteBuf buffer;
  OpcUaBinaryEncoder writer;

  @BeforeTest
  public void initializeTest() {
    buffer = Unpooled.buffer();
    writer = new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
  }

  @Test
  public void testWriteBit() throws Exception {
    {
      writer.encodeBit(1);
      writer.encodeBit(1);
      writer.encodeBit(1);
      writer.encodeBit(1);
      writer.encodeBit(0);
      writer.encodeBit(0);
      writer.encodeBit(0);
      writer.encodeBit(0);
      assertEquals(buffer.readUnsignedByte(), 0b00001111);
    }
    {
      writer.encodeBit(0);
      writer.encodeBit(0);
      writer.encodeBit(0);
      writer.encodeBit(0);
      writer.encodeBit(1);
      writer.encodeBit(1);
      writer.encodeBit(1);
      writer.encodeBit(1);
      assertEquals(buffer.readUnsignedByte(), 0b11110000);
    }
    {
      writer.encodeBit(0);
      writer.encodeBit(1);
      writer.encodeBit(0);
      writer.encodeBit(1);
      writer.encodeBit(0);
      writer.encodeBit(1);
      writer.encodeBit(0);
      writer.encodeBit(1);
      assertEquals(buffer.readUnsignedByte(), 0b10101010);
    }
    {
      writer.encodeBit(1);
      writer.encodeBit(0);
      writer.encodeBit(1);
      writer.encodeBit(0);
      writer.encodeBit(1);
      writer.encodeBit(0);
      writer.encodeBit(1);
      writer.encodeBit(0);
      assertEquals(buffer.readUnsignedByte(), 0b01010101);
    }
  }

  @Test
  public void testWriteOptionSet() {
    AccessLevelType accessLevelType =
        AccessLevelType.of(AccessLevelType.Field.CurrentRead, AccessLevelType.Field.CurrentWrite);

    writer.encodeVariant(new Variant(accessLevelType));

    assertEquals(ubyte(buffer.readUnsignedByte()), accessLevelType.getValue());
  }
}
