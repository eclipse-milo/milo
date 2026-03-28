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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;

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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.ExceptionHandler;
import org.eclipse.milo.opcua.stack.core.channel.headers.HeaderDecoder;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.eclipse.milo.opcua.stack.transport.client.tcp.ChannelConsumerRegistry;
import org.eclipse.milo.opcua.stack.transport.client.tcp.EndpointResolver;
import org.eclipse.milo.opcua.stack.transport.client.tcp.InboundChannelTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.ReverseConnectChannelFsm;
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
  private final AtomicInteger pendingConnections = new AtomicInteger(0);
  private final ChannelGroup childChannels;

  private volatile ServerBootstrap serverBootstrap;
  private volatile Channel serverChannel;

  /**
   * Create a new {@link MultiplexedReverseConnectListener}.
   *
   * @param config the listener configuration.
   */
  public MultiplexedReverseConnectListener(MultiplexedReverseConnectListenerConfig config) {
    this.config = config;
    this.childChannels = new DefaultChannelGroup(config.getTransportConfig().getEventLoop().next());
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
   * <p>This method is idempotent; calling it when the listener is already started has no effect.
   */
  public synchronized void start() {
    if (serverBootstrap != null) {
      return;
    }

    serverBootstrap =
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
                    if (maxConnections > 0 && activeConnections.get() >= maxConnections) {
                      logger.debug(
                          "Max connections ({}) reached, closing {}",
                          maxConnections,
                          ch.remoteAddress());
                      ch.close();
                      return;
                    }

                    activeConnections.incrementAndGet();
                    childChannels.add(ch);
                    ch.closeFuture().addListener(f -> activeConnections.decrementAndGet());

                    if (config.isRateLimitingEnabled()) {
                      ch.pipeline().addLast(RateLimitingHandler.getInstance());
                    }

                    ch.pipeline().addLast(new ReverseHelloDecoder());

                    config.getChannelPipelineCustomizer().accept(ch.pipeline());
                  }
                });

    config.getServerBootstrapCustomizer().accept(serverBootstrap);

    try {
      serverChannel =
          serverBootstrap.bind(config.getListenAddress()).syncUninterruptibly().channel();
    } catch (Exception e) {
      serverBootstrap = null;
      serverChannel = null;
      throw e;
    }

    logger.info("Listening for reverse connections on {}", serverChannel.localAddress());
  }

  /**
   * Stop the listener. Unbinds the listen address, closes all child channels, and clears the
   * dispatch table.
   *
   * @return a future that completes when all child channels have been closed.
   */
  public CompletableFuture<Void> stop() {
    synchronized (this) {
      if (serverChannel != null && serverChannel.isOpen()) {
        var addr = serverChannel.localAddress();
        serverChannel.close().syncUninterruptibly();
        logger.info("Stopped listening on {}", addr);
      }
      serverBootstrap = null;
      serverChannel = null;
    }

    consumers.clear();

    CompletableFuture<Void> future = new CompletableFuture<>();
    childChannels.close().addListener(f -> future.complete(null));
    return future;
  }

  @Override
  public void register(String serverUri, ChannelConsumer consumer) {
    consumers.computeIfAbsent(serverUri, k -> new CopyOnWriteArrayList<>()).add(consumer);
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
  public void deregister(String serverUri, ChannelConsumer consumer) {
    CopyOnWriteArrayList<ChannelConsumer> list = consumers.get(serverUri);
    if (list != null) {
      list.remove(consumer);
      if (list.isEmpty()) {
        consumers.remove(serverUri, list);
      }
    }
  }

  private void dispatch(Channel channel, ReverseHelloMessage rhe) {
    String serverUri = rhe.serverUri();

    CopyOnWriteArrayList<ChannelConsumer> list = consumers.get(serverUri);

    if (list != null && !list.isEmpty()) {
      // Find first consumer whose FSM is in a connection-needing state
      for (ChannelConsumer consumer : list) {
        ReverseConnectChannelFsm.State state = consumer.channelFsm().getState();
        if (state == ReverseConnectChannelFsm.State.NotConnected
            || state == ReverseConnectChannelFsm.State.Reconnecting) {
          consumer
              .channelFsm()
              .fireEvent(new ReverseConnectChannelFsm.Event.ConnectionAccepted(channel, rhe));
          return;
        }
      }

      // All consumers are Connected or Handshaking — hand to first consumer.
      // Its FSM will close the duplicate via its existing internal transition.
      list.get(0)
          .channelFsm()
          .fireEvent(new ReverseConnectChannelFsm.Event.ConnectionAccepted(channel, rhe));
      return;
    }

    // No registered consumer — try EndpointResolver or close
    if (config.getEndpointResolver() != null) {
      dispatchToResolver(channel, rhe);
    } else {
      logger.debug(
          "No consumer registered for ServerUri={}, closing channel {}",
          serverUri,
          channel.remoteAddress());
      channel.close();
    }
  }

  /**
   * Dispatch an unmatched connection to the configured {@link EndpointResolver}. Enforces the
   * {@code maxPendingConnections} backpressure limit before invoking the resolver.
   */
  private void dispatchToResolver(Channel channel, ReverseHelloMessage rhe) {
    int pending = pendingConnections.incrementAndGet();
    if (pending > config.getMaxPendingConnections()) {
      pendingConnections.decrementAndGet();
      logger.debug(
          "maxPendingConnections ({}) exceeded, closing channel from {}",
          config.getMaxPendingConnections(),
          channel.remoteAddress());
      channel.close();
      return;
    }

    EndpointResolver resolver = config.getEndpointResolver();
    assert resolver != null;

    // Create a Discovery capability backed by the inbound channel.
    // The flag is set synchronously when getEndpoints() is called,
    // before any async work begins.
    var channelConsumed = new AtomicBoolean(false);

    EndpointResolver.Discovery discovery =
        () -> {
          channelConsumed.set(true);

          OpcTcpMultiplexedReverseConnectTransportConfig transportConfig =
              createDiscoveryTransportConfig();
          var transport = new InboundChannelTransport(transportConfig, channel, rhe);

          var endpoint =
              new EndpointDescription(
                  rhe.endpointUrl(),
                  null,
                  null,
                  MessageSecurityMode.None,
                  SecurityPolicy.None.getUri(),
                  null,
                  Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
                  ubyte(0));

          var discoveryClient = new DiscoveryClient(endpoint, transport);

          return discoveryClient
              .connectAsync()
              .thenCompose(
                  u ->
                      discoveryClient.getEndpoints(rhe.endpointUrl(), new String[0], new String[0]))
              .thenApply(response -> Lists.ofNullable(response.getEndpoints()))
              .whenComplete((endpoints, ex) -> discoveryClient.disconnectAsync())
              .orTimeout(60, TimeUnit.SECONDS);
        };

    CompletableFuture<EndpointDescription> resolveFuture;
    try {
      resolveFuture = resolver.resolve(rhe.serverUri(), rhe.endpointUrl(), discovery);
    } catch (Exception ex) {
      pendingConnections.decrementAndGet();
      logger.warn("EndpointResolver threw for ServerUri={}", rhe.serverUri(), ex);
      channel.close();
      return;
    }

    resolveFuture.whenComplete(
        (endpoint, ex) -> {
          pendingConnections.decrementAndGet();

          if (ex != null) {
            logger.warn("EndpointResolver failed for ServerUri={}", rhe.serverUri(), ex);
            channel.close();
            return;
          }

          try {
            onEndpointResolved(channel, rhe, endpoint, channelConsumed.get());
          } catch (Exception e) {
            logger.error("Error handling resolved endpoint for ServerUri={}", rhe.serverUri(), e);
            channel.close();
          }
        });
  }

  /**
   * Called when the {@link EndpointResolver} successfully resolves an {@link EndpointDescription}
   * for a previously unknown server. Creates a {@link OpcTcpMultiplexedReverseConnectTransport} and
   * notifies the configured {@link ClientListener}.
   */
  private void onEndpointResolved(
      Channel channel,
      ReverseHelloMessage rhe,
      EndpointDescription endpoint,
      boolean channelConsumed) {

    String serverUri = rhe.serverUri();

    logger.debug("Resolved endpoint for ServerUri={}: {}", serverUri, endpoint.getEndpointUrl());

    OpcTcpMultiplexedReverseConnectTransport transport = createTransport(serverUri);

    if (!channelConsumed) {
      transport.offerChannel(channel, rhe);
    }

    ClientListener clientListener = config.getClientListener();
    if (clientListener != null) {
      OpcUaClientConfigBuilder builder = OpcUaClientConfig.builder();
      builder.setEndpoint(endpoint);
      builder.setSessionEndpointValidationEnabled(false);

      ClientCustomizer clientCustomizer = config.getClientCustomizer();
      if (clientCustomizer != null) {
        clientCustomizer.configure(serverUri, builder);
      }

      OpcUaClient client = new OpcUaClient(builder.build(), transport);
      clientListener.onClientCreated(client);
    }
  }

  private OpcTcpMultiplexedReverseConnectTransportConfig createDiscoveryTransportConfig() {
    return OpcTcpMultiplexedReverseConnectTransportConfig.newBuilder()
        .setExecutor(config.getTransportConfig().getExecutor())
        .setScheduledExecutor(config.getTransportConfig().getScheduledExecutor())
        .setEventLoop(config.getTransportConfig().getEventLoop())
        .setWheelTimer(config.getTransportConfig().getWheelTimer())
        .build();
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

      if (timeoutFuture != null) {
        timeoutFuture.cancel(false);
      }

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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      logger.error("Exception in ReverseHelloDecoder", cause);
      ExceptionHandler.sendErrorMessage(ctx, cause);
    }
  }
}
