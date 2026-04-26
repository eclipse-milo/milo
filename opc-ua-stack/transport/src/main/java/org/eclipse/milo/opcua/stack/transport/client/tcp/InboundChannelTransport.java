/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.tcp;

import io.netty.channel.Channel;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.AbstractUascClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.eclipse.milo.opcua.stack.transport.client.uasc.InboundUascResponseHandler.DelegatingUascResponseHandler;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientReverseHelloHandler;

/**
 * An {@link AbstractUascClientTransport} subclass that wraps an already-connected inbound Netty
 * {@link Channel} and a pre-decoded {@link ReverseHelloMessage} for short-lived discovery.
 *
 * <p>This transport drives the Hello/Ack/OpenSecureChannel handshake on the inbound channel. It is
 * used by the {@code Discovery} lambda in {@code MultiplexedReverseConnectListener} to perform
 * GetEndpoints discovery on the channel.
 */
public class InboundChannelTransport extends AbstractUascClientTransport {

  private final OpcTcpMultiplexedReverseConnectTransportConfig transportConfig;
  private final Channel channel;
  private final ReverseHelloMessage reverseHello;

  private volatile CompletableFuture<ClientSecureChannel> handshakeFuture;

  public InboundChannelTransport(
      OpcTcpMultiplexedReverseConnectTransportConfig transportConfig,
      Channel channel,
      ReverseHelloMessage reverseHello) {

    super(transportConfig);
    this.transportConfig = transportConfig;
    this.channel = channel;
    this.reverseHello = reverseHello;
  }

  @Override
  public OpcClientTransportConfig getConfig() {
    return transportConfig;
  }

  @Override
  public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
    var hf = new CompletableFuture<ClientSecureChannel>();
    this.handshakeFuture = hf;

    // Safety net: complete handshake future if channel dies mid-handshake.
    channel
        .closeFuture()
        .addListener(
            f -> {
              if (!hf.isDone()) {
                hf.completeExceptionally(new Exception("channel closed during handshake"));
              }
            });

    // Pipeline mutation must run on the channel's event loop; this transport starts from an
    // already-accepted channel, so it cannot rely on the normal client Bootstrap path to do it.
    channel
        .eventLoop()
        .execute(
            () -> {
              channel.pipeline().addLast(new DelegatingUascResponseHandler(this));

              var handler =
                  new UascClientReverseHelloHandler(
                      transportConfig,
                      applicationContext,
                      requestId::getAndIncrement,
                      hf,
                      Set.of(), // allowedServerUris: empty = accept all (already validated)
                      0L); // reverseHelloTimeoutMs: 0 = no timeout (already received)

              channel.pipeline().addLast(handler);

              try {
                handler.onReverseHello(channel.pipeline().context(handler), reverseHello);
              } catch (Exception e) {
                hf.completeExceptionally(e);
                channel.close();
              }
            });

    return hf.thenApply(sc -> Unit.VALUE);
  }

  @Override
  public CompletableFuture<Unit> disconnect() {
    channel.close();
    return CompletableFuture.completedFuture(Unit.VALUE);
  }

  @Override
  protected CompletableFuture<Channel> getChannel() {
    CompletableFuture<ClientSecureChannel> hf = handshakeFuture;
    if (hf != null) {
      return hf.thenApply(ClientSecureChannel::getChannel);
    }
    return CompletableFuture.failedFuture(new IllegalStateException("not connected"));
  }
}
