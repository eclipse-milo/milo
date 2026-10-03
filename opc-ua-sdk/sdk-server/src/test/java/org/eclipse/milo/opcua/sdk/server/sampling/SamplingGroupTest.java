/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.sampling;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** The cycle {@link SamplingGroup} runs on behalf of a subclass. */
class SamplingGroupTest {

  private static final long INTERVAL = 1000;

  private final ManualScheduler scheduler = new ManualScheduler();
  private final OpcUaServer server = mock(OpcUaServer.class);
  private final Session session = mock(Session.class);

  private final List<String> events = new CopyOnWriteArrayList<>();
  private final RecordingPolicy policy = new RecordingPolicy(events);

  private MonitoredDataItem a;
  private MonitoredDataItem b;
  private RecordingGroup group;

  @BeforeEach
  void setUp() {
    when(server.getExecutorService()).thenReturn(scheduler.executor());
    when(server.getScheduledExecutorService()).thenReturn(scheduler.executor());

    a = SamplingTestItems.item(server, session, "a");
    b = SamplingTestItems.item(server, session, "b");

    group = new RecordingGroup(server, INTERVAL, events);
    group.configure(SamplingManagerConfig.defaults().withReadAccessPolicy(policy));
  }

  /**
   * Part 4 §5.13.2.1: the refresh runs before the sample, so a value produced by this cycle is
   * gated by this cycle's result, and an item the Session may not read is not read at all. Its
   * denial is already on the item.
   */
  @Test
  void aCycleRefreshesBeforeSamplingAndSamplesOnlyAllowedItems() {
    policy.results.put(b, AccessResult.DENIED_USER_ACCESS);
    group.addItems(List.of(a, b));
    group.startup();

    runCycle();

    assertEquals(List.of("changed", "refresh", "sample"), events);
    assertEquals(List.of(List.of(a, b)), policy.refreshes);
    assertEquals(List.of(List.of(a)), group.samples);
    assertEquals(AccessResult.DENIED_USER_ACCESS, b.getReadAccessResult());
  }

  // The debounced initial sample is a second path into sample. It gets the same refresh, for the
  // new items only, and samples only them, so a burst of CreateMonitoredItems does not resample
  // the whole group.
  @Test
  void anInitialSampleRefreshesFirstAndSamplesOnlyTheNewItems() {
    group.addItems(List.of(a));
    group.startup();
    runInitialSample();

    assertEquals(List.of("changed", "refresh", "sample"), events);
    assertEquals(List.of(List.of(a)), group.samples);

    group.addItems(List.of(b));
    runInitialSample();

    assertEquals(List.of(a, b), group.changes.get(1), "the subclass sees the whole set");
    assertEquals(List.of(b), policy.refreshes.get(1), "only the new item is refreshed");
    assertEquals(List.of(b), group.samples.get(1), "only the new item is sampled");
  }

  // New items arriving within the debounce window are sampled together, once.
  @Test
  void aBurstOfNewItemsGetsOneInitialSample() {
    group.startup();
    group.addItems(List.of(a));
    group.addItems(List.of(b));

    assertEquals(1, pendingInitialSamples(), "the second add reschedules rather than adds");

    runInitialSample();

    assertEquals(List.of(List.of(a, b)), group.samples);
  }

  // A cycle samples everything, so an initial sample still pending has nothing left to do.
  @Test
  void aCycleCancelsAPendingInitialSample() {
    group.addItems(List.of(a));
    group.startup();

    runCycle();

    assertEquals(0, pendingInitialSamples());
    assertEquals(List.of(List.of(a)), group.samples);
  }

  // Today a sampler that throws stops its group until the next rebuild. The group must schedule
  // its next cycle whatever sample did.
  @Test
  void aSampleThatThrowsDoesNotStopTheGroup() {
    group.sampler =
        items -> {
          throw new IllegalStateException("device unreachable");
        };
    group.addItems(List.of(a));
    group.startup();

    runCycle();

    assertEquals(1, pendingCycles(), "the next cycle is scheduled after a failed sample");

    group.sampler = items -> CompletableFuture.completedFuture(null);
    runCycle();

    assertEquals(2, group.samples.size());
  }

  // A fault in the check is not an access decision: items keep their last result and are sampled
  // according to it.
  @Test
  void aFailingRefreshSamplesWithTheLastResults() {
    b.setReadAccessResult(AccessResult.DENIED_USER_ACCESS);
    policy.failure = new IllegalStateException("address space unavailable");
    group.addItems(List.of(a, b));
    group.startup();

    runCycle();

    assertEquals(List.of(List.of(a)), group.samples);
    assertEquals(1, pendingCycles());
  }

  @Test
  void onItemsChangedRunsOnlyWhenTheSetChanged() {
    group.addItems(List.of(a, b));
    group.startup();

    runCycle();
    runCycle();
    assertEquals(List.of(List.of(a, b)), group.changes);

    group.removeItems(List.of(b));
    runCycle();
    assertEquals(List.of(List.of(a, b), List.of(a)), group.changes);
    assertEquals(List.of(List.of(b)), group.removed);
  }

  // The next cycle is timed from the completion of the stage sample returned, so a protocol that
  // delivers asynchronously is never asked to sample again while the last request is in flight.
  @Test
  void theNextCycleWaitsForTheSampleStage() {
    var stage = new CompletableFuture<@Nullable Void>();
    group.sampler = items -> stage;
    group.addItems(List.of(a));
    group.startup();

    runCycle();

    assertEquals(0, pendingCycles(), "no cycle is scheduled while the stage is open");
    assertEquals(1, pendingWatchdogs(), "a watchdog waits for the stage instead");

    stage.complete(null);

    assertEquals(1, pendingCycles());
    assertEquals(0, pendingWatchdogs());
  }

  @Test
  void shutdownStopsTheCycleAndReportsEveryItemRemoved() {
    group.addItems(List.of(a, b));
    group.startup();

    group.shutdown();

    assertEquals(List.of(List.of(a, b)), group.removed);
    assertEquals(List.of(), scheduler.pendingDelays(), "nothing is left scheduled");
    assertFalse(group.isRunning());
  }

  // A group created while its manager shuts down is stopped before it ever started. The subclass
  // still hears that its items are gone, so it can release what onItemsAdded acquired for them.
  @Test
  void shutdownOfAGroupThatNeverStartedReportsItsItemsRemoved() {
    group.addItems(List.of(a, b));

    group.shutdown();

    assertEquals(List.of(List.of(a, b)), group.removed);
    assertEquals(List.of(), scheduler.pendingDelays(), "nothing is left scheduled");
  }

  // Only a completing stage disarms the watchdog, so shutting down a group whose turn never
  // completes must cancel it, or it stays scheduled, holding the group, until its deadline.
  @Test
  void shutdownCancelsTheWatchdogOfATurnThatNeverCompletes() {
    group.sampler = items -> new CompletableFuture<@Nullable Void>();
    group.addItems(List.of(a));
    group.startup();
    runCycle();
    assertEquals(1, pendingWatchdogs());

    group.shutdown();

    assertEquals(List.of(), scheduler.pendingDelays(), "nothing is left scheduled");
  }

  /**
   * Overlapping checks for the same item would let an older result land after a newer one, which is
   * what PR #2076's sequence numbers existed for. The group avoids it by taking turns: an initial
   * sample due while a cycle's refresh is in flight waits for that cycle's sample call.
   */
  @Test
  void aCycleAndAnInitialSampleTakeTurnsAtRefreshAndSample() throws Exception {
    ExecutorService pool = Executors.newCachedThreadPool();
    try {
      when(server.getExecutorService()).thenReturn(pool);
      group = new RecordingGroup(server, INTERVAL, events);
      group.configure(SamplingManagerConfig.defaults().withReadAccessPolicy(policy));

      var refreshStarted = new CountDownLatch(1);
      var releaseRefresh = new CountDownLatch(1);
      var sampled = new CountDownLatch(2);
      policy.beforeRefresh =
          () -> {
            if (policy.refreshes.isEmpty()) {
              refreshStarted.countDown();
              assertTrue(releaseRefresh.await(5, TimeUnit.SECONDS));
            }
          };
      group.sampler =
          items -> {
            sampled.countDown();
            return CompletableFuture.completedFuture(null);
          };

      group.addItems(List.of(a));
      group.startup();
      scheduler.run(delay -> delay == INTERVAL); // the cycle starts and blocks in its refresh
      assertTrue(refreshStarted.await(5, TimeUnit.SECONDS));

      group.addItems(List.of(b));
      runInitialSample(); // due now, but the cycle holds the turn

      assertFalse(sampled.await(300, TimeUnit.MILLISECONDS));
      assertEquals(0, policy.refreshes.size(), "the initial sample has not refreshed yet");

      releaseRefresh.countDown();
      runHandoffsWhenScheduled(); // the cycle's completion hands the turn to the initial sample
      assertTrue(sampled.await(5, TimeUnit.SECONDS));

      assertEquals(List.of(List.of(a), List.of(b)), policy.refreshes);
      assertEquals(List.of("changed", "refresh", "sample", "changed", "refresh", "sample"), events);
    } finally {
      pool.shutdownNow();
    }
  }

  /**
   * The turn covers the stage, not just the call: a cycle that becomes due while an initial
   * sample's asynchronous stage is open waits for it, and an initial sample that becomes due while
   * a cycle's stage is open waits too. Otherwise a protocol that delivers asynchronously would be
   * asked for overlapping requests and could deliver values out of order.
   */
  @Test
  void aCycleWaitsForAnOpenInitialSampleStageAndTheOtherWayRound() {
    var initialStage = new CompletableFuture<@Nullable Void>();
    group.sampler = items -> initialStage;
    group.startup();
    group.addItems(List.of(a));

    runInitialSample();
    assertEquals(List.of(List.of(a)), group.samples);

    runCycle();
    assertEquals(1, group.samples.size(), "the cycle waits for the initial sample's stage");

    var cycleStage = new CompletableFuture<@Nullable Void>();
    group.sampler = items -> cycleStage;
    initialStage.complete(null);
    assertEquals(1, group.samples.size(), "the handoff goes through the scheduler, not inline");
    runHandoffs();
    assertEquals(2, group.samples.size(), "the cycle runs once the stage completes");

    group.addItems(List.of(b));
    runInitialSample();
    assertEquals(2, group.samples.size(), "the initial sample waits for the cycle's stage");

    group.sampler = items -> CompletableFuture.completedFuture(null);
    cycleStage.complete(null);
    assertEquals(1, pendingCycles(), "the next cycle is scheduled from the cycle's completion");
    runHandoffs();
    assertEquals(List.of(b), group.samples.get(2), "the initial sample runs once it completes");
  }

  // A due cycle samples every item, pending ones included, so it goes first when both are due;
  // otherwise a steady stream of new items could keep the existing ones from being sampled.
  @Test
  void aDueCycleGoesBeforeADueInitialSample() {
    var initialStage = new CompletableFuture<@Nullable Void>();
    group.sampler = items -> initialStage;
    group.startup();
    group.addItems(List.of(a));
    runInitialSample();

    group.addItems(List.of(b));
    runInitialSample(); // due, waiting for the turn
    runCycle(); // due too
    group.sampler = items -> CompletableFuture.completedFuture(null);

    initialStage.complete(null);
    runHandoffs();

    assertEquals(List.of(List.of(a), List.of(a, b)), group.samples, "the cycle took b with it");
    assertEquals(0, pendingInitialSamples());
    runHandoffs();
    assertEquals(2, group.samples.size(), "and no separate initial sample follows");
  }

  // The initial sample's turn is watched like a cycle's: a first debounced sample that blocks
  // would otherwise hold the turn forever with no warning.
  @Test
  void theOverrunWatchdogIsArmedForAnInitialSample() {
    var watchdogsAtSample = new CopyOnWriteArrayList<Integer>();
    group.sampler =
        items -> {
          watchdogsAtSample.add(pendingWatchdogs());
          return CompletableFuture.completedFuture(null);
        };
    group.startup();
    group.addItems(List.of(a));

    runInitialSample();

    assertEquals(List.of(1), watchdogsAtSample);
    assertEquals(0, pendingWatchdogs());
  }

  // An initial sample that was deferred and then finds nothing to do, because its items were
  // removed meanwhile, must leave the change flag for the next cycle, or a subclass's cached
  // requests would keep a membership that no longer exists.
  @Test
  void anEmptyDeferredInitialSampleLeavesTheChangeFlagForTheNextCycle() {
    var cycleStage = new CompletableFuture<@Nullable Void>();
    group.sampler = items -> cycleStage;
    group.addItems(List.of(a, b));
    group.startup();
    runCycle(); // holds the turn with an open stage

    MonitoredDataItem c = SamplingTestItems.item(server, session, "c");
    group.addItems(List.of(c));
    runInitialSample(); // due, deferred behind the cycle
    group.removeItems(List.of(b, c));

    group.sampler = items -> CompletableFuture.completedFuture(null);
    cycleStage.complete(null);
    runHandoffs(); // the deferred initial sample, with nothing left to sample
    assertEquals(1, group.samples.size());

    runCycle();
    assertEquals(List.of(List.of(a, b), List.of(a)), group.changes, "the next cycle sees [a]");
  }

  /**
   * A TransferSubscriptions that moves an item while its check is in flight applies the new
   * Session's answer itself. The answer checked for the old Session must not land over it, and the
   * item is not read this turn under a Session nobody checked it for.
   */
  @Test
  void aCheckOvertakenByATransferIsNotAppliedAndTheMovedItemIsNotSampled() {
    Session newSession = mock(Session.class);
    policy.results.put(a, AccessResult.ALLOWED);
    policy.beforeRefresh =
        () -> {
          a.setSession(newSession);
          a.setReadAccessResult(AccessResult.DENIED_SECURITY_MODE);
        };
    group.addItems(List.of(a, b));
    group.startup();

    runCycle();

    assertEquals(
        AccessResult.DENIED_SECURITY_MODE,
        a.getReadAccessResult(),
        "the answer for the new Session stands");
    assertEquals(List.of(List.of(b)), group.samples, "the moved item is not read this turn");
  }

  // Applying a result can throw, for example from a DataItem implementation outside the SDK. The
  // turn must still be released and the next cycle scheduled, or the group stops for good.
  @Test
  void aFailureWhileApplyingAResultReleasesTheTurn() {
    DataItem failing = mock(DataItem.class);
    when(failing.getSession()).thenReturn(session);
    when(failing.getReadValueId()).thenReturn(a.getReadValueId());
    when(failing.getSamplingInterval()).thenReturn(100.0);
    when(failing.isSamplingEnabled()).thenReturn(true);
    doThrow(new IllegalStateException("cannot apply")).when(failing).setReadAccessResult(any());
    policy.results.put(failing, AccessResult.ALLOWED);

    group.addItems(List.of(failing));
    group.startup();

    runCycle();

    assertEquals(List.of(), group.samples, "the failed turn samples nothing");
    assertEquals(1, pendingCycles(), "but the next cycle is scheduled");
    assertEquals(0, pendingWatchdogs(), "and the watchdog is disarmed");

    policy.results.remove(failing);
    runCycle();
    assertEquals(1, group.samples.size(), "the group keeps going");
  }

  // A subclass acquires what the protocol needs for an item in onItemsAdded. A cycle that is due
  // while that runs must not see the item yet.
  @Test
  void anItemIsNotSampledBeforeOnItemsAddedReturns() throws Exception {
    ExecutorService pool = Executors.newCachedThreadPool();
    try {
      when(server.getExecutorService()).thenReturn(pool);
      group = new RecordingGroup(server, INTERVAL, events);
      group.configure(SamplingManagerConfig.defaults().withReadAccessPolicy(policy));

      var hookStarted = new CountDownLatch(1);
      var releaseHook = new CountDownLatch(1);
      var cycleSampled = new CountDownLatch(1);
      group.onItemsAddedHook =
          items -> {
            if (items.contains(b)) {
              hookStarted.countDown();
              assertTrue(releaseHook.await(5, TimeUnit.SECONDS));
            }
          };
      group.sampler =
          items -> {
            cycleSampled.countDown();
            return CompletableFuture.completedFuture(null);
          };

      group.addItems(List.of(a));
      group.startup();

      Future<?> adding = pool.submit(() -> group.addItems(List.of(b)));
      assertTrue(hookStarted.await(5, TimeUnit.SECONDS));

      runCycle(); // due while the add hook for b is still running
      assertTrue(cycleSampled.await(5, TimeUnit.SECONDS));
      assertEquals(List.of(List.of(a)), group.samples, "b is not visible until its hook returns");

      releaseHook.countDown();
      adding.get(5, TimeUnit.SECONDS);
      assertEquals(List.of(a, b), group.getItems());
      assertEquals(1, pendingInitialSamples(), "b gets its initial sample after the hook");
    } finally {
      pool.shutdownNow();
    }
  }

  // A synchronous read that blocks is the overrun the watchdog exists for, so it must be armed
  // before the read starts, not after it returns.
  @Test
  void theOverrunWatchdogIsArmedBeforeSampleRuns() {
    var watchdogsAtSample = new CopyOnWriteArrayList<Integer>();
    group.sampler =
        items -> {
          watchdogsAtSample.add(pendingWatchdogs());
          return CompletableFuture.completedFuture(null);
        };
    group.addItems(List.of(a));
    group.startup();

    runCycle();

    assertEquals(List.of(1), watchdogsAtSample);
    assertEquals(0, pendingWatchdogs(), "and disarmed once the stage completes");
  }

  @Test
  void theNextDelayKeepsAFixedRateUntilACycleIsOverdue() {
    assertEquals(75, SamplingGroup.nextDelayMillis(100, 25));
    assertEquals(1, SamplingGroup.nextDelayMillis(100, 100));
    assertEquals(1, SamplingGroup.nextDelayMillis(100, 125));
  }

  /**
   * Part 4 §7.40: a MonitoredItem's TimestampsToReturn decides which timestamps its Notifications
   * carry, and only the Value attribute has a source timestamp. A sampler reads with both and the
   * group keeps what the item asked for, so no protocol sampler has to know the rule.
   */
  @Test
  void deliverKeepsOnlyTheTimestampsTheItemAskedFor() throws Exception {
    MonitoredDataItem sourceOnly = SamplingTestItems.item(server, session, "s");
    sourceOnly.modify(
        TimestampsToReturn.Source,
        uint(1),
        100.0,
        MonitoredDataItem.DEFAULT_FILTER,
        uint(10),
        true);
    MonitoredDataItem nonValue =
        SamplingTestItems.item(server, session, "d", AttributeId.DisplayName, 100.0);

    var read = new DataValue(new Variant(1), StatusCode.GOOD, new DateTime(), new DateTime());

    SamplingGroup.deliver(sourceOnly, read);
    SamplingGroup.deliver(nonValue, read);

    DataValue sourceOnlyDelivered = SamplingTestItems.drain(sourceOnly).get(0);
    assertEquals(read.sourceTime(), sourceOnlyDelivered.sourceTime());
    assertNull(sourceOnlyDelivered.serverTime(), "Server timestamps were not asked for");

    DataValue nonValueDelivered = SamplingTestItems.drain(nonValue).get(0);
    assertNull(nonValueDelivered.sourceTime(), "a non-Value attribute has no source timestamp");
    assertNotNull(nonValueDelivered.serverTime(), "Both were asked for");
  }

  private void runCycle() {
    scheduler.run(delay -> delay > 500 && delay <= INTERVAL);
  }

  private void runInitialSample() {
    scheduler.run(SamplingGroupTest::isInitialSample);
  }

  /** Run a handoff of the turn, which the group dispatches through the scheduler with no delay. */
  private void runHandoffs() {
    scheduler.run(delay -> delay == 0);
  }

  /** Wait for a handoff to be scheduled by another thread, then run it. */
  private void runHandoffsWhenScheduled() throws InterruptedException {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
    while (!scheduler.pendingDelays().contains(0L)) {
      assertTrue(System.nanoTime() < deadline, "no handoff was scheduled");
      //noinspection BusyWait
      Thread.sleep(10);
    }
    runHandoffs();
  }

  private static boolean isInitialSample(long delay) {
    return delay > 0 && delay <= 100;
  }

  private int pendingCycles() {
    return (int) scheduler.pendingDelays().stream().filter(d -> d > 500 && d <= INTERVAL).count();
  }

  private int pendingInitialSamples() {
    return (int)
        scheduler.pendingDelays().stream().filter(SamplingGroupTest::isInitialSample).count();
  }

  private int pendingWatchdogs() {
    return (int) scheduler.pendingDelays().stream().filter(d -> d == 3 * INTERVAL).count();
  }

  /** Records what the group asks of its subclass, in order with the refreshes. */
  static final class RecordingGroup extends SamplingGroup {

    final List<List<DataItem>> samples = new CopyOnWriteArrayList<>();
    final List<List<DataItem>> changes = new CopyOnWriteArrayList<>();
    final List<List<DataItem>> added = new CopyOnWriteArrayList<>();
    final List<List<DataItem>> removed = new CopyOnWriteArrayList<>();
    final List<String> events;

    volatile Function<List<DataItem>, CompletionStage<@Nullable Void>> sampler =
        items -> CompletableFuture.completedFuture(null);
    volatile ThrowingConsumer<List<DataItem>> onItemsAddedHook = items -> {};

    RecordingGroup(OpcUaServer server, long intervalMillis, List<String> events) {
      super(server, intervalMillis);
      this.events = events;
    }

    @Override
    protected CompletionStage<@Nullable Void> sample(List<DataItem> items) {
      samples.add(List.copyOf(items));
      events.add("sample");
      return sampler.apply(items);
    }

    @Override
    protected void onItemsChanged(List<DataItem> items) {
      changes.add(List.copyOf(items));
      events.add("changed");
    }

    @Override
    protected void onItemsAdded(List<DataItem> items) {
      added.add(List.copyOf(items));
      try {
        onItemsAddedHook.accept(items);
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }

    @Override
    protected void onItemsRemoved(List<DataItem> items) {
      removed.add(List.copyOf(items));
    }
  }

  /** Applies the results a test configured, and records every refresh. */
  static final class RecordingPolicy implements ReadAccessPolicy {

    final List<List<DataItem>> refreshes = new CopyOnWriteArrayList<>();
    final Map<DataItem, AccessResult> results = new ConcurrentHashMap<>();
    final List<String> events;

    volatile @Nullable RuntimeException failure;
    volatile ThrowingRunnable beforeRefresh = () -> {};

    RecordingPolicy(List<String> events) {
      this.events = events;
    }

    @Override
    public Map<DataItem, AccessResult> check(OpcUaServer server, List<DataItem> items) {
      // The answers a check started with, as a real check reads the attributes when it runs.
      var answers = new HashMap<DataItem, AccessResult>();
      for (DataItem item : items) {
        AccessResult result = results.get(item);
        if (result != null) {
          answers.put(item, result);
        }
      }

      try {
        beforeRefresh.run();
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }

      refreshes.add(List.copyOf(items));
      events.add("refresh");

      RuntimeException toThrow = failure;
      if (toThrow != null) {
        throw toThrow;
      }

      return answers;
    }
  }

  interface ThrowingRunnable {
    void run() throws Exception;
  }

  interface ThrowingConsumer<T> {
    void accept(T value) throws Exception;
  }
}
