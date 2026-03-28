/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.ChannelParameters;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.channel.ExceptionHandler;
import org.eclipse.milo.opcua.stack.core.channel.headers.HeaderDecoder;
import org.eclipse.milo.opcua.stack.core.channel.messages.AcknowledgeMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.HelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty handler for the client side of the OPC UA Reverse Connect handshake.
 *
 * <p>Installed on a child channel accepted by the client's {@code ServerBootstrap}. Handles the
 * initial ReverseHello exchange before delegating to the standard {@link UascClientMessageHandler}.
 *
 * <p>State machine:
 *
 * <pre>
 * AWAITING_RHE →(RHE received)→ AWAITING_ACK →(ACK received)→ HANDSHAKE_COMPLETE
 * </pre>
 */
public class UascClientReverseHelloHandler extends ByteToMessageCodec<UaRequestMessageType>
    implements HeaderDecoder {

  private enum State {
    AWAITING_RHE,
    AWAITING_ACK,
    HANDSHAKE_COMPLETE
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final List<UaRequestMessageType> awaitingHandshake =
      Collections.synchronizedList(new ArrayList<>());

  private final AtomicBoolean timeoutStarted = new AtomicBoolean(false);

  private volatile State state = State.AWAITING_RHE;
  private volatile Timeout timeout;

  private final UascClientConfig config;
  private final ClientApplicationContext application;
  private final Supplier<Long> requestIdSupplier;
  private final CompletableFuture<ClientSecureChannel> handshakeFuture;
  private final Set<String> allowedServerUris;
  private final long reverseHelloTimeoutMs;

  public UascClientReverseHelloHandler(
      UascClientConfig config,
      ClientApplicationContext application,
      Supplier<Long> requestIdSupplier,
      CompletableFuture<ClientSecureChannel> handshakeFuture,
      Set<String> allowedServerUris,
      long reverseHelloTimeoutMs) {

    this.config = config;
    this.application = application;
    this.requestIdSupplier = requestIdSupplier;
    this.handshakeFuture = handshakeFuture;
    this.allowedServerUris = allowedServerUris;
    this.reverseHelloTimeoutMs = reverseHelloTimeoutMs;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    if (reverseHelloTimeoutMs > 0 && timeoutStarted.compareAndSet(false, true)) {
      timeout = startReverseHelloTimeout(ctx);
    }
    super.channelActive(ctx);
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    if (reverseHelloTimeoutMs > 0
        && ctx.channel().isActive()
        && timeoutStarted.compareAndSet(false, true)) {
      timeout = startReverseHelloTimeout(ctx);
    }
    super.handlerAdded(ctx);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out)
      throws Exception {

    int maxChunkSize = application.getEncodingContext().getEncodingLimits().getMaxChunkSize();

    if (buffer.readableBytes() >= HEADER_LENGTH) {
      int messageLength = getMessageLength(buffer, maxChunkSize);

      if (buffer.readableBytes() >= messageLength) {
        MessageType messageType =
            MessageType.fromMediumInt(buffer.getMediumLE(buffer.readerIndex()));

        switch (state) {
          case AWAITING_RHE -> {
            switch (messageType) {
              case ReverseHello -> {
                ReverseHelloMessage rhe =
                    TcpMessageDecoder.decodeReverseHello(buffer.readSlice(messageLength));
                onReverseHello(ctx, rhe);
              }
              case Error -> onError(ctx, buffer.readSlice(messageLength));
              default -> ctx.fireChannelRead(buffer.readRetainedSlice(messageLength));
            }
          }
          case AWAITING_ACK -> {
            switch (messageType) {
              case Acknowledge -> onAcknowledge(ctx, buffer.readSlice(messageLength));
              case Error -> onError(ctx, buffer.readSlice(messageLength));
              default -> ctx.fireChannelRead(buffer.readRetainedSlice(messageLength));
            }
          }
          default -> ctx.fireChannelRead(buffer.readRetainedSlice(messageLength));
        }
      }
    }
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, UaRequestMessageType message, ByteBuf byteBuf) {

    awaitingHandshake.add(message);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    logger.error(
        "[remote={}] exceptionCaught: {}",
        ctx.channel().remoteAddress(),
        cause.getMessage(),
        cause);

    if (timeout != null) {
      timeout.cancel();
      timeout = null;
    }
    handshakeFuture.completeExceptionally(cause);
    ctx.close();
  }

  /**
   * Process a pre-decoded {@link ReverseHelloMessage}. Called by the FSM when the ReverseHello was
   * already decoded by the {@code ReverseHelloDecoder} on the accepted channel, avoiding the need
   * to re-encode and re-decode the message through the pipeline.
   *
   * <p>Must be called on the channel's event loop after this handler has been added to the
   * pipeline.
   *
   * @param ctx this handler's {@link ChannelHandlerContext}.
   * @param reverseHello the decoded ReverseHello message.
   * @throws Exception if sending the Hello message fails.
   */
  public void onReverseHello(ChannelHandlerContext ctx, ReverseHelloMessage reverseHello)
      throws Exception {

    if (timeout != null) {
      timeout.cancel();
    }

    logger.debug("Received ReverseHello: {}", reverseHello);

    String serverUri = reverseHello.serverUri();

    if (!allowedServerUris.isEmpty() && !allowedServerUris.contains(serverUri)) {
      String message = String.format("ServerUri not allowed: %s", serverUri);

      handshakeFuture.completeExceptionally(
          new UaException(StatusCodes.Bad_TcpEndpointUrlInvalid, message));

      ExceptionHandler.sendErrorMessage(
          ctx, new UaException(StatusCodes.Bad_TcpEndpointUrlInvalid, message));

      ctx.close();
      return;
    }

    sendHello(ctx, reverseHello.endpointUrl());

    state = State.AWAITING_ACK;
  }

  private void sendHello(ChannelHandlerContext ctx, String endpointUrl) throws UaException {

    timeout = startAcknowledgeTimeout(ctx);

    EncodingLimits encodingLimits = application.getEncodingContext().getEncodingLimits();

    HelloMessage hello =
        new HelloMessage(
            PROTOCOL_VERSION,
            encodingLimits.getMaxChunkSize(),
            encodingLimits.getMaxChunkSize(),
            encodingLimits.getMaxMessageSize(),
            encodingLimits.getMaxChunkCount(),
            endpointUrl);

    logger.debug("Sending Hello: {}", hello);

    ByteBuf messageBuffer = TcpMessageEncoder.encode(hello);

    ctx.writeAndFlush(messageBuffer, ctx.voidPromise());
  }

  private void onAcknowledge(ChannelHandlerContext ctx, ByteBuf buffer) {
    if (timeout != null && !timeout.cancel()) {
      handshakeFuture.completeExceptionally(
          new UaException(StatusCodes.Bad_Timeout, "timed out waiting for Acknowledge"));
      ctx.close();
      return;
    }

    state = State.HANDSHAKE_COMPLETE;

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

    ChannelParameters channelParameters =
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
              ctx.pipeline().remove(UascClientReverseHelloHandler.this);
            });
  }

  private void onError(ChannelHandlerContext ctx, ByteBuf buffer) {
    if (timeout != null) {
      timeout.cancel();
    }

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

  private Timeout startReverseHelloTimeout(ChannelHandlerContext ctx) {
    return config
        .getWheelTimer()
        .newTimeout(
            t -> {
              if (!t.isCancelled()) {
                handshakeFuture.completeExceptionally(
                    new UaException(
                        StatusCodes.Bad_Timeout,
                        String.format(
                            "timed out waiting for ReverseHello after %sms",
                            reverseHelloTimeoutMs)));
                ctx.close();
              }
            },
            reverseHelloTimeoutMs,
            TimeUnit.MILLISECONDS);
  }

  private Timeout startAcknowledgeTimeout(ChannelHandlerContext ctx) {
    return config
        .getWheelTimer()
        .newTimeout(
            t -> {
              if (!t.isCancelled()) {
                handshakeFuture.completeExceptionally(
                    new UaException(
                        StatusCodes.Bad_Timeout,
                        String.format(
                            "timed out waiting for Acknowledge after %sms",
                            config.getAcknowledgeTimeout())));
                ctx.close();
              }
            },
            config.getAcknowledgeTimeout().longValue(),
            TimeUnit.MILLISECONDS);
  }
}
