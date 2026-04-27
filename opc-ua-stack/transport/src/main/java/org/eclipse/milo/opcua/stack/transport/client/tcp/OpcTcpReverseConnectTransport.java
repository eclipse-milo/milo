/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.tcp;

import io.netty.channel.Channel;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CopyOnWriteArrayList;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.AbstractUascClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.CurrentChannelProvider;
import org.jspecify.annotations.Nullable;

/**
 * A client transport that listens for inbound server connections using the OPC UA Reverse Connect
 * mechanism (Part 6, Section 7.1.3).
 *
 * <p>Instead of the client connecting to the server, the client opens a listening socket and waits
 * for the server to connect inbound and send a {@code ReverseHello} message. After the ReverseHello
 * exchange, the protocol continues identically to normal connect.
 */
public class OpcTcpReverseConnectTransport extends AbstractUascClientTransport
    implements ChannelStateObservable, CurrentChannelProvider {

  private final OpcTcpReverseConnectTransportConfig config;
  private final ReverseConnectChannelOwner channelOwner;
  private final OneToOneReverseConnectListener listener;

  private final CopyOnWriteArrayList<ChannelStateObservable.TransitionListener> stateListeners =
      new CopyOnWriteArrayList<>();

  private volatile Channel serverChannel;

  /**
   * Create a new {@link OpcTcpReverseConnectTransport}.
   *
   * @param config the Reverse Connect transport configuration.
   */
  public OpcTcpReverseConnectTransport(OpcTcpReverseConnectTransportConfig config) {
    super(config);

    this.config = config;

    this.channelOwner = newChannelOwner(config, null);
    this.listener = new OneToOneReverseConnectListener(config, channelOwner::accepted);

    channelOwner.addTransitionListener(this::onOwnerTransition);
  }

  OpcTcpReverseConnectTransport(
      OpcTcpReverseConnectTransportConfig config,
      ReverseConnectChannelOwner.HandshakeStarter handshakeStarter) {

    super(config);

    this.config = config;

    this.channelOwner = newChannelOwner(config, handshakeStarter);
    this.listener = new OneToOneReverseConnectListener(config, channelOwner::accepted);

    channelOwner.addTransitionListener(this::onOwnerTransition);
  }

  private ReverseConnectChannelOwner newChannelOwner(
      OpcTcpReverseConnectTransportConfig config,
      ReverseConnectChannelOwner.HandshakeStarter handshakeStarter) {

    var ownerConfig =
        new ReverseConnectChannelOwner.ChannelOwnerConfig(
            config,
            config.getAllowedServerUris(),
            config.getReverseHelloTimeout(),
            config.getConnectTimeout());

    var scheduler =
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
  public OpcTcpReverseConnectTransportConfig getConfig() {
    return config;
  }

  /**
   * Start listening for inbound server connections and wait for a SecureChannel to be established.
   *
   * @param applicationContext the client application context.
   * @return a future that completes when the first SecureChannel is ready.
   */
  @Override
  public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
    CompletableFuture<Channel> listening;
    try {
      listening = startListeningAsync();
    } catch (Exception e) {
      return CompletableFuture.failedFuture(e);
    }

    return listening
        .thenCompose(ch -> channelOwner.connect(applicationContext))
        .thenApply(ch -> Unit.VALUE);
  }

  /**
   * Disconnect the current SecureChannel, stop listening for inbound connections, and close any
   * accepted child sockets that have not yet reached the connected state.
   *
   * @return a future that completes when the disconnect is finished, the listening socket is
   *     closed, and accepted child sockets have been cleaned up.
   */
  @Override
  public CompletableFuture<Unit> disconnect() {
    return channelOwner
        .disconnect()
        .handle((v, ex) -> unwrap(ex))
        .thenCompose(
            disconnectFailure ->
                stopListeningAsync()
                    .handle(
                        (v, stopFailure) -> {
                          Throwable failure = combine(disconnectFailure, unwrap(stopFailure));
                          if (failure != null) {
                            throw completionException(failure);
                          }

                          return Unit.VALUE;
                        }));
  }

  @Override
  protected CompletableFuture<Channel> getChannel() {
    Channel activeChannel = channelOwner.getActiveChannel();
    if (activeChannel != null && activeChannel.isActive()) {
      return CompletableFuture.completedFuture(activeChannel);
    }

    if (!isListening()) {
      return CompletableFuture.failedFuture(
          new UaException(StatusCodes.Bad_Shutdown, "reverse connect listener is not active"));
    }

    return channelOwner.getChannel();
  }

  /**
   * Disconnect the current reverse-connected child channel while leaving the listener active.
   *
   * @return a future that completes when the current child-channel lifecycle is reset.
   */
  public CompletableFuture<Unit> disconnectChannel() {
    return channelOwner.disconnect();
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
  public void addTransitionListener(ChannelStateObservable.TransitionListener listener) {
    stateListeners.add(listener);
  }

  @Override
  public void removeTransitionListener(ChannelStateObservable.TransitionListener listener) {
    stateListeners.remove(listener);
  }

  private synchronized CompletableFuture<Channel> startListeningAsync() {
    if (serverChannel != null && serverChannel.isOpen()) {
      return CompletableFuture.completedFuture(serverChannel);
    }

    CompletableFuture<Channel> startFuture = listener.start();

    if (!startFuture.isDone() && isEventLoopThread()) {
      return startFuture.whenComplete(
          (channel, ex) -> {
            if (ex == null) {
              serverChannel = channel;
            } else {
              serverChannel = null;
            }
          });
    }

    try {
      Channel channel = startFuture.join();
      serverChannel = channel;
      return CompletableFuture.completedFuture(channel);
    } catch (CompletionException e) {
      serverChannel = null;
      return CompletableFuture.failedFuture(unwrap(e));
    }
  }

  private boolean isEventLoopThread() {
    for (EventExecutor executor : config.getEventLoop()) {
      if (executor.inEventLoop()) {
        return true;
      }
    }

    return false;
  }

  private synchronized CompletableFuture<Unit> stopListeningAsync() {
    serverChannel = null;

    return listener.stop();
  }

  private void onOwnerTransition(
      ReverseConnectChannelOwner.State from, ReverseConnectChannelOwner.State to) {

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

  private boolean isListening() {
    Channel channel = serverChannel;

    return channel != null && channel.isOpen();
  }

  Channel getListenerChannel() {
    return serverChannel;
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

  private static CompletionException completionException(Throwable failure) {
    if (failure instanceof CompletionException completionException) {
      return completionException;
    }

    return new CompletionException(failure);
  }

  private static Throwable unwrap(Throwable failure) {
    if (failure instanceof CompletionException && failure.getCause() != null) {
      return failure.getCause();
    }

    return failure;
  }
}
