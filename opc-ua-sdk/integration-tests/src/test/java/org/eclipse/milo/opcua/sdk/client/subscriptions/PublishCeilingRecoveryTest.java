/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.subscriptions;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Recovery of the Publish pipeline ceiling {@code PublishingManager} learns from
 * Bad_TooManyPublishRequests.
 *
 * <p>Part 4 §5.14.5.1: once a Server has answered that error a Client "shall not issue another
 * Publish request before one of its outstanding Publish requests is returned", and the client also
 * remembers how many the Server was in fact holding, so that the next returning request does not
 * restore the very outstanding count that drew the fault. The clamp is right. Keeping it forever is
 * not: a momentary server-side condition — a queue briefly full, a cap raised again a second later
 * — costs a long-lived client a permanently shallower pipeline, because the only two events that
 * clear the clamp today are a Subscription being added and a Session being activated, and neither
 * need ever happen again for the rest of that client's life.
 *
 * <p>What is required instead, and what these tests pin:
 *
 * <ol>
 *   <li>sustained successful Publish activity lets the ceiling recover to the natural target,
 *       {@code min(subscriptionCount + 1, maxPendingPublishRequests)};
 *   <li>recovery is incremental — one request at a time, never straight back to the target;
 *   <li>a probe costs a cooldown counted in successful Publish round trips, not in elapsed time, so
 *       that it is the Server's own answers which pay for the next attempt;
 *   <li>a probe that draws Bad_TooManyPublishRequests again re-clamps the ceiling <i>and</i>
 *       lengthens the next cooldown geometrically, so that a Server which always refuses sees a
 *       number of probes growing only logarithmically in the number of responses it delivers;
 *   <li>at most one probe is in flight at a time, and the natural target is never exceeded.
 * </ol>
 *
 * <p>The bounds asserted below deliberately leave the implementation room to choose its constants,
 * but they do constrain them: the first probe must come no earlier than {@link
 * #QUIET_PREFIX_DELIVERIES} + 1 successful round trips (so that the immediate one-replacement-per-
 * return behaviour pinned by {@link PublishPipelineRefillTest}, whose window is a single returning
 * request, is untouched), no later than a few tens of them, and against a Server that always
 * refuses the schedule must fit within {@link #MAX_PROBES} probes over {@link
 * #PROBE_WINDOW_DELIVERIES} delivered NotificationMessages, with the interval between probes never
 * shrinking. A base cooldown of 8 successful round trips, doubling on each refused probe and capped
 * in the hundreds, satisfies all of them with room to spare.
 *
 * <p>Every test drives a Server whose Publish requests are parked by {@link
 * ScriptableSubscriptionServiceSet} — nothing completes one unless the test says so — and answers
 * them one at a time, so the number of Publish requests the Server is left holding after each
 * answer <i>is</i> the client's current ceiling, observed rather than inferred, and every wait is
 * on a Publish request arriving rather than on time passing.
 *
 * @see PublishPipelineRefillTest for the immediate response to the fault, which this must not
 *     change.
 */
public class PublishCeilingRecoveryTest {

  /**
   * The natural target of the one-Subscription fixtures: {@code min(subscriptionCount + 1,
   * maxPendingPublishRequests)} = min(1 + 1, 2). maxPendingPublishRequests is configured to the
   * same value rather than left at its default so that the target is stated by the test.
   */
  private static final int SINGLE_SUBSCRIPTION_TARGET = 2;

  /** Three Subscriptions, and a natural target of min(3 + 1, 4) = 4. */
  private static final int DEEP_PIPELINE_SUBSCRIPTIONS = 3;

  private static final int DEEP_PIPELINE_TARGET = 4;

  /**
   * The ceiling one rejection leaves behind when the whole one-Subscription pipeline was
   * outstanding: {@code max(1, outstanding)} with the refused request accounted for, i.e. max(1, 2
   * - 1).
   */
  private static final int SINGLE_SUBSCRIPTION_CLAMP = 1;

  /**
   * The ceiling two rejections leave behind when the whole three-Subscription pipeline was
   * outstanding: the first is accounted for with three still outstanding, the second with two, and
   * the ceiling is the smallest of them.
   */
  private static final int DEEP_PIPELINE_CLAMP = 2;

  /**
   * The number of Publish requests the capped Server will hold for a Session before answering
   * Bad_TooManyPublishRequests. One is below every natural target used here, so the Server refuses
   * whenever the client tries to deepen its pipeline at all.
   */
  private static final int SERVER_PUBLISH_CAP = 1;

  /**
   * The number of successful Publish round trips a recovery run is given to reach the natural
   * target. Three raises at a doubling cooldown starting from 8 cost 8 + 16 + 32 = 56, so this
   * leaves the implementation an order of magnitude of slack while still failing rather than
   * hanging if the ceiling never moves.
   */
  private static final int MAX_RECOVERY_DELIVERIES = 256;

  /**
   * Successful Publish round trips the anti-hammering run delivers. Long enough that a cooldown
   * which doubles from a base of a few produces several probes, so that the intervals between them
   * can be compared and not merely counted.
   */
  private static final int PROBE_WINDOW_DELIVERIES = 256;

  /**
   * How long, after a Publish request has arrived to replace the one just answered, to keep waiting
   * for another before answering the next.
   *
   * <p>Not a synchronization mechanism: the round trip itself is waited for, and this is only the
   * window in which a <i>second</i> request — the probe that accompanies a replacement — is allowed
   * to land. The client sends both from one loop, so the gap between them is tens of microseconds;
   * this is three orders of magnitude more than that, and is what makes a probe meet a Server whose
   * queue is still occupied rather than one that has just been emptied.
   */
  private static final long BURST_SETTLE_MILLIS = 5;

  /**
   * The most refused probes a Server that always answers Bad_TooManyPublishRequests may see over
   * {@link #PROBE_WINDOW_DELIVERIES} delivered NotificationMessages.
   *
   * <p>This is the anti-hammering guarantee. Without a cooldown the client probes once per
   * returning request, which is one refusal per delivered NotificationMessage — 256 of them. With a
   * cooldown that does not grow it probes 256/cooldown times, still linear in the traffic. With a
   * cooldown that doubles on each refusal it probes log2(256/base) times: at most 6 for any base of
   * 4 or more and any cooldown cap of 64 or more. Ten leaves room for an implementation that counts
   * its successes a little differently without leaving room for one that hammers.
   */
  private static final int MAX_PROBES = 10;

  /**
   * The fewest refused probes that same run must see.
   *
   * <p>One would be satisfied by a client that tried once and gave up, which is the clamp again
   * under another name; the guarantee is that it keeps asking, only ever more rarely. Two also
   * keeps the test honest about the Server: every probe against this Server is refused, so a run
   * that recorded one refusal would mean a probe had slipped past the cap and the ceiling had been
   * allowed to stand.
   */
  private static final int MIN_PROBES = 2;

  /**
   * Successful Publish round trips over which, immediately after the clamp, one returning request
   * must still buy exactly one replacement and nothing more.
   *
   * <p>{@link PublishPipelineRefillTest} pins that for the first of them; this pins that the
   * cooldown before the first probe is longer than a handful, so that the two cannot be confused
   * for one another and a probe cannot appear inside that test's window.
   */
  private static final int QUIET_PREFIX_DELIVERIES = 4;

  /**
   * Keep-alives all carry sequence number 1: Part 4 §5.14.1.1 makes a keep-alive's sequence number
   * "the sequence number of the next NotificationMessage that is to be sent", so repeating it says
   * the Server has still sent nothing, leaves the client's sequence accounting exactly where it
   * started, and produces neither an acknowledgement nor a Republish. Every one of them is
   * nevertheless a successful Publish round trip: a request answered, a NotificationMessage
   * delivered, a permit released and a replacement request sent.
   */
  private static final long KEEP_ALIVE_SEQUENCE_NUMBER = 1L;

  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /** How long to watch for a Publish request that must not be sent. */
  private static final long QUIET_WINDOW_MILLIS = 1_000;

  /**
   * Long enough that nothing times out on its own: the PublishRequests these tests park at the
   * Server stay parked until the test decides how they end.
   */
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;

  /**
   * A transient rejection — the Server refuses while its queue is briefly full, then stops refusing
   * — must not cost the client its pipeline depth for good.
   */
  @Nested
  class RecoveryAfterATransientRejection {

    /**
     * Control: the same three-Subscription fixture with a Server that never refuses. It proves the
     * fixture can observe a four-deep pipeline at all, so {@link
     * #theCeilingClimbsBackOneRequestAtATimeRatherThanInOneJump} fails because the ceiling did not
     * recover and not because four outstanding requests are unobservable here.
     */
    @Test
    void theWholePipelineIsOutstandingWhenTheServerNeverRefuses() throws Exception {
      try (Fixture fixture = Fixture.withThreeSubscriptions()) {
        assertTrue(
            fixture.scriptable.awaitParked(DEEP_PIPELINE_TARGET, AWAIT_TIMEOUT_MILLIS),
            "the Publish pipeline never filled to its natural target");
      }
    }

    /**
     * The property the clamp costs today: a client that took one Bad_TooManyPublishRequests keeps a
     * pipeline one request shallower than the Server would now accept, for as long as it lives.
     */
    @Test
    void theCeilingRecoversToTheNaturalTargetOnceTheServerStopsRefusing() throws Exception {
      try (Fixture fixture = Fixture.withOneSubscription(SERVER_PUBLISH_CAP)) {
        // Filling a two-deep pipeline against a Server that holds one costs exactly one rejection:
        // the first request is held, the second refused, and the refused one is not replaced.
        fixture.assertPipelineSettlesAt(SINGLE_SUBSCRIPTION_CLAMP);
        assertEquals(
            1,
            fixture.scriptable.getRejectionCount(),
            "the clamp under test is the one a single Bad_TooManyPublishRequests installs");

        // The transient condition is over; from here the Server accepts everything the client cares
        // to send.
        fixture.scriptable.setCap(CappingSubscriptionServiceSet.UNCAPPED);

        List<Integer> steps =
            fixture.deliverUntilPipelineReaches(
                SINGLE_SUBSCRIPTION_CLAMP, SINGLE_SUBSCRIPTION_TARGET, MAX_RECOVERY_DELIVERIES);

        assertEquals(
            SINGLE_SUBSCRIPTION_TARGET,
            steps.isEmpty() ? SINGLE_SUBSCRIPTION_CLAMP : steps.get(steps.size() - 1),
            "the ceiling learned from one Bad_TooManyPublishRequests never recovered: after "
                + MAX_RECOVERY_DELIVERIES
                + " successful Publish round trips against a Server that refuses nothing, the"
                + " client still keeps a shallower pipeline than min(subscriptionCount + 1,"
                + " maxPendingPublishRequests). Nothing in the traffic itself can lift the clamp,"
                + " so it lasts until a Subscription is added or a Session is activated — for a"
                + " long-lived client, forever");
      }
    }

    /**
     * Recovery must be a probe, not a jump. Restoring the whole deficit at once would send the
     * Server exactly the burst that drew the fault, which is what the clamp exists to prevent;
     * raising the ceiling by one asks the question with a single request.
     *
     * <p>Two rejections against a four-deep pipeline leave a ceiling of two, so an incremental
     * recovery is observable as the pipeline going three deep before it goes four deep.
     */
    @Test
    void theCeilingClimbsBackOneRequestAtATimeRatherThanInOneJump() throws Exception {
      try (Fixture fixture = Fixture.withThreeSubscriptions()) {
        assertTrue(
            fixture.scriptable.awaitParked(DEEP_PIPELINE_TARGET, AWAIT_TIMEOUT_MILLIS),
            "the Publish pipeline never filled to its natural target");

        // Two of the four outstanding requests are refused. Their failures are accounted for
        // independently — each decrements the outstanding count once — so the ceiling ends up at
        // the smaller of the two counts they saw, whichever order they are handled in.
        fixture.scriptable.enqueueServiceFault(StatusCodes.Bad_TooManyPublishRequests);
        fixture.scriptable.enqueueServiceFault(StatusCodes.Bad_TooManyPublishRequests);

        fixture.assertPipelineSettlesAt(DEEP_PIPELINE_CLAMP);

        List<Integer> steps =
            fixture.deliverUntilPipelineReaches(
                DEEP_PIPELINE_CLAMP, DEEP_PIPELINE_TARGET, MAX_RECOVERY_DELIVERIES);

        assertEquals(
            List.of(DEEP_PIPELINE_CLAMP + 1, DEEP_PIPELINE_TARGET),
            steps,
            "the ceiling did not climb back one request at a time: the numbers of Publish requests"
                + " the Server was left holding, in the order they were first observed, were "
                + steps
                + ", starting from a clamp of "
                + DEEP_PIPELINE_CLAMP
                + " and aiming at a natural target of "
                + DEEP_PIPELINE_TARGET);
      }
    }
  }

  /**
   * The other half of leniency, and the one that decides whether it is safe: a Server that keeps
   * refusing must not be asked over and over.
   */
  @Nested
  class AntiHammering {

    /**
     * A Server whose cap really is below what the client wants answers every probe with
     * Bad_TooManyPublishRequests. The client must keep asking — otherwise a transient condition is
     * indistinguishable from a permanent one and the clamp is permanent again — but the asking must
     * cost the Server almost nothing: with a cooldown that doubles on each refusal, the number of
     * probes grows logarithmically in the number of responses the Server delivers, so doubling the
     * traffic adds a probe or two rather than doubling the probes.
     */
    @Test
    void aServerThatAlwaysRefusesSeesOnlyABoundedAndSlowlyGrowingNumberOfProbes() throws Exception {
      try (Fixture fixture = Fixture.withOneSubscription(SERVER_PUBLISH_CAP)) {
        fixture.assertPipelineSettlesAt(SINGLE_SUBSCRIPTION_CLAMP);

        assertEquals(
            1,
            fixture.scriptable.getRejectionCount(),
            "filling the pipeline against the capped Server cost one refusal");

        List<Integer> refusedAt = fixture.deliverAndRecordRefusals(PROBE_WINDOW_DELIVERIES);

        assertTrue(
            refusedAt.size() >= MIN_PROBES,
            "the client stopped probing the ceiling: over "
                + PROBE_WINDOW_DELIVERIES
                + " successful Publish round trips the Server was asked only "
                + refusedAt.size()
                + " time(s) whether it would now queue another PublishRequest, so a condition that"
                + " had long since passed would go on costing the client pipeline depth for as long"
                + " as it lived. The round trips the refusals were observed at were "
                + refusedAt);

        assertTrue(
            refusedAt.size() <= MAX_PROBES,
            "the client hammered the Server: "
                + refusedAt.size()
                + " refused probes over "
                + PROBE_WINDOW_DELIVERIES
                + " delivered NotificationMessages, more than the "
                + MAX_PROBES
                + " a cooldown that grows geometrically on each refusal allows. A Server that"
                + " always refuses must see a number of probes that grows only logarithmically in"
                + " the number of responses it delivers. The round trips they were observed at were"
                + " "
                + refusedAt);

        // The intervals between probes, the first measured from the clamp. Geometric backoff makes
        // them grow; nothing here requires how fast, only that no interval is shorter than the one
        // before it. The loop is empty for an implementation that probed once or twice in the whole
        // window, which has already satisfied the bound above by a wide margin.
        List<Integer> intervals = intervals(refusedAt);

        for (int i = 1; i < intervals.size(); i++) {
          assertTrue(
              intervals.get(i) >= intervals.get(i - 1),
              "the cooldown did not lengthen after a refused probe: the intervals between probes,"
                  + " in successful Publish round trips, were "
                  + intervals
                  + ". Each refusal must make the next wait longer, or a Server that always refuses"
                  + " is asked at a constant rate forever");
        }
      }
    }
  }

  /**
   * The immediate response to the fault, which recovery must leave exactly as it was.
   *
   * <p>Part 4 §5.14.5.1 buys one replacement per returning request, and {@link
   * PublishPipelineRefillTest} pins that for the first request to return after the fault. A probe
   * is a later and separate event; a cooldown short enough to fire inside that window would turn
   * this change into a regression of that one.
   */
  @Nested
  class ImmediateResponseToTheFault {

    /**
     * Passes both before and after recovery is implemented — deliberately. It is the guard that
     * keeps the cooldown clear of the window {@link PublishPipelineRefillTest} measures.
     */
    @Test
    void theFirstFewReturningRequestsStillBuyExactlyOneReplacementEach() throws Exception {
      try (Fixture fixture = Fixture.withOneSubscription(SERVER_PUBLISH_CAP)) {
        fixture.assertPipelineSettlesAt(SINGLE_SUBSCRIPTION_CLAMP);

        // Nothing the Server does from here can refuse a request, so an extra request going out is
        // the client's own decision and is visible as one arriving at the Server.
        fixture.scriptable.setCap(CappingSubscriptionServiceSet.UNCAPPED);

        int arrivalsBefore = fixture.scriptable.getArrivalCount();

        int highWater = fixture.deliverKeepAlives(QUIET_PREFIX_DELIVERIES);

        // Let anything already in flight arrive before the count is compared, so that an extra
        // request cannot be missed by having been sent a moment too late.
        assertFalse(
            fixture.scriptable.awaitArrivals(
                arrivalsBefore + QUIET_PREFIX_DELIVERIES + 1, QUIET_WINDOW_MILLIS),
            "more Publish requests were sent than the number of requests that returned");

        assertEquals(
            QUIET_PREFIX_DELIVERIES,
            fixture.scriptable.getArrivalCount() - arrivalsBefore,
            "one returning request bought more than one replacement within the first "
                + QUIET_PREFIX_DELIVERIES
                + " successful Publish round trips after the clamp, which is the window Part 4"
                + " §5.14.5.1's one-replacement-per-return rule is measured in");

        assertEquals(
            SINGLE_SUBSCRIPTION_CLAMP,
            highWater,
            "the clamp was lifted within the first "
                + QUIET_PREFIX_DELIVERIES
                + " successful Publish round trips after the fault; the cooldown before a probe"
                + " must be longer than that");
      }
    }
  }

  // region helpers

  /**
   * @param points the round trip counts at which something was observed, in order.
   * @return the intervals between them, the first measured from the start of the run.
   */
  private static List<Integer> intervals(List<Integer> points) {
    var intervals = new ArrayList<Integer>(points.size());
    int previous = 0;

    for (int point : points) {
      intervals.add(point - previous);
      previous = point;
    }

    return intervals;
  }

  /**
   * A {@link ScriptableSubscriptionServiceSet} that also models a Server which will not queue more
   * than a fixed number of PublishRequests for a Session: one that would take its queue past the
   * cap is answered Bad_TooManyPublishRequests, which is what Part 4 §5.14.5.1 has a Server do when
   * a Client has more requests queued than it supports. The cap is settable, so a test can model a
   * condition that passes.
   *
   * <p>The queue is the set of requests the script is holding, which is every request that has
   * arrived and not been answered — this harness answers nothing on its own. A Server queues a
   * PublishRequest from the moment it arrives until it has something to send in answer, so a test
   * that answers one request at a time and waits for the client's reply to land before answering
   * the next holds the queue exactly as a Server with data to send at a steady rate would.
   */
  private static final class CappingSubscriptionServiceSet
      extends ScriptableSubscriptionServiceSet {

    /** A Server that refuses nothing. */
    static final int UNCAPPED = Integer.MAX_VALUE;

    /**
     * Guards the counters below and, held across the decision, makes "measure the queue, then join
     * it" atomic: two requests arriving together must not both find room in a queue with one place
     * left.
     */
    private final ReentrantLock lock = new ReentrantLock();

    /** Signalled whenever a Publish request arrives, refused or not. */
    private final Condition arrived = lock.newCondition();

    /** Publish requests received, refused ones included. Guarded by {@link #lock}. */
    private int arrivals = 0;

    /** Publish requests refused for exceeding the cap. Guarded by {@link #lock}. */
    private int rejections = 0;

    private volatile int cap;

    CappingSubscriptionServiceSet(OpcUaServer server, int cap) {
      super(server);

      this.cap = cap;
    }

    void setCap(int cap) {
      this.cap = cap;
    }

    int getArrivalCount() {
      lock.lock();
      try {
        return arrivals;
      } finally {
        lock.unlock();
      }
    }

    int getRejectionCount() {
      lock.lock();
      try {
        return rejections;
      } finally {
        lock.unlock();
      }
    }

    /** Wait until at least {@code count} Publish requests have arrived, refused ones included. */
    boolean awaitArrivals(int count, long timeoutMillis) throws InterruptedException {
      long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis);

      lock.lock();
      try {
        while (arrivals < count) {
          long remaining = deadline - System.nanoTime();
          if (remaining <= 0) {
            return false;
          }
          arrived.awaitNanos(remaining);
        }

        return true;
      } finally {
        lock.unlock();
      }
    }

    /**
     * Wait until at least {@code count} Publish requests are parked, i.e. until the client has that
     * many outstanding for the script to answer.
     */
    boolean awaitParked(int count, long timeoutMillis) throws InterruptedException {
      long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis);

      lock.lock();
      try {
        while (getParkedRequestCount() < count) {
          long remaining = deadline - System.nanoTime();
          if (remaining <= 0) {
            return false;
          }
          arrived.awaitNanos(remaining);
        }

        return true;
      } finally {
        lock.unlock();
      }
    }

    @Override
    public CompletableFuture<PublishResponse> onPublish(
        ServiceRequestContext context, PublishRequest request) {

      lock.lock();
      try {
        arrivals++;
        arrived.signalAll();

        if (getParkedRequestCount() >= cap) {
          rejections++;

          return CompletableFuture.failedFuture(
              new UaException(StatusCodes.Bad_TooManyPublishRequests));
        }

        return super.onPublish(context, request);
      } finally {
        lock.unlock();
      }
    }
  }

  /**
   * A running Server whose Publish requests are all parked by {@link CappingSubscriptionServiceSet}
   * — nothing completes one unless the test says so, and nothing is refused unless its cap says so
   * — plus a connected client with its Subscriptions already created.
   */
  private static final class Fixture implements AutoCloseable {

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final CappingSubscriptionServiceSet scriptable;

    private final List<OpcUaSubscription> subscriptions = new ArrayList<>();

    /** min(subscriptionCount + 1, maxPendingPublishRequests) for this fixture's client. */
    private final int naturalTarget;

    static Fixture withOneSubscription(int cap) throws Exception {
      return new Fixture(1, SINGLE_SUBSCRIPTION_TARGET, cap);
    }

    /**
     * A fixture whose four-deep pipeline fills against a Server that refuses nothing, so the tests
     * that need a deep clamp can install one themselves, request by request.
     */
    static Fixture withThreeSubscriptions() throws Exception {
      return new Fixture(
          DEEP_PIPELINE_SUBSCRIPTIONS,
          DEEP_PIPELINE_TARGET,
          CappingSubscriptionServiceSet.UNCAPPED);
    }

    private Fixture(int subscriptionCount, int naturalTarget, int cap) throws Exception {
      this.naturalTarget = naturalTarget;

      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable = new CappingSubscriptionServiceSet(server, cap);
      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), scriptable);
      }

      server.startup().get();

      client =
          TestClient.create(
              server,
              cfg ->
                  cfg
                      // Long request timeout so parked Publish requests do not time out.
                      .setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS))
                      // No Session keep-alive traffic: the only requests in flight during a
                      // test are the ones it scripts.
                      .setKeepAliveInterval(uint(REQUEST_TIMEOUT_MILLIS))
                      // States the natural target in the test rather than inheriting the config
                      // default: min(subscriptionCount + 1, naturalTarget) == naturalTarget.
                      .setMaxPendingPublishRequests(uint(naturalTarget)));

      client.connect();

      for (int i = 0; i < subscriptionCount; i++) {
        var subscription = new OpcUaSubscription(client);
        subscription.create();

        subscriptions.add(subscription);
      }
    }

    /**
     * The Subscription every keep-alive is addressed to. The others exist only to raise the natural
     * target; a Subscription that receives nothing has nothing to say about the pipeline's depth,
     * and its watchdog timer merely notifies a listener it has none of.
     */
    UInteger notifiedSubscriptionId() {
      return subscriptions.get(0).getSubscriptionId().orElseThrow();
    }

    /**
     * Assert the pipeline has settled at {@code expected} outstanding Publish requests: the traffic
     * has stopped, and what the Server is left holding when it does is the ceiling the client is
     * working to.
     *
     * <p>Quiescence is waited for rather than merely sampled, because the client's opening burst is
     * one request per unit of its natural target and the tail of that burst is traffic which has
     * not stopped yet rather than traffic which never will.
     */
    void assertPipelineSettlesAt(int expected) throws Exception {
      assertTrue(
          scriptable.awaitArrivals(naturalTarget, AWAIT_TIMEOUT_MILLIS),
          "the client never sent the Publish requests its natural target calls for");

      long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(AWAIT_TIMEOUT_MILLIS);

      int arrivals;
      do {
        arrivals = scriptable.getArrivalCount();
      } while (scriptable.awaitArrivals(arrivals + 1, QUIET_WINDOW_MILLIS)
          && System.nanoTime() < deadline);

      assertEquals(
          arrivals,
          scriptable.getArrivalCount(),
          "the Publish pipeline never settled: the client kept sending requests without being"
              + " answered. Recovery must be driven by successful Publish round trips, not by time"
              + " passing");

      assertEquals(
          expected,
          scriptable.getParkedRequestCount(),
          "the Publish pipeline settled at a depth the test did not set up");
    }

    /**
     * Answer one parked Publish request with a keep-alive and wait until the client has dealt with
     * it, i.e. until every Publish request it sends in reply has arrived.
     *
     * <p>A request is answered only once one is parked, never by leaving a responder ready for a
     * request that has not arrived yet: a responder waiting in the script would answer the next
     * arrival immediately, and the Server's queue would never hold the two requests a probe
     * consists of at the same time.
     *
     * <p>The same reason is why the reply is waited out rather than merely waited for. A client
     * probing its ceiling sends two requests from one loop, microseconds apart; answering the first
     * before the second arrives would empty the queue in between and let the probe through a cap it
     * should have hit. {@link #BURST_SETTLE_MILLIS} is orders of magnitude more than that gap, and
     * is spent only on the reply that never comes — the second request of a burst that was not a
     * probe.
     */
    private void deliverOneKeepAlive() throws Exception {
      assertTrue(
          scriptable.awaitParked(1, AWAIT_TIMEOUT_MILLIS),
          "the client has no Publish request outstanding: the pipeline has stalled");

      int arrivals = scriptable.getArrivalCount();

      scriptable.enqueueKeepAlive(notifiedSubscriptionId(), KEEP_ALIVE_SEQUENCE_NUMBER);

      assertTrue(
          scriptable.awaitArrivals(arrivals + 1, AWAIT_TIMEOUT_MILLIS),
          "the answered Publish request was never replaced: the pipeline has stalled");

      scriptable.awaitArrivals(arrivals + 2, BURST_SETTLE_MILLIS);
    }

    /**
     * Deliver {@code count} keep-alives, one at a time.
     *
     * @return the greatest number of Publish requests the Server was left holding after any of
     *     them, i.e. the highest ceiling the client reached.
     */
    int deliverKeepAlives(int count) throws Exception {
      int highWater = 0;

      for (int i = 0; i < count; i++) {
        deliverOneKeepAlive();

        highWater = Math.max(highWater, sampleOutstanding());
      }

      return highWater;
    }

    /**
     * Deliver {@code count} keep-alives, one at a time, watching for the Server refusing one.
     *
     * @return the number of successful Publish round trips that had been delivered when each
     *     refused request was observed, in order. Each entry is a probe: an extra PublishRequest
     *     the client sent to find out whether the Server would now queue one more.
     */
    List<Integer> deliverAndRecordRefusals(int count) throws Exception {
      var refusedAt = new ArrayList<Integer>();
      int rejections = scriptable.getRejectionCount();

      for (int delivered = 1; delivered <= count; delivered++) {
        deliverOneKeepAlive();

        int total = scriptable.getRejectionCount();

        for (int i = rejections; i < total; i++) {
          refusedAt.add(delivered);
        }

        rejections = total;
      }

      return refusedAt;
    }

    /**
     * Deliver keep-alives, one at a time, until the Server is left holding {@code target} Publish
     * requests or {@code maxDeliveries} have been delivered.
     *
     * @return every increase in the number of Publish requests the Server was left holding, in the
     *     order they were first observed: the path the ceiling took from {@code clamp} upwards.
     */
    List<Integer> deliverUntilPipelineReaches(int clamp, int target, int maxDeliveries)
        throws Exception {

      var steps = new ArrayList<Integer>();
      int outstanding = clamp;

      for (int i = 0; i < maxDeliveries && outstanding < target; i++) {
        deliverOneKeepAlive();

        int sampled = sampleOutstanding();

        if (sampled > outstanding) {
          outstanding = sampled;
          steps.add(sampled);
        }
      }

      return steps;
    }

    /**
     * @return the number of Publish requests the Server is holding, which between two answers is
     *     the number the client is keeping outstanding.
     */
    private int sampleOutstanding() {
      int outstanding = scriptable.getParkedRequestCount();

      assertTrue(
          outstanding <= naturalTarget,
          "the client kept "
              + outstanding
              + " Publish requests outstanding, more than the natural target of "
              + naturalTarget);

      return outstanding;
    }

    @Override
    public void close() throws Exception {
      scriptable.setCap(CappingSubscriptionServiceSet.UNCAPPED);
      scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(10, TimeUnit.SECONDS);
      }
    }
  }

  // endregion
}
