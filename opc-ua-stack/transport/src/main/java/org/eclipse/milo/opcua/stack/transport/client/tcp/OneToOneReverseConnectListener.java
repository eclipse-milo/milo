/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.ExceptionHandler;
import org.eclipse.milo.opcua.stack.core.channel.headers.HeaderDecoder;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Owns the one-to-one Reverse Connect listening socket and accepted child sockets.
 *
 * <p>The listener only decodes the first {@link ReverseHelloMessage} from each accepted socket and
 * posts the accepted channel to its sink. Handshake and client lifecycle policy remain owned by the
 * channel owner that consumes the sink callback.
 */
final class OneToOneReverseConnectListener {

  /**
   * Maximum size for a ReverseHello message: 8-byte header + (4 + 4096) ServerUri + (4 + 4096)
   * EndpointUrl.
   */
  private static final int MAX_REVERSE_HELLO_MESSAGE_SIZE = 8 + 4 + 4096 + 4 + 4096;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(OneToOneReverseConnectListener.class);

  private final OpcTcpReverseConnectTransportConfig config;
  private final AcceptedChannelSink acceptedChannelSink;
  private final Function<Channel, CompletableFuture<Void>> channelCloser;
  private final Set<Channel> childChannels = ConcurrentHashMap.newKeySet();

  private ServerBootstrap serverBootstrap;
  private Channel serverChannel;
  private CompletableFuture<Channel> bindFuture;

  OneToOneReverseConnectListener(
      OpcTcpReverseConnectTransportConfig config, AcceptedChannelSink acceptedChannelSink) {

    this(config, acceptedChannelSink, OneToOneReverseConnectListener::closeNettyChannelAsync);
  }

  OneToOneReverseConnectListener(
      OpcTcpReverseConnectTransportConfig config,
      AcceptedChannelSink acceptedChannelSink,
      Function<Channel, CompletableFuture<Void>> channelCloser) {

    this.config = Objects.requireNonNull(config, "config");
    this.acceptedChannelSink = Objects.requireNonNull(acceptedChannelSink, "acceptedChannelSink");
    this.channelCloser = Objects.requireNonNull(channelCloser, "channelCloser");
  }

  synchronized CompletableFuture<Channel> start() {
    if (serverChannel != null && serverChannel.isOpen()) {
      return CompletableFuture.completedFuture(serverChannel);
    }

    if (bindFuture != null) {
      return bindFuture;
    }

    var bootstrap =
        new ServerBootstrap()
            .channel(NioServerSocketChannel.class)
            .group(config.getEventLoop())
            .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(
                new ChannelInitializer<SocketChannel>() {
                  @Override
                  protected void initChannel(SocketChannel ch) {
                    trackChildChannel(ch);

                    ch.pipeline().addLast(new ReverseHelloDecoder(config.getReverseHelloTimeout()));

                    config.getChannelPipelineCustomizer().accept(ch.pipeline());
                  }
                });

    config.getServerBootstrapCustomizer().accept(bootstrap);

    var future = new CompletableFuture<Channel>();

    serverBootstrap = bootstrap;
    bindFuture = future;

    try {
      ChannelFuture channelFuture = bootstrap.bind(config.getListenAddress());
      channelFuture.addListener(f -> bindComplete(bootstrap, future, channelFuture));
    } catch (RuntimeException e) {
      serverBootstrap = null;
      bindFuture = null;
      future.completeExceptionally(e);
    }

    return future;
  }

  synchronized CompletableFuture<Unit> stop() {
    CompletableFuture<Channel> pendingBind = bindFuture;
    Channel listenerChannel = serverChannel;
    Object listenAddress = listenerChannel != null ? listenerChannel.localAddress() : null;

    serverBootstrap = null;
    serverChannel = null;
    bindFuture = null;

    CompletableFuture<Void> listenerClose =
        pendingBind != null
            ? pendingBind.handle((channel, ex) -> channel).thenCompose(this::closeChannelAsync)
            : closeChannelAsync(listenerChannel);

    return listenerClose
        .handle((v, ex) -> ex)
        .thenCompose(
            listenerFailure -> {
              if (listenerFailure == null && listenAddress != null) {
                LOGGER.info("Stopped listening on {}", listenAddress);
              }

              return closeChildChannelsAsync()
                  .handle(
                      (v, childFailure) -> {
                        if (listenerFailure != null) {
                          if (childFailure != null) {
                            listenerFailure.addSuppressed(childFailure);
                          }
                          throw completionException(listenerFailure);
                        }
                        if (childFailure != null) {
                          throw completionException(childFailure);
                        }
                        return Unit.VALUE;
                      });
            });
  }

  private void bindComplete(
      ServerBootstrap bootstrap, CompletableFuture<Channel> future, ChannelFuture channelFuture) {

    if (channelFuture.isSuccess()) {
      Channel channel = channelFuture.channel();

      synchronized (this) {
        if (serverBootstrap == bootstrap && bindFuture == future) {
          serverChannel = channel;
          bindFuture = null;
        } else {
          closeChannelAsync(channel)
              .whenComplete(
                  (v, ex) -> {
                    if (ex != null) {
                      future.completeExceptionally(ex);
                    } else {
                      future.completeExceptionally(
                          new UaException(StatusCodes.Bad_Shutdown, "listener stopped"));
                    }
                  });
          return;
        }
      }

      LOGGER.info("Listening for reverse connections on {}", channel.localAddress());
      future.complete(channel);
    } else {
      synchronized (this) {
        if (bindFuture == future) {
          serverBootstrap = null;
          serverChannel = null;
          bindFuture = null;
        }
      }

      future.completeExceptionally(channelFuture.cause());
    }
  }

  private void trackChildChannel(Channel channel) {
    childChannels.add(channel);
    channel.closeFuture().addListener(f -> childChannels.remove(channel));
  }

  private CompletableFuture<Void> closeChannelAsync(Channel channel) {
    if (channel == null) {
      return CompletableFuture.completedFuture(null);
    }

    if (!channel.isOpen()) {
      childChannels.remove(channel);
      return CompletableFuture.completedFuture(null);
    }

    try {
      return Objects.requireNonNull(
          channelCloser.apply(channel), "channelCloser returned null future");
    } catch (RuntimeException e) {
      return CompletableFuture.failedFuture(e);
    }
  }

  private static CompletableFuture<Void> closeNettyChannelAsync(Channel channel) {
    var future = new CompletableFuture<Void>();
    channel
        .close()
        .addListener(
            f -> {
              if (f.isSuccess()) {
                future.complete(null);
              } else {
                future.completeExceptionally(f.cause());
              }
            });
    return future;
  }

  private static CompletionException completionException(Throwable failure) {
    if (failure instanceof CompletionException completionException) {
      return completionException;
    }

    return new CompletionException(failure);
  }

  private CompletableFuture<Unit> closeChildChannelsAsync() {
    Channel[] trackedChannels = childChannels.toArray(Channel[]::new);
    if (trackedChannels.length == 0) {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    var closeFutures = new CompletableFuture<?>[trackedChannels.length];
    for (int i = 0; i < trackedChannels.length; i++) {
      closeFutures[i] = closeChannelAsync(trackedChannels[i]);
    }

    return CompletableFuture.allOf(closeFutures).thenApply(v -> Unit.VALUE);
  }

  /**
   * A decoder installed on each accepted child channel that requires the first complete OPC UA TCP
   * message to be a {@code ReverseHello}.
   *
   * <p>After a {@code ReverseHello} is decoded, this handler removes itself so the channel can
   * continue through the normal reverse-connect handshake.
   */
  private final class ReverseHelloDecoder extends ByteToMessageDecoder implements HeaderDecoder {

    private final long reverseHelloTimeout;
    private ScheduledFuture<?> timeoutFuture;

    private ReverseHelloDecoder(long reverseHelloTimeout) {
      this.reverseHelloTimeout = reverseHelloTimeout;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
      if (reverseHelloTimeout > 0) {
        timeoutFuture =
            ctx.channel()
                .eventLoop()
                .schedule(
                    () -> {
                      if (ctx.channel().isActive()) {
                        LOGGER.debug(
                            "ReverseHello timeout ({}ms) elapsed, closing {}",
                            reverseHelloTimeout,
                            ctx.channel().remoteAddress());
                        ctx.close();
                      }
                    },
                    reverseHelloTimeout,
                    TimeUnit.MILLISECONDS);
      }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
        throws Exception {

      if (in.readableBytes() < HEADER_LENGTH) {
        return;
      }

      int messageTypeInt = in.getMediumLE(in.readerIndex());
      MessageType messageType;
      try {
        messageType = MessageType.fromMediumInt(messageTypeInt);
      } catch (UaException e) {
        ExceptionHandler.sendErrorMessage(ctx, e);
        return;
      }

      if (messageType != MessageType.ReverseHello) {
        ExceptionHandler.sendErrorMessage(
            ctx,
            new UaException(
                StatusCodes.Bad_TcpMessageTypeInvalid,
                "expected ReverseHello, received " + messageType));
        return;
      }

      int messageLength = getMessageLength(in, MAX_REVERSE_HELLO_MESSAGE_SIZE);

      if (in.readableBytes() < messageLength) {
        return;
      }

      ByteBuf messageBuffer = in.readSlice(messageLength);

      ReverseHelloMessage rhe = TcpMessageDecoder.decodeReverseHello(messageBuffer);

      cancelReverseHelloTimeout();
      ctx.pipeline().remove(this);

      LOGGER.debug(
          "Received ReverseHello: serverUri={}, endpointUrl={}",
          rhe.serverUri(),
          rhe.endpointUrl());

      acceptedChannelSink.accepted(ctx.channel(), rhe);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      cancelReverseHelloTimeout();
      super.channelInactive(ctx);
    }

    @Override
    protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
      cancelReverseHelloTimeout();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      LOGGER.error("Exception in ReverseHelloDecoder", cause);
      ExceptionHandler.sendErrorMessage(ctx, cause);
    }

    private void cancelReverseHelloTimeout() {
      ScheduledFuture<?> future = timeoutFuture;
      if (future != null) {
        future.cancel(false);
        timeoutFuture = null;
      }
    }
  }

  @FunctionalInterface
  interface AcceptedChannelSink {

    /**
     * Offers an accepted channel and its decoded ReverseHello message to the lifecycle owner.
     *
     * @param channel the accepted channel.
     * @param reverseHello the decoded ReverseHello message.
     */
    void accepted(Channel channel, ReverseHelloMessage reverseHello);
  }
}
