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

import com.digitalpetri.fsm.Fsm;
import com.digitalpetri.fsm.FsmContext;
import com.digitalpetri.fsm.dsl.ActionContext;
import com.digitalpetri.fsm.dsl.FsmBuilder;
import com.digitalpetri.fsm.dsl.TransitionAction;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.eclipse.milo.opcua.stack.transport.client.uasc.InboundUascResponseHandler.DelegatingUascResponseHandler;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientReverseHelloHandler;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@code strict-machine} FSM managing the lifecycle of a single reverse-connected channel on the
 * client side. The client listens for inbound server connections; this FSM tracks the state from
 * initial acceptance through handshake to an active SecureChannel, and handles reconnection when
 * the channel drops.
 */
public class ReverseConnectChannelFsm {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReverseConnectChannelFsm.class);

  static final FsmContext.Key<CompletableFuture> KEY_CF =
      new FsmContext.Key<>("connectFuture", CompletableFuture.class);

  static final FsmContext.Key<Channel> KEY_CHANNEL = new FsmContext.Key<>("channel", Channel.class);

  static final FsmContext.Key<ClientSecureChannel> KEY_SECURE_CHANNEL =
      new FsmContext.Key<>("secureChannel", ClientSecureChannel.class);

  static final FsmContext.Key<ClientApplicationContext> KEY_APPLICATION =
      new FsmContext.Key<>("application", ClientApplicationContext.class);

  /** States for the Reverse Connect channel FSM. */
  public enum State {
    /** Listening, no server has connected yet. */
    NotConnected,
    /** RHE received, Hello/Ack/OPN in progress. */
    Handshaking,
    /** SecureChannel active, ready for service requests. */
    Connected,
    /** Connection lost, waiting for server to re-connect. */
    Reconnecting
  }

  /** Events for the Reverse Connect channel FSM. */
  public sealed interface Event {

    /** External: request a channel. Carries a future completed when connected. */
    record Connect(CompletableFuture<Channel> future) implements Event {}

    /** External: request disconnect. Carries a future completed when disconnected. */
    record Disconnect(CompletableFuture<Unit> future) implements Event {}

    /** External: get the current channel. Carries a future completed with the channel. */
    record GetChannel(CompletableFuture<Channel> future) implements Event {}

    /** Internal: a server connected inbound and sent a ReverseHello. */
    record ConnectionAccepted(Channel channel, ReverseHelloMessage reverseHello) implements Event {}

    /** Internal: the handshake completed and a SecureChannel is ready. */
    record HandshakeSuccess(ClientSecureChannel secureChannel) implements Event {}

    /** Internal: the handshake failed. */
    record HandshakeFailure(Throwable failure) implements Event {}

    /** Internal: the channel became inactive. */
    record ChannelInactive() implements Event {}
  }

  /** Listener notified on every state transition. */
  public interface TransitionListener {

    /**
     * Called when the FSM transitions from one state to another.
     *
     * @param from the state the FSM is leaving.
     * @param to the state the FSM is entering.
     */
    void onStateTransition(State from, State to);
  }

  private final Fsm<State, Event> fsm;

  private final CopyOnWriteArrayList<TransitionListener> transitionListeners;

  private ReverseConnectChannelFsm(
      Fsm<State, Event> fsm, CopyOnWriteArrayList<TransitionListener> transitionListeners) {

    this.fsm = fsm;
    this.transitionListeners = transitionListeners;
  }

  /** Fire an event into the FSM. */
  public void fireEvent(Event event) {
    fsm.fireEvent(event);
  }

  /** Get the current FSM state. */
  public State getState() {
    return fsm.getState();
  }

  /** Get the current channel, or {@code null} if not connected. */
  public Channel getChannel() {
    return fsm.getFromContext(KEY_CHANNEL::get);
  }

  /** Store the {@link ClientApplicationContext} in the FSM context. */
  public void setApplicationContext(ClientApplicationContext application) {
    fsm.withContext(ctx -> KEY_APPLICATION.set(ctx, application));
  }

  /**
   * Add a listener that will be notified on every state transition.
   *
   * @param listener the listener to add.
   */
  public void addTransitionListener(TransitionListener listener) {
    transitionListeners.add(listener);
  }

  /**
   * Remove a previously added transition listener.
   *
   * @param listener the listener to remove.
   */
  public void removeTransitionListener(TransitionListener listener) {
    transitionListeners.remove(listener);
  }

  /**
   * Create a new {@link ReverseConnectChannelFsm}.
   *
   * @param config the Reverse Connect transport config.
   * @param responseHandler the response handler for delegating UASC responses.
   * @param requestIdSupplier supplier for UASC request IDs.
   * @param executor the executor for FSM event processing.
   * @return a new FSM in the {@link State#NotConnected} state.
   */
  static ReverseConnectChannelFsm create(
      OpcTcpReverseConnectTransportConfig config,
      UascResponseHandler responseHandler,
      Supplier<Long> requestIdSupplier,
      Executor executor) {

    var transitionListeners = new CopyOnWriteArrayList<TransitionListener>();

    var fb = new FsmBuilder<State, Event>("ReverseConnectChannel", Map.of(), executor, null);

    configureNotConnectedState(fb);
    configureHandshakingState(fb, config, responseHandler, requestIdSupplier);
    configureConnectedState(fb);
    configureReconnectingState(fb);
    configureNotConnectedEntryViaDisconnect(fb);

    fb.addTransitionAction(
        new TransitionAction<>() {
          @Override
          public void execute(ActionContext<State, Event> ctx) {
            transitionListeners.forEach(l -> l.onStateTransition(ctx.from(), ctx.to()));
          }

          @Override
          public boolean matches(State from, State to, Event event) {
            return true;
          }
        });

    Fsm<State, Event> fsm = fb.build(State.NotConnected);

    return new ReverseConnectChannelFsm(fsm, transitionListeners);
  }

  // ---------------------------------------------------------------------------
  // State configuration
  // ---------------------------------------------------------------------------

  private static void configureNotConnectedState(FsmBuilder<State, Event> fb) {
    /* Transitions */
    fb.when(State.NotConnected).on(Event.ConnectionAccepted.class).transitionTo(State.Handshaking);

    /* Internal transitions */
    fb.onInternalTransition(State.NotConnected)
        .via(Event.Connect.class)
        .execute(ReverseConnectChannelFsm::chainConnectFuture);

    fb.onInternalTransition(State.NotConnected)
        .via(Event.Disconnect.class)
        .execute(
            ctx -> {
              var disconnect = (Event.Disconnect) ctx.event();
              disconnect.future().complete(Unit.VALUE);
            });

    fb.onInternalTransition(State.NotConnected)
        .via(Event.GetChannel.class)
        .execute(ReverseConnectChannelFsm::chainGetChannelFuture);
  }

  private static void configureHandshakingState(
      FsmBuilder<State, Event> fb,
      OpcTcpReverseConnectTransportConfig config,
      UascResponseHandler responseHandler,
      Supplier<Long> requestIdSupplier) {

    /* Transitions */
    fb.when(State.Handshaking).on(Event.HandshakeSuccess.class).transitionTo(State.Connected);
    fb.when(State.Handshaking).on(Event.HandshakeFailure.class).transitionTo(State.Reconnecting);
    fb.when(State.Handshaking).on(Event.ChannelInactive.class).transitionTo(State.Reconnecting);

    /* Internal transitions — shelve Disconnect, chain Connect/GetChannel */
    fb.onInternalTransition(State.Handshaking)
        .via(Event.Disconnect.class)
        .execute(ctx -> ctx.shelveEvent(ctx.event()));

    fb.onInternalTransition(State.Handshaking)
        .via(Event.Connect.class)
        .execute(ReverseConnectChannelFsm::chainConnectFuture);

    fb.onInternalTransition(State.Handshaking)
        .via(Event.GetChannel.class)
        .execute(ReverseConnectChannelFsm::chainGetChannelFuture);

    /* Entry action: start the handshake on the accepted channel */
    fb.onTransitionTo(State.Handshaking)
        .fromAny()
        .via(Event.ConnectionAccepted.class)
        .execute(
            ctx -> {
              var event = (Event.ConnectionAccepted) ctx.event();
              Channel channel = event.channel();
              ReverseHelloMessage rhe = event.reverseHello();
              ClientApplicationContext application = KEY_APPLICATION.get(ctx);

              KEY_CHANNEL.set(ctx, channel);

              var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

              // Install the response handler first, then the reverse hello handler.
              channel.pipeline().addLast(new DelegatingUascResponseHandler(responseHandler));

              var handler =
                  new UascClientReverseHelloHandler(
                      config,
                      application,
                      requestIdSupplier,
                      handshakeFuture,
                      config.getAllowedServerUris(),
                      config.getReverseHelloTimeout());

              channel.pipeline().addLast(handler);

              // Re-fire the already-decoded RHE message into the pipeline so the
              // handler can process it. The ReverseHelloDecoder (installed by the
              // transport on the child channel) already consumed the raw bytes and
              // decoded the RHE; we re-encode it here so the handler receives the
              // complete wire-format message it expects.
              try {
                ByteBuf rheBuf = TcpMessageEncoder.encode(rhe);
                channel.pipeline().fireChannelRead(rheBuf);
              } catch (UaException e) {
                LOGGER.error("Failed to re-encode ReverseHello for pipeline replay", e);
                handshakeFuture.completeExceptionally(e);
                channel.close();
                return;
              }

              handshakeFuture.whenComplete(
                  (sc, ex) -> {
                    if (sc != null) {
                      ctx.fireEvent(new Event.HandshakeSuccess(sc));
                    } else {
                      ctx.fireEvent(
                          new Event.HandshakeFailure(
                              ex != null ? ex : new Exception("handshake failed")));
                    }
                  });

              channel.closeFuture().addListener(f -> ctx.fireEvent(new Event.ChannelInactive()));
            });
  }

  private static void configureConnectedState(FsmBuilder<State, Event> fb) {
    /* Transitions */
    fb.when(State.Connected).on(Event.ChannelInactive.class).transitionTo(State.Reconnecting);
    fb.when(State.Connected).on(Event.Disconnect.class).transitionTo(State.NotConnected);

    /* Internal transitions — complete Connect/GetChannel immediately */
    fb.onInternalTransition(State.Connected)
        .via(Event.Connect.class)
        .execute(
            ctx -> {
              var connect = (Event.Connect) ctx.event();
              Channel ch = KEY_CHANNEL.get(ctx);
              connect.future().complete(ch);
            });

    fb.onInternalTransition(State.Connected)
        .via(Event.GetChannel.class)
        .execute(
            ctx -> {
              var get = (Event.GetChannel) ctx.event();
              Channel ch = KEY_CHANNEL.get(ctx);
              get.future().complete(ch);
            });

    /* Reject duplicate inbound connections while already connected */
    fb.onInternalTransition(State.Connected)
        .via(Event.ConnectionAccepted.class)
        .execute(
            ctx -> {
              var event = (Event.ConnectionAccepted) ctx.event();
              Channel newChannel = event.channel();
              LOGGER.debug(
                  "Rejecting inbound connection from {} while already connected",
                  newChannel.remoteAddress());
              newChannel.close();
            });

    /* Entry action: store channel, complete waiting futures */
    fb.onTransitionTo(State.Connected)
        .fromAny()
        .via(Event.HandshakeSuccess.class)
        .execute(
            ctx -> {
              var event = (Event.HandshakeSuccess) ctx.event();
              ClientSecureChannel sc = event.secureChannel();
              KEY_CHANNEL.set(ctx, sc.getChannel());
              KEY_SECURE_CHANNEL.set(ctx, sc);

              @SuppressWarnings("unchecked")
              CompletableFuture<Channel> cf = KEY_CF.remove(ctx);
              if (cf != null && !cf.isDone()) {
                cf.complete(sc.getChannel());
              }

              ctx.processShelvedEvents();
            });
  }

  private static void configureReconnectingState(FsmBuilder<State, Event> fb) {
    /* Transitions */
    fb.when(State.Reconnecting).on(Event.ConnectionAccepted.class).transitionTo(State.Handshaking);
    fb.when(State.Reconnecting).on(Event.Disconnect.class).transitionTo(State.NotConnected);

    /* Internal transitions — chain Connect/GetChannel to KEY_CF */
    fb.onInternalTransition(State.Reconnecting)
        .via(Event.Connect.class)
        .execute(ReverseConnectChannelFsm::chainConnectFuture);

    fb.onInternalTransition(State.Reconnecting)
        .via(Event.GetChannel.class)
        .execute(ReverseConnectChannelFsm::chainGetChannelFuture);

    /* Entry action: clear old channel state, chain existing waiters */
    fb.onTransitionTo(State.Reconnecting)
        .fromAny()
        .viaAny()
        .execute(
            ctx -> {
              KEY_CHANNEL.remove(ctx);
              KEY_SECURE_CHANNEL.remove(ctx);

              @SuppressWarnings("unchecked")
              CompletableFuture<Channel> oldCf = KEY_CF.remove(ctx);
              var newCf = new CompletableFuture<Channel>();
              if (oldCf != null && !oldCf.isDone()) {
                newCf.whenComplete(
                    (ch, ex) -> {
                      if (ex != null) oldCf.completeExceptionally(ex);
                      else oldCf.complete(ch);
                    });
              }
              KEY_CF.set(ctx, newCf);

              ctx.processShelvedEvents();
            });
  }

  /**
   * Configure the entry action for transitioning to {@link State#NotConnected} via {@link
   * Event.Disconnect}: close the channel, fail pending waiters, and complete the disconnect future.
   */
  private static void configureNotConnectedEntryViaDisconnect(FsmBuilder<State, Event> fb) {
    fb.onTransitionTo(State.NotConnected)
        .fromAny()
        .via(Event.Disconnect.class)
        .execute(
            ctx -> {
              Channel ch = KEY_CHANNEL.remove(ctx);
              if (ch != null && ch.isActive()) {
                ch.close();
              }
              KEY_SECURE_CHANNEL.remove(ctx);

              @SuppressWarnings("unchecked")
              CompletableFuture<Channel> cf = KEY_CF.remove(ctx);
              if (cf != null && !cf.isDone()) {
                cf.completeExceptionally(
                    new UaException(StatusCodes.Bad_ConnectionClosed, "disconnected"));
              }

              var disconnect = (Event.Disconnect) ctx.event();
              disconnect.future().complete(Unit.VALUE);

              ctx.processShelvedEvents();
            });
  }

  // ---------------------------------------------------------------------------
  // Shared helpers
  // ---------------------------------------------------------------------------

  /**
   * Chain a {@link Event.Connect} or {@link Event.GetChannel} future to the existing {@code
   * KEY_CF}. If no existing future is present, create a new one and store it.
   */
  private static void chainConnectFuture(ActionContext<State, Event> ctx) {
    var connect = (Event.Connect) ctx.event();
    chainFuture(ctx, connect.future());
  }

  private static void chainGetChannelFuture(ActionContext<State, Event> ctx) {
    var get = (Event.GetChannel) ctx.event();
    chainFuture(ctx, get.future());
  }

  private static void chainFuture(
      ActionContext<State, Event> ctx, CompletableFuture<Channel> callerFuture) {

    @SuppressWarnings("unchecked")
    CompletableFuture<Channel> existingCf = KEY_CF.get(ctx);
    if (existingCf != null && !existingCf.isDone()) {
      existingCf.whenComplete(
          (ch, ex) -> {
            if (ex != null) callerFuture.completeExceptionally(ex);
            else callerFuture.complete(ch);
          });
    } else {
      var cf = new CompletableFuture<Channel>();
      KEY_CF.set(ctx, cf);
      cf.whenComplete(
          (ch, ex) -> {
            if (ex != null) callerFuture.completeExceptionally(ex);
            else callerFuture.complete(ch);
          });
    }
  }
}
