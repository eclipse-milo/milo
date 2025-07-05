/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.util.Lazy;
import org.eclipse.milo.opcua.stack.transport.server.AttributeKeys;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.uasc.UascServerHelloHandler;
import org.slf4j.LoggerFactory;

public class OpcTcpServerTransport implements OpcServerTransport {

  private final Set<InetSocketAddress> boundAddresses = new HashSet<>();
  private final Set<Channel> channelReferences = new HashSet<>();

  private final ConcurrentSkipListSet<Channel> childChannelReferences =
      new ConcurrentSkipListSet<>(
          Comparator.comparingLong(
              ch -> {
                Long createdAt = ch.attr(AttributeKeys.CREATED_AT).get();
                return createdAt != null ? createdAt : Long.MIN_VALUE;
              }));

  private final Lazy<ServerBootstrap> serverBootstrap = new Lazy<>();

  private final OpcTcpServerTransportConfig config;

  public OpcTcpServerTransport(OpcTcpServerTransportConfig config) {
    this.config = config;
  }

  @Override
  public synchronized void bind(
      ServerApplicationContext applicationContext, InetSocketAddress bindAddress) throws Exception {

    ServerBootstrap bootstrap =
        serverBootstrap.get(
            () ->
                new ServerBootstrap()
                    .channel(NioServerSocketChannel.class)
                    .group(config.getEventLoop())
                    .handler(new LoggingHandler(OpcTcpServerTransport.class))
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(
                        new ChannelInitializer<SocketChannel>() {
                          @Override
                          protected void initChannel(SocketChannel channel) {
                            channel.attr(AttributeKeys.CREATED_AT).set(System.nanoTime());

                            channel.pipeline().addLast(RateLimitingHandler.getInstance());
                            channel
                                .pipeline()
                                .addLast(
                                    new UascServerHelloHandler(
                                        config,
                                        applicationContext,
                                        TransportProfile.TCP_UASC_UABINARY));

                            config.getChannelPipelineCustomizer().accept(channel.pipeline());

                            if (childChannelReferences.size()
                                < Stack.ConnectionLimits.RATE_LIMIT_MAX_CONNECTIONS) {

                              childChannelReferences.add(channel);
                            } else {
                              Channel oldestChannelWithoutSession =
                                  childChannelReferences.stream()
                                      .filter(c -> !c.hasAttr(AttributeKeys.SESSION_ID))
                                      .findFirst()
                                      .orElse(null);

                              if (oldestChannelWithoutSession != null) {
                                oldestChannelWithoutSession.close();

                                childChannelReferences.add(channel);
                              } else {
                                channel.close();
                              }
                            }

                            channel
                                .closeFuture()
                                .addListener(future -> childChannelReferences.remove(channel));
                          }
                        }));

    assert bootstrap != null;

    config.getBootstrapCustomizer().accept(bootstrap);

    if (!boundAddresses.contains(bindAddress)) {
      ChannelFuture bindFuture = bootstrap.bind(bindAddress).sync();

      boundAddresses.add(bindAddress);
      channelReferences.add(bindFuture.channel());
    }
  }

  @Override
  public synchronized void unbind() {
    boundAddresses.clear();

    channelReferences.forEach(
        channel -> {
          try {
            channel.close().sync();
          } catch (InterruptedException ignored) {
          }
        });
    channelReferences.clear();

    synchronized (childChannelReferences) {
      childChannelReferences.forEach(
          channel -> {
            LoggerFactory.getLogger(getClass()).info("Closing child channel: {}", channel);
            channel.close();
          });
      childChannelReferences.clear();
    }

    serverBootstrap.reset();
  }
}
