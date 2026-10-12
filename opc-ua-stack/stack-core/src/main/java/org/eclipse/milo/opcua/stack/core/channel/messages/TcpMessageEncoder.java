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

/**
 * Encoder for the simple UA-TCP messages that are framed outside SecureChannel chunks.
 *
 * <p>These helpers write the common eight-byte UACP header, always use final chunk {@code F}, and
 * delegate the body layout to the corresponding message value. Secure conversation messages use the
 * chunk encoder path instead of this class.
 */
public class TcpMessageEncoder {

  /**
   * Encode a {@link HelloMessage} with a UA-TCP {@code HEL/F} header.
   *
   * @param helloMessage the hello payload to encode.
   * @return a buffer containing the complete UA-TCP message.
   * @throws UaException if the message type cannot be encoded.
   */
  public static ByteBuf encode(HelloMessage helloMessage) throws UaException {
    return encode(
        MessageType.Hello, (b) -> HelloMessage.encode(helloMessage, b), Unpooled.buffer());
  }

  /**
   * Encode a {@link ReverseHelloMessage} with a UA-TCP {@code RHE/F} header.
   *
   * @param reverseHelloMessage the reverse hello payload to encode.
   * @return a buffer containing the complete UA-TCP message.
   * @throws UaException if the message type cannot be encoded.
   */
  public static ByteBuf encode(ReverseHelloMessage reverseHelloMessage) throws UaException {
    return encode(
        MessageType.ReverseHello,
        (b) -> ReverseHelloMessage.encode(reverseHelloMessage, b),
        Unpooled.buffer());
  }

  /**
   * Encode an {@link AcknowledgeMessage} with a UA-TCP {@code ACK/F} header.
   *
   * @param acknowledgeMessage the acknowledge payload to encode.
   * @return a buffer containing the complete UA-TCP message.
   * @throws UaException if the message type cannot be encoded.
   */
  public static ByteBuf encode(AcknowledgeMessage acknowledgeMessage) throws UaException {
    return encode(
        MessageType.Acknowledge,
        b -> AcknowledgeMessage.encode(acknowledgeMessage, b),
        Unpooled.buffer());
  }

  /**
   * Encode an {@link ErrorMessage} with a UA-TCP {@code ERR/F} header.
   *
   * @param errorMessage the error payload to encode.
   * @return a buffer containing the complete UA-TCP message.
   * @throws UaException if the message type cannot be encoded.
   */
  public static ByteBuf encode(ErrorMessage errorMessage) throws UaException {
    return encode(
        MessageType.Error, (b) -> ErrorMessage.encode(errorMessage, b), Unpooled.buffer());
  }

  /**
   * Encode a simple UA TCP message.
   *
   * @param messageType {@link MessageType#Hello}, {@link MessageType#ReverseHello}, {@link
   *     MessageType#Acknowledge}, or {@link MessageType#Error}.
   * @param messageEncoder a function that encodes the message payload.
   * @param buffer the {@link ByteBuf} to encode into.
   */
  private static ByteBuf encode(
      MessageType messageType, Consumer<ByteBuf> messageEncoder, ByteBuf buffer) {

    boolean success = false;
    try {
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

      success = true;
      return buffer;
    } finally {
      if (!success) {
        buffer.release();
      }
    }
  }
}
