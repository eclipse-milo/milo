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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaServiceFaultException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
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
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserNameIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Deterministic unit tests for {@link SessionFsm} identity-change state transitions, fail-fast
 * boundaries, active-state hook guards, concurrency, and in-flight interruptions.
 */
class SessionFsmChangeIdentityTest {

  private static final long TIMEOUT_SECONDS = 5L;

  @Nested
  class FailFastOutsideActive {

    @Test
    void nullIdentityProviderThrowsNullPointerException() {
      var transport = new ScriptedTransport();
      OpcUaClient client = newClient(transport, AnonymousProvider.INSTANCE);

      assertThrows(NullPointerException.class, () -> client.changeIdentity(null));
      assertThrows(NullPointerException.class, () -> client.changeIdentityAsync(null));
      assertThrows(NullPointerException.class, () -> client.getSessionFsm().changeIdentity(null));
    }

    // Calling changeIdentity before connect() must fail immediately with Bad_SessionClosed rather
    // than blocking or opening a Session.
    @Test
    void inactiveStateFailsFastWithBadSessionClosed() {
      var transport = new ScriptedTransport();
      OpcUaClient client = newClient(transport, AnonymousProvider.INSTANCE);

      assertEquals(State.Inactive, client.getSessionFsm().getState());
      assertFailedWithStatus(
          client.changeIdentityAsync(new UsernameProvider("user1", "password")),
          StatusCodes.Bad_SessionClosed);
      assertEquals(0, transport.sentRequests.size(), "must not send requests while Inactive");
    }

    // In State.Closing, unmatched events are normally shelved; ChangeIdentity must fail fast with
    // Bad_SessionClosed instead of being shelved behind CloseSession.
    @Test
    void closingStateFailsFastWithBadSessionClosedWithoutShelving() throws Exception {
      var transport = new ScriptedTransport();
      OpcUaClient client = newClient(transport, AnonymousProvider.INSTANCE);
      try {
        connectToActive(client, transport);

        transport.parkNextCloseSession();
        CompletableFuture<OpcUaClient> disconnectFuture = client.disconnectAsync();
        PendingRequest closeReq = transport.awaitRequest(CloseSessionRequest.class);
        awaitState(client, State.Closing);

        assertFailedWithStatus(
            client.changeIdentityAsync(new UsernameProvider("user1", "password")),
            StatusCodes.Bad_SessionClosed);

        closeReq.complete(new CloseSessionResponse(goodHeader()));
        disconnectFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        awaitState(client, State.Inactive);
      } finally {
        transport.releaseAll();
      }
    }

    // Calling changeIdentity while CreateSession is in flight (Creating) or waiting to retry
    // (CreatingWait) must fail fast with Bad_SessionNotActivated.
    @Test
    void creatingAndCreatingWaitFailFastWithBadSessionNotActivated() throws Exception {
      var transport = new ScriptedTransport();
      OpcUaClient client = newClient(transport, AnonymousProvider.INSTANCE);
      try {
        transport.parkNextCreateSession();
        CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();
        PendingRequest createReq = transport.awaitRequest(CreateSessionRequest.class);
        awaitState(client, State.Creating);

        assertFailedWithStatus(
            client.changeIdentityAsync(new UsernameProvider("user1", "password")),
            StatusCodes.Bad_SessionNotActivated);

        // Fail CreateSession so the FSM moves to CreatingWait and assert fail-fast there too.
        createReq.fail(new UaException(StatusCodes.Bad_CommunicationError));
        awaitState(client, State.CreatingWait);

        assertFailedWithStatus(
            client.changeIdentityAsync(new UsernameProvider("user1", "password")),
            StatusCodes.Bad_SessionNotActivated);

        client.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertThrows(
            ExecutionException.class, () -> connectFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS));
      } finally {
        transport.releaseAll();
      }
    }

    // Calling changeIdentity during Activating or Initializing must fail fast with
    // Bad_SessionNotActivated.
    @Test
    void activatingAndInitializingFailFastWithBadSessionNotActivated() throws Exception {
      var transport = new ScriptedTransport();
      OpcUaClient client = newClient(transport, AnonymousProvider.INSTANCE);
      var initializerEntered = new CountDownLatch(1);
      var releaseInitializer = new CompletableFuture<Unit>();
      client.addSessionInitializer(
          (c, s) -> {
            initializerEntered.countDown();
            return releaseInitializer;
          });

      try {
        transport.parkNextActivateSession();
        CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();
        PendingRequest activateReq = transport.awaitRequest(ActivateSessionRequest.class);
        awaitState(client, State.Activating);

        assertFailedWithStatus(
            client.changeIdentityAsync(new UsernameProvider("user1", "password")),
            StatusCodes.Bad_SessionNotActivated);

        activateReq.complete(activateResponse(StatusCode.GOOD, ByteString.of(new byte[] {1, 2})));
        assertTrue(
            initializerEntered.await(TIMEOUT_SECONDS, TimeUnit.SECONDS),
            "FSM should enter Initializing");
        awaitState(client, State.Initializing);

        assertFailedWithStatus(
            client.changeIdentityAsync(new UsernameProvider("user1", "password")),
            StatusCodes.Bad_SessionNotActivated);

        releaseInitializer.complete(Unit.VALUE);
        connectFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        awaitState(client, State.Active);
      } finally {
        releaseInitializer.complete(Unit.VALUE);
        transport.releaseAll();
        client.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      }
    }

    // OPC 10000-4 §5.7.3.1 forbids changing user identity on a replacement SecureChannel during
    // reactivation, so ReactivatingWait and Reactivating must fail fast with
    // Bad_SessionNotActivated.
    @Test
    void reactivatingWaitAndReactivatingFailFastWithBadSessionNotActivated() throws Exception {
      var transport = new ScriptedTransport();
      OpcUaClient client = newClient(transport, AnonymousProvider.INSTANCE);
      try {
        connectToActive(client, transport);

        transport.parkNextActivateSession();
        transport.fireConnectionLost();
        awaitState(client, State.ReactivatingWait);

        assertFailedWithStatus(
            client.changeIdentityAsync(new UsernameProvider("user1", "password")),
            StatusCodes.Bad_SessionNotActivated);

        // Wait for the 1s backoff timer to move the FSM into Reactivating.
        PendingRequest reactivateReq = transport.awaitRequest(ActivateSessionRequest.class);
        awaitState(client, State.Reactivating);

        assertFailedWithStatus(
            client.changeIdentityAsync(new UsernameProvider("user1", "password")),
            StatusCodes.Bad_SessionNotActivated);

        reactivateReq.complete(activateResponse(StatusCode.GOOD, ByteString.of(new byte[] {3, 4})));
        awaitState(client, State.Active);
      } finally {
        transport.releaseAll();
        client.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      }
    }
  }

  @Nested
  class ConcurrencyAndActiveGuards {

    // A second concurrent changeIdentity call while one is already in flight must fail immediately
    // with Bad_InvalidState, while getSessionAsync() remains immediately usable and no
    // onSessionInactive / onSessionActive callbacks or transport-listener removals occur.
    @Test
    void concurrentCallFailsWithBadInvalidStateAndActiveHooksStayIntact() throws Exception {
      var transport = new ScriptedTransport();
      OpcUaClient client = newClient(transport, AnonymousProvider.INSTANCE);
      var listener = new RecordingActivityListener();
      client.addSessionActivityListener(listener);

      try {
        OpcUaSession initialSession = connectToActive(client, transport);
        assertTrue(
            listener.awaitActiveCount(1, TIMEOUT_SECONDS),
            "initial connect should fire onSessionActive once");

        int listenerRemovalsBefore = transport.removeTransitionListenerCalls.get();

        transport.parkNextActivateSession();
        CompletableFuture<OpcUaSession> first =
            client.changeIdentityAsync(new UsernameProvider("user1", "password"));

        PendingRequest changeReq = transport.awaitRequest(ActivateSessionRequest.class);
        awaitState(client, State.ChangingIdentity);

        // Session stays available to normal callers while ChangingIdentity is in flight.
        assertSame(
            initialSession,
            client.getSessionAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS),
            "getSessionAsync() must still return the active Session during ChangingIdentity");

        // Concurrent changeIdentity fails fast with Bad_InvalidState.
        assertFailedWithStatus(
            client.changeIdentityAsync(new UsernameProvider("user2", "password")),
            StatusCodes.Bad_InvalidState);

        // Active-state hooks must NOT have fired or detached while entering ChangingIdentity.
        assertEquals(
            listenerRemovalsBefore,
            transport.removeTransitionListenerCalls.get(),
            "transition Active -> ChangingIdentity must not detach transport TransitionListener");
        assertEquals(0, listener.inactiveCount.get(), "must not fire onSessionInactive");
        assertEquals(1, listener.activeCount.get(), "must not fire extra onSessionActive");

        changeReq.complete(
            activateResponse(
                new StatusCode(StatusCodes.Good_PasswordChangeRequired),
                ByteString.of(new byte[] {9, 9})));

        OpcUaSession updated = first.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertSame(initialSession, updated);
        assertEquals(
            StatusCodes.Good_PasswordChangeRequired,
            updated.getLastActivateSessionServiceResult().orElseThrow().value());
        awaitState(client, State.Active);

        assertTrue(
            listener.awaitIdentityChangedCount(1, TIMEOUT_SECONDS),
            "successful changeIdentity must fire onSessionIdentityChanged once");
        assertEquals(0, listener.inactiveCount.get(), "must not fire onSessionInactive");
        assertEquals(1, listener.activeCount.get(), "must not fire extra onSessionActive");
        assertEquals(0, listener.attemptFailedCount.get(), "must not fire onSessionAttemptFailed");
        assertEquals(
            listenerRemovalsBefore,
            transport.removeTransitionListenerCalls.get(),
            "returning ChangingIdentity -> Active must not detach transport TransitionListener");
      } finally {
        transport.releaseAll();
        client.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      }
    }

    // A refused ActivateSession during ChangingIdentity must return to Active without abandoning
    // the Session, without firing onSessionInactive/onSessionActive/onSessionAttemptFailed/
    // onSessionIdentityChanged, and without losing the previous identity.
    @Test
    void refusedActivateSessionReturnsToActiveWithoutFiringLifecycleFailureHooks()
        throws Exception {
      var transport = new ScriptedTransport();
      OpcUaClient client = newClient(transport, AnonymousProvider.INSTANCE);
      var listener = new RecordingActivityListener();
      client.addSessionActivityListener(listener);

      try {
        connectToActive(client, transport);
        assertTrue(listener.awaitActiveCount(1, TIMEOUT_SECONDS));

        transport.parkNextActivateSession();
        CompletableFuture<OpcUaSession> changeFuture =
            client.changeIdentityAsync(new UsernameProvider("user1", "wrong"));
        PendingRequest changeReq = transport.awaitRequest(ActivateSessionRequest.class);
        awaitState(client, State.ChangingIdentity);

        changeReq.fail(
            new UaServiceFaultException(
                new ServiceFault(
                    new ResponseHeader(
                        DateTime.now(),
                        uint(0),
                        new StatusCode(StatusCodes.Bad_UserAccessDenied),
                        DiagnosticInfo.NULL_VALUE,
                        new String[0],
                        null))));

        assertFailedWithStatus(changeFuture, StatusCodes.Bad_UserAccessDenied);
        awaitState(client, State.Active);

        assertEquals(0, listener.inactiveCount.get());
        assertEquals(1, listener.activeCount.get());
        assertEquals(0, listener.identityChangedCount.get());
        assertEquals(0, listener.attemptFailedCount.get());
        assertEquals(
            0,
            transport.removeTransitionListenerCalls.get(),
            "refused identity change must not detach transport TransitionListener");
      } finally {
        transport.releaseAll();
        client.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      }
    }
  }

  @Nested
  class InFlightInterruptions {

    // CloseSession arriving while ChangingIdentity is in flight must fail the pending future with
    // Bad_SessionClosed, fire onSessionInactive, send CloseSessionRequest, and ignore any late
    // ActivateSessionResponse without mutating the sticky identity provider.
    @Test
    void closeSessionDuringChangingIdentityFailsFutureAndPreservesPreviousIdentity()
        throws Exception {

      var transport = new ScriptedTransport();
      OpcUaClient client = newClient(transport, AnonymousProvider.INSTANCE);
      var listener = new RecordingActivityListener();
      client.addSessionActivityListener(listener);

      try {
        connectToActive(client, transport);
        assertTrue(listener.awaitActiveCount(1, TIMEOUT_SECONDS));

        transport.parkNextActivateSession();
        CompletableFuture<OpcUaSession> changeFuture =
            client.changeIdentityAsync(new UsernameProvider("user1", "password"));
        PendingRequest changeReq = transport.awaitRequest(ActivateSessionRequest.class);
        awaitState(client, State.ChangingIdentity);

        client.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        assertFailedWithStatus(changeFuture, StatusCodes.Bad_SessionClosed);
        awaitState(client, State.Inactive);
        assertTrue(
            listener.awaitInactiveCount(1, TIMEOUT_SECONDS),
            "leaving ChangingIdentity for Closing must fire onSessionInactive");
        assertEquals(0, listener.identityChangedCount.get());

        // Complete the old ActivateSessionResponse late; it must not promote user1 to sticky.
        changeReq.complete(activateResponse(StatusCode.GOOD, ByteString.of(new byte[] {7, 7})));

        // Reconnect and verify the new Session still uses the original Anonymous identity.
        transport.recordedActivateRequests.clear();
        connectToActive(client, transport);
        assertEquals(1, transport.recordedActivateRequests.size());
        UserIdentityToken token =
            (UserIdentityToken)
                transport
                    .recordedActivateRequests
                    .get(0)
                    .getUserIdentityToken()
                    .decode(client.getStaticEncodingContext());
        assertEquals("anonymous", token.getPolicyId());
        assertEquals(0, listener.identityChangedCount.get());
      } finally {
        transport.releaseAll();
        client.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      }
    }

    // ConnectionLost arriving while ChangingIdentity is in flight must fail the pending future
    // with Bad_ConnectionClosed, fire onSessionInactive, keep the previous identity for
    // reactivation, and prevent any late response from the aborted attempt from affecting a later
    // changeIdentity call.
    @Test
    void connectionLostDuringChangingIdentityAbortsAndIgnoresStaleCompletion() throws Exception {
      var transport = new ScriptedTransport();
      OpcUaClient client = newClient(transport, AnonymousProvider.INSTANCE);
      var listener = new RecordingActivityListener();
      client.addSessionActivityListener(listener);

      try {
        connectToActive(client, transport);
        assertTrue(listener.awaitActiveCount(1, TIMEOUT_SECONDS));

        // Attempt 1: parked in ChangingIdentity when ConnectionLost arrives.
        transport.parkNextActivateSession();
        CompletableFuture<OpcUaSession> attempt1 =
            client.changeIdentityAsync(new UsernameProvider("user1", "password"));
        PendingRequest attempt1Req = transport.awaitRequest(ActivateSessionRequest.class);
        awaitState(client, State.ChangingIdentity);

        transport.recordedActivateRequests.clear();
        transport.fireConnectionLost();

        assertFailedWithStatus(attempt1, StatusCodes.Bad_ConnectionClosed);
        assertTrue(
            listener.awaitInactiveCount(1, TIMEOUT_SECONDS),
            "leaving ChangingIdentity for ReactivatingWait must fire onSessionInactive");

        // Reactivation succeeds automatically after backoff and must use the previous Anonymous
        // identity, NOT user1.
        assertTrue(
            listener.awaitActiveCount(2, TIMEOUT_SECONDS),
            "session should reactivate back to Active");
        awaitState(client, State.Active);
        assertEquals(1, transport.recordedActivateRequests.size());
        UserIdentityToken reactivatedToken =
            (UserIdentityToken)
                transport
                    .recordedActivateRequests
                    .get(0)
                    .getUserIdentityToken()
                    .decode(client.getStaticEncodingContext());
        assertEquals(
            "anonymous",
            reactivatedToken.getPolicyId(),
            "reactivation after aborted changeIdentity must preserve previous identity");

        // Attempt 2: start a new changeIdentity and complete attempt 1's stale response late.
        transport.parkNextActivateSession();
        CompletableFuture<OpcUaSession> attempt2 =
            client.changeIdentityAsync(new UsernameProvider("user2", "password"));
        PendingRequest attempt2Req = transport.awaitRequest(ActivateSessionRequest.class);
        awaitState(client, State.ChangingIdentity);

        // Stale completion of attempt 1 must NOT finish attempt 2 or leave ChangingIdentity.
        attempt1Req.complete(activateResponse(StatusCode.GOOD, ByteString.of(new byte[] {1, 1})));
        assertEquals(
            State.ChangingIdentity,
            client.getSessionFsm().getState(),
            "stale ChangeIdentitySuccess from attempt 1 must not complete attempt 2");
        assertTrue(!attempt2.isDone(), "attempt 2 must still be waiting for its own response");

        attempt2Req.complete(activateResponse(StatusCode.GOOD, ByteString.of(new byte[] {2, 2})));
        attempt2.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        awaitState(client, State.Active);
        assertTrue(listener.awaitIdentityChangedCount(1, TIMEOUT_SECONDS));

        // Verify the token sent in attempt 2 was user2.
        ActivateSessionRequest req2 = (ActivateSessionRequest) attempt2Req.request;
        UserNameIdentityToken token2 =
            assertInstanceOf(
                UserNameIdentityToken.class,
                req2.getUserIdentityToken().decode(client.getStaticEncodingContext()));
        assertEquals("user2", token2.getUserName());
      } finally {
        transport.releaseAll();
        client.disconnectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      }
    }
  }

  // region Helpers

  private static OpcUaClient newClient(
      ScriptedTransport transport, IdentityProvider initialProvider) {
    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint())
            .setDiscoveryEndpoints(List.of())
            .setIdentityProvider(initialProvider)
            .setKeepAliveInterval(uint(60_000))
            .build();
    return new OpcUaClient(config, transport);
  }

  private static OpcUaSession connectToActive(OpcUaClient client, ScriptedTransport transport)
      throws Exception {
    client.connectAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    awaitState(client, State.Active);
    return client.getSessionAsync().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
  }

  private static void awaitState(OpcUaClient client, State expected) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
    while (System.nanoTime() < deadline) {
      if (client.getSessionFsm().getState() == expected) {
        return;
      }
      LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
    }
    assertEquals(expected, client.getSessionFsm().getState());
  }

  private static void assertFailedWithStatus(CompletableFuture<?> future, long expectedStatusCode) {
    ExecutionException ex =
        assertThrows(
            ExecutionException.class,
            () -> future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS),
            "expected failure with status 0x" + Long.toHexString(expectedStatusCode));
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
    var usernamePolicy =
        new UserTokenPolicy(
            "username", UserTokenType.UserName, null, null, SecurityPolicy.None.getUri());

    return new EndpointDescription(
        "opc.tcp://localhost:12685/test",
        server,
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[] {anonymousPolicy, usernamePolicy},
        Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
        ubyte(0));
  }

  private static ResponseHeader goodHeader() {
    return new ResponseHeader(
        DateTime.now(), uint(0), StatusCode.GOOD, DiagnosticInfo.NULL_VALUE, new String[0], null);
  }

  private static ActivateSessionResponse activateResponse(
      StatusCode serviceResult, ByteString serverNonce) {
    var header =
        new ResponseHeader(
            DateTime.now(), uint(0), serviceResult, DiagnosticInfo.NULL_VALUE, new String[0], null);
    return new ActivateSessionResponse(
        header, serverNonce, new StatusCode[0], new DiagnosticInfo[0]);
  }

  private record PendingRequest(
      UaRequestMessageType request, CompletableFuture<UaResponseMessageType> future) {
    void complete(UaResponseMessageType response) {
      future.complete(response);
    }

    void fail(Throwable ex) {
      future.completeExceptionally(ex);
    }
  }

  private static final class RecordingActivityListener implements SessionActivityListener {
    final AtomicInteger activeCount = new AtomicInteger();
    final AtomicInteger inactiveCount = new AtomicInteger();
    final AtomicInteger identityChangedCount = new AtomicInteger();
    final AtomicInteger attemptFailedCount = new AtomicInteger();

    @Override
    public void onSessionActive(UaSession session) {
      activeCount.incrementAndGet();
    }

    @Override
    public void onSessionInactive(UaSession session) {
      inactiveCount.incrementAndGet();
    }

    @Override
    public void onSessionIdentityChanged(UaSession session) {
      identityChangedCount.incrementAndGet();
    }

    @Override
    public void onSessionAttemptFailed(AttemptStep step, Throwable failure) {
      attemptFailedCount.incrementAndGet();
    }

    boolean awaitActiveCount(int expected, long timeoutSeconds) {
      return awaitCount(activeCount, expected, timeoutSeconds);
    }

    boolean awaitInactiveCount(int expected, long timeoutSeconds) {
      return awaitCount(inactiveCount, expected, timeoutSeconds);
    }

    boolean awaitIdentityChangedCount(int expected, long timeoutSeconds) {
      return awaitCount(identityChangedCount, expected, timeoutSeconds);
    }

    private static boolean awaitCount(AtomicInteger counter, int expected, long timeoutSeconds) {
      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(timeoutSeconds);
      while (System.nanoTime() < deadline) {
        if (counter.get() >= expected) {
          return true;
        }
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
      }
      return counter.get() >= expected;
    }
  }

  private static final class ScriptedTransport
      implements OpcClientTransport, ChannelStateObservable {

    private final OpcClientTransportConfig config =
        OpcTcpClientTransportConfig.newBuilder().build();
    private final List<TransitionListener> listeners = new CopyOnWriteArrayList<>();
    private final AtomicBoolean parkNextCreate = new AtomicBoolean();
    private final AtomicBoolean parkNextActivate = new AtomicBoolean();
    private final AtomicBoolean parkNextClose = new AtomicBoolean();
    private final BlockingQueue<PendingRequest> parkedRequests = new LinkedBlockingQueue<>();
    private final Queue<PendingRequest> allParked = new LinkedBlockingQueue<>();

    final List<UaRequestMessageType> sentRequests = new CopyOnWriteArrayList<>();
    final List<ActivateSessionRequest> recordedActivateRequests = new CopyOnWriteArrayList<>();
    final AtomicInteger removeTransitionListenerCalls = new AtomicInteger();
    private volatile ChannelState channelState = ChannelState.DISCONNECTED;

    void parkNextCreateSession() {
      parkNextCreate.set(true);
    }

    void parkNextActivateSession() {
      parkNextActivate.set(true);
    }

    void parkNextCloseSession() {
      parkNextClose.set(true);
    }

    PendingRequest awaitRequest(Class<? extends UaRequestMessageType> requestType)
        throws InterruptedException {
      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
      while (System.nanoTime() < deadline) {
        long remainingMillis =
            Math.max(1L, TimeUnit.NANOSECONDS.toMillis(deadline - System.nanoTime()));
        PendingRequest req = parkedRequests.poll(remainingMillis, TimeUnit.MILLISECONDS);
        if (req != null && requestType.isInstance(req.request)) {
          return req;
        }
      }
      throw new AssertionError("timed out waiting for parked " + requestType.getSimpleName());
    }

    void fireConnectionLost() {
      channelState = ChannelState.RECONNECTING;
      for (TransitionListener listener : listeners) {
        listener.onStateTransition(false);
      }
    }

    void releaseAll() {
      PendingRequest req;
      while ((req = allParked.poll()) != null) {
        req.future.completeExceptionally(new UaException(StatusCodes.Bad_ConnectionClosed));
      }
    }

    @Override
    public OpcClientTransportConfig getConfig() {
      return config;
    }

    @Override
    public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
      channelState = ChannelState.CONNECTED;
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<Unit> disconnect() {
      channelState = ChannelState.DISCONNECTED;
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      sentRequests.add(requestMessage);

      if (requestMessage instanceof CreateSessionRequest) {
        if (parkNextCreate.compareAndSet(true, false)) {
          return park(requestMessage);
        }
        return CompletableFuture.completedFuture(
            new CreateSessionResponse(
                goodHeader(),
                new NodeId(1, "session-1"),
                new NodeId(0, ByteString.of(new byte[] {1, 2, 3, 4})),
                60_000.0,
                ByteString.of(new byte[32]),
                ByteString.NULL_VALUE,
                new EndpointDescription[] {endpoint()},
                new SignedSoftwareCertificate[0],
                new SignatureData(null, null),
                uint(0)));
      }

      if (requestMessage instanceof ActivateSessionRequest asr) {
        recordedActivateRequests.add(asr);
        if (parkNextActivate.compareAndSet(true, false)) {
          return park(requestMessage);
        }
        return CompletableFuture.completedFuture(
            activateResponse(StatusCode.GOOD, ByteString.of(new byte[32])));
      }

      if (requestMessage instanceof CloseSessionRequest) {
        if (parkNextClose.compareAndSet(true, false)) {
          return park(requestMessage);
        }
        return CompletableFuture.completedFuture(new CloseSessionResponse(goodHeader()));
      }

      if (requestMessage instanceof ReadRequest rr) {
        int count = rr.getNodesToRead() != null ? rr.getNodesToRead().length : 0;
        DataValue[] values = new DataValue[count];
        for (int i = 0; i < count; i++) {
          values[i] = new DataValue(Variant.ofInt32(ServerState.Running.getValue()));
        }
        return CompletableFuture.completedFuture(
            new ReadResponse(goodHeader(), values, new DiagnosticInfo[0]));
      }

      return CompletableFuture.failedFuture(
          new UaException(StatusCodes.Bad_ServiceUnsupported, "unexpected: " + requestMessage));
    }

    private CompletableFuture<UaResponseMessageType> park(UaRequestMessageType request) {
      var future = new CompletableFuture<UaResponseMessageType>();
      var pending = new PendingRequest(request, future);
      allParked.add(pending);
      parkedRequests.add(pending);
      return future;
    }

    @Override
    public void addTransitionListener(TransitionListener listener) {
      listeners.add(listener);
    }

    @Override
    public void removeTransitionListener(TransitionListener listener) {
      removeTransitionListenerCalls.incrementAndGet();
      listeners.remove(listener);
    }

    @Override
    public ChannelState getChannelState() {
      return channelState;
    }
  }

  // endregion
}
