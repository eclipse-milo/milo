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

public class TcpMessageDecoder {

  public static HelloMessage decodeHello(ByteBuf buffer) throws UaException {
    MessageType messageType = MessageType.fromMediumInt(buffer.readMediumLE());
    char chunkType = (char) buffer.readByte();
    buffer.skipBytes(4); // length

    if (messageType != MessageType.Hello || chunkType != 'F') {
      throw new UaException(
          StatusCodes.Bad_TcpMessageTypeInvalid,
          "unexpected MessageType/chunkType: " + messageType + "/" + chunkType);
    }

    return HelloMessage.decode(buffer);
  }

  public static AcknowledgeMessage decodeAcknowledge(ByteBuf buffer) throws UaException {
    MessageType messageType = MessageType.fromMediumInt(buffer.readMediumLE());
    char chunkType = (char) buffer.readByte();
    buffer.skipBytes(4); // length

    if (messageType != MessageType.Acknowledge || chunkType != 'F') {
      throw new UaException(
          StatusCodes.Bad_TcpMessageTypeInvalid,
          "unexpected MessageType/chunkType: " + messageType + "/" + chunkType);
    }

    return AcknowledgeMessage.decode(buffer);
  }

  public static ErrorMessage decodeError(ByteBuf buffer) throws UaException {
    MessageType messageType = MessageType.fromMediumInt(buffer.readMediumLE());
    char chunkType = (char) buffer.readByte();
    buffer.skipBytes(4); // length

    if (messageType != MessageType.Error || chunkType != 'F') {
      throw new UaException(
          StatusCodes.Bad_TcpMessageTypeInvalid,
          "unexpected MessageType/chunkType: " + messageType + "/" + chunkType);
    }

    return ErrorMessage.decode(buffer);
  }

  public static ReverseHelloMessage decodeReverseHello(ByteBuf buffer) throws UaException {
    MessageType messageType = MessageType.fromMediumInt(buffer.readMediumLE());
    char chunkType = (char) buffer.readByte();
    buffer.skipBytes(4); // length

    if (messageType != MessageType.ReverseHello || chunkType != 'F') {
      throw new UaException(
          StatusCodes.Bad_TcpMessageTypeInvalid,
          "unexpected MessageType/chunkType: " + messageType + "/" + chunkType);
    }

    return ReverseHelloMessage.decode(buffer);
  }
}
