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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingGroupTest.RecordingGroup;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingGroupTest.RecordingPolicy;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** How {@link SamplingManager} places items in groups and moves them. */
class SamplingManagerTest {

  private final ManualScheduler scheduler = new ManualScheduler();
  private final OpcUaServer server = mock(OpcUaServer.class);
  private final Session session = mock(Session.class);

  private final List<String> events = new CopyOnWriteArrayList<>();
  private final RecordingPolicy policy = new RecordingPolicy(events);
  private final Map<Long, RecordingGroup> groups = new ConcurrentHashMap<>();
  private final SamplingGroupFactory factory =
      (s, intervalMillis) -> {
        var group = new RecordingGroup(s, intervalMillis, events);
        groups.put(intervalMillis, group);
        return group;
      };

  private SamplingManager manager;

  @BeforeEach
  void setUp() {
    when(server.getExecutorService()).thenReturn(scheduler.executor());
    when(server.getScheduledExecutorService()).thenReturn(scheduler.executor());

    manager = manager(SamplingManagerConfig.defaults());
    manager.startup();
  }

  private SamplingManager manager(SamplingManagerConfig config) {
    return new SamplingManager(server, factory, config.withReadAccessPolicy(policy));
  }

  // Groups are keyed on the bucketed interval, rounded up, so items whose intervals were not
  // revised by their AddressSpace still land in the supported interval at or above their own.
  @Test
  void itemsWithIntervalsInTheSameBucketShareAGroup() {
    manager.onDataItemsCreated(
        List.of(item("a", 100.0), item("b", 100.4), item("c", 124.0), item("d", 125.0)));

    assertEquals(
        List.of(new SamplingGroupInfo(100, 1, 0), new SamplingGroupInfo(125, 3, 0)),
        manager.getGroups());
  }

  // An interval of 0 asks for the fastest the server supports: the first bucket with the default
  // configuration, or the configured floor.
  @Test
  void aZeroIntervalSamplesAtTheConfiguredFloor() {
    manager.onDataItemsCreated(List.of(item("a", 0.0)));
    assertEquals(List.of(new SamplingGroupInfo(25, 1, 0)), manager.getGroups());

    SamplingManager floored =
        manager(SamplingManagerConfig.defaults().withMinimumIntervalMillis(200));
    floored.startup();
    floored.onDataItemsCreated(List.of(item("b", 0.0)));
    assertEquals(List.of(new SamplingGroupInfo(200, 1, 0)), floored.getGroups());
  }

  @Test
  void aChangedIntervalMovesTheItemToTheGroupForTheNewInterval() throws Exception {
    MonitoredDataItem item = item("a", 100.0);
    manager.onDataItemsCreated(List.of(item));
    RecordingGroup before = groups.get(100L);

    modifyInterval(item, 200.0);
    manager.onDataItemsModified(List.of(item));

    assertEquals(List.of(new SamplingGroupInfo(200, 1, 0)), manager.getGroups());
    assertEquals(List.of(List.of(item)), before.removed);
    assertFalse(before.isRunning(), "the group left empty is shut down");
    assertEquals(List.of(List.of(item)), groups.get(200L).added);
  }

  // Filter, queue size, and timestamps changes need no group change: the group reads them from
  // the item when it delivers a value.
  @Test
  void aModificationThatKeepsTheIntervalLeavesTheGroupAlone() throws Exception {
    MonitoredDataItem item = item("a", 100.0);
    manager.onDataItemsCreated(List.of(item));

    modifyInterval(item, 100.0);
    manager.onDataItemsModified(List.of(item));

    RecordingGroup group = groups.get(100L);
    assertEquals(List.of(), group.removed);
    assertEquals(List.of(List.of(item)), group.added, "added once, at creation");
    assertEquals(1, groups.size());
  }

  /**
   * Part 4 §7.23: a Disabled item is not sampled. It leaves its group, and when enabled again it
   * rejoins with an initial sample that refreshes its read access first, so access revoked while it
   * was parked is reported on resume and a value never slips through a stale allowed result. The
   * SDK applies no result while the item is Disabled, so nothing stale is queued on resume either.
   */
  @Test
  void aDisabledItemLeavesItsGroupAndReturnsWithARefreshedInitialSample() {
    MonitoredDataItem item = item("a", 100.0);
    manager.onDataItemsCreated(List.of(item));
    RecordingGroup before = groups.get(100L);

    item.setMonitoringMode(MonitoringMode.Disabled);
    manager.onMonitoringModeChanged(List.of(item));

    assertEquals(List.of(List.of(item)), before.removed);
    assertEquals(List.of(), manager.getGroups(), "the group left empty is gone");
    assertEquals(List.of(item), manager.getDataItems(), "the item is still managed");

    policy.results.put(item, AccessResult.DENIED_USER_ACCESS);
    item.setMonitoringMode(MonitoringMode.Reporting);
    manager.onMonitoringModeChanged(List.of(item));

    assertTrue(SamplingTestItems.drain(item).isEmpty(), "nothing is queued by the resume itself");

    scheduler.run(delay -> delay <= 100); // the initial sample

    RecordingGroup after = groups.get(100L);
    assertEquals(List.of(List.of(item)), policy.refreshes);
    assertEquals(List.of(), after.samples, "a denied item is not sampled");
    List<DataValue> queued = SamplingTestItems.drain(item);
    assertEquals(1, queued.size(), "the refresh reports the denial that applies now");
    assertEquals(StatusCodes.Bad_UserAccessDenied, queued.get(0).statusCode().getValue());
  }

  @Test
  void eventItemsInAMonitoringModeChangeAreIgnored() {
    MonitoredItem eventItem = mock(MonitoredItem.class);

    manager.onMonitoringModeChanged(List.of(eventItem));

    assertEquals(List.of(), manager.getGroups());
    assertEquals(List.of(), manager.getDataItems());
  }

  @Test
  void deletingTheLastItemShutsTheGroupDown() {
    MonitoredDataItem item = item("a", 100.0);
    manager.onDataItemsCreated(List.of(item));
    RecordingGroup group = groups.get(100L);

    manager.onDataItemsDeleted(List.of(item));

    assertEquals(List.of(), manager.getGroups());
    assertEquals(List.of(), manager.getDataItems());
    assertEquals(List.of(List.of(item)), group.removed);
    assertFalse(group.isRunning());
  }

  // The manager starts with its AddressSpace's registration, but a callback can still arrive first.
  // It is kept rather than refused, and its group starts with the manager.
  @Test
  void itemsCreatedBeforeStartupAreSampledOnceTheManagerStarts() {
    SamplingManager unstarted = manager(SamplingManagerConfig.defaults());
    MonitoredDataItem item = item("a", 100.0);

    unstarted.onDataItemsCreated(List.of(item));
    RecordingGroup group = groups.get(100L);
    assertFalse(group.isRunning());
    assertEquals(List.of(), scheduler.pendingDelays(), "nothing is scheduled before startup");

    unstarted.startup();

    assertTrue(group.isRunning());
    scheduler.run(delay -> delay <= 100); // the initial sample
    assertEquals(List.of(List.of(item)), group.samples);
  }

  // A startup that fails is not followed by a shutdown. When the scheduler refuses one group's
  // timer, the group that already started must not keep sampling with nothing left to stop it.
  @Test
  void aStartupThatFailsStopsTheGroupsThatStarted() {
    ScheduledExecutorService timers = spy(scheduler.executor());
    doThrow(new RejectedExecutionException("scheduler unavailable"))
        .when(timers)
        .schedule(any(Runnable.class), eq(200L), eq(TimeUnit.MILLISECONDS));
    when(server.getScheduledExecutorService()).thenReturn(timers);

    SamplingManager unstarted = manager(SamplingManagerConfig.defaults());
    MonitoredDataItem first = item("a", 100.0);
    MonitoredDataItem second = item("b", 200.0);
    unstarted.onDataItemsCreated(List.of(first, second));

    assertThrows(RejectedExecutionException.class, unstarted::startup);

    assertFalse(groups.get(100L).isRunning(), "the group that started is stopped again");
    assertEquals(List.of(List.of(first)), groups.get(100L).removed);
    assertEquals(List.of(List.of(second)), groups.get(200L).removed);
    assertEquals(List.of(), unstarted.getGroups());
  }

  @Test
  void callbacksAfterShutdownAreIgnored() {
    MonitoredDataItem item = item("a", 100.0);
    manager.onDataItemsCreated(List.of(item));
    RecordingGroup group = groups.get(100L);

    manager.shutdown();
    assertFalse(group.isRunning());
    assertEquals(List.of(List.of(item)), group.removed);

    manager.onDataItemsCreated(List.of(item("b", 100.0)));

    assertEquals(List.of(), manager.getGroups());
    assertEquals(1, groups.size(), "no group is created after shutdown");
  }

  /**
   * The cross-group form of PR #2076's cancelled-cycle finding: an item moves to another group
   * while its old group's check is in flight. The new group applies a denial; when the old check
   * returns with its stale allowance, it must not land on an item that is no longer the old
   * group's.
   */
  @Test
  void anItemMovedWhileItsOldGroupIsCheckingKeepsTheNewGroupsResult() throws Exception {
    ExecutorService pool = Executors.newCachedThreadPool();
    try {
      when(server.getExecutorService()).thenReturn(pool);
      MonitoredDataItem a = item("a", 100.0);
      MonitoredDataItem b = item("b", 100.0);
      policy.results.put(a, AccessResult.ALLOWED);
      policy.results.put(b, AccessResult.ALLOWED);

      var oldCheckStarted = new CountDownLatch(1);
      var releaseOldCheck = new CountDownLatch(1);
      var checks = new AtomicInteger();
      policy.beforeRefresh =
          () -> {
            // Only the first check, the old group's, blocks; the new group's runs concurrently.
            if (checks.getAndIncrement() == 0) {
              oldCheckStarted.countDown();
              assertTrue(releaseOldCheck.await(5, TimeUnit.SECONDS));
            }
          };

      manager.onDataItemsCreated(List.of(a, b));
      RecordingGroup oldGroup = groups.get(100L);

      scheduler.run(delay -> delay == 100); // the old group's cycle, which blocks in its check
      assertTrue(oldCheckStarted.await(5, TimeUnit.SECONDS));

      modifyInterval(a, 200.0);
      manager.onDataItemsModified(List.of(a));
      RecordingGroup newGroup = groups.get(200L);

      policy.results.put(a, AccessResult.DENIED_USER_ACCESS);
      scheduler.run(delay -> delay <= 100); // the new group's initial sample
      // The denial is what matters, and it is applied after the check returns, so wait for it.
      waitUntil(() -> a.getReadAccessResult() == AccessResult.DENIED_USER_ACCESS);

      releaseOldCheck.countDown();
      waitUntil(() -> !oldGroup.samples.isEmpty());

      assertEquals(
          AccessResult.DENIED_USER_ACCESS,
          a.getReadAccessResult(),
          "the old group's stale allowance does not land on the moved item");
      assertEquals(List.of(List.of(b)), oldGroup.samples, "and the old group does not sample it");
      assertEquals(List.of(), newGroup.samples, "the new group holds it as denied");
    } finally {
      pool.shutdownNow();
    }
  }

  /**
   * The round trip: the item leaves its group and comes back while the old check is still in
   * flight. Membership alone would say the result may apply; the token the item rejoined with says
   * it may not, so the denial it got meanwhile stands.
   */
  @Test
  void anItemMovedAwayAndBackWhileItsOldGroupIsCheckingKeepsTheNewerResult() throws Exception {
    ExecutorService pool = Executors.newCachedThreadPool();
    try {
      when(server.getExecutorService()).thenReturn(pool);
      MonitoredDataItem a = item("a", 100.0);
      MonitoredDataItem b = item("b", 100.0);
      policy.results.put(a, AccessResult.ALLOWED);
      policy.results.put(b, AccessResult.ALLOWED);

      var oldCheckStarted = new CountDownLatch(1);
      var releaseOldCheck = new CountDownLatch(1);
      var checks = new AtomicInteger();
      policy.beforeRefresh =
          () -> {
            if (checks.getAndIncrement() == 0) {
              oldCheckStarted.countDown();
              assertTrue(releaseOldCheck.await(5, TimeUnit.SECONDS));
            }
          };

      manager.onDataItemsCreated(List.of(a, b));
      RecordingGroup group = groups.get(100L);

      scheduler.run(delay -> delay == 100); // the group's cycle, which blocks in its check
      assertTrue(oldCheckStarted.await(5, TimeUnit.SECONDS));

      modifyInterval(a, 200.0);
      manager.onDataItemsModified(List.of(a));
      policy.results.put(a, AccessResult.DENIED_USER_ACCESS);
      scheduler.run(delay -> delay > 0 && delay <= 100); // the 200 ms group's initial sample
      waitUntil(() -> a.getReadAccessResult() == AccessResult.DENIED_USER_ACCESS);

      modifyInterval(a, 100.0);
      manager.onDataItemsModified(List.of(a)); // back in the group whose check is still open
      assertEquals(List.of(a, b).size(), group.getItems().size());

      releaseOldCheck.countDown();
      waitUntil(() -> !group.samples.isEmpty());

      assertEquals(
          AccessResult.DENIED_USER_ACCESS,
          a.getReadAccessResult(),
          "the stale allowance from before the round trip does not land");
      assertEquals(
          List.of(List.of(b)), group.samples, "and the rejoined item is not sampled by it");
    } finally {
      pool.shutdownNow();
    }
  }

  private static void waitUntil(BooleanSupplier condition) throws InterruptedException {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
    while (!condition.getAsBoolean()) {
      assertTrue(System.nanoTime() < deadline, "timed out waiting");
      //noinspection BusyWait
      Thread.sleep(10);
    }
  }

  private MonitoredDataItem item(String name, double samplingInterval) {
    return SamplingTestItems.item(server, session, name, samplingInterval);
  }

  private static void modifyInterval(MonitoredDataItem item, double samplingInterval)
      throws Exception {
    item.modify(
        TimestampsToReturn.Both,
        uint(item.getClientHandle()),
        samplingInterval,
        MonitoredDataItem.DEFAULT_FILTER,
        uint(item.getQueueSize()),
        item.isDiscardOldest());
  }
}
