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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SetPublishingModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetPublishingModeResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusChangeNotification;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * What an {@link OpcUaSubscription} lifecycle transition is allowed to hold up while it waits for
 * the Server.
 *
 * <p>{@code create()}, {@code modify()}, {@code delete()}, {@code setPublishingMode(boolean)} and
 * {@code reset()} serialize on one private lock, and the first four hold it across their blocking
 * service call. {@code reset()} therefore waits for a full network round trip before it can do its
 * work — and {@code reset()} is not called only by applications:
 *
 * <ul>
 *   <li>{@code PublishingManager} calls it, via {@code notifyStatusChanged}, when the Server
 *       reports Bad_Timeout for a Subscription (Part 4 §5.13.1.1: the Subscription no longer
 *       exists). That runs on the Subscription's <b>delivery queue</b>, which is backed by the
 *       transport executor.
 *   <li>The Session FSM calls it, via {@code notifyTransferFailed}, when TransferSubscriptions did
 *       not carry a Subscription over to a new Session.
 * </ul>
 *
 * <p>Two consequences, and the two nested classes assert against them:
 *
 * <ol>
 *   <li><b>Stall.</b> On any executor, including the unbounded cached pool the client uses by
 *       default, the Bad_Timeout path waits behind whatever lifecycle call happens to be in flight
 *       — up to a full request timeout, 60s by default. The Subscription's delivery queue is
 *       stopped for that whole time, and a Session FSM callback that blocks is worse still.
 *   <li><b>Deadlock.</b> The threads that block are executor threads, and the response that would
 *       release the lock is completed <i>by the transport executor</i> ({@code
 *       AbstractUascClientTransport.handleResponse}). A client whose executor has one thread — an
 *       ordinary application choice; {@code OpcTcpClientTransportConfigBuilder.setExecutor} exists
 *       for it — therefore wedges permanently: the only thread that could complete the in-flight
 *       lifecycle call's response is the one blocked inside {@code reset()}, and {@code
 *       handleResponse} has already cancelled the request timeout that would otherwise rescue it.
 * </ol>
 *
 * <p><b>A failure here manifests as a timeout</b>, and that is the correct signal: the defect is
 * precisely that a call which does no network I/O of its own does not return. Every wait below is
 * bounded, every thread the tests start is a daemon, and both executors use daemon threads, so a
 * permanently blocked thread cannot hang the build or keep the JVM alive; teardown tolerates a
 * client too wedged to disconnect.
 */
public class SubscriptionLifecycleContentionTest {

  /** How long to wait for something that must happen. */
  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /**
   * How long a call that performs no network I/O of its own is given to return. A loopback round
   * trip completes in single-digit milliseconds, so this is generous by two orders of magnitude
   * against any implementation that does not wait for one — and an implementation that does wait
   * for the gated call below would need {@link #GATE_TIMEOUT_MILLIS}, so no larger value would
   * change an outcome.
   */
  private static final long NON_BLOCKING_WINDOW_MILLIS = 2_000;

  /**
   * How long to wait for a Server-side Subscription to be cleaned up. Well under the Subscription's
   * own lifetime on the Server — the default 1000ms PublishingInterval derives a LifetimeCount of
   * 30, i.e. 30s — so a Subscription expiring on its own cannot satisfy the assertion.
   */
  private static final long CLEANUP_WINDOW_MILLIS = 5_000;

  /** Upper bound on how long a gated Server handler holds a request, so nothing hangs forever. */
  private static final long GATE_TIMEOUT_MILLIS = 30_000;

  /**
   * Long enough that nothing times out on its own: neither a parked Publish request nor a gated
   * lifecycle call. The stalls asserted against below are therefore the client's own doing.
   */
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;

  /** How long teardown waits for a client that may be wedged to disconnect. */
  private static final long DISCONNECT_TIMEOUT_MILLIS = 5_000;

  /** The client keeps one more PublishRequest in flight than it has Subscriptions. */
  private static final int PIPELINE_DEPTH = 2;

  /**
   * {@code reset()} and {@code notifyTransferFailed()} discard the client's knowledge of a
   * Subscription. Neither sends anything to the Server, so neither has any reason to wait for it.
   */
  @Nested
  class ResetWhileACreateIsInFlight {

    /**
     * The core defect. A {@code create()} parked inside the CreateSubscription service call holds
     * the lifecycle lock, so {@code reset()} — which only has local state to discard — cannot run
     * until the Server answers.
     *
     * <p>Failure manifests as {@code reset()} not returning inside {@link
     * #NON_BLOCKING_WINDOW_MILLIS}.
     */
    @Test
    void resetCompletesWhileACreateIsInFlight() throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        var subscription = new OpcUaSubscription(fixture.client);

        Call create = fixture.startGatedCreate(subscription);
        Call reset = Call.start("reset", subscription::reset);

        assertTrue(
            reset.awaitFinished(NON_BLOCKING_WINDOW_MILLIS),
            "reset() has not returned "
                + NON_BLOCKING_WINDOW_MILLIS
                + "ms after it was called, because a create() parked inside the CreateSubscription"
                + " service call is holding the lifecycle lock. On the Bad_Timeout path this call"
                + " is made from the Subscription's delivery queue and on the transfer-failed path"
                + " from the Session FSM, so both are stopped for as long as the Server takes to"
                + " answer someone else's request");

        assertFalse(
            create.isFinished(),
            "the gated create() has already returned, so reset() did not overlap it and the"
                + " assertion above proved nothing");
      }
    }

    /**
     * The same defect on the path the Session FSM takes. {@code notifyTransferFailed()} is
     * dispatched when TransferSubscriptions fails to carry a Subscription over to a new Session,
     * and it resets the Subscription; blocking there blocks the state machine that owns
     * reconnection.
     */
    @Test
    void notifyTransferFailedCompletesWhileACreateIsInFlight() throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        var subscription = new OpcUaSubscription(fixture.client);

        Call create = fixture.startGatedCreate(subscription);
        Call transferFailed =
            Call.start(
                "transfer-failed",
                () ->
                    subscription.notifyTransferFailed(
                        new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid)));

        assertTrue(
            transferFailed.awaitFinished(NON_BLOCKING_WINDOW_MILLIS),
            "notifyTransferFailed() has not returned "
                + NON_BLOCKING_WINDOW_MILLIS
                + "ms after it was called: it resets the Subscription, and the reset waits for the"
                + " CreateSubscription round trip a concurrent create() is holding the lifecycle"
                + " lock across. This callback runs on the Session FSM");

        assertFalse(
            create.isFinished(),
            "the gated create() has already returned, so notifyTransferFailed() did not overlap it"
                + " and the assertion above proved nothing");
      }
    }

    /**
     * The control that keeps the two timing assertions above from being vacuous: with no lifecycle
     * call in flight, {@code reset()} returns well inside the same window.
     */
    @Test
    void resetCompletesWhenNoCreateIsInFlight() throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        OpcUaSubscription subscription = fixture.createSubscription();

        Call reset = Call.start("reset", subscription::reset);

        assertTrue(
            reset.awaitFinished(NON_BLOCKING_WINDOW_MILLIS),
            "control: reset() must return promptly when no lifecycle call is in flight");
      }
    }

    /**
     * A {@code reset()} that supersedes an in-flight {@code create()} must not abandon the
     * Subscription that call went on to create. Part 4 §5.13.8 gives DeleteSubscriptions the
     * SubscriptionIds as its only handle on a Subscription, so a Subscription the Server created
     * and no client object still names runs until its lifetime expires and cannot be deleted — the
     * same leak the immutable-id work was about.
     *
     * <p>Nothing here is asserted about timing; this is about what is left on the Server once both
     * calls have finished, in whichever order they finish.
     */
    @Test
    void noServerSideSubscriptionSurvivesAResetIssuedDuringACreate() throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        var subscription = new OpcUaSubscription(fixture.client);

        Call create = fixture.startGatedCreate(subscription);
        Call reset = Call.start("reset", subscription::reset);

        fixture.scriptable.releaseCreates();

        assertTrue(create.awaitFinished(AWAIT_TIMEOUT_MILLIS), "create() never finished");
        assertTrue(reset.awaitFinished(AWAIT_TIMEOUT_MILLIS), "reset() never finished");

        assertFalse(
            fixture.scriptable.createdSubscriptionIds().isEmpty(),
            "the gated create() never created a Subscription on the Server, so there was nothing"
                + " that could have been leaked and the assertion below proves nothing");

        assertTrue(
            fixture.awaitTrue(
                () -> fixture.unnamedServerSubscriptions(subscription).isEmpty(),
                CLEANUP_WINDOW_MILLIS),
            () ->
                "the Server is still running Subscription(s) "
                    + fixture.unnamedServerSubscriptions(subscription)
                    + " that no client object names: the create() that made them completed after a"
                    + " reset() had discarded the Subscription, so its ServerState was either"
                    + " overwritten or thrown away and delete() can no longer name it. Created on"
                    + " the Server: "
                    + fixture.scriptable.createdSubscriptionIds()
                    + ", named by the client: "
                    + subscription.getSubscriptionId());
      }
    }

    /**
     * The invariant the lifecycle lock was added for, asserted here so that removing the lock from
     * around the service call cannot quietly give it up: a second {@code create()} made while the
     * first is still in flight is answered {@code Bad_InvalidState} and does not create a second
     * Subscription on the Server. Passes today; it is a guard, not a reproduction.
     */
    @Test
    void secondCreateIsRejectedWhileTheFirstIsInFlight() throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        var subscription = new OpcUaSubscription(fixture.client);

        Call first = fixture.startGatedCreate(subscription);
        Call second = Call.start("create-second", subscription::create);

        // Either the second call has been rejected client-side, or it has reached the Server. Not
        // asserted: an implementation that blocks it on the lock does neither until the gate opens,
        // and the assertions below hold just as well then.
        fixture.awaitTrue(
            () -> second.isFinished() || fixture.scriptable.gatedCreateArrivals() >= 2,
            NON_BLOCKING_WINDOW_MILLIS);

        fixture.scriptable.releaseCreates();

        assertTrue(first.awaitFinished(AWAIT_TIMEOUT_MILLIS), "the first create() never finished");
        assertTrue(
            second.awaitFinished(AWAIT_TIMEOUT_MILLIS), "the second create() never finished");

        UaException failure =
            assertInstanceOf(
                UaException.class,
                second.failure().orElse(null),
                "the second create() must be rejected while a Subscription already exists or is"
                    + " being created, or the Server ends up running a Subscription whose"
                    + " SubscriptionId the client object immediately overwrites");
        assertEquals(
            StatusCodes.Bad_InvalidState,
            failure.getStatusCode().value(),
            "the second create() must fail with Bad_InvalidState");

        assertEquals(
            1,
            fixture.scriptable.createdSubscriptionIds().size(),
            "two concurrent create() calls created "
                + fixture.scriptable.createdSubscriptionIds().size()
                + " Subscriptions on the Server; only one of them can be named by the single"
                + " ServerState the client object holds");
      }
    }
  }

  /**
   * The Bad_Timeout StatusChangeNotification path, which is how {@code reset()} is reached in
   * production. {@code PublishingManager} delivers the notification on the Subscription's delivery
   * queue and {@code notifyStatusChanged} resets the Subscription before handing the status to the
   * application, so a blocked reset blocks the queue and the callback with it.
   */
  @Nested
  class StatusChangeWhileALifecycleCallIsInFlight {

    /**
     * Hazard 2, on the shape of executor the client uses by default (an unbounded cached pool, as
     * {@code Stack.sharedExecutor()} is). No thread starvation is involved: the delivery queue
     * simply waits for a SetPublishingMode round trip it has nothing to do with, and until it
     * returns the application is not told its Subscription has timed out and no further
     * notification for that Subscription is delivered.
     *
     * <p>Failure manifests as the {@code onStatusChanged} callback not arriving inside {@link
     * #NON_BLOCKING_WINDOW_MILLIS}.
     */
    @Test
    void badTimeoutStatusChangeIsDeliveredWhileASetPublishingModeIsInFlight() throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        OpcUaSubscription subscription = fixture.createSubscription();

        var statusChanged = new CountDownLatch(1);
        subscription.setSubscriptionListener(statusChangeListener(statusChanged));

        UInteger id = subscriptionId(subscription);
        fixture.awaitPipelineFilled();

        Call setPublishingMode = fixture.startGatedSetPublishingMode(subscription);

        fixture.scriptable.enqueueNotification(id, 1, badTimeout(fixture.scriptable), uint(1));

        assertTrue(
            statusChanged.await(NON_BLOCKING_WINDOW_MILLIS, TimeUnit.MILLISECONDS),
            "the Bad_Timeout StatusChangeNotification was not delivered within "
                + NON_BLOCKING_WINDOW_MILLIS
                + "ms: notifyStatusChanged() resets the Subscription first, and that reset is"
                + " waiting for the SetPublishingMode round trip the lifecycle lock is held across."
                + " The Subscription's delivery queue is stopped for the whole round trip, which is"
                + " bounded only by the request timeout");

        assertFalse(
            setPublishingMode.isFinished(),
            "the gated setPublishingMode() has already returned, so nothing was in flight while the"
                + " status change was delivered and the assertion above proved nothing");
      }
    }

    /**
     * Hazard 1: the same interleaving on a single-threaded transport executor is not a stall but a
     * permanent deadlock. The delivery queue runs on that executor, so the thread blocked inside
     * {@code reset()} is the only thread that could complete the SetPublishingMode response the
     * lock-holder is waiting for, and {@code handleResponse} cancelled that request's timeout the
     * moment the response arrived.
     *
     * <p>The first assertion is the stall; the second — made only once the Server has answered — is
     * the deadlock: nothing can ever release the in-flight call.
     */
    @Test
    void badTimeoutStatusChangeIsDeliveredWhileASetPublishingModeIsInFlightOnOneThread()
        throws Exception {

      try (var fixture = new Fixture(singleThreadExecutor())) {
        OpcUaSubscription subscription = fixture.createSubscription();

        var statusChanged = new CountDownLatch(1);
        subscription.setSubscriptionListener(statusChangeListener(statusChanged));

        UInteger id = subscriptionId(subscription);
        fixture.awaitPipelineFilled();

        Call setPublishingMode = fixture.startGatedSetPublishingMode(subscription);

        fixture.scriptable.enqueueNotification(id, 1, badTimeout(fixture.scriptable), uint(1));

        assertTrue(
            statusChanged.await(NON_BLOCKING_WINDOW_MILLIS, TimeUnit.MILLISECONDS),
            "the Bad_Timeout StatusChangeNotification was not delivered within "
                + NON_BLOCKING_WINDOW_MILLIS
                + "ms: the delivery queue task is blocked inside reset(), waiting for the lifecycle"
                + " lock a SetPublishingMode round trip is holding, and it is occupying the"
                + " transport executor's only thread while it waits");

        fixture.scriptable.releaseSetPublishingMode();

        assertTrue(
            setPublishingMode.awaitFinished(AWAIT_TIMEOUT_MILLIS),
            "setPublishingMode() never returned although the Server answered it: the response is"
                + " completed by the transport executor, whose only thread is blocked inside a"
                + " reset() waiting for the lock setPublishingMode() holds. The request timeout was"
                + " cancelled when the response arrived, so nothing breaks the cycle");
      }
    }

    /**
     * The control for both tests above: the identical script with no lifecycle call in flight
     * delivers the status change. Without it, a Bad_Timeout that is never delivered at all would
     * look the same as one that is delivered late.
     */
    @Test
    void badTimeoutStatusChangeIsDeliveredWhenNoLifecycleCallIsInFlight() throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        OpcUaSubscription subscription = fixture.createSubscription();

        var statusChanged = new CountDownLatch(1);
        subscription.setSubscriptionListener(statusChangeListener(statusChanged));

        UInteger id = subscriptionId(subscription);
        fixture.awaitPipelineFilled();

        fixture.scriptable.enqueueNotification(id, 1, badTimeout(fixture.scriptable), uint(1));

        assertTrue(
            statusChanged.await(NON_BLOCKING_WINDOW_MILLIS, TimeUnit.MILLISECONDS),
            "control: a Bad_Timeout StatusChangeNotification must be delivered promptly when no"
                + " lifecycle call is in flight");
      }
    }

    /**
     * The control that isolates the deadlock as the cause rather than the executor: on the same
     * single-threaded executor, a SetPublishingMode that nothing holds up completes, and the
     * Bad_Timeout that follows it is delivered.
     */
    @Test
    void badTimeoutStatusChangeIsDeliveredOnOneThreadWhenNoLifecycleCallIsInFlight()
        throws Exception {

      try (var fixture = new Fixture(singleThreadExecutor())) {
        OpcUaSubscription subscription = fixture.createSubscription();

        var statusChanged = new CountDownLatch(1);
        subscription.setSubscriptionListener(statusChangeListener(statusChanged));

        UInteger id = subscriptionId(subscription);
        fixture.awaitPipelineFilled();

        Call setPublishingMode =
            Call.start("set-publishing-mode", () -> subscription.setPublishingMode(false));

        assertTrue(
            setPublishingMode.awaitFinished(AWAIT_TIMEOUT_MILLIS),
            "control: a lifecycle call must complete on a single-threaded transport executor when"
                + " nothing is occupying that thread");
        assertEquals(
            Optional.empty(),
            setPublishingMode.failure(),
            "control: the ungated setPublishingMode() must succeed");

        fixture.scriptable.enqueueNotification(id, 1, badTimeout(fixture.scriptable), uint(1));

        assertTrue(
            statusChanged.await(NON_BLOCKING_WINDOW_MILLIS, TimeUnit.MILLISECONDS),
            "control: a Bad_Timeout StatusChangeNotification must be delivered on a"
                + " single-threaded transport executor when nothing is occupying that thread");
      }
    }
  }

  // region helpers

  private static UInteger subscriptionId(OpcUaSubscription subscription) {
    return subscription.getSubscriptionId().orElseThrow();
  }

  private static OpcUaSubscription.SubscriptionListener statusChangeListener(
      CountDownLatch statusChanged) {

    return new OpcUaSubscription.SubscriptionListener() {
      @Override
      public void onStatusChanged(OpcUaSubscription subscription, StatusCode status) {
        statusChanged.countDown();
      }
    };
  }

  /** A NotificationMessage body reporting that the Subscription has timed out. */
  private static ExtensionObject[] badTimeout(ScriptableSubscriptionServiceSet scriptable) {
    return new ExtensionObject[] {
      scriptable.encode(
          new StatusChangeNotification(
              new StatusCode(StatusCodes.Bad_Timeout), DiagnosticInfo.NULL_VALUE))
    };
  }

  /**
   * A lifecycle call running on its own daemon thread, with the outcome recorded however it turned
   * out.
   *
   * <p>The thread is a daemon and the outcome is recorded in a {@code finally} block because these
   * calls are expected to block indefinitely when the defect is present: the test has to be able to
   * observe that and move on, and the JVM has to be able to exit afterwards.
   */
  private static final class Call {

    private final CountDownLatch finished = new CountDownLatch(1);
    private final AtomicReference<Exception> failure = new AtomicReference<>();

    private final Thread thread;

    private Call(String name, ThrowingRunnable body) {
      this.thread =
          new Thread(
              () -> {
                try {
                  body.run();
                } catch (Exception e) {
                  failure.set(e);
                } finally {
                  finished.countDown();
                }
              },
              name);
      this.thread.setDaemon(true);
    }

    static Call start(String name, ThrowingRunnable body) {
      var call = new Call(name, body);
      call.thread.start();

      return call;
    }

    boolean awaitFinished(long timeoutMillis) throws InterruptedException {
      return finished.await(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    boolean isFinished() {
      return finished.getCount() == 0;
    }

    /** The Exception the call threw, if any. */
    Optional<Exception> failure() {
      return Optional.ofNullable(failure.get());
    }
  }

  /**
   * A {@link ScriptableSubscriptionServiceSet} that can hold a CreateSubscription or a
   * SetPublishingMode request inside the Server handler until the test releases it, and that
   * records every SubscriptionId it hands out.
   *
   * <p>Holding a request <i>inside</i> the handler is what makes the overlap real rather than
   * raced: the client has sent the request and is waiting for the response, so it is demonstrably
   * inside the service call — and therefore holding whatever the service call holds — for as long
   * as the test wants.
   */
  private static final class GatedSubscriptionServiceSet extends ScriptableSubscriptionServiceSet {

    private final AtomicInteger gatedCreateArrivals = new AtomicInteger(0);
    private final AtomicInteger gatedSetPublishingModeArrivals = new AtomicInteger(0);

    private final CountDownLatch createGate = new CountDownLatch(1);
    private final CountDownLatch setPublishingModeGate = new CountDownLatch(1);

    private final List<UInteger> createdSubscriptionIds = new CopyOnWriteArrayList<>();

    private volatile boolean createsGated = false;
    private volatile boolean setPublishingModeGated = false;

    GatedSubscriptionServiceSet(OpcUaServer server) {
      super(server);
    }

    void gateCreates() {
      createsGated = true;
    }

    void gateSetPublishingMode() {
      setPublishingModeGated = true;
    }

    int gatedCreateArrivals() {
      return gatedCreateArrivals.get();
    }

    int gatedSetPublishingModeArrivals() {
      return gatedSetPublishingModeArrivals.get();
    }

    void releaseCreates() {
      createGate.countDown();
    }

    void releaseSetPublishingMode() {
      setPublishingModeGate.countDown();
    }

    void releaseAllGates() {
      releaseCreates();
      releaseSetPublishingMode();
    }

    /** Every SubscriptionId the Server has assigned, in the order it assigned them. */
    List<UInteger> createdSubscriptionIds() {
      return List.copyOf(createdSubscriptionIds);
    }

    @Override
    public CreateSubscriptionResponse onCreateSubscription(
        ServiceRequestContext context, CreateSubscriptionRequest request) throws UaException {

      if (createsGated) {
        gatedCreateArrivals.incrementAndGet();
        await(createGate, "CreateSubscription");
      }

      CreateSubscriptionResponse response = super.onCreateSubscription(context, request);
      createdSubscriptionIds.add(response.getSubscriptionId());

      return response;
    }

    @Override
    public SetPublishingModeResponse onSetPublishingMode(
        ServiceRequestContext context, SetPublishingModeRequest request) throws UaException {

      if (setPublishingModeGated) {
        gatedSetPublishingModeArrivals.incrementAndGet();
        await(setPublishingModeGate, "SetPublishingMode");
      }

      return super.onSetPublishingMode(context, request);
    }

    private static void await(CountDownLatch gate, String service) throws UaException {
      try {
        if (!gate.await(GATE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
          throw new UaException(
              StatusCodes.Bad_Timeout, "the " + service + " gate was never opened");
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new UaException(StatusCodes.Bad_UnexpectedError, e);
      }
    }
  }

  /**
   * A running Server whose Publish responses are scripted and whose CreateSubscription and
   * SetPublishingMode responses can be held, plus a client driven by a caller-supplied {@link
   * ExecutorService}.
   */
  private static final class Fixture implements AutoCloseable {

    private final ExecutorService executor;
    private final OpcUaServer server;
    private final OpcUaClient client;
    private final GatedSubscriptionServiceSet scriptable;

    Fixture(ExecutorService executor) throws Exception {
      this.executor = executor;

      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable = new GatedSubscriptionServiceSet(server);
      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), scriptable);
      }

      server.startup().get();

      client =
          TestClient.create(
              server,
              transportConfig -> transportConfig.setExecutor(executor),
              cfg ->
                  cfg
                      // Long request timeout so parked or gated requests do not time out.
                      .setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS))
                      // No Session keep-alive traffic: the only requests in flight during a test
                      // are the ones it scripts.
                      .setKeepAliveInterval(uint(REQUEST_TIMEOUT_MILLIS)));
      client.connect();
    }

    OpcUaSubscription createSubscription() throws UaException {
      var subscription = new OpcUaSubscription(client);
      subscription.create();

      return subscription;
    }

    /**
     * Start a {@code create()} on its own thread and return once it is inside the Server's
     * CreateSubscription handler, i.e. once it is demonstrably waiting for the response.
     */
    Call startGatedCreate(OpcUaSubscription subscription) throws Exception {
      scriptable.gateCreates();

      Call call = Call.start("create", subscription::create);

      assertTrue(
          awaitTrue(() -> scriptable.gatedCreateArrivals() >= 1, AWAIT_TIMEOUT_MILLIS),
          "the create() never reached the Server, so it is not inside the CreateSubscription"
              + " service call and the scenario under test did not happen");

      return call;
    }

    /**
     * Start a {@code setPublishingMode()} on its own thread and return once it is inside the
     * Server's SetPublishingMode handler.
     */
    Call startGatedSetPublishingMode(OpcUaSubscription subscription) throws Exception {
      scriptable.gateSetPublishingMode();

      Call call = Call.start("set-publishing-mode", () -> subscription.setPublishingMode(false));

      assertTrue(
          awaitTrue(() -> scriptable.gatedSetPublishingModeArrivals() >= 1, AWAIT_TIMEOUT_MILLIS),
          "the setPublishingMode() never reached the Server, so it is not inside the"
              + " SetPublishingMode service call and the scenario under test did not happen");

      return call;
    }

    void awaitPipelineFilled() throws Exception {
      assertTrue(
          awaitTrue(
              () -> scriptable.getParkedRequestCount() >= PIPELINE_DEPTH, AWAIT_TIMEOUT_MILLIS),
          "the client did not fill its Publish pipeline");
    }

    /**
     * The Subscriptions the Server is running that {@code subscription} cannot name, i.e. the ones
     * no DeleteSubscriptions call the client could make would name either.
     */
    Set<UInteger> unnamedServerSubscriptions(OpcUaSubscription subscription) {
      Optional<UInteger> named = subscription.getSubscriptionId();

      return server.getSubscriptions().keySet().stream()
          .filter(id -> named.filter(id::equals).isEmpty())
          .collect(Collectors.toUnmodifiableSet());
    }

    /** Polls {@code condition} until it holds or {@code timeoutMillis} elapses. */
    boolean awaitTrue(ThrowingBooleanSupplier condition, long timeoutMillis) throws Exception {
      long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis);

      while (System.nanoTime() < deadline) {
        if (condition.get()) {
          return true;
        }
        Thread.sleep(25);
      }

      return condition.get();
    }

    @Override
    public void close() throws Exception {
      scriptable.releaseAllGates();
      scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
      try {
        client.disconnectAsync().get(DISCONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
      } catch (TimeoutException ignored) {
        // A client whose executor thread is wedged behind a blocking reset() cannot run the
        // disconnect it is asked for. Tolerated so teardown does not mask the assertion that
        // detected the wedge; shutting the Server and the executor down below releases what can be
        // released, and every thread involved is a daemon.
      } finally {
        try {
          server.shutdown().get(10, TimeUnit.SECONDS);
        } finally {
          executor.shutdownNow();
        }
      }
    }
  }

  /**
   * The shape of {@code Stack.sharedExecutor()}: an unbounded cached pool. Daemon threads, so a
   * task left blocked by a failing test cannot keep the JVM alive.
   */
  private static ExecutorService cachedThreadPool() {
    return new ThreadPoolExecutor(
        0,
        Integer.MAX_VALUE,
        60L,
        TimeUnit.SECONDS,
        new SynchronousQueue<>(),
        daemonThreadFactory("lifecycle-contention-pool"));
  }

  /**
   * A single-threaded transport executor, which {@code
   * OpcTcpClientTransportConfigBuilder.setExecutor} exists to allow.
   */
  private static ExecutorService singleThreadExecutor() {
    return Executors.newSingleThreadExecutor(daemonThreadFactory("lifecycle-contention-single"));
  }

  private static ThreadFactory daemonThreadFactory(String namePrefix) {
    var counter = new AtomicInteger(0);

    return runnable -> {
      var thread = new Thread(runnable, namePrefix + "-" + counter.incrementAndGet());
      thread.setDaemon(true);

      return thread;
    };
  }

  @FunctionalInterface
  private interface ThrowingRunnable {
    void run() throws Exception;
  }

  @FunctionalInterface
  private interface ThrowingBooleanSupplier {
    boolean get() throws Exception;
  }

  // endregion
}
