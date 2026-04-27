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
import java.util.concurrent.CompletionException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.AbstractUascClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.CurrentChannelProvider;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.jspecify.annotations.Nullable;

/**
 * An {@link OpcClientTransport} that receives its channels from a shared {@link
 * ChannelConsumerRegistry} instead of owning a {@code ServerBootstrap}.
 *
 * <p>Each instance manages a single reverse-connected channel via a {@link
 * ReverseConnectChannelOwner}. The transport registers itself with the registry on {@link
 * #connect(ClientApplicationContext)} and deregisters on {@link #disconnect()}.
 */
public class OpcTcpMultiplexedReverseConnectTransport extends AbstractUascClientTransport
    implements ChannelStateObservable, CurrentChannelProvider {

  private final String serverUri;
  private final OpcTcpMultiplexedReverseConnectTransportConfig config;
  private final ChannelConsumerRegistry registry;
  private final ReverseConnectChannelOwner channelOwner;
  private final ChannelConsumerRegistry.ChannelConsumer channelConsumer;

  private final CopyOnWriteArrayList<ChannelStateObservable.TransitionListener> stateListeners =
      new CopyOnWriteArrayList<>();

  // Registration and owner state are intentionally separate: duplicate connect() calls should
  // share the owner lifecycle without adding duplicate listener entries.
  private final AtomicBoolean registered = new AtomicBoolean(false);

  // The listener may still hold a reference to this sink briefly after stop or deregistration.
  // Keep a fast local gate so late dispatches close their channel instead of reaching the owner.
  private final AtomicBoolean acceptingChannels = new AtomicBoolean(false);
  private final AtomicBoolean channelReserved = new AtomicBoolean(false);

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

    this(serverUri, config, registry, null);
  }

  OpcTcpMultiplexedReverseConnectTransport(
      String serverUri,
      OpcTcpMultiplexedReverseConnectTransportConfig config,
      ChannelConsumerRegistry registry,
      ReverseConnectChannelOwner.HandshakeStarter handshakeStarter) {

    super(config);

    this.serverUri = serverUri;
    this.config = config;
    this.registry = registry;

    this.channelOwner = newChannelOwner(config, handshakeStarter);
    this.channelConsumer =
        new ChannelConsumerRegistry.ChannelConsumer() {
          @Override
          public boolean needsChannel() {
            return OpcTcpMultiplexedReverseConnectTransport.this.needsChannel();
          }

          @Override
          public boolean acceptsDuplicateChannel() {
            return OpcTcpMultiplexedReverseConnectTransport.this.acceptsDuplicateChannel();
          }

          @Override
          public boolean tryAccept(Channel channel, ReverseHelloMessage reverseHello) {
            return OpcTcpMultiplexedReverseConnectTransport.this.tryAccept(channel, reverseHello);
          }

          @Override
          public void accept(Channel channel, ReverseHelloMessage reverseHello) {
            OpcTcpMultiplexedReverseConnectTransport.this.accept(channel, reverseHello);
          }

          @Override
          public CompletableFuture<Unit> listenerStopped(Throwable cause) {
            return OpcTcpMultiplexedReverseConnectTransport.this.listenerStopped(cause);
          }
        };

    channelOwner.addTransitionListener(this::onOwnerTransition);
  }

  private ReverseConnectChannelOwner newChannelOwner(
      OpcTcpMultiplexedReverseConnectTransportConfig config,
      ReverseConnectChannelOwner.HandshakeStarter handshakeStarter) {

    var ownerConfig =
        new ReverseConnectChannelOwner.ChannelOwnerConfig(
            config,
            config.getAllowedServerUris(),
            config.getReverseHelloTimeout(),
            config.getConnectTimeout());

    ReverseConnectChannelOwner.Scheduler scheduler =
        ReverseConnectChannelOwner.Scheduler.fromScheduledExecutor(config.getScheduledExecutor());

    if (handshakeStarter == null) {
      return new ReverseConnectChannelOwner(
          ownerConfig, this, requestId::getAndIncrement, config.getExecutor(), scheduler);
    }

    return new ReverseConnectChannelOwner(
        ownerConfig,
        this,
        requestId::getAndIncrement,
        config.getExecutor(),
        scheduler,
        handshakeStarter);
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
   * Offer a channel for reuse when {@link #connect} is called.
   *
   * <p>If {@code connect()} has not installed the real client context yet, the channel owner
   * buffers at most one offered channel until the configured connect timeout. Duplicate, stale, or
   * stopped-lifecycle offers are closed by the owner.
   *
   * @param channel the accepted channel.
   * @param reverseHello the ReverseHello message received on the channel.
   */
  public void offerChannel(Channel channel, ReverseHelloMessage reverseHello) {
    if (!channel.isActive() || !matchesServerUri(reverseHello)) {
      close(channel);
      return;
    }

    channelOwner.accepted(channel, reverseHello);
  }

  @Override
  public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
    acceptingChannels.set(true);

    // Queue context installation before registering with the listener. If a server dial-in wins
    // the executor race, the owner still serializes the accepted channel behind this connect event.
    CompletableFuture<Channel> connectFuture = channelOwner.connect(applicationContext);

    try {
      register();
    } catch (Throwable t) {
      acceptingChannels.set(false);
      channelOwner.disconnect();
      return CompletableFuture.failedFuture(t);
    }

    return connectFuture.handle((channel, ex) -> unwrap(ex)).thenCompose(this::completeConnect);
  }

  @Override
  public CompletableFuture<Unit> disconnect() {
    acceptingChannels.set(false);
    channelReserved.set(false);

    return channelOwner
        .disconnect()
        .handle((v, ex) -> unwrap(ex))
        .thenCompose(
            disconnectFailure -> {
              Throwable deregisterFailure = null;
              try {
                deregister();
              } catch (Throwable t) {
                deregisterFailure = t;
              }

              Throwable failure = combine(disconnectFailure, deregisterFailure);
              if (failure != null) {
                return CompletableFuture.failedFuture(failure);
              }

              return CompletableFuture.completedFuture(Unit.VALUE);
            });
  }

  @Override
  protected CompletableFuture<Channel> getChannel() {
    Channel activeChannel = channelOwner.getActiveChannel();
    if (activeChannel != null && activeChannel.isActive()) {
      return CompletableFuture.completedFuture(activeChannel);
    }

    if (!acceptingChannels.get()
        && channelOwner.getState() != ReverseConnectChannelOwner.State.Stopped) {
      return CompletableFuture.failedFuture(
          new UaException(
              StatusCodes.Bad_Shutdown, "multiplexed reverse connect transport is not active"));
    }

    return channelOwner.getChannel();
  }

  /**
   * Get the active reverse-connected child channel, if one is currently connected.
   *
   * @return the active child channel, or {@code null}.
   */
  public @Nullable Channel getActiveChannel() {
    return getCurrentChannel();
  }

  @Override
  public @Nullable Channel getCurrentChannel() {
    return channelOwner.getActiveChannel();
  }

  @Override
  public void addTransitionListener(TransitionListener listener) {
    stateListeners.add(listener);
  }

  @Override
  public void removeTransitionListener(TransitionListener listener) {
    stateListeners.remove(listener);
  }

  private void register() {
    if (registered.compareAndSet(false, true)) {
      try {
        registry.register(serverUri, channelConsumer);
      } catch (Throwable t) {
        registered.set(false);
        throw t;
      }
    }
  }

  private void deregister() {
    if (registered.compareAndSet(true, false)) {
      registry.deregister(serverUri, channelConsumer);
    }
  }

  private CompletableFuture<Unit> completeConnect(Throwable connectFailure) {
    if (connectFailure == null) {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    acceptingChannels.set(false);
    channelReserved.set(false);

    Throwable deregisterFailure = null;
    try {
      deregister();
    } catch (Throwable t) {
      deregisterFailure = t;
    }

    Throwable failure = combine(connectFailure, deregisterFailure);

    return channelOwner
        .disconnect()
        .handle(
            (v, disconnectFailure) -> {
              Throwable combined = combine(failure, unwrap(disconnectFailure));
              if (combined != null) {
                throw completionException(combined);
              }

              return Unit.VALUE;
            });
  }

  private boolean needsChannel() {
    return !channelReserved.get() && ownerNeedsChannel();
  }

  private boolean ownerNeedsChannel() {
    if (!acceptingChannels.get()) {
      return false;
    }

    ReverseConnectChannelOwner.State state = channelOwner.getState();

    return state == ReverseConnectChannelOwner.State.Idle
        || state == ReverseConnectChannelOwner.State.Armed
        || (state == ReverseConnectChannelOwner.State.Connected && !hasActiveChannel());
  }

  private boolean acceptsDuplicateChannel() {
    if (!acceptingChannels.get()) {
      return false;
    }

    ReverseConnectChannelOwner.State state = channelOwner.getState();

    return state != ReverseConnectChannelOwner.State.Disconnecting
        && state != ReverseConnectChannelOwner.State.Stopped;
  }

  private boolean tryAccept(Channel channel, ReverseHelloMessage reverseHello) {
    if (!channel.isActive() || !matchesServerUri(reverseHello)) {
      close(channel);
      return true;
    }

    if (!ownerNeedsChannel() || !channelReserved.compareAndSet(false, true)) {
      return false;
    }

    if (!ownerNeedsChannel()) {
      channelReserved.set(false);
      return false;
    }

    try {
      channelOwner.accepted(channel, reverseHello);
    } catch (Throwable t) {
      channelReserved.set(false);
      throw t;
    }

    return true;
  }

  private void accept(Channel channel, ReverseHelloMessage reverseHello) {
    if (!acceptingChannels.get() || !matchesServerUri(reverseHello)) {
      close(channel);
      return;
    }

    // Duplicate, displaced, or already-stopped channels are closed by the owner. Keeping that
    // policy centralized is what prevents stale listener dispatch from starting handshakes.
    channelOwner.accepted(channel, reverseHello);
  }

  private CompletableFuture<Unit> listenerStopped(Throwable cause) {
    acceptingChannels.set(false);
    channelReserved.set(false);
    registered.set(false);

    return channelOwner.listenerStopped(cause);
  }

  private void onOwnerTransition(
      ReverseConnectChannelOwner.State from, ReverseConnectChannelOwner.State to) {

    if (to == ReverseConnectChannelOwner.State.Handshaking
        || to == ReverseConnectChannelOwner.State.Disconnecting
        || to == ReverseConnectChannelOwner.State.Stopped) {
      channelReserved.set(false);
    }

    if (from == ReverseConnectChannelOwner.State.Connected
        && to != ReverseConnectChannelOwner.State.Connected) {
      notifyStateListeners(false);
    } else if (from != ReverseConnectChannelOwner.State.Connected
        && to == ReverseConnectChannelOwner.State.Connected) {
      notifyStateListeners(true);
    }
  }

  private void notifyStateListeners(boolean connected) {
    config
        .getExecutor()
        .execute(() -> stateListeners.forEach(l -> l.onConnectionStateChange(connected)));
  }

  private boolean hasActiveChannel() {
    Channel activeChannel = channelOwner.getActiveChannel();

    return activeChannel != null && activeChannel.isActive();
  }

  private boolean matchesServerUri(ReverseHelloMessage reverseHello) {
    return serverUri.equals(reverseHello.serverUri());
  }

  private static void close(Channel channel) {
    channel.close();
  }

  private static Throwable combine(Throwable primary, Throwable secondary) {
    if (primary == null) {
      return secondary;
    }

    if (secondary != null) {
      primary.addSuppressed(secondary);
    }

    return primary;
  }

  private static Throwable unwrap(Throwable failure) {
    if (failure instanceof CompletionException && failure.getCause() != null) {
      return failure.getCause();
    }

    return failure;
  }

  private static CompletionException completionException(Throwable failure) {
    if (failure instanceof CompletionException completionException) {
      return completionException;
    }

    return new CompletionException(failure);
  }
}
