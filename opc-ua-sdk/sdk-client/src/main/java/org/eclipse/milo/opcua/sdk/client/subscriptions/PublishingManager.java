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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFieldList;
import org.eclipse.milo.opcua.stack.core.types.structured.EventNotificationList;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.NotificationMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionAcknowledgement;
import org.eclipse.milo.opcua.stack.core.util.TaskQueue;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Keeps a Session supplied with PublishRequests and dispatches the PublishResponses that answer
 * them to the Subscriptions they belong to.
 *
 * <p>Every method is safe to call from any thread. The work one PublishResponse generates is split
 * across three execution contexts, and the Javadoc of each method below names the one it runs in:
 * the transport's serial PublishResponse handler, which is where the order the Server sent
 * NotificationMessages in still exists and where nothing may block; each Subscription's own serial
 * processing queue, where sequence-number accounting and Republish recovery happen; and each
 * Subscription's own serial delivery queue, where application callbacks are invoked.
 *
 * <p>A registration ({@link SubscriptionDetails}) is bound to the SubscriptionId it was registered
 * under and is never reused: {@link OpcUaSubscription#reset()} unregisters it, and a subsequent
 * {@link OpcUaSubscription#create()} registers a new one, even though the same {@link
 * OpcUaSubscription} object represents both. Part 4 §5.13.1.1 makes the SubscriptionId "the
 * Server-assigned identifier for the Subscription", so a NotificationMessage received under one
 * SubscriptionId is not a statement about any other, and work queued for one registration must
 * never be applied to another. That is why nothing here asks the live Subscription object what its
 * current SubscriptionId is: the id an entry is registered under is captured once, when the entry
 * is created, and every question about identity is answered from it.
 */
public class PublishingManager {

  /**
   * Upper bound on the number of missing NotificationMessages the client will try to recover from a
   * PublishResponse when the Server does not tell it what it is holding, i.e. when the response
   * carries no availableSequenceNumbers.
   *
   * <p>Recovery is one Republish call per missing message, so the gap has to be bounded by
   * something: a sequence number that is far ahead — because it is corrupt, or because it comes
   * from a Subscription whose state the client has lost track of — would otherwise cost up to 2^32
   * round trips. When the Server does advertise availableSequenceNumbers, that list is the bound.
   */
  private static final long DEFAULT_MAX_RECOVERABLE_GAP = 64L;

  /**
   * The ceiling of a {@link PendingPublishCeiling} while the Server has never refused a
   * PublishRequest for holding too many, i.e. while nothing but the client's own target applies.
   */
  private static final long NO_PENDING_PUBLISH_CEILING = Long.MAX_VALUE;

  /**
   * The number of successful Publish round trips that must pass before the client asks a Server
   * that once refused a PublishRequest whether it would now queue one more.
   *
   * <p>Counted in the Server's own answers rather than in elapsed time: a round trip is a
   * PublishRequest the Server accepted, answered, and had the answer delivered from, which is
   * exactly the evidence that the condition behind the refusal may have passed, and it needs no
   * clock.
   *
   * <p>Eight is chosen to be comfortably clear of the window Part 4 §5.14.5.1 is measured in — "one
   * of its outstanding Publish requests is returned" buys exactly one replacement, so the immediate
   * response to the fault is a property of the first returning request — while still being a small
   * number of NotificationMessages: a Subscription publishing once a second reaches the first probe
   * in under ten seconds.
   */
  private static final long PROBE_COOLDOWN_BASE_SUCCESSES = 8L;

  /**
   * The factor the cooldown grows by when a probe is refused.
   *
   * <p>Doubling is what bounds the cost of leniency: a Server that always refuses is asked at
   * roughly successes 8, 24, 56, 120, 248, ..., so the number of probes it sees grows only
   * logarithmically in the number of responses it delivers, and the interval between two probes is
   * never shorter than the interval before it.
   */
  private static final long PROBE_COOLDOWN_GROWTH = 2L;

  /**
   * The longest cooldown a repeatedly refused probe can grow to, in successful Publish round trips.
   *
   * <p>A cap is needed because the alternative to a bounded backoff is one that gives up: without
   * it the interval doubles until a Server whose cap really was raised is never asked again. At 512
   * a Subscription publishing once a second probes about every eight minutes in the worst case,
   * which costs a Server that will never accept more essentially nothing and still finds a
   * condition that has passed within minutes.
   */
  private static final long PROBE_COOLDOWN_MAX_SUCCESSES = 512L;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final ConcurrentMap<NodeId, AtomicLong> pendingCountMap = new ConcurrentHashMap<>();

  private final Object registrationLock = new Object();

  // Replace the immutable map only when registration changes. Each Publish request can retain
  // its identity snapshot with one volatile read instead of copying the registry on the hot path.
  private volatile Map<UInteger, SubscriptionDetails> subscriptionDetails = Map.of();

  /**
   * Incremented every time a Subscription is registered with or unregistered from this manager.
   *
   * <p>Each PublishRequest records the value in effect when it was sent, which is what lets a
   * failure be told apart from a failure that is still relevant: a Server's answer describes the
   * Subscription set the request was sent for, and once that set has changed the answer no longer
   * describes the client.
   */
  private final AtomicLong subscriptionGeneration = new AtomicLong(0L);

  /**
   * What the client has learned from Bad_TooManyPublishRequests about how many PublishRequests the
   * Server will queue for this Session, and what it has to see before asking for one more.
   */
  private final AtomicReference<PendingPublishCeiling> pendingPublishCeiling =
      new AtomicReference<>(PendingPublishCeiling.none(0L));

  /**
   * The number of times a Session has become Active, i.e. the number of times the client has had to
   * recover the Subscriptions it holds before resuming Publish traffic.
   */
  private final AtomicLong sessionActivations = new AtomicLong(0L);

  /**
   * The deepest Publish pipeline Milo has permitted for this client.
   *
   * <p>Recorded when the target is chosen, before requests can fail and release their permits. A
   * Session-inactive callback is dispatched asynchronously and may run only after those failures
   * and concurrent Subscription removals have made both the outstanding count and current target
   * smaller, so it is too late to reconstruct the depth the outage may have lost. Keeping the
   * lifetime high-water mark can overestimate a later, shallower outage, but the first
   * Bad_MessageNotAvailable still terminates recovery immediately and the safety bound remains
   * finite.
   */
  private final AtomicLong maxPermittedPublishPipelineDepth = new AtomicLong(0L);

  /**
   * Whether a Session has ever become inactive, and therefore whether an activation is a
   * <i>re</i>-activation that the Part 4 §6.7 Republish drain has a reason to run for.
   *
   * <p>On the first activation the Session, and every Subscription registered against it, were
   * created moments earlier on that same Session: the Server cannot be holding a
   * NotificationMessage the client has not collected, so the drain can only spend a round trip per
   * Subscription being told Bad_MessageNotAvailable. It is not merely wasteful — the drain holds
   * the publish gate shut until it ends (see {@link #isPublishingAllowed(UaSession)}), so it also
   * delays the first PublishRequest of every Subscription created before the {@code
   * onSessionActive} callbacks run. That window is reachable: {@code connect()} returns when the
   * Session future completes, which happens in a task submitted before the callback fan-out.
   */
  private final AtomicBoolean sessionEverInactive = new AtomicBoolean(false);

  /**
   * The newest Session activation whose recovery has finished, one way or the other, and the
   * Session that recovery ran on.
   *
   * <p>Publish traffic is allowed only while this describes the Session a request would be sent on;
   * see {@link #isPublishingAllowed(UaSession)}. The activation must have caught up with {@link
   * #sessionActivations} <i>and</i> the Session in hand must be the one recovered: an activation is
   * only counted when the {@code onSessionActive} callbacks run, but the Session future completes
   * in a separate task that can release callbacks parked on it first, and in that window the
   * counters alone still describe the previous activation. The Session reference is cleared the
   * moment its Session becomes inactive, because a re-activation can hand back the same Session
   * object, and in the same window the reference alone would still match it.
   *
   * <p>The pair is monotonic, and the recovery of the newest activation always finishes, so the
   * pipeline cannot be held shut by a recovery that is over.
   */
  private final AtomicReference<RecoveredActivation> lastRecoveredActivation =
      new AtomicReference<>(new RecoveredActivation(0L, null));

  private final OpcUaClient client;

  public PublishingManager(OpcUaClient client) {
    this.client = client;

    // When a Session gets re-activated after a connection loss we need to make sure PublishRequests
    // are being sent again -- but only after every Subscription has had the chance to collect what
    // the Server generated while the Session was unusable. See recoverAndResumePublishing().
    client.addSessionActivityListener(
        new SessionActivityListener() {
          @Override
          public void onSessionActive(UaSession session) {
            recoverAndResumePublishing(session);
          }

          @Override
          public void onSessionInactive(UaSession session) {
            // From here on an activation is a re-activation, and the Server may be holding
            // NotificationMessages generated while the Session was unusable.
            sessionEverInactive.set(true);

            // The Session is gone, and the recovery that ran for it says nothing about the next
            // activation — which may hand back this very Session object, re-activated. Shut the
            // gate here rather than when the next activation is counted: the next Session future
            // can complete, releasing callbacks parked on it, before the activation callbacks run.
            lastRecoveredActivation.getAndUpdate(
                r -> r.session() == session ? new RecoveredActivation(r.activation(), null) : r);
          }
        });
  }

  /**
   * Register {@code subscription} under the SubscriptionId it currently holds.
   *
   * <p>Called by {@link OpcUaSubscription#create()} while it holds that Subscription's lifecycle
   * lock, so the id read here is the one the Server just assigned and cannot be cleared by a
   * concurrent {@link OpcUaSubscription#reset()} before the entry is created.
   *
   * @param subscription the Subscription to register.
   */
  void addSubscription(OpcUaSubscription subscription) {
    Executor executor = client.getTransport().getConfig().getExecutor();

    subscription
        .getSubscriptionId()
        .ifPresent(
            id -> {
              // Construct outside registrationLock because this reads the subscription lifecycle.
              var details = new SubscriptionDetails(subscription, id, executor);
              synchronized (registrationLock) {
                var updated = new HashMap<>(subscriptionDetails);
                SubscriptionDetails displaced = updated.put(id, details);
                if (displaced != null) {
                  displaced.registered = false;
                }
                subscriptionDetails = Map.copyOf(updated);
                subscriptionGeneration.incrementAndGet();
              }
            });

    // The client wants a deeper pipeline than it did when any ceiling was learned, and Part 4
    // §5.14.5.1 requires a Server to accept at least one more queued PublishRequest per
    // Subscription, so it is worth finding out whether this one now will — immediately, rather than
    // after the cooldown a probe would have to serve.
    pendingPublishCeiling.set(PendingPublishCeiling.none(sessionActivations.get()));

    maybeSendPublishRequests();
  }

  /**
   * Unregister every entry held for {@code subscription}.
   *
   * <p>Entries are matched by identity rather than by the Subscription's current SubscriptionId:
   * the caller is discarding this Subscription, and an entry that outlived the id it was registered
   * under would keep answering PublishResponses for a Subscription the client no longer has.
   *
   * @param subscription the Subscription to unregister.
   */
  void removeSubscription(OpcUaSubscription subscription) {
    for (SubscriptionDetails details : subscriptionDetails.values()) {
      if (details.subscription == subscription) {
        unregister(details);
      }
    }

    maybeSendPublishRequests();
  }

  /**
   * Remove {@code details} from the registry, if it is still the entry registered under its
   * SubscriptionId, and mark it unregistered so that work already queued for it is discarded rather
   * than applied to whatever Subscription exists by the time it runs.
   *
   * @param details the entry to unregister.
   * @return {@code true} if {@code details} was still the registered entry and this call removed
   *     it; {@code false} if something else unregistered it first.
   */
  private boolean unregister(SubscriptionDetails details) {
    synchronized (registrationLock) {
      if (subscriptionDetails.get(details.subscriptionId) != details) {
        return false;
      }

      var updated = new HashMap<>(subscriptionDetails);
      updated.remove(details.subscriptionId);
      details.registered = false;
      subscriptionDetails = Map.copyOf(updated);
      subscriptionGeneration.incrementAndGet();
      return true;
    }
  }

  /**
   * Record what a Server said it was still holding for {@code subscriptionId} when it accepted a
   * TransferSubscriptions request for it.
   *
   * <p>Part 4 §5.14.7.1 gives each successful TransferResult "the sequence numbers of the
   * NotificationMessages that are available for retransmission". It is the exact input the
   * reconnect recovery needs: it says which NotificationMessages the Republish loop of Part 4 §6.7
   * can still collect, and therefore where that loop starts and where it stops.
   *
   * <p>Called by the Session FSM while the Session that transfer was part of is still on its way to
   * Active, so the list is in place before the recovery that consumes it runs.
   *
   * @param session the Session the Subscription was transferred to.
   * @param subscriptionId the SubscriptionId that was transferred.
   * @param availableSequenceNumbers the availableSequenceNumbers of its TransferResult.
   */
  public void notifySubscriptionTransferred(
      UaSession session, UInteger subscriptionId, UInteger @Nullable [] availableSequenceNumbers) {

    SubscriptionDetails details = subscriptionDetails.get(subscriptionId);

    if (details != null) {
      details.transferredSequenceNumbers.set(
          new TransferredSequenceNumbers(session, availableSequenceNumbers));
    }
  }

  private void maybeSendPublishRequests() {
    long maxPendingPublishes = getMaxPendingPublishes();

    if (maxPendingPublishes > 0) {
      maxPermittedPublishPipelineDepth.accumulateAndGet(maxPendingPublishes, Math::max);

      client
          .getSessionAsync()
          .whenComplete(
              (session, ex) -> {
                if (session != null) {
                  if (!isPublishingAllowed(session)) {
                    // The Republish loop Part 4 §6.7 requires before Publish resumes has not
                    // finished yet for this Session. Tested here, once the Session is in hand,
                    // rather than on the way in: a caller that found no Session is parked on the
                    // one being established, and must not send the instant it arrives either.
                    // Whatever deficit builds up while the pipeline is shut is made good by
                    // resumePublishing(), which every recovery reaches.
                    logger.debug("Publish suspended pending reconnect recovery");
                    return;
                  }

                  AtomicLong pendingCount =
                      pendingCountMap.computeIfAbsent(
                          session.getSessionId(), id -> new AtomicLong(0L));

                  for (long i = pendingCount.get(); i < maxPendingPublishes; i++) {
                    if (pendingCount.incrementAndGet() <= maxPendingPublishes) {
                      sendPublishRequest(session, pendingCount);
                    } else {
                      pendingCount.getAndUpdate(p -> (p > 0) ? p - 1 : 0);
                    }
                  }

                  if (pendingCountMap.size() > 1) {
                    // Prune any old sessions...
                    pendingCountMap
                        .entrySet()
                        .removeIf(e -> !e.getKey().equals(session.getSessionId()));
                  }
                } else {
                  logger.debug("Session not available", ex);

                  pendingCountMap.clear();
                }
              });
    }
  }

  /**
   * @param session the Session a PublishRequest would be sent on.
   * @return {@code true} if PublishRequests may be sent on {@code session}, i.e. if every Session
   *     activation so far has had its reconnect recovery run and {@code session} is the Session the
   *     newest of those recoveries ran on.
   */
  private boolean isPublishingAllowed(UaSession session) {
    RecoveredActivation recovered = lastRecoveredActivation.get();

    return recovered.activation() >= sessionActivations.get() && recovered.session() == session;
  }

  /**
   * @return {@code true} if Publish traffic is suspended, i.e. no PublishResponse can arrive:
   *     either no recovered Session is in hand, or an activation has been counted whose reconnect
   *     recovery has not finished. The watchdog timer is fed only by PublishResponses, so it must
   *     not be armed while this is true — see {@link OpcUaSubscription#resetWatchdogTimer()} — and
   *     {@link #resumePublishing} re-arms it when the suspension ends.
   */
  boolean isPublishingSuspended() {
    RecoveredActivation recovered = lastRecoveredActivation.get();

    return recovered.session() == null || recovered.activation() < sessionActivations.get();
  }

  /**
   * Recover every registered Subscription on the newly Active {@code session} and only then let
   * PublishRequests flow again.
   *
   * <p>Part 4 §6.7: "After re-establishing the connection the Client shall call Republish in a
   * loop, starting with the next expected sequence number and incrementing the sequence number
   * until the Server returns the status Bad_MessageNotAvailable. After the Republish returns
   * Bad_MessageNotAvailable the Client shall start sending Publish requests with the normal Publish
   * handling. This sequence ensures that the lost NotificationMessages queued in the Server are not
   * overwritten by new Publish responses." A Server's retransmission queue is finite — Part 4
   * §5.14.1.1: "In the case of a retransmission queue overflow, the oldest sent NotificationMessage
   * gets deleted" — so every NotificationMessage sent in answer to a resumed PublishRequest can
   * evict one the client has not collected yet, which is what the ordering prevents.
   *
   * <p>Runs on the transport's executor, as one of the Session activity callbacks, and returns
   * immediately: the recovery is a chain of Republish round trips and nothing here waits for it.
   *
   * @param session the Session that has just become Active.
   */
  private void recoverAndResumePublishing(UaSession session) {
    long activation = sessionActivations.incrementAndGet();

    // A Session has its own queue of PublishRequests, so what an earlier one was willing to hold
    // says nothing about this one — and neither does how reluctant it was to accept more. Tagging
    // the reset with this activation makes it atomic with respect to a delayed refusal: an old
    // failure either updates the old state before this set replaces it, or sees the new tag and is
    // ignored.
    pendingPublishCeiling.set(PendingPublishCeiling.none(activation));

    try {
      recoverSubscriptions(session, activation)
          .whenComplete((unit, ex) -> resumePublishing(activation, session));
    } catch (Exception e) {
      // Nothing is going to complete the recovery that never started, and a pipeline that stays
      // shut is worse than one that resumes without having drained: it never recovers at all.
      logger.error("Reconnect recovery could not be started", e);

      resumePublishing(activation, session);
    }
  }

  /**
   * Let PublishRequests flow again now that the recovery of Session activation {@code activation}
   * is over, and send the ones that were not sent while it ran.
   *
   * @param activation the {@link #sessionActivations} value the finished recovery belongs to.
   * @param session the Session that recovery ran on.
   */
  private void resumePublishing(long activation, UaSession session) {
    lastRecoveredActivation.getAndUpdate(
        r -> r.activation() >= activation ? r : new RecoveredActivation(activation, session));

    // The watchdog watches for the Server going quiet, but no PublishResponse — the only event
    // that feeds it — can arrive while Publish traffic is suspended, so it is re-armed only now,
    // not the moment the Session became active: a recovery longer than the watchdog delay must
    // not fire it on a Subscription whose recovery is proceeding normally.
    subscriptionDetails.values().forEach(d -> d.subscription.resetWatchdogTimer());

    maybeSendPublishRequests();
  }

  /**
   * Run the Part 4 §6.7 Republish loop for every registered Subscription, all of them at once: they
   * have independent sequence numbers, independent processing queues, and independent Republish
   * round trips, so one Subscription's recovery is not a reason for another's to wait.
   *
   * @param session the Session that has just become Active.
   * @param activation the {@link #sessionActivations} value this recovery belongs to.
   * @return a {@link CompletableFuture} that completes, never exceptionally, when the last of them
   *     is done.
   */
  private CompletableFuture<Unit> recoverSubscriptions(UaSession session, long activation) {
    List<SubscriptionDetails> details = List.copyOf(subscriptionDetails.values());

    if (details.isEmpty()) {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    // A transfer names what the Server still holds, so a Subscription carrying that list has
    // something to collect whatever else is true; see republishUntilUnavailable().
    boolean transferred =
        details.stream()
            .map(d -> d.transferredSequenceNumbers.get())
            .anyMatch(t -> t != null && t.session() == session);

    if (!sessionEverInactive.get() && !transferred) {
      logger.debug(
          "First Session activation, so nothing predates it: skipping recovery of {}"
              + " Subscription(s)",
          details.size());

      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    var futures = new ArrayList<CompletableFuture<Unit>>(details.size());

    for (SubscriptionDetails d : details) {
      futures.add(recoverSubscription(session, d, activation));
    }

    return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
        .handle((unit, ex) -> Unit.VALUE);
  }

  /**
   * Run the Part 4 §6.7 Republish loop for one Subscription.
   *
   * <p>The loop runs with the Subscription's processing queue paused, exactly as the reactive gap
   * repair in {@link #processPublishResponse} does, which is what orders the NotificationMessages
   * it collects ahead of any PublishResponse for the same Subscription: none is processed, and
   * therefore none is delivered, until the loop is done.
   *
   * @param session the Session that has just become Active.
   * @param details the {@link SubscriptionDetails} for the Subscription to recover.
   * @param activation the {@link #sessionActivations} value this recovery belongs to.
   * @return a {@link CompletableFuture} that completes, never exceptionally, when the loop is done.
   */
  private CompletableFuture<Unit> recoverSubscription(
      UaSession session, SubscriptionDetails details, long activation) {

    var recovered = new CompletableFuture<Unit>();

    boolean queued =
        details.processingQueue.execute(
            () -> {
              // Safe from here because this task is running on the queue it pauses, so no other
              // task for this Subscription can be in flight.
              details.processingQueue.pause();

              try {
                republishUntilUnavailable(session, details, activation)
                    .whenComplete((unit, ex) -> finishRecovery(details, recovered));
              } catch (Exception e) {
                logger.error(
                    "Reconnect recovery failed, subscriptionId={}", details.subscriptionId, e);

                finishRecovery(details, recovered);
              }
            });

    if (!queued) {
      // The queue is shut down or saturated, so the task above will never run and there is no loop
      // to wait for.
      recovered.complete(Unit.VALUE);
    }

    return recovered;
  }

  /**
   * Hand a Subscription's processing queue back and report its recovery finished, whatever happened
   * to it. Every branch of a recovery ends here: a paused queue that is never resumed stops the
   * Subscription being delivered to, and a recovery that is never reported finished stops the whole
   * client's Publish traffic.
   *
   * @param details the {@link SubscriptionDetails} for the Subscription that was recovered.
   * @param recovered the {@link CompletableFuture} to complete.
   */
  private static void finishRecovery(
      SubscriptionDetails details, CompletableFuture<Unit> recovered) {

    try {
      details.processingQueue.resume();
    } finally {
      recovered.complete(Unit.VALUE);
    }
  }

  /**
   * Request, one at a time, the NotificationMessages the Server generated for one Subscription
   * while the client could not collect them, starting at the sequence number expected next.
   *
   * <p>Where the Session was replaced and the Subscription transferred to it, the TransferResult
   * has already named the sequence numbers the Server still holds (Part 4 §5.14.7.1), and that list
   * is what the loop follows: it starts at the oldest of them the client is missing and ends where
   * the list does, without spending a round trip to be told about a sequence number the Server has
   * already said it does not have. Anything older than that oldest one is gone — Part 4 §5.14.1.1
   * deletes the oldest NotificationMessage first — and is reported as lost data rather than
   * requested.
   *
   * <p>Where the Session was merely re-activated there is no such list, and the loop is the one
   * Part 4 §6.7 describes: increment until the Server answers Bad_MessageNotAvailable. At most one
   * NotificationMessage could have been sent for each PublishRequest Milo allowed outstanding
   * before the disconnect, so that pipeline depth plus the terminating request is both sufficient
   * to drain everything Milo could have left unanswered and a finite bound on the loop.
   *
   * @param session the Session that has just become Active.
   * @param details the {@link SubscriptionDetails} for the Subscription to recover.
   * @param activation the {@link #sessionActivations} value this recovery belongs to.
   * @return a {@link CompletableFuture} that completes, never exceptionally, when the loop is done.
   */
  private CompletableFuture<Unit> republishUntilUnavailable(
      UaSession session, SubscriptionDetails details, long activation) {

    TransferredSequenceNumbers transferred = takeTransferredSequenceNumbers(details, session);
    UInteger[] advertised = transferred != null ? transferred.sequenceNumbers() : null;

    long expectedSequenceNumber = SequenceNumbers.successor(details.lastSequenceNumber);
    long firstSequenceNumber = expectedSequenceNumber;
    long maxRepublishes = getBlindReconnectRecoveryLimit();
    Set<Long> heldByServer = null;

    if (advertised != null && advertised.length > 0) {
      heldByServer = legalSequenceNumbers(advertised);
      maxRepublishes = heldByServer.size();

      long oldestHeld = oldestHeldAtOrAfter(heldByServer, expectedSequenceNumber);

      if (oldestHeld == SequenceNumbers.NONE) {
        logger.debug(
            "Nothing the Server advertised on transfer is missing, subscriptionId={}, "
                + "expectedSequenceNumber={}",
            details.subscriptionId,
            expectedSequenceNumber);

        return CompletableFuture.completedFuture(Unit.VALUE);
      }

      if (oldestHeld != expectedSequenceNumber) {
        logger.warn(
            "The oldest NotificationMessage the Server can retransmit after the transfer is "
                + "sequenceNumber={}, so the {} starting at sequenceNumber={} are gone; treating "
                + "them as lost data, subscriptionId={}",
            oldestHeld,
            SequenceNumbers.forwardDistance(expectedSequenceNumber, oldestHeld),
            expectedSequenceNumber,
            details.subscriptionId);

        details.lastSequenceNumber = SequenceNumbers.predecessor(oldestHeld);
        details.subscription.notifyNotificationDataLost();

        firstSequenceNumber = oldestHeld;
      }
    }

    return republishNext(
        session, details, firstSequenceNumber, maxRepublishes, heldByServer, activation);
  }

  /**
   * Consume only the TransferResult produced for {@code session}.
   *
   * <p>A recovery task can remain queued after its Session has been superseded. In that interval a
   * replacement Session can transfer the same Subscription and publish its available sequence
   * numbers. Matching by Session identity before the compare-and-set keeps the stale task from
   * taking the replacement's input; leaving a non-matching value in place lets the replacement's
   * own recovery consume it later.
   */
  private static @Nullable TransferredSequenceNumbers takeTransferredSequenceNumbers(
      SubscriptionDetails details, UaSession session) {

    while (true) {
      TransferredSequenceNumbers transferred = details.transferredSequenceNumbers.get();

      if (transferred == null || transferred.session() != session) {
        return null;
      }

      if (details.transferredSequenceNumbers.compareAndSet(transferred, null)) {
        return transferred;
      }
    }
  }

  /**
   * @return the most Republish requests a blind reconnect drain may make: one for every
   *     PublishRequest Milo permitted or actually had outstanding immediately before the outage,
   *     plus one to receive the Bad_MessageNotAvailable that terminates Part 4 §6.7's loop. Unlike
   *     a fixed default this covers a configured pipeline deeper than 64, while remaining bounded
   *     by the deepest pipeline this client has permitted.
   */
  private long getBlindReconnectRecoveryLimit() {
    long pipelineDepth =
        Math.max(maxPermittedPublishPipelineDepth.get(), getNaturalMaxPendingPublishes());

    return Math.max(1L, pipelineDepth + 1L);
  }

  /**
   * Request the NotificationMessage with {@code sequenceNumber} and, if it arrives, the one after
   * it.
   *
   * @param session the Session that has just become Active.
   * @param details the {@link SubscriptionDetails} for the Subscription being recovered.
   * @param sequenceNumber the sequence number to request.
   * @param maxRepublishes the number of requests, including this one, the loop may still make.
   * @param heldByServer the sequence numbers the Server said it holds, or {@code null} if it did
   *     not say and the loop has to run until it answers Bad_MessageNotAvailable.
   * @param activation the {@link #sessionActivations} value this recovery belongs to.
   * @return a {@link CompletableFuture} that completes, never exceptionally, when the loop is done.
   */
  private CompletableFuture<Unit> republishNext(
      UaSession session,
      SubscriptionDetails details,
      long sequenceNumber,
      long maxRepublishes,
      @Nullable Set<Long> heldByServer,
      long activation) {

    if (maxRepublishes <= 0
        || !details.registered
        || activation != sessionActivations.get()
        || (heldByServer != null && !heldByServer.contains(sequenceNumber))) {

      // Either the loop has run its course, or the Subscription is gone, or the Session this
      // recovery was for has been superseded by one with a recovery of its own.
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    return republish(session, details.subscriptionId, uint(sequenceNumber))
        .handle(
            (response, ex) -> {
              if (!details.registered || activation != sessionActivations.get()) {
                // The response belongs to a recovery that a later Session activation superseded.
                // It cannot update sequence accounting or deliver a NotificationMessage for the
                // replacement activation.
                return false;
              }

              if (ex != null) {
                StatusCode statusCode =
                    UaException.extract(ex).map(UaException::getStatusCode).orElse(StatusCode.BAD);

                if (statusCode.value() == StatusCodes.Bad_MessageNotAvailable) {
                  // The termination condition of Part 4 §6.7's loop, and not lost data: the Server
                  // is holding nothing with this sequence number because it never sent one, which
                  // is the normal case when nothing was missed.
                  logger.debug(
                      "Reconnect recovery complete, subscriptionId={}, sequenceNumber={} is not "
                          + "available for retransmission",
                      details.subscriptionId,
                      sequenceNumber);
                } else {
                  logger.warn(
                      "Republish service failure during reconnect recovery, subscriptionId={}, "
                          + "sequenceNumber={}: {}",
                      details.subscriptionId,
                      sequenceNumber,
                      statusCode);
                }

                return false;
              }

              // The processing queue is paused for the duration of the loop, so nothing else is
              // accounting for this Subscription's sequence numbers and nothing else is queueing
              // deliveries for it. Advancing lastSequenceNumber here is what stops the first
              // PublishResponse after the recovery from finding a gap the recovery has just closed
              // and delivering these NotificationMessages a second time.
              details.availableAcknowledgements.add(uint(sequenceNumber));
              details.lastSequenceNumber = sequenceNumber;

              NotificationMessage notificationMessage = response.getNotificationMessage();

              details
                  .subscription
                  .getDeliveryQueue()
                  .execute(() -> deliverNotificationMessage(details, notificationMessage));

              return true;
            })
        .thenCompose(
            recovered ->
                recovered
                    ? republishNext(
                        session,
                        details,
                        SequenceNumbers.successor(sequenceNumber),
                        maxRepublishes - 1,
                        heldByServer,
                        activation)
                    : CompletableFuture.completedFuture(Unit.VALUE));
  }

  /**
   * Call the Republish service on {@code session}.
   *
   * <p>Bound to the Session the recovery is for, rather than made through {@link
   * OpcUaClient#republishAsync}, which resolves the Session again for every call: a recovery that
   * outlives the Session it began on must not have its remaining requests silently re-issued on the
   * next one, which has a recovery of its own, and must never park waiting for a Session that does
   * not exist yet — a request parked there is a recovery that never finishes and a Publish pipeline
   * that never reopens.
   *
   * @param session the Session to send the request on.
   * @param subscriptionId the SubscriptionId to request a NotificationMessage of.
   * @param sequenceNumber the sequence number to request.
   * @return a {@link CompletableFuture} that completes with the {@link RepublishResponse}.
   */
  private CompletableFuture<RepublishResponse> republish(
      UaSession session, UInteger subscriptionId, UInteger sequenceNumber) {

    var request =
        new RepublishRequest(
            client.newRequestHeader(session.getAuthenticationToken()),
            subscriptionId,
            sequenceNumber);

    return client.sendRequestAsync(request).thenApply(RepublishResponse.class::cast);
  }

  /**
   * @param sequenceNumbers the availableSequenceNumbers of a TransferResult.
   * @return the legal sequence numbers among them.
   */
  private static Set<Long> legalSequenceNumbers(UInteger[] sequenceNumbers) {
    var legal = new HashSet<Long>(sequenceNumbers.length);

    for (UInteger sequenceNumber : sequenceNumbers) {
      if (sequenceNumber != null && SequenceNumbers.isLegal(sequenceNumber.longValue())) {
        legal.add(sequenceNumber.longValue());
      }
    }

    return legal;
  }

  /**
   * @param heldByServer the sequence numbers the Server said it holds.
   * @param expectedSequenceNumber the sequence number the client expects next.
   * @return the oldest of {@code heldByServer} that the client has not accounted for, or {@link
   *     SequenceNumbers#NONE} if the Server holds nothing the client is missing.
   */
  private static long oldestHeldAtOrAfter(Set<Long> heldByServer, long expectedSequenceNumber) {
    long oldest = SequenceNumbers.NONE;
    long oldestDistance = Long.MAX_VALUE;

    for (long sequenceNumber : heldByServer) {
      if (sequenceNumber != expectedSequenceNumber
          && !SequenceNumbers.isAhead(sequenceNumber, expectedSequenceNumber)) {
        // Already accounted for: received, recovered, or given up on.
        continue;
      }

      long distance = SequenceNumbers.forwardDistance(expectedSequenceNumber, sequenceNumber);

      if (distance < oldestDistance) {
        oldestDistance = distance;
        oldest = sequenceNumber;
      }
    }

    return oldest;
  }

  void sendPublishRequest(OpcUaSession session, AtomicLong pendingCount) {
    // Acknowledgements are removed from the Subscription's queue as they are drained into this
    // request, so this request now owns them: if it fails they have to be put back, or the client
    // has silently decided never to acknowledge those NotificationMessages.
    var drainedAcknowledgements = new ArrayList<DrainedAcknowledgements>();

    // Read before the request is built, so that a Subscription registered or unregistered while it
    // is in flight is seen as a change by the failure handler rather than missed.
    long generation = subscriptionGeneration.get();
    Map<UInteger, SubscriptionDetails> requestRegistrations = subscriptionDetails;

    // Read for the same reason: a Session-level failure describes the Session the request was sent
    // on, and once another activation has been counted it no longer describes the client.
    long activation = sessionActivations.get();

    try {
      var subscriptionAcknowledgements = new ArrayList<SubscriptionAcknowledgement>();

      for (SubscriptionDetails details : subscriptionDetails.values()) {
        List<UInteger> sequenceNumbers;

        synchronized (details.availableAcknowledgements) {
          if (details.availableAcknowledgements.isEmpty()) {
            continue;
          }

          sequenceNumbers = List.copyOf(details.availableAcknowledgements);
          details.availableAcknowledgements.clear();
        }

        for (UInteger sequenceNumber : sequenceNumbers) {
          // Part 4 §5.14.5.2 pairs a sequenceNumber with the subscriptionId of the Subscription the
          // NotificationMessage was "received on", and lets the Server delete the message with that
          // sequence number from that Subscription's retransmission queue. The id the entry is
          // registered under is that Subscription; the live object's current id may by now be a
          // different one, under which these sequence numbers were never sent.
          subscriptionAcknowledgements.add(
              new SubscriptionAcknowledgement(details.subscriptionId, sequenceNumber));
        }

        drainedAcknowledgements.add(new DrainedAcknowledgements(details, sequenceNumbers));
      }

      RequestHeader requestHeader =
          client.newRequestHeader(session.getAuthenticationToken(), getTimeoutHint());

      UInteger requestHandle = requestHeader.getRequestHandle();

      var request =
          new PublishRequest(
              requestHeader,
              subscriptionAcknowledgements.toArray(new SubscriptionAcknowledgement[0]));

      if (logger.isDebugEnabled()) {
        String[] ackStrings =
            subscriptionAcknowledgements.stream()
                .map(
                    ack ->
                        String.format(
                            "id=%s/seq=%s", ack.getSubscriptionId(), ack.getSequenceNumber()))
                .toArray(String[]::new);

        logger.debug(
            "Sending PublishRequest, requestHandle={}, acknowledgements={}",
            requestHandle,
            Arrays.toString(ackStrings));
      }

      client
          .sendRequestAsync(request)
          .whenComplete(
              (response, ex) -> {
                if (response instanceof PublishResponse publishResponse) {
                  // This handler runs inline on the transport's serial PublishResponse queue (see
                  // AbstractUascClientTransport#handleResponse), which is the only place the order
                  // the Server sent NotificationMessages in still exists. Queueing the work here,
                  // rather than hopping through the general-purpose executor first, is what carries
                  // that order into the Subscription's processing queue, which is itself serial.
                  // Nothing that can block belongs in this handler.
                  logger.debug(
                      "Received PublishResponse, requestHandle={}, sequenceNumber={}",
                      publishResponse.getResponseHeader().getRequestHandle(),
                      publishResponse.getNotificationMessage().getSequenceNumber());

                  reportRefusedAcknowledgements(subscriptionAcknowledgements, publishResponse);

                  UInteger subscriptionId = publishResponse.getSubscriptionId();
                  SubscriptionDetails details = subscriptionDetails.get(subscriptionId);

                  boolean queued = false;

                  SubscriptionDetails requestedDetails = requestRegistrations.get(subscriptionId);
                  if (details != null
                      && (requestedDetails == null || requestedDetails == details)) {
                    // A queued Publish may serve a subscription created after the request. Only
                    // reject a known id that now belongs to another registration: its delayed
                    // response cannot safely be attributed to the replacement incarnation.
                    // Cheap and non-blocking: cancels and re-schedules a timer. The watchdog
                    // watches for the Server going quiet, so it is reset when the response is
                    // received rather than when it is eventually processed.
                    details.subscription.resetWatchdogTimer();

                    // The entry resolved here rides along with the task: by the time the task
                    // runs, the entry registered under this SubscriptionId may be a different
                    // one, and this task was queued for this one.
                    queued =
                        details.processingQueue.execute(
                            () ->
                                processPublishResponse(
                                    details, publishResponse, pendingCount, activation));
                  }

                  if (!queued) {
                    // Nothing to process — no entry, or a queue already shut down — but the
                    // permit still has to be released, and doing it here would re-enter
                    // sendPublishRequest() on this thread.
                    client
                        .getTransport()
                        .getConfig()
                        .getExecutor()
                        .execute(() -> releasePendingPublish(pendingCount, activation));
                  }
                } else {
                  // The failure path is dispatched asynchronously: it may run on a wheel timer
                  // thread (request timeout) or inline on the caller's thread (a request that
                  // fails before it is sent), and it re-enters maybeSendPublishRequests(), which
                  // would otherwise recurse into sendPublishRequest() on that same thread.
                  client
                      .getTransport()
                      .getConfig()
                      .getExecutor()
                      .execute(
                          () ->
                              handlePublishFailure(
                                  ex,
                                  requestHandle,
                                  pendingCount,
                                  drainedAcknowledgements,
                                  generation,
                                  activation));
                }
              });
    } catch (Exception e) {
      // The caller took a pending-publish permit before invoking this method. If building or
      // sending the request fails synchronously no completion handler will ever run, so release
      // the permit here; otherwise it leaks and Publish traffic eventually stops for good. The
      // acknowledgements already drained are in the same position: nothing will put them back
      // unless it happens here.
      restoreAcknowledgements(drainedAcknowledgements);

      pendingCount.getAndUpdate(p -> (p > 0) ? p - 1 : 0);

      logger.error("Error sending PublishRequest", e);
    }
  }

  /**
   * Report every acknowledgement the Server refused in {@code response}.
   *
   * <p>Part 4 §5.14.5.2 gives the PublishResponse a "List of results for the acknowledgements",
   * whose "size and order... matches the size and order of the subscriptionAcknowledgements request
   * parameter", so the result at index {@code i} belongs to the acknowledgement at index {@code i}
   * of the request that produced it.
   *
   * <p>A refused acknowledgement is reported and then forgotten, never re-queued. The reasons a
   * Server refuses one are statements that there is nothing left to acknowledge —
   * Bad_SequenceNumberUnknown means it is not holding a NotificationMessage with that sequence
   * number, Bad_SubscriptionIdInvalid that the Subscription itself is gone — so repeating the
   * acknowledgement could only be refused the same way, forever.
   *
   * <p>Runs inline on the transport's PublishResponse handler and performs no I/O.
   *
   * @param acknowledgements the acknowledgements carried by the PublishRequest, in request order.
   * @param response the {@link PublishResponse} that answered it.
   */
  private void reportRefusedAcknowledgements(
      List<SubscriptionAcknowledgement> acknowledgements, PublishResponse response) {

    StatusCode[] results = response.getResults();

    if (acknowledgements.isEmpty() || results == null || results.length == 0) {
      return;
    }

    int count = Math.min(results.length, acknowledgements.size());

    if (results.length != acknowledgements.size()) {
      logger.debug(
          "PublishResponse carried {} acknowledgement result(s) for a PublishRequest with {} "
              + "acknowledgement(s); pairing the first {}",
          results.length,
          acknowledgements.size(),
          count);
    }

    for (int i = 0; i < count; i++) {
      StatusCode result = results[i];

      if (result != null && result.isBad()) {
        SubscriptionAcknowledgement acknowledgement = acknowledgements.get(i);

        logger.warn(
            "Server refused SubscriptionAcknowledgement, subscriptionId={}, sequenceNumber={}: {}",
            acknowledgement.getSubscriptionId(),
            acknowledgement.getSequenceNumber(),
            result);
      }
    }
  }

  /**
   * Put back the acknowledgements drained into a PublishRequest that failed, so that a later
   * PublishRequest carries them.
   *
   * <p>They go back at the head of the queue, ahead of anything queued since, because they are
   * older. A sequence number that is queued again already — a duplicate NotificationMessage can
   * re-queue one while the request carrying the first acknowledgement is still in flight — is not
   * added a second time.
   *
   * @param drainedAcknowledgements what the failed request drained, per Subscription.
   */
  private static void restoreAcknowledgements(
      List<DrainedAcknowledgements> drainedAcknowledgements) {

    for (DrainedAcknowledgements drained : drainedAcknowledgements) {
      List<UInteger> availableAcknowledgements = drained.details().availableAcknowledgements;

      synchronized (availableAcknowledgements) {
        var restored = new ArrayList<UInteger>(drained.sequenceNumbers().size());

        for (UInteger sequenceNumber : drained.sequenceNumbers()) {
          if (!availableAcknowledgements.contains(sequenceNumber)) {
            restored.add(sequenceNumber);
          }
        }

        availableAcknowledgements.addAll(0, restored);
      }
    }
  }

  /**
   * Handle a PublishRequest that failed rather than returning a PublishResponse.
   *
   * @param ex the failure.
   * @param requestHandle the requestHandle of the PublishRequest that failed.
   * @param pendingCount the pending-publish permits held for the Session the request was sent on.
   * @param drainedAcknowledgements the acknowledgements the failed request was carrying.
   * @param generation the {@link #subscriptionGeneration} in effect when the request was sent.
   * @param activation the {@link #sessionActivations} in effect when the request was sent.
   */
  private void handlePublishFailure(
      Throwable ex,
      UInteger requestHandle,
      AtomicLong pendingCount,
      List<DrainedAcknowledgements> drainedAcknowledgements,
      long generation,
      long activation) {

    // The acknowledgements went down with the request. Part 4 §5.14.7.1: "The Client should
    // acknowledge all Messages in this list for which it will not request retransmission" — and
    // these are messages the client has and will not request again, so abandoning the
    // acknowledgement leaves the Server holding them, and re-advertising them in
    // availableSequenceNumbers, until its retransmission queue evicts them. Restoring them here,
    // before the permit is released, is what puts them on the next PublishRequest.
    restoreAcknowledgements(drainedAcknowledgements);

    // A null failure means the request completed with a response that was not a PublishResponse;
    // there is no exception to extract a StatusCode from, but the permit release and the refill
    // below still have to happen.
    StatusCode statusCode =
        ex == null
            ? StatusCode.BAD
            : UaException.extract(ex).map(UaException::getStatusCode).orElse(StatusCode.BAD);

    long outstanding = pendingCount.updateAndGet(p -> (p > 0) ? p - 1 : 0);

    long code = statusCode.value();

    if (code == StatusCodes.Bad_SessionClosed || code == StatusCodes.Bad_SessionIdInvalid) {
      // The Session is gone, not the Subscription: no PublishResponse can arrive until the Session
      // is re-activated, so the watchdog must be suspended rather than destroyed. The Session FSM
      // treats both codes as Session faults and reconnects, after which TransferSubscriptions may
      // well keep the Subscription alive; cancelling here would de-register the watchdog's
      // SessionActivityListener and leave it unable to ever arm again. Only while the answer still
      // describes this client, though: a straggling failure from a Session that a later activation
      // has already replaced must not disarm the watchdogs that activation's recovery re-armed.
      if (activation == sessionActivations.get()) {
        subscriptionDetails.values().forEach(d -> d.subscription.pauseWatchdogTimer());
      }
    } else if (code == StatusCodes.Bad_NoSubscription) {
      // Part 4 §5.14.8.1: when the last Subscription of a Session is deleted, "all Publish requests
      // still queued for that Session are de-queued and shall be returned with Bad_NoSubscription".
      // Replacing a request the Server answered that way with another one for the same Subscription
      // set could only be answered the same way, so the refill is suppressed — but only while the
      // answer still describes this client. A Subscription registered or unregistered since the
      // request was sent makes it stale: an application that deletes its last Subscription and
      // immediately creates another takes the whole de-queued burst *after* addSubscription() has
      // run, and addSubscription() cannot start Publish traffic for the new Subscription because
      // the de-queued requests still hold every pending-publish permit. Suppressing the refill here
      // as well would leave the new Subscription with no PublishRequest ever sent for it: no other
      // caller of maybeSendPublishRequests() is left to run, and the watchdog only reports the
      // silence, it does not recover from it.
      if (generation != subscriptionGeneration.get()) {
        maybeSendPublishRequests();
      }
    } else if (code == StatusCodes.Bad_TooManyPublishRequests) {
      // Part 4 §5.14.5.1: after this error a Client "shall not issue another Publish request before
      // one of its outstanding Publish requests is returned". Not replacing this one is the letter
      // of that; remembering how many the Server was in fact holding is what stops the next
      // PublishResponse to be delivered from restoring the very outstanding count that drew the
      // fault, since the target would otherwise still be subscriptionCount + 1 and the refill loop
      // closes the whole deficit at once.
      //
      // Never below one: a ceiling of zero is a pipeline that can never refill. The same clause
      // requires a Server to accept at least subscriptionCount + 1 queued Publish requests, which
      // is exactly what the client aims for, so a conformant Server never gets here.
      //
      // The ceiling is not permanent — see maybeRaisePendingPublishCeiling(long) — and this is
      // where a probe that was refused is charged for: the ceiling drops back and the next probe
      // costs geometrically more successful Publish round trips than the one that was refused.
      long ceiling = Math.max(1L, outstanding);

      PendingPublishCeiling before =
          pendingPublishCeiling.getAndUpdate(
              c -> c.activation() == activation ? c.refused(ceiling) : c);

      if (before.activation() != activation) {
        logger.debug(
            "Ignoring Bad_TooManyPublishRequests from superseded Session activation {} "
                + "(current={})",
            activation,
            before.activation());
      } else {
        if (before.probing()) {
          PendingPublishCeiling after = before.refused(ceiling);

          logger.debug(
              "Server refused the probe raising the pending Publish ceiling to {}; the ceiling is "
                  + "{} again and the next probe costs {} successful Publish round trips",
              before.ceiling(),
              after.ceiling(),
              after.cooldown());
        }

        if (outstanding == 0) {
          // This failure was itself the last outstanding request being returned, so the clause's
          // condition for issuing another is met — and nothing else is left in flight whose
          // completion would refill the pipeline. Without this the pipeline stays empty until an
          // unrelated event (a Subscription added or a Session re-activated) restarts it.
          maybeSendPublishRequests();
        }
      }
    } else {
      maybeSendPublishRequests();
    }

    logger.debug("Publish service failure (requestHandle={}): {}", requestHandle, statusCode, ex);
  }

  /**
   * Process a PublishResponse.
   *
   * <p>Runs on the Subscription's own processing queue, which is serial, so the sequence-number
   * accounting below needs no further synchronization and sees PublishResponses in the order the
   * Server sent them. Nothing here may block: the queue runs on the transport's executor, which is
   * also what completes the responses to any request this method might make.
   *
   * @param details the entry the response was routed to when it was received. Not looked up again
   *     here: the entry now registered under the response's SubscriptionId may be a different one —
   *     the Server can assign a new Subscription an id it has used before — and work queued for one
   *     registration must never be applied to another.
   * @param response the {@link PublishResponse} to process.
   * @param pendingCount the pending-publish permits held for the Session the request was sent on.
   * @param activation the Session activation the request was sent on.
   */
  private void processPublishResponse(
      SubscriptionDetails details,
      PublishResponse response,
      AtomicLong pendingCount,
      long activation) {

    UInteger subscriptionId = details.subscriptionId;

    if (!details.registered) {
      logger.debug(
          "Discarding PublishResponse for a Subscription that no longer exists, "
              + "subscriptionId={}",
          subscriptionId);

      releasePendingPublish(pendingCount, activation);
      return;
    }

    NotificationMessage notificationMessage = response.getNotificationMessage();

    boolean isKeepAlive =
        notificationMessage.getNotificationData() == null
            || notificationMessage.getNotificationData().length == 0;

    long receivedSequenceNumber = notificationMessage.getSequenceNumber().longValue();

    logger.debug(
        "Processing PublishResponse, subscriptionId={}, isKeepAlive={}, "
            + "lastSequenceNumber={}, receivedSequenceNumber={}",
        subscriptionId,
        isKeepAlive,
        details.lastSequenceNumber,
        receivedSequenceNumber);

    List<UInteger> missingSequenceNumbers = List.of();

    if (SequenceNumbers.isLegal(receivedSequenceNumber)) {
      if (!isKeepAlive) {
        // Acknowledge only NotificationMessages that were actually received: Part 4 §5.14.5.2 lets
        // the Server delete an acknowledged message from its retransmission queue, so
        // acknowledging one that never arrived destroys the only copy of it. A message that has
        // already been accounted for was still received, so it is still acknowledged — but only
        // once: a duplicate copy arriving before the first acknowledgement has been drained must
        // not queue a second one, which the Server could only answer Bad_SequenceNumberUnknown.
        UInteger sequenceNumber = notificationMessage.getSequenceNumber();

        synchronized (details.availableAcknowledgements) {
          if (!details.availableAcknowledgements.contains(sequenceNumber)) {
            details.availableAcknowledgements.add(sequenceNumber);
          }
        }
      }

      long expectedSequenceNumber = SequenceNumbers.successor(details.lastSequenceNumber);

      if (receivedSequenceNumber != expectedSequenceNumber
          && !SequenceNumbers.isAhead(receivedSequenceNumber, expectedSequenceNumber)) {

        long backwardDistance =
            SequenceNumbers.forwardDistance(receivedSequenceNumber, expectedSequenceNumber);

        UInteger[] availableSequenceNumbers = response.getAvailableSequenceNumbers();

        long duplicateWindow =
            Math.max(
                DEFAULT_MAX_RECOVERABLE_GAP,
                availableSequenceNumbers == null ? 0 : availableSequenceNumbers.length);

        if (backwardDistance <= duplicateWindow) {
          // Neither the message expected next nor ahead of it, so it has already been accounted
          // for: a duplicate, or a message the client recovered via Republish before this copy of
          // it arrived. Accounting must only ever move forwards; processing this message again
          // would hand it to the application a second time and roll lastSequenceNumber back,
          // making every message already received after it look missing and provoking a Republish
          // for each one.
          logger.debug(
              "Discarding NotificationMessage already accounted for, subscriptionId={}, "
                  + "lastSequenceNumber={}, receivedSequenceNumber={}",
              subscriptionId,
              details.lastSequenceNumber,
              receivedSequenceNumber);

          releasePendingPublish(pendingCount, activation);
          return;
        }

        // Too far behind to be a duplicate of anything recently recovered or retransmitted: the
        // Server's numbering has regressed, e.g. because it restarted and renumbered a restored
        // Subscription. Deliver the message and resynchronize the accounting to it — discarding
        // instead would silently drop every NotificationMessage until the Server's numbering
        // catches back up to where it used to be.
        logger.warn(
            "NotificationMessage sequence number regressed by {}; resynchronizing to it, "
                + "subscriptionId={}, lastSequenceNumber={}, receivedSequenceNumber={}",
            backwardDistance,
            subscriptionId,
            details.lastSequenceNumber,
            receivedSequenceNumber);
      }

      missingSequenceNumbers = missingSequenceNumbers(details, response, receivedSequenceNumber);

      // Part 4 §5.14.1.1: a keep-alive "contains the sequence number of the next
      // NotificationMessage that is to be sent", so it accounts for everything up to that sequence
      // number's predecessor and is *not* evidence that the sequence number it carries was
      // received. A data message accounts for itself.
      details.lastSequenceNumber =
          isKeepAlive
              ? SequenceNumbers.predecessor(receivedSequenceNumber)
              : receivedSequenceNumber;
    } else {
      // Part 4 §5.14.1.1: "The value 0 is never used for the sequence number." There is no sequence
      // arithmetic that can be done with an illegal value, so deliver the message but leave the
      // sequence accounting untouched.
      logger.warn(
          "Received NotificationMessage with illegal sequenceNumber={}, subscriptionId={}",
          receivedSequenceNumber,
          subscriptionId);
    }

    if (missingSequenceNumbers.isEmpty()) {
      deliverAndReleasePendingPublish(details, notificationMessage, pendingCount, activation);
    } else {
      // Recovery is a Republish round trip per missing NotificationMessage and must not be waited
      // for here. Pausing this Subscription's processing queue is what keeps it ordered: no later
      // PublishResponse for this Subscription is processed — and, crucially, none is delivered —
      // until every recovered NotificationMessage, and then the one that revealed the gap, has
      // been handed to the delivery queue. Other Subscriptions have their own queues and are
      // unaffected. pause() is safe to call from here because this task is running on the queue it
      // pauses, so no other task for this Subscription can be in flight.
      details.processingQueue.pause();

      republishMissingNotificationMessages(details, subscriptionId, missingSequenceNumbers)
          .whenComplete(
              (unit, ex) -> {
                try {
                  deliverAndReleasePendingPublish(
                      details, notificationMessage, pendingCount, activation);
                } finally {
                  details.processingQueue.resume();
                }
              });
    }
  }

  /**
   * Deliver {@code notificationMessage} to the application and release the pending-publish permit
   * once it has been delivered.
   *
   * @param details the {@link SubscriptionDetails} for the Subscription the message belongs to.
   * @param notificationMessage the {@link NotificationMessage} to deliver.
   * @param pendingCount the pending-publish permits held for the Session the request was sent on.
   * @param activation the Session activation the request was sent on.
   */
  private void deliverAndReleasePendingPublish(
      SubscriptionDetails details,
      NotificationMessage notificationMessage,
      AtomicLong pendingCount,
      long activation) {

    CompletionStage<Unit> callback =
        details
            .subscription
            .getDeliveryQueue()
            .submit(() -> deliverNotificationMessage(details, notificationMessage));

    if (callback != null) {
      // Once delivery of notifications is complete, we can consider sending another
      // PublishRequest. Waiting until the client has finished receiving notifications
      // is the backpressure mechanism that prevents the server from flooding the client
      // with data change notifications faster than it can process them.
      callback.whenCompleteAsync(
          (unit, ex) -> {
            if (ex != null) {
              logger.warn(
                  "Notification delivery threw an unexpected Exception: {}", ex.getMessage(), ex);
            }

            releasePendingPublish(pendingCount, activation);
          },
          client.getTransport().getConfig().getExecutor());
    } else {
      // The delivery queue is shut down, so the message will never be delivered — but the permit
      // still has to be released, or the pipeline permanently shrinks by one.
      releasePendingPublish(pendingCount, activation);
    }
  }

  /**
   * Release the pending-publish permit taken for a PublishRequest whose response has been dealt
   * with, and send a replacement PublishRequest if one is wanted.
   *
   * @param pendingCount the pending-publish permits held for the Session the request was sent on.
   * @param activation the Session activation the request was sent on.
   */
  private void releasePendingPublish(AtomicLong pendingCount, long activation) {
    pendingCount.getAndUpdate(p -> (p > 0) ? p - 1 : 0);

    // This is the one place that knows a PublishRequest went out and came back without being
    // refused, so it is where a ceiling learned from Bad_TooManyPublishRequests is paid off. Done
    // before the refill below, so a raise takes effect in the very refill this return pays for.
    maybeRaisePendingPublishCeiling(activation);

    maybeSendPublishRequests();
  }

  /**
   * Count one successful Publish round trip against any ceiling learned from
   * Bad_TooManyPublishRequests, and raise that ceiling by one if enough of them have gone by.
   *
   * <p>Part 4 §5.14.5.1 says what a Client must do the moment a Server refuses a PublishRequest for
   * holding too many, and nothing about how long it must go on believing it. The condition behind
   * the refusal is often momentary — a queue briefly full, a cap raised again a second later — and
   * the only two events that clear the ceiling outright, a Subscription being added and a Session
   * being activated, need never happen again in the life of a long-lived client. Left alone, one
   * transient refusal costs such a client a permanently shallower pipeline, i.e. throughput, for no
   * reason.
   *
   * <p>So the ceiling recovers, under three rules that together keep leniency from turning into
   * hammering:
   *
   * <ul>
   *   <li>it recovers by <b>one request</b> at a time, never straight back to the target: restoring
   *       the whole deficit at once would send the Server exactly the burst that drew the fault,
   *       which is what the ceiling exists to prevent;
   *   <li>each raise costs a <b>cooldown</b> of successful Publish round trips — {@link
   *       #PROBE_COOLDOWN_BASE_SUCCESSES} of them to begin with — which is also what keeps a single
   *       probe in flight: the one extra request a raise adds is answered long before another whole
   *       cooldown of requests has been;
   *   <li>a probe the Server refuses <b>lengthens</b> the next cooldown geometrically, up to {@link
   *       #PROBE_COOLDOWN_MAX_SUCCESSES}. A Server that always refuses therefore sees a number of
   *       probes growing only logarithmically in the number of responses it delivers.
   * </ul>
   *
   * <p>Counting the Server's own answers, rather than watching a clock, is what makes the schedule
   * cost the Server nothing when it has nothing to say: a Subscription that is publishing pays for
   * its probes, and one that is silent — or a Session whose Publish pipeline is stalled — does not
   * probe at all.
   *
   * <p>Runs on the transport's executor, as part of releasing a pending-publish permit, and
   * performs no I/O.
   */
  private void maybeRaisePendingPublishCeiling(long activation) {
    long natural = getNaturalMaxPendingPublishes();

    PendingPublishCeiling before =
        pendingPublishCeiling.getAndUpdate(
            c ->
                c.activation() == activation && c.ceiling() != NO_PENDING_PUBLISH_CEILING
                    ? c.roundTrip(natural)
                    : c);

    if (before.activation() != activation || before.ceiling() == NO_PENDING_PUBLISH_CEILING) {
      // Either this response belongs to a superseded Session or no ceiling has been learned, so
      // there is nothing for this round trip to pay down.
      return;
    }

    // roundTrip() is a function of the state it is applied to, so the transition this call made is
    // the one from the state it replaced, whatever any concurrent update has done since.
    PendingPublishCeiling after = before.roundTrip(natural);

    if (after.ceiling() > before.ceiling()) {
      logger.debug(
          "Probing whether the Server will now queue {} PublishRequests, after {} successful "
              + "Publish round trips at a ceiling of {}",
          after.ceiling(),
          before.successes() + 1,
          before.ceiling());
    }
  }

  /**
   * Determine which NotificationMessages are missing between the last sequence number accounted for
   * and {@code receivedSequenceNumber}, and which of those the Server can still retransmit.
   *
   * <p>Part 4 §5.14.1.1: "In the case of a retransmission queue overflow, the oldest sent
   * NotificationMessage gets deleted." The availableSequenceNumbers of a PublishResponse are what
   * is left in that queue, so anything older than the oldest of them is gone for good and
   * Republishing it can only be answered Bad_MessageNotAvailable. Recovery therefore starts at the
   * oldest sequence number the Server says it still holds, and what precedes it is reported as lost
   * data. A gap too large to be recoverable — more than the Server is holding, or more than {@link
   * #DEFAULT_MAX_RECOVERABLE_GAP} when it does not say — is reported as lost data in its entirety.
   *
   * <p>Runs on the Subscription's processing queue and performs no I/O.
   *
   * @param details the {@link SubscriptionDetails} for the Subscription the response belongs to.
   * @param response the {@link PublishResponse} being processed.
   * @param receivedSequenceNumber the sequence number of the received NotificationMessage.
   * @return the sequence numbers to request via Republish, oldest first; empty if there is no gap
   *     or nothing in it can be recovered.
   */
  private List<UInteger> missingSequenceNumbers(
      SubscriptionDetails details, PublishResponse response, long receivedSequenceNumber) {

    long expectedSequenceNumber = SequenceNumbers.successor(details.lastSequenceNumber);

    if (!SequenceNumbers.isAhead(receivedSequenceNumber, expectedSequenceNumber)) {
      return List.of();
    }

    UInteger subscriptionId = response.getSubscriptionId();
    UInteger[] availableSequenceNumbers = response.getAvailableSequenceNumbers();
    boolean advertised = availableSequenceNumbers != null && availableSequenceNumbers.length > 0;

    long firstRecoverable = expectedSequenceNumber;
    long maxRecoverableGap = DEFAULT_MAX_RECOVERABLE_GAP;
    boolean dataLost = false;

    if (advertised) {
      maxRecoverableGap = availableSequenceNumbers.length;

      long oldestAvailable = oldestAvailable(availableSequenceNumbers, receivedSequenceNumber);

      if (oldestAvailable != SequenceNumbers.NONE
          && SequenceNumbers.isAhead(oldestAvailable, expectedSequenceNumber)) {

        logger.warn(
            "The oldest NotificationMessage the Server can retransmit is sequenceNumber={}, so "
                + "the {} starting at sequenceNumber={} are gone; treating them as lost data, "
                + "subscriptionId={}",
            oldestAvailable,
            SequenceNumbers.forwardDistance(expectedSequenceNumber, oldestAvailable),
            expectedSequenceNumber,
            subscriptionId);

        firstRecoverable = oldestAvailable;
        dataLost = true;
      }
    }

    long missingCount = SequenceNumbers.forwardDistance(firstRecoverable, receivedSequenceNumber);

    if (missingCount > maxRecoverableGap) {
      logger.warn(
          "Gap of {} NotificationMessage(s) starting at sequenceNumber={} exceeds the {} the "
              + "Server can retransmit; treating it as lost data and resynchronizing to "
              + "sequenceNumber={}, subscriptionId={}",
          missingCount,
          firstRecoverable,
          maxRecoverableGap,
          receivedSequenceNumber,
          subscriptionId);

      if (advertised) {
        // Part 4 §5.14.7.1: "The Client should acknowledge all Messages in this list for which it
        // will not request retransmission." Nothing in this gap will be requested, and an
        // unacknowledged NotificationMessage stays in the Server's retransmission queue —
        // re-advertised in every PublishResponse and holding its memory — for the life of the
        // Subscription.
        acknowledgeAbandonedSequenceNumbers(
            details, availableSequenceNumbers, receivedSequenceNumber);
      }

      details.subscription.notifyNotificationDataLost();

      return List.of();
    }

    if (dataLost) {
      details.subscription.notifyNotificationDataLost();
    }

    var sequenceNumbers = new ArrayList<UInteger>((int) missingCount);
    long sequenceNumber = firstRecoverable;

    for (long i = 0; i < missingCount; i++) {
      sequenceNumbers.add(uint(sequenceNumber));

      sequenceNumber = SequenceNumbers.successor(sequenceNumber);
    }

    return sequenceNumbers;
  }

  /**
   * Queue an acknowledgement for every NotificationMessage the Server advertised as available for
   * retransmission but which the client has decided not to recover, so the Server can delete them
   * instead of holding them for a retransmission that will never be requested.
   *
   * @param details the {@link SubscriptionDetails} for the Subscription the messages belong to.
   * @param availableSequenceNumbers the availableSequenceNumbers of the PublishResponse whose gap
   *     is being given up on.
   * @param receivedSequenceNumber the sequence number of the received NotificationMessage.
   */
  private static void acknowledgeAbandonedSequenceNumbers(
      SubscriptionDetails details,
      UInteger[] availableSequenceNumbers,
      long receivedSequenceNumber) {

    synchronized (details.availableAcknowledgements) {
      for (UInteger sequenceNumber : availableSequenceNumbers) {
        if (sequenceNumber == null) {
          continue;
        }

        long value = sequenceNumber.longValue();

        // A sequence number that is illegal, or that the Server has not sent yet, is not one the
        // client is abandoning.
        if (!SequenceNumbers.isLegal(value)
            || SequenceNumbers.isAhead(value, receivedSequenceNumber)) {
          continue;
        }

        if (!details.availableAcknowledgements.contains(sequenceNumber)) {
          details.availableAcknowledgements.add(sequenceNumber);
        }
      }
    }
  }

  /**
   * @param availableSequenceNumbers a non-empty availableSequenceNumbers from a PublishResponse.
   * @param receivedSequenceNumber the sequence number of the received NotificationMessage.
   * @return the oldest sequence number the Server advertised as available for retransmission, or
   *     {@link SequenceNumbers#NONE} if it advertised none the client can make sense of.
   */
  private static long oldestAvailable(
      UInteger[] availableSequenceNumbers, long receivedSequenceNumber) {

    long oldest = SequenceNumbers.NONE;
    long oldestAge = -1;

    for (UInteger availableSequenceNumber : availableSequenceNumbers) {
      if (availableSequenceNumber == null) {
        continue;
      }

      long sequenceNumber = availableSequenceNumber.longValue();

      // A sequence number that is illegal, or that the Server has not sent yet, says nothing about
      // what its retransmission queue still holds.
      if (!SequenceNumbers.isLegal(sequenceNumber)
          || SequenceNumbers.isAhead(sequenceNumber, receivedSequenceNumber)) {
        continue;
      }

      long age = SequenceNumbers.forwardDistance(sequenceNumber, receivedSequenceNumber);

      if (age > oldestAge) {
        oldestAge = age;
        oldest = sequenceNumber;
      }
    }

    return oldest;
  }

  /**
   * Recover, via Republish, the NotificationMessages identified by {@code sequenceNumbers}.
   *
   * <p>The requests are made one at a time and asynchronously: each recovered NotificationMessage
   * is handed to the delivery queue as it arrives, so the application sees them in sequence order
   * and ahead of the message that revealed the gap, and no thread ever waits for a round trip. If
   * any of them cannot be recovered the Subscription is notified that notification data was lost.
   *
   * @param details the {@link SubscriptionDetails} for the Subscription the messages belong to.
   * @param subscriptionId the Server-assigned identifier of that Subscription.
   * @param sequenceNumbers the sequence numbers to request, oldest first.
   * @return a {@link CompletableFuture} that completes when the last of them has been dealt with.
   */
  private CompletableFuture<Unit> republishMissingNotificationMessages(
      SubscriptionDetails details, UInteger subscriptionId, List<UInteger> sequenceNumbers) {

    // Bound to the Session in hand when the gap was found, exactly as the reconnect recovery is:
    // a repair that outlives its Session must not have its remaining requests silently re-issued
    // on the next one, which has a recovery of its own, and must never park waiting for a Session
    // that does not exist yet — the processing queue stays paused until this repair completes,
    // and a parked repair would hold the reconnect recovery, and with it every Subscription's
    // Publish traffic, shut.
    CompletableFuture<OpcUaSession> sessionFuture = client.getSessionAsync();

    OpcUaSession session =
        sessionFuture.isDone() && !sessionFuture.isCompletedExceptionally()
            ? sessionFuture.getNow(null)
            : null;

    if (session == null) {
      logger.warn(
          "No Session in hand to repair the gap on; treating it as lost data, subscriptionId={}",
          subscriptionId);

      details.subscription.notifyNotificationDataLost();

      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    var recovery = new Recovery();

    CompletableFuture<Unit> chain = CompletableFuture.completedFuture(Unit.VALUE);

    for (UInteger sequenceNumber : sequenceNumbers) {
      chain =
          chain.thenCompose(
              unit ->
                  republishNotificationMessage(
                      session, details, subscriptionId, sequenceNumber, recovery));
    }

    return chain.whenComplete(
        (unit, ex) -> {
          if (ex != null || recovery.dataLost) {
            details.subscription.notifyNotificationDataLost();
          }
        });
  }

  /**
   * Request one NotificationMessage via Republish and, if it arrives, acknowledge it and hand it to
   * the delivery queue.
   *
   * @param session the Session the repair is bound to.
   * @param details the {@link SubscriptionDetails} for the Subscription the message belongs to.
   * @param subscriptionId the Server-assigned identifier of that Subscription.
   * @param sequenceNumber the sequence number to request.
   * @param recovery the state shared by every step of this recovery.
   * @return a {@link CompletableFuture} that completes, never exceptionally, once the request has
   *     been answered one way or the other.
   */
  private CompletableFuture<Unit> republishNotificationMessage(
      UaSession session,
      SubscriptionDetails details,
      UInteger subscriptionId,
      UInteger sequenceNumber,
      Recovery recovery) {

    if (recovery.abandoned) {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    return republish(session, subscriptionId, sequenceNumber)
        .handle(
            (republishResponse, ex) -> {
              if (ex != null) {
                StatusCode statusCode =
                    UaException.extract(ex).map(UaException::getStatusCode).orElse(StatusCode.BAD);

                recovery.dataLost = true;

                if (statusCode.value() != StatusCodes.Bad_MessageNotAvailable) {
                  // Bad_MessageNotAvailable is an answer about this one NotificationMessage: the
                  // Server no longer holds it, but it may well still hold the ones after it, so
                  // the rest of the recovery is still worth attempting. Any other failure is the
                  // service call itself failing — a lost Session, a closed connection, a timeout —
                  // and repeating it for every remaining sequence number can only fail the same
                  // way.
                  recovery.abandoned = true;
                }

                logger.warn(
                    "Republish service failure, subscriptionId={}, sequenceNumber={}: {}",
                    subscriptionId,
                    sequenceNumber,
                    statusCode);
              } else {
                NotificationMessage notificationMessage =
                    republishResponse.getNotificationMessage();

                details.availableAcknowledgements.add(sequenceNumber);

                details
                    .subscription
                    .getDeliveryQueue()
                    .execute(() -> deliverNotificationMessage(details, notificationMessage));
              }

              return Unit.VALUE;
            });
  }

  /**
   * Deliver {@code notificationMessage} to the application, unless the Subscription it was received
   * on is gone.
   *
   * <p>Runs on the Subscription's delivery queue, which is a queue of the {@link OpcUaSubscription}
   * object rather than of any one Subscription it has represented: it is neither drained nor
   * replaced by {@link OpcUaSubscription#reset()}, so a message can still be waiting here — behind
   * an application callback that has not returned — when the Subscription it belongs to is
   * discarded and another created in its place. Delivering it then would tell the application that
   * a MonitoredItem of the current Subscription has a value that Subscription never reported, and,
   * for a Bad_Timeout StatusChangeNotification, would tear down a Subscription that has not timed
   * out. The entry's registration is what distinguishes the two: it is dropped the moment its
   * Subscription is.
   *
   * @param details the entry for the Subscription the message was received on.
   * @param notificationMessage the {@link NotificationMessage} to deliver.
   */
  private void deliverNotificationMessage(
      SubscriptionDetails details, NotificationMessage notificationMessage) {

    if (!details.registered) {
      logger.debug(
          "Discarding NotificationMessage for a Subscription that no longer exists, "
              + "subscriptionId={}, sequenceNumber={}",
          details.subscriptionId,
          notificationMessage.getSequenceNumber());

      ExtensionObject[] discardedData = notificationMessage.getNotificationData();

      if (discardedData != null && discardedData.length > 0) {
        // These notifications were received — and may already have been acknowledged, letting the
        // Server delete its only retransmission copy — but can no longer be attributed: report
        // them as lost data rather than discarding them without a trace.
        details.subscription.notifyNotificationDataLost();
      }

      return;
    }

    ExtensionObject[] notificationData = notificationMessage.getNotificationData();

    if (notificationData == null || notificationData.length == 0) {
      details.subscription.notifyKeepAliveReceived();
    } else {
      for (ExtensionObject xo : notificationData) {
        Object notification = xo.decode(client.getStaticEncodingContext());

        if (notification instanceof DataChangeNotification) {
          MonitoredItemNotification[] monitoredItems =
              ((DataChangeNotification) notification).getMonitoredItems();

          if (monitoredItems != null && monitoredItems.length > 0) {
            details.subscription.notifyDataReceived(monitoredItems);
          }
        } else if (notification instanceof EventNotificationList) {
          EventFieldList[] events = ((EventNotificationList) notification).getEvents();

          if (events != null && events.length > 0) {
            details.subscription.notifyEventsReceived(events);
          }
        } else if (notification instanceof StatusChangeNotification scn) {
          StatusCode status = scn.getStatus();

          if (status.value() == StatusCodes.Bad_Timeout) {
            // The Subscription this message was received on no longer exists on the Server, so its
            // entry goes with it. Keyed on the entry's own id: the live object's current id belongs
            // to whatever Subscription it represents now, which has not timed out. The registered
            // check at the top of this method is stale by now — application callbacks of arbitrary
            // duration have run since — so the teardown happens only if this entry is still the
            // registered one, and the reset only if the object still represents the Subscription
            // the entry was registered for.
            if (!unregister(details)) {
              logger.debug(
                  "Discarding Bad_Timeout StatusChangeNotification for a Subscription that no "
                      + "longer exists, subscriptionId={}",
                  details.subscriptionId);

              continue;
            }

            details.subscription.resetIfIncarnation(details.incarnation);
          }

          details.subscription.notifyStatusChanged(status);
        } else {
          logger.warn("Unhandled notification type: {}", notification);
        }
      }
    }
  }

  private long getMaxPendingPublishes() {
    return Math.min(getNaturalMaxPendingPublishes(), pendingPublishCeiling.get().ceiling());
  }

  /**
   * @return the number of PublishRequests the client would keep outstanding if no Server had ever
   *     refused one, i.e. {@code min(subscriptionCount + 1, maxPendingPublishRequests)}, or zero if
   *     there is no Subscription to publish for. It is the target any learned ceiling recovers
   *     towards and never exceeds.
   */
  private long getNaturalMaxPendingPublishes() {
    if (subscriptionDetails.isEmpty()) {
      return 0;
    }

    long maxPendingPublishRequests = client.getConfig().getMaxPendingPublishRequests().longValue();

    return Math.min(subscriptionDetails.size() + 1L, maxPendingPublishRequests);
  }

  private UInteger getTimeoutHint() {
    double maxKeepAlive = client.getConfig().getRequestTimeout().doubleValue();

    List<SubscriptionDetails> subscriptions = List.copyOf(subscriptionDetails.values());

    for (SubscriptionDetails details : subscriptions) {
      Optional<Double> revisedPublishingInterval =
          details.subscription.getRevisedPublishingInterval();
      Optional<UInteger> revisedMaxKeepAliveCount =
          details.subscription.getRevisedMaxKeepAliveCount();

      if (revisedPublishingInterval.isPresent() && revisedMaxKeepAliveCount.isPresent()) {
        double keepAlive =
            revisedPublishingInterval.get() * revisedMaxKeepAliveCount.get().doubleValue();

        if (keepAlive >= maxKeepAlive) {
          maxKeepAlive = keepAlive;
        }
      }
    }

    long maxPendingPublishes = getMaxPendingPublishes();
    double timeoutHint = maxKeepAlive * maxPendingPublishes * 1.5;

    if (Double.isInfinite(timeoutHint) || timeoutHint > UInteger.MAX_VALUE) {
      // The timeoutHint is encoded as a UInt32; clamp rather than let an out-of-range value
      // reach uint(), which would throw and leave this request unsent.
      timeoutHint = UInteger.MAX_VALUE;
    }

    logger.debug(
        "getTimeoutHint() maxKeepAlive={} maxPendingPublishes={} timeoutHint={}",
        maxKeepAlive,
        maxPendingPublishes,
        timeoutHint);

    return uint((long) timeoutHint);
  }

  /**
   * The acknowledgements taken from one Subscription's queue for a single PublishRequest.
   *
   * @param details the Subscription they were taken from.
   * @param sequenceNumbers the sequence numbers acknowledged, oldest first.
   */
  private record DrainedAcknowledgements(
      SubscriptionDetails details, List<UInteger> sequenceNumbers) {}

  /**
   * A finished reconnect recovery: the {@link #sessionActivations} value it belonged to and the
   * Session it ran on, or {@code null} once that Session has become inactive.
   */
  private record RecoveredActivation(long activation, @Nullable UaSession session) {}

  /**
   * The available sequence numbers returned while transferring a Subscription to one exact Session.
   * Session identity is part of the value so recovery queued for the Session being replaced cannot
   * consume or apply the replacement Session's TransferResult.
   */
  private record TransferredSequenceNumbers(
      UaSession session, UInteger @Nullable [] sequenceNumbers) {}

  /**
   * How many PublishRequests the Server has been observed to queue for a Session, and what the
   * client has to see before it asks whether it would now queue one more.
   *
   * <p>Immutable, and every transition is a function of the state it is applied to, so the whole of
   * it — the ceiling, the cooldown, and whether a probe is outstanding — is read and replaced as
   * one value rather than as four fields that a concurrent refusal could interleave with a raise.
   *
   * @param activation the Session activation this ceiling was learned for.
   * @param ceiling the number of PublishRequests the Server was holding when it refused one, or
   *     {@link #NO_PENDING_PUBLISH_CEILING} if it has never refused one. Never below one: a ceiling
   *     of zero is a pipeline that can never refill.
   * @param successes successful Publish round trips counted towards the next probe.
   * @param cooldown successful Publish round trips a probe currently costs.
   * @param probing {@code true} while the newest raise is still answerable, i.e. from the raise
   *     until either a Bad_TooManyPublishRequests is charged to it or the ceiling it installed has
   *     stood for a whole cooldown. Deliberately not cleared by the first successful round trip
   *     after the raise: a refused PublishRequest and a delivered NotificationMessage are handled
   *     on paths of different lengths, so which the client finishes with first is a race, and a
   *     delivery that could clear this flag would leave the refusal behind it uncharged — which is
   *     precisely the case a Server that always refuses produces, and would have it probed at a
   *     constant rate.
   */
  private record PendingPublishCeiling(
      long activation, long ceiling, long successes, long cooldown, boolean probing) {

    /** No Server has refused a PublishRequest, so only the client's own target applies. */
    static PendingPublishCeiling none(long activation) {
      return new PendingPublishCeiling(
          activation, NO_PENDING_PUBLISH_CEILING, 0L, PROBE_COOLDOWN_BASE_SUCCESSES, false);
    }

    /**
     * @param observed the number of PublishRequests the Server was in fact holding.
     * @return the state after a Bad_TooManyPublishRequests. The ceiling only ever descends, since a
     *     Server that refused at {@code observed} has said nothing to withdraw what it refused at
     *     less; a refusal that answers a probe also lengthens the next cooldown geometrically,
     *     which is what stops a Server that always refuses from being asked at a constant rate.
     *     Only the first refusal after a raise is charged for it, so a raise a Server answers with
     *     more than one refusal costs one lengthening rather than several.
     */
    PendingPublishCeiling refused(long observed) {
      return new PendingPublishCeiling(
          activation,
          Math.min(ceiling, observed),
          0L,
          probing
              ? Math.min(PROBE_COOLDOWN_MAX_SUCCESSES, cooldown * PROBE_COOLDOWN_GROWTH)
              : cooldown,
          false);
    }

    /**
     * @param natural the number of PublishRequests the client would keep outstanding if nothing had
     *     ever been refused. The ceiling never rises above it: it is what recovery aims at, not a
     *     value to overshoot.
     * @return the state after one successful Publish round trip, which is either a payment towards
     *     the next probe or — when it completes the cooldown — the raise that <i>is</i> that probe.
     */
    PendingPublishCeiling roundTrip(long natural) {
      if (ceiling >= natural) {
        // Fully recovered: there is nothing left to ask for. The raise that got here stops being
        // answerable once the ceiling it installed has stood for a whole cooldown, so that a
        // Bad_TooManyPublishRequests long afterwards is treated as a new condition on the Server
        // rather than as the answer to this probe. Charging it as one would let a client that lives
        // through many unrelated transient conditions ratchet its cooldown up to the cap.
        return new PendingPublishCeiling(
            activation, ceiling, successes + 1, cooldown, probing && successes + 1 < cooldown);
      }

      if (successes + 1 < cooldown) {
        // Still paying for the next probe.
        return new PendingPublishCeiling(activation, ceiling, successes + 1, cooldown, probing);
      }

      // One more request than the Server last refused, and no more than that: the question is
      // whether it will now take one more, and one extra PublishRequest asks it. No second raise
      // follows until a whole further cooldown of round trips has been answered, which is what
      // keeps a single probe in flight.
      return new PendingPublishCeiling(activation, ceiling + 1, 0L, cooldown, true);
    }
  }

  /** State shared by the steps of a single Republish recovery. */
  private static class Recovery {

    /** {@code true} if at least one NotificationMessage could not be recovered. */
    private volatile boolean dataLost = false;

    /** {@code true} if the sequence numbers not yet requested are not worth requesting. */
    private volatile boolean abandoned = false;
  }

  /**
   * One Subscription's registration with this manager.
   *
   * <p>An entry represents a single Subscription, not the {@link OpcUaSubscription} object that
   * currently stands for it: the object outlives the Subscription and can be made to stand for
   * another, so an entry is bound to the SubscriptionId it was registered under and is discarded,
   * never re-bound, when that Subscription goes away.
   */
  private static class SubscriptionDetails {

    /**
     * The Server-assigned identifier of the Subscription this entry represents, and the key it is
     * registered under. Immutable, unlike {@link OpcUaSubscription#getSubscriptionId()}.
     */
    private final UInteger subscriptionId;

    /**
     * The incarnation of the {@link OpcUaSubscription} object at the moment this entry was
     * registered, i.e. while it still represented the Subscription this entry represents. A reset
     * changes it, so comparing it again later asks whether the object still does.
     */
    private final long incarnation;

    /**
     * {@code false} once this entry has been unregistered, i.e. once the Subscription it represents
     * is known to be gone. Work queued for it before then is discarded rather than applied to the
     * Subscription the {@link #subscription} object represents now.
     */
    private volatile boolean registered = true;

    /** Sequence numbers of received NotificationMessages awaiting acknowledgement. */
    private final List<UInteger> availableAcknowledgements =
        Collections.synchronizedList(new ArrayList<>());

    /**
     * The availableSequenceNumbers of the TransferResult that last transferred this Subscription to
     * an exact Session, or {@code null} if the client has not been told what the Server holds since
     * the last time it asked for it.
     *
     * <p>Set while the Session the transfer was part of is on its way to Active and consumed by the
     * recovery that runs when it gets there. Session identity is stored so a task queued for the
     * Session being replaced cannot consume the replacement Session's result. No future activation
     * number is predicted here: Session activity callbacks are asynchronous and may be reordered.
     */
    private final AtomicReference<@Nullable TransferredSequenceNumbers> transferredSequenceNumbers =
        new AtomicReference<>();

    /**
     * Serial queue on which this Subscription's PublishResponses are processed, in the order the
     * Server sent them.
     *
     * <p>One queue per Subscription rather than one for the client: a Subscription that is
     * recovering a gap pauses its own queue for the duration, and a Subscription with nothing
     * missing must not have to wait behind it.
     */
    private final TaskQueue processingQueue;

    /**
     * The sequence number of the last NotificationMessage accounted for, i.e. received, recovered
     * via Republish, or given up on. {@link SequenceNumbers#NONE} until the first PublishResponse
     * has been processed, at which point the next NotificationMessage expected is {@link
     * SequenceNumbers#FIRST}.
     */
    private volatile long lastSequenceNumber = SequenceNumbers.NONE;

    private final OpcUaSubscription subscription;

    private SubscriptionDetails(
        OpcUaSubscription subscription, UInteger subscriptionId, Executor executor) {

      this.subscription = subscription;
      this.subscriptionId = subscriptionId;

      incarnation = subscription.getIncarnation();

      processingQueue = new TaskQueue(executor);
    }
  }
}
