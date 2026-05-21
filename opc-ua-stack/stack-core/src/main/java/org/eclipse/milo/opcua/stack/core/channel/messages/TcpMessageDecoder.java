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
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * Decoder for complete simple UA-TCP messages whose eight-byte UACP header is still present.
 *
 * <p>Callers choose the decode method after their transport pipeline has already determined which
 * simple message is expected in the current handshake state. The methods consume the common header
 * and then delegate payload decoding to the corresponding message value.
 */
public class TcpMessageDecoder {

  /**
   * Decode a complete UA-TCP {@code HEL/F} message.
   *
   * @param buffer the buffer positioned at the start of the UA-TCP header.
   * @return the decoded hello payload.
   * @throws UaException if the header type or payload cannot be decoded.
   */
  public static HelloMessage decodeHello(ByteBuf buffer) throws UaException {
    MessageType messageType = MessageType.fromMediumInt(buffer.readMediumLE());
    char chunkType = (char) buffer.readByte();
    buffer.skipBytes(4); // length

    assert (messageType == MessageType.Hello && chunkType == 'F');

    return HelloMessage.decode(buffer);
  }

  /**
   * Decode a complete UA-TCP {@code RHE/F} message.
   *
   * @param buffer the buffer positioned at the start of the UA-TCP header.
   * @return the decoded reverse hello payload.
   * @throws UaException if the header type or payload cannot be decoded.
   */
  public static ReverseHelloMessage decodeReverseHello(ByteBuf buffer) throws UaException {
    if (buffer.readableBytes() < 8) {
      throw new UaException(
          StatusCodes.Bad_DecodingError, "not enough bytes to read ReverseHello message header");
    }

    MessageType messageType = MessageType.fromMediumInt(buffer.readMediumLE());
    char chunkType = (char) buffer.readByte();
    buffer.skipBytes(4); // length

    if (messageType != MessageType.ReverseHello) {
      throw new UaException(
          StatusCodes.Bad_TcpMessageTypeInvalid,
          "expected ReverseHello message type, received " + messageType);
    }

    if (chunkType != 'F') {
      throw new UaException(
          StatusCodes.Bad_TcpMessageTypeInvalid,
          "expected final ReverseHello chunk, received " + chunkType);
    }

    return ReverseHelloMessage.decode(buffer);
  }

  /**
   * Decode a complete UA-TCP {@code ACK/F} message.
   *
   * @param buffer the buffer positioned at the start of the UA-TCP header.
   * @return the decoded acknowledge payload.
   * @throws UaException if the header type cannot be decoded.
   */
  public static AcknowledgeMessage decodeAcknowledge(ByteBuf buffer) throws UaException {
    MessageType messageType = MessageType.fromMediumInt(buffer.readMediumLE());
    char chunkType = (char) buffer.readByte();
    buffer.skipBytes(4); // length

    assert (messageType == MessageType.Acknowledge && chunkType == 'F');

    return AcknowledgeMessage.decode(buffer);
  }

  /**
   * Decode a complete UA-TCP {@code ERR/F} message.
   *
   * @param buffer the buffer positioned at the start of the UA-TCP header.
   * @return the decoded error payload.
   * @throws UaException if the header type or payload cannot be decoded.
   */
  public static ErrorMessage decodeError(ByteBuf buffer) throws UaException {
    MessageType messageType = MessageType.fromMediumInt(buffer.readMediumLE());
    char chunkType = (char) buffer.readByte();
    buffer.skipBytes(4); // length

    assert (messageType == MessageType.Error && chunkType == 'F');

    return ErrorMessage.decode(buffer);
  }
}
