/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.https;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.concurrent.FutureListener;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpcHttpClientTransport implements OpcClientTransport {

  private static final Logger LOGGER = LoggerFactory.getLogger(OpcHttpClientTransport.class);

  private ChannelPool channelPool = null;

  private final OpcClientTransportConfig config;

  public OpcHttpClientTransport(OpcClientTransportConfig config) {
    this.config = config;
  }

  @Override
  public OpcClientTransportConfig getConfig() {
    return config;
  }

  @Override
  public synchronized CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
    if (channelPool == null) {
      channelPool = createChannelPool(config, applicationContext);
    }

    return CompletableFuture.completedFuture(Unit.VALUE);
  }

  @Override
  public synchronized CompletableFuture<Unit> disconnect() {
    if (channelPool != null) {
      channelPool.close();
      channelPool = null;
    }

    return CompletableFuture.completedFuture(Unit.VALUE);
  }

  @Override
  public synchronized CompletableFuture<UaResponseMessageType> sendRequestMessage(
      UaRequestMessageType request) {
    LOGGER.trace("sendRequest({})", request.getClass().getSimpleName());

    return acquireChannel()
        .thenCompose(
            channel ->
                sendRequestMessage(request, channel)
                    .whenComplete((response, ex) -> releaseChannel(channel)));
  }

  private CompletableFuture<UaResponseMessageType> sendRequestMessage(
      UaRequestMessageType request, Channel channel) {
    return CompletableFuture.failedFuture(new RuntimeException("TODO")); // TODO
  }

  private synchronized CompletableFuture<Channel> acquireChannel() {
    if (channelPool == null) {
      return CompletableFuture.failedFuture(new Exception("not connected"));
    }

    CompletableFuture<Channel> future = new CompletableFuture<>();

    channelPool
        .acquire()
        .addListener(
            (FutureListener<Channel>)
                cf -> {
                  if (cf.isSuccess()) {
                    future.complete(cf.getNow());
                  } else {
                    future.completeExceptionally(cf.cause());
                  }
                });

    return future;
  }

  private synchronized void releaseChannel(Channel channel) {
    if (channelPool != null) {
      channelPool.release(channel);
    }
  }

  private static ChannelPool createChannelPool(
      OpcClientTransportConfig config, ClientApplicationContext application) {
    final String endpointUrl = application.getEndpoint().getEndpointUrl();

    String host = EndpointUtil.getHost(endpointUrl);
    if (host == null) host = "";

    int port = EndpointUtil.getPort(endpointUrl);

    LOGGER.debug("createChannelPool() host={} port={}", host, port);

    Bootstrap bootstrap =
        new Bootstrap()
            .channel(NioSocketChannel.class)
            .group(config.getEventLoop())
            .remoteAddress(host, port);

    return new SimpleChannelPool(
        bootstrap,
        new AbstractChannelPoolHandler() {
          @Override
          public void channelCreated(Channel channel) throws Exception {
            String scheme = EndpointUtil.getScheme(endpointUrl);

            if ("https".equalsIgnoreCase(scheme) || "opc.https".equalsIgnoreCase(scheme)) {
              SslContext sslContext =
                  SslContextBuilder.forClient()
                      .trustManager(InsecureTrustManagerFactory.INSTANCE)
                      .build();

              channel.pipeline().addLast(sslContext.newHandler(channel.alloc()));
            }

            int maxMessageSize =
                application.getEncodingContext().getEncodingLimits().getMaxMessageSize();

            channel.pipeline().addLast(new LoggingHandler(LogLevel.TRACE));
            channel.pipeline().addLast(new HttpClientCodec());
            channel.pipeline().addLast(new HttpObjectAggregator(maxMessageSize));
            channel.pipeline().addLast(new OpcClientHttpCodec(config, application));

            LOGGER.debug("channelCreated(): " + channel);
          }

          @Override
          public void channelAcquired(Channel channel) {
            LOGGER.debug("channelAcquired(): " + channel);
          }

          @Override
          public void channelReleased(Channel channel) {
            LOGGER.debug("channelReleased(): " + channel);
          }
        });
  }
}
