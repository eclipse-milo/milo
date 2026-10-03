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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The items sampled at one interval, and the cycle that samples them.
 *
 * <p>A subclass implements {@link #sample(List)}, the protocol read, delivers each value with
 * {@link #deliver(DataItem, DataValue)}, and gets the rest: scheduling on the server's executors,
 * membership, a debounced initial sample for new items, and a read access refresh before every
 * sample. Each cycle, in order, tells the subclass about a changed item set through {@link
 * #onItemsChanged(List)}, refreshes the read access result of every item with the group's {@link
 * ReadAccessPolicy}, and calls {@code sample} with the items whose Session may read them. An item
 * whose Session may not is left out, and its denial is already on the item.
 *
 * <p>{@link SamplingManager} creates groups through a {@link SamplingGroupFactory}, adds and
 * removes their items, and starts and stops them. {@link AddressSpaceSamplingGroup} is the group
 * that reads through an {@link org.eclipse.milo.opcua.sdk.server.AddressSpace}.
 *
 * <h2>Threading</h2>
 *
 * <p>{@code onItemsChanged} and {@code sample} run on the server's executor, one turn at a time: a
 * cycle or an initial sample holds the group's turn from its refresh until the stage its {@code
 * sample} returned completes, and whichever of the two becomes due meanwhile runs when the turn is
 * released, dispatched through the scheduler rather than on the thread that completed the stage. A
 * due cycle runs before a due initial sample, since it samples every item. The next cycle is timed
 * from the completion of its stage. {@link #onItemsAdded(List)} runs on the thread that added the
 * items, before they can be sampled; {@link #onItemsRemoved(List)} runs on the thread that removed
 * them, while a {@code sample} may still be in progress for them, so a subclass that keeps per-item
 * state guards it. A {@code sample} that completes after its items were removed, or after the group
 * was shut down, is harmless: every value passes through the item's read access gate, and a read
 * access result from a check that started before an item left the group, left and rejoined it, or
 * moved to another Session, is never applied to it.
 *
 * <p>An exception from {@code sample}, {@code onItemsChanged}, the refresh, or applying its results
 * is logged and does not stop the group; the turn is released and the next cycle is scheduled all
 * the same. A {@code sample} whose stage never completes, or a refresh or synchronous read that
 * blocks, is the one failure the group cannot recover from, so a turn that outlasts a configured
 * number of intervals is logged.
 */
public abstract class SamplingGroup {

  private static final CompletionStage<@Nullable Void> DONE =
      CompletableFuture.completedFuture(null);

  private final Logger logger = LoggerFactory.getLogger(getClass());

  /** Guards membership, the turn, the flags, and the timers. */
  private final Object lock = new Object();

  // Each member carries the token it joined with. A check snapshots the tokens, and its results
  // apply only where the token is unchanged, so an item that left, or left and rejoined, keeps the
  // result it got since.
  private final Map<DataItem, Long> items = new LinkedHashMap<>();
  private long nextToken = 1;

  private final Set<DataItem> pendingInitialSample = new LinkedHashSet<>();
  private boolean itemsChanged = false;

  // The turn: one refresh-and-sample at a time, held until its stage completes. A cycle or an
  // initial sample that becomes due while the turn is held runs when it is released.
  private boolean sampling = false;
  private boolean cycleDue = false;
  private boolean initialSampleDue = false;

  private @Nullable ScheduledFuture<?> nextCycle;
  private @Nullable ScheduledFuture<?> initialSampleTimer;
  private @Nullable ScheduledFuture<?> overrunWatchdog;
  private long initialSampleWindowStartNanos;

  private volatile boolean running = false;
  private boolean stopped = false;
  private volatile boolean overrunReported = false;
  private volatile int requestCount = 0;

  private volatile ReadAccessPolicy readAccessPolicy = ReadAccessPolicy.perCycle();
  private volatile long initialSampleDelayMillis = 100;
  private volatile long initialSampleMaxWindowMillis = 500;
  private volatile long overrunWarningMultiple = 3;

  private final OpcUaServer server;
  private final long intervalMillis;
  private final ExecutorService executor;
  private final ScheduledExecutorService scheduler;

  /**
   * Create a group that samples at {@code intervalMillis}.
   *
   * @param server the server whose executors run the cycle.
   * @param intervalMillis the sampling interval, in milliseconds.
   */
  protected SamplingGroup(OpcUaServer server, long intervalMillis) {
    this.server = server;
    this.intervalMillis = intervalMillis;

    executor = server.getExecutorService();
    scheduler = server.getScheduledExecutorService();
  }

  /**
   * Sample every one of {@code items} once and deliver each result with {@link #deliver(DataItem,
   * DataValue)}, synchronously or asynchronously.
   *
   * <p>Called on the server's executor at each interval, and for the initial sample of new items.
   * The items' Sessions may read them; the group has already refreshed and applied their read
   * access results. The returned stage completes when every item has been delivered or failed; the
   * group holds its turn and times the next cycle from that completion. Returning {@code null} is
   * treated as immediate completion.
   *
   * @param items the items to sample, never empty.
   * @return a stage that completes when every item has been delivered or failed.
   */
  protected abstract @Nullable CompletionStage<@Nullable Void> sample(List<DataItem> items);

  /**
   * The item set changed since the last call. Called on the executor before the next {@code
   * sample}, with the whole set, so a subclass that caches protocol requests can rebuild them.
   *
   * @param items every item in the group now.
   */
  protected void onItemsChanged(List<DataItem> items) {}

  /**
   * Items are being added to the group. Called on the thread that adds them, before they can be
   * seen by {@code onItemsChanged} or {@code sample}, so a subclass can acquire whatever the
   * protocol needs for them first. An exception is logged and the items are added anyway.
   *
   * @param items the items being added.
   */
  protected void onItemsAdded(List<DataItem> items) {}

  /**
   * Items were removed from the group, or the group was shut down. Called on the thread that
   * removed them; a {@code sample} may still be in progress for them.
   *
   * @param items the items that were removed.
   */
  protected void onItemsRemoved(List<DataItem> items) {}

  /**
   * Report how many protocol requests this group currently samples with, for {@link
   * SamplingManager#getGroups()}.
   *
   * @param requestCount the number of requests.
   */
  protected final void setRequestCount(int requestCount) {
    this.requestCount = requestCount;
  }

  /**
   * Deliver {@code value} to {@code item} with the timestamps the item asked for.
   *
   * <p>A sampler reads with both timestamps and lets this method keep only the ones the item's
   * {@link DataItem#getTimestampsToReturn()} asks for; a server timestamp, when asked for, is the
   * time of delivery. A value for an attribute other than Value never carries a source timestamp.
   * The value then goes through {@link DataItem#setValue}, where the item's read access result
   * applies as for any other delivery.
   *
   * @param item the item to deliver to.
   * @param value the value read for it, with whatever timestamps the read produced.
   */
  protected static void deliver(DataItem item, DataValue value) {
    TimestampsToReturn timestamps = item.getTimestampsToReturn();

    if (timestamps != null) {
      value =
          AttributeId.Value.isEqual(item.getReadValueId().getAttributeId())
              ? DataValue.derivedValue(value, timestamps)
              : DataValue.derivedNonValue(value, timestamps);
    }

    item.setValue(value);
  }

  /**
   * Get the server this group runs on.
   *
   * @return the server this group runs on.
   */
  public final OpcUaServer getServer() {
    return server;
  }

  /**
   * Get this group's sampling interval.
   *
   * @return this group's sampling interval, in milliseconds.
   */
  public final long getIntervalMillis() {
    return intervalMillis;
  }

  /**
   * Get a snapshot of the items in this group.
   *
   * @return a copy of the items in this group.
   */
  public final List<DataItem> getItems() {
    synchronized (lock) {
      return List.copyOf(items.keySet());
    }
  }

  /**
   * Get the number of protocol requests last reported with {@link #setRequestCount(int)}.
   *
   * @return the number of protocol requests, or zero if none was reported.
   */
  public final int getRequestCount() {
    return requestCount;
  }

  /**
   * Whether this group has been started and not yet shut down.
   *
   * @return {@code true} if this group is running.
   */
  public final boolean isRunning() {
    return running;
  }

  /**
   * Get a diagnostic snapshot of this group.
   *
   * @return this group's interval, item count, and request count.
   */
  public final SamplingGroupInfo getInfo() {
    synchronized (lock) {
      return new SamplingGroupInfo(intervalMillis, items.size(), requestCount);
    }
  }

  // region Managed by SamplingManager

  final void configure(SamplingManagerConfig config) {
    readAccessPolicy = config.readAccessPolicy();
    initialSampleDelayMillis = config.initialSampleDelayMillis();
    initialSampleMaxWindowMillis = config.initialSampleMaxWindowMillis();
    overrunWarningMultiple = config.overrunWarningMultiple();
  }

  final void addItems(List<DataItem> newItems) {
    var added = new ArrayList<DataItem>(newItems.size());

    synchronized (lock) {
      for (DataItem item : newItems) {
        if (!items.containsKey(item) && !added.contains(item)) {
          added.add(item);
        }
      }
    }

    if (added.isEmpty()) {
      return;
    }

    // The subclass sees the items before a cycle can, so whatever it acquires for them is in
    // place by the time they are sampled.
    try {
      onItemsAdded(added);
    } catch (Throwable t) {
      logger.error("onItemsAdded failed for the {} ms group", intervalMillis, t);
    }

    synchronized (lock) {
      for (DataItem item : added) {
        items.put(item, nextToken++);
      }
      itemsChanged = true;
      pendingInitialSample.addAll(added);

      if (running) {
        scheduleInitialSample();
      }
    }
  }

  final void removeItems(List<DataItem> removedItems) {
    var removed = new ArrayList<DataItem>(removedItems.size());

    synchronized (lock) {
      for (DataItem item : removedItems) {
        if (items.remove(item) != null) {
          removed.add(item);
          pendingInitialSample.remove(item);
        }
      }

      if (removed.isEmpty()) {
        return;
      }

      itemsChanged = true;
    }

    try {
      onItemsRemoved(removed);
    } catch (Throwable t) {
      logger.error("onItemsRemoved failed for the {} ms group", intervalMillis, t);
    }
  }

  final boolean isEmpty() {
    synchronized (lock) {
      return items.isEmpty();
    }
  }

  final void startup() {
    synchronized (lock) {
      if (running || stopped) {
        return;
      }
      running = true;

      nextCycle = scheduleCycle(intervalMillis);

      if (!pendingInitialSample.isEmpty()) {
        scheduleInitialSample();
      }
    }
  }

  /**
   * Stop the group without waiting for a turn in progress. The caller may hold locks a sample
   * needs, so this never blocks on sampling; a turn in progress applies no result and samples
   * nothing further once the membership is cleared, and its stage is ignored when it completes. A
   * group that never started is stopped too, and its items are reported removed all the same.
   */
  final void shutdown() {
    List<DataItem> removed;

    synchronized (lock) {
      if (stopped) {
        return;
      }
      stopped = true;
      running = false;

      if (nextCycle != null) {
        nextCycle.cancel(false);
        nextCycle = null;
      }
      cancelInitialSampleTimer();

      if (overrunWatchdog != null) {
        overrunWatchdog.cancel(false);
        overrunWatchdog = null;
      }

      cycleDue = false;
      initialSampleDue = false;

      removed = List.copyOf(items.keySet());
      items.clear();
      pendingInitialSample.clear();
    }

    if (!removed.isEmpty()) {
      try {
        onItemsRemoved(removed);
      } catch (Throwable t) {
        logger.error("onItemsRemoved failed for the {} ms group", intervalMillis, t);
      }
    }
  }

  // endregion

  // region Cycle

  private ScheduledFuture<?> scheduleCycle(long delayMillis) {
    return scheduler.schedule(
        () -> executor.execute(this::runCycle), delayMillis, TimeUnit.MILLISECONDS);
  }

  /** A cycle became due. Takes the turn, or waits for it to be released. */
  private void runCycle() {
    synchronized (lock) {
      if (!running) {
        return;
      }
      if (sampling) {
        cycleDue = true;
        return;
      }
      sampling = true;
    }

    cycle();
  }

  /** Caller holds the turn. */
  private void cycle() {
    long startNanos = System.nanoTime();

    runTurn(
        "Sampling",
        () -> {
          Map<DataItem, Long> snapshot;
          boolean changed;

          synchronized (lock) {
            snapshot = new LinkedHashMap<>(items);
            changed = itemsChanged;
            itemsChanged = false;

            // This cycle samples every item, so a pending initial sample has nothing left to do.
            pendingInitialSample.clear();
            initialSampleDue = false;
            cancelInitialSampleTimer();
          }

          return refreshAndSample(List.copyOf(snapshot.keySet()), changed, snapshot);
        },
        () -> scheduleNextCycle(startNanos));
  }

  /**
   * Run one turn: arm the watchdog, do the work, and when the stage it returned completes, run
   * {@code afterCompletion} and release the turn. Whatever the work throws, the turn is released.
   * Caller holds the turn.
   */
  private void runTurn(
      String what,
      Supplier<CompletionStage<@Nullable Void>> work,
      @Nullable Runnable afterCompletion) {

    long startNanos = System.nanoTime();

    // Armed before the work, so a refresh or a synchronous read that blocks is reported too.
    ScheduledFuture<?> watchdog = armOverrunWatchdog();

    CompletionStage<@Nullable Void> stage;
    try {
      stage = work.get();
    } catch (Throwable t) {
      stage = CompletableFuture.failedStage(t);
    }

    stage.whenComplete(
        (ignored, failure) -> {
          disarmOverrunWatchdog(watchdog);

          if (failure != null) {
            logger.warn("{} failed for the {} ms group", what, intervalMillis, failure);
          }

          if (overrunReported) {
            overrunReported = false;
            logger.info(
                "{} turn for the {} ms group completed after {} ms",
                what,
                intervalMillis,
                TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos));
          }

          try {
            if (afterCompletion != null) {
              afterCompletion.run();
            }
          } finally {
            releaseTurn();
          }
        });
  }

  private void scheduleNextCycle(long startNanos) {
    long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
    long delayMillis = nextDelayMillis(intervalMillis, elapsedMillis);

    synchronized (lock) {
      if (running) {
        nextCycle = scheduleCycle(delayMillis);
      }
    }
  }

  /**
   * Release the turn and hand it to whichever of a cycle or an initial sample became due meanwhile.
   * A due cycle goes first: it samples every item, pending ones included, so a steady stream of new
   * items cannot starve it.
   */
  private void releaseTurn() {
    Runnable next = null;

    synchronized (lock) {
      sampling = false;

      if (!running) {
        return;
      }

      if (cycleDue) {
        cycleDue = false;
        sampling = true;
        next = this::cycle;
      } else if (initialSampleDue) {
        initialSampleDue = false;
        sampling = true;
        next = this::initialSample;
      }
    }

    if (next != null) {
      Runnable step = next;

      // Through the scheduler, never inline: the handoff must not run on the thread that completed
      // the stage, and an executor that runs tasks on the calling thread must not recurse through
      // one handoff after another.
      scheduler.schedule(() -> executor.execute(step), 0, TimeUnit.MILLISECONDS);
    }
  }

  /**
   * Arm the overrun watchdog for a turn, unless the group has stopped, and keep it where shutdown
   * can cancel it, since a stage that never completes never would.
   */
  private @Nullable ScheduledFuture<?> armOverrunWatchdog() {
    synchronized (lock) {
      if (!running) {
        return null;
      }

      overrunWatchdog = scheduleOverrunWatchdog();

      return overrunWatchdog;
    }
  }

  private void disarmOverrunWatchdog(@Nullable ScheduledFuture<?> watchdog) {
    if (watchdog != null) {
      watchdog.cancel(false);

      synchronized (lock) {
        if (overrunWatchdog == watchdog) {
          overrunWatchdog = null;
        }
      }
    }
  }

  /** Caller holds the lock. */
  private ScheduledFuture<?> scheduleOverrunWatchdog() {
    long limitMillis = intervalMillis * overrunWarningMultiple;

    return scheduler.schedule(
        () -> {
          if (running) {
            overrunReported = true;
            logger.warn(
                "A sampling turn for the {} ms group has not completed after {} ms; the group"
                    + " cannot sample again until it does",
                intervalMillis,
                limitMillis);
          }
        },
        limitMillis,
        TimeUnit.MILLISECONDS);
  }

  /** The delay before the next cycle, so that cycles keep a fixed rate while they keep up. */
  static long nextDelayMillis(long intervalMillis, long elapsedMillis) {
    return Math.max(1, intervalMillis - elapsedMillis);
  }

  /**
   * Run the refresh-and-sample step for {@code toSample}, telling the subclass about a changed item
   * set first. Caller holds the turn.
   */
  private CompletionStage<@Nullable Void> refreshAndSample(
      List<DataItem> allItems, boolean changed, Map<DataItem, Long> toSample) {

    if (changed) {
      try {
        onItemsChanged(allItems);
      } catch (Throwable t) {
        logger.error("onItemsChanged failed for the {} ms group", intervalMillis, t);
      }
    }

    List<DataItem> readable;
    try {
      readable = refreshReadAccess(toSample);
    } catch (Throwable t) {
      return CompletableFuture.failedStage(t);
    }

    if (readable.isEmpty()) {
      return DONE;
    }

    try {
      CompletionStage<@Nullable Void> stage = sample(readable);

      return stage != null ? stage : DONE;
    } catch (Throwable t) {
      return CompletableFuture.failedStage(t);
    }
  }

  /**
   * Check the read access of each of {@code toRefresh}, apply the results to the items that are
   * still in the group with the token they were checked under and still on the Session they were
   * checked for, and return the ones whose Session may read them.
   */
  private List<DataItem> refreshReadAccess(Map<DataItem, Long> toRefresh) {
    List<DataItem> checked = List.copyOf(toRefresh.keySet());

    // Taken before the check, which resolves each item's Session itself. A transfer that moves an
    // item meanwhile applies the new Session's answer on its own, so this check's answer must not
    // land over it, and the item is not read this turn.
    var sessions = new HashMap<DataItem, Session>(checked.size());
    checked.forEach(item -> sessions.put(item, item.getSession()));

    Map<DataItem, AccessResult> results;
    try {
      results = readAccessPolicy.check(server, checked);
    } catch (Throwable t) {
      logger.warn("Read access check failed for the {} ms group", intervalMillis, t);
      results = Map.of();
    }

    var readable = new ArrayList<DataItem>(checked.size());

    // Applied under the lock and only to members whose token and Session are unchanged, so a
    // result from a check that started before an item left the group, even if it has since come
    // back, before it moved to another Session, or before shutdown, never lands over a newer one.
    synchronized (lock) {
      if (!running) {
        return readable;
      }

      for (DataItem item : checked) {
        if (!toRefresh.get(item).equals(items.get(item))
            || item.getSession() != sessions.get(item)) {
          continue;
        }

        AccessResult result = results.get(item);

        if (result != null) {
          item.setReadAccessResult(result);
        }

        AccessResult current = item.getReadAccessResult();

        if (current == null || !current.isDenied()) {
          readable.add(item);
        }
      }
    }

    return readable;
  }

  // endregion

  // region Initial sample

  /** Caller holds the lock. */
  private void scheduleInitialSample() {
    long nowNanos = System.nanoTime();

    if (initialSampleTimer == null) {
      initialSampleWindowStartNanos = nowNanos;
    } else {
      initialSampleTimer.cancel(false);
    }

    long windowRemainingMillis =
        initialSampleMaxWindowMillis
            - TimeUnit.NANOSECONDS.toMillis(nowNanos - initialSampleWindowStartNanos);

    long delayMillis = Math.max(0, Math.min(initialSampleDelayMillis, windowRemainingMillis));

    initialSampleTimer =
        scheduler.schedule(
            () -> executor.execute(this::runInitialSample), delayMillis, TimeUnit.MILLISECONDS);
  }

  /** Caller holds the lock. */
  private void cancelInitialSampleTimer() {
    if (initialSampleTimer != null) {
      initialSampleTimer.cancel(false);
      initialSampleTimer = null;
    }
  }

  /** An initial sample became due. Takes the turn, or waits for it to be released. */
  private void runInitialSample() {
    synchronized (lock) {
      initialSampleTimer = null;

      if (!running || pendingInitialSample.isEmpty()) {
        return;
      }
      if (sampling) {
        initialSampleDue = true;
        return;
      }
      sampling = true;
    }

    initialSample();
  }

  /** Caller holds the turn. */
  private void initialSample() {
    runTurn(
        "Initial sampling",
        () -> {
          List<DataItem> allItems;
          Map<DataItem, Long> toSample;
          boolean changed;

          synchronized (lock) {
            toSample = new LinkedHashMap<>();
            for (DataItem item : pendingInitialSample) {
              toSample.put(item, items.get(item));
            }
            pendingInitialSample.clear();

            if (toSample.isEmpty()) {
              // Nothing to sample, and the change flag stays for the next turn that samples.
              return DONE;
            }

            allItems = List.copyOf(items.keySet());
            changed = itemsChanged;
            itemsChanged = false;
          }

          return refreshAndSample(allItems, changed, toSample);
        },
        null);
  }

  // endregion
}
