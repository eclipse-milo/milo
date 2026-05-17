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

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.junit.jupiter.api.Test;

public class ErrorMessageTest {

  @Test
  public void testDecodeFailsWhenReasonLengthIsNegative() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(0);
    buffer.writeIntLE(-2);

    assertThrows(UaException.class, () -> ErrorMessage.decode(buffer));

    buffer.release();
  }

  @Test
  public void testDecodeFailsWhenReasonLengthExceedsEncodingLimit() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(0);
    buffer.writeIntLE(EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE + 1);

    assertThrows(UaException.class, () -> ErrorMessage.decode(buffer));

    buffer.release();
  }

  @Test
  public void testDecodeFailsWhenReasonLengthExceedsReadableBytes() {
    ByteBuf buffer = Unpooled.buffer();
    buffer.writeIntLE(0);
    buffer.writeIntLE(EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE);

    assertThrows(UaException.class, () -> ErrorMessage.decode(buffer));

    buffer.release();
  }
}
