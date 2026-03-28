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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.AbstractUascClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;

/**
 * An {@link org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport} that receives its
 * channels from a shared {@link ChannelConsumerRegistry} instead of owning a {@code
 * ServerBootstrap}.
 *
 * <p>Each instance manages a single reverse-connected channel via a {@link
 * ReverseConnectChannelFsm}. The transport registers itself with the registry on {@link
 * #connect(ClientApplicationContext)} and deregisters on {@link #disconnect()}.
 */
public class OpcTcpMultiplexedReverseConnectTransport extends AbstractUascClientTransport
    implements ChannelStateObservable {

  private final String serverUri;
  private final OpcTcpMultiplexedReverseConnectTransportConfig config;
  private final ChannelConsumerRegistry registry;
  private final ReverseConnectChannelFsm channelFsm;
  private final ChannelConsumerRegistry.ChannelConsumer channelConsumer;

  private final CopyOnWriteArrayList<ChannelStateObservable.TransitionListener> stateListeners =
      new CopyOnWriteArrayList<>();

  record PendingChannel(Channel channel, ReverseHelloMessage reverseHello) {}

  private final AtomicReference<PendingChannel> pendingChannel = new AtomicReference<>();

  /**
   * Create a new {@link OpcTcpMultiplexedReverseConnectTransport}.
   *
   * @param serverUri the ServerUri this transport is registered for.
   * @param config the transport configuration.
   * @param registry the registry that will dispatch channels to this transport.
   */
  public OpcTcpMultiplexedReverseConnectTransport(
      String serverUri,
      OpcTcpMultiplexedReverseConnectTransportConfig config,
      ChannelConsumerRegistry registry) {

    super(config);

    this.serverUri = serverUri;
    this.config = config;
    this.registry = registry;

    var fsmConfig =
        new ReverseConnectChannelFsm.ChannelFsmConfig(
            config, config.getAllowedServerUris(), config.getReverseHelloTimeout());

    this.channelFsm =
        ReverseConnectChannelFsm.create(
            fsmConfig, this, requestId::getAndIncrement, config.getExecutor());

    this.channelConsumer = new ChannelConsumerRegistry.ChannelConsumer(channelFsm, this);

    channelFsm.addTransitionListener(
        (from, to) -> {
          if (from == ReverseConnectChannelFsm.State.Connected
              && to != ReverseConnectChannelFsm.State.Connected) {
            stateListeners.forEach(l -> l.onConnectionStateChange(false));
          } else if (from != ReverseConnectChannelFsm.State.Connected
              && to == ReverseConnectChannelFsm.State.Connected) {
            stateListeners.forEach(l -> l.onConnectionStateChange(true));
          }
        });
  }

  @Override
  public OpcTcpMultiplexedReverseConnectTransportConfig getConfig() {
    return config;
  }

  /**
   * Get the ServerUri this transport is registered for.
   *
   * @return the server URI.
   */
  public String getServerUri() {
    return serverUri;
  }

  /**
   * Get the {@link ReverseConnectChannelFsm} used by this transport.
   *
   * @return the {@link ReverseConnectChannelFsm}.
   */
  public ReverseConnectChannelFsm getChannelFsm() {
    return channelFsm;
  }

  /**
   * Offer a channel for reuse when {@link #connect} is called.
   *
   * <p>If the channel becomes inactive before {@code connect()} consumes it, the offer is
   * automatically withdrawn.
   *
   * @param channel the accepted channel.
   * @param reverseHello the ReverseHello message received on the channel.
   */
  public void offerChannel(Channel channel, ReverseHelloMessage reverseHello) {
    var pending = new PendingChannel(channel, reverseHello);
    pendingChannel.set(pending);

    channel.closeFuture().addListener(f -> pendingChannel.compareAndSet(pending, null));
  }

  @Override
  public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
    channelFsm.setApplicationContext(applicationContext);

    registry.register(serverUri, channelConsumer);

    var connect = new ReverseConnectChannelFsm.Event.Connect(new CompletableFuture<>());
    channelFsm.fireEvent(connect);

    // If a channel was offered before connect() was called (1-shot path),
    // consume it and fire ConnectionAccepted immediately.
    PendingChannel pending = pendingChannel.getAndSet(null);
    if (pending != null && pending.channel().isActive()) {
      channelFsm.fireEvent(
          new ReverseConnectChannelFsm.Event.ConnectionAccepted(
              pending.channel(), pending.reverseHello()));
    }

    return connect.future().thenApply(ch -> Unit.VALUE);
  }

  @Override
  public CompletableFuture<Unit> disconnect() {
    var disconnect = new ReverseConnectChannelFsm.Event.Disconnect(new CompletableFuture<>());
    channelFsm.fireEvent(disconnect);

    return disconnect
        .future()
        .thenApply(
            v -> {
              registry.deregister(serverUri, channelConsumer);
              return Unit.VALUE;
            });
  }

  @Override
  protected CompletableFuture<Channel> getChannel() {
    Channel ch = channelFsm.getChannel();
    if (ch != null && ch.isActive()) {
      return CompletableFuture.completedFuture(ch);
    }

    var get = new ReverseConnectChannelFsm.Event.GetChannel(new CompletableFuture<>());
    channelFsm.fireEvent(get);
    return get.future();
  }

  @Override
  public void addTransitionListener(TransitionListener listener) {
    stateListeners.add(listener);
  }

  @Override
  public void removeTransitionListener(TransitionListener listener) {
    stateListeners.remove(listener);
  }
}
