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
 * UA-TCP client transport that waits for or receives claimed inbound reverse-connect channels.
 *
 * <p>The manager owns listener binding, {@code ReverseHello} decoding, selector matching, pending
 * holds, and channel handoff. In manager/selector mode this transport registers a selector and
 * waits for a claimed channel. In direct mode it consumes one pre-claimed {@link
 * ReverseConnectConnection}. Both modes install the normal client UASC pipeline and complete
 * connection only after the SecureChannel handshake is ready for Session creation.
 *
 * <p>Once connected, the Session FSM treats this transport like any other {@link
 * org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport}. Channel-state notifications
 * are exposed through {@link ChannelStateObservable}, and keep-alive failure recovery can close the
 * active channel through {@link CurrentChannelProvider}.
 *
 * <p>Manager/selector mode is reusable for the lifetime of the client transport. If a claimed
 * candidate fails before the client SecureChannel handshake completes, or if an established
 * reverse-opened channel later closes while the client is still started, the transport registers a
 * fresh one-shot selector and waits for the next matching candidate. Direct pre-claimed connection
 * mode is deliberately one-shot: the supplied {@link ReverseConnectConnection} is consumed by the
 * first connect attempt, and any later failure leaves the transport terminal until the application
 * creates a new client from a new claimed connection.
 *
 * <p>Transition listeners observe completed transport state, not every claimed socket. A listener
 * receives {@code true} after a claimed channel completes client UASC setup, and receives {@code
 * false} when that connected channel later closes or is explicitly disconnected. Claims that fail
 * before the handshake completes do not emit a disconnected transition.
 */
public final class ReverseTcpClientTransport extends AbstractUascClientTransport
    implements ChannelStateObservable, CurrentChannelProvider {

  private final Object lock = new Object();

  private final @Nullable ReverseConnectManager manager;
  private final @Nullable ReverseConnectSelector selector;
  private final @Nullable ReverseConnectConnection directConnection;
  private final OpcTcpClientTransportConfig config;
  private final List<ChannelStateObservable.TransitionListener> transitionListeners =
      new CopyOnWriteArrayList<>();

  private CompletableFuture<Channel> channelFuture = new CompletableFuture<>();

  private boolean started = false;
  private boolean disconnecting = true;
  private boolean waitingForReverseConnection = false;
  private boolean directConnectionConsumed = false;

  private @Nullable ClientApplicationContext applicationContext;
  private @Nullable ReverseConnectRegistration registration;
  private @Nullable Channel currentChannel;
  private @Nullable Throwable directTerminalFailure;

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
    this.directConnection = null;
  }

  /**
   * Create a reverse TCP client transport from one pre-claimed reverse-connect connection.
   *
   * <p>Direct mode is one-shot. The transport consumes the supplied connection on the first {@link
   * #connect(ClientApplicationContext)} call, does not register selectors with a manager, and does
   * not rearm after handshake failure or later channel close. Reconnect requires a new claimed
   * connection and client instance.
   *
   * @param config the TCP client transport configuration used after the channel is claimed.
   * @param connection the pre-claimed reverse-connect connection.
   */
  public ReverseTcpClientTransport(
      OpcTcpClientTransportConfig config, ReverseConnectConnection connection) {

    super(config);

    this.config = requireNonNull(config, "config");
    this.manager = null;
    this.selector = null;
    this.directConnection = requireNonNull(connection, "connection");
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

    if (directConnection != null) {
      return connectDirect(applicationContext);
    }

    CompletableFuture<Channel> futureToReturn;
    CompletableFuture<Channel> futureToRegister = null;
    boolean emitSyntheticDisconnect = false;

    synchronized (lock) {
      this.applicationContext = applicationContext;
      started = true;
      disconnecting = false;

      if (currentChannel != null && currentChannel.isActive()) {
        return CompletableFuture.completedFuture(Unit.VALUE);
      }

      // If the previous channel completed its handshake (channelFuture done normally) but its
      // close listener has not yet fired, emit the matching connected=false synthetically before
      // we null currentChannel. After nulling, onChannelClosed will see currentChannel != closed
      // and return silently, which would otherwise drop the pending disconnect notification.
      emitSyntheticDisconnect =
          currentChannel != null
              && channelFuture.isDone()
              && !channelFuture.isCompletedExceptionally()
              && !channelFuture.isCancelled();

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

    if (emitSyntheticDisconnect) {
      notifyTransitionListeners(false);
    }

    if (futureToRegister != null) {
      registerForNextChannel(futureToRegister);
    }

    return futureToReturn.thenApply(channel -> Unit.VALUE);
  }

  private CompletableFuture<Unit> connectDirect(ClientApplicationContext applicationContext) {
    ReverseConnectConnection directConnection =
        requireNonNull(this.directConnection, "directConnection");
    CompletableFuture<Channel> futureToReturn;
    ReverseConnectConnection connectionToInitialize;

    synchronized (lock) {
      this.applicationContext = applicationContext;
      started = true;
      disconnecting = false;

      if (currentChannel != null && currentChannel.isActive()) {
        return CompletableFuture.completedFuture(Unit.VALUE);
      }

      if (directTerminalFailure != null) {
        return CompletableFuture.failedFuture(directTerminalFailure);
      }

      if (directConnectionConsumed) {
        UaException failure =
            new UaException(
                StatusCodes.Bad_ConnectionClosed,
                "reverse-connect direct connection has already been consumed");

        enterDirectTerminalStateLocked(failure);

        return CompletableFuture.failedFuture(failure);
      }

      directConnectionConsumed = true;

      if (channelFuture.isDone()) {
        channelFuture = new CompletableFuture<>();
      }

      futureToReturn = channelFuture;
      connectionToInitialize = directConnection;
    }

    initializeClaimedConnection(connectionToInitialize, futureToReturn, false);

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
    boolean notifyDisconnected;

    synchronized (lock) {
      started = false;
      disconnecting = true;
      waitingForReverseConnection = false;

      registrationToClose = registration;
      registration = null;

      channelToClose = currentChannel;
      futureToCancel = channelFuture;
      notifyDisconnected =
          channelToClose != null
              && futureToCancel.isDone()
              && !futureToCancel.isCompletedExceptionally()
              && !futureToCancel.isCancelled();

      if (channelToClose == null && directConnection != null && !directConnectionConsumed) {
        channelToClose = directConnection.channel();
        directConnectionConsumed = true;
      }
      currentChannel = null;

      if (directConnection != null) {
        enterDirectTerminalStateLocked(
            new UaException(
                StatusCodes.Bad_ConnectionClosed,
                "reverse-connect direct connection has been disconnected"));
      } else {
        channelFuture = new CompletableFuture<>();
      }
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
                if (notifyDisconnected) {
                  notifyTransitionListeners(false);
                }
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
    ReverseConnectManager manager = this.manager;
    ReverseConnectSelector selector = this.selector;

    if (manager == null || selector == null) {
      failRegistration(
          targetFuture,
          new UaException(StatusCodes.Bad_UnexpectedError, "reverse-connect manager is null"));
      return;
    }

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

    initializeClaimedConnection(connection, targetFuture, true);
  }

  private void initializeClaimedConnection(
      ReverseConnectConnection connection,
      CompletableFuture<Channel> targetFuture,
      boolean rearmOnFailure) {

    String endpointUrl = connection.endpointUrl();
    if (endpointUrl == null || endpointUrl.isBlank()) {
      var failure =
          new UaException(
              StatusCodes.Bad_TcpEndpointUrlInvalid, "ReverseHello endpointUrl is null or blank");
      CompletableFuture<Channel> nextFuture =
          rearmOnFailure ? rearmAfterFailedClaim(targetFuture, null) : null;

      if (!rearmOnFailure) {
        synchronized (lock) {
          enterDirectTerminalStateLocked(failure);
        }
      }

      connection.close();
      targetFuture.completeExceptionally(failure);

      if (nextFuture != null) {
        registerForNextChannel(nextFuture);
      }

      return;
    }

    initializeClaimedChannel(connection.channel(), endpointUrl, targetFuture, rearmOnFailure);
  }

  private void initializeClaimedChannel(
      Channel channel,
      String endpointUrl,
      CompletableFuture<Channel> targetFuture,
      boolean rearmOnFailure) {

    ClientApplicationContext application;
    synchronized (lock) {
      application = applicationContext;
    }

    if (application == null) {
      var failure =
          new UaException(StatusCodes.Bad_UnexpectedError, "client application context is null");

      if (!rearmOnFailure) {
        synchronized (lock) {
          enterDirectTerminalStateLocked(failure);
        }
      }

      channel.close();
      targetFuture.completeExceptionally(failure);
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

    // Fail handshakeFuture if the channel closes before or while the initializer dispatches its
    // pipeline. Otherwise handshakeFuture could remain pending forever when the close listener
    // fires before UascClientAcknowledgeHandler.handlerAdded had a chance to schedule its timer.
    channel
        .closeFuture()
        .addListener(
            future -> {
              if (!handshakeFuture.isDone()) {
                handshakeFuture.completeExceptionally(
                    new UaException(
                        StatusCodes.Bad_ConnectionClosed,
                        "reverse-connect channel closed before handshake completed"));
              }
            });

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

                if (!channel.isActive() && !handshakeFuture.isDone()) {
                  handshakeFuture.completeExceptionally(
                      new UaException(
                          StatusCodes.Bad_ConnectionClosed,
                          "reverse-connect channel closed before initializer dispatch"));
                }
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
                            rearmOnFailure ? rearmAfterFailedClaim(targetFuture, channel) : null;

                        targetFuture.completeExceptionally(failure);
                        channel.close();

                        if (nextFuture != null) {
                          registerForNextChannel(nextFuture);
                        }
                      }
                    }));
  }

  private @Nullable CompletableFuture<Channel> rearmAfterFailedClaim(
      CompletableFuture<Channel> failedFuture, @Nullable Channel failedChannel) {

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
        // Complete inside the lock so a concurrent disconnect() observes futureToCancel.isDone()
        // and emits a matching connected=false transition. Completing outside the lock allows the
        // disconnect to interleave between the publish of currentChannel and the future
        // completion, suppressing its matching disconnect notification and producing an orphan
        // connected=true event.
        targetFuture.complete(channel);
      }
    }

    if (stale) {
      channel.close();
    } else {
      notifyTransitionListeners(true);
    }
  }

  private void onChannelClosed(Channel closedChannel) {
    var failure =
        new UaException(StatusCodes.Bad_ConnectionClosed, "reverse-connect channel closed");
    CompletableFuture<Channel> closedFuture = null;
    CompletableFuture<Channel> nextFuture = null;
    boolean notifyDisconnected = false;

    synchronized (lock) {
      if (currentChannel == closedChannel) {
        currentChannel = null;

        notifyDisconnected =
            channelFuture.isDone()
                && !channelFuture.isCompletedExceptionally()
                && !channelFuture.isCancelled();

        if (started && !disconnecting) {
          closedFuture = channelFuture;
          if (directConnection == null) {
            channelFuture = new CompletableFuture<>();
            waitingForReverseConnection = true;
            nextFuture = channelFuture;
          } else {
            enterDirectTerminalStateLocked(failure);
          }
        }
      }
    }

    if (closedFuture != null && !closedFuture.isDone()) {
      closedFuture.completeExceptionally(failure);
    }

    if (nextFuture != null) {
      registerForNextChannel(nextFuture);
    }

    if (notifyDisconnected) {
      notifyTransitionListeners(false);
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

  private void enterDirectTerminalStateLocked(Throwable failure) {
    // Preserve the first terminal cause; later calls (for example, disconnect() after
    // onChannelClosed already recorded "channel closed") must not overwrite the earlier, more
    // specific failure.
    if (directTerminalFailure != null) {
      return;
    }

    directTerminalFailure = failure;
    waitingForReverseConnection = false;

    if (!channelFuture.isDone()) {
      channelFuture.completeExceptionally(failure);
    }

    channelFuture = CompletableFuture.failedFuture(failure);
  }

  private void notifyTransitionListeners(boolean connected) {
    for (ChannelStateObservable.TransitionListener listener : transitionListeners) {
      listener.onStateTransition(connected);
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
