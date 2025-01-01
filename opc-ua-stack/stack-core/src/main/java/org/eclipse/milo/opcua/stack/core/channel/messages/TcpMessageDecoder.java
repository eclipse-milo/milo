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
import org.eclipse.milo.opcua.stack.core.UaException;

public class TcpMessageDecoder {

  public static HelloMessage decodeHello(ByteBuf buffer) throws UaException {
    MessageType messageType = MessageType.fromMediumInt(buffer.readMediumLE());
    char chunkType = (char) buffer.readByte();
    buffer.skipBytes(4); // length

    assert (messageType == MessageType.Hello && chunkType == 'F');

    return HelloMessage.decode(buffer);
  }

  public static AcknowledgeMessage decodeAcknowledge(ByteBuf buffer) throws UaException {
    MessageType messageType = MessageType.fromMediumInt(buffer.readMediumLE());
    char chunkType = (char) buffer.readByte();
    buffer.skipBytes(4); // length

    assert (messageType == MessageType.Acknowledge && chunkType == 'F');

    return AcknowledgeMessage.decode(buffer);
  }

  public static ErrorMessage decodeError(ByteBuf buffer) throws UaException {
    MessageType messageType = MessageType.fromMediumInt(buffer.readMediumLE());
    char chunkType = (char) buffer.readByte();
    buffer.skipBytes(4); // length

    assert (messageType == MessageType.Error && chunkType == 'F');

    return ErrorMessage.decode(buffer);
  }
}
