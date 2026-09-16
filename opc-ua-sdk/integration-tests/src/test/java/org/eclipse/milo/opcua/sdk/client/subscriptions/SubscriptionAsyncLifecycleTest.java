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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * What thread an {@link OpcUaSubscription}'s advertised asynchronous lifecycle calls need in order
 * to finish.
 *
 * <p>{@code createAsync()}, {@code modifyAsync()}, {@code deleteAsync()} and {@code
 * setPublishingModeAsync(boolean)} are not composed from the client's asynchronous services. Each
 * one hands its <i>blocking</i> counterpart to {@code FutureUtils.supplyAsyncCompose} with the
 * transport executor, i.e. {@code CompletableFuture.supplyAsync(supplier, transportExecutor)}, so
 * the blocking call runs <b>on the transport executor</b> and waits there for a service response.
 *
 * <p>That response is completed <i>by the transport executor</i>: {@code
 * AbstractUascClientTransport.handleResponse} dispatches everything that is not a PublishResponse
 * via {@code config.getExecutor().execute(...)}. So the call waits on a thread pool for work only
 * that same thread pool can do, and it consumes one of its threads while it waits. Two
 * consequences, one per nested class below:
 *
 * <ol>
 *   <li><b>Self-deadlock.</b> With a single-threaded executor — an ordinary application choice;
 *       {@code OpcTcpClientTransportConfigBuilder.setExecutor} exists for it — the only thread that
 *       could complete the response is the one blocked waiting for it. The wedge is permanent
 *       rather than merely slow: {@code handleResponse} cancels the request timeout the moment the
 *       response arrives, so the wheel timer that rescues a Server which never answers cannot
 *       rescue a Server which does.
 *   <li><b>Pool exhaustion.</b> The same arithmetic on any bounded executor: n outstanding
 *       asynchronous lifecycle calls occupy n threads doing nothing, and once they occupy all of
 *       them none of their responses can be completed. Two concurrent {@code createAsync()} calls
 *       are enough on a two-thread pool.
 * </ol>
 *
 * <p>The Server is entirely healthy throughout: it answers every request immediately.
 *
 * <p><b>A failure here manifests as a timeout</b>, and that is the correct signal: the defect is
 * precisely that a {@link CompletionStage} the SDK handed out never completes and never fails, so
 * there is nothing else to observe. Every wait is bounded, both executors use daemon threads, and
 * teardown tolerates a client too wedged to disconnect — a RED run leaves a permanently blocked
 * thread, and it must not hang the build or leak into another test in the same fork.
 */
public class SubscriptionAsyncLifecycleTest {

  /**
   * How long an asynchronous lifecycle call is given to complete. Against a loopback Server every
   * one of them is a single round trip that completes in single-digit milliseconds, so this is
   * generous by three orders of magnitude — and a self-deadlocked call does not complete at all, so
   * no larger value would change an outcome.
   */
  private static final long ASYNC_TIMEOUT_MILLIS = 5_000;

  /**
   * Long enough that nothing times out on its own: neither a parked Publish request nor a lifecycle
   * request in flight. A stage that fails to complete below is therefore the client's own doing and
   * not a request timeout.
   */
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;

  /** How long teardown waits for a client that may be wedged to disconnect. */
  private static final long DISCONNECT_TIMEOUT_MILLIS = 5_000;

  /** The PublishingInterval {@code modifyAsync()} is asked to install; the default is 1000ms. */
  private static final double MODIFIED_PUBLISHING_INTERVAL = 2_000.0;

  /**
   * One thread, and the response every one of these calls waits for needs that thread to be free.
   *
   * <p>Where a test needs a Subscription that already exists, it is created with the
   * <i>blocking</i> {@code create()}, called from the test thread: a blocking call made from a
   * thread that is not an executor thread completes on any executor, which is what leaves the
   * asynchronous call that follows as the only thing under test.
   */
  @Nested
  class SelfDeadlockOnASingleThreadedExecutor {

    /** Failure manifests as {@code createAsync()} never completing. */
    @Test
    void createAsyncCompletesWhenTheExecutorHasOneThread() throws Exception {
      try (var fixture = new Fixture(singleThreadExecutor())) {
        var subscription = new OpcUaSubscription(fixture.client);

        assertCompletes(subscription.createAsync(), "createAsync()");

        assertEquals(
            OpcUaSubscription.SyncState.SYNCHRONIZED,
            subscription.getSyncState(),
            "createAsync() completed without creating the Subscription");
        assertTrue(
            subscription.getSubscriptionId().isPresent(),
            "createAsync() completed without installing the Server-assigned SubscriptionId");
      }
    }

    /** Failure manifests as {@code modifyAsync()} never completing. */
    @Test
    void modifyAsyncCompletesWhenTheExecutorHasOneThread() throws Exception {
      try (var fixture = new Fixture(singleThreadExecutor())) {
        OpcUaSubscription subscription = fixture.createdSubscription();

        subscription.setPublishingInterval(MODIFIED_PUBLISHING_INTERVAL);
        assertEquals(
            OpcUaSubscription.SyncState.UNSYNCHRONIZED,
            subscription.getSyncState(),
            "there is nothing pending for modifyAsync() to send, so it would return without"
                + " calling the ModifySubscription service and the scenario under test would not"
                + " happen");

        assertCompletes(subscription.modifyAsync(), "modifyAsync()");

        assertEquals(
            OpcUaSubscription.SyncState.SYNCHRONIZED,
            subscription.getSyncState(),
            "modifyAsync() completed without applying the revised parameters");
        assertEquals(
            Optional.of(MODIFIED_PUBLISHING_INTERVAL),
            subscription.getRevisedPublishingInterval(),
            "modifyAsync() completed without installing the revised PublishingInterval");
      }
    }

    /** Failure manifests as {@code setPublishingModeAsync()} never completing. */
    @Test
    void setPublishingModeAsyncCompletesWhenTheExecutorHasOneThread() throws Exception {
      try (var fixture = new Fixture(singleThreadExecutor())) {
        OpcUaSubscription subscription = fixture.createdSubscription();

        assertCompletes(
            subscription.setPublishingModeAsync(false), "setPublishingModeAsync(false)");

        assertEquals(
            Optional.of(false),
            subscription.isPublishingEnabled(),
            "setPublishingModeAsync(false) completed without recording the new publishing mode");
      }
    }

    /** Failure manifests as {@code deleteAsync()} never completing. */
    @Test
    void deleteAsyncCompletesWhenTheExecutorHasOneThread() throws Exception {
      try (var fixture = new Fixture(singleThreadExecutor())) {
        OpcUaSubscription subscription = fixture.createdSubscription();

        assertCompletes(subscription.deleteAsync(), "deleteAsync()");

        assertEquals(
            OpcUaSubscription.SyncState.INITIAL,
            subscription.getSyncState(),
            "deleteAsync() completed without discarding the Subscription");
        assertEquals(
            0,
            fixture.server.getSubscriptions().size(),
            "deleteAsync() completed without deleting the Subscription from the Server");
      }
    }

    /**
     * The control that isolates the wrapper, rather than the executor, as the cause: the client's
     * own asynchronous CreateSubscription service completes on a single-threaded transport
     * executor, because nothing occupies that thread while the response is in flight.
     *
     * <p>One thread is therefore enough for the round trip {@code createAsync()} makes. What is not
     * enough is spending that thread on a blocking wait for it.
     */
    @Test
    void createSubscriptionAsyncCompletesWhenTheExecutorHasOneThread() throws Exception {
      try (var fixture = new Fixture(singleThreadExecutor())) {
        CreateSubscriptionResponse response =
            fixture
                .client
                .createSubscriptionAsync(1_000.0, uint(30), uint(10), uint(0), true, ubyte(0))
                .get(ASYNC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        assertTrue(
            response.getResponseHeader().getServiceResult().isGood(),
            "control: the client's asynchronous CreateSubscription service must complete on a"
                + " single-threaded transport executor");
      }
    }
  }

  /**
   * The same arithmetic without the degenerate single-threaded case: a bounded executor loses one
   * thread per outstanding asynchronous lifecycle call, and once they are all outstanding at once
   * none of them can be answered.
   */
  @Nested
  class PoolExhaustionOnABoundedExecutor {

    /**
     * Two Subscriptions, two threads. Nothing serializes {@code createAsync()} on one Subscription
     * against {@code createAsync()} on another — they are separate objects with separate lifecycle
     * locks — so both are legitimately outstanding at once, and between them they hold every thread
     * that could complete either response.
     *
     * <p>Failure manifests as the first of the two never completing.
     */
    @Test
    void concurrentCreateAsyncCallsCompleteWhenTheExecutorHasTwoThreads() throws Exception {
      try (var fixture = new Fixture(fixedThreadPool(2))) {
        var first = new OpcUaSubscription(fixture.client);
        var second = new OpcUaSubscription(fixture.client);

        // Both submitted before either is awaited: the point is that they are outstanding together.
        CompletionStage<Unit> firstCreate = first.createAsync();
        CompletionStage<Unit> secondCreate = second.createAsync();

        assertCompletes(firstCreate, "the first of two concurrent createAsync() calls");
        assertCompletes(secondCreate, "the second of two concurrent createAsync() calls");

        assertEquals(
            2,
            fixture.server.getSubscriptions().size(),
            "two concurrent createAsync() calls must create two Subscriptions on the Server");
      }
    }
  }

  /**
   * The controls that fix every assertion above in place: the identical calls against an unbounded
   * cached pool, which is the shape of the executor the client uses by default ({@code
   * Stack.sharedExecutor()}). Each of them has a thread to spare for the response it is waiting
   * for, so each completes today.
   *
   * <p>Without these, the assertions above could be measuring a broken fixture — a Server that
   * never answers, a Subscription that was never created — rather than executor starvation.
   */
  @Nested
  class ControlsOnAnUnboundedExecutor {

    @Test
    void createAsyncCompletesWhenTheExecutorHasThreadsToSpare() throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        var subscription = new OpcUaSubscription(fixture.client);

        assertCompletes(subscription.createAsync(), "control: createAsync()");

        assertEquals(
            OpcUaSubscription.SyncState.SYNCHRONIZED,
            subscription.getSyncState(),
            "control: createAsync() must create the Subscription");
        assertTrue(
            subscription.getSubscriptionId().isPresent(),
            "control: createAsync() must install the Server-assigned SubscriptionId");
      }
    }

    @Test
    void modifyAsyncCompletesWhenTheExecutorHasThreadsToSpare() throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        OpcUaSubscription subscription = fixture.createdSubscription();

        subscription.setPublishingInterval(MODIFIED_PUBLISHING_INTERVAL);

        assertCompletes(subscription.modifyAsync(), "control: modifyAsync()");

        assertEquals(
            OpcUaSubscription.SyncState.SYNCHRONIZED,
            subscription.getSyncState(),
            "control: modifyAsync() must apply the revised parameters");
        assertEquals(
            Optional.of(MODIFIED_PUBLISHING_INTERVAL),
            subscription.getRevisedPublishingInterval(),
            "control: modifyAsync() must install the revised PublishingInterval");
      }
    }

    @Test
    void setPublishingModeAsyncCompletesWhenTheExecutorHasThreadsToSpare() throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        OpcUaSubscription subscription = fixture.createdSubscription();

        assertCompletes(
            subscription.setPublishingModeAsync(false), "control: setPublishingModeAsync(false)");

        assertEquals(
            Optional.of(false),
            subscription.isPublishingEnabled(),
            "control: setPublishingModeAsync(false) must record the new publishing mode");
      }
    }

    @Test
    void deleteAsyncCompletesWhenTheExecutorHasThreadsToSpare() throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        OpcUaSubscription subscription = fixture.createdSubscription();

        assertCompletes(subscription.deleteAsync(), "control: deleteAsync()");

        assertEquals(
            OpcUaSubscription.SyncState.INITIAL,
            subscription.getSyncState(),
            "control: deleteAsync() must discard the Subscription");
        assertEquals(
            0,
            fixture.server.getSubscriptions().size(),
            "control: deleteAsync() must delete the Subscription from the Server");
      }
    }

    /**
     * The control for the contract each of these stages has to keep on the way to completing: an
     * asynchronous lifecycle call must complete exceptionally with exactly the {@link UaException}
     * its blocking counterpart throws. Here that is {@code Bad_InvalidState} for a {@code
     * createAsync()} on a Subscription that already exists (Part 4 §5.13.2 has no notion of
     * creating one twice).
     *
     * <p>This passes today. It is here because composing these calls from the client's asynchronous
     * services rewrites the path an error takes out of them, and an existing caller must not start
     * seeing a different failure — or a success.
     */
    @Test
    void createAsyncFailsWithBadInvalidStateWhenTheSubscriptionAlreadyExists() throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        OpcUaSubscription subscription = fixture.createdSubscription();

        ExecutionException failure =
            assertThrows(
                ExecutionException.class,
                () ->
                    subscription
                        .createAsync()
                        .toCompletableFuture()
                        .get(ASYNC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
                "control: createAsync() on a Subscription that already exists must complete"
                    + " exceptionally");

        UaException cause =
            assertInstanceOf(
                UaException.class,
                failure.getCause(),
                "control: createAsync() must report the same UaException create() throws");
        assertEquals(
            StatusCodes.Bad_InvalidState,
            cause.getStatusCode().value(),
            "control: createAsync() on a Subscription that already exists must fail with"
                + " Bad_InvalidState");
      }
    }

    @Test
    void concurrentCreateAsyncCallsCompleteWhenTheExecutorHasThreadsToSpare() throws Exception {
      try (var fixture = new Fixture(cachedThreadPool())) {
        var first = new OpcUaSubscription(fixture.client);
        var second = new OpcUaSubscription(fixture.client);

        CompletionStage<Unit> firstCreate = first.createAsync();
        CompletionStage<Unit> secondCreate = second.createAsync();

        assertCompletes(firstCreate, "control: the first of two concurrent createAsync() calls");
        assertCompletes(secondCreate, "control: the second of two concurrent createAsync() calls");

        assertEquals(
            2,
            fixture.server.getSubscriptions().size(),
            "control: two concurrent createAsync() calls must create two Subscriptions on the"
                + " Server");
      }
    }
  }

  // region helpers

  /**
   * Wait for an asynchronous lifecycle call to finish, and fail the test if it does not.
   *
   * <p>A call that has wedged the transport executor neither completes nor completes exceptionally,
   * so {@link #ASYNC_TIMEOUT_MILLIS} elapsing <i>is</i> this assertion failing rather than the test
   * being impatient. The wait is bounded so that a RED run reports a failure instead of hanging.
   *
   * @param stage the {@link CompletionStage} returned by the call under test.
   * @param call how to name that call in the failure message.
   */
  private static void assertCompletes(CompletionStage<Unit> stage, String call) throws Exception {
    try {
      stage.toCompletableFuture().get(ASYNC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    } catch (TimeoutException e) {
      fail(
          call
              + " did not complete within "
              + ASYNC_TIMEOUT_MILLIS
              + " ms against a Server that answers immediately: the blocking call it wraps was"
              + " dispatched onto the transport executor and is waiting there for a response that"
              + " only the transport executor can complete");
    }
  }

  /**
   * A running Server whose Publish responses are parked — so the only requests that matter during a
   * test are the lifecycle ones it makes — and a connected client driven by a caller-supplied
   * {@link ExecutorService}.
   *
   * <p>CreateSubscription, ModifySubscription, DeleteSubscriptions and SetPublishingMode are
   * handled by the real Server implementation, so every assertion about what the client installed
   * is an assertion about a real service round trip.
   */
  private static final class Fixture implements AutoCloseable {

    private final ExecutorService executor;
    private final OpcUaServer server;
    private final OpcUaClient client;
    private final ScriptableSubscriptionServiceSet scriptable;

    Fixture(ExecutorService executor) throws Exception {
      this.executor = executor;

      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable = new ScriptableSubscriptionServiceSet(server);
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
                      // Long request timeout so a parked Publish request does not time out, and so
                      // a stalled lifecycle call is not rescued by one.
                      .setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS))
                      // No Session keep-alive traffic competing for the executor.
                      .setKeepAliveInterval(uint(REQUEST_TIMEOUT_MILLIS)));
      client.connect();
    }

    /**
     * Create a Subscription with the blocking {@code create()}, called from the test thread.
     *
     * @return the created {@link OpcUaSubscription}.
     */
    OpcUaSubscription createdSubscription() throws UaException {
      var subscription = new OpcUaSubscription(client);
      subscription.create();

      return subscription;
    }

    @Override
    public void close() throws Exception {
      scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
      try {
        client.disconnectAsync().get(DISCONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
      } catch (TimeoutException ignored) {
        // A client whose executor is wedged by the call under test cannot run the disconnect it is
        // asked for. Tolerated so teardown does not mask the assertion that detected the wedge;
        // shutting the Server down and then interrupting the executor below releases the blocked
        // thread, and every thread involved is a daemon.
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
        daemonThreadFactory("async-lifecycle-cached"));
  }

  /**
   * A single-threaded transport executor, which {@code
   * OpcTcpClientTransportConfigBuilder.setExecutor} exists to allow.
   */
  private static ExecutorService singleThreadExecutor() {
    return Executors.newSingleThreadExecutor(daemonThreadFactory("async-lifecycle-single"));
  }

  /** A transport executor with a hard upper bound on the number of threads. */
  private static ExecutorService fixedThreadPool(int threads) {
    return Executors.newFixedThreadPool(threads, daemonThreadFactory("async-lifecycle-bounded"));
  }

  private static ThreadFactory daemonThreadFactory(String namePrefix) {
    var counter = new AtomicInteger(0);

    return runnable -> {
      var thread = new Thread(runnable, namePrefix + "-" + counter.incrementAndGet());
      thread.setDaemon(true);

      return thread;
    };
  }

  // endregion
}
