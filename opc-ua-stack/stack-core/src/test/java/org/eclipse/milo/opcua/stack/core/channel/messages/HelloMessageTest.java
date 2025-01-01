/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel.messages;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.junit.jupiter.api.Test;

public class HelloMessageTest {

  @Test
  public void testDecodeFailsWhenEndpointUrlTooLarge() throws UaException {
    for (int i = 0; i < HelloMessage.MAX_ENDPOINT_URL_LENGTH * 2; i++) {
      ByteBuf buffer = Unpooled.buffer();
      buffer.writeIntLE(0);
      buffer.writeIntLE(8192);
      buffer.writeIntLE(8192);
      buffer.writeIntLE(0);
      buffer.writeIntLE(0);
      buffer.writeIntLE(i);
      byte[] bs = new byte[i];
      buffer.writeBytes(bs);

      if (i <= HelloMessage.MAX_ENDPOINT_URL_LENGTH) {
        assertNotNull(HelloMessage.decode(buffer));
      } else {
        assertThrows(
            UaException.class,
            () -> {
              HelloMessage.decode(buffer);
            });
      }
      buffer.release();
    }
  }
}
