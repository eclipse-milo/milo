/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.uasc;

import com.google.common.primitives.Ints;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.Timeout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.ChannelParameters;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.channel.messages.AcknowledgeMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.HelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UascClientAcknowledgeHandler extends ByteToMessageCodec<UaRequestMessageType> {

  private static final long PROTOCOL_VERSION = 0L;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final List<UaRequestMessageType> awaitingHandshake =
      Collections.synchronizedList(new ArrayList<>());

  private final AtomicBoolean helloSent = new AtomicBoolean(false);
  private Timeout helloTimeout;

  private final UascClientConfig config;
  private final ClientApplicationContext application;
  private final Supplier<Long> requestIdSupplier;
  private final CompletableFuture<ClientSecureChannel> handshakeFuture;

  public UascClientAcknowledgeHandler(
      UascClientConfig config,
      ClientApplicationContext application,
      Supplier<Long> requestIdSupplier,
      CompletableFuture<ClientSecureChannel> handshakeFuture) {

    this.config = config;
    this.application = application;
    this.requestIdSupplier = requestIdSupplier;
    this.handshakeFuture = handshakeFuture;
  }

  /*
   * Sending the Hello message can be triggered from two locations.
   *
   * When using TCP transport the handler is added to the pipeline during
   * initialization and Hello can't be sent until channelActive().
   *
   * When using WebSocket transport the channel is already activated by
   * the time this handler is added to the pipeline and Hello needs to be
   * sent in handlerAdded().
   *
   * We also check to see if the channel is active in handlerAdded() to ensure
   * the Hello isn't immediately sent when this handler is added to a TCP pipeline.
   */

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    if (helloSent.compareAndSet(false, true)) {
      sendHello(ctx);
    }

    super.channelActive(ctx);
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    if (ctx.channel().isActive() && helloSent.compareAndSet(false, true)) {
      sendHello(ctx);
    }

    super.handlerAdded(ctx);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    logger.error(
        "[remote={}] Exception caught: {}",
        ctx.channel().remoteAddress(),
        cause.getMessage(),
        cause);

    // If the handshake hasn't completed yet this cause will be more
    // accurate than the generic "connection closed" exception that
    // channelInactive() will use.
    handshakeFuture.completeExceptionally(cause);

    ctx.close();
  }

  private void sendHello(ChannelHandlerContext ctx) throws UaException {
    helloTimeout = startHelloTimeout(ctx);

    String endpointUrl = application.getEndpoint().getEndpointUrl();

    EncodingLimits encodingLimits = application.getEncodingContext().getEncodingLimits();

    var hello =
        new HelloMessage(
            PROTOCOL_VERSION,
            encodingLimits.getMaxChunkSize(),
            encodingLimits.getMaxChunkSize(),
            encodingLimits.getMaxMessageSize(),
            encodingLimits.getMaxChunkCount(),
            endpointUrl);

    ByteBuf messageBuffer = TcpMessageEncoder.encode(hello);

    ctx.writeAndFlush(messageBuffer, ctx.voidPromise());

    logger.debug("Sent Hello message on channel={}.", ctx.channel());
  }

  private Timeout startHelloTimeout(ChannelHandlerContext ctx) {
    long acknowledgeTimeoutMs = config.getAcknowledgeTimeout().longValue();

    return config
        .getWheelTimer()
        .newTimeout(
            timeout -> {
              if (!timeout.isCancelled()) {
                handshakeFuture.completeExceptionally(
                    new UaException(StatusCodes.Bad_Timeout, "timed out waiting for acknowledge"));
                ctx.close();
              }
            },
            acknowledgeTimeoutMs,
            TimeUnit.MILLISECONDS);
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, UaRequestMessageType message, ByteBuf byteBuf)
      throws Exception {
    awaitingHandshake.add(message);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> list)
      throws Exception {
    int maxChunkSize = application.getEncodingContext().getEncodingLimits().getMaxChunkSize();

    if (buffer.readableBytes() >= 8) {
      int messageLength = getMessageLength(buffer, maxChunkSize);

      if (buffer.readableBytes() >= messageLength) {
        MessageType messageType =
            MessageType.fromMediumInt(buffer.getMediumLE(buffer.readerIndex()));

        switch (messageType) {
          case Acknowledge:
            onAcknowledge(ctx, buffer.readSlice(messageLength));
            break;

          case Error:
            onError(ctx, buffer.readSlice(messageLength));
            break;

          default:
            ctx.fireChannelRead(buffer.readRetainedSlice(messageLength));
        }
      }
    }
  }

  private void onAcknowledge(ChannelHandlerContext ctx, ByteBuf buffer) {
    if (helloTimeout != null && !helloTimeout.cancel()) {
      helloTimeout = null;
      handshakeFuture.completeExceptionally(
          new UaException(StatusCodes.Bad_Timeout, "timed out waiting for acknowledge"));
      ctx.close();
      return;
    }

    logger.debug("Received Acknowledge message on channel={}.", ctx.channel());

    buffer.skipBytes(3 + 1 + 4); // Skip messageType, chunkType, and messageSize

    AcknowledgeMessage acknowledge = AcknowledgeMessage.decode(buffer);

    long remoteProtocolVersion = acknowledge.getProtocolVersion();
    long remoteReceiveBufferSize = acknowledge.getReceiveBufferSize();
    long remoteSendBufferSize = acknowledge.getSendBufferSize();
    long remoteMaxMessageSize = acknowledge.getMaxMessageSize();
    long remoteMaxChunkCount = acknowledge.getMaxChunkCount();

    if (PROTOCOL_VERSION > remoteProtocolVersion) {
      logger.warn(
          "Client protocol version ({}) does not match server protocol version ({}).",
          PROTOCOL_VERSION,
          remoteProtocolVersion);
    }

    EncodingLimits encodingLimits = application.getEncodingContext().getEncodingLimits();

    /* Our receive buffer size is determined by the remote send buffer size. */
    long localReceiveBufferSize = Math.min(remoteSendBufferSize, encodingLimits.getMaxChunkSize());

    /* Our send buffer size is determined by the remote receive buffer size. */
    long localSendBufferSize = Math.min(remoteReceiveBufferSize, encodingLimits.getMaxChunkSize());

    /* Max message size the remote can send us; not influenced by remote configuration. */
    long localMaxMessageSize = encodingLimits.getMaxMessageSize();

    /* Max chunk count the remote can send us; not influenced by remote configuration. */
    long localMaxChunkCount = encodingLimits.getMaxChunkCount();

    var channelParameters =
        new ChannelParameters(
            Ints.saturatedCast(localMaxMessageSize),
            Ints.saturatedCast(localReceiveBufferSize),
            Ints.saturatedCast(localSendBufferSize),
            Ints.saturatedCast(localMaxChunkCount),
            Ints.saturatedCast(remoteMaxMessageSize),
            Ints.saturatedCast(remoteReceiveBufferSize),
            Ints.saturatedCast(remoteSendBufferSize),
            Ints.saturatedCast(remoteMaxChunkCount));

    ctx.executor()
        .execute(
            () -> {
              var messageHandler =
                  new UascClientMessageHandler(
                      config,
                      application,
                      requestIdSupplier,
                      handshakeFuture,
                      awaitingHandshake,
                      channelParameters);

              ctx.pipeline().addFirst(messageHandler);
            });
  }

  private void onError(ChannelHandlerContext ctx, ByteBuf buffer) {
    try {
      ErrorMessage errorMessage = TcpMessageDecoder.decodeError(buffer);
      StatusCode statusCode = errorMessage.getError();

      logger.error(
          "[remote={}] received error message: {}", ctx.channel().remoteAddress(), errorMessage);

      handshakeFuture.completeExceptionally(new UaException(statusCode, errorMessage.getReason()));

      ctx.fireUserEventTriggered(errorMessage);
    } catch (UaException e) {
      logger.error(
          "[remote={}] an exception occurred while decoding an error message: {}",
          ctx.channel().remoteAddress(),
          e.getMessage(),
          e);

      handshakeFuture.completeExceptionally(e);
    } finally {
      ctx.close();
    }
  }

  private static int getMessageLength(ByteBuf buffer, int maxMessageLength) throws UaException {
    long messageLength = buffer.getUnsignedIntLE(buffer.readerIndex() + 4);

    if (messageLength <= maxMessageLength) {
      return (int) messageLength;
    } else {
      throw new UaException(
          StatusCodes.Bad_TcpMessageTooLarge,
          String.format("max message length exceeded (%s > %s)", messageLength, maxMessageLength));
    }
  }
}
