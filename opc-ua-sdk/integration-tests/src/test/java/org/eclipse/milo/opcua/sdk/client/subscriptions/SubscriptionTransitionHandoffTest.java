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
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
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
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifySubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifySubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * How {@code OpcUaSubscription} hands the transition slot from one queued lifecycle transition to
 * the next.
 *
 * <p>{@code runTransition} lets one transition hold the slot at a time and queues the rest in
 * {@code transitionWaiters}; {@code endTransition} hands the slot to the one that has been waiting
 * longest by completing its future. Before 93bedb32c it completed that future <b>inline</b>, on the
 * stack of the transition that was finishing. That is fine for a transition that has to wait for
 * the Server, because the wait unwinds the stack — but not every transition does: {@code
 * modifyAsync()} with nothing pending to send, and every transition that is answered {@code
 * Bad_InvalidState}, complete <i>synchronously</i>, so the hand-off re-entered {@code
 * endTransition} on the same stack. N queued synchronously-completing transitions therefore unwound
 * with recursion depth N, and enough of them is a StackOverflowError thrown while the slot is
 * claimed and never released — a lifecycle that is frozen for good, not merely one failed call. The
 * hand-off now goes through the transport executor.
 *
 * <p>Queueing transitions at all requires one to be in flight, so every test here starts by parking
 * a {@code create()} inside the Server's CreateSubscription handler: that claims the slot, and
 * every {@code modifyAsync()} made while it is parked is queued behind it. Once the gate opens, the
 * create's completion is what has to hand the slot down the whole queue.
 *
 * <p>Failure manifests either as a queued transition completing exceptionally with a
 * StackOverflowError or as the queue never draining at all; both are asserted against, and every
 * wait is bounded.
 */
public class SubscriptionTransitionHandoffTest {

  /**
   * How many synchronously-completing transitions to queue behind the parked create.
   *
   * <p>Chosen an order of magnitude above the depth at which the inline hand-off overflowed the
   * stack of the thread that unwinds it. Measured against the pre-fix code on this JVM: 500 drained
   * cleanly, 1000 did not, and the hand-off stopped after 883 and 916 transitions in two runs — so
   * the threshold is around 900. Frame sizes vary between JVMs, platforms and stack-size settings,
   * which is why the margin here is large rather than snug: a value near the threshold would make
   * the test a stack-size probe rather than a regression test.
   */
  private static final int QUEUED_TRANSITIONS = 10_000;

  /**
   * How long the queued transitions are given to drain. They perform no I/O — each one finds
   * nothing to send and completes immediately — so the only work is {@link #QUEUED_TRANSITIONS}
   * hand-offs through the executor.
   */
  private static final long DRAIN_WINDOW_MILLIS = 30_000;

  /** How long to wait for something that must happen. */
  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /** Upper bound on how long the gated Server handler holds a request, so nothing hangs forever. */
  private static final long GATE_TIMEOUT_MILLIS = 30_000;

  /**
   * Long enough that nothing times out on its own: neither a parked Publish request nor the gated
   * CreateSubscription.
   */
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;

  private TestServer testServer;
  private OpcUaServer server;
  private OpcUaClient client;
  private GatedSubscriptionServiceSet scriptable;
  private OpcUaSubscription subscription;

  @BeforeEach
  void startClientAndServer() throws Exception {
    testServer = TestServer.create();
    server = testServer.getServer();

    scriptable = new GatedSubscriptionServiceSet(server);
    for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
      server.addServiceSet(endpoint.getPath(), scriptable);
    }

    server.startup().get();

    client =
        TestClient.create(
            server,
            cfg ->
                cfg.setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS))
                    // No Session keep-alive traffic: the only requests in flight during a test are
                    // the ones it makes.
                    .setKeepAliveInterval(uint(REQUEST_TIMEOUT_MILLIS)));
    client.connect();

    subscription = new OpcUaSubscription(client);
  }

  @AfterEach
  void stopClientAndServer() throws Exception {
    scriptable.releaseCreateGate();
    scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
    try {
      client.disconnectAsync().get(5, TimeUnit.SECONDS);
    } catch (Exception ignored) {
      // A lifecycle frozen by a StackOverflowError in the hand-off cannot be shut down cleanly.
      // Tolerated so teardown does not mask the assertion that detected it; the Server shutdown
      // below releases what can be released.
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
  }

  /**
   * The defect. Every queued transition must complete, and the transition slot must be free
   * afterwards.
   *
   * <p>The queued transitions are {@code modifyAsync()} calls on a Subscription with no pending
   * modifications, which Part 4 requires nothing of: {@code modifyTransition} returns an
   * already-completed stage without calling ModifySubscription at all. That is the crux — a
   * transition that waits for the Server unwinds the stack before the next hand-off, and only a
   * synchronously-completing one recurses. The "no ModifySubscription reached the Server" assertion
   * below is what keeps that premise honest.
   */
  @Test
  void queuedSynchronousTransitionsAllCompleteAndReleaseTheSlot() throws Exception {
    CompletionStage<Unit> create = startGatedCreate();

    var queued = new ArrayList<CompletableFuture<Unit>>(QUEUED_TRANSITIONS);
    for (int i = 0; i < QUEUED_TRANSITIONS; i++) {
      queued.add(subscription.modifyAsync().toCompletableFuture());
    }

    scriptable.releaseCreateGate();

    // The create is awaited together with the queue it hands the slot to: it is the transition
    // whose
    // completion does the unwinding, so an error thrown there surfaces on its stage.
    var all = new ArrayList<>(queued);
    all.add(create.toCompletableFuture());

    try {
      CompletableFuture.allOf(all.toArray(CompletableFuture[]::new))
          .get(DRAIN_WINDOW_MILLIS, TimeUnit.MILLISECONDS);
    } catch (TimeoutException e) {
      fail(
          completed(queued)
              + " of "
              + QUEUED_TRANSITIONS
              + " queued transitions completed within "
              + DRAIN_WINDOW_MILLIS
              + "ms. The hand-off from one queued transition to the next stopped part way through,"
              + " which is what a StackOverflowError thrown inside endTransition() does: the"
              + " transition slot is left claimed and every transition behind it waits for a slot"
              + " nobody holds");
    } catch (ExecutionException e) {
      fail(
          "a transition completed exceptionally after "
              + completed(queued)
              + " of "
              + QUEUED_TRANSITIONS
              + " queued transitions had completed: "
              + e.getCause()
              + ". Transitions that complete synchronously are handed the slot one after another,"
              + " and an inline hand-off unwinds them with recursion depth "
              + QUEUED_TRANSITIONS,
          e.getCause());
    }

    assertEquals(
        0,
        scriptable.modifySubscriptionArrivals(),
        "premise: none of the queued modify transitions may reach the Server. A Subscription with"
            + " no pending modifications has nothing to send, and it is exactly that synchronous"
            + " completion that recursed — a transition that waits for a response unwinds the stack"
            + " first and would not reproduce the defect");

    // The slot has to be free for something to be able to use it again. A real ModifySubscription,
    // so this asserts against the whole transition and not just the queueing.
    subscription.setPublishingInterval(500.0);

    CompletableFuture<Unit> modify = subscription.modifyAsync().toCompletableFuture();

    try {
      modify.get(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    } catch (TimeoutException e) {
      fail(
          "a modify() made after the queue drained never completed: the transition slot was left"
              + " claimed by a hand-off that did not finish, so no further transition on this"
              + " Subscription can ever run");
    }

    assertEquals(
        OpcUaSubscription.SyncState.SYNCHRONIZED,
        subscription.getSyncState(),
        "the Subscription must be usable after the queue has drained");
    assertEquals(
        1,
        scriptable.modifySubscriptionArrivals(),
        "the modify() made after the queue drained had a pending modification and must have reached"
            + " the Server");
  }

  /**
   * Cancelling the caller's lifecycle future must not cancel the internal completion that owns the
   * transition slot. Releasing the slot on cancellation would let the queued delete run while the
   * CreateSubscription call is still in flight; never releasing it after the Server answers would
   * wedge every later lifecycle operation.
   */
  @Test
  void cancellingCallerFutureDoesNotReleaseOrWedgeTheTransitionSlot() throws Exception {
    CompletionStage<Unit> create = startGatedCreate();
    CompletableFuture<Unit> callerFuture = create.toCompletableFuture();

    assertTrue(callerFuture.cancel(false), "the caller must be able to cancel its future");
    assertTrue(callerFuture.isCancelled(), "cancellation must remain visible to the caller");

    CompletableFuture<Unit> queuedDelete = subscription.deleteAsync().toCompletableFuture();

    assertFalse(
        queuedDelete.isDone(),
        "cancelling the caller's view must not release the slot while CreateSubscription is still"
            + " in flight");

    scriptable.releaseCreateGate();

    try {
      queuedDelete.get(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    } catch (TimeoutException e) {
      fail(
          "the DeleteSubscription queued behind the cancelled caller future never ran after the"
              + " internal CreateSubscription completed; cancellation prevented transition-slot"
              + " cleanup");
    }

    assertTrue(callerFuture.isCancelled(), "internal completion must not undo caller cancellation");
    assertEquals(
        OpcUaSubscription.SyncState.INITIAL,
        subscription.getSyncState(),
        "the queued delete must run after the actual CreateSubscription operation completes");
  }

  // region fixture helpers

  private static int completed(List<CompletableFuture<Unit>> futures) {
    return (int) futures.stream().filter(CompletableFuture::isDone).count();
  }

  /**
   * Start a {@code createAsync()} and return once it is inside the Server's CreateSubscription
   * handler, i.e. once it demonstrably holds the transition slot and is waiting for the response.
   */
  private CompletionStage<Unit> startGatedCreate() throws Exception {
    scriptable.gateCreates();

    CompletionStage<Unit> create = subscription.createAsync();

    assertTrue(
        awaitTrue(() -> scriptable.gatedCreateArrivals() >= 1),
        "the create() never reached the Server, so it is not holding the transition slot and"
            + " nothing could be queued behind it");

    return create;
  }

  /** Polls {@code condition} until it holds or {@link #AWAIT_TIMEOUT_MILLIS} elapses. */
  private static boolean awaitTrue(ThrowingBooleanSupplier condition) throws Exception {
    long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(AWAIT_TIMEOUT_MILLIS);

    while (System.nanoTime() < deadline) {
      if (condition.get()) {
        return true;
      }
      Thread.sleep(25);
    }

    return condition.get();
  }

  @FunctionalInterface
  private interface ThrowingBooleanSupplier {
    boolean get() throws Exception;
  }

  /**
   * A {@link ScriptableSubscriptionServiceSet} that can hold a CreateSubscription inside the Server
   * handler until the test releases it, and that counts the ModifySubscription requests it
   * receives.
   *
   * <p>Holding the request <i>inside</i> the handler is what makes the transition demonstrably in
   * flight rather than merely likely to be: the client has sent it and is waiting for the response,
   * so it holds the transition slot for as long as the test wants.
   */
  private static final class GatedSubscriptionServiceSet extends ScriptableSubscriptionServiceSet {

    private final AtomicInteger gatedCreateArrivals = new AtomicInteger(0);
    private final AtomicInteger modifySubscriptionArrivals = new AtomicInteger(0);

    private final CountDownLatch createGate = new CountDownLatch(1);

    private volatile boolean createsGated = false;

    GatedSubscriptionServiceSet(OpcUaServer server) {
      super(server);
    }

    void gateCreates() {
      createsGated = true;
    }

    void releaseCreateGate() {
      createGate.countDown();
    }

    int gatedCreateArrivals() {
      return gatedCreateArrivals.get();
    }

    int modifySubscriptionArrivals() {
      return modifySubscriptionArrivals.get();
    }

    @Override
    public CreateSubscriptionResponse onCreateSubscription(
        ServiceRequestContext context, CreateSubscriptionRequest request) throws UaException {

      if (createsGated) {
        gatedCreateArrivals.incrementAndGet();

        try {
          if (!createGate.await(GATE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
            throw new UaException(
                StatusCodes.Bad_Timeout, "the CreateSubscription gate was never opened");
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new UaException(StatusCodes.Bad_UnexpectedError, e);
        }
      }

      return super.onCreateSubscription(context, request);
    }

    @Override
    public ModifySubscriptionResponse onModifySubscription(
        ServiceRequestContext context, ModifySubscriptionRequest request) throws UaException {

      modifySubscriptionArrivals.incrementAndGet();

      return super.onModifySubscription(context, request);
    }
  }

  // endregion
}
