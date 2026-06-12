/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.transport.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.nio.NioDatagramChannel;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * A {@link PublisherChannel} that sends connectionless UDP datagrams to the connection's {@link
 * UdpDatagramAddress}.
 *
 * <p>The channel binds an ephemeral local port and addresses every datagram to the configured host
 * and port. For multicast destinations a multicast TTL is applied, and the egress interface is set
 * when the address configures a network interface.
 *
 * <p>Ownership of buffers passed to {@link #send(ByteBuf)} transfers to the channel: Netty releases
 * the buffer once the write completes, whether successfully or not.
 */
final class UdpPublisherChannel implements PublisherChannel {

  /**
   * TTL applied to multicast datagrams, chosen to allow routed multicast without letting datagrams
   * circulate indefinitely. The OS default of 1 would restrict traffic to the local link.
   */
  private static final int MULTICAST_TTL = 64;

  private final Channel channel;
  private final InetSocketAddress remoteAddress;

  private UdpPublisherChannel(Channel channel, InetSocketAddress remoteAddress) {
    this.channel = channel;
    this.remoteAddress = remoteAddress;
  }

  @Override
  public CompletableFuture<Void> send(ByteBuf message) {
    var future = new CompletableFuture<Void>();

    channel
        .writeAndFlush(new DatagramPacket(message, remoteAddress))
        .addListener(
            (ChannelFutureListener)
                f -> {
                  if (f.isSuccess()) {
                    future.complete(null);
                  } else {
                    future.completeExceptionally(f.cause());
                  }
                });

    return future;
  }

  @Override
  public CompletableFuture<Void> closeAsync() {
    var future = new CompletableFuture<Void>();

    channel
        .close()
        .addListener(
            (ChannelFutureListener)
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
   * Open a new {@link UdpPublisherChannel} for {@code connection}.
   *
   * @param connection the connection config supplying the destination address.
   * @param eventLoopGroup the {@link EventLoopGroup} the channel uses for I/O.
   * @return an open {@link UdpPublisherChannel}.
   * @throws UaException if the destination is unresolvable, the configured network interface does
   *     not exist, or the local bind fails.
   */
  static UdpPublisherChannel open(UdpConnectionConfig connection, EventLoopGroup eventLoopGroup)
      throws UaException {

    UdpDatagramAddress address = connection.getAddress();

    var remoteAddress = new InetSocketAddress(address.host(), address.port());
    if (remoteAddress.isUnresolved()) {
      throw new UaException(
          StatusCodes.Bad_CommunicationError, "unresolvable address: " + address.url());
    }

    Bootstrap bootstrap =
        new Bootstrap()
            .group(eventLoopGroup)
            .handler(
                new SimpleChannelInboundHandler<DatagramPacket>() {
                  @Override
                  protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
                    // Inbound datagrams arriving on the publisher's ephemeral port are ignored;
                    // the packet is released by SimpleChannelInboundHandler.
                  }
                });

    if (address.isMulticast()) {
      InternetProtocolFamily family =
          remoteAddress.getAddress() instanceof Inet6Address
              ? InternetProtocolFamily.IPv6
              : InternetProtocolFamily.IPv4;

      bootstrap.channelFactory(() -> new NioDatagramChannel(family));
      bootstrap.option(ChannelOption.IP_MULTICAST_TTL, MULTICAST_TTL);

      String networkInterface = address.networkInterface();
      if (networkInterface != null) {
        bootstrap.option(
            ChannelOption.IP_MULTICAST_IF,
            UdpTransportProvider.resolveNetworkInterface(networkInterface));
      }
    } else {
      bootstrap.channel(NioDatagramChannel.class);
    }

    ChannelFuture bindFuture = bootstrap.bind(0).awaitUninterruptibly();

    if (bindFuture.isSuccess()) {
      return new UdpPublisherChannel(bindFuture.channel(), remoteAddress);
    } else {
      throw new UaException(
          StatusCodes.Bad_CommunicationError,
          "failed to open UDP publisher channel for " + address.url(),
          bindFuture.cause());
    }
  }
}
