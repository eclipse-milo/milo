/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import org.eclipse.milo.opcua.sdk.client.session.State;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.SecureChannelHandshakeException;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpcUaClientDisconnectTest {

  private long savedTimeoutMillis;

  @BeforeEach
  void setUp() {
    savedTimeoutMillis = OpcUaClient.disconnectCloseSessionTimeoutMillis;
  }

  @AfterEach
  void tearDown() {
    OpcUaClient.disconnectCloseSessionTimeoutMillis = savedTimeoutMillis;
  }

  /**
   * Reproduces CR-002: when the SessionFsm is in a non-terminal state (Creating/Activating/...)
   * waiting on a request whose transport channel is hung, the CloseSession event is shelved
   * indefinitely. disconnectAsync() must still complete in bounded time so callers aren't
   * deadlocked.
   */
  @Test
  void disconnectAsyncCompletesEvenWhenSessionFsmCloseIsShelved() throws Exception {
    OpcUaClient.disconnectCloseSessionTimeoutMillis = 250L;

    HangingClientTransport transport = new HangingClientTransport();
    OpcUaClient client = newClient(transport);

    // Start connect: transport.connect() succeeds, then sessionFsm.openSession() fires
    // CreateSessionRequest via sendRequestMessage(), which never completes.
    CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();

    assertTrue(
        transport.createSessionSent.await(5, TimeUnit.SECONDS),
        "SessionFsm should reach Creating and call sendRequestMessage");

    long startNanos = System.nanoTime();
    client.disconnectAsync().get(5, TimeUnit.SECONDS);
    long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);

    assertTrue(
        elapsedMillis < 3_000L,
        "disconnectAsync must complete in bounded time despite shelved CloseSession; took "
            + elapsedMillis
            + "ms");

    // Free the dangling futures so JUnit doesn't leak threads.
    transport.releasePending();
    connectFuture.completeExceptionally(new RuntimeException("test cleanup"));
  }

  // Regression test for #2213: disconnecting while transport.connect() is still pending must
  // prevent connectAsync()'s completion handler from opening a Session after transport.disconnect()
  // fails the pending connect future, leaving SessionFsm Inactive and ready for a later connect.
  @Test
  void disconnectDuringPendingConnectCancelsSessionOpenWhenTransportFails() throws Exception {
    OpcUaClient.disconnectCloseSessionTimeoutMillis = 250L;

    var transport = new ControllableClientTransport(true);
    OpcUaClient client = newClient(transport);

    CompletableFuture<OpcUaClient> firstConnect = client.connectAsync();
    assertEquals(1, transport.connectCalls.get());

    client.disconnectAsync().get(5, TimeUnit.SECONDS);

    assertFailedWithStatus(firstConnect, StatusCodes.Bad_ConnectionClosed);
    assertEquals(
        0,
        transport.sendRequestCalls.get(),
        "overtaken connectAsync() must not invoke sessionFsm.openSession()");
    assertEquals(State.Inactive, client.getSessionFsm().getState());
    assertFailedWithStatus(client.getSessionAsync(), StatusCodes.Bad_SessionClosed);

    // Control: a subsequent connectAsync() proceeds normally and starts Session creation.
    CompletableFuture<OpcUaClient> secondConnect = client.connectAsync();
    assertEquals(2, transport.connectCalls.get());
    transport.completePendingConnect(Unit.VALUE);

    assertTrue(
        transport.firstRequestSent.await(5, TimeUnit.SECONDS),
        "subsequent connectAsync() should open a Session once transport connects");
    assertEquals(1, transport.sendRequestCalls.get());

    client.disconnectAsync().get(5, TimeUnit.SECONDS);
    assertThrows(ExecutionException.class, () -> secondConnect.get(5, TimeUnit.SECONDS));
    awaitState(
        client, State.Inactive, "control disconnectAsync() should settle SessionFsm to Inactive");
  }

  // Even when a transport's connect future completes successfully after disconnectAsync() has
  // already incremented the disconnect counter, connectAsync() must still fail with
  // Bad_ConnectionClosed and must never open a Session.
  @Test
  void disconnectDuringPendingConnectCancelsSessionOpenEvenWhenTransportSucceeds()
      throws Exception {
    var transport = new ControllableClientTransport(false);
    OpcUaClient client = newClient(transport);

    CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();

    client.disconnectAsync().get(5, TimeUnit.SECONDS);
    transport.completePendingConnect(Unit.VALUE);

    assertFailedWithStatus(connectFuture, StatusCodes.Bad_ConnectionClosed);
    assertEquals(
        0,
        transport.sendRequestCalls.get(),
        "late transport connect success after disconnectAsync() must not open a Session");
    assertEquals(State.Inactive, client.getSessionFsm().getState());
    assertFailedWithStatus(client.getSessionAsync(), StatusCodes.Bad_SessionClosed);
  }

  // When disconnectAsync() races with transport.connect() completing, lifecycleLock orders
  // openSession() and closeSession() so either openSession() is skipped or closeSession() is
  // queued immediately after it; in both orderings SessionFsm must settle in State.Inactive.
  @Test
  void concurrentDisconnectAtConnectCompletionAlwaysSettlesInactive() throws Exception {
    OpcUaClient.disconnectCloseSessionTimeoutMillis = 50L;

    ExecutorService racers = Executors.newFixedThreadPool(2);
    try {
      for (int i = 0; i < 25; i++) {
        var transport = new ControllableClientTransport(false);
        OpcUaClient client = newClient(transport);

        CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();
        var barrier = new CyclicBarrier(2);

        Future<?> completeConnect =
            racers.submit(
                () -> {
                  try {
                    barrier.await(5, TimeUnit.SECONDS);
                    transport.completePendingConnect(Unit.VALUE);
                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }
                });

        Future<CompletableFuture<OpcUaClient>> startDisconnect =
            racers.submit(
                () -> {
                  try {
                    barrier.await(5, TimeUnit.SECONDS);
                    return client.disconnectAsync();
                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }
                });

        completeConnect.get(5, TimeUnit.SECONDS);
        startDisconnect.get(5, TimeUnit.SECONDS).get(5, TimeUnit.SECONDS);

        // Wait for both the connect future and any in-flight SessionFsm close transition to finish
        // without issuing a second closeSession() that could mask a lost close.
        assertThrows(
            ExecutionException.class,
            () -> connectFuture.get(5, TimeUnit.SECONDS),
            "raced connectAsync() must fail rather than leaving an open Session");
        awaitState(client, State.Inactive, "iteration " + i + " must settle in State.Inactive");
        assertFailedWithStatus(client.getSessionAsync(), StatusCodes.Bad_SessionClosed);
      }
    } finally {
      racers.shutdownNow();
    }
  }

  // Reproduces the ReverseTcpClientTransport.completeHandshake() <-> disconnectAsync() lock
  // ordering: completing the transport connect future inline while holding an internal transport
  // lock concurrently with disconnectAsync() (which acquires that same transport lock inside
  // transport.disconnect()) must never deadlock because lifecycleLock is a strict leaf lock.
  @Test
  void disconnectConcurrentWithTransportCompletingUnderLockDoesNotDeadlock() throws Exception {
    OpcUaClient.disconnectCloseSessionTimeoutMillis = 50L;

    ExecutorService racers = Executors.newFixedThreadPool(2);
    try {
      for (int i = 0; i < 20; i++) {
        var transport = new LockHoldingCompletingTransport();
        OpcUaClient client = newClient(transport);

        CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();
        var barrier = new CyclicBarrier(2);

        Future<?> completeUnderLock =
            racers.submit(
                () -> {
                  try {
                    transport.completePendingConnectUnderLock(barrier);
                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }
                });

        Future<CompletableFuture<OpcUaClient>> startDisconnect =
            racers.submit(
                () -> {
                  try {
                    barrier.await(5, TimeUnit.SECONDS);
                    return client.disconnectAsync();
                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }
                });

        completeUnderLock.get(5, TimeUnit.SECONDS);
        startDisconnect.get(5, TimeUnit.SECONDS).get(5, TimeUnit.SECONDS);
        assertThrows(
            ExecutionException.class,
            () -> connectFuture.get(5, TimeUnit.SECONDS),
            "raced connectAsync() must settle without deadlocking");
        awaitState(client, State.Inactive, "iteration " + i + " must settle in State.Inactive");
        assertFailedWithStatus(client.getSessionAsync(), StatusCodes.Bad_SessionClosed);
      }

      // Control: a subsequent connectAsync() on a disconnected transport proceeds normally.
      var transport = new LockHoldingCompletingTransport();
      OpcUaClient client = newClient(transport);
      CompletableFuture<OpcUaClient> firstConnect = client.connectAsync();
      client.disconnectAsync().get(5, TimeUnit.SECONDS);
      transport.completePendingConnectUnderLock(null);
      assertFailedWithStatus(firstConnect, StatusCodes.Bad_ConnectionClosed);

      CompletableFuture<OpcUaClient> secondConnect = client.connectAsync();
      transport.completePendingConnectUnderLock(null);
      assertTrue(
          transport.firstRequestSent.await(5, TimeUnit.SECONDS),
          "subsequent connectAsync() should proceed and open a Session");
      client.disconnectAsync().get(5, TimeUnit.SECONDS);
      assertThrows(ExecutionException.class, () -> secondConnect.get(5, TimeUnit.SECONDS));
      awaitState(client, State.Inactive, "control disconnect should settle in State.Inactive");
    } finally {
      racers.shutdownNow();
    }
  }

  // If a newer connectAsync() starts while an earlier disconnectAsync() is blocked on a shelved
  // CloseSession whose outstanding CreateSession only fails when transport.disconnect() runs,
  // the timed-out disconnect must still tear down the old transport to unblock CloseSession, and
  // the newer connectAsync() must wait for that teardown to complete before calling
  // transport.connect() so the earlier disconnect never tears down the newer transport.
  @Test
  void timedOutDisconnectTearsDownOldTransportAndReconnectAwaitsTeardown() throws Exception {
    OpcUaClient.disconnectCloseSessionTimeoutMillis = 250L;

    var transport = new ControllableClientTransport(false);
    OpcUaClient client = newClient(transport);

    CompletableFuture<OpcUaClient> firstConnect = client.connectAsync();
    transport.completePendingConnect(Unit.VALUE);
    assertTrue(
        transport.firstRequestSent.await(5, TimeUnit.SECONDS),
        "first connectAsync() should enter Creating");
    assertEquals(1, transport.connectCalls.get());
    assertEquals(1, transport.sendRequestCalls.get());

    // Hold transport.disconnect()'s completion so we can verify secondConnect waits for the
    // teardown future itself, even after transport.disconnect() fails the stalled CreateSession
    // and unblocks the shelved CloseSession.
    var disconnectCompletion = new CompletableFuture<Unit>();
    transport.setNextDisconnectFuture(disconnectCompletion);

    // Start disconnect #1: CloseSession is shelved in Creating because CreateSessionRequest is
    // stalled and will only fail when transport.disconnect() runs after the 250ms timeout.
    CompletableFuture<OpcUaClient> firstDisconnect = client.disconnectAsync();

    // Immediately start connect #2 before disconnect #1's timeout fires.
    CompletableFuture<OpcUaClient> secondConnect = client.connectAsync();
    assertEquals(1, transport.connectCalls.get());
    assertFalse(secondConnect.isDone());

    // The timed-out disconnect must still tear down the old transport rather than skipping it and
    // leaving secondConnect waiting forever behind the shelved CloseSession.
    assertTrue(
        transport.firstDisconnectStarted.await(5, TimeUnit.SECONDS),
        "timed-out disconnectAsync() must tear down old transport even after newer connectAsync()");
    assertEquals(1, transport.disconnectCalls.get());

    // Failing the stalled request inside transport.disconnect() unblocks the shelved CloseSession.
    awaitState(
        client,
        State.Inactive,
        "tearing down the old transport must unblock the shelved CloseSession to Inactive");
    assertThrows(ExecutionException.class, () -> firstConnect.get(5, TimeUnit.SECONDS));

    // secondConnect must still wait until transport.disconnect()'s returned future completes.
    assertEquals(
        1,
        transport.connectCalls.get(),
        "second connectAsync() must await old transport.disconnect() completion before connecting");
    assertFalse(secondConnect.isDone());

    disconnectCompletion.complete(Unit.VALUE);
    firstDisconnect.get(5, TimeUnit.SECONDS);

    // Now secondConnect proceeds on a clean transport and opens a fresh Session; the earlier
    // disconnect has already completed and cannot tear down secondConnect's transport.
    assertTrue(
        transport.secondConnectStarted.await(5, TimeUnit.SECONDS),
        "second connectAsync() should start transport.connect() after old teardown finishes");
    assertEquals(2, transport.connectCalls.get());
    assertEquals(1, transport.disconnectCalls.get());

    transport.completePendingConnect(Unit.VALUE);
    assertTrue(
        transport.secondRequestSent.await(5, TimeUnit.SECONDS),
        "second connectAsync() should send a fresh CreateSession");
    assertEquals(2, transport.sendRequestCalls.get());
    assertEquals(1, transport.disconnectCalls.get());

    // Cleanup.
    client.disconnectAsync().get(5, TimeUnit.SECONDS);
    assertEquals(2, transport.disconnectCalls.get());
    assertThrows(ExecutionException.class, () -> secondConnect.get(5, TimeUnit.SECONDS));
    awaitState(client, State.Inactive, "cleanup disconnect should settle in State.Inactive");
  }

  // If a newer connectAsync() starts while an earlier disconnectAsync()'s CloseSession is still
  // shelved in Creating, connectAsync() must wait for that session close and old transport
  // teardown to finish before starting its transport connect and Session open rather than joining
  // the dying Creating attempt.
  @Test
  void reconnectDuringShelvedCloseSessionWaitsForCloseBeforeStartingNewConnect() throws Exception {
    OpcUaClient.disconnectCloseSessionTimeoutMillis = 5_000L;

    var transport = new ControllableClientTransport(false);
    OpcUaClient client = newClient(transport);

    CompletableFuture<OpcUaClient> firstConnect = client.connectAsync();
    transport.completePendingConnect(Unit.VALUE);
    assertTrue(
        transport.firstRequestSent.await(5, TimeUnit.SECONDS),
        "first connectAsync() should reach Creating");
    assertEquals(1, transport.connectCalls.get());
    assertEquals(1, transport.sendRequestCalls.get());

    // disconnectAsync() shelves CloseSession in Creating.
    CompletableFuture<OpcUaClient> disconnectFuture = client.disconnectAsync();

    // A newer connectAsync() must wait behind pendingSessionClose rather than calling
    // transport.connect() or sessionFsm.openSession() while CloseSession is still pending.
    CompletableFuture<OpcUaClient> secondConnect = client.connectAsync();
    assertEquals(
        1,
        transport.connectCalls.get(),
        "second connectAsync() must wait for prior pendingSessionClose before transport.connect()");
    assertFalse(secondConnect.isDone());

    // Fail the first CreateSessionRequest so the shelved CloseSession finishes transitioning
    // SessionFsm to Inactive.
    transport.failPendingRequests(new UaException(StatusCodes.Bad_ConnectionClosed));

    disconnectFuture.get(5, TimeUnit.SECONDS);
    assertThrows(ExecutionException.class, () -> firstConnect.get(5, TimeUnit.SECONDS));

    // Now the earlier disconnect has completed both CloseSession and old transport teardown, and
    // secondConnect proceeds on the clean Inactive FSM without losing its new transport.
    assertTrue(
        transport.secondConnectStarted.await(5, TimeUnit.SECONDS),
        "second connectAsync() should start transport.connect() after prior disconnect finishes");
    assertEquals(2, transport.connectCalls.get());
    assertEquals(1, transport.disconnectCalls.get());

    transport.completePendingConnect(Unit.VALUE);
    assertTrue(
        transport.secondRequestSent.await(5, TimeUnit.SECONDS),
        "second connectAsync() should start a fresh CreateSession after prior CloseSession");
    assertEquals(2, transport.sendRequestCalls.get());
    assertEquals(1, transport.disconnectCalls.get());

    // Cleanup.
    OpcUaClient.disconnectCloseSessionTimeoutMillis = 250L;
    client.disconnectAsync().get(5, TimeUnit.SECONDS);
    assertThrows(ExecutionException.class, () -> secondConnect.get(5, TimeUnit.SECONDS));
    awaitState(client, State.Inactive, "cleanup disconnect should settle in State.Inactive");
  }

  // If transport.connect() throws synchronously during connectAsync(), the setup failure must
  // propagate to the caller without invoking sessionFsm.openSession() or sending any session
  // requests, and without leaking the connect barrier.
  @Test
  void synchronousTransportConnectFailurePropagatesWithoutOpeningSession() throws Exception {
    OpcUaClient.disconnectCloseSessionTimeoutMillis = 250L;

    var connectError = new IllegalStateException("transport setup failed");
    var transport = new ControllableClientTransport(false);
    transport.setNextConnectException(connectError);
    OpcUaClient client = newClient(transport);

    CompletableFuture<OpcUaClient> failedConnect = client.connectAsync();

    ExecutionException ex =
        assertThrows(
            ExecutionException.class,
            () -> failedConnect.get(5, TimeUnit.SECONDS),
            "synchronous transport.connect() failure must propagate");
    assertSame(connectError, ex.getCause());
    assertEquals(1, transport.connectCalls.get());
    assertEquals(
        0,
        transport.sendRequestCalls.get(),
        "synchronous transport.connect() failure must not start SessionFsm or send requests");
    assertEquals(State.Inactive, client.getSessionFsm().getState());
    assertFailedWithStatus(client.getSessionAsync(), StatusCodes.Bad_SessionClosed);

    // Control: connectBarrier completed in finally, so disconnectAsync() returns promptly and a
    // subsequent connectAsync() succeeds normally.
    client.disconnectAsync().get(5, TimeUnit.SECONDS);
    CompletableFuture<OpcUaClient> nextConnect = client.connectAsync();
    assertEquals(2, transport.connectCalls.get());
    transport.completePendingConnect(Unit.VALUE);
    assertTrue(
        transport.firstRequestSent.await(5, TimeUnit.SECONDS),
        "subsequent connectAsync() should proceed normally after synchronous setup failure");
    assertEquals(1, transport.sendRequestCalls.get());

    client.disconnectAsync().get(5, TimeUnit.SECONDS);
    assertThrows(ExecutionException.class, () -> nextConnect.get(5, TimeUnit.SECONDS));
    awaitState(client, State.Inactive, "cleanup disconnect should settle in State.Inactive");
  }

  // If a reconnect runs concurrently with disconnectAsync() while an EndpointResolver refresh from
  // the old connection is in flight, disconnectAsync() must still cancel the in-flight refresh and
  // advance EndpointRefresh's generation before releasing the reconnect so a late resolver result
  // from the old connection cannot replace the endpoint used by the reconnect.
  @Test
  void concurrentReconnectDoesNotPreventDisconnectFromInvalidatingInFlightEndpointRefresh()
      throws Exception {

    OpcUaClient.disconnectCloseSessionTimeoutMillis = 250L;

    EndpointDescription initialEndpoint = securedEndpoint(new byte[] {1, 2, 3, 4});
    EndpointDescription staleRotatedEndpoint = securedEndpoint(new byte[] {5, 6, 7, 8});
    EndpointDescription freshRotatedEndpoint = securedEndpoint(new byte[] {9, 10, 11, 12});

    ExecutorService racers = Executors.newFixedThreadPool(2);
    try {
      int reconnectAfterDisconnectCount = 0;

      for (int i = 0; i < 16; i++) {
        var cancelCalled = new AtomicBoolean(false);
        var resolveCalls = new AtomicInteger();
        var firstResolveStarted = new CountDownLatch(1);
        var secondResolveCompleted = new CountDownLatch(1);

        // Return a future that records cancel(true) without completing itself so we can deliver a
        // late result after reconnect has re-activated EndpointRefresh and assert that generation
        // invalidation still discards it.
        var staleResolveFuture =
            new CompletableFuture<EndpointConfiguration>() {
              @Override
              public boolean cancel(boolean mayInterruptIfRunning) {
                cancelCalled.set(true);
                return false;
              }
            };

        EndpointResolver resolver =
            () -> {
              if (resolveCalls.incrementAndGet() == 1) {
                firstResolveStarted.countDown();
                return staleResolveFuture;
              }
              secondResolveCompleted.countDown();
              return CompletableFuture.completedFuture(
                  new EndpointConfiguration(freshRotatedEndpoint, List.of(freshRotatedEndpoint)));
            };

        var transport = new ObservableControllableClientTransport();
        OpcUaClientConfig config =
            OpcUaClientConfig.builder()
                .setEndpoint(initialEndpoint)
                .setDiscoveryEndpoints(List.of(initialEndpoint))
                .setEndpointResolver(resolver)
                .build();
        var client = new OpcUaClient(config, transport);

        CompletableFuture<OpcUaClient> firstConnect = client.connectAsync();
        assertEquals(
            1,
            transport.connectCalls.get(),
            "iteration " + i + ": first connectAsync() should initiate transport.connect()");

        // Start an in-flight EndpointRefresh tied to the old connection while the transport is
        // attempting SecureChannel establishment.
        transport.fireConnectFailure(
            new SecureChannelHandshakeException(
                new UaException(StatusCodes.Bad_SecurityChecksFailed)));
        assertTrue(
            firstResolveStarted.await(5, TimeUnit.SECONDS),
            "iteration " + i + ": in-flight EndpointResolver should start");

        final CompletableFuture<OpcUaClient> disconnectFuture;
        final CompletableFuture<OpcUaClient> reconnectFuture;
        if (i == 0) {
          // Iteration 0 deterministically exercises disconnect followed immediately by reconnect.
          disconnectFuture = client.disconnectAsync();
          reconnectFuture = client.connectAsync();
        } else {
          // Iterations 1..15 race disconnectAsync() and connectAsync() across two threads.
          var barrier = new CyclicBarrier(2);
          Future<CompletableFuture<OpcUaClient>> disconnectTask =
              racers.submit(
                  () -> {
                    barrier.await(5, TimeUnit.SECONDS);
                    return client.disconnectAsync();
                  });
          Future<CompletableFuture<OpcUaClient>> reconnectTask =
              racers.submit(
                  () -> {
                    barrier.await(5, TimeUnit.SECONDS);
                    return client.connectAsync();
                  });

          disconnectFuture = disconnectTask.get(5, TimeUnit.SECONDS);
          reconnectFuture = reconnectTask.get(5, TimeUnit.SECONDS);
        }

        disconnectFuture.get(5, TimeUnit.SECONDS);
        firstConnect.handle((c, ex) -> Unit.VALUE).get(5, TimeUnit.SECONDS);

        // Wait until reconnect has either failed (if disconnect overtook it) or entered its own
        // transport.connect() and re-activated EndpointRefresh (if disconnect preceded it).
        long waitDeadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (System.nanoTime() < waitDeadline
            && !reconnectFuture.isDone()
            && transport.secondConnectStarted.getCount() > 0) {
          LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(5));
        }

        assertTrue(
            cancelCalled.get(),
            "iteration " + i + ": disconnectAsync() must cancel the old in-flight refresh");

        boolean reconnectStartedAfterDisconnect =
            !reconnectFuture.isDone() && transport.secondConnectStarted.getCount() == 0;

        // Complete the old resolver late; even when reconnect has already re-activated
        // EndpointRefresh, generation invalidation must discard the stale result.
        staleResolveFuture.complete(
            new EndpointConfiguration(staleRotatedEndpoint, List.of(staleRotatedEndpoint)));
        assertEquals(
            initialEndpoint.getServerCertificate(),
            transport.lastApplicationContext.get().getEndpoint().getServerCertificate(),
            "iteration " + i + ": stale resolver result must be discarded");

        if (reconnectStartedAfterDisconnect) {
          reconnectAfterDisconnectCount++;

          // Verify EndpointRefresh was cleanly restarted with reset cooldown so a new failure on
          // the reconnected attempt does refresh the endpoint.
          transport.fireConnectFailure(
              new SecureChannelHandshakeException(
                  new UaException(StatusCodes.Bad_SecurityChecksFailed)));
          assertTrue(
              secondResolveCompleted.await(5, TimeUnit.SECONDS),
              "iteration "
                  + i
                  + ": fresh handshake failure after reconnect should trigger refresh");

          long refreshDeadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
          while (System.nanoTime() < refreshDeadline
              && !freshRotatedEndpoint
                  .getServerCertificate()
                  .equals(
                      transport
                          .lastApplicationContext
                          .get()
                          .getEndpoint()
                          .getServerCertificate())) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(5));
          }
          assertEquals(
              freshRotatedEndpoint.getServerCertificate(),
              transport.lastApplicationContext.get().getEndpoint().getServerCertificate(),
              "iteration " + i + ": fresh refresh after reconnect must apply");

          client.disconnectAsync().get(5, TimeUnit.SECONDS);
          transport.completePendingConnect(Unit.VALUE);
        }

        reconnectFuture.handle((c, ex) -> Unit.VALUE).get(5, TimeUnit.SECONDS);
        awaitState(client, State.Inactive, "iteration " + i + " must settle in State.Inactive");
      }

      assertTrue(
          reconnectAfterDisconnectCount >= 1,
          "test must exercise disconnect-before-reconnect ordering");
    } finally {
      racers.shutdownNow();
    }
  }

  // If scheduling the disconnect closeSession timeout throws RejectedExecutionException (e.g., on
  // shutdown), disconnectAsync() must treat it as an immediate timeout, tear down the old
  // transport after priorConnectStart, and let closeSession settle its own barrier rather than
  // prematurely releasing both barriers before teardown.
  @Test
  void rejectedTimeoutSchedulingStillTearsDownTransportAndUnblocksReconnect() throws Exception {
    OpcUaClient.disconnectCloseSessionTimeoutMillis = 5_000L;

    ScheduledExecutorService shutdownScheduler = Executors.newSingleThreadScheduledExecutor();
    shutdownScheduler.shutdownNow();

    OpcClientTransportConfig transportConfig =
        OpcTcpClientTransportConfig.newBuilder().setScheduledExecutor(shutdownScheduler).build();
    var transport = new ControllableClientTransport(false, transportConfig);
    OpcUaClient client = newClient(transport);

    CompletableFuture<OpcUaClient> firstConnect = client.connectAsync();
    transport.completePendingConnect(Unit.VALUE);
    assertTrue(
        transport.firstRequestSent.await(5, TimeUnit.SECONDS),
        "first connectAsync() should reach Creating");

    var disconnectCompletion = new CompletableFuture<Unit>();
    transport.setNextDisconnectFuture(disconnectCompletion);

    // Scheduling the 5s timeout rejects immediately; disconnectAsync() must treat that as an
    // immediate timeout and call transport.disconnect() rather than skipping teardown or releasing
    // barriers early.
    CompletableFuture<OpcUaClient> disconnectFuture = client.disconnectAsync();
    CompletableFuture<OpcUaClient> secondConnect = client.connectAsync();

    assertTrue(
        transport.firstDisconnectStarted.await(2, TimeUnit.SECONDS),
        "scheduling rejection must trigger immediate transport.disconnect() teardown");
    assertEquals(1, transport.disconnectCalls.get());

    // Failing the stalled CreateSessionRequest inside transport.disconnect() lets the shelved
    // CloseSession settle its own barrier.
    awaitState(
        client,
        State.Inactive,
        "transport teardown must unblock the shelved CloseSession to Inactive");
    assertThrows(ExecutionException.class, () -> firstConnect.get(5, TimeUnit.SECONDS));

    // secondConnect must still wait until transport.disconnect()'s returned future completes.
    assertEquals(
        1,
        transport.connectCalls.get(),
        "second connectAsync() must wait for transportDisconnectBarrier before connecting");
    assertFalse(secondConnect.isDone());

    disconnectCompletion.complete(Unit.VALUE);
    disconnectFuture.get(5, TimeUnit.SECONDS);

    assertTrue(
        transport.secondConnectStarted.await(5, TimeUnit.SECONDS),
        "second connectAsync() should start once teardown finishes");
    assertEquals(2, transport.connectCalls.get());
    assertEquals(1, transport.disconnectCalls.get());

    transport.completePendingConnect(Unit.VALUE);
    assertTrue(
        transport.secondRequestSent.await(5, TimeUnit.SECONDS),
        "second connectAsync() should open a fresh Session");
    assertEquals(2, transport.sendRequestCalls.get());

    // Cleanup.
    client.disconnectAsync().get(5, TimeUnit.SECONDS);
    assertThrows(ExecutionException.class, () -> secondConnect.get(5, TimeUnit.SECONDS));
    awaitState(client, State.Inactive, "cleanup disconnect should settle in State.Inactive");
  }

  private static OpcUaClient newClient(OpcClientTransport transport) {
    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint())
            .setDiscoveryEndpoints(List.of())
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

  private static EndpointDescription securedEndpoint(byte[] serverCertificateBytes) {
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
        ByteString.of(serverCertificateBytes),
        MessageSecurityMode.SignAndEncrypt,
        SecurityPolicy.Basic256Sha256.getUri(),
        new UserTokenPolicy[] {anonymousPolicy},
        Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
        ubyte(0));
  }

  private static final class HangingClientTransport implements OpcClientTransport {

    private final OpcClientTransportConfig config =
        OpcTcpClientTransportConfig.newBuilder().build();
    private final CountDownLatch createSessionSent = new CountDownLatch(1);
    private final AtomicInteger disconnectCalls = new AtomicInteger();
    private final CompletableFuture<UaResponseMessageType> pendingResponse =
        new CompletableFuture<>();

    @Override
    public OpcClientTransportConfig getConfig() {
      return config;
    }

    @Override
    public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<Unit> disconnect() {
      disconnectCalls.incrementAndGet();
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      createSessionSent.countDown();
      return pendingResponse;
    }

    void releasePending() {
      pendingResponse.completeExceptionally(new RuntimeException("test cleanup"));
    }
  }

  private static class ControllableClientTransport implements OpcClientTransport {

    private final OpcClientTransportConfig config;
    private final boolean failPendingConnectOnDisconnect;
    final AtomicInteger connectCalls = new AtomicInteger();
    final AtomicInteger disconnectCalls = new AtomicInteger();
    final AtomicInteger sendRequestCalls = new AtomicInteger();
    final CountDownLatch firstDisconnectStarted = new CountDownLatch(1);
    final CountDownLatch secondConnectStarted = new CountDownLatch(1);
    final CountDownLatch firstRequestSent = new CountDownLatch(1);
    final CountDownLatch secondRequestSent = new CountDownLatch(1);
    final AtomicReference<ClientApplicationContext> lastApplicationContext =
        new AtomicReference<>();
    private final AtomicReference<CompletableFuture<Unit>> pendingConnect = new AtomicReference<>();
    private final Queue<CompletableFuture<Unit>> pendingConnects = new ConcurrentLinkedQueue<>();
    private final AtomicReference<CompletableFuture<Unit>> nextDisconnectFuture =
        new AtomicReference<>();
    private final AtomicReference<RuntimeException> nextConnectException = new AtomicReference<>();
    private final Queue<CompletableFuture<UaResponseMessageType>> pendingRequests =
        new ConcurrentLinkedQueue<>();
    volatile boolean disconnected;

    ControllableClientTransport(boolean failPendingConnectOnDisconnect) {
      this(failPendingConnectOnDisconnect, OpcTcpClientTransportConfig.newBuilder().build());
    }

    ControllableClientTransport(
        boolean failPendingConnectOnDisconnect, OpcClientTransportConfig config) {
      this.failPendingConnectOnDisconnect = failPendingConnectOnDisconnect;
      this.config = config;
    }

    void setNextDisconnectFuture(CompletableFuture<Unit> future) {
      nextDisconnectFuture.set(future);
    }

    void setNextConnectException(RuntimeException exception) {
      nextConnectException.set(exception);
    }

    @Override
    public OpcClientTransportConfig getConfig() {
      return config;
    }

    @Override
    public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
      lastApplicationContext.set(applicationContext);
      disconnected = false;
      int call = connectCalls.incrementAndGet();
      RuntimeException failure = nextConnectException.getAndSet(null);
      if (failure != null) {
        throw failure;
      }
      var future = new CompletableFuture<Unit>();
      pendingConnect.set(future);
      pendingConnects.add(future);
      if (call >= 2) {
        secondConnectStarted.countDown();
      }
      return future;
    }

    @Override
    public CompletableFuture<Unit> disconnect() {
      disconnectCalls.incrementAndGet();
      disconnected = true;

      if (failPendingConnectOnDisconnect) {
        CompletableFuture<Unit> connectFuture;
        while ((connectFuture = pendingConnects.poll()) != null) {
          connectFuture.completeExceptionally(new UaException(StatusCodes.Bad_ConnectionClosed));
        }
      }

      failPendingRequests(new UaException(StatusCodes.Bad_ConnectionClosed));
      firstDisconnectStarted.countDown();

      CompletableFuture<Unit> customDisconnect = nextDisconnectFuture.getAndSet(null);
      return customDisconnect != null
          ? customDisconnect
          : CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      int call = sendRequestCalls.incrementAndGet();

      CompletableFuture<UaResponseMessageType> response;
      if (disconnected) {
        response =
            CompletableFuture.failedFuture(new UaException(StatusCodes.Bad_ConnectionClosed));
      } else {
        response = new CompletableFuture<>();
        pendingRequests.add(response);
        if (disconnected) {
          response.completeExceptionally(new UaException(StatusCodes.Bad_ConnectionClosed));
        }
      }

      firstRequestSent.countDown();
      if (call >= 2) {
        secondRequestSent.countDown();
      }
      return response;
    }

    void failPendingRequests(Throwable failure) {
      CompletableFuture<UaResponseMessageType> request;
      while ((request = pendingRequests.poll()) != null) {
        request.completeExceptionally(failure);
      }
    }

    void completePendingConnect(Unit value) {
      CompletableFuture<Unit> connectFuture = pendingConnect.get();
      if (connectFuture != null) {
        connectFuture.complete(value);
      }
    }
  }

  private static final class ObservableControllableClientTransport
      extends ControllableClientTransport implements ChannelStateObservable {

    private final List<TransitionListener> listeners = new CopyOnWriteArrayList<>();

    ObservableControllableClientTransport() {
      super(true);
    }

    @Override
    public void addTransitionListener(TransitionListener listener) {
      listeners.add(listener);
    }

    @Override
    public void removeTransitionListener(TransitionListener listener) {
      listeners.remove(listener);
    }

    @Override
    public ChannelState getChannelState() {
      return disconnected ? ChannelState.DISCONNECTED : ChannelState.CONNECTED;
    }

    void fireConnectFailure(Throwable failure) {
      for (TransitionListener listener : listeners) {
        listener.onConnectFailure(failure);
      }
    }
  }

  private static final class LockHoldingCompletingTransport implements OpcClientTransport {

    private final Object transportLock = new Object();
    private final OpcClientTransportConfig config =
        OpcTcpClientTransportConfig.newBuilder().build();
    private final CountDownLatch firstRequestSent = new CountDownLatch(1);
    private final Queue<CompletableFuture<UaResponseMessageType>> pendingRequests =
        new ConcurrentLinkedQueue<>();
    private CompletableFuture<Unit> pendingConnect;
    private boolean disconnected;

    @Override
    public OpcClientTransportConfig getConfig() {
      return config;
    }

    @Override
    public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
      synchronized (transportLock) {
        disconnected = false;
        pendingConnect = new CompletableFuture<>();
        return pendingConnect;
      }
    }

    @Override
    public CompletableFuture<Unit> disconnect() {
      synchronized (transportLock) {
        disconnected = true;
      }

      CompletableFuture<UaResponseMessageType> request;
      while ((request = pendingRequests.poll()) != null) {
        request.completeExceptionally(new UaException(StatusCodes.Bad_ConnectionClosed));
      }

      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      CompletableFuture<UaResponseMessageType> response;
      synchronized (transportLock) {
        if (disconnected) {
          response =
              CompletableFuture.failedFuture(new UaException(StatusCodes.Bad_ConnectionClosed));
        } else {
          response = new CompletableFuture<>();
          pendingRequests.add(response);
        }
      }

      firstRequestSent.countDown();
      return response;
    }

    void completePendingConnectUnderLock(CyclicBarrier barrier) throws Exception {
      synchronized (transportLock) {
        if (barrier != null) {
          barrier.await(5, TimeUnit.SECONDS);
        }
        if (pendingConnect != null) {
          // Intentionally complete inline while holding transportLock, mirroring
          // ReverseTcpClientTransport.completeHandshake().
          pendingConnect.complete(Unit.VALUE);
        }
      }
    }
  }
}
