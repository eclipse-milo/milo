/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.uasc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty channel handler for server-initiated Reverse Connect channels.
 *
 * <p>On {@link #channelActive(ChannelHandlerContext)}, sends a {@code ReverseHello} message
 * containing the server's URI and endpoint URL, then waits for the client's {@code Hello} — the
 * inverse of {@link UascServerHelloHandler}, which waits for {@code Hello} on channel activation.
 *
 * <p>After the {@code Hello}/{@code Ack} exchange, the pipeline evolves identically to a normal
 * connection.
 */
public class UascServerReverseHelloHandler extends UascServerHelloHandler {

  private static final int MAX_REVERSE_HANDSHAKE_MESSAGE_SIZE = 8 + 20 + 4 + 4096;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final String serverUri;
  private final String endpointUrl;
  private final Consumer<Throwable> reverseHelloWriteFailureListener;
  private final Consumer<ErrorMessage> clientRejectedListener;

  /**
   * @param config the server configuration.
   * @param application the server application context.
   * @param transportProfile the transport profile for this connection.
   * @param serverUri the ApplicationUri of the server.
   * @param endpointUrl the endpoint URL the client should use in its Hello message.
   */
  public UascServerReverseHelloHandler(
      UascServerConfig config,
      ServerApplicationContext application,
      TransportProfile transportProfile,
      String serverUri,
      String endpointUrl) {

    this(
        config,
        application,
        transportProfile,
        serverUri,
        endpointUrl,
        failure -> {},
        errorMessage -> {});
  }

  /**
   * @param config the server configuration.
   * @param application the server application context.
   * @param transportProfile the transport profile for this connection.
   * @param serverUri the ApplicationUri of the server.
   * @param endpointUrl the endpoint URL the client should use in its Hello message.
   * @param reverseHelloWriteFailureListener notified if the outbound ReverseHello write fails.
   * @param clientRejectedListener notified when the client sends an OPC UA {@code Error} while the
   *     reverse handshake is waiting for {@code Hello}.
   */
  public UascServerReverseHelloHandler(
      UascServerConfig config,
      ServerApplicationContext application,
      TransportProfile transportProfile,
      String serverUri,
      String endpointUrl,
      Consumer<Throwable> reverseHelloWriteFailureListener,
      Consumer<ErrorMessage> clientRejectedListener) {

    super(config, application, transportProfile);
    this.serverUri = serverUri;
    this.endpointUrl = endpointUrl;
    this.reverseHelloWriteFailureListener =
        Objects.requireNonNull(reverseHelloWriteFailureListener);
    this.clientRejectedListener = Objects.requireNonNull(clientRejectedListener);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    var rhe = new ReverseHelloMessage(serverUri, endpointUrl);
    ByteBuf rheBuffer = TcpMessageEncoder.encode(rhe);
    ctx.writeAndFlush(rheBuffer)
        .addListener(
            future -> {
              if (!future.isSuccess()) {
                Throwable cause =
                    future.cause() != null
                        ? future.cause()
                        : new IllegalStateException("ReverseHello write failed");

                logger.error(
                    "[remote={}] Error writing ReverseHello: {}",
                    ctx.channel().remoteAddress(),
                    cause.getMessage(),
                    cause);

                notifyReverseHelloWriteFailure(cause);
                ctx.close();
                ctx.fireExceptionCaught(cause);
              }
            });

    // Start the Hello deadline timer (inherited behavior).
    // super.channelActive() schedules the "no Hello received" deadline.
    super.channelActive(ctx);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out)
      throws Exception {
    if (buffer.readableBytes() < HEADER_LENGTH) {
      return;
    }

    int messageLength = getMessageLength(buffer, MAX_REVERSE_HANDSHAKE_MESSAGE_SIZE);

    if (buffer.readableBytes() < messageLength) {
      return;
    }

    MessageType messageType = MessageType.fromMediumInt(buffer.getMediumLE(buffer.readerIndex()));

    if (messageType == MessageType.Hello) {
      onHello(ctx, buffer.readSlice(messageLength));
    } else if (messageType == MessageType.Error) {
      onError(ctx, buffer.readSlice(messageLength));
    } else {
      throw new UaException(
          StatusCodes.Bad_TcpMessageTypeInvalid, "unexpected MessageType: " + messageType);
    }
  }

  private void onError(ChannelHandlerContext ctx, ByteBuf buffer) throws UaException {
    ErrorMessage errorMessage = TcpMessageDecoder.decodeError(buffer);

    logger.debug(
        "[remote={}] Received Error during ReverseHello handshake: {}",
        ctx.channel().remoteAddress(),
        errorMessage);

    notifyClientRejected(errorMessage);
    ctx.close();
  }

  private void notifyReverseHelloWriteFailure(Throwable cause) {
    try {
      reverseHelloWriteFailureListener.accept(cause);
    } catch (Throwable t) {
      logger.warn("ReverseHello write failure listener threw.", t);
    }
  }

  private void notifyClientRejected(ErrorMessage errorMessage) {
    try {
      clientRejectedListener.accept(errorMessage);
    } catch (Throwable t) {
      logger.warn("Reverse Connect rejection listener threw.", t);
    }
  }
}
