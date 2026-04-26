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
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.AbstractUascClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;

/**
 * A client transport that listens for inbound server connections using the OPC UA Reverse Connect
 * mechanism (Part 6, Section 7.1.3).
 *
 * <p>Instead of the client connecting to the server, the client opens a listening socket and waits
 * for the server to connect inbound and send a {@code ReverseHello} message. After the ReverseHello
 * exchange, the protocol continues identically to normal connect.
 */
public class OpcTcpReverseConnectTransport extends AbstractUascClientTransport
    implements ChannelStateObservable {

  private final OpcTcpReverseConnectTransportConfig config;
  private final ReverseConnectChannelFsm channelFsm;
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

    var fsmConfig =
        new ReverseConnectChannelFsm.ChannelFsmConfig(
            config, config.getAllowedServerUris(), config.getReverseHelloTimeout());

    this.channelFsm =
        ReverseConnectChannelFsm.create(
            fsmConfig, this, requestId::getAndIncrement, config.getExecutor());
    this.listener =
        new OneToOneReverseConnectListener(
            config,
            (channel, reverseHello) ->
                channelFsm.fireEvent(
                    new ReverseConnectChannelFsm.Event.ConnectionAccepted(channel, reverseHello)));

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
    channelFsm.setApplicationContext(applicationContext);

    CompletableFuture<Channel> listening;
    try {
      listening = startListeningAsync();
    } catch (Exception e) {
      return CompletableFuture.failedFuture(e);
    }

    var connect = new ReverseConnectChannelFsm.Event.Connect(new CompletableFuture<>());
    return listening
        .thenCompose(
            ch -> {
              channelFsm.fireEvent(connect);
              return connect.future();
            })
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
    var disconnect = new ReverseConnectChannelFsm.Event.Disconnect(new CompletableFuture<>());
    channelFsm.fireEvent(disconnect);

    // Always stop the listener, even if the FSM disconnect path fails unexpectedly.
    // The stop path closes the listening socket and all tracked child channels.
    return disconnect
        .future()
        .handle((v, ex) -> ex)
        .thenCompose(
            disconnectFailure ->
                stopListeningAsync()
                    .thenApply(
                        v -> {
                          if (disconnectFailure != null) {
                            throw new CompletionException(disconnectFailure);
                          }
                          return v;
                        }));
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

  /**
   * Get the {@link ReverseConnectChannelFsm} used by this transport.
   *
   * @return the {@link ReverseConnectChannelFsm}.
   */
  public ReverseConnectChannelFsm getChannelFsm() {
    return channelFsm;
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
      return CompletableFuture.failedFuture(e);
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
}
