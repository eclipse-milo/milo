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

import com.digitalpetri.fsm.Fsm;
import com.digitalpetri.fsm.FsmContext;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.stack.core.util.Unit;

public class SessionFsm {

  static final String LOGGER_NAME = "org.eclipse.milo.opcua.sdk.client.SessionFsm";

  private final List<SessionInitializer> sessionInitializers;
  private final List<SessionActivityListener> sessionActivityListeners;

  private final Fsm<State, Event> fsm;

  SessionFsm(Fsm<State, Event> fsm) {
    this.fsm = fsm;

    sessionInitializers =
        fsm.getFromContext(
            ctx -> {
              KEY_SESSION_INITIALIZERS.set(ctx, new SessionInitializers());
              return KEY_SESSION_INITIALIZERS.get(ctx).sessionInitializers;
            });

    sessionActivityListeners =
        fsm.getFromContext(
            ctx -> {
              KEY_SESSION_ACTIVITY_LISTENERS.set(ctx, new SessionActivityListeners());
              return KEY_SESSION_ACTIVITY_LISTENERS.get(ctx).sessionActivityListeners;
            });
  }

  public CompletableFuture<OpcUaSession> openSession() {
    Event.OpenSession openSession = new Event.OpenSession();

    fsm.fireEvent(openSession);

    return openSession.future;
  }

  public CompletableFuture<Unit> closeSession() {
    Event.CloseSession closeSession = new Event.CloseSession();

    fsm.fireEvent(closeSession);

    return closeSession.future;
  }

  public CompletableFuture<OpcUaSession> getSession() {
    CompletableFuture<OpcUaSession> future =
        fsm.getFromContext(
            ctx -> {
              State state = ctx.currentState();

              if (state == State.Active) {
                SessionFuture sf = KEY_SESSION_FUTURE.get(ctx);

                assert sf != null;

                return sf.future;
              } else {
                return null;
              }
            });

    if (future != null) {
      return future;
    } else {
      // "Slow" path... not activated yet.
      Event.GetSession getSession = new Event.GetSession();

      fsm.fireEvent(getSession);

      return getSession.future;
    }
  }

  public void addInitializer(SessionInitializer initializer) {
    sessionInitializers.add(initializer);
  }

  public void removeInitializer(SessionInitializer initializer) {
    sessionInitializers.remove(initializer);
  }

  public void addActivityListener(SessionActivityListener listener) {
    sessionActivityListeners.add(listener);
  }

  public void removeActivityListener(SessionActivityListener listener) {
    sessionActivityListeners.remove(listener);
  }

  static final FsmContext.Key<Long> KEY_WAIT_TIME = new FsmContext.Key<>("delay", Long.class);

  static final FsmContext.Key<ScheduledFuture> KEY_WAIT_FUTURE =
      new FsmContext.Key<>("delayFuture", ScheduledFuture.class);

  static final FsmContext.Key<CloseFuture> KEY_CLOSE_FUTURE =
      new FsmContext.Key<>("closeFuture", CloseFuture.class);

  static final FsmContext.Key<OpcUaSession> KEY_SESSION =
      new FsmContext.Key<>("session", OpcUaSession.class);

  static final FsmContext.Key<SessionFuture> KEY_SESSION_FUTURE =
      new FsmContext.Key<>("sessionFuture", SessionFuture.class);

  static final FsmContext.Key<Long> KEY_KEEP_ALIVE_FAILURE_COUNT =
      new FsmContext.Key<>("keepAliveFailureCount", Long.class);

  static final FsmContext.Key<ScheduledFuture> KEY_KEEP_ALIVE_SCHEDULED_FUTURE =
      new FsmContext.Key<>("keepAliveScheduledFuture", ScheduledFuture.class);

  static final FsmContext.Key<SessionInitializers> KEY_SESSION_INITIALIZERS =
      new FsmContext.Key<>("sessionInitializers", SessionInitializers.class);

  static final FsmContext.Key<SessionActivityListeners> KEY_SESSION_ACTIVITY_LISTENERS =
      new FsmContext.Key<>("sessionActivityListeners", SessionActivityListeners.class);

  static class CloseFuture {
    final CompletableFuture<Unit> future = new CompletableFuture<>();
  }

  static class SessionFuture {
    final CompletableFuture<OpcUaSession> future = new CompletableFuture<>();
  }

  static class SessionInitializers {
    private SessionInitializers() {}

    final List<SessionInitializer> sessionInitializers = new CopyOnWriteArrayList<>();
  }

  static class SessionActivityListeners {
    private SessionActivityListeners() {}

    final List<SessionActivityListener> sessionActivityListeners = new CopyOnWriteArrayList<>();
  }

  public interface SessionInitializer {

    CompletableFuture<Unit> initialize(OpcUaClient client, OpcUaSession session);
  }
}
