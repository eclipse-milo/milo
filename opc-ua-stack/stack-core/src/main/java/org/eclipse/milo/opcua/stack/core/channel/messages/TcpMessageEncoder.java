/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel.messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.UaException;

public class TcpMessageEncoder {

  public static ByteBuf encode(HelloMessage helloMessage) throws UaException {
    return encode(
        MessageType.Hello, (b) -> HelloMessage.encode(helloMessage, b), Unpooled.buffer());
  }

  public static ByteBuf encode(AcknowledgeMessage acknowledgeMessage) throws UaException {
    return encode(
        MessageType.Acknowledge,
        b -> AcknowledgeMessage.encode(acknowledgeMessage, b),
        Unpooled.buffer());
  }

  public static ByteBuf encode(ErrorMessage errorMessage) throws UaException {
    return encode(
        MessageType.Error, (b) -> ErrorMessage.encode(errorMessage, b), Unpooled.buffer());
  }

  /**
   * Encode a simple UA TCP message.
   *
   * @param messageType {@link MessageType#Hello}, {@link MessageType#Acknowledge}, or {@link
   *     MessageType#Error}.
   * @param messageEncoder a function that encodes the message payload.
   * @param buffer the {@link ByteBuf} to encode into.
   */
  private static ByteBuf encode(
      MessageType messageType, Consumer<ByteBuf> messageEncoder, ByteBuf buffer)
      throws UaException {

    buffer.writeMediumLE(MessageType.toMediumInt(messageType));
    buffer.writeByte('F');

    int lengthIndex = buffer.writerIndex();
    buffer.writeIntLE(0);

    int indexBefore = buffer.writerIndex();
    messageEncoder.accept(buffer);
    int indexAfter = buffer.writerIndex();
    int bytesWritten = indexAfter - indexBefore;

    buffer.writerIndex(lengthIndex);
    buffer.writeIntLE(8 + bytesWritten);
    buffer.writerIndex(indexAfter);

    return buffer;
  }
}
