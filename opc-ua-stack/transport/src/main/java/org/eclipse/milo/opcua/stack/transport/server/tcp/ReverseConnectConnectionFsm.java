/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import com.digitalpetri.fsm.Fsm;
import com.digitalpetri.fsm.FsmContext;
import com.digitalpetri.fsm.dsl.ActionContext;
import com.digitalpetri.fsm.dsl.FsmBuilder;
import com.digitalpetri.fsm.dsl.TransitionAction;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.uasc.SecureChannelOpenedEvent;
import org.jspecify.annotations.Nullable;

/**
 * A {@code strict-machine} FSM managing the lifecycle of a single outbound reverse connection to a
 * client. The Reverse Connect manager holds one or more FSM instances per registered client target.
 */
public class ReverseConnectConnectionFsm {

  static final FsmContext.Key<Channel> KEY_CHANNEL = new FsmContext.Key<>("channel", Channel.class);

  @SuppressWarnings("rawtypes")
  static final FsmContext.Key<ScheduledFuture> KEY_RETRY_FUTURE =
      new FsmContext.Key<>("retryFuture", ScheduledFuture.class);

  static final FsmContext.Key<Long> KEY_RETRY_DELAY_MS =
      new FsmContext.Key<>("retryDelayMs", Long.class);

  private static final FsmContext.Key<Boolean> KEY_STOP_REQUESTED_WHILE_CONNECTING =
      new FsmContext.Key<>("stopRequestedWhileConnecting", Boolean.class);

  private ReverseConnectConnectionFsm() {}

  /** States for the Reverse Connect connection FSM. */
  public enum State {
    /** Initial state, no outbound connection. */
    Idle,
    /** TCP connect in progress. */
    Connecting,
    /** Backoff before retrying. */
    ConnectWait,
    /** TCP connected, ReverseHello sent, waiting for Hello. */
    RheSent,
    /** Client has opened a SecureChannel on this socket. */
    Active,
    /** Terminal - connection removed or manager stopped. */
    Disconnected
  }

  /** Events for the Reverse Connect connection FSM. */
  public sealed interface Event {

    /** Begin connecting to the client. Transitions from {@link State#Idle} to Connecting. */
    record Start() implements Event {}

    /** Stop this connection. Carries a future completed when the FSM reaches Disconnected. */
    record Stop(CompletableFuture<Void> future) implements Event {}

    /** TCP connect succeeded. Carries the newly opened channel. */
    record ConnectSuccess(Channel channel) implements Event {}

    /** TCP connect failed. Carries the cause of the failure. */
    record ConnectFailure(Throwable failure) implements Event {}

    /** The client opened a SecureChannel on this socket. */
    record SecureChannelOpened() implements Event {}

    /** The client rejected the connection with the given OPC UA status code. */
    record ClientRejected(long statusCode) implements Event {}

    /** The underlying TCP channel became inactive. */
    record ChannelInactive() implements Event {}

    /** The retry delay timer has elapsed; the FSM should attempt reconnection. */
    record RetryDelayElapsed() implements Event {}
  }

  /**
   * Create a new FSM for managing a single Reverse Connect connection.
   *
   * @param clientEndpointUrl the client's listening endpoint URL.
   * @param endpointUrl the server endpoint URL sent in ReverseHello messages.
   * @param serverUri the server's ApplicationUri sent in ReverseHello messages.
   * @param transport the transport used to initiate outbound connections.
   * @param applicationContext the server application context.
   * @param config the Reverse Connect configuration (timeouts, backoff, etc.).
   * @param executor the executor for FSM event processing.
   * @param scheduler the scheduler for retry timers.
   * @param onActiveCallback optional callback invoked when the FSM transitions to {@link
   *     State#Active}. May be {@code null}.
   * @return a new FSM in the {@link State#Idle} state.
   */
  public static Fsm<State, Event> newFsm(
      String clientEndpointUrl,
      String endpointUrl,
      String serverUri,
      OpcTcpReverseConnectServerTransport transport,
      ServerApplicationContext applicationContext,
      ReverseConnectConfig config,
      ExecutorService executor,
      ScheduledExecutorService scheduler,
      @Nullable Runnable onActiveCallback) {

    var fb =
        new FsmBuilder<State, Event>(
            "ReverseConnect", Map.of("client-url", clientEndpointUrl), executor, null);

    long connectTimeoutMs = config.getConnectTimeout().toMillis();
    long connectIntervalMs = config.getConnectInterval().toMillis();
    long rejectBackoffMs = config.getRejectBackoff().toMillis();
    long maxReconnectDelayMs = config.getMaxReconnectDelay().toMillis();

    configureIdleState(fb);
    configureConnectingState(
        fb,
        clientEndpointUrl,
        endpointUrl,
        serverUri,
        transport,
        applicationContext,
        connectTimeoutMs);
    configureConnectWaitState(fb, connectIntervalMs, maxReconnectDelayMs, scheduler);
    configureRheSentState(fb);
    configureActiveState(fb);
    configureDisconnectedState(fb);

    configureConnectWaitRejectOverride(fb, rejectBackoffMs);

    if (onActiveCallback != null) {
      fb.addTransitionAction(
          new TransitionAction<>() {
            @Override
            public void execute(ActionContext<State, Event> ctx) {
              onActiveCallback.run();
            }

            @Override
            public boolean matches(State from, State to, Event event) {
              return to == State.Active;
            }
          });
    }

    return fb.build(State.Idle);
  }

  private static void configureIdleState(FsmBuilder<State, Event> fb) {
    /* Transitions */
    fb.when(State.Idle).on(Event.Start.class).transitionTo(State.Connecting);
    fb.when(State.Idle).on(Event.Stop.class).transitionTo(State.Disconnected);
  }

  private static void configureConnectingState(
      FsmBuilder<State, Event> fb,
      String clientEndpointUrl,
      String endpointUrl,
      String serverUri,
      OpcTcpReverseConnectServerTransport transport,
      ServerApplicationContext applicationContext,
      long connectTimeoutMs) {

    /* Transitions */
    fb.when(State.Connecting).on(Event.ConnectSuccess.class).transitionTo(State.RheSent);
    fb.when(State.Connecting).on(Event.ConnectFailure.class).transitionTo(State.ConnectWait);

    /* Internal Transition Actions - shelve Stop events */
    fb.onInternalTransition(State.Connecting)
        .via(Event.Stop.class)
        .execute(
            ctx -> {
              KEY_STOP_REQUESTED_WHILE_CONNECTING.set(ctx, true);
              ctx.shelveEvent(ctx.event());
            });

    /* External Transition Actions */
    // Exclude self-transitions so shelved Stop handling in Connecting
    // does not re-enter the outbound connect action.
    fb.onTransitionTo(State.Connecting)
        .from(state -> state != State.Connecting)
        .viaAny()
        .execute(
            ctx -> {
              String host = EndpointUtil.getHost(clientEndpointUrl);
              int port = EndpointUtil.getPort(clientEndpointUrl);
              var clientAddress = new InetSocketAddress(Objects.requireNonNull(host), port);

              transport
                  .connect(
                      applicationContext, clientAddress, serverUri, endpointUrl, connectTimeoutMs)
                  .whenComplete(
                      (channel, ex) -> {
                        if (ex != null) {
                          ctx.fireEvent(new Event.ConnectFailure(ex));
                        } else {
                          ctx.fireEvent(new Event.ConnectSuccess(channel));
                        }
                      });
            });
  }

  private static void configureConnectWaitState(
      FsmBuilder<State, Event> fb,
      long connectIntervalMs,
      long maxReconnectDelayMs,
      ScheduledExecutorService scheduler) {

    /* Transitions */
    fb.when(State.ConnectWait).on(Event.RetryDelayElapsed.class).transitionTo(State.Connecting);
    fb.when(State.ConnectWait).on(Event.Stop.class).transitionTo(State.Disconnected);

    /* External Transition Actions */

    // Leaving ConnectWait: cancel the retry timer
    fb.onTransitionFrom(State.ConnectWait)
        .toAny()
        .viaAny()
        .execute(
            ctx -> {
              ScheduledFuture<?> sf = KEY_RETRY_FUTURE.remove(ctx);
              if (sf != null) {
                sf.cancel(false);
              }
            });

    // Entering ConnectWait: schedule the retry timer with exponential backoff
    fb.onTransitionTo(State.ConnectWait)
        .fromAny()
        .viaAny()
        .execute(
            ctx -> {
              if (isStopRequestedWhileConnecting(ctx)) {
                ctx.processShelvedEvents();
                return;
              }

              long delayMs =
                  Optional.ofNullable(KEY_RETRY_DELAY_MS.get(ctx)).orElse(connectIntervalMs);

              long nextDelay = Math.min(delayMs * 2, maxReconnectDelayMs);
              KEY_RETRY_DELAY_MS.set(ctx, nextDelay);

              ScheduledFuture<?> sf =
                  scheduler.schedule(
                      () -> ctx.fireEvent(new Event.RetryDelayElapsed()),
                      delayMs,
                      TimeUnit.MILLISECONDS);
              KEY_RETRY_FUTURE.set(ctx, sf);

              ctx.processShelvedEvents();
            });
  }

  /**
   * Override the backoff delay when entering ConnectWait via ClientRejected. Uses {@code
   * executeFirst} so the reject backoff is set before the general ConnectWait entry action reads
   * it.
   */
  private static void configureConnectWaitRejectOverride(
      FsmBuilder<State, Event> fb, long rejectBackoffMs) {

    fb.onTransitionTo(State.ConnectWait)
        .from(State.RheSent)
        .via(Event.ClientRejected.class)
        .executeFirst(ctx -> KEY_RETRY_DELAY_MS.set(ctx, rejectBackoffMs));
  }

  private static void configureRheSentState(FsmBuilder<State, Event> fb) {
    /* Transitions */
    fb.when(State.RheSent).on(Event.SecureChannelOpened.class).transitionTo(State.Active);
    fb.when(State.RheSent).on(Event.ClientRejected.class).transitionTo(State.ConnectWait);
    fb.when(State.RheSent).on(Event.ChannelInactive.class).transitionTo(State.ConnectWait);
    fb.when(State.RheSent).on(Event.Stop.class).transitionTo(State.Disconnected);

    /* External Transition Actions */

    // Entering RheSent: store channel, reset backoff, monitor channel
    fb.onTransitionTo(State.RheSent)
        .from(State.Connecting)
        .via(Event.ConnectSuccess.class)
        .execute(
            ctx -> {
              Channel channel = ((Event.ConnectSuccess) ctx.event()).channel();
              KEY_CHANNEL.set(ctx, channel);

              if (isStopRequestedWhileConnecting(ctx)) {
                ctx.processShelvedEvents();
                return;
              }

              // Reset backoff on successful connect
              KEY_RETRY_DELAY_MS.remove(ctx);

              // Monitor for channel close
              channel.closeFuture().addListener(f -> ctx.fireEvent(new Event.ChannelInactive()));

              // Monitor for SecureChannel open via Netty user event
              channel
                  .pipeline()
                  .addLast(
                      new ChannelInboundHandlerAdapter() {
                        @Override
                        public void userEventTriggered(ChannelHandlerContext chCtx, Object evt) {
                          if (evt instanceof SecureChannelOpenedEvent) {
                            ctx.fireEvent(new Event.SecureChannelOpened());
                          }
                          chCtx.fireUserEventTriggered(evt);
                        }
                      });

              // Drain shelved events so any Stop shelved during Connecting
              // is replayed now that the channel is available for cleanup.
              ctx.processShelvedEvents();
            });
  }

  private static void configureActiveState(FsmBuilder<State, Event> fb) {
    /* Transitions */
    fb.when(State.Active).on(Event.ChannelInactive.class).transitionTo(State.ConnectWait);
    fb.when(State.Active).on(Event.Stop.class).transitionTo(State.Disconnected);

    /* External Transition Actions */

    // Entering Active: reset backoff, process shelved events
    fb.onTransitionTo(State.Active)
        .fromAny()
        .via(Event.SecureChannelOpened.class)
        .execute(
            ctx -> {
              KEY_RETRY_DELAY_MS.remove(ctx);
              ctx.processShelvedEvents();
            });
  }

  private static void configureDisconnectedState(FsmBuilder<State, Event> fb) {
    /* External Transition Actions */

    // Entering Disconnected: clean up everything
    fb.onTransitionTo(State.Disconnected)
        .fromAny()
        .viaAny()
        .execute(
            ctx -> {
              Channel ch = KEY_CHANNEL.remove(ctx);
              if (ch != null && ch.isActive()) {
                ch.close();
              }

              ScheduledFuture<?> sf = KEY_RETRY_FUTURE.remove(ctx);
              if (sf != null) {
                sf.cancel(false);
              }

              KEY_RETRY_DELAY_MS.remove(ctx);
              KEY_STOP_REQUESTED_WHILE_CONNECTING.remove(ctx);

              ctx.processShelvedEvents();
            });

    // Complete the Stop future when entering Disconnected via Stop
    fb.onTransitionTo(State.Disconnected)
        .fromAny()
        .via(Event.Stop.class)
        .execute(
            ctx -> {
              var stop = (Event.Stop) ctx.event();
              stop.future().complete(null);
            });

    /* Internal Transition Actions */

    // Complete Stop futures replayed from the shelf while already Disconnected
    fb.onInternalTransition(State.Disconnected)
        .via(Event.Stop.class)
        .execute(
            ctx -> {
              var stop = (Event.Stop) ctx.event();
              stop.future().complete(null);
            });
  }

  private static boolean isStopRequestedWhileConnecting(FsmContext<State, Event> ctx) {
    return Boolean.TRUE.equals(KEY_STOP_REQUESTED_WHILE_CONNECTING.get(ctx));
  }
}
