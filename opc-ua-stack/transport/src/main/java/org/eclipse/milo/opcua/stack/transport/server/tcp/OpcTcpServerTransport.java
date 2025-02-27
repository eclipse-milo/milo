/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.util.Lazy;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.uasc.UascServerHelloHandler;

public class OpcTcpServerTransport implements OpcServerTransport {

  private final Map<InetSocketAddress, Channel> serverChannelMap = new HashMap<>();

  private final Map<InetSocketAddress, Set<Channel>> childChannelMap =
      Collections.synchronizedMap(new HashMap<>());

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
                            channel.pipeline().addLast(RateLimitingHandler.getInstance());
                            channel
                                .pipeline()
                                .addLast(
                                    new UascServerHelloHandler(
                                        config,
                                        applicationContext,
                                        TransportProfile.TCP_UASC_UABINARY));

                            Set<Channel> channels =
                                childChannelMap.computeIfAbsent(bindAddress, k -> new HashSet<>());

                            channels.add(channel);

                            channel
                                .closeFuture()
                                .addListener(
                                    future -> {
                                      Set<Channel> cs = childChannelMap.get(bindAddress);
                                      if (cs != null) cs.remove(channel);
                                    });
                          }
                        }));

    assert bootstrap != null;

    if (!serverChannelMap.containsKey(bindAddress)) {
      ChannelFuture bindFuture = bootstrap.bind(bindAddress).sync();

      serverChannelMap.put(bindAddress, bindFuture.channel());
    }
  }

  @Override
  public synchronized void unbind(InetSocketAddress address) throws Exception {
    Channel channel = serverChannelMap.remove(address);

    if (channel != null) {
      channel.close().sync();
    }

    Set<Channel> childChannels = childChannelMap.remove(address);

    if (childChannels != null) {
      for (Channel childChannel : childChannels) {
        childChannel.close().sync();
      }
    }
  }

  @Override
  public synchronized void unbindAll() {
    serverChannelMap.forEach(
        (address, channel) -> {
          try {
            channel.close().sync();
          } catch (InterruptedException ignored) {
          }
        });

    serverChannelMap.clear();

    childChannelMap.forEach(
        (address, channels) -> {
          channels.forEach(
              channel -> {
                try {
                  channel.close().sync();
                } catch (InterruptedException ignored) {
                }
              });
        });

    childChannelMap.clear();

    serverBootstrap.reset();
  }
}
