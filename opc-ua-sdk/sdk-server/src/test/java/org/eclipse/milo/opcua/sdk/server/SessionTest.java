/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.server.diagnostics.ServerDiagnosticsSummary;
import org.eclipse.milo.opcua.sdk.server.identity.DefaultAnonymousIdentity;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.AnonymousIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Tests session lifecycle behavior that is local to {@link Session}. */
class SessionTest {

  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final ScheduledExecutorService scheduledExecutor = mock(ScheduledExecutorService.class);
  private final ScheduledFuture<?> timeoutFuture = mock(ScheduledFuture.class);

  /** The timeout checks the Session scheduled, in order, so a test can run one on demand. */
  private final List<Runnable> timeoutChecks = new ArrayList<>();

  private OpcUaServer server;

  @BeforeEach
  void setUp() {
    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getExecutor()).thenReturn(executor);

    server = mock(OpcUaServer.class);
    when(server.getConfig()).thenReturn(config);
    when(server.getScheduledExecutorService()).thenReturn(scheduledExecutor);
    when(server.getDiagnosticsSummary()).thenReturn(new ServerDiagnosticsSummary(server));
    doAnswer(
            invocation -> {
              timeoutChecks.add(invocation.getArgument(0));
              return timeoutFuture;
            })
        .when(scheduledExecutor)
        .schedule(any(Runnable.class), anyLong(), eq(TimeUnit.NANOSECONDS));
  }

  @AfterEach
  void tearDown() {
    executor.shutdownNow();
  }

  @Test
  void closeNotifiesLifecycleListenersOnce() {
    Session session = newSession(Duration.ofMinutes(1));

    AtomicInteger closed = new AtomicInteger();
    session.addLifecycleListener((s, subscriptionsDeleted) -> closed.incrementAndGet());

    session.close(false);
    session.close(false);

    assertEquals(1, closed.get());
    verify(timeoutFuture, times(1)).cancel(false);
  }

  // A component that holds Sessions it learned of from a listener needs to tell an open Session
  // from one that has closed since, without waiting for the close notification.
  @Test
  void isClosedFlipsWhenTheSessionIsClosed() {
    Session session = newSession(Duration.ofMinutes(1));
    assertFalse(session.isClosed(), "a Session is open until something closes it");

    session.close(false);

    assertTrue(session.isClosed());
  }

  @Nested
  class Timeouts {

    // Canceling the scheduled future cannot retract timeout work already handed to the executor.
    @Test
    void queuedTimeoutDoesNotRearmAfterNormalClose() {
      List<Runnable> timeoutWork = captureTimeoutWork();
      Session session = newSession(Duration.ofMinutes(1));

      timeoutChecks.get(0).run();
      assertEquals(1, timeoutWork.size());
      session.close(true);
      timeoutWork.get(0).run();

      assertTrue(session.isClosed());
      assertEquals(1, timeoutChecks.size(), "a closed Session must not schedule another timeout");
      verify(timeoutFuture).cancel(false);
    }

    // A normal close must not be reported as expiration when queued timeout work runs afterward.
    @Test
    void queuedExpiredTimeoutDoesNotCountAfterNormalClose() throws Exception {
      List<Runnable> timeoutWork = captureTimeoutWork();
      Session session = newSession(Duration.ofMinutes(1));
      expire(session);

      AtomicInteger notifications = new AtomicInteger();
      session.addLifecycleListener(
          (s, subscriptionsDeleted) -> {
            assertTrue(subscriptionsDeleted);
            notifications.incrementAndGet();
          });

      timeoutChecks.get(0).run();
      assertEquals(1, timeoutWork.size());
      session.close(true);
      timeoutWork.get(0).run();

      assertEquals(0, server.getDiagnosticsSummary().getSessionTimeoutCount().sum());
      assertEquals(1, notifications.get());
      assertEquals(1, timeoutChecks.size());
    }

    // Closing while a replacement timer is being scheduled must cancel that replacement too.
    @Test
    void closeCancelsTimeoutScheduledConcurrently() throws Exception {
      List<Runnable> timeoutWork = captureTimeoutWork();
      Session session = newSession(Duration.ofMinutes(1));
      timeoutChecks.get(0).run();

      ScheduledFuture<?> replacement = mock(ScheduledFuture.class);
      CountDownLatch scheduling = new CountDownLatch(1);
      CountDownLatch releaseSchedule = new CountDownLatch(1);
      doAnswer(
              invocation -> {
                scheduling.countDown();
                assertTrue(releaseSchedule.await(10, SECONDS));
                return replacement;
              })
          .when(scheduledExecutor)
          .schedule(any(Runnable.class), anyLong(), eq(TimeUnit.NANOSECONDS));

      Future<?> checking = executor.submit(timeoutWork.get(0));
      FutureTask<Void> closing =
          new FutureTask<>(
              () -> {
                session.close(true);
                return null;
              });
      Thread closeThread = new Thread(closing, "session-close-test");

      try {
        assertTrue(scheduling.await(10, SECONDS));
        closeThread.start();

        // Observe the close either finishing or waiting for timer publication before allowing
        // schedule() to return. Merely starting a thread would not establish the race ordering.
        long deadline = System.nanoTime() + SECONDS.toNanos(10);
        while (!closing.isDone()
            && !(closeThread.getState() == Thread.State.BLOCKED && !session.isClosed())
            && System.nanoTime() < deadline) {
          Thread.onSpinWait();
        }
        assertTrue(
            closing.isDone()
                || (closeThread.getState() == Thread.State.BLOCKED && !session.isClosed()),
            "close must complete or wait for the replacement timer");
      } finally {
        releaseSchedule.countDown();
      }

      checking.get(10, SECONDS);
      closing.get(10, SECONDS);
      closeThread.join(SECONDS.toMillis(10));

      assertTrue(session.isClosed());
      verify(replacement).cancel(false);
      assertEquals(0, server.getDiagnosticsSummary().getSessionTimeoutCount().sum());
    }

    // Expiration still owns one close and one diagnostic increment, even with duplicate queued
    // work.
    @Test
    void expirationClosesAndCountsExactlyOnce() throws Exception {
      List<Runnable> timeoutWork = captureTimeoutWork();
      Session session = newSession(Duration.ofMinutes(1));
      expire(session);

      AtomicInteger notifications = new AtomicInteger();
      session.addLifecycleListener(
          (s, subscriptionsDeleted) -> {
            assertTrue(s.isClosed());
            assertFalse(subscriptionsDeleted);
            notifications.incrementAndGet();
          });

      timeoutChecks.get(0).run();
      timeoutChecks.get(0).run();
      assertEquals(2, timeoutWork.size());
      timeoutWork.get(0).run();

      assertTrue(session.isClosed());
      assertEquals(1, server.getDiagnosticsSummary().getSessionTimeoutCount().sum());

      timeoutWork.get(1).run();

      assertEquals(1, server.getDiagnosticsSummary().getSessionTimeoutCount().sum());
      assertEquals(1, notifications.get());
      assertEquals(1, timeoutChecks.size());
      verify(timeoutFuture).cancel(false);
    }
  }

  // Application callbacks may wait for another thread that also attempts to close the Session.
  @Test
  void closeListenersRunOutsideTheTimeoutLock() {
    Session session = newSession(Duration.ofMinutes(1));
    session.addLifecycleListener(
        (s, subscriptionsDeleted) ->
            assertDoesNotThrow(() -> executor.submit(() -> s.close(false)).get(10, SECONDS)));

    session.close(true);

    assertTrue(session.isClosed());
  }

  // A component that checks access for a Session discards answers that straddle a change of what
  // they are made from. Both the identity and the endpoint feed them, so each moves the epoch.
  @Test
  void identityAndEndpointChangesEachMoveTheAccessEpoch() {
    Session session = newSession(Duration.ofMinutes(1));
    long initial = session.getAccessEpoch();

    session.setIdentity(new DefaultAnonymousIdentity(), new AnonymousIdentityToken("anonymous"));
    long afterIdentity = session.getAccessEpoch();

    session.setEndpoint(endpointDescription());
    long afterEndpoint = session.getAccessEpoch();

    assertNotEquals(initial, afterIdentity, "an identity change moves the epoch");
    assertNotEquals(afterIdentity, afterEndpoint, "an endpoint change moves the epoch");
  }

  private Session newSession(Duration sessionTimeout) {
    return new Session(
        server,
        new NodeId(1, "session"),
        "session",
        sessionTimeout,
        clientDescription(),
        "urn:eclipse:milo:test",
        uint(0),
        endpointDescription(),
        1L,
        new SecurityConfiguration(
            SecurityPolicy.None, MessageSecurityMode.None, null, null, null, null, null));
  }

  private List<Runnable> captureTimeoutWork() {
    List<Runnable> work = new ArrayList<>();
    ExecutorService queuedExecutor = mock(ExecutorService.class);
    doAnswer(
            invocation -> {
              work.add(invocation.getArgument(0));
              return null;
            })
        .when(queuedExecutor)
        .execute(any(Runnable.class));
    when(server.getConfig().getExecutor()).thenReturn(queuedExecutor);
    return work;
  }

  private static void expire(Session session) throws ReflectiveOperationException {
    Field lastActivity = Session.class.getDeclaredField("lastActivityNanos");
    lastActivity.setAccessible(true);
    lastActivity.setLong(session, System.nanoTime() - Duration.ofMinutes(2).toNanos());
  }

  private static ApplicationDescription clientDescription() {
    return new ApplicationDescription(
        "urn:eclipse:milo:test:client",
        "urn:eclipse:milo:test",
        LocalizedText.english("test client"),
        ApplicationType.Client,
        null,
        null,
        null);
  }

  private static EndpointDescription endpointDescription() {
    return new EndpointDescription(
        "opc.tcp://localhost:12685",
        clientDescription(),
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[0],
        TransportProfile.TCP_UASC_UABINARY.getUri(),
        ubyte(0));
  }
}
