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
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.nio.NioDatagramChannel;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link SubscriberChannel} that receives UDP datagrams for the connection's {@link
 * UdpDatagramAddress}.
 *
 * <p>Unicast addresses are bound directly on host:port; multicast addresses bind the wildcard
 * address on the configured port and join the group on the configured network interface, or on
 * every multicast-capable interface when none is configured. The port is always bound with {@code
 * SO_REUSEADDR} so multiple subscribers can share it.
 *
 * <p>Each received datagram's content is delivered to the message consumer on the channel's event
 * loop thread. The buffer is not retained for the consumer: it is valid only for the duration of
 * the callback and is released by the channel after the consumer returns, so the consumer must
 * {@code retain()} it to keep it longer and must never {@code release()} it.
 *
 * <p>The per-read receive buffer is fixed at {@value #MAX_DATAGRAM_SIZE} bytes so any datagram a
 * conforming publisher can emit is read intact; without it Netty's 2048-byte datagram default
 * silently truncates larger datagrams on read.
 */
final class UdpSubscriberChannel implements SubscriberChannel {

  /**
   * The largest UDP datagram a conforming publisher can emit: OPC UA Part 14 §7.3.2.1, "For OPC UA
   * UDP the MaxNetworkMessageSize plus additional headers shall be limited to 65535 Byte."
   */
  private static final int MAX_DATAGRAM_SIZE = 65535;

  private static final Logger LOGGER = LoggerFactory.getLogger(UdpSubscriberChannel.class);

  private final Channel channel;

  private UdpSubscriberChannel(Channel channel) {
    this.channel = channel;
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
   * Open a new {@link UdpSubscriberChannel} for {@code connection}.
   *
   * @param connection the connection config supplying the address to receive on.
   * @param eventLoopGroup the {@link EventLoopGroup} the channel uses for I/O.
   * @param messageConsumer the consumer received datagram contents are delivered to.
   * @return an open {@link UdpSubscriberChannel}.
   * @throws UaException if the address is unresolvable, the bind fails, or no multicast group
   *     membership could be established.
   */
  static UdpSubscriberChannel open(
      UdpConnectionConfig connection,
      EventLoopGroup eventLoopGroup,
      Consumer<ByteBuf> messageConsumer)
      throws UaException {

    UdpDatagramAddress address = connection.getAddress();

    if (address.isMulticast()) {
      return openMulticast(address, eventLoopGroup, messageConsumer);
    } else {
      return openUnicast(address, eventLoopGroup, messageConsumer);
    }
  }

  private static UdpSubscriberChannel openUnicast(
      UdpDatagramAddress address, EventLoopGroup eventLoopGroup, Consumer<ByteBuf> messageConsumer)
      throws UaException {

    var bindAddress = new InetSocketAddress(address.host(), address.port());
    if (bindAddress.isUnresolved()) {
      throw new UaException(
          StatusCodes.Bad_CommunicationError, "unresolvable address: " + address.url());
    }

    Bootstrap bootstrap =
        new Bootstrap()
            .group(eventLoopGroup)
            .channel(NioDatagramChannel.class)
            .option(ChannelOption.SO_REUSEADDR, true)
            .option(
                ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(MAX_DATAGRAM_SIZE))
            .handler(new MessageHandler(messageConsumer));

    ChannelFuture bindFuture = bootstrap.bind(bindAddress).awaitUninterruptibly();

    if (bindFuture.isSuccess()) {
      return new UdpSubscriberChannel(bindFuture.channel());
    } else {
      throw new UaException(
          StatusCodes.Bad_CommunicationError,
          "failed to bind UDP subscriber channel on " + address.url(),
          bindFuture.cause());
    }
  }

  private static UdpSubscriberChannel openMulticast(
      UdpDatagramAddress address, EventLoopGroup eventLoopGroup, Consumer<ByteBuf> messageConsumer)
      throws UaException {

    InetAddress groupAddress;
    try {
      groupAddress = InetAddress.getByName(address.host());
    } catch (UnknownHostException e) {
      throw new UaException(
          StatusCodes.Bad_CommunicationError, "unresolvable address: " + address.url(), e);
    }

    InternetProtocolFamily family =
        groupAddress instanceof Inet6Address
            ? InternetProtocolFamily.IPv6
            : InternetProtocolFamily.IPv4;

    List<NetworkInterface> networkInterfaces;
    String networkInterface = address.networkInterface();
    if (networkInterface != null) {
      networkInterfaces = List.of(UdpTransportProvider.resolveNetworkInterface(networkInterface));
    } else {
      networkInterfaces = multicastCapableInterfaces(family);
      if (networkInterfaces.isEmpty()) {
        throw new UaException(
            StatusCodes.Bad_ResourceUnavailable,
            "no multicast-capable network interfaces available for " + address.url());
      }
    }

    Bootstrap bootstrap =
        new Bootstrap()
            .group(eventLoopGroup)
            .channelFactory(() -> new NioDatagramChannel(family))
            .option(ChannelOption.SO_REUSEADDR, true)
            .option(
                ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(MAX_DATAGRAM_SIZE))
            .handler(new MessageHandler(messageConsumer));

    // Bind the wildcard address rather than the group address; binding a multicast group
    // address is not supported on all platforms.
    ChannelFuture bindFuture =
        bootstrap.bind(new InetSocketAddress(address.port())).awaitUninterruptibly();

    if (!bindFuture.isSuccess()) {
      throw new UaException(
          StatusCodes.Bad_CommunicationError,
          "failed to bind UDP subscriber channel on " + address.url(),
          bindFuture.cause());
    }

    var channel = (NioDatagramChannel) bindFuture.channel();
    var groupSocketAddress = new InetSocketAddress(groupAddress, address.port());

    int joined = 0;
    for (NetworkInterface ni : networkInterfaces) {
      ChannelFuture joinFuture = channel.joinGroup(groupSocketAddress, ni).awaitUninterruptibly();

      if (joinFuture.isSuccess()) {
        joined++;
      } else {
        LOGGER.warn(
            "failed to join multicast group {} on interface {}",
            address.url(),
            ni.getName(),
            joinFuture.cause());
      }
    }

    if (joined == 0) {
      channel.close().awaitUninterruptibly();
      throw new UaException(
          StatusCodes.Bad_CommunicationError,
          "failed to join multicast group " + address.url() + " on any network interface");
    }

    return new UdpSubscriberChannel(channel);
  }

  /**
   * Enumerate the network interfaces that are up, support multicast, and have at least one address
   * of {@code family}.
   */
  private static List<NetworkInterface> multicastCapableInterfaces(InternetProtocolFamily family)
      throws UaException {

    var networkInterfaces = new ArrayList<NetworkInterface>();

    try {
      Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();

      while (e.hasMoreElements()) {
        NetworkInterface ni = e.nextElement();

        try {
          if (ni.isUp() && ni.supportsMulticast() && hasAddressOfFamily(ni, family)) {
            networkInterfaces.add(ni);
          }
        } catch (SocketException ex) {
          LOGGER.debug("skipping network interface {}", ni.getName(), ex);
        }
      }
    } catch (SocketException e) {
      throw new UaException(
          StatusCodes.Bad_ResourceUnavailable, "failed to enumerate network interfaces", e);
    }

    return networkInterfaces;
  }

  private static boolean hasAddressOfFamily(
      NetworkInterface networkInterface, InternetProtocolFamily family) {

    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

    while (addresses.hasMoreElements()) {
      InetAddress address = addresses.nextElement();

      if (family == InternetProtocolFamily.IPv4
          ? address instanceof Inet4Address
          : address instanceof Inet6Address) {
        return true;
      }
    }

    return false;
  }

  /**
   * Delivers received datagram contents to the message consumer.
   *
   * <p>{@link SimpleChannelInboundHandler} releases the datagram (and its content buffer) after
   * {@link #channelRead0} returns, enforcing the ownership rule documented on {@link
   * UdpSubscriberChannel}.
   */
  private static final class MessageHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private final Consumer<ByteBuf> messageConsumer;

    private MessageHandler(Consumer<ByteBuf> messageConsumer) {
      this.messageConsumer = messageConsumer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
      try {
        messageConsumer.accept(packet.content());
      } catch (Throwable t) {
        LOGGER.warn("message consumer threw an exception", t);
      }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
      LOGGER.warn("exception in UDP subscriber channel", cause);
    }
  }
}
