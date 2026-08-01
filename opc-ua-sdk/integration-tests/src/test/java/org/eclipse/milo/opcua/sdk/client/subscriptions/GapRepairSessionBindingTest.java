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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.DelegatingSessionServiceSet;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

/**
 * Which Session the reactive gap repair sends its Republish requests on.
 *
 * <p>When a PublishResponse reveals a gap in a Subscription's NotificationMessage sequence, the
 * client recovers the missing messages with Republish (Part 4 §5.14.6) while that Subscription's
 * processing queue is paused, which is what orders the recovered messages ahead of the one that
 * revealed the gap. The repair is a chain of round trips, and a Session can disappear in the middle
 * of it — the fault that took it away may well be what created the gap.
 *
 * <p>Resolving the Session again for each request in the chain is what makes that dangerous. A
 * request issued while the Session is being re-established finds no Session and waits for the next
 * one, and a repair that waits is a processing queue that stays paused. Two things are then stuck
 * behind it. The Subscription's own gap never closes, so the NotificationMessage that revealed it
 * is never delivered — the client is waiting for a Session in order to ask for data the Session it
 * already had could have given it. And the reconnect recovery of Part 4 §6.7 is queued on that same
 * paused queue, so the recovery of <i>every</i> Subscription on the client is incomplete, the
 * Publish suspension gate never opens, and Subscriptions with nothing missing get no Publish
 * traffic either.
 *
 * <p>The Session outage is bounded by the test, not by timing: the Server holds the reconnect's
 * ActivateSession until the test releases it, so "while no Session is available" is a window with
 * ends the test controls.
 */
public class GapRepairSessionBindingTest {

  /** How long to wait for something that must happen. */
  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /**
   * How long to wait for work a parked repair would be holding up. A repair that sends its
   * remaining requests on the Session it already has gets through a loopback Republish round trip
   * in single-digit milliseconds, so this is generous by three orders of magnitude — but a repair
   * waiting for a Session that the test is holding back does not finish at all, so no larger value
   * would change an outcome.
   */
  private static final long STALL_WINDOW_MILLIS = 5_000;

  /**
   * The Session FSM waits one second in {@code ReactivatingWait} before its first re-activation
   * attempt and doubles the wait on every failure; this window allows for several attempts.
   */
  private static final long RECONNECT_TIMEOUT_MILLIS = 30_000;

  /**
   * Long enough that nothing times out on its own: no parked Publish request, no Republish, and no
   * Session keep-alive. Every stall asserted against below is therefore the client's own doing.
   */
  private static final long REQUEST_TIMEOUT_MILLIS = 60_000;

  /** Upper bound on how long a gated request is held, so nothing hangs indefinitely. */
  private static final long GATE_TIMEOUT_MILLIS = 30_000;

  private static final long DISCONNECT_TIMEOUT_MILLIS = 5_000;

  /** The last NotificationMessage each Subscription accounts for before the gap is created. */
  private static final long FIRST_SEQUENCE_NUMBER = 1;

  /** NotificationMessages 2, 3 and 4 are missing when 5 arrives. */
  private static final long GAP_REVEALED_BY = 5;

  private static final long FIRST_MISSING = 2;
  private static final long SECOND_MISSING = 3;
  private static final long LAST_MISSING = 4;

  /**
   * A repair that outlives the Session it began on must finish on the Session it has, not wait for
   * the one being established. Everything it still needs is data the Server was already holding for
   * it, and the Session that can ask for it is the one the gap was found on.
   */
  @Test
  void gapRepairFinishesOnTheSessionInHandWhileTheNextOneIsBeingEstablished() throws Exception {
    try (var fixture = new Fixture()) {
      Sub repairing = fixture.repairing();

      fixture.holdFirstRepublish();
      fixture.holdReconnectActivateSession();

      fixture.revealGap(repairing);
      fixture.awaitFirstRepublishHeld();

      fixture.faultSession();
      fixture.awaitReconnectActivateSessionHeld();

      fixture.releaseFirstRepublish();

      assertTrue(
          fixture.awaitRepublished(repairing, LAST_MISSING, STALL_WINDOW_MILLIS),
          "the repair stopped after its first Republish was answered: the requests still to be made"
              + " went looking for a Session, found the one they began on gone, and waited for the"
              + " one being established. Republish requests received: "
              + fixture.republishRequests());

      assertEquals(
          List.of(1, 2, 3, 4, 5),
          fixture.deliveredValues(repairing),
          "every recovered NotificationMessage, and then the one that revealed the gap, must reach"
              + " the application while the repair's own Session is still the only one there is;"
              + " nothing here needs the Session that is being established");

      assertEquals(
          1,
          fixture.sessionReactivatedCount(),
          "precondition: the Server is still holding the reconnect's ActivateSession, so the"
              + " assertions above must have been met with no new Session available at all");
    }
  }

  /**
   * The repair pauses one Subscription's processing queue, and the reconnect recovery of Part 4
   * §6.7 is queued on that same queue. A repair that never finishes therefore holds a recovery that
   * never finishes, and the Publish suspension gate — which waits for every Subscription's recovery
   * — never opens for any of them. A Subscription with nothing missing stops receiving data because
   * a different one lost a message.
   *
   * <p>The Server stops answering the repair's remaining Republish requests at the moment the
   * reconnect completes, so a repair that has already finished by then is unaffected and a repair
   * that resumes on the new Session is held. That is the difference the assertion measures.
   */
  @Test
  void publishTrafficForOtherSubscriptionsIsNotHeldBehindAParkedGapRepair() throws Exception {
    try (var fixture = new Fixture()) {
      Sub repairing = fixture.repairing();
      Sub healthy = fixture.healthy();

      fixture.holdFirstRepublish();
      fixture.holdReconnectActivateSession();

      fixture.revealGap(repairing);
      fixture.awaitFirstRepublishHeld();

      fixture.faultSession();
      fixture.awaitReconnectActivateSessionHeld();

      fixture.releaseFirstRepublish();

      // Not asserted: a repair bound to the Session it began on is done by now, and a repair
      // waiting
      // for the next Session is not. Either way, what follows only holds the requests a repair has
      // not made yet.
      fixture.awaitRepublished(repairing, LAST_MISSING, STALL_WINDOW_MILLIS);

      fixture.holdRepublishesFor(repairing, SECOND_MISSING, LAST_MISSING);
      fixture.releaseReconnectActivateSession();
      fixture.awaitReactivation();

      fixture.enqueueDataChange(healthy, FIRST_SEQUENCE_NUMBER + 1);

      assertTrue(
          fixture.awaitDeliveredValues(
              healthy,
              List.of((int) FIRST_SEQUENCE_NUMBER, (int) FIRST_SEQUENCE_NUMBER + 1),
              STALL_WINDOW_MILLIS),
          "a Subscription with nothing missing received no NotificationMessage after the reconnect:"
              + " the other Subscription's gap repair resumed on the new Session and is holding its"
              + " processing queue paused, so the reconnect recovery queued there cannot run, the"
              + " Publish suspension gate never opens, and no PublishRequest is sent for any"
              + " Subscription. Delivered: "
              + fixture.deliveredValues(healthy));
    }
  }

  /**
   * The control for the test above: the identical reconnect, with no gap and therefore no repair in
   * flight when the Session went away. It proves the fixture's second Subscription does receive
   * NotificationMessages once a reconnect is over, so a failure above is about the parked repair
   * rather than about a fixture that never delivers anything.
   */
  @Test
  void publishTrafficForOtherSubscriptionsResumesAfterAReconnectWithNoRepairInFlight()
      throws Exception {

    try (var fixture = new Fixture()) {
      Sub healthy = fixture.healthy();

      fixture.holdReconnectActivateSession();

      fixture.faultSession();
      fixture.awaitReconnectActivateSessionHeld();

      fixture.releaseReconnectActivateSession();
      fixture.awaitReactivation();

      fixture.enqueueDataChange(healthy, FIRST_SEQUENCE_NUMBER + 1);

      assertTrue(
          fixture.awaitDeliveredValues(
              healthy,
              List.of((int) FIRST_SEQUENCE_NUMBER, (int) FIRST_SEQUENCE_NUMBER + 1),
              STALL_WINDOW_MILLIS),
          "control: a Subscription with nothing missing must receive NotificationMessages again"
              + " once the reconnect is over. Delivered: "
              + fixture.deliveredValues(healthy));
    }
  }

  // region fixture

  /** One Subscription: its Server-assigned id and what its listener has observed. */
  private record Sub(
      UInteger subscriptionId, UInteger clientHandle, List<Integer> deliveredValues) {}

  /**
   * A {@link DelegatingSessionServiceSet} that can hold one ActivateSession until the test releases
   * it, which is how the tests above bound the window during which the client has no Session.
   */
  private static final class GatingSessionServiceSet extends DelegatingSessionServiceSet {

    private final AtomicBoolean holdNext = new AtomicBoolean(false);

    private final CountDownLatch heldActivateSession = new CountDownLatch(1);
    private final CountDownLatch activateSessionGate = new CountDownLatch(1);

    GatingSessionServiceSet(OpcUaServer server) {
      super(server);
    }

    @Override
    public ActivateSessionResponse onActivateSession(
        ServiceRequestContext context, ActivateSessionRequest request) throws UaException {

      if (holdNext.compareAndSet(true, false)) {
        heldActivateSession.countDown();

        await(activateSessionGate);
      }

      return super.onActivateSession(context, request);
    }
  }

  /**
   * A running Server whose Publish and Republish responses are scripted and whose reconnect
   * ActivateSession can be held, plus a connected client with two Subscriptions: one that will be
   * made to lose NotificationMessages, and one that has nothing missing throughout.
   *
   * <p>The client is configured for two pending PublishRequests, which is what makes the pipeline
   * state unambiguous once a gap has been revealed and the Session faulted: one request carries the
   * PublishResponse that reveals the gap, the other carries the Session fault, and nothing is left
   * parked at the Server that could answer a later scripted response without the client having sent
   * a new request for it.
   */
  private static final class Fixture implements AutoCloseable {

    private static final long MAX_PENDING_PUBLISH_REQUESTS = 2;

    /** Every Republish the Server has received, as {@code subscriptionId:sequenceNumber}. */
    private final List<String> republishRequests = Collections.synchronizedList(new ArrayList<>());

    /** Counted down when a Republish the test asked to have held reaches the Server. */
    private final CountDownLatch firstRepublishHeld = new CountDownLatch(1);

    /** Releases the first held Republish. */
    private final CountDownLatch firstRepublishGate = new CountDownLatch(1);

    /** Releases every Republish held by {@link #holdRepublishesFor}. */
    private final CountDownLatch repairGate = new CountDownLatch(1);

    private final AtomicBoolean holdFirstRepublish = new AtomicBoolean(false);

    /**
     * Republish requests, as {@code subscriptionId:sequenceNumber}, held by {@link #repairGate}.
     */
    private final Set<String> heldRepairRequests = ConcurrentHashMap.newKeySet();

    /**
     * The sequence numbers the Server is holding for retransmission, per Subscription. Anything
     * else is answered Bad_MessageNotAvailable.
     */
    private final Map<UInteger, Set<Long>> retransmissionQueues = new ConcurrentHashMap<>();

    private final CountDownLatch sessionInactive = new CountDownLatch(1);
    private final CountDownLatch sessionReactivated = new CountDownLatch(1);

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final ScriptableSubscriptionServiceSet scriptable;
    private final GatingSessionServiceSet sessionServiceSet;

    private final Sub repairing;
    private final Sub healthy;

    Fixture() throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      scriptable = new ScriptableSubscriptionServiceSet(server);
      sessionServiceSet = new GatingSessionServiceSet(server);

      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), scriptable);
        server.addServiceSet(endpoint.getPath(), sessionServiceSet);
      }

      server.startup().get();

      client =
          TestClient.create(
              server,
              cfg ->
                  cfg.setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS))
                      // No Session keep-alive traffic: the only requests in flight during a test
                      // are
                      // the ones it scripts.
                      .setKeepAliveInterval(uint(REQUEST_TIMEOUT_MILLIS))
                      .setMaxPendingPublishRequests(uint(MAX_PENDING_PUBLISH_REQUESTS)));
      client.connect();

      client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionInactive(UaSession session) {
              sessionInactive.countDown();
            }

            @Override
            public void onSessionActive(UaSession session) {
              if (sessionInactive.getCount() == 0) {
                sessionReactivated.countDown();
              }
            }
          });

      scriptable.setRepublishResponder(this::respondToRepublish);

      repairing = createSubscription();
      healthy = createSubscription();

      // The Server is holding every NotificationMessage the gap will be made of, and nothing for
      // the
      // Subscription that has nothing missing.
      retransmissionQueues.put(
          repairing.subscriptionId(), Set.of(FIRST_MISSING, SECOND_MISSING, LAST_MISSING));
      retransmissionQueues.put(healthy.subscriptionId(), Set.of());

      deliverFirstNotification(repairing);
      deliverFirstNotification(healthy);

      assertTrue(
          awaitTrue(
              () -> scriptable.getParkedRequestCount() == MAX_PENDING_PUBLISH_REQUESTS,
              AWAIT_TIMEOUT_MILLIS),
          "the client did not refill its Publish pipeline");
    }

    Sub repairing() {
      return repairing;
    }

    Sub healthy() {
      return healthy;
    }

    private Sub createSubscription() throws UaException {
      List<Integer> deliveredValues = Collections.synchronizedList(new ArrayList<>());

      var subscription = new OpcUaSubscription(client);
      subscription.setSubscriptionListener(
          new OpcUaSubscription.SubscriptionListener() {
            @Override
            public void onDataReceived(
                OpcUaSubscription s, List<OpcUaMonitoredItem> items, List<DataValue> values) {

              for (DataValue value : values) {
                deliveredValues.add((Integer) value.getValue().getValue());
              }
            }
          });
      subscription.create();

      // The MonitoredItem only has to exist on the client: addMonitoredItem assigns the
      // ClientHandle
      // the notification fan-out looks scripted notifications up by, and no Server-side item
      // participates in delivering one.
      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(item);

      return new Sub(
          subscription.getSubscriptionId().orElseThrow(),
          item.getClientHandle().orElseThrow(),
          deliveredValues);
    }

    /**
     * Deliver NotificationMessage {@value #FIRST_SEQUENCE_NUMBER}, leaving this Subscription's last
     * accounted-for sequence number at it.
     */
    private void deliverFirstNotification(Sub sub) throws Exception {
      enqueueDataChange(sub, FIRST_SEQUENCE_NUMBER);

      assertTrue(
          awaitTrue(() -> !deliveredValues(sub).isEmpty(), AWAIT_TIMEOUT_MILLIS),
          "the first NotificationMessage was never delivered");
    }

    /**
     * Script the next PublishResponse as a data message for {@code sub} carrying {@code
     * sequenceNumber}, whose value identifies the NotificationMessage it came from.
     */
    void enqueueDataChange(Sub sub, long sequenceNumber) {
      scriptable.enqueue(
          request ->
              CompletableFuture.completedFuture(
                  scriptable.buildPublishResponse(
                      request,
                      sub.subscriptionId(),
                      sequenceNumber,
                      notificationData(sub, sequenceNumber),
                      new UInteger[] {uint(sequenceNumber)},
                      false)));
    }

    /**
     * Script a PublishResponse for {@code sub} carrying sequence number {@value #GAP_REVEALED_BY},
     * leaving {@value #FIRST_MISSING} to {@value #LAST_MISSING} missing and advertising every one
     * of them as available for retransmission, so the whole gap is recoverable.
     */
    void revealGap(Sub sub) {
      scriptable.enqueue(
          request ->
              CompletableFuture.completedFuture(
                  scriptable.buildPublishResponse(
                      request,
                      sub.subscriptionId(),
                      GAP_REVEALED_BY,
                      notificationData(sub, GAP_REVEALED_BY),
                      new UInteger[] {
                        uint(FIRST_MISSING),
                        uint(SECOND_MISSING),
                        uint(LAST_MISSING),
                        uint(GAP_REVEALED_BY)
                      },
                      false)));
    }

    /** Hold the next Republish request the Server receives until the test releases it. */
    void holdFirstRepublish() {
      holdFirstRepublish.set(true);
    }

    void awaitFirstRepublishHeld() throws Exception {
      assertTrue(
          firstRepublishHeld.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "precondition: the gap was never detected, so no repair is in flight");
    }

    void releaseFirstRepublish() {
      firstRepublishGate.countDown();
    }

    /**
     * Stop answering these Republish requests, if they have not been made yet, until teardown. Used
     * once the reconnect is about to complete, so that only a repair which resumes on the new
     * Session is affected.
     */
    void holdRepublishesFor(Sub sub, long... sequenceNumbers) {
      for (long sequenceNumber : sequenceNumbers) {
        heldRepairRequests.add(republishKey(sub.subscriptionId(), sequenceNumber));
      }
    }

    /** Hold the ActivateSession of the next reconnect until the test releases it. */
    void holdReconnectActivateSession() {
      sessionServiceSet.holdNext.set(true);
    }

    void awaitReconnectActivateSessionHeld() throws Exception {
      assertTrue(
          sessionInactive.await(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the scripted Bad_SessionIdInvalid Publish fault did not take the Session out of Active");
      assertTrue(
          sessionServiceSet.heldActivateSession.await(
              RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the client never tried to activate a Session again");
    }

    void releaseReconnectActivateSession() {
      sessionServiceSet.activateSessionGate.countDown();
    }

    void awaitReactivation() throws Exception {
      assertTrue(
          sessionReactivated.await(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "the Session never became Active again");
    }

    /**
     * Answer one parked PublishRequest with a Bad_SessionIdInvalid ServiceFault, which {@code
     * SessionFsmFactory}'s SessionFaultListener classifies as a Session error and turns into a
     * reconnect. The Server-side Session is untouched, so re-activation succeeds and both
     * Subscriptions survive it.
     */
    void faultSession() {
      scriptable.enqueueServiceFault(StatusCodes.Bad_SessionIdInvalid);
    }

    /**
     * Answer a Republish the way a Server holding a bounded retransmission queue would: with the
     * NotificationMessage if it is holding one with that sequence number for that Subscription, and
     * Bad_MessageNotAvailable if it is not — which is also what terminates the Republish loop Part
     * 4 §6.7 describes.
     */
    private RepublishResponse respondToRepublish(RepublishRequest request) throws UaException {
      UInteger subscriptionId = request.getSubscriptionId();
      long sequenceNumber = request.getRetransmitSequenceNumber().longValue();
      String key = republishKey(subscriptionId, sequenceNumber);

      republishRequests.add(key);

      if (holdFirstRepublish.compareAndSet(true, false)) {
        firstRepublishHeld.countDown();

        await(firstRepublishGate);
      } else if (heldRepairRequests.contains(key)) {
        await(repairGate);
      }

      if (!retransmissionQueues.getOrDefault(subscriptionId, Set.of()).contains(sequenceNumber)) {
        throw new UaException(StatusCodes.Bad_MessageNotAvailable);
      }

      return scriptable.buildRepublishResponse(
          request,
          sequenceNumber,
          notificationData(subscriptionOf(subscriptionId), sequenceNumber));
    }

    private Sub subscriptionOf(UInteger subscriptionId) {
      return repairing.subscriptionId().equals(subscriptionId) ? repairing : healthy;
    }

    private ExtensionObject[] notificationData(Sub sub, long sequenceNumber) {
      var notification =
          new MonitoredItemNotification(
              sub.clientHandle(), new DataValue(Variant.ofInt32((int) sequenceNumber)));

      return new ExtensionObject[] {
        scriptable.encode(
            new DataChangeNotification(new MonitoredItemNotification[] {notification}, null))
      };
    }

    List<String> republishRequests() {
      return List.copyOf(republishRequests);
    }

    boolean awaitRepublished(Sub sub, long sequenceNumber, long timeoutMillis) throws Exception {
      String key = republishKey(sub.subscriptionId(), sequenceNumber);

      return awaitTrue(() -> republishRequests.contains(key), timeoutMillis);
    }

    List<Integer> deliveredValues(Sub sub) {
      return List.copyOf(sub.deliveredValues());
    }

    boolean awaitDeliveredValues(Sub sub, List<Integer> expected, long timeoutMillis)
        throws Exception {

      return awaitTrue(() -> deliveredValues(sub).equals(expected), timeoutMillis);
    }

    long sessionReactivatedCount() {
      return sessionReactivated.getCount();
    }

    /** Polls {@code condition} until it holds or the timeout elapses. */
    boolean awaitTrue(ThrowingBooleanSupplier condition, long timeoutMillis) throws Exception {
      long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis);

      while (System.nanoTime() < deadline) {
        if (condition.get()) {
          return true;
        }
        Thread.sleep(10);
      }

      return condition.get();
    }

    @Override
    public void close() throws Exception {
      repairGate.countDown();
      firstRepublishGate.countDown();
      sessionServiceSet.activateSessionGate.countDown();
      scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
      try {
        client.disconnectAsync().get(DISCONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
      } catch (TimeoutException ignored) {
        // A client whose processing queue is held by a repair that cannot finish may not manage the
        // disconnect it is asked for; shutting the Server down below is what releases it. Tolerated
        // here so teardown does not mask the assertion that detected the stall.
      } finally {
        server.shutdown().get(10, TimeUnit.SECONDS);
      }
    }
  }

  private static String republishKey(UInteger subscriptionId, long sequenceNumber) {
    return subscriptionId + ":" + sequenceNumber;
  }

  /**
   * Suspend the calling Server dispatch thread until {@code gate} is released. Bounded, so a test
   * that goes wrong fails rather than hangs; the Server dispatches every service request on its own
   * executor, so only the thread handling this one request waits.
   */
  private static void await(CountDownLatch gate) throws UaException {
    try {
      if (!gate.await(GATE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
        throw new UaException(StatusCodes.Bad_Timeout);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();

      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @FunctionalInterface
  private interface ThrowingBooleanSupplier {
    boolean get() throws Exception;
  }

  // endregion
}
