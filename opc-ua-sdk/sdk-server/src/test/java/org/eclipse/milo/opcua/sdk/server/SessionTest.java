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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
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
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AnonymousIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
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

  @Nested
  class BrowseContinuationPoints {

    /**
     * Part 4 §7.9: at capacity, allocating a continuation point for a new request must reclaim the
     * oldest continuation point from prior requests and keep at most MaxBrowseContinuationPoints
     * active (not limit + 1).
     */
    @Test
    void newRequestReclaimsOldestPriorContinuationPointAtLimit() {
      Session session = newSession(Duration.ofMinutes(1));
      ContinuationPoint cp1 = continuationPoint(1, 3, 1);
      ContinuationPoint cp2 = continuationPoint(2, 3, 1);
      ContinuationPoint cp3 = continuationPoint(3, 3, 1);

      assertTrue(session.addBrowseContinuationPoint(cp1, 2));
      assertTrue(session.addBrowseContinuationPoint(cp2, 2));
      assertEquals(2, session.getBrowseContinuationPoints().size());

      assertTrue(session.addBrowseContinuationPoint(cp3, 2));

      assertEquals(2, session.getBrowseContinuationPoints().size());
      assertEquals(
          List.of(cp2.id(), cp3.id()),
          List.copyOf(session.getBrowseContinuationPoints().keySet()),
          "oldest prior continuation point should be evicted while preserving insertion order");
    }

    /**
     * Part 4 §7.9: a single request may reclaim prior requests' continuation points up to the
     * session limit, but must never evict earlier operations from the same request; excess
     * operations are rejected instead.
     */
    @Test
    void singleRequestReclaimsPriorEntriesWithoutEvictingItsOwn() {
      Session session = newSession(Duration.ofMinutes(1));
      ContinuationPoint old1 = continuationPoint(1, 2, 1);
      ContinuationPoint old2 = continuationPoint(2, 2, 1);
      assertTrue(session.addBrowseContinuationPoint(old1, 2));
      assertTrue(session.addBrowseContinuationPoint(old2, 2));

      ContinuationPoint req1 = continuationPoint(10, 2, 1);
      ContinuationPoint req2 = continuationPoint(11, 2, 1);
      ContinuationPoint req3 = continuationPoint(12, 2, 1);

      int allocated = session.addBrowseContinuationPoints(List.of(req1, req2, req3), 2);

      assertEquals(2, allocated);
      assertEquals(
          List.of(req1.id(), req2.id()),
          List.copyOf(session.getBrowseContinuationPoints().keySet()),
          "prior entries should be evicted, while the third entry from the same request is not");
    }

    // Part 5 §6.3.2: MaxBrowseContinuationPoints == 0 means no restriction on parallel points.
    @Test
    void zeroLimitAllowsUnrestrictedContinuationPointsWithoutEviction() {
      Session session = newSession(Duration.ofMinutes(1));
      List<ContinuationPoint> points =
          List.of(
              continuationPoint(1, 2, 1),
              continuationPoint(2, 2, 1),
              continuationPoint(3, 2, 1),
              continuationPoint(4, 2, 1));

      assertEquals(4, session.addBrowseContinuationPoints(points, 0));
      assertEquals(4, session.getBrowseContinuationPoints().size());
      assertEquals(
          points.stream().map(ContinuationPoint::id).toList(),
          List.copyOf(session.getBrowseContinuationPoints().keySet()));
    }

    // If external mutation or a higher prior limit left more than max entries, a new allocation
    // trims the oldest prior entries so the session returns to within the configured limit.
    @Test
    void allocationTrimsExcessPreExistingEntriesDownToLimit() {
      Session session = newSession(Duration.ofMinutes(1));
      ContinuationPoint cp1 = continuationPoint(1, 2, 1);
      ContinuationPoint cp2 = continuationPoint(2, 2, 1);
      ContinuationPoint cp3 = continuationPoint(3, 2, 1);
      ContinuationPoint cp4 = continuationPoint(4, 2, 1);

      assertEquals(3, session.addBrowseContinuationPoints(List.of(cp1, cp2, cp3), 0));
      assertTrue(session.addBrowseContinuationPoint(cp4, 2));

      assertEquals(
          List.of(cp3.id(), cp4.id()), List.copyOf(session.getBrowseContinuationPoints().keySet()));
    }

    /**
     * Part 4 §7.9: continuing a multi-page operation must reuse its slot in place until the final
     * page exhausts it, without changing the slot count or requiring a new allocation.
     */
    @Test
    void nextUpdatesContinuationPointInPlaceUntilExhausted() {
      Session session = newSession(Duration.ofMinutes(1));
      ContinuationPoint cp1 = continuationPoint(1, 3, 1);
      ContinuationPoint cp2 = continuationPoint(2, 2, 1);

      assertEquals(2, session.addBrowseContinuationPoints(List.of(cp1, cp2), 2));

      Optional<ContinuationPoint.Page> page1 = session.nextBrowseContinuationPoint(cp1.id());
      assertTrue(page1.isPresent());
      assertEquals(cp1.id(), page1.get().continuationPoint());
      assertEquals(1, page1.get().references().size());
      assertEquals(
          List.of(cp1.id(), cp2.id()),
          List.copyOf(session.getBrowseContinuationPoints().keySet()),
          "in-place reuse preserves slot count and order");

      Optional<ContinuationPoint.Page> page2 = session.nextBrowseContinuationPoint(cp1.id());
      assertTrue(page2.isPresent());
      assertEquals(cp1.id(), page2.get().continuationPoint());
      assertEquals(1, page2.get().references().size());

      Optional<ContinuationPoint.Page> page3 = session.nextBrowseContinuationPoint(cp1.id());
      assertTrue(page3.isPresent());
      assertNull(page3.get().continuationPoint(), "final page clears continuation point");
      assertEquals(1, page3.get().references().size());
      assertEquals(
          List.of(cp2.id()),
          List.copyOf(session.getBrowseContinuationPoints().keySet()),
          "exhausted continuation point is removed without affecting sibling points");

      assertTrue(
          session.nextBrowseContinuationPoint(cp1.id()).isEmpty(),
          "exhausted continuation point must no longer resolve");
    }

    // Releasing an active continuation point removes it once; null or unknown ids return false.
    @Test
    void releaseRemovesActiveContinuationPointOnce() {
      Session session = newSession(Duration.ofMinutes(1));
      ContinuationPoint cp = continuationPoint(1, 2, 1);
      assertTrue(session.addBrowseContinuationPoint(cp, 2));

      assertFalse(session.releaseBrowseContinuationPoint(null));
      assertFalse(session.releaseBrowseContinuationPoint(ByteString.NULL_VALUE));
      assertTrue(session.releaseBrowseContinuationPoint(cp.id()));
      assertFalse(session.releaseBrowseContinuationPoint(cp.id()));
      assertTrue(session.getBrowseContinuationPoints().isEmpty());
    }

    // Part 4 §7.9: closing a Session frees all active continuation points and rejects new ones.
    @Test
    void closeClearsContinuationPointsAndRejectsFurtherOperations() {
      Session session = newSession(Duration.ofMinutes(1));
      ContinuationPoint cp1 = continuationPoint(1, 3, 1);
      ContinuationPoint cp2 = continuationPoint(2, 3, 1);
      assertEquals(2, session.addBrowseContinuationPoints(List.of(cp1, cp2), 2));

      session.close(false);

      assertTrue(session.getBrowseContinuationPoints().isEmpty());
      assertTrue(session.nextBrowseContinuationPoint(cp1.id()).isEmpty());
      assertFalse(session.addBrowseContinuationPoint(continuationPoint(3, 2, 1), 2));
      assertTrue(session.getBrowseContinuationPoints().isEmpty());
    }

    @Test
    void negativeLimitThrowsIllegalArgumentException() {
      Session session = newSession(Duration.ofMinutes(1));

      assertThrows(
          IllegalArgumentException.class,
          () -> session.addBrowseContinuationPoint(continuationPoint(1, 2, 1), -1));
    }

    // Batch validation must reject malformed or duplicate entries before evicting prior points or
    // partially mutating session state.
    @Test
    void invalidBatchDoesNotMutateExistingContinuationPoints() {
      Session session = newSession(Duration.ofMinutes(1));
      ContinuationPoint existing1 = continuationPoint(1, 2, 1);
      ContinuationPoint existing2 = continuationPoint(2, 2, 1);
      assertEquals(2, session.addBrowseContinuationPoints(List.of(existing1, existing2), 2));

      ContinuationPoint valid = continuationPoint(10, 2, 1);
      ContinuationPoint duplicateValid =
          new ContinuationPoint(valid.id(), valid.references(), valid.max());
      ContinuationPoint nullId =
          new ContinuationPoint(ByteString.NULL_VALUE, valid.references(), valid.max());
      ContinuationPoint emptyId =
          new ContinuationPoint(ByteString.of(new byte[0]), valid.references(), valid.max());

      assertThrows(
          NullPointerException.class,
          () -> session.addBrowseContinuationPoints(Arrays.asList(valid, null), 2));
      assertThrows(
          IllegalArgumentException.class,
          () -> session.addBrowseContinuationPoints(List.of(valid, nullId), 2));
      assertThrows(
          IllegalArgumentException.class,
          () -> session.addBrowseContinuationPoints(List.of(valid, emptyId), 2));
      assertThrows(
          IllegalArgumentException.class, () -> session.addBrowseContinuationPoint(emptyId, 2));
      assertThrows(
          IllegalArgumentException.class,
          () -> session.addBrowseContinuationPoints(List.of(valid, duplicateValid), 2));

      assertEquals(
          List.of(existing1.id(), existing2.id()),
          List.copyOf(session.getBrowseContinuationPoints().keySet()),
          "invalid batches must leave prior continuation points untouched");
    }

    // Slicing via ContinuationPoint.ofRemaining advances a cursor over one immutable list rather
    // than copying the remaining tail on every BrowseNext page.
    @Test
    void slicedContinuationPointPagesSequentiallyWithoutLosingEntries() {
      Session session = newSession(Duration.ofMinutes(1));
      int total = 500;
      ContinuationPoint template = continuationPoint(7, total, 1);
      List<ReferenceDescription> allRefs = template.references();

      ContinuationPoint cp = ContinuationPoint.ofRemaining(allRefs, 1, 1);
      assertTrue(session.addBrowseContinuationPoint(cp, 2));

      var collected = new ArrayList<ReferenceDescription>(total);
      collected.add(allRefs.get(0));

      ByteString handle = cp.id();
      while (handle != null) {
        Optional<ContinuationPoint.Page> page = session.nextBrowseContinuationPoint(handle);
        assertTrue(page.isPresent());
        assertEquals(1, page.get().references().size());
        collected.addAll(page.get().references());
        handle = page.get().continuationPoint();
      }

      assertEquals(allRefs, collected);
      assertTrue(session.getBrowseContinuationPoints().isEmpty());
    }

    /**
     * Browse allocation and BrowseNext continuation must share the synchronized map's monitor so
     * BrowseNext cannot temporarily drop a slot for a concurrent Browse to claim or exceed the cap.
     */
    @Test
    void concurrentBrowseAndBrowseNextNeverExceedLimitOrLosePages() throws Exception {
      Session session = newSession(Duration.ofMinutes(1));
      int maxContinuationPoints = 2;
      int iterations = 100;

      // Verify BrowseNext and allocation serialize on the same monitor exposed by
      // getBrowseContinuationPoints().
      ContinuationPoint guarded = continuationPoint(1, 2, 1);
      assertTrue(session.addBrowseContinuationPoint(guarded, maxContinuationPoints));

      CountDownLatch lockHeld = new CountDownLatch(1);
      CountDownLatch releaseLock = new CountDownLatch(1);
      ExecutorService pool = Executors.newFixedThreadPool(3);
      try {
        Future<?> holder =
            pool.submit(
                () -> {
                  synchronized (session.getBrowseContinuationPoints()) {
                    lockHeld.countDown();
                    try {
                      assertTrue(releaseLock.await(10, SECONDS));
                    } catch (InterruptedException e) {
                      Thread.currentThread().interrupt();
                      throw new AssertionError(e);
                    }
                  }
                });

        assertTrue(lockHeld.await(10, SECONDS));

        FutureTask<Optional<ContinuationPoint.Page>> blockedNext =
            new FutureTask<>(() -> session.nextBrowseContinuationPoint(guarded.id()));
        FutureTask<Boolean> blockedAdd =
            new FutureTask<>(
                () ->
                    session.addBrowseContinuationPoint(
                        continuationPoint(2, 2, 1), maxContinuationPoints));

        Thread nextThread = new Thread(blockedNext, "browse-next-lock-test");
        Thread addThread = new Thread(blockedAdd, "browse-add-lock-test");
        nextThread.start();
        addThread.start();

        long deadline = System.nanoTime() + SECONDS.toNanos(10);
        while ((nextThread.getState() != Thread.State.BLOCKED
                || addThread.getState() != Thread.State.BLOCKED)
            && !blockedNext.isDone()
            && !blockedAdd.isDone()
            && System.nanoTime() < deadline) {
          Thread.onSpinWait();
        }

        // Both callers must block on the monitor exposed by getBrowseContinuationPoints().
        assertEquals(Thread.State.BLOCKED, nextThread.getState());
        assertEquals(Thread.State.BLOCKED, addThread.getState());
        assertFalse(blockedNext.isDone());
        assertFalse(blockedAdd.isDone());

        releaseLock.countDown();
        holder.get(10, SECONDS);
        Optional<ContinuationPoint.Page> resumedPage = blockedNext.get(10, SECONDS);
        assertTrue(resumedPage.isPresent());
        assertEquals(guarded.references().subList(0, 1), resumedPage.get().references());
        assertTrue(blockedAdd.get(10, SECONDS));
        nextThread.join(SECONDS.toMillis(10));
        addThread.join(SECONDS.toMillis(10));

        // Now hammer concurrent Browse allocations against concurrent BrowseNext continuations
        // and verify the active count never exceeds maxContinuationPoints and every returned page
        // is strictly sequential until eviction or exhaustion.
        session.clearBrowseContinuationPoints();
        ContinuationPoint longLived = continuationPoint(100, iterations + 5, 1);
        List<ReferenceDescription> expectedLongLived = List.copyOf(longLived.references());
        assertTrue(session.addBrowseContinuationPoint(longLived, maxContinuationPoints));

        var startBarrier = new CyclicBarrier(2);
        AtomicInteger maxObservedSize = new AtomicInteger(0);
        List<ReferenceDescription> delivered = new ArrayList<>();

        Future<?> allocator =
            pool.submit(
                () -> {
                  try {
                    startBarrier.await(10, SECONDS);
                    for (int i = 0; i < iterations; i++) {
                      session.addBrowseContinuationPoint(
                          continuationPoint(200 + i, 2, 1), maxContinuationPoints);
                      int size = session.getBrowseContinuationPoints().size();
                      maxObservedSize.accumulateAndGet(size, Math::max);
                    }
                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }
                });

        Future<?> continuer =
            pool.submit(
                () -> {
                  try {
                    startBarrier.await(10, SECONDS);
                    boolean ended = false;
                    for (int i = 0; i < iterations; i++) {
                      Optional<ContinuationPoint.Page> page =
                          session.nextBrowseContinuationPoint(longLived.id());
                      if (ended) {
                        assertTrue(
                            page.isEmpty(),
                            "once evicted or exhausted, BrowseNext must keep returning empty");
                      } else if (page.isPresent()) {
                        delivered.addAll(page.get().references());
                        if (page.get().continuationPoint() == null) {
                          ended = true;
                        }
                      } else {
                        ended = true;
                      }
                      int size = session.getBrowseContinuationPoints().size();
                      maxObservedSize.accumulateAndGet(size, Math::max);
                    }
                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }
                });

        allocator.get(10, SECONDS);
        continuer.get(10, SECONDS);

        assertEquals(
            expectedLongLived.subList(0, delivered.size()),
            delivered,
            "every returned page prior to eviction must match the exact prefix in order");
        assertTrue(
            maxObservedSize.get() <= maxContinuationPoints,
            "observed active continuation points exceeded limit: " + maxObservedSize.get());
        assertTrue(session.getBrowseContinuationPoints().size() <= maxContinuationPoints);
      } finally {
        releaseLock.countDown();
        pool.shutdownNow();
        assertTrue(pool.awaitTermination(10, SECONDS));
      }
    }

    // Concurrent BrowseNext calls on the same continuation point must serialize cleanly so every
    // reference is returned exactly once and exactly one caller observes exhaustion.
    @Test
    void concurrentBrowseNextOnSameContinuationPointDeliversEveryReferenceOnce() throws Exception {
      Session session = newSession(Duration.ofMinutes(1));
      int total = 40;
      ContinuationPoint cp = continuationPoint(50, total, 1);
      List<ReferenceDescription> expected = List.copyOf(cp.references());
      assertTrue(session.addBrowseContinuationPoint(cp, 2));

      var barrier = new CyclicBarrier(2);
      List<ReferenceDescription> collected = Collections.synchronizedList(new ArrayList<>(total));
      AtomicInteger finalPagesSeen = new AtomicInteger(0);
      ExecutorService pool = Executors.newFixedThreadPool(2);

      try {
        Runnable drainer =
            () -> {
              try {
                barrier.await(10, SECONDS);
                while (true) {
                  Optional<ContinuationPoint.Page> page =
                      session.nextBrowseContinuationPoint(cp.id());
                  if (page.isEmpty()) {
                    break;
                  }
                  collected.addAll(page.get().references());
                  if (page.get().continuationPoint() == null) {
                    finalPagesSeen.incrementAndGet();
                  }
                }
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            };

        Future<?> t1 = pool.submit(drainer);
        Future<?> t2 = pool.submit(drainer);
        t1.get(10, SECONDS);
        t2.get(10, SECONDS);

        assertEquals(total, collected.size(), "every reference must be delivered once");
        assertEquals(
            new HashSet<>(expected),
            new HashSet<>(collected),
            "no duplicates or dropped references");
        assertEquals(1, finalPagesSeen.get(), "exactly one BrowseNext call observes exhaustion");
        assertTrue(session.getBrowseContinuationPoints().isEmpty());
      } finally {
        pool.shutdownNow();
        assertTrue(pool.awaitTermination(10, SECONDS));
      }
    }

    private ContinuationPoint continuationPoint(int idSeed, int referenceCount, int max) {
      var refs = new ArrayList<ReferenceDescription>(referenceCount);
      for (int i = 0; i < referenceCount; i++) {
        refs.add(
            new ReferenceDescription(
                NodeIds.Organizes,
                true,
                new NodeId(1, "node-" + idSeed + "-" + i).expanded(),
                new QualifiedName(1, "node-" + idSeed + "-" + i),
                LocalizedText.english("node-" + idSeed + "-" + i),
                NodeClass.Object,
                NodeIds.FolderType.expanded()));
      }
      return new ContinuationPoint(ByteString.of(new byte[] {(byte) idSeed}), refs, max);
    }
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
