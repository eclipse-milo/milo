/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.digitalpetri.fsm.Fsm;
import io.netty.util.HashedWheelTimer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.client.subscriptions.PublishingManager;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.Test;

/** Session listener ordering across reactivation of the same Session object. */
class SessionActivityOrderingTest {
  private static class QueueExecutor extends AbstractExecutorService {
    public void shutdown() {}

    public List<Runnable> shutdownNow() {
      return List.of();
    }

    public boolean isShutdown() {
      return false;
    }

    public boolean isTerminated() {
      return false;
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) {
      return false;
    }

    final Deque<Runnable> q = new ArrayDeque<>();

    public void execute(Runnable r) {
      q.addLast(r);
    }

    void drain() {
      int count = 0;
      while (!q.isEmpty()) {
        if (++count > 1000) throw new AssertionError("loop");
        q.removeFirst().run();
      }
    }
  }

  private static class Transport implements OpcClientTransport {
    final QueueExecutor executor = new QueueExecutor();
    final HashedWheelTimer timer = new HashedWheelTimer();
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    final OpcClientTransportConfig config =
        OpcTcpClientTransportConfig.newBuilder()
            .setExecutor(executor)
            .setWheelTimer(timer)
            .setScheduledExecutor(scheduler)
            .build();
    int publishes;

    public OpcClientTransportConfig getConfig() {
      return config;
    }

    public CompletableFuture<Unit> connect(ClientApplicationContext context) {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    public CompletableFuture<Unit> disconnect() {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    public CompletableFuture<UaResponseMessageType> sendRequestMessage(UaRequestMessageType req) {
      ResponseHeader h =
          new ResponseHeader(
              DateTime.now(),
              req.getRequestHeader().getRequestHandle(),
              StatusCode.GOOD,
              null,
              null,
              null);
      UaResponseMessageType resp;
      if (req instanceof CreateSessionRequest)
        resp =
            new CreateSessionResponse(
                h,
                new NodeId(1, 1),
                new NodeId(1, 2),
                120000.0,
                ByteString.NULL_VALUE,
                ByteString.NULL_VALUE,
                null,
                null,
                new SignatureData(null, null),
                uint(0));
      else if (req instanceof ActivateSessionRequest)
        resp = new ActivateSessionResponse(h, ByteString.NULL_VALUE, null, null);
      else if (req instanceof ReadRequest)
        resp =
            new ReadResponse(
                h,
                new DataValue[] {
                  new DataValue(new Variant(new String[] {"http://opcfoundation.org/UA/"})),
                  new DataValue(new Variant(new String[] {"urn:test:server"}))
                },
                null);
      else if (req instanceof CreateSubscriptionRequest)
        resp = new CreateSubscriptionResponse(h, uint(1), 1000.0, uint(50), uint(10));
      else if (req instanceof PublishRequest) {
        publishes++;
        return new CompletableFuture<>();
      } else if (req instanceof CloseSessionRequest) resp = new CloseSessionResponse(h);
      else throw new AssertionError(req.getClass());
      return CompletableFuture.completedFuture(resp);
    }
  }

  private static EndpointDescription endpoint() {
    var app =
        new ApplicationDescription(
            "urn:test:server",
            "urn:test",
            LocalizedText.english("test"),
            ApplicationType.Server,
            null,
            null,
            null);
    return new EndpointDescription(
        "opc.tcp://localhost:12685",
        app,
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[] {
          new UserTokenPolicy("anon", UserTokenType.Anonymous, null, null, null)
        },
        org.eclipse.milo.opcua.stack.core.Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
        ubyte(0));
  }

  @Test
  void delayedInactiveNotificationCannotOvertakeReactivation() throws Exception {
    var transport = new Transport();
    try {
      var client =
          new OpcUaClient(
              OpcUaClientConfig.builder()
                  .setEndpoint(endpoint())
                  .setDiscoveryEndpoints(List.of())
                  .setKeepAliveInterval(uint(1000000))
                  .build(),
              transport);
      var notifications = new ArrayList<String>();
      client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionActive(UaSession session) {
              notifications.add("active");
            }

            @Override
            public void onSessionInactive(UaSession session) {
              notifications.add("inactive");
            }
          });
      var connected = client.connectAsync();
      transport.executor.drain();
      if (!connected.isDone() || connected.isCompletedExceptionally())
        throw new AssertionError("connect failed");
      var sf = client.getSessionFsm();
      Field f = SessionFsm.class.getDeclaredField("fsm");
      f.setAccessible(true);
      @SuppressWarnings("unchecked")
      Fsm<State, Event> fsm = (Fsm<State, Event>) f.get(sf);
      var pm = client.getPublishingManager();
      Method suspended = PublishingManager.class.getDeclaredMethod("isPublishingSuspended");
      suspended.setAccessible(true);
      if ((boolean) suspended.invoke(pm)) throw new AssertionError("initial gate shut");
      var session = sf.getSession().join();
      // Run the FSM's inactive transition while deferring the separate listener-dispatch runnable.
      fsm.fireEvent(new Event.ServiceFault(new StatusCode(StatusCodes.Bad_SessionIdInvalid)));
      while (sf.getState() != State.ReactivatingWait) transport.executor.q.removeFirst().run();
      if (transport.executor.q.size() != 1)
        throw new AssertionError("unexpected queued tasks " + transport.executor.q.size());
      Runnable delayedInactive = transport.executor.q.removeFirst();

      // The FSM executor continues on another worker while its inactive callback is delayed.
      fsm.fireEvent(new Event.ReactivatingWaitExpired());
      transport.executor.drain();
      if (sf.getState() != State.Active || sf.getSession().join() != session)
        throw new AssertionError("reactivation failed");
      delayedInactive.run();
      transport.executor.drain();
      assertFalse(
          (boolean) suspended.invoke(pm),
          "an inactive callback from before reactivation must not leave publishing suspended");
      var subscription = new OpcUaSubscription(client);
      var created = subscription.createAsync().toCompletableFuture();
      transport.executor.drain();
      if (!created.isDone() || created.isCompletedExceptionally())
        throw new AssertionError("subscription failed");
      assertEquals(List.of("active", "inactive", "active"), notifications);
      assertEquals(
          2, transport.publishes, "the recovered Session must resume its Publish pipeline");
    } finally {
      transport.scheduler.shutdownNow();
      transport.timer.stop();
    }
  }
}
