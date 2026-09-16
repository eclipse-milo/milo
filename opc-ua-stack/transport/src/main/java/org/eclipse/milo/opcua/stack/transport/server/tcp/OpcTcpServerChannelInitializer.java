/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import io.netty.channel.Channel;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.uasc.UascServerHelloHandler;

/**
 * Installs the server-side UA-TCP UASC pipeline on accepted or already-connected channels.
 *
 * <p>Passive listeners use the same rate limiting and child-channel tracking that {@link
 * OpcTcpServerTransport} has always applied. Future reverse connectors can use the reverse path
 * after they open an outbound socket and send {@code ReverseHello}; from that point the peer is
 * expected to send the normal client {@code Hello} and the channel can rejoin the existing server
 * SecureChannel flow.
 */
final class OpcTcpServerChannelInitializer {

  private OpcTcpServerChannelInitializer() {}

  /**
   * Initializes a child channel accepted by a passive TCP listener.
   *
   * <p>This path preserves listener behavior by installing inbound rate limiting, the normal {@link
   * UascServerHelloHandler}, the configured pipeline customizer, and close-time child-channel
   * cleanup.
   */
  static void initializePassiveChannel(
      Channel channel,
      OpcTcpServerTransportConfig config,
      ServerApplicationContext applicationContext,
      Set<Channel> childChannelReferences) {

    channel.pipeline().addLast(RateLimitingHandler.getInstance());
    initializeHelloChannel(channel, config, applicationContext);

    childChannelReferences.add(channel);
    channel.closeFuture().addListener(future -> childChannelReferences.remove(channel));
  }

  /**
   * Initializes a server-owned outbound channel after {@code ReverseHello} has been sent.
   *
   * <p>Inbound rate limiting is intentionally skipped because this path is for sockets initiated by
   * the server transport rather than accepted from an untrusted remote peer.
   */
  static void initializeReverseChannel(
      Channel channel,
      OpcTcpServerTransportConfig config,
      ServerApplicationContext applicationContext) {

    initializeHelloChannel(channel, config, applicationContext);
  }

  private static void initializeHelloChannel(
      Channel channel,
      OpcTcpServerTransportConfig config,
      ServerApplicationContext applicationContext) {

    channel
        .pipeline()
        .addLast(
            new UascServerHelloHandler(
                config, applicationContext, TransportProfile.TCP_UASC_UABINARY));

    config.getChannelPipelineCustomizer().accept(channel.pipeline());
  }
}
