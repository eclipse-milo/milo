/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.uasc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;

/**
 * Netty channel handler for server-initiated Reverse Connect channels.
 *
 * <p>On {@link #channelActive(ChannelHandlerContext)}, sends a {@code ReverseHello} message
 * containing the server's URI and endpoint URL, then waits for the client's {@code Hello} — the
 * inverse of {@link UascServerHelloHandler}, which waits for {@code Hello} on channel activation.
 *
 * <p>After the {@code Hello}/{@code Ack} exchange, the pipeline evolves identically to a normal
 * connection.
 */
public class UascServerReverseHelloHandler extends UascServerHelloHandler {

  private final String serverUri;
  private final String endpointUrl;

  /**
   * @param config the server configuration.
   * @param application the server application context.
   * @param transportProfile the transport profile for this connection.
   * @param serverUri the ApplicationUri of the server.
   * @param endpointUrl the endpoint URL the client should use in its Hello message.
   */
  public UascServerReverseHelloHandler(
      UascServerConfig config,
      ServerApplicationContext application,
      TransportProfile transportProfile,
      String serverUri,
      String endpointUrl) {

    super(config, application, transportProfile);
    this.serverUri = serverUri;
    this.endpointUrl = endpointUrl;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    var rhe = new ReverseHelloMessage(serverUri, endpointUrl);
    ByteBuf rheBuffer = TcpMessageEncoder.encode(rhe);
    ctx.writeAndFlush(rheBuffer);

    // Start the Hello deadline timer (inherited behavior).
    // super.channelActive() schedules the "no Hello received" deadline.
    super.channelActive(ctx);
  }
}
