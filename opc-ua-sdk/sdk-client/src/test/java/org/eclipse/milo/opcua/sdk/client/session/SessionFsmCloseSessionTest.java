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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaServiceFaultException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable.ChannelState;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.CurrentChannelProvider;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class SessionFsmCloseSessionTest {

  // Regression test for #2239: when the transport is not CONNECTED in ReactivatingWait, a
  // CloseSession request would wait for a new SecureChannel that the Server will reject because
  // the Session is bound to the lost channel (Part 4 §5.7.2.1, §7.35). SessionFsm must skip
  // CloseSession, fail waiting getSession() callers with Bad_SessionClosed, and settle Inactive.
  @ParameterizedTest
  @EnumSource(
      value = ChannelState.class,
      names = {"RECONNECTING", "CONNECTING", "DISCONNECTED", "DISCONNECTING"})
  void disconnectInReactivatingWaitSkipsCloseSessionWhenChannelNotConnected(
      ChannelState nonConnectedState) throws Exception {

    var transport = new StubSessionTransport();
    OpcUaClient client = newClient(transport);

    try {
      client.connectAsync().get(5, TimeUnit.SECONDS);
      assertEquals(State.Active, client.getSessionFsm().getState());

      transport.loseChannel(nonConnectedState);
      awaitState(
          client, State.ReactivatingWait, "channel loss should move FSM to ReactivatingWait");

      CompletableFuture<OpcUaSession> waitingSession = client.getSessionAsync();

      client.disconnectAsync().get(5, TimeUnit.SECONDS);

      assertEquals(
          0,
          transport.closeSessionCalls.get(),
          "CloseSession must not be sent when channel state is " + nonConnectedState);
      assertEquals(1, transport.disconnectCalls.get(), "transport.disconnect() must run");
      assertEquals(State.Inactive, client.getSessionFsm().getState());
      assertFailedWithStatus(waitingSession, StatusCodes.Bad_SessionClosed);
    } finally {
      transport.cleanup();
    }
  }

  // Part 4 §5.7.2.1, §7.35: even if the transport has already opened a replacement SecureChannel
  // while SessionFsm is still in ReactivatingWait after ConnectionLost, the Session is still bound
  // to the old channel and has not been activated on the replacement, so CloseSession must be
  // skipped rather than sent over the wrong SecureChannel.
  @Test
  void disconnectInReactivatingWaitAfterConnectionLostSkipsCloseSessionOnReplacementChannel()
      throws Exception {

    var transport = new StubSessionTransport();
    OpcUaClient client = newClient(transport);

    try {
      client.connectAsync().get(5, TimeUnit.SECONDS);
      assertEquals(State.Active, client.getSessionFsm().getState());

      transport.loseChannel(ChannelState.RECONNECTING);
      awaitState(
          client, State.ReactivatingWait, "channel loss should move FSM to ReactivatingWait");

      // Simulate the transport reconnecting a brand new SecureChannel before ReactivatingWait
      // expires or disconnectAsync() is called.
      transport.reconnectNewChannel();
      assertEquals(ChannelState.CONNECTED, transport.getChannelState());

      client.disconnectAsync().get(5, TimeUnit.SECONDS);

      assertEquals(
          0,
          transport.closeSessionCalls.get(),
          "CloseSession must not be sent over a replacement channel before reactivation");
      assertEquals(State.Inactive, client.getSessionFsm().getState());
    } finally {
      transport.cleanup();
    }
  }

  // Preserves #1414: if SessionFsm entered ReactivatingWait while the Session's original channel
  // is still CONNECTED (e.g., after a ServiceFault), CloseSession can still close the Session on
  // the Server and must be sent.
  @Test
  void disconnectInReactivatingWaitSendsCloseSessionWhenSameChannelRemainsConnected()
      throws Exception {

    var transport = new StubSessionTransport();
    OpcUaClient client = newClient(transport);

    try {
      client.connectAsync().get(5, TimeUnit.SECONDS);
      assertEquals(State.Active, client.getSessionFsm().getState());

      // Trigger Active -> ReactivatingWait via ServiceFault without dropping the channel.
      var fault =
          new ServiceFault(
              new ResponseHeader(
                  DateTime.now(),
                  uint(1),
                  new StatusCode(StatusCodes.Bad_SessionNotActivated),
                  DiagnosticInfo.NULL_VALUE,
                  null,
                  null));
      transport.failNextReadWith(new UaServiceFaultException(fault));
      client
          .readValuesAsync(0.0, null, List.of())
          .handle((values, ex) -> Unit.VALUE)
          .get(5, TimeUnit.SECONDS);

      awaitState(
          client,
          State.ReactivatingWait,
          "session ServiceFault should move FSM to ReactivatingWait");
      assertEquals(ChannelState.CONNECTED, transport.getChannelState());

      client.disconnectAsync().get(5, TimeUnit.SECONDS);

      assertEquals(
          1,
          transport.closeSessionCalls.get(),
          "CloseSession must still be sent when the Session's original channel is CONNECTED");
      assertEquals(State.Inactive, client.getSessionFsm().getState());
    } finally {
      transport.cleanup();
    }
  }

  // If disconnectAsync() races ahead of ConnectionLost while SessionFsm is still in Active after
  // the channel has already left CONNECTED, SessionFsm must skip CloseSession, fire
  // onSessionInactive with the Session, and transition directly to Inactive.
  @Test
  void disconnectInActiveSkipsCloseSessionWhenChannelAlreadyNotConnected() throws Exception {
    var transport = new StubSessionTransport();
    OpcUaClient client = newClient(transport);

    var inactiveSession = new AtomicReference<UaSession>();
    var inactiveCalled = new CountDownLatch(1);
    client.addSessionActivityListener(
        new SessionActivityListener() {
          @Override
          public void onSessionInactive(UaSession session) {
            inactiveSession.set(session);
            inactiveCalled.countDown();
          }
        });

    try {
      client.connectAsync().get(5, TimeUnit.SECONDS);
      assertEquals(State.Active, client.getSessionFsm().getState());

      // Move channel out of CONNECTED without notifying SessionFsm yet, simulating CloseSession
      // winning the queue race against ConnectionLost.
      transport.dropChannelWithoutNotification(ChannelState.RECONNECTING);

      client.disconnectAsync().get(5, TimeUnit.SECONDS);

      assertEquals(
          0,
          transport.closeSessionCalls.get(),
          "CloseSession must be skipped in Active when the channel is already not CONNECTED");
      assertEquals(State.Inactive, client.getSessionFsm().getState());
      assertTrue(
          inactiveCalled.await(5, TimeUnit.SECONDS),
          "onSessionInactive must still fire when leaving Active directly to Inactive");
      assertNotNull(
          inactiveSession.get(), "onSessionInactive must receive the departing OpcUaSession");
    } finally {
      transport.cleanup();
    }
  }

  // Control: when the channel is CONNECTED in Active, disconnectAsync() must still send
  // CloseSession and transition to Inactive.
  @Test
  void disconnectInActiveSendsCloseSessionWhenChannelConnected() throws Exception {
    var transport = new StubSessionTransport();
    OpcUaClient client = newClient(transport);

    try {
      client.connectAsync().get(5, TimeUnit.SECONDS);
      assertEquals(State.Active, client.getSessionFsm().getState());

      client.disconnectAsync().get(5, TimeUnit.SECONDS);

      assertEquals(
          1,
          transport.closeSessionCalls.get(),
          "CloseSession must be sent when disconnecting with a CONNECTED channel");
      assertEquals(State.Inactive, client.getSessionFsm().getState());
    } finally {
      transport.cleanup();
    }
  }

  // Regression test for channel drop racing with Active entry: if the channel drops right before
  // addTransitionListener() registers, the post-registration channel liveness check must still
  // detect the loss, move SessionFsm to ReactivatingWait, and skip CloseSession on disconnect.
  @Test
  void channelDropBeforeTransitionListenerRegistrationTriggersRecoveryAndSkipsCloseSession()
      throws Exception {

    var transport = new StubSessionTransport();
    transport.setOnBeforeAddTransitionListener(
        () -> transport.loseChannel(ChannelState.RECONNECTING));
    OpcUaClient client = newClient(transport);

    try {
      client.connectAsync().get(5, TimeUnit.SECONDS);

      awaitState(
          client,
          State.ReactivatingWait,
          "channel drop before addTransitionListener must still trigger ConnectionLost");

      client.disconnectAsync().get(5, TimeUnit.SECONDS);

      assertEquals(
          0,
          transport.closeSessionCalls.get(),
          "CloseSession must be skipped when channel dropped during Active entry");
      assertEquals(State.Inactive, client.getSessionFsm().getState());
    } finally {
      transport.cleanup();
    }
  }

  // Regression test for #2239 debug-logging edge case: closeSession() must propagate a Server
  // rejection or transport failure on its returned future so SessionFsm logs "CloseSession failed"
  // rather than "Session closed", while disconnectAsync() still swallows the error and settles in
  // State.Inactive.
  @Test
  void closeSessionPropagatesServerRejectionWhileDisconnectStillSettlesInactive() throws Exception {
    var transport = new StubSessionTransport();
    OpcUaClient client = newClient(transport);

    var rejectedFault =
        new ServiceFault(
            new ResponseHeader(
                DateTime.now(),
                uint(1),
                new StatusCode(StatusCodes.Bad_SessionIdInvalid),
                DiagnosticInfo.NULL_VALUE,
                null,
                null));

    try {
      client.connectAsync().get(5, TimeUnit.SECONDS);

      // Direct check on SessionFsmFactory.closeSession(): server rejection fails the future.
      transport.failNextCloseWith(new UaServiceFaultException(rejectedFault));
      CompletableFuture<Unit> closeHelperFuture =
          SessionFsmFactory.closeSession(
              "test-instance", client, new NodeId(1, "session-1"), new NodeId(1, "auth-1"), true);
      assertFailedWithStatus(closeHelperFuture, StatusCodes.Bad_SessionIdInvalid);

      // Control: a successful CloseSessionResponse completes closeSession() normally.
      assertEquals(
          Unit.VALUE,
          SessionFsmFactory.closeSession(
                  "test-instance",
                  client,
                  new NodeId(1, "session-1"),
                  new NodeId(1, "auth-1"),
                  true)
              .get(5, TimeUnit.SECONDS));

      // Caller contract: through disconnectAsync(), a rejected CloseSession is swallowed and the
      // FSM still settles cleanly in State.Inactive.
      transport.failNextCloseWith(new UaServiceFaultException(rejectedFault));
      client.disconnectAsync().get(5, TimeUnit.SECONDS);
      assertEquals(State.Inactive, client.getSessionFsm().getState());
    } finally {
      transport.cleanup();
    }
  }

  private static OpcUaClient newClient(OpcClientTransport transport) {
    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint())
            .setDiscoveryEndpoints(List.of())
            .setSessionEndpointValidationEnabled(false)
            .setKeepAliveInterval(uint(60_000))
            .build();
    return new OpcUaClient(config, transport);
  }

  private static void awaitState(OpcUaClient client, State expectedState, String message) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
    while (System.nanoTime() < deadline) {
      if (client.getSessionFsm().getState() == expectedState) {
        return;
      }
      LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
    }
    assertEquals(expectedState, client.getSessionFsm().getState(), message);
  }

  private static void assertFailedWithStatus(CompletableFuture<?> future, long expectedStatusCode) {
    ExecutionException ex =
        assertThrows(
            ExecutionException.class,
            () -> future.get(5, TimeUnit.SECONDS),
            "future should fail with status 0x" + Long.toHexString(expectedStatusCode));

    UaException uaException = assertInstanceOf(UaException.class, ex.getCause());
    assertEquals(expectedStatusCode, uaException.getStatusCode().value());
  }

  private static EndpointDescription endpoint() {
    var server =
        new ApplicationDescription(
            "urn:eclipse:milo:test:server",
            "urn:eclipse:milo:test:product",
            LocalizedText.english("test server"),
            ApplicationType.Server,
            null,
            null,
            null);

    var anonymousPolicy =
        new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null);

    return new EndpointDescription(
        "opc.tcp://localhost:12685/test",
        server,
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[] {anonymousPolicy},
        Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
        ubyte(0));
  }

  private static ResponseHeader goodHeader() {
    return new ResponseHeader(
        DateTime.now(), uint(1), StatusCode.GOOD, DiagnosticInfo.NULL_VALUE, null, null);
  }

  /**
   * Minimal in-memory transport implementing {@link ChannelStateObservable} and {@link
   * CurrentChannelProvider} so SessionFsm transitions can be driven deterministically without
   * sockets or timers.
   */
  private static final class StubSessionTransport
      implements OpcClientTransport, ChannelStateObservable, CurrentChannelProvider {

    private final OpcClientTransportConfig config =
        OpcTcpClientTransportConfig.newBuilder().build();
    private final List<TransitionListener> listeners = new CopyOnWriteArrayList<>();
    private final List<EmbeddedChannel> createdChannels = new CopyOnWriteArrayList<>();

    final AtomicInteger closeSessionCalls = new AtomicInteger();
    final AtomicInteger disconnectCalls = new AtomicInteger();

    private volatile ChannelState channelState = ChannelState.DISCONNECTED;
    private volatile @Nullable EmbeddedChannel currentChannel;
    private final AtomicReference<Throwable> nextReadFailure = new AtomicReference<>();
    private final AtomicReference<Throwable> nextCloseFailure = new AtomicReference<>();
    private final AtomicReference<@Nullable Runnable> onBeforeAddTransitionListener =
        new AtomicReference<>();

    @Override
    public OpcClientTransportConfig getConfig() {
      return config;
    }

    @Override
    public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
      EmbeddedChannel channel = new EmbeddedChannel();
      createdChannels.add(channel);
      currentChannel = channel;
      channelState = ChannelState.CONNECTED;
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<Unit> disconnect() {
      disconnectCalls.incrementAndGet();
      EmbeddedChannel channel = currentChannel;
      currentChannel = null;
      channelState = ChannelState.DISCONNECTED;
      if (channel != null && channel.isOpen()) {
        channel.close();
      }
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      if (requestMessage instanceof CreateSessionRequest) {
        var response =
            new CreateSessionResponse(
                goodHeader(),
                new NodeId(1, "session-1"),
                new NodeId(1, "auth-1"),
                60_000.0,
                ByteString.NULL_VALUE,
                ByteString.NULL_VALUE,
                new EndpointDescription[0],
                new SignedSoftwareCertificate[0],
                new SignatureData(null, null),
                uint(0));
        return CompletableFuture.completedFuture(response);
      }

      if (requestMessage instanceof ActivateSessionRequest) {
        var response =
            new ActivateSessionResponse(
                goodHeader(), ByteString.NULL_VALUE, new StatusCode[0], new DiagnosticInfo[0]);
        return CompletableFuture.completedFuture(response);
      }

      if (requestMessage instanceof CloseSessionRequest) {
        closeSessionCalls.incrementAndGet();
        Throwable failure = nextCloseFailure.getAndSet(null);
        if (failure != null) {
          return CompletableFuture.failedFuture(failure);
        }
        if (channelState != ChannelState.CONNECTED) {
          // Simulate a transport that parks requests until the next channel arrives (so tests
          // fail loudly if CloseSession is mistakenly sent while disconnected/reconnecting).
          return new CompletableFuture<>();
        }
        return CompletableFuture.completedFuture(new CloseSessionResponse(goodHeader()));
      }

      if (requestMessage instanceof ReadRequest) {
        Throwable failure = nextReadFailure.getAndSet(null);
        if (failure != null) {
          return CompletableFuture.failedFuture(failure);
        }
        return CompletableFuture.failedFuture(
            new UaException(StatusCodes.Bad_NotImplemented, "stub read"));
      }

      return CompletableFuture.failedFuture(
          new UaException(StatusCodes.Bad_NotImplemented, "unexpected request: " + requestMessage));
    }

    @Override
    public void addTransitionListener(TransitionListener listener) {
      Runnable hook = onBeforeAddTransitionListener.getAndSet(null);
      if (hook != null) {
        hook.run();
      }
      listeners.add(listener);
    }

    @Override
    public void removeTransitionListener(TransitionListener listener) {
      listeners.remove(listener);
    }

    @Override
    public ChannelState getChannelState() {
      return channelState;
    }

    @Override
    public @Nullable Channel getCurrentChannel() {
      return currentChannel;
    }

    void failNextReadWith(Throwable failure) {
      nextReadFailure.set(failure);
    }

    void failNextCloseWith(Throwable failure) {
      nextCloseFailure.set(failure);
    }

    void setOnBeforeAddTransitionListener(Runnable hook) {
      onBeforeAddTransitionListener.set(hook);
    }

    void dropChannelWithoutNotification(ChannelState nextState) {
      EmbeddedChannel channel = currentChannel;
      currentChannel = null;
      channelState = nextState;
      if (channel != null && channel.isOpen()) {
        channel.close();
      }
    }

    void loseChannel(ChannelState nextState) {
      dropChannelWithoutNotification(nextState);
      for (TransitionListener listener : listeners) {
        listener.onStateTransition(false);
      }
    }

    void reconnectNewChannel() {
      EmbeddedChannel channel = new EmbeddedChannel();
      createdChannels.add(channel);
      currentChannel = channel;
      channelState = ChannelState.CONNECTED;
      for (TransitionListener listener : listeners) {
        listener.onStateTransition(true);
      }
    }

    void cleanup() {
      for (EmbeddedChannel channel : createdChannels) {
        channel.finishAndReleaseAll();
      }
      createdChannels.clear();
    }
  }
}
