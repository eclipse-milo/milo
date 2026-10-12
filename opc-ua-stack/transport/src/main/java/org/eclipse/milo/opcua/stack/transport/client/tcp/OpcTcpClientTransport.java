/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.tcp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import com.digitalpetri.fsm.FsmContext;
import com.digitalpetri.netty.fsm.ChannelActions;
import com.digitalpetri.netty.fsm.ChannelFsm;
import com.digitalpetri.netty.fsm.ChannelFsmConfig;
import com.digitalpetri.netty.fsm.ChannelFsmFactory;
import com.digitalpetri.netty.fsm.Event;
import com.digitalpetri.netty.fsm.State;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSecureChannelRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.AbstractUascClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.CurrentChannelProvider;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * UA-TCP client transport for the normal outbound client connection path.
 *
 * <p>The transport owns the outbound socket lifecycle through a {@link ChannelFsm}, installs the
 * client UASC pipeline, and exposes optional channel-state capabilities for higher-level client
 * lifecycle code. Session management remains in the SDK layer; this transport only reports when its
 * SecureChannel-backed Netty channel becomes available or leaves service.
 */
public class OpcTcpClientTransport extends AbstractUascClientTransport
    implements ChannelStateObservable, CurrentChannelProvider {

  private static final FsmContext.Key<ClientApplicationContext> KEY_CLIENT_APPLICATION =
      new FsmContext.Key<>("clientApplication", ClientApplicationContext.class);

  private static final String CHANNEL_FSM_LOGGER_NAME =
      "org.eclipse.milo.opcua.stack.client.ChannelFsm";
  private static final AtomicLong INSTANCE_ID = new AtomicLong();

  private final String instanceId;

  private final ChannelFsm channelFsm;

  private final OpcTcpClientTransportConfig config;
  private volatile ClientSecureChannel secureChannel;

  // The ChannelFsm shelves Disconnect while an attempt is in progress, so disconnect() aborts the
  // in-flight socket and handshake itself rather than waiting for a peer or a handshake timeout.
  private volatile boolean connectionRequested;
  private final AtomicReference<Channel> connectingChannel = new AtomicReference<>();
  private final AtomicReference<CompletableFuture<ClientSecureChannel>> pendingHandshake =
      new AtomicReference<>();

  private final List<ChannelStateObservable.TransitionListener> transitionListeners =
      new CopyOnWriteArrayList<>();

  /**
   * Create an outbound UA-TCP client transport.
   *
   * <p>The supplied configuration provides the Netty resources, SecureChannel settings, timers, and
   * pipeline customization used for the lifetime of this transport.
   *
   * @param config the TCP client transport configuration.
   */
  public OpcTcpClientTransport(OpcTcpClientTransportConfig config) {
    super(config);

    this.config = config;

    this.instanceId = String.valueOf(INSTANCE_ID.incrementAndGet());

    ChannelFsmConfig fsmConfig =
        ChannelFsmConfig.newBuilder()
            .setLazy(false) // reconnect immediately
            .setMaxIdleSeconds(0) // keep alive handled by SessionFsm
            .setMaxReconnectDelaySeconds(16)
            .setPersistent(true)
            .setChannelActions(new ClientChannelActions())
            .setExecutor(config.getExecutor())
            .setScheduler(config.getScheduledExecutor())
            .setLoggerName(CHANNEL_FSM_LOGGER_NAME)
            .setLoggingContext(Map.of("instance-id", instanceId))
            .build();

    var factory = new ChannelFsmFactory(fsmConfig);

    channelFsm = factory.newChannelFsm();

    channelFsm.addTransitionListener(
        (from, to, via) -> {
          if (via instanceof Event.ConnectFailure connectFailure) {
            notifyConnectFailure(connectFailure.failure);
          }
          if (from != State.Connected && to == State.Connected) {
            notifyTransitionListeners(true);
          } else if (from == State.Connected && to != State.Connected) {
            notifyTransitionListeners(false);
          }
        });
  }

  @Override
  public OpcTcpClientTransportConfig getConfig() {
    return config;
  }

  @Override
  public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
    connectionRequested = true;
    channelFsm.getFsm().withContext(ctx -> ctx.set(KEY_CLIENT_APPLICATION, applicationContext));

    return channelFsm.connect().thenApply(c -> Unit.VALUE);
  }

  @Override
  public CompletableFuture<Unit> disconnect() {
    connectionRequested = false;
    Channel connecting = connectingChannel.getAndSet(null);
    if (connecting != null) connecting.close();
    CompletableFuture<ClientSecureChannel> handshake = pendingHandshake.getAndSet(null);
    if (handshake != null) {
      handshake.completeExceptionally(new UaException(StatusCodes.Bad_ConnectionClosed));
    }
    return channelFsm
        .disconnect()
        .thenApply(
            v -> {
              secureChannel = null;
              return Unit.VALUE;
            });
  }

  @Override
  public ByteString getChannelThumbprint() {
    ClientSecureChannel channel = secureChannel;
    return channel != null ? channel.getChannelThumbprint() : ByteString.NULL_VALUE;
  }

  @Override
  protected CompletableFuture<Channel> getChannel() {
    return channelFsm.getChannel();
  }

  public ChannelFsm getChannelFsm() {
    return channelFsm;
  }

  /**
   * Get the most recently established SecureChannel, including its immutable certificate bindings.
   *
   * @return the established channel, or empty before the first handshake or after disconnect.
   */
  public Optional<ClientSecureChannel> getSecureChannel() {
    return Optional.ofNullable(secureChannel);
  }

  /**
   * {@inheritDoc}
   *
   * <p>The request is built only after the ChannelFsm's channel future completes, which happens
   * after the handshake publishes the new channel's certificate and thumbprint, so a channel-bound
   * signature never covers a dead channel during a reconnect.
   */
  @Override
  public CompletableFuture<UaResponseMessageType> sendRequestMessage(
      Callable<UaRequestMessageType> requestSupplier, long channelTimeoutMillis) {
    CompletableFuture<Channel> channelReady = getChannelFsm().getChannel().copy();
    if (channelTimeoutMillis > 0) {
      channelReady.orTimeout(channelTimeoutMillis, TimeUnit.MILLISECONDS);
    }
    return channelReady
        .exceptionallyCompose(
            error ->
                CompletableFuture.failedFuture(
                    error instanceof TimeoutException
                        ? new UaException(StatusCodes.Bad_Timeout, "timed out waiting for channel")
                        : error))
        .thenCompose(
            channel -> {
              try {
                return sendRequestMessage(requestSupplier.call(), channel);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public void addTransitionListener(ChannelStateObservable.TransitionListener listener) {
    transitionListeners.add(listener);
  }

  @Override
  public void removeTransitionListener(ChannelStateObservable.TransitionListener listener) {
    transitionListeners.remove(listener);
  }

  @Override
  public @Nullable Channel getCurrentChannel() {
    try {
      return channelFsm.getChannel().getNow(null);
    } catch (RuntimeException e) {
      return null;
    }
  }

  private void notifyConnectFailure(Throwable failure) {
    for (ChannelStateObservable.TransitionListener listener : transitionListeners) {
      try {
        listener.onConnectFailure(failure);
      } catch (Throwable t) {
        logger.warn("Channel connect failure listener failed.", t);
      }
    }
  }

  private void notifyTransitionListeners(boolean connected) {
    for (ChannelStateObservable.TransitionListener listener : transitionListeners) {
      try {
        listener.onStateTransition(connected);
      } catch (Throwable t) {
        logger.warn("Channel state transition listener failed.", t);
      }
    }
  }

  private class ClientChannelActions implements ChannelActions {

    private final Logger logger = LoggerFactory.getLogger(CHANNEL_FSM_LOGGER_NAME);

    @Override
    public CompletableFuture<Channel> connect(FsmContext<State, Event> ctx) {
      ClientApplicationContext application =
          (ClientApplicationContext) ctx.get(KEY_CLIENT_APPLICATION);

      // The ChannelFsm runs one connect attempt at a time, so a plain set cannot overwrite another
      // attempt's future. The compareAndSet on completion guards against disconnect(), which may
      // have already cleared this reference concurrently.
      var handshakeFuture = new CompletableFuture<ClientSecureChannel>();
      pendingHandshake.set(handshakeFuture);

      var bootstrap = new Bootstrap();

      bootstrap
          .channel(NioSocketChannel.class)
          .group(OpcTcpClientTransport.this.config.getEventLoop())
          .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
          .option(
              ChannelOption.CONNECT_TIMEOUT_MILLIS,
              OpcTcpClientTransport.this.config.getConnectTimeout().intValue())
          .option(ChannelOption.TCP_NODELAY, true)
          .handler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                  OpcTcpClientChannelInitializer.initializeOutboundChannel(
                      ch,
                      config,
                      application,
                      OpcTcpClientTransport.this,
                      requestId::getAndIncrement,
                      handshakeFuture);
                }
              });

      config.getBootstrapCustomizer().accept(bootstrap);

      String endpointUrl = application.getEndpoint().getEndpointUrl();

      String host = EndpointUtil.getHost(endpointUrl);
      assert host != null;

      int port = EndpointUtil.getPort(endpointUrl);

      ChannelFuture connection = bootstrap.connect(new InetSocketAddress(host, port));
      connectingChannel.set(connection.channel());
      // disconnect() may have run between the ChannelFsm dispatching this attempt and the socket
      // being published above.
      if (!connectionRequested) {
        connection.channel().close();
        handshakeFuture.completeExceptionally(new UaException(StatusCodes.Bad_ConnectionClosed));
      }
      connection.addListener(
          (ChannelFuture f) -> {
            if (!f.isSuccess()) {
              Throwable cause = f.cause();

              if (cause instanceof ConnectTimeoutException) {
                handshakeFuture.completeExceptionally(
                    new UaException(StatusCodes.Bad_Timeout, f.cause()));
              } else if (cause instanceof ConnectException) {
                handshakeFuture.completeExceptionally(
                    new UaException(StatusCodes.Bad_ConnectionRejected, f.cause()));
              } else {
                handshakeFuture.completeExceptionally(cause);
              }
            }
          });

      // One completion step clears the in-flight references before the channel is published, so
      // disconnect() cannot close a channel the ChannelFsm is about to own.
      return handshakeFuture.handle(
          (secureChannel, error) -> {
            connectingChannel.compareAndSet(connection.channel(), null);
            pendingHandshake.compareAndSet(handshakeFuture, null);
            if (error != null) throw new CompletionException(error);
            if (!connectionRequested) {
              secureChannel.getChannel().close();
              throw new CompletionException(new UaException(StatusCodes.Bad_ConnectionClosed));
            }
            OpcTcpClientTransport.this.secureChannel = secureChannel;
            return secureChannel.getChannel();
          });
    }

    @Override
    public CompletableFuture<Void> disconnect(FsmContext<State, Event> ctx, Channel channel) {
      var disconnectFuture = new CompletableFuture<Void>();

      TimerTask onTimeout =
          t ->
              channel
                  .close()
                  .addListener(
                      (ChannelFutureListener) channelFuture -> disconnectFuture.complete(null));

      Timeout timeout = config.getWheelTimer().newTimeout(onTimeout, 5, TimeUnit.SECONDS);

      channel
          .pipeline()
          .addFirst(
              new ChannelInboundHandlerAdapter() {
                @Override
                public void channelInactive(ChannelHandlerContext channelContext) throws Exception {
                  try (MDC.MDCCloseable ignored = MDC.putCloseable("instance-id", instanceId)) {
                    logger.debug("channelInactive() disconnect complete");
                  }

                  timeout.cancel();
                  disconnectFuture.complete(null);
                  super.channelInactive(channelContext);
                }
              });

      var requestHeader =
          new RequestHeader(
              NodeId.NULL_VALUE, DateTime.now(), uint(0), uint(0), null, uint(0), null);

      try (MDC.MDCCloseable ignored = MDC.putCloseable("instance-id", instanceId)) {
        logger.debug("Sending CloseSecureChannelRequest...");
      }

      channel.pipeline().fireUserEventTriggered(new CloseSecureChannelRequest(requestHeader));

      return disconnectFuture;
    }
  }
}
