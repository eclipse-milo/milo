/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.ExceptionHandler;
import org.eclipse.milo.opcua.stack.core.channel.headers.HeaderDecoder;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.transport.client.tcp.ChannelConsumerRegistry;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.RateLimitingHandler;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A shared client-side TCP listener that accepts inbound OPC UA Reverse Connect connections from
 * multiple servers and dispatches each connection to an independent client instance.
 *
 * <p>The listener owns a single Netty {@link ServerBootstrap}. Each accepted TCP connection gets a
 * {@link ReverseHelloDecoder} installed on its pipeline. After decoding the {@code ReverseHello}
 * message, the listener dispatches the channel to the appropriate registered consumer based on
 * {@code ServerUri}.
 */
public class MultiplexedReverseConnectListener implements ChannelConsumerRegistry {

  private static final Logger logger =
      LoggerFactory.getLogger(MultiplexedReverseConnectListener.class);

  /**
   * Maximum size for a ReverseHello message: 8-byte header + (4 + 4096) ServerUri + (4 + 4096)
   * EndpointUrl.
   */
  private static final int MAX_REVERSE_HELLO_MESSAGE_SIZE = 8 + 4 + 4096 + 4 + 4096;

  private final MultiplexedReverseConnectListenerConfig config;

  private final ConcurrentHashMap<String, CopyOnWriteArrayList<ChannelConsumer>> consumers =
      new ConcurrentHashMap<>();

  private final AtomicInteger activeConnections = new AtomicInteger(0);
  private final ChannelGroup childChannels;
  private final @Nullable MultiplexedReverseConnectClientController clientController;

  private volatile ServerBootstrap serverBootstrap;
  private volatile Channel serverChannel;
  private boolean stopped;
  private @Nullable CompletableFuture<Void> stopFuture;

  /**
   * Create a new {@link MultiplexedReverseConnectListener}.
   *
   * @param config the listener configuration.
   */
  public MultiplexedReverseConnectListener(MultiplexedReverseConnectListenerConfig config) {
    this.config = config;
    this.childChannels = new DefaultChannelGroup(config.getTransportConfig().getEventLoop().next());
    this.clientController =
        config.getEndpointResolver() != null
            ? new MultiplexedReverseConnectClientController(config, this::createTransport)
            : null;
  }

  /**
   * Get the listener configuration.
   *
   * @return the listener configuration.
   */
  public MultiplexedReverseConnectListenerConfig getConfig() {
    return config;
  }

  /**
   * Get the local address the listener is bound to, or {@code null} if not started.
   *
   * @return the local address, or {@code null}.
   */
  public @Nullable InetSocketAddress getLocalAddress() {
    Channel sc = serverChannel;
    return sc != null ? (InetSocketAddress) sc.localAddress() : null;
  }

  /**
   * Start the listener. Binds the {@link ServerBootstrap} to the configured listen address.
   *
   * <p>This method is idempotent while the listener is already started. A stopped listener cannot
   * be restarted; create a new listener instance instead.
   */
  public synchronized void start() {
    if (stopped) {
      throw new IllegalStateException("MultiplexedReverseConnectListener cannot be restarted");
    }

    if (serverBootstrap != null) {
      return;
    }

    ServerBootstrap bootstrap =
        new ServerBootstrap()
            .channel(NioServerSocketChannel.class)
            .group(config.getTransportConfig().getEventLoop())
            .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(
                new ChannelInitializer<SocketChannel>() {
                  @Override
                  protected void initChannel(SocketChannel ch) {
                    int maxConnections = config.getMaxConnections();
                    int newCount = activeConnections.incrementAndGet();
                    if (maxConnections > 0 && newCount > maxConnections) {
                      activeConnections.decrementAndGet();
                      logger.debug(
                          "Max connections ({}) reached, closing {}",
                          maxConnections,
                          ch.remoteAddress());
                      ch.close();
                      return;
                    }

                    childChannels.add(ch);
                    ch.closeFuture().addListener(f -> activeConnections.decrementAndGet());

                    if (config.isRateLimitingEnabled()) {
                      ch.pipeline().addLast(RateLimitingHandler.getInstance());
                    }

                    ch.pipeline().addLast(new ReverseHelloDecoder());

                    config.getChannelPipelineCustomizer().accept(ch.pipeline());
                  }
                });

    config.getServerBootstrapCustomizer().accept(bootstrap);

    try {
      serverChannel = bootstrap.bind(config.getListenAddress()).syncUninterruptibly().channel();
      serverBootstrap = bootstrap;
    } catch (Exception e) {
      serverBootstrap = null;
      serverChannel = null;
      throw e;
    }

    logger.info("Listening for reverse connections on {}", serverChannel.localAddress());
  }

  /**
   * Stop the listener. Unbinds the listen address, notifies registered consumers, closes all child
   * channels, and clears the dispatch table.
   *
   * @return a future that completes when listener stop notification and listener/child channel
   *     closes are complete.
   */
  public CompletableFuture<Void> stop() {
    CompletableFuture<Void> future;

    synchronized (this) {
      if (stopFuture != null) {
        return stopFuture;
      }

      stopped = true;

      Channel listenerChannel = serverChannel;
      Object listenAddress = listenerChannel != null ? listenerChannel.localAddress() : null;
      UaException stopCause = newStopCause();

      List<ChannelConsumer> registeredConsumers = snapshotConsumers();
      CompletableFuture<Void> notificationFuture =
          notifyConsumersStopped(registeredConsumers, stopCause);
      consumers.clear();

      serverBootstrap = null;
      serverChannel = null;

      CompletableFuture<Void> listenerCloseFuture =
          closeChannelAsync(listenerChannel)
              .whenComplete(
                  (v, ex) -> {
                    if (ex == null && listenAddress != null) {
                      logger.info("Stopped listening on {}", listenAddress);
                    }
                  });
      CompletableFuture<Void> childCloseFuture = closeChildChannelsAsync();
      CompletableFuture<Void> controllerStopFuture =
          clientController != null
              ? clientController.stop(stopCause)
              : CompletableFuture.completedFuture(null);

      future =
          CompletableFuture.allOf(
              notificationFuture, controllerStopFuture, listenerCloseFuture, childCloseFuture);
      stopFuture = future;
    }

    return future;
  }

  @Override
  public synchronized void register(String serverUri, ChannelConsumer consumer) {
    if (stopped) {
      consumer.listenerStopped(newStopCause());
      return;
    }

    CopyOnWriteArrayList<ChannelConsumer> list =
        consumers.computeIfAbsent(serverUri, k -> new CopyOnWriteArrayList<>());
    if (!list.contains(consumer)) {
      list.add(consumer);
    }
  }

  /**
   * Create a {@link OpcTcpMultiplexedReverseConnectTransport} for a known server. The returned
   * transport is not yet registered — registration happens when {@link
   * OpcTcpMultiplexedReverseConnectTransport#connect} is called.
   *
   * @param serverUri the server's ApplicationUri.
   * @param transportConfig the transport configuration.
   * @return a new {@link OpcTcpMultiplexedReverseConnectTransport} wired to this listener.
   */
  public OpcTcpMultiplexedReverseConnectTransport createTransport(
      String serverUri, OpcTcpMultiplexedReverseConnectTransportConfig transportConfig) {

    return new OpcTcpMultiplexedReverseConnectTransport(serverUri, transportConfig, this);
  }

  /**
   * Create a {@link OpcTcpMultiplexedReverseConnectTransport} for a known server using default
   * transport configuration derived from this listener's shared transport config.
   *
   * @param serverUri the server's ApplicationUri.
   * @return a new {@link OpcTcpMultiplexedReverseConnectTransport} wired to this listener.
   */
  public OpcTcpMultiplexedReverseConnectTransport createTransport(String serverUri) {
    OpcTcpMultiplexedReverseConnectTransportConfig transportConfig =
        OpcTcpMultiplexedReverseConnectTransportConfig.newBuilder()
            .setExecutor(config.getTransportConfig().getExecutor())
            .setScheduledExecutor(config.getTransportConfig().getScheduledExecutor())
            .setEventLoop(config.getTransportConfig().getEventLoop())
            .setWheelTimer(config.getTransportConfig().getWheelTimer())
            .build();

    return createTransport(serverUri, transportConfig);
  }

  @Override
  public synchronized void deregister(String serverUri, ChannelConsumer consumer) {
    CopyOnWriteArrayList<ChannelConsumer> list = consumers.get(serverUri);
    if (list != null) {
      list.removeIf(consumer::equals);
      if (list.isEmpty()) {
        consumers.remove(serverUri, list);
      }
    }
  }

  private void dispatch(Channel channel, ReverseHelloMessage rhe) {
    String serverUri = rhe.serverUri();

    MultiplexedReverseConnectClientController controller;

    synchronized (this) {
      if (stopped) {
        ReverseConnectRejection.sendErrorAndClose(
            channel, StatusCodes.Bad_Shutdown, "multiplexed reverse connect listener stopped");
        return;
      }

      CopyOnWriteArrayList<ChannelConsumer> list = consumers.get(serverUri);

      if (list != null && !list.isEmpty()) {
        ChannelConsumer duplicateConsumer = null;

        // Find the first consumer that can reserve this channel synchronously.
        for (ChannelConsumer consumer : list) {
          if (consumer.tryAccept(channel, rhe)) {
            return;
          }

          if (duplicateConsumer == null && consumer.acceptsDuplicateChannel()) {
            duplicateConsumer = consumer;
          }
        }

        if (duplicateConsumer != null) {
          // All consumers are Connected or Handshaking. Hand to the first
          // available consumer; its channel owner closes the duplicate through
          // its existing internal transition.
          duplicateConsumer.accept(channel, rhe);
          return;
        }

        logger.debug(
            "All consumers for ServerUri={} are stopped, closing channel {}",
            serverUri,
            channel.remoteAddress());
        ReverseConnectRejection.sendErrorAndClose(
            channel,
            StatusCodes.Bad_ConnectionRejected,
            "all consumers for ServerUri=" + serverUri + " are stopped");
        return;
      }

      controller = clientController;
    }

    // No registered consumer — delegate unknown-server handling or close.
    if (controller != null) {
      controller.dispatch(channel, rhe);
    } else {
      logger.debug(
          "No consumer registered for ServerUri={}, closing channel {}",
          serverUri,
          channel.remoteAddress());
      ReverseConnectRejection.sendErrorAndClose(
          channel,
          StatusCodes.Bad_ServerUriInvalid,
          "no consumer registered for ServerUri=" + serverUri);
    }
  }

  private List<ChannelConsumer> snapshotConsumers() {
    var snapshot = new ArrayList<ChannelConsumer>();
    for (CopyOnWriteArrayList<ChannelConsumer> list : consumers.values()) {
      for (ChannelConsumer consumer : list) {
        if (!snapshot.contains(consumer)) {
          snapshot.add(consumer);
        }
      }
    }
    return snapshot;
  }

  private static UaException newStopCause() {
    return new UaException(
        StatusCodes.Bad_Shutdown, "multiplexed reverse connect listener stopped");
  }

  private CompletableFuture<Void> notifyConsumersStopped(
      List<ChannelConsumer> registeredConsumers, Throwable cause) {

    if (registeredConsumers.isEmpty()) {
      return CompletableFuture.completedFuture(null);
    }

    var futures = new CompletableFuture<?>[registeredConsumers.size()];
    for (int i = 0; i < registeredConsumers.size(); i++) {
      try {
        futures[i] = registeredConsumers.get(i).listenerStopped(cause);
      } catch (RuntimeException e) {
        futures[i] = CompletableFuture.failedFuture(e);
      }
    }

    return CompletableFuture.allOf(futures);
  }

  private CompletableFuture<Void> closeChildChannelsAsync() {
    return toCompletableFuture(childChannels.close());
  }

  private CompletableFuture<Void> closeChannelAsync(@Nullable Channel channel) {
    if (channel == null || !channel.isOpen()) {
      return CompletableFuture.completedFuture(null);
    }

    return toCompletableFuture(channel.close());
  }

  private static CompletableFuture<Void> toCompletableFuture(
      io.netty.util.concurrent.Future<?> nettyFuture) {

    var future = new CompletableFuture<Void>();
    nettyFuture.addListener(
        f -> {
          if (f.isSuccess()) {
            future.complete(null);
          } else {
            future.completeExceptionally(f.cause());
          }
        });
    return future;
  }

  /**
   * A decoder that reads the first message on each accepted child channel, verifies it is a
   * ReverseHello, decodes it, and dispatches the channel via the listener's dispatch logic.
   *
   * <p>A configurable timeout closes the channel if no ReverseHello arrives within the configured
   * duration.
   */
  private class ReverseHelloDecoder extends ByteToMessageDecoder implements HeaderDecoder {

    private @Nullable ScheduledFuture<?> timeoutFuture;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
      long timeout = config.getReverseHelloTimeout();
      if (timeout > 0) {
        timeoutFuture =
            ctx.channel()
                .eventLoop()
                .schedule(
                    () -> {
                      if (ctx.channel().isActive()) {
                        logger.debug(
                            "ReverseHello timeout ({}ms) elapsed, closing {}",
                            timeout,
                            ctx.channel().remoteAddress());
                        ctx.channel().close();
                      }
                    },
                    timeout,
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

      cancelReverseHelloTimeout();

      ByteBuf messageBuffer = in.readSlice(messageLength);

      ReverseHelloMessage rhe = TcpMessageDecoder.decodeReverseHello(messageBuffer);

      ctx.pipeline().remove(this);

      logger.debug(
          "Received ReverseHello: serverUri={}, endpointUrl={}",
          rhe.serverUri(),
          rhe.endpointUrl());

      dispatch(ctx.channel(), rhe);
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
      logger.error("Exception in ReverseHelloDecoder", cause);
      ExceptionHandler.sendErrorMessage(ctx, cause);
    }

    private void cancelReverseHelloTimeout() {
      if (timeoutFuture != null) {
        timeoutFuture.cancel(false);
      }
    }
  }
}
