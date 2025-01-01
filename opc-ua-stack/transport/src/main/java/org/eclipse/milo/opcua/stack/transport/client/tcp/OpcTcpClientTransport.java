/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSecureChannelRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.AbstractUascClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.eclipse.milo.opcua.stack.transport.client.uasc.InboundUascResponseHandler.DelegatingUascResponseHandler;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientAcknowledgeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpcTcpClientTransport extends AbstractUascClientTransport {

  private static final FsmContext.Key<ClientApplicationContext> KEY_CLIENT_APPLICATION =
      new FsmContext.Key<>("clientApplication", ClientApplicationContext.class);

  private static final String CHANNEL_FSM_LOGGER_NAME =
      "org.eclipse.milo.opcua.stack.client.ChannelFsm";
  private static final AtomicLong INSTANCE_ID = new AtomicLong();

  private final ChannelFsm channelFsm;

  private final OpcTcpClientTransportConfig config;

  public OpcTcpClientTransport(OpcTcpClientTransportConfig config) {
    super(config);

    this.config = config;

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
            .setLoggingContext(Map.of("instance-id", String.valueOf(INSTANCE_ID.incrementAndGet())))
            .build();

    var factory = new ChannelFsmFactory(fsmConfig);

    channelFsm = factory.newChannelFsm();
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
    return channelFsm.disconnect().thenApply(v -> Unit.VALUE);
  }

  @Override
  protected CompletableFuture<Channel> getChannel() {
    return channelFsm.getChannel();
  }

  public ChannelFsm getChannelFsm() {
    return channelFsm;
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
                  var acknowledgeHandler =
                      new UascClientAcknowledgeHandler(
                          config, application, requestId::getAndIncrement, handshakeFuture);

                  ch.pipeline()
                      .addLast(new DelegatingUascResponseHandler(OpcTcpClientTransport.this));
                  ch.pipeline().addLast(acknowledgeHandler);

                  config.getChannelPipelineCustomizer().accept(ch.pipeline());
                }
              });

      config.getBootstrapCustomizer().accept(bootstrap);

      String endpointUrl = application.getEndpoint().getEndpointUrl();

      String host = EndpointUtil.getHost(endpointUrl);
      assert host != null;

      int port = EndpointUtil.getPort(endpointUrl);

      bootstrap
          .connect(host, port)
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

      return handshakeFuture.thenApply(ClientSecureChannel::getChannel);
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
                  // TODO MDC?
                  logger.debug("channelInactive() disconnect complete");
                  timeout.cancel();
                  disconnectFuture.complete(null);
                  super.channelInactive(channelContext);
                }
              });

      var requestHeader =
          new RequestHeader(
              NodeId.NULL_VALUE, DateTime.now(), uint(0), uint(0), null, uint(0), null);

      // TODO MDC?
      logger.debug("Sending CloseSecureChannelRequest...");

      channel.pipeline().fireUserEventTriggered(new CloseSecureChannelRequest(requestHeader));

      return disconnectFuture;
    }
  }
}
