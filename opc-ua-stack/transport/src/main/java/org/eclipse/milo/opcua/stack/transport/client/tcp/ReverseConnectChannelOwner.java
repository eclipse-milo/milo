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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientConfig;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientReverseConnectHandshake;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Owns one logical client-side Reverse Connect channel lifecycle.
 *
 * <p>Transport adapters use this owner to install the real client application context, offer
 * accepted reverse channels, wait for a usable channel, and cancel the lifecycle on disconnect or
 * listener stop. The owner accepts at most one in-flight channel, closes duplicate accepted
 * channels, and resets after a normal disconnect so it can be used for a later connection attempt.
 */
final class ReverseConnectChannelOwner {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReverseConnectChannelOwner.class);

  /** Lifecycle states exposed for transport tests and adapter assertions. */
  enum State {
    /** No application context or owned channel is installed. */
    Idle,
    /** An application context is installed and the owner is waiting for an accepted channel. */
    Armed,
    /** An accepted channel is running the reverse-connect handshake. */
    Handshaking,
    /** A secure channel is active and ready for service requests. */
    Connected,
    /** A normal disconnect is closing owned channels before returning to idle. */
    Disconnecting,
    /** The listener stopped, and this owner will not accept future lifecycles. */
    Stopped
  }

  /** Events accepted by the channel owner. */
  interface Event {

    /**
     * Requests a connected channel using the supplied application context.
     *
     * @param application the real client application context for this lifecycle.
     * @param future the future completed when a usable channel is ready.
     */
    record Connect(ClientApplicationContext application, CompletableFuture<Channel> future)
        implements Event {}

    /**
     * Requests the active channel or waits for the next successful handshake.
     *
     * @param future the future completed with the active channel.
     */
    record GetChannel(CompletableFuture<Channel> future) implements Event {}

    /**
     * Offers a reverse-connected channel that has already provided a ReverseHello message.
     *
     * @param channel the accepted channel.
     * @param reverseHello the decoded ReverseHello message for the accepted channel.
     */
    record Accepted(Channel channel, ReverseHelloMessage reverseHello) implements Event {}

    /**
     * Reports a successful reverse-connect handshake for a channel.
     *
     * @param generation the lifecycle generation that started the handshake.
     * @param channel the channel that completed the handshake.
     * @param secureChannel the ready secure channel.
     */
    record HandshakeSucceeded(long generation, Channel channel, ClientSecureChannel secureChannel)
        implements Event {}

    /**
     * Reports a failed reverse-connect handshake for a channel.
     *
     * @param generation the lifecycle generation that started the handshake.
     * @param channel the channel whose handshake failed.
     * @param failure the failure reported by the handshake operation.
     */
    record HandshakeFailed(long generation, Channel channel, Throwable failure) implements Event {}

    /**
     * Reports that an owned channel became inactive.
     *
     * @param generation the lifecycle generation that owned the channel.
     * @param channel the inactive channel.
     */
    record ChannelInactive(long generation, Channel channel) implements Event {}

    /**
     * Requests normal lifecycle cleanup so the owner can be used again later.
     *
     * @param future the future completed after owned channels are closed.
     */
    record Disconnect(CompletableFuture<Unit> future) implements Event {}

    /**
     * Reports completion of channel closes started by a disconnect request.
     *
     * @param generation the lifecycle generation closed by the disconnect.
     * @param failure the close failure, or {@code null} if cleanup succeeded.
     */
    record DisconnectClosed(long generation, Throwable failure) implements Event {}

    /**
     * Reports that no more accepted channels will arrive from the listener.
     *
     * @param cause the stop reason, or {@code null} to use the default shutdown error.
     * @param future the future completed after owned channels are closed.
     */
    record ListenerStopped(Throwable cause, CompletableFuture<Unit> future) implements Event {}

    /**
     * Reports that a channel accepted before context installation waited too long.
     *
     * @param generation the lifecycle generation that accepted the channel.
     * @param channel the pending accepted channel.
     */
    record PendingAcceptTimedOut(long generation, Channel channel) implements Event {}

    /**
     * Reports that a pending channel waiter waited too long.
     *
     * @param future the waiter future to complete with timeout.
     */
    record ChannelWaiterTimedOut(CompletableFuture<Channel> future) implements Event {}
  }

  /**
   * Configuration needed to own one reverse-connect channel lifecycle.
   *
   * @param uascConfig the UASC client configuration used for handshakes.
   * @param allowedServerUris the accepted ReverseHello server URIs, or empty to accept all.
   * @param reverseHelloTimeout the ReverseHello handshake timeout in milliseconds.
   * @param connectTimeout the timeout for pending accepts and channel waiters in milliseconds.
   */
  record ChannelOwnerConfig(
      UascClientConfig uascConfig,
      Set<String> allowedServerUris,
      long reverseHelloTimeout,
      long connectTimeout) {

    ChannelOwnerConfig {
      Objects.requireNonNull(uascConfig, "uascConfig");
      allowedServerUris =
          allowedServerUris != null ? Set.copyOf(allowedServerUris) : Collections.emptySet();
    }
  }

  /** Starts a reverse-connect client handshake for an accepted channel. */
  @FunctionalInterface
  interface HandshakeStarter {

    /**
     * Starts the handshake and returns a future for the ready secure channel.
     *
     * @param channel the accepted channel.
     * @param reverseHello the decoded ReverseHello message.
     * @param config the UASC client configuration.
     * @param application the client application context for this lifecycle.
     * @param responseHandler the handler for normal UASC responses.
     * @param requestIdSupplier the supplier for UASC request IDs.
     * @param allowedServerUris the accepted ReverseHello server URIs.
     * @param reverseHelloTimeoutMs the ReverseHello handshake timeout in milliseconds.
     * @return the future completed when the secure channel is ready.
     */
    CompletableFuture<ClientSecureChannel> start(
        Channel channel,
        ReverseHelloMessage reverseHello,
        UascClientConfig config,
        ClientApplicationContext application,
        UascResponseHandler responseHandler,
        Supplier<Long> requestIdSupplier,
        Set<String> allowedServerUris,
        long reverseHelloTimeoutMs);
  }

  /** Schedules owner timeout events. */
  @FunctionalInterface
  interface Scheduler {

    /**
     * Schedules a task and returns a handle that can cancel it.
     *
     * @param task the task to run when the delay expires.
     * @param delay the delay amount.
     * @param unit the delay unit.
     * @return the cancellation handle for the scheduled task.
     */
    Cancellable schedule(Runnable task, long delay, TimeUnit unit);

    /**
     * Creates a scheduler backed by a {@link ScheduledExecutorService}.
     *
     * @param executor the executor used to schedule tasks.
     * @return the owner scheduler.
     */
    static Scheduler fromScheduledExecutor(ScheduledExecutorService executor) {
      return (task, delay, unit) -> {
        var scheduledFuture = executor.schedule(task, delay, unit);

        return () -> scheduledFuture.cancel(false);
      };
    }
  }

  /** Cancels a scheduled owner task. */
  @FunctionalInterface
  interface Cancellable {

    /** Cancels the task if it has not run yet. */
    void cancel();
  }

  /** Observes lifecycle state changes after the owner applies a transition. */
  @FunctionalInterface
  interface TransitionListener {

    /**
     * Invoked after the owner changes lifecycle state.
     *
     * @param from the previous lifecycle state.
     * @param to the new lifecycle state.
     */
    void onTransition(State from, State to);
  }

  /**
   * Accepted channel waiting for context installation or handshake completion.
   *
   * @param channel the accepted channel.
   * @param reverseHello the decoded ReverseHello message.
   * @param generation the lifecycle generation that accepted the channel.
   * @param pendingAcceptTimeout the timeout for accepted-before-context buffering.
   */
  private record AcceptedChannel(
      Channel channel,
      ReverseHelloMessage reverseHello,
      long generation,
      Cancellable pendingAcceptTimeout) {}

  /**
   * Caller waiting for a usable client channel.
   *
   * @param future the caller future.
   * @param timeout the timeout for the waiter.
   * @param connectWaiter {@code true} when this waiter belongs to a public connect lifecycle.
   */
  private record ChannelWaiter(
      CompletableFuture<Channel> future, Cancellable timeout, boolean connectWaiter) {}

  private final ChannelOwnerConfig config;
  private final UascResponseHandler responseHandler;
  private final Supplier<Long> requestIdSupplier;
  private final Executor executor;
  private final Scheduler scheduler;
  private final HandshakeStarter handshakeStarter;
  private final ArrayDeque<Event> mailbox = new ArrayDeque<>();
  private final List<ChannelWaiter> channelWaiters = new ArrayList<>();
  private final List<CompletableFuture<Unit>> disconnectWaiters = new ArrayList<>();
  private final List<CompletableFuture<Unit>> listenerStoppedWaiters = new ArrayList<>();
  private final List<TransitionListener> transitionListeners = new CopyOnWriteArrayList<>();

  private boolean draining;
  private boolean cleanupInProgress;
  private long generation;
  private volatile State state = State.Idle;

  private ClientApplicationContext application;
  private AcceptedChannel pendingAccepted;
  private AcceptedChannel handshaking;
  private volatile Channel activeChannel;
  private ClientSecureChannel activeSecureChannel;

  ReverseConnectChannelOwner(
      ChannelOwnerConfig config,
      UascResponseHandler responseHandler,
      Supplier<Long> requestIdSupplier,
      Executor executor,
      Scheduler scheduler) {

    this(
        config, responseHandler, requestIdSupplier, executor, scheduler, defaultHandshakeStarter());
  }

  ReverseConnectChannelOwner(
      ChannelOwnerConfig config,
      UascResponseHandler responseHandler,
      Supplier<Long> requestIdSupplier,
      Executor executor,
      Scheduler scheduler,
      HandshakeStarter handshakeStarter) {

    this.config = Objects.requireNonNull(config, "config");
    this.responseHandler = Objects.requireNonNull(responseHandler, "responseHandler");
    this.requestIdSupplier = Objects.requireNonNull(requestIdSupplier, "requestIdSupplier");
    this.executor = Objects.requireNonNull(executor, "executor");
    this.scheduler = Objects.requireNonNull(scheduler, "scheduler");
    this.handshakeStarter = Objects.requireNonNull(handshakeStarter, "handshakeStarter");
  }

  CompletableFuture<Channel> connect(ClientApplicationContext application) {
    var future = new CompletableFuture<Channel>();

    fireEvent(new Event.Connect(application, future));

    return future;
  }

  CompletableFuture<Channel> getChannel() {
    var future = new CompletableFuture<Channel>();

    fireEvent(new Event.GetChannel(future));

    return future;
  }

  void accepted(Channel channel, ReverseHelloMessage reverseHello) {
    fireEvent(new Event.Accepted(channel, reverseHello));
  }

  CompletableFuture<Unit> disconnect() {
    var future = new CompletableFuture<Unit>();

    fireEvent(new Event.Disconnect(future));

    return future;
  }

  CompletableFuture<Unit> listenerStopped(Throwable cause) {
    var future = new CompletableFuture<Unit>();

    fireEvent(new Event.ListenerStopped(cause, future));

    return future;
  }

  void fireEvent(Event event) {
    synchronized (mailbox) {
      mailbox.add(event);

      if (draining) {
        return;
      }

      draining = true;
    }

    executor.execute(this::drainMailbox);
  }

  State getState() {
    return state;
  }

  Channel getActiveChannel() {
    return activeChannel;
  }

  void addTransitionListener(TransitionListener listener) {
    transitionListeners.add(listener);
  }

  void removeTransitionListener(TransitionListener listener) {
    transitionListeners.remove(listener);
  }

  private static HandshakeStarter defaultHandshakeStarter() {
    var handshake = new UascClientReverseConnectHandshake();

    return handshake::start;
  }

  private void drainMailbox() {
    while (true) {
      Event event;

      synchronized (mailbox) {
        event = mailbox.poll();

        if (event == null) {
          draining = false;
          return;
        }
      }

      try {
        handleEvent(event);
      } catch (Throwable t) {
        LOGGER.error("Error handling Reverse Connect channel owner event: {}", event, t);
      }
    }
  }

  private void handleEvent(Event event) {
    if (event instanceof Event.Connect connect) {
      handleConnect(connect);
    } else if (event instanceof Event.GetChannel getChannel) {
      handleGetChannel(getChannel);
    } else if (event instanceof Event.Accepted accepted) {
      handleAccepted(accepted);
    } else if (event instanceof Event.HandshakeSucceeded succeeded) {
      handleHandshakeSucceeded(succeeded);
    } else if (event instanceof Event.HandshakeFailed failed) {
      handleHandshakeFailed(failed);
    } else if (event instanceof Event.ChannelInactive inactive) {
      handleChannelInactive(inactive);
    } else if (event instanceof Event.Disconnect disconnect) {
      handleDisconnect(disconnect);
    } else if (event instanceof Event.DisconnectClosed closed) {
      handleDisconnectClosed(closed);
    } else if (event instanceof Event.ListenerStopped stopped) {
      handleListenerStopped(stopped);
    } else if (event instanceof Event.PendingAcceptTimedOut timedOut) {
      handlePendingAcceptTimedOut(timedOut);
    } else if (event instanceof Event.ChannelWaiterTimedOut timedOut) {
      handleChannelWaiterTimedOut(timedOut);
    } else {
      throw new IllegalArgumentException("unexpected event: " + event);
    }
  }

  private void handleConnect(Event.Connect event) {
    if (state == State.Stopped) {
      event.future().completeExceptionally(shutdown("listener stopped"));
      return;
    }

    if (state == State.Disconnecting) {
      event.future().completeExceptionally(connectionClosed("disconnect in progress"));
      return;
    }

    if (state == State.Connected && hasActiveChannel()) {
      event.future().complete(activeChannel);
      return;
    }

    clearInactiveConnectedChannel();

    if (application == null) {
      application = Objects.requireNonNull(event.application(), "application");
    }

    addChannelWaiter(event.future(), true);

    if (pendingAccepted != null) {
      startHandshake(pendingAccepted);
    } else if (state == State.Idle) {
      transitionTo(State.Armed);
    }
  }

  private void handleGetChannel(Event.GetChannel event) {
    if (state == State.Connected && hasActiveChannel()) {
      event.future().complete(activeChannel);
      return;
    }

    clearInactiveConnectedChannel();

    if (state == State.Stopped) {
      event.future().completeExceptionally(shutdown("listener stopped"));
      return;
    }

    if (state == State.Disconnecting) {
      event.future().completeExceptionally(connectionClosed("disconnect in progress"));
      return;
    }

    addChannelWaiter(event.future(), false);
  }

  private void handleAccepted(Event.Accepted event) {
    if (state == State.Disconnecting || state == State.Stopped) {
      close(event.channel());
      return;
    }

    clearInactiveConnectedChannel();

    if (application == null && !isAllowedServerUri(event.reverseHello().serverUri())) {
      close(event.channel());
      return;
    }

    if (state == State.Handshaking || state == State.Connected || pendingAccepted != null) {
      close(event.channel());
      return;
    }

    var accepted = newAcceptedChannel(event.channel(), event.reverseHello());

    if (application == null) {
      pendingAccepted = accepted;
      return;
    }

    startHandshake(accepted);
  }

  private void handleHandshakeSucceeded(Event.HandshakeSucceeded event) {
    if (isStale(event.generation(), event.channel())) {
      close(event.secureChannel().getChannel());
      return;
    }

    if (state != State.Handshaking
        || handshaking == null
        || handshaking.channel != event.channel()) {
      close(event.secureChannel().getChannel());
      return;
    }

    handshaking = null;
    activeSecureChannel = event.secureChannel();
    activeChannel = event.secureChannel().getChannel();

    if (activeChannel == null) {
      activeChannel = event.channel();
    }

    transitionTo(State.Connected);

    completeChannelWaiters(activeChannel);
  }

  private void handleHandshakeFailed(Event.HandshakeFailed event) {
    if (isStale(event.generation(), event.channel())) {
      return;
    }

    if (state != State.Handshaking
        || handshaking == null
        || handshaking.channel != event.channel()) {
      return;
    }

    var failure =
        event.failure() != null ? event.failure() : connectionClosed("reverse connect failed");

    handshaking = null;
    transitionTo(application != null ? State.Armed : State.Idle);

    failChannelWaiters(failure);
    close(event.channel());
  }

  private void handleChannelInactive(Event.ChannelInactive event) {
    if (event.generation() != generation) {
      return;
    }

    if (pendingAccepted != null && pendingAccepted.channel == event.channel()) {
      cancelPendingAcceptTimeout(pendingAccepted);
      pendingAccepted = null;
      return;
    }

    if (handshaking != null && handshaking.channel == event.channel()) {
      handshaking = null;
      transitionTo(application != null ? State.Armed : State.Idle);
      failChannelWaiters(connectionClosed("channel closed during reverse connect handshake"));
      return;
    }

    if (activeChannel == event.channel()) {
      activeChannel = null;
      activeSecureChannel = null;
      transitionTo(application != null ? State.Armed : State.Idle);
    }
  }

  private void handleDisconnect(Event.Disconnect event) {
    if (state == State.Stopped) {
      if (cleanupInProgress) {
        disconnectWaiters.add(event.future());
      } else {
        event.future().complete(Unit.VALUE);
      }
      return;
    }

    if (state == State.Disconnecting) {
      disconnectWaiters.add(event.future());
      return;
    }

    var channelsToClose = clearOwnedState(connectionClosed("disconnected"));
    long disconnectGeneration = ++generation;

    disconnectWaiters.add(event.future());
    cleanupInProgress = true;
    transitionTo(State.Disconnecting);

    closeAll(channelsToClose)
        .whenComplete(
            (ignored, ex) -> fireEvent(new Event.DisconnectClosed(disconnectGeneration, ex)));
  }

  private void handleDisconnectClosed(Event.DisconnectClosed event) {
    if (event.generation() != generation || !cleanupInProgress) {
      return;
    }

    cleanupInProgress = false;

    if (state == State.Disconnecting) {
      transitionTo(State.Idle);
    }

    Throwable failure = event.failure() != null ? unwrap(event.failure()) : null;

    if (failure != null && disconnectWaiters.isEmpty() && listenerStoppedWaiters.isEmpty()) {
      LOGGER.warn("Reverse Connect channel cleanup failed", failure);
    }

    completeUnitWaiters(disconnectWaiters, failure);
    completeUnitWaiters(listenerStoppedWaiters, failure);
  }

  private void handleListenerStopped(Event.ListenerStopped event) {
    if (state == State.Stopped) {
      if (cleanupInProgress) {
        listenerStoppedWaiters.add(event.future());
      } else {
        event.future().complete(Unit.VALUE);
      }
      return;
    }

    if (state == State.Disconnecting) {
      listenerStoppedWaiters.add(event.future());
      transitionTo(State.Stopped);
      return;
    }

    var failure = event.cause() != null ? event.cause() : shutdown("listener stopped");
    var channelsToClose = clearOwnedState(failure);
    long listenerStopGeneration = ++generation;

    listenerStoppedWaiters.add(event.future());
    cleanupInProgress = true;
    transitionTo(State.Stopped);

    closeAll(channelsToClose)
        .whenComplete(
            (ignored, ex) -> fireEvent(new Event.DisconnectClosed(listenerStopGeneration, ex)));
  }

  private void handlePendingAcceptTimedOut(Event.PendingAcceptTimedOut event) {
    if (event.generation() != generation) {
      return;
    }

    if (application != null
        || pendingAccepted == null
        || pendingAccepted.channel != event.channel()) {
      return;
    }

    var timedOut = pendingAccepted;
    pendingAccepted = null;
    cancelPendingAcceptTimeout(timedOut);
    close(event.channel());
  }

  private void handleChannelWaiterTimedOut(Event.ChannelWaiterTimedOut event) {
    ChannelWaiter timedOutWaiter = null;

    for (var waiter : channelWaiters) {
      if (waiter.future() == event.future()) {
        timedOutWaiter = waiter;
        break;
      }
    }

    if (timedOutWaiter == null) {
      return;
    }

    var timeout =
        new UaException(StatusCodes.Bad_Timeout, "timed out waiting for reverse connect channel");

    if (!timedOutWaiter.connectWaiter()) {
      channelWaiters.remove(timedOutWaiter);
      timedOutWaiter.timeout().cancel();
      timedOutWaiter.future().completeExceptionally(timeout);
      return;
    }

    var channelsToClose = clearOwnedState(timeout);
    long timeoutGeneration = ++generation;

    cleanupInProgress = true;
    transitionTo(State.Disconnecting);

    closeAll(channelsToClose)
        .whenComplete(
            (ignored, ex) -> fireEvent(new Event.DisconnectClosed(timeoutGeneration, ex)));
  }

  private AcceptedChannel newAcceptedChannel(Channel channel, ReverseHelloMessage reverseHello) {
    long acceptedGeneration = generation;

    channel
        .closeFuture()
        .addListener(f -> fireEvent(new Event.ChannelInactive(acceptedGeneration, channel)));

    var timeout =
        scheduler.schedule(
            () -> fireEvent(new Event.PendingAcceptTimedOut(acceptedGeneration, channel)),
            config.connectTimeout(),
            TimeUnit.MILLISECONDS);

    return new AcceptedChannel(channel, reverseHello, acceptedGeneration, timeout);
  }

  private void startHandshake(AcceptedChannel accepted) {
    cancelPendingAcceptTimeout(accepted);

    pendingAccepted = null;
    handshaking = accepted;
    transitionTo(State.Handshaking);

    CompletableFuture<ClientSecureChannel> handshakeFuture;

    try {
      handshakeFuture =
          handshakeStarter.start(
              accepted.channel(),
              accepted.reverseHello(),
              config.uascConfig(),
              application,
              responseHandler,
              requestIdSupplier,
              config.allowedServerUris(),
              config.reverseHelloTimeout());

      if (handshakeFuture == null) {
        handshakeFuture =
            CompletableFuture.failedFuture(
                new NullPointerException("handshake future must not be null"));
      }
    } catch (Throwable t) {
      handshakeFuture = CompletableFuture.failedFuture(t);
    }

    handshakeFuture.whenComplete(
        (secureChannel, ex) -> {
          if (secureChannel != null) {
            fireEvent(
                new Event.HandshakeSucceeded(
                    accepted.generation(), accepted.channel(), secureChannel));
          } else {
            fireEvent(new Event.HandshakeFailed(accepted.generation(), accepted.channel(), ex));
          }
        });
  }

  private boolean isStale(long eventGeneration, Channel channel) {
    return eventGeneration != generation
        || (handshaking != null && handshaking.channel != channel && activeChannel != channel)
        || (handshaking == null && activeChannel != channel);
  }

  private List<Channel> clearOwnedState(Throwable waiterFailure) {
    var channelsToClose = new ArrayList<Channel>();

    if (pendingAccepted != null) {
      cancelPendingAcceptTimeout(pendingAccepted);
      channelsToClose.add(pendingAccepted.channel());
      pendingAccepted = null;
    }

    if (handshaking != null) {
      channelsToClose.add(handshaking.channel());
      handshaking = null;
    }

    if (activeChannel != null) {
      channelsToClose.add(activeChannel);
      activeChannel = null;
    }

    activeSecureChannel = null;
    application = null;

    failChannelWaiters(waiterFailure);

    return channelsToClose;
  }

  private void cancelPendingAcceptTimeout(AcceptedChannel accepted) {
    if (accepted.pendingAcceptTimeout() != null) {
      accepted.pendingAcceptTimeout().cancel();
    }
  }

  private void addChannelWaiter(CompletableFuture<Channel> future, boolean connectWaiter) {
    var timeout =
        scheduler.schedule(
            () -> fireEvent(new Event.ChannelWaiterTimedOut(future)),
            config.connectTimeout(),
            TimeUnit.MILLISECONDS);

    channelWaiters.add(new ChannelWaiter(future, timeout, connectWaiter));
  }

  private void completeChannelWaiters(Channel channel) {
    var waiters = List.copyOf(channelWaiters);
    channelWaiters.clear();

    waiters.forEach(
        waiter -> {
          waiter.timeout().cancel();
          waiter.future().complete(channel);
        });
  }

  private void failChannelWaiters(Throwable failure) {
    var waiters = List.copyOf(channelWaiters);
    channelWaiters.clear();

    waiters.forEach(
        waiter -> {
          waiter.timeout().cancel();
          waiter.future().completeExceptionally(failure);
        });
  }

  private void completeUnitWaiters(List<CompletableFuture<Unit>> waiters, Throwable failure) {
    var futures = List.copyOf(waiters);
    waiters.clear();

    for (CompletableFuture<Unit> future : futures) {
      if (failure != null) {
        future.completeExceptionally(failure);
      } else {
        future.complete(Unit.VALUE);
      }
    }
  }

  private CompletableFuture<Void> closeAll(List<Channel> channels) {
    if (channels.isEmpty()) {
      return CompletableFuture.completedFuture(null);
    }

    CompletableFuture<?>[] closeFutures =
        channels.stream().distinct().map(this::close).toArray(CompletableFuture[]::new);

    return CompletableFuture.allOf(closeFutures);
  }

  private CompletableFuture<Void> close(Channel channel) {
    if (channel == null) {
      return CompletableFuture.completedFuture(null);
    }

    var closeFuture = new CompletableFuture<Void>();

    channel
        .close()
        .addListener(
            f -> {
              if (f.isSuccess()) {
                closeFuture.complete(null);
              } else {
                closeFuture.completeExceptionally(f.cause());
              }
            });

    return closeFuture;
  }

  private static Throwable unwrap(Throwable failure) {
    if (failure instanceof CompletionException && failure.getCause() != null) {
      return failure.getCause();
    }

    return failure;
  }

  private boolean isAllowedServerUri(String serverUri) {
    return config.allowedServerUris().isEmpty() || config.allowedServerUris().contains(serverUri);
  }

  private void transitionTo(State newState) {
    State oldState = state;
    if (oldState == newState) {
      return;
    }

    state = newState;

    transitionListeners.forEach(
        listener -> {
          try {
            listener.onTransition(oldState, newState);
          } catch (Throwable t) {
            LOGGER.warn("Reverse Connect channel owner transition listener failed", t);
          }
        });
  }

  private boolean hasActiveChannel() {
    return activeChannel != null && activeChannel.isActive();
  }

  private void clearInactiveConnectedChannel() {
    if (state == State.Connected && !hasActiveChannel()) {
      activeChannel = null;
      activeSecureChannel = null;
      transitionTo(application != null ? State.Armed : State.Idle);
    }
  }

  private static UaException connectionClosed(String message) {
    return new UaException(StatusCodes.Bad_ConnectionClosed, message);
  }

  private static UaException shutdown(String message) {
    return new UaException(StatusCodes.Bad_Shutdown, message);
  }
}
