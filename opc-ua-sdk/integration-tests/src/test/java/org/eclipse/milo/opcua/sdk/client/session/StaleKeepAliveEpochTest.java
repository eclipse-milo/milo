/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.servicesets.AttributeServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultAttributeServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

/**
 * Which Session epoch a keep-alive is allowed to act on.
 *
 * <p>The keep-alive is a Read of Server_ServerStatus_State sent while the Session FSM is in {@code
 * Active}, and its {@code whenComplete} decides — from a machine-wide failure count — whether the
 * Session is still alive. Leaving {@code Active} cancels the timer that schedules new keep-alives,
 * but a Read that has already been sent is not cancelled with it: the transport only fails pending
 * requests when the channel errors or goes inactive, and the {@code Event.ServiceFault} route out
 * of {@code Active} leaves the channel up. That request therefore outlives the epoch it was sent
 * for, and re-activation (Part 4 §5.6.3) puts the FSM back in {@code Active} over the same channel
 * with the request still parked at the Server.
 *
 * <p>When it finally completes it is answering a question about a Session epoch that is over, and
 * the answer has already been superseded: the FSM saw the fault, reconnected, and the Server
 * accepted the ActivateSession, which is proof the Session it now has is healthy. A callback that
 * runs against whatever state the machine is in when it happens to complete, rather than the state
 * it was sent in, spends the new epoch's keep-alive budget on the old epoch's observation and takes
 * a Session down that nothing is wrong with.
 *
 * <p>Nothing here is timing-dependent: the Server holds the keep-alive Read until the test releases
 * it, and the test does not release it until it has watched the Session go inactive and become
 * active again.
 */
class StaleKeepAliveEpochTest {

  /** How long to wait for something that must happen. */
  private static final long AWAIT_TIMEOUT_MILLIS = 10_000;

  /**
   * The Session FSM waits one second in {@code ReactivatingWait} before its first re-activation
   * attempt and doubles the wait on every failure; this window allows for several attempts.
   */
  private static final long RECONNECT_TIMEOUT_MILLIS = 30_000;

  /**
   * How long the re-activated Session is watched after the stale keep-alive is released. The stale
   * callback runs as soon as the parked Read is answered and fires its event synchronously, so a
   * Session that is going to be taken down by it is taken down in milliseconds; a longer window
   * would not change an outcome.
   */
  private static final long STALL_WINDOW_MILLIS = 3_000;

  /** Upper bound on how long the parked keep-alive is held, so nothing hangs indefinitely. */
  private static final long GATE_TIMEOUT_MILLIS = 30_000;

  /** Short enough that the first keep-alive is sent as soon as the Session is Active. */
  private static final long KEEP_ALIVE_INTERVAL_MILLIS = 250;

  /**
   * Long enough that the parked keep-alive Read never times out on its own: the test decides when
   * it completes, and it completes only once the next epoch is Active.
   */
  private static final long KEEP_ALIVE_TIMEOUT_MILLIS = 60_000;

  private static final long REQUEST_TIMEOUT_MILLIS = 30_000;

  /**
   * A Node the Server answers Bad_SessionIdInvalid for, which is how the test provokes the {@code
   * Event.ServiceFault} route out of {@code Active}. It is the only route that ends the epoch
   * without taking the channel down, and therefore the only one that leaves an already-sent
   * keep-alive Read pending.
   */
  private static final NodeId FAULT_TRIGGER = new NodeId(0, "SessionFaultTrigger");

  /**
   * A keep-alive that was sent on an epoch which has since ended must not be counted against the
   * epoch that replaced it. The Server accepted the re-activation, so the Session the failure is
   * charged to is one that has just been shown to work.
   */
  @Test
  void keepAliveSentOnAPreviousEpochDoesNotFaultTheCurrentOne() throws Exception {
    TestServer testServer = TestServer.create();
    OpcUaServer server = testServer.getServer();

    var attributeServiceSet = new GatingAttributeServiceSet(server);

    for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
      server.addServiceSet(endpoint.getPath(), attributeServiceSet);
    }

    server.startup().get();

    OpcUaClient client =
        TestClient.create(
            server,
            cfg ->
                cfg.setRequestTimeout(uint(REQUEST_TIMEOUT_MILLIS))
                    .setKeepAliveInterval(uint(KEEP_ALIVE_INTERVAL_MILLIS))
                    .setKeepAliveTimeout(uint(KEEP_ALIVE_TIMEOUT_MILLIS))
                    // One failed keep-alive is enough to end an epoch, so exactly one parked Read
                    // decides the outcome. With the default of one the same defect needs two of
                    // them and pins nothing further.
                    .setKeepAliveFailuresAllowed(uint(0)));

    var inactiveCount = new AtomicInteger(0);
    var firstInactive = new CountDownLatch(1);
    var secondInactive = new CountDownLatch(1);
    var reactivated = new CountDownLatch(1);

    try {
      client.connect();

      client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionInactive(UaSession session) {
              if (inactiveCount.incrementAndGet() == 1) {
                firstInactive.countDown();
              } else {
                secondInactive.countDown();
              }
            }

            @Override
            public void onSessionActive(UaSession session) {
              if (firstInactive.getCount() == 0) {
                reactivated.countDown();
              }
            }
          });

      attributeServiceSet.holdNextKeepAlive();

      assertTrue(
          attributeServiceSet.keepAliveHeld.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "precondition: no keep-alive Read reached the Server, so there is none in flight to"
              + " outlive the epoch it was sent on");

      // Ends the epoch without touching the channel, leaving the parked keep-alive Read pending.
      client.readValuesAsync(0.0, TimestampsToReturn.Neither, List.of(FAULT_TRIGGER));

      assertTrue(
          firstInactive.await(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "precondition: the Bad_SessionIdInvalid fault did not take the Session out of Active");
      assertTrue(
          reactivated.await(RECONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
          "precondition: the Session never became Active again, so there is no new epoch for the"
              + " parked keep-alive to land in");

      // Answer the keep-alive the way the Server would have if it had never come back: a failure,
      // but a failure about the epoch that is already over and already recovered from.
      attributeServiceSet.releaseKeepAlive();

      assertFalse(
          secondInactive.await(STALL_WINDOW_MILLIS, TimeUnit.MILLISECONDS),
          "the re-activated Session was taken out of Active with nothing wrong with it: the"
              + " keep-alive sent on the previous epoch completed against the machine-wide failure"
              + " count of the current one and spent its whole keep-alive budget, even though the"
              + " Server had just accepted the ActivateSession that established it");
    } finally {
      attributeServiceSet.releaseKeepAlive();
      try {
        client.disconnectAsync().get(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
      } catch (TimeoutException ignored) {
        // Shutting the Server down below is what releases a client that cannot complete its
        // disconnect. Tolerated here so teardown does not mask the assertion above.
      } finally {
        server.shutdown().get(10, TimeUnit.SECONDS);
      }
    }
  }

  /**
   * An {@link AttributeServiceSet} that can hold one keep-alive Read until the test releases it,
   * and that answers {@link #FAULT_TRIGGER} with the Session fault the test uses to end an epoch.
   * Everything else is answered normally, so the keep-alives of the next epoch succeed.
   */
  private static final class GatingAttributeServiceSet implements AttributeServiceSet {

    private final AttributeServiceSet delegate;

    private final AtomicBoolean holdNextKeepAlive = new AtomicBoolean(false);

    /** Counted down when the keep-alive Read the test asked to have held reaches the Server. */
    final CountDownLatch keepAliveHeld = new CountDownLatch(1);

    /** Releases the held keep-alive Read. */
    private final CountDownLatch keepAliveGate = new CountDownLatch(1);

    GatingAttributeServiceSet(OpcUaServer server) {
      this.delegate = new DefaultAttributeServiceSet(Objects.requireNonNull(server, "server"));
    }

    void holdNextKeepAlive() {
      holdNextKeepAlive.set(true);
    }

    void releaseKeepAlive() {
      keepAliveGate.countDown();
    }

    @Override
    public ReadResponse onRead(ServiceRequestContext context, ReadRequest request)
        throws UaException {

      if (isFaultTrigger(request)) {
        throw new UaException(StatusCodes.Bad_SessionIdInvalid);
      }

      if (isKeepAlive(request) && holdNextKeepAlive.compareAndSet(true, false)) {
        keepAliveHeld.countDown();

        await(keepAliveGate);

        // The failure a keep-alive sees when the Server it was sent to has stopped answering.
        throw new UaException(StatusCodes.Bad_Timeout);
      }

      return delegate.onRead(context, request);
    }

    @Override
    public HistoryReadResponse onHistoryRead(
        ServiceRequestContext context, HistoryReadRequest request) throws UaException {
      return delegate.onHistoryRead(context, request);
    }

    @Override
    public WriteResponse onWrite(ServiceRequestContext context, WriteRequest request)
        throws UaException {
      return delegate.onWrite(context, request);
    }

    @Override
    public HistoryUpdateResponse onHistoryUpdate(
        ServiceRequestContext context, HistoryUpdateRequest request) throws UaException {
      return delegate.onHistoryUpdate(context, request);
    }

    private static boolean isKeepAlive(ReadRequest request) {
      return isSingleValueReadOf(request, NodeIds.Server_ServerStatus_State);
    }

    private static boolean isFaultTrigger(ReadRequest request) {
      return isSingleValueReadOf(request, FAULT_TRIGGER);
    }

    private static boolean isSingleValueReadOf(ReadRequest request, NodeId nodeId) {
      ReadValueId[] nodesToRead = request.getNodesToRead();

      return nodesToRead != null
          && nodesToRead.length == 1
          && nodeId.equals(nodesToRead[0].getNodeId())
          && AttributeId.Value.uid().equals(nodesToRead[0].getAttributeId());
    }
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
}
