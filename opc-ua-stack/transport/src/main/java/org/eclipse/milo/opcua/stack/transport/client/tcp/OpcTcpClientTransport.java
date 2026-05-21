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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
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
    channelFsm.getFsm().withContext(ctx -> ctx.set(KEY_CLIENT_APPLICATION, applicationContext));

    return channelFsm.connect().thenApply(c -> Unit.VALUE);
  }

  @Override
  public CompletableFuture<Unit> disconnect() {
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

  private void notifyTransitionListeners(boolean connected) {
    for (ChannelStateObservable.TransitionListener listener : transitionListeners) {
      listener.onStateTransition(connected);
    }
  }

  private class ClientChannelActions implements ChannelActions {

    private final Logger logger = LoggerFactory.getLogger(CHANNEL_FSM_LOGGER_NAME);

    @Override
    public CompletableFuture<Channel> connect(FsmContext<State, Event> ctx) {
      ClientApplicationContext application =
          (ClientApplicationContext) ctx.get(KEY_CLIENT_APPLICATION);

      var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

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

      bootstrap
          .connect(new InetSocketAddress(host, port))
          .addListener(
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

      return handshakeFuture.thenApply(
          secureChannel -> {
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
