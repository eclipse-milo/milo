/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_CHANNEL_FSM_TRANSITION_LISTENER;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_CLOSE_FUTURE;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_KEEP_ALIVE_FAILURE_COUNT;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_KEEP_ALIVE_SCHEDULED_FUTURE;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_PENDING_SESSION;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_SESSION;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_SESSION_ACTIVITY_LISTENERS;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_SESSION_FUTURE;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_SESSION_INITIALIZERS;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_WAIT_FUTURE;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_WAIT_TIME;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.complete;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.failedFuture;

import com.digitalpetri.fsm.Fsm;
import com.digitalpetri.fsm.FsmContext;
import com.digitalpetri.fsm.dsl.ActionContext;
import com.digitalpetri.fsm.dsl.FsmBuilder;
import com.digitalpetri.netty.fsm.ChannelFsm;
import com.google.common.collect.Streams;
import com.google.common.primitives.Bytes;
import io.netty.channel.Channel;
import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.*;
import org.eclipse.milo.opcua.sdk.client.identity.SignedIdentityToken;
import org.eclipse.milo.opcua.sdk.client.session.SessionFsm.SessionFuture;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.stack.core.*;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferResult;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.core.util.SignatureUtil;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.MDC.MDCCloseable;

public class SessionFsmFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(SessionFsm.LOGGER_NAME);

  private static final AtomicLong INSTANCE_ID = new AtomicLong();

  private static final int MAX_WAIT_SECONDS = 16;

  /**
   * Lower bound, in milliseconds, on the keep-alive interval derived from a revised session
   * timeout, so that a Server revising the timeout down to a very small value can't turn the
   * keep-alive into a flood of requests.
   */
  private static final long MIN_KEEP_ALIVE_INTERVAL = 1000L;

  /**
   * StatusCodes that mean the Server no longer has a usable Session for us, i.e. the Session it
   * would have to be reactivated against is gone or was never activated.
   */
  private static final Predicate<StatusCode> SESSION_ERROR =
      statusCode -> {
        long status = statusCode.value();

        return status == StatusCodes.Bad_SessionClosed
            || status == StatusCodes.Bad_SessionIdInvalid
            || status == StatusCodes.Bad_SessionNotActivated;
      };

  private SessionFsmFactory() {}

  public static SessionFsm newSessionFsm(OpcUaClient client) {
    Long instanceId = INSTANCE_ID.incrementAndGet();

    FsmBuilder<State, Event> builder =
        new FsmBuilder<>(
            SessionFsm.LOGGER_NAME,
            Map.of("instance-id", String.valueOf(instanceId)),
            client.getTransport().getConfig().getExecutor(),
            instanceId);

    configureSessionFsm(builder, client);

    Fsm<State, Event> fsm = builder.build(State.Inactive);

    client.addFaultListener(new SessionFaultListener(fsm));

    return new SessionFsm(fsm);
  }

  private static void configureSessionFsm(FsmBuilder<State, Event> fb, OpcUaClient client) {
    configureInactiveState(fb, client);
    configureCreatingWaitState(fb, client);
    configureCreatingState(fb, client);
    configureActivatingState(fb, client);
    configureTransferringState(fb, client);
    configureInitializingState(fb, client);
    configureActiveState(fb, client);
    configureClosingState(fb, client);
    configureReactivatingWaitState(fb, client);
    configureReactivatingState(fb, client);
  }

  private static void configureInactiveState(
      FsmBuilder<State, Event> fb, @SuppressWarnings("unused") OpcUaClient client) {

    /* Transitions */

    fb.when(State.Inactive).on(Event.OpenSession.class).transitionTo(State.Creating);

    /* External Transition Actions */

    fb.onTransitionTo(State.Inactive)
        .from(s -> s != State.Inactive)
        .viaAny()
        .execute(FsmContext::processShelvedEvents);

    /* Internal Transition Actions */

    fb.onInternalTransition(State.Inactive)
        .via(Event.GetSession.class)
        .execute(
            ctx -> {
              Event.GetSession event = (Event.GetSession) ctx.event();

              client
                  .getTransport()
                  .getConfig()
                  .getExecutor()
                  .execute(
                      () ->
                          event.future.completeExceptionally(
                              new UaException(StatusCodes.Bad_SessionClosed)));
            });

    fb.onInternalTransition(State.Inactive)
        .via(Event.CloseSession.class)
        .execute(
            ctx -> {
              Event.CloseSession event = (Event.CloseSession) ctx.event();

              client
                  .getTransport()
                  .getConfig()
                  .getExecutor()
                  .execute(() -> event.future.complete(Unit.VALUE));
            });
  }

  private static void configureCreatingWaitState(
      FsmBuilder<State, Event> fb, @SuppressWarnings("unused") OpcUaClient client) {

    /* Transitions */

    fb.when(State.CreatingWait).on(Event.CreatingWaitExpired.class).transitionTo(State.Creating);

    fb.when(State.CreatingWait).on(Event.CloseSession.class).transitionTo(State.Inactive);

    /* External Transition Actions */

    fb.onTransitionTo(State.CreatingWait)
        .from(s -> s != State.CreatingWait)
        .viaAny()
        .execute(FsmContext::processShelvedEvents);

    fb.onTransitionTo(State.CreatingWait)
        .from(s -> s != State.CreatingWait)
        .viaAny()
        .execute(
            ctx -> {
              SessionFuture sessionFuture = new SessionFuture();
              KEY_SESSION_FUTURE.set(ctx, sessionFuture);

              Long waitTime = KEY_WAIT_TIME.get(ctx);
              if (waitTime == null) {
                waitTime = 1L;
              } else {
                waitTime = Math.min(MAX_WAIT_SECONDS, waitTime << 1);
              }
              KEY_WAIT_TIME.set(ctx, waitTime);

              ScheduledFuture<?> waitFuture =
                  client
                      .getTransport()
                      .getConfig()
                      .getScheduledExecutor()
                      .schedule(
                          () -> ctx.fireEvent(new Event.CreatingWaitExpired()),
                          waitTime,
                          TimeUnit.SECONDS);
              KEY_WAIT_FUTURE.set(ctx, waitFuture);
            });

    fb.onTransitionFrom(State.CreatingWait)
        .to(State.Inactive)
        .via(Event.CloseSession.class)
        .execute(
            ctx -> {
              ScheduledFuture<?> waitFuture = KEY_WAIT_FUTURE.remove(ctx);
              if (waitFuture != null) {
                waitFuture.cancel(false);
              }

              KEY_WAIT_TIME.remove(ctx);

              handleFailureToOpenSession(
                  client, ctx, new UaException(StatusCodes.Bad_SessionClosed));

              Event.CloseSession event = (Event.CloseSession) ctx.event();

              client
                  .getTransport()
                  .getConfig()
                  .getExecutor()
                  .execute(() -> event.future.complete(Unit.VALUE));
            });

    /* Internal Transition Actions */

    fb.onInternalTransition(State.CreatingWait)
        .via(Event.GetSession.class)
        .execute(SessionFsmFactory::handleGetSessionEvent);

    fb.onInternalTransition(State.CreatingWait)
        .via(Event.OpenSession.class)
        .execute(SessionFsmFactory::handleOpenSessionEvent);
  }

  private static void configureCreatingState(FsmBuilder<State, Event> fb, OpcUaClient client) {
    /* Transitions */

    fb.when(State.Creating).on(Event.CreateSessionSuccess.class).transitionTo(State.Activating);

    fb.when(State.Creating)
        .on(Event.CreateSessionFailure.class)
        .transitionTo(State.CreatingWait)
        .executeFirst(
            ctx -> {
              Event.CreateSessionFailure e = (Event.CreateSessionFailure) ctx.event();

              handleFailureToOpenSession(client, ctx, e.failure);
            });

    /* External Transition Actions */

    fb.onTransitionTo(State.Creating)
        .from(State.Inactive)
        .via(Event.OpenSession.class)
        .execute(
            ctx -> {
              SessionFuture sessionFuture = new SessionFuture();
              KEY_SESSION_FUTURE.set(ctx, sessionFuture);

              handleOpenSessionEvent(ctx);

              //noinspection Duplicates
              createSession(ctx, client)
                  .whenComplete(
                      (csr, ex) -> {
                        if (csr != null) {
                          try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                              MDCCloseable ignoredSessionId = putSessionId(csr.getSessionId())) {

                            LOGGER.debug("CreateSession succeeded: {}", csr.getSessionId());
                          }

                          ctx.fireEvent(new Event.CreateSessionSuccess(csr));
                        } else {
                          try (MDCCloseable ignored = putInstanceId(ctx)) {

                            LOGGER.debug("CreateSession failed: {}", ex.getMessage(), ex);
                          }

                          ctx.fireEvent(new Event.CreateSessionFailure(ex));
                        }
                      });
            });

    fb.onTransitionTo(State.Creating)
        .from(State.CreatingWait)
        .via(Event.CreatingWaitExpired.class)
        .execute(
            ctx -> {
              //noinspection Duplicates
              createSession(ctx, client)
                  .whenComplete(
                      (csr, ex) -> {
                        if (csr != null) {
                          try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                              MDCCloseable ignoredSessionId = putSessionId(csr.getSessionId())) {

                            LOGGER.debug("CreateSession succeeded: {}", csr.getSessionId());
                          }

                          ctx.fireEvent(new Event.CreateSessionSuccess(csr));
                        } else {
                          try (MDCCloseable ignored = putInstanceId(ctx)) {

                            LOGGER.debug("CreateSession failed: {}", ex.getMessage(), ex);
                          }

                          ctx.fireEvent(new Event.CreateSessionFailure(ex));
                        }
                      });
            });

    /* Internal Transition Actions */

    fb.onInternalTransition(State.Creating)
        .via(Event.GetSession.class)
        .execute(SessionFsmFactory::handleGetSessionEvent);

    fb.onInternalTransition(State.Creating)
        .via(Event.OpenSession.class)
        .execute(SessionFsmFactory::handleOpenSessionEvent);

    fb.onInternalTransition(State.Creating)
        .via(Event.CloseSession.class)
        .execute(ctx -> ctx.shelveEvent(ctx.event()));
  }

  private static void configureActivatingState(FsmBuilder<State, Event> fb, OpcUaClient client) {
    /* Transitions */

    fb.when(State.Activating)
        .on(Event.ActivateSessionSuccess.class)
        .transitionTo(State.Transferring);

    fb.when(State.Activating)
        .on(Event.ActivateSessionFailure.class)
        .transitionTo(State.CreatingWait)
        .executeFirst(
            ctx -> {
              Event.ActivateSessionFailure e = (Event.ActivateSessionFailure) ctx.event();

              handleFailureToOpenSession(client, ctx, e.failure);
            });

    /* External Transition Actions */

    fb.onTransitionTo(State.Activating)
        .from(State.Creating)
        .via(Event.CreateSessionSuccess.class)
        .execute(
            ctx -> {
              Event.CreateSessionSuccess event = (Event.CreateSessionSuccess) ctx.event();

              // The Session now exists on the Server; remember it so it can be closed if the
              // remainder of the establishment sequence fails.
              KEY_PENDING_SESSION.set(ctx, event.response);

              activateSession(ctx, client, event.response)
                  .whenComplete(
                      (session, ex) -> {
                        if (session != null) {
                          try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                              MDCCloseable ignoredSessionId = putSessionId(session)) {

                            LOGGER.debug("Session activated: {}", session);
                          }

                          ctx.fireEvent(new Event.ActivateSessionSuccess(session));
                        } else {
                          try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                              MDCCloseable ignoredSessionId =
                                  putSessionId(event.response.getSessionId())) {

                            LOGGER.debug("ActivateSession failed: {}", ex.getMessage(), ex);
                          }

                          ctx.fireEvent(new Event.ActivateSessionFailure(ex));
                        }
                      });
            });

    /* Internal Transition Actions */

    fb.onInternalTransition(State.Activating)
        .via(Event.GetSession.class)
        .execute(SessionFsmFactory::handleGetSessionEvent);

    fb.onInternalTransition(State.Activating)
        .via(Event.OpenSession.class)
        .execute(SessionFsmFactory::handleOpenSessionEvent);

    fb.onInternalTransition(State.Activating)
        .via(Event.CloseSession.class)
        .execute(ctx -> ctx.shelveEvent(ctx.event()));
  }

  private static void configureTransferringState(FsmBuilder<State, Event> fb, OpcUaClient client) {
    /* Transitions */

    fb.when(State.Transferring)
        .on(Event.TransferSubscriptionsSuccess.class)
        .transitionTo(State.Initializing);

    fb.when(State.Transferring)
        .on(Event.TransferSubscriptionsFailure.class)
        .transitionTo(State.CreatingWait)
        .executeFirst(
            ctx -> {
              Event.TransferSubscriptionsFailure e =
                  (Event.TransferSubscriptionsFailure) ctx.event();

              handleFailureToOpenSession(client, ctx, e.failure);
            });

    /* External Transition Actions */

    fb.onTransitionTo(State.Transferring)
        .from(State.Activating)
        .via(Event.ActivateSessionSuccess.class)
        .execute(
            ctx -> {
              Event.ActivateSessionSuccess event = (Event.ActivateSessionSuccess) ctx.event();

              transferSubscriptions(ctx, client, event.session)
                  .whenComplete(
                      (u, ex) -> {
                        if (u != null) {
                          try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                              MDCCloseable ignoredSessionId = putSessionId(event.session)) {

                            LOGGER.debug("TransferSubscriptions succeeded");
                          }

                          ctx.fireEvent(new Event.TransferSubscriptionsSuccess(event.session));
                        } else {
                          try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                              MDCCloseable ignoredSessionId = putSessionId(event.session)) {

                            LOGGER.debug("TransferSubscriptions failed: {}", ex.getMessage(), ex);
                          }

                          ctx.fireEvent(new Event.TransferSubscriptionsFailure(ex));
                        }
                      });
            });

    /* Internal Transition Actions */

    fb.onInternalTransition(State.Transferring)
        .via(Event.GetSession.class)
        .execute(SessionFsmFactory::handleGetSessionEvent);

    fb.onInternalTransition(State.Transferring)
        .via(Event.OpenSession.class)
        .execute(SessionFsmFactory::handleOpenSessionEvent);

    fb.onInternalTransition(State.Transferring)
        .via(Event.CloseSession.class)
        .execute(ctx -> ctx.shelveEvent(ctx.event()));
  }

  private static void configureInitializingState(FsmBuilder<State, Event> fb, OpcUaClient client) {
    /* Transitions */

    fb.when(State.Initializing).on(Event.InitializeSuccess.class).transitionTo(State.Active);

    fb.when(State.Initializing)
        .on(Event.InitializeFailure.class)
        .transitionTo(State.CreatingWait)
        .executeFirst(
            ctx -> {
              Event.InitializeFailure e = (Event.InitializeFailure) ctx.event();

              handleFailureToOpenSession(client, ctx, e.failure);
            });

    /* External Transition Actions */

    fb.onTransitionTo(State.Initializing)
        .from(State.Transferring)
        .via(Event.TransferSubscriptionsSuccess.class)
        .execute(
            ctx -> {
              Event.TransferSubscriptionsSuccess event =
                  (Event.TransferSubscriptionsSuccess) ctx.event();

              OpcUaSession session = event.session;

              initialize(ctx, client, session)
                  .whenComplete(
                      (u, ex) -> {
                        if (u != null) {
                          try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                              MDCCloseable ignoredSessionId = putSessionId(session)) {

                            LOGGER.debug("Initialization succeeded: {}", session);
                          }

                          ctx.fireEvent(new Event.InitializeSuccess(session));
                        } else {
                          try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                              MDCCloseable ignoredSessionId = putSessionId(session)) {

                            LOGGER.warn("Initialization failed: {}", session, ex);
                          }

                          ctx.fireEvent(new Event.InitializeFailure(ex));
                        }
                      });
            });

    fb.onTransitionTo(State.Initializing)
        .from(State.Reactivating)
        .via(Event.ReactivateSessionSuccess.class)
        .execute(
            ctx -> {
              Event.ReactivateSessionSuccess event = (Event.ReactivateSessionSuccess) ctx.event();

              OpcUaSession session = event.session;

              initialize(ctx, client, session)
                  .whenComplete(
                      (u, ex) -> {
                        if (u != null) {
                          try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                              MDCCloseable ignoredSessionId = putSessionId(session)) {

                            LOGGER.debug("Initialization succeeded: {}", session);
                          }

                          ctx.fireEvent(new Event.InitializeSuccess(session));
                        } else {
                          try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                              MDCCloseable ignoredSessionId = putSessionId(session)) {

                            LOGGER.warn("Initialization failed: {}", session, ex);
                          }

                          ctx.fireEvent(new Event.InitializeFailure(ex));
                        }
                      });
            });

    /* Internal Transition Actions */

    fb.onInternalTransition(State.Initializing)
        .via(Event.GetSession.class)
        .execute(SessionFsmFactory::handleGetSessionEvent);

    fb.onInternalTransition(State.Initializing)
        .via(Event.OpenSession.class)
        .execute(SessionFsmFactory::handleOpenSessionEvent);

    fb.onInternalTransition(State.Initializing)
        .via(Event.CloseSession.class)
        .execute(ctx -> ctx.shelveEvent(ctx.event()));
  }

  private static void configureActiveState(FsmBuilder<State, Event> fb, OpcUaClient client) {
    /* Transitions */

    fb.when(State.Active).on(Event.CloseSession.class).transitionTo(State.Closing);

    fb.when(State.Active)
        .on(
            e ->
                e.getClass() == Event.KeepAliveFailure.class
                    || e.getClass() == Event.ServiceFault.class
                    || e.getClass() == Event.ConnectionLost.class)
        .transitionTo(State.ReactivatingWait);

    /* External Transition Actions */

    fb.onTransitionTo(State.Active)
        .from(State.Initializing)
        .via(Event.InitializeSuccess.class)
        .execute(
            ctx -> {
              Event.InitializeSuccess event = (Event.InitializeSuccess) ctx.event();

              // reset the wait time
              KEY_WAIT_TIME.remove(ctx);

              // The Server is free to revise the requested session timeout downwards, and it
              // terminates the Session if the Client issues no request within the revised
              // interval (Part 4 §5.7.2.2). Keep-alives scheduled from the configured interval
              // alone would let the Session expire between them, so bound the interval at half
              // the revised timeout, leaving room for one keep-alive to be missed. The clamp only
              // ever lowers the interval; a configured value that is already safe is untouched.
              long configuredInterval = client.getConfig().getKeepAliveInterval().longValue();
              long revisedTimeout = event.session.getSessionTimeout().longValue();

              long keepAliveInterval =
                  Math.min(
                      configuredInterval, Math.max(MIN_KEEP_ALIVE_INTERVAL, revisedTimeout / 2));

              if (keepAliveInterval != configuredInterval) {
                try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                    MDCCloseable ignoredSessionId = putSessionId(event.session)) {

                  LOGGER.warn(
                      "Server revised the session timeout to {}ms; the configured keep-alive"
                          + " interval of {}ms leaves too little margin before the Session"
                          + " expires, using {}ms instead.",
                      revisedTimeout,
                      configuredInterval,
                      keepAliveInterval);
                }
              }

              // A new counter instance per epoch, so a keep-alive sent on a previous epoch can
              // recognize that the one it captured is no longer the current one.
              KEY_KEEP_ALIVE_FAILURE_COUNT.set(ctx, new AtomicLong(0L));

              ScheduledFuture<?> scheduledFuture =
                  client
                      .getTransport()
                      .getConfig()
                      .getScheduledExecutor()
                      .scheduleWithFixedDelay(
                          () -> ctx.fireEvent(new Event.KeepAlive(event.session)),
                          keepAliveInterval,
                          keepAliveInterval,
                          TimeUnit.MILLISECONDS);
              KEY_KEEP_ALIVE_SCHEDULED_FUTURE.set(ctx, scheduledFuture);

              KEY_SESSION.set(ctx, event.session);

              // The Session is established; it's reachable via KEY_SESSION from here on.
              KEY_PENDING_SESSION.remove(ctx);

              SessionFuture sessionFuture = KEY_SESSION_FUTURE.get(ctx);

              OpcClientTransport transport = client.getTransport();

              if (transport instanceof OpcTcpClientTransport) {
                ChannelFsm channelFsm = ((OpcTcpClientTransport) transport).getChannelFsm();

                ChannelFsm.TransitionListener listener =
                    new ChannelFsm.TransitionListener() {
                      @Override
                      public void onStateTransition(
                          com.digitalpetri.netty.fsm.State from,
                          com.digitalpetri.netty.fsm.State to,
                          com.digitalpetri.netty.fsm.Event via) {

                        if (from == com.digitalpetri.netty.fsm.State.Connected
                            && to != com.digitalpetri.netty.fsm.State.Connected) {

                          try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                              MDCCloseable ignoredSessionId = putSessionId(event.session)) {

                            LOGGER.debug(
                                "ChannelFsm transition from={} to={} via={}", from, to, via);
                          }

                          ctx.fireEvent(new Event.ConnectionLost());
                        }
                      }
                    };

                channelFsm.addTransitionListener(listener);
                KEY_CHANNEL_FSM_TRANSITION_LISTENER.set(ctx, listener);

                // ChannelFsm notifies its listeners synchronously while evaluating a transition,
                // so a connection loss that occurred while the Session was being created,
                // activated, or initialized was dispatched before the listener above existed and
                // will never be delivered to it: the reconnect cycle leaves and re-enters
                // Connected without another transition originating from Connected. Synthesize the
                // missed event rather than leaving the Session bound to a secure channel that no
                // longer exists.
                if (channelFsm.getState() != com.digitalpetri.netty.fsm.State.Connected) {
                  ctx.fireEvent(new Event.ConnectionLost());
                }
              }

              client
                  .getTransport()
                  .getConfig()
                  .getExecutor()
                  .execute(() -> sessionFuture.future.complete(event.session));
            });

    fb.onTransitionTo(State.Active)
        .from(State.Initializing)
        .via(Event.InitializeSuccess.class)
        .execute(FsmContext::processShelvedEvents);

    fb.onTransitionFrom(State.Active)
        .to(s -> s != State.Active)
        .viaAny()
        .execute(
            ctx -> {
              ScheduledFuture<?> scheduledFuture = KEY_KEEP_ALIVE_SCHEDULED_FUTURE.remove(ctx);

              if (scheduledFuture != null) {
                scheduledFuture.cancel(false);
              }

              ChannelFsm.TransitionListener listener =
                  KEY_CHANNEL_FSM_TRANSITION_LISTENER.remove(ctx);

              if (listener != null) {
                OpcClientTransport clientTransport = client.getTransport();
                if (clientTransport instanceof OpcTcpClientTransport tcpClientTransport) {
                  tcpClientTransport.getChannelFsm().removeTransitionListener(listener);
                }
              }
            });

    // onSessionActive() callbacks
    fb.onTransitionTo(State.Active)
        .from(s -> s != State.Active)
        .viaAny()
        .execute(
            ctx -> {
              OpcUaSession session = KEY_SESSION.get(ctx);

              SessionFsm.SessionActivityListeners sessionActivityListeners =
                  KEY_SESSION_ACTIVITY_LISTENERS.get(ctx);

              client
                  .getTransport()
                  .getConfig()
                  .getExecutor()
                  .execute(
                      () ->
                          sessionActivityListeners.sessionActivityListeners.forEach(
                              listener -> listener.onSessionActive(session)));
            });

    // onSessionInactive() callbacks
    fb.onTransitionFrom(State.Active)
        .to(s -> s != State.Active)
        .viaAny()
        .execute(
            ctx -> {
              OpcUaSession session = KEY_SESSION.get(ctx);

              SessionFsm.SessionActivityListeners sessionActivityListeners =
                  KEY_SESSION_ACTIVITY_LISTENERS.get(ctx);

              client
                  .getTransport()
                  .getConfig()
                  .getExecutor()
                  .execute(
                      () ->
                          sessionActivityListeners.sessionActivityListeners.forEach(
                              listener -> listener.onSessionInactive(session)));
            });

    /* Internal Transition Actions */

    fb.onInternalTransition(State.Active)
        .via(Event.KeepAlive.class)
        .execute(
            ctx -> {
              Event.KeepAlive event = (Event.KeepAlive) ctx.event();

              // Capture the counter belonging to the epoch this keep-alive is sent on. Leaving
              // Active cancels the scheduling of new keep-alives but does not cancel one that has
              // already been sent, and the Event.ServiceFault route leaves Active without taking
              // the channel down, so the request below can still be pending when the Session is
              // re-activated (Part 4 §5.6.3) and complete against a later epoch.
              AtomicLong failureCount = KEY_KEEP_ALIVE_FAILURE_COUNT.get(ctx);

              sendKeepAlive(client, event.session)
                  .whenComplete(
                      (response, ex) -> {
                        if (ctx.currentState() != State.Active
                            || KEY_SESSION.get(ctx) != event.session
                            || KEY_KEEP_ALIVE_FAILURE_COUNT.get(ctx) != failureCount) {

                          // The epoch this keep-alive was sent on is over; whatever it observed is
                          // no longer relevant to the Session the FSM has now.
                          return;
                        }

                        if (response != null) {
                          DataValue[] results = response.getResults();

                          if (results != null && results.length > 0) {
                            Object value = results[0].value().value();
                            if (value instanceof Integer) {
                              ServerState state = ServerState.from((Integer) value);

                              try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                                  MDCCloseable ignoredSessionId = putSessionId(event.session)) {

                                LOGGER.debug("ServerState: {}", state);
                              }
                            }
                          }

                          failureCount.set(0L);
                        } else {
                          long keepAliveFailureCount = failureCount.incrementAndGet();

                          long keepAliveFailuresAllowed =
                              client.getConfig().getKeepAliveFailuresAllowed().longValue();

                          if (keepAliveFailureCount > keepAliveFailuresAllowed) {
                            try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                                MDCCloseable ignoredSessionId = putSessionId(event.session)) {

                              LOGGER.warn(
                                  "Keep Alive failureCount={} exceeds failuresAllowed={}",
                                  keepAliveFailureCount,
                                  keepAliveFailuresAllowed);
                            }

                            ctx.fireEvent(new Event.KeepAliveFailure());

                            // Close the underlying channel to force a reconnect.
                            // This is useful if the server has gone offline in an "unclean"
                            // manner to avoid having to wait for the underlying TCP stack's keep
                            // alive to kick in.
                            OpcClientTransport transport = client.getTransport();
                            if (transport instanceof OpcTcpClientTransport) {
                              ChannelFsm channelFsm =
                                  ((OpcTcpClientTransport) transport).getChannelFsm();
                              Channel channel = channelFsm.getChannel().getNow(null);
                              if (channel != null) {
                                channel.close();
                              }
                            }
                          } else {
                            try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                                MDCCloseable ignoredSessionId = putSessionId(event.session)) {

                              LOGGER.debug("Keep Alive failureCount={}", keepAliveFailureCount, ex);
                            }
                          }
                        }
                      });
            });

    fb.onInternalTransition(State.Active)
        .via(Event.GetSession.class)
        .execute(SessionFsmFactory::handleGetSessionEvent);

    fb.onInternalTransition(State.Active)
        .via(Event.OpenSession.class)
        .execute(SessionFsmFactory::handleOpenSessionEvent);
  }

  private static void configureClosingState(FsmBuilder<State, Event> fb, OpcUaClient client) {
    /* Transitions */

    fb.when(State.Closing).on(Event.CloseSessionSuccess.class).transitionTo(State.Inactive);

    /* External Transition Actions */

    fb.onTransitionTo(State.Closing)
        .from(s -> s == State.Active || s == State.ReactivatingWait)
        .via(Event.CloseSession.class)
        .execute(
            ctx -> {
              SessionFsm.CloseFuture closeFuture = new SessionFsm.CloseFuture();
              KEY_CLOSE_FUTURE.set(ctx, closeFuture);

              Event.CloseSession closeSession = (Event.CloseSession) ctx.event();
              complete(closeSession.future).with(closeFuture.future);

              OpcUaSession session = KEY_SESSION.get(ctx);

              closeSession(ctx, client, session)
                  .whenComplete(
                      (u, ex) -> {
                        try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                            MDCCloseable ignoredSessionId = putSessionId(session)) {

                          if (u != null) {
                            LOGGER.debug("Session closed: {}", session);
                          } else {
                            LOGGER.debug("CloseSession failed: {}", ex.getMessage(), ex);
                          }
                        }

                        ctx.fireEvent(new Event.CloseSessionSuccess());
                      });
            });

    fb.onTransitionFrom(State.Closing)
        .to(State.Inactive)
        .via(Event.CloseSessionSuccess.class)
        .execute(
            ctx -> {
              SessionFsm.CloseFuture closeFuture = KEY_CLOSE_FUTURE.get(ctx);

              if (closeFuture != null) {
                client
                    .getTransport()
                    .getConfig()
                    .getExecutor()
                    .execute(() -> closeFuture.future.complete(Unit.VALUE));
              }
            });

    /* Internal Transition Actions */

    fb.onInternalTransition(State.Closing)
        .via(Event.CloseSession.class)
        .execute(
            ctx -> {
              Event.CloseSession event = (Event.CloseSession) ctx.event();

              SessionFsm.CloseFuture closeFuture = KEY_CLOSE_FUTURE.get(ctx);

              if (closeFuture != null) {
                complete(event.future).with(closeFuture.future);
              }
            });

    fb.onInternalTransition(State.Closing)
        .via(e -> e.getClass() != Event.CloseSession.class)
        .execute(ctx -> ctx.shelveEvent(ctx.event()));
  }

  private static void configureReactivatingWaitState(
      FsmBuilder<State, Event> fb, OpcUaClient client) {

    fb.when(State.ReactivatingWait)
        .on(Event.ReactivatingWaitExpired.class)
        .transitionTo(State.Reactivating);

    fb.when(State.ReactivatingWait).on(Event.CloseSession.class).transitionTo(State.Closing);

    fb.onTransitionTo(State.ReactivatingWait)
        .from(s -> s != State.ReactivatingWait)
        .viaAny()
        .execute(FsmContext::processShelvedEvents);

    fb.onTransitionTo(State.ReactivatingWait)
        .from(s -> s != State.ReactivatingWait)
        .viaAny()
        .execute(
            ctx -> {
              SessionFuture sessionFuture = new SessionFuture();
              KEY_SESSION_FUTURE.set(ctx, sessionFuture);

              Long waitTime = KEY_WAIT_TIME.get(ctx);
              if (waitTime == null) {
                waitTime = 1L;
              } else {
                waitTime = Math.min(MAX_WAIT_SECONDS, waitTime << 1);
              }
              KEY_WAIT_TIME.set(ctx, waitTime);

              ScheduledFuture<?> waitFuture =
                  client
                      .getTransport()
                      .getConfig()
                      .getScheduledExecutor()
                      .schedule(
                          () -> ctx.fireEvent(new Event.ReactivatingWaitExpired()),
                          waitTime,
                          TimeUnit.SECONDS);
              KEY_WAIT_FUTURE.set(ctx, waitFuture);
            });

    fb.onTransitionFrom(State.ReactivatingWait)
        .to(State.Closing)
        .via(Event.CloseSession.class)
        .execute(
            ctx -> {
              ScheduledFuture<?> waitFuture = KEY_WAIT_FUTURE.remove(ctx);
              if (waitFuture != null) {
                waitFuture.cancel(false);
              }

              KEY_WAIT_TIME.remove(ctx);

              handleFailureToOpenSession(
                  client, ctx, new UaException(StatusCodes.Bad_SessionClosed));
            });

    /* Internal Transition Actions */

    fb.onInternalTransition(State.ReactivatingWait)
        .via(Event.GetSession.class)
        .execute(SessionFsmFactory::handleGetSessionEvent);

    fb.onInternalTransition(State.ReactivatingWait)
        .via(Event.OpenSession.class)
        .execute(SessionFsmFactory::handleOpenSessionEvent);
  }

  private static void configureReactivatingState(FsmBuilder<State, Event> fb, OpcUaClient client) {
    Predicate<Event> isReactivateSessionFailure = e -> e instanceof Event.ReactivateSessionFailure;

    // Part 4 §6.7: a Client shall create a new Session if ActivateSession fails. Escalate on the
    // StatusCode rather than the exception type so that a Server reporting a definitive
    // session-level error with a channel-level Error message instead of an application-level
    // ServiceFault - the same defective behavior guarded against on the transfer path below -
    // escalates as well. Failures that carry no such StatusCode, e.g. Bad_Timeout or
    // Bad_ConnectionClosed, are connectivity problems; Part 4 §5.7.2.1 says to keep trying to
    // reactivate over a new connection, so those stay on the ReactivatingWait retry loop.
    Predicate<Event> isReactivateSessionFailureFatal =
        isReactivateSessionFailure.and(
            e -> {
              Event.ReactivateSessionFailure event = (Event.ReactivateSessionFailure) e;
              return UaException.extract(event.failure)
                  .map(
                      ex ->
                          ex instanceof UaServiceFaultException
                              || SESSION_ERROR.test(ex.getStatusCode()))
                  .orElse(false);
            });

    // Neither retrying the reactivation nor creating a new Session can succeed while the transport
    // is disconnected for good, and both routes retry indefinitely, so this has to be evaluated
    // before the escalation to CreatingWait below.
    Predicate<Event> isReactivateSessionFailureTerminal =
        isReactivateSessionFailure.and(e -> isTransportDisconnectedForGood(client));

    fb.when(State.Reactivating)
        .on(isReactivateSessionFailureTerminal)
        .transitionTo(State.Inactive)
        .executeFirst(
            ctx -> {
              KEY_WAIT_TIME.remove(ctx);

              Event.ReactivateSessionFailure e = (Event.ReactivateSessionFailure) ctx.event();

              OpcUaSession session = KEY_SESSION.remove(ctx);

              try (MDCCloseable ignoredInstanceId = putInstanceId(ctx)) {
                LOGGER.warn(
                    "Transport is disconnected and will not reconnect on its own; abandoning"
                        + " Session {}.",
                    session != null ? session.getSessionId() : null);
              }

              // The Session can't be closed on the Server without a channel to send the request
              // over; it is left to expire when the Session timeout elapses (Part 4 §5.6.2).
              handleFailureToOpenSession(client, ctx, e.failure);
            });

    fb.when(State.Reactivating)
        .on(isReactivateSessionFailureFatal)
        .transitionTo(State.CreatingWait)
        .executeFirst(
            ctx -> {
              KEY_WAIT_TIME.remove(ctx);

              Event.ReactivateSessionFailure e = (Event.ReactivateSessionFailure) ctx.event();

              handleFailureToOpenSession(client, ctx, e.failure);

              // Reactivation is being abandoned in favor of creating a new Session. The old one may
              // still exist on the Server (the fault might have been e.g.
              // Bad_IdentityTokenRejected rather than Bad_SessionIdInvalid), so close it
              // best-effort without deleting its Subscriptions: the replacement Session will try
              // to transfer them. A fault in response to this is expected and ignored.
              OpcUaSession session = KEY_SESSION.remove(ctx);

              if (session != null) {
                abandonSession(ctx, client, session);
              }
            });

    // If reactivating fails for any other reason, move back to ReactivatingWait and keep trying to
    // reactivate
    fb.when(State.Reactivating)
        .on(isReactivateSessionFailure)
        .transitionTo(State.ReactivatingWait)
        .executeFirst(
            ctx -> {
              Event.ReactivateSessionFailure e = (Event.ReactivateSessionFailure) ctx.event();

              handleFailureToOpenSession(client, ctx, e.failure);
            });

    fb.when(State.Reactivating)
        .on(Event.ReactivateSessionSuccess.class)
        .transitionTo(State.Initializing);

    fb.onTransitionTo(State.Reactivating)
        .from(State.ReactivatingWait)
        .via(Event.ReactivatingWaitExpired.class)
        .execute(
            ctx -> {
              OpcUaSession currentSession = KEY_SESSION.get(ctx);

              reactivateSession(ctx, client)
                  .whenComplete(
                      (session, ex) -> {
                        if (session != null) {
                          try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                              MDCCloseable ignoredSessionId = putSessionId(session)) {

                            LOGGER.debug("Session reactivated: {}", session);
                          }

                          ctx.fireEvent(new Event.ReactivateSessionSuccess(session));
                        } else {
                          try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                              MDCCloseable ignoredSessionId = putSessionId(currentSession)) {

                            LOGGER.debug("Reactivation failed: {}", ex.getMessage(), ex);
                          }

                          ctx.fireEvent(new Event.ReactivateSessionFailure(ex));
                        }
                      });
            });

    /* Internal Transition Actions */

    fb.onInternalTransition(State.Reactivating)
        .via(Event.GetSession.class)
        .execute(SessionFsmFactory::handleGetSessionEvent);

    fb.onInternalTransition(State.Reactivating)
        .via(Event.OpenSession.class)
        .execute(SessionFsmFactory::handleOpenSessionEvent);

    fb.onInternalTransition(State.Reactivating)
        .via(Event.CloseSession.class)
        .execute(ctx -> ctx.shelveEvent(ctx.event()));
  }

  /**
   * Check whether the transport is disconnected in a way that nothing will undo on its own.
   *
   * <p>The ChannelFsm is configured persistent, so a channel that drops unexpectedly is reconnected
   * without any help from the Session. {@code NotConnected} is the exception: it is reached only
   * when the application deliberately disconnects the transport, its only outbound edge is an
   * explicit Connect, and every request attempted from it fails immediately. A Session has nothing
   * left to reactivate over until the application connects again.
   *
   * @param client the {@link OpcUaClient} the Session belongs to.
   * @return {@code true} if the transport is disconnected and will not reconnect on its own.
   */
  private static boolean isTransportDisconnectedForGood(OpcUaClient client) {
    OpcClientTransport transport = client.getTransport();

    if (transport instanceof OpcTcpClientTransport tcpClientTransport) {
      return tcpClientTransport.getChannelFsm().getState()
          == com.digitalpetri.netty.fsm.State.NotConnected;
    } else {
      return false;
    }
  }

  private static void handleGetSessionEvent(ActionContext<State, Event> ctx) {
    CompletableFuture<OpcUaSession> sessionFuture = KEY_SESSION_FUTURE.get(ctx).future;

    Event.GetSession event = (Event.GetSession) ctx.event();
    complete(event.future).with(sessionFuture);
  }

  private static MDCCloseable putInstanceId(FsmContext<State, Event> ctx) {
    return MDC.putCloseable("instance-id", ctx.getUserContext().toString());
  }

  private static MDCCloseable putSessionId(OpcUaSession session) {
    return putSessionId(session.getSessionId());
  }

  private static MDCCloseable putSessionId(NodeId sessionId) {
    return MDC.putCloseable("session-id", sessionId.toParseableString());
  }

  private static void handleOpenSessionEvent(ActionContext<State, Event> ctx) {
    CompletableFuture<OpcUaSession> sessionFuture = KEY_SESSION_FUTURE.get(ctx).future;

    Event.OpenSession event = (Event.OpenSession) ctx.event();
    complete(event.future).with(sessionFuture);
  }

  private static void handleFailureToOpenSession(
      OpcUaClient client, ActionContext<State, Event> ctx, Throwable failure) {

    SessionFuture sessionFuture = KEY_SESSION_FUTURE.remove(ctx);

    if (sessionFuture != null) {
      client
          .getTransport()
          .getConfig()
          .getExecutor()
          .execute(() -> sessionFuture.future.completeExceptionally(failure));
    }

    // If CreateSession already succeeded the Session exists on the Server, and a new one is about
    // to be created in its place. Close it best-effort so the Server isn't left holding an orphan
    // until the Session timeout expires, but preserve any Subscriptions already transferred to it
    // so the replacement Session can recover them. Failure to close changes nothing about the
    // outcome here.
    CreateSessionResponse pendingSession = KEY_PENDING_SESSION.remove(ctx);

    if (pendingSession != null) {
      abandonSession(ctx, client, pendingSession);
    }
  }

  private static CompletableFuture<Unit> closeSession(
      FsmContext<State, Event> ctx, OpcUaClient client, OpcUaSession session) {

    return closeSession(
        ctx, client, session.getSessionId(), session.getAuthenticationToken(), true);
  }

  private static CompletableFuture<Unit> abandonSession(
      FsmContext<State, Event> ctx, OpcUaClient client, OpcUaSession session) {

    return closeSession(
        ctx, client, session.getSessionId(), session.getAuthenticationToken(), false);
  }

  private static CompletableFuture<Unit> abandonSession(
      FsmContext<State, Event> ctx, OpcUaClient client, CreateSessionResponse session) {

    return closeSession(
        ctx, client, session.getSessionId(), session.getAuthenticationToken(), false);
  }

  private static CompletableFuture<Unit> closeSession(
      FsmContext<State, Event> ctx,
      OpcUaClient client,
      NodeId sessionId,
      NodeId authToken,
      boolean deleteSubscriptions) {

    try {
      CompletableFuture<Unit> closeFuture = new CompletableFuture<>();

      RequestHeader requestHeader = client.newRequestHeader(authToken, uint(5000));

      CloseSessionRequest request = new CloseSessionRequest(requestHeader, deleteSubscriptions);

      try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
          MDCCloseable ignoredSessionId = putSessionId(sessionId)) {

        LOGGER.debug("Sending CloseSessionRequest...");
      }

      client
          .getTransport()
          .sendRequestMessage(request)
          .whenCompleteAsync(
              (csr, ex2) -> closeFuture.complete(Unit.VALUE),
              client.getTransport().getConfig().getExecutor());

      return closeFuture;
    } catch (Exception ex) {
      return failedFuture(ex);
    }
  }

  @SuppressWarnings("Duplicates")
  private static CompletableFuture<CreateSessionResponse> createSession(
      FsmContext<State, Event> ctx, OpcUaClient client) {

    try {
      EndpointDescription endpoint = client.getConfig().getEndpoint();

      String gatewayServerUri = endpoint.getServer().getGatewayServerUri();

      String serverUri;
      if (gatewayServerUri != null && !gatewayServerUri.isEmpty()) {
        serverUri = endpoint.getServer().getApplicationUri();
      } else {
        serverUri = null;
      }

      ByteString clientNonce = NonceUtil.generateNonce(32);

      ByteString clientCertificate =
          client
              .getConfig()
              .getCertificate()
              .map(
                  c -> {
                    try {
                      return ByteString.of(c.getEncoded());
                    } catch (CertificateEncodingException e) {
                      return ByteString.NULL_VALUE;
                    }
                  })
              .orElse(ByteString.NULL_VALUE);

      ApplicationDescription clientDescription =
          new ApplicationDescription(
              client.getConfig().getApplicationUri(),
              client.getConfig().getProductUri(),
              client.getConfig().getApplicationName(),
              ApplicationType.Client,
              null,
              null,
              null);

      CreateSessionRequest request =
          new CreateSessionRequest(
              client.newRequestHeader(),
              clientDescription,
              serverUri,
              client.getConfig().getEndpoint().getEndpointUrl(),
              client.getConfig().getSessionName().get(),
              clientNonce,
              clientCertificate,
              client.getConfig().getSessionTimeout().doubleValue(),
              client.getConfig().getMaxResponseMessageSize());

      try (MDCCloseable ignored = putInstanceId(ctx)) {

        LOGGER.debug("Sending CreateSessionRequest...");
      }

      return client
          .getTransport()
          .sendRequestMessage(request)
          .thenApply(CreateSessionResponse.class::cast)
          .thenCompose(
              response -> {
                try {
                  SecurityPolicy securityPolicy =
                      SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri());

                  if (securityPolicy != SecurityPolicy.None) {
                    if (response.getServerCertificate().isNullOrEmpty()) {
                      throw new UaException(
                          StatusCodes.Bad_SecurityChecksFailed,
                          "Certificate missing from CreateSessionResponse");
                    }

                    List<X509Certificate> serverCertificateChain =
                        CertificateUtil.decodeCertificates(
                            response.getServerCertificate().bytesOrEmpty());

                    X509Certificate serverCertificate = serverCertificateChain.get(0);

                    X509Certificate certificateFromEndpoint =
                        CertificateUtil.decodeCertificate(
                            endpoint.getServerCertificate().bytesOrEmpty());

                    if (!serverCertificate.equals(certificateFromEndpoint)) {
                      throw new UaException(
                          StatusCodes.Bad_SecurityChecksFailed,
                          "Certificate from CreateSessionResponse did not "
                              + "match certificate from EndpointDescription!");
                    }

                    client
                        .getConfig()
                        .getCertificateValidator()
                        .validateCertificateChain(
                            serverCertificateChain,
                            endpoint.getServer().getApplicationUri(),
                            new String[] {EndpointUtil.getHost(endpoint.getEndpointUrl())});

                    SignatureData serverSignature = response.getServerSignature();

                    byte[] dataBytes =
                        Bytes.concat(clientCertificate.bytesOrEmpty(), clientNonce.bytesOrEmpty());

                    byte[] signatureBytes = serverSignature.getSignature().bytesOrEmpty();

                    SignatureUtil.verify(
                        SecurityAlgorithm.fromUri(serverSignature.getAlgorithm()),
                        serverCertificate,
                        dataBytes,
                        signatureBytes);
                  }

                  if (client.getConfig().isSessionEndpointValidationEnabled()) {
                    validateSessionEndpoints(
                        endpoint.getTransportProfileUri(),
                        client.getConfig().getDiscoveryEndpoints(),
                        List.of(
                            Objects.requireNonNullElse(
                                response.getServerEndpoints(), new EndpointDescription[0])));
                  }

                  return completedFuture(response);
                } catch (UaException e) {
                  return failedFuture(e);
                }
              });
    } catch (Exception ex) {
      return failedFuture(ex);
    }
  }

  /**
   * Validate the Session endpoints against the endpoints from discovery.
   *
   * <p>Client shall compare only the following parameters:
   *
   * <ul>
   *   <li>server.applicationUri
   *   <li>endpointUrl
   *   <li>securityMode
   *   <li>securityPolicyUri
   *   <li>userIdentityTokens
   *   <li>transportProfileUri
   *   <li>securityLevel
   * </ul>
   *
   * @param transportProfileUri the transport profile URI to filter endpoints by. Only endpoints
   *     with matching transport profile URIs will be compared.
   * @param discoveryEndpoints the list of endpoints obtained during the discovery process that will
   *     be used as the reference for validation.
   * @param sessionEndpoints the list of endpoints from the session that need to be validated
   *     against the discovery endpoints.
   */
  static void validateSessionEndpoints(
      String transportProfileUri,
      List<EndpointDescription> discoveryEndpoints,
      List<EndpointDescription> sessionEndpoints)
      throws UaException {

    List<EndpointDescription> filteredDiscoveryEndpoints =
        discoveryEndpoints.stream()
            .filter(e -> Objects.equals(transportProfileUri, e.getTransportProfileUri()))
            .toList();

    List<EndpointDescription> filteredSessionEndpoints =
        sessionEndpoints.stream()
            .filter(e -> Objects.equals(transportProfileUri, e.getTransportProfileUri()))
            .collect(Collectors.toList());

    if (filteredDiscoveryEndpoints.isEmpty()) {
      return;
    }

    if (filteredDiscoveryEndpoints.size() != filteredSessionEndpoints.size()) {
      throw new UaException(
          StatusCodes.Bad_SecurityChecksFailed,
          "endpoints returned during discovery do not match session endpoints");
    }

    for (EndpointDescription discoveryEndpoint : filteredDiscoveryEndpoints) {
      boolean matched = false;
      for (EndpointDescription sessionEndpoint : filteredSessionEndpoints) {
        if (checkEndpointEquivalence(sessionEndpoint, discoveryEndpoint)) {
          filteredSessionEndpoints.remove(sessionEndpoint);
          matched = true;
          break;
        }
      }
      if (!matched) {
        throw new UaException(
            StatusCodes.Bad_SecurityChecksFailed,
            "endpoints returned during discovery do not match session endpoints");
      }
    }
  }

  private static boolean checkEndpointEquivalence(
      EndpointDescription endpoint1, EndpointDescription endpoint2) {

    return Objects.equals(
            endpoint1.getServer().getApplicationUri(), endpoint2.getServer().getApplicationUri())
        && Objects.equals(endpoint1.getEndpointUrl(), endpoint2.getEndpointUrl())
        && Objects.equals(endpoint1.getSecurityMode(), endpoint2.getSecurityMode())
        && Objects.equals(endpoint1.getSecurityPolicyUri(), endpoint2.getSecurityPolicyUri())
        && Arrays.equals(endpoint1.getUserIdentityTokens(), endpoint2.getUserIdentityTokens())
        && Objects.equals(endpoint1.getTransportProfileUri(), endpoint2.getTransportProfileUri())
        && Objects.equals(endpoint1.getSecurityLevel(), endpoint2.getSecurityLevel());
  }

  @SuppressWarnings("Duplicates")
  private static CompletableFuture<OpcUaSession> activateSession(
      FsmContext<State, Event> ctx, OpcUaClient client, CreateSessionResponse csr) {

    try {
      EndpointDescription endpoint = client.getConfig().getEndpoint();

      ByteString csrNonce = csr.getServerNonce();

      SignedIdentityToken signedIdentityToken =
          client.getConfig().getIdentityProvider().getIdentityToken(endpoint, csrNonce);

      UserIdentityToken userIdentityToken = signedIdentityToken.getToken();
      SignatureData userTokenSignature = signedIdentityToken.getSignature();

      ActivateSessionRequest request =
          new ActivateSessionRequest(
              client.newRequestHeader(csr.getAuthenticationToken()),
              buildClientSignature(client.getConfig(), csrNonce),
              new SignedSoftwareCertificate[0],
              client.getConfig().getSessionLocaleIds(),
              ExtensionObject.encode(client.getStaticEncodingContext(), userIdentityToken),
              userTokenSignature);

      try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
          MDCCloseable ignoredSessionId = putSessionId(csr.getSessionId())) {

        LOGGER.debug("Sending ActivateSessionRequest...");
      }

      return client
          .getTransport()
          .sendRequestMessage(request)
          .thenApply(ActivateSessionResponse.class::cast)
          .thenCompose(
              asr -> {
                ByteString asrNonce = asr.getServerNonce();

                // TODO check for repeated nonce?

                OpcUaSession session =
                    new OpcUaSession(
                        csr.getAuthenticationToken(),
                        csr.getSessionId(),
                        client.getConfig().getSessionName().get(),
                        csr.getRevisedSessionTimeout(),
                        csr.getMaxRequestMessageSize(),
                        csr.getServerCertificate(),
                        csr.getServerSoftwareCertificates());

                session.setLastActivateSessionServiceResult(
                    asr.getResponseHeader().getServiceResult());
                session.setServerNonce(asrNonce);

                return completedFuture(session);
              });
    } catch (Exception ex) {
      return failedFuture(ex);
    }
  }

  private static CompletableFuture<OpcUaSession> reactivateSession(
      FsmContext<State, Event> ctx, OpcUaClient client) {

    try {
      OpcUaSession session = KEY_SESSION.get(ctx);
      assert session != null;

      EndpointDescription endpoint = client.getConfig().getEndpoint();

      ByteString serverNonce = session.getServerNonce();

      SignedIdentityToken signedIdentityToken =
          client.getConfig().getIdentityProvider().getIdentityToken(endpoint, serverNonce);

      UserIdentityToken userIdentityToken = signedIdentityToken.getToken();
      SignatureData userTokenSignature = signedIdentityToken.getSignature();

      var request =
          new ActivateSessionRequest(
              client.newRequestHeader(session.getAuthenticationToken()),
              buildClientSignature(client.getConfig(), serverNonce),
              new SignedSoftwareCertificate[0],
              client.getConfig().getSessionLocaleIds(),
              ExtensionObject.encode(client.getStaticEncodingContext(), userIdentityToken),
              userTokenSignature);

      try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
          MDCCloseable ignoredSessionId = putSessionId(session)) {

        LOGGER.debug("Sending ActivateSessionRequest...");
      }

      return client
          .getTransport()
          .sendRequestMessage(request)
          .thenApply(ActivateSessionResponse.class::cast)
          .thenCompose(
              asr -> {
                session.setLastActivateSessionServiceResult(
                    asr.getResponseHeader().getServiceResult());
                session.setServerNonce(asr.getServerNonce());

                return completedFuture(session);
              });
    } catch (Exception ex) {
      return failedFuture(ex);
    }
  }

  @SuppressWarnings("Duplicates")
  private static CompletableFuture<Unit> transferSubscriptions(
      FsmContext<State, Event> ctx, OpcUaClient client, OpcUaSession session) {

    List<OpcUaSubscription> subscriptions = client.getSubscriptions();

    if (subscriptions.isEmpty()) {
      return completedFuture(Unit.VALUE);
    }

    // Pair each Subscription with its SubscriptionId once, up front. A concurrent reset() removes a
    // Subscription from the client before clearing its id, so the id is an unsynchronized read that
    // can disappear between the snapshot above and the request below; deriving the ids separately
    // would leave the results indexed against a list the request was not built from, shifting every
    // position at or after a dropped Subscription.
    record Transferable(OpcUaSubscription subscription, UInteger subscriptionId) {}

    List<Transferable> transferable =
        subscriptions.stream()
            .flatMap(s -> s.getSubscriptionId().map(id -> new Transferable(s, id)).stream())
            .toList();

    if (transferable.isEmpty()) {
      return completedFuture(Unit.VALUE);
    }

    CompletableFuture<Unit> transferFuture = new CompletableFuture<>();

    UInteger[] subscriptionIdsArray =
        transferable.stream().map(Transferable::subscriptionId).toArray(UInteger[]::new);

    TransferSubscriptionsRequest request =
        new TransferSubscriptionsRequest(
            client.newRequestHeader(session.getAuthenticationToken()), subscriptionIdsArray, true);

    try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
        MDCCloseable ignoredSessionId = putSessionId(session)) {

      LOGGER.debug("Sending TransferSubscriptionsRequest...");
    }

    client
        .getTransport()
        .sendRequestMessage(request)
        .thenApply(TransferSubscriptionsResponse.class::cast)
        .whenComplete(
            (tsr, ex) -> {
              if (tsr != null) {
                TransferResult[] results = requireNonNull(tsr.getResults());

                try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                    MDCCloseable ignoredSessionId = putSessionId(session)) {

                  LOGGER.debug(
                      "TransferSubscriptions supported: {}",
                      tsr.getResponseHeader().getServiceResult());

                  if (LOGGER.isDebugEnabled()) {
                    try {
                      Stream<UInteger> subscriptionIds = Stream.of(subscriptionIdsArray);
                      Stream<StatusCode> statusCodes =
                          Stream.of(results).map(TransferResult::getStatusCode);

                      //noinspection UnstableApiUsage
                      String[] ss =
                          Streams.zip(
                                  subscriptionIds,
                                  statusCodes,
                                  (i, s) -> {
                                    assert s != null;
                                    return String.format(
                                        "id=%s/%s",
                                        i,
                                        StatusCodes.lookup(s.value())
                                            .map(sa -> sa[0])
                                            .orElse(s.toString()));
                                  })
                              .toArray(String[]::new);

                      LOGGER.debug("TransferSubscriptions results: {}", Arrays.toString(ss));
                    } catch (Throwable t) {
                      LOGGER.error("error logging TransferSubscription results", t);
                    }
                  }
                }

                // Part 4 §5.14.7.1: a successful TransferResult carries "the sequence numbers
                // of the NotificationMessages that are available for retransmission", which is
                // what tells the client which NotificationMessages the Republish loop Part 4
                // §6.7 requires before Publish resumes can still collect. Recorded here, inline,
                // rather than dispatched: the loop runs when this Session becomes Active, and
                // the list has to be in place before it does. Indexed against the
                // SubscriptionIds the request was built from, which is what the results are a
                // "list of results for the subscriptions to transfer" of.
                for (int i = 0; i < results.length && i < subscriptionIdsArray.length; i++) {
                  TransferResult result = results[i];

                  if (result.getStatusCode().isGood()) {
                    client
                        .getPublishingManager()
                        .notifySubscriptionTransferred(
                            session, subscriptionIdsArray[i], result.getAvailableSequenceNumbers());
                  }
                }

                // Bounded by the requested Subscriptions as well: Part 4 §5.14.7.2 defines results
                // as one per requested SubscriptionId, but a Server returning a longer list must
                // not reach past the end of them.
                for (int i = 0; i < results.length && i < transferable.size(); i++) {
                  TransferResult result = results[i];

                  if (!result.getStatusCode().isGood()) {
                    handleTransferFailure(
                        ctx, session, transferable.get(i).subscription(), result.getStatusCode());
                  }
                }

                // Failed Subscriptions must be reset and unregistered before this completion can
                // move the FSM through Initializing and into Active. Otherwise reconnect recovery
                // can still see them and issue Republish requests for SubscriptionIds that were
                // not transferred to this Session.
                transferFuture.complete(Unit.VALUE);
              } else {
                StatusCode statusCode =
                    UaException.extract(ex).map(UaException::getStatusCode).orElse(StatusCode.BAD);

                try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                    MDCCloseable ignoredSessionId = putSessionId(session)) {

                  LOGGER.debug("TransferSubscriptions not supported: {}", statusCode);
                }

                for (OpcUaSubscription subscription : subscriptions) {
                  handleTransferFailure(ctx, session, subscription, statusCode);
                }

                // Bad_ServiceUnsupported is the correct response when transfers aren't
                // supported but server implementations interpret the spec differently.
                if (statusCode.value() == StatusCodes.Bad_NotImplemented
                    || statusCode.value() == StatusCodes.Bad_NotSupported
                    || statusCode.value() == StatusCodes.Bad_OutOfService
                    || statusCode.value() == StatusCodes.Bad_ServiceUnsupported) {

                  // One of the expected responses; continue moving through the FSM.

                  transferFuture.complete(Unit.VALUE);
                } else {
                  // An unexpected response; complete exceptionally and start over.
                  // Subsequent runs through the FSM will not attempt transfer because
                  // transferFailed() has been called for all the existing subscriptions.
                  // This will prevent us from getting stuck in a "loop" attempting to
                  // reconnect to a defective server that responds with a channel-level
                  // Error message to subscription transfer requests instead of an
                  // application-level ServiceFault.

                  transferFuture.completeExceptionally(ex);
                }
              }
            })
        // The whenComplete above has its own result future discarded, so a response its handling
        // can't make sense of - e.g. a Good response carrying no results at all - would otherwise
        // leave transferFuture uncompleted and the FSM parked in Transferring with no event
        // pending and no timeout. A no-op when the callback completed transferFuture itself.
        .exceptionally(
            ex -> {
              transferFuture.completeExceptionally(ex);
              return null;
            });

    return transferFuture;
  }

  /**
   * Reset and unregister a Subscription that was not transferred to {@code session}.
   *
   * <p>{@link OpcUaSubscription#handleTransferFailure(StatusCode)} performs non-overridable local
   * teardown synchronously and dispatches the overridable notification separately. Contain internal
   * failures here so one Subscription cannot leave the Session FSM in {@link State#Transferring} or
   * prevent the remaining failed Subscriptions from being reset.
   */
  private static void handleTransferFailure(
      FsmContext<State, Event> ctx,
      OpcUaSession session,
      OpcUaSubscription subscription,
      StatusCode statusCode) {

    try {
      subscription.handleTransferFailure(statusCode);
    } catch (Exception e) {
      try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
          MDCCloseable ignoredSessionId = putSessionId(session)) {

        LOGGER.warn(
            "Subscription transfer-failure cleanup failed: id={}",
            subscription.getSubscriptionId().orElse(null),
            e);
      }
    }
  }

  private static CompletableFuture<Unit> initialize(
      FsmContext<State, Event> ctx, OpcUaClient client, OpcUaSession session) {

    LinkedList<SessionFsm.SessionInitializer> initializers =
        new LinkedList<>(KEY_SESSION_INITIALIZERS.get(ctx).sessionInitializers);

    if (initializers.isEmpty()) {
      return completedFuture(Unit.VALUE);
    } else {
      return runSequentially(ctx, client, session, initializers);
    }
  }

  private static CompletableFuture<Unit> runSequentially(
      FsmContext<State, Event> ctx,
      OpcUaClient client,
      OpcUaSession session,
      LinkedList<SessionFsm.SessionInitializer> initializers) {

    if (initializers.isEmpty()) {
      return CompletableFuture.completedFuture(Unit.VALUE);
    } else {
      SessionFsm.SessionInitializer initializer = initializers.removeFirst();

      return initializer
          .initialize(client, session)
          .exceptionally(
              ex -> {
                try (MDCCloseable ignoredInstanceId = putInstanceId(ctx);
                    MDCCloseable ignoredSessionId = putSessionId(session)) {

                  LOGGER.error(
                      "Uncaught initialization error: {}",
                      initializer.getClass().getSimpleName(),
                      ex);
                }

                return Unit.VALUE;
              })
          .thenCompose(u -> runSequentially(ctx, client, session, initializers));
    }
  }

  private static CompletableFuture<ReadResponse> sendKeepAlive(
      OpcUaClient client, OpcUaSession session) {
    ReadRequest keepAliveRequest = createKeepAliveRequest(client, session);

    return client
        .getTransport()
        .sendRequestMessage(keepAliveRequest)
        .thenApply(ReadResponse.class::cast);
  }

  private static ReadRequest createKeepAliveRequest(OpcUaClient client, OpcUaSession session) {
    RequestHeader requestHeader =
        client.newRequestHeader(
            session.getAuthenticationToken(), client.getConfig().getKeepAliveTimeout());

    return new ReadRequest(
        requestHeader,
        0.0,
        TimestampsToReturn.Neither,
        new ReadValueId[] {
          new ReadValueId(
              NodeIds.Server_ServerStatus_State,
              AttributeId.Value.uid(),
              null,
              QualifiedName.NULL_VALUE)
        });
  }

  @SuppressWarnings("Duplicates")
  private static SignatureData buildClientSignature(
      OpcUaClientConfig config, ByteString serverNonce) throws Exception {

    EndpointDescription endpoint = config.getEndpoint();

    SecurityPolicy securityPolicy = SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri());

    if (securityPolicy == SecurityPolicy.None) {
      return new SignatureData(null, null);
    } else {
      SecurityAlgorithm signatureAlgorithm = securityPolicy.getAsymmetricSignatureAlgorithm();
      PrivateKey privateKey = config.getKeyPair().map(KeyPair::getPrivate).orElse(null);
      List<X509Certificate> serverCertificates =
          CertificateUtil.decodeCertificates(endpoint.getServerCertificate().bytesOrEmpty());

      // Signature data is serverCert + serverNonce signed with our private key.
      byte[] serverNonceBytes = serverNonce.bytesOrEmpty();
      byte[] serverCertificateBytes = serverCertificates.get(0).getEncoded();
      byte[] dataToSign = Bytes.concat(serverCertificateBytes, serverNonceBytes);

      byte[] signature =
          SignatureUtil.sign(signatureAlgorithm, privateKey, ByteBuffer.wrap(dataToSign));

      return new SignatureData(signatureAlgorithm.getUri(), ByteString.of(signature));
    }
  }

  private static class SessionFaultListener implements ServiceFaultListener {

    private static final Predicate<StatusCode> SECURE_CHANNEL_ERROR =
        statusCode -> {
          long status = statusCode.value();

          return status == StatusCodes.Bad_SecureChannelIdInvalid
              || status == StatusCodes.Bad_SecurityChecksFailed
              || status == StatusCodes.Bad_TcpSecureChannelUnknown
              || status == StatusCodes.Bad_RequestTypeInvalid;
        };

    private final Logger logger = LoggerFactory.getLogger(SessionFsm.LOGGER_NAME);

    private final Fsm<State, Event> fsm;

    private SessionFaultListener(Fsm<State, Event> fsm) {
      this.fsm = fsm;
    }

    @Override
    public void onServiceFault(ServiceFault serviceFault) {
      StatusCode serviceResult = serviceFault.getResponseHeader().getServiceResult();

      if (SESSION_ERROR.or(SECURE_CHANNEL_ERROR).test(serviceResult)) {
        logger.debug("ServiceFault: {}", serviceResult);

        fsm.fireEvent(new Event.ServiceFault(serviceResult));
      }
    }
  }
}
