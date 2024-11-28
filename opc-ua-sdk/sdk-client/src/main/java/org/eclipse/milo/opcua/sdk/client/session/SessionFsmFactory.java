/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.digitalpetri.netty.fsm.ChannelFsm;
import com.digitalpetri.strictmachine.Fsm;
import com.digitalpetri.strictmachine.FsmContext;
import com.digitalpetri.strictmachine.dsl.ActionContext;
import com.digitalpetri.strictmachine.dsl.FsmBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.common.primitives.Bytes;
import io.netty.channel.Channel;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.api.ServiceFaultListener;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.identity.SignedIdentityToken;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.sdk.client.session.SessionFsm.SessionFuture;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscriptionManager;
import org.eclipse.milo.opcua.stack.client.UaStackClient;
import org.eclipse.milo.opcua.stack.client.transport.UaTransport;
import org.eclipse.milo.opcua.stack.client.transport.tcp.OpcTcpTransport;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_CLOSE_FUTURE;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_KEEP_ALIVE_FAILURE_COUNT;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_KEEP_ALIVE_SCHEDULED_FUTURE;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_SESSION;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_SESSION_ACTIVITY_LISTENERS;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_SESSION_FUTURE;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_SESSION_INITIALIZERS;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_WAIT_FUTURE;
import static org.eclipse.milo.opcua.sdk.client.session.SessionFsm.KEY_WAIT_TIME;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.l;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.complete;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.failedFuture;

public class SessionFsmFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionFsm.LOGGER_NAME);

    private static final int MAX_WAIT_SECONDS = 16;

    private SessionFsmFactory() {
    }

    public static SessionFsm newSessionFsm(OpcUaClient client) {
        FsmBuilder<State, Event> builder = new FsmBuilder<>(
            client.getConfig().getExecutor(),
            SessionFsm.LOGGER_NAME
        );

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
    }

    private static void configureInactiveState(
        FsmBuilder<State, Event> fb,
        @SuppressWarnings("unused") OpcUaClient client) {

        /* Transitions */

        fb.when(State.Inactive)
            .on(Event.OpenSession.class)
            .transitionTo(State.Creating);


        /* External Transition Actions */

        fb.onTransitionTo(State.Inactive)
            .from(s -> s != State.Inactive)
            .viaAny()
            .execute(FsmContext::processShelvedEvents);


        /* Internal Transition Actions */

        fb.onInternalTransition(State.Inactive)
            .via(Event.GetSession.class)
            .execute(ctx -> {
                Event.GetSession event = (Event.GetSession) ctx.event();

                client.getConfig().getExecutor().execute(() ->
                    event.future.completeExceptionally(
                        new UaException(StatusCodes.Bad_SessionClosed))
                );
            });

        fb.onInternalTransition(State.Inactive)
            .via(Event.CloseSession.class)
            .execute(ctx -> {
                Event.CloseSession event = (Event.CloseSession) ctx.event();

                client.getConfig().getExecutor().execute(() ->
                    event.future.complete(Unit.VALUE)
                );
            });
    }

    private static void configureCreatingWaitState(
        FsmBuilder<State, Event> fb,
        @SuppressWarnings("unused") OpcUaClient client) {

        /* Transitions */

        fb.when(State.CreatingWait)
            .on(Event.CreatingWaitExpired.class)
            .transitionTo(State.Creating);

        fb.when(State.CreatingWait)
            .on(Event.CloseSession.class)
            .transitionTo(State.Inactive);


        /* External Transition Actions */

        fb.onTransitionTo(State.CreatingWait)
            .from(s -> s != State.CreatingWait)
            .viaAny()
            .execute(FsmContext::processShelvedEvents);

        fb.onTransitionTo(State.CreatingWait)
            .from(s -> s != State.CreatingWait)
            .viaAny()
            .execute(ctx -> {
                SessionFuture sessionFuture = new SessionFuture();
                KEY_SESSION_FUTURE.set(ctx, sessionFuture);

                Long waitTime = KEY_WAIT_TIME.get(ctx);
                if (waitTime == null) {
                    waitTime = 1L;
                } else {
                    waitTime = Math.min(MAX_WAIT_SECONDS, waitTime << 1);
                }
                KEY_WAIT_TIME.set(ctx, waitTime);

                ScheduledFuture<?> waitFuture = client.getConfig().getScheduledExecutor().schedule(
                    () -> ctx.fireEvent(new Event.CreatingWaitExpired()),
                    waitTime,
                    TimeUnit.SECONDS
                );
                KEY_WAIT_FUTURE.set(ctx, waitFuture);
            });

        fb.onTransitionFrom(State.CreatingWait)
            .to(State.Inactive)
            .via(Event.CloseSession.class)
            .execute(ctx -> {
                ScheduledFuture<?> waitFuture = KEY_WAIT_FUTURE.remove(ctx);
                if (waitFuture != null) waitFuture.cancel(false);

                KEY_WAIT_TIME.remove(ctx);

                Event.CloseSession event = (Event.CloseSession) ctx.event();

                client.getConfig().getExecutor().execute(() ->
                    event.future.complete(Unit.VALUE)
                );
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

        fb.when(State.Creating)
            .on(Event.CreateSessionSuccess.class)
            .transitionTo(State.Activating);

        fb.when(State.Creating)
            .on(Event.CreateSessionFailure.class)
            .transitionTo(State.CreatingWait)
            .executeFirst(ctx -> {
                Event.CreateSessionFailure e = (Event.CreateSessionFailure) ctx.event();

                handleFailureToOpenSession(client, ctx, e.failure);
            });


        /* External Transition Actions */

        fb.onTransitionTo(State.Creating)
            .from(State.Inactive)
            .via(Event.OpenSession.class)
            .execute(ctx -> {
                SessionFuture sessionFuture = new SessionFuture();
                KEY_SESSION_FUTURE.set(ctx, sessionFuture);

                handleOpenSessionEvent(ctx);

                //noinspection Duplicates
                createSession(ctx, client).whenComplete((csr, ex) -> {
                    if (csr != null) {
                        LOGGER.debug("[{}] CreateSession succeeded: {}", ctx.getInstanceId(), csr.getSessionId());

                        ctx.fireEvent(new Event.CreateSessionSuccess(csr));
                    } else {
                        LOGGER.debug("[{}] CreateSession failed: {}", ctx.getInstanceId(), ex.getMessage(), ex);

                        ctx.fireEvent(new Event.CreateSessionFailure(ex));
                    }
                });
            });

        fb.onTransitionTo(State.Creating)
            .from(State.CreatingWait)
            .via(Event.CreatingWaitExpired.class)
            .execute(ctx -> {
                //noinspection Duplicates
                createSession(ctx, client).whenComplete((csr, ex) -> {
                    if (csr != null) {
                        LOGGER.debug("[{}] CreateSession succeeded: {}", ctx.getInstanceId(), csr.getSessionId());

                        ctx.fireEvent(new Event.CreateSessionSuccess(csr));
                    } else {
                        LOGGER.debug("[{}] CreateSession failed: {}", ctx.getInstanceId(), ex.getMessage(), ex);

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
            .executeFirst(ctx -> {
                Event.ActivateSessionFailure e = (Event.ActivateSessionFailure) ctx.event();

                handleFailureToOpenSession(client, ctx, e.failure);
            });


        /* External Transition Actions */

        fb.onTransitionTo(State.Activating)
            .from(State.Creating)
            .via(Event.CreateSessionSuccess.class)
            .execute(ctx -> {
                Event.CreateSessionSuccess event = (Event.CreateSessionSuccess) ctx.event();

                activateSession(ctx, client, event.response).whenComplete((session, ex) -> {
                    if (session != null) {
                        LOGGER.debug("[{}] Session activated: {}", ctx.getInstanceId(), session);

                        ctx.fireEvent(new Event.ActivateSessionSuccess(session));
                    } else {
                        LOGGER.debug("[{}] ActivateSession failed: {}", ctx.getInstanceId(), ex.getMessage(), ex);

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
            .executeFirst(ctx -> {
                Event.TransferSubscriptionsFailure e = (Event.TransferSubscriptionsFailure) ctx.event();

                handleFailureToOpenSession(client, ctx, e.failure);
            });


        /* External Transition Actions */

        fb.onTransitionTo(State.Transferring)
            .from(State.Activating)
            .via(Event.ActivateSessionSuccess.class)
            .execute(ctx -> {
                Event.ActivateSessionSuccess event = (Event.ActivateSessionSuccess) ctx.event();

                transferSubscriptions(ctx, client, event.session).whenComplete((u, ex) -> {
                    if (u != null) {
                        LOGGER.debug("[{}] TransferSubscriptions succeeded", ctx.getInstanceId());

                        ctx.fireEvent(new Event.TransferSubscriptionsSuccess(event.session));
                    } else {
                        LOGGER.debug(
                            "[{}] TransferSubscriptions failed: {}",
                            ctx.getInstanceId(), ex.getMessage(), ex);

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

        fb.when(State.Initializing)
            .on(Event.InitializeSuccess.class)
            .transitionTo(State.Active);

        fb.when(State.Initializing)
            .on(Event.InitializeFailure.class)
            .transitionTo(State.CreatingWait)
            .executeFirst(ctx -> {
                Event.InitializeFailure e = (Event.InitializeFailure) ctx.event();

                handleFailureToOpenSession(client, ctx, e.failure);
            });


        /* External Transition Actions */

        fb.onTransitionTo(State.Initializing)
            .from(State.Transferring)
            .via(Event.TransferSubscriptionsSuccess.class)
            .execute(ctx -> {
                Event.TransferSubscriptionsSuccess event = (Event.TransferSubscriptionsSuccess) ctx.event();

                OpcUaSession session = event.session;

                initialize(ctx, client, session).whenComplete((u, ex) -> {
                    if (u != null) {
                        LOGGER.debug("[{}] Initialization succeeded: {}", ctx.getInstanceId(), session);

                        ctx.fireEvent(new Event.InitializeSuccess(session));
                    } else {
                        LOGGER.warn("[{}] Initialization failed: {}", ctx.getInstanceId(), session, ex);

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

        fb.when(State.Active)
            .on(Event.CloseSession.class)
            .transitionTo(State.Closing);

        fb.when(State.Active)
            .on(e ->
                e.getClass() == Event.KeepAliveFailure.class ||
                    e.getClass() == Event.ServiceFault.class ||
                    e.getClass() == Event.ConnectionLost.class
            )
            .transitionTo(State.CreatingWait);


        /* External Transition Actions */

        fb.onTransitionTo(State.Active)
            .from(State.Initializing)
            .via(Event.InitializeSuccess.class)
            .execute(ctx -> {
                Event.InitializeSuccess event = (Event.InitializeSuccess) ctx.event();

                // reset the wait time
                KEY_WAIT_TIME.remove(ctx);

                long keepAliveInterval = client.getConfig().getKeepAliveInterval().longValue();
                KEY_KEEP_ALIVE_FAILURE_COUNT.set(ctx, 0L);

                ScheduledFuture<?> scheduledFuture = client.getConfig().getScheduledExecutor().scheduleWithFixedDelay(
                    () -> ctx.fireEvent(new Event.KeepAlive(event.session)),
                    keepAliveInterval,
                    keepAliveInterval,
                    TimeUnit.MILLISECONDS
                );
                KEY_KEEP_ALIVE_SCHEDULED_FUTURE.set(ctx, scheduledFuture);

                KEY_SESSION.set(ctx, event.session);

                SessionFuture sessionFuture = KEY_SESSION_FUTURE.get(ctx);

                UaTransport transport = client.getStackClient().getTransport();

                if (transport instanceof OpcTcpTransport) {
                    ChannelFsm channelFsm = ((OpcTcpTransport) transport).channelFsm();

                    channelFsm.addTransitionListener(new ChannelFsm.TransitionListener() {
                        @Override
                        public void onStateTransition(
                            com.digitalpetri.netty.fsm.State from,
                            com.digitalpetri.netty.fsm.State to,
                            com.digitalpetri.netty.fsm.Event via
                        ) {

                            if (from == com.digitalpetri.netty.fsm.State.Connected &&
                                to != com.digitalpetri.netty.fsm.State.Connected
                            ) {

                                channelFsm.removeTransitionListener(this);

                                LOGGER.debug("ChannelFsm transition from={} to={} via={}", from, to, via);

                                ctx.fireEvent(new Event.ConnectionLost());
                            }
                        }
                    });
                }

                client.getConfig().getExecutor().execute(() ->
                    sessionFuture.future.complete(event.session)
                );
            });

        fb.onTransitionTo(State.Active)
            .from(State.Initializing)
            .via(Event.InitializeSuccess.class)
            .execute(FsmContext::processShelvedEvents);

        fb.onTransitionFrom(State.Active)
            .to(s -> s == State.Closing || s == State.CreatingWait)
            .viaAny()
            .execute(ctx -> {
                ScheduledFuture<?> scheduledFuture =
                    KEY_KEEP_ALIVE_SCHEDULED_FUTURE.remove(ctx);

                if (scheduledFuture != null) {
                    scheduledFuture.cancel(false);
                }
            });

        // onSessionActive() callbacks
        fb.onTransitionTo(State.Active)
            .from(s -> s != State.Active)
            .viaAny()
            .execute(ctx -> {
                OpcUaSession session = KEY_SESSION.get(ctx);

                SessionFsm.SessionActivityListeners sessionActivityListeners =
                    KEY_SESSION_ACTIVITY_LISTENERS.get(ctx);

                client.getConfig().getExecutor().execute(
                    () ->
                        sessionActivityListeners.sessionActivityListeners
                            .forEach(listener -> listener.onSessionActive(session))
                );
            });

        // onSessionInactive() callbacks
        fb.onTransitionFrom(State.Active)
            .to(s -> s != State.Active)
            .viaAny()
            .execute(ctx -> {
                OpcUaSession session = KEY_SESSION.get(ctx);

                SessionFsm.SessionActivityListeners sessionActivityListeners =
                    KEY_SESSION_ACTIVITY_LISTENERS.get(ctx);

                client.getConfig().getExecutor().execute(
                    () ->
                        sessionActivityListeners.sessionActivityListeners
                            .forEach(listener -> listener.onSessionInactive(session))
                );
            });


        /* Internal Transition Actions */

        fb.onInternalTransition(State.Active)
            .via(Event.KeepAlive.class)
            .execute(ctx -> {
                Event.KeepAlive event = (Event.KeepAlive) ctx.event();

                sendKeepAlive(client, event.session).whenComplete((response, ex) -> {
                    if (response != null) {
                        DataValue[] results = response.getResults();

                        if (results != null && results.length > 0) {
                            Object value = results[0].getValue().getValue();
                            if (value instanceof Integer) {
                                ServerState state = ServerState.from((Integer) value);
                                LOGGER.debug("[{}] ServerState: {}", ctx.getInstanceId(), state);
                            }
                        }

                        KEY_KEEP_ALIVE_FAILURE_COUNT.set(ctx, 0L);
                    } else {
                        Long keepAliveFailureCount = KEY_KEEP_ALIVE_FAILURE_COUNT.get(ctx);

                        if (keepAliveFailureCount == null) {
                            keepAliveFailureCount = 1L;
                        } else {
                            keepAliveFailureCount += 1L;
                        }

                        KEY_KEEP_ALIVE_FAILURE_COUNT.set(ctx, keepAliveFailureCount);

                        long keepAliveFailuresAllowed = client.getConfig().getKeepAliveFailuresAllowed().longValue();

                        if (keepAliveFailureCount > keepAliveFailuresAllowed) {
                            LOGGER.warn(
                                "[{}] Keep Alive failureCount={} exceeds failuresAllowed={}",
                                ctx.getInstanceId(), keepAliveFailureCount, keepAliveFailuresAllowed
                            );

                            ctx.fireEvent(new Event.KeepAliveFailure());

                            // Close the underlying channel to force a reconnect.
                            // This is useful if the server has gone offline in an "unclean"
                            // manner to avoid having to wait for the underlying TCP stack's keep
                            // alive to kick in.
                            UaTransport transport = client.getStackClient().getTransport();
                            if (transport instanceof OpcTcpTransport) {
                                Channel channel = ((OpcTcpTransport) transport).channel().getNow(null);
                                if (channel != null) {
                                    channel.close();
                                }
                            }
                        } else {
                            LOGGER.debug(
                                "[{}] Keep Alive failureCount={}",
                                ctx.getInstanceId(), keepAliveFailureCount, ex
                            );
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

        fb.when(State.Closing)
            .on(Event.CloseSessionSuccess.class)
            .transitionTo(State.Inactive);


        /* External Transition Actions */

        fb.onTransitionTo(State.Closing)
            .from(State.Active)
            .via(Event.CloseSession.class)
            .execute(ctx -> {
                SessionFsm.CloseFuture closeFuture = new SessionFsm.CloseFuture();
                KEY_CLOSE_FUTURE.set(ctx, closeFuture);

                Event.CloseSession closeSession = (Event.CloseSession) ctx.event();
                complete(closeSession.future).with(closeFuture.future);

                OpcUaSession session = KEY_SESSION.get(ctx);

                closeSession(ctx, client, session).whenComplete((u, ex) -> {
                    if (u != null) {
                        LOGGER.debug("[{}] Session closed: {}", ctx.getInstanceId(), session);
                    } else {
                        LOGGER.debug("[{}] CloseSession failed: {}", ctx.getInstanceId(), ex.getMessage(), ex);
                    }

                    ctx.fireEvent(new Event.CloseSessionSuccess());
                });
            });

        fb.onTransitionFrom(State.Closing)
            .to(State.Inactive)
            .via(Event.CloseSessionSuccess.class)
            .execute(ctx -> {
                SessionFsm.CloseFuture closeFuture = KEY_CLOSE_FUTURE.get(ctx);

                if (closeFuture != null) {
                    client.getConfig().getExecutor().execute(() ->
                        closeFuture.future.complete(Unit.VALUE)
                    );
                }
            });


        /* Internal Transition Actions */

        fb.onInternalTransition(State.Closing)
            .via(Event.CloseSession.class)
            .execute(ctx -> {
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

    private static void handleGetSessionEvent(ActionContext<State, Event> ctx) {
        CompletableFuture<OpcUaSession> sessionFuture = KEY_SESSION_FUTURE.get(ctx).future;

        Event.GetSession event = (Event.GetSession) ctx.event();
        complete(event.future).with(sessionFuture);
    }

    private static void handleOpenSessionEvent(ActionContext<State, Event> ctx) {
        CompletableFuture<OpcUaSession> sessionFuture = KEY_SESSION_FUTURE.get(ctx).future;

        Event.OpenSession event = (Event.OpenSession) ctx.event();
        complete(event.future).with(sessionFuture);
    }

    private static void handleFailureToOpenSession(
        OpcUaClient client,
        ActionContext<State, Event> ctx,
        Throwable failure
    ) {

        SessionFuture sessionFuture = KEY_SESSION_FUTURE.remove(ctx);

        if (sessionFuture != null) {
            client.getConfig().getExecutor().execute(() ->
                sessionFuture.future.completeExceptionally(failure)
            );
        }
    }

    private static CompletableFuture<Unit> closeSession(
        FsmContext<State, Event> ctx,
        OpcUaClient client,
        OpcUaSession session) {

        CompletableFuture<Unit> closeFuture = new CompletableFuture<>();

        UaStackClient stackClient = client.getStackClient();

        RequestHeader requestHeader = stackClient.newRequestHeader(
            session.getAuthenticationToken(),
            uint(5000)
        );

        CloseSessionRequest request = new CloseSessionRequest(requestHeader, true);

        LOGGER.debug("[{}] Sending CloseSessionRequest...", ctx.getInstanceId());

        stackClient.sendRequest(request).whenCompleteAsync(
            (csr, ex2) -> {
                client.getSubscriptionManager().cancelWatchdogTimers();

                closeFuture.complete(Unit.VALUE);
            },
            client.getConfig().getExecutor()
        );

        return closeFuture;
    }

    @SuppressWarnings("Duplicates")
    private static CompletableFuture<CreateSessionResponse> createSession(
        FsmContext<State, Event> ctx,
        OpcUaClient client) {

        UaStackClient stackClient = client.getStackClient();

        EndpointDescription endpoint = stackClient.getConfig().getEndpoint();

        String gatewayServerUri = endpoint.getServer().getGatewayServerUri();

        String serverUri;
        if (gatewayServerUri != null && !gatewayServerUri.isEmpty()) {
            serverUri = endpoint.getServer().getApplicationUri();
        } else {
            serverUri = null;
        }

        ByteString clientNonce = NonceUtil.generateNonce(32);

        ByteString clientCertificate = stackClient.getConfig().getCertificate()
            .map(c -> {
                try {
                    return ByteString.of(c.getEncoded());
                } catch (CertificateEncodingException e) {
                    return ByteString.NULL_VALUE;
                }
            })
            .orElse(ByteString.NULL_VALUE);

        ApplicationDescription clientDescription = new ApplicationDescription(
            client.getConfig().getApplicationUri(),
            client.getConfig().getProductUri(),
            client.getConfig().getApplicationName(),
            ApplicationType.Client,
            null,
            null,
            null
        );

        CreateSessionRequest request = new CreateSessionRequest(
            client.newRequestHeader(),
            clientDescription,
            serverUri,
            client.getConfig().getEndpoint().getEndpointUrl(),
            client.getConfig().getSessionName().get(),
            clientNonce,
            clientCertificate,
            client.getConfig().getSessionTimeout().doubleValue(),
            client.getConfig().getMaxResponseMessageSize()
        );

        LOGGER.debug("[{}] Sending CreateSessionRequest...", ctx.getInstanceId());

        return stackClient.sendRequest(request)
            .thenApply(CreateSessionResponse.class::cast)
            .thenCompose(response -> {
                try {
                    SecurityPolicy securityPolicy = SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri());

                    if (securityPolicy != SecurityPolicy.None) {
                        if (response.getServerCertificate().isNullOrEmpty()) {
                            throw new UaException(StatusCodes.Bad_SecurityChecksFailed,
                                "Certificate missing from CreateSessionResponse");
                        }

                        List<X509Certificate> serverCertificateChain = CertificateUtil
                            .decodeCertificates(response.getServerCertificate().bytesOrEmpty());

                        X509Certificate serverCertificate = serverCertificateChain.get(0);

                        X509Certificate certificateFromEndpoint = CertificateUtil
                            .decodeCertificate(endpoint.getServerCertificate().bytesOrEmpty());

                        if (!serverCertificate.equals(certificateFromEndpoint)) {
                            throw new UaException(
                                StatusCodes.Bad_SecurityChecksFailed,
                                "Certificate from CreateSessionResponse did not " +
                                    "match certificate from EndpointDescription!");
                        }

                        client.getConfig().getCertificateValidator().validateCertificateChain(
                            serverCertificateChain,
                            endpoint.getServer().getApplicationUri(),
                            EndpointUtil.getHost(endpoint.getEndpointUrl())
                        );

                        SignatureData serverSignature = response.getServerSignature();

                        byte[] dataBytes = Bytes.concat(
                            clientCertificate.bytesOrEmpty(),
                            clientNonce.bytesOrEmpty()
                        );

                        byte[] signatureBytes = serverSignature.getSignature().bytesOrEmpty();

                        SignatureUtil.verify(
                            SecurityAlgorithm.fromUri(serverSignature.getAlgorithm()),
                            serverCertificate,
                            dataBytes,
                            signatureBytes
                        );
                    }

                    return completedFuture(response);
                } catch (UaException e) {
                    return failedFuture(e);
                }
            });
    }

    @SuppressWarnings("Duplicates")
    private static CompletableFuture<OpcUaSession> activateSession(
        FsmContext<State, Event> ctx,
        OpcUaClient client,
        CreateSessionResponse csr) {

        UaStackClient stackClient = client.getStackClient();

        try {
            EndpointDescription endpoint = client.getConfig().getEndpoint();

            ByteString csrNonce = csr.getServerNonce();

            SignedIdentityToken signedIdentityToken =
                client.getConfig().getIdentityProvider()
                    .getIdentityToken(endpoint, csrNonce);

            UserIdentityToken userIdentityToken = signedIdentityToken.getToken();
            SignatureData userTokenSignature = signedIdentityToken.getSignature();

            ActivateSessionRequest request = new ActivateSessionRequest(
                client.newRequestHeader(csr.getAuthenticationToken()),
                buildClientSignature(client.getConfig(), csrNonce),
                new SignedSoftwareCertificate[0],
                client.getConfig().getSessionLocaleIds(),
                ExtensionObject.encode(client.getStaticSerializationContext(), userIdentityToken),
                userTokenSignature
            );

            LOGGER.debug("[{}] Sending ActivateSessionRequest...", ctx.getInstanceId());

            return stackClient.sendRequest(request)
                .thenApply(ActivateSessionResponse.class::cast)
                .thenCompose(asr -> {
                    ByteString asrNonce = asr.getServerNonce();

                    // TODO check for repeated nonce?

                    OpcUaSession session = new OpcUaSession(
                        csr.getAuthenticationToken(),
                        csr.getSessionId(),
                        client.getConfig().getSessionName().get(),
                        csr.getRevisedSessionTimeout(),
                        csr.getMaxRequestMessageSize(),
                        csr.getServerCertificate(),
                        csr.getServerSoftwareCertificates()
                    );

                    session.setServerNonce(asrNonce);

                    return completedFuture(session);
                });
        } catch (Exception ex) {
            return failedFuture(ex);
        }
    }

    @SuppressWarnings("Duplicates")
    private static CompletableFuture<Unit> transferSubscriptions(
        FsmContext<State, Event> ctx,
        OpcUaClient client,
        OpcUaSession session) {

        UaStackClient stackClient = client.getStackClient();
        OpcUaSubscriptionManager subscriptionManager = client.getSubscriptionManager();
        ImmutableList<UaSubscription> subscriptions = subscriptionManager.getSubscriptions();

        if (subscriptions.isEmpty()) {
            return completedFuture(Unit.VALUE);
        }

        CompletableFuture<Unit> transferFuture = new CompletableFuture<>();

        UInteger[] subscriptionIdsArray = subscriptions.stream()
            .map(UaSubscription::getSubscriptionId)
            .toArray(UInteger[]::new);

        TransferSubscriptionsRequest request = new TransferSubscriptionsRequest(
            client.newRequestHeader(session.getAuthenticationToken()),
            subscriptionIdsArray,
            true
        );

        LOGGER.debug("[{}] Sending TransferSubscriptionsRequest...", ctx.getInstanceId());

        stackClient.sendRequest(request)
            .thenApply(TransferSubscriptionsResponse.class::cast)
            .whenComplete((tsr, ex) -> {
                if (tsr != null) {
                    List<TransferResult> results = l(tsr.getResults());

                    LOGGER.debug(
                        "[{}] TransferSubscriptions supported: {}",
                        ctx.getInstanceId(), tsr.getResponseHeader().getServiceResult());

                    if (LOGGER.isDebugEnabled()) {
                        try {
                            Stream<UInteger> subscriptionIds = subscriptions.stream()
                                .map(UaSubscription::getSubscriptionId);
                            Stream<StatusCode> statusCodes = results.stream()
                                .map(TransferResult::getStatusCode);

                            //noinspection UnstableApiUsage
                            String[] ss = Streams.zip(
                                subscriptionIds,
                                statusCodes,
                                (i, s) -> String.format("id=%s/%s",
                                    i, StatusCodes.lookup(s.getValue())
                                        .map(sa -> sa[0]).orElse(s.toString()))
                            ).toArray(String[]::new);

                            LOGGER.debug(
                                "[{}] TransferSubscriptions results: {}",
                                ctx.getInstanceId(), Arrays.toString(ss));
                        } catch (Throwable t) {
                            LOGGER.error("[{}] error logging TransferSubscription results", ctx.getInstanceId(), t);
                        }
                    }

                    client.getConfig().getExecutor().execute(() -> {
                        for (int i = 0; i < results.size(); i++) {
                            TransferResult result = results.get(i);

                            if (!result.getStatusCode().isGood()) {
                                UaSubscription subscription = subscriptions.get(i);

                                subscriptionManager.transferFailed(
                                    subscription.getSubscriptionId(),
                                    result.getStatusCode()
                                );
                            }
                        }
                    });

                    transferFuture.complete(Unit.VALUE);
                } else {
                    StatusCode statusCode = UaException.extract(ex)
                        .map(UaException::getStatusCode)
                        .orElse(StatusCode.BAD);

                    LOGGER.debug("[{}] TransferSubscriptions not supported: {}", ctx.getInstanceId(), statusCode);

                    client.getConfig().getExecutor().execute(() -> {
                        // transferFailed() will remove the subscription, but that is okay
                        // because the list from getSubscriptions() above is a copy.
                        for (UaSubscription subscription : subscriptions) {
                            subscriptionManager.transferFailed(
                                subscription.getSubscriptionId(), statusCode);
                        }
                    });

                    // Bad_ServiceUnsupported is the correct response when transfers aren't
                    // supported but server implementations interpret the spec differently.
                    if (statusCode.getValue() == StatusCodes.Bad_NotImplemented ||
                        statusCode.getValue() == StatusCodes.Bad_NotSupported ||
                        statusCode.getValue() == StatusCodes.Bad_OutOfService ||
                        statusCode.getValue() == StatusCodes.Bad_ServiceUnsupported) {

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
            });

        return transferFuture;
    }

    private static CompletableFuture<Unit> initialize(
        FsmContext<State, Event> ctx,
        OpcUaClient client,
        OpcUaSession session) {

        List<SessionFsm.SessionInitializer> initializers =
            KEY_SESSION_INITIALIZERS.get(ctx).sessionInitializers;

        if (initializers.isEmpty()) {
            return completedFuture(Unit.VALUE);
        } else {
            UaStackClient stackClient = client.getStackClient();

            CompletableFuture<?>[] futures = initializers.stream()
                .map(i -> i.initialize(stackClient, session))
                .toArray(CompletableFuture[]::new);

            return CompletableFuture.allOf(futures).thenApply(v -> Unit.VALUE);
        }
    }

    private static CompletableFuture<ReadResponse> sendKeepAlive(OpcUaClient client, OpcUaSession session) {
        ReadRequest keepAliveRequest = createKeepAliveRequest(client, session);

        return client.getStackClient()
            .sendRequest(keepAliveRequest)
            .thenApply(ReadResponse.class::cast);
    }

    private static ReadRequest createKeepAliveRequest(OpcUaClient client, OpcUaSession session) {
        RequestHeader requestHeader = client.getStackClient().newRequestHeader(
            session.getAuthenticationToken(),
            client.getConfig().getKeepAliveTimeout()
        );

        return new ReadRequest(
            requestHeader,
            0.0,
            TimestampsToReturn.Neither,
            new ReadValueId[]{new ReadValueId(
                Identifiers.Server_ServerStatus_State,
                AttributeId.Value.uid(),
                null,
                QualifiedName.NULL_VALUE
            )}
        );
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
            List<X509Certificate> serverCertificates = CertificateUtil.decodeCertificates(
                endpoint.getServerCertificate().bytesOrEmpty()
            );

            // Signature data is serverCert + serverNonce signed with our private key.
            byte[] serverNonceBytes = serverNonce.bytesOrEmpty();
            byte[] serverCertificateBytes = serverCertificates.get(0).getEncoded();
            byte[] dataToSign = Bytes.concat(serverCertificateBytes, serverNonceBytes);

            byte[] signature = SignatureUtil.sign(
                signatureAlgorithm,
                privateKey,
                ByteBuffer.wrap(dataToSign)
            );

            return new SignatureData(signatureAlgorithm.getUri(), ByteString.of(signature));
        }
    }

    private static class SessionFaultListener implements ServiceFaultListener {

        private static final Predicate<StatusCode> SESSION_ERROR = statusCode -> {
            long status = statusCode.getValue();

            return status == StatusCodes.Bad_SessionClosed ||
                status == StatusCodes.Bad_SessionIdInvalid ||
                status == StatusCodes.Bad_SessionNotActivated;
        };

        private static final Predicate<StatusCode> SECURE_CHANNEL_ERROR = statusCode -> {
            long status = statusCode.getValue();

            return status == StatusCodes.Bad_SecureChannelIdInvalid ||
                status == StatusCodes.Bad_SecurityChecksFailed ||
                status == StatusCodes.Bad_TcpSecureChannelUnknown ||
                status == StatusCodes.Bad_RequestTypeInvalid;
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
                logger.debug("[{}] ServiceFault: {}", fsm.getFromContext(FsmContext::getInstanceId), serviceResult);

                fsm.fireEvent(new Event.ServiceFault(serviceResult));
            }
        }

    }

}
