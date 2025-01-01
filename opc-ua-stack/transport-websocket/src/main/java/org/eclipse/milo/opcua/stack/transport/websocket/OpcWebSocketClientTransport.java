/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.websocket;

import static java.util.Objects.requireNonNullElse;
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
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import java.net.ConnectException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSecureChannelRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.AbstractUascClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpcWebSocketClientTransport extends AbstractUascClientTransport {

  private static final FsmContext.Key<ClientApplicationContext> KEY_CLIENT_APPLICATION =
      new FsmContext.Key<>("clientApplication", ClientApplicationContext.class);

  private static final String CHANNEL_FSM_LOGGER_NAME =
      "org.eclipse.milo.opcua.stack.client.WebSocketChannelFsm";
  private static final AtomicLong INSTANCE_ID = new AtomicLong();

  private final ChannelFsm channelFsm;

  private final OpcWebSocketClientTransportConfig config;

  public OpcWebSocketClientTransport(OpcWebSocketClientTransportConfig config) {
    super(config);

    this.config = config;

    // TODO use configurable executors
    ChannelFsmConfig fsmConfig =
        ChannelFsmConfig.newBuilder()
            .setLazy(false) // reconnect immediately
            .setMaxIdleSeconds(0) // keep alive handled by SessionFsm
            .setMaxReconnectDelaySeconds(16)
            .setPersistent(true)
            .setChannelActions(new ClientChannelActions(config))
            .setExecutor(Stack.sharedExecutor())
            .setScheduler(Stack.sharedScheduledExecutor())
            .setLoggerName(CHANNEL_FSM_LOGGER_NAME)
            .setLoggingContext(Map.of("instance-id", String.valueOf(INSTANCE_ID.incrementAndGet())))
            .build();

    var factory = new ChannelFsmFactory(fsmConfig);

    channelFsm = factory.newChannelFsm();
  }

  @Override
  public OpcWebSocketClientTransportConfig getConfig() {
    return config;
  }

  @Override
  public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
    channelFsm
        .getFsm()
        .withContext(
            (Consumer<FsmContext<State, Event>>)
                ctx -> ctx.set(KEY_CLIENT_APPLICATION, applicationContext));

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

  private class ClientChannelActions implements ChannelActions {

    private final Logger logger = LoggerFactory.getLogger(CHANNEL_FSM_LOGGER_NAME);

    private final UascClientConfig config;

    private ClientChannelActions(UascClientConfig config) {
      this.config = config;
    }

    @Override
    public CompletableFuture<Channel> connect(FsmContext<State, Event> ctx) {
      ClientApplicationContext application =
          (ClientApplicationContext) ctx.get(KEY_CLIENT_APPLICATION);

      var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

      var bootstrap = new Bootstrap();

      bootstrap
          .channel(NioSocketChannel.class)
          .group(OpcWebSocketClientTransport.this.config.getEventLoop())
          .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
          .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5_000)
          .option(ChannelOption.TCP_NODELAY, true)
          .handler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                  String endpointUrl =
                      requireNonNullElse(application.getEndpoint().getEndpointUrl(), "");
                  String scheme = EndpointUtil.getScheme(endpointUrl);

                  TransportProfile transportProfile =
                      TransportProfile.fromUri(
                          requireNonNullElse(
                              application.getEndpoint().getTransportProfileUri(), ""));

                  String subprotocol;
                  if (transportProfile == TransportProfile.WSS_UASC_UABINARY) {
                    subprotocol = "opcua+cp";
                  } else if (transportProfile == TransportProfile.WSS_UAJSON) {
                    subprotocol = "opcua+uajson";
                  } else {
                    throw new UaException(
                        StatusCodes.Bad_InternalError,
                        "unexpected TransportProfile: " + transportProfile);
                  }

                  if ("opc.wss".equalsIgnoreCase(scheme)) {
                    SslContext sslContext =
                        SslContextBuilder.forClient()
                            .trustManager(InsecureTrustManagerFactory.INSTANCE)
                            .build();

                    channel.pipeline().addLast(sslContext.newHandler(channel.alloc()));
                  }

                  EncodingLimits encodingLimits =
                      application.getEncodingContext().getEncodingLimits();

                  int maxMessageSize = encodingLimits.getMaxMessageSize();

                  channel.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                  channel.pipeline().addLast(new HttpClientCodec());
                  channel.pipeline().addLast(new HttpObjectAggregator(maxMessageSize));

                  channel
                      .pipeline()
                      .addLast(
                          new WebSocketClientProtocolHandler(
                              WebSocketClientHandshakerFactory.newHandshaker(
                                  new URI(endpointUrl),
                                  WebSocketVersion.V13,
                                  subprotocol,
                                  true,
                                  new DefaultHttpHeaders(),
                                  encodingLimits.getMaxChunkSize())));

                  channel
                      .pipeline()
                      .addLast(new WebSocketFrameAggregator(encodingLimits.getMaxMessageSize()));

                  // TODO when/where does the InboundUascResponseHandler get added?
                  // OpcClientWebSocketFrameCodec adds UascClientAcknowledgeHandler when the WS
                  // upgrade is done.
                  var codec =
                      new OpcClientWebSocketBinaryFrameCodec(
                          config, application, requestId::getAndIncrement, handshakeFuture);
                  channel.pipeline().addLast(codec);
                }
              });

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
