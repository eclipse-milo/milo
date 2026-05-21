/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.channel.headers.HeaderDecoder;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.transport.server.uasc.UascServerHelloHandler;

/**
 * Handles the first client response after a server-initiated {@code ReverseHello}.
 *
 * <p>{@code ERR/F} is a reverse-connect attempt failure. {@code HEL/F} is retained, this handler is
 * removed, and the frame is forwarded into the normal server {@code Hello} handler.
 */
final class OpcTcpServerReverseConnectResponseHandler extends ByteToMessageDecoder
    implements HeaderDecoder {

  private final OpcTcpServerReverseConnector connector;
  private final OpcTcpServerReverseConnectAttempt attempt;

  OpcTcpServerReverseConnectResponseHandler(
      OpcTcpServerReverseConnector connector, OpcTcpServerReverseConnectAttempt attempt) {

    this.connector = connector;
    this.attempt = attempt;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out)
      throws Exception {

    if (buffer.readableBytes() < HEADER_LENGTH) {
      return;
    }

    MessageType messageType = MessageType.fromMediumInt(buffer.getMediumLE(buffer.readerIndex()));
    char chunkType = (char) buffer.getByte(buffer.readerIndex() + 3);
    int maxMessageLength =
        messageType == MessageType.Hello
            ? UascServerHelloHandler.MAX_HELLO_MESSAGE_SIZE
            : EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE;

    int messageLength = getMessageLength(buffer, maxMessageLength);
    if (messageLength < HEADER_LENGTH) {
      throw new UaException(
          StatusCodes.Bad_DecodingError,
          "invalid message length after ReverseHello: " + messageLength);
    }
    if (buffer.readableBytes() < messageLength) {
      return;
    }

    if (chunkType != 'F') {
      throw new UaException(
          StatusCodes.Bad_TcpMessageTypeInvalid,
          "expected final chunk after ReverseHello, received " + messageType + "/" + chunkType);
    }

    if (messageType == MessageType.Error) {
      ErrorMessage errorMessage = TcpMessageDecoder.decodeError(buffer.readSlice(messageLength));

      connector.onClientError(attempt, ctx, errorMessage);
    } else if (messageType == MessageType.Hello) {
      ByteBuf helloBuffer = buffer.readRetainedSlice(messageLength);

      ctx.pipeline().remove(this);
      connector.onHelloReceived(attempt, ctx.channel());
      ctx.fireChannelRead(helloBuffer);
    } else {
      throw new UaException(
          StatusCodes.Bad_TcpMessageTypeInvalid,
          "expected Hello or Error after ReverseHello, received " + messageType + "/" + chunkType);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    connector.onFirstMessageInvalid(attempt, ctx, cause);
  }
}
