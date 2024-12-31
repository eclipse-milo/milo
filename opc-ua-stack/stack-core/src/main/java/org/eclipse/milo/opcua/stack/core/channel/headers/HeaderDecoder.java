/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel.headers;

import io.netty.buffer.ByteBuf;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;

public interface HeaderDecoder {

  long PROTOCOL_VERSION = 0L;

  int HEADER_LENGTH = 8;
  int HEADER_LENGTH_INDEX = 4;

  /**
   * Get the message length from a {@link ByteBuf} containing a {@link TcpMessageEncoder}. The
   * reader index will not be advanced.
   *
   * @param buffer {@link ByteBuf} to extract from.
   * @return The message length, which includes the size of the header.
   */
  default int getMessageLength(ByteBuf buffer, int maxMessageLength) throws UaException {
    long messageLength = buffer.getUnsignedIntLE(buffer.readerIndex() + HEADER_LENGTH_INDEX);

    if (messageLength <= maxMessageLength) {
      return (int) messageLength;
    } else {
      throw new UaException(
          StatusCodes.Bad_TcpMessageTooLarge,
          String.format("max message length exceeded (%s > %s)", messageLength, maxMessageLength));
    }
  }
}
