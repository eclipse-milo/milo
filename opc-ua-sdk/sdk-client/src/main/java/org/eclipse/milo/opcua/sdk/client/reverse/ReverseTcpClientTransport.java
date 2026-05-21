/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.reverse;

import static java.util.Objects.requireNonNull;

import io.netty.channel.Channel;
import java.util.List;
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
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientChannelInitializer;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.jspecify.annotations.Nullable;

/**
 * UA-TCP client transport that waits for a {@link ReverseConnectManager} to claim inbound channels.
 *
 * <p>The manager owns listener binding, {@code ReverseHello} decoding, selector matching, pending
 * holds, and channel handoff. This transport starts at that handoff point: it registers a selector,
 * waits for a claimed channel, installs the normal client UASC pipeline, and completes connection
 * only after the SecureChannel handshake is ready for Session creation.
 *
 * <p>Once connected, the Session FSM treats this transport like any other {@link
 * org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport}. Channel-state notifications
 * are exposed through {@link ChannelStateObservable}, and keep-alive failure recovery can close the
 * active channel through {@link CurrentChannelProvider}.
 */
public final class ReverseTcpClientTransport extends AbstractUascClientTransport
    implements ChannelStateObservable, CurrentChannelProvider {

  private final Object lock = new Object();

  private final ReverseConnectManager manager;
  private final ReverseConnectSelector selector;
  private final OpcTcpClientTransportConfig config;
  private final List<ChannelStateObservable.TransitionListener> transitionListeners =
      new CopyOnWriteArrayList<>();

  private CompletableFuture<Channel> channelFuture = new CompletableFuture<>();

  private boolean started = false;
  private boolean disconnecting = true;
  private boolean waitingForReverseConnection = false;

  private @Nullable ClientApplicationContext applicationContext;
  private @Nullable ReverseConnectRegistration registration;
  private @Nullable Channel currentChannel;

  /**
   * Create a reverse TCP client transport that uses a shared manager for channel acquisition.
   *
   * <p>The constructor does not start the manager or reserve a channel. The selector is registered
   * when {@link #connect(ClientApplicationContext)} is called, keeping each connection attempt
   * one-shot and tied to the Session FSM lifecycle.
   *
   * @param config the TCP client transport configuration used after a channel is claimed.
   * @param manager the reverse-connect manager that owns listener sockets and candidate matching.
   * @param selector the selector used to claim a matching reverse connection.
   */
  public ReverseTcpClientTransport(
      OpcTcpClientTransportConfig config,
      ReverseConnectManager manager,
      ReverseConnectSelector selector) {

    super(config);

    this.config = requireNonNull(config, "config");
    this.manager = requireNonNull(manager, "manager");
    this.selector = requireNonNull(selector, "selector");
  }

  @Override
  public OpcTcpClientTransportConfig getConfig() {
    return config;
  }

  /**
   * Register the selector and wait for a claimed reverse channel to finish client UASC setup.
   *
   * <p>The returned future completes when the claimed channel has completed the same SecureChannel
   * handshake used by outbound TCP clients. If a claimed channel fails before the handshake
   * completes, the transport arms a fresh selector so the surrounding Session open can continue
   * waiting for the next matching reverse connection.
   *
   * @param applicationContext the client application context used for endpoint and security
   *     configuration.
   * @return a future that completes when a claimed channel is ready for Session creation.
   */
  @Override
  public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
    requireNonNull(applicationContext, "applicationContext");

    CompletableFuture<Channel> futureToReturn;
    CompletableFuture<Channel> futureToRegister = null;

    synchronized (lock) {
      this.applicationContext = applicationContext;
      started = true;
      disconnecting = false;

      if (currentChannel != null && currentChannel.isActive()) {
        return CompletableFuture.completedFuture(Unit.VALUE);
      }

      currentChannel = null;

      if (channelFuture.isDone()) {
        channelFuture = new CompletableFuture<>();
      }

      futureToReturn = channelFuture;

      if (!waitingForReverseConnection) {
        waitingForReverseConnection = true;
        futureToRegister = channelFuture;
      }
    }

    if (futureToRegister != null) {
      registerForNextChannel(futureToRegister);
    }

    return futureToReturn.thenApply(channel -> Unit.VALUE);
  }

  /**
   * Unregister any outstanding selector and close the currently claimed channel.
   *
   * <p>Disconnecting this transport does not shut down the shared {@link ReverseConnectManager};
   * the manager may be serving other reverse-connect clients.
   *
   * @return a future that completes when selector cleanup and channel close have finished.
   */
  @Override
  public CompletableFuture<Unit> disconnect() {
    ReverseConnectRegistration registrationToClose;
    Channel channelToClose;
    CompletableFuture<Channel> futureToCancel;

    synchronized (lock) {
      started = false;
      disconnecting = true;
      waitingForReverseConnection = false;

      registrationToClose = registration;
      registration = null;

      channelToClose = currentChannel;
      currentChannel = null;

      futureToCancel = channelFuture;
      channelFuture = new CompletableFuture<>();
    }

    if (registrationToClose != null) {
      registrationToClose.close();
    }

    if (!futureToCancel.isDone()) {
      futureToCancel.completeExceptionally(new UaException(StatusCodes.Bad_SessionClosed));
    }

    if (channelToClose == null) {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    var disconnectFuture = new CompletableFuture<Unit>();
    channelToClose
        .close()
        .addListener(
            future -> {
              if (future.isSuccess()) {
                disconnectFuture.complete(Unit.VALUE);
              } else {
                disconnectFuture.completeExceptionally(future.cause());
              }
            });

    return disconnectFuture;
  }

  @Override
  protected CompletableFuture<Channel> getChannel() {
    synchronized (lock) {
      return channelFuture;
    }
  }

  @Override
  public void addTransitionListener(ChannelStateObservable.TransitionListener listener) {
    transitionListeners.add(listener);
  }

  @Override
  public void removeTransitionListener(ChannelStateObservable.TransitionListener listener) {
    transitionListeners.remove(listener);
  }

  @Override
  public @Nullable Channel getCurrentChannel() {
    synchronized (lock) {
      return currentChannel;
    }
  }

  private void registerForNextChannel(CompletableFuture<Channel> targetFuture) {
    ReverseConnectRegistration nextRegistration;

    try {
      nextRegistration = manager.registerSelector(selector);
    } catch (Throwable t) {
      failRegistration(targetFuture, t);
      return;
    }

    boolean stale;
    synchronized (lock) {
      stale = disconnecting || targetFuture != channelFuture;
      if (!stale) {
        registration = nextRegistration;
      } else {
        waitingForReverseConnection = false;
      }
    }

    nextRegistration
        .connectionFuture()
        .whenComplete(
            (connection, ex) ->
                config
                    .getExecutor()
                    .execute(
                        () ->
                            handleClaimedConnection(
                                nextRegistration, targetFuture, connection, ex)));

    if (stale) {
      nextRegistration.close();
    }
  }

  private void handleClaimedConnection(
      ReverseConnectRegistration claimedRegistration,
      CompletableFuture<Channel> targetFuture,
      @Nullable ReverseConnectConnection connection,
      @Nullable Throwable ex) {

    boolean stale;

    synchronized (lock) {
      stale = disconnecting || registration != claimedRegistration || targetFuture != channelFuture;

      if (!stale) {
        registration = null;
        waitingForReverseConnection = false;
      }
    }

    if (stale) {
      if (connection != null) {
        connection.close();
      }
      return;
    }

    if (ex != null) {
      targetFuture.completeExceptionally(unwrap(ex));
      return;
    }

    if (connection == null) {
      targetFuture.completeExceptionally(
          new UaException(StatusCodes.Bad_UnexpectedError, "reverse-connect claim was null"));
      return;
    }

    String endpointUrl = connection.endpointUrl();
    if (endpointUrl == null || endpointUrl.isBlank()) {
      connection.close();
      targetFuture.completeExceptionally(
          new UaException(
              StatusCodes.Bad_TcpEndpointUrlInvalid, "ReverseHello endpointUrl is null"));
      return;
    }

    initializeClaimedChannel(connection.channel(), endpointUrl, targetFuture);
  }

  private void initializeClaimedChannel(
      Channel channel, String endpointUrl, CompletableFuture<Channel> targetFuture) {

    ClientApplicationContext application;
    synchronized (lock) {
      application = applicationContext;
    }

    if (application == null) {
      channel.close();
      targetFuture.completeExceptionally(
          new UaException(StatusCodes.Bad_UnexpectedError, "client application context is null"));
      return;
    }

    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

    synchronized (lock) {
      if (disconnecting || targetFuture != channelFuture) {
        channel.close();
        return;
      }

      currentChannel = channel;
    }

    channel.closeFuture().addListener(future -> onChannelClosed(channel));

    channel
        .eventLoop()
        .execute(
            () -> {
              try {
                OpcTcpClientChannelInitializer.initializeConnectedChannel(
                    channel,
                    config,
                    application,
                    ReverseTcpClientTransport.this,
                    requestId::getAndIncrement,
                    handshakeFuture,
                    endpointUrl);
              } catch (Throwable t) {
                handshakeFuture.completeExceptionally(t);
                channel.close();
              }
            });

    handshakeFuture.whenComplete(
        (secureChannel, ex) ->
            config
                .getExecutor()
                .execute(
                    () -> {
                      if (secureChannel != null) {
                        completeHandshake(targetFuture, secureChannel.getChannel());
                      } else {
                        Throwable failure = unwrap(ex);
                        CompletableFuture<Channel> nextFuture =
                            rearmAfterFailedHandshake(targetFuture, channel);

                        targetFuture.completeExceptionally(failure);
                        channel.close();

                        if (nextFuture != null) {
                          registerForNextChannel(nextFuture);
                        }
                      }
                    }));
  }

  private @Nullable CompletableFuture<Channel> rearmAfterFailedHandshake(
      CompletableFuture<Channel> failedFuture, Channel failedChannel) {

    synchronized (lock) {
      if (disconnecting || !started || failedFuture != channelFuture) {
        return null;
      }

      if (currentChannel == failedChannel) {
        currentChannel = null;
      }

      channelFuture = new CompletableFuture<>();
      waitingForReverseConnection = true;

      return channelFuture;
    }
  }

  private void completeHandshake(CompletableFuture<Channel> targetFuture, Channel channel) {
    boolean stale;

    synchronized (lock) {
      stale = disconnecting || targetFuture != channelFuture;
      if (!stale) {
        currentChannel = channel;
      }
    }

    if (stale) {
      channel.close();
    } else {
      targetFuture.complete(channel);
      notifyTransitionListeners(true);
    }
  }

  private void onChannelClosed(Channel closedChannel) {
    CompletableFuture<Channel> closedFuture = null;
    CompletableFuture<Channel> nextFuture = null;
    boolean notifyDisconnected = false;

    synchronized (lock) {
      if (currentChannel == closedChannel) {
        currentChannel = null;

        if (started && !disconnecting) {
          notifyDisconnected = true;
          closedFuture = channelFuture;
          channelFuture = new CompletableFuture<>();
          waitingForReverseConnection = true;
          nextFuture = channelFuture;
        }
      }
    }

    if (closedFuture != null && !closedFuture.isDone()) {
      closedFuture.completeExceptionally(
          new UaException(StatusCodes.Bad_ConnectionClosed, "reverse-connect channel closed"));
    }

    if (notifyDisconnected) {
      notifyTransitionListeners(false);
    }

    if (nextFuture != null) {
      registerForNextChannel(nextFuture);
    }
  }

  private void failRegistration(CompletableFuture<Channel> targetFuture, Throwable t) {
    synchronized (lock) {
      if (targetFuture == channelFuture) {
        waitingForReverseConnection = false;
      }
    }

    targetFuture.completeExceptionally(t);
  }

  private void notifyTransitionListeners(boolean connected) {
    for (ChannelStateObservable.TransitionListener listener : transitionListeners) {
      config.getExecutor().execute(() -> listener.onStateTransition(connected));
    }
  }

  private static Throwable unwrap(@Nullable Throwable ex) {
    if (ex instanceof CompletionException && ex.getCause() != null) {
      return ex.getCause();
    } else if (ex != null) {
      return ex;
    } else {
      return new UaException(StatusCodes.Bad_UnexpectedError);
    }
  }
}
