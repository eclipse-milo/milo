/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * The datagram address of a {@link UdpConnectionConfig}: a multicast group or unicast host plus
 * port, with an optional network interface restriction.
 *
 * <p>Instances are immutable; {@link #networkInterface(String)} returns a new instance.
 */
public final class UdpDatagramAddress {

  private final String host;
  private final int port;
  private final boolean multicast;
  private final @Nullable String networkInterface;

  private UdpDatagramAddress(
      String host, int port, boolean multicast, @Nullable String networkInterface) {

    this.host = host;
    this.port = port;
    this.multicast = multicast;
    this.networkInterface = networkInterface;
  }

  /**
   * Create a multicast address.
   *
   * @param group the multicast group address, e.g. "224.0.2.14".
   * @param port the UDP port, in the range [1, 65535].
   * @return a new multicast {@link UdpDatagramAddress}.
   * @throws PubSubConfigValidationException if {@code group} is blank or {@code port} is out of
   *     range.
   */
  public static UdpDatagramAddress multicast(String group, int port) {
    validate(group, "group", port);

    return new UdpDatagramAddress(group, port, true, null);
  }

  /**
   * Create a unicast address.
   *
   * @param host the host name or address.
   * @param port the UDP port, in the range [1, 65535].
   * @return a new unicast {@link UdpDatagramAddress}.
   * @throws PubSubConfigValidationException if {@code host} is blank or {@code port} is out of
   *     range.
   */
  public static UdpDatagramAddress unicast(String host, int port) {
    validate(host, "host", port);

    return new UdpDatagramAddress(host, port, false, null);
  }

  private static void validate(String host, String hostLabel, int port) {
    if (host.isBlank()) {
      throw new PubSubConfigValidationException(
          "UDP datagram address: " + hostLabel + " must not be blank");
    }
    if (port < 1 || port > 65535) {
      throw new PubSubConfigValidationException(
          "UDP datagram address '" + host + "': port must be in the range [1, 65535]: " + port);
    }
  }

  /**
   * Create a copy of this address restricted to a network interface.
   *
   * @param interfaceName the name or address of the network interface to send or receive on.
   * @return a new {@link UdpDatagramAddress} with the network interface set.
   */
  public UdpDatagramAddress networkInterface(String interfaceName) {
    return new UdpDatagramAddress(host, port, multicast, interfaceName);
  }

  /**
   * Check if this is a multicast address.
   *
   * @return {@code true} if this is a multicast address, {@code false} if unicast.
   */
  public boolean isMulticast() {
    return multicast;
  }

  /**
   * Get the host name or address; the group address if this is a multicast address.
   *
   * @return the host name or address.
   */
  public String host() {
    return host;
  }

  /**
   * Get the UDP port.
   *
   * @return the UDP port.
   */
  public int port() {
    return port;
  }

  /**
   * Get the network interface restriction, if configured.
   *
   * @return the network interface name or address, or {@code null} if unrestricted.
   */
  public @Nullable String networkInterface() {
    return networkInterface;
  }

  /**
   * Get this address as an OPC UA datagram URL.
   *
   * @return the address in "opc.udp://host:port" form.
   */
  public String url() {
    return "opc.udp://" + host + ":" + port;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UdpDatagramAddress that)) {
      return false;
    }
    return host.equals(that.host)
        && port == that.port
        && multicast == that.multicast
        && Objects.equals(networkInterface, that.networkInterface);
  }

  @Override
  public int hashCode() {
    return Objects.hash(host, port, multicast, networkInterface);
  }

  @Override
  public String toString() {
    return "UdpDatagramAddress["
        + (multicast ? "multicast" : "unicast")
        + " "
        + url()
        + (networkInterface != null ? " via " + networkInterface : "")
        + "]";
  }
}
