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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Ordering and idempotence of NotificationMessage accounting in {@code
 * PublishingManager.processPublishResponse}.
 *
 * <p>Part 4 §5.14.1.1 numbers NotificationMessages so a Client can tell a gap from an ordinary
 * delivery; {@code lastSequenceNumber} is the client's record of the last one accounted for and is
 * the sole input to gap detection and therefore to Republish recovery. Two independent things have
 * to hold for that record to stay meaningful:
 *
 * <ol>
 *   <li>PublishResponses must be <i>processed</i> in the order the Server sent them. The transport
 *       guarantees ordered <i>delivery</i>: {@code AbstractUascClientTransport.handleResponse}
 *       completes PublishResponse futures on a serial {@code ExecutionQueue} for exactly this
 *       reason. {@code PublishingManager} then hands its completion handler to {@code
 *       whenCompleteAsync(..., executor)}, and that executor is a genuinely multi-threaded pool, so
 *       two handlers dispatched in order can run in either order.
 *   <li>Processing must be monotonic. A NotificationMessage that has already been accounted for
 *       must not move {@code lastSequenceNumber} backwards or be delivered a second time, whatever
 *       the reason it arrived late.
 * </ol>
 *
 * <p>Either failure produces the same corruption: {@code lastSequenceNumber} regresses, the next
 * NotificationMessage looks like the far end of a gap, and the client issues a blocking Republish
 * for a message it already has — repeatedly, once per message, for the rest of the Subscription's
 * life. The nested classes below drive each cause and assert on the same two observable
 * consequences: which Republish requests reach the Server, and how many times each
 * NotificationMessage is delivered to the application.
 */
public class PublishResponseOrderingTest {

  /** How long to wait for a condition that must become true. */
  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /** How long to wait for the two Publish completions to be inverted before giving up on it. */
  private static final long REORDER_TIMEOUT_MILLIS = 3_000;

  /**
   * The Server sent 2 and then 3, and both were delivered to the client in that order. Whichever
   * order the client's own executor happens to run the two completion handlers in, nothing was
   * lost, so nothing may be requested via Republish.
   */
  @Nested
  class ReorderedInsideTheClient {

    @Test
    void reorderedPublishResponsesDoNotTriggerRepublish() throws Exception {
      try (var fixture = new Fixture()) {
        fixture.driveReorderedResponses();

        assertEquals(
            List.of(),
            List.copyOf(fixture.republishRequests),
            "no NotificationMessage was lost — the Server sent 2, 3, 4 and the transport delivered"
                + " them in that order — so the client must not ask the Server to retransmit"
                + " anything");
      }
    }

    /**
     * The other half of the same corruption. Processing the later response first republishes the
     * earlier one and delivers it, and processing the earlier one afterwards delivers it again: the
     * application sees the same NotificationMessage twice.
     */
    @Test
    void reorderedPublishResponsesAreEachDeliveredExactlyOnce() throws Exception {
      try (var fixture = new Fixture()) {
        fixture.driveReorderedResponses();

        assertEquals(
            List.of(1, 2, 3, 4),
            List.copyOf(fixture.deliveredValues),
            "each NotificationMessage must be delivered to the application exactly once, in the"
                + " order the Server sent it");
      }
    }
  }

  /**
   * The same stale input, produced without touching the client's threading: the Server is scripted
   * to send NotificationMessage 3 before 2. Sequence 3 legitimately looks like a gap, so Republish(
   * 2) is correct and expected; what must not happen is that the subsequent, already-accounted-for
   * sequence 2 rolls {@code lastSequenceNumber} back to 2 and turns the perfectly ordinary sequence
   * 4 into another gap.
   *
   * <p>This pins the monotonicity requirement independently of the reordering above: preserving the
   * order of the completion handlers fixes the cause, but does not by itself make processing safe
   * against a message that has already been accounted for.
   */
  @Nested
  class StaleNotificationMessage {

    @Test
    void alreadyAccountedNotificationMessageDoesNotCauseAFurtherRepublish() throws Exception {
      try (var fixture = new Fixture()) {
        fixture.driveStaleResponse();

        assertEquals(
            List.of(2L),
            List.copyOf(fixture.republishRequests),
            "sequence 2 was missing when 3 arrived and is correctly recovered; sequence 3 was"
                + " received and accounted for, so the arrival of the stale sequence 2 must not"
                + " make 3 look missing again");
      }
    }

    @Test
    void alreadyAccountedNotificationMessageIsNotDeliveredAgain() throws Exception {
      try (var fixture = new Fixture()) {
        fixture.driveStaleResponse();

        assertEquals(
            List.of(1, 2, 3, 4),
            List.copyOf(fixture.deliveredValues),
            "sequence 2 was already delivered when it was recovered via Republish; delivering the"
                + " stale copy as well hands the application a duplicate");
      }
    }
  }

  /**
   * Positive control for every "was Republish called for X?" assertion above: a genuine gap is
   * recorded by the fixture's Republish responder. If this fails, the fixture is broken rather than
   * the sequence accounting.
   */
  @Test
  void genuineGapIsRecordedByTheRepublishResponder() throws Exception {
    try (var fixture = new Fixture()) {
      // Sequence 2 is lost; sequence 3 reveals the gap.
      fixture.scriptable.enqueueDataChange(
          fixture.subscriptionId, 3, fixture.notifications(3), uint(2), uint(3));

      assertTrue(
          fixture.awaitTrue(() -> fixture.deliveredValues.contains(3)),
          "sequence 3 was never delivered");

      assertEquals(
          List.of(2L),
          List.copyOf(fixture.republishRequests),
          "the missing NotificationMessage 2 must be requested via Republish");
    }
  }

  /**
   * Control for the fixture's {@link ReorderingExecutor}: it must actually invert two tasks that a
   * single ordered source dispatched in order. Without this, {@link ReorderedInsideTheClient} could
   * pass simply because no reordering ever took place.
   */
  @Test
  void reorderingExecutorInvertsTwoDispatchesFromASingleOrderedSource() throws Exception {
    var executor = new ReorderingExecutor();

    try {
      List<String> order = Collections.synchronizedList(new ArrayList<>());
      var bothRan = new CountDownLatch(2);

      executor.armReorder();

      // The shape of the transport's serial publish-response queue: one task dispatching, in order,
      // the dependent stages of two futures it completes.
      executor.execute(
          () -> {
            executor.execute(
                () -> {
                  order.add("first");
                  bothRan.countDown();
                });
            executor.execute(
                () -> {
                  order.add("second");
                  bothRan.countDown();
                });
          });

      assertTrue(
          executor.awaitReorder(REORDER_TIMEOUT_MILLIS),
          "the executor did not invert the two dispatches");
      assertTrue(bothRan.await(5, TimeUnit.SECONDS), "not every dispatched task ran");

      assertEquals(
          List.of("second", "first"),
          List.copyOf(order),
          "the task dispatched first must run after the task dispatched second");
    } finally {
      executor.shutdownNow();
    }
  }

  /**
   * A running Server whose Publish and Republish responses are scripted, plus a client driven by a
   * {@link ReorderingExecutor}, a Subscription with one client-side MonitoredItem, and a listener
   * recording every value delivered.
   *
   * <p>Construction leaves the client in a known state: NotificationMessage 1 has been received and
   * delivered, so {@code lastSequenceNumber} is 1, and the Publish pipeline is full again.
   */
  private static final class Fixture implements AutoCloseable {

    /** Every {@code retransmitSequenceNumber} the client has asked the Server to Republish. */
    private final List<Long> republishRequests = Collections.synchronizedList(new ArrayList<>());

    /** Every value handed to {@code onDataReceived}, in delivery order. */
    private final List<Integer> deliveredValues = Collections.synchronizedList(new ArrayList<>());

    private final ReorderingExecutor executor = new ReorderingExecutor();

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final ScriptableSubscriptionServiceSet scriptable;
    private final OpcUaSubscription subscription;
    private final UInteger subscriptionId;
    private final UInteger clientHandle;

    Fixture() throws Exception {
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
                      // Long request timeout so parked Publish requests do not time out.
                      .setRequestTimeout(uint(60_000))
                      // No Session keep-alive traffic: the only responses in flight during the test
                      // are the scripted ones.
                      .setKeepAliveInterval(uint(60_000)));
      client.connect();

      subscription = new OpcUaSubscription(client);
      subscription.setSubscriptionListener(
          new OpcUaSubscription.SubscriptionListener() {
            @Override
            public void onDataReceived(
                OpcUaSubscription subscription,
                List<OpcUaMonitoredItem> items,
                List<DataValue> values) {

              values.forEach(value -> deliveredValues.add((Integer) value.getValue().getValue()));
            }
          });
      subscription.create();

      subscriptionId = subscription.getSubscriptionId().orElseThrow();

      // Client-side only: addMonitoredItem() assigns the ClientHandle the notification fan-out
      // looks values up by, which is all a scripted notification needs.
      var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(item);
      clientHandle = item.getClientHandle().orElseThrow();

      scriptable.setRepublishResponder(
          request -> {
            long sequenceNumber = request.getRetransmitSequenceNumber().longValue();
            republishRequests.add(sequenceNumber);

            return scriptable.buildRepublishResponse(
                request, sequenceNumber, encodedNotifications((int) sequenceNumber));
          });

      establishFirstNotificationMessage();
    }

    /**
     * Drive {@code lastSequenceNumber} to 1 and wait until the Publish pipeline has refilled, so
     * that both of the responses the scenarios script below land on requests that are already
     * parked at the Server.
     */
    private void establishFirstNotificationMessage() throws Exception {
      scriptable.enqueueDataChange(subscriptionId, 1, notifications(1), uint(1));

      assertTrue(
          awaitTrue(() -> deliveredValues.contains(1)), "the first NotificationMessage was lost");
      assertTrue(
          awaitTrue(() -> scriptable.getParkedRequestCount() >= 2),
          "the client did not refill its Publish pipeline");
    }

    /**
     * NotificationMessages 2 and 3 are sent, in order, onto the two parked Publish requests; the
     * client's executor is armed to run their completion handlers in the opposite order. Sequence 4
     * follows once both have been processed, and its delivery is the barrier that proves everything
     * before it has been delivered too: the processing queue and the delivery queue are both FIFO.
     */
    void driveReorderedResponses() throws Exception {
      int publishRequestsBefore = scriptable.getPublishRequestCount();

      executor.armReorder();

      scriptable.enqueueDataChange(subscriptionId, 2, notifications(2), uint(2));
      scriptable.enqueueDataChange(subscriptionId, 3, notifications(3), uint(3));

      // Bounded: an implementation that does not dispatch the two completions through this executor
      // simply proceeds in order, which is the behavior being asserted anyway.
      executor.awaitReorder(REORDER_TIMEOUT_MILLIS);

      awaitResponsesProcessed(publishRequestsBefore, 2);

      scriptable.enqueueDataChange(subscriptionId, 4, notifications(4), uint(4));

      assertTrue(awaitTrue(() -> deliveredValues.contains(4)), "sequence 4 was never delivered");
    }

    /**
     * The Server sends NotificationMessage 3 before 2, then 4. Sequence 3 is a genuine gap and 2 is
     * recovered from it; the copy of 2 that arrives afterwards has already been accounted for.
     */
    void driveStaleResponse() throws Exception {
      int publishRequestsBefore = scriptable.getPublishRequestCount();

      scriptable.enqueueDataChange(subscriptionId, 3, notifications(3), uint(2), uint(3));

      assertTrue(awaitTrue(() -> deliveredValues.contains(3)), "sequence 3 was never delivered");

      scriptable.enqueueDataChange(subscriptionId, 2, notifications(2), uint(2));

      awaitResponsesProcessed(publishRequestsBefore, 2);

      scriptable.enqueueDataChange(subscriptionId, 4, notifications(4), uint(4));

      assertTrue(awaitTrue(() -> deliveredValues.contains(4)), "sequence 4 was never delivered");
    }

    /**
     * Wait until {@code count} PublishResponses have been fully processed and delivered.
     *
     * <p>The client holds a fixed number of Publish requests in flight and only replaces one once
     * the notifications it carried have been delivered — that is the SDK's backpressure mechanism —
     * so {@code count} further Publish requests arriving at the Server is exactly that signal.
     *
     * @param publishRequestsBefore the Publish request count before the responses were scripted.
     * @param count the number of PublishResponses to wait for.
     */
    private void awaitResponsesProcessed(int publishRequestsBefore, int count) throws Exception {
      assertTrue(
          awaitTrue(() -> scriptable.getPublishRequestCount() >= publishRequestsBefore + count),
          "the client did not finish processing " + count + " PublishResponses");
    }

    /** A DataChangeNotification carrying {@code value} for this Fixture's MonitoredItem. */
    List<MonitoredItemNotification> notifications(int value) {
      return List.of(
          new MonitoredItemNotification(clientHandle, new DataValue(Variant.ofInt32(value))));
    }

    private ExtensionObject[] encodedNotifications(int value) {
      return new ExtensionObject[] {
        scriptable.encode(
            new DataChangeNotification(
                notifications(value).toArray(MonitoredItemNotification[]::new), null))
      };
    }

    /** Polls {@code condition} until it holds or the (generous) timeout elapses. */
    boolean awaitTrue(ThrowingBooleanSupplier condition) throws Exception {
      long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(AWAIT_TIMEOUT_MILLIS);

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
      executor.releaseHeld();
      scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } finally {
        try {
          server.shutdown().get(5, TimeUnit.SECONDS);
        } finally {
          executor.shutdownNow();
        }
      }
    }
  }

  @FunctionalInterface
  private interface ThrowingBooleanSupplier {
    boolean get() throws Exception;
  }

  /**
   * The client's {@link ExecutorService}, with one test affordance: once {@link #armReorder()} has
   * been called, the next task submitted from <i>inside</i> a task that was itself submitted from
   * outside this executor is held; the following such task is run to completion; only then is the
   * held task released.
   *
   * <p>That "one level in" position is exactly where the PublishResponse completion handlers land.
   * {@code AbstractUascClientTransport} completes PublishResponse futures from a serial {@code
   * ExecutionQueue} whose drain task runs on this executor (submitted from a Netty I/O thread, so
   * it is at the outer level), and {@code PublishingManager}'s {@code whenCompleteAsync(...,
   * executor)} dispatches the handler back onto this executor from there. Inverting those two
   * dispatches is a scheduling decision an unordered pool is entitled to make; the pool the client
   * uses by default — {@code Stack.sharedExecutor()}, whose shape this class copies — makes it by
   * luck rather than on request.
   *
   * <p>The hold is bounded by {@link #awaitReorder(long)} and by {@link #releaseHeld()}, so an
   * implementation that never dispatches a second such task is not deadlocked by the fixture.
   */
  private static final class ReorderingExecutor extends ThreadPoolExecutor {

    /** Nesting level of the task the current thread is running; absent when it is not one. */
    private static final ThreadLocal<Integer> DEPTH = new ThreadLocal<>();

    private enum State {
      PASS_THROUGH,
      ARMED,
      HOLDING
    }

    private final Object lock = new Object();
    private final CountDownLatch reordered = new CountDownLatch(1);

    private State state = State.PASS_THROUGH;
    private Runnable held;

    ReorderingExecutor() {
      // The shape of Stack.sharedExecutor(): an unbounded cached pool, so nothing but this class's
      // own bookkeeping orders the tasks that run on it.
      super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());
    }

    void armReorder() {
      synchronized (lock) {
        state = State.ARMED;
      }
    }

    /**
     * Wait for the inversion to complete, then release the held task if a second one never arrived.
     *
     * @param timeoutMillis how long to wait for the inversion.
     * @return {@code true} if two tasks were actually inverted.
     * @throws InterruptedException if interrupted while waiting.
     */
    boolean awaitReorder(long timeoutMillis) throws InterruptedException {
      boolean applied = reordered.await(timeoutMillis, TimeUnit.MILLISECONDS);

      releaseHeld();

      return applied;
    }

    /** Release the held task, if any, without waiting for a second one. */
    void releaseHeld() {
      Runnable toRelease;

      synchronized (lock) {
        toRelease = held;
        held = null;
        state = State.PASS_THROUGH;
      }

      if (toRelease != null) {
        super.execute(toRelease);
      }
    }

    @Override
    public void execute(Runnable command) {
      Integer parentDepth = DEPTH.get();
      int depth = parentDepth == null ? 0 : parentDepth + 1;

      Runnable task =
          () -> {
            DEPTH.set(depth);
            try {
              command.run();
            } finally {
              DEPTH.remove();
            }
          };

      if (depth == 1) {
        synchronized (lock) {
          if (state == State.ARMED) {
            held = task;
            state = State.HOLDING;
            return;
          } else if (state == State.HOLDING) {
            Runnable first = held;
            held = null;
            state = State.PASS_THROUGH;

            super.execute(
                () -> {
                  try {
                    task.run();
                  } finally {
                    super.execute(first);
                    reordered.countDown();
                  }
                });
            return;
          }
        }
      }

      super.execute(task);
    }
  }
}
