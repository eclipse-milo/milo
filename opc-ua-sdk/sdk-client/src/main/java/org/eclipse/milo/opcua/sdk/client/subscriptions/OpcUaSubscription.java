/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.subscriptions;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.supplyAsyncCompose;

import com.google.common.primitives.Ints;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFieldList;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifySubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SetPublishingModeResponse;
import org.eclipse.milo.opcua.stack.core.util.Lazy;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.eclipse.milo.opcua.stack.core.util.TaskQueue;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Subscription on a Server, and the client-side object that represents it.
 *
 * <p>The two are not the same thing and do not have the same lifetime. A Subscription exists on the
 * Server from {@link #create()} until it is deleted, times out, or {@link #reset()} discards the
 * client's knowledge of it; this object exists for as long as the application holds it, and may
 * represent several Subscriptions in sequence.
 *
 * <p><b>Threading:</b> the lifecycle transitions that call a service — {@link #create()}, {@link
 * #modify()}, {@link #delete()} and {@link #setPublishingMode(boolean)} — are each a check of the
 * current state, a blocking service call, and an update of that state. They are serialized against
 * each other: one runs to completion before the next begins, and a second concurrent {@link
 * #create()} is answered {@code Bad_InvalidState} rather than creating a second Subscription on the
 * Server that no client object could then name or delete.
 *
 * <p>{@link #reset()} is <i>not</i> one of them. It only discards what the client knows, sends
 * nothing, and never waits for a transition that is waiting for the Server — because it is called
 * from places that must not be stopped for a network round trip: the Bad_Timeout
 * StatusChangeNotification path runs on this Subscription's delivery queue, and {@link
 * #notifyTransferFailed(StatusCode)} runs on the Session's state machine. A {@link #reset()} that
 * overtakes a transition already in flight wins: the transition's result is discarded when it
 * arrives rather than applied to a Subscription this object no longer represents.
 *
 * <p>None of this is serialized against the accessors or the parameter setters, which read and
 * write individual volatile fields and never block. MonitoredItem management is not serialized
 * either: it is concurrent with the lifecycle only in the sense that it reports {@code
 * Bad_InvalidState} when the Subscription is gone.
 */
public class OpcUaSubscription {

  private static final int DEFAULT_MAX_MONITORED_ITEMS_PER_CALL = 10000;
  private static final UInteger DEFAULT_MAX_NOTIFICATIONS_PER_PUBLISH = uint(65535);
  private static final UByte DEFAULT_PRIORITY = ubyte(0);
  private static final double DEFAULT_PUBLISHING_INTERVAL = 1000.0;
  private static final double DEFAULT_TARGET_KEEP_ALIVE_INTERVAL = 10000.0;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * Guards the lifecycle state — {@link #syncState}, {@link #serverState}, {@link #modifications},
   * {@link #transitionInFlight} and {@link #incarnation} — and serializes the lifecycle
   * transitions, each of which is a check-then-act around a blocking service call: without that,
   * two of them interleave and the Server ends up in a state no single client-side field describes
   * — most visibly a second Subscription created by a concurrent {@link #create()} whose
   * SubscriptionId is immediately overwritten, leaving it running on the Server with nothing left
   * that can name it.
   *
   * <p><b>Never held across a service call.</b> A transition claims {@link #transitionInFlight},
   * releases this lock, makes the call, then takes the lock again to apply the result. Holding it
   * for the round trip would make every other user of the lock — above all {@link #reset()}, which
   * is called from the delivery queue and from the Session's state machine — wait out someone
   * else's request timeout, and on a bounded transport executor it deadlocks: the thread that would
   * complete the response is one of the threads blocked on the lock.
   *
   * <p>The accessors and parameter setters do not take it and never block on it.
   */
  private final Object lifecycleLock = new Object();

  /**
   * {@code true} while a lifecycle transition holds the right to change the lifecycle state and is
   * waiting for the Server. Guarded by {@link #lifecycleLock}; every thread waiting on that monitor
   * is woken when it is cleared.
   *
   * <p>Deliberately not a {@link SyncState}: that enum is public API and describes what the client
   * knows about the Subscription, not what it is currently asking the Server for. {@link
   * #getSyncState()} therefore keeps reporting the state the Subscription had when the in-flight
   * call was made, which is exactly what is still true of it until the Server answers.
   */
  private boolean transitionInFlight = false;

  /**
   * Identifies the Subscription this object currently represents. Incremented by every {@link
   * #reset()}, i.e. whenever this object stops representing the Subscription it did.
   *
   * <p>A transition captures it while claiming {@link #transitionInFlight} and compares it again
   * when the response arrives: a change means a {@link #reset()} discarded the Subscription the
   * call was made for, so the result must not be applied to whatever this object holds now. For
   * {@link #create()} it also means the Subscription the Server just created belongs to nobody, and
   * has to be deleted rather than left running.
   *
   * <p>Guarded by {@link #lifecycleLock}.
   */
  private long incarnation = 0L;

  private volatile SyncState syncState = SyncState.INITIAL;
  private volatile @Nullable ServerState serverState;
  private volatile Modifications modifications;

  private volatile WatchdogTimer watchdogTimer;

  /** MonitoredItems added to this Subscription, by ClientHandle. */
  private final Map<UInteger, OpcUaMonitoredItem> monitoredItems = new ConcurrentHashMap<>();

  /**
   * MonitoredItems that have been removed from the Subscription and are pending deletion on the
   * Server.
   */
  private final Set<OpcUaMonitoredItem> itemsToDelete =
      Collections.newSetFromMap(new ConcurrentHashMap<>());

  private final ClientHandleSequence clientHandleSequence =
      new ClientHandleSequence(monitoredItems::containsKey);

  private volatile Double publishingInterval = DEFAULT_PUBLISHING_INTERVAL;
  private volatile UInteger maxKeepAliveCount =
      calculateMaxKeepAliveCount(publishingInterval, DEFAULT_TARGET_KEEP_ALIVE_INTERVAL);
  private volatile UInteger lifetimeCount = calculateLifetimeCount(maxKeepAliveCount);
  private volatile UInteger maxNotificationsPerPublish = DEFAULT_MAX_NOTIFICATIONS_PER_PUBLISH;
  private volatile UByte priority = DEFAULT_PRIORITY;

  private volatile boolean lifetimeAndKeepAliveCalculated = true;
  private volatile double watchdogMultiplier = 1.5;

  private volatile UInteger maxMonitoredItemsPerCall = uint(DEFAULT_MAX_MONITORED_ITEMS_PER_CALL);
  private final Lazy<UInteger> monitoredItemPartitionSize = new Lazy<>();

  private volatile @Nullable Object userObject;

  private volatile @Nullable SubscriptionListener listener;

  private final TaskQueue deliveryQueue;

  private final OpcUaClient client;

  public OpcUaSubscription(OpcUaClient client) {
    this.client = client;

    deliveryQueue = new TaskQueue(client.getTransport().getConfig().getExecutor());
  }

  /**
   * Create a Subscription with the given PublishingInterval.
   *
   * <p>The MaxKeepAliveCount and LifetimeCount are derived from {@code publishingInterval}, as they
   * would be by {@link #setPublishingInterval(Double)}.
   *
   * @param client the {@link OpcUaClient} this Subscription belongs to.
   * @param publishingInterval the PublishingInterval to request.
   */
  public OpcUaSubscription(OpcUaClient client, double publishingInterval) {
    this(client);

    // Delegate to the setter so the MaxKeepAliveCount and LifetimeCount are derived from
    // this PublishingInterval instead of being left at their default-derived values.
    setPublishingInterval(publishingInterval);
  }

  /**
   * Get the client this Subscription belongs to.
   *
   * @return the {@link OpcUaClient} this Subscription belongs to.
   */
  public OpcUaClient getClient() {
    return client;
  }

  // region Subscription Management

  /**
   * Create this Subscription on the Server.
   *
   * <p>Serialized against the other lifecycle transitions; see the class documentation. A call made
   * while this Subscription already exists on the Server, including one made concurrently with the
   * call that created it, fails with {@code Bad_InvalidState}.
   *
   * <p>A {@link #reset()} made while this call is waiting for the Server also fails it with {@code
   * Bad_InvalidState}: the reset has discarded the Subscription being created, so the Subscription
   * the Server did create is deleted again rather than installed.
   *
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void create() throws UaException {
    long incarnation;

    synchronized (lifecycleLock) {
      awaitTransitionSlot();

      if (syncState != SyncState.INITIAL) {
        throw new UaException(StatusCodes.Bad_InvalidState);
      }

      if (maxKeepAliveCount == null) {
        maxKeepAliveCount =
            calculateMaxKeepAliveCount(publishingInterval, DEFAULT_TARGET_KEEP_ALIVE_INTERVAL);
      }
      if (lifetimeCount == null) {
        lifetimeCount = calculateLifetimeCount(maxKeepAliveCount);
      }

      transitionInFlight = true;
      incarnation = this.incarnation;
    }

    UInteger abandonedSubscriptionId = null;

    try {
      CreateSubscriptionResponse response =
          client.createSubscription(
              publishingInterval,
              lifetimeCount,
              maxKeepAliveCount,
              maxNotificationsPerPublish,
              true,
              priority);

      synchronized (lifecycleLock) {
        if (this.incarnation != incarnation) {
          // A reset() overtook this call, so the Subscription the Server has just created is one
          // this object has already been told to forget. Deleted below rather than here: nothing
          // that blocks belongs in this critical section.
          abandonedSubscriptionId = response.getSubscriptionId();
        } else {
          // Before the SyncState says the Subscription exists: a SYNCHRONIZED Subscription with no
          // ServerState has no SubscriptionId to offer, and every operation that needs one answers
          // Bad_InvalidState until it appears.
          serverState =
              new ServerState(
                  response.getSubscriptionId(),
                  response.getRevisedPublishingInterval(),
                  response.getRevisedLifetimeCount(),
                  response.getRevisedMaxKeepAliveCount(),
                  maxNotificationsPerPublish,
                  priority,
                  true);

          syncState = SyncState.SYNCHRONIZED;

          watchdogTimer = new WatchdogTimer();
          client.addSessionActivityListener(watchdogTimer);
          resetWatchdogTimer();

          // Registered while the lock is still held, so the SubscriptionId the PublishingManager
          // binds its entry to is the one installed above and not one a reset() has since cleared.
          client.addSubscription(this);
          client.getPublishingManager().addSubscription(this);
        }
      }
    } finally {
      endTransition();
    }

    if (abandonedSubscriptionId != null) {
      deleteAbandonedSubscription(abandonedSubscriptionId);

      throw new UaException(
          StatusCodes.Bad_InvalidState, "the Subscription was reset while it was being created");
    }
  }

  /**
   * Create this Subscription on the Server.
   *
   * @return a {@link CompletionStage} that completes successfully if the Subscription was created,
   *     or completes exceptionally if there was a service- or operation-level error.
   */
  public CompletionStage<Unit> createAsync() {
    return supplyAsyncCompose(
        () -> {
          try {
            create();
            return CompletableFuture.completedFuture(Unit.VALUE);
          } catch (UaException e) {
            return CompletableFuture.failedFuture(e);
          }
        },
        client.getTransport().getConfig().getExecutor());
  }

  /**
   * Call the ModifySubscription service to update the Subscription's parameters on the Server.
   *
   * <p>Serialized against the other lifecycle transitions; see the class documentation. A {@link
   * #reset()} made while this call is waiting for the Server supersedes it: the revised parameters
   * describe a Subscription this object no longer represents, so they are discarded and the call
   * fails with {@code Bad_InvalidState}.
   *
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void modify() throws UaException {
    long incarnation;
    ServerState serverState;
    Modifications diff;

    synchronized (lifecycleLock) {
      awaitTransitionSlot();

      if (syncState == SyncState.INITIAL) {
        throw new UaException(StatusCodes.Bad_InvalidState);
      } else if (syncState != SyncState.UNSYNCHRONIZED) {
        return;
      }

      serverState = this.serverState;
      if (serverState == null) {
        throw new UaException(StatusCodes.Bad_InvalidState);
      }

      diff = modifications;
      modifications = null;

      assert diff != null;

      transitionInFlight = true;
      incarnation = this.incarnation;
    }

    try {
      ModifySubscriptionResponse response;
      try {
        response =
            client.modifySubscription(
                serverState.getSubscriptionId(),
                diff.publishingInterval().orElse(serverState.getPublishingInterval()),
                diff.lifetimeCount().orElse(serverState.getLifetimeCount()),
                diff.maxKeepAliveCount().orElse(serverState.getMaxKeepAliveCount()),
                diff.maxNotificationsPerPublish().orElse(maxNotificationsPerPublish),
                diff.priority().orElse(priority));
      } catch (Exception e) {
        synchronized (lifecycleLock) {
          if (this.incarnation == incarnation) {
            // The service call failed, so the Subscription remains UNSYNCHRONIZED. Restore the
            // pending modifications so the next modify() retries them instead of finding nothing to
            // send. Not restored if a reset() overtook the call: it cleared them on purpose, and
            // they describe a Subscription that no longer exists.
            restorePendingModifications(diff);
          }
        }

        throw e;
      }

      synchronized (lifecycleLock) {
        if (this.incarnation != incarnation) {
          throw new UaException(
              StatusCodes.Bad_InvalidState,
              "the Subscription was reset while it was being modified");
        }

        this.serverState =
            new ServerState(
                serverState.getSubscriptionId(),
                response.getRevisedPublishingInterval(),
                response.getRevisedLifetimeCount(),
                response.getRevisedMaxKeepAliveCount(),
                maxNotificationsPerPublish,
                priority,
                serverState.isPublishingEnabled());

        // Must happen after the revised parameters are installed: the watchdog delay is
        // derived from the current ServerState, so re-arming any earlier would use the
        // pre-modify PublishingInterval and MaxKeepAliveCount.
        resetWatchdogTimer();

        if (modifications == null) {
          syncState = SyncState.SYNCHRONIZED;
        }
      }
    } finally {
      endTransition();
    }
  }

  /**
   * Call the ModifySubscription service to update the Subscription's parameters on the Server.
   *
   * @return a {@link CompletionStage} that completes successfully if the Subscription was modified,
   *     or completes exceptionally if there was a service- or operation-level error.
   */
  public CompletionStage<Unit> modifyAsync() {
    return supplyAsyncCompose(
        () -> {
          try {
            modify();
            return CompletableFuture.completedFuture(Unit.VALUE);
          } catch (UaException e) {
            return CompletableFuture.failedFuture(e);
          }
        },
        client.getTransport().getConfig().getExecutor());
  }

  /**
   * Restore {@code diff} as the pending {@link Modifications} after a failed modify service call,
   * so that a subsequent {@link #modify()} retries the same parameters.
   *
   * <p>Any Modifications requested while the failed service call was in flight take precedence over
   * the values being restored.
   *
   * @param diff the {@link Modifications} the failed service call attempted to apply.
   */
  private void restorePendingModifications(Modifications diff) {
    Modifications pending = modifications;

    if (pending == null) {
      modifications = diff;
    } else {
      if (pending.publishingInterval == null) {
        pending.publishingInterval = diff.publishingInterval;
      }
      if (pending.lifetimeCount == null) {
        pending.lifetimeCount = diff.lifetimeCount;
      }
      if (pending.maxKeepAliveCount == null) {
        pending.maxKeepAliveCount = diff.maxKeepAliveCount;
      }
      if (pending.maxNotificationsPerPublish == null) {
        pending.maxNotificationsPerPublish = diff.maxNotificationsPerPublish;
      }
      if (pending.priority == null) {
        pending.priority = diff.priority;
      }
    }
  }

  /**
   * Delete this Subscription from the Server.
   *
   * <p>Serialized against the other lifecycle transitions; see the class documentation. A {@link
   * #reset()} made while this call is waiting for the Server has already discarded the Subscription
   * by the time the response arrives, so this call does not reset it a second time; the
   * operation-level result of the DeleteSubscriptions call is reported either way.
   *
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void delete() throws UaException {
    long incarnation;
    ServerState serverState;

    synchronized (lifecycleLock) {
      awaitTransitionSlot();

      if (syncState == SyncState.INITIAL) {
        return;
      }

      serverState = this.serverState;
      if (serverState == null) {
        throw new UaException(StatusCodes.Bad_InvalidState);
      }

      transitionInFlight = true;
      incarnation = this.incarnation;
    }

    StatusCode result;
    try {
      DeleteSubscriptionsResponse response =
          client.deleteSubscriptions(List.of(serverState.getSubscriptionId()));

      result = requireNonNull(response.getResults())[0];

      synchronized (lifecycleLock) {
        // A reset() that overtook this call has already discarded the Subscription, so there is
        // nothing left for this one to discard.
        if (this.incarnation == incarnation
            && (result.isGood() || result.value() == StatusCodes.Bad_SubscriptionIdInvalid)) {
          reset();
        }
      }
    } finally {
      endTransition();
    }

    if (!result.isGood()) {
      throw new UaException(result);
    }
  }

  /**
   * Delete this Subscription from the Server.
   *
   * @return a {@link CompletionStage} that completes successfully if the Subscription was deleted,
   *     or completes exceptionally if there was a service- or operation-level error.
   */
  public CompletionStage<Unit> deleteAsync() {
    return supplyAsyncCompose(
        () -> {
          try {
            delete();
            return CompletableFuture.completedFuture(Unit.VALUE);
          } catch (UaException e) {
            return CompletableFuture.failedFuture(e);
          }
        },
        client.getTransport().getConfig().getExecutor());
  }

  /**
   * Wait until no lifecycle transition is in flight and then claim the right to run one, i.e. set
   * {@link #transitionInFlight}.
   *
   * <p>Must be called while holding {@link #lifecycleLock}, which is released for the duration of
   * the wait; the caller must therefore read the state it validates <i>after</i> calling this.
   *
   * @throws UaException if the calling thread is interrupted while waiting.
   */
  private void awaitTransitionSlot() throws UaException {
    while (transitionInFlight) {
      try {
        lifecycleLock.wait();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new UaException(StatusCodes.Bad_UnexpectedError, e);
      }
    }
  }

  /**
   * Give up the transition claimed by {@link #awaitTransitionSlot()} and wake whoever is waiting
   * for it.
   *
   * <p>Called from a {@code finally} in each transition, so that however the service call or the
   * handling of its result ends, the next transition is not left waiting on a claim nobody holds.
   */
  private void endTransition() {
    synchronized (lifecycleLock) {
      transitionInFlight = false;
      lifecycleLock.notifyAll();
    }
  }

  /**
   * Delete a Subscription the Server created for this object but which a {@link #reset()} means it
   * can no longer represent.
   *
   * <p>Best effort and never awaited: the caller's result has already been discarded, and the
   * reason the reset was allowed to overtake it in the first place is that its own callers cannot
   * afford to wait for the Server. Part 4 §5.13.8 gives DeleteSubscriptions the SubscriptionId as
   * its only handle on a Subscription, so this is the last chance anything has to name this one; if
   * the attempt fails it runs on the Server until its lifetime expires, which is worth a warning
   * and nothing more.
   *
   * @param subscriptionId the SubscriptionId of the Subscription to delete.
   */
  private void deleteAbandonedSubscription(UInteger subscriptionId) {
    logger.debug("id={}, deleting Subscription abandoned by a concurrent reset()", subscriptionId);

    client
        .deleteSubscriptionsAsync(List.of(subscriptionId))
        .whenComplete(
            (response, ex) -> {
              if (ex != null) {
                logger.warn("id={}, failed to delete abandoned Subscription", subscriptionId, ex);
              } else {
                StatusCode result = requireNonNull(response.getResults())[0];

                if (!result.isGood() && result.value() != StatusCodes.Bad_SubscriptionIdInvalid) {
                  logger.warn(
                      "id={}, failed to delete abandoned Subscription: {}", subscriptionId, result);
                }
              }
            });
  }

  // endregion

  // region MonitoredItem Management

  /**
   * Add a MonitoredItem to this Subscription.
   *
   * <p>This item will not be created on the server until {@link #synchronizeMonitoredItems()} is
   * called.
   *
   * @param item the MonitoredItem to add.
   * @throws UaRuntimeException if the Subscription has not been created yet.
   */
  public void addMonitoredItem(OpcUaMonitoredItem item) {
    Optional<UInteger> existingHandle = item.getClientHandle();

    if (existingHandle.isPresent()) {
      UInteger handle = existingHandle.get();

      // O(1) check: if this item is already in the map, nothing to do
      if (monitoredItems.get(handle) == item) {
        return;
      }

      // Item has a handle but isn't in map - check if pending deletion
      if (itemsToDelete.remove(item)) {
        monitoredItems.put(handle, item);
      }
      // else: item has a handle from a different context, ignore
    } else {
      // Brand-new item with no handle
      UInteger clientHandle = clientHandleSequence.nextClientHandle();
      item.setClientHandle(clientHandle);

      monitoredItems.put(clientHandle, item);
    }
  }

  /**
   * Add a group of MonitoredItems to this Subscription.
   *
   * <p>These items will not be created on the server until {@link #synchronizeMonitoredItems()} is
   * called.
   *
   * @param items the MonitoredItems to add.
   * @throws UaRuntimeException if the Subscription has not been created yet.
   */
  public void addMonitoredItems(List<OpcUaMonitoredItem> items) {
    items.forEach(this::addMonitoredItem);
  }

  /**
   * Remove a MonitoredItem from this Subscription.
   *
   * <p>This item will not be deleted from the server until {@link #synchronizeMonitoredItems()} is
   * called
   *
   * @param item the MonitoredItem to remove.
   */
  public void removeMonitoredItem(OpcUaMonitoredItem item) {
    item.getClientHandle().map(monitoredItems::remove).ifPresent(itemsToDelete::add);
  }

  /**
   * Remove a group of MonitoredItems from this Subscription.
   *
   * <p>These items will not be deleted from the server until {@link #synchronizeMonitoredItems()}
   * is called.
   *
   * @param items the MonitoredItems to remove.
   * @throws UaRuntimeException if the Subscription has not been created yet.
   */
  public void removeMonitoredItems(List<OpcUaMonitoredItem> items) {
    items.forEach(this::removeMonitoredItem);
  }

  /**
   * Synchronize the Subscription's MonitoredItems with the Server.
   *
   * <p>This is a compound operation that deletes any MonitoredItems that have been removed from the
   * Subscription, modifies any existing MonitoredItems that have been changed, and creates any new
   * MonitoredItems that have been added but not yet created on the Server.
   *
   * @throws MonitoredItemSynchronizationException if one or more MonitoredItems failed to
   *     synchronize for any reason. This could be a service-level failure or an operation-level
   *     failure. Check the {@link MonitoredItemServiceOperationResult}s for details.
   */
  public void synchronizeMonitoredItems() throws MonitoredItemSynchronizationException {
    List<MonitoredItemServiceOperationResult> deleteResults = deleteMonitoredItems();
    List<MonitoredItemServiceOperationResult> modifyResults = modifyMonitoredItems();
    List<MonitoredItemServiceOperationResult> createResults = createMonitoredItems();

    if (Stream.of(deleteResults, modifyResults, createResults)
        .flatMap(List::stream)
        .anyMatch(
            r -> !r.serviceResult().isGood() || !r.operationResult().orElseThrow().isGood())) {

      throw new MonitoredItemSynchronizationException(
          "failed to synchronize one or more MonitoredItems",
          createResults,
          modifyResults,
          deleteResults);
    }
  }

  /**
   * Create any MonitoredItems that have been added to the Subscription but not yet created on the
   * Server.
   *
   * @return a List of {@link MonitoredItemServiceOperationResult}s that contain the MonitoredItem
   *     and the service- and operation-level results associated with the attempt to create it.
   */
  public List<MonitoredItemServiceOperationResult> createMonitoredItems() {
    List<OpcUaMonitoredItem> itemsToCreate =
        monitoredItems.values().stream()
            .filter(item -> item.getSyncState() == OpcUaMonitoredItem.SyncState.INITIAL)
            .collect(Collectors.toList());

    return createMonitoredItems(itemsToCreate);
  }

  /**
   * Create any MonitoredItems that have been added to the Subscription but not yet created on the
   * Server, filtered by the given predicate.
   *
   * <p>This could be used to e.g. further restrict the MonitoredItems to be created to only those
   * that have not had a previous creation attempt:
   *
   * <pre>
   *   List<MonitoredItemServiceOperationResult> results = subscription.createMonitoredItems(
   *      item -> item.getCreateResult().isEmpty()
   *   );
   * </pre>
   *
   * @param filter a predicate that allows further restriction of the MonitoredItems to be created.
   * @return a List of {@link MonitoredItemServiceOperationResult}s that contain the MonitoredItem
   *     and the service- and operation-level results associated with the attempt to create it.
   */
  public List<MonitoredItemServiceOperationResult> createMonitoredItems(
      Predicate<OpcUaMonitoredItem> filter) {

    List<OpcUaMonitoredItem> itemsToCreate =
        monitoredItems.values().stream()
            .filter(item -> item.getSyncState() == OpcUaMonitoredItem.SyncState.INITIAL)
            .filter(filter)
            .collect(Collectors.toList());

    return createMonitoredItems(itemsToCreate);
  }

  private List<MonitoredItemServiceOperationResult> createMonitoredItems(
      List<OpcUaMonitoredItem> itemsToCreate) {

    if (itemsToCreate.isEmpty()) {
      return Collections.emptyList();
    }

    var serviceOperationsResults =
        new ArrayList<MonitoredItemServiceOperationResult>(itemsToCreate.size());

    ServerState serverState = this.serverState;
    if (serverState == null) {
      logger.debug("Bad_InvalidState: subscription not created yet");

      for (OpcUaMonitoredItem item : itemsToCreate) {
        serviceOperationsResults.add(
            new MonitoredItemServiceOperationResult(
                item, new StatusCode(StatusCodes.Bad_InvalidState), null));
      }

      return serviceOperationsResults;
    }

    UInteger partitionSize = getMonitoredItemPartitionSize();

    List<List<OpcUaMonitoredItem>> partitions =
        Lists.partition(itemsToCreate, partitionSize.intValue()).toList();

    for (List<OpcUaMonitoredItem> partition : partitions) {
      try {
        logger.debug(
            "id={}, createMonitoredItems partition.size(): {}",
            serverState.getSubscriptionId(),
            partition.size());

        CreateMonitoredItemsResponse response =
            client.createMonitoredItems(
                serverState.getSubscriptionId(),
                TimestampsToReturn.Both,
                partition.stream()
                    .map(OpcUaMonitoredItem::newCreateRequest)
                    .collect(Collectors.toList()));

        MonitoredItemCreateResult[] results = requireNonNull(response.getResults());

        for (int i = 0; i < results.length; i++) {
          MonitoredItemCreateResult result = results[i];
          OpcUaMonitoredItem monitoredItem = partition.get(i);

          monitoredItem.applyCreateResult(result);

          serviceOperationsResults.add(
              new MonitoredItemServiceOperationResult(
                  monitoredItem, StatusCode.GOOD, result.getStatusCode()));
        }
      } catch (UaException e) {
        for (OpcUaMonitoredItem item : partition) {
          serviceOperationsResults.add(
              new MonitoredItemServiceOperationResult(item, e.getStatusCode(), null));
        }
      }
    }

    return serviceOperationsResults;
  }

  /**
   * Modify any MonitoredItems that have been changed.
   *
   * @return a List of the MonitoredItems that were modified.
   */
  public List<MonitoredItemServiceOperationResult> modifyMonitoredItems() {
    List<OpcUaMonitoredItem> itemsToModify =
        monitoredItems.values().stream()
            .filter(item -> item.getSyncState() == OpcUaMonitoredItem.SyncState.UNSYNCHRONIZED)
            .collect(Collectors.toList());

    if (!itemsToModify.isEmpty()) {
      return modifyMonitoredItems(itemsToModify);
    } else {
      return Collections.emptyList();
    }
  }

  private List<MonitoredItemServiceOperationResult> modifyMonitoredItems(
      List<OpcUaMonitoredItem> itemsToModify) {

    var serviceOperationsResults =
        new ArrayList<MonitoredItemServiceOperationResult>(itemsToModify.size());

    ServerState serverState = this.serverState;
    if (serverState == null) {
      for (OpcUaMonitoredItem item : itemsToModify) {
        serviceOperationsResults.add(
            new MonitoredItemServiceOperationResult(
                item, new StatusCode(StatusCodes.Bad_InvalidState), null));
      }

      return serviceOperationsResults;
    }

    UInteger partitionSize = getMonitoredItemPartitionSize();

    List<List<OpcUaMonitoredItem>> partitions =
        Lists.partition(itemsToModify, partitionSize.intValue()).toList();

    for (List<OpcUaMonitoredItem> partition : partitions) {
      try {
        logger.debug(
            "id={}, modifyMonitoredItems partition.size(): {}",
            serverState.subscriptionId,
            partition.size());

        ModifyMonitoredItemsResponse response =
            client.modifyMonitoredItems(
                serverState.getSubscriptionId(),
                TimestampsToReturn.Both,
                partition.stream()
                    .map(OpcUaMonitoredItem::newModifyRequest)
                    .collect(Collectors.toList()));

        MonitoredItemModifyResult[] results = requireNonNull(response.getResults());

        for (int i = 0; i < results.length; i++) {
          MonitoredItemModifyResult result = results[i];
          OpcUaMonitoredItem monitoredItem = partition.get(i);

          monitoredItem.applyModifyResult(result);

          serviceOperationsResults.add(
              new MonitoredItemServiceOperationResult(
                  monitoredItem, StatusCode.GOOD, result.getStatusCode()));
        }
      } catch (UaException e) {
        for (OpcUaMonitoredItem item : partition) {
          serviceOperationsResults.add(
              new MonitoredItemServiceOperationResult(item, e.getStatusCode(), null));
        }
      }
    }

    return serviceOperationsResults;
  }

  /**
   * Delete any MonitoredItems that have been removed from the Subscription.
   *
   * <p>A MonitoredItem is only removed from the pending deletion queue once the Server has reported
   * an operation-level result for it. A deletion that never reached the Server, e.g. because the
   * service call failed, remains pending and is attempted again by the next call.
   *
   * @return a List of the MonitoredItems that were deleted.
   */
  public List<MonitoredItemServiceOperationResult> deleteMonitoredItems() {
    // Items that were never created on the Server don't need to be deleted from it.
    this.itemsToDelete.removeIf(
        item -> item.getSyncState() == OpcUaMonitoredItem.SyncState.INITIAL);

    List<OpcUaMonitoredItem> itemsToDelete = List.copyOf(this.itemsToDelete);

    if (itemsToDelete.isEmpty()) {
      return Collections.emptyList();
    }

    List<MonitoredItemServiceOperationResult> results = deleteMonitoredItems(itemsToDelete);

    for (MonitoredItemServiceOperationResult result : results) {
      // An operation-level result means the Server acted on the deletion, either by deleting the
      // item or by reporting Bad_MonitoredItemIdInvalid because it was already gone. Either way
      // the item no longer exists on the Server and applyDeleteResult() has detached it. Anything
      // else, e.g. a service fault, leaves the item on the Server, so it stays queued.
      if (result.operationResult().isPresent()) {
        this.itemsToDelete.remove(result.monitoredItem());
      }
    }

    return results;
  }

  private List<MonitoredItemServiceOperationResult> deleteMonitoredItems(
      List<OpcUaMonitoredItem> itemsToDelete) {

    var serviceOperationsResults =
        new ArrayList<MonitoredItemServiceOperationResult>(itemsToDelete.size());

    ServerState serverState = this.serverState;
    if (serverState == null) {
      for (OpcUaMonitoredItem item : itemsToDelete) {
        serviceOperationsResults.add(
            new MonitoredItemServiceOperationResult(
                item, new StatusCode(StatusCodes.Bad_InvalidState), null));
      }

      return serviceOperationsResults;
    }

    UInteger partitionSize = getMonitoredItemPartitionSize();

    List<List<OpcUaMonitoredItem>> partitions =
        Lists.partition(itemsToDelete, partitionSize.intValue()).toList();

    for (List<OpcUaMonitoredItem> partition : partitions) {
      // Server state may be cleared concurrently, so read each item id only once.
      //noinspection DuplicatedCode
      var monitoredItemIds = new ArrayList<UInteger>(partition.size());
      var itemIds = new ArrayList<Optional<UInteger>>(partition.size());

      for (OpcUaMonitoredItem item : partition) {
        Optional<UInteger> itemId = item.getMonitoredItemId();

        itemIds.add(itemId);
        itemId.ifPresent(monitoredItemIds::add);
      }

      if (monitoredItemIds.isEmpty()) {
        for (OpcUaMonitoredItem item : partition) {
          serviceOperationsResults.add(
              new MonitoredItemServiceOperationResult(
                  item, new StatusCode(StatusCodes.Bad_InvalidState), null));
        }

        continue;
      }

      try {
        logger.debug(
            "id={}, deleteMonitoredItems partition.size(): {}",
            serverState.subscriptionId,
            partition.size());

        DeleteMonitoredItemsResponse response =
            client.deleteMonitoredItems(serverState.getSubscriptionId(), monitoredItemIds);

        StatusCode[] results = requireNonNull(response.getResults());

        int resultIndex = 0;
        for (int i = 0; i < partition.size(); i++) {
          OpcUaMonitoredItem item = partition.get(i);

          if (itemIds.get(i).isPresent()) {
            StatusCode result = results[resultIndex++];

            item.applyDeleteResult(result);

            serviceOperationsResults.add(
                new MonitoredItemServiceOperationResult(item, StatusCode.GOOD, result));
          } else {
            serviceOperationsResults.add(
                new MonitoredItemServiceOperationResult(
                    item, new StatusCode(StatusCodes.Bad_InvalidState), null));
          }
        }
      } catch (UaException e) {
        for (int i = 0; i < partition.size(); i++) {
          OpcUaMonitoredItem item = partition.get(i);

          if (itemIds.get(i).isPresent()) {
            serviceOperationsResults.add(
                new MonitoredItemServiceOperationResult(item, e.getStatusCode(), null));
          } else {
            serviceOperationsResults.add(
                new MonitoredItemServiceOperationResult(
                    item, new StatusCode(StatusCodes.Bad_InvalidState), null));
          }
        }
      }
    }

    return serviceOperationsResults;
  }

  private UInteger getMonitoredItemPartitionSize() {
    return monitoredItemPartitionSize.get(
        () -> {
          UInteger serverMaxMonitoredItemsPerCall;
          try {
            serverMaxMonitoredItemsPerCall =
                client.getOperationLimits().maxMonitoredItemsPerCall().orElse(UInteger.MAX);

            if (serverMaxMonitoredItemsPerCall.intValue() == 0) {
              serverMaxMonitoredItemsPerCall = UInteger.MAX;
            }
          } catch (UaException e) {
            serverMaxMonitoredItemsPerCall = UInteger.MAX;
          }

          int configuredMax = Ints.saturatedCast(maxMonitoredItemsPerCall.longValue());
          int serverMax = Ints.saturatedCast(serverMaxMonitoredItemsPerCall.longValue());

          return uint(Math.min(configuredMax, serverMax));
        });
  }

  // endregion

  // region MonitoringMode Management

  /**
   * Set the {@link MonitoringMode} for a group of MonitoredItems.
   *
   * <p>A MonitoredItem that does not exist on the Server produces a {@code Bad_InvalidState}
   * service result.
   *
   * @param monitoringMode the MonitoringMode to set.
   * @param monitoredItems the MonitoredItems to set the MonitoringMode for.
   * @return a List of {@link MonitoredItemServiceOperationResult}s that contain the MonitoredItem
   *     and the service- and operation-level results associated with the attempt to set the
   *     MonitoringMode.
   */
  public List<MonitoredItemServiceOperationResult> setMonitoringMode(
      MonitoringMode monitoringMode, List<OpcUaMonitoredItem> monitoredItems) {

    if (monitoredItems.isEmpty()) {
      return Collections.emptyList();
    }

    var serviceOperationResults =
        new ArrayList<MonitoredItemServiceOperationResult>(monitoredItems.size());

    ServerState serverState = this.serverState;
    if (serverState == null) {
      for (OpcUaMonitoredItem item : monitoredItems) {
        serviceOperationResults.add(
            new MonitoredItemServiceOperationResult(
                item, new StatusCode(StatusCodes.Bad_InvalidState), null));
      }
      return serviceOperationResults;
    }

    UInteger partitionSize = getMonitoredItemPartitionSize();

    List<List<OpcUaMonitoredItem>> partitions =
        Lists.partition(monitoredItems, partitionSize.intValue()).toList();

    for (List<OpcUaMonitoredItem> partition : partitions) {
      // Server state may be cleared concurrently, so read each item id only once.
      //noinspection DuplicatedCode
      var monitoredItemIds = new ArrayList<UInteger>(partition.size());
      var itemIds = new ArrayList<Optional<UInteger>>(partition.size());

      for (OpcUaMonitoredItem item : partition) {
        Optional<UInteger> itemId = item.getMonitoredItemId();

        itemIds.add(itemId);
        itemId.ifPresent(monitoredItemIds::add);
      }

      if (monitoredItemIds.isEmpty()) {
        for (OpcUaMonitoredItem item : partition) {
          serviceOperationResults.add(
              new MonitoredItemServiceOperationResult(
                  item, new StatusCode(StatusCodes.Bad_InvalidState), null));
        }

        continue;
      }

      try {
        logger.debug(
            "id={}, setMonitoringMode partition.size(): {}",
            serverState.subscriptionId,
            partition.size());

        SetMonitoringModeResponse response =
            client.setMonitoringMode(
                serverState.getSubscriptionId(), monitoringMode, monitoredItemIds);

        StatusCode[] results = requireNonNull(response.getResults());

        int resultIndex = 0;
        for (int i = 0; i < partition.size(); i++) {
          OpcUaMonitoredItem item = partition.get(i);

          if (itemIds.get(i).isPresent()) {
            StatusCode result = results[resultIndex++];

            item.applySetMonitoringModeResult(result);
            if (result.isGood()) {
              item.setMonitoringMode(monitoringMode);
            }
            serviceOperationResults.add(
                new MonitoredItemServiceOperationResult(item, StatusCode.GOOD, result));
          } else {
            serviceOperationResults.add(
                new MonitoredItemServiceOperationResult(
                    item, new StatusCode(StatusCodes.Bad_InvalidState), null));
          }
        }
      } catch (UaException e) {
        for (int i = 0; i < partition.size(); i++) {
          OpcUaMonitoredItem item = partition.get(i);

          if (itemIds.get(i).isPresent()) {
            item.applySetMonitoringModeResult(e.getStatusCode());

            serviceOperationResults.add(
                new MonitoredItemServiceOperationResult(item, e.getStatusCode(), null));
          } else {
            serviceOperationResults.add(
                new MonitoredItemServiceOperationResult(
                    item, new StatusCode(StatusCodes.Bad_InvalidState), null));
          }
        }
      }
    }

    return serviceOperationResults;
  }

  // endregion

  // region Publishing Management

  /**
   * Set the publishing mode, i.e. enable or disable publishing, for this Subscription.
   *
   * <p>Serialized against the other lifecycle transitions; see the class documentation. A {@link
   * #reset()} made while this call is waiting for the Server supersedes it: the new publishing mode
   * belongs to a Subscription this object no longer represents, so it is discarded and the call
   * fails with {@code Bad_InvalidState}.
   *
   * @param enabled {@code true} to enable publishing, {@code false} to disable publishing.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void setPublishingMode(boolean enabled) throws UaException {
    long incarnation;
    ServerState serverState;

    synchronized (lifecycleLock) {
      awaitTransitionSlot();

      if (syncState == SyncState.INITIAL) {
        throw new UaException(StatusCodes.Bad_InvalidState);
      }

      serverState = this.serverState;
      if (serverState == null) {
        throw new UaException(StatusCodes.Bad_InvalidState);
      }

      transitionInFlight = true;
      incarnation = this.incarnation;
    }

    try {
      SetPublishingModeResponse response =
          client.setPublishingMode(enabled, List.of(serverState.getSubscriptionId()));

      StatusCode result = requireNonNull(response.getResults())[0];

      synchronized (lifecycleLock) {
        if (this.incarnation != incarnation) {
          throw new UaException(
              StatusCodes.Bad_InvalidState,
              "the Subscription was reset while its publishing mode was being set");
        }

        if (result.isGood()) {
          this.serverState =
              new ServerState(
                  serverState.getSubscriptionId(),
                  serverState.getPublishingInterval(),
                  serverState.getLifetimeCount(),
                  serverState.getMaxKeepAliveCount(),
                  maxNotificationsPerPublish,
                  priority,
                  enabled);
        } else {
          throw new UaException(result);
        }
      }
    } finally {
      endTransition();
    }
  }

  /**
   * Set the publishing mode, i.e. enable or disable publishing, for this Subscription.
   *
   * @param enabled {@code true} to enable publishing, {@code false} to disable publishing.
   * @return a {@link CompletionStage} that completes successfully if the operation was successful,
   *     or completes exceptionally if there was a service- or operation-level error.
   */
  public CompletionStage<Unit> setPublishingModeAsync(boolean enabled) {
    return supplyAsyncCompose(
        () -> {
          try {
            setPublishingMode(enabled);
            return CompletableFuture.completedFuture(Unit.VALUE);
          } catch (UaException e) {
            return CompletableFuture.failedFuture(e);
          }
        },
        client.getTransport().getConfig().getExecutor());
  }

  // endregion

  /**
   * Get the current {@link SyncState} of this Subscription.
   *
   * <p>A lifecycle transition waiting for the Server is not visible here: the state does not
   * advance until the Server has answered, so a Subscription being created still reports {@link
   * SyncState#INITIAL}, and one being modified still reports {@link SyncState#UNSYNCHRONIZED}. That
   * is what remains true of the Subscription while the request is in flight — and it is also why a
   * transition can fail without the state having to be rolled back.
   *
   * @return the current {@link SyncState} of this Subscription.
   */
  public SyncState getSyncState() {
    return syncState;
  }

  /**
   * Check if all {@link OpcUaMonitoredItem}s belonging to this subscription are synchronized with
   * the server.
   *
   * <p>Returns {@code true} when no MonitoredItems that require server-side deletion are pending
   * deletion and every item in the subscription has {@link
   * OpcUaMonitoredItem.SyncState#SYNCHRONIZED}. Items pending deletion while still in {@link
   * OpcUaMonitoredItem.SyncState#INITIAL} are ignored because they were never created on the
   * server.
   *
   * @return {@code true} if all MonitoredItems are synchronized.
   */
  public boolean isMonitoredItemsSynchronized() {
    return itemsToDelete.stream()
            .noneMatch(item -> item.getSyncState() != OpcUaMonitoredItem.SyncState.INITIAL)
        && monitoredItems.values().stream()
            .allMatch(item -> item.getSyncState() == OpcUaMonitoredItem.SyncState.SYNCHRONIZED);
  }

  /**
   * Check if this subscription and all its {@link OpcUaMonitoredItem}s are synchronized with the
   * server.
   *
   * <p>Equivalent to checking that {@link #getSyncState()} is {@link SyncState#SYNCHRONIZED} and
   * {@link #isMonitoredItemsSynchronized()} is {@code true}.
   *
   * @return {@code true} if the subscription settings and all MonitoredItems are synchronized.
   */
  public boolean isFullySynchronized() {
    return syncState == SyncState.SYNCHRONIZED && isMonitoredItemsSynchronized();
  }

  /**
   * @return the current {@link ServerState} of this Subscription, if it has been created.
   */
  public Optional<ServerState> getServerState() {
    return Optional.ofNullable(serverState);
  }

  /**
   * Get a list of the MonitoredItems belonging to this Subscription.
   *
   * <p>If {@link #synchronizeMonitoredItems()} has not been called since adding or removing
   * MonitoredItems, this List may not represent the server's view of the MonitoredItems.
   *
   * @return a List of the MonitoredItems belonging to this Subscription.
   */
  public List<OpcUaMonitoredItem> getMonitoredItems() {
    return List.copyOf(monitoredItems.values());
  }

  /**
   * Get the current PublishingInterval for this Subscription.
   *
   * <p>The server may have revised it upon creation or modification.
   *
   * @return the current PublishingInterval for this Subscription.
   * @see #getRevisedPublishingInterval()
   */
  public Double getPublishingInterval() {
    return publishingInterval;
  }

  /**
   * Get the current LifetimeCount for this Subscription.
   *
   * <p>The server may have revised it upon creation or modification.
   *
   * @return the current LifetimeCount for this Subscription.
   * @see #getRevisedLifetimeCount()
   */
  public UInteger getLifetimeCount() {
    return lifetimeCount;
  }

  /**
   * Get the current MaxKeepAliveCount for this Subscription.
   *
   * <p>The server may have revised it upon creation or modification.
   *
   * @return the current MaxKeepAliveCount for this Subscription.
   * @see #getRevisedMaxKeepAliveCount()
   */
  public UInteger getMaxKeepAliveCount() {
    return maxKeepAliveCount;
  }

  /**
   * Get the Subscription's Priority setting.
   *
   * <p>The Server does not revise this setting, so the value reflects the most recently requested
   * by a create or modify operation.
   *
   * @return the Subscription's Priority setting.
   */
  public UByte getPriority() {
    return priority;
  }

  /**
   * Get the Subscription's MaxNotificationsPerPublish setting.
   *
   * <p>The Server does not revise this setting, so the value reflects the most recently requested
   * by a create or modify operation.
   *
   * @return the Subscription's MaxNotificationsPerPublish setting.
   */
  public UInteger getMaxNotificationsPerPublish() {
    return maxNotificationsPerPublish;
  }

  /**
   * Get whether publishing is enabled for this Subscription.
   *
   * <p>This is available only after the Subscription has been created.
   *
   * @return {@code true} if publishing is enabled for this Subscription.
   */
  public Optional<Boolean> isPublishingEnabled() {
    return getServerState().map(ServerState::isPublishingEnabled);
  }

  /**
   * Get the SubscriptionId assigned to this Subscription by the Server.
   *
   * <p>The SubscriptionId is available only after the Subscription has been created.
   *
   * @return the SubscriptionId assigned to this Subscription by the Server.
   */
  public Optional<UInteger> getSubscriptionId() {
    return getServerState().map(ServerState::getSubscriptionId);
  }

  /**
   * Get the revised PublishingInterval from the most recent create or modify operation.
   *
   * <p>The revised PublishingInterval is available only after the Subscription has been created or
   * modified.
   *
   * @return the revised PublishingInterval from the most recent create or modify operation.
   */
  public Optional<Double> getRevisedPublishingInterval() {
    return getServerState().map(ServerState::getPublishingInterval);
  }

  /**
   * Get the revised LifetimeCount from the most recent create or modify operation.
   *
   * <p>The revised LifetimeCount is available only after the Subscription has been created or
   * modified.
   *
   * @return the revised LifetimeCount from the most recent create or modify operation.
   */
  public Optional<UInteger> getRevisedLifetimeCount() {
    return getServerState().map(ServerState::getLifetimeCount);
  }

  /**
   * Get the revised MaxKeepAliveCount from the most recent create or modify operation.
   *
   * <p>The revised MaxKeepAliveCount is available only after the Subscription has been created or
   * modified.
   *
   * @return the revised MaxKeepAliveCount from the most recent create or modify operation.
   */
  public Optional<UInteger> getRevisedMaxKeepAliveCount() {
    return getServerState().map(ServerState::getMaxKeepAliveCount);
  }

  /**
   * Set a new PublishingInterval for this Subscription.
   *
   * <p>If the Subscription has not yet been created, this will be the PublishingInterval used
   * during the create service call.
   *
   * <p>If the Subscription has already been created, this will be the PublishingInterval used
   * during the next modify service call.
   *
   * @param publishingInterval the new PublishingInterval.
   * @see #create()
   * @see #createAsync()
   * @see #modify()
   * @see #modifyAsync()
   */
  public void setPublishingInterval(Double publishingInterval) {
    this.publishingInterval = publishingInterval;

    if (syncState != SyncState.INITIAL) {
      if (modifications == null) {
        modifications = new Modifications();
      }

      modifications.publishingInterval = publishingInterval;

      syncState = SyncState.UNSYNCHRONIZED;
    }

    if (lifetimeAndKeepAliveCalculated) {
      UInteger maxKeepAliveCount =
          calculateMaxKeepAliveCount(publishingInterval, DEFAULT_TARGET_KEEP_ALIVE_INTERVAL);
      UInteger lifetimeCount = calculateLifetimeCount(maxKeepAliveCount);

      setMaxKeepAliveCount(maxKeepAliveCount);
      setLifetimeCount(lifetimeCount);
    }
  }

  /**
   * Set a new LifetimeCount for this Subscription.
   *
   * <p>If the Subscription has not yet been created, this will be the LifetimeCount used during the
   * create service call.
   *
   * <p>If the Subscription has already been created, this will be the LifetimeCount used during the
   * next modify service call.
   *
   * @param lifetimeCount the new LifetimeCount.
   * @see #create()
   * @see #createAsync()
   * @see #modify()
   * @see #modifyAsync()
   */
  public void setLifetimeCount(UInteger lifetimeCount) {
    this.lifetimeCount = lifetimeCount;

    if (syncState != SyncState.INITIAL) {
      if (modifications == null) {
        modifications = new Modifications();
      }

      modifications.lifetimeCount = lifetimeCount;

      syncState = SyncState.UNSYNCHRONIZED;
    }
  }

  /**
   * Set a new MaxKeepAliveCount for this Subscription.
   *
   * <p>If the Subscription has not yet been created, this will be the MaxKeepAliveCount used during
   * the create service call.
   *
   * <p>If the Subscription has already been created, this will be the MaxKeepAliveCount used during
   * the next modify service call.
   *
   * @param maxKeepAliveCount the new MaxKeepAliveCount.
   * @see #create()
   * @see #createAsync()
   * @see #modify()
   * @see #modifyAsync()
   */
  public void setMaxKeepAliveCount(UInteger maxKeepAliveCount) {
    this.maxKeepAliveCount = maxKeepAliveCount;

    if (syncState != SyncState.INITIAL) {
      if (modifications == null) {
        modifications = new Modifications();
      }

      modifications.maxKeepAliveCount = maxKeepAliveCount;

      syncState = SyncState.UNSYNCHRONIZED;
    }
  }

  /**
   * Set a new Priority for this Subscription.
   *
   * <p>If the Subscription has not yet been created, this will be the Priority used during the
   * create service call.
   *
   * <p>If the Subscription has already been created, this will be the Priority used during the next
   * modify service call.
   *
   * @param priority the new Priority.
   * @see #create()
   * @see #createAsync()
   * @see #modify()
   * @see #modifyAsync()
   */
  public void setPriority(UByte priority) {
    this.priority = priority;

    if (syncState != SyncState.INITIAL) {
      if (modifications == null) {
        modifications = new Modifications();
      }

      modifications.priority = priority;

      syncState = SyncState.UNSYNCHRONIZED;
    }
  }

  /**
   * Set a new MaxNotificationsPerPublish for this Subscription.
   *
   * <p>If the Subscription has not yet been created, this will be the MaxNotificationsPerPublish
   * used during the create service call.
   *
   * <p>If the Subscription has already been created, this will be the MaxNotificationsPerPublish
   * used during the next modify service call.
   *
   * @param maxNotificationsPerPublish the new MaxNotificationsPerPublish.
   * @see #create()
   * @see #createAsync()
   * @see #modify()
   * @see #modifyAsync()
   */
  public void setMaxNotificationsPerPublish(UInteger maxNotificationsPerPublish) {
    this.maxNotificationsPerPublish = maxNotificationsPerPublish;

    if (syncState != SyncState.INITIAL) {
      if (modifications == null) {
        modifications = new Modifications();
      }

      modifications.maxNotificationsPerPublish = maxNotificationsPerPublish;

      syncState = SyncState.UNSYNCHRONIZED;
    }
  }

  /**
   * Set whether the LifetimeCount and MaxKeepAliveCount should be calculated automatically any time
   * the PublishingInterval is set.
   *
   * @param lifetimeAndKeepAliveCalculated {@code true} if the LifetimeCount and MaxKeepAliveCount
   *     should be calculated automatically.
   * @see #isLifetimeAndKeepAliveCalculated()
   */
  public void setLifetimeAndKeepAliveCalculated(boolean lifetimeAndKeepAliveCalculated) {
    this.lifetimeAndKeepAliveCalculated = lifetimeAndKeepAliveCalculated;
  }

  /**
   * @return {@code true} if the LifetimeCount and MaxKeepAliveCount are calculated automatically
   *     any time the Publishing Interval is set.
   * @see #setLifetimeAndKeepAliveCalculated(boolean)
   */
  public boolean isLifetimeAndKeepAliveCalculated() {
    return lifetimeAndKeepAliveCalculated;
  }

  /**
   * Set the target keep-alive interval, in milliseconds, for this Subscription.
   *
   * <p>The Subscription must be configured to automatically calculate the Lifetime and KeepAlive
   * for this to have any effect. MaxKeepAliveCount and LifetimeCount will be recalculated based on
   * the target value. The Subscription settings must be synchronized before this change takes
   * effect.
   *
   * @param targetKeepAliveInterval the target keep-alive interval, in milliseconds.
   * @see #isLifetimeAndKeepAliveCalculated()
   * @see #setLifetimeAndKeepAliveCalculated(boolean)
   */
  public void setTargetKeepAliveInterval(double targetKeepAliveInterval) {
    if (isLifetimeAndKeepAliveCalculated()) {
      setMaxKeepAliveCount(calculateMaxKeepAliveCount(publishingInterval, targetKeepAliveInterval));
      setLifetimeCount(calculateLifetimeCount(maxKeepAliveCount));
    }
  }

  /**
   * Set the maximum number of MonitoredItems that can be created/modified/deleted in a single
   * service call.
   *
   * <p>This value is compared against the value read from the Server's OperationLimits object, and
   * the smaller of the two is used.
   *
   * @param maxMonitoredItemsPerCall the maximum number of MonitoredItems that can be
   *     created/modified/deleted in a single service call.
   */
  public void setMaxMonitoredItemsPerCall(UInteger maxMonitoredItemsPerCall) {
    this.maxMonitoredItemsPerCall = maxMonitoredItemsPerCall;

    // next service call will re-calculate the partition size
    monitoredItemPartitionSize.reset();
  }

  /**
   * Set the multiplier used to calculate the watchdog timeout. The multiplier is applied to the
   * keep-alive interval.
   *
   * @param watchdogMultiplier the watchdog multiplier.
   */
  public void setWatchdogMultiplier(double watchdogMultiplier) {
    this.watchdogMultiplier = Math.max(1.0, watchdogMultiplier);
  }

  /**
   * Set the {@link SubscriptionListener} for this Subscription.
   *
   * @param listener the {@link SubscriptionListener} for this Subscription.
   */
  public void setSubscriptionListener(@Nullable SubscriptionListener listener) {
    this.listener = listener;
  }

  /**
   * Associate an arbitrary user object with this Subscription.
   *
   * @param userObject the user object to associate with this Subscription.
   */
  public void setUserObject(@Nullable Object userObject) {
    this.userObject = userObject;
  }

  /**
   * @return the user object associated with this Subscription.
   */
  public Optional<Object> getUserObject() {
    return Optional.ofNullable(userObject);
  }

  /**
   * Get the TaskQueue used to deliver notifications for this Subscription.
   *
   * @return the TaskQueue used to deliver notifications for this Subscription.
   */
  public TaskQueue getDeliveryQueue() {
    return deliveryQueue;
  }

  /**
   * Reset this Subscription.
   *
   * <p>Resetting the Subscription removes it from the Client and PublishingManager, cancels the
   * watchdog timer, and sets the {@link SyncState} back to {@link SyncState#INITIAL}.
   *
   * <p>{@link OpcUaMonitoredItem}s that have been added are reset, but the collection is not
   * cleared. If the Subscription is created again, the call {@link #synchronizeMonitoredItems()} or
   * {@link #createMonitoredItems()} to create the items on the Server again.
   *
   * <p>MonitoredItems that were removed from the Subscription but not yet deleted from the Server
   * are discarded: they belong to a Subscription that no longer exists, so there is nothing left to
   * delete.
   *
   * <p>This is called automatically when the Subscription is deleted, but can also be called
   * manually when necessary if it has been determined the Subscription no longer exists on the
   * Server.
   *
   * <p>Never waits for the Server, and never waits for a lifecycle transition that is waiting for
   * the Server; see the class documentation for why the callers cannot afford it to.
   *
   * <p>A reset still never lands in the middle of a {@link #create()}: it either discards the
   * Subscription that existed before it, or the one that call goes on to create. What it no longer
   * does is wait for that call to finish first — a {@link #create()} still in flight is superseded,
   * so it fails with {@code Bad_InvalidState} and the Subscription the Server created for it is
   * deleted rather than left running with nothing able to name it. A {@link #modify()}, {@link
   * #delete()} or {@link #setPublishingMode(boolean)} in flight is superseded the same way.
   */
  public void reset() {
    synchronized (lifecycleLock) {
      // Unconditional, and before anything else: this is the only record a transition already
      // waiting for the Server has that the Subscription it was called for is gone. A create() in
      // flight has published no SyncState yet, so the SyncState check below says nothing about it,
      // but its result must be discarded all the same.
      incarnation++;

      if (syncState != SyncState.INITIAL) {
        cancelWatchdogTimer();
        client.removeSubscription(this);
        client.getPublishingManager().removeSubscription(this);

        serverState = null;
        modifications = null;

        monitoredItemPartitionSize.reset();
        monitoredItems.values().forEach(OpcUaMonitoredItem::reset);

        // MonitoredItemIds are scoped to the Subscription that no longer exists, so the items
        // pending deletion are already gone and their ids must never be sent again. Detach them
        // completely, including the ClientHandle, so they can be added to a Subscription again.
        itemsToDelete.forEach(
            item -> {
              item.reset();
              item.setClientHandle(null);
            });
        itemsToDelete.clear();

        syncState = SyncState.INITIAL;
      }
    }
  }

  /**
   * Permanently cancel the watchdog timer: the pending expiry is cancelled, the {@link
   * SessionActivityListener} is de-registered, and the timer is discarded.
   *
   * <p>This is for teardown of the Subscription itself, e.g. {@link #reset()}. It cannot be undone;
   * a new timer is only created by {@link #create()}. To suspend the timer while the Session is
   * unavailable use {@link #pauseWatchdogTimer()} instead.
   */
  synchronized void cancelWatchdogTimer() {
    WatchdogTimer watchdog = this.watchdogTimer;
    if (watchdog != null) {
      client.removeSessionActivityListener(watchdog);
      watchdog.cancel();
      this.watchdogTimer = null;
      logger.debug(
          "id={}, watchdog timer cancelled",
          getServerState().map(ServerState::getSubscriptionId).orElse(null));
    }
  }

  /**
   * Suspend the watchdog timer: the pending expiry is cancelled, but the timer remains registered
   * as a {@link SessionActivityListener} and is re-armed when the Session becomes active again.
   *
   * <p>This is for temporary Session unavailability, where no PublishResponse can arrive and the
   * Server's keep-alive obligation is therefore in abeyance, but the Subscription itself may well
   * survive (e.g. via TransferSubscriptions once the Session is re-activated).
   */
  synchronized void pauseWatchdogTimer() {
    WatchdogTimer watchdog = this.watchdogTimer;
    if (watchdog != null) {
      watchdog.pause();
      logger.debug(
          "id={}, watchdog timer paused",
          getServerState().map(ServerState::getSubscriptionId).orElse(null));
    }
  }

  synchronized void resetWatchdogTimer() {
    WatchdogTimer watchdog = this.watchdogTimer;
    if (watchdog != null) {
      watchdog.reset();
      logger.trace(
          "id={}, watchdog timer reset",
          getServerState().map(ServerState::getSubscriptionId).orElse(null));
    }
  }

  private static UInteger calculateMaxKeepAliveCount(
      double publishingInterval, double targetKeepAliveInterval) {
    // Send a keep-alive every targetKeepAliveInterval milliseconds if the publishing
    // interval is faster, or every publishing interval otherwise.
    int count = (int) Math.ceil(targetKeepAliveInterval / Math.max(1, publishingInterval));

    return uint(Math.max(1, count));
  }

  private static UInteger calculateLifetimeCount(UInteger maxKeepAliveCount) {
    // Lifetime must be 3x (or greater) the keep-alive count.
    BigInteger lifetimeCount =
        maxKeepAliveCount
            .toBigInteger()
            .multiply(BigInteger.valueOf(5))
            .min(BigInteger.valueOf(UInteger.MAX_VALUE));

    return uint(lifetimeCount.longValue());
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", OpcUaSubscription.class.getSimpleName() + "[", "]")
        .add("subscriptionId=" + getSubscriptionId().orElse(null))
        .add("syncState=" + syncState)
        .toString();
  }

  void notifyDataReceived(MonitoredItemNotification[] notifications) {
    var items = new ArrayList<OpcUaMonitoredItem>(notifications.length);
    var values = new ArrayList<DataValue>(notifications.length);

    for (MonitoredItemNotification notification : notifications) {
      UInteger clientHandle = notification.getClientHandle();
      OpcUaMonitoredItem item = monitoredItems.get(clientHandle);
      if (item != null) {
        items.add(item);
        values.add(notification.getValue());
      } else {
        // This can happen if an item is deleted while a notification is in-flight.
        logger.debug(
            "id={}, received data for unknown ClientHandle: {}",
            getServerState().map(ServerState::getSubscriptionId).orElse(null),
            clientHandle);
      }
    }

    SubscriptionListener listener = this.listener;
    if (listener != null) {
      // Unmodifiable views: the Lists below are the ones the fan-out iterates.
      List<OpcUaMonitoredItem> itemsView = Collections.unmodifiableList(items);
      List<DataValue> valuesView = Collections.unmodifiableList(values);

      deliverToListener(
          "onDataReceived", () -> listener.onDataReceived(this, itemsView, valuesView));
    }

    for (int i = 0; i < items.size(); i++) {
      OpcUaMonitoredItem item = items.get(i);
      DataValue value = values.get(i);
      deliverToListener("DataValueListener", () -> item.notifyDataValueReceived(value));
    }
  }

  void notifyEventsReceived(EventFieldList[] events) {
    var items = new ArrayList<OpcUaMonitoredItem>(events.length);
    var eventValuesList = new ArrayList<Variant[]>(events.length);

    for (EventFieldList event : events) {
      UInteger clientHandle = event.getClientHandle();
      OpcUaMonitoredItem item = monitoredItems.get(clientHandle);
      if (item != null) {
        items.add(item);
        eventValuesList.add(event.getEventFields());
      } else {
        // This can happen if an item is deleted while a notification is in-flight.
        logger.debug(
            "id={}, received event for unknown ClientHandle: {}",
            getServerState().map(ServerState::getSubscriptionId).orElse(null),
            clientHandle);
      }
    }

    SubscriptionListener listener = this.listener;
    if (listener != null) {
      // Unmodifiable views: the Lists below are the ones the fan-out iterates.
      List<OpcUaMonitoredItem> itemsView = Collections.unmodifiableList(items);
      List<Variant[]> fieldsView = Collections.unmodifiableList(eventValuesList);

      deliverToListener(
          "onEventReceived", () -> listener.onEventReceived(this, itemsView, fieldsView));
    }

    for (int i = 0; i < items.size(); i++) {
      OpcUaMonitoredItem item = items.get(i);
      Variant[] eventValues = eventValuesList.get(i);
      deliverToListener("EventValueListener", () -> item.notifyEventValuesReceived(eventValues));
    }
  }

  /**
   * Called from {@link PublishingManager} while already executing on {@link #deliveryQueue}, so the
   * listener is invoked inline to keep it ordered with the data and event callbacks and inside the
   * delivery task the backpressure mechanism waits on.
   */
  void notifyKeepAliveReceived() {
    SubscriptionListener listener = this.listener;
    if (listener != null) {
      deliverToListener("onKeepAliveReceived", () -> listener.onKeepAliveReceived(this));
    }
  }

  /**
   * Called from {@link PublishingManager} while already executing on {@link #deliveryQueue}, so the
   * listener is invoked inline to keep it ordered with the data and event callbacks and inside the
   * delivery task the backpressure mechanism waits on.
   */
  void notifyStatusChanged(StatusCode status) {
    if (status.getValue() == StatusCodes.Bad_Timeout) {
      reset();
    }

    SubscriptionListener listener = this.listener;
    if (listener != null) {
      deliverToListener("onStatusChanged", () -> listener.onStatusChanged(this, status));
    }
  }

  /**
   * Unlike the notification callbacks above, this is called from <i>off</i> the {@link
   * #deliveryQueue} and must therefore be enqueued onto it.
   */
  void notifyNotificationDataLost() {
    SubscriptionListener listener = this.listener;
    if (listener != null) {
      deliveryQueue.execute(() -> listener.onNotificationDataLost(this));
    }
  }

  /**
   * Unlike the notification callbacks above, this is called from <i>off</i> the {@link
   * #deliveryQueue} and must therefore be enqueued onto it.
   */
  public void notifyTransferFailed(StatusCode status) {
    reset();

    SubscriptionListener listener = this.listener;
    if (listener != null) {
      deliveryQueue.execute(() -> listener.onTransferFailed(this, status));
    }
  }

  /**
   * Invoke an application-supplied callback, containing any Exception it throws.
   *
   * <p>The {@link SubscriptionListener} and each MonitoredItem's listener are independent sinks for
   * the same notification: one of them failing must not cost the others their notification, and
   * must not abort delivery of the remaining NotificationData in the same NotificationMessage.
   *
   * @param callback the name of the callback being invoked, for logging.
   * @param delivery the callback invocation.
   */
  private void deliverToListener(String callback, Runnable delivery) {
    try {
      delivery.run();
    } catch (Exception e) {
      logger.warn(
          "id={}, {} threw an unhandled Exception",
          getServerState().map(ServerState::getSubscriptionId).orElse(null),
          callback,
          e);
    }
  }

  private static class Modifications {

    private volatile @Nullable Double publishingInterval;
    private volatile @Nullable UInteger lifetimeCount;
    private volatile @Nullable UInteger maxKeepAliveCount;
    private volatile @Nullable UInteger maxNotificationsPerPublish;
    private volatile @Nullable UByte priority;

    private Optional<Double> publishingInterval() {
      return Optional.ofNullable(publishingInterval);
    }

    private Optional<UInteger> lifetimeCount() {
      return Optional.ofNullable(lifetimeCount);
    }

    private Optional<UInteger> maxKeepAliveCount() {
      return Optional.ofNullable(maxKeepAliveCount);
    }

    private Optional<UInteger> maxNotificationsPerPublish() {
      return Optional.ofNullable(maxNotificationsPerPublish);
    }

    private Optional<UByte> priority() {
      return Optional.ofNullable(priority);
    }
  }

  private class WatchdogTimer implements SessionActivityListener {

    /**
     * Guards every transition of this timer. The state below is read and written by unrelated
     * threads - the transport executor completing a PublishResponse, the Session FSM notifying
     * activity listeners, and the scheduled executor running an expiry - and each transition spans
     * a cancel/schedule/store sequence that must be atomic as a whole.
     */
    private final Object lock = new Object();

    /** The pending expiry, or {@code null} if the timer is not armed. Guarded by {@link #lock}. */
    private @Nullable ScheduledFuture<?> scheduledFuture;

    /** Terminal once set: a cancelled timer never arms again. Guarded by {@link #lock}. */
    private boolean cancelled = false;

    /**
     * Incremented on every transition, and captured by each expiry when it is scheduled, so that an
     * expiry which has already begun running - and which {@code ScheduledFuture.cancel(false)}
     * therefore cannot stop - is recognised as stale and ignored. Guarded by {@link #lock}.
     */
    private long epoch = 0L;

    void reset() {
      synchronized (lock) {
        if (cancelled) {
          return;
        }

        cancelPending();
        scheduleNext();
      }
    }

    /** Cancel the pending expiry, leaving the timer able to arm again. */
    void pause() {
      synchronized (lock) {
        cancelPending();
      }
    }

    /** Cancel the pending expiry permanently; subsequent calls to {@link #reset()} are no-ops. */
    void cancel() {
      synchronized (lock) {
        cancelled = true;

        cancelPending();
      }
    }

    /** Must be called while holding {@link #lock}. */
    private void cancelPending() {
      epoch++;

      ScheduledFuture<?> sf = scheduledFuture;
      if (sf != null) {
        sf.cancel(false);
        scheduledFuture = null;
      }
    }

    /** Must be called while holding {@link #lock}. */
    private void scheduleNext() {
      getServerState()
          .ifPresent(
              state -> {
                long delay =
                    Math.round(
                        (state.publishingInterval * (state.maxKeepAliveCount.longValue() + 1))
                            * watchdogMultiplier);

                long scheduledEpoch = epoch;

                scheduledFuture =
                    client
                        .getTransport()
                        .getConfig()
                        .getScheduledExecutor()
                        .schedule(
                            () -> notifyWatchdogTimerElapsed(scheduledEpoch, delay),
                            delay,
                            TimeUnit.MILLISECONDS);

                logger.debug(
                    "id={} watchdog timer scheduled for +{}ms", state.subscriptionId, delay);
              });
    }

    private void notifyWatchdogTimerElapsed(long scheduledEpoch, long delay) {
      synchronized (lock) {
        if (cancelled || scheduledEpoch != epoch) {
          // This expiry was cancelled or superseded while it was already running.
          return;
        }

        scheduledFuture = null;
      }

      SubscriptionListener listener = OpcUaSubscription.this.listener;

      if (listener != null) {
        deliveryQueue.execute(
            () -> {
              logger.debug(
                  "id={}, watchdog timer expired after {}ms",
                  getServerState().map(ServerState::getSubscriptionId).orElse(null),
                  delay);

              listener.onWatchdogTimerElapsed(OpcUaSubscription.this);
            });
      }
    }

    @Override
    public void onSessionActive(UaSession session) {
      reset();
      logger.debug(
          "id={}, watchdog timer reset via onSessionActive()",
          getServerState().map(ServerState::getSubscriptionId).orElse(null));
    }

    @Override
    public void onSessionInactive(UaSession session) {
      pause();
      logger.debug(
          "id={}, watchdog timer paused via onSessionInactive()",
          getServerState().map(ServerState::getSubscriptionId).orElse(null));
    }
  }

  /**
   * The state of the Subscription as it exists on the server, after the most recent successful
   * operation.
   */
  public static class ServerState {

    private final UInteger subscriptionId;
    private final Double publishingInterval;
    private final UInteger lifetimeCount;
    private final UInteger maxKeepAliveCount;
    private final UInteger maxNotificationsPerPublish;
    private final UByte priority;
    private final boolean publishingEnabled;

    private ServerState(
        UInteger subscriptionId,
        Double publishingInterval,
        UInteger lifetimeCount,
        UInteger maxKeepAliveCount,
        UInteger maxNotificationsPerPublish,
        UByte priority,
        boolean publishingEnabled) {

      this.subscriptionId = subscriptionId;
      this.publishingInterval = publishingInterval;
      this.lifetimeCount = lifetimeCount;
      this.maxKeepAliveCount = maxKeepAliveCount;
      this.maxNotificationsPerPublish = maxNotificationsPerPublish;
      this.priority = priority;
      this.publishingEnabled = publishingEnabled;
    }

    public UInteger getSubscriptionId() {
      return subscriptionId;
    }

    public Double getPublishingInterval() {
      return publishingInterval;
    }

    public UInteger getLifetimeCount() {
      return lifetimeCount;
    }

    public UInteger getMaxKeepAliveCount() {
      return maxKeepAliveCount;
    }

    public UInteger getMaxNotificationsPerPublish() {
      return maxNotificationsPerPublish;
    }

    public UByte getPriority() {
      return priority;
    }

    public boolean isPublishingEnabled() {
      return publishingEnabled;
    }
  }

  public enum SyncState {

    /** The Subscription has been instantiated but does not exist on the server. */
    INITIAL,

    /**
     * The Subscription has been created on the server and has no outstanding modifications to
     * synchronize.
     */
    SYNCHRONIZED,

    /**
     * The Subscription has been created on the server but has outstanding modifications to
     * synchronize.
     */
    UNSYNCHRONIZED
  }

  public interface SubscriptionListener {

    /**
     * Called when a Subscription receives a data change notification from the Server.
     *
     * <p>Take care not to block unnecessarily in this callback because subscription notifications
     * are processed synchronously as a backpressure mechanism. Blocking inside this callback will
     * prevent subsequent notifications from being processed and new PublishRequests from being
     * sent.
     *
     * @param subscription the Subscription that received the data change notification.
     * @param items the List of MonitoredItems targeted by the data change notification.
     * @param values the corresponding List of DataValues for the MonitoredItems.
     */
    default void onDataReceived(
        OpcUaSubscription subscription, List<OpcUaMonitoredItem> items, List<DataValue> values) {}

    /**
     * Called when a Subscription receives an event notification from the Server.
     *
     * <p>Take care not to block unnecessarily in this callback because subscription notifications
     * are processed synchronously as a backpressure mechanism. Blocking inside this callback will
     * prevent subsequent notifications from being processed and new PublishRequests from being
     * sent.
     *
     * @param subscription the Subscription that received the event notification.
     * @param items the List of MonitoredItems targeted by the event notification.
     * @param fields the corresponding List of EventFields for the MonitoredItems.
     */
    default void onEventReceived(
        OpcUaSubscription subscription, List<OpcUaMonitoredItem> items, List<Variant[]> fields) {}

    /**
     * Called when a Subscription receives a keep-alive notification from the Server.
     *
     * <p>Take care not to block unnecessarily in this callback because subscription notifications
     * are processed synchronously as a backpressure mechanism. Blocking inside this callback will
     * prevent subsequent notifications from being processed and new PublishRequests from being
     * sent.
     *
     * @param subscription the Subscription that received the keep-alive notification.
     */
    default void onKeepAliveReceived(OpcUaSubscription subscription) {}

    /**
     * Called when attempts to recover missed data notifications have failed, i.e. the Republish
     * service was called for one or more missing sequence numbers but the Server was unable to
     * fulfill the requests.
     *
     * @param subscription the Subscription that missed data notifications.
     */
    default void onNotificationDataLost(OpcUaSubscription subscription) {}

    /**
     * The Subscription's watchdog timer has elapsed.
     *
     * <p>The timer elapses when the configurable multiplier applied to the keep-alive interval has
     * elapsed without receiving a PublishResponse for this Subscription.
     *
     * <p>This is an indication that the Server may be experiencing problems servicing this
     * Subscription, and the absence of data change notifications no longer implies that values are
     * not changing. Consider deleting and creating a new Subscription.
     *
     * @param subscription the Subscription whose watchdog timer has elapsed.
     */
    default void onWatchdogTimerElapsed(OpcUaSubscription subscription) {}

    /**
     * Called when the status of the Subscription has changed.
     *
     * <p>Expected status updates include:
     *
     * <ul>
     *   <li>Bad_Timeout: the Subscription has timed out and no longer exists on the Server.
     *   <li>Good_Transferred: the Subscription was transferred to another Session.
     * </ul>
     *
     * <p>Take care not to block unnecessarily in this callback because subscription notifications
     * are processed synchronously as a backpressure mechanism. Blocking inside this callback will
     * prevent subsequent notifications from being processed and new PublishRequests from being
     * sent.
     *
     * @param subscription the Subscription whose status has changed.
     * @param status the new status of the Subscription.
     */
    default void onStatusChanged(OpcUaSubscription subscription, StatusCode status) {}

    /**
     * Called when a new Session is established after reconnecting, but transferring this
     * Subscription to the new Session was unsuccessful.
     *
     * @param subscription the Subscription that failed to transfer to the new Session.
     * @param status the {@link StatusCode} for the transfer failure.
     */
    default void onTransferFailed(OpcUaSubscription subscription, StatusCode status) {}
  }
}
