/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server;

import io.netty.channel.Channel;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;

/** Transport layer details that accompany an inbound {@link UaRequestMessageType}. */
public interface ServiceRequestContext {

  /**
   * Get the endpoint URL of the endpoint the client that originated the request is connected to.
   *
   * @return the endpoint URL of the endpoint the client that originated the request is connected
   *     to.
   */
  String getEndpointUrl();

  /**
   * Get the {@link TransportProfile} of the transport the client that originated the request used
   * to connect.
   *
   * @return the {@link TransportProfile} of the transport the client that originated the request
   *     used to connect.
   */
  TransportProfile getTransportProfile();

  /**
   * Get the Netty {@link Channel} the request originated from.
   *
   * @return the Netty {@link Channel} the request originated from.
   */
  Channel getChannel();

  /**
   * Get the {@link SecureChannel} the request originated from.
   *
   * @return the {@link SecureChannel} the request originated from.
   */
  SecureChannel getSecureChannel();

  /**
   * Get the system time, in nanos, that this request was received at.
   *
   * @return the system time, in nanos, that this request was received at.
   * @see System#nanoTime()
   */
  Long receivedAtNanos();

  /**
   * Get the {@link InetAddress} of the client that originated the request.
   *
   * @return the {@link InetAddress} of the client that originated the request.
   */
  default InetAddress clientAddress() {
    return ((InetSocketAddress) getChannel().remoteAddress()).getAddress();
  }
}
