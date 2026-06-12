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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;

/**
 * The built-in {@link TransportProvider} for the {@code
 * http://opcfoundation.org/UA-Profile/Transport/pubsub-udp-uadp} transport profile.
 *
 * <p>Channels are Netty {@code NioDatagramChannel}s on the {@code EventLoopGroup} supplied by the
 * transport context. The publisher channel sends connectionless datagrams to the connection's
 * {@code UdpDatagramAddress}; the subscriber channel binds the configured port and, for multicast
 * addresses, joins the group on the configured network interface or on every multicast-capable
 * interface when none is configured.
 */
public final class UdpTransportProvider implements TransportProvider {

  /** The transport profile URI implemented by this provider. */
  public static final String TRANSPORT_PROFILE_URI =
      "http://opcfoundation.org/UA-Profile/Transport/pubsub-udp-uadp";

  /** Create a new {@link UdpTransportProvider}. */
  public UdpTransportProvider() {}

  @Override
  public String transportProfileUri() {
    return TRANSPORT_PROFILE_URI;
  }

  @Override
  public boolean supports(PubSubConnectionConfig connection) {
    return connection instanceof UdpConnectionConfig;
  }

  @Override
  public PublisherChannel openPublisher(PublisherTransportContext context) throws UaException {
    UdpConnectionConfig connection = udpConnection(context.connection());

    return UdpPublisherChannel.open(connection, context.eventLoopGroup());
  }

  @Override
  public SubscriberChannel openSubscriber(SubscriberTransportContext context) throws UaException {
    UdpConnectionConfig connection = udpConnection(context.connection());

    return UdpSubscriberChannel.open(
        connection, context.eventLoopGroup(), context.messageConsumer());
  }

  private static UdpConnectionConfig udpConnection(PubSubConnectionConfig connection)
      throws UaException {

    if (connection instanceof UdpConnectionConfig udpConnection) {
      return udpConnection;
    } else {
      throw new UaException(
          StatusCodes.Bad_InvalidArgument,
          "connection '" + connection.name() + "' is not a UdpConnectionConfig");
    }
  }

  /**
   * Resolve a {@link NetworkInterface} from its name (e.g. "eth0") or from an address assigned to
   * it (e.g. "192.168.1.10").
   *
   * @param nameOrAddress the interface name or an address assigned to the interface.
   * @return the resolved {@link NetworkInterface}.
   * @throws UaException if no matching interface exists or the lookup fails.
   */
  static NetworkInterface resolveNetworkInterface(String nameOrAddress) throws UaException {
    try {
      @Nullable NetworkInterface byName = NetworkInterface.getByName(nameOrAddress);
      if (byName != null) {
        return byName;
      }

      @Nullable NetworkInterface byAddress =
          NetworkInterface.getByInetAddress(InetAddress.getByName(nameOrAddress));
      if (byAddress != null) {
        return byAddress;
      }
    } catch (SocketException | UnknownHostException e) {
      throw new UaException(
          StatusCodes.Bad_ResourceUnavailable,
          "failed to resolve network interface: " + nameOrAddress,
          e);
    }

    throw new UaException(
        StatusCodes.Bad_ResourceUnavailable, "network interface not found: " + nameOrAddress);
  }
}
